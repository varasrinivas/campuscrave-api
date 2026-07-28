#!/usr/bin/env bash
# BUG-06 — the dish that sells out forever.
# Order the last portions, cancel, then look at the shelf.

API=http://localhost:8080

echo "Biryani stock before:"
curl -s "$API/api/menu/1" | grep -o '"stock":[^,]*'

echo "Ordering 2 biryani..."
ORDER_ID=$(curl -s "$API/api/orders" \
  -H 'Content-Type: application/json' \
  -d '{"studentId": 3, "pickupBlock": "Block C", "totalRupees": 180,
       "items": [ { "dishId": 1, "quantity": 2 } ]}' | grep -o '"orderId":[0-9]*' | cut -d: -f2)

echo "Cancelling order $ORDER_ID..."
curl -s -X POST "$API/api/orders/$ORDER_ID/cancel"

echo "Biryani stock after the cancel:"
curl -s "$API/api/menu/1" | grep -o '"stock":[^,]*'
echo "The student got their money back. Did the kitchen get its biryani back?"
