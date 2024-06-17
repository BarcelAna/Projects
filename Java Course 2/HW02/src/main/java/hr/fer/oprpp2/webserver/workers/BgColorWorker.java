package hr.fer.oprpp2.webserver.workers;

import hr.fer.oprpp2.webserver.IWebWorker;
import hr.fer.oprpp2.webserver.RequestContext;

public class BgColorWorker implements IWebWorker {
    @Override
    public void processRequest(RequestContext context) throws Exception {
        String color = context.getParameter("bgcolor");
        String oldColor = context.getPersistentParameter("bgcolor");
        if(color.trim().length()==6 && isHexNumber(color)) {
            context.setPersistentParameter("bgcolor", color);
            if(!color.equals(oldColor))context.setTemporaryParameter("colorChanged", "The color was updated.");
        }
        context.getDispatcher().dispatchRequest("/private/pages/bgColorChangedMessage.smscr");
    }

    private boolean isHexNumber(String color) {
        try {
            Long.parseLong(color, 16);
        } catch(NumberFormatException e) {
            return false;
        }
        return true;
    }
}
