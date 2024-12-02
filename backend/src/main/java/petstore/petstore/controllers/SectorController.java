package petstore.petstore.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import petstore.petstore.dtos.sectors.NewSectorDto;
import petstore.petstore.dtos.sectors.PatchSectorDto;
import petstore.petstore.dtos.sectors.SectorDto;
import petstore.petstore.entities.Sector;
import petstore.petstore.services.SectorService;

@RequiredArgsConstructor
@RestController
@Validated
public class SectorController
{
  private final SectorService sectorService;

  @GetMapping("/sector/{id}")
  public ResponseEntity<SectorDto> getOne(@PathVariable Long id)
  {
    SectorDto sectorDto = sectorService.findById(id);
    return ResponseEntity.ok(sectorDto);
  }

  @GetMapping("/sectors")
  public ResponseEntity<Page<Sector>> getAll(Pageable pageable, @RequestParam(required = false) String search)
  {
    Page<Sector> sectors = sectorService.findAll(pageable);
    return ResponseEntity.ok(sectors);
  }

  @PostMapping("/sector")
  public ResponseEntity<SectorDto> add(@RequestBody @Valid NewSectorDto newSectorDto)
  {
    SectorDto sectorDto = sectorService.save(newSectorDto);
    return ResponseEntity.ok(sectorDto);
  }

  @PatchMapping("/sector/{id}")
  public ResponseEntity<SectorDto> update(
      @RequestBody @Valid PatchSectorDto patchSectorDto,
      @PathVariable Long id
  ) {
    SectorDto updatedSectorDto = sectorService.update(patchSectorDto, id);
    return ResponseEntity.ok(updatedSectorDto);
  }

  @DeleteMapping("/sector/{id}")
  public void delete(@PathVariable Long id)
  {
    sectorService.delete(id);
  }
}