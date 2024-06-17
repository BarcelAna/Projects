package hr.fer.ooup.lab4.model;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class SelectShapeState implements State {
    private DocumentModel documentModel;

    public SelectShapeState(DocumentModel documentModel) {
        this.documentModel = documentModel;
    }

    @Override
    public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
        GraphicalObject selected = documentModel.findSelectedGraphicalObject(mousePoint);
        if(selected==null) return;

        if(selected.isSelected()) {
            int selectedHPIndex = documentModel.findSelectedHotPoint(selected, mousePoint);
            if(selectedHPIndex!=-1) {
                selected.setHotPointSelected(selectedHPIndex, true);
                for(int i = 0; i < selected.getNumberOfHotPoints(); ++i) {
                    if(i!=selectedHPIndex) {
                        selected.setHotPointSelected(i, false);
                    }
                }
            }
        } else {
            selected.setSelected(true);
            //documentModel.addGraphicalObject(selected);
            if(!ctrlDown) {
                for (GraphicalObject obj : documentModel.list()) {
                    if(obj.isSelected() && obj!=selected) {
                        //documentModel.removeGraphicalObject(obj);
                        obj.setSelected(false);
                    }
                }
            }
        }

    }

    @Override
    public void mouseUp(Point mousePoint, boolean shiftDown, boolean ctrlDown) {

    }

    @Override
    public void mouseDragged(Point mousePoint) {
        if(documentModel.getSelectedObjects().size()!=1) return;
        GraphicalObject selected = documentModel.getSelectedObjects().getFirst();
        int selectedHPIndex = documentModel.findSelectedHotPoint(selected, mousePoint);
        if(selectedHPIndex!=-1) {
            Point selectedHP = selected.getHotPoint(selectedHPIndex);
            Point newHP = selectedHP.translate(mousePoint.difference(selectedHP));
            selected.setHotPoint(selectedHPIndex, newHP);
        }
    }

    @Override
    public void keyPressed(int keyCode) {
        if(keyCode==KeyEvent.VK_UP) {
            for(GraphicalObject obj: documentModel.getSelectedObjects()) {
                obj.translate(new Point(0, -3));
            }
        } else if(keyCode==KeyEvent.VK_DOWN) {
            for(GraphicalObject obj: documentModel.getSelectedObjects()) {
                obj.translate(new Point(0, 3));
            }
        } else if(keyCode==KeyEvent.VK_LEFT) {
            for(GraphicalObject obj: documentModel.getSelectedObjects()) {
                obj.translate(new Point(-3, 0));
            }
        } else if(keyCode==KeyEvent.VK_RIGHT) {
            for(GraphicalObject obj: documentModel.getSelectedObjects()) {
                obj.translate(new Point(3, 0));
            }
        } else if(keyCode==61) {
            for(GraphicalObject obj: documentModel.getSelectedObjects()) {
                documentModel.increaseZ(obj);
            }
        } else if(keyCode==47) {
            for(GraphicalObject obj: documentModel.getSelectedObjects()) {
                documentModel.decreaseZ(obj);
            }
        } else if(keyCode==KeyEvent.VK_G) {
            List<GraphicalObject> group = new ArrayList<>();
            //List<GraphicalObject> copyList = new ArrayList<>(documentModel.getSelectedObjects());
            for(GraphicalObject obj: documentModel.list()) {
                if(obj.isSelected()) {
                    obj.setSelected(false);
                    group.add(obj);
                }
            }
            for(GraphicalObject obj: group) {
                documentModel.removeGraphicalObject(obj);
            }
            GraphicalObject composite = new CompositeShape(group);
            composite.setSelected(true);
            documentModel.addGraphicalObject(composite);
        } else if(keyCode==KeyEvent.VK_U) {
            if(documentModel.getSelectedObjects().size()==1) {
                GraphicalObject selected = documentModel.getSelectedObjects().getFirst();
                if(selected instanceof CompositeShape) {
                    documentModel.removeGraphicalObject(selected);
                    for(GraphicalObject child: ((CompositeShape) selected).getChildren()) {
                        child.setSelected(true);
                        documentModel.addGraphicalObject(child);
                    }
                }
            }
        }
    }

    @Override
    public void afterDraw(Renderer r, GraphicalObject go) {
        if(go.isSelected()) {
            Rectangle bb = go.getBoundingBox();
            Point a = new Point(bb.getX(), bb.getY());
            Point b = new Point(bb.getX()+bb.getWidth(), bb.getY());
            Point c = new Point(bb.getX(), bb.getY()-bb.getHeight());
            Point d = new Point(bb.getX()+bb.getWidth(), bb.getY() - bb.getHeight());
            r.drawLine(a, b);
            r.drawLine(a, c);
            r.drawLine(b, d);
            r.drawLine(c,d);
        }
    }

    @Override
    public void afterDraw(Renderer r) {
        if(documentModel.getSelectedObjects().size()==1) {
            GraphicalObject object = documentModel.getSelectedObjects().getFirst();
            for(int i = 0; i < object.getNumberOfHotPoints(); ++i) {
                Point hp = object.getHotPoint(i);
                Point a = new Point(hp.getX()-5, hp.getY()-5);
                Point b = new Point(hp.getX()+5, hp.getY()-5);
                Point c = new Point(hp.getX()-5, hp.getY()+5);
                Point d = new Point(hp.getX()+5, hp.getY()+5);
                r.drawLine(a, b);
                r.drawLine(a, c);
                r.drawLine(b, d);
                r.drawLine(c,d);
            }
        }
    }

    @Override
    public void onLeaving() {
        for(GraphicalObject obj: documentModel.list()) {
            if(obj.isSelected()) {
                obj.setSelected(false);
            }
        }
    }
}
