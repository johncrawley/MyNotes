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
import com.jcrawley.mynotes.repository.DocumentRepository;
import com.jcrawley.mynotes.repository.DocumentRepositoryImpl;

import java.util.List;

public class FilesListActivity extends AppCompatActivity {

    private DocumentRepository documentRepository;
    private long categoryId;
    private String categoryName;
    private ListAdapterHelper listAdapterHelper;
    public final static String DOCUMENT_NAME_TAG = "documentName";
    public final static String DOCUMENT_ID_TAG =  "documentId";
    long currentDocumentId = -1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_files_list);
        Intent intent = getIntent();
        String categoryName = intent.getStringExtra(MainActivity.CATEGORY_NAME_TAG);
        categoryId = intent.getLongExtra(MainActivity.CATEGORY_ID_TAG, -1);
        documentRepository = new DocumentRepositoryImpl(this);
        ListView categoryList = findViewById(R.id.categoryList);
        listAdapterHelper = new ListAdapterHelper(this, categoryList,
                this::startEditActivityFor,
                listItem -> {
                    currentDocumentId = listItem.getId();
                });
        setupInputText();
        refreshListFromDb();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.document, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_delete_selected_document){
            deleteCurrentlySelectedDocument();
        }
        return true;
    }


    private void startEditActivityFor(ListItem listItem){
        Intent editFileIntent = new Intent(this, FileEditActivity.class);
        editFileIntent.putExtra(DOCUMENT_NAME_TAG, listItem.getName());
        editFileIntent.putExtra(DOCUMENT_ID_TAG, listItem.getId());
        startActivity(editFileIntent);
    }


    private void deleteCurrentlySelectedDocument(){
        if(currentDocumentId != -1){
            documentRepository.delete(currentDocumentId);
            refreshListFromDb();
        }
    }


    private void setupInputText(){
        EditText addCategoryEditText = findViewById(R.id.addFileEditText);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        addCategoryEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                String categoryName = v.getText().toString().trim();
                long docId = documentRepository.create(categoryName, categoryId);
                if(docId != -1) {
                    listAdapterHelper.addToList(new ListItem(categoryName, docId));
                    addCategoryEditText.getText().clear();
                }
                imm.hideSoftInputFromWindow(addCategoryEditText.getWindowToken(), 0);
                return true;
            }
            return false;
        });
    }


    public void refreshListFromDb(){
        List<ListItem> items = documentRepository.getDocuments(categoryId);
        listAdapterHelper.setupList(items, android.R.layout.simple_list_item_1, findViewById(R.id.noResultsFoundText));
    }

}