package com.example.fernando.appcivico.adapters;

import android.content.Context;
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
import com.example.fernando.appcivico.estrutura.Comentario;

import java.util.ArrayList;

/**
 * Created by fernando on 13/10/16.
 */
public class ComentarioAdapter extends RecyclerView.Adapter<ComentarioAdapter.ComentarioViewHolder>{
    private Context context;
    private ArrayList<Comentario> comentarios;

    public ComentarioAdapter(Context context, ArrayList<Comentario> comentarios) {
        this.context = context;
        this.comentarios = comentarios;
    }

    @Override
    public ComentarioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_comentario,parent,false);
        ComentarioViewHolder comentarioViewHolder = new ComentarioViewHolder(view,context);
        return comentarioViewHolder;
    }

    @Override
    public void onBindViewHolder(ComentarioViewHolder holder, int position) {
        Comentario comentario = comentarios.get(position);
        holder.ratingbar_nota.setRating(comentario.getValor());
        holder.txt_autor.setText(comentario.getNomeUsuario());
        holder.txt_comentario.setText("\""+comentario.getTexto()+"\"");
        holder.txt_data_hora.setText(comentario.getDataComentario());
    }


    @Override
    public int getItemCount() {
        return this.comentarios.size();
    }

    public static class ComentarioViewHolder extends RecyclerView.ViewHolder  {
        private RatingBar ratingbar_nota;
        private TextView txt_comentario;
        private TextView txt_autor;
        private TextView txt_data_hora;

        public ComentarioViewHolder(View itemView, Context context) {
            super(itemView);

            ratingbar_nota = (RatingBar)itemView.findViewById(R.id.ratingbar_nota);
            txt_comentario = (TextView)itemView.findViewById(R.id.txt_comentario);
            txt_autor = (TextView)itemView.findViewById(R.id.txt_autor);
            txt_data_hora = (TextView)itemView.findViewById(R.id.txt_data_hora);


        }
    }


}
