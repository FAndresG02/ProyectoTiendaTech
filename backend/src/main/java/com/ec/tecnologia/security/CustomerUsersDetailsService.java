package com.ec.tecnologia.security;

import com.ec.tecnologia.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

//Le indica a Spring que esta clase es un servicio, lo que permite que sea detectada y 
//gestionada por el contenedor de Spring.
@Slf4j
//Implementa la interfaz UserDetailsService, que es una parte fundamental de Spring Security 
//para cargar los detalles del usuario durante el proceso de autenticación.
@Service
public class CustomerUsersDetailsService implements UserDetailsService {

    //Inyección de dependencia para UserRepository, que se utiliza para acceder a los datos de los 
    //usuarios en la base de datos.
    @Autowired
    UserRepository userRepository;

    //Variable para almacenar los detalles del usuario que se cargan durante la autenticación,
    private com.ec.tecnologia.entity.UserEntity userDetail;

    //Sobrescribe el método loadUserByUsername de la interfaz UserDetailsService, que se llama durante
    //el proceso de autenticación para cargar los detalles del usuario a partir de su nombre de usuario 
    //(en este caso, el email).
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("Inside loadUserByUsername {}", username);

        //Busca el usuario por email con el método findByEmail del UserRepository.
        userDetail = userRepository.findByEmail(username);


        //Si existe, devuelve un objeto UserDetails con:
        //email
        //password
        //authorities (en este caso una lista vacía)
        if (userDetail != null) {
            return new User(
                    userDetail.getEmail(),
                    userDetail.getPassword(),
                    new ArrayList<>()
            );
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }

    //Método para obtener los detalles del usuario que se cargan durante la autenticación.
    public com.ec.tecnologia.entity.UserEntity getUserDetail(){
        return userDetail;
    }
}
