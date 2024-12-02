package petstore.petstore.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import petstore.petstore.animalCategories.AnimalCategoryDto;
import petstore.petstore.animalCategories.NewAnimalCategoryDto;
import petstore.petstore.animalCategories.PatchAnimalCategoryDto;
import petstore.petstore.entities.AnimalCategory;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AnimalCategoryMapper
{
  AnimalCategoryDto toAnimalCategoryDto(AnimalCategory animalCategory);

  AnimalCategory toAnimalCategory(NewAnimalCategoryDto newAnimalCategoryDto);

  List<AnimalCategoryDto> toAnimalCategoryDtoList(List<AnimalCategory> animalCategories);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void update(@MappingTarget AnimalCategory animalCategory, PatchAnimalCategoryDto patchAnimalCategoryDto);
}