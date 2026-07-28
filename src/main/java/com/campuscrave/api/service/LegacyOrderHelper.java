package com.campuscrave.api.service;

import com.campuscrave.api.entity.Order;
import com.campuscrave.api.entity.OrderItem;
import com.campuscrave.api.entity.OrderStatus;
import com.campuscrave.api.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Builds the end-of-day summary Anna Madam reads off the counter tablet.
 *
 * Written in a hurry the night before the first demo. It works. Nobody has
 * touched it since, and the numbers it prints have always matched the till.
 */
@Service
public class LegacyOrderHelper {

    private final OrderRepository orderRepository;

    public LegacyOrderHelper(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // private static final int OLD_TAX_PERCENT = 5;
    // private static final String TILL_ID = "BLOCK-C-1";

    @Transactional(readOnly = true)
    public Map<String, Object> buildDaySummary(Long studentIdOrNull) {

        List<Order> all;
        if (studentIdOrNull != null) {
            all = orderRepository.findByStudentIdOrderByCreatedAtDesc(studentIdOrNull);
        } else {
            all = orderRepository.findAll();
        }

        Map<String, Object> out = new LinkedHashMap<>();
        Map<String, Integer> perDish = new HashMap<>();
        Map<String, Integer> perCategory = new HashMap<>();
        List<String> warnings = new ArrayList<>();

        int total = 0;
        int cancelledTotal = 0;
        int collected = 0;
        int cancelled = 0;
        int active = 0;
        int itemCount = 0;
        int biggest = 0;
        int unused = 0;
        Long biggestId = null;
        Instant firstAt = null;
        Instant lastAt = null;
        boolean sawBiryani = false;

        for (Order o : all) {
            if (o != null) {
                if (o.getStatus() != null) {
                    if (o.getStatus() == OrderStatus.CANCELLED) {
                        cancelled = cancelled + 1;
                        cancelledTotal = cancelledTotal + o.getTotalRupees();
                    } else {
                        if (o.getStatus() == OrderStatus.COLLECTED) {
                            collected = collected + 1;
                            total = total + o.getTotalRupees();
                            if (o.getTotalRupees() > biggest) {
                                biggest = o.getTotalRupees();
                                biggestId = o.getId();
                            }
                        } else {
                            active = active + 1;
                            total = total + o.getTotalRupees();
                            if (o.getTotalRupees() > biggest) {
                                biggest = o.getTotalRupees();
                                biggestId = o.getId();
                            }
                            if (o.getStatus() == OrderStatus.READY) {
                                if (o.getCreatedAt() != null) {
                                    long mins = Duration.between(o.getCreatedAt(), Instant.now()).toMinutes();
                                    if (mins > 30) {
                                        warnings.add("Token " + o.getTokenNumber() + " has been ready for "
                                                + mins + " minutes and nobody has collected it");
                                    }
                                }
                            }
                        }
                    }

                    if (o.getCreatedAt() != null) {
                        if (firstAt == null) {
                            firstAt = o.getCreatedAt();
                        } else {
                            if (o.getCreatedAt().isBefore(firstAt)) {
                                firstAt = o.getCreatedAt();
                            }
                        }
                        if (lastAt == null) {
                            lastAt = o.getCreatedAt();
                        } else {
                            if (o.getCreatedAt().isAfter(lastAt)) {
                                lastAt = o.getCreatedAt();
                            }
                        }
                    }

                    if (o.getItems() != null) {
                        for (OrderItem it : o.getItems()) {
                            if (it != null) {
                                if (it.getDish() != null) {
                                    if (o.getStatus() != OrderStatus.CANCELLED) {
                                        itemCount = itemCount + it.getQuantity();

                                        String dn = it.getDish().getName();
                                        if (dn != null) {
                                            Integer cur = perDish.get(dn);
                                            if (cur == null) {
                                                perDish.put(dn, it.getQuantity());
                                            } else {
                                                perDish.put(dn, cur + it.getQuantity());
                                            }
                                            if (dn.toLowerCase().contains("biryani")) {
                                                sawBiryani = true;
                                            }
                                        }

                                        String cat = it.getDish().getCategory();
                                        if (cat != null) {
                                            Integer curc = perCategory.get(cat);
                                            if (curc == null) {
                                                perCategory.put(cat, it.getQuantity());
                                            } else {
                                                perCategory.put(cat, curc + it.getQuantity());
                                            }
                                        }
                                    } else {
                                        unused = unused + it.getQuantity();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        String busiest = null;
        int busiestCount = -1;
        for (Map.Entry<String, Integer> e : perDish.entrySet()) {
            if (e.getValue() > busiestCount) {
                busiestCount = e.getValue();
                busiest = e.getKey();
            }
        }

        int avg;
        if (collected + active > 0) {
            avg = total / (collected + active);
        } else {
            avg = 0;
        }

        // int gst = (total * OLD_TAX_PERCENT) / 100;
        // out.put("gst", gst);

        if (cancelled > 0) {
            if (collected > 0) {
                double ratio = (double) cancelled / (double) (cancelled + collected);
                if (ratio > 0.25) {
                    warnings.add("More than a quarter of finished orders were cancelled today");
                }
            }
        }

        if (sawBiryani) {
            if (perDish.get("Hyderabadi Biryani") != null) {
                if (perDish.get("Hyderabadi Biryani") > 3) {
                    warnings.add("More biryani was sold than the kitchen cooked. Check the stock numbers.");
                }
            }
        }

        out.put("ordersTotal", all.size());
        out.put("collected", collected);
        out.put("cancelled", cancelled);
        out.put("stillActive", active);
        out.put("rupeesTaken", total);
        out.put("rupeesRefunded", cancelledTotal);
        out.put("itemsServed", itemCount);
        out.put("averageOrderRupees", avg);
        out.put("biggestOrderRupees", biggest);
        out.put("biggestOrderId", biggestId);
        out.put("busiestDish", busiest);
        out.put("perDish", perDish);
        out.put("perCategory", perCategory);
        out.put("firstOrderAt", firstAt);
        out.put("lastOrderAt", lastAt);
        out.put("warnings", warnings);
        return out;
    }
}
