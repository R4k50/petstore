package petstore.petstore.mappers;

import petstore.petstore.dtos.products.NewProductDto;
import petstore.petstore.dtos.products.ProductDto;
import petstore.petstore.entities.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper
{
  ProductDto toProductDto(Product product);
  Product newProductDtoToProduct(NewProductDto newProductDto);
}
