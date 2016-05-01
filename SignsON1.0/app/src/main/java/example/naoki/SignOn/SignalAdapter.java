package example.naoki.SignOn;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import example.naoki.ble_myo.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import android.os.Handler;

import com.squareup.picasso.Picasso;

/**
 * Created by Mariana on 4/3/2016.
 */

public class SignalAdapter extends BaseAdapter {

    public LayoutInflater mInflater;
    public ArrayList<SignalListView> itens;
    public String urlimg;
    public Bitmap img = null;
    private Handler handler = new Handler();

    public SignalAdapter(Context context, ArrayList<SignalListView> itens)
    {
        //Itens que preencheram o listview
        this.itens = itens;
        //responsavel por pegar o Layout do item.
        mInflater = LayoutInflater.from(context);

    }

    /**
     * Retorna a quantidade de itens
     *
     * @return
     */
    public int getCount()
    {
        return itens.size();
    }

    /**
     * Retorna o item de acordo com a posicao dele na tela.
     *
     * @param position
     * @return
     */
    public SignalListView getItem(int position)
    {
        return itens.get(position);
    }

    /**
     * Sem implementação
     *
     * @param position
     * @return
     */
    public long getItemId(int position)
    {
        return position;
    }

    public View getView(int position, View view, ViewGroup parent)
    {
        //Pega o item de acordo com a posção.
        SignalListView item = itens.get(position);
        //infla o layout para podermos preencher os dados
        view = mInflater.inflate(R.layout.signal_view, null);

        //atravez do layout pego pelo LayoutInflater, pegamos cada id relacionado
        //ao item e definimos as informações.
        ((TextView) view.findViewById(R.id.sinalTxt)).setText(item.getTexto());

        ((ImageView) view.findViewById(R.id.statusImg)).setImageResource(item.getStatus());

        urlimg = item.getIconeRid();

        ImageView img = (ImageView) view.findViewById(R.id.sinalImg);
        Picasso.with(view.getContext()).load(urlimg).into(img);

        return view;
    }
}