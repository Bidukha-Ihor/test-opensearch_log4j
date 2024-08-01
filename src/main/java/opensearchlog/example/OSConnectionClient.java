package opensearchlog.example;

import lombok.Data;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.opensearch.client.RestClient;
import org.opensearch.client.RestClientBuilder;
import org.opensearch.client.json.jackson.JacksonJsonpMapper;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.transport.OpenSearchTransport;
import org.opensearch.client.transport.rest_client.RestClientTransport;

import javax.net.ssl.*;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class OSConnectionClient {
    private final OpenSearchClient client;

    public OSConnectionClient() {
        HttpHost host = new HttpHost("localhost", 9200, "https");
        final BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        //Only for demo purposes. Don't specify your credentials in code.
        credentialsProvider.setCredentials(new AuthScope(host), new UsernamePasswordCredentials("admin", "Harry_1993200813"));
        final RestClient restClient = RestClient.builder(host).
                setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                    @Override
                    public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                        httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                        try {
                            SSLContext sc = SSLContext.getInstance("TLSv1.2");
                            sc.init(null, new TrustManager[]{
                                    new X509TrustManager() {
                                        @Override
                                        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                                            return;
                                        }
                                        @Override
                                        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                                            return;
                                        }
                                        @Override
                                        public X509Certificate[] getAcceptedIssuers() {
                                            return null;
                                        }
                                    }
                            }, new SecureRandom());
                            httpClientBuilder.setSSLContext(sc);
                            httpClientBuilder.setSSLHostnameVerifier(new HostnameVerifier() {
                                @Override
                                public boolean verify(String s, SSLSession sslSession) {
                                    return true;
                                }
                            });
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        return httpClientBuilder;
                    }
                }).build();

        this.client = new OpenSearchClient(new RestClientTransport(restClient, new JacksonJsonpMapper()));
    }

    public OpenSearchClient getClient() {
        return client;
    }
}
