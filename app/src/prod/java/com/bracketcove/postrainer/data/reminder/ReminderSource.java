package com.bracketcove.postrainer.data.reminder;

import com.bracketcove.postrainer.BaseSource;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Maybe;

/**
 * Created by Ryan on 09/03/2017.
 */

public interface ReminderSource extends BaseSource{

    Maybe<List<Reminder>> getReminderList();



}
