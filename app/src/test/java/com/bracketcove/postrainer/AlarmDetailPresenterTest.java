package com.bracketcove.postrainer;

import com.bracketcove.postrainer.data.alarmdatabase.AlarmSource;
import com.bracketcove.postrainer.data.alarmdatabase.AlarmSource;
import com.bracketcove.postrainer.data.viewmodel.Alarm;
import com.bracketcove.postrainer.alarmdetail.AlarmDetailContract;
import com.bracketcove.postrainer.alarmdetail.AlarmDetailPresenter;
import com.bracketcove.postrainer.usecase.GetAlarm;
import com.bracketcove.postrainer.util.SchedulerProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import io.reactivex.Completable;
import io.reactivex.Flowable;

/**
 * This is a "Unit Test", which uses Mockito to test the Apps Behaviour/Logic.
 * Created by Ryan on 09/03/2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class AlarmDetailPresenterTest {
    //Figure out what dependencies this Presenter needs


    /**
     * Why Mock? Mock's aren't real implementations of classes. Instead,
     * they can do things like record which method was called on the
     * Mock, and they can return predefined response by setting up
     * Mockito.when().return() clauses (see whenBackIconIsPressed for
     * a direct example).
     */
    @Mock
    AlarmDetailContract.View view;

    @Mock
    GetAlarm getAlarm;

    @Mock
    AlarmSource alarmSource;

    private static final String TITLE = "Coffee Break";

    private static final int MINUTE = 30;

    private static final int HOUR = 10;


    //true means active
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


    private AlarmDetailPresenter presenter;

    @Before
    public void setUp() {
        //In order to set up Mockito properly, we must call:
        MockitoAnnotations.initMocks(this);

        SchedulerProvider schedulerProvider = new SchedulerProvider();

        presenter = new AlarmDetailPresenter(
                view,
                alarmSource,
                schedulerProvider
        );
    }




    /**
     * Edge case where RealmAlarm Id isn't valid to retrieve a RealmAlarm from storage. In this case,
     * not much to do but start the List Activity Again and inform the user of an error
     */
    @Test
    public void whenAlarmIdArgumentsInvalid(){
        //Try to get a Alarm based on Id
        Mockito.when(view.getAlarmId())
                .thenReturn(ALARM_ID);

        Mockito.when(view.getViewModel())
                .thenReturn(ACTIVE_ALARM);

        Mockito.when(alarmSource.getAlarmsById(ALARM_ID))
                .thenReturn(Flowable.<Alarm>error(new Exception("Something Went Wrong")));


        presenter.start();

        Mockito.verify(view).makeToast(R.string.error_invalid_alarm_id);
        Mockito.verify(view).startAlarmListActivity();
    }

    /**
     * Retrieve alarm at specified Id, and set up the view.
     */
    @Test
    public void whenAlarmIdArgumentsValid(){
        Mockito.when(view.getAlarmId())
                .thenReturn(ALARM_ID);

        Mockito.when(view.getViewModel())
                .thenReturn(ACTIVE_ALARM);

        Mockito.when(alarmSource.getAlarmsById(ALARM_ID))
                .thenReturn(Flowable.<Alarm>just(ACTIVE_ALARM));

        presenter.start();

        Mockito.verify(view).setAlarmTitle(ACTIVE_ALARM.getAlarmTitle());
        Mockito.verify(view).setVibrateOnly(ACTIVE_ALARM.isVibrateOnly());
        Mockito.verify(view).setRenewAutomatically(ACTIVE_ALARM.isRenewAutomatically());
        Mockito.verify(view).setPickerTime(ACTIVE_ALARM.getHourOfDay(), ACTIVE_ALARM.getMinute());

    }

    /**
     * Basic procedure for testing (as far as this I'm aware)
     * 1. prepare any Mocks with Mockito.when().return clauses (if necessary)
     * 2. Call the event which you are testing
     * 3. Verify the result of the event.
     */
    @Test
    public void whenBackIconIsPressed(){
        //1. We'll get to that later

        //2. Call the event
        presenter.onBackIconPress();

        //3. Verify that this is the result of calling onBackIconPress:
        Mockito.verify(view).startAlarmListActivity();
    }

    /**
     * Build up a RealmAlarm with the appropriate alarmId, but update according to View state
     * (picker, check boxes, so forth).
     * Tell the user that their RealmAlarm has been updated,
     * and start AlarmListActivity
     */
    @Test
    public void whenAlarmUpdatedSuccessful(){
        Mockito.when(view.getViewModel()).thenReturn(INACTIVE_ALARM);

        Mockito.when(alarmSource.updateAlarm(INACTIVE_ALARM))
                .thenReturn(Completable.complete());

        presenter.onDoneIconPress();

        //[4]
        Mockito.verify(view).makeToast(R.string.message_database_write_successful);
        Mockito.verify(view).startAlarmListActivity();
    }

    /**
     * Tell the user that their RealmAlarm wasn't updated.
     */
    @Test
    public void whenAlarmUpdatedUnsuccessful(){
        Mockito.when(view.getViewModel()).thenReturn(INACTIVE_ALARM);

        Mockito.when(alarmSource.updateAlarm(INACTIVE_ALARM))
                .thenReturn(Completable.error(new Exception("Something went wrong")));

        presenter.onDoneIconPress();

        Mockito.verify(view).makeToast(R.string.error_database_write_failure);
    }
}
