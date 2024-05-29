package pt.isec.pa.javalife.model.data;


import pt.isec.pa.javalife.model.factory.ElementFactory;
import pt.isec.pa.javalife.model.gameengine.IGameEngine;
import pt.isec.pa.javalife.model.gameengine.IGameEngineEvolve;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Ecossistema implements Serializable, IGameEngineEvolve {
    private final Set<IElemento> elementos = new HashSet<>();
    private Area area;

    // set up inicial do ecossistema (criação e inserção de elementos)
    public Ecossistema() {
        //TODO alterar para meter as coordenadas pelas definições
        // definir as unidades (tipo 1000 pixeis de largura são 2 pixeis por unidade)

        //region criação de pedras
        Area area = new Area(0, 0, 10, 10);
        //preenche a cerca da area com pedras
        Area aux = new Area(0, 0, 0, 0);
        // Adiciona pedras na borda superior e inferior
        for (double i = area.esquerda(); i <= area.direita(); i += 1) {
            // Adiciona pedras na borda superior e inferior
            aux = new Area(i, area.cima(), 1, 1);
            elementos.add(ElementFactory.createElement(Elemento.INANIMADO, aux));
            aux = new Area((int) i, (int) area.baixo(), 1, 1);
            elementos.add(ElementFactory.createElement(Elemento.INANIMADO, aux));
        }

        // Adiciona pedras na borda esquerda e direita(exceto nos cantos)
        for (double j = area.cima() + 1; j < area.baixo(); j += 1) {
            // adiciona pedras na borda esquerda
            aux = new Area(area.esquerda(), j, 1, 1);
            elementos.add(ElementFactory.createElement(Elemento.INANIMADO, aux));
            // adiciona pedras na borda direita
            aux = new Area(j, (int) area.direita(), 1, 1);
            elementos.add(ElementFactory.createElement(Elemento.INANIMADO, aux));
        }

        //cria pedras de varios tamanhos
        int quantidade = 10;
        // Cria um objeto Random para gerar dimensões aleatórias
        java.util.Random random = new java.util.Random();
        for (int i = 0; i < quantidade; i++) {
            // dimensões aleatórias (1x1, 1x2, 2x1 ou 2x2)
            int largura = random.nextInt(2) + 1;
            int altura = random.nextInt(2) + 1;

            // posição aleatória dentro da área especificada
            double x = random.nextDouble() * (area.direita() - 1) + area.esquerda() + 1; //gera valores entre 1 e 10 nao inclusive (para area 0 0 10 10)
            double y = random.nextDouble() * (area.baixo() - 1) + area.cima() + 1;// gera valores entre 1 e 10 nao inclusive (para area 0 0 10 10)

            // verifica se ha alguma elemento na posição gerada
            for (IElemento e : elementos) {
                // se houver um elemento no sitio, ou dentro dos limites deste nao o adiciona e retira 1 ao ciclo
                if (e.getArea().esquerda() <= x && e.getArea().direita() >= x + largura && e.getArea().cima() <= y && e.getArea().baixo() >= y + altura) {
                    i--;
                    System.out.println("x = " + x + "Y =" + y);
                    System.out.println("valor:" + (e.getArea().esquerda() <= x && e.getArea().direita() >= x + largura && e.getArea().cima() <= y && e.getArea().baixo() >= y + altura));
                    break;
                }
            }

            // considero o x e y cima e esquerda como a base do elemento e adiciono a altura e a largura
            elementos.add(ElementFactory.createElement(Elemento.INANIMADO, new Area(x, y, x + altura, y + largura)));
        }

        //endregion


    }


    @Override
    public void evolve(IGameEngine gameEngine, long currentTime) {
        //class memento

        //! TEMPORÁRIO apenas para testes
        for (IElemento e : elementos) {
            System.out.println(e.getArea());
            System.out.println("e.getId()  = " + e.getId() + " e.getType() = " + e.getType());
            //region evoluçao da flora
            if (e instanceof Flora flora) {
                flora.evoluir();
                if (flora.reproduz()) {
                    Area a = verificaAdjacentes(flora);
                    if (a != null) {
                        flora.reproduziu();
                        //!MUDAR ISTO (Adicionar erva)
                        if (flora instanceof Erva) addElemento(new Erva(a));
                    }
                }
                for (int i = 0; i < verificaSobreposicao(e).size(); i++)
                    flora.reduzirForcaSobreposicao();
                if (flora.getForca() <= 0) elementos.remove(flora);
            }
            //endregion

        }
    }

    public void addElemento(IElemento elemento) {
        elementos.add(elemento);
    }

    public Area verificaAdjacentes(IElemento elemento) {
        Area areaA = elemento.getArea();
        double altura = areaA.baixo() - areaA.cima() + 1;
        double largura = areaA.direita() - areaA.esquerda() + 1;

        // Lista para armazenar as áreas adjacentes
        List<Area> areasAdjacentes = new ArrayList<>();

        //region verifica ajacentes
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
        //endregion

        // Verifica se alguma área adjacente está livre
        for (Area adj : areasAdjacentes) {
            if (isAreaLivre(adj)) {
                return adj;
            }
        }

        return null;
    }

    private boolean isAreaLivre(Area area) {
        for (IElemento elemento : elementos) {
            if (elemento.getArea().compareTo(area)) {
                return true;
            }
        }
        return false;
    }

    public List<Elemento> verificaSobreposicao(IElemento elemento) {
        Area area = elemento.getArea();
        List<Elemento> list = new ArrayList<>();
        for (IElemento e : elementos) {
            if (e.getId() == elemento.getId() && e.getType() == elemento.getType()) continue;
            if (elemento.getArea().compareTo(area)) list.add(e.getType());
        }
        return list;
    }


    //* novo a partir daqui


}