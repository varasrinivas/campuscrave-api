#!/usr/bin/env bash
# BUG-03 — who decides what the biryani costs?
# The cart total travels from the browser to the server. Send a "generous" one.

curl -s http://localhost:8080/api/orders \
  -H 'Content-Type: application/json' \
  -d '{
    "studentId": 3,
    "pickupBlock": "Block C",
    "totalRupees": 1,
    "items": [ { "dishId": 1, "quantity": 1 } ]
  }'
echo
echo "One biryani, and the wallet was charged... check the receipt above."
echo "Then check the wallet: curl -s http://localhost:8080/api/wallet/3"
