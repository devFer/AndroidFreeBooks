package ioc.xtec.cat.freebooks;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import static ioc.xtec.cat.freebooks.CriptoUtils.encriptaDades;
import static ioc.xtec.cat.freebooks.CriptoUtils.passwordKeyGeneration;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PERMISSION = 0;
    public static final String SEPARADOR = "Sep@!-@rad0R";
    public static final String ALGORISME = "AES/ECB/PKCS5Padding";
    public static final String EXTRA_MESSAGE = "ioc.xtec.cat.freeboks.MESSAGE";

    Context c;

    // Variables dels botons
    Button botoInici, botoAlta, botoSortir;

    // Variables dels TextViews
    TextView textUsuari, textContrasenya, missatge;

    // Codi per identificar la sessió
    private String codiSessio = "";

    /**
     * Accions en la creació
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        c = this;

        try {
            //Demanem a l'usuari que dongui els permisos necessaris a l'aplicació
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.INTERNET
            }, PERMISSION);

        } catch (SecurityException se) {
            se.printStackTrace();
        }

    }

    /**
     * Verifica si s'han donat els permisos
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    //showToast("S'han donat els permisos");

                    // Definim els listeners
                    botoInici = ((Button) findViewById(R.id.buttonIniciarSessio));
                    botoInici.setOnClickListener(this);
                    botoAlta = ((Button) findViewById(R.id.buttonDonarAlta));
                    botoAlta.setOnClickListener(this);
                    botoSortir = ((Button) findViewById(R.id.buttonSortir));
                    botoSortir.setOnClickListener(this);

                    textUsuari = ((TextView) findViewById(R.id.textUsuari));
                    textContrasenya = ((TextView) findViewById(R.id.textContrasenya));
                    missatge = ((TextView) findViewById(R.id.missatge));
                } else {
                    finish();
                    showToast("No s'han donat els permisos, torna a inciar l'app...");
                }
        }
    }

    /**
     * Accions al fer click sobre els botons
     *
     * @param v amb el view on s'ha fet click
     */
    @Override
    public void onClick(View v) {

        //  Mirem en quin botó ha fet click l'usuari
        if (v == botoInici) {
            // Crida per iniciar la sessió
            iniciarSessio();
        } else if (v == botoAlta) {
            Intent i = new Intent(this, AltaUsuariActivity.class);
            startActivity(i);
            finish();
        } else if (v == botoSortir) {
            finish();
        }

    }

    /**
     * Envia les dades al servidor perqué aquest validi el login
     * Si es correcte inicia sessió, en cas contrari mostrarà missatges indicant l'error
     */
    public void iniciarSessio() {
        final SecretKey sKey = passwordKeyGeneration("pass*12@", 128);

        // Obté les dades de login introduïdes per l'usuari
        final String usuariIntroduit = textUsuari.getText().toString();
        final String passIntroduit = textContrasenya.getText().toString();

        final String codiRequest = "userLogin" + SEPARADOR + usuariIntroduit + SEPARADOR + passIntroduit + SEPARADOR + "Mobile";


        final Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                String codiRequestXifrat = "";
                try {
                    try {
                        if (usuariIntroduit.equals("") || passIntroduit.equals("")) {
                            showToast("Falten dades");
                        } else {
                            ConnexioServidor connexioServidor = new ConnexioServidor(c);
                            codiRequestXifrat = encriptaDades(codiRequest, (SecretKeySpec) sKey, ALGORISME);
                            codiSessio = connexioServidor.consulta(codiRequestXifrat);
                            if (codiSessio.equals("FAIL")) {
                                showToast("Usuari o contrasenya invàlids");
                            } else if (codiSessio.startsWith("OK")) {
                                String message = usuariIntroduit + SEPARADOR + "Mobile";
                                showToast("Usuari i contrasenya vàlids");
                                Intent i = new Intent(MainActivity.this, PrincipalActivity.class);
                                i.putExtra(EXTRA_MESSAGE, message);
                                startActivity(i);
                                finish();
                            } else {
                                showToast("El servidor no respon");
                            }
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    /**
     * Crea un missatge tipus toast
     *
     * @param toast amb el text del missatge
     */
    public void showToast(final String toast) {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(MainActivity.this, toast, Toast.LENGTH_SHORT).show();
            }
        });
    }

}