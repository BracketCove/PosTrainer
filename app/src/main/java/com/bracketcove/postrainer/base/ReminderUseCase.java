package com.bracketcove.postrainer.base;


import com.bracketcove.postrainer.data.viewmodel.Reminder;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

/**
 *Contract for any Use Case which needs to operate upon a Reminder. My hypothesis is that instead
 * of creating an Abstract Class with Generic Types for all Use Cases, I'll instead create a Use
 * Case Interface seperated by which Model these Cases need to Act upon. This may not be a Viable
 * approach in a much more complex application, but it may be a much more viable approach in a
 * smaller App like this. This is simply conjecture until I get it working though.
 */
public interface ReminderUseCase {

    Observable runUseCase(Reminder reminder);

}