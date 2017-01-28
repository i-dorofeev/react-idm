import { combineReducers} from 'redux';
import * as People from './people/index';

//noinspection JSUnusedGlobalSymbols
export const people = combineReducers(People);

