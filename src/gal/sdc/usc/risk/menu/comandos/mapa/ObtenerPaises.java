package gal.sdc.usc.risk.menu.comandos.mapa;

import gal.sdc.usc.risk.menu.Partida;
import gal.sdc.usc.risk.menu.Resultado;
import gal.sdc.usc.risk.menu.comandos.Comando;
import gal.sdc.usc.risk.tablero.Continente;
import gal.sdc.usc.risk.tablero.Pais;
import gal.sdc.usc.risk.tablero.valores.Errores;

import java.util.Iterator;
import java.util.Map;

public class ObtenerPaises extends Partida implements Comando {
    @Override
    public void ejecutar(String[] comandos) {
        String clave = comandos[2];

        if (super.getMapa() == null) {
            Resultado.error(Errores.MAPA_NO_CREADO);
            return;
        }

        Continente continente = super.getMapa().getContinentePorNombre(clave);
        if (continente == null) {
            Resultado.error(Errores.CONTINENTE_NO_EXISTE);
            return;
        }

        StringBuilder out = new StringBuilder("{\n" +
                "  paises: [ ");
        Iterator<Map.Entry<String, Pais>> it = continente.getPaises().entrySet().iterator();
        boolean primero = true;
        while (it.hasNext()) {
            Pais pais = it.next().getValue();
            if (!primero) {
                out.append(String.format("%-11s", ""));
            } else {
                primero = false;
            }
            out.append("\"").append(pais.getNombre()).append("\"");
            if (it.hasNext()) {
                out.append(",\n");
            }
        }
        out.append("\n").append("          ]\n" + "}");
        Resultado.correcto(out.toString());
    }
}
