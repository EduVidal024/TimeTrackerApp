package fita3;

import fita1.Activitat;
import fita1.Projecte;
import fita1.Tasca;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe que implementa la funcionalitat de buscar quins projectes tenen
 * l'id sol·licitada. Implementa el patró Visitor per poder accedir als projectes
 * i les tasques i trobar les que coincideixen amb l'id demanada.*/
public class SearchById implements VisitorId {
    static Logger logger = LoggerFactory.getLogger("Fita2.Visitor.SearchByTag");


    public Activitat visitProjectId(Projecte proj, int id) {
        int i = 0;
        Activitat aux = null;

        if (proj.getId() == id){
            return proj;
        }
        while (i<proj.getLlistaActivitats().toArray().length && aux == null){
            aux = proj.getLlistaActivitats().get(i).findActivityById(id);
            i++;
        }
        if (aux != null){
            return aux;
        }else{
            return null;
        }
    }


    public Activitat visitTaskId(Tasca tasca, int id) {
        if(tasca.getId() == id){
            return tasca;
        }
        return null;
    }
}
