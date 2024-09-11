package ru.denisaql.virtflux;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MainController {
    private final MainServize mainServize;
    private final ExecutorService executorService =
            new ForkJoinPool(100);

    @GetMapping("/ping")
    public Mono<String> ping() {
        return Mono.justOrEmpty("pong");
    }

    private final CompletableFuture<String> fut =
            CompletableFuture.supplyAsync(() -> sleepAndReturn(150, "pong"),
                    executorService);

    @GetMapping("/ping-with-delay")
    public Mono<String> pingWithDelay(@RequestParam("delay") Integer delay) {
        long longDelay = (delay != null) ? delay : 0L;
        return Mono.fromFuture(fut);
    }

    private String sleepAndReturn(long longDelay, String res) {
        try {
            Thread.sleep(longDelay);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    @GetMapping("/ping-and-read-file")
    public Flux<String> pingAndReadFile(@RequestParam Integer id,
                                        @RequestParam Integer fake) {
        if (id == null) {
            throw new RuntimeException("id should be not null");
        }
        return mainServize.fileById(id);
    }
}
