package br.com.sales.score.service;

import br.com.sales.score.dto.ProductDto;
import br.com.sales.score.dto.SalesmanDto;
import br.com.sales.score.entity.Product;
import br.com.sales.score.entity.Salesman;
import br.com.sales.score.exception.ResourceAlreadyExistsException;
import br.com.sales.score.exception.ResourceNotFoundException;
import br.com.sales.score.repository.ProductRepository;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@RequestScoped
public class ProductService {

    public static final String PRODUCT_NOT_FOUND = "Product not found";
    public static final String PRODUCT_ALREADY_EXISTS = "Product already exists";

    @Inject
    private ProductRepository productRepository;

    @Transactional
    public ProductDto create(ProductDto productDto) {

        verifyProductSkuAlreadyExists(productDto);

        productRepository.persist(Product.builder()
                .sku(productDto.getSku())
                .name(productDto.getName())
                .price(productDto.getPrice().setScale(2)).build());

        return toDto(productRepository.findBySku(productDto.getSku()));
    }

    public List<Product> findProductsById(List<Long> ids) {
        return productRepository.list("id in (?1)", ids);
    }

    private ProductDto toDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .sku(product.getSku())
                .price(product.getPrice())
                .build();
    }

    private void verifyProductSkuAlreadyExists(ProductDto productDto) {
        var count = productRepository.count("sku", productDto.getSku());
        if (count > 0) {
            throw new ResourceAlreadyExistsException(PRODUCT_ALREADY_EXISTS + " sku: " + productDto.getSku());
        }
    }

    @Transactional
    public void update(Long id, ProductDto productDto) {
        var count = productRepository.count("sku = ?1 and id != ?2", productDto.getSku(), id);
        if (count > 0) {
            throw new ResourceAlreadyExistsException(PRODUCT_ALREADY_EXISTS + " sku: " + productDto.getSku());
        }

        var product = productRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException(PRODUCT_NOT_FOUND + " id: " + id));

        product.setSku(productDto.getSku());
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());

        productRepository.persist(product);
    }

    @Transactional
    public void delete(Long id) {
        var isDeleted = productRepository.deleteById(id);

        if (!isDeleted) {
            throw new ResourceNotFoundException(PRODUCT_NOT_FOUND + id);
        }
    }

}
