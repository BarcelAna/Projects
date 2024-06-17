package hr.fer.ooup.lab4.gui;

import hr.fer.ooup.lab4.model.*;
import hr.fer.ooup.lab4.model.Point;
import hr.fer.ooup.lab4.model.Renderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Canvas extends JComponent {

    private DocumentModel documentModel;
    private GUI parent;
    private Color c = Color.RED;


    public Canvas(DocumentModel documentModel, GUI parent) {
        this.documentModel = documentModel;
        this.parent=parent;
        this.documentModel.addDocumentModelListener(new DocumentModelListener() {
            @Override
            public void documentChange() {
                repaint();
            }
        });
        setFocusable(true);
        initListeners();
    }

    private void initListeners() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_ESCAPE) {
                    parent.changeState(new IdleState());
                } else {
                    parent.currentState().keyPressed(e.getKeyCode());
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                parent.currentState().mouseDown(new hr.fer.ooup.lab4.model.Point(e.getX(), e.getY()), e.isShiftDown(), e.isControlDown());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                parent.currentState().mouseUp(new hr.fer.ooup.lab4.model.Point(e.getX(), e.getY()), e.isShiftDown(), e.isControlDown());
            }

        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                parent.currentState().mouseDragged(new Point(e.getX(), e.getY()));
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        g.setColor(this.c);
        Renderer r = new G2DRendererImpl(g2d);
        for(GraphicalObject go: documentModel.list()) {
            go.render(r);
            parent.currentState().afterDraw(r, go);
        }
        parent.currentState().afterDraw(r);
    }

}
