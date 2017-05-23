package com.bracketcove.postrainer.settings;

import com.bracketcove.postrainer.dependencyinjection.components.ApplicationComponent;
import com.bracketcove.postrainer.util.FragmentScoped;

import dagger.Component;

/**
 * Created by Ryan on 16/03/2017.
 */

@FragmentScoped
@Component(dependencies = ApplicationComponent.class,
        modules = SettingsPresenterModule.class)
public interface SettingsComponent {

    void inject(SettingsActivity settingsActivity);

}
