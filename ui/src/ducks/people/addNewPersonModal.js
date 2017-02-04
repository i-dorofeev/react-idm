// Actions
const SHOW = 'reactIdm/people/addNewPersonModal/SHOW';
const HIDE = 'reactIdm/people/addNewPersonModal/HIDE';
const FIRST_NAME_SET = 'reactIdm/people/addNewPersonModal/FIRST_NAME_SET';
const LAST_NAME_SET = 'reactIdm/people/addNewPersonModal/LAST_NAME_SET';

let defaultState = {
    isActive: false,
    firstName: '',
    lastName: ''
};

// Reducer
export default function (state = defaultState, action) {
    switch (action.type) {
        case SHOW:
            return Object.assign({}, state, {
                isActive: true,
                firstName: '',
                lastName: ''
            });

        case HIDE:
            return Object.assign({}, state, {
                isActive: false
            });

        case FIRST_NAME_SET:
            return Object.assign({}, state, {
                firstName: action.value
            });

        case LAST_NAME_SET:
            return Object.assign({}, state, {
                lastName: action.value
            });

        default:
            return state;
    }
}

// Action creators
export function showModal() {
    return { type: SHOW };
}

export function hideModal() {
    return { type: HIDE };
}

export function setFirstName(firstName) {
    return {
        type: FIRST_NAME_SET,
        value: firstName
    };
}

export function setLastName(lastName) {
    return {
        type: LAST_NAME_SET,
        value: lastName
    };
}
