package com.example.backend.controller;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.backend.repository.RutineRepository;
import com.example.backend.model.Rutine;
import com.example.backend.model.ExRutine;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/pdf")
public class PDFController {
    @Autowired
    private RutineRepository rutinaRepository;

    @GetMapping("/download/{date}/{id}")
    public void downloadPdf(HttpServletResponse response, @PathVariable String date, @PathVariable Long id) throws IOException {
        response.setContentType("application/pdf");
        String truncatedDate = date.substring(0, Math.min(date.length(), 10)); // date in file
        String fileName = "rutina_" + truncatedDate + ".pdf";
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        Rutine rutine = rutinaRepository.findById(id).orElseThrow();
        List<ExRutine> list= rutine.getExercises();

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
                for (ExRutine exRutine : list) {
                    contentStream.setFont(PDType1Font.COURIER_BOLD_OBLIQUE, 11); 
                    contentStream.showText("   Ejercicio: ");
                    contentStream.setFont(PDType1Font.HELVETICA, 10); 
                    contentStream.showText(exRutine.getExercise());
                    contentStream.setFont(PDType1Font.COURIER_BOLD_OBLIQUE, 11);
                    contentStream.showText("    S X Rep: ");
                    contentStream.setFont(PDType1Font.HELVETICA, 10); 
                    contentStream.showText(exRutine.getSeries());
                    contentStream.setFont(PDType1Font.COURIER_BOLD_OBLIQUE, 11);
                    contentStream.showText("    Peso (kg): ");
                    contentStream.setFont(PDType1Font.HELVETICA, 10);
                    contentStream.showText(exRutine.getWeight().toString());
                    contentStream.newLineAtOffset(0, -10); 
                    contentStream.setFont(PDType1Font.HELVETICA, 12); 
                    contentStream.showText("_________________________________________________________________");
                    contentStream.newLineAtOffset(0, -20);
                }
                
                contentStream.endText();
            }

            document.save(response.getOutputStream());
        }
    }
}
