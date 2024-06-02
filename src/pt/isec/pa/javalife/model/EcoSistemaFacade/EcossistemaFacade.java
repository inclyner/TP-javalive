package pt.isec.pa.javalife.model.EcoSistemaFacade;


import pt.isec.pa.javalife.model.command.*;
import pt.isec.pa.javalife.model.data.Area;
import pt.isec.pa.javalife.model.data.Ecossistema;
import pt.isec.pa.javalife.model.data.Elemento;
import pt.isec.pa.javalife.model.gameengine.GameEngine;
import pt.isec.pa.javalife.model.gameengine.GameEngineState;
import pt.isec.pa.javalife.model.gameengine.IGameEngine;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;

//gerar javadoc: javadoc -d doc -sourcepath src -subpackages pt.isec.pa.javalife.model.EcoSistemaFacade

/**
 * A classe EcossistemaFacade fornece uma interface simplificada para interagir com o ecossistema e controlar a simulação.
 */
public class EcossistemaFacade {

    private final PropertyChangeSupport support; // Suporte para propriedades de mudança
    private final CommandManager cm; // Gestor de comandos
    private Ecossistema ecossistema; // O ecossistema
    private IGameEngine gameEngine; // Motor de jogo
    private int timeUnit; // Unidade de tempo da simulação

    /**
     * Cria uma nova instância de EcossistemaFacade.
     */
    public EcossistemaFacade() {
        this.cm = new CommandManager();
        this.support = new PropertyChangeSupport(this);
    }

    /**
     * Adiciona um elemento ao ecossistema.
     *
     * @param string O elemento a adicionar.
     */
    public void AdicionarElemento(String string) {
        support.firePropertyChange("adicionarElemento", null, string);
    }

    /**
     * Verifica se o ecossistema é nulo.
     *
     * @return true se o ecossistema for nulo, caso contrário false.
     */
    public boolean verificaEcossitemaNull() {
        return ecossistema == null;
    }

    /**
     * Desfaz a última ação.
     */
    public void undo() {
        if (cm.undo())
            adcionarPopUpAviso("Undo Feito");
        else
            adcionarPopUpAviso("Nao foi possivel fazer undo");
    }

    /**
     * Refaz a última ação.
     *
     * @throws IOException Se ocorrer um erro durante a execução do comando.
     */
    public void redo() throws IOException {
        if (cm.redo())
            adcionarPopUpAviso("Redo Feito");
        else
            adcionarPopUpAviso("Nao foi possivel fazer redo");
    }

    /**
     * Adiciona um listener para mudanças de propriedade.
     *
     * @param pcl O listener a adicionar.
     */
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    /**
     * Remove um listener para mudanças de propriedade.
     *
     * @param pcl O listener a remover.
     */
    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

    /**
     * Cria um novo ecossistema com as configurações especificadas.
     *
     * @param dimension      A dimensão do ecossistema.
     * @param escala         A escala do ecossistema.
     * @param timeUnit       A unidade de tempo da simulação.
     * @param initialForce   A força inicial do ecossistema.
     * @param growthRate     A taxa de crescimento do ecossistema.
     * @param overlapLoss    A perda de sobreposição do ecossistema.
     * @param movementRate   A taxa de movimento do ecossistema.
     * @throws InterruptedException Se a thread for interrompida enquanto espera.
     */
    public void createEcossistema(int dimension, double escala, int timeUnit, int initialForce, double growthRate, int overlapLoss, double movementRate) throws InterruptedException {
        ecossistema = new Ecossistema(this, dimension, initialForce, growthRate, overlapLoss, movementRate);
        gameEngine = new GameEngine();
        gameEngine.registerClient(ecossistema);
        this.timeUnit = timeUnit;
    }

    /**
     * Altera as configurações do ecossistema.
     *
     * @param initialForce A nova força inicial.
     * @param growthRate   A nova taxa de crescimento.
     * @param overlapLoss  A nova perda de sobreposição.
     * @param movementRate A nova taxa de movimento.
     */
    public void changeEcossistema(int initialForce, double growthRate, int overlapLoss, double movementRate) {
        ecossistema.setForcaInicial(initialForce);
        ecossistema.setTaxaCrescimento(growthRate);
        ecossistema.setForcaSobreposicao(overlapLoss);
        ecossistema.setVelocidade(movementRate);
    }

    /**
     * Verifica o estado atual do motor de jogo.
     *
     * @return O estado atual do motor de jogo.
     */
    public GameEngineState checkGameState() {
        return gameEngine.getCurrentState();
    }

    /**
     * Atualiza o ecossistema.
     *
     * @param string A string de atualização.
     */
    public void atualiza(String string) {
        support.firePropertyChange("atualiza", null, string);
    }

    /**
     * Adiciona um elemento ao ecossistema utilizando um comando.
     *
     * @param tipo     O tipo do elemento.
     * @param x        A coordenada x.
     * @param y        A coordenada y.
     * @param altura   A altura do elemento.
     * @param largura  A largura do elemento.
     * @param forca    A força do elemento.
     * @throws IOException Se ocorrer um erro durante a execução do comando.
     */
    public void adicionaElementoCommand(String tipo, double x, double y, double altura, double largura, double forca) throws IOException {
        if (ecossistema != null) {
            if (cm.invokeCommand(new AdicionarElementoCommand(this.ecossistema, tipo, x, y, x + altura, y + largura, forca)))
                adcionarPopUpAviso("Elemento adicionado com sucesso");
            else
                adcionarPopUpAviso("Elemento nao foi adicionado");
        } else
            adcionarPopUpAviso("Ecossistema ainda não foi criado");
    }

    /**
     * Adiciona um aviso pop-up.
     *
     * @param string A mensagem do aviso.
     */
    private void adcionarPopUpAviso(String string) {
        support.firePropertyChange("adicionarPopUpAviso", null, string);
    }
    /**
     * Pausa ou retoma a simulação do ecossistema.
     *
     * @return Uma mensagem indicando o estado da simulação.
     */
    public String pause_unpause() {
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

    /**
     * Inicia ou interrompe a simulação do ecossistema.
     */
    public void execute_stop() {
        if (ecossistema != null) {
            if (gameEngine.getCurrentState() == GameEngineState.READY)
                gameEngine.start(timeUnit);
            else if (gameEngine.getCurrentState() == GameEngineState.RUNNING) {
                gameEngine.stop();
            }
        } else
            adcionarPopUpAviso("Ecossistema ainda não foi criado");
    }

    /**
     * Remove um elemento do ecossistema utilizando um comando.
     *
     * @param tipo     O tipo do elemento a ser removido.
     * @param id       O ID do elemento a ser removido.
     * @param a        A área do elemento a ser removido.
     * @param forca    A força do elemento a ser removido.
     * @return true se o elemento foi removido com sucesso, caso contrário false.
     * @throws IOException Se ocorrer um erro durante a execução do comando.
     */
    public boolean removerElementoCommand(String tipo, int id, Area a, double forca) throws IOException {
        if (cm.invokeCommand(new RemoverElementoCommand(this.ecossistema, tipo, id, a, forca))) {
            support.firePropertyChange("atualiza", null, ecossistema);
            return true;
        }
        return false;
    }



    /**
     * Edita um elemento do ecossistema utilizando um comando.
     *
     * @param tipo       O tipo do elemento a ser editado.
     * @param id         O ID do elemento a ser editado.
     * @param velocidade A nova velocidade do elemento.
     * @param forca      A nova força do elemento.
     * @return true se o elemento foi editado com sucesso, caso contrário false.
     * @throws IOException Se ocorrer um erro durante a execução do comando.
     */
    public boolean editarElementoCommand(String tipo, int id, double velocidade, double forca) throws IOException {
        if (cm.invokeCommand(new EditarElementoCommand(this.ecossistema, tipo, id, velocidade, forca))) {
            support.firePropertyChange("atualiza", null, ecossistema);
            return true;
        }
        return false;
    }

    /**
     * Exporta a simulação do ecossistema para um arquivo.
     *
     * @param file O arquivo para exportar a simulação.
     * @throws IOException Se ocorrer um erro durante a exportação.
     */
    public void exportasimulacao(File file) throws IOException {
        cm.invokeCommand(new ExportarCommand(this.ecossistema, file));
    }

    /**
     * Importa uma simulação do ecossistema de um arquivo.
     *
     * @param selectedFile O arquivo contendo a simulação a ser importada.
     * @throws IOException Se ocorrer um erro durante a importação.
     */
    public void importasimulacao(File selectedFile) throws IOException {
        cm.invokeCommand(new ImportarCommand(this.ecossistema, selectedFile));
    }

    /**
     * Salva um snapshot do estado atual do ecossistema.
     *
     * @throws IOException Se ocorrer um erro durante a gravação do snapshot.
     */
    public void saveSnapShot() throws IOException {
        if (!cm.invokeCommand(new GravarSnapshot(ecossistema, cm.getOriginator(), cm.getCareTaker())))
            adcionarPopUpAviso("Nao foi possivel guardar snapshot");
    }

    /**
     * Carrega o snapshot anterior do estado do ecossistema.
     *
     * @return true se o snapshot anterior foi carregado com sucesso, caso contrário false.
     * @throws IOException Se ocorrer um erro durante o carregamento do snapshot.
     */
    public boolean loadSnapShotAnterior() throws IOException {
        if (cm.invokeCommand(new CarregarSnapshotCommand(ecossistema, cm.getOriginator(), cm.getCareTaker()))) {
            ecossistema.setElementos(cm.getOriginator().state);
            return true;
        } else {
            adcionarPopUpAviso("Nao existe snapshot criado");
            return false;
        }
    }

    /**
     * Atualiza o estado do ecossistema.
     */
    public void atualizaEcossistema() {
        ecossistema.atualiza();
    }

    /**
     * Aplica herbicida a um elemento do ecossistema.
     *
     * @param elemento O elemento ao qual aplicar herbicida.
     * @param id       O ID do elemento.
     * @return true se o herbicida foi aplicado com sucesso, caso contrário false.
     * @throws IOException Se ocorrer um erro durante a aplicação do herbicida.
     */
    public boolean aplicarHerbicida(Elemento elemento, int id) throws IOException {
        if (cm.invokeCommand(new AplicarHerbicidaCommand(ecossistema, elemento, id))) {
            support.firePropertyChange("atualiza", null, ecossistema);
            return true;
        }
        return false;
    }

    /**
     * Injeta força em um elemento do ecossistema.
     *
     * @param elemento O elemento ao qual injetar força.
     * @param id       O ID do elemento.
     * @param forca    A força a ser injetada.
     * @return true se a força foi injetada com sucesso, caso contrário false.
     * @throws IOException Se ocorrer um erro durante a injeção de força.
     */
    public boolean injetarForca(Elemento elemento, int id, double forca) throws IOException {
        if (cm.invokeCommand(new injetarForcaCommand(ecossistema, elemento, id, forca))) {
            support.firePropertyChange("atualiza", null, ecossistema);
            return true;
        }
        return false;
    }

    /**
     * Aplica sol ao ecossistema.
     */
    public void aplicarSol() {
        ecossistema.aplicaSol();
    }

    /**
     * Salva o estado atual do jogo.
     *
     * @param file O arquivo onde salvar o jogo.
     * @return true se o jogo foi salvo com sucesso, caso contrário false.
     * @throws IOException Se ocorrer um erro durante a gravação do jogo.
     */
    public boolean salvarJogo(File file) throws IOException {
        if (cm.invokeCommand(new SalvarJogoCommand(ecossistema, file, gameEngine.getInterval()))) {
            support.firePropertyChange("atualiza", null, ecossistema);
            return true;
        }
        return false;
    }

    /**
     * Abre um jogo previamente salvo.
     *
     * @param selectedFile O arquivo do jogo a ser aberto.
     * @return true se o jogo foi aberto com sucesso, caso contrário false.
     * @throws IOException Se ocorrer um erro durante a abertura do jogo.
     */
    public boolean abrirJogo(File selectedFile) throws IOException {
        ecossistema = new Ecossistema(this);
        if (cm.invokeCommand(new AbrirJogoCommand(ecossistema, selectedFile))) {
            gameEngine = new GameEngine();
            gameEngine.registerClient(ecossistema);
            timeUnit = ecossistema.getTimeUnit();
            support.firePropertyChange("createPane", null, ecossistema.getDimensao());
            atualizaEcossistema();
            return true;
        }
        return false;
    }

    /**
     * Configura a unidade de tempo da simulação.
     *
     * @param unitime A nova unidade de tempo.
     * @return true se a unidade de tempo foi configurada com sucesso, caso contrário false.
     */
    public boolean configurarUnitime(double unitime) {
        if(gameEngine.getCurrentState() == GameEngineState.RUNNING)
            return false;
        this.timeUnit = (int) unitime;
        if (gameEngine.getCurrentState() == GameEngineState.READY)
            gameEngine.setInterval((long) unitime);
        return true;
    }
}