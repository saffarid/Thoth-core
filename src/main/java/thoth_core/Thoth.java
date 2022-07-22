package thoth_core;

import thoth_core.config.Configuration;
import thoth_core.db_data.db_data_element.properties.Identifiable;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Flow;

public interface Thoth {

    void close();

    /**
     * Функция для вызова комманды для работы с базой данных.
     *
     * @param command команда для определения действия с базой данных
     * @param table   таблица, над которой проводятся манипуляции
     * @param datas   обрабатываемые данные.
     *
     * @throws ClassCastException
     * @throws SQLException
     *
     * @return Объект класс Response.
     *
     * @see CommandDatabase
     * @see ClassCastException
     * @see Response
     */
    Response executeDataBase(CommandDatabase command,
                             AvailableTables table,
                             List<? extends Identifiable> datas) throws ClassNotFoundException, SQLException;

    /**
     * Функция возвращает объект конфигурации текущей системы.
     *
     * @return Объект конфигурации, реализующий интерфейс Configuration
     *
     * @see Configuration
     * @see Response
     */
    Response getConfig();

    /**
     * Функция для вызова комманды для работы с подписками.
     *
     * @param publisher  доступный для подписки публикатор данных
     * @param subscriber подписчик
     *
     * @return Объект Response.
     *
     * @see Publicator
     * @see Response
     */
    Response subscribe(Publicator publisher,
                       Flow.Subscriber subscriber);

}
