import React from 'react';
import { Component } from 'react';
import { connect } from 'react-redux';

import { Button, Grid } from 'react-bootstrap';

import PersonTable from './PersonTable';
import AddNewPersonModal from './AddNewPersonModal';
import { showModal } from './ducks/people/addNewPersonModal';

class People extends Component {

    render() { return (
        <Grid>
            <Button onClick={this.props.showAddNewPersonModal}>New person...</Button>
            <PersonTable />
            <AddNewPersonModal />
        </Grid>
        )
    }
}

const mapDispatchToPeopleProps = (dispatch) => {
    return {
        showAddNewPersonModal: () => { dispatch(showModal()); }
    };
};

export default connect(null, mapDispatchToPeopleProps)(People);





