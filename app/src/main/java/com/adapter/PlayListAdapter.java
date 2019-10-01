package com.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.model.Items;
import com.model.PlayList;
import com.spotifyapp.MainActivity;
import com.spotifyapp.R;
import com.spotifyapp.SplashActivity;

import java.util.ArrayList;

public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.ViewHolder>
{
    private final PlayListAdapter.TotalCallback mTotalCallback;

    public Items[] playLists;

    Context cont;

    public PlayListAdapter(Context context, Items[] playLists)
    {
        this.playLists = playLists;
        this.mTotalCallback = ((PlayListAdapter.TotalCallback) context);
        this.cont = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_playlist, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i)
    {
        viewHolder.txtTitulo.setText(playLists[i].name);

        Glide.with(cont)
                .load(playLists[i].images[0].url)
                .placeholder(R.mipmap.ic_launcher_round)
                .dontAnimate()
                .override(150, 150)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.imageView);

        viewHolder.accion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTotalCallback.verDetallePlayList(i);
            }
        });

       /* viewHolder.eliminarButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mTotalCallback.eliminarDireccion(i);
            }
        });*/
    }

    @Override
    public int getItemCount()
    {
        return playLists.length;
    }

    @Override
    public int getItemViewType(int position)
    {
        return position;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView txtTitulo, lblSubTitulo;
        private ImageView imageView;
        private ConstraintLayout accion;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            txtTitulo = itemView.findViewById(R.id.txtTitulo);
            lblSubTitulo = itemView.findViewById(R.id.lblSubTitulo);
            imageView = itemView.findViewById(R.id.imageView);
            accion = itemView.findViewById(R.id.accion);
        }
    }

    public interface TotalCallback
    {
        void verDetallePlayList(int i);
    }

}
