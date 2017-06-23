package com.bracketcove.postrainer.base;

/**
 * Created by Ryan on 05/03/2017.
 */

public interface BasePresenter {
    /**
     * Initialize MVP Feature as required. If data is required to load the View,
     * this is when the Data will be queried; otherwise we may just wait for User Input and not do
     * anything here
     */
    void start();

    /**
     * Remove Listeners and prepare stuff for Gabage Collection
     */
    void stop();

}
