package fita2;

import fita1.Projecte;
import fita1.Tasca;

/**
 * Interfície que implementa el patró Visitor.
 * Aquest patró implementa funcionalitats diferents sense necessitat d'alterar la
 * jerarquia de classes existent. També facilita l'addició de noves funcionalitats,
 * mantenint així la cohesió del codi.*/
public interface Visitor {

  public void visitTask(Tasca tasca, String tag);

  public void visitProject(Projecte project, String tag);

}
