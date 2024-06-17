package hr.fer.oprpp2.servlets.glasanje;

import hr.fer.oprpp2.beans.Band;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@WebServlet(name="grafika", urlPatterns = {"/glasanje-grafika"})
public class GlasanjeGrafikaServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("image/png");

        OutputStream os = res.getOutputStream();

        Map<String, Band> map = (HashMap<String, Band>) req.getServletContext().getAttribute("results");
        if(map==null) System.out.println("Null je");

        JFreeChart chart = getChart(new HashSet<>(map.values()));
        int width = 400;
        int height = 400;
        ChartUtils.writeChartAsPNG(os, chart, width, height);
    }

    private JFreeChart getChart(Set<Band> data) {
        DefaultPieDataset dataset = new DefaultPieDataset<>();
        for(Band b : data) {
            dataset.setValue(b.getName(), b.getVotes());
        }
        boolean legend = true;
        boolean tooltips = false;
        boolean urls = false;

        JFreeChart chart = ChartFactory.createPieChart("Rezultati", dataset, legend, tooltips, urls);
        chart.setBorderPaint(Color.red);
        chart.setBorderStroke(new BasicStroke(5.0f));
        chart.setBorderVisible(true);

        return chart;
    }
}
