package tech.abralica.clinicalaluzapp.models;

import com.google.firebase.Timestamp;

public class Horario {
    private int id;
    private Medico medico;
    private Timestamp horaInicio;
    private Timestamp horaFin;

    public Horario(int id, Medico medico, Timestamp horaInicio, Timestamp horaFin) {
        this.id = id;
        this.medico = medico;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }
}
