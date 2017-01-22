import React from 'react';
import { connect } from 'react-redux';
import PersonTableRow from './PersonTableRow';

const mapStateToProps = (state) => {
    return { "data": state.people };
};

const _PersonTable = ({ data }) => (
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

const PersonTable = connect(mapStateToProps)(_PersonTable);

export default PersonTable;

