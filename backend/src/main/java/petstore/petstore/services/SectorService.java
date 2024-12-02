package petstore.petstore.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import petstore.petstore.dtos.sectors.NewSectorDto;
import petstore.petstore.dtos.sectors.PatchSectorDto;
import petstore.petstore.dtos.sectors.SectorDto;
import petstore.petstore.entities.Sector;
import petstore.petstore.exceptions.AppException;
import petstore.petstore.mappers.SectorMapper;
import petstore.petstore.repositories.SectorRepository;


@RequiredArgsConstructor
@Service
public class SectorService
{
  private final SectorRepository sectorRepository;
  private final SectorMapper sectorMapper;

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

  public void delete(Long id)
  {
    Sector sector = sectorRepository.findById(id)
        .orElseThrow(() -> new AppException("Nieznana sekcja", HttpStatus.NOT_FOUND));

    sectorRepository.delete(sector);
  }
}