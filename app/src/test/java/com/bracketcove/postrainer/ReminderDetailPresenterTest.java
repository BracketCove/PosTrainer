package com.bracketcove.postrainer;

import com.bracketcove.postrainer.reminderdetail.ReminderDetailContract;
import com.bracketcove.postrainer.reminderdetail.ReminderDetailPresenter;
import com.bracketcove.postrainer.reminderservice.ReminderSource;

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

    //We don't Mock the class which we're testing!!!
    private ReminderDetailPresenter presenter;

    @Before
    public void setUp() {
        //In order to set up Mockito properly, we must call:
        MockitoAnnotations.initMocks(this);

        reminderSource = ReminderInjection.provideReminderService();

        presenter = new ReminderDetailPresenter(
                view,
                reminderSource,
                SchedulerInjection.provideSchedulerProvider()
        );
    }
                                //BACK IN 2 MINUTES
    /**
     * Basic procedure for testing (as far as this Intermediate Dev knows)
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
     * Tell the user that their Reminder has been updated,
     * and start ReminderListActivity
     */
    @Test
    public void whenReminderUpdatedSuccessful(){

        presenter.onDoneIconPress();

        Mockito.verify(view).showDatabaseUpdatedMessage();
        Mockito.verify(view).startReminderListActivity();
    }

    /**
     * Tell the user that their Reminder wasn't updated.
     */
    @Test
    public void whenReminderUpdatedUnsuccessful(){
        reminderSource.setReturnFail();

        presenter.onDoneIconPress();

        Mockito.verify(view).showDatabaseErrorMessage();
    }




}
