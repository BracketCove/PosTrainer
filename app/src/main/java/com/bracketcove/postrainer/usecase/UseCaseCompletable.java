package com.bracketcove.postrainer.usecase;

import io.reactivex.Completable;

/**
 * Created by R_KAY on 7/17/2017.
 */

public interface UseCaseCompletable<Params> {
        Completable runUseCase(Params... params);
}
