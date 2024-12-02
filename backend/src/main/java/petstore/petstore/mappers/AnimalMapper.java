package petstore.petstore.mappers;

import org.mapstruct.*;
import petstore.petstore.dtos.animals.AnimalDto;
import petstore.petstore.dtos.animals.NewAnimalDto;
import petstore.petstore.dtos.animals.PatchAnimalDto;
import petstore.petstore.entities.Animal;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AnimalMapper
{
  AnimalDto toAnimalDto(Animal animal);
  Animal newAnimalDtoToAnimal(NewAnimalDto newAnimalDto);
  List<AnimalDto> toAnimalDtoList(List<Animal> animals);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void update(@MappingTarget Animal animal, PatchAnimalDto patchAnimalDto);
}
