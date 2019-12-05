package ru.xarok.musicvisualizer.visualizers;

import ru.xarok.musicvisualizer.Visualizer;

import java.awt.*;
import java.awt.geom.Line2D;

public class CircleBarsVisualizer extends Visualizer {
    private int barsCount;
    private float gap;
    private float radius;

    public CircleBarsVisualizer(int width, int height, int barsCount, float radius, float gap){
        super(width, height);
        this.barsCount = barsCount;
        this.radius = radius;
        this.gap = gap;
    }

    @Override
    public void paint(Graphics2D g2){
        super.paint(g2);

        float[] samples = getSamples();
        if(samples == null){
            return;
        }

        int height = getHeight();
        int width = getWidth();

        g2.translate(width / 2f, height / 2f);

        float maxBarHeight = (Math.min(height, width)) / 2f - radius;
        float barWidth = (float)width / barsCount;
        float dAlpha = (float)(2 * Math.PI / barsCount);
        Line2D.Float line2D = new Line2D.Float();
        g2.setStroke(new BasicStroke(barWidth - gap));

        for(int i = 0; i < barsCount; i++){
            double alpha = i * dAlpha;
            int samplePos = (int)Math.ceil((float)i / barsCount * samples.length);

            float cos = (float) Math.cos(alpha);
            float sin = (float) Math.sin(alpha);
            float x1 = radius * cos;
            float y1 = radius * sin;
            float dx = maxBarHeight * Math.abs(samples[samplePos]) * cos + 2*cos;
            float dy = maxBarHeight * Math.abs(samples[samplePos]) * sin + 2*sin;

            line2D.setLine(x1,y1,x1 + dx,y1 + dy);
            g2.draw(line2D);
        }
    }
}
