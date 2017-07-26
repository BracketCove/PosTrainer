package com.bracketcove.postrainer;

import com.bracketcove.postrainer.data.alarmdatabase.AlarmDatabase;
import com.bracketcove.postrainer.data.alarmdatabase.AlarmSource;
import com.bracketcove.postrainer.data.alarmservice.AlarmManager;
import com.bracketcove.postrainer.data.alarmservice.AlarmService;
import com.bracketcove.postrainer.data.viewmodel.Alarm;
import com.bracketcove.postrainer.alarmlist.AlarmListContract;
import com.bracketcove.postrainer.alarmlist.AlarmListPresenter;
import com.bracketcove.postrainer.util.SchedulerProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Ryan on 09/03/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class AlarmListPresenterTest {

    @Mock
    private AlarmListContract.View view;

    @Mock
    private AlarmSource alarmSource;

    @Mock
    private AlarmManager alarmManager;

    private AlarmListPresenter presenter;

    private static final String TITLE = "Coffee Break";

    private static final int MINUTE = 30;

    private static final int HOUR = 10;

    private static final boolean ALARM_STATE = true;

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

       SchedulerProvider schedulerProvider = new SchedulerProvider();

        presenter = new AlarmListPresenter(
                view,
                alarmSource,
                alarmManager,
                schedulerProvider
        );
    }

    /**
     * At least one RealmAlarm found in storage. Display it/them to user.
     */
    @Test
    public void onGetAlarmListSuccess() {
        List<Alarm> alarmList = new ArrayList<>();
        alarmList.add(INACTIVE_ALARM);

        when(alarmSource.getAlarms()).thenReturn(Flowable.just(alarmList));

        presenter.start();

        verify(view).setAlarmListData(Mockito.anyList());
    }

    /**
     * At least one RealmAlarm found in storage. Display it/them to user.
     */
    @Test
    public void onGetAlarmListEmpty() {
        List<Alarm> alarmList = new ArrayList<>();
        alarmList.add(INACTIVE_ALARM);

        when(alarmSource.getAlarms()).thenReturn(Flowable.just(alarmList));

        presenter.start();

        verify(view).setAlarmListData(Mockito.anyList());
    }

    /**
     * At least one RealmAlarm found in storage. Display it/them to user.
     */
    @Test
    public void onGetAlarmListException() {
        List<Alarm> alarmList = new ArrayList<>();
        alarmList.add(INACTIVE_ALARM);

        when(alarmSource.getAlarms()).thenReturn(Flowable.just(alarmList));

        presenter.start();

        verify(view).setAlarmListData(Mockito.anyList());
    }


    /**
     * At least one RealmAlarm found in storage. Display it/them to user.
     */
    @Test
    public void onGetAlarmsNotEmpty() {
        List<Alarm> alarmList = new ArrayList<>();
        alarmList.add(INACTIVE_ALARM);

        when(alarmSource.getAlarms()).thenReturn(Flowable.just(alarmList));

        presenter.start();

        verify(view).setAlarmListData(Mockito.anyList());
    }

    /**
     * No alarms found in storage. Show add alarm prompt.
     */
    @Test
    public void onGetAlarmsEmpty() {
        when(alarmSource.getAlarms()).thenReturn(Flowable.<List<Alarm>>empty());

        presenter.start();

        verify(view).setNoAlarmListDataFound();
    }

    /**
     * Storage throws an error
     */
    @Test
    public void onGetAlarmsError() {
        when(alarmSource.getAlarms()).thenReturn(
                Flowable.<List<Alarm>>error(new Exception())
        );


        presenter.start();

        verify(view).makeToast(R.string.error_database_connection_failure);
    }

    /**
     * Tests be behaviour when:
     * User toggle's RealmAlarm Active Switch, and
     * current state of alarm matches is the same.
     * If so, no need to update the Repository
     */
    @Test
    public void onAlarmToggledStatesMatchFalse() {
        presenter.onAlarmToggled(false, INACTIVE_ALARM);
        verify(view).makeToast(R.string.msg_alarm_deactivated);
    }

    @Test
    public void onAlarmToggledStatesMatchTrue() {
        presenter.onAlarmToggled(true, ACTIVE_ALARM);

        verify(view).makeToast(R.string.msg_alarm_activated);
    }

    /**
     * Tests be behaviour when:
     * User toggle's RealmAlarm Active Switch, and
     * current state of alarm matches is different.
     * If so, update Repo accordingly.
     */
    @Test
    public void onAlarmToggledStatesDifferActivate() {

        Alarm alarm = new Alarm(
                ALARM_ID,
                TITLE,
                false,
                false,
                false,
                MINUTE,
                HOUR
        );

        Mockito.when(alarmSource.updateAlarm(alarm))
                .thenReturn(Completable.complete());

        Mockito.when(alarmManager.setAlarm(alarm))
                .thenReturn(Completable.complete());

        presenter.onAlarmToggled(true, alarm);

        verify(view).makeToast(R.string.msg_alarm_activated);
    }

    /**
     * This test compares the desired state of an Alarm (based on the UI), vs the State of this Alarm
     * in the Model itself.
     * - If states matches, we shouldn't need to update the Repository.
     * - If state doesn't match, animate the toggle in the view, and update the Repository to
     * desired state
     */
    @Test
    public void onAlarmToggledStatesDifferDeactivate() {

        /**
         * One for the Error Log:
         * By using different Test Objects to set up my when/thenReturn clauses, a NPE was
         * being thrown in a strange spot.
         */

        Alarm alarm = new Alarm(
                ALARM_ID,
                TITLE,
                true,
                false,
                false,
                MINUTE,
                HOUR
        );

        Mockito.when(alarmSource.updateAlarm(alarm))
                .thenReturn(Completable.complete());

        Mockito.when(alarmManager.cancelAlarm(alarm))
                .thenReturn(Completable.complete());

        presenter.onAlarmToggled(false, alarm);

        verify(view).makeToast(R.string.msg_alarm_deactivated);
    }


    @Test
    public void onAlarmSuccessfullyDeleted() {
        Mockito.when(alarmSource.deleteAlarm(ACTIVE_ALARM))
                .thenReturn(Completable.complete());

        Mockito.when(alarmManager.cancelAlarm(ACTIVE_ALARM))
                .thenReturn(Completable.complete());

        presenter.onAlarmSwiped(1, ACTIVE_ALARM);

        verify(view).showUndoSnackbar();
    }

    @Test
    public void onAlarmUnsuccessfullyDeleted() {
        Mockito.when(alarmSource.deleteAlarm(ACTIVE_ALARM))
                .thenReturn(Completable.error(new Exception()));

        presenter.onAlarmSwiped(1, ACTIVE_ALARM);

        verify(view).makeToast(R.string.error_database_connection_failure);
        verify(view).insertAlarmAt(1, ACTIVE_ALARM);
    }

    @Test
    public void onSettingsIconClicked() {
        presenter.onSettingsIconClick();
        verify(view).startSettingsActivity();
    }

    /**
     * Maximum number of Alarms is currently 5. I'm not sure why you'd need more, but hopefully
     * customer feedback will solve this issue.
     */
    @Test
    public void whenUserTriesToAddMoreThanFiveAlarms() {
        Alarm alarm = new Alarm(
                ALARM_ID,
                TITLE,
                true,
                false,
                false,
                MINUTE,
                HOUR
        );

        presenter.onCreateAlarmButtonClick(5, alarm);
         view.makeToast(R.string.msg_alarm_limit_reached);
    }

    /**
     * When we create a RealmAlarm, we must add it to storage
     * as well as the View.
     */
    @Test
    public void onNewAlarmCreatedSuccessfully() {
        Alarm alarm = new Alarm(
                ALARM_ID,
                TITLE,
                true,
                false,
                false,
                MINUTE,
                HOUR
        );

        List<Alarm> alarms = new ArrayList<>();
        alarms.add(INACTIVE_ALARM);

        Mockito.when(alarmSource.updateAlarm(alarm))
                .thenReturn(Completable.complete());

        Mockito.when(alarmSource.getAlarms())
                .thenReturn(Flowable.just(alarms));

        presenter.onCreateAlarmButtonClick(1, alarm);

        verify(view).setAlarmListData(alarms);
    }

    @Test
    public void onNewAlarmCreatedUnsuccessfully() {
        Alarm alarm = new Alarm(
                ALARM_ID,
                TITLE,
                true,
                false,
                false,
                MINUTE,
                HOUR
        );

        Mockito.when(alarmSource.updateAlarm(alarm))
                .thenReturn(Completable.error(new Exception()));

        presenter.onCreateAlarmButtonClick(1, alarm);

        verify(view).makeToast(R.string.error_database_write_failure);
    }

    /**
     * This means that the user wants to edit a RealmAlarm
     */
    @Test
    public void onAlarmIconClicked() {
        presenter.onAlarmIconClick(ACTIVE_ALARM);

        verify(view).startAlarmDetailActivity(ALARM_ID);
    }

}