package com.sportspro.mapper;

import com.sportspro.dto.ConnectionDTO;
import com.sportspro.model.Connection;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConnectionMapper {

    ConnectionDTO connectionToConnectionDTO(Connection connection);

    Connection connectionDTOToConnection(ConnectionDTO connectionDTO);
}
