package hr.fer.oprpp2.jmbag0036529634.servlets.glasanje;

import hr.fer.oprpp2.jmbag0036529634.dao.DAOProvider;
import hr.fer.oprpp2.jmbag0036529634.model.PollOption;
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
import java.util.*;
import java.util.List;

@WebServlet(name="grafika", urlPatterns = {"/servleti/glasanje-grafika"})
public class GlasanjeGrafikaServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("image/png");

        OutputStream os = res.getOutputStream();

        long pollID = Long.parseLong(req.getParameter("pollID"));
        List<PollOption> pollOptions = DAOProvider.getDao().getPollOptions(pollID);

        JFreeChart chart = getChart(pollOptions);
        int width = 400;
        int height = 400;
        ChartUtils.writeChartAsPNG(os, chart, width, height);
    }

    private JFreeChart getChart(List<PollOption> data) {
        DefaultPieDataset dataset = new DefaultPieDataset<>();
        for(PollOption pOpt : data) {
            dataset.setValue(pOpt.getOptionTitle(), pOpt.getVotesCount());
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
