package petstore.petstore.services;

import petstore.petstore.animalCategories.AnimalCategoryDto;
import petstore.petstore.animalCategories.NewAnimalCategoryDto;
import petstore.petstore.animalCategories.PatchAnimalCategoryDto;
import petstore.petstore.entities.AnimalCategory;
import petstore.petstore.exceptions.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import petstore.petstore.mappers.AnimalCategoryMapper;
import petstore.petstore.repositories.AnimalCategoryRepository;

@RequiredArgsConstructor
@Service
public class AnimalCategoryService
{
  private final AnimalCategoryRepository animalCategoryRepository;
  private final AnimalCategoryMapper animalCategoryMapper;

  public AnimalCategoryDto findById(Long id)
  {
    AnimalCategory animalCategory = animalCategoryRepository.findById(id)
        .orElseThrow(() -> new AppException("Nieznana kategoria", HttpStatus.NOT_FOUND));

    return animalCategoryMapper.toAnimalCategoryDto(animalCategory);
  }

  public Page<AnimalCategory> findAll(Pageable pageable)
  {
    Page<AnimalCategory> categories = animalCategoryRepository.findAll(pageable);
    return categories;
  }

  public AnimalCategoryDto save(NewAnimalCategoryDto newAnimalCategoryDto)
  {
    AnimalCategory animalCategory = animalCategoryMapper.toAnimalCategory(newAnimalCategoryDto);
    AnimalCategory savedCategory = animalCategoryRepository.save(animalCategory);

    return animalCategoryMapper.toAnimalCategoryDto(savedCategory);
  }

  public AnimalCategoryDto update(PatchAnimalCategoryDto patchAnimalCategoryDto, Long id)
  {
    AnimalCategory animalCategory = animalCategoryRepository.findById(id)
        .orElseThrow(() -> new AppException("Nieznana kategoria", HttpStatus.NOT_FOUND));

    animalCategoryMapper.update(animalCategory, patchAnimalCategoryDto);
    AnimalCategory updatedCategory = animalCategoryRepository.save(animalCategory);

    return animalCategoryMapper.toAnimalCategoryDto(updatedCategory);
  }

  public void delete(Long id)
  {
    AnimalCategory animalCategory = animalCategoryRepository.findById(id)
        .orElseThrow(() -> new AppException("Nieznana kategoria", HttpStatus.NOT_FOUND));

    animalCategoryRepository.delete(animalCategory);
  }
}
