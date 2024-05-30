import javafx.application.Application;
import javafx.scene.control.Button;
import pt.isec.pa.javalife.model.EcoSistemaFacade.EcossistemaFacade;
import pt.isec.pa.javalife.model.data.Ecossistema;
import pt.isec.pa.javalife.model.gameengine.GameEngine;
import pt.isec.pa.javalife.model.gameengine.IGameEngine;
import pt.isec.pa.javalife.ui.gui.res.MainJFX;


public class Main {

Button button;
    public static void main(String[] args) {


        System.out.println("Hello World!");
        Application.launch(MainJFX.class, args);

    }


}