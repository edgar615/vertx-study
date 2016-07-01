package com.edgar.vertx.servicediscovery;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import io.vertx.servicediscovery.ServiceReference;
import io.vertx.servicediscovery.impl.DiscoveryImpl;
import io.vertx.servicediscovery.types.MessageSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static com.jayway.awaitility.Awaitility.await;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class MessageSourceTest {

    private Vertx vertx;
    private ServiceDiscovery discovery;

    @Before
    public void setUp() {
        vertx = Vertx.vertx();
        discovery = ServiceDiscovery.create(vertx);
//        discovery = ServiceDiscovery.create(vertx, new ServiceDiscoveryOptions()
//                .setBackendConfiguration(new JsonObject().put("host", "10.11.0.31").put("key", "records")));;
    }

    @After
    public void tearDown() {
        discovery.close();
        AtomicBoolean completed = new AtomicBoolean();
        vertx.close((v) -> completed.set(true));
        await().untilAtomic(completed, is(true));
    }

    @Test
    public void test() throws InterruptedException {
        Random random = new Random();
        vertx.setPeriodic(10, l -> {
            vertx.eventBus().publish("data", random.nextDouble());
        });

        Record record = MessageSource.createRecord("Hello", "data");

        discovery.publish(record, (r) -> {
        });
        await().until(() -> record.getRegistration() != null);

        AtomicReference<Record> found = new AtomicReference<>();
        discovery.getRecord(new JsonObject().put("name", "Hello"), ar -> {
            found.set(ar.result());
        });

        await().until(() -> found.get() != null);

        ServiceReference service = discovery.getReference(found.get());
        MessageConsumer<Double> consumer = service.get();

        List<Double> data = new ArrayList<>();
        consumer.handler(message -> {
            data.add(message.body());
        });
        await().until(() -> !data.isEmpty());
        service.release();
        int size = data.size();
        Thread.sleep(500);
        assertThat(data.size()).isEqualTo(size);

        // Just there to be sure we can call it twice
        service.release();
    }
}