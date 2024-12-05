package petstore.petstore.loaders;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import petstore.petstore.dtos.animals.NewAnimalDto;
import petstore.petstore.entities.Animal;
import petstore.petstore.entities.AnimalCategory;
import petstore.petstore.entities.Image;
import petstore.petstore.entities.Sector;
import petstore.petstore.mappers.AnimalMapper;
import petstore.petstore.repositories.AnimalCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import petstore.petstore.repositories.AnimalRepository;
import petstore.petstore.repositories.ImageRepository;
import petstore.petstore.repositories.SectorRepository;
import petstore.petstore.services.ImageService;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

@Component
@RequiredArgsConstructor
public class PopulateAnimalDataLoader implements ApplicationRunner
{
  private final AnimalRepository animalRepository;
  private final AnimalMapper animalMapper;
  private final AnimalCategoryRepository animalCategoryRepository;
  private final SectorRepository sectorRepository;
  private final ImageRepository imageRepository;
  private final ImageService imageService;

  @Value("classpath:ryba.jpg")
  private Resource resourceRyba;

  public void run(ApplicationArguments args)
  {
    uploadImage();
    populateAnimalCategories();
    populateSectors();
    populateAnimals();
  }

  private void populateAnimals()
  {
    AnimalCategory birds = animalCategoryRepository.getByName("ptaki");
    AnimalCategory rodents = animalCategoryRepository.getByName("gryzonie");
    AnimalCategory rats = animalCategoryRepository.getByName("szczury");
    Sector sector;

    for (int i = 0; i < 12; i++)
    {
      Set<AnimalCategory> animalCategories = new HashSet<>();

      if (i % 2 == 0) {
        animalCategories.add(birds);
        sector = sectorRepository.getByName("terrarium2");
      }
      else {
        animalCategories.add(rodents);
        sector = sectorRepository.getByName("klatka1");
      }

      if (i % 3 == 0) {
        animalCategories.add(rats);
      }

      createAnimal(NewAnimalDto.builder()
          .name("Ryba " + (i + 1))
          .quantity((i % 2 == 0) ? i + 1 : 1)
          .price(new BigDecimal((i + 1) * 2.5))
          .categories(animalCategories)
          .sector(sector)
          .img("ryba.jpg")
          .build(),
          animalCategories,
          sector
      );
    }
  }

  private void createAnimal(NewAnimalDto newAnimalDto, Set<AnimalCategory> animalCategories, Sector sector)
  {
    Optional<Animal> existingAnimal = animalRepository.findByName(newAnimalDto.getName());

    if (existingAnimal.isEmpty())
    {
      Animal animal = animalMapper.newAnimalDtoToAnimal(newAnimalDto);
      animal.setCategories(animalCategories);
      animal.setSector(sector);
      animal.setImg("ryba.jpg");

      animalRepository.save(animal);
    }
  }

  private void populateSectors()
  {
    createSector("terrarium2");
    createSector("klatka1");
  }

  private void createSector(String name)
  {
    Optional<Sector> existingSector = sectorRepository.findByName(name);

    if (existingSector.isEmpty())
    {
      sectorRepository.save(Sector.builder().name(name).build());
    }
  }
  private void populateAnimalCategories()
  {
    createAnimalCategory("ptaki");
    createAnimalCategory("gryzonie");
    createAnimalCategory("ryby");
    createAnimalCategory("gady");
    createAnimalCategory("szczury");
    createAnimalCategory("jaszczurki");
  }

  private void createAnimalCategory(String name)
  {
    Optional<AnimalCategory> existingAnimalCategory = animalCategoryRepository.findByName(name);

    if (existingAnimalCategory.isEmpty())
    {
      animalCategoryRepository.save(AnimalCategory.builder().name(name).build());
    }
  }

  public void uploadImage()
  {
    Optional<Image> optionalRybaImage = imageRepository.findByName(resourceRyba.getFilename());

    if (optionalRybaImage.isPresent())
      return;

    try (InputStream inputStream = resourceRyba.getInputStream()) {
      byte[] imageBytes = inputStream.readAllBytes();

      imageRepository.save(
          Image.builder()
              .name(resourceRyba.getFilename())
              .type(MediaType.IMAGE_JPEG_VALUE)
              .imageBlob(imageService.compress(imageBytes))
              .build()
      );

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}