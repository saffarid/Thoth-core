package thoth_core;

public enum ResponseCode {

    /* Успешные */
    OK(200),

    /* Клиентские */
    BAD_REQUEST(400),
    NOT_FOUND(404),

    /* Системные */
    INTERNAL_ERROR(500),
    NOT_IMPLEMENTED(501)
    ;

    private int i;
    ResponseCode(int i) {
        this.i = i;
    }

}
