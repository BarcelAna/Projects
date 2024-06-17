package hr.fer.oprpp2.webserver.workers;

import hr.fer.oprpp2.webserver.IWebWorker;
import hr.fer.oprpp2.webserver.RequestContext;

public class SumWorker implements IWebWorker {
    @Override
    public void processRequest(RequestContext context) throws Exception {
        String aValue = context.getParameter("a");
        String bValue = context.getParameter("b");

        int a, b;
        if(aValue == null) {
            a = 1;
        } else {
            try {
                a = Integer.parseInt(aValue);
            } catch(NumberFormatException e) {
                a = 1;
            }
        }
        if(bValue == null) {
            b = 2;
        } else {
            try {
                b = Integer.parseInt(bValue);
            } catch(NumberFormatException e) {
                b = 2;
            }
        }
        int sum = a + b;
        String sumValue = Integer.toString(sum);
        context.setTemporaryParameter("zbroj", sumValue);
        context.setTemporaryParameter("varA", Integer.toString(a));
        context.setTemporaryParameter("varB", Integer.toString(b));
        context.setTemporaryParameter("imgName", sum % 2 == 0 ? "/images/Gitara.png" : "/images/Barca.jpg");

        context.getDispatcher().dispatchRequest("/private/pages/calc.smscr");

    }
}
