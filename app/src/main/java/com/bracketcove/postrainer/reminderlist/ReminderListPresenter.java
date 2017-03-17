package com.bracketcove.postrainer.reminderlist;

import com.bracketcove.postrainer.data.reminder.Reminder;
import com.bracketcove.postrainer.data.reminder.ReminderSource;
import com.bracketcove.postrainer.util.BaseScheduler;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableMaybeObserver;

/**
 * Created by Ryan on 05/03/2017.
 */

final class ReminderListPresenter implements ReminderListContract.Presenter {

    private final ReminderListContract.View view;
    private final ReminderSource reminderSource;
    private final BaseScheduler schedulerProvider;
    private final CompositeDisposable compositeDisposable;

    @Inject
    public ReminderListPresenter(ReminderListContract.View view,
                                 ReminderSource reminderSource,
                                 BaseScheduler schedulerProvider) {
        this.view = view;
        this.reminderSource = reminderSource;
        this.schedulerProvider = schedulerProvider;
        this.compositeDisposable = new CompositeDisposable();
        this.view.setPresenter(this);
    }

    @Inject
    void registerView() {
            //TODO:WHAT IT DO??????
    }

    @Override
    public void subscribe() {
        compositeDisposable.add(
                reminderSource.getReminderList()
                .subscribeWith(new DisposableMaybeObserver<List<Reminder>>() {
                    @Override
                    public void onSuccess(List<Reminder> reminders) {
                        view.setReminderListData(reminders);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                })
        );

    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void onAlarmToggled(boolean active) {

    }

    @Override
    public void onAlarmIconClicked() {

    }

    @Override
    public void onAlarmWidgetSwiped(int swipedItemPosition) {

    }

}
