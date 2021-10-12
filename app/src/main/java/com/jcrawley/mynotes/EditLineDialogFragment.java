package com.jcrawley.mynotes;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.jcrawley.mynotes.list.ListItem;
import com.jcrawley.mynotes.viewModel.EditDocumentViewModel;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

public class EditLineDialogFragment extends DialogFragment {

    EditText updateLineEditText;
    EditDocumentViewModel viewModel;
    private boolean hasTextChanged = false;

    static EditLineDialogFragment newInstance() {
        return new EditLineDialogFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.update_line_dialog, container, false);
        Dialog dialog =  getDialog();
        if(getActivity() == null){
            return rootView;
        }

        ViewModelProvider vmp = new ViewModelProvider(getActivity());
        viewModel = vmp.get(EditDocumentViewModel.class);

        getDialog().getWindow().setLayout(viewModel.dialogWidth, viewModel.dialogHeight);
        updateLineEditText = rootView.findViewById(R.id.updateLineEditText);
        updateLineEditText.setText(viewModel.selectedListItem.getName());
        setupInputText();

        if(dialog != null){
            dialog.setTitle("Edit Line");
        }
        return rootView;
    }


    public void onResume(){
        super.onResume();
        if(viewModel == null || viewModel.selectedListItem == null){
            log("onResume() viewModel or selectedItem is null!");
            return;
        }
        updateLineEditText.setText(viewModel.selectedListItem.getName());
    }


    private void log(String msg){
        System.out.println("^^^ EditLineDialogFragment: " + msg);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    public void onDismiss(@NonNull DialogInterface dialog){
        Activity activity = getActivity();
        if(hasTextChanged) {
            viewModel.selectedListItem.setName(updateLineEditText.getText().toString());
        }
        hasTextChanged = false;
        if(activity instanceof CustomDialogCloseListener) {
            ((CustomDialogCloseListener) activity).handleDialogClose(dialog, updateLineEditText.getText().toString());
        }
        super.onDismiss(dialog);
    }


    private void setupInputText(){
        if (getActivity() == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        updateLineEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hasTextChanged = true;
                dismiss();
                imm.hideSoftInputFromWindow(updateLineEditText.getWindowToken(), 0);
                return true;
            }
            return false;
        });
    }

}