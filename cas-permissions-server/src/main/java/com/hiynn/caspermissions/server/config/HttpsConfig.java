package com.hiynn.caspermissions.server.config;

import org.eclipse.jetty.http.HttpScheme;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yanchao
 * @date 2017/11/22 19:55
 *
 * 使用Jetty配置 Http + Https 双端口进行测试
 */
@Configuration
public class HttpsConfig {

    @Value("${server.port}")
    private int httpPort;

    @Value("${server.https.port}")
    private int httpsPort;

    @Value("${server.https.keyStorePath}")
    private String keyStorePath;

    @Value("${server.https.keyStorePassword}")
    private String keyStorePassword;

    @Bean
    public EmbeddedServletContainerCustomizer servletContainerCustomizer() {
        return new EmbeddedServletContainerCustomizer() {
            @Override
            public void customize(ConfigurableEmbeddedServletContainer container) {
                if (container instanceof JettyEmbeddedServletContainerFactory) {
                    customizeJetty((JettyEmbeddedServletContainerFactory) container);
                }
            }

            public void customizeJetty(JettyEmbeddedServletContainerFactory container) {
                container.addServerCustomizers((Server server) -> {
                    //HTTP
                    ServerConnector connector = new ServerConnector(server);
                    connector.setPort(httpPort);

                    //Https
                    SslContextFactory sslContextFactory = new SslContextFactory();
                    sslContextFactory.setKeyStorePath(keyStorePath);
                    sslContextFactory.setKeyStorePassword(keyStorePassword);

                    HttpConfiguration config = new HttpConfiguration();
                    config.setSecureScheme(HttpScheme.HTTPS.asString());
                    config.addCustomizer(new SecureRequestCustomizer());

                    ServerConnector sslConnector = new ServerConnector(
                            server,
                            new SslConnectionFactory(sslContextFactory, HttpVersion.HTTP_1_1.asString()),
                            new HttpConnectionFactory(config)
                    );
                    sslConnector.setPort(httpsPort);
                    server.setConnectors(new Connector[] {connector, sslConnector});
                    System.out.println("Jetty HTTP setting successful on Port " + httpPort);
                    System.out.println("Jetty SSL setting successful on Port " + httpsPort);
                });
            }
        };
    }
}
