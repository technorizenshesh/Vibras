package com.my.vibras.utility;


import android.util.Base64;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * Created by Ravindra Birla on 20,July,2022
 */
public class RandomString {

    // function to generate a random string of length n
    public static String getAlphaNumericString(int n)
    {

        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }

    public static void main(String[] args)
    {

        // Get the size n
        int n = 20;

        // Get and display the alphanumeric string
        System.out.println(RandomString
                .getAlphaNumericString(n));
    }



    public  static  String  IncodeIntoBase64(String data){
      /*  String base64 = null;
        byte[] encrpt= new byte[0];
        encrpt = data.getBytes(StandardCharsets.UTF_8);
        base64 = Base64.encodeToString(encrpt, Base64.DEFAULT);
        return  base64;*/
        return StringEscapeUtils.escapeJava(data);

    }
    public  static  String  DecodeFromBase64(String data) {

       /* String text = null;
        byte[] decrypt = Base64.decode(data, Base64.DEFAULT);
        text = new String(decrypt, StandardCharsets.UTF_8);
        return text;*/
        return StringEscapeUtils.unescapeJava(data);

    }
    }

