package tech.abralica.clinicalaluzapp.models;

import java.util.List;

public class Medico {
    private Especialidad especialidad;
    private String cargo;
    private String descripcion;

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<CodigoMedico> getCodigosMedico() {
        return codigosMedico;
    }

    public void setCodigosMedico(List<CodigoMedico> codigosMedico) {
        this.codigosMedico = codigosMedico;
    }

    public boolean isEstaDisponible() {
        return estaDisponible;
    }

    public void setEstaDisponible(boolean estaDisponible) {
        this.estaDisponible = estaDisponible;
    }

    public String getFacebookLink() {
        return facebookLink;
    }

    public void setFacebookLink(String facebookLink) {
        this.facebookLink = facebookLink;
    }

    private List<CodigoMedico> codigosMedico;
    private boolean estaDisponible;
    private String facebookLink;

    public Medico(Especialidad especialidad, String cargo, String descripcion, List<CodigoMedico> codigosMedico, boolean estaDisponible, String facebookLink) {
        this.especialidad = especialidad;
        this.cargo = cargo;
        this.descripcion = descripcion;
        this.codigosMedico = codigosMedico;
        this.estaDisponible = estaDisponible;
        this.facebookLink = facebookLink;
    }
}
