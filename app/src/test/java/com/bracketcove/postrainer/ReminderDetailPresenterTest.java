package com.bracketcove.postrainer;

import com.bracketcove.postrainer.data.reminder.FakeReminderRepository;
import com.bracketcove.postrainer.data.reminder.Reminder;
import com.bracketcove.postrainer.reminderdetail.ReminderDetailContract;
import com.bracketcove.postrainer.reminderdetail.ReminderDetailPresenter;
import com.bracketcove.postrainer.data.reminder.ReminderSource;
import com.bracketcove.postrainer.schedulers.SchedulerProvider;
import com.bracketcove.postrainer.util.BaseSchedulerProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * This is a "Unit Test", which uses Mockito to test the Apps Behaviour/Logic.
 * Created by Ryan on 09/03/2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class ReminderDetailPresenterTest {
    //Figure out what dependencies this Presenter needs


    /**
     * Why Mock? Mock's aren't real implementations of classes. Instead,
     * they can do things like record which method was called on the
     * Mock, and they can return predefined response by setting up
     * Mockito.when().return() clauses (see whenBackIconIsPressed for
     * a direct example).
     */
    @Mock
    private ReminderDetailContract.View view;

    private ReminderSource reminderSource;

    private BaseSchedulerProvider schedulerProvider;

    private static final String TITLE = "Coffee Break";

    private static final int MINUTE = 30;

    private static final int HOUR = 10;

    private static final String DEFAULT_NAME = "New Alarm";

    //true means active
    private static final boolean ALARM_STATE = true;

    //TODO: fix this test data to look the same as implementation would
    private static final String REMINDER_ID = "111111111111111";

    private static final Reminder ACTIVE_REMINDER = new Reminder(HOUR,
            MINUTE,
            TITLE,
            true,
            false,
            false,
            REMINDER_ID
    );

    private static final Reminder INACTIVE_REMINDER = new Reminder(HOUR,
            MINUTE,
            DEFAULT_NAME,
            false,
            false,
            false,
            REMINDER_ID
    );
    private ReminderDetailPresenter presenter;

    @Before
    public void setUp() {
        //In order to set up Mockito properly, we must call:
        MockitoAnnotations.initMocks(this);

        reminderSource = new FakeReminderRepository();
        schedulerProvider = SchedulerProvider.getInstance();

        presenter = new ReminderDetailPresenter(
                view,
                reminderSource,
                schedulerProvider
        );
    }


    /**
     * Edge case where Reminder Id isn't valid to retrieve a Reminder from storage. In this case,
     * not much to do but start the List Activity Again and inform the user of an error
     */
    @Test
    public void whenReminderIdArgumentsInvalid(){
        Mockito.when(view.getReminderId()).thenReturn(REMINDER_ID);
        reminderSource.setReturnFail();

        presenter.subscribe();

        Mockito.verify(view).makeToast(R.string.error_invalid_reminder_id);
        Mockito.verify(view).startReminderListActivity();
    }

    /**
     * Retrieve reminder at specified Id, and set up the view.
     */
    @Test
    public void whenReminderIdArgumentsValid(){
        Mockito.when(view.getReminderId()).thenReturn(REMINDER_ID);

        presenter.subscribe();

        Mockito.verify(view).setReminderTitle(ACTIVE_REMINDER.getReminderTitle());
        Mockito.verify(view).setVibrateOnly(ACTIVE_REMINDER.isVibrateOnly());
        Mockito.verify(view).setRenewAutomatically(ACTIVE_REMINDER.isRenewAutomatically());
        Mockito.verify(view).setPickerTime(ACTIVE_REMINDER.getHourOfDay(), ACTIVE_REMINDER.getMinute());

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
        Mockito.verify(view).startReminderListActivity();
    }

    /**
     * Build up a Reminder with the appropriate reminderId, but update according to View state
     * (picker, check boxes, so forth).
     * Tell the user that their Reminder has been updated,
     * and start ReminderListActivity
     */
    @Test
    public void whenReminderUpdatedSuccessful(){
        Mockito.when(view.getReminderTitle()).thenReturn(TITLE);
        Mockito.when(view.getReminderId()).thenReturn(REMINDER_ID);
        Mockito.when(view.getVibrateOnly()).thenReturn(ACTIVE_REMINDER.isVibrateOnly());
        Mockito.when(view.getRenewAutomatically()).thenReturn(ACTIVE_REMINDER.isRenewAutomatically());
        Mockito.when(view.getPickerHour()).thenReturn(HOUR);
        Mockito.when(view.getPickerMinute()).thenReturn(MINUTE);
        Mockito.when(view.getCurrentAlarmState()).thenReturn(ALARM_STATE);

        presenter.onDoneIconPress();

        Mockito.verify(view).makeToast(R.string.message_database_write_successful);
        Mockito.verify(view).startReminderListActivity();
    }

    /**
     * Tell the user that their Reminder wasn't updated.
     */
    @Test
    public void whenReminderUpdatedUnsuccessful(){
        reminderSource.setReturnFail();
        Mockito.when(view.getReminderTitle()).thenReturn(TITLE);
        Mockito.when(view.getReminderId()).thenReturn(REMINDER_ID);
        Mockito.when(view.getVibrateOnly()).thenReturn(ACTIVE_REMINDER.isVibrateOnly());
        Mockito.when(view.getRenewAutomatically()).thenReturn(ACTIVE_REMINDER.isRenewAutomatically());
        Mockito.when(view.getPickerHour()).thenReturn(HOUR);
        Mockito.when(view.getPickerMinute()).thenReturn(MINUTE);
        Mockito.when(view.getCurrentAlarmState()).thenReturn(ALARM_STATE);

        presenter.onDoneIconPress();

        Mockito.verify(view).makeToast(R.string.error_database_write_failure);
    }




}
