// Actions
const MODAL_SHOW = 'MODAL_SHOW';
const MODAL_HIDE = 'MODAL_HIDE';

let defaultState = {
    isActive: false
};

// Reducer
export default function (state = defaultState, action) {
    switch (action.type) {
        case MODAL_SHOW:
            return Object.assign({}, state, { isActive: true });
        case MODAL_HIDE:
            return Object.assign({}, state, { isActive: false});
        default:
            return state;
    }
}

// Action creators
export function showModal() {
    return { type: MODAL_SHOW };
}

export function hideModal() {
    return { type: MODAL_HIDE };
}
