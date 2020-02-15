package my.jviracocha.talleractivity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import my.jviracocha.talleractivity.Frgments.GestionMecanicoFragment;

public class TemplatePDF {
    private Context context;
    private File pdfFile;
    //pdf fiel name
    String filename= new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
    //pdf file path
    String filepath= Environment.getExternalStorageDirectory()+"/"+filename+".pdf";
    private Document document;
    private PdfWriter pdfWriter;
    private Paragraph paragraph;
    //formatos para la escritura
    private Font fTitulo=new Font(Font.FontFamily.TIMES_ROMAN,13,Font.BOLD, BaseColor.RED);
    private Font fSubTitulo=new Font(Font.FontFamily.TIMES_ROMAN,12,Font.BOLD);
    private Font fText=new Font(Font.FontFamily.TIMES_ROMAN,10,Font.BOLD);
    private Font fHighText=new Font(Font.FontFamily.TIMES_ROMAN,10,Font.BOLD);

    public TemplatePDF(Context context) {
        this.context=context;
    }
    public void openDocument(){

        try{
            document=new Document(PageSize.A4);
            pdfWriter=PdfWriter.getInstance(document,new FileOutputStream(filepath));
            document.open();

        }catch (Exception e){
            Log.e("openDocument",e.toString());
        }
    }
    private void createFile(){
        File folder=new File(Environment.getExternalStorageDirectory().toString(),"PDF");
        if(!folder.exists()){
            folder.mkdirs();
            pdfFile=new File(folder,"TemplatePDF.pdf");
        }
    }

    public void clseDocument(){
        document.close();
    }
    public void addMetaData(String titulo, String subjet, String autor){
        document.addTitle(titulo);
        document.addSubject(subjet);
        document.addAuthor(autor);
    }
    public void addTitulo(String titulo, String subtitulo, String fecha){
        try {
            paragraph=new Paragraph();
            addChildP(new Paragraph(titulo,fTitulo));
            addChildP(new Paragraph(subtitulo,fSubTitulo));
            addChildP(new Paragraph("Generado :"+fecha,fText));
            paragraph.setSpacingAfter(30);
            document.add(paragraph);

        }catch (Exception e){
            Log.e("addTutulo",e.toString());
        }

    }
    private void addChildP(Paragraph childParagraph){
        //parrafos hijos se muestran dentro del parrafo padre
        childParagraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.add(childParagraph);
    }
    public void addPagraph(String text){
        try {
            paragraph=new Paragraph(text,fText);
            paragraph.setSpacingAfter(5);
            paragraph.setSpacingBefore(5);
            document.add(paragraph);

        }catch (Exception e){
            Log.e("addParrafo",e.toString());
        }

    }
    public void createTable(String[]headers, ArrayList<String[]>kilometraje){
        try {
            //tabla llenada con un parrafo
            paragraph=new Paragraph();
            paragraph.setFont(fText);
            PdfPTable pdfPTable=new PdfPTable(headers.length);
            pdfPTable.setWidthPercentage(100);
            PdfPCell pdfPCell;//<----- creación de una celda
            //cargar con el número de columar existentes en nuestra tabla
            //encabezados
            int indexC=0;
            while (indexC<headers.length){
                pdfPCell= new PdfPCell(new Phrase(headers[indexC++],fSubTitulo));
                pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfPCell.setBackgroundColor(BaseColor.GREEN);
                pdfPTable.addCell(pdfPCell);
            }
            //resto de nuestras tablas
            for (int indexR=0;indexR<kilometraje.size();indexR++){
                String[] row=kilometraje.get(indexR);
                for ( indexC=0;indexC<headers.length;indexC++){
                    pdfPCell=new PdfPCell(new Phrase(row[indexC]));
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPCell.setFixedHeight(20);
                   // pdfPCell.setFollowingIndent(40);
                    pdfPTable.addCell(pdfPCell);

                }
            }

            paragraph.add(pdfPTable);
            document.add(paragraph);
        }catch (Exception e){
            Log.e("createTable",e.toString());
        }

    }


    public void viewPDF(Context context){
        //Intent intent= new Intent(context,ClienteActivityPDf.class);
        //intent.putExtra("path",pdfFile.getAbsoluteFile());
       // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
       // context.startActivity(intent);
        Toast.makeText(context,filename+".pdf\n en guardado en\n"+filepath,Toast.LENGTH_SHORT).show();


    }
    public void appviewPDF(Activity activity){
        if(pdfFile.exists()){
            Uri uri=Uri.fromFile(pdfFile);
            Intent intent=new Intent(Intent.ACTION_VIEW);
            //intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(uri,"application/pdf");
            try{

                activity.startActivity(intent);
               // activity.startActivity(Intent.createChooser(intent, "Your title"));
            }catch (ActivityNotFoundException e){
                Log.e("creacionPDF",e.toString());
                activity.startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("market://details?id=com.adobe.reader&hl")));
                Toast.makeText(activity.getApplicationContext(),"no cuentas con aap para ver pdf",Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(activity.getApplicationContext(),"no se encontro el archivo ",Toast.LENGTH_LONG).show();
        }
    }
}
