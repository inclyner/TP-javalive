package pt.isec.pa.javalive.model.data;


import pt.isec.pa.javalive.model.gameengine.IGameEngine;
import pt.isec.pa.javalive.model.gameengine.IGameEngineEvolve;

import java.io.Serializable;
import java.util.Set;

public class Ecossistema
        implements Serializable, IGameEngineEvolve {
    private Set<IElemento> elementos;

    @Override
    public void evolve(IGameEngine gameEngine, long currentTime) {

    }
//TODO
}