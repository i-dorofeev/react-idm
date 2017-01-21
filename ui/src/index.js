import React from 'react';
import ReactDOM from 'react-dom';
import { createStore } from 'redux';
import { Provider } from 'react-redux';
import App from './App';
import './index.css';
import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap/dist/css/bootstrap-theme.css';

let defaultState = {
    "people": [
        {
            "id": "1",
            "firstName": "Ilya",
            "lastName": "Dorofeev"
        },
        {
            "id": "2",
            "firstName": "Boris",
            "lastName": "Romanov"
        }
    ]
}

function reducer(state = defaultState, action) {
    switch (action.type) {
        default:
            return state;
    }
}


let store = createStore(reducer);

ReactDOM.render(
    <Provider store={store}>
        <App />
    </Provider>,
  document.getElementById('root')
);
