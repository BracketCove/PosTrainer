package com.bracketcove.postrainer.usecase;


import com.bracketcove.postrainer.data.viewmodel.Reminder;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;


public interface UseCase {

    /**
     * Use cases which would benefit from a RequestModel Possibly
     */
    interface RequestModel {
        Observable runUseCase(Reminder reminder);
    }

    /**
     * Use cases which don't require a RequestModel
     */
    interface Request {
        Observable runUseCase();
    }
}