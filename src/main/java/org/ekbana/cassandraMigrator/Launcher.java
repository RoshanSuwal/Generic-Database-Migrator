package org.ekbana.cassandraMigrator;

import java.io.FileNotFoundException;

public class Launcher {

    public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        DatabaseMigrator.run(args);

    }
}
