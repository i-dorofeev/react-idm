import {REFRESH_PERSON_TABLE} from "./actions";

let defaultState = {
    "people": []
}

export function reducer(state = defaultState, action) {
    switch (action.type) {
        case REFRESH_PERSON_TABLE:
            return Object.assign({}, state, { people: action.data });
        default:
            return state;
    }
}

