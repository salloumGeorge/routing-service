-- Create the "products" database
CREATE DATABASE "products";

-- Connect to the "products" database
\c "products";

-- Create the "products_schema" schema if it doesn't exist
CREATE SCHEMA IF NOT EXISTS "products_schema";

-- Create the "products_user" role with necessary privileges
CREATE USER "products_user" WITH PASSWORD 'products_password';
GRANT USAGE ON SCHEMA "products_schema" TO "products_user";
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA "products_schema" TO "products_user";
ALTER DEFAULT PRIVILEGES IN SCHEMA "products_schema" GRANT ALL PRIVILEGES ON TABLES TO "products_user";

\c "products";

-- Set the search path to include the "products_schema"
SET search_path TO "products_schema";

-- Create the "store_products" table within the "products_schema"
CREATE TABLE IF NOT EXISTS "store_products" (
                                                "id" serial PRIMARY KEY,
                                                "name" varchar(255) NOT NULL,
                                                "price" numeric(10, 2) NOT NULL
);

GRANT USAGE, SELECT ON SEQUENCE store_products_id_seq TO products_user;


-- Insert a sample entry into the "store_products" table
INSERT INTO "store_products" ("name", "price")
VALUES ('Sample Product', 19.99);
