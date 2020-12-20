package gal.sdc.usc.risk.comandos.generico;

import gal.sdc.usc.risk.jugar.Partida;
import gal.sdc.usc.risk.salida.Resultado;
import gal.sdc.usc.risk.comandos.Comando;
import gal.sdc.usc.risk.comandos.Comandos;
import gal.sdc.usc.risk.comandos.Estado;
import gal.sdc.usc.risk.comandos.IComando;
import gal.sdc.usc.risk.salida.SalidaObjeto;
import gal.sdc.usc.risk.tablero.Continente;
import gal.sdc.usc.risk.excepciones.Errores;

@Comando(estado = Estado.CUALQUIERA, comando = Comandos.OBTENER_PAISES)
public class ObtenerPaises extends Partida implements IComando {
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

        SalidaObjeto salida = new SalidaObjeto();
        salida.put("paises", continente.getPaises().values());
        Resultado.correcto(salida);
    }

    @Override
    public String ayuda() {
        return "obtener paises <abreviatura_continente>";
    }
}
