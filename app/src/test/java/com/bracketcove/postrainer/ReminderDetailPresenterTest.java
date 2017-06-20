package com.bracketcove.postrainer;

import com.bracketcove.postrainer.data.reminder.ReminderService;
import com.bracketcove.postrainer.data.viewmodel.Reminder;
import com.bracketcove.postrainer.reminderdetail.ReminderDetailContract;
import com.bracketcove.postrainer.reminderdetail.ReminderDetailPresenter;
import com.bracketcove.postrainer.usecase.GetReminder;
import com.bracketcove.postrainer.usecase.UpdateOrCreateReminder;
import com.bracketcove.postrainer.util.BaseSchedulerProvider;
import com.bracketcove.postrainer.util.SchedulerProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

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
    ReminderDetailContract.View view;

    @Mock
    GetReminder getReminder;

    @Mock
    ReminderService reminderService;

    private static final String TITLE = "Coffee Break";

    private static final int MINUTE = 30;

    private static final int HOUR = 10;


    //true means active
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


    private ReminderDetailPresenter presenter;

    @Before
    public void setUp() {
        //In order to set up Mockito properly, we must call:
        MockitoAnnotations.initMocks(this);

        SchedulerProvider schedulerProvider = new SchedulerProvider();

        presenter = new ReminderDetailPresenter(
                view,
                reminderService,
                schedulerProvider
        );
    }




    /**
     * Edge case where RealmReminder Id isn't valid to retrieve a RealmReminder from storage. In this case,
     * not much to do but start the List Activity Again and inform the user of an error
     */
    @Test
    public void whenReminderIdArgumentsInvalid(){
        //Try to get a Reminder based on Id
        Mockito.when(view.getReminderId())
                .thenReturn(REMINDER_ID);

        Mockito.when(view.getViewModel())
                .thenReturn(ACTIVE_REMINDER);

        Mockito.when(reminderService.getReminderById(ACTIVE_REMINDER))
                .thenReturn(Observable.<Reminder>error(new Exception("Something Went Wrong")));


        presenter.subscribe();

        Mockito.verify(view).makeToast(R.string.error_invalid_reminder_id);
        Mockito.verify(view).startReminderListActivity();
    }

    /**
     * Retrieve reminder at specified Id, and set up the view.
     */
    @Test
    public void whenReminderIdArgumentsValid(){
        Mockito.when(view.getReminderId())
                .thenReturn(REMINDER_ID);

        Mockito.when(view.getViewModel())
                .thenReturn(ACTIVE_REMINDER);

        Mockito.when(reminderService.getReminderById(ACTIVE_REMINDER))
                .thenReturn(Observable.<Reminder>just(ACTIVE_REMINDER));

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
     * Build up a RealmReminder with the appropriate reminderId, but update according to View state
     * (picker, check boxes, so forth).
     * Tell the user that their RealmReminder has been updated,
     * and start ReminderListActivity
     */
    @Test
    public void whenReminderUpdatedSuccessful(){
        Mockito.when(view.getViewModel()).thenReturn(INACTIVE_REMINDER);

        Mockito.when(reminderService.updateReminder(INACTIVE_REMINDER))
                .thenReturn(Observable.empty());

        presenter.onDoneIconPress();

        //[4]
        Mockito.verify(view).makeToast(R.string.message_database_write_successful);
        Mockito.verify(view).startReminderListActivity();
    }

    /**
     * Tell the user that their RealmReminder wasn't updated.
     */
    @Test
    public void whenReminderUpdatedUnsuccessful(){
        Mockito.when(view.getViewModel()).thenReturn(INACTIVE_REMINDER);

        Mockito.when(reminderService.updateReminder(INACTIVE_REMINDER))
                .thenReturn(Observable.error(new Exception("Something went wrong")));

        presenter.onDoneIconPress();

        Mockito.verify(view).makeToast(R.string.error_database_write_failure);
    }
}
