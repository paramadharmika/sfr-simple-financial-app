package com.ssudio.sfr.registration.event;

import com.ssudio.sfr.event.BaseActionEvent;
import com.ssudio.sfr.registration.model.UserModel;

public class ProfileEvent extends BaseActionEvent {
    public static int REGISTERED = 1;
    public static int UPDATED = 2;
    public static int LOADED = 3;

    private int eventType;
    private UserModel model;

    public ProfileEvent(boolean isSuccess, String message, int eventType, UserModel model) {
        super(isSuccess, message);

        this.eventType = eventType;
        this.model = model;
    }

    public UserModel getModel() {
        return model;
    }

    public int getEventType() {
        return eventType;
    }
}
