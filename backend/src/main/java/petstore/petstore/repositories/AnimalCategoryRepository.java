package petstore.petstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import petstore.petstore.entities.AnimalCategory;

import java.util.Optional;

public interface AnimalCategoryRepository extends JpaRepository<AnimalCategory, Long>
{
  Optional<AnimalCategory> findByName(String name);
  AnimalCategory getByName(String name);
}