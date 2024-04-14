package pt.isec.pa.javalife.model.data;


import pt.isec.pa.javalife.model.gameengine.IGameEngine;
import pt.isec.pa.javalife.model.gameengine.IGameEngineEvolve;

import java.io.Serializable;
import java.util.Set;

public class Ecossistema implements Serializable, IGameEngineEvolve {
    private Set<IElemento> elementos;


    // set up inicial do ecossistema (criação e inserção de elementos)
    public Ecossistema(){
        //TODO alterar para meter as coordenadas pelas definições
        Area area= new Area(100,1,1,100);
        //preenche a cerca da area com pedras

        // Adiciona pedras na borda superior
        for (double i = area.esquerda(); i <= area.direita(); i += 1) {
            elementos.add(new Pedra((int) i, (int) area.cima()));
        }
        // Adiciona pedras na borda inferior
        for (double i = area.esquerda(); i <= area.direita(); i += 1) {
            elementos.add(new Pedra((int) i, (int) area.baixo()));
        }
        // Adiciona pedras na borda esquerda (exceto nos cantos)
        for (double j = area.baixo() + 1; j < area.cima(); j += 1) {
            elementos.add(new Pedra((int) area.esquerda(), (int) j));
        }
        // Adiciona pedras na borda direita (exceto nos cantos)
        for (double j = area.baixo() + 1; j < area.cima(); j += 1) {
            elementos.add(new Pedra((int) area.direita(), (int) j));
        }




    }
    @Override
    public void evolve(IGameEngine gameEngine, long currentTime) {

    }

    public void addElemento(IElemento elemento) {
        this.elementos.add(elemento);
    }



}