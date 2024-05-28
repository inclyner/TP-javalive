package pt.isec.pa.javalife.model.data;


import pt.isec.pa.javalife.model.gameengine.IGameEngine;
import pt.isec.pa.javalife.model.gameengine.IGameEngineEvolve;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Ecossistema implements Serializable, IGameEngineEvolve {
    private Set<IElemento> elementos= new HashSet<>();
    private Area area;

    // set up inicial do ecossistema (criação e inserção de elementos)
    public Ecossistema(){
        //TODO alterar para meter as coordenadas pelas definições
        area= new Area(0,0,10,10);
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

        //cria pedras de varios tamanhos
        int quantidade= 10;
        // Cria um objeto Random para gerar dimensões aleatórias
        java.util.Random random = new java.util.Random();
        for (int i = 0; i < quantidade; i++) {
            // dimensões aleatórias (1x1, 1x2, 2x1 ou 2x2)
            int largura = random.nextInt(2) + 1;
            int altura = random.nextInt(2) + 1;

            // posição aleatória dentro da área especificada
            double x = random.nextDouble() * (area.direita()-1)+area.esquerda()+1; //gera valores entre 1 e 10 nao inclusive (para area 0 0 10 10)
            double y = random.nextDouble()* (area.baixo()-1)+ area.cima() + 1;// gera valores entre 1 e 10 nao inclusive (para area 0 0 10 10)

            // verifica se ha alguma elemento na posição gerada
            for (IElemento e: elementos) {
                if(e.getArea().esquerda()<= x && e.getArea().direita()>=x+ largura && e.getArea().cima() <= y && e.getArea().baixo() >= y+ altura) { // se houver um elemento no sitio, ou dentro dos limites deste nao o adiciona e retira 1 ao ciclo
                    i--;
                    System.out.println("x = " + x + "Y =" +y);
                    System.out.println("valor:" + (e.getArea().esquerda()<= x && e.getArea().direita()>=x+ largura && e.getArea().cima() <= y && e.getArea().baixo() >= y+ altura));
                    break;
                }
            }

            // considero o x e y cima e esquerda como a base do elemento e adiciono a altura e a largura
            elementos.add(new Pedra(x, y, x+ altura, y+ largura));
        }




    }

    @Override
    public void evolve(IGameEngine gameEngine, long currentTime) {

        //! TEMPORÁRIO apenas para testes
        for (IElemento e: elementos) {
            System.out.println(e.getArea());
            System.out.println("e.getId()  = " + e.getId() + " e.getType() = " + e.getType());
            if(e instanceof Flora flora) {
                flora.evoluir();
                if(flora.reproduz()){
                    if(verificaAdjacentes(flora)!=null)
                        flora.reproduziu();
                }

            }
        }
    }

    public void addElemento(IElemento elemento) {
        elementos.add(elemento);
    }

    public Area verificaAdjacentes(IElemento elemento){
        Area areaA = elemento.getArea();
        double altura = areaA.baixo() - areaA.cima() + 1;
        double largura = areaA.direita() - areaA.esquerda() + 1;

        // Lista para armazenar as áreas adjacentes
        List<Area> areasAdjacentes = new ArrayList<>();

        // Adiciona áreas adjacentes
        if (areaA.cima() - altura >= 0) {
            areasAdjacentes.add(new Area(areaA.cima() - altura, areaA.esquerda(), areaA.cima(), areaA.direita()));
        }
        if (areaA.baixo() + altura <= area.baixo()) {
            areasAdjacentes.add(new Area(areaA.baixo(), areaA.esquerda(), areaA.baixo() + altura, areaA.direita()));
        }
        if (areaA.esquerda() - largura >= 0) {
            areasAdjacentes.add(new Area(areaA.cima(), areaA.esquerda() - largura, areaA.baixo(), areaA.esquerda()));
        }
        if (areaA.direita() + largura <= area.direita()) {
            areasAdjacentes.add(new Area(areaA.cima(), areaA.direita(), areaA.baixo(), areaA.direita() + largura));
        }

        // Verifica se alguma área adjacente está livre
        for (Area adj : areasAdjacentes) {
            if (isAreaLivre(adj)) {
                return adj;
            }
        }

        return null;
    }
    private boolean isAreaLivre(Area area){
        for(IElemento elemento : elementos){
            if(elemento.getArea().compareTo(area)){
                return true;
            }
        }
        return false;
    }
/*
    public boolean verificaSobreposicao(IElemento elemento){
        Area areaA = elemento.getArea();
        for(IElemento elemento : elementos){
            if(elemento instanceof Fauna fauna){
                fauna.getArea();
            }
        }

    }
*/
}