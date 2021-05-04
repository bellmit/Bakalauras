import React, { PropTypes } from 'react';
import { Field, reduxForm } from 'redux-form';
import FieldInput from '../common/FieldInput';
import { connect } from 'react-redux';
import { getTradeResponse as getTrade } from "../../action/TradeAction";


let TradeForm = ({ handleSubmit, pristine, reset, submitting, heading, handleSave, handleCancel }) => {
    return (
        <form onSubmit={handleSubmit(handleSave)}>
            <h1>{heading}</h1>

            <Field
                type="text"
                name="ticker"
                label="Ticker"
                placeholder="Company ticker"
                component={FieldInput}
            />

            <Field
                type="text"
                name="type"
                label="Type"
                placeholder="Buy or Sell"
                component={FieldInput}
            />

            <Field
                type="text"
                name="price"
                label="Price"
                placeholder="Price"
                component={FieldInput}
            />

            <Field
                type="text"
                name="currentPrice"
                label="Current Price"
                placeholder="Current Price"
                component={FieldInput}
            />

            <Field
                type="text"
                name="quantity"
                label="Quantity"
                placeholder="Quantity"
                component={FieldInput}
            />

            <Field
                type="text"
                name="currency"
                label="Currency"
                placeholder="Currency"
                component={FieldInput}
            />

            <Field
                type="text"
                name="dateValid"
                label="Expires at"
                placeholder="Expire date"
                component={FieldInput}
            />

            <Field
                type="text"
                name="datePlaced"
                label="Created"
                placeholder="Create date"
                component={FieldInput}
            />

            <div>
                <button type="submit" disabled={submitting} className="btn btn-primary"><i className="fa fa-paper-plane-o" aria-hidden="true" /> Submit</button>

                {heading === 'Create' && <button type="button" disabled={pristine || submitting} onClick={reset} className="btn btn-default btn-space">Clear Values</button>}

                <button type="button" className="btn btn-default btn-space" onClick={handleCancel}>Cancel</button>
            </div>
        </form>
    );
};




//TODO
const validate = values => {
    const errors = {};

    if (!values.ticker) {
        errors.ticker = 'Required';
    }

    if (!values.category) {
        errors.category = 'Required';
    }

    if (!values.length) {
        errors.length = 'Required';
    }

    if (!values.authorId) {
        errors.authorId = 'Required';
    }

    return errors;
};



TradeForm.propTypes = {
    handleSubmit: PropTypes.func.isRequired,
    pristine: PropTypes.bool.isRequired,
    reset: PropTypes.func.isRequired,
    submitting: PropTypes.bool.isRequired,
    heading: PropTypes.string.isRequired,
    handleSave: PropTypes.func.isRequired,
    handleCancel: PropTypes.func.isRequired
};

TradeForm = reduxForm({
    form: 'TradeForm',
    validate
})(TradeForm)

// TODO destroy state
TradeForm = connect(
    state => ({
        initialValues: state.selectedTradeReducer.trade
    }),
    { getTradeResponse: getTrade }
)(TradeForm)

export default TradeForm;
