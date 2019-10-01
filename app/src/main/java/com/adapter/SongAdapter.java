package com.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.model.Cancion;
import com.spotifyapp.R;

import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder>
{
    private final SongAdapter.TotalCallback mTotalCallback;

    ArrayList<Cancion> canciones = new ArrayList<>();

    Context cont;

    public SongAdapter(Context context, ArrayList<Cancion> canciones)
    {
        this.canciones = canciones;
        this.mTotalCallback = ((SongAdapter.TotalCallback) context);
        this.cont = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_song, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i)
    {
        viewHolder.txtTitulo.setText(canciones.get(i).getName());

        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTotalCallback.reproducirSong(i);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return canciones.size();
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

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            txtTitulo = itemView.findViewById(R.id.txtTitulo);
            lblSubTitulo = itemView.findViewById(R.id.lblSubTitulo);
            imageView = itemView.findViewById(R.id.imageView);

        }
    }

    public interface TotalCallback
    {
        void reproducirSong(int i);
    }

}
