import pt.isec.pa.javalife.TestClient;
import pt.isec.pa.javalife.model.data.Ecossistema;
import pt.isec.pa.javalife.model.gameengine.GameEngine;
import pt.isec.pa.javalife.model.gameengine.IGameEngine;

public class Main {
    public static void main(String[] args) {

        IGameEngine gameEngine = new GameEngine();
        //TestClient client = new TestClient();
        Ecossistema ecossistema = new Ecossistema();
        gameEngine.registerClient(ecossistema);
        gameEngine.start(1000);
        gameEngine.waitForTheEnd();




    }
}