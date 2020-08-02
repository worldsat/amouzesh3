package com.makancompany.assistant.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.makancompany.assistant.Adapter.ShowItemListAdapter;
import com.makancompany.assistant.Domain.Question;
import com.makancompany.assistant.Interface.OnClick;
import com.makancompany.assistant.R;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends BaseActivity {
    private EditText searchEdt;
    private ImageView searchIcon;
    private RecyclerView recyclerViewlist;
    private RecyclerView.Adapter adapter;
    //    private ArrayList<PersonelTask> response = new ArrayList<>();
    private int SKIP = 0;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        initView();
        setVariable();
        getDataApi(question1(), false);
    }

    private void setVariable() {
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataApi(question1(), false);
            }
        });
    }

    private void getDataApi(  ArrayList<Question> array_object, boolean notify) {
//
//        PersonelTaskListBll personelTaskListBll = new PersonelTaskListBll(this);
//
//        ArrayList<Filter> filter = new ArrayList<>();
//
//
//
//
//
//        filter.add(new Filter("PersonelTaskId", getIntent().getStringExtra("PersonelTaskId")));
//
//
//        warningTxt.setVisibility(View.GONE);
//        progressBar.setVisibility(View.VISIBLE);
//        personelTaskListBll.Get(filter, TAKE, SKIP, false, new CallbackGet() {
//            @Override
//            public <T> void onSuccess(ArrayList<T> result, int count) {
//
//
//                int previousResponseSize = SKIP;
//                if (notify) {
//                    response.clear();
//                }
//                response.addAll((Collection<? extends PersonelTask>) result);
//
//                SKIP = response.size();
//
////                row.setVisibility(View.VISIBLE);
////                String Str = count + " ردیف ";
////                row.setText(Str);
//
//                // check if the server response if not error if so show a text
//                if (response.size() == 0) {
//                    warningTxt.setVisibility(View.VISIBLE);
//                } else {
//                    warningTxt.setVisibility(View.GONE);
//                    if (notify) {
//                        progressBar.setVisibility(View.GONE);
//                        adapter.notifyDataSetChanged();
//                        return;
//                    }
//                    // check if the response count is less or equal to the total available data
//                    // remove the scroll listener
//                    if (count != -1 && SKIP == count) {
//                        recyclerViewlist.clearOnScrollListeners();
//                        isScrollListenerAdded = false;
//                    }
//                    if (adapter == null) {
//                        recyclerViewlist.setVisibility(View.VISIBLE);
//
//        ArrayList<String> array_object = new ArrayList<>();
//        array_object.add("مایل به خرید کدام یک از موارد زیر می باشید؟");
//        array_object.add("پوشاک");
//        array_object.add("لوازم دیجیتال");
//        array_object.add("خودرو");
//        array_object.add("مواد غذایی");

        adapter = new ShowItemListAdapter(array_object, new OnClick() {
            @Override
            public void clicked(String type) {
                if (type.equals("question1")) {
                    getDataApi(question2(), false);
                }
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewlist.setLayoutManager(linearLayoutManager);

        recyclerViewlist.setAdapter(adapter);
    }


    private void initView() {
        searchEdt = findViewById(R.id.searchEdt);
        searchIcon = findViewById(R.id.searchIcon);
        recyclerViewlist = findViewById(R.id.view);
        progressBar = findViewById(R.id.progressBarRow);

    }

    private ArrayList<Question> question1() {

        ArrayList<Question> array_object = new ArrayList<>();
        array_object.add(new Question("آیا قصد خرید دارید؟", "1", "question1"));
        array_object.add(new Question("آیا قصد گشت و گذار دارید؟", "1", "question1"));
        array_object.add(new Question("آیا سوالی دارید؟", "1", "question1"));
        array_object.add(new Question("آیا قصد ثبت یاداوری دارید؟", "1", "question1"));

        return array_object;
    }

    private ArrayList<Question> question2() {

        ArrayList<Question> array_object = new ArrayList<>();
        array_object.add(new Question("چه جنسی؟", "2", "question2"));

        return array_object;
    }
}