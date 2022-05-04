package org.ekbana.cassandraMigrator.model;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
@ToString
public class CassandraProps implements Serializable {
    private String host="localhost";
    private int port=9042;
    private String username="cassandra";
    private String password="cassandra";
    private String keyStorePath;
    private String keyStorePassword;
    private String trustStorePath;
    private String trustStorePassword;
    private String keyspace;
    private String tables;

    private boolean enableSSL=false;

    public boolean isEnableSSL() {
        return enableSSL;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getKeyStorePath() {
        return keyStorePath;
    }

    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    public String getTrustStorePath() {
        return trustStorePath;
    }

    public String getTrustStorePassword() {
        return trustStorePassword;
    }

    public String getKeyspace() {
        return keyspace;
    }

    public String getTables() {
        return tables;
    }
}
