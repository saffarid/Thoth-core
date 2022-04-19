package thoth_core.thoth_lite.config;

/**
 * Периодичность автообновления БД.
 * Измеряется в минутах.
 * */
public enum PeriodAutoupdateDatabase
    implements ConfigEnums<Integer>
{

    /**
     * Никогда.
     * Never.
     * */
    NEVER(-1),
    /**
     * 1 минута.
     * 1 minute.
     * */
    ONE(1),
    /**
     * 5 минут.
     * 5 minutes.
     * */
    FIVE(5),
    /**
     * 10 минут.
     * 10 minutes.
     * */
    TEN(10),
    /**
     * 15 минут.
     * 15 minutes.
     * */
    FEFTEEN(15),
    /**
     * 30 минут.
     * 30 minutes.
     * */
    THIRTY(30),
    /**
     * 60 минут.
     * 60 minutes.
     * */
    HOUR(60)
    ;
    private int period;
    PeriodAutoupdateDatabase(int period) {
        this.period = period;
    }
    @Override
    public String getName() {
        return toString();
    }

    @Override
    public Integer getValue() {
        return period;
    }
}
