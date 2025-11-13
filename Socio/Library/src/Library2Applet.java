import javax.swing.JApplet;

import java.awt.*;
import java.net.*;

/**
 * 
 *
 * @version 1.10 11/17/05
 * @author Jeff Dinkins
 */

public class Library2Applet extends JApplet {
    public void init() {
        System.out.println("Инициируем апплет");
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(new Library(this), BorderLayout.CENTER);
    }

    public URL getURL(String filename) {
        URL codeBase = this.getCodeBase();
        URL url = null;
	
        try {
            url = new URL(codeBase, filename);
	    System.out.println(url);
        } catch (java.net.MalformedURLException e) {
            System.out.println("Error: badly specified URL");
            return null;
        }

        return url;
    }


}
