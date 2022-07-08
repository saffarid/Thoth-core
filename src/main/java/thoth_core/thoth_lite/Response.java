package thoth_core.thoth_lite;

import thoth_core.thoth_lite.config.Configuration;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Класс представляет объект-ответ который система возвращает в ответ на запрос к ней.
 */
public final class Response<T> {
    /**
     * Код ошибки
     * */
    private final ResponseCode    responseCode;
    /**
     * Сообщение ошибки
     * */
    private final String message;
    /**
     * Возвращаемые данные
     * */
    private final T datas;

    public Response(
            ResponseCode responseCode,
            String message,
            T datas
                   ) {
        this.responseCode = responseCode;
        this.message      = message;
        this.datas        = datas;
    }

    public ResponseCode getResponseCode() {
        return responseCode;
    }

    public String getMessage() {
        return message;
    }

    public T getDatas() throws NoSuchElementException{
        if(datas == null){
            throw new NoSuchElementException();
        }
        return datas;
    }
}
