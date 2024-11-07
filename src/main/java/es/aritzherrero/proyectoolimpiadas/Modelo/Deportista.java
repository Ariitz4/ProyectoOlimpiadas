package es.aritzherrero.proyectoolimpiadas.Modelo;


public class Deportista {

    private int idDeportista;
    private String nombre;
    private Character sexo;
    private Integer peso,altura;


    public Deportista(int idDep, String nom, Character sex, Integer pes, Integer alt) {
        idDeportista = idDep;
        nombre = nom;
        sexo = sex;
        peso = pes;
        altura = alt;
    }


    public int getIdDeportista() {
        return idDeportista;
    }

    public void setIdDeportista(int idDeportista) {
        this.idDeportista = idDeportista;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Character getSexo() {
        return sexo;
    }

    public void setSexo(Character sexo) {
        this.sexo = sexo;
    }

    public Integer getPeso() {
        return peso;
    }

    public void setPeso(Integer peso) {
        this.peso = peso;
    }

    public Integer getAltura() {
        return altura;
    }

    public void setAltura(Integer altura) {
        this.altura = altura;
    }

    @Override
    public String toString() {
        return nombre;
    }
}