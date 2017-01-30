import React from 'react';

const PersonTableRow = ({ firstName, lastName }) => (
    <tr>
        <td>{firstName}</td>
        <td>{lastName}</td>
    </tr>
);

PersonTableRow.propTypes = {
    firstName: React.PropTypes.string,
    lastName: React.PropTypes.string
};

export default PersonTableRow;
