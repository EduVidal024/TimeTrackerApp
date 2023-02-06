package fita3;

public class ContadorId {

    private int id;

    private static ContadorId contador;

    private static ContadorId contadorInstancies;

    private ContadorId(){
        super();
        contador = this;
        id = 0;
    }

    public synchronized static ContadorId getContador(){
        if(contadorInstancies == null){
            contadorInstancies = new ContadorId();
        }
        return contadorInstancies;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        int idReturn = this.id;
        this.id++;
        return idReturn;
    }
}
