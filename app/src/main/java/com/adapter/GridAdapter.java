package com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.model.Cancion;
import com.spotifyapp.R;

import java.util.ArrayList;

public class GridAdapter extends BaseAdapter {

    private ArrayList<Cancion> canciones = new ArrayList<>();
    private Context context;
    private final GridAdapter.TotalCallback mTotalCallback;

    public GridAdapter(Context context, ArrayList<Cancion> canciones) {
        this.mTotalCallback = ((GridAdapter.TotalCallback) context);
        this.canciones = canciones;
        this.context = context;
    }

    @Override
    public int getCount() {
        return canciones.size();
    }

    @Override
    public Cancion getItem(int position) {
        return canciones.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View item = convertView;
        final ViewHolder viewHolder;

        if (item == null) {
            item = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_song_search, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.txtTitulo = (TextView) item.findViewById(R.id.txtTitulo);
            viewHolder.btnAdd = (Button) item.findViewById(R.id.btnAdd);
            viewHolder.position = position;
            item.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.position = position;
        }

        Cancion cancion = getItem(position);

        viewHolder.txtTitulo.setText(cancion.getName());

        viewHolder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTotalCallback.agregarSong(position);
            }
        });

        return item;
    }

    public void setData(ArrayList<Cancion> canciones) {
        this.canciones = canciones;
    }

    public class ViewHolder {
        private int position;
        private TextView txtTitulo;
        Button btnAdd;
        //private ImageView imgPreview;
        //private TextView lblTitulo, lblSubTitulo;
    }

    public interface TotalCallback
    {
        void agregarSong(int i);
    }


}
