package petstore.petstore.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import petstore.petstore.animalCategories.AnimalCategoryDto;
import petstore.petstore.animalCategories.NewAnimalCategoryDto;
import petstore.petstore.animalCategories.PatchAnimalCategoryDto;
import petstore.petstore.entities.AnimalCategory;
import petstore.petstore.services.AnimalCategoryService;

@RequiredArgsConstructor
@RestController
@Validated
public class AnimalCategoryController
{
  private final AnimalCategoryService animalCategoryService;

  @GetMapping("/animal-category/{id}")
  public ResponseEntity<AnimalCategoryDto> getOne(@PathVariable Long id)
  {
    AnimalCategoryDto animalCategoryDto = animalCategoryService.findById(id);
    return ResponseEntity.ok(animalCategoryDto);
  }

  @GetMapping("/animal-categories")
  public ResponseEntity<Page<AnimalCategory>> getAll(Pageable pageable, @RequestParam(required = false) String search)
  {
    Page<AnimalCategory> categories = animalCategoryService.findAll(pageable);
    return ResponseEntity.ok(categories);
  }

  @PostMapping("/animal-category")
  public ResponseEntity<AnimalCategoryDto> add(@RequestBody @Valid NewAnimalCategoryDto newAnimalCategoryDto)
  {
    AnimalCategoryDto animalCategoryDto = animalCategoryService.save(newAnimalCategoryDto);
    return ResponseEntity.ok(animalCategoryDto);
  }

  @PatchMapping("/animal-category/{id}")
  public ResponseEntity<AnimalCategoryDto> update(
      @RequestBody @Valid PatchAnimalCategoryDto patchAnimalCategoryDto,
      @PathVariable Long id
  ) {
    AnimalCategoryDto updatedAnimalCategoryDto = animalCategoryService.update(patchAnimalCategoryDto, id);
    return ResponseEntity.ok(updatedAnimalCategoryDto);
  }

  @DeleteMapping("/animal-category/{id}")
  public void delete(@PathVariable Long id)
  {
    animalCategoryService.delete(id);
  }
}
