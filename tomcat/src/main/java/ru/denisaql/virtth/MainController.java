package ru.denisaql.virtth;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.denisaql.virtth.dto.TestRequest;
import ru.denisaql.virtth.dto.TestResponse;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MainController {
    private final MainServize mainServize;

    @GetMapping("/ping")
    public String ping() {
        var isVirtual = Thread.currentThread().isVirtual();

        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "pong";
    }

    @GetMapping("/ping-with-delay")
    public String pingWithDelay(@RequestParam Integer delay) {

        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "pong";
    }

    @GetMapping("/ping-and-read")
    public String pingAndRead(@RequestParam Integer id,
                              @RequestParam Integer fake) {
        if (id == null) {
            throw new RuntimeException("id should be not null");
        }
        return mainServize.nameById(id);
    }

    @GetMapping("/ping-and-read-file")
    public String pingAndReadFile(@RequestParam Integer id,
                                  @RequestParam Integer fake) {
        if (id == null) {
            throw new RuntimeException("id should be not null");
        }
        return mainServize.fileById(id);
    }

    @PostMapping("/test")
    public TestResponse testMethod(@RequestBody TestRequest request) {
        LocalDateTime dt = LocalDateTime.now();
        return new TestResponse(request.param1(),
                request.param2(),
                dt.toString());
    }
}
