package petstore.petstore.services;

import com.google.common.base.Joiner;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import petstore.petstore.dtos.sectors.NewSectorDto;
import petstore.petstore.dtos.sectors.PatchSectorDto;
import petstore.petstore.dtos.sectors.SectorDto;
import petstore.petstore.entities.Animal;
import petstore.petstore.entities.Sector;
import petstore.petstore.enums.SearchOperation;
import petstore.petstore.exceptions.AppException;
import petstore.petstore.mappers.SectorMapper;
import petstore.petstore.repositories.AnimalRepository;
import petstore.petstore.repositories.SectorRepository;
import petstore.petstore.specifications.SectorSpecificationsBuilder;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@RequiredArgsConstructor
@Service
public class SectorService
{
  private final SectorRepository sectorRepository;
  private final SectorMapper sectorMapper;
  @PersistenceContext
  EntityManager entityManager;

  public SectorDto findById(Long id)
  {
    Sector sector = sectorRepository.findById(id)
        .orElseThrow(() -> new AppException("Nieznana sekcja", HttpStatus.NOT_FOUND));

    return sectorMapper.toSectorDto(sector);
  }

  public SectorDto findByName(String name)
  {
    Sector sector = sectorRepository.findByName(name)
        .orElseThrow(() -> new AppException("Nieznana sekcja", HttpStatus.NOT_FOUND));

    return sectorMapper.toSectorDto(sector);
  }

  public Page<Sector> findAll(Pageable pageable)
  {
    Page<Sector> sectors = sectorRepository.findAll(pageable);
    return sectors;
  }

  public Page<Sector> findAll(Pageable pageable, String search) {
    SectorSpecificationsBuilder builder = new SectorSpecificationsBuilder();

    String operationSetExper = Joiner.on("|").join(SearchOperation.SIMPLE_OPERATION_SET);
    Pattern pattern = Pattern.compile(
        "(\\w+?)(" + operationSetExper + ")(\\p{Punct}?)(\\w+?)(\\p{Punct}?),"
    );

    if (search != null) {
      Matcher matcher = pattern.matcher(search + ",");

      while (matcher.find()) {
        builder.with(
            matcher.group(1),
            matcher.group(2),
            matcher.group(4),
            matcher.group(3),
            matcher.group(5)
        );
      }
    }

    Specification<Sector> sectorSpecification = builder.build();
    Page<Sector> sectors = sectorRepository.findAll(sectorSpecification, pageable);

    return sectors;
  }

  public SectorDto save(NewSectorDto newSectorDto)
  {
    Sector sector = sectorMapper.toSector(newSectorDto);
    Sector savedSector = sectorRepository.save(sector);

    return sectorMapper.toSectorDto(savedSector);
  }

  public SectorDto update(PatchSectorDto patchSectorDto, Long id)
  {
    Sector sector = sectorRepository.findById(id)
        .orElseThrow(() -> new AppException("Nieznana sekcja", HttpStatus.NOT_FOUND));

    sectorMapper.update(sector, patchSectorDto);
    Sector updatedSector = sectorRepository.save(sector);

    return sectorMapper.toSectorDto(updatedSector);
  }

  @Transactional
  public void delete(Long id)
  {
    Sector sector = sectorRepository.findById(id)
        .orElseThrow(() -> new AppException("Nieznana sekcja", HttpStatus.NOT_FOUND));

    String deleteJoinTableQuery = "UPDATE animals SET sector_id = NULL WHERE sector_id = ?;";
    Query query = entityManager.createNativeQuery(deleteJoinTableQuery);
    query.setParameter(1, id);
    query.executeUpdate();

    sectorRepository.delete(sector);
  }
}