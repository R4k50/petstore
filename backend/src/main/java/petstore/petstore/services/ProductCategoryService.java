package petstore.petstore.services;

import petstore.petstore.dtos.productCategories.NewProductCategoryDto;
import petstore.petstore.dtos.productCategories.PatchProductCategoryDto;
import petstore.petstore.dtos.productCategories.ProductCategoryDto;
import petstore.petstore.entities.ProductCategory;
import petstore.petstore.exceptions.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import petstore.petstore.mappers.ProductCategoryMapper;
import petstore.petstore.repositories.ProductCategoryRepository;

@RequiredArgsConstructor
@Service
public class ProductCategoryService
{
  private final ProductCategoryRepository productCategoryRepository;
  private final ProductCategoryMapper productCategoryMapper;

  public ProductCategoryDto findById(Long id)
  {
    ProductCategory productCategory = productCategoryRepository.findById(id)
        .orElseThrow(() -> new AppException("Unknown category", HttpStatus.NOT_FOUND));

    return productCategoryMapper.toProductCategoryDto(productCategory);
  }

  public Page<ProductCategory> findAll(Pageable pageable)
  {
    Page<ProductCategory> categories = productCategoryRepository.findAll(pageable);
    return categories;
  }

  public ProductCategoryDto save(NewProductCategoryDto newProductCategoryDto)
  {
    ProductCategory productCategory = productCategoryMapper.toProductCategory(newProductCategoryDto);
    ProductCategory savedCategory = productCategoryRepository.save(productCategory);

    return productCategoryMapper.toProductCategoryDto(savedCategory);
  }

  public ProductCategoryDto update(PatchProductCategoryDto patchProductCategoryDto, Long id)
  {
    ProductCategory productCategory = productCategoryRepository.findById(id)
        .orElseThrow(() -> new AppException("Unknown category", HttpStatus.NOT_FOUND));

    productCategoryMapper.update(productCategory, patchProductCategoryDto);
    ProductCategory updatedCategory = productCategoryRepository.save(productCategory);

    return productCategoryMapper.toProductCategoryDto(updatedCategory);
  }

  public void delete(Long id)
  {
    ProductCategory productCategory = productCategoryRepository.findById(id)
        .orElseThrow(() -> new AppException("Unknown category", HttpStatus.NOT_FOUND));

    productCategoryRepository.delete(productCategory);
  }
}