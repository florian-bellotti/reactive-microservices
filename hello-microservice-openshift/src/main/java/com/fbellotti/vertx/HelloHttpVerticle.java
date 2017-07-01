package com.fbellotti.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.*;

/**
 * @author <a href="http://fbellotti.com">Florian BELLOTTI</a>
 */
public class HelloHttpVerticle extends AbstractVerticle {

  static final String HOSTNAME = System.getenv("HOSTNAME");

  @Override
  public void start() {
    Router router = Router.router(vertx);
    router.get("/").handler(this::hello);
    router.get("/:name").handler(this::hello);
    vertx.createHttpServer()
      .requestHandler(router::accept)
      .listen(8080);
  }

  private void hello(RoutingContext rc) { String message = "hello";
    if (rc.pathParam("name") != null) {
      message += " " + rc.pathParam("name");
    }
    JsonObject json = new JsonObject()
      .put("message", message)
      .put("served-by", HOSTNAME);
    rc.response()
      .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
      .end(json.encode());
  }
}
