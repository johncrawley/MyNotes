package com.jcrawley.mynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
    public final static String CATEGORY_NAME_TAG = "categoryName";
    public final static String CATEGORY_ID_TAG =  "categoryId";
    private long selectedCategoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        categoryRepository = new CategoryRepositoryImpl(this);
        ListView categoryList = findViewById(R.id.categoryList);
        listAdapterHelper = new ListAdapterHelper(this, categoryList,
                listItem -> {
                    Intent intent = new Intent(this, FilesListActivity.class);
                    intent.putExtra(CATEGORY_NAME_TAG, listItem.getName());
                    intent.putExtra(CATEGORY_ID_TAG, listItem.getId());
                    startActivity(intent);},
                listItem -> {
                    selectedCategoryId = listItem.getId();
                });
        setupInputText();
        refreshListFromDb();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.category, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_delete_selected_category){
            deleteCurrentlySelectedCategory();
        }
        return true;
    }


    public void deleteCurrentlySelectedCategory(){
        if(selectedCategoryId != -1){
            categoryRepository.delete(selectedCategoryId);
            refreshListFromDb();
        }
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