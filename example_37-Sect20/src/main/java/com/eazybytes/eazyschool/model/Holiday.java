package com.eazybytes.eazyschool.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name="holidays")
public class Holiday extends BaseEntity {

    @Id
    private String day;

    private String reason;

    @Enumerated(EnumType.STRING)
    private Type type;

    public Holiday(String day, String reason, Type type) {
    }

    public enum Type {
        FESTIVAL, FEDERAL
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Holiday holiday = (Holiday) o;
        return day != null && Objects.equals(day, holiday.day);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
