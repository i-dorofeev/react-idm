function url(path) {
    return 'http://localhost:8080/' + path;
}

export function fetchPeople() {
    return fetch(url('people'))
        .then(response => response.json())
}

export function addNewPerson(firstName, lastName) {
    return fetch(url('people'), {
        method: 'put',
        headers: new Headers({
            'Content-Type': 'application/json'
        }),
        body: JSON.stringify({
            firstName: firstName,
            lastName: lastName
            })
    });
}