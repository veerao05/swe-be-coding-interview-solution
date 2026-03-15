package com.gyg.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name = "supplier")
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String address;
    private String zip;
    private String city;
    private String country;
    @OneToMany(mappedBy = "supplier", fetch = FetchType.LAZY)
    private List<Activity> activities;

    // 🚨 Issues in Supplier Entity:

    // 1️⃣ @Data on entity
    //    - Risk: generates equals(), hashCode(), toString() for all fields including activities
    //    - Suggestion: avoid @Data; use @Getter/@Setter and carefully manage equals/hashCode

    // 2️⃣ Lazy collection and JSON serialization
    //    - Risk: infinite recursion during JSON serialization
    //    - Suggestion: use @JsonManagedReference in Supplier, @JsonBackReference in Activity
    //      OR keep @JsonIgnore

    // 3️⃣ No @JoinColumn needed here since mappedBy is used
    //    - Still, consider proper DB constraints for nullability

    // 4️⃣ No validation annotations
    //    - Risk: name/address/zip/city/country could be null
    //    - Suggestion: use @NotNull, @Size(max=255) etc.

    // 5️⃣ No business constraints
    //    - Risk: zip could be invalid length, name could be empty
    //    - Suggestion: enforce with validation annotations

    // 6️⃣ Missing indexes
    //    - Risk: queries by city/country may be slow
    //    - Suggestion: add @Table(indexes = {@Index(name="idx_supplier_city", columnList="city")})

    // 7️⃣ Collection type
    //    - Risk: List can allow duplicates, potential NPE if not initialized
    //    - Suggestion: initialize collection: private List<Activity> activities = new ArrayList<>();
    //      or use Set<Activity> if uniqueness is important

}

