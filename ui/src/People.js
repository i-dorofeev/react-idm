import React from 'react';
import { connect } from 'react-redux';

import { Button, Grid } from 'react-bootstrap';

import PersonTable from './PersonTable';
import AddNewPersonModal from './AddNewPersonModal';
import { showModal, hideModal } from './ducks/people/addNewPersonModal';
import { addNewPerson, fetchPeople} from './api';
import { setData as refreshPersonList } from "./ducks/people/personList";

class People extends React.Component {

    componentDidMount() {
        this.props.onRefresh();
    }

    render() { return (
        <Grid>
            <Button onClick={this.props.onShowAddNewPersonModal}>New person...</Button>
            <PersonTable />
            <AddNewPersonModal
                onSave={this.props.onSave}
                onClose={this.props.onCloseAddNewPersonModal} />
        </Grid>
        )
    }
}

People.propTypes = {
    onRefresh: React.PropTypes.func,
    onShowAddNewPersonModal: React.PropTypes.func,
    onCloseAddNewPersonModal: React.PropTypes.func,
    onSave: React.PropTypes.func
};

function saveNewPerson(firstName, lastName) {
    return function(dispatch) {
        addNewPerson(firstName, lastName)
            .then(() => dispatch(hideModal()))
            .then(() => dispatch(refresh()));
    }
}

function refresh() {
    return function (dispatch) {
        fetchPeople()
            .then(people => dispatch(refreshPersonList(people)));
    }
}

const mapDispatchToPeopleProps = (dispatch) => {
    return {
        onRefresh: () => { dispatch(refresh()); },
        onShowAddNewPersonModal: () => { dispatch(showModal()); },
        onCloseAddNewPersonModal: () => { dispatch(hideModal()); },
        onSave: (firstName, lastName) => { dispatch(saveNewPerson(firstName, lastName)); }
    };
};

export default connect(null, mapDispatchToPeopleProps)(People);





