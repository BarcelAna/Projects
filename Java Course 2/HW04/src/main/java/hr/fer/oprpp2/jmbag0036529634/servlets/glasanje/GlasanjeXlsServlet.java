package hr.fer.oprpp2.jmbag0036529634.servlets.glasanje;

import hr.fer.oprpp2.jmbag0036529634.dao.DAOProvider;
import hr.fer.oprpp2.jmbag0036529634.model.PollOption;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@WebServlet(name="rezultati-xls", urlPatterns = {"/servleti/glasanje-xls"})
public class GlasanjeXlsServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        long pollID = Long.parseLong(req.getParameter("pollID"));
        List<PollOption> pollOptions = DAOProvider.getDao().getPollOptions(pollID);

        res.setContentType("application/vnd.ms-excel");

        writeXLSFile(pollOptions, res.getOutputStream());

    }

    private void writeXLSFile(List<PollOption> options, ServletOutputStream os) throws IOException {
        HSSFWorkbook wb = new HSSFWorkbook();

        int numberOfRows = options.size();
        HSSFSheet sheet = wb.createSheet("results");
        for (int j = 0; j < numberOfRows; ++j) {
            PollOption pollOption = options.get(j);
            HSSFRow row = sheet.createRow(j);
            row.createCell(0).setCellValue(pollOption.getOptionTitle());
            row.createCell(1).setCellValue(pollOption.getVotesCount());
        }
        wb.write(os);
        os.close();
    }
}
