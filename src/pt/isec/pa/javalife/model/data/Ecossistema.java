package pt.isec.pa.javalife.model.data;


import javafx.application.Platform;
import pt.isec.pa.javalife.model.EcoSistemaFacade.EcossistemaFacade;
import pt.isec.pa.javalife.model.factory.ElementFactory;
import pt.isec.pa.javalife.model.gameengine.IGameEngine;
import pt.isec.pa.javalife.model.gameengine.IGameEngineEvolve;

import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.*;

public class Ecossistema implements Serializable, IGameEngineEvolve {
    private Set<IElemento> elementos = new HashSet<>();
    private Area area;

    private double escala;
    private int forcaInicial, taxaCrescimento, forcaSobreposicao, velocidade;
    private boolean contemPedra = false;
    private boolean oneTime = true;
    private EcossistemaFacade ecossistemaFacade;
    List<String> listStringElementos = new ArrayList<>();

    // set up inicial do ecossistema (criação e inserção de elementos)
    public Ecossistema(EcossistemaFacade ecossistemaFacade, int dimensao,double escala, int forcaInicial, double taxaCrescimento, int forcaSobreposicao, double velocidade) {
        this.ecossistemaFacade = ecossistemaFacade;
        //TODO alterar para meter as coordenadas pelas definições
        // definir as unidades (tipo 1000 pixeis de largura são 2 pixeis por unidade)
        //region criação de pedras
        area = new Area(0, 0, dimensao, dimensao);
        this.escala = escala;
        System.out.println("escala = " + escala);
        //preenche a cerca da area com pedras
        Area aux;
        // Adiciona pedras na borda superior e inferior
        for (double i = area.cima(); i < area.baixo(); i += 1) {
            // Adiciona pedras na borda superior e inferior
            aux = new Area(i, area.cima(), i+1, area.cima()+1);
            addElemento(Elemento.INANIMADO, aux,0);
            aux = new Area( i, area.baixo()-1, i+1, area.baixo());
            addElemento(Elemento.INANIMADO, aux,0);
        }

        // Adiciona pedras na borda esquerda e direita(exceto nos cantos)
        for (double j = area.cima() + 1; j < area.baixo()-1; j += 1) {
            // adiciona pedras na borda esquerda
            aux = new Area(area.esquerda(), j, area.esquerda()+1, j+1);
            addElemento(Elemento.INANIMADO, aux,0);
            // adiciona pedras na borda direita
            aux = new Area(area.direita()-1, j, area.direita(), j+1);
            addElemento(Elemento.INANIMADO, aux,0);
        }

    }

    @Override
    public void evolve(IGameEngine gameEngine, long currentTime) {
        synchronized (elementos) {
            /*verificarElementoMorre();*/
            for(IElemento elemento : elementos) {
                if(elemento instanceof Inanimado)
                    continue;
                if (elemento instanceof Flora flora) {
                    evolveFlora(flora);
                }
                else if (elemento instanceof Fauna fauna) {
                    evolveFauna(fauna);
                    Platform.runLater(()->ecossistemaFacade.atualiza(fauna.toString()));
                }
            }
        }
        synchronized (listStringElementos){
            if(!listStringElementos.isEmpty()) {
                addElementoPorString();
            }
        }
    }

    private void addElementoPorString() {
        for(String aux : listStringElementos){
            System.out.println(aux);
            String[] partes = aux.split(":");
            if(Elemento.FLORA.toString().equals(partes[0])){
                IElemento temp = ElementFactory.createElement(Elemento.FLORA, converterStringParaArea(partes[1]));
                synchronized (elementos) {
                    elementos.add(temp);
                    Platform.runLater(()->ecossistemaFacade.AdicionarElemento(temp.toString()));
                }
            }
        }
        listStringElementos.clear();
    }

    private Area converterStringParaArea(String areaString) {
        areaString = areaString.replace("(", "").replace(")", "");
        String[] valores = areaString.split(",");
        if (valores.length != 4) {
            throw new IllegalArgumentException("A string deve conter exatamente 4 valores separados por ','");
        }

        try {
            double cima = Double.parseDouble(valores[0]);
            double esquerda = Double.parseDouble(valores[1]);
            double baixo = Double.parseDouble(valores[2]);
            double direita = Double.parseDouble(valores[3]);

            return new Area(cima, esquerda, baixo, direita);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Todos os valores devem ser números válidos", e);
        }
    }

    private void evolveFlora(Flora flora) {
        //Aumenta a força e verifica se pode reproduzir, caso sim aumenta o numero de reproducao e verifica se tem espaço para o fazer
        //caso tenha adiciona elemento
        if (flora.evoluir()) {
            Area a = verificaAdjacentes(flora, false);
            if (a != null) {
                flora.reproduziu();
                if (flora instanceof Erva)
                    addElementoNoEvolve(Elemento.FLORA, a);
            }
        }
        //verifica se tem sobreposiçao de elementos fauna e reduz a forca caso tenha
        for (int i = 0; i < verificaSobreposicao(flora); i++)
            flora.reduzirForcaSobreposicao();
    }

    private void addElementoNoEvolve(Elemento elemento, Area aux) {
        listStringElementos.add(elemento+":"+aux.toString());
    }

    private void evolveFauna(Fauna fauna) {
        Area a;
        boolean mov = true;
        //region verifica sobreposiçoes
        for (int i = 0; i < verificaSobreposicaoFlora(fauna); i++) {
            if (fauna.getForca() < 100) {
                mov = false;
                fauna.setForca(fauna.getForca() + Flora.getForcaSobreposicao());
            }
        }
        //endregion
        if (mov) {
            if (fauna.getState() == Fauna.FaunaState.PROCURA_COMIDA) {
                if (existeFlora()) {
                    //region procura elemento flora mais perto atualiza area e forca
                    do {
                        a = fauna.moverParaComida(checkElementoMaisPerto(fauna.getArea()), contemPedra);
                        contemPedra = isLivrePedraFauna(a);
                    } while (!contemPedra);
                    fauna.setArea(a);
                    fauna.setForca(fauna.getForca() - fauna.getForcaMovimentacao());
                    //endregion
                } else {
                    if (oneTime || !elementos.contains(fauna.getElemetoPerseguir())) {
                        procuraFaunaComMenosForca(fauna);
                        oneTime = false;
                    }
                    //region perseguir elemento da fauna e atualizar area e forca
                    do {
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
                    if (fauna.verificarAdjacente()) {
                        for (IElemento aux : elementos) {
                            if (fauna.getElemetoPerseguir().getId() == aux.getId() && Elemento.FAUNA == aux.getType() && aux instanceof Fauna f) {
                                if (fauna.getForca() > 10) {
                                    fauna.setForca(fauna.getForca() + f.getForca() - 10);
                                    f.setForca(0);
                                } else if (fauna.getForca() < 10) {
                                    f.setForca(fauna.getForca() + f.getForca());
                                    fauna.setForca(0);
                                } else if (fauna.getState() == Fauna.FaunaState.PROCURA_COMIDA && f.getState() == Fauna.FaunaState.PROCURA_COMIDA) {
                                    if (fauna.getForca() > f.getForca()) {
                                        fauna.setForca(fauna.getForca() - 10 + f.getForca() - 10);
                                        f.setForca(0);
                                    } else if (fauna.getForca() < f.getForca()) {
                                        f.setForca(f.getForca() - 10 + fauna.getForca() - 10);
                                        fauna.setForca(0);
                                    }
                                }
                            }
                        }
                    }
                    //endregion
                }
            } else if (fauna.getState() == Fauna.FaunaState.NAO_PROCURA_COMIDA) {
                IElemento aux;
                if (fauna.getForca() > 50) {
                    aux = procuraFaunaComMaisForca(fauna);
                    if (aux != null) {
                        do {
                            a = fauna.moverParaComida(aux.getArea(), contemPedra);
                            contemPedra = isLivrePedraFauna(a);
                        } while (!contemPedra);
                        fauna.setArea(a);
                        fauna.setForca(fauna.getForca() - fauna.getForcaMovimentacao());
                    }
                    if (fauna.reproducao(verificarFaunaDistancia(fauna))) {
                        Area temp = verificaAdjacentes(fauna, true);
                        if(temp != null)
                            addElemento(Elemento.FAUNA, temp,0);
                    }
                }else{
                    do {
                        a = fauna.movimentacao();
                        contemPedra = isLivrePedraFauna(a);
                    } while (contemPedra);
                    fauna.setArea(a);
                    fauna.setForca(fauna.getForca() - fauna.getForcaMovimentacao());
                }

            }
        }
    }

    public void addElemento(Elemento tipo, Area aux,double forca) {

        Area a = new Area(aux.cima(), aux.esquerda(), aux.baixo(), aux.direita());
        IElemento temp = ElementFactory.createElement(tipo, a);
        if(forca != 0) {
            if (temp instanceof Fauna fauna)
                fauna.setForca(forca);
            if (temp instanceof Flora flora)
                flora.setForca(forca);
        }
        synchronized (elementos) {
          elementos.add(temp);
        }
        ecossistemaFacade.AdicionarElemento(temp.toString());
    }

    public boolean removeElemento(int id) {
        IElemento elemento = null;
        for (IElemento e :elementos) {
            if (e.getId() == id) {
                elemento=e;
                break;
            }
        }

        return elementos.remove(elemento);
    }

    public void verificarElementoMorre() {
        synchronized (elementos) {
            Iterator<IElemento> iterator = elementos.iterator();
            while (iterator.hasNext()) {
                IElemento elemento = iterator.next();
                if (elemento instanceof Fauna fauna) {
                    if (fauna.getForca() <= 0) {
                        iterator.remove(); // Remove the current element using the iterator
                        Platform.runLater(() -> ecossistemaFacade.atualiza(fauna.toString()));
                    }
                } else if (elemento instanceof Flora flora) {
                    if (flora.getForca() <= 0) {
                        iterator.remove(); // Remove the current element using the iterator
                        Platform.runLater(() -> ecossistemaFacade.atualiza(flora.toString()));
                    }
                }
            }
        }
    }


    public boolean isAreaLivre(Area area) {
        synchronized (elementos) {
            for (IElemento elemento : elementos) {
                if (elemento.getArea().compareTo(area)) {
                    return false;
                }
            }
        }
        synchronized (listStringElementos){
            if(!listStringElementos.isEmpty())
                for (String string : listStringElementos) {
                    String[] partes = string.split(":");
                    if (converterStringParaArea(partes[1]).compareTo(area)) {
                        return false;
                    }
                }
        }
        return true;
    }

    public Area verificaAdjacentes(IElemento elemento, boolean checkFlora) {
        Area areaA = elemento.getArea();
        double altura = areaA.baixo() - areaA.cima();
        double largura = areaA.direita() - areaA.esquerda();

        // Lista para armazenar as áreas adjacentes
        List<Area> areasAdjacentes = new ArrayList<>();

        //region verifica ajacentes
        if (areaA.cima() - area.cima() > altura) {
            areasAdjacentes.add(new Area(areaA.cima() - altura, areaA.esquerda(), areaA.baixo() - altura, areaA.direita()));
        }
        if (area.baixo() - areaA.baixo() > altura) {
            areasAdjacentes.add(new Area(areaA.cima() + altura, areaA.esquerda(), areaA.baixo() + altura, areaA.direita()));
        }
        if (areaA.esquerda() - area.esquerda() > largura) {
            areasAdjacentes.add(new Area(areaA.cima(), areaA.esquerda() - largura, areaA.baixo(), areaA.direita() - largura));
        }
        if (area.direita() - areaA.direita() > largura) {
            areasAdjacentes.add(new Area(areaA.cima(), areaA.esquerda() + largura, areaA.baixo(), areaA.direita() + largura));
        }
        //endregion
        // Verifica se alguma área adjacente está livre
        if (!checkFlora) {
            for (Area adj : areasAdjacentes) {
                if (isAreaLivre(adj)) {
                    return adj;
                }
            }
        } else {
            for (Area adj : areasAdjacentes) {
                if (isLivrePedraFauna(adj)) {
                    return adj;
                }
            }
        }

        return null;
    }

    private boolean isLivrePedraFauna(Area area) {
        synchronized (elementos) {
            for (IElemento elemento : elementos) {
                if (elemento.getArea().compareTo(area) && (elemento.getType() == Elemento.INANIMADO || elemento.getType() == Elemento.FAUNA)) {
                    return false;
                }
            }
        }
        return true;
    }

    private Area checkElementoMaisPerto(Area a) {
        Area last = null;
        double dist = Double.MAX_VALUE;
        synchronized (elementos) {
            for (IElemento e : elementos) {
                if (e.getType() == Elemento.FLORA) {
                    double dx = a.esquerda() - e.getArea().esquerda();
                    double dy = a.cima() - e.getArea().cima();
                    double res = Math.sqrt(dx * dx + dy * dy);
                    if (res < dist) {
                        dist = res;
                        last = e.getArea();
                    }
                }
            }
        }
        return last;
    }

    private boolean existeFlora() {
        synchronized (elementos) {
            for (IElemento e : elementos) {
                if (e.getType() == Elemento.FLORA) {
                    return true;
                }
            }
        }
        return false;
    }

    private void procuraFaunaComMenosForca(Fauna elemento) {
        double lastforca = 100;
        synchronized (elementos) {
            for (IElemento e : elementos) {
                if (e instanceof Fauna fauna) {
                    if (fauna.getForca() < lastforca) {
                        lastforca = fauna.getForca();
                        elemento.setElemetoPerseguir(e);
                    }
                }
            }
        }
    }

    private IElemento procuraFaunaComMaisForca(Fauna elemento) {
        double lastforca = 0;
        IElemento aux = null;
        synchronized (elementos) {
            for (IElemento e : elementos) {
                if (e instanceof Fauna fauna) {
                    if (fauna.getForca() > lastforca) {
                        lastforca = fauna.getForca();
                        aux = e;
                    }
                }
            }
        }
        return aux;
    }

    public int verificaSobreposicao(IElemento elemento) {
        Area area = elemento.getArea();
        int count = 0;
        synchronized (elementos) {
            for (IElemento e : elementos) {
                if (e.getId() == elemento.getId() && e.getType() == elemento.getType()) continue;
                if (e.getArea().compareTo(area)) count++;
            }
        }
        return count;
    }

    public int verificaSobreposicaoFlora(IElemento elemento) {
        Area area = elemento.getArea();
        int count = 0;
        synchronized (elementos) {
            for (IElemento e : elementos) {
                if (e.getId() == elemento.getId() && e.getType() == elemento.getType()) continue;
                if (e.getArea().compareTo(area) && e.getType()==Elemento.FLORA) count++;
            }
        }
        return count;
    }


    //* novo a partir daqui


    public boolean verificarFaunaDistancia(IElemento elemento) {
        double dist = 5;
        synchronized (elementos) {
            for (IElemento e : elementos) {
                if (e.getType() == Elemento.FAUNA && e.getId() != elemento.getId()) {
                    double dx = elemento.getArea().esquerda() - e.getArea().esquerda();
                    double dy = elemento.getArea().cima() - e.getArea().cima();
                    double res = Math.sqrt(dx * dx + dy * dy);
                    if (res < dist) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}