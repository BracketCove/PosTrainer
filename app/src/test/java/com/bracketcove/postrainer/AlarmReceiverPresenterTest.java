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
        Reminder nonRepeatingReminder = new Reminder(
                REMINDER_ID,
                TITLE,
                true,
                false,
                false,
                MINUTE,
                HOUR
        );

        when(view.getReminderViewModel())
                .thenReturn(nonRepeatingReminder);

        //get the reminder so that we know if it needs to repeat or not
        when(reminderService.getReminderById(nonRepeatingReminder))
                .thenReturn(Observable.just(nonRepeatingReminder));

        //since this tests non-repeating reminders, we must rewrite the Reminder as INACTIVE after
        //it is retrieve
        when(reminderService.updateReminder(nonRepeatingReminder))
                .thenReturn(Observable.empty());

        when(alarmService.startAlarm(nonRepeatingReminder))
                .thenReturn(Observable.empty());

        presenter.subscribe();

        verify(alarmService).startAlarm(nonRepeatingReminder);
    }

    @Test
    public void retrieveCurrentAlarmRepeating() {
        Reminder repeatingReminder = new Reminder(
                REMINDER_ID,
                TITLE,
                true,
                false,
                true,
                MINUTE,
                HOUR
        );

        when(view.getReminderViewModel())
                .thenReturn(repeatingReminder);

        when(reminderService.getReminderById(repeatingReminder))
                .thenReturn(Observable.just(repeatingReminder));

        when(alarmService.startAlarm(repeatingReminder))
                .thenReturn(Observable.empty());

        presenter.subscribe();

        verify(alarmService).startAlarm(repeatingReminder);
    }

    @Test
    public void retrieveCurrentAlarmUnsuccessful() {
        Reminder repeatingReminder = new Reminder(
                REMINDER_ID,
                TITLE,
                true,
                false,
                true,
                MINUTE,
                HOUR
        );

        when(view.getReminderViewModel())
                .thenReturn(repeatingReminder);

        when(reminderService.getReminderById(repeatingReminder))
                .thenReturn(Observable.<Reminder>error(new Exception()));

        presenter.subscribe();

        verify(view).makeToast(R.string.error_database_connection_failure);
        verify(view).finishActivity();
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
                .thenReturn(Observable.empty());

        presenter.onAlarmDismissClick();

        verify(view).finishActivity();

    }

}