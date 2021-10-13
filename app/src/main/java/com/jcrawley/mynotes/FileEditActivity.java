package com.jcrawley.mynotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
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
import com.jcrawley.mynotes.viewModel.EditDocumentViewModel;

import java.util.List;

public class FileEditActivity extends AppCompatActivity implements CustomDialogCloseListener {

    private DocumentLinesRepository documentLinesRepository;
    private long documentId;
    private ListAdapterHelper listAdapterHelper;
    private EditDocumentViewModel viewModel;

    FileHandler fileHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_edit);
        setTitle(getIntent().getStringExtra(FilesListActivity.DOCUMENT_NAME_TAG));
        fileHandler = new FileHandler(this);
        setupLineEditText();
        viewModel = new ViewModelProvider(this).get(EditDocumentViewModel.class);
        setupDialogDimensions();
        documentLinesRepository = new DocumentLinesRepositoryImpl(this);
        assignDocumentId();
        setupList();
        setupInputText();
        refreshListFromDb();
    }


    public void setupLineEditText(){
        EditText lineEditText = findViewById(R.id.editTextFileContents);
        lineEditText.setText(fileHandler.getFileContents());
    }


    public void assignDocumentId(){
        Intent intent = getIntent();
        documentId = intent.getLongExtra(FilesListActivity.DOCUMENT_ID_TAG, -1);
    }


     private void setupList(){
         ListView documentLinesList = findViewById(R.id.documentLinesList);
         listAdapterHelper = new ListAdapterHelper(this, documentLinesList,
                 listItem -> viewModel.selectedListItem = listItem,
                 listItem -> {});
     }


    public void setupDialogDimensions(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        viewModel.dialogWidth =(int)(displayMetrics.widthPixels /1.5f);
        viewModel.dialogHeight=(int)(displayMetrics.heightPixels / 2f);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_document, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_edit_line){
            startDialogFragment();
        }
        else if(id == R.id.action_delete_selected_line){
            deleteCurrentlySelectedLine();
        }
        return true;
    }


    @Override
    public void handleDialogClose(DialogInterface dialogInterface, String updatedLine) {
        listAdapterHelper.notifyChanges();
        documentLinesRepository.update(viewModel.selectedListItem.getId(), documentId, updatedLine);
    }


    private void deleteCurrentlySelectedLine(){
        if(viewModel.selectedListItem == null){
            return;
        }
        documentLinesRepository.delete(viewModel.selectedListItem.getId());
        listAdapterHelper.deleteFromList(viewModel.selectedListItem);
        listAdapterHelper.clearSelection();
    }


    private void setupInputText(){
        EditText fileContentsEditText = findViewById(R.id.editTextFileContents);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        fileContentsEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                String contents = v.getText().toString().trim();
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


    private void startDialogFragment(){
        if(viewModel.selectedListItem == null){
            return;
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        EditLineDialogFragment configureDialogFragment = EditLineDialogFragment.newInstance();

        configureDialogFragment.show(ft, "dialog");
    }


}