package fita1;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.JSONObject;
import org.json.JSONArray;


/**
 * Classe que hereta les propietats de la classe Activitat.
 * Conté una llista d'intervals de temps en què la tasca ha estat activa per
 * poder calcular-ne eventualment el temps total transcorregut.*/
public class Tasca extends Activitat {

  private List<Interval> intervals = new ArrayList<Interval>();


  private Interval intervalActual;
  static Logger logger = LoggerFactory.getLogger("Fita1.Activitat.Tasca");

  /**
   * Constructor per defecte de la classe.
   * Crida al constructor de la classe base Activitat.*/
  public Tasca() {
    super();
    logger.debug("Tasca creada.");
  }

  /**
   * Constructor de la classe.
   * Crida primer al constructor d'Activitat i després s'afegeix a la llista
   * d'activitats del seu pare.
   *
   * @param nom Nom de la tasca.
   * @param pare Projecte pare de la tasca.*/
  public Tasca(String nom, Projecte pare) {
    super(nom, pare);
    logger.debug("Tasca creada al projecte {}.", pare.getNom());
    pare.getLlistaActivitats().add(this);
  }

  /**
   * Mètode que inicialitza la tasca.
   * Crea un interval i l'assigna a l'atribut intervalActual per poder començar a
   * mesurar el temps transcorregut.*/
  public void startTasca() {
    intervalActual = new Interval();
    intervalActual.setTascaPare(this);
    intervals.add(intervalActual);
    dataHoraInici = intervals.get(0).getHoraInici();
    this.setActive(true);
    this.pare.setActive(true);
    logger.info("Començant tasca " + this.getNom() + ":\n");
    System.out.println("Hola, tasca començada.");
  }

  public void stopTasca() {
    this.setActive(false);
    intervalActual.stop();
    this.pare.updateProjecte();
    logger.info("Finalitzem tasca " + this.getNom() + "\n");
  }

  /**
   * Mètode que calcula la durada total del projecte a través de la suma de tots els seus intervals.
   * Mostra les dades de la tasca actualitzades i si és possible, crida recursivament el mètode
   * updateProjecte() del seu pare.*/
  public void updateTasca() {
    Duration durationTemp = Duration.ZERO;
    for (Interval interval : intervals) {
      durationTemp = durationTemp.plus(interval.getDurada());
    }
    durada = durationTemp;

    int lastIntervalIndex = intervals.size() - 1;

    dataHoraFinal = intervals.get(lastIntervalIndex).getHoraFinal();

    logger.debug("Duració de la tasca {} actualitzada.", this.getNom());

    this.mostrar();

    if (this.getPare() == null) {
      logger.warn("Ha arribat a dalt de l'arbre. No es pot actualitzar un pare inexistent.");
    }

    if (this.getPare() != null) {
      this.getPare().updateProjecte();
    }

  }

  public void mostrarArbre() {
    System.out.println("Tasca: " + this.getNom() + "\n");
  }

  /**
   * Mostra el nom de la tasca, l'hora d'inici, l'hora final i la durada total de la tasca.
   * Si és possible, també mostra les dades de l'interval actual.*/
  public void mostrar() {
    logger.info("Tasca: " + this.getNom()
           + " Hora Inici: " + this.getHoraInici().format(formatejador)
           + " Hora final: " + this.getHoraFinal().format(formatejador)
           + " Durada: " + this.getDurada().plusMillis(200).getSeconds());

    if (intervalActual != null) {
      logger.info(String.format("Interval actual: "
              + intervalActual.getHoraInici().format(formatejador)
              + " " + intervalActual.getHoraFinal().format(formatejador)
              + " Durada: " + intervalActual.getDurada().plusMillis(200).getSeconds()));
    }
  }

  public void searchByTag(String tag) {
    this.acceptSearchByTag().visitTask(this, tag);
  }

  public Activitat findActivityById(int id){
    return this.acceptSearchById().visitTaskId(this, id);
  }

  //JSON
  public JSONObject toJson(int depth) {
    // depth not used here
    JSONObject json = new JSONObject();
    json.put("initialDate", this.dataHoraInici==null
            ? JSONObject.NULL : this.formatejador.format(this.dataHoraInici));
    json.put("finalDate", this.dataHoraFinal==null
            ? JSONObject.NULL : this.formatejador.format(this.dataHoraFinal));
    json.put("class", "tasca");
    super.toJson(json);
    json.put("active", active);
    if (depth > 0) {
      JSONArray jsonIntervals = new JSONArray();
      for (Interval interval : intervals) {
        jsonIntervals.put(interval.toJson());
      }
      json.put("intervals", jsonIntervals);
    } else {
      json.put("intervals", new JSONArray());
    }
    return json;
  }
}
