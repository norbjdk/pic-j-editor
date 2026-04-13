package com.norbjdk.picjeditor.core.picture;

import javafx.scene.image.Image;

import java.nio.file.Path;

public class ImageData {
    private String originalName;
    private Path originalPath;

    private int originalWidth;
    private int originalHeight;
    private long originalSizeBytes;

    private int stdWidth;
    private int stdHeight;
    private long standardizedSizeBytes;

    private Image originalImage;
    private Image standardizedImage;

    private ColorStats colorStats;

    private Image processedImage;
    private String processedDescription;
    private long processedSizeBytes;

    public ImageData() {}

    public String getOriginalName() { return originalName; }
    public void setOriginalName(String originalName) { this.originalName = originalName; }

    public Path getOriginalPath() { return originalPath; }
    public void setOriginalPath(Path originalPath) { this.originalPath = originalPath; }

    public int getOriginalWidth() { return originalWidth; }
    public void setOriginalWidth(int originalWidth) { this.originalWidth = originalWidth; }

    public int getOriginalHeight() { return originalHeight; }
    public void setOriginalHeight(int originalHeight) { this.originalHeight = originalHeight; }

    public long getOriginalSizeBytes() { return originalSizeBytes; }
    public void setOriginalSizeBytes(long originalSizeBytes) { this.originalSizeBytes = originalSizeBytes; }

    public int getStdWidth() { return stdWidth; }
    public void setStdWidth(int stdWidth) { this.stdWidth = stdWidth; }

    public int getStdHeight() { return stdHeight; }
    public void setStdHeight(int stdHeight) { this.stdHeight = stdHeight; }

    public long getStandardizedSizeBytes() { return standardizedSizeBytes; }
    public void setStandardizedSizeBytes(long standardizedSizeBytes) { this.standardizedSizeBytes = standardizedSizeBytes; }

    public Image getOriginalImage() { return originalImage; }
    public void setOriginalImage(Image originalImage) { this.originalImage = originalImage; }

    public Image getStandardizedImage() { return standardizedImage; }
    public void setStandardizedImage(Image standardizedImage) { this.standardizedImage = standardizedImage; }

    public ColorStats getColorStats() { return colorStats; }
    public void setColorStats(ColorStats colorStats) { this.colorStats = colorStats; }

    public Image getProcessedImage() { return processedImage; }
    public void setProcessedImage(Image processedImage) { this.processedImage = processedImage; }

    public String getProcessedDescription() { return processedDescription; }
    public void setProcessedDescription(String processedDescription) { this.processedDescription = processedDescription; }

    public long getProcessedSizeBytes() { return processedSizeBytes; }
    public void setProcessedSizeBytes(long processedSizeBytes) { this.processedSizeBytes = processedSizeBytes; }

    public long getOriginalSizeKB() { return originalSizeBytes / 1024; }

    public long getStandardizedSizeKB() { return standardizedSizeBytes / 1024; }

    public double getCompressionRatio() {
        if (originalSizeBytes == 0) return 0;
        return (double) standardizedSizeBytes / originalSizeBytes;
    }

    public String getOriginalDimensions() {
        return originalWidth + "x" + originalHeight;
    }

    public String getStdDimensions() {
        return stdWidth + "x" + stdHeight;
    }

    @Override
    public String toString() {
        return originalName + " [" + getOriginalDimensions() + " → " + getStdDimensions() + "]";
    }
}
