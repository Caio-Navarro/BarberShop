package com.projeto.barbershop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<Agendamento> data;
    private Context context;

    public Adapter(Context context, List<Agendamento> data) {
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.custom_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Agendamento agendamento = data.get(i);
        viewHolder.nomeCliente.setText("Cliente: " + agendamento.getNomeCliente());
        viewHolder.horarioAgendamento.setText("Horário: " +agendamento.getHorarioAgendamento());
        viewHolder.nomeBarbeiro.setText("Barbeiro: " +agendamento.getNomeBarbeiro());
        viewHolder.dataAgendamento.setText("Data: " +agendamento.getDataAgendamento());

        viewHolder.btnAtendido.setTag(agendamento.getDocumentId());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nomeCliente, dataAgendamento, horarioAgendamento, nomeBarbeiro;
        AppCompatButton btnAtendido;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nomeCliente = itemView.findViewById(R.id.nomeCliente);
            horarioAgendamento = itemView.findViewById(R.id.horarioAgendamento);
            nomeBarbeiro = itemView.findViewById(R.id.nomeBarbeiro);
            dataAgendamento = itemView.findViewById(R.id.dataAgendamento);
            btnAtendido = itemView.findViewById(R.id.btnClienteAtendido);
        }
    }
}
