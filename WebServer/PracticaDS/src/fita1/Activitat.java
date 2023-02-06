package fita1;

import fita2.SearchByTag;
import fita3.ContadorId;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import fita3.SearchById;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.JSONObject;


/**
* Classe base de Projecte i Tasca.
* Conté tots els atributs comuns entre ambdues classes i els mètodes compartits.
* Implementem el patró Composite per permetre que un projecte contingui elements tant
* de la classe Projecte com de Tasca.
* */
public abstract class Activitat {
  protected static final DateTimeFormatter formatejador = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  protected boolean active;

  protected int id;
  protected Duration durada;
  protected LocalDateTime dataHoraInici;
  protected LocalDateTime dataHoraFinal;
  protected String nom;

  protected Projecte pare;

  public ArrayList<String> tags = new ArrayList<>();

  private final SearchByTag mySearch = new SearchByTag();

  private final SearchById IdSearch = new SearchById();

  static Logger logger = LoggerFactory.getLogger("Fita1.Activitat");

  /**
   * Constructor per defecte de la classe.
   * Assigna els valors null al nom i al pare i l'hora actual a la inicial i a la final.
   * La duració en el moment de la creació és zero.*/
  public Activitat() {
    this.nom = null;
    this.dataHoraInici = LocalDateTime.now(); // Funció que retorna la hora actual.
    this.dataHoraFinal = LocalDateTime.now();
    this.durada = Duration.ZERO;
    this.pare = null;
    this.id = this.assignId();
  }

  /**
   * Constructor per paràmetre de la classe.
   * Assigna els mateixos valors que el constructor per defecte, però assigna el nom
   * passat per paràmetre.
   *
   * @param nom Nom de l'activitat*/
  public Activitat(String nom) {
    this.nom = nom;
    this.dataHoraInici = LocalDateTime.now();
    this.dataHoraFinal = LocalDateTime.now();
    this.durada = Duration.ZERO;
    this.pare = null;
    this.id = this.assignId();
  }

  /**
   * Constructor per paràmetres de la classe.
   * Assigna els mateixos valors que el constructor per defecte, però assigna el nom
   * i el pare passats per paràmetre.
   *
   * @param nom Nom de l'activitat
   * @param pare Projecte pare de l'activitat
   * */
  public Activitat(String nom, Projecte pare) {
    this.nom = nom;
    this.dataHoraInici = LocalDateTime.now();
    this.dataHoraFinal = LocalDateTime.now();
    this.durada = Duration.ZERO;
    this.pare = pare;
    this.id = this.assignId();
  }

  public Duration getDurada() {
    return durada;
  }

  public String getNom() {
    return nom;
  }

  public LocalDateTime getHoraInici() {
    return dataHoraInici;
  }

  public LocalDateTime getHoraFinal() {
    return dataHoraFinal;
  }

  public Projecte getPare() {
    return pare;
  }

  public int getId() { return id; }

  public void setActive(boolean active) {
    this.active = active;
  }

  public void mostrar(){

  }

  public void mostrarArbre(){

  }

  public ArrayList<String> getTags() {
    return this.tags;
  }


  public void addTags(String tag) {
    this.tags.add(tag);
    logger.trace("Tag '{}' afegit a l'activitat '{}'", tag, this.getNom());
  }

  public SearchByTag acceptSearchByTag() {
    return this.mySearch;
  }

  public SearchById acceptSearchById(){return this.IdSearch;}

  public void searchByTag(String tag){

  }
  public abstract Activitat findActivityById(int id);
  //JSON

  public abstract JSONObject toJson(int depth); // added 16-dec-2022

  protected void toJson(JSONObject json) {
    json.put("id", this.id);
    json.put("name", this.nom);

    json.put("duration", this.durada.toSeconds());
  }

  public int assignId(){
    ContadorId contador = ContadorId.getContador();
    contador.setId(contador.getId());
    return contador.getId();
  }

}
