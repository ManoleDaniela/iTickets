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

public class BileteRecyclerAdapter extends RecyclerView.Adapter<BileteRecyclerAdapter.ItemViewHolder>{
    private ArrayList<Bilet> lista_bilete = new ArrayList<>();
    private Context mContext;

    public List<Bilet> getLista_bilete() {
        return lista_bilete;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.firebase_row,parent,false);
        return new ItemViewHolder(view);
    }

    public BileteRecyclerAdapter(Context mContext, ArrayList<Bilet> lista_bilete) {
        this.mContext=mContext;
        this.lista_bilete = lista_bilete;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        final Bilet b = lista_bilete.get(position);

        holder.tv_linie.setText("Linia: " + b.getLinie_bilet());
        holder.tv_tip.setText("Tip bilet: " + b.getTip_bilet());
        holder.tv_pret.setText("Pret bilete: " + b.getPret_bilet());
        holder.relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,plata.class);
               intent.putExtra("Linie",b.getLinie_bilet());
                intent.putExtra("Tip",b.getTip_bilet());
                intent.putExtra("Pret",b.getPret_bilet());
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
        TextView tv_linie, tv_tip,tv_pret;
        RelativeLayout relative;
        public ItemViewHolder(View itemView) {
            super(itemView);
            tv_linie = itemView.findViewById(R.id.tv_linie);
            tv_tip = itemView.findViewById(R.id.tv_tip);
            tv_pret = itemView.findViewById(R.id.tv_pret);
            relative=itemView.findViewById(R.id.relative);
        }
    }
}
















