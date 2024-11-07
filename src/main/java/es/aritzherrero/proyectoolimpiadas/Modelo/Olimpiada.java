package es.aritzherrero.proyectoolimpiadas.Modelo;


public class Olimpiada {

    private int idOlimpiada;
    private String nombre, ciudad, temporada;
    private Integer anio;

    /**
     * Clase para crear una olimpiada.
     * @param idOli ID de la olimpiada
     * @param nom nombre
     * @param ani anio
     * @param temp temporada
     * @param ciu ciudad
     */
    public Olimpiada(int idOli, String nom, Integer ani, String temp, String ciu) {
        idOlimpiada = idOli;
        nombre = nom;
        anio = ani;
        temporada = temp;
        ciudad = ciu;
    }

    // METODOS GETTERS Y SETTERS

    public int getIdOlimpiada() {
        return idOlimpiada;
    }

    public void setIdOlimpiada(int idOlimpiada) {
        this.idOlimpiada = idOlimpiada;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public String getTemporada() {
        return temporada;
    }

    public void setTemporada(String temporada) {
        this.temporada = temporada;
    }

    @Override
    public String toString() {
        return nombre;
    }


}