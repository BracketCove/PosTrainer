package com.bracketcove.postrainer.data.reminder;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Maybe;
import io.reactivex.MaybeEmitter;
import io.reactivex.MaybeOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by Ryan on 10/04/2017.
 */

public class ReminderService implements ReminderSource {

    private Realm realm;

    public ReminderService(Context context) {
        //Context is ApplicationContext via Dagger 2
        Realm.init(context);
        realm = Realm.getDefaultInstance();
    }

    @Override
    public Completable createReminder(final String reminderId) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(final CompletableEmitter e) throws Exception {
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm bgRealm) {
                        RealmReminder rem = realm.createObject(RealmReminder.class);

                        rem.setReminderId(reminderId);
                        rem.setHourOfDay(12);
                        rem.setMinute(0);
                        rem.setReminderTitle("New Alarm");
                        rem.setActive(false);
                        rem.setVibrateOnly(false);
                        rem.setRenewAutomatically(false);
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        e.onComplete();
                    }
                }, new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error) {
                        e.onError(error);
                    }
                });


            }
        });
    }

    @Override
    public Completable deleteReminder(final String reminderId) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(final CompletableEmitter e) throws Exception {
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm bgRealm) {
                        RealmQuery<RealmReminder> query = realm.where(RealmReminder.class);

                        query.equalTo("reminderId", reminderId);
                        RealmResults<RealmReminder> result = query.findAll();

                        if (result.size() == 0) {
                            e.onError(new Exception("ReminderNotFoundException"));
                        } else {
                            RealmReminder rem = result.get(0);
                            rem.deleteFromRealm();
                        }
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        e.onComplete();
                    }
                }, new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error) {
                        e.onError(error);
                    }
                });
            }
        });
    }

    @Override
    public Completable updateReminder(final RealmReminder reminder) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter e) throws Exception {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.copyToRealmOrUpdate(reminder);
                    }
                });

            }
        });

    }

    @Override
    public Maybe<List<RealmReminder>> getReminders() {
        return Maybe.create(new MaybeOnSubscribe<List<RealmReminder>>() {
            @Override
            public void subscribe(MaybeEmitter<List<RealmReminder>> e) throws Exception {
                RealmQuery<RealmReminder> query = realm.where(RealmReminder.class);
                RealmResults<RealmReminder> result = query.findAll();

                List<RealmReminder> reminderList = new ArrayList<RealmReminder>();

                if (result.size() == 0) {
                    e.onComplete();
                } else {
                    for (int i = 0; i < result.size(); i++) {
                        reminderList.add(
                                result.get(i)
                        );
                    }
                    e.onSuccess(reminderList);
                }
            }
        });
    }

    @Override
    public Single<RealmReminder> getReminderById(final String reminderId) {
        return Single.create(new SingleOnSubscribe<RealmReminder>() {
            @Override
            public void subscribe(SingleEmitter<RealmReminder> e) throws Exception {
                RealmQuery<RealmReminder> query = realm.where(RealmReminder.class);

                query.equalTo("reminderId", reminderId);
                RealmResults<RealmReminder> result = query.findAll();

                if (result.size() == 0){
                    e.onError(new Exception("ReminderNotFoundException"));
                } else {
                    e.onSuccess(
                            result.get(0)
                    );
                }
            }
        });
    }
}
