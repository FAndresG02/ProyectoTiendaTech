package com.ec.tecnologia.service;

import com.ec.tecnologia.config.TecConstants;
import com.ec.tecnologia.dto.factura.GenerateReportDto;
import com.ec.tecnologia.dto.factura.ProductoDetalleDto;
import com.ec.tecnologia.entity.FacturaEntity;
import com.ec.tecnologia.repository.FacturaRepository;
import com.ec.tecnologia.security.JwtAuthenticationFilter;
import com.ec.tecnologia.utils.PdfReportBuilder;
import com.ec.tecnologia.utils.TecUtils;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class FacturaService {

    @Autowired
    FacturaRepository facturaRepository;

    @Autowired
    JwtAuthenticationFilter jwtAuthenticationFilter;

    public ResponseEntity<?> generateReport(GenerateReportDto generateReportDto){

        log.info("Generando el reporte....");
        System.out.println("REQUEST MAP: " + generateReportDto);
        System.out.println("CWD: " + new File(".").getAbsolutePath());
        System.out.println("STORE_LOCATION resuelto: " + new File(TecConstants.STORE_LOCATION).getAbsolutePath());

        try {

            // Variable para almacenar el nombre del archivo PDF
            String fileName;

            if (generateReportDto.getIsGenerate() != null && !generateReportDto.getIsGenerate()) {
                fileName = generateReportDto.getUuid();  // reutiliza UUID existente
            } else {
                fileName = TecUtils.getUUID();
                generateReportDto.setUuid(fileName);     // asigna el nuevo UUID al DTO
                insertFactura(generateReportDto);
            }

            // Construye el texto con los datos del cliente que se mostrarán en el PDF
            String data = "Nombre: " + generateReportDto.getName() + "\n" + "Número de contacto: " + generateReportDto.getContactNumber() +
                    "\n" + "Email: " + generateReportDto.getEmail() + "\n" + "Método de pago: " + generateReportDto.getPaymentMethod();

            // Crea un nuevo documento PDF
            com.itextpdf.text.Document document = new Document();

            // Crea el objeto File para la ruta donde se guardarán los PDFs
            File dir = new File(TecConstants.STORE_LOCATION);
            // Verifica si la carpeta existe
            if (!dir.exists()) {
                // Crea la carpeta si no existe
                dir.mkdirs();
            }

            // Inicializa el escritor del PDF indicando la ruta y nombre del archivo
            PdfWriter.getInstance(document, new FileOutputStream(TecConstants.STORE_LOCATION+"/"+fileName+".pdf"));
            // Abre el documento para empezar a escribir contenido
            document.open();
            // Dibuja un rectángulo borde en el documento PDF
            PdfReportBuilder pdfReportBuilder = new PdfReportBuilder();
            pdfReportBuilder.setRectangleInPdf(document);

            // Crea el título del documento con estilo de encabezado
            Paragraph chunk = new Paragraph("Sistema Café", pdfReportBuilder.getFontHeader("Header"));
            // Centra el título en el documento
            chunk.setAlignment(Element.ALIGN_CENTER);
            // Agrega el título al documento
            document.add(chunk);

            // Crea un párrafo con los datos del cliente
            Paragraph paragraph = new Paragraph(data + "\n \n", pdfReportBuilder.getFontHeader("Data"));
            // Agrega el párrafo al documento
            document.add(paragraph);

            // Crea una tabla con 5 columnas para los productos
            PdfPTable table = new PdfPTable(5);
            // Establece el ancho de la tabla al 100% del documento
            table.setWidthPercentage(100);

            // Agrega los encabezados de la tabla
            pdfReportBuilder.addTableHeader(table);

            // Convierte el JSON de productos (String) a JSONArray
            for (ProductoDetalleDto producto : generateReportDto.getProductDetails()){
                pdfReportBuilder.addRows(table, producto);
            }

            // Agrega la tabla al documento PDF
            document.add(table);

            // Crea el pie de página con el total y mensaje final
            Paragraph footer = new Paragraph("Total: " + generateReportDto.getTotalAmount()+ "\n" +
                    "Gracias por su visita", pdfReportBuilder.getFontHeader("Data"));
            // Agrega el pie de página al documento
            document.add(footer);

            // Cierra el documento para finalizar la escritura del PDF
            document.close();

            // Imprime en consola la ruta donde se guardó el archivo
            System.out.println("RUTA: " + TecConstants.STORE_LOCATION);
            // Imprime en consola si la carpeta existe
            System.out.println("EXISTE: " + dir.exists());

            // Retorna el UUID del archivo generado en formato JSON con estado HTTP 200
            return new ResponseEntity<>("{\"uuid\":\"" + fileName + "\"}", HttpStatus.OK);


        }catch (Exception e){
            log.error("Error al generar un reporte", e);
            return TecUtils.getResponseEntity(TecConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void insertFactura(GenerateReportDto generateReportDto){
        try {
            FacturaEntity facturaEntity = new FacturaEntity();
            facturaEntity.setUuid(generateReportDto.getUuid());
            facturaEntity.setName(generateReportDto.getName());
            facturaEntity.setEmail(generateReportDto.getEmail());
            facturaEntity.setContactNumber(generateReportDto.getContactNumber());
            facturaEntity.setPaymentMethod(generateReportDto.getPaymentMethod());
            facturaEntity.setTotal(generateReportDto.getTotalAmount());
            facturaEntity.setCreateBy(jwtAuthenticationFilter.getCurrentUser());

            // Convierte la lista a String JSON para guardar en BD
            ObjectMapper mapper = new ObjectMapper();
            String productDetailsJson = mapper.writeValueAsString(generateReportDto.getProductDetails());
            facturaEntity.setProductDetail(productDetailsJson);

            facturaRepository.save(facturaEntity);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    //-----------------------------------------------------------------------------------------------------------------

    public ResponseEntity<List<FacturaEntity>> getFacturas(){

        try {

            //Creamos una lista vacia para almacenar las facturas
            List<FacturaEntity> list = new ArrayList<>();

            if (jwtAuthenticationFilter.isAdmin()){
                // si somos admin obtnenmos todas la facturas que han sido creadas por usuarios con correo admin
                list = facturaRepository.getAllFacturas();
            }else {
                // si somos usuarios obtenemos solo las facturas creadas por usuarios con correos user
                list = facturaRepository.getAllFacturasUser(jwtAuthenticationFilter.getCurrentUser());
            }

            //devuelve la respuesta ya sea de usuario o admin
            return new ResponseEntity<>(list, HttpStatus.OK);

        }catch (Exception e){
            log.error("Error al obtener las facturas", e);
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //----------------------------------------------------------------------------------------------------------------

    public ResponseEntity<byte[]> getPdf(String uuid) {

        log.info("Inside getPdf: uuid {}", uuid);

        try {
                byte[] byteArray = new byte[0];

                // Valida que venga el uuid
                if (uuid == null || uuid.isBlank()) {
                    return new ResponseEntity<>(byteArray, HttpStatus.BAD_REQUEST);
                }

                // Construye la ruta del archivo
                String filePath = TecConstants.STORE_LOCATION + "/" + uuid + ".pdf";

                // Caso 1: El PDF ya existe en disco
                if (TecUtils.ifFileExist(filePath)) {
                    byteArray = getByteArray(filePath);
                    return new ResponseEntity<>(byteArray, HttpStatus.OK);
                } else {
                    // Caso 2: El PDF no existe, lo regenera desde los datos de la BD
                    FacturaEntity factura = facturaRepository.findByUuid(uuid);

                    if (factura == null) {
                        return new ResponseEntity<>(byteArray, HttpStatus.NOT_FOUND);
                    }

                    // Reconstruye el DTO desde la BD
                    GenerateReportDto dto = new GenerateReportDto();
                    dto.setUuid(factura.getUuid());
                    dto.setName(factura.getName());
                    dto.setEmail(factura.getEmail());
                    dto.setContactNumber(factura.getContactNumber());
                    dto.setPaymentMethod(factura.getPaymentMethod());
                    dto.setTotalAmount(factura.getTotal().doubleValue());
                    dto.setIsGenerate(false); // no inserta en BD, solo regenera PDF

                    // Convierte el JSON String de la BD a List<ProductoDetalleDto>
                    ObjectMapper mapper = new ObjectMapper();
                    List<ProductoDetalleDto> productos = mapper.readValue(
                            factura.getProductDetail(),
                            new TypeReference<List<ProductoDetalleDto>>() {}
                    );
                    dto.setProductDetails(productos);

                    // Regenera el PDF
                    generateReport(dto);

                    byteArray = getByteArray(filePath);
                    return new ResponseEntity<>(byteArray, HttpStatus.OK);
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new byte[0], HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //Ese método sirve para leer un archivo desde el disco y convertirlo en un arreglo de bytes (byte[]).
    //Es justo lo que se necesita, por ejemplo, para devolver un PDF en un endpoint.
    private byte[] getByteArray(String filePath) throws Exception{
        //Toma un archivo (por su ruta) y lo transforma en bytes para poder usarlo en memoria.
        //Crea una referencia al archivo en esa ruta.
        File initialFile = new File(filePath);
        //Abre el archivo como flujo de entrada (para leerlo).
        InputStream targetStream = new FileInputStream(initialFile);
        //Lee TODO el archivo y lo convierte en un byte[].
        //IOUtils (de Apache Commons IO) hace el trabajo pesado automáticamente.
        byte[] byteArray = IOUtils.toByteArray(targetStream);
        //Cierra el stream (muy importante para no consumir recursos).
        targetStream.close();
        //Devuelve el archivo en forma de bytes.
        return byteArray;
    }

    //-----------------------------------------------------------------------------------------------------------------

    public ResponseEntity<?> deleteFactura(Long id){

        try {

            if (jwtAuthenticationFilter.isAdmin()){

                Optional<FacturaEntity> facturaEntity = facturaRepository.findById(id);

                if (facturaEntity.isPresent()){

                    facturaRepository.deleteById(id);

                    return TecUtils.getResponseEntity("Factura Eliminada", HttpStatus.OK);

                }else  {
                    return TecUtils.getResponseEntity("Factura no encontrada", HttpStatus.NOT_FOUND);
                }
            }else  {
                return TecUtils.getResponseEntity("No tienes Autorización", HttpStatus.UNAUTHORIZED);
            }

        }catch (Exception e){
            log.error("Error al eliminar la factura", e);
            return TecUtils.getResponseEntity(TecConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
