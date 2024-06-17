package hr.fer.ooup.lab4.model;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SVGRendererImpl implements Renderer {

    private List<String> lines = new ArrayList<>();
    private String fileName;

    public SVGRendererImpl(String fileName) {
        this.fileName=fileName;
        lines.add("<svg xmlns=\"http://www.w3.org/2000/svg\">");
    }

    public void close() {
        lines.add("</svg>");
        try(FileWriter fw = new FileWriter(fileName)) {
            for(String line: lines) {
                fw.write(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void drawLine(Point s, Point e) {
        lines.add(String.format("<line x1=\"%d\" y1=\"%d\" x2=\"%d\" y2=\"%d\" style=\"stroke:#0000ff;\" />",
                s.getX(), s.getY(), e.getX(), e.getY()));
    }

    @Override
    public void fillPolygon(Point[] points) {
        StringBuilder sb = new StringBuilder("<polygon points=\"");
        for(int i = 0; i < points.length; ++i) {
            Point p = points[i];
            sb.append(p.getX()).append(",").append(p.getY());
            if(i != points.length-1) {
                sb.append(" ");
            }
        }
        sb.append("\" style=\"stroke:#ff0000; fill:#0000ff;\" />");
        lines.add(sb.toString());
    }

}
