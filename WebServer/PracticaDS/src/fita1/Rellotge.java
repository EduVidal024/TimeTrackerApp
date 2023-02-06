package fita1;

import java.time.LocalDateTime;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Classe que conté el rellotge amb el qual accedim a l'hora actual.
 * Implementa el patró Observable, de tal manera que els objectes que ho necessitin,
 * quan l'hora canviï cada 2 segons aquest els notifiqui del canvi de l'hora.
 * Implementa el patró Singleton per garantir que només hi ha una sola instància de
 * l'objecte al qual els observers accedeixen alhora.*/
public class Rellotge extends Observable {
  private static Rellotge rellotgeIntern = null;
  private Timer timer;
  private LocalDateTime horaActual;

  static Logger logger = LoggerFactory.getLogger("Fita1.Observable.Rellotge");

  /**
   * Constructor privat de la classe.
   * Crea un observable amb cap observer a la llista.
   * Crea un objecte Timer amb el paràmetre isDaemon = true perquè s'executi en segon pla. */
  private Rellotge() {
    super();
    timer = new Timer(true);
    logger.debug("Rellotge creat.");
  }

  /**
   * Mètode estàtic que retorna una única instància del Rellotge, el rellotgeIntern.
   * És el mètode que implementa el patró Singleton per garantir que només existirà
   * una única instància del rellotge.*/
  public static Rellotge getInstance() {
    if (rellotgeIntern == null) {
      rellotgeIntern = new Rellotge();
    }
    return rellotgeIntern;
  }

  /**
   * Tasca de tipus TimerTask que actualitza l'hora a l'hora actual cada cop que és realitzada.
   * Un cop actualitzada, notifica a tots els observers del canvi.*/
  private final TimerTask task = new TimerTask() {
      @Override
    public void run() {
        horaActual = LocalDateTime.now();
        setChanged();
        if (hasChanged()) {
            notifyObservers(horaActual);
            logger.trace("Observers notificats.");
          }
    }
  };

  /**
   * Mètode amb el qual inicialitzem el rellotge.
   * Realitza la TimerTask cada dos segons.*/
  public void start() {
    timer.scheduleAtFixedRate(task, 0, 2000);
  }

  public void stop() {
    timer.cancel();
  }




}
