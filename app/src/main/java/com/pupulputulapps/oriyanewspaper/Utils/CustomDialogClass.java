package com.pupulputulapps.oriyanewspaper.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.pupulputulapps.oriyanewspaper.R;

public class CustomDialogClass extends Dialog implements
        android.view.View.OnClickListener {

    public TextView ePaper, website;

    public CustomDialogClass(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.news_selection_dialog);
        ePaper = findViewById(R.id.selectEpaper);
        website =  findViewById(R.id.selectWebsite);
        ePaper.setOnClickListener(this);
        website.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        dismiss();
    }


}