package hr.fer.ooup.lab4.gui;

import hr.fer.ooup.lab4.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.*;
import java.util.List;

public class GUI extends JFrame {
    private List<GraphicalObject> objects;
    private DocumentModel documentModel;
    private Canvas canvas;
    private State currentState;
    private Map<String, GraphicalObject> map = new HashMap<>();

    public GUI(List<GraphicalObject> objects) {
        this.objects = objects;
        for(GraphicalObject obj: objects) {
            map.put(obj.getShapeID(), obj);
        }
        this.documentModel = new DocumentModel();
        this.currentState = new IdleState();
        this.canvas = new Canvas(documentModel, this);

        setLocation(30,30);
        setSize(500, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("Vector Editor");
        setFocusable(false);
        initGUI();
    }


    private void initGUI() {
        setLayout(new BorderLayout());

        getContentPane().add(canvas);

        JToolBar toolBar = new JToolBar();
        for(GraphicalObject obj: objects) {
            if(obj instanceof CompositeShape) continue;
            JButton objButton = new JButton(obj.getShapeName());
            objButton.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    changeState(new AddShapeState(documentModel, obj));
                }
            });
            objButton.setFocusable(false);
            toolBar.add(objButton);
        }

        JButton selectButton = new JButton("Selektiraj");
        selectButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeState(new SelectShapeState(documentModel));
            }
        });
        selectButton.setFocusable(false);
        toolBar.add(selectButton);

        JButton eraseButton = new JButton("Brisalo");
        eraseButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeState(new EraserState(documentModel));
            }
        });
        eraseButton.setFocusable(false);
        toolBar.add(eraseButton);

        JButton svgExport = new JButton("SVG Export");
        svgExport.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int option = fileChooser.showOpenDialog(GUI.this);
                if(option==JFileChooser.APPROVE_OPTION) {
                    String fileName = fileChooser.getSelectedFile().getPath();
                    SVGRendererImpl r = new SVGRendererImpl(fileName);
                    for(GraphicalObject obj: documentModel.list()) {
                        obj.render(r);
                    }
                    r.close();
                }
            }
        });
        svgExport.setFocusable(false);
        toolBar.add(svgExport);

        JButton saveButton = new JButton("Pohrani");
        saveButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int option = fileChooser.showOpenDialog(GUI.this);
                if(option==JFileChooser.APPROVE_OPTION) {
                    String fileName = fileChooser.getSelectedFile().getPath();
                    List<String> rows = new ArrayList<>();
                    for(GraphicalObject obj: documentModel.list()) {
                        obj.save(rows);
                    }
                    try(FileWriter fw = new FileWriter(fileName)) {
                        for(String line: rows) {
                            fw.write(line+"\n");
                        }
                    } catch(IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        saveButton.setFocusable(false);
        toolBar.add(saveButton);

        JButton loadButton = new JButton("Ucitaj");
        loadButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                documentModel.clear();
                JFileChooser fileChooser = new JFileChooser();
                int option = fileChooser.showOpenDialog(GUI.this);
                if(option==JFileChooser.APPROVE_OPTION) {
                    String fileName = fileChooser.getSelectedFile().getPath();
                    List<String> rows = new ArrayList<>();
                    try(Scanner sc = new Scanner(new File(fileName))) {
                        while(sc.hasNextLine()) {
                            rows.add(sc.nextLine());
                        }
                    } catch (FileNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                    Stack<GraphicalObject> stack = new Stack<>();
                    for(String line: rows) {
                        String id = line.substring(0, line.indexOf(" "));
                        String data = line.substring(line.indexOf(" ")+1);
                        GraphicalObject prototype = map.get(id);
                        prototype.load(stack, data);
                    }
                    while(!stack.empty()) {
                        documentModel.addGraphicalObject(stack.pop());
                    }
                }

            }
        });
        loadButton.setFocusable(false);
        toolBar.add(loadButton);

        JButton decomposeButton = new JButton("Dikompozaj");
        decomposeButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeState(new DecomposeState(documentModel));
            }
        });
        decomposeButton.setFocusable(false);
        toolBar.add(decomposeButton);


        toolBar.setFocusable(false);
        getContentPane().add(toolBar, BorderLayout.NORTH);
    }

    public void changeState(State state) {
        currentState.onLeaving();
        currentState = state;
    }

    public State currentState() {
        return currentState;
    }
}
