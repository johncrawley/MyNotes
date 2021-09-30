package com.jcrawley.mynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;

import com.jcrawley.mynotes.list.ListAdapterHelper;
import com.jcrawley.mynotes.list.ListItem;
import com.jcrawley.mynotes.repository.CategoryRepository;
import com.jcrawley.mynotes.repository.CategoryRepositoryImpl;

import java.util.List;

public class MainActivity extends AppCompatActivity {


    private CategoryRepository categoryRepository;
    private ListAdapterHelper listAdapterHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        categoryRepository = new CategoryRepositoryImpl(this);
        ListView categoryList = findViewById(R.id.categoryList);
        listAdapterHelper = new ListAdapterHelper(this, categoryList,
                listItem -> {},
                listItem -> {});
        setupInputText();
    }


    private void setupInputText(){
        EditText addCategoryEditText = findViewById(R.id.addCategoryEditText);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        addCategoryEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                String categoryName = v.getText().toString().trim();
                long id = categoryRepository.create(categoryName);
                listAdapterHelper.addToList(new ListItem(categoryName, id));
                addCategoryEditText.getText().clear();
                imm.hideSoftInputFromWindow(addCategoryEditText.getWindowToken(), 0);
                return true;
            }
            return false;
        });
    }



    public void refreshListFromDb(){
        List<ListItem> items = categoryRepository.getCategories();
        listAdapterHelper.setupList(items, android.R.layout.simple_list_item_1, findViewById(R.id.noResultsFoundText));
    }


}