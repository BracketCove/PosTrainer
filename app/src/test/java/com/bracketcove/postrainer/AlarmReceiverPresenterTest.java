package com.bracketcove.postrainer;

import com.bracketcove.postrainer.alarmreceiver.AlarmReceiverContract;
import com.bracketcove.postrainer.alarmreceiver.AlarmReceiverPresenter;
import com.bracketcove.postrainer.data.alarm.AlarmService;
import com.bracketcove.postrainer.data.reminder.ReminderService;
import com.bracketcove.postrainer.data.viewmodel.Reminder;
import com.bracketcove.postrainer.util.SchedulerProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AlarmReceiverPresenterTest {

    @Mock
    private AlarmService alarmService;

    @Mock
    private ReminderService reminderService;

    @Mock
    private AlarmReceiverContract.View view;

    private SchedulerProvider schedulerProvider;

    private AlarmReceiverPresenter presenter;

    private static final String TITLE = "Coffee Break";

    private static final int MINUTE = 30;

    private static final int HOUR = 10;

    private static final String DEFAULT_NAME = "New Alarm";

    private static final boolean ALARM_STATE = true;

    //TODO: fix this test data to look the same as implementation would
    private static final String REMINDER_ID = "111111111111111";

    private static final Reminder ACTIVE_REMINDER = new Reminder(
            REMINDER_ID,
            TITLE,
            true,
            false,
            false,
            MINUTE,
            HOUR
    );

    private static final Reminder INACTIVE_REMINDER = new Reminder(
            REMINDER_ID,
            TITLE,
            false,
            false,
            false,
            HOUR,
            MINUTE

    );

    @Before
    public void SetUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        schedulerProvider = new SchedulerProvider();

        presenter = new AlarmReceiverPresenter(
                view,
                reminderService,
                alarmService,
                schedulerProvider
        );

    }


    /**
     * When an Alarm fires, ReminderService will have to retrieve it from the Database. Other than
     * failing, there are two possible results:
     *
     * 1. If Reminder is set to Repeat, it should not be deactivated after being retrieved.
     *
     * 2. If Reminder isn't set to repeat, it should be deactivated and written back to the Database
     * before returning it to the Presenter.
     *
     * We can verify this logic flow by checking the state of a Reminder once it is passed to the
     * AlarmService
     */
    @Test
    public void retrieveCurrentAlarm() {
        Reminder repeatingReminder = new Reminder(
                REMINDER_ID,
                TITLE,
                true,
                false,
                true,
                MINUTE,
                HOUR
        );

        when(view.getReminderId())
                .thenReturn(REMINDER_ID);

        when(reminderService.getReminderById(repeatingReminder))
                .thenReturn(Observable.just(repeatingReminder));

        when(alarmService.startAlarm(repeatingReminder))
                .thenReturn(Completable.complete());

        presenter.subscribe();

        verify(alarmService).startAlarm(repeatingReminder);
    }

    @Test
    public void retrieveCurrentAlarmUnsuccessful() {


        when(view.getReminderId())
                .thenReturn(REMINDER_ID);

        when(reminderService.getReminderById(ACTIVE_REMINDER))
                .thenReturn(Observable.<Reminder>error(new Exception()));

        presenter.subscribe();
    }


    /**
     * When the user clicks the Stop Alarm Button, two things should occur:
     * 1. Stop Media Player and Vibrator if they were active
     *
     * 2. Release the WakeLock, which forces the Device's CPU on (You don't want to leave that on)
     *
     */
    @Test
    public void onAlarmDismissSuccessful() {

        when(alarmService.dismissAlarm())
                .thenReturn(Completable.complete());

        when(alarmService.dismissAlarm())
                .thenReturn(Completable.complete());

        presenter.onAlarmDismissClick();

        verify(view).finishActivity();

    }

    /**Not entirely sure if this is a good test. I don't really know what to do if I can't release
     * the CPU after the first try, but perhaps I/you'll think of something.
     */
//    @Test
//    public void onAlarmDismissUnsuccessful () {
//        when(alarmService.dismissAlarm())
//                .thenReturn(Completable.complete());
//
//        when(alarmService.releaseWakeLock())
//                .thenReturn(Completable.error(new Exception()));
//
//        presenter.onAlarmDismissClick();
//
//        verify(view).finishActivity();
//
//    }
}