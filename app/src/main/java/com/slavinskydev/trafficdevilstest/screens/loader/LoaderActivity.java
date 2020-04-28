package com.slavinskydev.trafficdevilstest.screens.loader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.slavinskydev.trafficdevilstest.R;
import com.slavinskydev.trafficdevilstest.screens.game.GameActivity;
import com.slavinskydev.trafficdevilstest.screens.webview.WebViewActivity;

public class LoaderActivity extends AppCompatActivity implements LoaderView {

    private LoaderPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new LoaderPresenter(getApplicationContext(), SharedPreferencesManager.getInstance(getApplicationContext()));
        presenter.loadData();

    }

    @Override
    public void showGameData(Context context) {
        Intent intent = new Intent(context, GameActivity.class);
        context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    @Override
    public void showWebData(Context context) {
        Intent intent = new Intent(context, WebViewActivity.class);
        context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    @Override
    public void showServerError(Task task) {
        Toast.makeText(LoaderActivity.this, "Error " + task.getException(), Toast.LENGTH_SHORT).show();
    }

}
