const data = {
    backgroundState : "Unknown", 
    heartbeatCount: 0,
    hasStarted : false,
    appComponentMounted : false,
    startTask: function () {
    return;
      if(DATA.hasStarted)
        return;

      setTimeout(() => {
        data.startAsyncTask();
      }, 10);
    },
    startAsyncTask : function () {
      let i = 0;
      data.hasStarted = true;
      while(true) {
        console.log("Counting blocking task " + i++);
      }
      data.hasStarted = false; 
    }
  };


export const DATA=data;