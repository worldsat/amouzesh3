package com.makancompany.assistant.Kernel.Helper;

import android.util.Log;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA2 {
    public static String GenerateHash(String input, String digestSize)
            throws NoSuchAlgorithmException {

        String algorithm = "SHA-";
        MessageDigest objSHA = MessageDigest.getInstance(algorithm.concat(digestSize));
        byte[] bytSHA = objSHA.digest(input.getBytes(StandardCharsets.UTF_16LE));
        BigInteger intNumber = new BigInteger(1, bytSHA);
        String strHashCode = intNumber.toString(16);

        // pad with 0 if the hexa digits are less then 64.
        while (strHashCode.length() < 64) {
            strHashCode = "0" + strHashCode;
        }
        Log.i("moh3n", "GenerateHash: "+strHashCode);
        return strHashCode;
    }

}