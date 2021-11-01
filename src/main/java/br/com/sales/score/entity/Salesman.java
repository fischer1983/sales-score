package br.com.sales.score.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "registry", name = "salesman_registry_uk"))
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Salesman {

    @Id
    @GeneratedValue
    private Long id;

    @Setter
    private String name;

    @Setter
    private String registry;
}
