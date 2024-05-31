package pt.isec.pa.javalife.model.EcoSistemaFacade;


import pt.isec.pa.javalife.model.command.AdicionarElementoCommand;
import pt.isec.pa.javalife.model.command.CommandManager;
import pt.isec.pa.javalife.model.command.RemoverElementoCommand;
import pt.isec.pa.javalife.model.data.Ecossistema;
import pt.isec.pa.javalife.model.data.IElemento;
import pt.isec.pa.javalife.model.gameengine.GameEngine;
import pt.isec.pa.javalife.model.gameengine.GameEngineState;
import pt.isec.pa.javalife.model.gameengine.IGameEngine;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class EcossistemaFacade {

    private final PropertyChangeSupport support;
    private Ecossistema ecossistema;
    private IGameEngine gameEngine;
    private final CommandManager cm;


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


        //cm.invokeCommand(new AdicionarElementoCommand(elemento));
        //cm.invokeCommand(new AdicionarElementoCommand(ecossistema));
    }

    public boolean removeElement(IElemento elemento) {


        support.firePropertyChange("ecossistema", null, ecossistema);

        return cm.invokeCommand(new RemoverElementoCommand(ecossistema,elemento.getId()));
    }


    public void undo() {
        CommandManager.undo();
        support.firePropertyChange("ecosystem", null, ecossistema);
    }


    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    // Remove um listener para mudanças de propriedade
    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }


    public void createEcossistema(int dimension, double escala, int timeUnit, int initialForce, double growthRate, int overlapLoss, double movementRate) throws InterruptedException {
        ecossistema = new Ecossistema(this, dimension,escala);
        gameEngine = new GameEngine();
        gameEngine.registerClient(ecossistema);
        gameEngine.start(200);
    }

    public void changeEcossistema(int timeUnit, int initialForce, double growthRate, int overlapLoss, double movementRate) {


    }

    public GameEngineState checkGameState(){
        return gameEngine.getCurrentState();
    }


    public void atualiza(String string){
        support.firePropertyChange("atualiza", null, string);
    }

    public void adicionaElementoCommand(String tipo, double x, double y, double altura, double largura, double forca) {
        cm.invokeCommand(new AdicionarElementoCommand(this.ecossistema,tipo,x,y,altura,largura,forca));
        System.out.println("adicionaelementocommand na facade");
        System.out.println("tipo = " + tipo + ", x = " + x + ", y = " + y + ", altura = " + altura + ", largura = " + largura + ", forca = " + forca);
    }

    public void pause_unpause() {
        if(gameEngine.getCurrentState()== GameEngineState.RUNNING){
            gameEngine.pause();
        }
        else if(gameEngine.getCurrentState()== GameEngineState.PAUSED){
            gameEngine.resume();
        }

    }


    public void removerElementoCommand(int id) {
        cm.invokeCommand(new RemoverElementoCommand(this.ecossistema,id));
    }
}
