/* eslint-disable prettier/prettier */
import {createStore, combineReducers} from 'redux';
import {createAction, handleActions} from 'redux-actions';

const appInitialState = {
  msg: 'No message to display',
};

const SET_UI_MESSAGE = 'SET_UI_MESSAGE';
export const setUiMessage = createAction(SET_UI_MESSAGE);

const App = handleActions(
  {
    [SET_UI_MESSAGE]: (state, {payload}) => {
      console.log(state);
      return payload ? {msg : payload} : {...state};
    },
  },
  appInitialState,
);

const rootReducer = combineReducers({
  App,
});

const configureStore = () => createStore(rootReducer);
export const store = configureStore();
