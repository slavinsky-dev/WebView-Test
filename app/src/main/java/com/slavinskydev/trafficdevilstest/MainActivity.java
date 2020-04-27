package com.slavinskydev.trafficdevilstest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.slavinskydev.trafficdevilstest.game.GameActivity;
import com.slavinskydev.trafficdevilstest.webview.WebViewActivity;

public class MainActivity extends AppCompatActivity {

    private static SharedPreferences sharedPreferencesFirstRun = null;
    private static boolean firstRun;
    private SharedPreferences sharedPreferencesNextRun = null;
    private boolean nextRun;

    private boolean condition = false;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = FirebaseFirestore.getInstance();

        sharedPreferencesFirstRun = getSharedPreferences("com.slavinskydev.trafficdevilstest", MODE_PRIVATE);
        sharedPreferencesNextRun = getSharedPreferences("com.slavinskydev.trafficdevilstest", MODE_PRIVATE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (sharedPreferencesFirstRun.getBoolean("firstrun", true)) {
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
                                    Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                                    startActivity(intent);
                                    nextRun = sharedPreferencesNextRun.edit().putBoolean("nextrun", true).commit();
                                } else {
                                    Intent intent = new Intent(MainActivity.this, GameActivity.class);
                                    startActivity(intent);
                                    nextRun = sharedPreferencesNextRun.edit().putBoolean("nextrun", false).commit();
                                }
                            } else {
                                Toast.makeText(MainActivity.this, "Error " + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            firstRun = sharedPreferencesFirstRun.edit().putBoolean("firstrun", false).commit();
        } else {
            nextRun = sharedPreferencesNextRun.getBoolean("nextrun", false);
            if (nextRun) {
                Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                startActivity(intent);
            }
        }
    }

    public static void firstRunPlease() {
        firstRun = sharedPreferencesFirstRun.edit().putBoolean("firstrun", true).commit();
    }
}
