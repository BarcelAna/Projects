package hr.fer.ooup.lab4.model;

import java.awt.*;
import java.util.Arrays;

public class G2DRendererImpl implements Renderer {

    private Graphics2D g2d;

    public G2DRendererImpl(Graphics2D g2d) {
        this.g2d = g2d;
    }

    @Override
    public void drawLine(Point s, Point e) {
        //g2d.setColor(Color.BLUE);
        g2d.drawLine(s.getX(), s.getY(), e.getX(), e.getY());
    }

    @Override
    public void fillPolygon(Point[] points) {
        //g2d.setColor(Color.BLUE);
        g2d.fillPolygon(Arrays.stream(points).map(Point::getX).mapToInt(Integer::intValue).toArray(),
                Arrays.stream(points).map(Point::getY).mapToInt(Integer::intValue).toArray(),
                points.length);
        g2d.setColor(Color.RED);
        g2d.drawPolygon(Arrays.stream(points).map(Point::getX).mapToInt(Integer::intValue).toArray(),
                Arrays.stream(points).map(Point::getY).mapToInt(Integer::intValue).toArray(),
                points.length);
    }

}
