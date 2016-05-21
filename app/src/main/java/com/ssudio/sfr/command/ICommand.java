package com.ssudio.sfr.command;

public interface ICommand<T> {
    void executeAsync(T param);
}
