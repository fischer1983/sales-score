package br.com.sales.score.service;

import br.com.sales.score.dto.SalesmanDto;
import br.com.sales.score.entity.Salesman;
import br.com.sales.score.exception.ResourceAlreadyExistsException;
import br.com.sales.score.exception.ResourceNotFoundException;
import br.com.sales.score.repository.SalesmanRepository;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequestScoped
public class SalesmanService {

    public static final String SALESMAN_NOT_FOUND = "Salesman not found";
    public static final String SALESMAN_ALREADY_EXISTS = "Salesman already exists";

    @Inject
    private SalesmanRepository salesmanRepository;

    @Transactional
    public SalesmanDto create(SalesmanDto salesmanDto) {

        verifySalesmanRegistryAlreadyExists(salesmanDto);

        salesmanRepository.persist(Salesman.builder().registry(salesmanDto.getRegistry()).name(salesmanDto.getName()).build());

        return toDto(salesmanRepository.findByRegistry(salesmanDto.getRegistry()));
    }

    @Transactional
    public void update(Long id, SalesmanDto salesmanDto) {

        var count = salesmanRepository.count("registry = ?1 and id != ?2", salesmanDto.getRegistry(), id);
        if (count > 0) {
            throw new ResourceAlreadyExistsException(SALESMAN_ALREADY_EXISTS + " registry: " + salesmanDto.getRegistry());
        }

        var salesman = salesmanRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException(SALESMAN_NOT_FOUND + " id: " + id));

        salesman.setRegistry(salesmanDto.getRegistry());
        salesman.setName(salesmanDto.getName());

        salesmanRepository.persist(salesman);
    }

    @Transactional
    public SalesmanDto findByRegistry(String registry) {
        var salesman = Optional.ofNullable(salesmanRepository.findByRegistry(registry))
                .orElseThrow(() -> new ResourceNotFoundException(SALESMAN_NOT_FOUND + " registry: " + registry));

        return toDto(salesman);
    }

    @Transactional
    public void delete(Long id) {
        var isDeleted = salesmanRepository.deleteById(id);

        if (!isDeleted) {
            throw new ResourceNotFoundException(SALESMAN_NOT_FOUND + id);
        }
    }

    public Optional<Salesman> findById(Long id) {
        return salesmanRepository.findByIdOptional(id);
    }

    public List<SalesmanDto> findAll() {
        return salesmanRepository.findAll().stream()
                .map(salesman -> toDto(salesman))
                .collect(Collectors.toList());
    }

    private void verifySalesmanRegistryAlreadyExists(SalesmanDto salesmanDto) {
        var count = salesmanRepository.count("registry", salesmanDto.getRegistry());
        if (count > 0) {
            throw new ResourceAlreadyExistsException(SALESMAN_ALREADY_EXISTS + " registry: " + salesmanDto.getRegistry());
        }
    }

    private SalesmanDto toDto(Salesman salesman) {
        return SalesmanDto.builder()
                .id(salesman.getId())
                .name(salesman.getName())
                .registry(salesman.getRegistry())
                .build();
    }

}
