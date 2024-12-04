package petstore.petstore.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;
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
  @PersistenceContext
  EntityManager entityManager;

  public ProductCategoryDto findById(Long id)
  {
    ProductCategory productCategory = productCategoryRepository.findById(id)
        .orElseThrow(() -> new AppException("Nieznana kategoria", HttpStatus.NOT_FOUND));

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
        .orElseThrow(() -> new AppException("Nieznana kategoria", HttpStatus.NOT_FOUND));

    productCategoryMapper.update(productCategory, patchProductCategoryDto);
    ProductCategory updatedCategory = productCategoryRepository.save(productCategory);

    return productCategoryMapper.toProductCategoryDto(updatedCategory);
  }

  @Transactional
  public void delete(Long id)
  {
    ProductCategory productCategory = productCategoryRepository.findById(id)
        .orElseThrow(() -> new AppException("Nieznana kategoria", HttpStatus.NOT_FOUND));

    String deleteJoinTableQuery = "DELETE FROM product_category_map WHERE category_id = ?";
    Query query = entityManager.createNativeQuery(deleteJoinTableQuery);
    query.setParameter(1, id);
    query.executeUpdate();

    try {
      productCategoryRepository.delete(productCategory);
    }
    catch (DataIntegrityViolationException ex)
    {
      throw new AppException("Nie można usunąć kategorii", HttpStatus.BAD_REQUEST);
    }
  }
}