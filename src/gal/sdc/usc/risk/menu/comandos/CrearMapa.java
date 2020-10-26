package gal.sdc.usc.risk.menu.comandos;

import gal.sdc.usc.risk.menu.Comando;
import gal.sdc.usc.risk.tablero.Celda;
import gal.sdc.usc.risk.tablero.Continente;
import gal.sdc.usc.risk.tablero.Continentes;
import gal.sdc.usc.risk.tablero.Mapa;
import gal.sdc.usc.risk.tablero.Pais;
import gal.sdc.usc.risk.tablero.Paises;


public class CrearMapa implements Comando {
    public CrearMapa() {
        Mapa.Builder preMapa = new Mapa.Builder();

        // Primero continentes
        for (Continentes continente : Continentes.values()) {
            Continente.Builder preContinente = new Continente.Builder(continente)
                    .withNombre(continente.getNombre())
                    .withColor(continente.getColor());

            // Luego países del continente
            for (Paises pais : Paises.values()) {
                if (pais.getContinente() != continente) {
                    continue;
                }

                Celda celda = new Celda.Builder()
                        .withX(pais.getX())
                        .withY(pais.getY())
                        .build();

                Pais nuevoPais = new Pais.Builder(pais)
                        .withNombre(pais.getNombre())
                        .withCelda(celda)
                        .build();
                preContinente.withPais(nuevoPais);
                preMapa.withPais(celda, nuevoPais);
            }

            Continente nuevoContinente = preContinente.build();
            for (Pais pais : nuevoContinente.getPaises()) {
                pais.setContinente(nuevoContinente);
            }
            preMapa.withContinente(nuevoContinente);
        }

        Mapa mapa = preMapa.build();
        System.out.println(mapa);
    }
 }
