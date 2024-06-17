package hr.fer.ooup.lab4.model;

import java.util.List;
import java.util.Stack;

public class DecomposeState implements State {
    private DocumentModel model;

    public DecomposeState(DocumentModel model) {
        this.model=model;
    }

    @Override
    public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
        GraphicalObject selected = model.findSelectedGraphicalObject(mousePoint);
        if(selected instanceof CompositeShape) {
            CompositeShape composite = (CompositeShape) selected;
            model.removeGraphicalObject(selected);
            Stack<GraphicalObject> stack = new Stack<>();
            decompose(composite, stack);
            while(!stack.isEmpty()) {
                GraphicalObject obj = stack.pop();
                model.addGraphicalObject(obj);
            }
        }
    }

    private void decompose(CompositeShape obj, Stack<GraphicalObject> stack) {
        List<GraphicalObject> children = obj.getChildren();
        for(GraphicalObject c: children) {
            if(c instanceof CompositeShape) {
                decompose((CompositeShape) c, stack);
            } else {
                stack.push(c);
            }
        }
    }

    @Override
    public void mouseUp(Point mousePoint, boolean shiftDown, boolean ctrlDown) {

    }

    @Override
    public void mouseDragged(Point mousePoint) {

    }

    @Override
    public void keyPressed(int keyCode) {

    }

    @Override
    public void afterDraw(Renderer r, GraphicalObject go) {

    }

    @Override
    public void afterDraw(Renderer r) {

    }

    @Override
    public void onLeaving() {

    }
}
