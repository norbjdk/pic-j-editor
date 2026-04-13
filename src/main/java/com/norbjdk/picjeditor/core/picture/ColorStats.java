package com.norbjdk.picjeditor.core.picture;

public class ColorStats {
    private double meanR, meanG, meanB;
    private double meanH, meanS, meanV;
    private double[] histR = new double[256];
    private double[] histG = new double[256];
    private double[] histB = new double[256];
    private double[] histGray = new double[256];
    private int uniqueColors;
    private int dominantR, dominantG, dominantB;
    private double brightnessVariance;
    private double darkPixelRatio;
    private double brightPixelRatio;
    private double entropy;

    public ColorStats() {}

    public double getMeanR() {
        return meanR;
    }

    public void setMeanR(double meanR) {
        this.meanR = meanR;
    }

    public double getMeanG() {
        return meanG;
    }

    public void setMeanG(double meanG) {
        this.meanG = meanG;
    }

    public double getMeanB() {
        return meanB;
    }

    public void setMeanB(double meanB) {
        this.meanB = meanB;
    }

    public double getMeanH() {
        return meanH;
    }

    public void setMeanH(double meanH) {
        this.meanH = meanH;
    }

    public double getMeanS() {
        return meanS;
    }

    public void setMeanS(double meanS) {
        this.meanS = meanS;
    }

    public double getMeanV() {
        return meanV;
    }

    public void setMeanV(double meanV) {
        this.meanV = meanV;
    }

    public double[] getHistR() {
        return histR;
    }

    public void setHistR(double[] histR) {
        this.histR = histR;
    }

    public double[] getHistG() {
        return histG;
    }

    public void setHistG(double[] histG) {
        this.histG = histG;
    }

    public double[] getHistB() {
        return histB;
    }

    public void setHistB(double[] histB) {
        this.histB = histB;
    }

    public double[] getHistGray() {
        return histGray;
    }

    public void setHistGray(double[] histGray) {
        this.histGray = histGray;
    }

    public int getUniqueColors() {
        return uniqueColors;
    }

    public void setUniqueColors(int uniqueColors) {
        this.uniqueColors = uniqueColors;
    }

    public int getDominantR() {
        return dominantR;
    }

    public void setDominantR(int dominantR) {
        this.dominantR = dominantR;
    }

    public int getDominantG() {
        return dominantG;
    }

    public void setDominantG(int dominantG) {
        this.dominantG = dominantG;
    }

    public int getDominantB() {
        return dominantB;
    }

    public void setDominantB(int dominantB) {
        this.dominantB = dominantB;
    }

    public double getBrightnessVariance() {
        return brightnessVariance;
    }

    public void setBrightnessVariance(double brightnessVariance) {
        this.brightnessVariance = brightnessVariance;
    }

    public double getDarkPixelRatio() {
        return darkPixelRatio;
    }

    public void setDarkPixelRatio(double darkPixelRatio) {
        this.darkPixelRatio = darkPixelRatio;
    }

    public double getBrightPixelRatio() {
        return brightPixelRatio;
    }

    public void setBrightPixelRatio(double brightPixelRatio) {
        this.brightPixelRatio = brightPixelRatio;
    }

    public double getEntropy() {
        return entropy;
    }

    public void setEntropy(double entropy) {
        this.entropy = entropy;
    }
}
