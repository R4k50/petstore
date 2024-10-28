package petstore.petstore.controllers;

import petstore.petstore.dtos.products.ProductDto;
import petstore.petstore.entities.Product;
import petstore.petstore.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Validated
public class ProductController
{
  private final ProductService productService;

  @GetMapping("/product/{id}")
  public ResponseEntity<ProductDto> getOne(@PathVariable Long id)
  {
    ProductDto productDto = productService.findById(id);

    return ResponseEntity.ok(productDto);
  }

  @GetMapping("/products")
  public ResponseEntity<Page<Product>> getAll(Pageable pageable, @RequestParam(required = false) String search)
  {
    if (search == null || search.split(",").length != 2)
    {
      Page<Product> products = productService.findAll(pageable);
      return ResponseEntity.ok(products);
    }

    Page<Product> products = productService.findAll(pageable, search);
    return ResponseEntity.ok(products);
  }
}
