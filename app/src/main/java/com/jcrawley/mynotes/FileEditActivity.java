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
import com.jcrawley.mynotes.repository.DocumentLinesRepository;
import com.jcrawley.mynotes.repository.DocumentLinesRepositoryImpl;
import com.jcrawley.mynotes.repository.FileHandler;

import java.util.List;

public class FileEditActivity extends AppCompatActivity {


    private final String testCategoryName="testCategory";
    private final String testFileName= "test";
    private DocumentLinesRepository documentLinesRepository;
    private long documentId;
    private ListAdapterHelper listAdapterHelper;
    private long currentDocumentId;

    FileHandler fileHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_edit);
        fileHandler = new FileHandler(this);
        String contents = fileHandler.readFileFromInternalStorage(testCategoryName, testFileName);
        documentLinesRepository = new DocumentLinesRepositoryImpl(this);
        Intent intent = getIntent();
        ListView documentLinesList = findViewById(R.id.documentLinesList);
        documentId = intent.getLongExtra(FilesListActivity.DOCUMENT_ID_TAG, -1);
        listAdapterHelper = new ListAdapterHelper(this, documentLinesList,
                listItem -> {},
                listItem -> {});



        EditText editText = findViewById(R.id.editTextFileContents);
        editText.setText(fileHandler.getFileContents());
        setupInputText();
        refreshListFromDb();
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