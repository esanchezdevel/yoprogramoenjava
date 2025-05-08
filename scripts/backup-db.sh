#!/bin/bash

# Script that retrieve a backup from a postgreSQL database that is deployed in a Docker container
# and send it via mail

export HOME=/home/ec2-user

# === CONFIGURATION ===
CONTAINER_NAME="" # database docker container name
DB_NAME="" # database name
DB_USER="" # database user
BACKUP_DIR="/tmp/postgres_backups"
DATE=$(date +"%Y-%m-%d_%H-%M-%S")
BACKUP_FILE="${BACKUP_DIR}/${DB_NAME}_backup_${DATE}.sql"
ARCHIVE_FILE="${BACKUP_FILE}.tar.gz"
EMAIL_TO="" # email where the script must send the backup file
EMAIL_SUBJECT="programandoconjava.com PostgreSQL Backup - ${DATE}"
EMAIL_BODY="Backup of programandoconjava.com database '${DB_NAME}' created on ${DATE}."

# === CREATE BACKUP DIR ===
mkdir -p "$BACKUP_DIR"

# === RUN PG_DUMP INSIDE CONTAINER ===
/usr/bin/docker exec -i "$CONTAINER_NAME" pg_dump -U "$DB_USER" --no-owner --no-privileges --no-comments --inserts "$DB_NAME" > "$BACKUP_FILE"
if [ $? -ne 0 ]; then
  echo "Backup failed!"
  exit 1
fi

# === COMPRESS BACKUP ===
/usr/bin/tar -czf "$ARCHIVE_FILE" -C "$BACKUP_DIR" "$(basename "$BACKUP_FILE")"
rm "$BACKUP_FILE"

# === SEND BY EMAIL (using mutt) ===
echo "$EMAIL_BODY" | /usr/bin/mutt -s "$EMAIL_SUBJECT" -a "$ARCHIVE_FILE" -- "$EMAIL_TO"

# === OPTIONAL CLEANUP ===
rm "$ARCHIVE_FILE"