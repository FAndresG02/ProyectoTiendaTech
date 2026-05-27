package com.ec.tecnologia.controller;


import com.ec.tecnologia.config.TecConstants;
import com.ec.tecnologia.dto.user.*;
import com.ec.tecnologia.service.UserService;
import com.ec.tecnologia.utils.TecUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController //manejara peticiones HTTP
@RequestMapping(path = "/users") //define la ruta de las peticiones
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupDto signupRequest) {
        try {

            return userService.signUp(signupRequest);

        }catch(Exception e){
            log.error("Error al agregar al usuario", e);
            return TecUtils.getResponseEntity(TecConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginRequest) {

        try {

            return userService.login(loginRequest);

        }catch (Exception e){
            log.error("Error al iniciar sesión", e);
            return TecUtils.getResponseEntity(TecConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getUsers")
    public ResponseEntity<List<UsersDto>> getUsers() {

        try {

            return userService.getUsers();

        }catch (Exception e){
            log.error("Error al obtener a los usuarios", e);
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //metodo para actualizar el status del usuario normal siendo administrador
    @PatchMapping("/update-status")
    public ResponseEntity<?> updateStatus(@RequestBody UpdateStatusDto updateStatusRequest) {

        try {

            return userService.updateStatus(updateStatusRequest);

        }catch (Exception e){
            log.error("Error al actualizar el usuario", e);
            return TecUtils.getResponseEntity(TecConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Peticion post para cambiar la contraseña
    @PatchMapping(path = "/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDto changePasswordRequest) {

        try {

            return userService.changePassword(changePasswordRequest);

        }catch (Exception e){
            log.error("Error al cambiar la contraseña", e);
            return TecUtils.getResponseEntity(TecConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Eliminar usuarios normales
    @DeleteMapping(path = "/deleteUser/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {

        try {

            return userService.deleteUser(id);

        }catch (Exception e){
            log.error("Error al eliminar al usuario", e);
            return TecUtils.getResponseEntity(TecConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }






}
