package gal.sdc.usc.risk.gui.componentes.mapa;

import com.jfoenix.controls.JFXButton;
import gal.sdc.usc.risk.comandos.Ejecutor;
import gal.sdc.usc.risk.gui.componentes.infopais.InfoPais;
import gal.sdc.usc.risk.jugar.Partida;
import gal.sdc.usc.risk.tablero.Celda;
import gal.sdc.usc.risk.tablero.Pais;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.HashMap;

import static gal.sdc.usc.risk.tablero.Mapa.MAX_PAISES_X;
import static gal.sdc.usc.risk.tablero.Mapa.MAX_PAISES_Y;

public class MapaController extends Partida {
    @FXML
    private VBox contenedor;

    public MapaController() {
    }

    @FXML
    private void initialize() {
        Ejecutor.comando("crear mapa", () -> {
            HashMap<Celda, Pais> paises = super.getMapa().getPaisesPorCeldas();
            for (int i = 0; i < MAX_PAISES_Y; i++) {
                for (int j = 0; j < MAX_PAISES_X; j++) {
                    Celda celda = new Celda.Builder().withX(j).withY(i).build();

                    Pais norte = paises.get(new Celda.Builder().withX(j).withY(i - 1).build());
                    Pais sur = paises.get(new Celda.Builder().withX(j).withY(i + 1).build());
                    Pais este = paises.get(new Celda.Builder().withX(j - 1).withY(i).build());
                    Pais oeste = paises.get(new Celda.Builder().withX(j + 1).withY(i).build());
                    Pais sombra = paises.get(new Celda.Builder().withX(j + 1).withY(i + 1).build());

                    JFXButton button = (JFXButton) contenedor.getScene().lookup("#" + i + "-" + j);
                    button.setDisable(true);
                    if (paises.containsKey(celda)) {
                        button.setDisable(false);
                        Pais pais = paises.get(celda);
                        button.setStyle(button.getStyle() + "-fx-background-color: " + pais.getColor().getHex() + ";");

                        String bordes = bordes(norte != null, sur != null, este != null, oeste != null);
                        button.setStyle(button.getStyle()
                                + "-fx-border-radius: " + bordes + ";"
                                + "-fx-background-radius: " + bordes + ";");

                        if (sur == null || oeste == null || sombra == null) {
                            button.getStyleClass().add("pais-sombra");
                        }

                        button.getStyleClass().add("pais");
                        button.setText(pais.getAbreviatura());

                        button.setOnAction(action -> {
                            Parent parent = button;
                            while (parent.getParent() != null) parent = parent.getParent();
                            assert parent instanceof StackPane;
                            InfoPais.dialogo((StackPane) parent, pais).show();
                        });
                    }
                }
            }
        });
    }

    private String bordes(boolean norte, boolean sur, boolean este, boolean oeste) {
        boolean supizq = !norte && !este;
        boolean supder = !norte && !oeste;
        boolean infizq = !sur && !este;
        boolean infder = !sur && !oeste;

        StringBuilder out = new StringBuilder();

        if (supizq) out.append("5pt ");
        else out.append("0pt ");
        if (supder) out.append("5pt ");
        else out.append("0pt ");
        if (infder) out.append("5pt ");
        else out.append("0pt ");
        if (infizq) out.append("5pt ");
        else out.append("0pt ");

        return out.toString();
    }
}
