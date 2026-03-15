package com.gyg.helpers;

import com.gyg.entity.Activity;
import com.gyg.entity.Supplier;

public class ActivityHelper {
    public static Activity createActivity(
            String title,
            int price,
            double rating,
            boolean specialOffer,
            Supplier supplier
    ) {
        var activity = new Activity();
        activity.setTitle(title);
        activity.setPrice(price);
        activity.setCurrency("$");
        activity.setRating(rating);
        activity.setSpecialOffer(specialOffer);
        activity.setSupplier(supplier);
        return activity;
    }
}
