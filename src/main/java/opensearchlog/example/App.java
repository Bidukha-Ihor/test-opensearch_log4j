package opensearchlog.example;

import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch.core.IndexRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Random;
import java.util.logging.LogManager;

/**
 * Hello world!
 */
public class App {
    private final Logger logger = LoggerFactory.getLogger(App.class);
    private final OSConnectionClient connectionClient = new OSConnectionClient();

    private void index(String index, String key, String value) throws IOException {
        IndexRequest indexRequest = new IndexRequest.Builder<IndexData>().index(index).id("id_" + new Random().nextLong(System.currentTimeMillis())).document(new IndexData(key, value)).build();
        connectionClient.getClient().index(indexRequest);
        logger.info("send index {}", key);
    }

    public static void main(String[] args) {
        App app = new App();
        System.out.println("---------start----------");
        for (int i = 0; i < 10; i++) {
            try {
                app.index("test_1", "key_" + i, "value_+i");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("---------done----------");
    }
}
