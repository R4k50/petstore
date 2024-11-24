package petstore.petstore.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import petstore.petstore.dtos.productCategories.NewProductCategoryDto;
import petstore.petstore.dtos.productCategories.PatchProductCategoryDto;
import petstore.petstore.dtos.productCategories.ProductCategoryDto;
import petstore.petstore.entities.ProductCategory;
import petstore.petstore.services.ProductCategoryService;

@RequiredArgsConstructor
@RestController
@Validated
public class ProductCategoryController
{
  private final ProductCategoryService productCategoryService;

  @GetMapping("/product-category/{id}")
  public ResponseEntity<ProductCategoryDto> getOne(@PathVariable Long id)
  {
    ProductCategoryDto productCategoryDto = productCategoryService.findById(id);
    return ResponseEntity.ok(productCategoryDto);
  }

  @GetMapping("/product-categories")
  public ResponseEntity<Page<ProductCategory>> getAll(Pageable pageable, @RequestParam(required = false) String search)
  {
    Page<ProductCategory> categories = productCategoryService.findAll(pageable);
    return ResponseEntity.ok(categories);
  }

  @PostMapping("/product-category")
  public ResponseEntity<ProductCategoryDto> add(@RequestBody @Valid NewProductCategoryDto newProductCategoryDto)
  {
    ProductCategoryDto productCategoryDto = productCategoryService.save(newProductCategoryDto);
    return ResponseEntity.ok(productCategoryDto);
  }

  @PatchMapping("/product-category/{id}")
  public ResponseEntity<ProductCategoryDto> update(
      @RequestBody @Valid PatchProductCategoryDto patchProductCategoryDto,
      @PathVariable Long id
  ) {
    ProductCategoryDto updatedProductCategoryDto = productCategoryService.update(patchProductCategoryDto, id);
    return ResponseEntity.ok(updatedProductCategoryDto);
  }

  @DeleteMapping("/product-category/{id}")
  public void delete(@PathVariable Long id)
  {
    productCategoryService.delete(id);
  }
}