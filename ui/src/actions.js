import fetch from 'isomorphic-fetch';


export const REFRESH_PERSON_TABLE = 'REFRESH_PERSON_TABLE';

export function refreshPersonTable(data) {
    return { type: REFRESH_PERSON_TABLE, data }
}

export function fetchPeople() {

    return function(dispatch) {

        fetch('http://localhost:8080/people')
            .then(response => response.json())
            .then(json => dispatch(refreshPersonTable(json)));
    }

}
