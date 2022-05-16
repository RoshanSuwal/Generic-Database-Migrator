package org.ekbana.cassandraMigrator.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class MigratorConfiguration {
    // spark properties
    private SparkConfiguration sparkConfiguration;

    // Data Source/Sink Properties
    private Props sourceProps;
    private Props sinkProps;

    private String transformer="org.ekbana.cassandraMigrator.transformer.DefaultTransformer";

    public SparkConfiguration getSparkConfiguration() {
        return sparkConfiguration;
    }

    public Props getSourceProps() {
        return sourceProps;
    }

    public Props getSinkProps() {
        return sinkProps;
    }

    public String getTransformer() {
        return transformer;
    }
}
