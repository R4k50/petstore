package petstore.petstore.services;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import petstore.petstore.dtos.products.NewProductDto;
import petstore.petstore.dtos.products.PatchProductDto;
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

import java.io.IOException;

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

  public Page<Product> findAll(Example productExample, Pageable pageable)
  {
    Page<Product> products = productRepository.findAll(productExample, pageable);

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

  public ProductDto save(NewProductDto newProductDto) throws IOException
  {
    Product product = productMapper.newProductDtoToProduct(newProductDto);
    Product savedProduct = productRepository.save(product);

    return productMapper.toProductDto(savedProduct);
  }

  public ProductDto update(PatchProductDto patchProductDto, Long id)
  {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new AppException("Unknown product", HttpStatus.NOT_FOUND));

    productMapper.update(product, patchProductDto);
    Product updatedProduct = productRepository.save(product);

    return productMapper.toProductDto(updatedProduct);
  }

  public ProductDto update(PatchProductDto patchProductDto, String img, Long id)
  {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new AppException("Unknown product", HttpStatus.NOT_FOUND));

    productMapper.update(product, patchProductDto);
    product.setImg(img);

    Product updatedProduct = productRepository.save(product);

    return productMapper.toProductDto(updatedProduct);
  }

  public Product delete(Long id)
  {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new AppException("Unknown product", HttpStatus.NOT_FOUND));

    try {
      productRepository.delete(product);
    }
    catch (DataIntegrityViolationException ex)
    {
      throw new AppException("Cannot delete the product, this product is ordered", HttpStatus.BAD_REQUEST);
    }

    return product;
  }
}
