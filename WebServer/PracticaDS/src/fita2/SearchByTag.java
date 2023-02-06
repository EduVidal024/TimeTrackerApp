package fita2;

import fita1.Activitat;
import fita1.Projecte;
import fita1.Tasca;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe que implementa la funcionalitat de buscar quins projectes contenen
 * l'etiqueta sol·licitada. Implementa el patró Visitor per poder accedir als projectes
 * i les tasques i trobar les que coincideixen amb l'etiqueta.*/
public class SearchByTag implements Visitor {
  static Logger logger = LoggerFactory.getLogger("Fita2.Visitor.SearchByTag");

  /**
   * Mètode que "visita" una tasca i recorre la llista d'etiquetes
   * per trobar si coincideix alguna amb la desitjada.*/
  public void visitTask(Tasca tasca, String tag) {


    for (String etiquetaTasca : tasca.getTags()) {
      String lower1 = etiquetaTasca.toLowerCase();
      String lower2 = tag.toLowerCase();

      if (lower1.equals(lower2)) {
        logger.info("Name: " + tasca.getNom() + " Tags: " + tasca.getTags());
      }
    }
  }

  /**
   * Mètode que "visita" un projecte i recorre la llista d'etiquetes per trobar si coincideix
   * alguna amb la desitjada. Com que la classe Projecte pot contenir altres projectes
   * o tasques amb etiquetes diferents, després recorre la llista d'activitats per seguir
   * "visitant-les" per veure si alguna coincideix amb la desitjada.*/
  public void visitProject(Projecte root, String tag) {

    for (String etiquetaProjecte : root.getTags()) {
      String lower1 = etiquetaProjecte.toLowerCase();
      String lower2 = tag.toLowerCase();

      if (lower1.equals(lower2)) {
        logger.info("Name: " + root.getNom() + " Tags: " + root.getTags());
      }
    }

    for (Activitat act : root.getLlistaActivitats()) {
      act.searchByTag(tag);
    }
  }
}
