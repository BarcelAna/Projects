package hr.fer.ooup.lab4.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DocumentModel {

    public final static double SELECTION_PROXIMITY = 10;

    // Kolekcija svih grafičkih objekata:
    private List<GraphicalObject> objects = new ArrayList<>();
    // Read-Only proxy oko kolekcije grafičkih objekata:
    private List<GraphicalObject> roObjects = Collections.unmodifiableList(objects);
    // Kolekcija prijavljenih promatrača:
    private List<DocumentModelListener> listeners = new ArrayList<>();
    // Kolekcija selektiranih objekata:
    private List<GraphicalObject> selectedObjects = new ArrayList<>();
    // Read-Only proxy oko kolekcije selektiranih objekata:
    private List<GraphicalObject> roSelectedObjects = Collections.unmodifiableList(selectedObjects);

    // Promatrač koji će biti registriran nad svim objektima crteža...
    private final GraphicalObjectListener goListener = new GraphicalObjectListener() {

        @Override
        public void graphicalObjectChanged(GraphicalObject go) {
            notifyListeners();
        }

        @Override
        public void graphicalObjectSelectionChanged(GraphicalObject go) {
            if(go.isSelected()) {
                selectedObjects.add(go);
            } else {
                selectedObjects.remove(go);
            }
            notifyListeners();
        }
    };

    // Konstruktor...
    public DocumentModel() {
        for(GraphicalObject obj: objects) {
            obj.addGraphicalObjectListener(goListener);
        }
    }

    // Brisanje svih objekata iz modela (pazite da se sve potrebno odregistrira)
    // i potom obavijeste svi promatrači modela
    public void clear() {
        objects.clear();
        selectedObjects.clear();
        notifyListeners();
    }

    // Dodavanje objekta u dokument (pazite je li već selektiran; registrirajte model kao promatrača)
    public void addGraphicalObject(GraphicalObject obj) {
        if (obj.isSelected() && !selectedObjects.contains(obj)) {
            selectedObjects.add(obj);
        }
        objects.add(obj);
        obj.addGraphicalObjectListener(goListener);
        notifyListeners();
    }

    // Uklanjanje objekta iz dokumenta (pazite je li već selektiran; odregistrirajte model kao promatrača)
    public void removeGraphicalObject(GraphicalObject obj)  {
        if(obj.isSelected()) {
            obj.setSelected(false);
        }
        objects.remove(obj);
        obj.removeGraphicalObjectListener(goListener);
        notifyListeners();
    }

    // Vrati nepromjenjivu listu postojećih objekata (izmjene smiju ići samo kroz metode modela)
    public List<GraphicalObject> list() {
        return roObjects;
    }

    // Prijava...
    public void addDocumentModelListener(DocumentModelListener l) {
        listeners.add(l);
    }

    // Odjava...
    public void removeDocumentModelListener(DocumentModelListener l) {
        listeners.remove(l);
    }

    // Obavještavanje...
    public void notifyListeners() {
        for(DocumentModelListener l:listeners) {
            l.documentChange();
        }
    }

    // Vrati nepromjenjivu listu selektiranih objekata
    public List<GraphicalObject> getSelectedObjects() {
        return roSelectedObjects;
    }

    // Pomakni predani objekt u listi objekata na jedno mjesto kasnije...
    // Time će se on iscrtati kasnije (pa će time možda veći dio biti vidljiv)
    public void increaseZ(GraphicalObject go) {
        int index = objects.indexOf(go);
        if(index==objects.size()-1) return;
        objects.remove(go);
        objects.add(index+1, go);
        notifyListeners();
    }

    // Pomakni predani objekt u listi objekata na jedno mjesto ranije...
    public void decreaseZ(GraphicalObject go) {
        int index = objects.indexOf(go);
        if(index==0) return;
        objects.remove(go);
        objects.add(index-1, go);
        notifyListeners();
    }

    // Pronađi postoji li u modelu neki objekt koji klik na točku koja je
    // predana kao argument selektira i vrati ga ili vrati null. Točka selektira
    // objekt kojemu je najbliža uz uvjet da ta udaljenost nije veća od
    // SELECTION_PROXIMITY. Status selektiranosti objekta ova metoda NE dira.
    public GraphicalObject findSelectedGraphicalObject(Point mousePoint) {
        double min_distance = 0;
        GraphicalObject selectedObject = null;
        for(GraphicalObject obj: objects) {
            double distance = obj.selectionDistance(mousePoint);
            if(distance <= SELECTION_PROXIMITY && (selectedObject == null || distance < min_distance)) {
                min_distance = distance;
                selectedObject = obj;
            }
        }
        return selectedObject;
    }

    // Pronađi da li u predanom objektu predana točka miša selektira neki hot-point.
    // Točka miša selektira onaj hot-point objekta kojemu je najbliža uz uvjet da ta
    // udaljenost nije veća od SELECTION_PROXIMITY. Vraća se indeks hot-pointa
    // kojeg bi predana točka selektirala ili -1 ako takve nema. Status selekcije
    // se pri tome NE dira.
    public int findSelectedHotPoint(GraphicalObject object, Point mousePoint) {
        double min_distance = 0;
        int selectedHPIndex = -1;
        for(int i = 0; i < object.getNumberOfHotPoints(); ++i) {
            double distance = object.getHotPointDistance(i, mousePoint);
            if(distance <= SELECTION_PROXIMITY && (selectedHPIndex == -1 || distance < min_distance)) {
                min_distance = distance;
                selectedHPIndex = i;
            }
        }
        return selectedHPIndex;
    }

}
