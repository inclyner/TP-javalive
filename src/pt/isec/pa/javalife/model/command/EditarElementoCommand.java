package pt.isec.pa.javalife.model.command;

import pt.isec.pa.javalife.model.data.Area;
import pt.isec.pa.javalife.model.data.Ecossistema;
import pt.isec.pa.javalife.model.data.Elemento;
import pt.isec.pa.javalife.model.data.Fauna;

import java.io.Serializable;

public class EditarElementoCommand  extends AbstractCommand implements ICommand{

    private Ecossistema ecossistema;
    private int id;
    private String tipo;
    private double velocidade, forca;

    public EditarElementoCommand(Ecossistema ecossistema, String tipo,int id,double velocidade, double forca) {
        super(ecossistema);
        this.ecossistema = ecossistema;
        this.tipo = tipo;
        this.id = id;
        this.velocidade = velocidade;
        this.forca = forca;


    }

    @Override
    public boolean execute() {
         return ecossistema.editElemento(tipo,id,velocidade,forca);
    }

    @Override
    public boolean undo() {
        return false;
    }
}


