package petstore.petstore.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import petstore.petstore.dtos.products.NewProductDto;
import petstore.petstore.dtos.products.PatchProductDto;
import petstore.petstore.dtos.products.ProductDto;
import petstore.petstore.entities.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper
{
  ProductDto toProductDto(Product product);
  Product newProductDtoToProduct(NewProductDto newProductDto);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void update(@MappingTarget Product product, PatchProductDto patchProductDto);
}
