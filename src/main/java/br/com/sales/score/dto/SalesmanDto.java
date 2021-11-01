package br.com.sales.score.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesmanDto {

    private Long id;

    @NotBlank
    private String registry;

    @NotBlank
    private String name;

    public String getRegistry() {
        return registry;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRegistry(String registry) {
        this.registry = registry;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
