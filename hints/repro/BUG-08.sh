#!/usr/bin/env bash
# BUG-08 — two repos, two vocabularies.
# Place an order, then read the raw status the API sends to the browser.

API=http://localhost:8080

ORDER_ID=$(curl -s "$API/api/orders" \
  -H 'Content-Type: application/json' \
  -d '{"studentId": 3, "pickupBlock": "Block C", "totalRupees": 50,
       "items": [ { "dishId": 2, "quantity": 1 } ]}' | grep -o '"orderId":[0-9]*' | cut -d: -f2)

curl -s "$API/api/orders/$ORDER_ID/status"
echo
echo "Note the exact spelling of every status this API can produce"
echo "(entity/OrderStatus.java), then find the strings campuscrave-web's"
echo "StatusTimeline component switches on. Compare the lists. Slowly."
