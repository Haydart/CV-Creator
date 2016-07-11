package com.example.radek.cv_creator;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.pdf.PrintedPdfDocument;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Radek on 2016-07-11.
 */
public class PDFManager {
    private Context context;

    public PDFManager(Context context, boolean savePermissionGranted) {
        this.context = context;
        this.savePermissionGranted = savePermissionGranted;
    }

    private boolean savePermissionGranted = false;

    public void savePDF(String fileName, View content, boolean permissionGranted){
        savePermissionGranted = permissionGranted;
        if(savePermissionGranted){
            // Create a shiny new (but blank) PDF document in memory
            // We want it to optionally be printable, so add PrintAttributes
            // and use a PrintedPdfDocument. Simpler: new PdfDocument().
            PrintAttributes printAttrs = new PrintAttributes.Builder().
                    setColorMode(PrintAttributes.COLOR_MODE_COLOR).
                    setMediaSize(PrintAttributes.MediaSize.NA_LETTER).
                    setResolution(new PrintAttributes.Resolution("x", "PRINT_SERVICE", 256, 256)).
                    setMinMargins(PrintAttributes.Margins.NO_MARGINS).
                    build();
            PdfDocument document = new PrintedPdfDocument(context, printAttrs);
            // crate a page description
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(content.getWidth(), (int)(content.getWidth()*Math.sqrt(2)), 1).create();
            // create a new page from the PageInfo
            PdfDocument.Page page = document.startPage(pageInfo);
            // repaint the user's text into the page
            content.draw(page.getCanvas());
            // do final processing of the page
            document.finishPage(page);
            // Here you could add more pages in a longer doc app, but you'd have
            // to handle page-breaking yourself in e.g., write your own word processor...
            // Now write the PDF document to a file; it actually needs to be a file
            // since the Share mechanism can't accept a byte[]. though it can
            // accept a String/CharSequence. Meh.
            try {
                File f = new File(Environment.getExternalStorageDirectory().getPath() + "/" + fileName);
                FileOutputStream fos = new FileOutputStream(f);
                document.writeTo(fos);
                document.close();
                fos.close();
            } catch (IOException e) {
                throw new RuntimeException("Error generating file", e);
            }
        }
    }

    public void displayCreatedPDF(String fileName){
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ "/" + fileName);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        context.startActivity(intent);
    }
}
