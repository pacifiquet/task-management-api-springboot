package com.pacifique.todoapp.config.db;

import org.testcontainers.containers.PostgreSQLContainer;

public class TodoAppPostgresqlContainer
    extends PostgreSQLContainer<TodoAppPostgresqlContainer> {
    private static final String IMAGE_VERSION = "postgres:12.3-alpine";

    private static boolean initialized = false;
    private static TodoAppPostgresqlContainer container;

    private TodoAppPostgresqlContainer() {
        super(IMAGE_VERSION);
    }

    public static TodoAppPostgresqlContainer getInstance() {
        if (container == null) {
            container = new TodoAppPostgresqlContainer();
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
