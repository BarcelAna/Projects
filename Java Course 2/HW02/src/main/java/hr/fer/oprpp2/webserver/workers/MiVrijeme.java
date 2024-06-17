package hr.fer.oprpp2.webserver.workers;

import hr.fer.oprpp2.webserver.IWebWorker;
import hr.fer.oprpp2.webserver.RequestContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class MiVrijeme implements IWebWorker {
    @Override
    public void processRequest(RequestContext context) throws Exception {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime currentTime = LocalDateTime.now();
        context.setTemporaryParameter("currentTime", dtf.format(currentTime));
        String[] images = new String[] {"Barca.jpg", "Gitara.png"};
        if(context.getPersistentParameter("bgImage")==null) {
            int randomInd = new Random().nextInt(2);
            context.setPersistentParameter("bgUrl", images[randomInd]);
        }
        context.getDispatcher().dispatchRequest("/private/pages/vrijeme.smscr");
    }
}
