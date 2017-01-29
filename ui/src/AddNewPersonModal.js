import React from 'react';
import { Component } from 'react';
import { connect } from 'react-redux';

import { Button, Modal } from 'react-bootstrap';

import { hideModal } from './ducks/people/addNewPersonModal';

class AddNewPersonModal extends Component {

    render() {
        return (
            <Modal show={this.props.isActive}>
                <Modal.Header closeButton>
                    <Modal.Title>Add new person</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <p>Hello!</p>
                </Modal.Body>
                <Modal.Footer>
                    <Button bsStyle="primary">Save</Button>
                    <Button onClick={this.props.hide}>Cancel</Button>
                </Modal.Footer>
            </Modal>
        );
    }
}

const mapStateToAddNewPersonModalProps = (state) => {
    return {
        isActive: state.people.addNewPersonModal.isActive
    }
};

const mapDispatchToAddNewPersonModalProps = (dispatch) => {
    return {
        hide: () => {
            dispatch(hideModal());
        }
    };
};

export default connect(
    mapStateToAddNewPersonModalProps,
    mapDispatchToAddNewPersonModalProps
)(AddNewPersonModal);