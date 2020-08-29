package edi.md.mydesign;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

import edi.md.mydesign.remote.transaction.Transaction;

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
            textTip.setText("Suplinire");

            layoutAlimentare.setVisibility(View.GONE);
            layoutSuplinire.setVisibility(View.VISIBLE);

            tipAchitare.setText("Suplinire");
            dataSuplinire.setText(transaction.getDocumentDate().replace("-",".") + " " + transaction.getDocumentTime().replace(".",":"));
            sumaSuplinire.setText("+" + transaction.getAmount() + " MDL");
            sumaSuplinire.setTextColor(getResources().getColor(R.color.green));
        }
        else {
            textLoc.setText(transaction.getStation());
            textTip.setText("Alimentare");

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
            directoryDownload = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Suplinire-" + transaction.getDocumentDate() + "-" + transaction.getDocumentTime() + ".pdf";
        }
        else{
            directoryDownload = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Alimentare-" + transaction.getDocumentDate() + "-" + transaction.getDocumentTime() + ".pdf";
        }


        PdfDocument document = new PdfDocument();

        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(300,600,1).create();
        PdfDocument.Page myPage = document.startPage(myPageInfo);

        Paint myPaint = new Paint();
        int x = 10, y=25;

        myPage.getCanvas().drawText(transaction.getAmount() + " MDL", x, y, myPaint);

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
                "edi.md.mydesign.provider", //(use your app signature + ".provider" )
                myFile);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivity(intent);
    }

    private void shareTransaction(Transaction transaction) {
        String directoryDownload = "";
        if(transaction.getAmount() > 0){
            directoryDownload = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Suplinire-" + transaction.getDocumentDate() + "-" + transaction.getDocumentTime() + ".pdf";
        }
        else{
            directoryDownload = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Alimentare-" + transaction.getDocumentDate() + "-" + transaction.getDocumentTime() + ".pdf";
        }


        PdfDocument document = new PdfDocument();

        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(300,600,1).create();
        PdfDocument.Page myPage = document.startPage(myPageInfo);

        Paint myPaint = new Paint();
        int x = 10, y=25;

        myPage.getCanvas().drawText(transaction.getAmount() + " MDL", x, y, myPaint);

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
                "edi.md.mydesign.provider", //(use your app signature + ".provider" )
                myFile);

        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.setType("application/pdf");
        share.putExtra(Intent.EXTRA_STREAM, uri);

        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        share.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        Intent shareIntent = Intent.createChooser(share, "Share document...");

        startActivity(shareIntent);
    }
}