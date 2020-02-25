
package siap.sico.saml.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 *CryptPassword - Classe di Utilita' per criptare la pwd del keystore e del certificato.
 * La pwd criptata generata deve essere memorizzata nel file di configurazione nsc.properties.
 * Viene stampata a video.
 * @author Giselda De Vita
 */
public class CryptPassword {

    public static void main(String[] args) {
        try {

            //leggo la string in Input
            String pwdArg;

            if (args.length > 0) {
                pwdArg = new String(args[0]);
            } else {
                return;
            }

            DESKeySpec keySpec = new DESKeySpec("cr1pth0n".getBytes("UTF8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(keySpec);
            BASE64Encoder base64encoder = new BASE64Encoder();
            BASE64Decoder base64decoder = new BASE64Decoder();


// ENCODE plainTextPassword String
            byte[] cleartext = pwdArg.getBytes("UTF8");
            Cipher cipher = Cipher.getInstance("DES"); // cipher is not thread safe
            cipher.init(Cipher.ENCRYPT_MODE, key);
            String encrypedPwd = base64encoder.encode(cipher.doFinal(cleartext));
// now you can store it

// DECODE encryptedPwd String
            byte[] encrypedPwdBytes = base64decoder.decodeBuffer(encrypedPwd);
            Cipher cipherDec = Cipher.getInstance("DES");// cipher is not thread safe
            cipherDec.init(Cipher.DECRYPT_MODE, key);
            /*byte[] plainTextPwdBytes = (*/cipherDec.doFinal(encrypedPwdBytes)/*)*/;
            return;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    
    /**
     *
     * @param encrypedPwd
     * @return
     */
public    static String decryptPwd(String encrypedPwd) {
        try {

            if (!(encrypedPwd != null && encrypedPwd.length() > 0)) {

                throw new Exception("Illegal Password Size");
            }

            DESKeySpec keySpec = new DESKeySpec("cr1pth0n".getBytes("UTF8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(keySpec);
            BASE64Decoder base64decoder = new BASE64Decoder();

// DECODE encryptedPwd String
            byte[] encrypedPwdBytes = base64decoder.decodeBuffer(encrypedPwd);
            Cipher cipherDec = Cipher.getInstance("DES");// cipher is not thread safe
            cipherDec.init(Cipher.DECRYPT_MODE, key);
            byte[] plainTextPwdBytes = (cipherDec.doFinal(encrypedPwdBytes));
            String pwdDecriptata = new String(plainTextPwdBytes);

            return pwdDecriptata;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }
}