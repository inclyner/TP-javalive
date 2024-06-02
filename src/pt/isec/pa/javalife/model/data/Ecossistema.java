package pt.isec.pa.javalife.model.data;

import javafx.application.Platform;
import pt.isec.pa.javalife.model.EcoSistemaFacade.EcossistemaFacade;
import pt.isec.pa.javalife.model.factory.ElementFactory;
import pt.isec.pa.javalife.model.gameengine.IGameEngine;
import pt.isec.pa.javalife.model.gameengine.IGameEngineEvolve;

import java.io.*;
import java.util.*;

public class Ecossistema implements Serializable, IGameEngineEvolve {
    List<String> listStringElementos = new ArrayList<>();
    int num_ticks = 0;
    List<String> listStringElementosRemove = new ArrayList<>();
    private Set<IElemento> elementos = new HashSet<>();
    private Area area;
    private Area areaBoard;
    private int forcaInicial, forcaSobreposicao;
    private double taxaCrescimento, velocidade;
    private EcossistemaFacade ecossistemaFacade;
    boolean sol=false;


    // set up inicial do ecossistema (criação e inserção de elementos)
    public Ecossistema(EcossistemaFacade ecossistemaFacade, int dimensao, int forcaInicial, double taxaCrescimento, int forcaSobreposicao, double velocidade) {
        this.ecossistemaFacade = ecossistemaFacade;
        //TODO alterar para meter as coordenadas pelas definições
        // definir as unidades (tipo 1000 pixeis de largura são 2 pixeis por unidade)
        //region criação de pedras
        area = new Area(0, 0, dimensao, dimensao);
        areaBoard = area;
        this.forcaInicial = forcaInicial;
        this.taxaCrescimento = taxaCrescimento;
        this.forcaSobreposicao = forcaSobreposicao;
        this.velocidade = velocidade;
        //preenche a cerca da area com pedras
        Area aux;
        // Adiciona pedras na borda superior e inferior
        for (double i = area.cima(); i < area.baixo(); i += 1) {
            // Adiciona pedras na borda superior e inferior
            aux = new Area(i, area.cima(), i + 1, area.cima() + 1);
            addElemento(Elemento.INANIMADO, aux, 0);
            aux = new Area(i, area.baixo() - 1, i + 1, area.baixo());
            addElemento(Elemento.INANIMADO, aux, 0);
        }
        // Adiciona pedras na borda esquerda e direita(exceto nos cantos)
        for (double j = area.cima() + 1; j < area.baixo() - 1; j += 1) {
            // adiciona pedras na borda esquerda
            aux = new Area(area.esquerda(), j, area.esquerda() + 1, j + 1);
            addElemento(Elemento.INANIMADO, aux, 0);
            // adiciona pedras na borda direita
            aux = new Area(area.direita() - 1, j, area.direita(), j + 1);
            addElemento(Elemento.INANIMADO, aux, 0);
        }

    }

    public void aplicaSol() {

        num_ticks = 10;
        sol=true;
        //adicionar no evolve, quando chamo a funcao ele fica a 10
        //no evolve ele vai aplicar a velocidade metade e o crescimento ao dobro
        // e retira 1 tick
    }

    public void setForcaInicial(int forcaInicial) {
        this.forcaInicial = forcaInicial;
    }

    public void setForcaSobreposicao(int forcaSobreposicao) {
        this.forcaSobreposicao = forcaSobreposicao;
    }

    public void setTaxaCrescimento(double taxaCrescimento) {
        this.taxaCrescimento = taxaCrescimento;
    }

    public void setVelocidade(double velocidade) {
        this.velocidade = velocidade;
    }

    @Override
    public void evolve(IGameEngine gameEngine, long currentTime) {
        synchronized (elementos) {
            verificarElementoMorre();
            for (IElemento elemento : elementos) {
                if (elemento instanceof Inanimado) continue;
                if (elemento instanceof Flora flora) {
                    //envia o boolean sol, para que quando haja sol faça os multiplicadores
                    evolveFlora(flora,sol);
                    Platform.runLater(() -> ecossistemaFacade.atualiza(flora.toString()));
                } else if (elemento instanceof Fauna fauna) {
                    //envia o boolean sol, para que quando haja sol faça os multiplicadores
                    evolveFauna(fauna,sol);
                    Platform.runLater(() -> ecossistemaFacade.atualiza(fauna.toString()));
                }


                if (num_ticks != 0) {num_ticks--;}
                if(num_ticks==0) sol=false;
            }
            }

        synchronized (listStringElementos) {
            if (!listStringElementos.isEmpty()) {
                addElementoPorString();
                removeElelementoPorString();
                listStringElementos.clear();
                listStringElementosRemove.clear();
            }
        }


    }

    private void removeElelementoPorString() {
        for (String aux : listStringElementosRemove) {
            String[] partes = aux.split(":");
            if(partes.length==2){
                synchronized (elementos){
                    for(IElemento e: elementos){
                        if(e.getType().toString().equals(partes[0]) && e.getId()==Integer.parseInt(partes[1])){
                            elementos.remove(e);
                            Platform.runLater(()->ecossistemaFacade.AdicionarElemento(e.toString()));
                        }
                    }
                }
            }
        }
    }

    private void addElementoPorString() {
        for (String aux : listStringElementos) {
            String[] partes = aux.split(":");
            if (Elemento.FLORA.toString().equals(partes[0])) {
                IElemento temp = ElementFactory.createElement(Elemento.FLORA, converterStringParaArea(partes[1]), forcaInicial, taxaCrescimento, forcaSobreposicao);
                synchronized (elementos) {
                    elementos.add(temp);
                    Platform.runLater(() -> ecossistemaFacade.AdicionarElemento(temp.toString()));
                }
            }
            if (Elemento.FAUNA.toString().equals(partes[0])) {
                IElemento temp = ElementFactory.createElement(Elemento.FAUNA, converterStringParaArea(partes[1]), forcaInicial, velocidade);
                synchronized (elementos) {
                    elementos.add(temp);
                    Platform.runLater(() -> ecossistemaFacade.AdicionarElemento(temp.toString()));
                }
            }
        }
    }

    private Area converterStringParaArea(String areaString) {
        areaString = areaString.replace("(", "").replace(")", "");
        String[] valores = areaString.split(";");
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

    private void evolveFlora(Flora flora,boolean sol) {
        //Aumenta a força e verifica se pode reproduzir, caso sim aumenta o numero de reproducao e verifica se tem espaço para o fazer
        //caso tenha adiciona elemento
        if (flora.evoluir(sol)) {
            Area a = verificaAdjacentes(flora, false);
            if (a != null) {
                flora.reproduziu();
                if (flora instanceof Erva) addElementoNoEvolve(Elemento.FLORA, a);
            }
        }
        //verifica se tem sobreposiçao de elementos fauna e reduz a forca caso tenha
        for (int i = 0; i < verificaSobreposicao(flora); i++)
            flora.reduzirForcaSobreposicao();
    }

    private void addElementoNoEvolve(Elemento elemento, Area aux) {
        listStringElementos.add(elemento + ":" + aux.toString());
    }

    private void removeElementoNoEvolve(Elemento elemento, int id){
        listStringElementosRemove.add(elemento+":"+id);
    }

    private void evolveFauna(Fauna fauna,boolean sol) {
        //! falta introduzir o boolean para metade da velocidade nos movimentos
        Area areaParaOndeVai;
        boolean mov = true;
        boolean contemPedra = true;
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
                        areaParaOndeVai = fauna.moverParaComida(checkElementoMaisPerto(fauna.getArea()), contemPedra,sol);
                        contemPedra = verificaPedraouFauna(areaParaOndeVai, fauna);
                    } while (contemPedra);
                    fauna.setArea(areaParaOndeVai);
                    fauna.setForca(fauna.getForca() - fauna.getForcaMovimentacao());
                    //endregion
                } else {
                    if (fauna.getElemetoPerseguir()==null) {
                        procuraFaunaComMenosForca(fauna);
                    }
                    if (fauna.getElemetoPerseguir()!=null){
                        if (fauna.verificarAdjacente(this.areaBoard,sol)) {
                            for (IElemento aux : elementos) {
                                if (fauna.getElemetoPerseguir().getId() == aux.getId() && Elemento.FAUNA == aux.getType() && aux instanceof Fauna f) {
                                    if (fauna.getForca() > 10) {
                                        fauna.setForca(fauna.getForca() + f.getForca() - 10);
                                        f.setForca(0);
                                        fauna.setElemetoPerseguir(null);
                                        removeElementoNoEvolve(f.getType(), f.getId());
                                    } else if (fauna.getForca() < 10) {
                                        f.setForca(fauna.getForca() + f.getForca());
                                        fauna.setForca(0);
                                        f.setElemetoPerseguir(null);
                                        removeElementoNoEvolve(fauna.getType(), fauna.getId());
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
                        else {
                            //region perseguir elemento da fauna e atualizar area e forca
                            contemPedra = false;
                            do {
                                for (IElemento aux : elementos) {
                                    if (fauna.getElemetoPerseguir().getId() == aux.getId() && Elemento.FAUNA == aux.getType()) {
                                        fauna.setElemetoPerseguir(aux);
                                    }
                                }
                                areaParaOndeVai = fauna.moverParaComida(fauna.getElemetoPerseguir().getArea(), !contemPedra,sol);
                                contemPedra = verificaPedraouFauna(areaParaOndeVai, fauna);
                            } while (contemPedra);
                            fauna.setArea(areaParaOndeVai);
                            fauna.setForca(fauna.getForca() - fauna.getForcaMovimentacao());
                        }
                    }else
                        movimentacaoFauna(fauna,sol);
                    //region verifica adjacentes e atualiza forca caso seja adjacente
                    //endregion
                }
            } else if (fauna.getState() == Fauna.FaunaState.NAO_PROCURA_COMIDA) {
                IElemento aux;
                if (fauna.getForca() > 50) {
                    //region procura elemento com mais forca e ir na direcao do mesmo
                    aux = procuraFaunaComMaisForca(fauna);
                    if (aux != null) {
                        contemPedra = false;
                        do {
                            areaParaOndeVai = fauna.moverParaComida(aux.getArea(), !contemPedra,sol);
                            contemPedra = verificaPedraouFauna(areaParaOndeVai, fauna);
                        } while (contemPedra);
                        fauna.setArea(areaParaOndeVai);
                        fauna.setForca(fauna.getForca() - fauna.getForcaMovimentacao());
                    } else {
                        movimentacaoFauna(fauna,sol);
                    }
                    //endregion
                    if (fauna.reproducao(verificarFaunaDistancia(fauna))) {
                        Area temp = verificaAdjacentes(fauna, true);
                        if (temp != null) {
                            addElementoNoEvolve(Elemento.FAUNA, temp);
                        }
                    }
                } else {
                    movimentacaoFauna(fauna,sol);
                }

            }
        }
    }

    private void movimentacaoFauna(Fauna fauna,boolean sol) {
        Area areaParaOndeVai;
        boolean contemPedra = true;
        do {
            areaParaOndeVai = fauna.movimentacao(sol);
            contemPedra = verificaPedraouFauna(areaParaOndeVai, fauna);
        } while (contemPedra);
        fauna.setArea(areaParaOndeVai);
        fauna.setForca(fauna.getForca() - fauna.getForcaMovimentacao());
    }

    public void addElemento(Elemento tipo, Area aux, double forca) {
        Area a = new Area(aux.cima(), aux.esquerda(), aux.baixo(), aux.direita());
        IElemento temp = null;
        if (tipo == Elemento.FLORA)
            temp = ElementFactory.createElement(tipo, a, forcaInicial, taxaCrescimento, forcaSobreposicao);
        else if (tipo == Elemento.FAUNA) temp = ElementFactory.createElement(tipo, a, forcaInicial, velocidade);
        else if (tipo == Elemento.INANIMADO) temp = ElementFactory.createElement(tipo, a);
        if (forca != 0) {
            if (temp instanceof Fauna fauna) fauna.setForca(forca);
            if (temp instanceof Flora flora) flora.setForca(forca);
        }
        synchronized (elementos) {
            if (temp != null) elementos.add(temp);
        }
        ecossistemaFacade.AdicionarElemento(temp.toString());
    }

    public boolean removeElemento(String tipo, int id) {
        IElemento elemento = null;
        for (IElemento e : elementos) {
            if (e.getId() == id && e.getType().toString().equals(tipo)) {
                elemento = e;
                break;
            }
        }
        return elementos.remove(elemento);
    }

    public boolean removeElementoPelaArea(String tipo, Area aux) {
        IElemento elemento = null;
        String finalElementoString = null;
        for (IElemento e : elementos) {
            if (e.getArea().iguais(aux) && e.getType().toString().equals(tipo)) {
                elemento = e;
                finalElementoString = e.toString();
                break;
            }
        }
        if (elementos.remove(elemento)) {
            ecossistemaFacade.atualiza(finalElementoString);
            return true;
        }
        return false;
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
                if (area.compareTo(elemento.getArea(), this.areaBoard)) {
                    return false;
                }
            }
        }
        synchronized (listStringElementos) {
            if (!listStringElementos.isEmpty()) for (String string : listStringElementos) {
                String[] partes = string.split(":");
                if (converterStringParaArea(partes[1]).compareTo(area, this.areaBoard)) {
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
                if (!verificaPedraouFauna(adj, elemento)) {
                    return adj;
                }
            }
        }

        return null;
    }

    private boolean verificaPedraouFauna(Area area, IElemento e) {
        synchronized (elementos) {
            for (IElemento elemento : elementos) {
                if (area.compareTo(elemento.getArea(), this.areaBoard)) {
                    if (elemento.getType() == Elemento.INANIMADO || (elemento.getType() == Elemento.FAUNA && elemento.getId() != e.getId()))
                        return true;
                }
            }
        }
        return false;
    }

    private Area checkElementoMaisPerto(Area a) {
        Area last = null;
        double largura = this.area.direita() - this.area.esquerda();
        double dist = Math.sqrt(Math.pow(largura, 2) + Math.pow(largura, 2));
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
                    if (fauna.getForca() < lastforca && fauna.getId() != elemento.getId()) {
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
                    if (fauna.getId() != elemento.getId()) if (fauna.getForca() > lastforca) {
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
                if (e.getArea().compareTo(area, this.areaBoard)) count++;
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
                if (area.compareTo(e.getArea(), this.areaBoard) && e.getType() == Elemento.FLORA) count++;
            }
        }
        return count;
    }

    public boolean verificarFaunaDistancia(IElemento elemento) {
        double dist = 5;
        synchronized (elementos) {
            for (IElemento e : elementos) {
                if (e.getType() == Elemento.FAUNA && e.getId() != elemento.getId()) {
                    double horizontalDistance = 0;
                    double verticalDistance = 0;
                    if (e.getArea().direita() < elemento.getArea().esquerda()) {
                        horizontalDistance = elemento.getArea().esquerda() - e.getArea().direita();
                    } else if (e.getArea().esquerda() > elemento.getArea().direita()) {
                        horizontalDistance = e.getArea().esquerda() - elemento.getArea().direita();
                    }
                    //r1 e e r2 elemento
                    if (e.getArea().cima() < elemento.getArea().baixo()) {
                        verticalDistance = elemento.getArea().baixo() - e.getArea().cima();
                    } else if (e.getArea().baixo() > elemento.getArea().cima()) {
                        verticalDistance = e.getArea().baixo() - elemento.getArea().cima();
                    }
                    if (horizontalDistance <= dist && verticalDistance <= dist) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Elemento getTipo(int id) {
        synchronized (elementos) {
            for (IElemento e : elementos) {
                if (e.getType() == Elemento.FAUNA) {
                    return Elemento.FAUNA;
                }
                if (e.getType() == Elemento.INANIMADO) {
                    return Elemento.INANIMADO;
                }
                if (e.getType() == Elemento.FLORA) {
                    return Elemento.FLORA;
                }
            }
        }
        return null;
    }

    public boolean editElemento(String tipo, int id , double velocidade, double forca) {
        synchronized (elementos) {
            for (IElemento e : elementos) {
                if (e instanceof Fauna fauna && e.getId() == id && tipo.equals("FAUNA")) {
                    if (velocidade > 0) fauna.setVelocidade(velocidade);

                    if (velocidade == -1) {
                        fauna.setForca(forca + ((Fauna) e).getForca());
                    } //a velocidade é -1 quando o edit elemento é chamado pelo injetarforcacommand

                    if(forca>0)
                        fauna.setForca(forca);

                    return true;
                }
                if (e instanceof Flora flora && e.getId() == id && tipo.equals("FLORA")) {
                    flora.setForca(forca);
                    return true;
                }
            }

        }

        return false;
    }

    public boolean exportaSimulacao(File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // Escrever cabeçalho do CSV
            writer.write("id,type,area,forca");
            writer.newLine();

            // Escrever dados dos elementos
            for (IElemento elemento : elementos) {

                writer.write(String.valueOf(elemento.getId())); // Escrever id
                writer.write(","); // Separador de coluna

                writer.write(elemento.getType().toString()); // Escrever type
                writer.write(","); // Separador de coluna

                writer.write(String.valueOf(elemento.getArea())); // Escrever area
                writer.write(","); // Separador de coluna

                if (elemento instanceof Inanimado) {
                    writer.write("0"); // Escrever forca
                    writer.newLine(); // Nova linha
                }
                if (elemento instanceof Fauna fauna) {
                    writer.write(String.valueOf(((Fauna) elemento).getForca())); // Escrever forca
                    writer.newLine(); // Nova linha
                } else if (elemento instanceof Flora flora) {
                    writer.write(String.valueOf(((Flora) elemento).getForca())); // Escrever forca
                    writer.newLine(); // Nova linha
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean importaSimulacao(File selectedFile) {
        //! o check verifica element nao funciona
        try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(","); // Separar os elementos por tabulação
                String tipo = parts[1].trim(); // Extrair o tipo removendo espaços em branco
                String areaStr = parts[2].trim(); // Extrair a área removendo espaços em branco
                double forca = Double.parseDouble(parts[3].trim());
                // Remove parênteses da string da área
                areaStr = areaStr.substring(1, areaStr.length() - 1);
                // Separa os valores da área
                String[] areaParts = areaStr.split(";");

                Area temp = new Area(Double.parseDouble(areaParts[0]), Double.parseDouble(areaParts[1]), Double.parseDouble(areaParts[2]), Double.parseDouble(areaParts[3]));

                if (temp.compareTo(temp, areaBoard)) {
                    // Verifica se o elemento se sobrepõe a algum existente
                    if (verificaElementoArea(temp, tipo)) {
                        // Se o elemento passar nas verificações, cria e adiciona
                        addElemento(Elemento.valueOf(tipo), temp, forca);
                        System.out.println("ELEMENTO ADICIONADO temp = " + temp);

                    } else {
                        // Se o elemento não passar nas verificações, cria e adiciona
                        System.out.println("ELEMENTO NAO FOI ADICIONADO");
                    }
                }


            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    private boolean verificaElementoArea(Area temp, String tipo) {
        //! a logica ainda nao foi imensamente pensada
        for (IElemento elemento : elementos) {
            if (temp.compareTo(elemento.getArea(), areaBoard) && tipo.equalsIgnoreCase(elemento.getType().toString())) {
                return false;
            }
        }
        return true;
    }

    public Set<IElemento> getElementos() {
        return elementos;
    }

    public void setElementos(Set<IElemento> elementos) {
        this.elementos = elementos;
    }

    public void atualiza() {
        if (!elementos.isEmpty()) for (IElemento e : elementos) {
            Platform.runLater(() -> ecossistemaFacade.atualiza(e.toString()));
        }
    }

    public boolean aplicarHerbicida(Elemento tipo, int id) {

        for (IElemento e : elementos) {
            if (e instanceof Flora flora && e.getId() == id && tipo==Elemento.FLORA) {
                flora.setForca(0);
                return true;
            }
        }
        return false;
    }

    public double obtemValoresAntigos(String tipo, int id, String aux) {
        for (IElemento e : elementos) {
            if(e.getType().toString().equalsIgnoreCase(tipo) && e.getId()==id){
                if(aux.equalsIgnoreCase("Velocidade")) {
                    if (e instanceof Fauna fauna)
                        return fauna.getVelocidade();
                }
                else if(aux.equalsIgnoreCase("Forca"))
                    if(e instanceof Fauna fauna)
                        return fauna.getForca();
                    else if (e instanceof Flora flora)
                        return flora.getForca();
            }
        }
        return 0;
    }

}



