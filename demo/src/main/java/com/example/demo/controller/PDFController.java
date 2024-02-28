package com.example.demo.controller;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.demo.repository.RutinaRepository;
import com.example.demo.model.Rutina;
import com.example.demo.model.EjerRutina;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/pdf")
public class PDFController {
    @Autowired
    private RutinaRepository rutinaRepository;

    @GetMapping("/download/{fecha}/{id}")
    public void downloadPdf(HttpServletResponse response, @PathVariable String fecha, @PathVariable Long id) throws IOException {
        response.setContentType("application/pdf");
        String truncatedFecha = fecha.substring(0, Math.min(fecha.length(), 10)); // Tomar los primeros 10 caracteres
        String fileName = "rutina_" + truncatedFecha + ".pdf";
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        Rutina rutine = rutinaRepository.findById(id).orElseThrow();
        List<EjerRutina> lista= rutine.getEjercicios();

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.beginText();
                contentStream.setFont(PDType1Font.TIMES_BOLD, 20);
                contentStream.newLineAtOffset(100, 700);
                contentStream.showText("                                  RUTINA");
                contentStream.newLineAtOffset(0, -30);
                contentStream.setFont(PDType1Font.HELVETICA, 12); 
                contentStream.showText("_________________________________________________________________");
                contentStream.newLineAtOffset(0, -20);
                for (EjerRutina ejerRutina : lista) {
                    contentStream.setFont(PDType1Font.COURIER_BOLD_OBLIQUE, 11); // Cambiar el tipo de letra
                    contentStream.showText("   Ejercicio: ");
                    contentStream.setFont(PDType1Font.HELVETICA, 10); // Restablecer el tipo de letra
                    contentStream.showText(ejerRutina.getEjercicio());
                    contentStream.setFont(PDType1Font.COURIER_BOLD_OBLIQUE, 11); // Cambiar el tipo de letra
                    contentStream.showText("    S X Rep: ");
                    contentStream.setFont(PDType1Font.HELVETICA, 10); // Restablecer el tipo de letra
                    contentStream.showText(ejerRutina.getSeries());
                    contentStream.setFont(PDType1Font.COURIER_BOLD_OBLIQUE, 11); // Cambiar el tipo de letra
                    contentStream.showText("    Peso (kg): ");
                    contentStream.setFont(PDType1Font.HELVETICA, 10); // Restablecer el tipo de letra
                    contentStream.showText(ejerRutina.getPeso().toString());
                    contentStream.newLineAtOffset(0, -10); // Mover hacia abajo
                    contentStream.setFont(PDType1Font.HELVETICA, 12); 
                    contentStream.showText("_________________________________________________________________");
                    contentStream.newLineAtOffset(0, -20); // Mover hacia abajo
                }
                
                contentStream.endText();
            }

            document.save(response.getOutputStream());
        }
    }
}
