package com.fbellotti.vertx;

import io.vertx.core.AbstractVerticle;

/**
 * @author <a href="http://fbellotti.com">Florian BELLOTTI</a>
 */
public class HelloWorld extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    vertx.createHttpServer()
        .requestHandler(req -> req.response().end("Hello World!"))
        .listen(8080);
  }
}
