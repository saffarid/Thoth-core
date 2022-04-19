package thoth_core.thoth_lite.exceptions;

import thoth_core.thoth_lite.info.SystemInfoKeys;

import java.util.List;

public class DontSetSystemInfoException extends RuntimeException{

    private static final String message = "DontSetSystemInfoException";
    private final List<SystemInfoKeys> missingConfig;

    public DontSetSystemInfoException(List<SystemInfoKeys> missingConfig) {
        this.missingConfig = missingConfig;
    }

    public List<SystemInfoKeys> getMissingConfig() {
        return missingConfig;
    }
}
