package hr.fer.oprpp2.servlets;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartUtils;
import org.jfree.data.general.DefaultPieDataset;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;

@WebServlet(name="chartServlet", urlPatterns = {"/reportImage"})
public class ReportServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        response.setContentType("image/png");

        OutputStream outputStream = response.getOutputStream();

        JFreeChart chart = getChart();
        int width = 500;
        int height = 350;
        ChartUtils.writeChartAsPNG(outputStream, chart, width, height);
    }

    public JFreeChart getChart() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("MacOS", 23.3);
        dataset.setValue("Linux", 32.4);
        dataset.setValue("Windows", 44.2);

        boolean legend = true;
        boolean tooltips = false;
        boolean urls = false;

        JFreeChart chart = ChartFactory.createPieChart("OS", dataset, legend, tooltips, urls);

        chart.setBorderPaint(Color.GREEN);
        chart.setBorderStroke(new BasicStroke(5.0f));
        chart.setBorderVisible(true);

        return chart;
    }
}
