package ru.fedurovkostya.todolist;

import android.app.Application;

import com.onesignal.OneSignal;
//класс позволяет работать с сервисом для уведомлений onesignal
public class ApplicationClass extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
    }
}