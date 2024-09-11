package ru.denisaql.virtth;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.denisaql.virtth.persist.Repo;
import ru.denisaql.virtth.persist.TestTab;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MainServize {
    private final Repo repo;

    private static final Random RANDOM = new SecureRandom();
    private static final String PREFIX = "LONGTABLENAMEMEDOUBLE";
    private static final String ROOT_PATH = "D:/dev2/_virtual/~files";

    @Transactional
    public String nameById(int param) {
        if (param < 0) {
            param = -param;
        }
        param = param % 1_100_000;
        String idToFind = PREFIX + param;
        return repo.findById(idToFind)
                .orElseThrow(() -> new RuntimeException("test"))
                .getName();
    }

    public String fileById(int param) {
        try {
            return fileByIdInternal(param);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String fileByIdInternal(int param) throws IOException {
        if (param < 0) {
            param = -param;
        }

        param = param % 1_000;
        String fileName = ROOT_PATH + "/" + param + ".txt";

        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String currentLine = reader.readLine();
        reader.close();
        return currentLine;
    }

    @PostConstruct
    public void createFiles() throws FileNotFoundException {

        for (int i = 0; i <= 1000; ++i) {
            String fileName = ROOT_PATH + "/" + i + ".txt";
            PrintWriter printWriter = new PrintWriter(fileName);
            for (int j = 0; j < 100; ++j) {
                Long content = 100_000L + RANDOM.nextLong(899_999L);
                printWriter.print(content);
            }
            printWriter.close();
        }
    }
}
