package hr.fer.ooup.lab4.model;

public class GeometryUtil {

    public static double distanceFromPoint(Point point1, Point point2) {
        return Math.sqrt(Math.pow(point1.getX()-point2.getX() ,2) + Math.pow(point1.getY()-point2.getY(), 2));
    }

    public static double distanceFromLineSegment(Point start, Point end, Point p) {
        Point s = start.getX() < end.getX() ? start : end;
        Point e = s.equals(start) ? end : start;
        if (p.getX() <= s.getX()) {
            return distanceFromPoint(p, s);
        } else if (p.getX() >= e.getX()) {
            return distanceFromPoint(p, e);
        } else {
            double numerator = Math.abs((e.getY() - s.getY()) * p.getX() - (e.getX() - s.getX()) * p.getY() + e.getX() * s.getY() - e.getY() * s.getX());
            double denominator = distanceFromPoint(e, s);
            return numerator / denominator;
        }
    }
}
