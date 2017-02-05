import React from 'react';
import { connect } from 'react-redux';

import { Button, Grid } from 'react-bootstrap';

import PersonTable from './PersonTable';
import AddNewPersonModal from './AddNewPersonModal';
import { showModal, hideModal } from './ducks/people/addNewPersonModal';
import { fetchPeople} from './api';
import { setData as refreshPersonList } from "./ducks/people/personList";

class People extends React.Component {

    componentDidMount() {
        this.props.onRefresh();
    }

    render() { return (
        <Grid>
            <Button onClick={this.props.onShowAddNewPersonModal}>New person...</Button>
            <PersonTable />
            <AddNewPersonModal onNewPersonAdded={this.props.onNewPersonAdded} />
        </Grid>
        )
    }
}

People.propTypes = {
    onRefresh: React.PropTypes.func,
    onShowAddNewPersonModal: React.PropTypes.func,
    onCloseAddNewPersonModal: React.PropTypes.func,
    onNewPersonAdded: React.PropTypes.func
};

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
        onNewPersonAdded: () => {
            dispatch(hideModal());
            dispatch(refresh());
        }
    };
};

export default connect(null, mapDispatchToPeopleProps)(People);





