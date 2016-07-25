package fatec.tg.SignsOn;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import android.os.Handler;

import com.squareup.picasso.Picasso;

public class SignalAdapter extends BaseAdapter {

    public LayoutInflater mInflater;
    public ArrayList<SignalListView> itens;
    public String urlimg;

    public SignalAdapter(Context context, ArrayList<SignalListView> itens)
    {
        this.itens = itens;
        mInflater = LayoutInflater.from(context);

    }

    public int getCount()
    {
        return itens.size();
    }

    public SignalListView getItem(int position)
    {
        return itens.get(position);
    }

    public long getItemId(int position)
    {
        return position;
    }

    public View getView(int position, View view, ViewGroup parent)
    {

        SignalListView item = itens.get(position);

        view = mInflater.inflate(R.layout.signal_view, null);

        ((TextView) view.findViewById(R.id.sinalTxt)).setText(item.getTexto());

        ((ImageView) view.findViewById(R.id.statusImg)).setImageResource(item.getStatus());

        urlimg = item.getIconeRid();

        ImageView img = (ImageView) view.findViewById(R.id.sinalImg);
        Picasso.with(view.getContext()).load(urlimg).into(img);

        return view;
    }
}