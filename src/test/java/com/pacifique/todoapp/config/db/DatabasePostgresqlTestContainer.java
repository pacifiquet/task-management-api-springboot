package com.pacifique.todoapp.config.db;

import org.testcontainers.containers.PostgreSQLContainer;

public class DatabasePostgresqlTestContainer
    extends PostgreSQLContainer<DatabasePostgresqlTestContainer> {
    private static final String IMAGE_VERSION = "postgres:12.3-alpine";

    private static boolean initialized = false;
    private static DatabasePostgresqlTestContainer container;

    private DatabasePostgresqlTestContainer() {
        super(IMAGE_VERSION);
    }

    public static DatabasePostgresqlTestContainer getInstance() {
        if (container == null) {
            container = new DatabasePostgresqlTestContainer();
        }
        return container;
    }

    @Override
    public void start() {
        if (!initialized) {
            super.start();
            System.setProperty("DB_URL", container.getJdbcUrl());
            System.setProperty("DB_USERNAME", container.getUsername());
            System.setProperty("DB_PASSWORD", container.getPassword());
            initialized = true;
        }
    }

    @Override
    public void stop() {
        //do nothing, JVM handles shut down
    }
}
