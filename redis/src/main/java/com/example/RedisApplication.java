package com.example;

import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.server.*;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.embedded.RedisServer;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class RedisApplication {

    private static final Logger logger = LoggerFactory.getLogger(RedisApplication.class);
    private static RedisServer redisServer;

    static {
        try {
            redisServer = new RedisServer(6379);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ServerBuilder sb = new ServerBuilder();
        Server server = sb.http(8080)
                .serverListener(new ServerListenerBuilder()
                        .addStartingCallback(s -> {
                            redisServer.start();
                            logger.info("redis start");
                        })
                        .addStoppingCallback(s -> {
                            if (redisServer.isActive()) {
                                redisServer.stop();
                            }
                            logger.info("redis stop");
                        })
                        .build())
                .service("/", new AbstractHttpService() {

                    private RedisTemplate redisTemplate = new RedisTemplate("redis://@localhost:6379");
                    @Override
                    protected HttpResponse doGet(ServiceRequestContext ctx, HttpRequest req) throws Exception {
                        return HttpResponse.of(String.format("Hello, Armeria %d!!", redisTemplate.incr("count")));
                    }
                })
                .build();
        CompletableFuture<Void> future = server.start();
        future.join();
    }
}

class RedisTemplate {

    private final RedisClient redisClient;

    public RedisTemplate(String uri) {
        redisClient = RedisClient.create(uri);
    }

    public Long incr(String key) {
        final StatefulRedisConnection<String, String> connection = redisClient.connect();
        final RedisCommands<String, String> syncCommands = connection.sync();
        try {
            return syncCommands.incr(key);
        } finally {
            connection.close();
        }
    }
}
