package gal.sdc.usc.risk.menu;

import gal.sdc.usc.risk.menu.comandos.Ejecutor;
import gal.sdc.usc.risk.menu.comandos.preparacion.RepartirEjercito;
import gal.sdc.usc.risk.util.Colores;
import gal.sdc.usc.risk.util.Colores.Color;
import gal.sdc.usc.risk.util.Recursos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;


public class Menu extends Partida {
    public Menu() {
        // Iniciar juego
        String orden;

        boolean primero = true;

        try {
            BufferedReader bufferLector;
            File fichero = Recursos.get("comandos.csv");
            FileReader lector = new FileReader(fichero);
            bufferLector = new BufferedReader(lector);

            while ((orden = bufferLector.readLine()) != null) {
                if (orden.startsWith("#") || orden.startsWith("//")) {
                    continue;
                }

                if (!primero) {
                    System.out.println();
                } else {
                    primero = false;
                }
                this.entrada();
                System.out.println(orden);

                if (!orden.isEmpty()) {
                    Ejecutor.comando(orden);
                }
            }
            bufferLector.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scanner input = new Scanner(System.in);
        while (true) {
            if (!primero) {
                System.out.println();
            } else {
                primero = false;
            }
            this.entrada();
            orden = input.nextLine();

            if (!orden.isEmpty()) {
                Ejecutor.comando(orden);
            }
        }
    }

    private void entrada() {
        String out = "";
        if (super.isJugando() || super.getComandos().getLista().contains(RepartirEjercito.class)) {
            out += "[" + new Colores(super.getJugadorTurno().getNombre(), super.getJugadorTurno().getColor()) + "] ";
        }
        out += new Colores("$> ", Color.AMARILLO);
        System.out.print(out);
    }
}
