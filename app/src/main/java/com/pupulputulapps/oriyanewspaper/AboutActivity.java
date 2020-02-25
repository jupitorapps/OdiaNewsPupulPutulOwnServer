package com.pupulputulapps.oriyanewspaper;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class AboutActivity extends AppCompatActivity {


    private static final String TAG = "AboutActivity TAGG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle("About App");

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    public void sendMail(View view) {
        composeEmail(getResources().getString(R.string.mail_address), getResources().getString(R.string.mail_subject));
    }

    public void shareApp(View view) {
        final String appUrl = "https://play.google.com/store/apps/details?id=" + getPackageName();
        final Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, appUrl + "\n\n"+getString(R.string.share_app_message));

        try {
            startActivity(Intent.createChooser(intent, "Select an action"));
        } catch (android.content.ActivityNotFoundException ex) {
            // (handle error)
        }
    }

    public void rateApp(View view) {
        final String appUrl = "https://play.google.com/store/apps/details?id=" + getPackageName();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(appUrl));
        //  intent.setPackage("com.android.vending");
        startActivity(intent);

    }

    private void composeEmail(String addresse, String subject) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);

      //  intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{addresse});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void goToPrivacyPolicy(View view) {
        Intent intent = new Intent(this, NewsAdvancedWebViewActivity.class);
        intent.putExtra("url", getString(R.string.privacy_policy_url));
        intent.putExtra("paper_name", "Privacy Policy");
        startActivity(intent);
    }
}
