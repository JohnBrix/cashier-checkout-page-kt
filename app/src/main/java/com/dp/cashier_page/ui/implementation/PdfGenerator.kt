package com.dp.final_receipt_zm.ui

import android.content.Context
import android.os.Environment

import android.widget.Toast
import com.dp.cashier_page.R
import com.dp.cashier_page.domain.request.HttpPosRequest
import com.dp.cashier_page.domain.response.pos.HttpPos
import com.itextpdf.io.font.constants.StandardFonts
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.action.PdfAction
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.element.Text
import com.itextpdf.layout.property.TextAlignment
import com.itextpdf.layout.property.UnitValue
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

object PdfGenerator {

    private val linkSample = "https://www.facebook.com/oemplusbacoor"
    fun generatePdf(
        context: Context,
        response: HttpPos,
        grandTo: Double,
        totalAmount: Double,
        customer: String,
        request: HttpPosRequest
    ) {
        val mFileName = SimpleDateFormat("yyyMMdd_HHmmss", Locale.getDefault())
            .format(System.currentTimeMillis())

        val FILENAME = "${mFileName}" + ".pdf"
        val filePath = getAppPath(context) + FILENAME

        if (File(filePath).exists()) {
            File(filePath).delete()
        }

        val fOut = FileOutputStream(filePath)
        val pdfWriter = PdfWriter(fOut)

        // Creating a PdfDocument
        val pdfDocument =
            PdfDocument(pdfWriter)
        val layoutDocument = Document(pdfDocument)

        // title
        addTitle(layoutDocument, "29-23 Avenida Rizal, Bacoor, Cavite")

        //add empty line
        addEmptyLine(layoutDocument,0)


        //Add sub heading
        val appName = "OEM plus Car Accessories & Auto Parts"
        addSubHeading(layoutDocument, "Generated via: ${appName}")
        addLink(layoutDocument, linkSample)

        //add empty line
        addEmptyLine(layoutDocument,1)

        // customer reference information
        addDebitCredit(layoutDocument, request,grandTo,totalAmount,response)

        //add empty line
        addEmptyLine(layoutDocument,1)

        //Add sub heading
        addSubHeading(layoutDocument, "Transactions")

        //Add list
        addTable(layoutDocument, response,customer,request)

        layoutDocument.close()
        Toast.makeText(context, "Pdf saved successfully to location $filePath", Toast.LENGTH_LONG).show()

        //FileUtils.openFile(context, File(filePath))
    }

    private fun getAppPath(context: Context): String {
        val dir = File(
            Environment.getExternalStorageDirectory()
                .toString() + File.separator
                    + context.resources.getString(R.string.app_name)
                    + File.separator
        )
        if (!dir.exists()) {
            dir.mkdir()
        }
        return dir.path + File.separator
    }

    private fun addTable(
        layoutDocument: Document,
        items: HttpPos,
        customer: String,
        request: HttpPosRequest
    ) {

        val table = Table(
            UnitValue.createPointArray(
                floatArrayOf(
                    100f,
                    180f,
                    80f,
                    80f,
                    80f,
                    100f
                )
            )
        )

        // headers
        //table.addCell(Paragraph("S.N.O.").setBold())
        table.addCell(Paragraph("Item").setBold())
        table.addCell(Paragraph("Customer").setBold())
        table.addCell(Paragraph("Qty").setBold())
        table.addCell(Paragraph("Srp").setBold())
        table.addCell(Paragraph("Total").setBold())
        table.addCell(Paragraph("Date").setBold())

        val dateBought = SimpleDateFormat(" MMMM dd, yyyy",Locale.getDefault())
            .format(System.currentTimeMillis())


        // items
        request.itemList?.forEach {it
            var total = it.quantity!! * it.srpPrice!!
            table.addCell(Paragraph(it.itemName + ""))
            table.addCell(Paragraph("${customer}}" + ""))
            table.addCell(Paragraph(it.quantity.toString() + ""))
            table.addCell(Paragraph(it.srpPrice.toString() + ""))
            table.addCell(Paragraph(total.toString()+ ""))
            table.addCell(Paragraph(dateBought.toString() + ""))
//            table.addCell(Paragraph(a.SNO.toString() + ""))
          /*  table.addCell(Paragraph(a.itemName + ""))
            table.addCell(Paragraph(a.custName + ""))
            table.addCell(Paragraph(a.quantity.toString() + ""))
            table.addCell(Paragraph(a.pricePerUnit.toString() + ""))
            table.addCell(Paragraph((a.quantity * a.pricePerUnit).toString() + ""))
            table.addCell(Paragraph(a.transactionDateStr + ""))*/
        }
        layoutDocument.add(table)
    }

    private fun addEmptyLine(layoutDocument: Document, number: Int) {
        for (i in 0 until number) {
            layoutDocument.add(Paragraph(" "))
        }
    }

    private fun addDebitCredit(
        layoutDocument: Document,
        info: HttpPosRequest,
        grandTo: Double,
        totalAmount: Double,
        response: HttpPos
    ) {

        val table = Table(
            UnitValue.createPointArray(
                floatArrayOf(
                    100f,
                    160f
                )
            )
        )
        table.addCell(Paragraph("Transaction Number: ").setBold())
        table.addCell(Paragraph("${response.response?.transactionNumber}"))
        table.addCell(Paragraph("Cash").setBold())
        table.addCell(Paragraph("${info.cash}"))
        table.addCell(Paragraph("Change").setBold())
        table.addCell(Paragraph("${info.change}"))
        table.addCell(Paragraph("Tax").setBold())
        table.addCell(Paragraph("${info.tax}"))
        table.addCell(Paragraph("Sub Total").setBold())
        table.addCell(Paragraph("${totalAmount}"))
        table.addCell(Paragraph("Grand Total").setBold())
        table.addCell(Paragraph("${grandTo}"))

        layoutDocument.add(table)
    }

    private fun addSubHeading(layoutDocument: Document, text: String) {
        layoutDocument.add(
            Paragraph(text).setBold()
                .setTextAlignment(TextAlignment.CENTER)
        )
    }

    private fun addLink(layoutDocument: Document, text: String) {

        val blueText: Text = Text(text)
            .setFontColor(ColorConstants.BLUE)
            .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))

        layoutDocument.add(
            Paragraph(blueText)
                .setAction(PdfAction.createURI(text))
                .setTextAlignment(TextAlignment.CENTER)
                .setUnderline()
                .setItalic()
        )
    }

    private fun addTitle(layoutDocument: Document, text: String) {
        layoutDocument.add(
            Paragraph(text).setBold().setUnderline()
                .setTextAlignment(TextAlignment.CENTER)
        )
    }
}