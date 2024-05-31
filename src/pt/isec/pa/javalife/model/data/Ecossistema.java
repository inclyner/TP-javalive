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
    private boolean contemPedra = false;
    private boolean oneTime = true;
    private EcossistemaFacade ecossistemaFacade;

    // set up inicial do ecossistema (criação e inserção de elementos)
    public Ecossistema(EcossistemaFacade ecossistemaFacade, int dimensao,double escala) {
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
            addElemento(Elemento.INANIMADO, aux);
            aux = new Area( i, area.baixo()-1, i+1, area.baixo());
            addElemento(Elemento.INANIMADO, aux);
        }

        // Adiciona pedras na borda esquerda e direita(exceto nos cantos)
        for (double j = area.cima() + 1; j < area.baixo()-1; j += 1) {
            // adiciona pedras na borda esquerda
            aux = new Area(area.esquerda(), j, area.esquerda()+1, j+1);
            addElemento(Elemento.INANIMADO, aux);
            // adiciona pedras na borda direita
            aux = new Area(area.direita()-1, j, area.direita(), j+1);
            addElemento(Elemento.INANIMADO, aux);
        }
        addElemento(Elemento.FLORA, new Area(area.baixo() / 2, area.direita() / 2, area.baixo() / 2 + 2, area.direita() / 2 + 2));
        //addElemento(Elemento.FAUNA, new Area(area.baixo() / 2, area.direita() / 2, area.baixo() / 2 + 2, area.direita() / 2 + 2));
    }

    @Override
    public void evolve(IGameEngine gameEngine, long currentTime) {
        /*synchronized (elementos){
            /*verificarElementoMorre();*/
            /*for(IElemento elemento : elementos) {
                if(elemento instanceof Inanimado)
                    continue;
                if (elemento instanceof Flora flora) {
                    evolveFlora(flora);
                    System.out.println(flora);
                }*/
                /*else if (elemento instanceof Fauna fauna) {
                    evolveFauna(fauna);
                }*/
            //}
        //}*/
    }

    private void evolveFlora(Flora flora) {
        //Aumenta a força e verifica se pode reproduzir, caso sim aumenta o numero de reproducao e verifica se tem espaço para o fazer
        //caso tenha adiciona elemento
        if (flora.evoluir()) {
            Area a = verificaAdjacentes(flora, false);
            if (a != null) {
                flora.reproduziu();
                if (flora instanceof Erva)
                    addElemento(Elemento.FLORA, a);
            }
        }
        //verifica se tem sobreposiçao de elementos fauna e reduz a forca caso tenha
        for (int i = 0; i < verificaSobreposicao(flora); i++)
            flora.reduzirForcaSobreposicao();
    }

    private void evolveFauna(Fauna fauna) {
        Area a;
        boolean mov = true;
        //region verifica sobreposiçoes
        for (int i = 0; i < verificaSobreposicao(fauna); i++) {
            if (fauna.getForca() < 100) {
                mov = false;
                fauna.setForca(fauna.getForca() + Flora.getForcaSobreposicao());
            }
        }
        //endregion
        if (mov) {
            if (fauna.getState() == Fauna.FaunaState.PROCURA_COMIDA) {
                if (existeFlora()) {
                    //region procura elemento flora mais perto a tualiza area e forca
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
                            if (fauna.getElemetoPerseguir().getId() == aux.getId() && Elemento.FAUNA == aux.getType()) {
                                if (aux instanceof Fauna f) {
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
                            addElemento(Elemento.FAUNA, temp);
                    }
                }
            }
        }
    }

    public void addElemento(Elemento elemento, Area aux) {
        Area a = new Area(aux.cima(), aux.esquerda(), aux.baixo(), aux.direita());
        IElemento temp = ElementFactory.createElement(elemento, a);
        synchronized (elementos) {
            elementos.add(temp);
        }
        ecossistemaFacade.AdicionarElemento(temp.toString());
    }

    public void removeElemento(IElemento elemento) {
        elementos.remove(elemento);
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


    private boolean isAreaLivre(Area area) {
        synchronized (elementos) {
            for (IElemento elemento : elementos) {
                if (elemento.getArea().compareTo(area)) {
                    return false;
                }
            }
        }
        return true;
    }

    public Area verificaAdjacentes(IElemento elemento, boolean checkFlora) {
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
        if(!checkFlora) {
            for (Area adj : areasAdjacentes) {
                if (isAreaLivre(adj)) {
                    return adj;
                }
            }
        }
        else{
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
        return last;
    }

    private boolean existeFlora() {
        for (IElemento e : elementos) {
            if (e.getType() == Elemento.FLORA) {
                return true;
            }
        }
        return false;
    }

    private void procuraFaunaComMenosForca(Fauna elemento) {
        double lastforca = 100;
        for (IElemento e : elementos) {
            if (e instanceof Fauna fauna) {
                if (fauna.getForca() < lastforca) {
                    lastforca = fauna.getForca();
                    elemento.setElemetoPerseguir(e);
                }
            }
        }
    }

    private IElemento procuraFaunaComMaisForca(Fauna elemento) {
        double lastforca = 0;
        IElemento aux = null;
        for (IElemento e : elementos) {
            if (e instanceof Fauna fauna) {
                if (fauna.getForca() > lastforca) {
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
        synchronized (elementos) {
            for (IElemento e : elementos) {
                if (e.getId() != elemento.getId() && e.getType() != elemento.getType())
                    if(area.compareTo(e.getArea()))
                        count++;
            }
        }
        return count;
    }



    //* novo a partir daqui


    public boolean verificarFaunaDistancia(IElemento elemento) {
        double dist = 5;
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
        return false;
    }
}