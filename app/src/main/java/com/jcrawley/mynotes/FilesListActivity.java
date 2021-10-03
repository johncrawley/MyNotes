package com.jcrawley.mynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;

import com.jcrawley.mynotes.list.ListAdapterHelper;
import com.jcrawley.mynotes.list.ListItem;
import com.jcrawley.mynotes.repository.FileRepository;
import com.jcrawley.mynotes.repository.FileRepositoryImpl;

import java.util.List;

public class FilesListActivity extends AppCompatActivity {

    private FileRepository fileRepository;
    private long categoryId;
    private String categoryName;
    private ListAdapterHelper listAdapterHelper;

    public final static String DOCUMENT_NAME_TAG = "documentName";
    public final static String DOCUMENT_ID_TAG =  "documentId";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_files_list);
        Intent intent = getIntent();
        String categoryName = intent.getStringExtra(MainActivity.CATEGORY_NAME_TAG);
        categoryId = intent.getLongExtra(MainActivity.CATEGORY_ID_TAG, -1);
        fileRepository = new FileRepositoryImpl(this);
        ListView categoryList = findViewById(R.id.categoryList);
        listAdapterHelper = new ListAdapterHelper(this, categoryList,
                listItem -> {
                    Intent editFileIntent = new Intent(this,FileEditActivity.class);
                    editFileIntent.putExtra(DOCUMENT_NAME_TAG, listItem.getName());
                    editFileIntent.putExtra(DOCUMENT_ID_TAG, listItem.getId());
                    startActivity(editFileIntent);},
                listItem -> {});
        setupInputText();
        refreshListFromDb();
    }


    private void setupInputText(){
        EditText addCategoryEditText = findViewById(R.id.addFileEditText);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        addCategoryEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                String categoryName = v.getText().toString().trim();
                fileRepository.create(categoryName, categoryId);
                listAdapterHelper.addToList(new ListItem(categoryName, categoryId));
                addCategoryEditText.getText().clear();
                imm.hideSoftInputFromWindow(addCategoryEditText.getWindowToken(), 0);
                return true;
            }
            return false;
        });
    }


    public void refreshListFromDb(){
        List<ListItem> items = fileRepository.getFiles(categoryId);
        listAdapterHelper.setupList(items, android.R.layout.simple_list_item_1, findViewById(R.id.noResultsFoundText));
    }

}