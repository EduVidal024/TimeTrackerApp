package fita1;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Observable;
import java.util.Observer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.JSONObject;
import org.json.JSONArray;



/**
 * Classe que serveix per poder guardar un interval de temps concret.
 * Implementa el patró Observer. Cada 2 segons rebrà una notificació de l'Observer amb l'hora actual
 * i actualitzarà la seva hora final amb aquesta.
 * */
public class Interval implements Observer {
  // Atributs
  protected DateTimeFormatter formatejador = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
  protected Duration durada;
  protected int identificador;
  protected LocalDateTime dataHoraInici;
  protected LocalDateTime dataHoraFinal;
  private Tasca tascaPare;

  protected boolean active = false;
  static Logger logger = LoggerFactory.getLogger("Fita1.Observer.Interval");

  /**
   * Constructor per defecte de la classe.
   * Quan es crea, l'afegeix a la llista d'Observers del rellotge.
   * Inicialitza els paràmetres d'hora d'inici i hora final amb el valor de l'hora actual.
   * Inicialitza la durada a zero.*/
  public Interval() {
    Rellotge.getInstance().addObserver(this);
    dataHoraInici = LocalDateTime.now();
    dataHoraFinal = dataHoraInici;
    durada = Duration.ZERO;
    logger.trace("Interval creat.");
    active = true;
  }

  public void setTascaPare(Tasca tasca) {
    tascaPare = tasca;
  }

  public Duration getDurada() {
    durada = Duration.between(dataHoraInici, dataHoraFinal);
    return durada;
  }

  public LocalDateTime getHoraInici() {
    return dataHoraInici;
  }

  public LocalDateTime getHoraFinal() {
    return dataHoraFinal;
  }

  /**
   * Mètode cridat quan volem finalitzar l'interval.
   * Calcula la durada de l'interval entre la data d'inici i la final i elimina l'interval
   * de la llista d'Observers.*/
  public void stop() {
    durada = Duration.between(dataHoraInici, dataHoraFinal);
    active = false;
    Rellotge.getInstance().deleteObserver(this);
    logger.trace("Interval aturat.");
  }


  /**
   * Mètode que es crida sol cada cop que l'observable notifica canvis en el seu estat.
   * En aquest cas, modifica la data final de l'interval amb l'hora actual notificada
   * per l'observable.*/
  @Override
  public void update(Observable obs, Object ob) {
    dataHoraFinal = (LocalDateTime) ob;
    tascaPare.updateTasca();
    logger.trace("Interval actualitzat.");
  }

  public JSONObject toJson() {
    JSONObject json = new JSONObject();

    json.put("class", "interval");
    json.put("id", identificador);
    json.put("initialDate", dataHoraInici==null
            ? JSONObject.NULL : formatejador.format(dataHoraInici));
    json.put("finalDate", dataHoraFinal==null
            ? JSONObject.NULL : formatejador.format(dataHoraFinal));
    json.put("duration", durada.toSeconds());
    json.put("active", active);
    return json;
  }

}
