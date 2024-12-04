package petstore.petstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import petstore.petstore.entities.Sector;

import java.util.Optional;

public interface SectorRepository
extends JpaRepository<Sector, Long>, JpaSpecificationExecutor<Sector>
{
  Optional<Sector> findByName(String name);
  Sector getByName(String name);
}