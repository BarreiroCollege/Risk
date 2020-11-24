package gal.sdc.usc.risk.menu.comandos.generico;

import gal.sdc.usc.risk.menu.Partida;
import gal.sdc.usc.risk.menu.Resultado;
import gal.sdc.usc.risk.menu.comandos.Comando;
import gal.sdc.usc.risk.menu.comandos.Comandos;
import gal.sdc.usc.risk.menu.comandos.Estado;
import gal.sdc.usc.risk.menu.comandos.IComando;
import gal.sdc.usc.risk.salida.SalidaObjeto;
import gal.sdc.usc.risk.salida.SalidaValor;
import gal.sdc.usc.risk.tablero.Pais;
import gal.sdc.usc.risk.tablero.valores.Errores;

@Comando(estado = Estado.CUALQUIERA, comando = Comandos.OBTENER_CONTINENTE)
public class ObtenerContinente extends Partida implements IComando {
    @Override
    public void ejecutar(String[] comandos) {
        String clave = comandos[2];

        if (super.getMapa() == null) {
            Resultado.error(Errores.MAPA_NO_CREADO);
            return;
        }

        Pais pais = super.getMapa().getPaisPorNombre(clave);
        if (pais == null) {
            Resultado.error(Errores.PAIS_NO_EXISTE);
            return;
        }

        SalidaObjeto salida = new SalidaObjeto();
        salida.withEntrada("continente", SalidaValor.withString(pais.getContinente().getNombre()));
        Resultado.correcto(salida);
    }

    @Override
    public String ayuda() {
        return "obtener continente <abreviatura_continente>";
    }
}
