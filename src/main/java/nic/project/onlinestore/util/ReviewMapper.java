package nic.project.onlinestore.util;

import nic.project.onlinestore.dto.product.ReviewResponse;
import nic.project.onlinestore.model.Review;
import nic.project.onlinestore.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(target = "user", source = "user", qualifiedByName = "mapUser")
    ReviewResponse mapToReviewResponse(Review review);

    @Named(value = "mapUser")
    default String mapUser(User user) {
        return user.getFirstname() + " " + user.getLastname();
    }

}
