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
import com.jcrawley.mynotes.repository.DocumentLinesRepository;
import com.jcrawley.mynotes.repository.DocumentLinesRepositoryImpl;
import com.jcrawley.mynotes.repository.FileHandler;

import java.util.List;

public class FileEditActivity extends AppCompatActivity {


    private DocumentLinesRepository documentLinesRepository;
    private long documentId;
    private ListAdapterHelper listAdapterHelper;
    private long currentDocumentId;
    private ListItem selectedListItem;
    private EditText lineEditText;
    private ListView documentLinesList;

    FileHandler fileHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_edit);
        fileHandler = new FileHandler(this);
        lineEditText = findViewById(R.id.editTextFileContents);
        lineEditText.setText(fileHandler.getFileContents());

        documentLinesRepository = new DocumentLinesRepositoryImpl(this);
        Intent intent = getIntent();
        documentLinesList = findViewById(R.id.documentLinesList);
        documentId = intent.getLongExtra(FilesListActivity.DOCUMENT_ID_TAG, -1);
        listAdapterHelper = new ListAdapterHelper(this, documentLinesList,
                listItem -> {
                    selectedListItem = listItem;
                    lineEditText.setText(listItem.getName());
                },
                listItem -> {});

        setupInputText();
        refreshListFromDb();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_document, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_new_line){

        }
        else if(id == R.id.action_delete_selected_line){
            deleteCurrentlySelectedLine();
        }
        return true;
    }


    private void deleteCurrentlySelectedLine(){
        if(selectedListItem == null){
            return;
        }
        documentLinesRepository.delete(selectedListItem.getId());
        listAdapterHelper.deleteFromList(selectedListItem);
        listAdapterHelper.clearSelection();
    }


    private void setupInputText(){
        EditText fileContentsEditText = findViewById(R.id.editTextFileContents);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        fileContentsEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                String contents = v.getText().toString().trim();
                //fileHandler.writeFileOnInternalStorage(testCategoryName, testFileName, contents);
                //  fileHandler.saveFileContents();
                documentLinesRepository.add(contents, documentId);
                listAdapterHelper.addToList(new ListItem(contents, documentId));
                imm.hideSoftInputFromWindow(fileContentsEditText.getWindowToken(), 0);
                return true;
            }
            return false;
        });
    }


    public void refreshListFromDb(){
        List<ListItem> items = documentLinesRepository.getDocumentLines(documentId);
        for(ListItem item : items){
            System.out.println("Item :" +  item.getId() + "  "  + item.getName());
        }
        listAdapterHelper.setupList(items, android.R.layout.simple_list_item_1, findViewById(R.id.noResultsFoundText));
    }




}