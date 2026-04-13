package com.norbjdk.picjeditor.core.picture;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.List;

public class PictureService {

    private static final PictureService instance = new PictureService();

    public static PictureService getInstance() { return instance; }

    private static final int STD_SIZE = 512;

    private PictureService() {}

    public ImageData loadAndStandardize(Path path) {
        File file = path.toFile();
        BufferedImage original;
        try {
            original = ImageIO.read(file);
        } catch (IOException e) {
            System.err.println("Błąd wczytywania: " + path);
            return null;
        }
        if (original == null) return null;

        ImageData data = new ImageData();
        data.setOriginalName(file.getName());
        data.setOriginalPath(path);
        data.setOriginalWidth(original.getWidth());
        data.setOriginalHeight(original.getHeight());
        data.setOriginalSizeBytes(file.length());
        data.setOriginalImage(SwingFXUtils.toFXImage(toRGB(original), null));

        BufferedImage standardized = standardize(original);
        data.setStdWidth(standardized.getWidth());
        data.setStdHeight(standardized.getHeight());
        data.setStandardizedImage(SwingFXUtils.toFXImage(standardized, null));

        File outFile = new File(file.getParent(), "std_" + stripExtension(file.getName()) + ".png");
        try {
            ImageIO.write(standardized, "PNG", outFile);
            data.setStandardizedSizeBytes(outFile.length());
        } catch (IOException e) {
            System.err.println("Błąd zapisu standaryzacji: " + outFile);
        }

        data.setColorStats(computeColorStats(standardized));

        return data;
    }

    private BufferedImage standardize(BufferedImage src) {
        int srcW = src.getWidth();
        int srcH = src.getHeight();

        double scale = (double) STD_SIZE / Math.min(srcW, srcH);
        int scaledW = (int) Math.round(srcW * scale);
        int scaledH = (int) Math.round(srcH * scale);

        BufferedImage scaled = new BufferedImage(scaledW, scaledH, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = scaled.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(src, 0, 0, scaledW, scaledH, null);
        g.dispose();

        int cropX = Math.max(0, (scaledW - STD_SIZE) / 2);
        int cropY = Math.max(0, (scaledH - STD_SIZE) / 2);
        int cropW = Math.min(STD_SIZE, scaledW - cropX);
        int cropH = Math.min(STD_SIZE, scaledH - cropY);

        BufferedImage result = new BufferedImage(STD_SIZE, STD_SIZE, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = result.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(scaled.getSubimage(cropX, cropY, cropW, cropH), 0, 0, STD_SIZE, STD_SIZE, null);
        g2.dispose();

        return result;
    }

    public ColorStats computeColorStats(BufferedImage img) {
        ColorStats stats = new ColorStats();
        int w = img.getWidth();
        int h = img.getHeight();
        int totalPixels = w * h;

        double sumR = 0, sumG = 0, sumB = 0;
        double sumH = 0, sumS = 0, sumV = 0;
        double sumBrightnessSq = 0;
        int dark = 0, bright = 0;

        Set<Integer> colorSet = new HashSet<>();
        int[] rCount = new int[256];
        int[] gCount = new int[256];
        int[] bCount = new int[256];
        int[] grayCount = new int[256];

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int rgb = img.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;

                sumR += r;
                sumG += g;
                sumB += b;
                rCount[r]++;
                gCount[g]++;
                bCount[b]++;

                float[] hsv = Color.RGBtoHSB(r, g, b, null);
                sumH += hsv[0];
                sumS += hsv[1];
                sumV += hsv[2];

                double v = hsv[2];
                sumBrightnessSq += v * v;
                if (v < 0.3) dark++;
                if (v > 0.7) bright++;

                int gray = (r + g + b) / 3;
                grayCount[gray]++;

                colorSet.add(rgb & 0x00FFFFFF);
            }
        }

        stats.setMeanR(sumR / totalPixels);
        stats.setMeanG(sumG / totalPixels);
        stats.setMeanB(sumB / totalPixels);
        stats.setMeanH(sumH / totalPixels);
        stats.setMeanS(sumS / totalPixels);
        stats.setMeanV(sumV / totalPixels);

        double[] histR = new double[256];
        double[] histG = new double[256];
        double[] histB = new double[256];
        double[] histGray = new double[256];
        for (int i = 0; i < 256; i++) {
            histR[i] = rCount[i] / (double) totalPixels;
            histG[i] = gCount[i] / (double) totalPixels;
            histB[i] = bCount[i] / (double) totalPixels;
            histGray[i] = grayCount[i] / (double) totalPixels;
        }
        stats.setHistR(histR);
        stats.setHistG(histG);
        stats.setHistB(histB);
        stats.setHistGray(histGray);

        stats.setUniqueColors(colorSet.size());
        double meanV = stats.getMeanV();
        stats.setBrightnessVariance((sumBrightnessSq / totalPixels) - (meanV * meanV));
        stats.setDarkPixelRatio(dark / (double) totalPixels);
        stats.setBrightPixelRatio(bright / (double) totalPixels);

        int maxCount = 0;
        Map<Integer, Integer> freqMap = buildFreqMap(img);
        for (Map.Entry<Integer, Integer> entry : freqMap.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                int rgb = entry.getKey();
                stats.setDominantR((rgb >> 16) & 0xFF);
                stats.setDominantG((rgb >> 8) & 0xFF);
                stats.setDominantB(rgb & 0xFF);
            }
        }

        double entropy = 0;
        for (int i = 0; i < 256; i++) {
            double p = histGray[i];
            if (p > 0) entropy -= p * (Math.log(p) / Math.log(2));
        }
        stats.setEntropy(entropy);

        return stats;
    }

    public double[] computeAverageColorSummary(List<ImageData> images) {
        double sumR = 0, sumG = 0, sumB = 0, sumH = 0, sumS = 0, sumV = 0;
        int n = 0;
        for (ImageData img : images) {
            ColorStats s = img.getColorStats();
            if (s == null) continue;
            sumR += s.getMeanR(); sumG += s.getMeanG(); sumB += s.getMeanB();
            sumH += s.getMeanH(); sumS += s.getMeanS(); sumV += s.getMeanV();
            n++;
        }
        if (n == 0) return new double[6];
        return new double[]{ sumR/n, sumG/n, sumB/n, sumH/n, sumS/n, sumV/n };
    }

    public int computeTotalUniqueColors(List<ImageData> images) {
        return images.stream()
                .map(ImageData::getColorStats)
                .filter(Objects::nonNull)
                .mapToInt(ColorStats::getUniqueColors)
                .sum();
    }

    public List<ImageData> sortByEntropy(List<ImageData> images) {
        List<ImageData> sorted = new ArrayList<>(images);
        sorted.sort(Comparator.comparingDouble(img -> {
            ColorStats s = img.getColorStats();
            return s != null ? s.getEntropy() : 0;
        }));
        return sorted;
    }

    public ImageData applyBinarization(ImageData source, int threshold, Path outputDir) {
        BufferedImage src = fxToBuffered(source.getStandardizedImage());
        BufferedImage result = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_BYTE_BINARY);

        for (int y = 0; y < src.getHeight(); y++) {
            for (int x = 0; x < src.getWidth(); x++) {
                int rgb = src.getRGB(x, y);
                int gray = ((rgb >> 16) & 0xFF) * 77 + ((rgb >> 8) & 0xFF) * 150 + (rgb & 0xFF) * 29;
                gray >>= 8;
                result.setRGB(x, y, gray > threshold ? 0xFFFFFF : 0x000000);
            }
        }

        return saveProcessed(source, result, "binary", "jpg",
                "Binaryzacja (próg=" + threshold + ")", outputDir);
    }

    public ImageData applyRotation(ImageData source, double angleDeg, Path outputDir) {
        BufferedImage src = fxToBuffered(source.getStandardizedImage());
        double rad = Math.toRadians(angleDeg);
        int w = src.getWidth(), h = src.getHeight();
        double cos = Math.abs(Math.cos(rad)), sin = Math.abs(Math.sin(rad));
        int newW = (int) Math.floor(w * cos + h * sin);
        int newH = (int) Math.floor(h * cos + w * sin);

        BufferedImage result = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = result.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        AffineTransform at = new AffineTransform();
        at.translate((newW - w) / 2.0, (newH - h) / 2.0);
        at.rotate(rad, w / 2.0, h / 2.0);
        g.drawImage(src, at, null);
        g.dispose();

        return saveProcessed(source, result, "rotated_" + (int) angleDeg + "deg", "png",
                "Obrót o " + angleDeg + "°", outputDir);
    }

    public ImageData applyContrastStretch(ImageData source, Path outputDir) {
        BufferedImage src = fxToBuffered(source.getStandardizedImage());
        BufferedImage result = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_RGB);

        int min = 255, max = 0;
        for (int y = 0; y < src.getHeight(); y++) {
            for (int x = 0; x < src.getWidth(); x++) {
                int rgb = src.getRGB(x, y);
                int gray = ((rgb >> 16) & 0xFF + (rgb >> 8) & 0xFF + (rgb & 0xFF)) / 3;
                if (gray < min) min = gray;
                if (gray > max) max = gray;
            }
        }

        for (int y = 0; y < src.getHeight(); y++) {
            for (int x = 0; x < src.getWidth(); x++) {
                int rgb = src.getRGB(x, y);
                int r = stretchChannel((rgb >> 16) & 0xFF, min, max);
                int g = stretchChannel((rgb >> 8) & 0xFF, min, max);
                int b = stretchChannel(rgb & 0xFF, min, max);
                result.setRGB(x, y, (r << 16) | (g << 8) | b);
            }
        }

        return saveProcessed(source, result, "contrast", "jpg",
                "Rozciąganie kontrastu", outputDir);
    }

    private ImageData saveProcessed(ImageData source, BufferedImage result,
                                    String suffix, String ext,
                                    String description, Path outputDir) {
        ImageData out = new ImageData();
        out.setOriginalName(source.getOriginalName());
        out.setOriginalPath(source.getOriginalPath());
        out.setProcessedDescription(description);

        BufferedImage displayable = toRGB(result);
        out.setProcessedImage(SwingFXUtils.toFXImage(displayable, null));

        String outName = "processed_" + suffix + "_" + stripExtension(source.getOriginalName()) + "." + ext;
        File outFile = outputDir != null
                ? outputDir.resolve(outName).toFile()
                : new File(outName);
        try {
            ImageIO.write(result, ext.equals("jpg") ? "jpeg" : ext, outFile);
            out.setProcessedSizeBytes(outFile.length());
        } catch (IOException e) {
            System.err.println("Błąd zapisu przetworzonego obrazu: " + outFile);
        }

        return out;
    }

    private BufferedImage fxToBuffered(Image fxImage) {
        BufferedImage bimg = SwingFXUtils.fromFXImage(fxImage, null);
        return toRGB(bimg);
    }

    private BufferedImage toRGB(BufferedImage src) {
        if (src.getType() == BufferedImage.TYPE_INT_RGB) return src;
        BufferedImage rgb = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = rgb.createGraphics();
        g.drawImage(src, 0, 0, null);
        g.dispose();
        return rgb;
    }

    private Map<Integer, Integer> buildFreqMap(BufferedImage img) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                int rgb = img.getRGB(x, y) & 0x00FFFFFF;
                map.merge(rgb, 1, Integer::sum);
            }
        }
        return map;
    }

    private int stretchChannel(int val, int min, int max) {
        if (max == min) return 128;
        return Math.min(255, Math.max(0, (val - min) * 255 / (max - min)));
    }

    private String stripExtension(String name) {
        int dot = name.lastIndexOf('.');
        return dot >= 0 ? name.substring(0, dot) : name;
    }
}