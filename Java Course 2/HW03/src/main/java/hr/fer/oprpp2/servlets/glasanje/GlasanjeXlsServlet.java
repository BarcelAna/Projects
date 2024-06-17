package hr.fer.oprpp2.servlets.glasanje;

import hr.fer.oprpp2.beans.Band;
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

@WebServlet(name="rezultati-xls", urlPatterns = {"/glasanje-xls"})
public class GlasanjeXlsServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
       Map<String, Band> map = (HashMap<String, Band>) req.getServletContext().getAttribute("results");

        res.setContentType("application/vnd.ms-excel");

        writeXLSFile(new ArrayList<>(map.values()), res.getOutputStream());

    }

    private void writeXLSFile(List<Band> bands, ServletOutputStream os) throws IOException {
        HSSFWorkbook wb = new HSSFWorkbook();

        int numberOfRows = bands.size();
        Iterator<Band> it = bands.iterator();
        HSSFSheet sheet = wb.createSheet("results");
        for (int j = 0; j < numberOfRows; ++j) {
            Band b = it.next();
            HSSFRow row = sheet.createRow(j);
            row.createCell(0).setCellValue(b.getName());
            row.createCell(1).setCellValue(b.getVotes());
        }
        wb.write(os);
        os.close();
    }
}
