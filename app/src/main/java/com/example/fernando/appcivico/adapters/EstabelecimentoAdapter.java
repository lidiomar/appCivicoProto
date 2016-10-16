package com.example.fernando.appcivico.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
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
        Estabelecimento estabelecimento = estabelecimentosList.get(position);
        holder.setEstabelecimento(estabelecimento);

        holder.txtnomeFantasia.setText(estabelecimento.getNomeFantasia());
        holder.txtVinculoSus.setText(String.format(context.getString(R.string.vinculo_sus),estabelecimento.getVinculoSus()));
        holder.txtVinculoTurno.setText(String.format(context.getString(R.string.turno_atendimento),estabelecimento.getTurnoAtendimento()));

        holder.tipoUnidade.setText(String.format(context.getString(R.string.tipo_unidade),
                estabelecimento.getTipoUnidade().substring(0,1).toUpperCase()+
                estabelecimento.getTipoUnidade().substring(1).toLowerCase()
        ));
        holder.categoriaUnidade.setText(String.format(context.getString(R.string.categoria_unidade),
                estabelecimento.getCategoriaUnidade().substring(0,1).toUpperCase()+
                estabelecimento.getCategoriaUnidade().substring(1).toLowerCase()
        ));

    }


    @Override
    public int getItemCount() {
        return estabelecimentosList.size();
    }

    public class EstabelecimentoViewHolder extends RecyclerView.ViewHolder {
        private TextView txtVinculoSus;
        private TextView txtnomeFantasia;
        private TextView txtVinculoTurno;
        private TextView tipoUnidade;
        private TextView categoriaUnidade;

        private Estabelecimento estabelecimento;

        public EstabelecimentoViewHolder(View itemView, final Context context) {
            super(itemView);
            txtnomeFantasia = (TextView)itemView.findViewById(R.id.txt_nome_fantasia);
            txtVinculoSus = (TextView) itemView.findViewById(R.id.txt_vinculo_sus);
            txtVinculoTurno = (TextView)itemView.findViewById(R.id.txt_vinculo_turno);
            tipoUnidade = (TextView)itemView.findViewById(R.id.tipo_unidade);
            categoriaUnidade = (TextView)itemView.findViewById(R.id.categoria_unidade);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, InformacoesActivity.class);
                    intent.putExtra("estabelecimento",getEstabelecimento());
                    context.startActivity(intent);
                }
            });
        }

        public Estabelecimento getEstabelecimento() {
            return estabelecimento;
        }

        public void setEstabelecimento(Estabelecimento estabelecimento) {
            this.estabelecimento = estabelecimento;
        }
    }
}
