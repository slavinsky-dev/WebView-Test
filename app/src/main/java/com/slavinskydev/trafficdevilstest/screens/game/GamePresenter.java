package com.slavinskydev.trafficdevilstest.screens.game;

class GamePresenter {

    private GameView view;

    GamePresenter(GameView view) {
        this.view = view;
    }

    void startGame() {
        view.startGame();
    }

    void clickMeButton() {
        view.clickMeButton();
    }


}
