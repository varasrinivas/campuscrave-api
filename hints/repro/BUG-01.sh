#!/usr/bin/env bash
# BUG-01 — the Biryani Bug.
# Three orders hit the same dish in the same second. Wednesday special,
# Rs.90, stock 3. Watch the API logs while this runs.
#
# Usage: ./BUG-01.sh   (API must be running on localhost:8080)

API=http://localhost:8080/api/orders

order() {
  curl -s -o /dev/null -w "student $1 -> HTTP %{http_code}\n" "$API" \
    -H 'Content-Type: application/json' \
    -d '{
      "studentId": '"$1"',
      "pickupBlock": "Block C",
      "totalRupees": 90,
      "items": [ { "dishId": 1, "quantity": 1 } ]
    }'
}

echo "12:31 PM. Three hungry students. Three biryanis. Stock: 3."
echo "All three orders leave at the same moment..."
echo

# The & is the whole experiment: all three requests are in flight at once.
order 1 &
order 2 &
order 3 &
wait

echo
echo "Now check the shelf:"
curl -s http://localhost:8080/api/menu/1 | grep -o '"stock":[^,]*'
echo "Run me a few times in a row. When the number above goes negative,"
echo "place one more order for dish 1 and watch what the API returns."
