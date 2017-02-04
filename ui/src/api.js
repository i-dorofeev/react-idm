function url(path) {
    return 'http://localhost:8080' + path;
}

function get(url) {
    return fetch(url)
        .then(response => response.json());
}

function put(url, payload) {
    return fetch(url, {
        method: 'put',
        headers: new Headers({
            'Content-Type': 'application/json'
        }),
        body: JSON.stringify(payload)
    });
}

export function fetchPeople() {
    return get(url('/people'));
}

export function addNewPerson(firstName, lastName) {
    return put(url('/people'), {
        firstName: firstName,
        lastName: lastName
    });
}