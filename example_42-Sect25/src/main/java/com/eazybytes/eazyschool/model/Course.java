package com.eazybytes.eazyschool.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class Course extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer courseId;

    private String name;

    private String fees;

    @ManyToMany(mappedBy = "courses", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @ToString.Exclude
    Set<Person> person = new HashSet<>(); // This initialization can be helpfully for the Tests!!!

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Course courses = (Course) o;
        return courseId != null && Objects.equals(courseId, courses.courseId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
