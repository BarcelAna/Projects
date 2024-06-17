package hr.fer.ooup.lab4.model;

import java.util.List;
import java.util.Stack;

public class LineSegment extends AbstractGeometryObject {

    public LineSegment() {
        super(new Point[] {new Point(0, 0), new Point(10, 0) });
    }

    public LineSegment(Point start, Point end) {
        super(new Point[] {start, end});
    }

    @Override
    public Rectangle getBoundingBox() {
        Point start = getHotPoint(0);
        Point end = getHotPoint(1);
        Point diff = end.difference(start);
        int x = Math.min(start.getX(), end.getX());
        int y = Math.max(start.getY(), end.getY());
        return new Rectangle(x, y, Math.abs(diff.getX()), Math.abs(diff.getY()));
    }

    @Override
    public double selectionDistance(Point mousePoint) {
        return GeometryUtil.distanceFromLineSegment(getHotPoint(0), getHotPoint(1), mousePoint);
    }

    @Override
    public void render(Renderer r) {
        r.drawLine(getHotPoint(0), getHotPoint(1));
    }

    @Override
    public String getShapeName() {
        return "Linija";
    }

    @Override
    public GraphicalObject duplicate() {
        return new LineSegment(getHotPoint(0), getHotPoint(1));
    }

    @Override
    public String getShapeID() {
        return "@LINE";
    }

    @Override
    public void load(Stack<GraphicalObject> stack, String data) {
        String[] elements = data.split(" ");
        int hp1_x = Integer.parseInt(elements[0]);
        int hp1_y = Integer.parseInt(elements[1]);
        int hp2_x = Integer.parseInt(elements[2]);
        int hp2_y = Integer.parseInt(elements[3]);
        GraphicalObject newLine = new LineSegment(new Point(hp1_x, hp1_y), new Point(hp2_x, hp2_y));
        stack.push(newLine);
    }

    @Override
    public void save(List<String> rows) {
        Point s = getHotPoint(0);
        Point e = getHotPoint(1);
        rows.add(String.format("%s %d %d %d %d", getShapeID(), s.getX(), s.getY(), e.getX(), e.getY()));
    }
}
