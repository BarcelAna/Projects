package hr.fer.oprpp2.webserver.workers;

import hr.fer.oprpp2.webserver.IWebWorker;
import hr.fer.oprpp2.webserver.RequestContext;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * CircleWorker is a web worker which, when called, will draw image of circle to the user's output.
 */
public class CircleWorker implements IWebWorker {
    @Override
    public void processRequest(RequestContext context) throws Exception {
        BufferedImage bim = new BufferedImage(200, 200, BufferedImage.TYPE_3BYTE_BGR);

        Graphics2D g2d = bim.createGraphics();
        g2d.fillOval(0, 0, 200, 200);
        g2d.dispose();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try{
            ImageIO.write(bim, "png", bos);
            context.setMimeType("image/png");
            context.write(bos.toByteArray());
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
