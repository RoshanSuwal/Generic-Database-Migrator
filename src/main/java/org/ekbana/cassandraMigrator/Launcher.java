package org.ekbana.cassandraMigrator;

import org.slf4j.Logger;

import java.io.FileNotFoundException;

public class Launcher {

    public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        DatabaseMigrator.run(args);
    }
}
