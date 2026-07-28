#!/usr/bin/env bash
# BUG-02 — the menu that queries the database once per dish.
# Turn on SQL logging first:
#   ./mvnw spring-boot:run -Dspring-boot.run.arguments=--logging.level.org.hibernate.SQL=DEBUG
# Then load the menu once and count the SELECT statements that scroll past.

curl -s http://localhost:8080/api/menu > /dev/null
echo "Menu loaded once. Now count the SELECTs in the API log."
echo "One page. Eight dishes. How many queries should that take?"
