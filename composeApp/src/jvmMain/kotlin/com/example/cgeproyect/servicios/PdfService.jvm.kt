package com.example.cgeproyect.servicios

import com.example.cgeproyect.Dominio.Boleta
import com.example.cgeproyect.Dominio.Cliente

// --- IMPORTACIONES DE PDFBOX (Estas faltaban) ---
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.font.PDType1Font
import org.apache.pdfbox.pdmodel.font.Standard14Fonts

// --- IMPORTACIONES DE JAVA (Estas también faltaban) ---
import java.io.ByteArrayOutputStream
import java.awt.Color
import java.text.DecimalFormat

// --- FUNCIONES AUXILIARES ---
fun PDPageContentStream.drawText(text: String, x: Float, y: Float, font: PDType1Font, fontSize: Float, color: Color = Color.BLACK) {
    this.setNonStrokingColor(color)
    this.beginText()
    this.setFont(font, fontSize)
    this.newLineAtOffset(x, y)
    this.showText(text)
    this.endText()
}

fun PDPageContentStream.drawLine(x1: Float, y1: Float, x2: Float, y2: Float, width: Float = 0.5f, color: Color = Color.LIGHT_GRAY) {
    this.setStrokingColor(color)
    this.setLineWidth(width)
    this.moveTo(x1, y1)
    this.lineTo(x2, y2)
    this.stroke()
}
// --- FIN FUNCIONES AUXILIARES ---

// Implementación 'actual' para JVM (Escritorio)
actual class PdfService {

    actual fun generarBoletaPDF(boleta: Boleta, cliente: Cliente, historial: Map<Int, Double>, medidorCodigo: String): ByteArray {
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
        var yPosition = pageHeight - margin

        val df = DecimalFormat("#,##0.0")

        try {
            // --- CABECERA ---
            contentStream.drawText("BOLETA DE VENTA", (pageWidth - boldFont.getStringWidth("BOLETA DE VENTA") / 1000 * 24f) / 2, yPosition, boldFont, 24f)
            yPosition -= 30
            contentStream.drawText("CGE Electricidad S.A.", (pageWidth - font.getStringWidth("CGE Electricidad S.A.") / 1000 * 16f) / 2, yPosition, font, 16f)
            yPosition -= 60

            // --- 2. Información del Cliente ---
            val lineHeight = 15f
            contentStream.drawText("Cliente: ${cliente.nombre}", margin, yPosition, font, 12f)
            yPosition -= lineHeight
            contentStream.drawText("RUT: ${cliente.rut}", margin, yPosition, font, 12f)
            yPosition -= lineHeight
            contentStream.drawText("Dirección: ${cliente.direccionFacturacion}", margin, yPosition, font, 12f)
            yPosition -= 40

            // --- SECCIÓN: Mi consumo en el mes actual ---
            contentStream.drawText("Mi consumo en el mes actual", margin, yPosition, boldFont, 16f)
            yPosition -= 20
            contentStream.drawText("Para determinar cuánta electricidad consumiste en el mes se considera...", margin, yPosition, font, 10f)
            yPosition -= 30

            contentStream.drawText("Periodo de lectura: ${boleta.mes}/${boleta.anio} - ${boleta.mes+1}/${boleta.anio}", margin, yPosition, font, 12f)
            contentStream.drawText("Fecha estimada próxima lectura: ${boleta.mes+1}/${boleta.anio}", margin + contentWidth / 2, yPosition, font, 12f)
            yPosition -= 20

            val tableYStart = yPosition
            val tableRowHeight = 25f
            val tableColWidths = listOf(contentWidth * 0.3f, contentWidth * 0.3f, contentWidth * 0.4f)
            var currentX = margin

            contentStream.setNonStrokingColor(Color(220, 220, 220))
            contentStream.addRect(margin, tableYStart - tableRowHeight, contentWidth, tableRowHeight)
            contentStream.fill()
            contentStream.setNonStrokingColor(Color.BLACK)

            contentStream.setFont(boldFont, 10f)
            contentStream.beginText()
            contentStream.newLineAtOffset(currentX + 5, tableYStart - 15)
            contentStream.showText("Medidor")
            currentX += tableColWidths[0]
            contentStream.newLineAtOffset(tableColWidths[0], 0f)
            contentStream.showText("Cliente")
            currentX += tableColWidths[1]
            contentStream.newLineAtOffset(tableColWidths[1], 0f)
            contentStream.showText("Consumo medidor")
            contentStream.endText()

            yPosition -= tableRowHeight

            contentStream.setFont(font, 10f)
            currentX = margin
            contentStream.beginText()
            contentStream.newLineAtOffset(currentX + 5, yPosition - 15)
            contentStream.showText(medidorCodigo)
            currentX += tableColWidths[0]
            contentStream.newLineAtOffset(tableColWidths[0], 0f)
            contentStream.showText("${cliente.nombre} (${cliente.rut})")
            currentX += tableColWidths[1]
            contentStream.newLineAtOffset(tableColWidths[1], 0f)
            contentStream.showText("${df.format(boleta.kwhTotal)} kWh")
            contentStream.endText()
            yPosition -= tableRowHeight

            contentStream.setStrokingColor(Color.BLACK)
            contentStream.setLineWidth(0.5f)
            contentStream.addRect(margin, tableYStart - 2 * tableRowHeight, contentWidth, 2 * tableRowHeight)
            contentStream.drawLine(margin, tableYStart - tableRowHeight, margin + contentWidth, tableYStart - tableRowHeight)
            contentStream.drawLine(margin + tableColWidths[0], tableYStart, margin + tableColWidths[0], tableYStart - 2 * tableRowHeight)
            contentStream.drawLine(margin + tableColWidths[0] + tableColWidths[1], tableYStart, margin + tableColWidths[0] + tableColWidths[1], tableYStart - 2 * tableRowHeight)
            contentStream.stroke()

            yPosition -= 50

            // --- SECCIÓN: Detalles de la Boleta ---
            contentStream.drawText("Detalles de tu boleta", margin, yPosition, boldFont, 16f)
            yPosition -= 20

            val tablaBoleta = boleta.toPdfTable()
            val boletaTableRowHeight = 20f
            val boletaColWidth = contentWidth / 2f
            val boletaTableYStart = yPosition

            contentStream.setNonStrokingColor(Color(220, 220, 220))
            contentStream.addRect(margin, boletaTableYStart - boletaTableRowHeight, contentWidth, boletaTableRowHeight)
            contentStream.fill()
            contentStream.setNonStrokingColor(Color.BLACK)

            contentStream.setFont(boldFont, 12f)
            currentX = margin
            contentStream.beginText()
            contentStream.newLineAtOffset(currentX + 5, boletaTableYStart - 15)
            contentStream.showText(tablaBoleta.headers[0])
            contentStream.newLineAtOffset(boletaColWidth, 0f)
            contentStream.showText(tablaBoleta.headers[1])
            contentStream.endText()

            yPosition -= boletaTableRowHeight

            contentStream.setFont(font, 12f)
            for ((index, row) in tablaBoleta.rows.withIndex()) {
                currentX = margin
                contentStream.beginText()
                contentStream.newLineAtOffset(currentX + 5, yPosition - 15)
                contentStream.showText(row[0])
                contentStream.newLineAtOffset(boletaColWidth, 0f)

                if (row[0].contains("TOTAL", ignoreCase = true)) {
                    contentStream.setFont(boldFont, 12f)
                    contentStream.showText(row[1])
                    contentStream.setFont(font, 12f)
                } else {
                    contentStream.showText(row[1])
                }
                contentStream.endText()

                if (index < tablaBoleta.rows.size -1) {
                    contentStream.drawLine(margin, yPosition - boletaTableRowHeight, margin + contentWidth, yPosition - boletaTableRowHeight)
                }
                yPosition -= boletaTableRowHeight
            }
            val totalTableHeight = (tablaBoleta.rows.size + 1) * boletaTableRowHeight
            contentStream.setStrokingColor(Color.BLACK)
            contentStream.setLineWidth(0.5f)
            contentStream.addRect(margin, boletaTableYStart - totalTableHeight, contentWidth, totalTableHeight)
            contentStream.drawLine(margin + boletaColWidth, boletaTableYStart, margin + boletaColWidth, boletaTableYStart - totalTableHeight)
            contentStream.stroke()

            yPosition -= 50

            // --- SECCIÓN: Historial de consumo ---
            contentStream.drawText("Consumo en los últimos 12 meses (kWh)", margin, yPosition, boldFont, 16f)
            yPosition -= 30

            val months = listOf("Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic")
            val maxConsumption = historial.values.maxOrNull()?.takeIf { it > 0 } ?: 1.0
            val maxBarHeight = 50f
            val barWidth = (contentWidth - 10) / months.size
            var barX = margin + 5

            contentStream.setNonStrokingColor(Color(100, 150, 200))

            for ((index, monthName) in months.withIndex()) {
                val monthNumber = index + 1
                val consumption = historial[monthNumber] ?: 0.0
                val barHeight = ((consumption / maxConsumption) * maxBarHeight).toFloat()

                if (barHeight > 0) {
                    contentStream.addRect(barX, yPosition - maxBarHeight, barWidth - 2, barHeight)
                    contentStream.fill()
                }

                contentStream.drawText(monthName, barX, yPosition - maxBarHeight - 10, font, 8f, Color.BLACK)

                barX += barWidth
            }
            contentStream.setNonStrokingColor(Color.BLACK)
            yPosition -= maxBarHeight + 30


            // --- PIE DE PÁGINA ---
            yPosition = margin
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