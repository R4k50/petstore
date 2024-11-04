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
    for (int i = 0; i < 100; i++)
    {
      createProduct(NewProductDto.builder()
          .name("Pet food " + (i + 1))
          .description("My son loves it!")
          .price(new BigDecimal("9.99"))
          .quantity(i)
          .img("karma1.jpg")
          .build());
    }
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