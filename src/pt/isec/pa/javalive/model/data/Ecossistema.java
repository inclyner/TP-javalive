package pt.isec.pa.javalive.model.data;


import pt.isec.pa.javalive.model.gameengine.IGameEngine;
import pt.isec.pa.javalive.model.gameengine.IGameEngineEvolve;

import java.io.Serializable;
import java.util.Set;

public class Ecossistema implements Serializable, IGameEngineEvolve {
    private Set<IElemento> elementos;


    // set up inicial do ecossistema (criação e inserção de elementos)
    public Ecossistema(){


    }
    @Override
    public void evolve(IGameEngine gameEngine, long currentTime) {

    }

    public void addElemento(IElemento elemento) {
        this.elementos.add(elemento);
    }



}