package hr.fer.ooup.lab4.model;

import java.util.*;

public class CompositeShape implements GraphicalObject {

    private List<GraphicalObject> children;
    private boolean selected;
    private List<GraphicalObjectListener> listeners = new ArrayList<>();

    public CompositeShape(List<GraphicalObject> children) {
        this.children=children;
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
        return 0;
    }

    @Override
    public Point getHotPoint(int index) {
        return null;
    }

    @Override
    public void setHotPoint(int index, Point point) {

    }

    @Override
    public boolean isHotPointSelected(int index) {
        return false;
    }

    @Override
    public void setHotPointSelected(int index, boolean selected) {

    }

    @Override
    public double getHotPointDistance(int index, Point mousePoint) {
        return 0;
    }

    @Override
    public void translate(Point delta) {
        //delegiranje
        for(GraphicalObject child: children) {
            child.translate(delta);
        }
        notifyListeners();
    }

    @Override
    public Rectangle getBoundingBox() {
        Stack<GraphicalObject> stack = new Stack<>();
        decomposeChildren(stack, children);
        //unija
        Integer minX = null;
        Integer maxY = null;
        Integer maxX = null;
        Integer maxXWidth = null;
        Integer minY = null;
        Integer minYHeight = null;
        while(!stack.isEmpty()) {
            GraphicalObject obj = stack.pop();
            int x = obj.getBoundingBox().getX();
            int y = obj.getBoundingBox().getY();
            if(minX==null || x < minX) {
                minX=x;
            }
            if(maxY==null || y > maxY) {
                maxY=y;
            }
            if(maxX==null || x > maxX) {
                maxX=x;
                maxXWidth=obj.getBoundingBox().getWidth();
            }
            if(minY==null || y < minY) {
                minY=y;
                minYHeight=obj.getBoundingBox().getHeight();
            }
        }
        return new Rectangle(minX, maxY, maxX-minX+maxXWidth, maxY-minY+minYHeight);
    }

    private void decomposeChildren(Stack<GraphicalObject> stack, List<GraphicalObject> children) {
        for(GraphicalObject child: children) {
            if(child instanceof CompositeShape) {
                decomposeChildren(stack, ((CompositeShape) child).children);
            } else {
                stack.push(child);
            }
        }
    }

    @Override
    public double selectionDistance(Point mousePoint) {
        Rectangle r = getBoundingBox();
        if(mousePoint.getX()>r.getX() && mousePoint.getX()<r.getX()+r.getWidth() &&
                mousePoint.getY()<r.getY() && mousePoint.getY()>r.getY()-r.getHeight()) {
            return 0;
        }
        LineSegment a1 = new LineSegment(new Point(r.getX(), r.getY()), new Point(r.getX()+r.getWidth(), r.getY()));
        LineSegment a2 = new LineSegment(new Point(r.getX(), r.getY()-r.getHeight()), new Point(r.getX()+r.getWidth(), r.getY()-r.getHeight()));
        LineSegment b1 = new LineSegment(new Point(r.getX(), r.getY()), new Point(r.getX(), r.getY()-r.getHeight()));
        LineSegment b2 = new LineSegment(new Point(r.getX()+r.getWidth(), r.getY()), new Point(r.getX()+r.getWidth(), r.getY()-r.getHeight()));

        double da1 = GeometryUtil.distanceFromLineSegment(a1.getHotPoint(0), a1.getHotPoint(1), mousePoint);
        double da2 = GeometryUtil.distanceFromLineSegment(a2.getHotPoint(0), a2.getHotPoint(1), mousePoint);
        double db1 = GeometryUtil.distanceFromLineSegment(b1.getHotPoint(0), b1.getHotPoint(1), mousePoint);
        double db2 = GeometryUtil.distanceFromLineSegment(b2.getHotPoint(0), b2.getHotPoint(1), mousePoint);

        return Collections.min(Arrays.stream(new Double[]{da1,da2,db1,db2}).toList());
    }

    @Override
    public void render(Renderer r) {
        //delegacija
        for(GraphicalObject child: children) {
            child.render(r);
        }
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
    public String getShapeName() {
        return "group";
    }

    @Override
    public GraphicalObject duplicate() {
        return new CompositeShape(children);
    }

    @Override
    public String getShapeID() {
        return "@COMP";
    }

    @Override
    public void load(Stack<GraphicalObject> stack, String data) {
        int childrenCnt = Integer.parseInt(data);
        List<GraphicalObject> newChildren = new ArrayList<>();
        for(int i = 0; i < childrenCnt; ++i) {
            newChildren.add(stack.pop());
        }
        GraphicalObject newComposite = new CompositeShape(newChildren);
        stack.push(newComposite);
    }

    @Override
    public void save(List<String> rows) {
        for(GraphicalObject child: children) {
            child.save(rows);
        }
        rows.add(String.format("%s %d", getShapeID(), children.size()));
    }

    public List<GraphicalObject> getChildren() {
        return children;
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
