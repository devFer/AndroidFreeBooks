package ioc.xtec.cat.freebooks;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import static ioc.xtec.cat.freebooks.CriptoUtils.desencriptaDades;
import static ioc.xtec.cat.freebooks.CriptoUtils.encriptaDades;
import static ioc.xtec.cat.freebooks.CriptoUtils.passwordKeyGeneration;

public class PrincipalActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String SEPARADOR = "Sep@!-@rad0R";
    public static final String ALGORISME = "AES/ECB/PKCS5Padding";
    public static final String EXTRA_MESSAGE = "ioc.xtec.cat.freeboks.MESSAGE";

    // Variable del botó logout
    Button botoLogout;

    // Variable amb l'intent
    Intent i;

    // Llista de llibres
    private ArrayList<Llibre> llistaLlibres;

    //Resta de variables utilitzades
    private RecyclerView recyclerView;
    private Adaptador adapter;
    private ProgressBar barra_progres;
    MenuItem searchMenuItem;
    MenuItem bookingsMenuItem;
    SearchView searchView;
    SearchManager searchManager;
    BroadcastReceiver broadcast_reciever;

    /**
     * Accions en la creació
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        broadcast_reciever = new BroadcastReceiver() {

            /**
             * Si es res un finish, es finalitza PrincipalActivity
             *
             * @param arg0
             * @param intent
             */
            @Override
            public void onReceive(Context arg0, Intent intent) {
                String action = intent.getAction();
                if (action.equals("finish")) {
                    finish();
                }
            }
        };
        // Registrem el receiver
        registerReceiver(broadcast_reciever, new IntentFilter("finish"));

        // Crea un intent amb la pantalla de login
        i = new Intent(PrincipalActivity.this, MainActivity.class);

        //Posem la barra de progrés amb un màxim de 100
        barra_progres = (ProgressBar) findViewById(R.id.progressBar);
        barra_progres.setMax(100);

        // Preparem la font de les dades
        llistaLlibres = new ArrayList<Llibre>();

        //Referenciem el RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.rView);

        //Afegim l'adaptador amb les dades i el LinearLayoutManager que pintarà les dades
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adaptador(this, llistaLlibres);
        recyclerView.setAdapter(adapter);

        //Podem utilitzar animacions sobre la llista
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // Amaguem el recyclerView
        recyclerView.setVisibility(View.INVISIBLE);
        // Mostrem la barra de progrés
        barra_progres.setVisibility(View.VISIBLE);

        // Executem la tasca per carregar els llibres
        new CarregaLlibres().execute((Void) null);

    }

    /**
     * Infla el menú
     *
     * @param menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        bookingsMenuItem = menu.findItem(R.id.app_bookings);
        bookingsMenuItem.getIcon().setTint(Color.WHITE);
        searchManager = (SearchManager)
                getSystemService(Context.SEARCH_SERVICE);
        searchMenuItem = menu.findItem(R.id.app_search);
        searchView =
                (SearchView) menu.findItem(R.id.app_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        searchView.setFocusable(true);
        searchView.setIconified(true);
        searchView.requestFocusFromTouch();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            /**
             * No implementat
             *
             * @param query
             * @return
             */
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            /**
             * Al modificar el text es van mostrant els resultats filtrats en la llista
             *
             * @param query Amb el text per el que es filtraran els resultats
             * @return
             */
            @Override
            public boolean onQueryTextChange(String query) {
                adapter.getFilter().filter(query);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Accions al seleccionar un item del menú
     *
     * @param item
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //Si el botó és el de buscar, obre el buscador
        if (id == R.id.app_search) {
            // Buscador de llibres

        } else if (id == R.id.app_editUser) {
            // Edita l'usuari

            // Crea un intent amb la pantalla d'edició d'usuari
            String extrasMessage = getIntent().getStringExtra(EXTRA_MESSAGE);
            Intent iEditUser = new Intent(PrincipalActivity.this, EditaUsuariActivity.class);
            iEditUser.putExtra(EXTRA_MESSAGE, extrasMessage);
            startActivity(iEditUser);
        } else if (id == R.id.app_logout) {
            // Tanca la sessió
            tancaSessio();
        } else if (id == R.id.app_refresh) {
            // Refresca la llista de llibres

            // Amaga el recyclerView
            recyclerView.setVisibility(View.GONE);

            // Mostrem la barra de progrés
            barra_progres.setVisibility(View.VISIBLE);

            // Executa la tasca per carregar els llibres
            new CarregaLlibres().execute((Void) null);

        } else if (id == R.id.app_bookings) {
            // TODO Al fer click t'ha de portar a la pantalla de reserves (Falta crear-la)
            showToast("Encara no implementat, es mostraran els llibres reservats");
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Accions al fer click sobre els botons
     *
     * @param v amb el view on s'ha fet click
     */
    @Override
    public void onClick(View v) {
        // No implementat
    }

    /**
     * Torna a la pantalla de login en cas de prémer el botó "Back"
     */
    @Override
    public void onBackPressed() {
        // Crida per tancar la sessió
        tancaSessio();
    }

    /**
     * Tanca la sessió activa i torna a la pantalla de login
     */
    public void tancaSessio() {
        final SecretKey sKey = passwordKeyGeneration("pass*12@", 128);
        // Crea un missatge d'alerta
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                PrincipalActivity.this);

        // Títol del missatge d'alerta
        alertDialogBuilder.setTitle("Tancar sessió: ");

        // Defineix el missatge de l'alerta
        alertDialogBuilder
                .setMessage("Vol abandonar la sessió?")
                .setCancelable(false)
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Thread thread = new Thread(new Runnable() {

                            @Override
                            public void run() {
                                String codiRequestXifrat = "";
                                ConnexioServidor connexioServidor = new ConnexioServidor(getApplicationContext());
                                String extras = getIntent().getStringExtra(EXTRA_MESSAGE);
                                String checkLogin = "userIsLogged" + SEPARADOR + extras.split(SEPARADOR)[0] + SEPARADOR + extras.split(SEPARADOR)[1];
                                try {
                                    codiRequestXifrat = encriptaDades(checkLogin, (SecretKeySpec) sKey, ALGORISME);
                                } catch (Exception ex) {
                                    System.err.println("Error al encriptar: " + ex);
                                }
                                String resposta = connexioServidor.consulta(codiRequestXifrat);
                                if (resposta.equals("OK")) {
                                    String codiRequest = "userLogout" + SEPARADOR + extras.split(SEPARADOR)[0] + SEPARADOR + extras.split(SEPARADOR)[1];
                                    try {
                                        codiRequestXifrat = encriptaDades(codiRequest, (SecretKeySpec) sKey, ALGORISME);
                                    } catch (Exception ex) {
                                        System.err.println("Error al encriptar: " + ex);
                                    }
                                    String resposta2 = connexioServidor.consulta(codiRequestXifrat);
                                    if (resposta2.equals("OK")) {
                                        showToast("Sessió tancada correctament");
                                    }
                                    startActivity(i);
                                    finish();
                                } else {
                                    showToast("El servidor no respón, torna a fer el login més tard...");
                                    startActivity(i);
                                    finish();
                                }
                            }
                        });
                        thread.start();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        // Crea un missatge d'alerta
        AlertDialog alertDialog = alertDialogBuilder.create();

        // Mostra el missatge d'alerta
        alertDialog.show();
    }

    /**
     * Crea un missatge tipus toast
     *
     * @param toast amb el text del missatge
     */
    public void showToast(final String toast) {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(PrincipalActivity.this, toast, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * AsyncTask que carrega les dades dels llibres del servidor, rep Strings com paràmetre
     * d'entrada, actualitza el progrés amb Integers i no retorna res
     */
    private class CarregaLlibres extends AsyncTask<Void, Integer, String> {
        final SecretKey sKey = passwordKeyGeneration("pass*12@", 128);
        String codiRequestXifrat = "";

        protected String doInBackground(Void... voids) {
            // Borrem la llista antiga de llibres
            llistaLlibres.clear();
            // Realitzem la connexió amb el servidor
            ConnexioServidor connexioServidor = new ConnexioServidor(getApplicationContext());
            //Primer revisem si l'usuari està logat
            String extras = getIntent().getStringExtra(EXTRA_MESSAGE);
            String checkLogin = "userIsLogged" + SEPARADOR + extras.split(SEPARADOR)[0] + SEPARADOR + extras.split(SEPARADOR)[1];
            try {
                codiRequestXifrat = encriptaDades(checkLogin, (SecretKeySpec) sKey, ALGORISME);
            } catch (Exception ex) {
                System.err.println("Error al encriptar: " + ex);
            }
            String resposta = connexioServidor.consulta(codiRequestXifrat);
            if (resposta.equals("OK")) {
                // El servidor retorna la llista de llibres en un string
                String llistaBooks = "getBooks";
                try {
                    codiRequestXifrat = encriptaDades(llistaBooks, (SecretKeySpec) sKey, ALGORISME);
                    // Anirem fent connexions al servidor demanant la llista de llibres, fins que aquesta es pugui desencriptar correctament
                    while (llistaBooks.equals("getBooks")) {
                        try {
                            llistaBooks = desencriptaDades(connexioServidor.consulta(codiRequestXifrat), (SecretKeySpec) sKey, ALGORISME);
                        } catch (Exception ex) {
                            System.err.println("Error al desencriptar: " + ex);
                        }
                    }
                } catch (Exception ex) {
                    System.err.println("Error al encriptar: " + ex);
                }
                // Si la llista no és buida carreguem els llibres
                if (!llistaBooks.isEmpty()) {
                    String[] llibresArray = llistaBooks.split("~");
                    int midaArray = llibresArray.length;
                    // Recorrem l'array de llibres
                    for (int i = 0; i < midaArray; i++) {
                        try {
                            llistaLlibres.add(new Llibre(llibresArray[i].split(SEPARADOR)[0], llibresArray[i].split(SEPARADOR)[1],
                                    llibresArray[i].split(SEPARADOR)[2], llibresArray[i].split(SEPARADOR)[3], llibresArray[i].split(SEPARADOR)[4],
                                    llibresArray[i].split(SEPARADOR)[5], llibresArray[i].split(SEPARADOR)[6], llibresArray[i].split(SEPARADOR)[7]));
                        } catch (Exception ex) {
                            System.err.println("Error al afegir llibre:  " + ex);
                        }

                    }

                    // Va massa ràpid a carregar les imatges i no es veu la barra de progrés
                    // Li afegim un segon de retard perqué és vegi sempre
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                } else {
                    showToast("No hi ha llibres a la base de dades");
                }
            } else {
                showToast("El servidor no respón, torna a fer el login més tard...");
                startActivity(i);
                finish();
            }

            return null;
        }

        /**
         * S'executa abans de l'execució de la tasca
         */
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * S'executa durant l'execució de la tasca
         *
         * @param args
         */
        public void onProgressUpdate(Integer... args) {
            super.onProgressUpdate();
        }

        /**
         * S'executa després de l'execució de la tasca
         *
         * @param result
         */
        protected void onPostExecute(String result) {
            // Després de cada modificació a la font de les dades, hem de notificar-ho a l'adaptador
            adapter.notifyDataSetChanged();
            // Mostrem el recyclerView
            recyclerView.setVisibility(View.VISIBLE);
            // Amaguem la barra de progrés
            barra_progres.setVisibility(View.GONE);
        }
    }

    /**
     * Desregistrem el receiver, al finalitzar l'activity.
     */
    @Override
    protected void onStop() {
        unregisterReceiver(broadcast_reciever);
        super.onStop();
    }


}
