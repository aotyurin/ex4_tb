package ru.ex4.apibt.requestPlugin;

import okhttp3.*;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class ExRequest {
    //      инкрементное число
    private static long _nonce;
    private String _path;
    private String _key;
    private String _secret;

    private ExRequest() {
    }

    public ExRequest(String path, String key, String secret) {
        _nonce = System.nanoTime();
        _path = path;
        _key = key;
        _secret = secret;
    }

    public final String post(String method, Map<String, String> arguments) {
        Mac mac;
        SecretKeySpec key;
        String sign;

        if (arguments == null) {
            arguments = new HashMap<>();
        }

        arguments.put("nonce", "" + ++_nonce);

        String postData = joinArguments(arguments);

        // Create a new secret key
        try {
            key = new SecretKeySpec(_secret.getBytes("UTF-8"), "HmacSHA512");
        } catch (UnsupportedEncodingException uee) {
            System.err.println("Unsupported encoding exception: " + uee.toString());
            return null;
        }

        // Create a new mac
        try {
            mac = Mac.getInstance("HmacSHA512");
        } catch (NoSuchAlgorithmException nsae) {
            System.err.println("No such algorithm exception: " + nsae.toString());
            return null;
        }

        // Init mac with key.
        try {
            mac.init(key);
        } catch (InvalidKeyException ike) {
            System.err.println("Invalid key exception: " + ike.toString());
            return null;
        }


        // Encode the post data by the secret and encode the result as base64.
        try {
            sign = Hex.encodeHexString(mac.doFinal(postData.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException uee) {
            System.err.println("Unsupported encoding exception: " + uee.toString());
            return null;
        }

        //  Now do the actual request
        MediaType form = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

        OkHttpClient client = new OkHttpClient();
        try {

            RequestBody body = RequestBody.create(form, postData);
            Request request = new Request.Builder()
                    .url(_path + method)
                    .addHeader("Key", _key)
                    .addHeader("Sign", sign)
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            System.err.println("Request post fail: " + e.toString());
            return null;  // An error occured...
        }
    }

    public final String get(String method, Map<String, String> arguments) {
        String path = _path + method + "/?" + joinArguments(arguments);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(path)
                .get()
                .build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            System.err.println("Request get fail: " + e.toString());
            return null;
        }
    }


    private String joinArguments(Map<String, String> arguments) {
        String postData = "";

        if (arguments == null) {
            return postData;
        }

        for (Map.Entry<String, String> stringStringEntry : arguments.entrySet()) {
            Map.Entry argument = (Map.Entry) stringStringEntry;

            if (postData.length() > 0) {
                postData += "&";
            }
            postData += argument.getKey() + "=" + argument.getValue();
        }
        return postData;
    }

}
