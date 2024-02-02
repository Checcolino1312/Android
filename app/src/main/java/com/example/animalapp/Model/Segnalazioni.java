package com.example.animalapp.Model;

import java.io.Serializable;

public class Segnalazioni implements Serializable {
    public String id;

    public static final String SEGNALAZIONE="Segnalazione";

    public static final String SEGNALAZIONi = "segnalazioni";
    public static final String ID_SEGNALAZIONE="idSegnalazione";
    public static final String DESTINATARIOVETERINARIO="destinatarioVeterionario";
    public static final String NAME ="descrizione";


    public String descrizione;
    public String destinatarioEnte;
    public String destinatarioUtente;
    public String destinatarioVeterionario;
    public String idMittente;
    public String posizione;
    public String tipologiaSegnalazione;
    public String imgSegnalazione;
    public String idPresaInCarico;
    public String presaInCarico;

    public double lattitudine;

    public double longitudine;

    public String destinatario;


    public Segnalazioni(){

    }

    public Segnalazioni(String id, String descrizione, String destinatarioVeterionario) {
        this.id = id;
        this.destinatarioVeterionario = destinatarioVeterionario;
        this.descrizione = descrizione;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getDestinatarioEnte() {
        return destinatarioEnte;
    }

    public void setDestinatarioEnte(String destinatarioEnte) {
        this.destinatarioEnte = destinatarioEnte;
    }

    public String getDestinatarioUtente() {
        return destinatarioUtente;
    }

    public void setDestinatarioUtente(String destinatarioUtente) {
        this.destinatarioUtente = destinatarioUtente;
    }

    public String getDestinatarioVeterionario() {
        return destinatarioVeterionario;
    }

    public void setDestinatarioVeterionario(String destinatarioVeterionario) {
        this.destinatarioVeterionario = destinatarioVeterionario;
    }

    public String getIdMittente() {
        return idMittente;
    }

    public void setIdMittente(String idMittente) {
        this.idMittente = idMittente;
    }

    public String getPosizione() {
        return posizione;
    }

    public void setPosizione(String posizione) {
        this.posizione = posizione;
    }

    public String getTipologiaSegnalazione() {
        return tipologiaSegnalazione;
    }

    public void setTipologiaSegnalazione(String tipologiaSegnalazione) {
        this.tipologiaSegnalazione = tipologiaSegnalazione;
    }

    public String getImgSegnalazione() {
        return imgSegnalazione;
    }

    public void setImgSegnalazione(String imgSegnalazione) {
        this.imgSegnalazione = imgSegnalazione;
    }

    public String getIdPresaInCarico() {
        return idPresaInCarico;
    }

    public void setIdPresaInCarico(String idPresaInCarico) {
        this.idPresaInCarico = idPresaInCarico;
    }

    public String getPresaInCarico() {
        return presaInCarico;
    }

    public void setPresaInCarico(String presaInCarico) {
        this.presaInCarico = presaInCarico;
    }

    public double getLattitudine() {
        return lattitudine;
    }

    public void setLattitudine(double lattitudine) {
        this.lattitudine = lattitudine;
    }

    public double getLongitudine() {
        return longitudine;
    }

    public void setLongitudine(double longitudine) {
        this.longitudine = longitudine;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }
}
