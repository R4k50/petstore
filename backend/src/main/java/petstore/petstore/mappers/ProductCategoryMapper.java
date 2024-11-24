package petstore.petstore.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import petstore.petstore.dtos.productCategories.NewProductCategoryDto;
import petstore.petstore.dtos.productCategories.PatchProductCategoryDto;
import petstore.petstore.dtos.productCategories.ProductCategoryDto;
import petstore.petstore.entities.ProductCategory;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductCategoryMapper
{
  ProductCategoryDto toProductCategoryDto(ProductCategory productCategory);

  ProductCategory toProductCategory(NewProductCategoryDto newProductCategoryDto);

  List<ProductCategoryDto> toProductCategoryDtoList(List<ProductCategory> productCategories);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void update(@MappingTarget ProductCategory productCategory, PatchProductCategoryDto patchProductCategoryDto);
}