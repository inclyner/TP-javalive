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
    private double direcao, velocidade, forca;

    public EditarElementoCommand(Ecossistema ecossistema, String tipo,int id,double direcao,double velocidade, double forca) {
        super(ecossistema);
        this.ecossistema = ecossistema;
        this.tipo = tipo;
        this.id = id;
        this.direcao = direcao;
        this.velocidade = velocidade;
        this.forca = forca;


    }

    @Override
    public boolean execute() {
        System.out.println("EDITAR ELEMENTO: " + tipo + " " + id + " " + direcao + " " + velocidade + " " + forca);
         return ecossistema.editElemento(tipo,id,direcao,velocidade,forca);
    }

    @Override
    public boolean undo() {
        return false;
    }
}


