package nl.sander;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.sqlclient.PoolOptions;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.function.Function;
import java.util.stream.StreamSupport;

@ApplicationScoped
public class BewegingRepo {
    @Inject
    PgPool client;

//    @PostConstruct
//    public void init() {
//        PgConnectOptions connectOptions = new PgConnectOptions()
//                .setPort(5432)
//                .setHost("localhost")
//                .setDatabase("postgres")
//                .setUser("postgres")
//                .setPassword("sander");
//
//        PoolOptions poolOptions = new PoolOptions()
//                .setMaxSize(2);
//
//        client = PgPool.pool(connectOptions, poolOptions);
//    }

    public Uni<Beweging> getBeweging(String id) {
        String sql = "select * from beweging where id = $1";

        Uni<RowSet<Row>> rowSet = client.preparedQuery(sql).execute(Tuple.of(Long.valueOf(id)));
        return rowSet
                .onItem().produceUni(set -> Uni.createFrom().item(() -> StreamSupport.stream(set.spliterator(), false).findAny().orElse(null)))
                .onItem().apply(mapRow());
    }

    public Multi<Beweging> getBewegingen() {
        String sql = "select * from beweging";

        Uni<RowSet<Row>> rowSet = client.query(sql).execute();
        return rowSet
                .onItem().produceMulti(set -> Multi.createFrom().items(() -> StreamSupport.stream(set.spliterator(), false)))
                .onItem().apply(mapRow());

    }

    private Function<Row, Beweging> mapRow() {
        return row -> new Beweging(row.getLong("id"), row.getLocalDate("vldatum"), row.getString("bewegingcode"));
    }



    @Transactional
    public void persist(Beweging beweging) {
        client.preparedQuery("insert into beweging(vldatum, bewegingcode) values ($1,$2)")
                .execute(Tuple.of(beweging.getVlDatum(), beweging.getBewegingcode())).await().indefinitely();
    }
}
