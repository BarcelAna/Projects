package hr.fer.ooup.lab4.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Oval extends AbstractGeometryObject {


    public Oval() {
        super(new Point[]{new Point(10, 0), new Point(0, 10)});
    } //desni hot point pa donji

    public Oval(Point rightHotPoint, Point bottomHotPoint) {
        super(new Point[]{rightHotPoint, bottomHotPoint});
    }

    @Override
    public Rectangle getBoundingBox() {
        Point rightHP = getHotPoint(0);
        Point bottomHP = getHotPoint(1);
        Point center = new Point(bottomHP.getX(), rightHP.getY());
        Point diff = rightHP.difference(bottomHP);
        int x = center.getX() - Math.abs(diff.getX());
        int y = center.getY() + Math.abs(diff.getY());
        return new Rectangle(x, y, 2*Math.abs(diff.getX()), 2*Math.abs(diff.getY()));
    }

    @Override
    public double selectionDistance(Point mousePoint) {
        Point center = new Point(getHotPoint(1).getX(), getHotPoint(0).getY());
        double a = GeometryUtil.distanceFromPoint(center, getHotPoint(0));
        double b = GeometryUtil.distanceFromPoint(center, getHotPoint(1));
        return GeometryUtil.distanceFromPoint(center, mousePoint)-Math.max(a,b);
    }

    @Override
    public void render(Renderer r) {
        int sides = 360;
        int radiusX = Math.abs(getHotPoint(0).getX() - getHotPoint(1).getX());
        int radiusY = Math.abs(getHotPoint(0).getY() - getHotPoint(1).getY());
        Point center = new Point(getHotPoint(1).getX(), getHotPoint(0).getY());
        Point[] points = new Point[sides];

        for (int i = 0; i < sides; i++) {
            double angle = 2 * Math.PI * i / sides;
            int x = center.getX() + (int) (radiusX * Math.cos(angle));
            int y = center.getY() + (int) (radiusY * Math.sin(angle));
            points[i] = new Point(x, y);
        }

        r.fillPolygon(points);
    }

    @Override
    public String getShapeName() {
        return "Oval";
    }

    @Override
    public GraphicalObject duplicate() {
        return new Oval(getHotPoint(0), getHotPoint(1));
    }

    @Override
    public String getShapeID() {
        return "@OVAL";
    }

    @Override
    public void load(Stack<GraphicalObject> stack, String data) {
        String[] elements = data.split(" ");
        int hp1_x = Integer.parseInt(elements[0]);
        int hp1_y = Integer.parseInt(elements[1]);
        int hp2_x = Integer.parseInt(elements[2]);
        int hp2_y = Integer.parseInt(elements[3]);
        GraphicalObject newOval = new Oval(new Point(hp1_x, hp1_y), new Point(hp2_x, hp2_y));
        stack.push(newOval);
    }

    @Override
    public void save(List<String> rows) {
        Point hp1 = getHotPoint(0);
        Point hp2 = getHotPoint(1);
        rows.add(String.format("%s %d %d %d %d", getShapeID(), hp1.getX(), hp1.getY(), hp2.getX(), hp2.getY()));
    }

}
