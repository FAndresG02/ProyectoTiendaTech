package com.ec.tecnologia.repository;

import com.ec.tecnologia.dto.user.UsersDto;
import com.ec.tecnologia.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

//    ¿Cuándo sí conviene usar @Query?
//
//    Queries complejas con JOINs, subqueries o lógica condicional
//    Proyecciones personalizadas
//    Queries nativas (nativeQuery = true)
//    Cuando el nombre del método resultaría demasiado largo y poco legible

    //Busca por email Spring Data JPA puede derivar la query automáticamente desde el nombre del método:
    UserEntity findByEmail(String email);

    /// Devuelve todos los usuarios con rol USER en formato DTO UsersDto
    @Query("""
    select new com.ec.tecnologia.dto.user.UsersDto(
        u.id,
        u.name,
        u.email,
        u.contactNumber,
        u.status
    )
    from UserEntity u
    where u.role = 'ROLE_USER'
    """)
    List<UsersDto> getAllUser();

    @Transactional
    @Modifying
    @Query("update UserEntity u set u.status = :status where u.id = :id")
    int updateStatus(@Param("status") Boolean status,
                     @Param("id") Long id);






}
