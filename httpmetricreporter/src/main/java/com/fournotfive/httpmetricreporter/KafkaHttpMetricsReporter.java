
package com.fournotfive.httpmetricreporter;

import kafka.metrics.KafkaMetricsConfig;
import kafka.metrics.KafkaMetricsReporter;
import kafka.utils.VerifiableProperties;
import org.apache.log4j.Logger;


public class KafkaHttpMetricsReporter implements KafkaMetricsReporter, KafkaHttpMetricsReporterMBean {

    private static Logger LOG = Logger.getLogger(KafkaHttpMetricsReporter.class);
    private boolean initialized = false;
    private boolean running = false;
    private boolean enabled = false;

    private KafkaHttpMetricsServer metricsServer = null;

    private static final int DEFAULT_PORT = 8080;
    private static final String DEFAULT_BIND_ADDRESS = "localhost";

    private String bindAddress = DEFAULT_BIND_ADDRESS;
    private int port = DEFAULT_PORT;


    public void init(VerifiableProperties verifiableProperties) {

        if (!initialized) {
            // get configured metrics from kafka
            KafkaMetricsConfig metricsConfig = new KafkaMetricsConfig(verifiableProperties);

            // get the configured properties from kafka to set the bindAddress and port.
            bindAddress = verifiableProperties.getProperty("kafka.http.metrics.host");
            port = Integer.parseInt(verifiableProperties.getProperty("kafka.http.metrics.port"));
            enabled = Boolean.parseBoolean(verifiableProperties.getProperty("kafka.http.metrics.reporter.enabled"));

            // construct the Metrics Server
            metricsServer = new KafkaHttpMetricsServer(bindAddress, port);
            initialized = true;

            // call the method startReporter
            startReporter(metricsConfig.pollingIntervalSecs());
        } else {
            LOG.error("Kafka Http Metrics Reporter already initialized");
        }
    }

    public synchronized void startReporter(long pollingPeriodSecs) {
        if (initialized && !running && enabled) {
            // start the metrics server
            metricsServer.start();
            running = true;
        } else {
            if (!enabled) {
                LOG.info("Kafka Http Metrics Reporter disabled");
            } else if (running) {
                LOG.error("Kafka Http Metrics Reporter already running");
            }
        }
    }

    public synchronized void stopReporter() {
        if (initialized && running) {
            // stop the metrics server
            metricsServer.stop();
        }
    }

    public String getMBeanName() {
        return "kafka:type=nl.techop.kafka.KafkaHttpMetricsReporter";
    }
}
