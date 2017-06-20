package com.fbellotti.vertx;

import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.ext.web.Router;
import io.vertx.rxjava.ext.web.RoutingContext;
import io.vertx.rxjava.ext.web.client.HttpRequest;
import io.vertx.rxjava.ext.web.client.HttpResponse;
import io.vertx.rxjava.ext.web.client.WebClient;
import io.vertx.rxjava.ext.web.codec.BodyCodec;
import rx.Single;

/**
 * @author <a href="http://fbellotti.com">Florian BELLOTTI</a>
 */
public class HelloConsumerMicroservice extends AbstractVerticle {

  private WebClient client;

  @Override
  public void start() {
    client = WebClient.create(vertx);

    Router router = Router.router(vertx);
    router.get("/").handler(this::invokeMyFirstMicroservice);

    vertx.createHttpServer()
      .requestHandler(router::accept)
      .listen(8081);
  }

  private void invokeMyFirstMicroservice(RoutingContext rc) {
    HttpRequest<JsonObject> request1 = client
      .get(8080, "localhost", "/Test")
      .as(BodyCodec.jsonObject());

    HttpRequest<JsonObject> request2 = client
      .get(8080, "localhost", "/Florian")
      .as(BodyCodec.jsonObject());

    Single<JsonObject> s1 = request1.rxSend().map(HttpResponse::body);
    Single<JsonObject> s2 = request2.rxSend().map(HttpResponse::body);

    Single.zip(s1, s2, (Test, Florian) -> {
      return new JsonObject()
        .put("Test", Test.getString("message"))
        .put("Florian", Florian.getString("message"));
    }).subscribe(
      result -> rc.response().end(result.encodePrettily()),
      error -> {
        error.printStackTrace();
        rc.response().setStatusCode(500).end(error.getMessage());
      }
    );
  }
}
