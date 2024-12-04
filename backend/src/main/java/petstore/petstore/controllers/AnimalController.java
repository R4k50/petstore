package petstore.petstore.controllers;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import petstore.petstore.dtos.animals.NewAnimalDto;
import petstore.petstore.dtos.animals.PatchAnimalDto;
import petstore.petstore.dtos.animals.AnimalDto;
import petstore.petstore.entities.Image;
import petstore.petstore.entities.Animal;
import petstore.petstore.services.ImageService;
import petstore.petstore.services.AnimalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@Validated
public class AnimalController
{
  private final AnimalService animalService;
  private final ImageService imageService;

  @GetMapping("/animal/{id}")
  public ResponseEntity<AnimalDto> getOne(@PathVariable Long id)
  {
    AnimalDto animalDto = animalService.findById(id);

    return ResponseEntity.ok(animalDto);
  }

  @GetMapping("/animals")
  public ResponseEntity<Page<Animal>> getAll(Pageable pageable, @RequestParam(required = false) String search)
  {
    Page<Animal> animals = (search != null)
        ? animalService.findAll(pageable, search)
        : animalService.findAll(pageable);

    return ResponseEntity.ok(animals);
  }

  @PostMapping("/animal")
  public ResponseEntity<AnimalDto> add(
      @RequestPart("animal") @Valid NewAnimalDto newAnimalDto,
      @RequestPart("image") MultipartFile image
  ) throws IOException
  {
    Image savedImage = imageService.upload(image);

    newAnimalDto.setImg(savedImage.getName());
    AnimalDto animalDto = animalService.save(newAnimalDto);

    return ResponseEntity.ok(animalDto);
  }

  @PatchMapping("/animal/{id}")
  public ResponseEntity<AnimalDto> update(
      @RequestPart(value="animal", required=false) @Valid PatchAnimalDto patchAnimalDto,
      @RequestPart(value="image", required=false) MultipartFile patchImage,
      @PathVariable Long id
  ) throws IOException
  {
    if (patchImage == null)
    {
      AnimalDto animalDto = animalService.update(patchAnimalDto, id);
      return ResponseEntity.ok(animalDto);
    }

    AnimalDto animalDto = animalService.findById(id);
    Image updatedImage = imageService.update(animalDto.getImg(), patchImage);

    AnimalDto patchedAnimalDto = animalService.update(patchAnimalDto, updatedImage.getName(), id);

    return ResponseEntity.ok(patchedAnimalDto);
  }

  @DeleteMapping("/animal/{id}")
  public void delete(@PathVariable Long id)
  {
    Animal animal = animalService.delete(id);
//    imageService.delete(animal.getImg());
  }
}