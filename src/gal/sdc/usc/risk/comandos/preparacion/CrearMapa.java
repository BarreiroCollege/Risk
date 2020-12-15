package gal.sdc.usc.risk.comandos.preparacion;

import gal.sdc.usc.risk.jugar.Partida;
import gal.sdc.usc.risk.jugar.Resultado;
import gal.sdc.usc.risk.comandos.Comando;
import gal.sdc.usc.risk.comandos.Comandos;
import gal.sdc.usc.risk.comandos.Estado;
import gal.sdc.usc.risk.comandos.IComando;
import gal.sdc.usc.risk.tablero.Celda;
import gal.sdc.usc.risk.tablero.Continente;
import gal.sdc.usc.risk.tablero.Mapa;
import gal.sdc.usc.risk.tablero.Pais;
import gal.sdc.usc.risk.tablero.valores.Continentes;
import gal.sdc.usc.risk.excepciones.Errores;
import gal.sdc.usc.risk.tablero.valores.Paises;


@Comando(estado = Estado.PREPARACION, comando = Comandos.CREAR_MAPA)
public class CrearMapa extends Partida implements IComando {
    @Override
    public void ejecutar(String[] comandos) {
        if (super.getMapa() != null) {
            Resultado.error(Errores.MAPA_YA_CREADO);
            return;
        }

        Mapa.Builder preMapa = new Mapa.Builder();

        // Primero continentes
        for (Continentes continente : Continentes.values()) {
            Continente.Builder preContinente = new Continente.Builder(continente)
                    .withNombre(continente.getNombre())
                    .withAbreviatura(continente.getAbreviatura())
                    .withColor(continente.getColor())
                    .withEjercitosRearme(continente.getEjercitos());

            // Luego países del continente
            for (Paises pais : Paises.values()) {
                if (pais.getContinente().equals(continente)) {
                    continue;
                }

                Celda celda = new Celda.Builder()
                        .withX(pais.getX())
                        .withY(pais.getY())
                        .build();

                Pais nuevoPais = new Pais.Builder(pais)
                        .withNombre(pais.getNombre())
                        .withAbreviatura(pais.getAbreviatura())
                        .withCelda(celda)
                        .build();
                preContinente.withPais(nuevoPais);
                preMapa.withPais(nuevoPais);
            }

            Continente nuevoContinente = preContinente.build();
            for (Pais pais : nuevoContinente.getPaises().values()) {
                if (!pais.setContinente(nuevoContinente)) {
                    System.err.println("El país " + pais.getNombre() + " ya tenía asignado continente!");
                }
            }
            preMapa.withContinente(nuevoContinente);
        }

        super.setMapa(preMapa.build());
        super.getConsola().imprimir(super.getMapa());
        // super.getComandos().mapaCreado();
    }

    @Override
    public String ayuda() {
        return "crear mapa";
    }
}
