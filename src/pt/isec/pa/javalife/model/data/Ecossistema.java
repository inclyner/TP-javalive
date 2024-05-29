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
    private boolean contemPedra = false;
    private boolean oneTime = true;

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
            elementos.add(ElementFactory.createElement(Elemento.INANIMADO, new Area(x, y,x+ altura, y+largura)));
        }

        //endregion


    }

    @Override
    public void evolve(IGameEngine gameEngine, long currentTime) {
        verificarElementoMorre();
        for(IElemento elemento : elementos) {
            if(elemento instanceof Flora flora)
                evolveFlora(flora);
            else if(elemento instanceof Fauna fauna)
                evolveFauna(fauna);
        }
    }

    private void evolveFlora(Flora flora){
        //Aumenta a força e verifica se pode reproduzir, caso sim aumenta o numero de reproducao e verifica se tem espaço para o fazer
        //caso tenha adiciona elemento
        if(flora.evoluir()){
            Area a = verificaAdjacentes(flora);
            if (a != null) {
                flora.reproduziu();
                if (flora instanceof Erva)
                    addElemento(Elemento.FLORA, a);
            }
        }
        //verifica se tem sobreposiçao e reduz a forca caso tenha
        for (int i = 0; i < verificaSobreposicao(flora); i++)
            flora.reduzirForcaSobreposicao();
    }

    private void evolveFauna(Fauna fauna){
        Area a;
        boolean mov = true;
        //region verifica sobreposiçoes
        for (int i = 0; i < verificaSobreposicao(fauna); i++){
            if(fauna.getForca()<100){
                mov = false;
                fauna.setForca(fauna.getForca() + Flora.getForcaSobreposicao());
            }
        }
        //endregion
        if(mov){
            if (fauna.getState() == Fauna.FaunaState.PROCURA_COMIDA) {
                if(existeFlora()){
                    //region procura elemento flora mais perto a tualiza area e forca
                    do {
                        a = fauna.moverParaComida(checkElementoMaisPerto(fauna.getArea()), contemPedra);
                        contemPedra = isLivrePedraFauna(a);
                    } while (!contemPedra);
                    fauna.setArea(a);
                    fauna.setForca(fauna.getForca() - fauna.getForcaMovimentacao());
                    //endregion
                }else {
                    if (oneTime || !elementos.contains(fauna.getElemetoPerseguir())){
                        procuraFaunaComMenosForca(fauna);
                        oneTime = false;
                    }
                    //region perseguir elemento da fauna e atualizar area e forca
                    do{
                        for (IElemento aux : elementos) {
                            if (fauna.getElemetoPerseguir().getId() == aux.getId() && Elemento.FAUNA == aux.getType()) {
                                fauna.setElemetoPerseguir(aux);
                            }
                        }
                        a = fauna.moverParaComida(fauna.getElemetoPerseguir().getArea(), contemPedra);
                        contemPedra = isLivrePedraFauna(a);
                    } while (!contemPedra);
                    fauna.setArea(a);
                    fauna.setForca(fauna.getForca() - fauna.getForcaMovimentacao());
                    //endregion
                    //region verifica adjacentes e atualiza forca caso seja adjacente
                    if(fauna.verificarAdjacente()){
                        for (IElemento aux : elementos) {
                            if (fauna.getElemetoPerseguir().getId() == aux.getId() && Elemento.FAUNA == aux.getType()) {
                                if (aux instanceof Fauna f) {
                                    if(fauna.getForca()>10) {
                                        fauna.setForca(fauna.getForca() + f.getForca() - 10);
                                        f.setForca(0);
                                    }else if (fauna.getForca() < 10){
                                        f.setForca(fauna.getForca() + f.getForca());
                                        fauna.setForca(0);
                                    }else  if (fauna.getState() == Fauna.FaunaState.PROCURA_COMIDA && f.getState() == Fauna.FaunaState.PROCURA_COMIDA){
                                        if(fauna.getForca()>f.getForca()){
                                            fauna.setForca(fauna.getForca() - 10 + f.getForca() - 10);
                                            f.setForca(0);
                                        }else if (fauna.getForca() < f.getForca()){
                                            f.setForca(f.getForca() - 10 + fauna.getForca() -10);
                                            fauna.setForca(0);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    //endregion
                }
            }else if(fauna.getState() == Fauna.FaunaState.NAO_PROCURA_COMIDA){
                IElemento aux;
                if(fauna.getForca()>50){
                    aux = procuraFaunaComMaisForca(fauna);
                    if(aux != null){
                        do{
                            a = fauna.moverParaComida(aux.getArea(), contemPedra);
                            contemPedra = isLivrePedraFauna(a);
                        } while (!contemPedra);
                        fauna.setArea(a);
                        fauna.setForca(fauna.getForca() - fauna.getForcaMovimentacao());
                    }
                    if(fauna.reproducao(verificarFaunaDistancia(fauna))){

                    }
                }
            }
        }
    }

    public void addElemento(Elemento elemento, Area aux) {
        elementos.add(ElementFactory.createElement(elemento, aux));
    }

    public void removeElemento(IElemento elemento) {
        elementos.remove(elemento);
    }

    public void verificarElementoMorre(){
        for(IElemento elemento : elementos){
            if(elemento instanceof Fauna fauna){
                if(fauna.getForca()<=0)
                    removeElemento(fauna);
            }
            else if (elemento instanceof Flora flora){
                if(flora.getForca()<=0)
                    removeElemento(flora);
            }
        }
    }

    private boolean isAreaLivre(Area area) {
        for (IElemento elemento : elementos) {
            if (elemento.getArea().compareTo(area)) {
                return false;
            }
        }
        return true;
    }

    public Area verificaAdjacentes(IElemento elemento) {
        Area areaA = elemento.getArea();
        double altura = areaA.baixo() - areaA.cima() + 1;
        double largura = areaA.direita() - areaA.esquerda() + 1;

        // Lista para armazenar as áreas adjacentes
        List<Area> areasAdjacentes = new ArrayList<>();

        //region verifica ajacentes
        if (areaA.cima() - altura >= 0) {
            areasAdjacentes.add(new Area(areaA.cima() - altura, areaA.esquerda(), areaA.baixo() - altura, areaA.direita()));
        }
        if (areaA.baixo() + altura <= area.baixo()) {
            areasAdjacentes.add(new Area(areaA.cima() + altura, areaA.esquerda(), areaA.baixo() + altura, areaA.direita()));
        }
        if (areaA.esquerda() - largura >= 0) {
            areasAdjacentes.add(new Area(areaA.cima(), areaA.esquerda() - largura, areaA.baixo(), areaA.direita() - largura));
        }
        if (areaA.direita() + largura <= area.direita()) {
            areasAdjacentes.add(new Area(areaA.cima(), areaA.esquerda() + largura, areaA.baixo(), areaA.direita() + largura));
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

    private boolean isLivrePedraFauna(Area area) {
        for (IElemento elemento : elementos) {
            if (elemento.getArea().compareTo(area) && (elemento.getType()==Elemento.INANIMADO || elemento.getType()==Elemento.FAUNA)){
                return false;
            }
        }
        return true;
    }

    private Area checkElementoMaisPerto(Area a){
        Area last = null;
        double dist = Double.MAX_VALUE;
        for(IElemento e : elementos){
            if(e.getType() == Elemento.FLORA){
                double dx = a.esquerda() - e.getArea().esquerda();
                double dy = a.cima() - e.getArea().cima();
                double res = Math.sqrt(dx * dx + dy * dy);
                if (res < dist){
                    dist = res;
                    last = e.getArea();
                }
            }
        }
        return last;
    }

    private boolean existeFlora(){
        for (IElemento e : elementos) {
            if(e.getType()==Elemento.FLORA){
            return true;
            }
        }
        return false;
    }

    private void procuraFaunaComMenosForca(Fauna elemento){
        double lastforca = 100;
        for (IElemento e : elementos) {
            if(e instanceof Fauna fauna){
                if(fauna.getForca()<lastforca){
                    lastforca = fauna.getForca();
                    elemento.setElemetoPerseguir(e);
                }
            }
        }
    }

    private IElemento procuraFaunaComMaisForca(Fauna elemento){
        double lastforca = 0;
        IElemento aux = null;
        for (IElemento e : elementos) {
            if(e instanceof Fauna fauna){
                if(fauna.getForca()>lastforca){
                    lastforca = fauna.getForca();
                    aux = e;
                }
            }
        }
        return aux;
    }

    public int verificaSobreposicao(IElemento elemento) {
        Area area = elemento.getArea();
        int count = 0;
        for (IElemento e : elementos) {
            if (e.getId() == elemento.getId() && e.getType() == elemento.getType())
                continue;
            if (elemento.getArea().compareTo(area))
                count ++;
        }
        return count;
    }


    //* novo a partir daqui



    public boolean verificarFaunaDistancia(IElemento elemento){
        double dist = 5;
        for(IElemento e : elementos){
            if(e.getType() == Elemento.FAUNA && e.getId()!=elemento.getId() ){
                double dx = elemento.getArea().esquerda() - e.getArea().esquerda();
                double dy = elemento.getArea().cima() - e.getArea().cima();
                double res = Math.sqrt(dx * dx + dy * dy);
                if (res < dist){
                    return true;
                }
            }
        }
        return false;
    }
}