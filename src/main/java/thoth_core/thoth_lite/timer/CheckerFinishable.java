package thoth_core.thoth_lite.timer;

import thoth_core.thoth_lite.config.impl.Config;
import thoth_core.thoth_lite.db_data.db_data_element.properties.Finishable;
import thoth_core.thoth_lite.db_data.db_data_element.properties.Identifiable;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.concurrent.*;

public class CheckerFinishable
        implements
        Traceable
        , Flow.Subscriber<List<Finishable>>
        , Closeable {

    private final int POOL_SIZE = 5;

    private SubmissionPublisher publisher;
    private Flow.Subscription subscription;

    private HashMap<Finishable, ScheduledFuture> taskMap;
    private List<HashMap<WhatDo, Finishable>> buffer;
    private ScheduledThreadPoolExecutor poolExecutor;

    public CheckerFinishable() {
        publisher = new SubmissionPublisher(ForkJoinPool.commonPool(), POOL_SIZE);
        poolExecutor = new ScheduledThreadPoolExecutor(POOL_SIZE);
        taskMap = new HashMap<>();
        buffer = new LinkedList<>();
    }

    /**
     * Проверка запланированности задачи.
     * Если задача запланирована, функция возвращает объект задачи
     */
    private Finishable isFinishableNotificationPlanning(Identifiable identifiable) {
        Optional<Finishable> first = taskMap.keySet()
                .stream()
                .filter(finishable -> ((Identifiable) finishable).getId().equals(identifiable.getId()))
                .findFirst();
        if (first.isEmpty()) return null;
        return first.get();
    }

    /**
     * Функция распределяет запланированные задачи для оповещения наступления даты
     */
    public void notificationPlanning(List<Finishable> finishables) {
        LocalDate currentDate = LocalDate.now();

        for (Finishable finishable : finishables) {

            Finishable finishablePlanning = isFinishableNotificationPlanning((Identifiable) finishable);
            boolean finishableIsPlan = finishablePlanning != null;

            if (finishableIsPlan) {
                //Ветка запланированной задачи
                if (finishable.isFinish()) {
                    taskMap.get(finishablePlanning).cancel(true);
                    taskMap.remove(finishablePlanning);
                    notifySubscribers(wrappedFinishable(WhatDo.CANCEL, finishablePlanning));
                }
            } else {
                //Ветка не запланированной задачи
                if (finishable.isFinish()) continue;

                LocalDate finishDate = finishable.finishDate();
                int daysDelay = Period.between(currentDate, finishDate).getDays();
                Runnable task = () -> notifySubscribers(wrappedFinishable(WhatDo.PLANNING, finishable));

                if ((daysDelay > Config.getInstance().getDelivered().getDayBeforeDelivery().getValue()) && (finishDate.isAfter(currentDate))) {
                    taskMap.put(
                            finishable,
                            poolExecutor.schedule(task, daysDelay, TimeUnit.DAYS)
                    );
                } else {
                    taskMap.put(
                            finishable,
                            poolExecutor.schedule(task, 1, TimeUnit.SECONDS)
                    );
                }

            }

        }
    }

    private HashMap<WhatDo, Finishable> wrappedFinishable(WhatDo whatDo, Finishable finishable) {
        HashMap<WhatDo, Finishable> res = new HashMap<>();
        res.put(whatDo, finishable);
        return res;
    }

    @Override
    public void close(Finishable finishable) {
        //Завершаем выполнение задачи
        taskMap.get(finishable).cancel(false);
        //Удаляем из списка finishable-объект
        taskMap.remove(finishable);
        //Завершаем finishable-объект
        finishable.finish();
    }

    /* --- Функции публикатора --- */
    @Override
    public void subscribe(Flow.Subscriber subscriber) {
        boolean hadSubscribers = publisher.hasSubscribers();
        publisher.subscribe(subscriber);
        if (!hadSubscribers) {
            for (HashMap<WhatDo, Finishable> finishable : buffer) {
                notifySubscribers(finishable);
                buffer.remove(finishable);
            }
        }
    }

    private void notifySubscribers(HashMap<WhatDo, Finishable> finishable) {
        if (publisher.hasSubscribers()) {
            publisher.submit(finishable);
        } else {
            buffer.add(finishable);
        }
    }

    /* --- Функции подписчика на таблицу --- */
    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1);
    }

    @Override
    public void onNext(List<Finishable> item) {
        notificationPlanning(item);
        this.subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onComplete() {

    }
}
