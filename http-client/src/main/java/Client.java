import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class Client {
    private static final int DELAY = 150;
    private static final int CLIENT_COUNT = 250;
    private static final int FUT_COUNT = 50_000;

    private static final Random RANDOM = new SecureRandom();

    public static void main(String[] args) {
        Client client = new Client();
        client.run();
    }

    public void run() {
        List<HttpClient> clients = new ArrayList<>();
        for (int i = 0; i < CLIENT_COUNT; ++i) {
            clients.add(HttpClient.newBuilder()
                    .build());
        }

        //CompletableFuture<HttpResponse<String>>[] fut = findFileById(clients);
        CompletableFuture<HttpResponse<String>>[] fut = pingWithDelay(clients);

        try {
            CompletableFuture.allOf(fut).join();
        } catch (Exception ex) {
            int cnt = 0;
            for (int i = 0; i < fut.length; ++i) {
                if (fut[i].isDone() && !fut[i].isCompletedExceptionally()) {
                    ++cnt;
                }
            }
            System.out.printf("%d of %d completed successfully\n",
                    cnt, fut.length);
        }

        System.out.println("The End");
    }

    public CompletableFuture<HttpResponse<String>>[] pingWithDelay(List<HttpClient> clients) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8084/api/v1/ping-with-delay?delay=" + DELAY))
                .GET()
                .build();

        CompletableFuture<HttpResponse<String>>[] res = new CompletableFuture[FUT_COUNT];

        for (int i = 0; i < res.length; ++i) {
            res[i] = (clients.get(i % clients.size())
                    .sendAsync(request, HttpResponse.BodyHandlers.ofString()));
        }

        return res;
    }

    private CompletableFuture<HttpResponse<String>>[] findById(List<HttpClient> clients) {
        CompletableFuture<HttpResponse<String>>[] res = new CompletableFuture[FUT_COUNT];

        for (int i = 0; i < res.length; ++i) {
            int idToFind = RANDOM.nextInt();
            int fake = RANDOM.nextInt();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8084/api/v1/ping-and-read?id=" + idToFind + "&fake=" + fake))
                    .GET()
                    .build();

            res[i] = (clients.get(i % clients.size())
                    .sendAsync(request, HttpResponse.BodyHandlers.ofString()));
        }

        return res;
    }

    private CompletableFuture<HttpResponse<String>>[] findFileById(List<HttpClient> clients) {
        CompletableFuture<HttpResponse<String>>[] res = new CompletableFuture[FUT_COUNT];

        for (int i = 0; i < res.length; ++i) {
            int idToFind = RANDOM.nextInt();
            int fake = RANDOM.nextInt();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8084/api/v1/ping-and-read-file?id=" + idToFind + "&fake=" + fake))
                    .GET()
                    .build();

            res[i] = (clients.get(i % clients.size())
                    .sendAsync(request, HttpResponse.BodyHandlers.ofString()));
        }

        return res;
    }
}


