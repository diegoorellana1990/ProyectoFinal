package com.example.diego.proyecto;

import android.app.Activity;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Diego on 01/11/2015.
 */



public class AdaptadorAnuncios extends BaseAdapter{

    Activity activity;
    TextView tvTitulo, tvFecha;

    Context c;
    ArrayList<Anuncio> anuncios;
    LayoutInflater inf;
    public AdaptadorAnuncios(Context context, ArrayList<Anuncio> anuncios)
    {

        this.c = context;
        this.anuncios = anuncios;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return anuncios.size();
    }

    @Override
    public Object getItem(int position) {
        return anuncios.get(position);
    }

    @Override
    public long getItemId(int position) {
        return anuncios.get(position).getId();
    }
    LayoutInflater mInflater;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null)
        {
            convertView = mInflater.inflate(R.layout.item_trabajo, null);
            holder = new ViewHolder();


            holder.tvTitulo = (TextView) convertView.findViewById(R.id.tvTitulo);
            holder.tvFecha = (TextView) convertView.findViewById(R.id.tvFecha);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvTitulo.setText(anuncios.get(position).titulo);
        holder.tvFecha.setText(anuncios.get(position).fecha.toString());
        return convertView;
    }
}

class ViewHolder{
    TextView tvTitulo, tvFecha;
}
