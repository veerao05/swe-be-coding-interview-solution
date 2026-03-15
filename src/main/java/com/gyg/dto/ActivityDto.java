package com.gyg.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActivityDto {
    private Long id;
    private String title;
    private int price;          // ⚠️ Primitive type: cannot represent null. Use Integer if DTO is used for request/partial updates.
    private String currency;    // ⚠️ Using String for currency can lead to invalid codes ("usd", "USD"). Consider java.util.Currency or Enum.
    private double rating;      // ⚠️ Primitive type: cannot represent null. Use Double for nullable fields.
    private boolean specialOffer; // ⚠️ Primitive type: cannot represent null. Use Boolean for nullable fields.
    private String supplierName;  // ⚠️ Flattened supplier info. If later more supplier info is needed, consider SupplierDto.

    // 🚨 Other considerations:

    // 1️⃣ @Data is safe here (unlike entities), generates getters/setters/toString/equals/hashCode
    // 2️⃣ No validation annotations present. If this DTO is used for input:
    //    - @NotNull, @Size(max=255) for title
    //    - @Min(0) for price
    //    - @DecimalMin("0.0") @DecimalMax("5.0") for rating
    // 3️⃣ No @Builder.Default for collections (not applicable now, but remember if adding lists/sets)
    // 4️⃣ DTO design depends on usage:
    //    - Response DTO: current design is fine
    //    - Request DTO: better to use wrapper types and validation annotations
}
