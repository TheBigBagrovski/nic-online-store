package nic.project.onlinestore.services.catalog;

import nic.project.onlinestore.models.Product;
import nic.project.onlinestore.models.Rating;
import nic.project.onlinestore.models.User;
import nic.project.onlinestore.repositories.RatingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RatingService {

    private final RatingRepository ratingRepository;

    public RatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    public Rating findRatingByUserAndProduct(User user, Product product) {
        return ratingRepository.findRatingByUserAndProduct(user, product);
    }

    public Integer findRatingsNumberByProduct(Product product) {
        return ratingRepository.countRatingsByProduct(product);
    }

    public Double findAverageRatingByProduct(Product product) {
        return ratingRepository.calculateAverageRatingByProductId(product.getId());
    }

    @Transactional
    public void deleteRating(Rating rating) {
        ratingRepository.delete(rating);
    }

    @Transactional
    public void updateValueById(Rating rating, Integer ratingValue) {
        ratingRepository.updateValueById(rating.getId(), ratingValue);
    }

    @Transactional
    public void saveRating(User user, Product product, Integer ratingValue) {
        ratingRepository.save(
                Rating.builder()
                        .value(ratingValue)
                        .user(user)
                        .product(product)
                        .build()
        );
    }

}
