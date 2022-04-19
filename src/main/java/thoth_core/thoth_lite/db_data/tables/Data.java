package thoth_core.thoth_lite.db_data.tables;

import thoth_core.thoth_lite.CoreLogger;
import thoth_core.thoth_lite.db_data.DBData;
import thoth_core.thoth_lite.db_data.db_data_element.properties.Identifiable;
import thoth_core.thoth_lite.db_data.db_data_element.properties.parts.Nameable;
import thoth_core.thoth_lite.exceptions.NotContainsException;

import java.util.*;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

public abstract class Data<T extends Identifiable>
        implements Nameable
        , TableReadable
        , Flow.Publisher {

    /**
     * Константа для ведения начала лога.
     * Принимает 2 параметра:
     *      1 - Наименование таблицы;
     *      2 - Целевое сообщение
     * */
    private final String TEMPLATE_LOG = "Table: %1$s; %2$s";

    /**
     * Лог новой подписки
     * */
    private final String NEW_SUBSCRIBER = "New subscriber.";

    /**
     * Лог данных
     * */
    private final String ALREADY_SUBCRIBER = "Already subscriber.";


    protected String name;
    protected List<T> datas;
    protected SubmissionPublisher<List<T>> publisher;

    public Data() {
        datas = new LinkedList<>();
        publisher = new SubmissionPublisher<>();
    }


    public void addData(T data) {
        if (!contains(data)) datas.add(data);
    }

    /**
     * Проверка существования записи в таблице
     * */
    public boolean contains(T data) {
        return datas
                .stream()
                .anyMatch(t -> t.equals(data));
    }

    /**
     * Функция конвертирует объект записи таблицы в карту.
     *
     * ------------------------------------------------------------------------------------
     * |         Key          |                      Value                                |
     * ------------------------------------------------------------------------------------
     * | Наименование таблицы | Данные для вставки - Карта (наименование колонки, данные) |
     * ------------------------------------------------------------------------------------
     * | Наименование таблицы | Данные для вставки - Карта (наименование колонки, данные) |
     * ------------------------------------------------------------------------------------
     *
     * @param list список добавляемых записей
     *
     * @return список добавляемых записей по каждой таблице.
     * */
    public abstract Map<String, List<HashMap<String, Object>>> convertToMap(List<? extends Identifiable> list);

    /**
     * @param id идентификатор объекта
     * @return Объект таблицы с заданным идентификатором
     * */
    public T getById(String id) throws NotContainsException {
        Optional<T> element = datas.stream().filter(t -> t.getId().equals(id)).findFirst();
        if (!element.isPresent()) throw new NotContainsException();
        return element.get();
    }

    /**
     * @return полный список записей таблицы
     * */
    public List<T> getDatas() {
        return datas;
    }

    public Identifiable getFromTableById(String tableName, String id) throws NotContainsException {
        return DBData.getInstance().getTable(tableName).getById(id);
    }

    public String getName() {
        return name;
    }

    public void removeData(T data) {
        if (contains(data)) datas.remove(data);
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Функция осуществляет подписку на таблицу
     * */
    @Override
    public void subscribe(Flow.Subscriber subscriber) {
        if (!publisher.isSubscribed(subscriber)) {
            CoreLogger.log.info(
                    String.format(TEMPLATE_LOG, name, NEW_SUBSCRIBER)
            );
            publisher.subscribe(subscriber);
            publisher.submit(datas);
        }
    }
}
