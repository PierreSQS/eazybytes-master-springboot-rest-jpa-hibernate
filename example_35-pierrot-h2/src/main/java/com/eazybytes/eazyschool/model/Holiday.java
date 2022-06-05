package com.eazybytes.eazyschool.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
@Table(name = "holidays")
public class Holiday extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String day;
    private String reason;
    @Enumerated(EnumType.STRING)
    private Type type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public enum Type {
        FESTIVAL, FEDERAL
    }

    public Holiday() {
    }

    public Holiday(String day, String reason, Type type) {
        this.day = day;
        this.reason = reason;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Holiday holiday = (Holiday) o;
        return id != null && Objects.equals(id, holiday.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
