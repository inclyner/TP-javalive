import pt.isec.pa.javalife.model.EcoSistemaFacade.EcosystemFacade;
import pt.isec.pa.javalife.model.data.Ecossistema;
import pt.isec.pa.javalife.model.gameengine.GameEngine;
import pt.isec.pa.javalife.model.gameengine.IGameEngine;

public class Main {
    public static void main(String[] args) {

        IGameEngine gameEngine = new GameEngine();
        //TestClient client = new TestClient();
        EcosystemFacade ecosystemFacade = new EcosystemFacade();
        Ecossistema ecossistema = new Ecossistema();
        //
        gameEngine.registerClient(ecossistema);
        gameEngine.start(1000);
        gameEngine.waitForTheEnd();


    }
}