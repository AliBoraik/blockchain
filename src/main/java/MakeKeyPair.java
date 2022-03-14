import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;
import service.DbWorker;

import java.security.*;

public class MakeKeyPair {

    public static void main(String[] args) throws NoSuchAlgorithmException {

        Security.addProvider(new BouncyCastleProvider());

        KeyPairGenerator rsa;

        rsa = KeyPairGenerator.getInstance("RSA");
        rsa.initialize(1024, new SecureRandom());
        KeyPair keyPair = rsa.generateKeyPair();

        PrivateKey priKey = keyPair.getPrivate();
        PublicKey pubKey = keyPair.getPublic();

        DbWorker dbWorker = new DbWorker();

        String privateKey = new String(Hex.encode(priKey.getEncoded()));
        String publicKey = new String(Hex.encode(pubKey.getEncoded()));

        dbWorker.addKeys(privateKey, publicKey);


    }
}
