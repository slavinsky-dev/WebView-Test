package com.slavinskydev.trafficdevilstest.screens.loader;

import android.content.Context;

import com.google.android.gms.tasks.Task;

public interface LoaderView {

    void showGameData(Context context);
    void showWebData(Context context);
    void showServerError(Task task);
}
