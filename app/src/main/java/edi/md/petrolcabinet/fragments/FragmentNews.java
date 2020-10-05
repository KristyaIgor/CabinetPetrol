package edi.md.petrolcabinet.fragments;


import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.text.Layout;
import android.text.SpannableString;
import android.text.style.AlignmentSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edi.md.petrolcabinet.DetailNewsActivity;
import edi.md.petrolcabinet.R;
import edi.md.petrolcabinet.adapters.PressListAdapter;
import edi.md.petrolcabinet.realm.objects.Company;
import edi.md.petrolcabinet.realm.objects.PressObjects;
import edi.md.petrolcabinet.remote.press.PressList;
import edi.md.petrolcabinet.remote.press.PressResponse;
import edi.md.petrolcabinet.remoteSettings.ApiUtils;
import edi.md.petrolcabinet.remoteSettings.CommandServices;
import edi.md.petrolcabinet.utils.RecyclerItemClickListener;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Igor on 30.06.2020
 */

public class FragmentNews extends Fragment {

    RecyclerView recyclerView;
//    Spinner companySpinner;
    List<Company> companyList = new ArrayList<>();
    Realm mRealm;

    RealmResults<PressObjects> results;

    PressListAdapter adapter;

    ImageView imgArrow;
    TextView textCompanyName;
    ConstraintLayout layoutMenu;

    boolean menuShow = false;

    View backgroundDim;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootViewAdmin = inflater.inflate(R.layout.fragment_news_v1, container, false);

        recyclerView = rootViewAdmin.findViewById(R.id.list_news);
//        companySpinner = rootViewAdmin.findViewById(R.id.spinner_news);

        layoutMenu = rootViewAdmin.findViewById(R.id.csl_filtering);
        textCompanyName = rootViewAdmin.findViewById(R.id.text_selected_menu_item);
        imgArrow = rootViewAdmin.findViewById(R.id.image_arrow_menu);
        backgroundDim = rootViewAdmin.findViewById(R.id.view_background_news);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mRealm = Realm.getDefaultInstance();

        Context wrapper = new ContextThemeWrapper(getContext(), R.style.PopupMenuTheme);
        PopupMenu popup = new PopupMenu(wrapper, layoutMenu, Gravity.CENTER);
        SpannableString s = new SpannableString(getString(R.string.all_company_news));

        s.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, s.length(), 0);
        s.setSpan(new RelativeSizeSpan(1.4f), 0, s.length(), 0);
        s.setSpan(new StyleSpan(Typeface.BOLD), 0, s.length(), 0);
        popup.getMenu().add(s).setNumericShortcut((char) 0);

        textCompanyName.setText(getString(R.string.all_company_news));

        TransitionDrawable transition = (TransitionDrawable) backgroundDim.getBackground();

        RealmResults<Company> companyRealmResults = mRealm.where(Company.class).equalTo("active",true).findAll();
        if(!companyRealmResults.isEmpty()){
            companyList.addAll(mRealm.copyFromRealm(companyRealmResults));

            List<String> companyName = new ArrayList<>();
            companyName.add(getString(R.string.all_company_news));

            for(Company company : companyList){
                companyName.add(company.getName());

                SpannableString s2 = new SpannableString(company.getName());

                s2.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, s2.length(), 0);
                s2.setSpan(new RelativeSizeSpan(1.4f), 0, s2.length(), 0);
                s2.setSpan(new StyleSpan(Typeface.BOLD), 0, s2.length(), 0);

                popup.getMenu().add(s2);
            }
        }

        results = mRealm.where(PressObjects.class).sort("dateTime", Sort.DESCENDING).findAll();
        adapter = new PressListAdapter(results,true);
        recyclerView.setAdapter(adapter);


        for(Company company: companyList){

            CommandServices commandServices = ApiUtils.getCommandServices(company.getIp());
            Call<PressResponse> call = commandServices.getPress(company.getServiceName(),company.getIdPress(),0);

            enqueueCall(call, company);
        }

        layoutMenu.setOnClickListener(view -> {
            if(!menuShow){
                backgroundDim.setVisibility(View.VISIBLE);

                transition.startTransition(300);

                ObjectAnimator rotate = ObjectAnimator.ofFloat(imgArrow, "rotation", 0f, 180f);
                rotate.setDuration(300);
                rotate.start();

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        String itemName = String.valueOf(item.getTitle());

                        textCompanyName.setText(itemName);

                        for(Company cpny : companyList){
                            String cmpnyName = cpny.getName();

                            if(itemName.equals(cmpnyName)){
                                results = mRealm.where(PressObjects.class).sort("dateTime", Sort.DESCENDING).equalTo("companyId", cpny.getId()).findAll();
                                adapter = new PressListAdapter(results,true);
                                recyclerView.setAdapter(adapter);

//                                CommandServices commandServices = ApiUtils.getCommandServices(cpny.getIp());
//                                Call<PressResponse> call = commandServices.getPress(cpny.getServiceName(),cpny.getIdPress(),0);
//
//                                enqueueCall(call, cpny);
                                break;
                            }
                            else if(itemName.equals(getString(R.string.all_company_news))){
                                results = mRealm.where(PressObjects.class).sort("dateTime", Sort.DESCENDING).findAll();
                                adapter = new PressListAdapter(results,true);
                                recyclerView.setAdapter(adapter);


                                for(Company company: companyList){

                                    CommandServices commandServices = ApiUtils.getCommandServices(company.getIp());
                                    Call<PressResponse> call = commandServices.getPress(company.getServiceName(),company.getIdPress(),0);

                                    enqueueCall(call, company);
                                }

                                Toast.makeText(getContext(), "Selected company: " + itemName, Toast.LENGTH_SHORT).show();

                                break;
                            }
                        }
                        Toast.makeText(getContext(), "Selected company: " + itemName, Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
                popup.show();

                menuShow = true;
            }
            else{
                popup.dismiss();
            }
        });

        popup.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                backgroundDim.setVisibility(View.GONE);

                transition.reverseTransition(300);

                ObjectAnimator rotate = ObjectAnimator.ofFloat(imgArrow, "rotation", 180f, 0f);
                rotate.setDuration(300);
                rotate.start();

                menuShow = false;
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                PressObjects press = adapter.getItem(position);
                String comId = press.getCompanyId();
                int idPress = press.getId();

                Intent intent = new Intent(getContext(), DetailNewsActivity.class);
                intent.putExtra("id",idPress);
                intent.putExtra("companyId",comId);

                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {
            }
        }));

        return rootViewAdmin;
    }

    private void enqueueCall(Call<PressResponse> call, Company company) {
        call.enqueue(new Callback<PressResponse>() {
            @Override
            public void onResponse(Call<PressResponse> call, Response<PressResponse> response) {
                PressResponse pressResponse = response.body();
                if(pressResponse != null){
                    if(pressResponse.getErrorCode() == 0){
                        List<PressList> pressList = pressResponse.getPressList();

                        if(pressList.size() > 0){
                            int idPress = company.getIdPress();
                            for(PressList press: pressList){
                                int idReceivPress = press.getID();
                                if(idReceivPress > idPress)
                                    idPress = idReceivPress;

                                PressObjects object = new PressObjects();

                                object.setCompanyId(company.getId());
                                object.setCompanyName(company.getName());
                                object.setCompanyLogo(company.getLogo());
                                object.setContent(press.getContent());
                                object.setHeader(press.getHeader());
                                object.setImage(press.getPicture());
                                object.setId(press.getID());
                                object.setType(press.getPressType());
                                String time = press.getDateTime();
                                time = time.replace("/Date(","");
                                time = time.replace("+0300)/","");
                                time = time.replace("+0200)/","");

                                long timeLong = Long.parseLong(time);
                                object.setDateTime(timeLong);

                                mRealm.executeTransaction(realm -> {
                                    realm.insert(object);
                                });
                            }
                            int finalIdPress = idPress;
                            mRealm.executeTransaction(realm -> {
                                Company com = realm.where(Company.class).equalTo("id",company.getId()).findFirst();
                                if(com != null)
                                    com.setIdPress(finalIdPress);
                            });


//                            PressList item = pressList.get(0);
//                            for (int i = 0; i < 10; i++){
//                                pressList.add(item);
//                            }
                        }
//                        NewsViewAdapter adapter = new NewsViewAdapter(getContext(),pressList);
//                        recyclerView.setAdapter(adapter);
                    }
                    else{
//                        String msg = RemoteException.getServiceException(pressResponse.getErrorCode());
//
//                        new MaterialAlertDialogBuilder(getContext(), R.style.MaterialAlertDialogCustom)
//                                .setTitle(getString(R.string.oops_text))
//                                .setMessage(company.getName() + " : " + msg)
//                                .setCancelable(false)
//                                .setPositiveButton(getString(R.string.dialog_button_understand) , (dialogInterface, i) -> {
//                                    dialogInterface.dismiss();
//                                })
//                                .show();
                    }
                }
                else{

                }
            }

            @Override
            public void onFailure(Call<PressResponse> call, Throwable t) {
//                new MaterialAlertDialogBuilder(getContext(), R.style.MaterialAlertDialogCustom)
//                        .setTitle(getString(R.string.oops_text)      )
//                        .setMessage(getString(R.string.dialog_failure_service) + t.getMessage())
//                        .setCancelable(false)
//                        .setPositiveButton(getString(R.string.dialog_button_understand) , (dialogInterface, i) -> {
//                            dialogInterface.dismiss();
//                        })
//                        .show();
            }
        });
    }
}
