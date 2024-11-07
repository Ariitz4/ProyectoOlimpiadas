package es.aritzherrero.proyectoolimpiadas.Modelo;

public class Deporte {

    private int idDeporte;
    private String nombre;


    public Deporte(int idDep, String nom) {
        idDeporte = idDep;
        nombre = nom;
    }


    public int getIdDeporte() {
        return idDeporte;
    }

    public void setIdDeporte(int idDeporte) {
        this.idDeporte = idDeporte;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }

}