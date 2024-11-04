package petstore.petstore.services;

import com.google.common.base.Joiner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;
import petstore.petstore.dtos.products.NewProductDto;
import petstore.petstore.dtos.products.PatchProductDto;
import petstore.petstore.dtos.products.ProductDto;
import petstore.petstore.entities.Product;
import petstore.petstore.enums.SearchOperation;
import petstore.petstore.exceptions.AppException;
import petstore.petstore.mappers.ProductMapper;
import petstore.petstore.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import petstore.petstore.specifications.ProductSpecificationsBuilder;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    ProductSpecificationsBuilder builder = new ProductSpecificationsBuilder();

    String operationSetExper = Joiner.on("|").join(SearchOperation.SIMPLE_OPERATION_SET);
    Pattern pattern = Pattern.compile(
        "(\\w+?)(" + operationSetExper + ")(\\p{Punct}?)(\\w+?)(\\p{Punct}?),"
    );

    if (search != null)
    {
      Matcher matcher = pattern.matcher(search + ",");

      while (matcher.find())
      {
        builder.with(
            matcher.group(1),
            matcher.group(2),
            matcher.group(4),
            matcher.group(3),
            matcher.group(5));
      }
    }

    Specification<Product> productSpecification = builder.build();
    Page<Product> products = productRepository.findAll(productSpecification, pageable);

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
