/* eslint-disable prettier/prettier */
/* eslint-disable no-trailing-spaces */
/* eslint-disable keyword-spacing */
/* eslint-disable no-alert */
/* eslint-disable quotes */
/* eslint-disable prettier/prettier */
import React, {useEffect} from 'react';
import { useSelector } from 'react-redux';
import {StyleSheet, Text, View, TouchableOpacity, Image, NativeEventEmitter} from 'react-native';
import KeepAwake from 'react-native-keep-awake';

import BleNative from './BleNative';
import {BleEvents} from './AppEvents';

import {setUiMessage} from './store';

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

function testAPICall() {
  fetch('https://reactnative.dev/movies.json', { method: 'GET'})
  .then((response) => response.json())
  .then((json) => {
    console.log(json.movies);
  })
  .catch((error) => {
    console.error(error);
  });
}

const EventManager = new NativeEventEmitter(BleNative);
const EventListeners = {};
EventListeners.printListener = null;
EventListeners.bleListener = null;
if (!EventListeners.printListener) {
  EventListeners.printListener = EventManager.addListener("EVENT_PRINT_LOG", (event) => {
    console.log("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ PRINT LOG EVENT RECEIVED FROM BACKEND");
    console.log(event);
    alert("Got event from backend Yaee -- " + new Date().getTime());
    //setUiMessage("Current time is - " + new Date().getTime() + "");
    //testAPICall();
    
  });
}
export default function App() {

  useEffect(() => {
    console.log(" ----------------- Component will mount ----------------------------");

    // if (!EventListeners.printListener) {
    //   EventListeners.printListener = EventManager.addListener("EVENT_PRINT_LOG", (event) => {
    //     console.log("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ PRINT LOG EVENT RECEIVED FROM BACKEND");
    //     console.log(event);
    //   });
    // }

    // if (!EventListeners.bleListener) {
    //   EventListeners.bleListener = EventManager.addListener("EVENT_BLE", (event) => {
    //     handleIncomingEvent(event);
    //   });
    // }

    // return function() {
    //   console.log(" ----------------- Component will UN-mount, event listners will get removed ----------------------------");

    //   if(EventListeners.printListener) {
    //     EventListeners.printListener.remove();
    //     EventListeners.printListener = null;
    //   }

    //   if(EventListeners.printListener) {
    //     EventListeners.bleListener.remove();
    //     EventListeners.bleListener = null;
    //   }
      
    // };
  });

  let msg = useSelector((state) => {
    
    try {
      return state.App.msg;
    }
    catch(e) {
      console.log(e);
    }

    return "Initial Message";
  });
  
  

  return (
    <View style={styles.container}>
      <KeepAwake />
      <View style={{...styles.view, backgroundColor: 'yellow', width: 400}}>
        <Text>{msg}</Text>
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
          onPress={emitEvent}>
          <Text style={styles.instructions}>Connect</Text>
        </TouchableOpacity>

        <TouchableOpacity
          style={styles.button}
          onPress={syncNow}>
          <Text style={styles.instructions}>Sync</Text>
        </TouchableOpacity>

        <TouchableOpacity
          style={styles.button}
          onPress={measureNow}>
          <Text style={styles.instructions}>Measure</Text>
        </TouchableOpacity>
      </View>
    </View>
  );
};

function startService() {
  console.log("Start Service requested");

  try {
    BleNative.startService();
  }
  catch (e) {
    console.error(e);
  }
}

function stopService() {
  console.log("Stop Service requested");

  try {
    BleNative.stopService();
  }
  catch (e) {
    console.error(e);
  }
}

async function emitEvent() {
  // BleNative.isServiceAlive((val) => {
  //   alert("IS SERVICE ALIVE - " + val);
  // });

  BleNative.testEmit();

}

async function connectBle() {
  let data = await BleNative.send(BleEvents.CONNECT);
  console.log(data);
}

async function measureNow() {
  let data = await BleNative.send(BleEvents.MEASURE);
  console.log(data);
}

async function syncNow() {
  let data = await BleNative.send(BleEvents.SYNC);
  console.log(data);
}

function handleIncomingEvent(event) {
  console.log("<============================= Received Incoming event =======================>");
  console.log(event);
  setUiMessage(event.data);
}
