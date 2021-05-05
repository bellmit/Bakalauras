import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'
import toastr from 'toastr'
import jwtDecode from 'jwt-decode'
import * as tradeAction from '../../action/TradeAction'
import TradeForm from './TradeForm'
import { authorsFormattedForDropdown } from '../../selectors/selectors'

export class AddOrEditTradeContainer extends React.Component {
	componentDidMount() {
		const { match, action } = this.props
		if (match.params.id) {
			action.getTradeAction(match.params.id).catch((error) => {
				toastr.error(error)
			})
		}
	}

	handleSave = (values) => {
		const { match, action, history } = this.props

		const token = localStorage.getItem('jwtToken')
		const user = jwtDecode(token)

		const trade = {
			id: match.params.id,
			ticker: values.ticker,
			type: values.type,
			price: values.price,
			currentPrice: values.currentPrice,
			quantity: values.quantity,
			currency: values.currency,
			dateValid: values.dateValid,
			datePlaced: values.datePlaced,
			investorsID: user.id,
		}

		// values.id undefined -> create else update
		// TODO ???

		action
			.saveTradeAction(trade)
			.then(() => {
				toastr.success('Trade saved')
				history.push('/Trades')
			})
			.catch((error) => {
				toastr.error(error)
			})
	}

	handleCancel = (event) => {
		const { history } = this.props
		event.preventDefault()
		history.replace('/Trades')
	}

	render() {
		const { initialValues, match } = this.props

		const heading = match.params.id ? 'Update' : 'Create'

		return (
			<div className='container'>
				<TradeForm
					heading={heading}
					handleSave={this.handleSave}
					handleCancel={this.handleCancel}
					initialValues={initialValues}
				/>
			</div>
		)
	}
}

const mapStateToProps = (state, ownProps) => {
	const tradeId = ownProps.match.params.id // from the path '/Trades/:id'

	if (
		tradeId &&
		state.selectedTradeReducer.trade &&
		tradeId === state.selectedTradeReducer.trade.id
	) {
		return {
			initialValues: state.selectedTradeReducer.trade,
		}
	}
	return {
		authors: authorsFormattedForDropdown(state.authorReducer.authors),
	}
}

const mapDispatchToProps = (dispatch) => ({
	action: bindActionCreators({ ...tradeAction }, dispatch),
})

AddOrEditTradeContainer.propTypes = {
	action: PropTypes.object.isRequired,
	history: PropTypes.object.isRequired,
	initialValues: PropTypes.object.isRequired,
	match: PropTypes.object.isRequired,
}

export default connect(
	mapStateToProps,
	mapDispatchToProps
)(AddOrEditTradeContainer)
