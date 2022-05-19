package org.ekbana.cassandraMigrator.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class SparkConfiguration implements Serializable {
//    private String serializer;
//    private String appName;
//    private String master;

    public List<Config> configs;

//    public String getAppName() {
//        return appName;
//    }

//    public String getMaster() {
//        return master;
//    }

    public List<Config> getConfigs() {
        return configs;
    }

    @Getter
    @Setter
    @AllArgsConstructor @NoArgsConstructor
    public static class Config implements Serializable {
        private String key;
        private String value;

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }
}
