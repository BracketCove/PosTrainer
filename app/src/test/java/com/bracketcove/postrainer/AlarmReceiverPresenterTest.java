package com.bracketcove.postrainer;

import com.bracketcove.postrainer.alarmreceiver.AlarmReceiverContract;
import com.bracketcove.postrainer.alarmreceiver.AlarmReceiverPresenter;
import com.bracketcove.postrainer.data.alarmdatabase.AlarmSource;
import com.bracketcove.postrainer.data.alarmservice.AlarmManager;
import com.bracketcove.postrainer.data.viewmodel.Alarm;
import com.bracketcove.postrainer.util.SchedulerProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import io.reactivex.Completable;
import io.reactivex.Flowable;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AlarmReceiverPresenterTest {

    @Mock
    private AlarmManager alarmManager;

    @Mock
    private AlarmSource alarmSource;

    @Mock
    private AlarmReceiverContract.View view;

    private SchedulerProvider schedulerProvider;

    private AlarmReceiverPresenter presenter;

    private static final String TITLE = "Coffee Break";

    private static final int MINUTE = 30;

    private static final int HOUR = 10;

    //TODO: fix this test data to look the same as implementation would
    private static final String ALARM_ID = "111111111111111";

    private static final Alarm ACTIVE_ALARM = new Alarm(
            ALARM_ID,
            TITLE,
            true,
            false,
            false,
            MINUTE,
            HOUR
    );

    private static final Alarm INACTIVE_ALARM = new Alarm(
            ALARM_ID,
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
                alarmSource,
                alarmManager,
                schedulerProvider
        );

    }


    /**
     * When an Alarm fires, AlarmSource will have to retrieve it from the Database. Other than
     * failing, there are two possible results:
     *
     * 1. If Alarm is set to Repeat, it should not be deactivated after being retrieved.
     *
     * 2. If Alarm isn't set to repeat, it should be deactivated and written back to the Database
     * before returning it to the Presenter.
     *
     * We can verify this logic flow by checking the state of a Alarm once it is passed to the
     * AlarmManager
     */
    @Test
    public void retrieveCurrentAlarm() {
        Alarm nonRepeatingAlarm = new Alarm(
                ALARM_ID,
                TITLE,
                true,
                false,
                false,
                MINUTE,
                HOUR
        );

        when(view.getViewModel())
                .thenReturn(nonRepeatingAlarm);

        //get the alarm so that we know if it needs to repeat or not
        when(alarmSource.getAlarmsById(ALARM_ID))
                .thenReturn(Flowable.just(nonRepeatingAlarm));

        //since this tests non-repeating alarms, we must rewrite the Alarm as INACTIVE after
        //it is retrieve
        when(alarmSource.updateAlarm(nonRepeatingAlarm))
                .thenReturn(Completable.complete());

        when(alarmManager.startAlarm(nonRepeatingAlarm))
                .thenReturn(Completable.complete());

        presenter.start();

        verify(alarmManager).startAlarm(nonRepeatingAlarm);
    }

    @Test
    public void retrieveCurrentAlarmRepeating() {
        Alarm repeatingAlarm = new Alarm(
                ALARM_ID,
                TITLE,
                true,
                false,
                true,
                MINUTE,
                HOUR
        );

        when(view.getViewModel())
                .thenReturn(repeatingAlarm);

        when(alarmSource.getAlarmsById(ALARM_ID))
                .thenReturn(Flowable.just(repeatingAlarm));

        when(alarmManager.startAlarm(repeatingAlarm))
                .thenReturn(Completable.complete());

        presenter.start();

        verify(alarmManager).startAlarm(repeatingAlarm);
    }

    @Test
    public void retrieveCurrentAlarmUnsuccessful() {
        Alarm repeatingAlarm = new Alarm(
                ALARM_ID,
                TITLE,
                true,
                false,
                true,
                MINUTE,
                HOUR
        );

        when(view.getViewModel())
                .thenReturn(repeatingAlarm);

        when(alarmSource.getAlarmsById(ALARM_ID))
                .thenReturn(Flowable.<Alarm>error(new Exception()));

        presenter.start();

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

        when(alarmManager.dismissAlarm())
                .thenReturn(Completable.complete());

        presenter.onAlarmDismissClick();

        verify(view).finishActivity();

    }

}