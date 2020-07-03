package nl.sander;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.Flowable;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalDate;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * A bean producing random prices every 5 seconds and sending them to the prices JMS queue.
 */
@ApplicationScoped
public class BewegingProducer {

    private final Random random = new Random();

    @Inject
    ObjectMapper objectMapper;


    @Outgoing("generated-beweging")
    public Flowable<String> run() {
        return Flowable.interval(5, TimeUnit.SECONDS).map(tick -> {
            System.out.println("tick");
            Beweging beweging = new Beweging();
            beweging.setBewegingcode(Integer.toString(random.nextInt()));
            beweging.setVlDatum(LocalDate.now());
            return objectMapper.writeValueAsString(beweging);
        });

    }
}