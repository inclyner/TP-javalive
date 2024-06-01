package pt.isec.pa.javalife.model.command;

import pt.isec.pa.javalife.model.data.Area;
import pt.isec.pa.javalife.model.data.Ecossistema;
import pt.isec.pa.javalife.model.data.Elemento;
import pt.isec.pa.javalife.model.data.Fauna;

public class EditarElementoCommand  extends AbstractCommand implements ICommand{

    Ecossistema ecossistema;
int id;
String tipo;
    double direcao, velocidade, forca;

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

         return ecossistema.editElemento(tipo,id,direcao,velocidade,forca);
    }

    @Override
    public boolean undo() {
        return false;
    }
}


