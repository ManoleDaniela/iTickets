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

public class IstoricBileteRecyclerAdapter extends RecyclerView.Adapter<IstoricBileteRecyclerAdapter.ItemViewHolder>{
    private ArrayList<BiletIstoric> lista_bilete = new ArrayList<>();
    private Context mContext;

    public List<BiletIstoric> getLista_bilete() {
        return lista_bilete;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.istoric_row,parent,false);
        return new ItemViewHolder(view);
    }

    public IstoricBileteRecyclerAdapter(Context mContext, ArrayList<BiletIstoric> lista_bilete) {
        this.mContext=mContext;
        this.lista_bilete = lista_bilete;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        final BiletIstoric b = lista_bilete.get(position);
        holder.d.setText(b.getD());
        holder.data3.setText("Data si ora: " + b.getData());
        holder.linie3.setText("Linia: " + b.getLinie());
        holder.tip3.setText("Tip bilet: " + b.getTip());
        holder.pret3.setText("Pret bilete: " + b.getPret());
       /* holder.relative3.setOnClickListener(new View.OnClickListener() {
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

        */
    }

    @Override
    public int getItemCount() {
        return lista_bilete.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView d,data3, linie3, tip3,pret3;
        RelativeLayout relative3;
        public ItemViewHolder(View itemView) {
            super(itemView);
            d = itemView.findViewById(R.id.d);
            data3 = itemView.findViewById(R.id.data3);
            linie3 = itemView.findViewById(R.id.linie3);
            tip3 = itemView.findViewById(R.id.tip3);
            pret3 = itemView.findViewById(R.id.pret3);
            relative3 = itemView.findViewById(R.id.relative3);
        }
    }
}
