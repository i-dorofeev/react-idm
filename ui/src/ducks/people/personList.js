// Actions
const DATA_SET = 'reactIdm/people/personList/DATA_SET';

// Reducer
export default function personList(state = [], action) {
    switch (action.type) {
        case DATA_SET:
            return action.data;
        default:
            return state;
    }
}

// Action creators
export function setData(data) {
    return { type: DATA_SET, data }
}


