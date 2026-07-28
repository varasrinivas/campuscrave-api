-- Lab mode seed: Anna Madam keeps the counter open late during exam week.
-- Loaded on top of data.sql by hints/lab-mode.sh — see that file for why.
merge into canteen_config (id, order_cutoff, rush_start, rush_end, max_active_orders, accepting_orders)
    key (id) values (1, '23:59:00', '12:15:00', '13:45:00', 3, true);
