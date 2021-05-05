/* eslint-disable */
import React from 'react'
// import PropTypes from 'prop-types'
import { connect } from 'react-redux'
import toastr from 'toastr'
// import { getTrades} from '../../action/TradeAction'
import { bindActionCreators } from 'redux';
import * as tradeAction from '../../action/TradeAction'

import TradeList from './TradeList'

export class TradeListContainer extends React.Component {
	constructor(props) {
		super(props)

		this.state = { selectedTradeId: undefined }
	}

	// componentDidMount() {
	// 	getTrades()
	// 		.catch((error) => {
	// 			toastr.error(error)
	// 	})
	// }

	componentDidMount() {
		this.props.action.getTrades()
			.catch((error) => {
				toastr.error(error)
			})
	}

	handleAddTrade = () => {
		this.props.history.push('/Trade')
	}

	handleEditTrade = () => {
		const { selectedTradeId } = this.state

		if (selectedTradeId) {
			this.setState({ selectedTradeId: undefined })
			this.props.history.push(`/Trade/${selectedTradeId}`)
		}
	}

	handleDelete = () => {
		const { selectedTradeId } = this.state

		if (selectedTradeId) {
			this.setState({ selectedTradeId: undefined })
			this.props.action.deleteTradeAction(selectedTradeId).catch((error) => {
				toastr.error(error)
			})
		}
	}

	handleRowSelect = (row, isSelected) => {
		if (isSelected) {
			this.setState({ selectedTradeId: row.id })
		}
	}

	render() {
		const { trades } = this.props
		if (!trades) {
			return <div>Loading...</div>
		}

		return (
			<div className='container-fluid'>
				<div className='row mt-3'>
					<div className='col'>
						<h1>Trades</h1>
					</div>
				</div>

				<div className='row mt-3'>
					<div className='col'>
						<div className='btn-group' role='group'>
							<button
								type='button'
								className='btn btn-primary'
								onClick={this.handleAddTrade}
							>
								<i className='fa fa-plus' aria-hidden='true' /> Create
							</button>

							<button
								type='button'
								className='btn btn-warning ml-2'
								onClick={this.handleEditTrade}
							>
								<i className='fa fa-pencil' aria-hidden='true' /> Update
							</button>

							<button
								type='button'
								className='btn btn-danger ml-2'
								onClick={this.handleDelete}
							>
								<i
									className='fa fa-trash-o'
									aria-hidden='true'
									onClick={this.handleDelete}
								/>{' '}
								Delete
							</button>
						</div>
					</div>
				</div>

				<div className='row'>
					<div className='col'>
						<TradeList trades={trades} handleRowSelect={this.handleRowSelect} />
					</div>
				</div>
			</div>
		)
	}
}

const mapStateToProps = (state) => ({
	trades: state.tradesReducer.trades,
})

const mapDispatchToProps = (dispatch) => ({
	action: bindActionCreators(tradeAction, dispatch),
})

// const mapDispatchToProps = dispatch => {
// 	return {
// 		getTradesActionProp: () =>
// 			getTradesAction()(dispatch)
// 	};
// };

// TradeListContainer.propTypes = {
// 	trades: PropTypes.array,
// 	tradeAction: PropTypes.object.isRequired,
// 	history: PropTypes.object.isRequired,
// }

// export default connect(mapStateToProps)(TradeListContainer)
export default connect(mapStateToProps, mapDispatchToProps)(TradeListContainer)
