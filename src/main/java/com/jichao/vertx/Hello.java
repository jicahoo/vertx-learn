package com.jichao.vertx;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;

import java.nio.charset.StandardCharsets;


public class Hello {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();

        HttpClient client = vertx.createHttpClient();
        client.getNow(443, "www.ietf.org", "/standards/rfcs/", response ->
        {
            response.bodyHandler(
                    body ->
                            System.out.println(body.toString(StandardCharsets.UTF_8)));
        });
        //Bad
    }
}
