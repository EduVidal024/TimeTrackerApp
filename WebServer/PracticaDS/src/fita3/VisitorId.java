package fita3;

import fita1.Activitat;
import fita1.Projecte;
import fita1.Tasca;

public interface VisitorId {

    public Activitat visitTaskId(Tasca tasca, int identificador);

    public Activitat visitProjectId(Projecte projecte, int identificador);
}
