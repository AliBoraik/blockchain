package service;

import Models.BlockModel;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Component
public class SignService {

    public static final String DIGEST_ALGORITHM = "SHA-256";
    public static final String KEY_ALGORITHM = "RSA";
    public static final String SIGN_ALGORITHM = "SHA256withRSA";

    public static String publicKey;
    public static String privateKey;

    public SignService(DbWorker dbWorker) throws Exception {

        String[] keys = dbWorker.getKeys();

        privateKey = keys[0];
        publicKey = keys[1];

    }


    public byte[] getHash(BlockModel block) throws NoSuchAlgorithmException, UnsupportedEncodingException {

        MessageDigest digest = MessageDigest.getInstance(DIGEST_ALGORITHM);

        byte[] result = digest.digest(
                concat(concat(block.getPrevhash() != null ? block.getPrevhash().getBytes() : null,
                                block.getData().toString().getBytes(StandardCharsets.UTF_8)),
                        block.getTs().getBytes()));
        return result;
    }

    public static byte[] concat(byte[] a, byte[] b) {
        if (a == null) return b;
        if (b == null) return a;
        int len_a = a.length;
        int len_b = b.length;
        byte[] C = new byte[len_a + len_b];
        System.arraycopy(a, 0, C, 0, len_a);
        System.arraycopy(b, 0, C, len_a, len_b);
        return C;
    }

    public static KeyPair loadKeys() throws Exception {

        byte[] publicKeyHex = publicKey.getBytes();
        byte[] privateKeyHex = privateKey.getBytes();

        PublicKey publicKey = convertArrayToPublicKey(Hex.decode(publicKeyHex), KEY_ALGORITHM);
        PrivateKey privateKey = convertArrayToPrivateKey(Hex.decode(privateKeyHex), KEY_ALGORITHM);

        KeyPair keyPair = new KeyPair(publicKey, privateKey);
        return keyPair;
    }

    public static PublicKey convertArrayToPublicKey(byte encoded[], String algorithm) throws Exception {
        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(encoded);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);

        return pubKey;
    }

    public static PrivateKey convertArrayToPrivateKey(byte encoded[], String algorithm) throws Exception {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        PrivateKey priKey = keyFactory.generatePrivate(keySpec);
        return priKey;
    }

    public byte[] generateRSAPSSSignature(PrivateKey privateKey, byte[] input)
            throws GeneralSecurityException {
        Signature signature = Signature.getInstance(SIGN_ALGORITHM);

        signature.initSign(privateKey);

        signature.update(input);

        return signature.sign();
    }

    public byte[] generateRSAPSSSignature(byte[] input)
            throws Exception {
        Signature signature = Signature.getInstance(SIGN_ALGORITHM);

        signature.initSign(convertArrayToPrivateKey(Hex.decode(privateKey), KEY_ALGORITHM));

        signature.update(input);

        return signature.sign();
    }

    public static boolean verifyRSAPSSSignature(PublicKey publicKey, byte[] input, byte[] encSignature)
            throws GeneralSecurityException {
        Signature signature = Signature.getInstance(SIGN_ALGORITHM);

        signature.initVerify(publicKey);

        signature.update(input);

        return signature.verify(encSignature);
    }

}
