package petstore.petstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import petstore.petstore.entities.Sector;

import java.util.Optional;

public interface SectorRepository extends JpaRepository<Sector, Long>
{
  Optional<Sector> findByName(String name);
  Sector getByName(String name);
}