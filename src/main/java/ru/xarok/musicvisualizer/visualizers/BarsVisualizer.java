package ru.xarok.musicvisualizer.visualizers;

import ru.xarok.musicvisualizer.Visualizer;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class BarsVisualizer extends Visualizer {
    private int barsCount;
    private float gap;

    public BarsVisualizer(int width, int height, int barsCount, float gap){
        super(width, height);
        this.barsCount = barsCount;
        this.gap = gap;
    }

    @Override
    protected void paint(Graphics g){
        Graphics2D g2 = (Graphics2D)g;
        super.paint(g2); //Clear canvas

        float[] samples = getSamples();
        if(samples == null){
            return;
        }

        int height = getHeight();
        int width = getWidth();
        float barWidth = (float)width / barsCount - gap;
        Rectangle2D.Float rect2D = new Rectangle2D.Float();

        for(int i = 0; i < barsCount; i++) {
            int samplePos = (int)Math.ceil((double)i/barsCount * samples.length);
            float x = (barWidth + gap) * i;
            float y = height - Math.abs(samples[samplePos]) * height;

            rect2D.setRect(x, y, barWidth, y);
            g2.fill(rect2D);
        }
    }
}
