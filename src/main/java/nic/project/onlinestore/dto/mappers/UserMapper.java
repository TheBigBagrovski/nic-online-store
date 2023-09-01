package nic.project.onlinestore.dto.mappers;

import nic.project.onlinestore.dto.auth.RegisterRequest;
import nic.project.onlinestore.dto.user.UserInfoResponse;
import nic.project.onlinestore.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    User mapRegisterRequestToUser(RegisterRequest registerRequest);

    UserInfoResponse mapToInfoResponse(User user);

}
