package com.slavinskydev.trafficdevilstest.screens.loader;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.slavinskydev.trafficdevilstest.screens.game.GameActivity;
import com.slavinskydev.trafficdevilstest.screens.webview.WebViewActivity;

public class LoaderPresenter {

    private boolean condition = false;
    private FirebaseFirestore db;
    private LoaderView view;
    private Context context;
    private SharedPreferencesManager instance;

    public LoaderPresenter(LoaderView view) {
        this.view = view;
    }

    LoaderPresenter(Context context, SharedPreferencesManager instance) {
        this.context = context;
        this.instance = instance;
    }

    void loadData() {
        db = FirebaseFirestore.getInstance();

        if (SharedPreferencesManager.loadFirstRunPreferences()) {
            DocumentReference documentReference = db.collection("boolean response").document("4KOk3vdhbh8OFZ7aX3Js");
            documentReference.get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot documentSnapshot = task.getResult();
                                if (documentSnapshot == null) return;
                                condition = documentSnapshot.getBoolean("response");
                                if (condition) {
                                    //view.showWebData(context);
                                    Intent intent = new Intent(context, WebViewActivity.class);
                                    context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                    SharedPreferencesManager.setNextRunPreferences(true);
                                } else {
                                    //view.showGameData(context);
                                    Intent intent = new Intent(context, GameActivity.class);
                                    context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                    SharedPreferencesManager.setNextRunPreferences(false);
                                }
                            } else {
                                //view.showServerError(task);
                                Toast.makeText(context, "Error " + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            SharedPreferencesManager.setFirstRunPreferences();
        } else {
            if (SharedPreferencesManager.loadNextRunPreferences()) {
                //view.showWebData(context);
                Intent intent = new Intent(context, WebViewActivity.class);
                context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            } else {
                //view.showGameData();
                Intent intent = new Intent(context, GameActivity.class);
                context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        }
    }

}
