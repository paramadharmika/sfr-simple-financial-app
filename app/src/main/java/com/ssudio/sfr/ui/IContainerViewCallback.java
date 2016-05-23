package com.ssudio.sfr.ui;

public interface IContainerViewCallback {
    void showMessage(boolean isSuccess, String message);
    void showLoading();
    void dismissLoading();
}
