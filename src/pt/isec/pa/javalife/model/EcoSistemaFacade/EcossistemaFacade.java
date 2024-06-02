package pt.isec.pa.javalife.model.EcoSistemaFacade;


import pt.isec.pa.javalife.model.command.*;
import pt.isec.pa.javalife.model.data.Area;
import pt.isec.pa.javalife.model.data.Ecossistema;
import pt.isec.pa.javalife.model.gameengine.GameEngine;
import pt.isec.pa.javalife.model.gameengine.GameEngineState;
import pt.isec.pa.javalife.model.gameengine.IGameEngine;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;

public class EcossistemaFacade{

    private final PropertyChangeSupport support;
    private Ecossistema ecossistema;
    private IGameEngine gameEngine;
    private final CommandManager cm;
    private int timeUnit;
    //memento




    public EcossistemaFacade() {
        this.cm = new CommandManager();
        this.support = new PropertyChangeSupport(this);
        //gameEngine.waitForTheEnd();
    }


    // Adiciona um elemento ao ecossistema
    public void AdicionarElemento(String string) {
        //* fazer verificação se da para adicionar
        //boolean canAdd = ecossistema.verificaAdjacentes(element);
        //ICommand command = new AdicionarElementoCommand(ecossistema, element);
        //commandManager.execute(command);
        support.firePropertyChange("adicionarElemento", null, string);
    }

    public boolean verificaEcossitemaNull() {
        return ecossistema == null;
    }


    public void undo() {
        if (cm.undo())
            adcionarPopUpAviso("Undo Feito");
        else
            adcionarPopUpAviso("Nao foi possivel fazer undo");
    }

    public void redo() throws IOException {
        if (cm.redo())
            adcionarPopUpAviso("Redo Feito");
        else
            adcionarPopUpAviso("Nao foi possivel fazer redo");
    }


    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    // Remove um listener para mudanças de propriedade
    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }


    public void createEcossistema(int dimension, double escala, int timeUnit, int initialForce, double growthRate, int overlapLoss, double movementRate) throws InterruptedException {
        ecossistema = new Ecossistema(this, dimension, initialForce, growthRate, overlapLoss, movementRate);
        gameEngine = new GameEngine();
        gameEngine.registerClient(ecossistema);
        this.timeUnit = timeUnit;
    }

    //!!!NAO se muda o tempo do jogo aqui e so nas definicoes da simulacao
    public void changeEcossistema(int initialForce, double growthRate, int overlapLoss, double movementRate) {
        ecossistema.setForcaInicial(initialForce);
        ecossistema.setTaxaCrescimento(growthRate);
        ecossistema.setForcaSobreposicao(overlapLoss);
        ecossistema.setVelocidade(movementRate);
    }

        public GameEngineState checkGameState () {
            return gameEngine.getCurrentState();
        }


        public void atualiza (String string){
            support.firePropertyChange("atualiza", null, string);
        }

        public void adicionaElementoCommand (String tipo,double x, double y, double altura, double largura, double forca) throws IOException {
            if (ecossistema != null) {
                if (cm.invokeCommand(new AdicionarElementoCommand(this.ecossistema, tipo, x, y, x + altura, y + largura, forca)))
                    adcionarPopUpAviso("Elemento adicionado com sucesso");
                else
                    adcionarPopUpAviso("Elemento nao foi adicionado");
            } else
                adcionarPopUpAviso("Ecossistema ainda não foi criado");

        }

        private void adcionarPopUpAviso (String string){
            support.firePropertyChange("adicionarPopUpAviso", null, string);
        }

        public String pause_unpause () {
            if (ecossistema != null) {
                if (gameEngine.getCurrentState() == GameEngineState.RUNNING) {
                    gameEngine.pause();
                    return "Simulação de Ecossistema (paused)";
                } else if (gameEngine.getCurrentState() == GameEngineState.PAUSED) {
                    gameEngine.resume();
                    return "Simulação de Ecossistema (running)";
                } else if (gameEngine.getCurrentState() == GameEngineState.READY) {
                    adcionarPopUpAviso("Ecossistema ainda não foi iniciado");
                }
            } else
                adcionarPopUpAviso("Ecossistema ainda não foi criado");
            return null;
        }

        public void execute_stop () {
            if (ecossistema != null) {
                if (gameEngine.getCurrentState() == GameEngineState.READY)
                    gameEngine.start(timeUnit);
                else if (gameEngine.getCurrentState() == GameEngineState.RUNNING) {
                    gameEngine.stop();
                }
            } else
                adcionarPopUpAviso("Ecossistema ainda não foi criado");
        }


        public boolean removerElementoCommand (String tipo, int id, Area a, double forca) throws IOException {
            if (cm.invokeCommand(new RemoverElementoCommand(this.ecossistema, tipo, id, a, forca))) {
                support.firePropertyChange("atualiza", null, ecossistema);
                return true;
            }
            return false;
        }

        public boolean editarElementoCommand (String tipo,int id, double direcao, double velocidade, double forca) throws IOException {
            if (cm.invokeCommand(new EditarElementoCommand(this.ecossistema, tipo, id, direcao, velocidade, forca))) {
                support.firePropertyChange("atualiza", null, ecossistema);
                return true;
            }
            return false;
        }

        public void exportasimulação (File file){

            ecossistema.exportaSimulacao(file);
        }


        public void importasimulação (File selectedFile){
            ecossistema.importaSimulacao(selectedFile);
        }

    public void saveSnapShot() throws IOException {
        if (!cm.invokeCommand(new GravarSnapshot(ecossistema,cm.getOriginator(),cm.getCareTaker())))
            adcionarPopUpAviso("Nao foi possivel guardar snapshot");
    }

    public boolean loadSnapShotAnterior() throws IOException {
        //
        if(cm.invokeCommand(new CarregarSnapshotCommand(ecossistema,cm.getOriginator(),cm.getCareTaker()))) {
            ecossistema.setElementos(cm.getOriginator().state);
            return true;
        }else {
            adcionarPopUpAviso("Nao existe snapshot criado");
            return false;
        }
    }

    public void atualiza(){
        ecossistema.atualiza();
    }

}