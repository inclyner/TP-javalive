package pt.isec.pa.javalife.model.data;


import pt.isec.pa.javalife.model.gameengine.IGameEngine;
import pt.isec.pa.javalife.model.gameengine.IGameEngineEvolve;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Ecossistema implements Serializable, IGameEngineEvolve {
    private Set<IElemento> elementos = null;


    // set up inicial do ecossistema (criação e inserção de elementos)
    public Ecossistema(){
        //TODO alterar para meter as coordenadas pelas definições
        Area area= new Area(0,0,100,100);
        //preenche a cerca da area com pedras

        // Adiciona pedras na borda superior e inferior
        for (double i = area.esquerda(); i <= area.direita(); i += 1) {
            elementos.add(new Pedra((int) i, (int) area.cima(), 1, 1));
            elementos.add(new Pedra((int) i, (int) area.baixo(), 1, 1));
        }

        // Adiciona pedras na borda esquerda e direita(exceto nos cantos)
        for (double j = area.cima() + 1; j < area.baixo(); j += 1) {
            elementos.add(new Pedra((int) area.esquerda(), (int) j,1,1));
            elementos.add(new Pedra((int) j, (int) area.direita(), 1, 1));
        }
    }

    @Override
    public void evolve(IGameEngine gameEngine, long currentTime) {

    }

    public void addElemento(IElemento elemento) {
        this.elementos.add(elemento);
    }



}