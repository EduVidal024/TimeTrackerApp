package webserver;

import fita3.ContadorId;
import fita1.Activitat;
import fita1.Projecte;
import fita1.Rellotge;
import fita1.Tasca;

public class MainWebServer {
    public static void main(String[] args) {
        webServer();
    }
    public static void webServer() {
        Activitat root = makeTreeCourses();
        Rellotge.getInstance().start();
        WebServer ws = new WebServer(root);
    }

    public static Activitat makeTreeCourses() {
        Projecte root = new Projecte("root"); //Arrel
        Projecte proj1 = new Projecte("software design", root); //Fill1
        Projecte proj2 = new Projecte("software testing", root); //Fill2
        Projecte proj3 = new Projecte("databases", root); //Fill2
        Activitat tasca1 = new Tasca("Transportation", root); //Fill1

        Projecte proj5 = new Projecte("problems", proj1); //Net 1
        Projecte proj6 = new Projecte("Time Tracker", proj1);

        Activitat tasca2 = new Tasca("first list", proj5); //Fill1
        Activitat tasca3 = new Tasca("second list", proj5);

        Activitat tasca4 = new Tasca("read handout", proj6); //Fill1
        Activitat tasca5 = new Tasca("first milestone", proj6);

        return root;
    }
}
