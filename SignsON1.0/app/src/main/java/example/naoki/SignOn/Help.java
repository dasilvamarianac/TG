package example.naoki.SignOn;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import example.naoki.ble_myo.R;




public class Help extends Activity implements View.OnClickListener {
    TextView help;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);
        Bundle extras = getIntent().getExtras();
        help = (TextView) findViewById(R.id.txthelp);
        final Button btn = (Button) findViewById(R.id.manualBtn);
        btn.setOnClickListener(this);

        help.setText("Trabalho de Graduação\n" +
                "Tecnologia em Análise e Desenvolvimento de Sistemas\n" +
                "Relatório Técnico de Desenvolvimento de Software\n" +
                "\n" +
                "Desenvolvimento de Aplicativo para Interpretação e Tradução Simultânea de Linguagem de Sinais\n" +
                "\n" +
                "Mariana Cristina da Silva\n" +
                "\n" +
                "Junho 2016");
    }


    public void onClick(View view) {

        Uri uri = Uri.parse("http://signson.orgfree.com/instrucoes");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

}
