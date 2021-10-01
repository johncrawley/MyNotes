package com.jcrawley.mynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.jcrawley.mynotes.list.ListItem;
import com.jcrawley.mynotes.repository.FileHandler;

public class FileEditActivity extends AppCompatActivity {


    private final String testCategoryName="testCategory";
    private final String testFileName= "test";

    FileHandler fileHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_edit);
        fileHandler = new FileHandler(this);
        String contents = fileHandler.readFileFromInternalStorage(testCategoryName, testFileName);
        EditText editText = findViewById(R.id.editTextFileContents);
        editText.setText(fileHandler.getFileContents());
        setupInputText();
    }


    private void setupInputText(){
        EditText fileContentsEditText = findViewById(R.id.editTextFileContents);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        fileContentsEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                String contents = v.getText().toString().trim();
                //fileHandler.writeFileOnInternalStorage(testCategoryName, testFileName, contents);
                fileHandler.saveFileContents();
                imm.hideSoftInputFromWindow(fileContentsEditText.getWindowToken(), 0);
                return true;
            }
            return false;
        });
    }

}