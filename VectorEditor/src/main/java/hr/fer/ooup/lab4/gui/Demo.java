package hr.fer.ooup.lab4.gui;

import hr.fer.ooup.lab4.model.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Demo {
    public static void main(String[] args) {
        List<GraphicalObject> objects = new ArrayList<>();
        objects.add(new LineSegment());
        objects.add(new Oval());
        objects.add(new CompositeShape(new ArrayList<>()));

        SwingUtilities.invokeLater(()->{
            new GUI(objects).setVisible(true);
        });
    }
}
