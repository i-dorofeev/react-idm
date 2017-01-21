import React from 'react';

const PersonTableRow = ({ firstName, lastName }) => (
    <tr>
        <td>{firstName}</td>
        <td>{lastName}</td>
    </tr>
);

export default PersonTableRow;
