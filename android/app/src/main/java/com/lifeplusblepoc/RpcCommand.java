package com.lifeplusblepoc;

public class RpcCommand {

    public static final class IncomingCommand {
        public static final String CONNECT             = "CONNECT";
        public static final String DISCONNECT          = "DISCONNECT";

        public static final String SYNC                = "SYNC";
        public static final String MEASURE             = "MEASURE";
        public static final String CALIBRATE           = "CALIBRATE";

        public static final String APP_IN_FOREGROUND   = "APP_IN_FOREGROUND";
        public static final String APP_IN_BACKGROUND   = "APP_IN_BACKGROUND";

        public static final String SERVICE_STATUS      = "SERVICE_STATUS";
    }

    public static final class OutgoingCommand {

        public static final String CONNECT_FAIL        = "CONNECT_FAIL";

        public static final String CONNECTED           =  "CONNECTED";
        public static final String DISCONNECTED        =  "DISCONNECTED";

        public static final String SYNC_SUCCESS        = "SYNC_SUCCESS";
        public static final String SYNC_FAIL           = "SYNC_FAIL";

        public static final String MEASURE_SUCCESS     = "MEASURE_SUCCESS";
        public static final String MEASURE_FAIL        = "MEASURE_FAIL";

        public static final String AUTO_MEASURE_SUCCESS= "AUTO_MEASURE_SUCCESS";
        public static final String AUTO_MEASURE_FAIL   = "AUTO_MEASURE_FAIL";

        public static final String CALIBRATE_SUCCESS   = "CALIBRATE_SUCCESS";
        public static final String CALIBRATE_FAIL      = "CALIBRATE_FAIL";

        public static final String BUSY                = "BUSY";
        public static final String REJECTED            = "REJECTED";
    }

}
