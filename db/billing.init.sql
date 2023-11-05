-- Create the "billing" database
CREATE DATABASE "billing";

-- Connect to the "billing" database
\c "billing";
-- Create the "products_schema" schema if it doesn't exist

CREATE SCHEMA IF NOT EXISTS "billing_schema";

-- Create the "billing_user" role with necessary privileges
CREATE USER "billing_user" WITH PASSWORD 'billing_password';
GRANT USAGE ON SCHEMA "billing_schema" TO "billing_user";
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA "billing_schema" TO "billing_user";
ALTER DEFAULT PRIVILEGES IN SCHEMA "billing_schema" GRANT ALL PRIVILEGES ON TABLES TO "billing_user";

\c "billing";

SET search_path TO "billing_schema";

-- Create the "purchases" table within the "products_schema"
CREATE TABLE IF NOT EXISTS "purchases" (
                                                "id"  SERIAL PRIMARY KEY,
                                                "orderId" BIGINT,
                                                "user" VARCHAR(255),
                                                "itemId" BIGINT,
                                                "numberOfUnits" INT,
                                                "dateTime" VARCHAR(255),
                                                "amount" numeric(10, 2)
);


GRANT USAGE, SELECT ON SEQUENCE purchases_id_seq TO billing_user;


-- Insert a sample entry into the "store_products" table
INSERT INTO "purchases" ("orderId", "user", "itemId", "numberOfUnits", "dateTime", "amount") VALUES (1, 'user1', 1, 1, '2020-01-01 00:00:00', 1.00);

