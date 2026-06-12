package com.ec.tecnologia.utils;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class TecUtils {

    public static ResponseEntity<String> getResponseEntity(String responseMessage, HttpStatus httpStatus){
        return new ResponseEntity<>(
                "{\"message\":\""+responseMessage+"\"}",
                httpStatus
        );
    }

    //Genera un identificador único basado en el tiempo actual.
    public static String getUUID(){
        //Crea un objeto con la fecha y hora actual.
        Date date = new Date();
        //Obtiene el tiempo en milisegundos desde 1970 (Unix epoch).
        long time = date.getTime();
        //Devuelve un String como: Factura: 1710639123456
        return "Factura_" + time;
    }

    // Metodo estático que recibe un String en formato JSON y devuelve un JSONArray, puede lanzar una excepción si el formato es inválido
    public static JSONArray getJsonArrayFromString(String data) throws JSONException {

        // Crea un JSONArray a partir del String recibido, parseando el texto a una estructura JSON
        JSONArray jsonArray = new JSONArray(data);

        // Retorna el JSONArray ya convertido para su uso en el sistema
        return jsonArray;
    }

    // Metodo estático que convierte un String en formato JSON a un Map<String, Object>, devolviendo un mapa vacío si el String es nulo o vacío
    public static Map<String, Object> getMapFromJson(String data){

        // Verifica que el String no sea nulo ni esté vacío antes de intentar convertirlo
        if (!Strings.isNullOrEmpty(data)){

            // Convierte el JSON (String) a un Map<String, Object> usando la librería Gson y TypeToken para manejar genéricos
            return new Gson().fromJson(data, new TypeToken<Map<String, Object>>(){}.getType());
        }

        // Retorna un HashMap vacío en caso de que el String sea nulo o vacío
        return new HashMap<>();
    }

}
