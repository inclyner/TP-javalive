package pt.isec.pa.javalife.ui.gui.res;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pt.isec.pa.javalife.model.EcoSistemaFacade.EcossistemaFacade;
import pt.isec.pa.javalife.model.data.Area;
import pt.isec.pa.javalife.model.data.Elemento;
import pt.isec.pa.javalife.model.gameengine.GameEngineState;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainJFX extends Application implements PropertyChangeListener {

    private static EcossistemaFacade ecossistemaFacade = new EcossistemaFacade();
    private int unidade_generica;
    private double escala; // dimensao da janela / unidades genericas
    private int timeUnit;
    private int initialForce;
    private double growthRate;
    private int overlapLoss;
    private double movementRate;
    private Scene scene;
    private BorderPane root;
    private Pane pane;
    private Stage primaryStage;
    private Map<String, Button> listButtons = new HashMap<>();
    private Map<String, Label> listLabels = new HashMap<>();
    private Button finalButton;
    private int valorReduzirJanela = 50;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Simulação de Ecossistema");
        // Menu Ficheiro
        Menu menuFicheiro = new Menu("Ficheiro");
        MenuItem criarItem = new MenuItem("Criar");
        MenuItem abrirItem = new MenuItem("Abrir");
        MenuItem gravarItem = new MenuItem("Gravar");
        MenuItem exportarItem = new MenuItem("Exportar");
        MenuItem importarItem = new MenuItem("Importar");
        MenuItem sairItem = new MenuItem("Sair");
        menuFicheiro.getItems().addAll(criarItem, abrirItem, gravarItem, new SeparatorMenuItem(), exportarItem, importarItem, new SeparatorMenuItem(), sairItem);
        // Menu Ecossistema
        Menu menuEcossistema = new Menu("Ecossistema");
        MenuItem configGeraisItem = new MenuItem("Configurações gerais do ecossistema");
        MenuItem adicionarInanimadoItem = new MenuItem("Adicionar elemento inanimado");
        MenuItem adicionarFloraItem = new MenuItem("Adicionar elemento flora");
        MenuItem adicionarFaunaItem = new MenuItem("Adicionar elemento fauna");
        //MenuItem editarElementoItem = new MenuItem("Editar elemento");
        //MenuItem eliminarElementoItem = new MenuItem("Eliminar elemento");
        MenuItem undoItem = new MenuItem("Undo");
        MenuItem redoItem = new MenuItem("Redo");
        menuEcossistema.getItems().addAll(configGeraisItem, adicionarInanimadoItem, adicionarFloraItem, adicionarFaunaItem, new SeparatorMenuItem(), undoItem, redoItem);
        // Menu Simulação
        Menu menuSimulacao = new Menu("Simulação");
        MenuItem configSimulacaoItem = new MenuItem("Configuração da simulação");
        MenuItem executarPararItem = new MenuItem("Executar/Parar");
        MenuItem pausarContinuarItem = new MenuItem("Pausar/Continuar");
        MenuItem gravarSnapshotItem = new MenuItem("Gravar snapshot");
        MenuItem restaurarSnapshotItem = new MenuItem("Restaurar snapshot");
        menuSimulacao.getItems().addAll(configSimulacaoItem, executarPararItem, pausarContinuarItem, new SeparatorMenuItem(), gravarSnapshotItem, restaurarSnapshotItem);
        // Menu Eventos
        Menu menuEventos = new Menu("Eventos");
        MenuItem aplicarSolItem = new MenuItem("Aplicar Sol");
        MenuItem aplicarHerbicidaItem = new MenuItem("Aplicar herbicida");
        MenuItem injetarForcaItem = new MenuItem("Injetar força");
        menuEventos.getItems().addAll(aplicarSolItem, aplicarHerbicidaItem, injetarForcaItem);

        // MenuBar
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(menuFicheiro, menuEcossistema, menuSimulacao, menuEventos);

        // Layout principal
        root = new BorderPane();
        root.setTop(menuBar);

        // Placeholder para a área de simulação
        //Label areaSimulacao = new Label("Área de Simulação");
        //areaSimulacao.setStyle("-fx-border-color: black; -fx-alignment: center; -fx-padding: 20px;");
        //root.setCenter(areaSimulacao);

        // Scene
        scene = new Scene(root, 800, 600);
        //stage = primaryStage;
        // Configuração da Stage
        primaryStage.setScene(scene);
        primaryStage.show();


        // Adicionar funcionalidade aos itens de menu
        criarItem.setOnAction(event -> criarSimulacao());
        abrirItem.setOnAction(event -> abrirSimulacao(primaryStage));
        gravarItem.setOnAction(event -> gravarSimulacao(primaryStage));
        exportarItem.setOnAction(event -> exportarSimulacao(primaryStage));
        importarItem.setOnAction(event -> importarSimulacao(primaryStage));
        sairItem.setOnAction(event -> sairSimulacao(primaryStage));

        configGeraisItem.setOnAction(event -> configurarEcossistema());
        adicionarInanimadoItem.setOnAction(event -> adicionarElemento("INANIMADO"));
        adicionarFloraItem.setOnAction(event -> adicionarElemento("FLORA"));
        adicionarFaunaItem.setOnAction(event -> adicionarElemento("FAUNA"));
        //editarElementoItem.setOnAction(event -> editarElemento());
        //eliminarElementoItem.setOnAction(event -> eliminarElemento());
        undoItem.setOnAction(event -> undo());
        redoItem.setOnAction(event -> {
            try {
                redo();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        configSimulacaoItem.setOnAction(event -> configurarSimulacao());
        executarPararItem.setOnAction(event -> executarPararSimulacao());
        pausarContinuarItem.setOnAction(event -> pausarContinuarSimulacao());
        gravarSnapshotItem.setOnAction(event -> {
            try {
                gravarSnapshot();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        restaurarSnapshotItem.setOnAction(event -> {
            try {
                restaurarSnapshot();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        aplicarSolItem.setOnAction(event -> aplicarSol());
        aplicarHerbicidaItem.setOnAction(event -> aplicarHerbicida());
        injetarForcaItem.setOnAction(event -> injetarForca());
        ecossistemaFacade.addPropertyChangeListener(this);
    }

    //region fucções menu
    private void criarSimulacao() {
        // Lógica para criar uma nova simulação
        showParameterPopup(true);
    }


    private void abrirSimulacao(Stage stage) {
        // Lógica para abrir uma simulação existente
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            //Funcao que le o ficheiro
        }
    }

    private void gravarSimulacao(Stage stage) {
        // Lógica para gravar o estado atual da simulação
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            //Funçao que guarda o ficheiro
        }
    }

    private void exportarSimulacao(Stage stage) {
        // Lógica para exportar a simulação para um ficheiro CSV
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            // Função que exporta os elementos
            ecossistemaFacade.exportasimulação(file);
        }
    }

    private void importarSimulacao(Stage stage) {
        // Lógica para importar uma simulação de um ficheiro CSV
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            //Funcao que le o ficheiro mas apenas coloca os elementos(sem ser os sobrepostos)
            ecossistemaFacade.importasimulação(selectedFile);
        }
    }

    private void sairSimulacao(Stage stage) {
        // Lógica para sair da simulação, perguntando se deseja gravar
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Deseja gravar antes de sair?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            gravarSimulacao(stage);
            stage.close();
        } else if (alert.getResult() == ButtonType.NO) {
            stage.close();
        }
    }

    private void configurarEcossistema() {
        // Lógica para configurar o ecossistema
        if(!ecossistemaFacade.verificaEcossitemaNull()) {
            if (ecossistemaFacade.checkGameState() == GameEngineState.READY)
                showParameterPopup(false);
            else {
                createPopUPInfo("O jogo não está em parado", "Game Status");
            }
        }else
            createPopUPInfo("Ecossistema ainda nao foi criado", "Game Status");
    }

    private void adicionarElemento(String tipo) {
        // Criar um diálogo para adicionar um elemento
        if(!ecossistemaFacade.verificaEcossitemaNull()) {
        Dialog<Elemento> dialog = new Dialog<>();
        dialog.setTitle("Adicionar Elemento " + tipo);
        dialog.setHeaderText("Insira os detalhes do novo elemento " + tipo);

        // Definir os botões do diálogo
        ButtonType adicionarButtonType = new ButtonType("Adicionar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(adicionarButtonType, ButtonType.CANCEL);

        // Criar os campos de entrada
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));


        TextField x = new TextField();
        x.setPromptText("Posição X");
        TextField y = new TextField();
        y.setPromptText("Posição Y");
        TextField altura = new TextField();
        altura.setPromptText("Altura");
        TextField largura = new TextField();
        largura.setPromptText("Largura");
        TextField forca = new TextField();
        forca.setPromptText("Forca Inicial");


        grid.add(new Label("Posição X:"), 0, 1);
        grid.add(x, 1, 1);
        grid.add(new Label("Posição Y:"), 0, 2);
        grid.add(y, 1, 2);
        grid.add(new Label("Altura:"), 0, 3);
        grid.add(altura, 1, 3);
        grid.add(new Label("Largura:"), 0, 4);
        grid.add(largura, 1, 4);
        if(!tipo.equalsIgnoreCase("inanimado") ) {
            grid.add(new Label("Forca Inicial:"), 0, 5);
            grid.add(forca, 1, 5);
        }
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == adicionarButtonType) {
                try {
                    //trocado porque assim funciona
                    double posX = Double.parseDouble(y.getText());
                    double posY = Double.parseDouble(x.getText());
                    double alt = Double.parseDouble(altura.getText());
                    double larg = Double.parseDouble(largura.getText());
                    double forcaInicial=0;
                    if(!tipo.equalsIgnoreCase("inanimado"))
                        forcaInicial = Double.parseDouble(forca.getText());

                    // Adicionar o elemento ao ecossistema
                    ecossistemaFacade.adicionaElementoCommand(tipo, posX, posY, alt, larg, forcaInicial);

                } catch (NumberFormatException e) {
                    // Tratar erro de formatação
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erro de Formatação");
                    alert.setHeaderText("Valores Inválidos");
                    alert.setContentText("Por favor, insira valores numéricos válidos.");
                    alert.showAndWait();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return null;
        });

        // Mostrar o diálogo e esperar pela resposta do utilizador
        dialog.showAndWait();
        }else
            createPopUPInfo("Ecossistema ainda nao foi criado", "Game Status");
    }

    private void undo() {
        // Lógica para desfazer a última ação
        ecossistemaFacade.undo();
    }

    private void redo() throws IOException {
        ecossistemaFacade.redo();
    }

    private void configurarSimulacao() {

    }

    private void executarPararSimulacao() {
        if(!ecossistemaFacade.verificaEcossitemaNull())
            ecossistemaFacade.execute_stop();
        else
            createPopUPInfo("Nao existe ecossitema criado", "Game Status");
    }

    private void pausarContinuarSimulacao() {
        String string = ecossistemaFacade.pause_unpause();
        if (string != null){
            primaryStage.setTitle(string);
        }
    }

    private void gravarSnapshot() throws IOException {
        ecossistemaFacade.saveSnapShot();
    }

    private void restaurarSnapshot() throws IOException {
        if(ecossistemaFacade.loadSnapShotAnterior()){
            listButtons.clear();
            listLabels.clear();
            ecossistemaFacade.atualiza();
        }
    }

    private void aplicarSol() {
    }

    private void aplicarHerbicida() {
    }


    private void injetarForca() {
    }

    //endregion

   /* private void createGridScene(Stage primaryStage, int width) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setMaxSize(width * 50, width * 50); // Ajusta a largura e altura da grade de acordo com a entrada do usuário

        // Adicionando linhas e colunas à grade
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                grid.add(new Label("[" + i + "," + j + "]"), i, j);
            }
        }

        Scene scene = new Scene(grid);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    */


    private void showParameterPopup(boolean permiteAlteracoes) {
        // Function to create and show the pop-up
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Ecosystem Parameter Collector");
        // Create the GridPane layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);
        // Create labels and text fields for each parameter
        Label dimensionLabel = new Label("Dimensao do Ecossistema:");
        GridPane.setConstraints(dimensionLabel, 0, 1);
        Slider dimensionSlider = new Slider();
        dimensionSlider.setMin(0);
        if(scene.getWidth()<scene.getHeight()) {
            dimensionSlider.setMax(scene.getWidth() - valorReduzirJanela); // Assume 800 como o valor máximo da cena, ajuste conforme necessário
            dimensionSlider.setValue(scene.getWidth() / 2);
        }else{
            dimensionSlider.setMax(scene.getHeight() - valorReduzirJanela); // Assume 800 como o valor máximo da cena, ajuste conforme necessário
            dimensionSlider.setValue(scene.getHeight() / 2);
        }
        dimensionSlider.setShowTickMarks(true);
        dimensionSlider.setShowTickLabels(true);
        dimensionSlider.setMajorTickUnit(200);
        dimensionSlider.setMinorTickCount(4);
        dimensionSlider.setBlockIncrement(1);
        GridPane.setConstraints(dimensionSlider, 1, 1);
        Label timeUnitLabel = new Label("Unidade de Tempo(ms):");
        GridPane.setConstraints(timeUnitLabel, 0, 3);
        if (!permiteAlteracoes) {
            dimensionSlider.setDisable(true);
            timeUnitLabel.setDisable(true);
        }
        TextField timeUnitInput = new TextField("1000");
        GridPane.setConstraints(timeUnitInput, 1, 3);
        Label initialForceLabel = new Label("Valor Inicial da Força:");
        GridPane.setConstraints(initialForceLabel, 0, 4);
        TextField initialForceInput = new TextField("50");
        GridPane.setConstraints(initialForceInput, 1, 4);
        Label growthRateLabel = new Label("Valor de Crescimento da Flora:");
        GridPane.setConstraints(growthRateLabel, 0, 5);
        TextField growthRateInput = new TextField("0.5");
        GridPane.setConstraints(growthRateInput, 1, 5);
        Label overlapLossLabel = new Label("Valor de Perda por Sobreposição:");
        GridPane.setConstraints(overlapLossLabel, 0, 6);
        TextField overlapLossInput = new TextField("1");
        GridPane.setConstraints(overlapLossInput, 1, 6);
        Label movementRateLabel = new Label("Valor de Movimentação da Fauna:");
        GridPane.setConstraints(movementRateLabel, 0, 7);
        TextField movementRateInput = new TextField("0.5");
        GridPane.setConstraints(movementRateInput, 1, 7);
        // Create the enter button
        Button enterButton = new Button("Enter");
        GridPane.setConstraints(enterButton, 1, 8);
        enterButton.setOnAction(e -> {
            unidade_generica = (int) (dimensionSlider.getValue());
            timeUnit = Integer.parseInt(timeUnitInput.getText());
            initialForce = Integer.parseInt(initialForceInput.getText());
            growthRate = Double.parseDouble(growthRateInput.getText());
            overlapLoss = Integer.parseInt(overlapLossInput.getText());
            movementRate = Double.parseDouble(movementRateInput.getText());
            if(scene.getWidth()<scene.getHeight())
                escala = (scene.getWidth() - valorReduzirJanela) / unidade_generica;
            else
                escala = (scene.getHeight() - valorReduzirJanela) / unidade_generica;
            if (permiteAlteracoes) {
                if(!listButtons.isEmpty()) {
                    for (Button button : listButtons.values()) {
                        pane.getChildren().removeAll(button);
                    }
                    listButtons.clear();
                    for(Label label : listLabels.values()) {
                        pane.getChildren().removeAll(label);
                    }
                    listLabels.clear();
                }
                desenharEcossistema();
                try {
                    ecossistemaFacade.createEcossistema(unidade_generica, escala, timeUnit, initialForce, growthRate, overlapLoss, movementRate);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                ecossistemaFacade.changeEcossistema(initialForce, growthRate, overlapLoss, movementRate);
            }
            //region Print or use the collected values
            System.out.println("Dimensão do Ecossistema: " + unidade_generica);
            System.out.println("Unidade de Tempo: " + timeUnit);
            System.out.println("Valor Inicial da Força: " + initialForce);
            System.out.println("Valor de Crescimento da Flora: " + growthRate);
            System.out.println("Valor de Perda por Sobreposição: " + overlapLoss);
            System.out.println("Valor de Movimentação da Fauna: " + movementRate);
            System.out.println("escala = " + escala);
            //endregion
            // Close the pop-up
            popupStage.close();
        });
        // Add all elements to the grid
        grid.getChildren().addAll(dimensionLabel, dimensionSlider, timeUnitLabel, timeUnitInput, initialForceLabel, initialForceInput, growthRateLabel, growthRateInput, overlapLossLabel, overlapLossInput, movementRateLabel, movementRateInput, enterButton);
        // Set up the scene and stage for the pop-up
        Scene popupScene = new Scene(grid, 400, 300);
        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }


    private void desenharEcossistema() {
        pane = new Pane();
        pane.setStyle("-fx-background-color: lightblue;");// Define a cor de fundo desejada
        if(scene.getWidth()<scene.getHeight())
            pane.setMaxSize(scene.getWidth() - valorReduzirJanela, scene.getWidth() - valorReduzirJanela);
        else
            pane.setMaxSize(scene.getHeight() - valorReduzirJanela, scene.getHeight()-valorReduzirJanela);
        root.setCenter(pane);
    }


    public void main(String[] args) {
        launch(args);
    }

    private void createeAtualizaElemento(String string) {

        Pattern idPattern = Pattern.compile("id:(\\d+)");
        Pattern typePattern = Pattern.compile("type:([A-Z]+)");
        Pattern areaPattern = Pattern.compile("area:\\(([^)]+)\\)");
        Pattern forcaPattern = Pattern.compile("forca:(\\d+)");
        Matcher idMatcher = idPattern.matcher(string);
        Matcher typeMatcher = typePattern.matcher(string);
        Matcher areaMatcher = areaPattern.matcher(string);
        Matcher forcaMatcher = forcaPattern.matcher(string);
        if (idMatcher.find() && typeMatcher.find() && areaMatcher.find()) {
            Label forcaLabel = new Label();
            Button button = new Button();
            // Extrair as informações
            String id = idMatcher.group(1);
            String type = typeMatcher.group(1);
            String area = areaMatcher.group(1);
            String forca = null;
            // Dividir e converter os valores da área
            String[] areaValues = area.split(";");
            double x = Double.parseDouble(areaValues[0]);
            double y = Double.parseDouble(areaValues[1]);
            double width = Double.parseDouble(areaValues[3]) - Double.parseDouble(areaValues[1]);
            double height = Double.parseDouble(areaValues[2]) - Double.parseDouble(areaValues[0]);
            if (forcaMatcher.find()) {
                forca = forcaMatcher.group(1);
                if(type.equals(Elemento.FAUNA.toString())) {
                    forcaLabel.setText(forca);
                    forcaLabel.layoutXProperty().bind(button.layoutXProperty().add(button.widthProperty()));
                    forcaLabel.layoutYProperty().bind(button.layoutYProperty());
                    pane.getChildren().add(forcaLabel);
                }
            }
            button.setLayoutY(x * escala); // Posição X do botão no Pane
            button.setLayoutX(y * escala);// Posição Y do botão no Pane
            button.setMinWidth(width * escala);
            button.setPrefWidth(width * escala);
            button.setMaxWidth(width * escala);
            button.setMinHeight(height * escala);
            button.setPrefHeight(height * escala);
            button.setMaxHeight(height * escala);
           if (!(x == 0 || y == 0 || Double.parseDouble(areaValues[2]) == unidade_generica || Double.parseDouble(areaValues[3]) == unidade_generica)) {
                button.setOnAction(actionEvent -> {
                    // chama funcao que abre pagina do elemento
                    PaginaElemento(Elemento.valueOf(type), Integer.parseInt(id));
                });
            }
            if (type.equals(Elemento.INANIMADO.toString())) {
                if (listButtons.containsKey(Elemento.INANIMADO + id)) {
                    pane.getChildren().remove(listButtons.get(Elemento.INANIMADO + id));
                    listButtons.remove(type+id);
                }else {
                    button.setStyle("-fx-background-color: #505050;");// Definir a cor de fundo do botão como cinzento para tipo inanimado
                    listButtons.put(Elemento.INANIMADO + id, button);
                    pane.getChildren().add(button);
                }
            } else if (type.equals(Elemento.FLORA.toString())) {
                int intensidade =100- (100 -Integer.parseInt(forca)) ;
                Color cor = Color.rgb(0, 128, 0,intensidade/100.0);
                // Converte a cor para uma string em formato hexadecimal
                String corRgba = String.format("rgba(0, 128, 0, %.2f)", intensidade/100.0);
                button.setStyle("-fx-background-color: " + corRgba + ";");
                if (listButtons.containsKey(Elemento.FLORA + id)) {
                    pane.getChildren().remove(listButtons.get(Elemento.FLORA + id));
                    listButtons.remove(type+id);
                }
                if(Double.parseDouble(forca)>0) {
                    button.setStyle("-fx-background-color: #008000;");// Definir a cor de fundo do botão como verde para tipo flora
                    listButtons.put(Elemento.FLORA + id, button);
                    pane.getChildren().add(button);
                }
            } else if (type.equals(Elemento.FAUNA.toString())) {
                if (listButtons.containsKey(Elemento.FAUNA + id) && listLabels.containsKey(Elemento.FAUNA + id)) {
                    pane.getChildren().remove(listButtons.get(Elemento.FAUNA + id));
                    pane.getChildren().remove(listLabels.get(Elemento.FAUNA + id));
                    listButtons.remove(type+id);
                    listLabels.remove(type+id);
                }
                if(Double.parseDouble(forca)>0) {
                    button.setStyle("-fx-background-color: #800000;");// Definir a cor de fundo do botão como vermelho para tipo fauna
                    listButtons.put(Elemento.FAUNA + id, button);
                    listLabels.put(Elemento.FAUNA + id, forcaLabel);
                    pane.getChildren().add(button);
                }
            }
        }
    }

    private void createPopUPInfo(String context, String title) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (title != null) {
            alert.setTitle(title);
        }
        alert.setContentText(context);
        alert.showAndWait();
    }


    public void PaginaElemento(Elemento elemento, int id) {
        Stage stage = new Stage();
        stage.setTitle("Página do Elemento");

        // Título estilizado
        Label titleLabel = new Label("Página do Elemento");
        titleLabel.setFont(new Font("Arial", 24));
        titleLabel.setPadding(new Insets(10, 0, 20, 0));

        // Informações do elemento
        Label label = new Label("Elemento: " + elemento + ", ID: " + id);
        label.setFont(new Font("Arial", 18));
        label.setPadding(new Insets(0, 0, 10, 0));

        // Botão de eliminar
        Button eliminarButton = new Button("Eliminar");
        eliminarButton.setFont(new Font("Arial", 14));
        eliminarButton.setStyle("-fx-background-color: #ff4c4c; -fx-text-fill: white;");
        eliminarButton.setOnAction(e -> {
            // Lógica de eliminação
            Dialog<Integer> dialog = new Dialog<>();
            dialog.setTitle("Eliminar Elemento");
            dialog.setHeaderText("Tem a certeza que quer eliminar o elemento " + elemento + ", ID: " + id + " ?");
            ButtonType removerButtonType = new ButtonType("Remover", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(removerButtonType, ButtonType.CANCEL);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == removerButtonType) {
                    return id;
                }
                return null;
            });

            Optional<Integer> result = dialog.showAndWait();
            result.ifPresent(value -> {
                Button button = listButtons.get(elemento.toString()+id);
                Area a = new Area(button.getLayoutY()/escala, button.getLayoutX()/escala, (button.getLayoutY()+ button.getHeight())/escala, (button.getLayoutX()+button.getWidth())/escala);
                double forca=0;
                if(listLabels.containsKey(elemento.toString()+id))
                    forca = Double.parseDouble(listLabels.get(elemento.toString()+id).getText());
                boolean success = false;
                try {
                    success = ecossistemaFacade.removerElementoCommand(elemento.toString(), value, a, forca);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                if (success) {
                    createPopUPInfo("O elemento com o ID " + id + " foi removido com sucesso.", "Elemento Removido");
                    pane.getChildren().remove(listButtons.get(elemento.toString() + id));
                    if(elemento.toString().equalsIgnoreCase(Elemento.FAUNA.toString()))
                        pane.getChildren().remove(listLabels.get(elemento.toString() + id));
                } else {
                    createPopUPInfo("O elemento com o ID " + id + " não pôde ser removido.", "Erro ao Remover");
                }
                stage.close();
            });
        });

        // Botão de editar
        Button editarButton = new Button("Editar");
        editarButton.setFont(new Font("Arial", 14));
        editarButton.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white;");
        editarButton.setOnAction(e -> {
            // Lógica de edição
            Dialog<Map<String, Object>> dialog = new Dialog<>();
            dialog.setTitle("Editar Elemento");
            dialog.setHeaderText("Insira os novos valores para o elemento");

            ButtonType editarButtonType = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(editarButtonType, ButtonType.CANCEL);

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            TextField direcaoField = new TextField();
            direcaoField.setPromptText("Direção");

            TextField velocidadeField = new TextField();
            velocidadeField.setPromptText("Velocidade");

            TextField forcaField = new TextField();
            forcaField.setPromptText("Força");

            if (elemento.toString().equalsIgnoreCase("fauna")) {
                grid.add(new Label("Direção:"), 0, 0);
                grid.add(direcaoField, 1, 0);
                grid.add(new Label("Velocidade:"), 0, 1);
                grid.add(velocidadeField, 1, 1);
                grid.add(new Label("Força:"), 0, 2);
                grid.add(forcaField, 1, 2);
            } else if (elemento.toString().equalsIgnoreCase("flora")) {
                grid.add(new Label("Força:"), 0, 0);
                grid.add(forcaField, 1, 0);
            }

            dialog.getDialogPane().setContent(grid);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == editarButtonType) {
                    try {
                        Map<String, Object> resultMap = new HashMap<>();
                        if (elemento.toString().equalsIgnoreCase("fauna")) {
                            resultMap.put("direcao", Double.parseDouble(direcaoField.getText()));
                            resultMap.put("velocidade", Double.parseDouble(velocidadeField.getText()));
                            resultMap.put("forca", Double.parseDouble(forcaField.getText()));
                        } else if (elemento.toString().equalsIgnoreCase("flora")) {
                            resultMap.put("forca", Double.parseDouble(forcaField.getText()));
                        }
                        return resultMap;
                    } catch (NumberFormatException e3) {
                        return null;
                    }
                }
                return null;
            });

            Optional<Map<String, Object>> result = dialog.showAndWait();
            result.ifPresent(values -> {
                boolean success = false;
                if (elemento.toString().equalsIgnoreCase("fauna")) {
                    double direcao = (double) values.get("direcao");
                    double velocidade = (double) values.get("velocidade");
                    double forca = (double) values.get("forca");
                    try {
                        success = ecossistemaFacade.editarElementoCommand(elemento.toString(), id, direcao, velocidade, forca);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                } else if (elemento.toString().equalsIgnoreCase("flora")) {
                    double forca = (double) values.get("forca");
                    try {
                        success = ecossistemaFacade.editarElementoCommand(elemento.toString(), id, 0, 0, forca);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Resultado da Operação");

                if (success) {
                    alert.setHeaderText("Elemento Editado");
                    alert.setContentText("O elemento com o ID " + id + " foi editado com sucesso.");
                } else {
                    alert.setHeaderText("Erro ao Editar");
                    alert.setContentText("O elemento com o ID " + id + " não existe ou não pôde ser editado.");
                }

                alert.showAndWait();
                stage.close();
            });
        });

        if (elemento.toString().equalsIgnoreCase("inanimado")) {
            editarButton.setDisable(true);
        }

        // Layout com VBox
        VBox layout = new VBox(20); // Espaçamento entre elementos
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(titleLabel, label, eliminarButton, editarButton);

        Scene scene = new Scene(layout, 400, 300);
        stage.setScene(scene);
        stage.show();
    }



    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("adicionarElemento") || evt.getPropertyName().equals("atualiza"))
            createeAtualizaElemento(evt.getNewValue().toString());
        if (evt.getPropertyName().equals("adicionarPopUpAviso")) createPopUPInfo(evt.getNewValue().toString(), null);
    }
}

