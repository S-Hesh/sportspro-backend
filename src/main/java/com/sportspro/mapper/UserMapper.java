package com.sportspro.mapper;

import com.sportspro.dto.UserDTO;
import com.sportspro.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "username", source = "name")
    UserDTO userToUserDTO(User user);

    @Mapping(target = "userId", ignore = true)  // We don't need to map userId during creation
    @Mapping(target = "name", source = "username")
    User userDTOToUser(UserDTO userDTO);
}
