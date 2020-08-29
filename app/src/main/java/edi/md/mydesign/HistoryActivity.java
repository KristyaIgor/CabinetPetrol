package edi.md.mydesign;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import edi.md.mydesign.adapters.AllTransactionListAdapter;
import edi.md.mydesign.bottomsheet.FilterTransactionBottomSheetDialog;
import edi.md.mydesign.models.FilterTransactions;
import edi.md.mydesign.remote.contract.CardsList;
import edi.md.mydesign.remote.transaction.Transaction;
import edi.md.mydesign.utils.BaseEnum;
import edi.md.mydesign.utils.RecyclerItemClickListener;

public class HistoryActivity extends AppCompatActivity {
    ImageButton btnBack,btnFilter;

    static FilterTransactionBottomSheetDialog filterForm;

    static RecyclerView recyclerViewTransaction;

    static AllTransactionListAdapter adapter = null;
    static List<Transaction> listTransactions = new ArrayList<>();

    static Context context;

    @SuppressLint("SimpleDateFormat")
    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
    TimeZone timeZone = TimeZone.getTimeZone("Europe/Chisinau");

    static  int currentDate, firstDateOfWeek, lastDateOfWeek, currentMonth, currentYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = getWindow().getDecorView();
        Window window = getWindow();
        view.setSystemUiVisibility(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        view.setFitsSystemWindows(true);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.green));

        setContentView(R.layout.activity_history);

        btnBack = findViewById(R.id.image_back_transaction);
        btnFilter = findViewById(R.id.image_filter_transaction);
        recyclerViewTransaction = findViewById(R.id.recyclerView_history_transaction);

        LinearLayoutManager layoutManagerV = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerViewTransaction.setLayoutManager(layoutManagerV);

        context = this;
        simpleDateFormat.setTimeZone(timeZone);

        Intent intent = getIntent();
        boolean accountCard = intent.getBooleanExtra("CardAccount", false);

        if(accountCard)
            listTransactions = BaseApp.getAppInstance().getCardAccount().getTransactions();
        else
            listTransactions = BaseApp.getAppInstance().getTransactions();

        Calendar cal = Calendar.getInstance();
        TimeZone timeZone = TimeZone.getTimeZone("Europe/Chisinau");
        cal.setTimeZone(timeZone);

        currentDate = cal.get(Calendar.DATE);
        //set first day of week
        cal.set(Calendar.DAY_OF_WEEK, 1);

        firstDateOfWeek = cal.get(Calendar.DAY_OF_MONTH) + 1;
        //set last day of week
        cal.set(Calendar.DAY_OF_WEEK, 7);

        lastDateOfWeek = cal.get(Calendar.DAY_OF_MONTH) + 1;
        currentMonth = cal.get(Calendar.MONTH) +1;
        currentYear = cal.get(Calendar.YEAR);

        onFilterList(BaseEnum.chipAll, BaseEnum.chipAllPeriod);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterForm = FilterTransactionBottomSheetDialog.newInstance();
                filterForm.show(getSupportFragmentManager(), FilterTransactionBottomSheetDialog.TAG);
            }
        });

        recyclerViewTransaction.addOnItemTouchListener(
                new RecyclerItemClickListener(context , recyclerViewTransaction,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Transaction item = adapter.getItem(position);
                        BaseApp.getAppInstance().setClickedTransaction(item);

                        startActivity(new Intent(context, HistoryDetailActivity.class));
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        getSharedPreferences("CHIP SELECTED", Context.MODE_PRIVATE).edit().clear().apply();
    }

    public static void onFilterList(int typeTransaction, int period){
       List<Transaction> filterList = new ArrayList<>();

       if(listTransactions != null && listTransactions.size() > 0){
           for(int i = 0; i < listTransactions.size(); i++){
               Transaction item = listTransactions.get(i);
               double amount = item.getAmount();

               boolean alimentare = false;
               boolean suplinire = false;

               if(amount < 0)
                   alimentare = true;
               else
                   suplinire = true;

               Date dateItem = null;
               try {
                   dateItem = simpleDateFormat.parse(item.getDocumentDate());
               } catch (ParseException e) {
                   dateItem = new Date();
               }

               long timTR = dateItem.getTime();
               Calendar calendar = Calendar.getInstance();
               calendar.setTimeInMillis(timTR);

               int dateTransaction = calendar.get(Calendar.DATE);
               int monthTransaction = calendar.get(Calendar.MONTH) + 1;
               int yearTransaction = calendar.get(Calendar.YEAR);

               if (typeTransaction == BaseEnum.chipAll){   //filter toate
                   if (period == BaseEnum.chipDay){
                       if (dateTransaction == currentDate && monthTransaction == currentMonth && yearTransaction == currentYear)
                           filterList.add(item);
                   }
                   else if (period == BaseEnum.chipWeek){
                       if (dateTransaction >= firstDateOfWeek && dateTransaction <= lastDateOfWeek && monthTransaction == currentMonth && yearTransaction == currentYear)
                           filterList.add(item);
                   }
                   else if (period == BaseEnum.chipMonth){
                       if (monthTransaction == currentMonth && yearTransaction == currentYear)
                           filterList.add(item);
                   }
                   else if (period == BaseEnum.chipYear){
                       if (yearTransaction == currentYear)
                           filterList.add(item);
                   }
                   else if (period == BaseEnum.chipAllPeriod){
                       filterList.add(item);
                   }
               }
               else if (typeTransaction == BaseEnum.chipSuplinire){    // filter suplinire
                   if (period == BaseEnum.chipDay){
                       if (dateTransaction == currentDate && monthTransaction == currentMonth && yearTransaction == currentYear && suplinire)
                           filterList.add(item);
                   }
                   else if (period == BaseEnum.chipWeek){
                       if (dateTransaction >= firstDateOfWeek && dateTransaction <= lastDateOfWeek && monthTransaction == currentMonth && yearTransaction == currentYear && suplinire)
                           filterList.add(item);
                   }
                   else if (period == BaseEnum.chipMonth){
                       if (monthTransaction == currentMonth && yearTransaction == currentYear && suplinire)
                           filterList.add(item);
                   }
                   else if (period == BaseEnum.chipYear){
                       if (yearTransaction == currentYear && suplinire)
                           filterList.add(item);
                   }
                   else if (period == BaseEnum.chipAllPeriod && suplinire){
                       filterList.add(item);
                   }
               }
               else if( typeTransaction == BaseEnum.chipAlimentare){    //filter alimentare
                   if (period == BaseEnum.chipDay){
                       if (dateTransaction == currentDate && monthTransaction == currentMonth && yearTransaction == currentYear && alimentare)
                           filterList.add(item);
                   }
                   else if (period == BaseEnum.chipWeek){
                       if (dateTransaction >= firstDateOfWeek && dateTransaction <= lastDateOfWeek && monthTransaction == currentMonth && yearTransaction == currentYear && alimentare)
                           filterList.add(item);
                   }
                   else if (period == BaseEnum.chipMonth){
                       if (monthTransaction == currentMonth && yearTransaction == currentYear && alimentare)
                           filterList.add(item);
                   }
                   else if (period == BaseEnum.chipYear){
                       if (yearTransaction == currentYear && alimentare)
                           filterList.add(item);
                   }
                   else if (period == BaseEnum.chipAllPeriod && alimentare){
                       filterList.add(item);
                   }
               }
           }

           adapter = new AllTransactionListAdapter(context, filterList);
           recyclerViewTransaction.setAdapter(adapter);
       }
       else{
           //TODO if list empty
       }
    }
}
