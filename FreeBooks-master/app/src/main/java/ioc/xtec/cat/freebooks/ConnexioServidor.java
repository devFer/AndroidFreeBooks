package ioc.xtec.cat.freebooks;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.conn.ssl.SSLSocketFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import javax.net.ssl.SSLSocket;

/**
 * Created by jordi on 07/03/2018.
 */

public class ConnexioServidor {

    // Variable amb la resposta
    String resposta = "";

    // Resta de variables
    private String ip_address;
    private int port;
    private SSLSocket socket = null;
    private BufferedWriter out = null;
    private BufferedReader in = null;
    private final String TAG = "TAG";
    private char keystorepass[] = "key12345".toCharArray();
    private char keypassword[] = "12345key".toCharArray();

    Context context;

    public ConnexioServidor(Context context) {
        this.context = context;
    }

    /**
     * Retorna la resposta del servidor, després de passar-li les dades
     * com a paràmetre.
     *
     * @param dades String amb les dades que se li pasaran al servidor
     * @return String amb la resposta del servidor
     */
    public String consulta(final String dades) {
        port = 9999;
        ip_address = "10.0.2.2";
        try {
            KeyStore ks = KeyStore.getInstance("BKS");
            InputStream keyin = context.getResources().openRawResource(R.raw.testserverkeys);
            ks.load(keyin, keystorepass);
            final SSLSocketFactory socketFactory = new SSLSocketFactory(ks);
            socketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            try {
                socket = (SSLSocket) socketFactory.createSocket();
                try {
                    socket.connect(new InetSocketAddress(ip_address,
                            port), 1000);
                    socket.startHandshake();
                    out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    out.write(dades);
                    out.newLine();
                    out.flush();
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    resposta = in.readLine();
                } catch (Exception e) {
                    Log.i(TAG, "El servidor no respon");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (UnknownHostException e) {
            Toast.makeText(context, "Host desconegut", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "host desconegut");
        } catch (IOException e) {
            Toast.makeText(context, "No I/O", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "no I/O");
            e.printStackTrace();
        } catch (KeyStoreException e) {
            Toast.makeText(context, "Keystore ks error", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "keystore ks error");
        } catch (NoSuchAlgorithmException e) {
            Toast.makeText(context, "No hi ha algoritme per ks.load", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "no hi ha algoritme per ks.load");
            e.printStackTrace();
        } catch (CertificateException e) {
            Toast.makeText(context, "Falta el certificat", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "falta el certificat");
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            Toast.makeText(context, "UnrecoverableKeyException", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "unrecoverableKeyException");
            e.printStackTrace();
        } catch (KeyManagementException e) {
            Toast.makeText(context, "KeyManagementException", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "key management exception");
            e.printStackTrace();
        }
        return resposta;
    }
}
