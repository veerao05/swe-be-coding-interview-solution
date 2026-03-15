package com.gyg.helpers;

import com.gyg.entity.Supplier;

public class SupplierHelper {
    public static Supplier createSupplier(String name) {
        var supplier = new Supplier();
        //supplier.setId(id);
        supplier.setName(name);
        return supplier;
    }
}
