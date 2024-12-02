package petstore.petstore.services;

import com.google.common.base.Joiner;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;
import petstore.petstore.dtos.animals.NewAnimalDto;
import petstore.petstore.dtos.animals.PatchAnimalDto;
import petstore.petstore.dtos.animals.AnimalDto;
import petstore.petstore.entities.Animal;
import petstore.petstore.entities.AnimalCategory;
import petstore.petstore.entities.Sector;
import petstore.petstore.enums.SearchOperation;
import petstore.petstore.exceptions.AppException;
import petstore.petstore.mappers.AnimalMapper;
import petstore.petstore.repositories.AnimalCategoryRepository;
import petstore.petstore.repositories.AnimalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import petstore.petstore.repositories.SectorRepository;
import petstore.petstore.specifications.AnimalSpecificationsBuilder;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class AnimalService
{
  private final AnimalRepository animalRepository;
  private final AnimalCategoryRepository animalCategoryRepository;
  private final SectorRepository sectorRepository;
  private final AnimalMapper animalMapper;

  public AnimalDto findById(Long id)
  {
    Animal animal = animalRepository.findById(id)
        .orElseThrow(() -> new AppException("Nieznane zwierzę", HttpStatus.NOT_FOUND));

    return animalMapper.toAnimalDto(animal);
  }

  public Page<Animal> findAll(Pageable pageable)
  {
    return animalRepository.findAll(pageable);
  }

  public Page<Animal> findAll(Pageable pageable, String search)
  {
    AnimalSpecificationsBuilder builder = new AnimalSpecificationsBuilder();

    String operationSetExper = Joiner.on("|").join(SearchOperation.SIMPLE_OPERATION_SET);
    Pattern pattern = Pattern.compile(
        "(\\w+?)(" + operationSetExper + ")(\\p{Punct}?)(\\w+?)(\\p{Punct}?),"
    );

    if (search != null)
    {
      Matcher matcher = pattern.matcher(search + ",");

      while (matcher.find())
      {
        builder.with(
            matcher.group(1),
            matcher.group(2),
            matcher.group(4),
            matcher.group(3),
            matcher.group(5)
        );
      }
    }

    Specification<Animal> animalSpecification = builder.build();
    return animalRepository.findAll(animalSpecification, pageable);
  }

  public AnimalDto save(NewAnimalDto newAnimalDto) throws IOException
  {
    Animal animal = animalMapper.newAnimalDtoToAnimal(newAnimalDto);

    Set<AnimalCategory> categories = new HashSet<>();
    for (AnimalCategory category : newAnimalDto.getCategories())
    {
      AnimalCategory fetchedCategory = animalCategoryRepository.findById(category.getId())
          .orElseThrow(() -> new AppException("Kategoria nie istnieje", HttpStatus.NOT_FOUND));
      categories.add(fetchedCategory);
    }
    animal.setCategories(categories);

    Sector sector = sectorRepository.findById(newAnimalDto.getSector().getId())
        .orElseThrow(() -> new AppException("Sekcja nie istnieje", HttpStatus.NOT_FOUND));
    animal.setSector(sector);

    animal.setImg(newAnimalDto.getImg());

    Animal savedAnimal = animalRepository.save(animal);

    return animalMapper.toAnimalDto(savedAnimal);
  }

  public AnimalDto update(PatchAnimalDto patchAnimalDto, Long id)
  {
    Animal animal = animalRepository.findById(id)
        .orElseThrow(() -> new AppException("Nieznane zwierzę", HttpStatus.NOT_FOUND));

    animalMapper.update(animal, patchAnimalDto);
    Animal updatedAnimal = animalRepository.save(animal);

    return animalMapper.toAnimalDto(updatedAnimal);
  }

  public AnimalDto update(PatchAnimalDto patchAnimalDto, String img, Long id)
  {
    Animal animal = animalRepository.findById(id)
        .orElseThrow(() -> new AppException("Nieznane zwierzę", HttpStatus.NOT_FOUND));

    animalMapper.update(animal, patchAnimalDto);
    animal.setImg(img);

    Animal updatedAnimal = animalRepository.save(animal);

    return animalMapper.toAnimalDto(updatedAnimal);
  }

  public Animal delete(Long id)
  {
    Animal animal = animalRepository.findById(id)
        .orElseThrow(() -> new AppException("Nieznane zwierzę", HttpStatus.NOT_FOUND));

    try {
      animalRepository.delete(animal);
    }
    catch (DataIntegrityViolationException ex)
    {
      throw new AppException("Nie można usunąć zwierzęcia", HttpStatus.BAD_REQUEST);
    }

    return animal;
  }
}