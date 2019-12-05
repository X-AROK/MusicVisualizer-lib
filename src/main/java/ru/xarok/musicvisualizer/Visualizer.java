package ru.xarok.musicvisualizer;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Visualizer {
    private float[] samples;
    private int width;
    private int height;

    public void setSamples(float[] samples) {
        this.samples = samples;
    }
    protected float[] getSamples(){
        return samples;
    }
    protected int getWidth() {
        return width;
    }
    protected int getHeight() {
        return height;
    }

    public Visualizer(int width, int height) {
        this.width = width;
        this.height = height;
    }

    protected void paint(Graphics2D g){
        g.clearRect(0, 0, getWidth(), getHeight());
    }

    protected BufferedImage getImage() {
        BufferedImage image = new BufferedImage(this.getWidth(), this.getHeight(),BufferedImage.TYPE_INT_RGB);

        Graphics2D g2=(Graphics2D)image.getGraphics();
        this.paint(g2);

        return image;
    }
}
