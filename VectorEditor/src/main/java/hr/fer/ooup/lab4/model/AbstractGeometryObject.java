package hr.fer.ooup.lab4.model;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGeometryObject implements GraphicalObject {

    private Point[] hotPoints;
    private boolean[] hotPointSelected;
    private boolean selected;
    protected List<GraphicalObjectListener> listeners = new ArrayList<>();

    protected AbstractGeometryObject(Point[] hotPoints) {
        this.hotPoints=hotPoints;
        this.hotPointSelected = new boolean[hotPoints.length];
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected=selected;
        notifySelectionListener();
    }

    @Override
    public int getNumberOfHotPoints() {
        return hotPoints.length;
    }

    @Override
    public Point getHotPoint(int index) {
        return hotPoints[index];
    }

    @Override
    public void setHotPoint(int index, Point point) {
        hotPoints[index] = point;
        notifyListeners();
    }

    @Override
    public boolean isHotPointSelected(int index) {
        return hotPointSelected[index];
    }

    @Override
    public void setHotPointSelected(int index, boolean selected) {
        hotPointSelected[index] = selected;
    }

    @Override
    public double getHotPointDistance(int index, Point mousePoint) {
        return GeometryUtil.distanceFromPoint(hotPoints[index], mousePoint);
    }

    @Override
    public void addGraphicalObjectListener(GraphicalObjectListener l) {
        listeners.add(l);
    }

    @Override
    public void removeGraphicalObjectListener(GraphicalObjectListener l) {
        listeners.remove(l);
    }

    @Override
    public void translate(Point delta) {
        for(int i = 0; i < getNumberOfHotPoints(); ++i) {
            Point newPoint=getHotPoint(i).translate(delta);
            setHotPoint(i, newPoint);
        }
        notifyListeners();
    }


    protected void notifyListeners() {
        for(GraphicalObjectListener l: listeners) {
            l.graphicalObjectChanged(this);
        }
    }

    protected void notifySelectionListener() {
        for(GraphicalObjectListener l:listeners) {
            l.graphicalObjectSelectionChanged(this);
        }
    }
}
