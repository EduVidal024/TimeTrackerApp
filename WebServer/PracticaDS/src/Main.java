import fita1.Activitat;
import fita1.Projecte;
import fita1.Rellotge;
import fita1.Tasca;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
  static Logger logger = LoggerFactory.getLogger("Main");

  public static void main(String[] args) {
    test3();
  }

  public static void test1() {
    Rellotge r = Rellotge.getInstance();
    r.start();

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

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
            // this part is executed when an exception (in this example InterruptedException) occurs
    }
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
            // this part is executed when an exception (in this example InterruptedException) occurs
    }
    r.stop();
    //root.mostrarArbre();
  }

  public static void test2() {
    Rellotge t = Rellotge.getInstance();
    t.start();
    Projecte p0 = new Projecte("root");
    Projecte p1 = new Projecte("software design", p0);
    Tasca t1 = new Tasca("transportation", p0);
    t1.startTasca();
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
            // this part is executed when an exception (in this example InterruptedException) occurs
    }
    t1.stopTasca();
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
            // this part is executed when an exception (in this example InterruptedException) occurs
    }
    Tasca t2  = new Tasca("first list", p1);
    Tasca t3  = new Tasca("second list", p1);
    t.stop();
  }

  public static void test3() {
    Rellotge t = Rellotge.getInstance();
    t.start();
    Projecte p0 = new Projecte("root");
    Tasca t1 = new Tasca("transportation", p0);
    Tasca t2 = new Tasca("first list", p0);
    Tasca t3 = new Tasca("second list", p0);
    t1.startTasca();
    try {
      Thread.sleep(4000);
    } catch (InterruptedException e) {
            // this part is executed when an exception (in this example InterruptedException) occurs
    }
    t1.stopTasca();
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
            // this part is executed when an exception (in this example InterruptedException) occurs
    }
    t2.startTasca();
    try {
      Thread.sleep(6000);
    } catch (InterruptedException e) {
      // this part is executed when an exception (in this example InterruptedException) occurs
    }
    t3.startTasca();
    try {
      Thread.sleep(4000);
    } catch (InterruptedException e) {
            // this part is executed when an exception (in this example InterruptedException) occurs
    }
    t2.stopTasca();
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
            // this part is executed when an exception (in this example InterruptedException) occurs
    }
    t3.stopTasca();
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
            // this part is executed when an exception (in this example InterruptedException) occurs
    }
    t1.startTasca();
    try {
      Thread.sleep(4000);
    } catch (InterruptedException e) {
            // this part is executed when an exception (in this example InterruptedException) occurs
    }
    t1.stopTasca();
    t.stop();

  }

  public static void test_tags() {

    logger.info("Començant test de tags.");
    final Projecte root = new Projecte("root");
    final Projecte P0 = new Projecte("Software design", root);
    final Projecte P1 = new Projecte("Software testing", root);
    final Projecte P2 = new Projecte("Data Base", root);
    final Tasca T0 = new Tasca("Task Transportation", root);
    final Projecte P3 = new Projecte("Problems", P0);
    final Projecte P4 = new Projecte("Time Tracker", P0);
    final Tasca T1 = new Tasca("First List", P3);
    final Tasca T2 = new Tasca("Second List", P3);
    final Tasca T3 = new Tasca("Hand out", P4);
    final Tasca T4 = new Tasca("First Milestone", P4);

    P0.addTags("Java");
    P0.addTags("Flutter");
    P1.addTags("C++");
    P1.addTags("Java");
    P1.addTags("Python");
    P2.addTags("SQL");
    P2.addTags("Python");
    P2.addTags("C++");
    T1.addTags("Java");
    T2.addTags("Dart");
    T4.addTags("Java");
    T4.addTags("intelliJ");

    root.searchByTag("java");
    root.searchByTag("JAVA");
    root.searchByTag("intellij");
    root.searchByTag("c++");
    root.searchByTag("python");

    logger.warn("El programa s'acaba aquí, ojo!");
  }
}