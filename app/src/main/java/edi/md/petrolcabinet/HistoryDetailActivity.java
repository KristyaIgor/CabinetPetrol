package edi.md.petrolcabinet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;

import edi.md.petrolcabinet.remote.transaction.Transaction;

public class HistoryDetailActivity extends AppCompatActivity {

    ImageButton btnBack;

    TextView textLoc, textTip;

    ConstraintLayout layoutAlimentare, layoutSuplinire;
    TextView cardName, dataAlimentare, produsul, cantitatea, statiaPeco, sumaAlimentare,numarulAlimentare;
    TextView tipAchitare, dataSuplinire, sumaSuplinire;

    Button btnDownloadAlimentare, btnShareAlimentare, btnDownloadSuplinire, btnShareSuplinire;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View decorView = getWindow().getDecorView();
        Window window = getWindow();
        decorView.setSystemUiVisibility(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        decorView.setFitsSystemWindows(true);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.green));

        setContentView(R.layout.activity_history_detail);

        btnBack = findViewById(R.id.image_back_transaction_item);
        textLoc = findViewById(R.id.texxt_locale_transaction);
        textTip = findViewById(R.id.text_type_transaction);
        layoutAlimentare = findViewById(R.id.layout_alimentare);
        layoutSuplinire = findViewById(R.id.layout_suplinire);
        cardName = findViewById(R.id.text_card_name);
        dataAlimentare = findViewById(R.id.text_document_data);
        produsul = findViewById(R.id.text_item_product);
        cantitatea = findViewById(R.id.text_item_cantitatea);
        statiaPeco = findViewById(R.id.text_item_location);
        sumaAlimentare = findViewById(R.id.text_suma_alimentare);
        numarulAlimentare = findViewById(R.id.text_item_nr);
        btnDownloadAlimentare = findViewById(R.id.button_download_chitanta);
        btnShareAlimentare = findViewById(R.id.button_share_chitanta);
        tipAchitare = findViewById(R.id.text_tip_suplinire);
        dataSuplinire = findViewById(R.id.text_data_suplinire);
        sumaSuplinire = findViewById(R.id.text_suma_suplinire);
        btnDownloadSuplinire = findViewById(R.id.button_download_chitanta_suplinire);
        btnShareSuplinire = findViewById(R.id.button_share_chitanta_suplinire);

        Transaction transaction = BaseApp.getAppInstance().getClickedTransaction();

        if(transaction.getAmount() > 0){
            textLoc.setText(transaction.getStation()); // trebuie de setat tipul
            textTip.setText(getString(R.string.type_transaction_suplinire));

            layoutAlimentare.setVisibility(View.GONE);
            layoutSuplinire.setVisibility(View.VISIBLE);

            tipAchitare.setText(getString(R.string.type_transaction_suplinire));
            dataSuplinire.setText(transaction.getDocumentDate().replace("-",".") + " " + transaction.getDocumentTime().replace(".",":"));
            sumaSuplinire.setText("+" + transaction.getAmount() + " MDL");
            sumaSuplinire.setTextColor(getResources().getColor(R.color.green));
        }
        else {
            textLoc.setText(transaction.getStation());
            textTip.setText(getString(R.string.alimentare_type_transaction));

            layoutAlimentare.setVisibility(View.VISIBLE);
            layoutSuplinire.setVisibility(View.GONE);

            cardName.setText(transaction.getCardName());
            dataAlimentare.setText(transaction.getDocumentDate().replace("-",".") + " " + transaction.getDocumentTime().replace(".",":"));
            numarulAlimentare.setText(transaction.getDocumentNr());
            produsul.setText(transaction.getAssortment());
            cantitatea.setText(transaction.getQuantity() + " L");
            statiaPeco.setText(transaction.getStation());
            sumaAlimentare.setText(transaction.getAmount() + " MDL");
            sumaAlimentare.setTextColor(getResources().getColor(R.color.red));
        }

        btnBack.setOnClickListener(view -> finish());

        btnShareSuplinire.setOnClickListener(view -> {
            shareTransaction(transaction);
        });

        btnShareAlimentare.setOnClickListener(view -> {
            shareTransaction(transaction);
        });

        btnDownloadSuplinire.setOnClickListener(view -> {
            downloadTransaction(transaction);
        });

        btnDownloadAlimentare.setOnClickListener(view -> {
            downloadTransaction(transaction);
        });
    }

    private void downloadTransaction(Transaction transaction) {
        String directoryDownload = "";
        if(transaction.getAmount() > 0){
            directoryDownload = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + getString(R.string.type_transaction_suplinire) + "-" + transaction.getDocumentDate() + "-" + transaction.getDocumentTime() + ".pdf";
        }
        else{
            directoryDownload = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + getString(R.string.alimentare_type_transaction) + "-" + transaction.getDocumentDate() + "-" + transaction.getDocumentTime() + ".pdf";
        }


        PdfDocument document = new PdfDocument();

        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder( 2481,3507,1).create();
        PdfDocument.Page myPage = document.startPage(myPageInfo);
        Canvas canvas = myPage.getCanvas();

        Paint paint = new Paint();
        canvas.drawPaint(paint);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.is_logo_dark);
        bitmap = Bitmap.createScaledBitmap(bitmap, 220, 43, true);

        canvas.drawBitmap(bitmap, 75, 50 , null);

        Paint myPaint = new Paint();
        int x = 100, y=150;

        canvas.drawText(transaction.getAmount() + " MDL", x, y, myPaint);

        document.finishPage(myPage);

        File myFile = new File(directoryDownload);
        try {
            document.writeTo(new FileOutputStream(myFile));
        }
        catch (Exception e){
            e.printStackTrace();
        }

        document.close();

//        Toast.makeText(this, "Documend saved: " + directoryDownload, Toast.LENGTH_SHORT).show();
        Uri uri = FileProvider.getUriForFile(
                this,
                "edi.md.petrolcabinet.provider", //(use your app signature + ".provider" )
                myFile);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivity(intent);
    }

    private void shareTransaction(Transaction transaction) {
        String directoryDownload = "";
        String typeTr = "";
        if(transaction.getAmount() > 0){
            typeTr = getString(R.string.type_transaction_suplinire);
            directoryDownload = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + getString(R.string.type_transaction_suplinire) + "-" + transaction.getDocumentDate() + "-" + transaction.getDocumentTime() + ".pdf";
        }
        else{
            typeTr = getString(R.string.alimentare_type_transaction);
            directoryDownload = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + getString(R.string.alimentare_type_transaction) + "-" + transaction.getDocumentDate() + "-" + transaction.getDocumentTime() + ".pdf";
        }


        PdfDocument document = new PdfDocument();

        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder( 580,820,1).create();
        PdfDocument.Page myPage = document.startPage(myPageInfo);
        Canvas canvas = myPage.getCanvas();

        Paint myPaint = new Paint();

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.is_logo_dark);
        bitmap = Bitmap.createScaledBitmap(bitmap, 220, 43, true);

        canvas.drawBitmap(bitmap, 180, 50 , myPaint);

        canvas.drawText(typeTr + " " + transaction.getStation(), 80, 110, myPaint);

        canvas.drawLine(50,120,535,121,myPaint);

        canvas.drawText(transaction.getAmount() + " MDL", 80, 140, myPaint);

        document.finishPage(myPage);

        File myFile = new File(directoryDownload);
        try {
            document.writeTo(new FileOutputStream(myFile));
        }
        catch (Exception e){
            e.printStackTrace();
        }

        document.close();


        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        Uri uri = FileProvider.getUriForFile(
                this,
                "edi.md.petrolcabinet.provider", //(use your app signature + ".provider" )
                myFile);

        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.setType("application/pdf");
        share.putExtra(Intent.EXTRA_STREAM, uri);

        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        share.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        Intent shareIntent = Intent.createChooser(share, getString(R.string.msg_share_transaction));

        startActivity(shareIntent);
    }
}