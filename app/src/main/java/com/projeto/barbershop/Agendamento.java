package com.projeto.barbershop;

public class Agendamento {
    private String nomeCliente;
    private String horarioAgendamento;
    private String nomeBarbeiro;
    private String dataAgendamento;
    private String documentId;

    public Agendamento(String nomeCliente, String horarioAgendamento, String nomeBarbeiro, String dataAgendamento, String documentId) {
        this.nomeCliente = nomeCliente;
        this.horarioAgendamento = horarioAgendamento;
        this.nomeBarbeiro = nomeBarbeiro;
        this.dataAgendamento = dataAgendamento;
        this.documentId = documentId;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public String getHorarioAgendamento() {
        return horarioAgendamento;
    }

    public String getNomeBarbeiro() {
        return nomeBarbeiro;
    }

    public String getDataAgendamento(){
        return dataAgendamento;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}
