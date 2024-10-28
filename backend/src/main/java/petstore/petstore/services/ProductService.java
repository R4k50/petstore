package petstore.petstore.services;

import petstore.petstore.dtos.products.ProductDto;
import petstore.petstore.entities.Product;
import petstore.petstore.exceptions.AppException;
import petstore.petstore.mappers.ProductMapper;
import petstore.petstore.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductService
{
  private final ProductRepository productRepository;
  private final ProductMapper productMapper;

  public ProductDto findById(Long id)
  {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new AppException("Unknown product", HttpStatus.NOT_FOUND));

    return productMapper.toProductDto(product);
  }

  public Page<Product> findAll(Pageable pageable)
  {
    Page<Product> products = productRepository.findAll(pageable);

    return products;
  }

  public Page<Product> findAll(Pageable pageable, String search)
  {
    String[] searchArray = search.split(",");

    String searchParam = searchArray[0];
    String searchValue = searchArray[1];

    Page<Product> products = productRepository.findAll(searchParam, searchValue, pageable);

    return products;
  }
}
