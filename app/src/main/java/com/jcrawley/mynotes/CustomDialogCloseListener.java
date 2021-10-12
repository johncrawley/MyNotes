package com.jcrawley.mynotes;

import android.content.DialogInterface;

public interface CustomDialogCloseListener {
    void handleDialogClose(DialogInterface dialogInterface, String updatedLine);
}
