package pt.isec.pa.javalife.model.EcoSistemaFacade;


import pt.isec.pa.javalife.model.command.AdicionarElementoCommand;
import pt.isec.pa.javalife.model.command.CommandManager;
import pt.isec.pa.javalife.model.command.RemoverElementoCommand;
import pt.isec.pa.javalife.model.data.Area;
import pt.isec.pa.javalife.model.data.Ecossistema;
import pt.isec.pa.javalife.model.data.Fauna;
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
    public boolean AdicionarElemento(IElemento elemento) {
        //* fazer verificação se da para adicionar


        //boolean canAdd = ecossistema.verificaAdjacentes(element);

        //ICommand command = new AdicionarElementoCommand(ecossistema, element);
        //commandManager.execute(command);

        support.firePropertyChange("ecossistema", null, string);
        //cm.invokeCommand(new AdicionarElementoCommand(elemento));
    }

    public boolean removeElement(IElemento elemento) {


        support.firePropertyChange("ecossistema", null, ecossistema);

        return cm.invokeCommand(new RemoverElementoCommand(elemento));
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
        gameEngine = new GameEngine();
        ecossistema = new Ecossistema(this, dimension,escala);
        gameEngine.registerClient(ecossistema);
        gameEngine.start(1000);

    }

    public void changeEcossistema(int timeUnit, int initialForce, double growthRate, int overlapLoss, double movementRate) {


    }

    public GameEngineState checkGameState(){
        return gameEngine.getCurrentState();
    }


    public void atualiza(List<String> list){
        for(String aux : list)
            support.firePropertyChange("ecossistema", null, aux);
    }
}
