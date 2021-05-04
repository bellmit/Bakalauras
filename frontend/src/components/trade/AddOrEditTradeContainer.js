import React, { PropTypes } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import toastr from 'toastr';
import * as tradeAction from '../../action/TradeAction';
import TradeForm from './TradeForm'; // eslint-disable-line import/no-named-as-default
import { authorsFormattedForDropdown } from '../../selectors/selectors';
import jwtDecode from "jwt-decode"; // eslint-disable-line import/no-named-as-default



export class AddOrEditTradeContainer extends React.Component {

    componentDidMount() {
        if (this.props.match.params.id) {
            this.props.action.getTradeAction(this.props.match.params.id)
                .catch(error => {
                    toastr.error(error);
                });
        }
    }

    handleSave = (values) => {

        const token = localStorage.getItem('jwtToken')
        const user = jwtDecode(token)

        const trade = {
            id: this.props.match.params.id,
            // ID: this.props.match.params.id,
            ticker: values.ticker,
            type: values.type,
            price: values.price,
            currentPrice: values.currentPrice,
            quantity: values.quantity,
            currency: values.currency,
            dateValid: values.dateValid,
            datePlaced: values.datePlaced,
            investorsID: user.id
        };

        // values.id undefined -> create else update
        //TODO ???

        this.props.action.saveTradeAction(trade)
            .then(() => {
                toastr.success('Trade saved');
                this.props.history.push('/Trades');
            }).catch(error => {
                toastr.error(error);
            });
    }

    handleCancel = (event) => {
        event.preventDefault();
        this.props.history.replace('/Trades');
    }


    render() {
        const { initialValues } = this.props;

        const heading = this.props.match.params.id ? 'Update' : 'Create'

        return (
            <div className="container">
                <TradeForm
                    heading={heading}
                    handleSave={this.handleSave}
                    handleCancel={this.handleCancel}
                    initialValues={initialValues}
                />
            </div>
        );
    }
}



const mapStateToProps = (state, ownProps) => {
    const tradeId = ownProps.match.params.id; //from the path '/Trades/:id'

    if (tradeId && state.selectedTradeReducer.trade && tradeId === state.selectedTradeReducer.trade.id) {
        return {
            initialValues: state.selectedTradeReducer.trade,
        };
    } else {
        return {
            authors: authorsFormattedForDropdown(state.authorReducer.authors)
        };
    }
};



const mapDispatchToProps = dispatch => ({
    action: bindActionCreators({ ...tradeAction }, dispatch)
});



AddOrEditTradeContainer.propTypes = {
    action: PropTypes.object.isRequired,
    history: PropTypes.object,
    initialValues: PropTypes.object,
    match: PropTypes.object.isRequired
};



export default connect(mapStateToProps, mapDispatchToProps)(AddOrEditTradeContainer);
