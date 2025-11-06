package com.example.cgeproyect.servicios

import com.example.cgeproyect.Dominio.Boleta
import com.example.cgeproyect.Dominio.Cliente
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.font.PDType1Font
import org.apache.pdfbox.pdmodel.font.Standard14Fonts
import java.io.ByteArrayOutputStream
import java.awt.Color
import java.text.DecimalFormat

// Función auxiliar para dibujar texto y bajar la posición Y
fun PDPageContentStream.drawText(text: String, x: Float, y: Float, font: PDType1Font, fontSize: Float, color: Color = Color.BLACK) {
    this.setNonStrokingColor(color)
    this.beginText()
    this.setFont(font, fontSize)
    this.newLineAtOffset(x, y)
    this.showText(text)
    this.endText()
}

// Función auxiliar para dibujar una línea
fun PDPageContentStream.drawLine(x1: Float, y1: Float, x2: Float, y2: Float, width: Float = 0.5f, color: Color = Color.LIGHT_GRAY) {
    this.setStrokingColor(color)
    this.setLineWidth(width)
    this.moveTo(x1, y1)
    this.lineTo(x2, y2)
    this.stroke()
}


actual class PdfService {

    actual fun generarBoletaPDF(boleta: Boleta, cliente: Cliente): ByteArray {
        val document = PDDocument()
        val page = PDPage()
        document.addPage(page)

        val contentStream = PDPageContentStream(document, page)

        val font = PDType1Font(Standard14Fonts.FontName.HELVETICA)
        val boldFont = PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD)
        val italicFont = PDType1Font(Standard14Fonts.FontName.HELVETICA_OBLIQUE)

        val margin = 50f
        val pageHeight = page.mediaBox.height
        val pageWidth = page.mediaBox.width
        val contentWidth = pageWidth - 2 * margin
        var yPosition = pageHeight - margin // Posición Y actual

        val df = DecimalFormat("#,##0.0") // Formato para números

        try {
            // --- CABECERA ---
            contentStream.drawText("BOLETA DE VENTA", (pageWidth - boldFont.getStringWidth("BOLETA DE VENTA") / 1000 * 24f) / 2, yPosition, boldFont, 24f)
            yPosition -= 30
            contentStream.drawText("CGE Electricidad S.A.", (pageWidth - font.getStringWidth("CGE Electricidad S.A.") / 1000 * 16f) / 2, yPosition, font, 16f)
            yPosition -= 60

            // --- SECCIÓN: Mi consumo en el mes actual ---
            contentStream.drawText("Mi consumo en el mes actual", margin, yPosition, boldFont, 16f)
            yPosition -= 20
            contentStream.drawText("Para determinar cuánta electricidad consumiste en el mes se considera...", margin, yPosition, font, 10f)
            yPosition -= 30

            // Datos principales del consumo actual
            contentStream.drawText("Periodo de lectura: ${boleta.mes}/${boleta.anio} - ${boleta.mes+1}/${boleta.anio}", margin, yPosition, font, 12f)
            contentStream.drawText("Fecha estimada próxima lectura: ${boleta.mes+1}/${boleta.anio}", margin + contentWidth / 2, yPosition, font, 12f)
            yPosition -= 20

            // Tabla de lectura actual (simplificada)
            val tableYStart = yPosition
            val tableRowHeight = 25f
            val tableColWidths = listOf(contentWidth * 0.3f, contentWidth * 0.3f, contentWidth * 0.4f)
            var currentX = margin

            // Dibuja el fondo del encabezado
            contentStream.setNonStrokingColor(Color(220, 220, 220)) // Gris claro
            contentStream.addRect(margin, tableYStart - tableRowHeight, contentWidth, tableRowHeight)
            contentStream.fill()
            contentStream.setNonStrokingColor(Color.BLACK) // Reset color

            contentStream.setFont(boldFont, 10f)
            contentStream.beginText()
            contentStream.newLineAtOffset(currentX + 5, tableYStart - 15)
            contentStream.showText("Medidor")
            currentX += tableColWidths[0]
            contentStream.newLineAtOffset(tableColWidths[0], 0f) // Mueve a la siguiente columna
            contentStream.showText("Cliente")
            currentX += tableColWidths[1]
            contentStream.newLineAtOffset(tableColWidths[1], 0f)
            contentStream.showText("Consumo medidor")
            contentStream.endText()

            yPosition -= tableRowHeight

            // Fila de datos
            contentStream.setFont(font, 10f)
            currentX = margin
            contentStream.beginText()
            contentStream.newLineAtOffset(currentX + 5, yPosition - 15)
            contentStream.showText("N/A") // No tenemos el ID del medidor aquí directamente en Boleta
            currentX += tableColWidths[0]
            contentStream.newLineAtOffset(tableColWidths[0], 0f)
            contentStream.showText("${cliente.nombre} (${cliente.rut})")
            currentX += tableColWidths[1]
            contentStream.newLineAtOffset(tableColWidths[1], 0f)
            contentStream.showText("${df.format(boleta.kwhTotal)} kWh")
            contentStream.endText()
            yPosition -= tableRowHeight

            // Dibuja bordes de la tabla
            contentStream.setStrokingColor(Color.BLACK)
            contentStream.setLineWidth(0.5f)
            contentStream.addRect(margin, tableYStart - 2 * tableRowHeight, contentWidth, 2 * tableRowHeight)
            contentStream.drawLine(margin, tableYStart - tableRowHeight, margin + contentWidth, tableYStart - tableRowHeight) // Horizontal
            contentStream.drawLine(margin + tableColWidths[0], tableYStart, margin + tableColWidths[0], tableYStart - 2 * tableRowHeight) // Vertical
            contentStream.drawLine(margin + tableColWidths[0] + tableColWidths[1], tableYStart, margin + tableColWidths[0] + tableColWidths[1], tableYStart - 2 * tableRowHeight) // Vertical
            contentStream.stroke()

            yPosition -= 50

            // --- SECCIÓN: Detalles de la Boleta (Tabla principal) ---
            contentStream.drawText("Detalles de tu boleta", margin, yPosition, boldFont, 16f)
            yPosition -= 20

            val tablaBoleta = boleta.toPdfTable()
            val boletaTableRowHeight = 20f
            val boletaColWidth = contentWidth / 2f

            // Dibujar Encabezado de la Tabla
            val boletaTableYStart = yPosition
            contentStream.setNonStrokingColor(Color(220, 220, 220)) // Gris claro
            contentStream.addRect(margin, boletaTableYStart - boletaTableRowHeight, contentWidth, boletaTableRowHeight)
            contentStream.fill()
            contentStream.setNonStrokingColor(Color.BLACK) // Reset color

            contentStream.setFont(boldFont, 12f)
            currentX = margin
            contentStream.beginText()
            contentStream.newLineAtOffset(currentX + 5, boletaTableYStart - 15)
            contentStream.showText(tablaBoleta.headers[0]) // Concepto
            contentStream.newLineAtOffset(boletaColWidth, 0f)
            contentStream.showText(tablaBoleta.headers[1]) // Valor
            contentStream.endText()

            yPosition -= boletaTableRowHeight

            // Dibujar Filas de la Tabla
            contentStream.setFont(font, 12f)
            for ((index, row) in tablaBoleta.rows.withIndex()) {
                currentX = margin
                contentStream.beginText()
                contentStream.newLineAtOffset(currentX + 5, yPosition - 15)
                contentStream.showText(row[0]) // Concepto
                contentStream.newLineAtOffset(boletaColWidth, 0f)

                // Formato especial para el Total
                if (row[0].contains("TOTAL", ignoreCase = true)) {
                    contentStream.setFont(boldFont, 12f)
                    contentStream.showText(row[1]) // Valor
                    contentStream.setFont(font, 12f) // Volver a la fuente normal
                } else {
                    contentStream.showText(row[1]) // Valor
                }
                contentStream.endText()

                // Dibujar línea horizontal de la fila (excepto la última)
                if (index < tablaBoleta.rows.size -1) {
                    contentStream.drawLine(margin, yPosition - boletaTableRowHeight, margin + contentWidth, yPosition - boletaTableRowHeight)
                }
                yPosition -= boletaTableRowHeight
            }
            // Dibuja el borde exterior y las líneas verticales de la tabla de boleta
            contentStream.setStrokingColor(Color.BLACK)
            contentStream.setLineWidth(0.5f)
            contentStream.addRect(margin, boletaTableYStart - (tablaBoleta.rows.size + 1) * boletaTableRowHeight, contentWidth, (tablaBoleta.rows.size + 1) * boletaTableRowHeight)
            contentStream.drawLine(margin + boletaColWidth, boletaTableYStart, margin + boletaColWidth, boletaTableYStart - (tablaBoleta.rows.size + 1) * boletaTableRowHeight)
            contentStream.stroke()

            yPosition -= 50

            // --- SECCIÓN: Historial de consumo (Gráfico simulado) ---
            // NOTA: Para un gráfico real, necesitarías datos históricos y lógica compleja.
            // Aquí solo simulamos un gráfico de barras con texto.
            contentStream.drawText("¿Cuál fue mi consumo en los últimos 12 meses?", margin, yPosition, boldFont, 16f)
            yPosition -= 20
            contentStream.drawText("N/A: esta funcion es solo para que se vea bonito :3", margin, yPosition, italicFont, 10f, Color.GRAY)
            yPosition -= 15

            // Simulación de gráfico de barras con caracteres
            contentStream.setFont(font, 8f)
            val months = listOf("Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic")
            val dummyConsumptions = listOf(50, 65, 80, 75, 90, 110, 100, 95, 85, boleta.kwhTotal.toInt() / 10, 70, 60) // Datos simulados
            val maxBarHeight = 50f
            val barWidth = (contentWidth - 10) / months.size
            var barX = margin + 5

            contentStream.setNonStrokingColor(Color(100, 150, 200)) // Color azul para las barras
            for ((index, consumption) in dummyConsumptions.withIndex()) {
                val barHeight = (consumption.toFloat() / dummyConsumptions.maxOrNull()!!) * maxBarHeight
                contentStream.addRect(barX, yPosition - barHeight, barWidth - 2, barHeight)
                contentStream.fill()
                contentStream.drawText(months[index], barX, yPosition - barHeight - 10, font, 8f, Color.BLACK)
                barX += barWidth
            }
            contentStream.setNonStrokingColor(Color.BLACK) // Reset color
            yPosition -= maxBarHeight + 30 // Ajusta la posición para el siguiente elemento

            yPosition -= 50

            // --- SECCIÓN: Consejos de Ahorro ---

            // --- PIE DE PÁGINA ---
            yPosition = margin // Bajar al fondo
            contentStream.drawText("Gracias por su preferencia.", (pageWidth - font.getStringWidth("Gracias por su preferencia.") / 1000 * 10f) / 2, yPosition, font, 10f)

        } finally {
            contentStream.close()
        }

        val outputStream = ByteArrayOutputStream()
        document.save(outputStream)
        document.close()

        return outputStream.toByteArray()
    }
}