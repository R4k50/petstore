package petstore.petstore.repositories;

import petstore.petstore.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long>
{
  Optional<Image> findByName(String name);
}
