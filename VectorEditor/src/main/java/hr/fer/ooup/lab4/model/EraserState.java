package hr.fer.ooup.lab4.model;

import java.util.ArrayList;
import java.util.List;

public class EraserState implements State {

    private DocumentModel documentModel;
    private List<Point> points;

    public EraserState(DocumentModel documentModel) {
        this.documentModel=documentModel;
        this.points=new ArrayList<>();
    }

    @Override
    public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
        points.add(mousePoint);
    }

    @Override
    public void mouseUp(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
        for(Point p: points) {
            GraphicalObject selected = documentModel.findSelectedGraphicalObject(p);
            if (selected != null) {
                documentModel.removeGraphicalObject(selected);
            }
        }
        points.clear();
        documentModel.notifyListeners();
    }

    @Override
    public void mouseDragged(Point mousePoint) {
        points.add(mousePoint);
        documentModel.notifyListeners();
    }

    @Override
    public void keyPressed(int keyCode) {

    }

    @Override
    public void afterDraw(Renderer r, GraphicalObject go) {

    }

    @Override
    public void afterDraw(Renderer r) {
        for(int i = 1; i < points.size(); ++i) {
            Point s = points.get(i-1);
            Point e = points.get(i);
            r.drawLine(s, e);
        }
    }

    @Override
    public void onLeaving() {
        points.clear();
    }
}
