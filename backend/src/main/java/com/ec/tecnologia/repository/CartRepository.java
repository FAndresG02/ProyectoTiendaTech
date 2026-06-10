package com.ec.tecnologia.repository;

import com.ec.tecnologia.entity.CartEntity;
import com.ec.tecnologia.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CartRepository extends JpaRepository<CartEntity, Long> {

    CartEntity findByUserEntity(UserEntity userEntity);

}
