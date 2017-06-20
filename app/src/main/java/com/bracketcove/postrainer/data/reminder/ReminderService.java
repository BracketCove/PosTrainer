package com.bracketcove.postrainer.data.reminder;

import com.bracketcove.postrainer.data.realmmodel.RealmReminder;
import com.bracketcove.postrainer.data.viewmodel.Reminder;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
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
    public Observable createReminder(final Reminder reminder) {
        return Observable.create(
                new ObservableOnSubscribe() {
                    @Override
                    public void subscribe(final ObservableEmitter e) throws Exception {
                        Realm realm = Realm.getDefaultInstance();

                        realm.beginTransaction();
                        RealmReminder rem = realm.createObject(
                                RealmReminder.class, reminder.getReminderId());

                        rem.setHourOfDay(12);
                        rem.setMinute(30);
                        rem.setReminderTitle("New Alarm");
                        rem.setActive(false);
                        rem.setVibrateOnly(false);
                        rem.setRenewAutomatically(false);

                        realm.commitTransaction();

                        e.onComplete();
                    }
                });
    }

    @Override
    public Observable deleteReminder(final Reminder reminder) {
        return Observable.create(
                new ObservableOnSubscribe() {
                    @Override
                    public void subscribe(final ObservableEmitter e) throws Exception {
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
                });
    }

    @Override
    public Observable updateReminder(final Reminder reminder) {
        return Observable.create(
                new ObservableOnSubscribe() {
                    @Override
                    public void subscribe(final ObservableEmitter e) throws Exception {
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
                });
    }

    @Override
    public Observable<List<Reminder>> getReminders() {
        return Observable.create(
                new ObservableOnSubscribe<List<Reminder>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<Reminder>> e) throws Exception {
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

                });
    }

    @Override
    public Observable<Reminder> getReminderById(final Reminder reminder) {
        return Observable.create(
                new ObservableOnSubscribe<Reminder>() {
                    @Override
                    public void subscribe(ObservableEmitter<Reminder> e) throws Exception {

                        Realm realm = Realm.getDefaultInstance();

                        RealmQuery<RealmReminder> query = realm.where(RealmReminder.class);
                        query.equalTo("reminderId", reminder.getReminderId());

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
                });
    }
}
