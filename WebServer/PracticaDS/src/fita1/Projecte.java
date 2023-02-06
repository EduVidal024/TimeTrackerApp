package fita1;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.JSONObject;
import org.json.JSONArray;


/**
 * Classe que hereta les propietats de la classe Activitat.
 * Conté una llista d'activitats genèriques, que poden ser tant projectes com tasques,
 * implementant així també el patró Composite.
 * La llista d'activitats serveix per poder crear un arbre de les dependències entre
 * projectes i tasques.
 * */
public class Projecte extends Activitat {
  private List<Activitat> llistaActivitats = new ArrayList<Activitat>();


  static Logger logger = LoggerFactory.getLogger("Fita1.Activitat.Projecte");

  /**
   * Constructor per defecte de la classe Projecte.
   * Crida al constructor per defecte de la classe base Activitat*/
  public Projecte() {
    super();
    id = 0;
  }

  /**
   * Constructor per paràmetres de la classe Projecte.
   * Crida al constructor de la classe base Activitat passant-li el nom com a paràmetre.
   *
   * @param nom Nom del projecte
   * */
  public Projecte(String nom) {
    super(nom);
    logger.debug("Projecte '{}' creat.", nom);
  }

  /**
   * Constructor per paràmetres de la classe Projecte.
   * Crida al constructor de la classe base Activitat i seguidament s'afegeix a si mateix a la
   * llista d'activitats del seu pare.
   *
   * @param nom Nom del projecte
   * @param pare Projecte pare
   * */
  public Projecte(String nom, Projecte pare) {
    super(nom, pare);
    logger.debug("Projecte '{}' creat a dins del projecte '{}'", nom, pare.getNom());
    pare.llistaActivitats.add(this);
  }

  public List<Activitat> getLlistaActivitats() {
    return llistaActivitats;
  }

  /**
   * Mostra el nom del projecte i crida recursivament el mateix mètode a cadascuna de
   * les activitats que conté per mostrar l'arbre de dependències.*/
  public void mostrarArbre() {
    logger.info("Projecte: " + this.getNom() + "\n");


    if (llistaActivitats != null) {
      for (Activitat act : llistaActivitats) {
        act.mostrarArbre();
      }

    }

  }

  /**
   * Mostra el nom, l'hora d'inici, l'hora final i la durada del projecte.
   * Si és possible, crida el mateix mètode al pare de forma recursiva.*/
  public void mostrar() {
    logger.info("Projecte: " + this.getNom() + " Hora inici: "
        + this.getHoraInici().format(formatejador) + " Hora final: "
        + this.getHoraFinal().format(formatejador) + " Durada: "
        + this.getDurada().plusMillis(300).getSeconds() + "\n");

    if (this.getPare() != null) {
      this.getPare().mostrar();
    }
  }

  /**
   * Mètode que calcula la durada total del projecte a través de la suma de tots els seus intervals.
   * Mostra les dades del projecte actualitzades i si és possible, crida recursivament el mètode
   * al seu pare.*/
  public void updateProjecte() {
    boolean estaActiu = false;
    for(Activitat act : llistaActivitats)
    {
      if(act.active)
      {
        estaActiu = true;
        break;
      }
    }

    this.setActive(estaActiu);

    Duration duracioTemporal = Duration.ZERO;
    for (Activitat act : llistaActivitats) {
      duracioTemporal = duracioTemporal.plus(act.getDurada());
    }

    LocalDateTime temp = llistaActivitats.get(0).getHoraFinal();

    dataHoraFinal = temp;

    for (int i = 0; i < llistaActivitats.size(); i++) {
      if (temp.isBefore(llistaActivitats.get(i).getHoraFinal())) {
        dataHoraFinal = llistaActivitats.get(i).getHoraFinal();
      }
    }

    durada = duracioTemporal;
    logger.debug("Duració del projecte {} actualitzada.", this.getNom());
    this.mostrar();

    if (this.getPare() == null) {
      logger.warn("Ha arribat a dalt de l'arbre. No es pot actualitzar un pare inexistent.");
    }

    if (this.getPare() != null) {
      this.getPare().updateProjecte();
    }
  }


  public void searchByTag(String tag) {
    this.acceptSearchByTag().visitProject(this, tag);
  }
  public Activitat findActivityById(int id){
     return this.acceptSearchById().visitProjectId(this, id);
  }
  //JSON

  public JSONObject toJson(int depth) {
    JSONObject json = new JSONObject();
    json.put("initialDate", this.dataHoraInici==null
            ? JSONObject.NULL : this.formatejador.format(this.dataHoraInici));
    json.put("finalDate", this.dataHoraFinal==null
            ? JSONObject.NULL : this.formatejador.format(this.dataHoraFinal));
    json.put("class", "project");
    json.put("active", this.active);
    super.toJson(json);

    if (depth > 0) {
      JSONArray jsonActivities = new JSONArray();
      for (Activitat activity : llistaActivitats) {
        jsonActivities.put(activity.toJson(depth - 1));
        // important: decrement depth
      }
      json.put("activities", jsonActivities);
    }
    return json;
  }

}
