package com.edusphere.edusphere.specification;

import com.edusphere.edusphere.entity.Course;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class CourseSpecification {
    public static Specification<Course> hasTitle(String title){
        return (root,query,criteriaBuilder)->criteriaBuilder.like(
                criteriaBuilder.lower(root.get("title")),
                "%"+title.toLowerCase()+"%"
        );
    }
    public static Specification<Course> priceGreaterThanOrEqualTo(BigDecimal minPrice){
        return ((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("price"),minPrice));

    }
    public static Specification<Course> priceLessThanOrEqualTo(BigDecimal maxPrice){
        return (((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("Price"),maxPrice)
                ));
    }
}
