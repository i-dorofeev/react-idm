import React from 'react';
import PersonTableRow from './PersonTableRow';

const PersonTable = ({ data }) => (
    <table className="table">
        <thead>
            <tr>
                <th>First name</th>
                <th>Last name</th>
            </tr>
        </thead>

        <tbody>
        {data.map(item =>
            <PersonTableRow key={item.id} {...item} />
        )}
        </tbody>
    </table>
);

export default PersonTable;

