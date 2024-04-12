package pt.isec.pa.javalive;

import pt.isec.pa.javalive.model.gameengine.IGameEngine;
import pt.isec.pa.javalive.model.gameengine.IGameEngineEvolve;

public class TestClient implements IGameEngineEvolve {
    int count = 0;
    @Override
    public void evolve(IGameEngine gameEngine, long currentTime) {
        System.out.printf("[%d] %d\n",currentTime,++count);
        if (count >= 20) gameEngine.stop();
    }
}