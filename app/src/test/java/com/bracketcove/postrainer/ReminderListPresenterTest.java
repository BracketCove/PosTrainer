package com.bracketcove.postrainer;

import com.bracketcove.postrainer.data.reminder.FakeReminderRepository;
import com.bracketcove.postrainer.data.reminder.Reminder;
import com.bracketcove.postrainer.data.reminder.ReminderSource;
import com.bracketcove.postrainer.reminderlist.ReminderListContract;
import com.bracketcove.postrainer.reminderlist.ReminderListPresenter;
import com.bracketcove.postrainer.schedulers.SchedulerProvider;
import com.bracketcove.postrainer.util.BaseSchedulerProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

/**
 * Created by Ryan on 09/03/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class ReminderListPresenterTest {


    @Mock
    private ReminderListContract.View view;

    private ReminderSource reminderSource;

    private BaseSchedulerProvider schedulerProvider;

    private ReminderListPresenter presenter;

    private static final String TITLE = "Coffee Break";

    private static final int MINUTE = 30;

    private static final int HOUR = 10;

    private static final String DEFAULT_NAME = "New Alarm";

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

    @Before
    public void SetUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        reminderSource = new FakeReminderRepository();

        schedulerProvider = SchedulerProvider.getInstance();
        presenter = new ReminderListPresenter(
                view,
                reminderSource,
                schedulerProvider
        );
    }

    /**
     * At least one Reminder found in storage. Display it/them to user.
     */
    @Test
    public void onGetRemindersNotEmpty() {
        presenter.subscribe();

        verify(view).setReminderListData(Mockito.anyList());
    }

    /**
     * No reminders found in storage. Show add reminder prompt.
     */
    @Test
    public void onGetRemindersEmpty() {
        reminderSource.setReturnEmpty();

        presenter.subscribe();

        verify(view).setNoReminderListDataFound();

    }

    /**
     * Storage throws an error
     */
    @Test
    public void onGetRemindersError() {
        reminderSource.setReturnFail();

        presenter.subscribe();

        verify(view).makeToast(R.string.error_database_connection_failure);
    }

    /**
     * Tests be behaviour when:
     * User toggle's Reminder Active Switch, and
     * current state of alarm matches is the same.
     * If so, no need to update the Repository
     */
    @Test
    public void onReminderToggledStatesMatchFalse() {
        presenter.onReminderToggled(false, ACTIVE_REMINDER);
        verify(view).makeToast(R.string.msg_alarm_deactivated);
    }

    @Test
    public void onReminderToggledStatesMatchTrue() {
        presenter.onReminderToggled(true, INACTIVE_REMINDER);
        verify(view).makeToast(R.string.msg_alarm_activated);
    }

    /**
     * Tests be behaviour when:
     * User toggle's Reminder Active Switch, and
     * current state of alarm matches is different.
     * If so, update Repo accordingly.
     */
    @Test
    public void onReminderToggledStatesDifferActivate() {
        presenter.onReminderToggled(true, INACTIVE_REMINDER);
        verify(view).makeToast(R.string.msg_alarm_activated);
    }

    @Test
    public void onReminderToggledStatesDifferDeactivate() {
        presenter.onReminderToggled(false, ACTIVE_REMINDER);
        verify(view).makeToast(R.string.msg_alarm_deactivated);
    }


    @Test
    public void onReminderSuccessfullyDeleted() {
        presenter.onReminderSwiped(1, ACTIVE_REMINDER);

        verify(view).makeToast(R.string.msg_alarm_deleted);
    }

    @Test
    public void onReminderUnsuccessfullyDeleted() {
        reminderSource.setReturnFail();

        presenter.onReminderSwiped(1, ACTIVE_REMINDER);

        verify(view).makeToast(R.string.error_database_connection_failure);
        verify(view).undoDeleteReminderAt(1, ACTIVE_REMINDER);
    }

    @Test
    public void onSettingsIconClicked() {
        presenter.onSettingsIconClick();
        verify(view).startSettingsActivity();
    }

    /**
     * Maximum number of Reminders is currently 5. I'm not sure why you'd need more, but hopefully
     * customer feedback will solve this issue.
     */
    @Test
    public void whenUserTriesToAddMoreThanFiveReminders() {
        presenter.onCreateReminderButtonClick(5, DEFAULT_NAME, REMINDER_ID);
         view.makeToast(R.string.msg_reminder_limit_reached);

    }

    /**
     * When we create a Reminder, we must add it to storage
     * as well as the View.
     */
    @Test
    public void onNewReminderCreatedSuccessfully() {
        presenter.onCreateReminderButtonClick(1, DEFAULT_NAME, REMINDER_ID);

        verify(view).addNewReminderToListView(Mockito.any(Reminder.class));
    }

    @Test
    public void onNewReminderCreatedUnsuccessfully() {
        reminderSource.setReturnFail();
        presenter.onCreateReminderButtonClick(1, DEFAULT_NAME, REMINDER_ID);

        verify(view).makeToast(R.string.error_database_write_failure);
    }

    /**
     * This means that the user wants to edit a Reminder
     */
    @Test
    public void onReminderIconClicked() {
        presenter.onReminderIconClick(ACTIVE_REMINDER);

        verify(view).startReminderDetailActivity(REMINDER_ID);
    }

    /*
    Test stub:

    /**
    *
    *
    @Test
    public void onSomethingHappened(){

    }
     */

}