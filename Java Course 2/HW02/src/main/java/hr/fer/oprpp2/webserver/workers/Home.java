package hr.fer.oprpp2.webserver.workers;

import hr.fer.oprpp2.webserver.IWebWorker;
import hr.fer.oprpp2.webserver.RequestContext;

/**
 * Home is a web worker which reads bgcolor parameter from persistent parameters map if such exists and dispatches request to the home.smscr script.
 */
public class Home implements IWebWorker {
    @Override
    public void processRequest(RequestContext context) throws Exception {
        String value = context.getPersistentParameter("bgcolor");
        if(value!=null) {
            context.setTemporaryParameter("background", value);
        } else {
            context.setTemporaryParameter("background", "7F7F7F"); //siva
        }

        context.getDispatcher().dispatchRequest("/private/pages/home.smscr");
    }
}
