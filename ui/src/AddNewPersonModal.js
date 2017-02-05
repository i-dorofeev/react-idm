import React from 'react';
import { connect } from 'react-redux';

import { Button, Modal, FormGroup, FormControl, ControlLabel } from 'react-bootstrap';
import {setFirstName, setLastName, hideModal} from "./ducks/people/addNewPersonModal";
import {addNewPerson} from "./api";

class AddNewPersonModal extends React.Component {

    updateFirstName = (e) => {
        this.props.setFirstName(e.target.value);
    };

    updateLastName = (e) => {
        this.props.setLastName(e.target.value);
    };

    save = () => {
        addNewPerson(this.props.firstName, this.props.lastName)
            .then(() => this.props.onNewPersonAdded());
    };

    render() {
        return (
            <Modal show={this.props.isActive} onHide={this.props.onClose}>
                <Modal.Header closeButton>
                    <Modal.Title>Add new person</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <form>
                        <FormGroup>
                            <ControlLabel>First name</ControlLabel>
                            <FormControl
                                type="text"
                                placeholder="Enter first name"
                                value={this.props.firstName}
                                onChange={this.updateFirstName}
                            />
                        </FormGroup>

                        <FormGroup>
                            <ControlLabel>Last name</ControlLabel>
                            <FormControl
                                type="text"
                                placeholder="Enter last name"
                                value={this.props.lastName}
                                onChange={this.updateLastName} />
                        </FormGroup>
                    </form>
                </Modal.Body>
                <Modal.Footer>
                    <Button onClick={() => this.save()} bsStyle="primary">Save</Button>
                    <Button onClick={this.props.onClose}>Cancel</Button>
                </Modal.Footer>
            </Modal>
        );
    }
}

AddNewPersonModal.propTypes = {
    isActive: React.PropTypes.bool,
    firstName: React.PropTypes.string,
    lastName: React.PropTypes.string,
    setFirstName: React.PropTypes.func,
    setLastName: React.PropTypes.func,
    onClose: React.PropTypes.func,
    onNewPersonAdded: React.PropTypes.func
};


const mapStateToProps = (state) => {
    let localState = state.people.addNewPersonModal;
    return {...localState};
};

const mapDispatchToProps = (dispatch) => {
    return {
        setFirstName: (firstName) => { dispatch(setFirstName(firstName)); },
        setLastName:  (lastName) =>  { dispatch(setLastName (lastName )); },
        onClose: () => { dispatch(hideModal()); }
    }
};

export default connect(
    mapStateToProps,
    mapDispatchToProps
)(AddNewPersonModal);