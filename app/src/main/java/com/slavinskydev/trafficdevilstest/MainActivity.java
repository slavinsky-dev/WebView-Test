package com.slavinskydev.trafficdevilstest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.slavinskydev.trafficdevilstest.game.GameActivity;
import com.slavinskydev.trafficdevilstest.webview.WebViewActivity;

public class MainActivity extends AppCompatActivity {

    private boolean condition = false;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = FirebaseFirestore.getInstance();

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
                                Toast.makeText(MainActivity.this, "Successful " + condition, Toast.LENGTH_SHORT).show();
                                Log.i("Traffic Devils Test", Boolean.toString(condition));
                            } else {
                                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                                Toast.makeText(MainActivity.this, "Successful " + condition, Toast.LENGTH_SHORT).show();
                                Log.i("Traffic Devils Test", Boolean.toString(condition));
                                startActivity(intent);
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Error " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
