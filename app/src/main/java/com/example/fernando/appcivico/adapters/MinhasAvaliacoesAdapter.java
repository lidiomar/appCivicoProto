package com.example.fernando.appcivico.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.fernando.appcivico.R;
import com.example.fernando.appcivico.activities.DialogAvaliarActivity;
import com.example.fernando.appcivico.activities.MinhasAvaliacoesActivity;
import com.example.fernando.appcivico.application.ApplicationAppCivico;
import com.example.fernando.appcivico.estrutura.Comentario;

import java.util.ArrayList;

/**
 * Created by fernando on 13/10/16.
 */
public class MinhasAvaliacoesAdapter extends RecyclerView.Adapter<MinhasAvaliacoesAdapter.MinhasAvaliacoesViewHolder>{
    private Context context;
    private ArrayList<Comentario> comentarios;

    public MinhasAvaliacoesAdapter(Context context, ArrayList<Comentario> comentarios) {
        this.context = context;
        this.comentarios = comentarios;
    }

    @Override
    public MinhasAvaliacoesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_minhas_avaliacoes,parent,false);
        MinhasAvaliacoesViewHolder minhasAvaliacoesViewHolder = new MinhasAvaliacoesViewHolder(view,context);
        return minhasAvaliacoesViewHolder;
    }

    @Override
    public void onBindViewHolder(MinhasAvaliacoesViewHolder holder, int position) {
        Comentario comentario = comentarios.get(position);
        holder.ratingbar_nota.setRating(comentario.getValor());

        holder.txt_comentario.setText("\""+comentario.getTexto()+"\"");
        holder.txt_data_hora.setText(comentario.getDataComentario());
        holder.txt_nome_fantasia.setText(comentario.getNomeFantasiaEstabelecimento());
        holder.setComentario(comentario);
    }


    @Override
    public int getItemCount() {
        return this.comentarios.size();
    }

    public static class MinhasAvaliacoesViewHolder extends RecyclerView.ViewHolder  {
        private RatingBar ratingbar_nota;
        private TextView txt_comentario;
        private TextView txt_data_hora;
        private TextView txt_nome_fantasia;
        private LinearLayout linearlayout_editar_avaliacao;
        private Comentario comentario;

        public MinhasAvaliacoesViewHolder(View itemView, final Context context) {
            super(itemView);

            ratingbar_nota = (RatingBar)itemView.findViewById(R.id.ratingbar_nota);
            txt_comentario = (TextView)itemView.findViewById(R.id.txt_comentario);
            txt_data_hora = (TextView)itemView.findViewById(R.id.txt_data_hora);
            txt_nome_fantasia = (TextView)itemView.findViewById(R.id.text_nome_fantasia);
            linearlayout_editar_avaliacao = (LinearLayout)itemView.findViewById(R.id.linearlayout_editar_avaliacao);
            linearlayout_editar_avaliacao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String codigoPostagem = getComentario().getCodigoPostagem();
                    String codConteudoPost = getComentario().getCodConteudoPost();
                    String nomeFantasiaEstabelecimento = getComentario().getNomeFantasiaEstabelecimento();
                    String nomeAutorComentario = getComentario().getNomeUsuario();
                    String comentario = getComentario().getTexto();
                    int valor = getComentario().getValor();

                    Intent intent = new Intent(context, DialogAvaliarActivity.class);
                    intent.putExtra("codigoPostagem",codigoPostagem);
                    intent.putExtra("codConteudoPost",codConteudoPost);
                    intent.putExtra("nomeFantasiaEstabelecimento",nomeFantasiaEstabelecimento);
                    intent.putExtra("nomeAutorComentario",nomeAutorComentario);
                    intent.putExtra("nomeAutorComentario",nomeAutorComentario);

                    intent.putExtra("comentario",comentario);
                    intent.putExtra("valor",valor);


                    ((MinhasAvaliacoesActivity)context).startActivityForResult(intent,8);

                }
            });


        }

        public Comentario getComentario() {
            return comentario;
        }

        public void setComentario(Comentario comentario) {
            this.comentario = comentario;
        }
    }


}
