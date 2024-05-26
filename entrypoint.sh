#!/bin/bash

# Function to wait for the database to become available
wait_for_db() {
    while ! nc -z db 5432; do
        echo "Waiting for the database to become available..."
        sleep 1
    done
}

# Wait for the database
wait_for_db

# Check if seeding has already been applied
if [ ! -f /app/.migrated ]; then
    echo "Running database seeding and precomputing tasks..."
    # Run database seeding
    java -jar app.jar seed && \
    java -jar app.jar precompute && \
    java -jar app.jar seed-dummy && \

    # Mark migrations as applied
    touch /app/.migrated
else
    echo "Seeding has already been applied, skipping..."
fi

# Finally, start the server
exec java -jar app.jar

