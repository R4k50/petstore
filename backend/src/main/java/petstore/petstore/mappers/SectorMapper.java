package petstore.petstore.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import petstore.petstore.dtos.sectors.NewSectorDto;
import petstore.petstore.dtos.sectors.PatchSectorDto;
import petstore.petstore.dtos.sectors.SectorDto;
import petstore.petstore.entities.Sector;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SectorMapper
{
  SectorDto toSectorDto(Sector sector);

  Sector toSector(NewSectorDto newSectorDto);

  List<SectorDto> toSectorDtoList(List<Sector> sectors);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void update(@MappingTarget Sector sector, PatchSectorDto patchSectorDto);
}