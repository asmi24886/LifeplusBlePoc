package com.lifeplusblepoc;

public class RpcManager {

    private static final RpcManager instance = new RpcManager();
    public static final  RpcManager getInstance() {
        return instance;
    }

    private final String BLE_EVENT_NAME = "EVENT_BLE";

    public void receive(String command) {

        //Process commands here from front end
        switch (command) {

            case RpcCommand.IncomingCommand.CONNECT : {
                BleManager.getInstance().connectNow(System.currentTimeMillis());
                break;
            }
            case RpcCommand.IncomingCommand.SYNC : {
                BleManager.getInstance().syncNow(System.currentTimeMillis());
                break;
            }
            case RpcCommand.IncomingCommand.MEASURE : {
                BleManager.getInstance().measureNow(System.currentTimeMillis());
                break;
            }
            default: {
                throw new IllegalArgumentException("No command was found at RpcManager.receive. Current command issued was - " + command);
            }
        }
    }

    public void sendEvent(String command, String data) {
        //Send to front end
        LocalBroadcastEventEmitter.getInstance().emitJsEvent(command, data);
    }

    public void sendBleEvent(String command, String data) {
        //Send to front end
        LocalBroadcastEventEmitter.getInstance().emitJsEvent(this.BLE_EVENT_NAME, command, data);
    }
}
