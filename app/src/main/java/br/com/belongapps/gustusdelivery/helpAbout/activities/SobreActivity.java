package br.com.belongapps.gustusdelivery.helpAbout.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import br.com.belongapps.gustusdelivery.R;
import br.com.belongapps.gustusdelivery.cardapioOnline.activitys.CardapioMainActivity;

public class SobreActivity extends AppCompatActivity {

    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_sobre);
        mToolbar.setTitle("Sobre o app");

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(SobreActivity.this, CardapioMainActivity.class);
        startActivity(intent);

        finish();
    }
}
