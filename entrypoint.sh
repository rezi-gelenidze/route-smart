#!/bin/bash

wait_for_db() {
    while ! nc -z db 5432; do
        echo "Waiting for the database to become available..."
        sleep 1
    done
}

wait_for_db

if [ ! -f /app/.migrated ]; then
    echo "Running database seeding and precomputing tasks..."

    java -jar app.jar seed-graph || exit 1
    java -jar app.jar precompute || exit 1
    java -jar app.jar seed-dummy || exit 1

    touch /app/.migrated
else
    echo "Seeding has already been applied, skipping..."
fi

# Now finally start the server (normal mode, no args)
exec java -jar app.jar
