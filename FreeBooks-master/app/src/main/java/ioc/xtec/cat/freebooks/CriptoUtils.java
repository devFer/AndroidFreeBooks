package ioc.xtec.cat.freebooks;

import android.util.Base64;

import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by ferranb on 2/04/18.
 */

public class CriptoUtils {
    /**
     * Genera un clau SecretKey a partir d'un text i un tamany de clau
     *
     * @param text    Text a partir del qual es generarà la clau
     * @param keySize Tamany de la clau
     * @return La SecretKey
     */
    public static SecretKey passwordKeyGeneration(String text, int keySize) {
        SecretKey sKey = null;
        if ((keySize == 128) || (keySize == 192) || (keySize == 256)) {
            try {
                byte[] data = text.getBytes("UTF-8");
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                byte[] hash = md.digest(data);
                byte[] key = Arrays.copyOf(hash, keySize / 8);
                sKey = new SecretKeySpec(key, "AES");
            } catch (Exception ex) {
                System.err.println("Error generant la clau:" + ex);
            }
        }
        return sKey;
    }

    /**
     * Genera un String encriptat a partir d'un String, una SecretKeySpec i l'algorisme de xifrat
     *
     * @param dades           Dades a encriptar
     * @param clauSecreta     La SecretKeySpec
     * @param algorismeXifrat L'algorisme
     * @return L'String encriptat
     * @throws Exception
     */
    public static String encriptaDades(String dades, SecretKeySpec clauSecreta, String algorismeXifrat) throws Exception {
        String res = "";
        byte[] data = dades.getBytes("UTF-8");
        byte[] encryptedData = null;
        try {
            Cipher cipher = Cipher.getInstance(algorismeXifrat);
            cipher.init(Cipher.ENCRYPT_MODE, clauSecreta);
            encryptedData = cipher.doFinal(data);
        } catch (Exception e) {
            System.err.println("Error xifrant les dades " + e);
        }
        res = Base64.encodeToString(encryptedData, Base64.NO_WRAP);
        return res;
    }

    /**
     * Genera un String desencriptat a partir d'un String encriptat, una SecretKeySpec i l'algorisme de xifrat
     *
     * @param dades       Dades a desencriptar
     * @param clauSecreta La SecretKeySpec
     * @param algorisme   L'algorisme
     * @return L'String desencriptat
     * @throws Exception
     */
    public static String desencriptaDades(String dades, SecretKeySpec clauSecreta, String algorisme) throws Exception {
        String res = "";
        byte[] data = Base64.decode(dades, Base64.NO_WRAP);
        byte[] decryptedData = null;
        try {
            Cipher cipher = Cipher.getInstance(algorisme);
            cipher.init(Cipher.DECRYPT_MODE, clauSecreta);
            decryptedData = cipher.doFinal(data);
        } catch (Exception e) {
            System.err.println("Error desxifrant les dades " + e);
        }
        res = new String(decryptedData);
        return res;
    }
}
