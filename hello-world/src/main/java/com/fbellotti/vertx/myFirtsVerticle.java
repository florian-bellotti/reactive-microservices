package com.fbellotti.vertx;


import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.http.HttpServer;

/**
 * @author <a href="http://fbellotti.com">Florian BELLOTTI</a>
 */
public class myFirtsVerticle extends AbstractVerticle {

  @Override
  public void start() {
    HttpServer server = vertx.createHttpServer();

    // get the stream of request as Observable
    server
      .requestStream()
      .toObservable()
      .subscribe(req ->
        req.response().end("Hello from " + Thread.currentThread().getName())
      );

    // start server and subcribe
    server
      .rxListen(8080)
      .subscribe();
  }
}