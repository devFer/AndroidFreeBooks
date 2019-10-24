package ioc.xtec.cat.freebooks;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class Reserves extends AppCompatActivity implements View.OnClickListener {
    public static final String EXTRA_MESSAGE = "ioc.xtec.cat.freeboks.MESSAGE";

    // Variable amb l'intent
    Intent i;

    Button btnEditaReserva;
    Button btnAnulaReserva;
    Button btnTornarReserves;
    ImageView imatgeReserves;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserves);

        Intent iFinalitza = new Intent("finish");
        sendBroadcast(iFinalitza);

        imatgeReserves = ((ImageView) findViewById(R.id.imageReserves));
        imatgeReserves.setColorFilter(Color.GRAY);
        // Definim els listeners
        btnTornarReserves = ((Button) findViewById(R.id.btnTornarReserves));
        btnTornarReserves.setOnClickListener(this);

        // Crea un intent amb la pantalla de login
        i = new Intent(Reserves.this, PrincipalActivity.class);
    }

    /**
     * Accions al fer click sobre els botons
     *
     * @param v amb el view on s'ha fet click
     */
    @Override
    public void onClick(View v) {
        if (v == btnEditaReserva) {
            // TODO Ha de poder editar la data de la reserva
        } else if (v == btnAnulaReserva) {
            // TODO Ha d'anular la reserva
        } else if (v == btnTornarReserves) {
            // Torna a la pantalla principal
            String extra = getIntent().getStringExtra(EXTRA_MESSAGE);
            i.putExtra(EXTRA_MESSAGE, extra);
            startActivity(i);
            finish();
        }
    }

    /**
     * Crea un missatge tipus toast
     *
     * @param toast amb el text del missatge
     */
    public void showToast(final String toast) {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(Reserves.this, toast, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Torna a la pantalla de login en cas de prémer el botó "Back"
     */
    @Override
    public void onBackPressed() {
        // Torna a la pantalla principal
        String extra = getIntent().getStringExtra(EXTRA_MESSAGE);
        i.putExtra(EXTRA_MESSAGE, extra);
        startActivity(i);
        finish();
    }
}
