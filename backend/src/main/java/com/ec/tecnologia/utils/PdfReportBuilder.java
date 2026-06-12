package com.ec.tecnologia.utils;

import com.ec.tecnologia.dto.factura.ProductoDetalleDto;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.stream.Stream;

@Slf4j
public class PdfReportBuilder {

    // Método que dibuja un rectángulo como borde dentro del documento PDF
    public void setRectangleInPdf(Document document) throws DocumentException {

        // Log para indicar que se está agregando el rectángulo al PDF
        log.info("Insertando Rectángulo...");

        // Crea un rectángulo con coordenadas específicas dentro del documento
        Rectangle rectangle = new Rectangle(577, 825, 18, 15);

        // Habilita el borde superior del rectángulo
        rectangle.enableBorderSide(1);

        // Habilita el borde inferior del rectángulo
        rectangle.enableBorderSide(2);

        // Habilita el borde izquierdo del rectángulo
        rectangle.enableBorderSide(4);

        // Habilita el borde derecho del rectángulo
        rectangle.enableBorderSide(8);

        // Establece el color del borde del rectángulo
        rectangle.setBorderColor(BaseColor.BLACK);

        // Define el grosor del borde del rectángulo
        rectangle.setBorderWidth(1);

        // Agrega el rectángulo al documento PDF
        document.add(rectangle);
    }

    // Método que agrega los encabezados de la tabla de productos en el PDF
    public void addTableHeader(PdfPTable table) {

        // Log para indicar que se están creando los encabezados de la tabla
        log.info("Insertando encabezado...");

        // Recorre cada título de columna para crear las celdas del encabezado
        Stream.of("Name", "Category", "Quantity", "Price", "Sub Total")
                .forEach(columnTitle -> {

                    // Crea una nueva celda para el encabezado
                    PdfPCell header = new PdfPCell();

                    // Establece un color de fondo inicial para la celda
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);

                    // Define el grosor del borde de la celda
                    header.setBorderWidth(2);

                    // Asigna el texto del encabezado a la celda
                    header.setPhrase(new Phrase(columnTitle));

                    // Sobrescribe el color de fondo de la celda a amarillo
                    header.setBackgroundColor(BaseColor.YELLOW);

                    // Alinea el texto horizontalmente al centro
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);

                    // Alinea el texto verticalmente al centro
                    header.setVerticalAlignment(Element.ALIGN_CENTER);

                    // Agrega la celda del encabezado a la tabla
                    table.addCell(header);
                });
    }

    // Método que agrega una fila de datos de producto a la tabla del PDF
    public void addRows(PdfPTable table, ProductoDetalleDto productoDetalleDto) {

        // Log para indicar que se está agregando una fila a la tabla
        log.info("Insertando filas...");

        table.addCell(productoDetalleDto.getName());
        table.addCell(productoDetalleDto.getCategory());
        table.addCell(productoDetalleDto.getQuantity());
        table.addCell(productoDetalleDto.getPrice().toString());
        table.addCell(productoDetalleDto.getTotal().toString());
    }

    // Método que devuelve diferentes estilos de fuente dependiendo del tipo solicitado
    public Font getFontHeader(String type) {

        // Log para indicar que se está seleccionando un tipo de fuente
        log.info("Inside getFontHeader");

        // Evalúa el tipo de fuente solicitado
        switch (type){

            // Caso para encabezados principales
            case "Header":

                // Crea una fuente Helvetica en negrita y cursiva con tamaño 18
                Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE, 18, BaseColor.BLACK);

                // Aplica estilo negrita adicional a la fuente
                headerFont.setStyle(Font.BOLD);

                // Retorna la fuente de encabezado
                return headerFont;

            // Caso para datos o contenido normal
            case "Data":

                // Crea una fuente Times New Roman con tamaño 11
                Font dataFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, BaseColor.BLACK);

                // Aplica estilo negrita a la fuente
                dataFont.setStyle(Font.BOLD);

                // Retorna la fuente de datos
                return dataFont;

            // Caso por defecto si no coincide ningún tipo
            default:

                // Retorna una fuente por defecto sin configuración especial
                return new Font();
        }
    }

}
