package com.bracketcove.postrainer.data.reminder;

import com.bracketcove.postrainer.data.realmmodel.RealmReminder;
import com.bracketcove.postrainer.data.viewmodel.Reminder;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by Ryan on 10/04/2017.
 */

public class ReminderService implements ReminderSource {

    public ReminderService() {

    }

    @Override
    public Completable deleteReminder(final Reminder reminder) {
        return Completable.create(
                new CompletableOnSubscribe() {
                    @Override
                    public void subscribe(final CompletableEmitter e) throws Exception {
                        Realm realm = Realm.getDefaultInstance();

                        realm.beginTransaction();

                        RealmQuery<RealmReminder> query = realm.where(RealmReminder.class);

                        query.equalTo("reminderId", reminder.getReminderId());
                        RealmResults<RealmReminder> result = query.findAll();

                        if (result.size() == 0) {
                            realm.cancelTransaction();
                            e.onError(new Exception());
                        } else {
                            result.deleteFromRealm(0);
                            realm.commitTransaction();
                            e.onComplete();
                        }
                    }
                }
        );
    }

    @Override
    public Completable updateReminder(final Reminder reminder) {
        return Completable.create(
                new CompletableOnSubscribe() {
                    @Override
                    public void subscribe(final CompletableEmitter e) throws Exception {
                        Realm realm = Realm.getDefaultInstance();

                        realm.beginTransaction();

                        RealmReminder realmReminder = new RealmReminder();

                        realmReminder.setReminderId(reminder.getReminderId());
                        realmReminder.setHourOfDay(reminder.getHourOfDay());
                        realmReminder.setMinute(reminder.getMinute());
                        realmReminder.setReminderTitle(reminder.getReminderTitle());
                        realmReminder.setActive(reminder.isActive());
                        realmReminder.setVibrateOnly(reminder.isVibrateOnly());
                        realmReminder.setRenewAutomatically(reminder.isRenewAutomatically());

                        realm.copyToRealmOrUpdate(realmReminder);

                        realm.commitTransaction();

                        e.onComplete();
                    }
                }
        );
    }

    @Override
    public Flowable<List<Reminder>> getReminders() {
        return Flowable.create(
                new FlowableOnSubscribe<List<Reminder>>() {
                    @Override
                    public void subscribe(FlowableEmitter<List<Reminder>> e) throws Exception {
                        Realm realm = Realm.getDefaultInstance();

                        RealmQuery<RealmReminder> query = realm.where(RealmReminder.class);
                        RealmResults<RealmReminder> result = query.findAll();

                        List<Reminder> reminderList = new ArrayList<>();

                        if (result.size() == 0) {
                            e.onComplete();
                        } else {
                            for (int i = 0; i < result.size(); i++) {
                                Reminder reminder = new Reminder();
                                RealmReminder realmReminder = result.get(i);

                                reminder.setActive(realmReminder.isActive());
                                reminder.setRenewAutomatically(realmReminder.isRenewAutomatically());
                                reminder.setVibrateOnly(realmReminder.isVibrateOnly());
                                reminder.setHourOfDay(realmReminder.getHourOfDay());
                                reminder.setMinute(realmReminder.getMinute());
                                reminder.setReminderTitle(realmReminder.getReminderTitle());
                                reminder.setReminderId(realmReminder.getReminderId());

                                reminderList.add(
                                        reminder
                                );
                            }
                            e.onNext(reminderList);
                        }
                    }

                },
                BackpressureStrategy.LATEST);
    }

    @Override
    public Flowable<Reminder> getReminderById(final String reminderId) {
        return Flowable.create(
                new FlowableOnSubscribe<Reminder>() {
                    @Override
                    public void subscribe(FlowableEmitter<Reminder> e) throws Exception {

                        Realm realm = Realm.getDefaultInstance();

                        RealmQuery<RealmReminder> query = realm.where(RealmReminder.class);
                        query.equalTo("reminderId", reminderId);

                        RealmResults<RealmReminder> result = query.findAll();

                        if (result.size() == 0) {
                            e.onError(new Exception("ReminderNotFoundException"));
                        } else {
                            RealmReminder realmReminder = result.get(0);
                            Reminder reminder = new Reminder();

                            reminder.setReminderId(realmReminder.getReminderId());
                            reminder.setActive(realmReminder.isActive());
                            reminder.setRenewAutomatically(realmReminder.isRenewAutomatically());
                            reminder.setVibrateOnly(realmReminder.isVibrateOnly());
                            reminder.setHourOfDay(realmReminder.getHourOfDay());
                            reminder.setMinute(realmReminder.getMinute());
                            reminder.setReminderTitle(realmReminder.getReminderTitle());

                            e.onNext(reminder);
                        }
                    }
                },
                BackpressureStrategy.LATEST);
    }
}
