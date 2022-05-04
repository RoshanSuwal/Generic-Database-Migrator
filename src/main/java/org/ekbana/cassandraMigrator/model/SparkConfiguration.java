package org.ekbana.cassandraMigrator.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class SparkConfiguration {
//    private String serializer;
    private String appName;
    private String master;

    public String getAppName() {
        return appName;
    }

    public String getMaster() {
        return master;
    }
}
