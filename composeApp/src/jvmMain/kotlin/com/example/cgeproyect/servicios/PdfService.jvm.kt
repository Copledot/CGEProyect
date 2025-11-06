package com.example.cgeproyect.servicios

import com.example.cgeproyect.Dominio.Boleta
import com.example.cgeproyect.Dominio.Cliente
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.font.PDType1Font
import org.apache.pdfbox.pdmodel.font.Standard14Fonts
import java.io.ByteArrayOutputStream
import java.awt.Color // Importa el color de Java AWT

// Implementación 'actual' para JVM (Escritorio) con diseño profesional
actual class PdfService {

    actual fun generarBoletaPDF(boleta: Boleta, cliente: Cliente): ByteArray {
        val document = PDDocument()
        val page = PDPage()
        document.addPage(page)

        val contentStream = PDPageContentStream(document, page)

        // --- Definición de Fuentes y Márgenes ---
        val font = PDType1Font(Standard14Fonts.FontName.HELVETICA)
        val boldFont = PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD)

        val margin = 50f
        val pageHeight = page.mediaBox.height
        val pageWidth = page.mediaBox.width
        val contentWidth = pageWidth - 2 * margin
        var yPosition = pageHeight - margin // Posición Y actual, empieza arriba

        try {
            // --- 1. Título ---
            contentStream.beginText()
            contentStream.setFont(boldFont, 18f)
            val title = "BOLETA DE VENTA"
            val titleWidth = boldFont.getStringWidth(title) / 1000 * 18f
            // Centrar el título
            contentStream.newLineAtOffset((pageWidth - titleWidth) / 2, yPosition)
            contentStream.showText(title)
            contentStream.endText()
            yPosition -= 20 // Bajar un poco

            contentStream.beginText()
            contentStream.setFont(font, 14f)
            val subtitle = "CGE Electricidad S.A."
            val subtitleWidth = font.getStringWidth(subtitle) / 1000 * 14f
            contentStream.newLineAtOffset((pageWidth - subtitleWidth) / 2, yPosition)
            contentStream.showText(subtitle)
            contentStream.endText()
            yPosition -= 40 // Bajar después del título

            // --- 2. Información del Cliente ---
            contentStream.beginText()
            contentStream.setFont(font, 12f)
            contentStream.newLineAtOffset(margin, yPosition)
            contentStream.showText("Cliente: ${cliente.nombre}")
            contentStream.newLine()
            contentStream.showText("RUT: ${cliente.rut}")
            contentStream.newLine()
            contentStream.showText("Dirección: ${cliente.direccionFacturacion}")
            contentStream.endText()
            yPosition -= 60 // Bajar después de los datos

            // --- 3. Tabla de Detalles ---
            val tabla = boleta.toPdfTable()
            val rowHeight = 20f
            val colWidth = contentWidth / 2f // Dos columnas: Concepto y Valor

            // Dibujar Encabezado de la Tabla
            contentStream.setFont(boldFont, 12f)
            var currentX = margin
            // Dibuja el fondo del encabezado
            contentStream.setNonStrokingColor(Color(220, 220, 220)) // Gris claro
            contentStream.addRect(margin, yPosition - rowHeight, contentWidth, rowHeight)
            contentStream.fill()
            contentStream.setNonStrokingColor(Color.BLACK) // Reset color

            // Textos del encabezado
            contentStream.beginText()
            contentStream.newLineAtOffset(currentX + 5, yPosition - 15) // 5 de padding
            contentStream.showText(tabla.headers[0]) // Concepto
            contentStream.endText()

            contentStream.beginText()
            currentX += colWidth
            contentStream.newLineAtOffset(currentX + 5, yPosition - 15)
            contentStream.showText(tabla.headers[1]) // Valor
            contentStream.endText()

            yPosition -= rowHeight

            // Dibujar Filas de la Tabla
            contentStream.setFont(font, 12f)
            for (row in tabla.rows) {
                currentX = margin

                // Texto de la fila (Concepto)
                contentStream.beginText()
                contentStream.newLineAtOffset(currentX + 5, yPosition - 15)
                contentStream.showText(row[0])
                contentStream.endText()

                // Texto de la fila (Valor)
                currentX += colWidth
                contentStream.beginText()
                contentStream.newLineAtOffset(currentX + 5, yPosition - 15)
                contentStream.showText(row[1])
                contentStream.endText()

                // Dibujar línea horizontal de la fila
                contentStream.setStrokingColor(Color.LIGHT_GRAY)
                contentStream.setLineWidth(0.5f)
                contentStream.moveTo(margin, yPosition - rowHeight)
                contentStream.lineTo(margin + contentWidth, yPosition - rowHeight)
                contentStream.stroke()

                yPosition -= rowHeight
            }

            // Dibujar líneas verticales de la tabla
            contentStream.moveTo(margin, yPosition + (tabla.rows.size + 1) * rowHeight)
            contentStream.lineTo(margin, yPosition)
            contentStream.moveTo(margin + colWidth, yPosition + (tabla.rows.size + 1) * rowHeight)
            contentStream.lineTo(margin + colWidth, yPosition)
            contentStream.moveTo(margin + contentWidth, yPosition + (tabla.rows.size + 1) * rowHeight)
            contentStream.lineTo(margin + contentWidth, yPosition)
            contentStream.stroke()

            // --- 4. Pie de Página ---
            yPosition = margin // Bajar al fondo
            contentStream.beginText()
            contentStream.setFont(font, 10f)
            val footer = "Gracias por su preferencia."
            val footerWidth = font.getStringWidth(footer) / 1000 * 10f
            contentStream.newLineAtOffset((pageWidth - footerWidth) / 2, yPosition)
            contentStream.showText(footer)
            contentStream.endText()

        } finally {
            contentStream.close()
        }

        // Guarda el documento en un array de bytes en memoria
        val outputStream = ByteArrayOutputStream()
        document.save(outputStream)
        document.close()

        return outputStream.toByteArray()
    }
}