package com.bracketcove.postrainer.usecase;


import com.bracketcove.postrainer.data.viewmodel.Reminder;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.Observable;


public interface UseCase<T, Params> {

    Flowable<T> runUseCase(Params... params);

}