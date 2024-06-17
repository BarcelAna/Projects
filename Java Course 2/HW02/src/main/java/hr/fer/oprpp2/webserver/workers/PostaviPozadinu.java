package hr.fer.oprpp2.webserver.workers;

import hr.fer.oprpp2.webserver.IWebWorker;
import hr.fer.oprpp2.webserver.RequestContext;

public class PostaviPozadinu implements IWebWorker {
    @Override
    public void processRequest(RequestContext context) throws Exception {
        String url = "/images/"+context.getParameter("url");
        context.setPersistentParameter("bgImage", url);
        context.setTemporaryParameter("bgUrl", url);
        context.getDispatcher().dispatchRequest("/private/pages/vrijeme.smscr");
    }
}
