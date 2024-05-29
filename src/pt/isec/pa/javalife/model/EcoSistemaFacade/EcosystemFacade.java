package pt.isec.pa.javalife.model.EcoSistemaFacade;


import pt.isec.pa.javalife.model.command.AdicionarElementoCommand;
import pt.isec.pa.javalife.model.command.CommandManager;
import pt.isec.pa.javalife.model.command.RemoverElementoCommand;
import pt.isec.pa.javalife.model.data.Ecossistema;
import pt.isec.pa.javalife.model.data.IElemento;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class EcosystemFacade {

    private final PropertyChangeSupport support;
    private Ecossistema ecossistema;
    private final CommandManager cm;


    public EcosystemFacade() {
        this.cm = new CommandManager();
        this.support = new PropertyChangeSupport(this);
    }


    // Adiciona um elemento ao ecossistema
    public boolean AdicionarElemento(IElemento elemento) {
        //* fazer verificação se da para adicionar


        //boolean canAdd = ecossistema.verificaAdjacentes(element);

        //ICommand command = new AdicionarElementoCommand(ecossistema, element);
        //commandManager.execute(command);
        support.firePropertyChange("ecossistema", null, ecossistema);

        return cm.invokeCommand(new AdicionarElementoCommand(elemento));

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


}
