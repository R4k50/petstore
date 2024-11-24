package petstore.petstore.loaders;

import petstore.petstore.dtos.products.NewProductDto;
import petstore.petstore.entities.Product;
import petstore.petstore.entities.ProductCategory;
import petstore.petstore.mappers.ProductMapper;
import petstore.petstore.repositories.ProductCategoryRepository;
import petstore.petstore.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component
@RequiredArgsConstructor
public class PopulateProductDataLoader implements ApplicationRunner
{
  private final ProductRepository productRepository;
  private final ProductMapper productMapper;
  private final ProductCategoryRepository productCategoryRepository;

  public void run(ApplicationArguments args)
  {
    populateProductCategories();
    populateProducts();
  }

  private void populateProducts()
  {
    ProductCategory food = productCategoryRepository.getByName("food");
    ProductCategory dogs = productCategoryRepository.getByName("dogs");
    ProductCategory cats = productCategoryRepository.getByName("cats");

    for (int i = 0; i < 100; i++)
    {
      Set<ProductCategory> productCategories = new HashSet<>();

      if (i % 2 == 0) {
        productCategories.add(dogs);
      }
      else {
        productCategories.add(cats);
      }

      if (i % 3 == 0) {
        productCategories.add(food);
      }

      createProduct(NewProductDto.builder()
          .name("Pet food " + (i + 1))
          .description("My son loves it!")
          .price(new BigDecimal("9.99"))
          .quantity(i)
          .categories(productCategories)
          .img("karma1.jpg")
          .build(),
          productCategories
      );
    }
  }

  private void createProduct(NewProductDto newProductDto, Set<ProductCategory> productCategories)
  {
    Optional<Product> existingProduct = productRepository.findByName(newProductDto.getName());

    if (existingProduct.isEmpty())
    {
      Product product = productMapper.newProductDtoToProduct(newProductDto);
      product.setCategories(productCategories);

      productRepository.save(product);
    }
  }

  private void populateProductCategories()
  {
    createProductCategory("toys");
    createProductCategory("accessories");
    createProductCategory("grooming");
    createProductCategory("food");
    createProductCategory("dogs");
    createProductCategory("cats");
    createProductCategory("birds");
    createProductCategory("rodents");
  }

  private void createProductCategory(String name)
  {
    Optional<ProductCategory> existingProductCategory = productCategoryRepository.findByName(name);

    if (existingProductCategory.isEmpty())
    {
      productCategoryRepository.save(ProductCategory.builder().name(name).build());
    }
  }
}