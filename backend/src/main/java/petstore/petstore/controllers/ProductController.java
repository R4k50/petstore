package petstore.petstore.controllers;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import petstore.petstore.dtos.products.NewProductDto;
import petstore.petstore.dtos.products.PatchProductDto;
import petstore.petstore.dtos.products.ProductDto;
import petstore.petstore.entities.Image;
import petstore.petstore.entities.Product;
import petstore.petstore.services.ImageService;
import petstore.petstore.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@Validated
public class ProductController
{
  private final ProductService productService;
  private final ImageService imageService;

  @GetMapping("/product/{id}")
  public ResponseEntity<ProductDto> getOne(@PathVariable Long id)
  {
    ProductDto productDto = productService.findById(id);

    return ResponseEntity.ok(productDto);
  }

  @GetMapping("/products")
  public ResponseEntity<Page<Product>> getAll(Pageable pageable, @RequestParam(required = false) String search)
  {
    Page<Product> products = (search != null)
        ? productService.findAll(pageable, search)
        : productService.findAll(pageable);

    return ResponseEntity.ok(products);
  }

  @PostMapping("/product")
  public ResponseEntity<ProductDto> add(
      @RequestPart("product") @Valid NewProductDto newProductDto,
      @RequestPart("image") MultipartFile image
  ) throws IOException
  {
    Image savedImage = imageService.upload(image);

    newProductDto.setImg(savedImage.getName());
    ProductDto productDto = productService.save(newProductDto);

    return ResponseEntity.ok(productDto);
  }

  @PatchMapping("/product/{id}")
  public ResponseEntity<ProductDto> update(
      @RequestPart(value="product", required=false) @Valid PatchProductDto patchProductDto,
      @RequestPart(value="image", required=false) MultipartFile patchImage,
      @PathVariable Long id
  ) throws IOException
  {
    if (patchImage == null)
    {
      ProductDto productDto = productService.update(patchProductDto, id);
      return ResponseEntity.ok(productDto);
    }

    ProductDto productDto = productService.findById(id);
    Image updatedImage = imageService.update(productDto.getImg(), patchImage);

    ProductDto patchedProductDto = productService.update(patchProductDto, updatedImage.getName(), id);

    return ResponseEntity.ok(patchedProductDto);
  }

  @DeleteMapping("/product/{id}")
  public void delete(@PathVariable Long id)
  {
    Product product = productService.delete(id);
    imageService.delete(product.getImg());
  }
}
