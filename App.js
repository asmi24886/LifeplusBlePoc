/* eslint-disable no-alert */
/* eslint-disable quotes */
/* eslint-disable prettier/prettier */
import React, {useEffect}from 'react';
import {StyleSheet, Text, View, TouchableOpacity, Image, NativeEventEmitter} from 'react-native';
import {connect} from 'react-redux';
import BleNative from './BleNative';

import KeepAwake from 'react-native-keep-awake';

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: 'white',
  },
  view: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  button: {
    backgroundColor: 'gray',
    padding: 10,
    margin: 10,
  },
  text: {
    fontSize: 20,
    color: 'white',
  },
});

const EventManager = new NativeEventEmitter(BleNative);
const EventListeners = {};
EventListeners.printListener = null;

const App = () => {

  useEffect(() => {
    console.log(" ----------------- Component will mount ----------------------------");
    EventListeners.printListener = EventManager.addListener("EVENT_PRINT_LOG", (event) => {
      console.log("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ PRINT LOG EVENT RECEIVED FROM BACKEND");
    });

    return function() {
      console.log(" ----------------- Component will UN-mount ----------------------------");
      EventListeners.printListener.remove();
    };
  });

  return (
    <View style={styles.container}>
      <KeepAwake />
      <View style={{...styles.view, backgroundColor: 'yellow', width: 400}}>
        <Text>{"Here goes messages from backend"}</Text>
      </View>
      <View style={styles.view}>
        <TouchableOpacity
          style={styles.button}
          onPress={startService}>
          <Text style={styles.instructions}>Start</Text>
        </TouchableOpacity>
        <TouchableOpacity
          style={styles.button}
          onPress={stopService}>
          <Text style={styles.instructions}>Stop</Text>
        </TouchableOpacity>

        <TouchableOpacity
          style={styles.button}
          onPress={()=>{emitEvent();}}>
          <Text style={styles.instructions}>Connect</Text>
        </TouchableOpacity>

        {/* <TouchableOpacity
          style={styles.button}
          onPress={()=>{}}>
          <Text style={styles.instructions}>Diconnect</Text>
        </TouchableOpacity> */}

        <TouchableOpacity
          style={styles.button}
          onPress={()=>{}}>
          <Text style={styles.instructions}>Sync</Text>
        </TouchableOpacity>

        <TouchableOpacity
          style={styles.button}
          onPress={()=>{}}>
          <Text style={styles.instructions}>Measure</Text>
        </TouchableOpacity>

        {/* <TouchableOpacity
          style={styles.button}
          onPress={()=>{}}>
          <Text style={styles.instructions}>Calibrate</Text>
        </TouchableOpacity> */}
      </View>
    </View>
  );
};

const mapStateToProps = (store) => ({
  heartBeat: store.App.heartBeat,
});

function startService() {
  alert("Start Service requested");
  console.log("Start Service requested");

  try {
    BleNative.startService();
  }
  catch (e) {
    console.error(e);
  }
}

function stopService() {
  alert("Stop Service requested");
  console.log("Stop Service requested");

  try {
    BleNative.stopService();
  }
  catch (e) {
    console.error(e);
  }
}

function emitEvent() {
  alert("EMIT EVENT EVENT_START_BROADCAST");
  BleNative.testEmit();
}
export default connect(mapStateToProps)(App);
