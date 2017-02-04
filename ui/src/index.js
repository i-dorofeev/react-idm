import 'babel-polyfill'

import React from 'react';
import ReactDOM from 'react-dom';
import { createStore, applyMiddleware, combineReducers} from 'redux';
import { Provider } from 'react-redux';
import thunk from 'redux-thunk';

import People from './People';
import Header from './Header';

import * as reducers from './ducks/index';

let reducer = combineReducers(reducers);

let store = createStore(
    reducer,
    applyMiddleware(thunk)
);

ReactDOM.render(
    <Provider store={store}>
        <div>
            <Header />
            <People />
        </div>
    </Provider>,
    document.getElementById('root')
);
