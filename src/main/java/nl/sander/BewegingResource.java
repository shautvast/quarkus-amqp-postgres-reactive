package nl.sander;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.stream.JsonCollectors;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Path("/beweging")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BewegingResource {

    @Inject
    BewegingRepo bewegingRepo;

    @GET
    @Path("{ID}")
    public CompletionStage<Response> getBeweging(@PathParam("ID") String id) {
        CompletableFuture<Response> future = new CompletableFuture<>();

        bewegingRepo.getBeweging(id).subscribeAsCompletionStage()
                .thenApply(this::bewegingJson)
                .thenApply(bewegingJson -> Response.ok(bewegingJson).build())
                .exceptionally(throwable -> Response.status(Response.Status.INTERNAL_SERVER_ERROR).build())
                .whenComplete((response, throwable) -> {
                    future.complete(response);
                });

        return future;
    }

    private JsonObject bewegingJson(Beweging beweging) {
        return Json.createObjectBuilder().add("id", beweging.getId()).add("vldatum", beweging.getVlDatum().toString()).add("bewegingcode", beweging.getBewegingcode()).build();
    }

    @GET
    @Path("/all")
    public CompletionStage<Response> getBewegingen() {
        CompletableFuture<Response> future = new CompletableFuture<>();

        bewegingRepo.getBewegingen().collectItems().asList().subscribeAsCompletionStage()
                .thenApply(bewegings -> bewegings.stream().map(this::bewegingJson).collect(JsonCollectors.toJsonArray()))
                .thenApply(jsonArray -> Response.ok(jsonArray).build())
                .exceptionally(throwable -> Response.status(Response.Status.INTERNAL_SERVER_ERROR).build())
                .whenComplete((response, throwable) -> {
                    future.complete(response);
                });

        return future;
    }

    @PUT
    public void putBeweging(Beweging beweging) {
        bewegingRepo.persist(beweging);
    }
}