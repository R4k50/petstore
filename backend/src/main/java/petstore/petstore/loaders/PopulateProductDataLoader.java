package petstore.petstore.loaders;

import petstore.petstore.dtos.products.NewProductDto;
import petstore.petstore.entities.Product;
import petstore.petstore.mappers.ProductMapper;
import petstore.petstore.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PopulateProductDataLoader implements ApplicationRunner
{
  private final ProductRepository productRepository;
  private final ProductMapper productMapper;

  public void run(ApplicationArguments args)
  {
    populateProducts();
  }

  private void populateProducts()
  {
    createProduct(NewProductDto.builder()
      .name("Pet food 1")
      .description("My son loves it!")
      .price(new BigDecimal("9.99"))
      .quantity(1)
      .img("karma1.jpg")
      .build());

    createProduct(NewProductDto.builder()
      .name("Pet food 2")
      .description("My son loves it!")
      .price(new BigDecimal("5.99"))
      .quantity(2)
      .img("karma1.jpg")
      .build());

    createProduct(NewProductDto.builder()
      .name("Pet food 3")
      .description("My son loves it!")
      .price(new BigDecimal("2.99"))
      .quantity(3)
      .img("karma1.jpg")
      .build());
  }

  private void createProduct(NewProductDto newProductDto)
  {
    Optional<Product> existingProduct = productRepository.findByName(newProductDto.getName());

    if (!existingProduct.isPresent())
    {
      Product product = productMapper.newProductDtoToProduct(newProductDto);
      productRepository.save(product);
    }
  }
}