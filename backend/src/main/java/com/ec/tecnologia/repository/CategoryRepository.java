package com.ec.tecnologia.repository;

import com.ec.tecnologia.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    @Transactional
    @Modifying
    @Query("update CategoryEntity u set u.name = :name where u.id = :id")
    int updateCategory(@Param("name") String name,
                     @Param("id") Long id);

}
