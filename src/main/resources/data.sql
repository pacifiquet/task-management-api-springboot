-- data.sql

-- Create the users table
CREATE TABLE IF NOT EXISTS users
(
    id        BIGSERIAL PRIMARY KEY,
    email     VARCHAR(255) NOT NULL,
    fullName  VARCHAR(255) NOT NULL,
    role      VARCHAR(50)  NOT NULL,
    createdAt TIMESTAMP    NOT NULL
);

-- Create the todo table
CREATE TABLE IF NOT EXISTS todo
(
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT  NOT NULL,
    description TEXT    NOT NULL,
    completed   BOOLEAN NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- Create a sequence for user_id if not exists
DO
$$
    BEGIN
        IF NOT EXISTS (SELECT 1
                       FROM pg_sequences
                       WHERE schemaname = 'public'
                         AND sequencename = 'user_id_sequence')
        THEN
            CREATE SEQUENCE user_id_sequence START 1;
        END IF;
    END
$$;
