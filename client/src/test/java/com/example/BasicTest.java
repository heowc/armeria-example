package com.example;

import com.linecorp.armeria.client.WebClient;
import com.linecorp.armeria.common.AggregatedHttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.common.MediaType;
import com.linecorp.armeria.internal.shaded.guava.base.Stopwatch;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class BasicTest {

    private WebClient webClient = WebClient.of("https://httpbin.org");

    @Test
    void testStatus() {
        final AggregatedHttpResponse response = webClient.get("status/200").aggregate().join();
        assertThat(response.status()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testJson() {
        final AggregatedHttpResponse response = webClient.get("json").aggregate().join();
        assertThat(response.contentType()).isEqualTo(MediaType.JSON);
    }

    @Test
    void testDelay3() {
        final Stopwatch started = Stopwatch.createStarted();
        webClient.get("delay/3").aggregate().join();
        started.stop();
        assertThat(started.elapsed()).isGreaterThan(Duration.ofSeconds(3));
    }
}
