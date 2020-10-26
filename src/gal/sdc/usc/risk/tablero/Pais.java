package gal.sdc.usc.risk.tablero;

import gal.sdc.usc.risk.util.Colores;

public class Pais {
    public final static int MAX_LENGTH_NOMBRE = 10;

    private final Paises identificador;
    private final String nombre;
    private final Celda celda;
    private Continente continente;

    private Pais(Paises identificador, String nombre, Celda celda) {
        this.identificador = identificador;
        this.nombre = nombre;
        this.celda = celda;
    }

    public boolean setContinente(Continente continente) {
        if (this.continente != null) {
            return false;
        }
        this.continente = continente;
        return true;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Continente getContinente() {
        return this.continente;
    }

    public Colores.Color getColor() {
        if (continente == null) {
            return null;
        }
        return this.continente.getColor();
    }

    public Celda getCelda() {
        return this.celda;
    }

    public static class Builder {
        private final Paises pais;
        private String nombre;
        private Celda celda = null;

        public Builder(Paises pais) {
            this.pais = pais;
        }

        public Builder withNombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public Builder withCelda(Celda celda) {
            this.celda = celda;
            return this;
        }

        public Pais build() {
            if (this.pais == null) {
                // TODO
            } else if (this.nombre == null) {
                // TODO
            } else if (this.celda == null) {
                // TODO
            } else {
                return new Pais(pais, nombre, celda);
            }
            return null;
        }
    }

    @Override
    public String toString() {
        return "Pais{" +
                "nombre='" + nombre + '\'' +
                ", continente=" + (continente != null ? continente.getNombre() : continente) +
                ", celda=" + celda +
                '}';
    }
}
