package hr.fer.oprpp2.webserver.workers;

import hr.fer.oprpp2.webserver.IWebWorker;
import hr.fer.oprpp2.webserver.RequestContext;

import java.util.Set;

/**
 * EchoParams is a web worker which displays all sent parameters as html table.
 */
public class EchoParams implements IWebWorker {
    @Override
    public void processRequest(RequestContext context) throws Exception {
        Set<String> parameterNames = context.getParameterNames();

        context.write("<html><body>");
        context.write("<table>");
        context.write("<thead><tr><th>Name</th><th>Value</th></tr></thead>");
        context.write("<tbody>");
        for(String name:parameterNames) {
            String value = context.getParameter(name);
            context.write("<tr><td>"+name+"</td><td>"+value+"</td></tr>");
        }
        context.write("</tbody>");
        context.write("</table>");
        context.write("</body></html>");
    }
}
