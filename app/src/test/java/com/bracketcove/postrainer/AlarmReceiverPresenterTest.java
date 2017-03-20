package com.bracketcove.postrainer;

import com.bracketcove.postrainer.settings.SettingsContract;
import com.bracketcove.postrainer.settings.SettingsPresenter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AlarmReceiverPresenterTest {

    @Mock
    private SettingsContract.View view;

    private SettingsPresenter presenter;

    public static final String VALID_PASSWORD = "123456";

    public static final String INVALID_PASSWORD = "12345";

    public static final String LONG_PASSWORD = "12345678912345678912";

    public static final String USERNAME = "Derp";

    public static final String INVALID_EMAIL = "userexample.com";

    public static final String VALID_EMAIL = "user@example.com";

    @Before
    public void SetUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        //authSource = AuthInjection.provideAuthSource();
//        presenter = new LoginPresenter(
//                authSource,
//                view,
//                SchedulerInjection.provideSchedulerProvider()
//        );

    }

    @Test
    public void whenUserClicksCreateButton() {
//        presenter.onCreateClick();
//        verify(view).startCreateAccountActivity();
    }
}