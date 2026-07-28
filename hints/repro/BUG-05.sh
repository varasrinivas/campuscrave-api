#!/usr/bin/env bash
# BUG-05 — the fourth order.
# MAX_ACTIVE_ORDERS is 3. Place four dosa orders in a row for the same
# student, then look at the order history and the wallet.

API=http://localhost:8080

for i in 1 2 3 4; do
  echo "--- order $i ---"
  curl -s "$API/api/orders" \
    -H 'Content-Type: application/json' \
    -d '{"studentId": 3, "pickupBlock": "Block C", "totalRupees": 50,
         "items": [ { "dishId": 2, "quantity": 1 } ]}'
  echo
done

echo
echo "The 4th order was rejected. Was it also... created?"
echo "curl -s '$API/api/orders?studentId=3'   # count them"
echo "curl -s '$API/api/wallet/3'             # count the rupees"
