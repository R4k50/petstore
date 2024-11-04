package petstore.petstore.controllers;

import com.google.common.base.Joiner;
import jakarta.validation.Valid;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import petstore.petstore.dtos.products.NewProductDto;
import petstore.petstore.dtos.products.PatchProductDto;
import petstore.petstore.dtos.products.ProductDto;
import petstore.petstore.entities.Image;
import petstore.petstore.entities.Product;
import petstore.petstore.enums.SearchOperation;
import petstore.petstore.repositories.ProductRepository;
import petstore.petstore.services.ImageService;
import petstore.petstore.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import petstore.petstore.specifications.ProductSpecificationsBuilder;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@RestController
@Validated
public class ProductController
{
  private final ProductService productService;
  private final ProductRepository productRepository;
  private final ImageService imageService;

  @GetMapping("/product/{id}")
  public ResponseEntity<ProductDto> getOne(@PathVariable Long id)
  {
    ProductDto productDto = productService.findById(id);

    return ResponseEntity.ok(productDto);
  }

  @GetMapping("/products")
  public ResponseEntity<Page<Product>> getAll(Pageable pageable, @RequestParam(required = false) String search) {
    ProductSpecificationsBuilder builder = new ProductSpecificationsBuilder();

    String operationSetExper = Joiner.on("|").join(SearchOperation.SIMPLE_OPERATION_SET);
    Pattern pattern = Pattern.compile(
        "(\\w+?)(" + operationSetExper + ")(\\p{Punct}?)(\\w+?)(\\p{Punct}?),"
    );

    if (search != null) {
      Matcher matcher = pattern.matcher(search + ",");
      while (matcher.find()) {
        builder.with(
            matcher.group(1),
            matcher.group(2),
            matcher.group(4),
            matcher.group(3),
            matcher.group(5));
      }
    }

    Specification<Product> spec = builder.build();
    Page<Product> page = productRepository.findAll(spec, pageable);

    return ResponseEntity.ok(page);
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
