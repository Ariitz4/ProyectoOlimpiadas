package es.aritzherrero.proyectoolimpiadas.Modelo;


public class Participacion {

    private int idDeportista, idEvento, idEquipo;
    private String nomDeportista,nomEvento,nomOlimpiada,nomDeporte,nomEquipo,abreviaturaEquipo,medalla;
    private Integer edad;


    public Participacion(int idDep, int idEv, int idEq,String nDeportista, String nEvento, String nOlimpiada, String nDeporte, String nEquipo, String abrev, Integer ed, String meda) {
        idDeportista = idDep;
        idEvento = idEv;
        idEquipo = idEq;
        nomDeportista = nDeportista;
        nomEvento = nEvento;
        nomOlimpiada = nOlimpiada;
        nomDeporte = nDeporte;
        nomEquipo = nEquipo;
        abreviaturaEquipo = abrev;
        edad = ed;
        medalla = meda;
    }

    public Participacion(Deportista d, Evento ev, Equipo eq, Integer nEdad, String sMedalla) {
        idDeportista = d.getIdDeportista();
        idEvento = ev.getIdEvento();
        idEquipo = eq.getIdEquipo();
        nomDeportista = d.getNombre();
        nomEvento = ev.getNomEvento();
        nomOlimpiada = ev.getNomOlimpiada();
        nomDeporte = ev.getNomDeporte();
        nomEquipo = eq.getNombre();
        abreviaturaEquipo = eq.getIniciales();
        edad = nEdad;
        medalla = sMedalla;
    }


    public int getIdDeportista() {
        return idDeportista;
    }

    public void setIdDeportista(int idDeportista) {
        this.idDeportista = idDeportista;
    }

    public int getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(int idEvento) {
        this.idEvento = idEvento;
    }

    public int getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(int idEquipo) {
        this.idEquipo = idEquipo;
    }

    public String getNomDeportista() {
        return nomDeportista;
    }

    public void setNomDeportista(String nomDeportista) {
        this.nomDeportista = nomDeportista;
    }

    public String getNomEvento() {
        return nomEvento;
    }

    public void setNomEvento(String nomEvento) {
        this.nomEvento = nomEvento;
    }

    public String getNomOlimpiada() {
        return nomOlimpiada;
    }

    public void setNomOlimpiada(String nomOlimpiada) {
        this.nomOlimpiada = nomOlimpiada;
    }

    public String getNomDeporte() {
        return nomDeporte;
    }

    public void setNomDeporte(String nomDeporte) {
        this.nomDeporte = nomDeporte;
    }

    public String getNomEquipo() {
        return nomEquipo;
    }

    public void setNomEquipo(String nomEquipo) {
        this.nomEquipo = nomEquipo;
    }

    public String getAbreviaturaEquipo() {
        return abreviaturaEquipo;
    }

    public void setAbreviaturaEquipo(String abreviaturaEquipo) {
        this.abreviaturaEquipo = abreviaturaEquipo;
    }

    public String getMedalla() {
        return medalla;
    }

    public void setMedalla(String medalla) {
        this.medalla = medalla;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }
}