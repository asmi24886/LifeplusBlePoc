package com.lifeplusblepoc;

import com.google.gson.Gson;

public class BleManager {
    private static final BleManager instance = new BleManager();
    public static final BleManager getInstance() {return instance;};

    private boolean _connected = false;

    public void connectNow(long timeInMillies) {
        BleEvent event = null;

        this._connected = true;
        event = new BleEvent(RpcCommand.OutgoingCommand.CONNECTED, new MeasureData(timeInMillies, "Connected to BLE peripheral").toJson());

        this.publishEvent(event);
    }

    public void syncNow(long timeInMillies) {
        BleEvent event = null;

        if(!this._connected)
            event =  new BleEvent(RpcCommand.OutgoingCommand.CONNECT_FAIL, "Cannot Sync as BLE not connected");

        event = new BleEvent(RpcCommand.OutgoingCommand.SYNC_SUCCESS, new MeasureData(timeInMillies, "Successfully synced").toJson());

        this.publishEvent(event);

    }

    public void measureNow(long timeInMillies) {
        BleEvent event = null;

        if(!this._connected)
            event = new BleEvent(RpcCommand.OutgoingCommand.CONNECT_FAIL, "Cannot Measure as BLE not connected");

        event = new BleEvent(RpcCommand.OutgoingCommand.MEASURE_SUCCESS, new MeasureData(timeInMillies, "Measure data : Glucose level 90").toJson());

        this.publishEvent(event);
    }

    public void publishEvent(BleEvent event) {
        RpcManager.getInstance().sendBleEvent( event.command, event.data);
    }

    public static class BleEvent {
        String command;
        String data;

        BleEvent (String _command, String _data) {
            command = _command;
            data = _data;
        }

        public String toJson() {
            return new Gson().toJson(this);
        }
    }

    public static class MeasureData {
        long timestamp;
        String message;

        MeasureData(long _ms, String _m) {
            timestamp = _ms;
            message = _m;
        }

        public String toJson() {
            return new Gson().toJson(this);
        }

    }
}
