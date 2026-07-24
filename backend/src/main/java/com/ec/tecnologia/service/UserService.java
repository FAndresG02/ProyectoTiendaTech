package com.ec.tecnologia.service;

import com.ec.tecnologia.config.TecConstants;
import com.ec.tecnologia.dto.user.*;
import com.ec.tecnologia.entity.UserEntity;
import com.ec.tecnologia.repository.UserRepository;
import com.ec.tecnologia.security.CustomerUsersDetailsService;
import com.ec.tecnologia.security.JwtAuthenticationFilter;
import com.ec.tecnologia.security.JwtUtil;
import com.ec.tecnologia.utils.TecUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    //configuracion del jwt
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    CustomerUsersDetailsService customerUsersDetailsService;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    JwtAuthenticationFilter jwtAuthenticationFilter;

    //Metodo para crear un nuevo usuario
    public ResponseEntity<?> signUp(UserSignupDto signupRequest) {

        try {

            //Busca el usuario si existe
            UserEntity userEntity = userRepository.findByEmail(signupRequest.getEmail());

            if (userEntity != null) {
                return new ResponseEntity("El email ya existe en el sistema", HttpStatus.BAD_REQUEST);
            } else {

                UserEntity user = new UserEntity();
                user.setName(signupRequest.getName());
                user.setContactNumber(signupRequest.getContactNumber());
                user.setEmail(signupRequest.getEmail());
                user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
                user.setStatus(false);
                user.setRole("ROLE_USER");

                userRepository.save(user);

                return TecUtils.getResponseEntity("Usuario registrado correctamente", HttpStatus.OK);

            }

        } catch (Exception e) {
            log.error("Error al agregar al usuario", e);
            return TecUtils.getResponseEntity(TecConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //-----------------------------------------------------------------------------------------------------------------

    //Metodo para iniciar sesion despues de haber creado un nuevo usuario
    public ResponseEntity<?> login(UserLoginDto loginRequest) {

        try {

//            “Autentica el usuario.”
//            Spring Security verifica:
//                  email
//                  password
//                  usuario existente
//                  contraseña correcta
//            Y crea un token temporal con:
//                  username/email
//                  password
//            para que Spring compare con la BD.
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            ); // autentica usuario con email y password

            if (auth.isAuthenticated()) {
                if (customerUsersDetailsService.getUserDetail().isStatus()) {
                    String token = jwtUtil.generateToken(
                            customerUsersDetailsService.getUserDetail().getEmail(),
                            customerUsersDetailsService.getUserDetail().getRole()
                    ); // genera token JWT

                    return ResponseEntity.ok(
                            Map.of(
                                    "message", "Inicio de sesión exitoso.",
                                    "token", token
                            )
                    );// devuelve token en JSON automático
                } else {
                    return ResponseEntity.badRequest()
                            .body(Map.of("message", "Espere la aprobación del administrador."));
                }
            }else{
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Credenciales inválidas."));
            }

        } catch (Exception e) {
            log.error("Error al iniciar sesión", e);
            return TecUtils.getResponseEntity(TecConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    //-----------------------------------------------------------------------------------------------------------------

    //Metodo para devolver los usuarios solo los campos definidos en el dto UsersDto
    public ResponseEntity<List<UserGetDto>> getUsers(){

        try {

            if (jwtAuthenticationFilter.isAdmin()){

                return new ResponseEntity<>(userRepository.getAllUser(), HttpStatus.OK);

            }else{

                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            }

        }catch (Exception e){
            log.error("Error al obtener a los usuarios", e);
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //-----------------------------------------------------------------------------------------------------------------

    //metodo para actualizar el status del usuario normal siendo admin
    public ResponseEntity<?> updateStatus(UserUpdateStatusDto updateStatusRequest){
        try {

            if (jwtAuthenticationFilter.isAdmin()) {

                Optional<UserEntity> userEntity = userRepository.findById(updateStatusRequest.getId());

                if (userEntity.isPresent()) {

                    //Mediante la consulta pide el status del usuario normal y su id para actualizar
                    userRepository.updateStatus(updateStatusRequest.getStatus(),
                            updateStatusRequest.getId());

                    return TecUtils.getResponseEntity("Estado del usuario actualizado correctamente",
                            HttpStatus.OK);
                } else {
                    return TecUtils.getResponseEntity("El usuario no existe", HttpStatus.BAD_REQUEST);
                }

            }else{
                return TecUtils.getResponseEntity(TecConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }

        }catch (Exception e){
            log.error("Error al actualizar el usuario", e);
            return TecUtils.getResponseEntity(TecConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //-----------------------------------------------------------------------------------------------------------------

    //Metodo para cambiar la contrasenia
    public ResponseEntity<?> changePassword(UserChangePasswordDto changePasswordRequest){

        try {

            //Aqui se obtiene el usuario ya iniciado sesion
            UserEntity userEntity = userRepository.findByEmail(jwtAuthenticationFilter.getCurrentUser());

            //Verificamos que el usuario exista en la base de datos
            if (userEntity != null) {

                //Comparamos la contraseña actual ingresada por el usuario
                //con la contraseña cifrada almacenada en la base de datos
                //passwordEncoder.matches funciona solo texto plano contra hash BCrypt
                if (passwordEncoder.matches(
                        changePasswordRequest.getOldPassword(),
                        userEntity.getPassword())) {

                    //Verificamos que la nueva contraseña y la confirmación
                    //de contraseña sean iguales
                    if(changePasswordRequest.getNewPassword()
                            .equals(changePasswordRequest.getConfirmNewPassword())){

                        //Ciframos la nueva contraseña antes de guardarla
                        userEntity.setPassword(
                                passwordEncoder.encode(
                                        changePasswordRequest.getNewPassword()
                                )
                        );

                        //Guardamos el usuario con la nueva contraseña
                        userRepository.save(userEntity);

                        return TecUtils.getResponseEntity(
                                "Contraseña actualizada correctamente",
                                HttpStatus.OK
                        );

                    }else{

                        return TecUtils.getResponseEntity(
                                "Las nuevas contraseñas no coinciden",
                                HttpStatus.BAD_REQUEST
                        );
                    }

                } else {

                    return TecUtils.getResponseEntity(
                            "Su antigua contraseña es incorrecta",
                            HttpStatus.BAD_REQUEST
                    );
                }

            } else {

                return TecUtils.getResponseEntity(
                        "El usuario no existe",
                        HttpStatus.BAD_REQUEST
                );
            }

        } catch (Exception e){
            log.error("Error al cambiar la contraseña", e);
            return TecUtils.getResponseEntity(TecConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //--------------------------------------------------------------------------------------------------------------------

    //Eliminar usuarios siendo administrador
    public ResponseEntity<?> deleteUser( Long id) {

        try {

            if (jwtAuthenticationFilter.isAdmin()) {

                Optional<UserEntity> userEntity = userRepository.findById(id);

                if (userEntity.isPresent()) {

                    userRepository.delete(userEntity.get());
                    return TecUtils.getResponseEntity("Usuario eliminado correctamente",
                            HttpStatus.OK

                    );
                }else{
                    return TecUtils.getResponseEntity("El usuario no existe",
                            HttpStatus.BAD_REQUEST);
                }
            }else{
                return  TecUtils.getResponseEntity(TecConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }

        }catch (Exception e){
            log.error("Error al eliminar al usuario", e);
            return TecUtils.getResponseEntity(TecConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //-----------------------------------------------------------------------------------------------------------------

    //Cambiar el role de cualquier usuario con permiso de un admin
    public ResponseEntity<?> updateRole(UserUpdateRoleDto updateRoleRequest) {

        try {

            if (jwtAuthenticationFilter.isAdmin()) {

                Optional<UserEntity> userEntity = userRepository.findById(updateRoleRequest.getId());

                if (userEntity.isPresent()) {

                    userRepository.updateRole(updateRoleRequest.getRole(), updateRoleRequest.getId());

                    return TecUtils.getResponseEntity("El rol del usuario actualizado correctamente",
                            HttpStatus.OK);
                }else{
                    return TecUtils.getResponseEntity(TecConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
                }
            }else{
                return TecUtils.getResponseEntity(TecConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }

        }catch (Exception e){
            log.error("Error al cambiar de rol al usuario", e);
            return TecUtils.getResponseEntity(TecConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //-----------------------------------------------------------------------------------------------------------------

    //Metodo para actualizar datos de un usuario
    public ResponseEntity<?> updateUser(UserUpdateDto updateUserRequest) {

        try {

            UserEntity userEntity = validateUser(updateUserRequest);

            if(userEntity == null){
                return TecUtils.getResponseEntity("No existen cambios por hacerse",
                        HttpStatus.BAD_REQUEST);
            }else{

                userEntity.setName(updateUserRequest.getName());
                userEntity.setContactNumber(updateUserRequest.getContactNumber());
                userEntity.setEmail(updateUserRequest.getEmail());
                userRepository.save(userEntity);

                return TecUtils.getResponseEntity("Usuario actualizado correctamente", HttpStatus.OK);
            }

        }catch (Exception e){
            log.error("Error al eliminar al usuario", e);
            return TecUtils.getResponseEntity(TecConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private UserEntity validateUser(UserUpdateDto updateUserRequest){

        Optional<UserEntity> userEntityOptional = userRepository.findById(updateUserRequest.getId());

        if (userEntityOptional.isEmpty()) {
            throw new RuntimeException("Usuario no existe");
        }

        UserEntity userEntity = userEntityOptional.get();

        boolean noChanges =
                userEntity.getName().equals(updateUserRequest.getName()) &&
                        userEntity.getContactNumber().equals(updateUserRequest.getContactNumber()) &&
                        userEntity.getEmail().equals(updateUserRequest.getEmail());

        if (noChanges) {
            return null;
        }
        return userEntity;
    }


}
