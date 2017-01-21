import React, { Component } from 'react';

class PersonTable extends Component {
    render() {
        return (
            <table className="table">
                <thead>
                    <tr>
                        <th>First name</th>
                        <th>Last name</th>
                    </tr>
                </thead>

                <tbody>
                    <tr>
                        <td>Ilya</td>
                        <td>Dorofeev</td>
                    </tr>
                    <tr>
                        <td>Alexey</td>
                        <td>Hrennikov</td>
                    </tr>
                </tbody>
            </table>
        );
    }
}

export default PersonTable;

