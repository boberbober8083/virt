package ru.denisaql.virtth.persist;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(schema = "public", name = "testtab")
public class TestTab {
    @Id
    @EqualsAndHashCode.Include
    @ToString.Include
    private String id;
    @ToString.Include
    private String name;
    @Column(name = "datecreated")
    private LocalDateTime dateCreated;
    @ToString.Include
    private String enigma;
    @Column(name = "enu")
    private Integer enu;
}
