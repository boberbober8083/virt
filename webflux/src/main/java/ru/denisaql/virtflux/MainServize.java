package ru.denisaql.virtflux;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Service
public class MainServize {
    private static final String ROOT_PATH = "D:/dev2/_virtual/~files";

    public Flux<String> fileById(Integer param) {
        if (param == null) {
            param = 0;
        }
        if (param < 0) {
            param = -param;
        }
        param = param % 1_000;
        String fileName = ROOT_PATH + "/" + param + ".txt";

        return Flux.using(
                () -> new FileReader(fileName),
                reader -> Flux.fromStream(new BufferedReader(reader).lines()),
                reader -> {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }
}
