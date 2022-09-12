package sample.com.hazelcast.demo.viridian;

import java.io.IOException;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;

import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.SSLConfig;

// tag::class[]
@SpringBootApplication
public class HzViridianDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(HzViridianDemoApplication.class, args);
    }

    @ConditionalOnProperty(
        name = "hazelcast.viridian.tlsEnabled",
        havingValue = "true"
    )
    @Bean
    ClientConfig hazelcastClientConfig(
        @Value("${hazelcast.viridian.discoveryToken}") String discoveryToken,
        @Value("${hazelcast.viridian.clusterName}") String clusterName,
        @Value("${hazelcast.viridian.keyStore}") Resource keyStore,
        @Value("${hazelcast.viridian.keyStorePassword}") String keyStorePassword,
        @Value("${hazelcast.viridian.trustStore}") Resource trustStore,
        @Value("${hazelcast.viridian.trustStorePassword}") String trustStorePassword
    ) throws IOException {
        Properties props = new Properties();
        props.setProperty("javax.net.ssl.keyStore", keyStore.getURI().getPath());
        props.setProperty("javax.net.ssl.keyStorePassword", keyStorePassword);
        props.setProperty("javax.net.ssl.trustStore", trustStore.getURI().getPath());
        props.setProperty("javax.net.ssl.trustStorePassword", trustStorePassword);
        ClientConfig config = new ClientConfig();
        config.getNetworkConfig().setRedoOperation(true);
        config.getNetworkConfig().setSSLConfig(new SSLConfig().setEnabled(true).setProperties(props));
        config.getNetworkConfig()
            .getCloudConfig()
                .setDiscoveryToken(discoveryToken)
                .setEnabled(true);
        config.setClusterName(clusterName);
        config.setProperty("hazelcast.client.cloud.url", "https://api.viridian.hazelcast.com");

        return config;
    }

}
// end::class[]
