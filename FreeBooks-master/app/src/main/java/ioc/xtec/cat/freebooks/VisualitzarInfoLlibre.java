package ioc.xtec.cat.freebooks;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by jordi on 17/03/2018.
 */

public class VisualitzarInfoLlibre extends AppCompatActivity implements View.OnClickListener {
    public static final String EXTRA_MESSAGE = "ioc.xtec.cat.freeboks.MESSAGE";
    // Variables per les dades del llibre
    String strImatge;
    TextView textTitol;
    TextView textAutor;
    TextView textDescripcio;
    TextView textEditorIAny;
    TextView textNumPagines;
    TextView textIdioma;
    TextView textISBN;

    // Resta de variables
    ImageView imatgeLlibre;
    Button btnReserva;
    Button btnTornar;

    // Variable amb l'intent
    Intent i;

    /**
     * Accions en la creació
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualitzar_info_llibre);
        Bundle bundle = getIntent().getExtras();
        SharedPreferences pref = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = pref.edit();

        Intent iFinalitza = new Intent("finish");
        sendBroadcast(iFinalitza);

        textTitol = (TextView) findViewById(R.id.textTitol);
        textAutor = (TextView) findViewById(R.id.textAutor);
        textDescripcio = (TextView) findViewById(R.id.textDescripcio);
        textEditorIAny = (TextView) findViewById(R.id.textEditorAny);
        textNumPagines = (TextView) findViewById(R.id.textPagines);
        textIdioma = (TextView) findViewById(R.id.textIdioma);
        textISBN = (TextView) findViewById(R.id.textISBN);
        imatgeLlibre = (ImageView) findViewById(R.id.imatgeLlibre);

        textTitol.setText(bundle.getString("Titol"));
        textAutor.setText(bundle.getString("Autor"));
        textDescripcio.setText(bundle.getString("Descripcio"));
        textDescripcio.setMovementMethod(new ScrollingMovementMethod());
        //strImatge = bundle.getString("ImatgePortada");
        strImatge = pref.getString("ImatgePortada", null);
        byte[] decodedString = Base64.decode(strImatge, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        // Si hi ha imatge carrega l'imatge
        if (decodedByte != null) {
            imatgeLlibre.setBackground(null);
            imatgeLlibre.setImageBitmap(decodedByte);
            // Si no hi ha imatge carrega una imatge per defecte
        } else {
            imatgeLlibre.setBackgroundResource(android.R.drawable.ic_menu_report_image);
        }
        textEditorIAny.setText(bundle.getString("EditorIAny"));
        textNumPagines.setText(bundle.getString("NumPagines"));
        textIdioma.setText(bundle.getString("Idioma"));
        textISBN.setText(bundle.getString("ISBN"));

        // Definim els listeners
        btnReserva = ((Button) findViewById(R.id.btnReserva));
        btnReserva.setOnClickListener(this);
        btnTornar = ((Button) findViewById(R.id.btnTornar));
        btnTornar.setOnClickListener(this);

        // Crea un intent amb la pantalla de login
        i = new Intent(VisualitzarInfoLlibre.this, PrincipalActivity.class);

        ed.remove("ImatgePortada");
        ed.clear();
        ed.commit();
    }

    /**
     * Accions al fer click sobre els botons
     *
     * @param v amb el view on s'ha fet click
     */
    @Override
    public void onClick(View v) {
        if (v == btnReserva) {
            showToast("Has reservat el llibre: \n" + textTitol.getText());
        } else if (v == btnTornar) {
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
                Toast.makeText(VisualitzarInfoLlibre.this, toast, Toast.LENGTH_SHORT).show();
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
