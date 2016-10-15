package com.example.fernando.appcivico.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fernando.appcivico.R;
import com.example.fernando.appcivico.activities.InformacoesActivity;
import com.example.fernando.appcivico.estrutura.Estabelecimento;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fernando on 15/10/16.
 */
public class EstabelecimentoAdapter extends RecyclerView.Adapter<EstabelecimentoAdapter.EstabelecimentoViewHolder>{

    private final Context context;
    private final ArrayList<Estabelecimento> estabelecimentosList;
    private Estabelecimento estabelecimento;


    public EstabelecimentoAdapter(Context context, ArrayList<Estabelecimento> estabelecimentosList) {
        this.context = context;
        this.estabelecimentosList = estabelecimentosList;
    }

    @Override
    public EstabelecimentoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_estabelecimento,parent,false);
        EstabelecimentoViewHolder estabelecimentoViewHolder = new EstabelecimentoViewHolder(view,context);
        return estabelecimentoViewHolder;
    }

    @Override
    public void onBindViewHolder(EstabelecimentoViewHolder holder, int position) {
        estabelecimento = estabelecimentosList.get(position);
        holder.txtnomeFantasia.setText(estabelecimento.getNomeFantasia());
    }


    @Override
    public int getItemCount() {
        return estabelecimentosList.size();
    }

    public class EstabelecimentoViewHolder extends RecyclerView.ViewHolder {
        private TextView txtnomeFantasia;
        public EstabelecimentoViewHolder(View itemView, final Context context) {
            super(itemView);
            txtnomeFantasia = (TextView)itemView.findViewById(R.id.txt_nome_fantasia);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, InformacoesActivity.class);
                    intent.putExtra("estabelecimento",estabelecimento);
                    context.startActivity(intent);
                }
            });
        }
    }
}
