package com.ec.tecnologia.controller;

import com.ec.tecnologia.config.TecConstants;
import com.ec.tecnologia.dto.factura.GenerateReportDto;
import com.ec.tecnologia.entity.FacturaEntity;
import com.ec.tecnologia.service.FacturaService;
import com.ec.tecnologia.utils.TecUtils;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping(path = "factura")
public class FacturaController {

    @Autowired
    FacturaService facturaService;

    @PostMapping(path = "/generateReport")
    public ResponseEntity<?> generateReport(@Valid  @RequestBody GenerateReportDto generateReportDto){

        try {

            return facturaService.generateReport(generateReportDto);

        }catch (Exception e){
            log.error("Error al generar un reporte", e);
            return TecUtils.getResponseEntity(TecConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/getFacturas")
    public ResponseEntity<List<FacturaEntity>> getFacturas(){

        try {

            return facturaService.getFacturas();

        }catch (Exception e){
            log.error("Error al obtener las facturas", e);
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Metodo para obtener el pdf
    @PostMapping(path = "/getPdf")
    public ResponseEntity<byte[]> getPdf(@RequestBody Map<String, Object> requestMap){
        try {

            return facturaService.getPdf(requestMap);

        }catch (Exception e){
            log.error("Error al obtener el pdf", e);
            return new ResponseEntity<>(new byte[0], HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<String> deleteFactura(@PathVariable("id") Integer id){

        try {

            return facturaService.deleteFactura(id);

        }catch (Exception e){
            log.error("Error al eliminar la factura", e);
            return TecUtils.getResponseEntity(TecConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
