import javafx.application.Application;
import javafx.stage.Stage;
import pt.isec.pa.javalife.model.EcoSistemaFacade.EcosystemFacade;
import pt.isec.pa.javalife.model.data.Ecossistema;
import pt.isec.pa.javalife.model.gameengine.GameEngine;
import pt.isec.pa.javalife.model.gameengine.IGameEngine;
import pt.isec.pa.javalife.ui.gui.res.MainJFX;


public class Main extends Application {
    public static void main(String[] args) {

        IGameEngine gameEngine = new GameEngine();
        //TestClient client = new TestClient();
        EcosystemFacade ecosystemFacade = new EcosystemFacade();
        Ecossistema ecossistema = new Ecossistema();
        //
        gameEngine.registerClient(ecossistema);
        gameEngine.start(1000);
        gameEngine.waitForTheEnd();

        Application.launch(MainJFX.class, args);


    }


    @Override
    public void start(Stage stage) throws Exception {


    }
}