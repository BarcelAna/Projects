package hr.fer.oprpp2.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import  java.io.*;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFRow;

@WebServlet(name="powers", urlPatterns = {"/powers"})
public class PowersServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String aStr = req.getParameter("a");
        String bStr = req.getParameter("b");
        String nStr = req.getParameter("n");

        int a = 0, b = 0, n = 0;
        try {
            a = Integer.parseInt(aStr);
            b = Integer.parseInt(bStr);
            n = Integer.parseInt(nStr);

            if(a < -100 || a > 100) {
                throw new NumberFormatException();
            }
            if(b < -100 || b > 100) {
                throw new NumberFormatException();
            }
            if(n < 1 || n > 5) {
                throw new NumberFormatException();
            }
        } catch(NumberFormatException e) {
            req.getRequestDispatcher("/WEB-INF/pages/paramError.jsp").forward(req,res);
        }

        res.setContentType("application/vnd.ms-excel");
        writeXLSFile(a, b, n, res.getOutputStream());
    }

    private void writeXLSFile(int a, int b, int numberOfPages, OutputStream os) throws IOException {
        HSSFWorkbook wb = new HSSFWorkbook();

        int numberOfRows = b - a + 1;
        for(int i = 1; i <= numberOfPages; ++i) {
            HSSFSheet sheet = wb.createSheet("sheet " + i);
            for (int j = 0; j < numberOfRows; ++j) {
                HSSFRow row = sheet.createRow(j);
                row.createCell(0).setCellValue(a + j);
                row.createCell(1).setCellValue(Math.pow(a + j, i));
            }
        }
        wb.write(os);
        os.close();
    }
}
