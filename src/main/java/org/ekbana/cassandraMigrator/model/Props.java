package org.ekbana.cassandraMigrator.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Props implements Serializable {
    public List<Option> options;
    public List<Format> formats;
    public List<Mode> modes;

    public String load;



    public List<String> getFilters() {
        return filters;
    }

    public List<String> filters;

    public List<Option> getOptions() {
        return options;
    }

    public List<Format> getFormats() {
        return formats;
    }

    public List<Mode> getModes() {
        return modes;
    }

    public String getLoad(){return load;}

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Option implements Serializable {
        public String key;
        public String value;
        public String getKey() {
            return key.trim();
        }
        public String getValue() {
            return value.trim();
        }
    }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Format implements Serializable {
        public String value;
        public String getValue() {
            return value.trim();
        }
    }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Mode implements Serializable {
        public String value;
        public String getValue() {
            return value.trim();
        }
    }

}
