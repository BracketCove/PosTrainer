package com.bracketcove.postrainer.data.alarmdatabase;

import com.bracketcove.postrainer.data.realmmodel.RealmAlarm;
import com.bracketcove.postrainer.data.viewmodel.Alarm;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by Ryan on 10/04/2017.
 */

public class AlarmDatabase implements AlarmSource {

    public AlarmDatabase() {

    }

    @Override
    public Completable deleteAlarm(final Alarm alarm) {
        return Completable.create(
                new CompletableOnSubscribe() {
                    @Override
                    public void subscribe(final CompletableEmitter e) throws Exception {
                        Realm realm = Realm.getDefaultInstance();

                        realm.beginTransaction();

                        RealmQuery<RealmAlarm> query = realm.where(RealmAlarm.class);

                        query.equalTo("alarmId", alarm.getAlarmId());
                        RealmResults<RealmAlarm> result = query.findAll();

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
    public Completable updateAlarm(final Alarm alarm) {
        return Completable.create(
                new CompletableOnSubscribe() {
                    @Override
                    public void subscribe(final CompletableEmitter e) throws Exception {
                        Realm realm = Realm.getDefaultInstance();

                        realm.beginTransaction();

                        RealmAlarm realmAlarm = new RealmAlarm();

                        realmAlarm.setAlarmId(alarm.getAlarmId());
                        realmAlarm.setHourOfDay(alarm.getHourOfDay());
                        realmAlarm.setMinute(alarm.getMinute());
                        realmAlarm.setAlarmTitle(alarm.getAlarmTitle());
                        realmAlarm.setActive(alarm.isActive());
                        realmAlarm.setVibrateOnly(alarm.isVibrateOnly());
                        realmAlarm.setRenewAutomatically(alarm.isRenewAutomatically());

                        realm.copyToRealmOrUpdate(realmAlarm);

                        realm.commitTransaction();

                        e.onComplete();
                    }
                }
        );
    }

    @Override
    public Flowable<List<Alarm>> getAlarms() {
        return Flowable.create(
                new FlowableOnSubscribe<List<Alarm>>() {
                    @Override
                    public void subscribe(FlowableEmitter<List<Alarm>> e) throws Exception {
                        Realm realm = Realm.getDefaultInstance();

                        RealmQuery<RealmAlarm> query = realm.where(RealmAlarm.class);
                        RealmResults<RealmAlarm> result = query.findAll();

                        List<Alarm> alarmList = new ArrayList<>();

                        if (result.size() == 0) {
                            e.onComplete();
                        } else {
                            for (int i = 0; i < result.size(); i++) {
                                Alarm alarm = new Alarm();
                                RealmAlarm realmAlarm = result.get(i);

                                alarm.setActive(realmAlarm.isActive());
                                alarm.setRenewAutomatically(realmAlarm.isRenewAutomatically());
                                alarm.setVibrateOnly(realmAlarm.isVibrateOnly());
                                alarm.setHourOfDay(realmAlarm.getHourOfDay());
                                alarm.setMinute(realmAlarm.getMinute());
                                alarm.setAlarmTitle(realmAlarm.getAlarmTitle());
                                alarm.setAlarmId(realmAlarm.getAlarmId());

                                alarmList.add(
                                        alarm
                                );
                            }
                            e.onNext(alarmList);
                        }
                    }

                },
                BackpressureStrategy.LATEST);
    }

    @Override
    public Flowable<Alarm> getAlarmsById(final String reminderId) {
        return Flowable.create(
                new FlowableOnSubscribe<Alarm>() {
                    @Override
                    public void subscribe(FlowableEmitter<Alarm> e) throws Exception {

                        Realm realm = Realm.getDefaultInstance();

                        RealmQuery<RealmAlarm> query = realm.where(RealmAlarm.class);
                        query.equalTo("alarmId", reminderId);

                        RealmResults<RealmAlarm> result = query.findAll();

                        if (result.size() == 0) {
                            e.onError(new Exception("AlarmNotFoundException"));
                        } else {
                            RealmAlarm realmAlarm = result.get(0);
                            Alarm alarm = new Alarm();

                            alarm.setAlarmId(realmAlarm.getAlarmId());
                            alarm.setActive(realmAlarm.isActive());
                            alarm.setRenewAutomatically(realmAlarm.isRenewAutomatically());
                            alarm.setVibrateOnly(realmAlarm.isVibrateOnly());
                            alarm.setHourOfDay(realmAlarm.getHourOfDay());
                            alarm.setMinute(realmAlarm.getMinute());
                            alarm.setAlarmTitle(realmAlarm.getAlarmTitle());

                            e.onNext(alarm);
                        }
                    }
                },
                BackpressureStrategy.LATEST);
    }
}
