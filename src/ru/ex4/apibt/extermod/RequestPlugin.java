package ru.ex4.apibt.extermod;

import okhttp3.*;
import org.apache.commons.codec.binary.Hex;
import ru.ex4.apibt.log.Logs;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


class RequestPlugin {
    //      инкрементное число
    private static long _nonce;
    private String _url;
    private String _key;
    private String _secret;

    private RequestPlugin() {
    }

    RequestPlugin(String url, String key, String secret) {
        _nonce = System.nanoTime();
        _url = url;
        _key = key;
        _secret = secret;
    }

    final synchronized String  post(String method, Map<String, String> arguments) {
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
            Logs.error("Unsupported encoding exception: " + uee.toString());
            return null;
        }

        // Create a new mac
        try {
            mac = Mac.getInstance("HmacSHA512");
        } catch (NoSuchAlgorithmException nsae) {
            Logs.error("No such algorithm exception: " + nsae.toString());
            return null;
        }

        // Init mac with key.
        try {
            mac.init(key);
        } catch (InvalidKeyException ike) {
            Logs.error("Invalid key exception: " + ike.toString());
            return null;
        }


        // Encode the post data by the secret and encode the result as base64.
        try {
            sign = Hex.encodeHexString(mac.doFinal(postData.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException uee) {
            Logs.error("Unsupported encoding exception: " + uee.toString());
            return null;
        }

        //  Now do the actual request
        MediaType form = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

        OkHttpClient client = new OkHttpClient.Builder().readTimeout(1, TimeUnit.MINUTES).build();
        try {
            RequestBody body = RequestBody.create(form, postData);
            Request request = new Request.Builder()
                    .url(_url + method)
                    .addHeader("Key", _key)
                    .addHeader("Sign", sign)
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            Logs.error("Request post fail: " + e.toString());
            return null;  // An error occured...
        }
    }

    final synchronized String get(String method, Map<String, String> arguments) {
        String path = _url + method + "/?" + joinArguments(arguments);

        OkHttpClient client = new OkHttpClient.Builder().readTimeout(1, TimeUnit.MINUTES).build();
        Request request = new Request.Builder()
                .url(path)
                .get()
                .build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            Logs.error("Request get fail: " + e.toString());
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
