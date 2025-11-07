package com.panaderiafeliz.api.mapper;

import com.panaderiafeliz.api.config.MapStructConfig;
import com.panaderiafeliz.api.dto.PanDto;
import com.panaderiafeliz.api.model.Pan;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface PanMapper {



    @Mapping(source = "nombre", target = "titulo")
    PanDto toDto(Pan p);

    @InheritInverseConfiguration(name = "toDto")
    Pan toEntity(PanDto dto);
}
