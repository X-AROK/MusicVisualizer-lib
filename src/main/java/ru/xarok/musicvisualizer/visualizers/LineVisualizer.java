package ru.xarok.musicvisualizer.visualizers;

import ru.xarok.musicvisualizer.Visualizer;

import java.awt.*;
import java.awt.geom.Line2D;

public class LineVisualizer extends Visualizer {
    private float stroke;


    public LineVisualizer(int width, int height){
        super(width,height);
        stroke = 1f;
    }
    public LineVisualizer(int width, int height, float stroke) {
        super(width, height);
        this.stroke = stroke;
    }

    @Override
    protected void paint(Graphics2D g2) {
        super.paint(g2); //Clear canvas

        float[] samples = getSamples();
        if(samples == null){
            return;
        }

        int height = getHeight();
        int width = getWidth();

        g2.setStroke(new BasicStroke(stroke));

        float prevX = 0;
        float prevY = height/2f - samples[0] * height/2;
        Line2D.Float line2D = new Line2D.Float();

        for(int i = 1; i < samples.length; i++){
            float x = (float)i / samples.length * width;
            float y = height/2f - samples[i] * height/2;
            line2D.setLine(prevX, prevY, x, y);
            g2.draw(line2D);
            prevX = x;
            prevY = y;
        }
    }
}
