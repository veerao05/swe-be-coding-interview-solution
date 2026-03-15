package com.gyg.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import java.util.Optional;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "activity")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private int price;
    private String currency;
    private double rating;
    private boolean specialOffer;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @ToString.Exclude
    @NotFound(action = NotFoundAction.IGNORE)
    private Supplier supplier;

    // 🚨 Issues in Activity Entity:

    // 1️⃣ @EqualsAndHashCode on entity with all fields
    //    - Risk: triggers lazy loading, infinite recursion, wrong Set behavior
    //    - Suggestion: use @EqualsAndHashCode(onlyExplicitlyIncluded = true) with id only

    // 2️⃣ @ToString on entity
    //    - Risk: recursion if relationships grow
    //    - Suggestion: avoid @ToString or carefully exclude relationships

    // 3️⃣ GenerationType.AUTO
    //    - Risk: inconsistent ID generation across DBs
    //    - Suggestion: prefer IDENTITY or SEQUENCE explicitly

    // 4️⃣ @NotFound(action = IGNORE)
    //    - Risk: hides broken foreign keys
    //    - Suggestion: enforce DB referential integrity

    // 5️⃣ Missing @JoinColumn
    //    - Risk: auto column generation, less readable, no control over nullability
    //    - Suggestion: define @JoinColumn(name="supplier_id", nullable=false)

    // 6️⃣ Primitive types (int, double, boolean)
    //    - Risk: cannot store null from DB
    //    - Suggestion: use Integer, Double, Boolean wrapper types

    // 7️⃣ No validation annotations
    //    - Risk: title null, price negative, rating out of range
    //    - Suggestion: use @NotNull, @Size, @Min, @Max etc.

    // 8️⃣ No business constraints
    //    - Risk: invalid rating or price allowed
    //    - Suggestion: enforce rating 0–5, price positive

    // 9️⃣ Missing indexes
    //    - Risk: queries on rating/price slow
    //    - Suggestion: add @Table(indexes = {@Index(name="idx_activity_rating", columnList="rating")})

    public String getSupplierName() {
        return Optional.ofNullable(supplier).map(Supplier::getName).orElse("");
    }
}

