package example.naoki.SignOn;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import example.naoki.ble_myo.R;

import java.util.List;

/**
 * Created by Mariana on 4/3/2016.
 */

public class SinalAdapter extends BaseAdapter {

    Activity act;
    List<String> nomes;

    public SinalAdapter(Activity act, List<String> nomes) {
        this.act = act;
        this.nomes = nomes;
    }

    @Override
    public View getView(int posicao, View convertView, ViewGroup parent) {
        View layout = act.getLayoutInflater().inflate(R.layout.signal_view, null);

        TextView campoNome = (TextView) layout.findViewById(R.id.nomeTxt);

        String nome = nomes.get(posicao);
        campoNome.setText(nome);

        return layout;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}