package com.jcrawley.mynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.jcrawley.mynotes.repository.CategoryRepository;
import com.jcrawley.mynotes.repository.CategoryRepositoryImpl;

public class MainActivity extends AppCompatActivity {


    private CategoryRepository categoryRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        categoryRepository = new CategoryRepositoryImpl(this);
        setupInputText();
    }


    private void setupInputText(){
        EditText addCategoryEditText = findViewById(R.id.addCategoryEditText);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        addCategoryEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                String categoryName = v.getText().toString().trim();
                categoryRepository.create(categoryName);
                addCategoryEditText.getText().clear();
                imm.hideSoftInputFromWindow(addCategoryEditText.getWindowToken(), 0);
                return true;
            }
            return false;
        });
    }


}