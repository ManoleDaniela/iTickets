package com.example.ittickets;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BileteleMeleRecyclerAdapter extends RecyclerView.Adapter<BileteleMeleRecyclerAdapter.ItemViewHolder>{
    private ArrayList<BiletulMeu> lista_bilete = new ArrayList<>();
    private Context mContext;

    public List<BiletulMeu> getLista_bilete() {
        return lista_bilete;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.biletelemele_row,parent,false);
        return new ItemViewHolder(view);
    }

    public BileteleMeleRecyclerAdapter(Context mContext, ArrayList<BiletulMeu> lista_bilete) {
        this.mContext=mContext;
        this.lista_bilete = lista_bilete;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        final BiletulMeu b = lista_bilete.get(position);
        holder.id.setText(b.getId());
        holder.linie.setText("Linia: " + b.getLinie());
        holder.tip.setText("Tip bilet: " + b.getTip());
        holder.pret.setText("Pret bilete: " + b.getPret());
        holder.relative2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,DetaliiBilet.class);
                intent.putExtra("Id",b.getId());
                intent.putExtra("Linie",b.getLinie());
                intent.putExtra("Tip",b.getTip());
                intent.putExtra("Pret",b.getPret());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista_bilete.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView id, linie, tip,pret;
        RelativeLayout relative2;
        public ItemViewHolder(View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.id);
            linie = itemView.findViewById(R.id.linie);
            tip = itemView.findViewById(R.id.tip);
            pret = itemView.findViewById(R.id.pret);
            relative2 = itemView.findViewById(R.id.relative2);
        }
    }
}
