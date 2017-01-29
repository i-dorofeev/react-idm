import React from 'react';
import { connect } from 'react-redux';
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

// Connected components
const mapStateToPersonTableProps = (state) => {
    return {
        data: state.people.personList
    };
};

export default connect(mapStateToPersonTableProps)(PersonTable);

