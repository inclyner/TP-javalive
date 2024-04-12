import pt.isec.pa.javalive.TestClient;
import pt.isec.pa.javalive.model.gameengine.GameEngine;
import pt.isec.pa.javalive.model.gameengine.IGameEngine;

public class Main {
    public static void main(String[] args) {

        IGameEngine gameEngine = new GameEngine();
        TestClient client = new TestClient();
        gameEngine.registerClient(client);
        gameEngine.start(500);
        gameEngine.waitForTheEnd();




    }
}