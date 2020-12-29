package gal.sdc.usc.risk.gui.componentes.controles;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbarLayout;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import com.jfoenix.validation.RegexValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import gal.sdc.usc.risk.comandos.Comando;
import gal.sdc.usc.risk.comandos.Ejecutor;
import gal.sdc.usc.risk.comandos.EjecutorAccion;
import gal.sdc.usc.risk.comandos.EjecutorListener;
import gal.sdc.usc.risk.comandos.IComando;
import gal.sdc.usc.risk.comandos.partida.AcabarTurno;
import gal.sdc.usc.risk.comandos.preparacion.AsignarMision;
import gal.sdc.usc.risk.comandos.preparacion.AsignarPais;
import gal.sdc.usc.risk.comandos.preparacion.CrearJugador;
import gal.sdc.usc.risk.comandos.preparacion.CrearMapa;
import gal.sdc.usc.risk.excepciones.ExcepcionRISK;
import gal.sdc.usc.risk.gui.PrincipalController;
import gal.sdc.usc.risk.gui.componentes.Utils;
import gal.sdc.usc.risk.gui.componentes.info.InfoJugador;
import gal.sdc.usc.risk.gui.componentes.mapa.MapaController;
import gal.sdc.usc.risk.gui.componentes.nuevo.NuevaMisionAsignada;
import gal.sdc.usc.risk.gui.componentes.nuevo.NuevoJugador;
import gal.sdc.usc.risk.gui.componentes.nuevo.NuevoPaisAsignado;
import gal.sdc.usc.risk.jugar.Partida;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;

public class ControlesController extends Partida {
    @FXML
    private Pane parent;
    @FXML
    private Pane contenedorComandos;
    @FXML
    private AnchorPane anchor;
    @FXML
    public JFXToggleButton toogleIndirectas;
    @FXML
    public VBox contenedorJugador;

    public ControlesController() {
    }

    public void actualizarComandos(Scene scene) {
        Pane contenedor = (Pane) scene.lookup("#contenedor-comandos");
        contenedor.getChildren().clear();
        for (Class<? extends IComando> comando : super.getComandos().getLista()) {
            JFXButton boton = new JFXButton();
            boton.setText(comando.getName());

            try {
                Object comandoObject;
                comandoObject = comando.newInstance();
                Method ejecutar = comando.getMethod("nombre");
                boton.setText((String) ejecutar.invoke(comandoObject));
            } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }

            contenedor.getChildren().add(boton);
        }
    }

    public void actualizarJugador(Scene scene) {
        VBox jugadores = (VBox) scene.lookup("#contenedor-jugador");
        jugadores.getChildren().clear();

        Parent parent = jugadores;
        while (parent.getParent() != null) parent = parent.getParent();
        assert parent instanceof StackPane;
        StackPane finalParent = (StackPane) parent;

        if (super.isJugando() || super.getComandos().getLista().contains(AcabarTurno.class)) {
            VBox contenedor = new VBox();
            contenedor.setFillWidth(true);
            contenedor.setPrefWidth(Double.MAX_VALUE);
            JFXButton button = new JFXButton(" " + super.getJugadorTurno().getNombre() + " ");
            HBox.setHgrow(button, Priority.ALWAYS);
            button.prefWidthProperty().bind(((VBox) scene.lookup("#parent-control")).widthProperty());
            button.setTextAlignment(TextAlignment.CENTER);
            button.setStyle(button.getStyle() + "-fx-text-fill: " + super.getJugadorTurno().getColor().getHex() + "; "
                    + "-fx-border-width: 3; "
                    + "-fx-border-radius: 5; "
                    + "-fx-border-color: " + super.getJugadorTurno().getColor().getHex() + "; "
                    + "-fx-font-weight: bold; "
                    + "-fx-font-size: 14; "
                    + "-fx-min-height: 20;");

            button.setOnAction((event -> this.generarDialogo(finalParent).show()));

            contenedor.getChildren().add(button);
            contenedor.getChildren().add(InfoJugador.generarJugadorCorto(super.getJugadorTurno()));
            jugadores.getChildren().add(contenedor);
        } else {
            if (super.getComandos().getLista().contains(CrearMapa.class)) {
                jugadores.getChildren().add(this.crearPreControl("Crear Mapa", MaterialDesignIcon.MAP, new EjecutorAccion() {
                    @Override
                    public void onClick(Object o) {
                        Ejecutor.comando("crear mapa", new EjecutorListener() {
                            @Override
                            public void onComandoEjecutado() {
                                Utils.actualizar(scene);
                                PrincipalController.mensaje("Mapa creado");
                            }

                            @Override
                            public void onComandoError(ExcepcionRISK e) {
                                e.printStackTrace();
                            }
                        });
                    }
                }));
            }

            if (super.getComandos().getLista().contains(CrearJugador.class)) {
                jugadores.getChildren().add(this.crearPreControl("Nuevo Jugador", MaterialDesignIcon.ACCOUNT_PLUS, new EjecutorAccion() {
                    @Override
                    public void onClick(Object o) {
                        NuevoJugador.generarDialogo(finalParent);
                    }
                }));
            }

            if (super.getComandos().getLista().contains(AsignarMision.class)) {
                jugadores.getChildren().add(this.crearPreControl("Asignar Misión", MaterialDesignIcon.ACCOUNT_CHECK, new EjecutorAccion() {
                    @Override
                    public void onClick(Object o) {
                        NuevaMisionAsignada.generarDialogo(finalParent);
                    }
                }));
            }

            if (super.getComandos().getLista().contains(AsignarPais.class)) {
                jugadores.getChildren().add(this.crearPreControl("Asignar Paises", MaterialDesignIcon.MAP_MARKER, new EjecutorAccion() {
                    @Override
                    public void onClick(Object o) {
                        NuevoPaisAsignado.generarDialogo(finalParent);
                    }
                }));
            }

            // TODO: Asignar países
        }
    }

    private JFXDialog generarDialogo(StackPane stackPane) {
        JFXDialog dialog = new JFXDialog();
        dialog.setTransitionType(JFXDialog.DialogTransition.CENTER);
        dialog.setDialogContainer(stackPane);

        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Label(super.getJugadorTurno().getNombre()));
        content.setBody(InfoJugador.generarJugador(super.getJugadorTurno()));

        JFXButton cerrar = new JFXButton("Cerrar");
        cerrar.setOnAction(event -> dialog.close());
        content.setActions(cerrar);

        dialog.setContent(content);
        return dialog;
    }

    private JFXButton crearPreControl(String nombre, MaterialDesignIcon ic, EjecutorAccion accion) {
        JFXButton boton = new JFXButton();
        boton.setRipplerFill(Paint.valueOf("black"));

        HBox contenedor = new HBox();
        contenedor.setPrefWidth(Double.MAX_VALUE);
        contenedor.setAlignment(Pos.CENTER);

        MaterialDesignIconView icono = new MaterialDesignIconView(ic);
        icono.setSize("24px");
        Label label = new Label("   " + nombre);
        label.getStyleClass().add("btn-pais-label");

        contenedor.getChildren().add(icono);
        contenedor.getChildren().add(label);

        boton.setGraphic(contenedor);
        boton.setOnAction(e -> accion.onClick(null));
        return boton;
    }

    @FXML
    private void initialize() {
        anchor.getStylesheets().add(ControlesController.class.getResource("controles.css").toExternalForm());
    }

    @FXML
    public void indirectas() {
        FXMLLoader loader = new FXMLLoader();
        try {
            loader.load(MapaController.class.getResource("mapa.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        MapaController controller = loader.getController();
        controller.actualizarFronteras(contenedorComandos.getScene(), toogleIndirectas.isSelected());
    }

    @FXML
    public void comandos() {
        JFXDialog dialog = new JFXDialog();
        dialog.setTransitionType(JFXDialog.DialogTransition.CENTER);

        Parent parent = this.parent;
        while (parent.getParent() != null) parent = parent.getParent();
        assert parent instanceof StackPane;
        dialog.setDialogContainer((StackPane) parent);

        JFXDialogLayout layout = new JFXDialogLayout();
        layout.setHeading(new Label("Introducir comando manualmente"));

        VBox contenedor = new VBox();

        JFXComboBox<Label> comboComando = new JFXComboBox<>();
        RequiredFieldValidator requiredFieldValidator = new RequiredFieldValidator();
        comboComando.getValidators().add(requiredFieldValidator);
        comboComando.setPrefWidth(Float.MAX_VALUE);
        List<Class<? extends IComando>> comandos = super.getComandos().getLista();
        comandos.sort(Comparator.comparing(Class::getName));
        for (Class<? extends IComando> comando : comandos) {
            if (comando.getName().toLowerCase().contains("obtener")
                    || comando.getName().toLowerCase().contains("describir")
                    || comando.getName().toLowerCase().contains("ayuda")
                    || comando.getName().toLowerCase().contains("ver")
                    || comando.getName().toLowerCase().contains("salir")) {
                continue;
            }

            try {
                Object comandoObject;
                comandoObject = comando.newInstance();
                Method ejecutar = comando.getMethod("nombre");
                Label label = new Label((String) ejecutar.invoke(comandoObject));
                label.setId(comando.getName());
                comboComando.getItems().add(label);
            } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        comboComando.setPromptText("Seleccionar comando");
        comboComando.validate();
        contenedor.getChildren().add(comboComando);

        JFXTextField comandoTexto = new JFXTextField();
        comandoTexto.setStyle(comandoTexto.getStyle() + "-fx-padding: 20pt 0 5pt 0;");
        comandoTexto.setDisable(true);
        comandoTexto.setLabelFloat(true);
        comandoTexto.setPromptText("Comando");
        RegexValidator validador = new RegexValidator();
        validador.setMessage("El formato no es correcto");
        comandoTexto.getValidators().add(validador);
        comandoTexto.textProperty().addListener((o) -> comandoTexto.validate());
        contenedor.getChildren().add(comandoTexto);

        VBox errorContenedor = new VBox();
        errorContenedor.setStyle(errorContenedor.getStyle() + "-fx-padding: 15pt 0 10pt 0;");
        errorContenedor.setPrefWidth(Float.MAX_VALUE);
        errorContenedor.setVisible(false);
        errorContenedor.setManaged(false);

        HBox error = new HBox();
        error.setPrefWidth(Float.MAX_VALUE);
        error.setStyle(error.getStyle() + "-fx-padding: 10pt 5pt 10pt 5pt; "
                + "-fx-border-width: 1pt; "
                + "-fx-border-radius: 5pt;"
                + "-fx-border-color: #d32f2f;");
        HBox.setHgrow(error, Priority.ALWAYS);
        Label errorTitulo = new Label("Error");
        errorTitulo.getStyleClass().add("dialogo-titulo");
        errorTitulo.getStyleClass().add("error");
        error.getChildren().add(errorTitulo);
        Label errorValor = new Label();
        HBox.setHgrow(errorValor, Priority.ALWAYS);
        errorValor.getStyleClass().add("dialogo-valor");
        error.getChildren().add(errorValor);
        errorContenedor.getChildren().add(error);
        contenedor.getChildren().add(errorContenedor);

        comboComando.getSelectionModel().selectedItemProperty().addListener((o, oldV, newV) -> {
            comandoTexto.setDisable(false);
            errorContenedor.setVisible(false);
            errorContenedor.setManaged(false);
            try {
                Class<? extends IComando> comando = (Class<? extends IComando>) Class.forName(newV.getId());
                IComando comandoObj = comando.newInstance();
                if (comando.isAnnotationPresent(Comando.class)) {
                    Comando comandoA = comando.getAnnotation(Comando.class);
                    validador.setRegexPattern(comandoA.comando().getRegex());
                }
                comandoTexto.setText(comandoObj.ayuda());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });

        layout.setBody(contenedor);

        JFXButton ejecutar = new JFXButton("Ejecutar");
        ejecutar.disableProperty().bind(comboComando.getValidators().get(0).hasErrorsProperty()
                .or(comandoTexto.getValidators().get(0).hasErrorsProperty()));
        ejecutar.setOnAction(event -> {
            errorContenedor.setVisible(false);
            errorContenedor.setManaged(false);
            Ejecutor.comando(comandoTexto.getText(), new EjecutorListener() {
                @Override
                public void onComandoError(ExcepcionRISK e) {
                    errorTitulo.setText("Error " + e.getCodigo());
                    errorValor.setText(e.getMensaje());
                    errorContenedor.setVisible(true);
                    errorContenedor.setManaged(true);
                }

                @Override
                public void onComandoEjecutado() {
                    Utils.actualizar(contenedor.getScene());
                    dialog.close();

                    PrincipalController.mensaje("[OK] " + comandoTexto.getText());
                }
            });
        });

        JFXButton cerrar = new JFXButton("Cerrar");
        cerrar.setOnAction(event -> dialog.close());

        layout.setActions(cerrar, ejecutar);

        dialog.setContent(layout);
        dialog.show();
    }
}
