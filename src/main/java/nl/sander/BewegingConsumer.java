package nl.sander;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class BewegingConsumer {

    @Inject
    ObjectMapper objectMapper;

    @Inject
    BewegingRepo bewegingRepo;

    @Incoming("bewegingen")
    public void consume(String body) {
        try {
            Beweging beweging = objectMapper.readValue(body, Beweging.class);
            System.out.println(beweging);
            bewegingRepo.persist(beweging);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}