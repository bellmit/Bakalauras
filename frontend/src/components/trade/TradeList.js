import React from 'react'
import PropTypes from 'prop-types'
import { BootstrapTable, TableHeaderColumn } from 'react-bootstrap-table-next'

const getCaret = (direction) => {
	if (direction === 'asc') {
		return (
			<span>
				{' '}
				<i className='fa fa-sort-asc' aria-hidden='true' />
			</span>
		)
	}

	if (direction === 'desc') {
		return (
			<span>
				{' '}
				<i className='fa fa-sort-desc' aria-hidden='true' />
			</span>
		)
	}

	return (
		<span>
			{' '}
			<i className='fa fa-sort' aria-hidden='true' />
		</span>
	)
}

class TradeList extends React.Component {
	constructor(props) {
		super(props)

		this.options = {
			sortIndicator: true,
			noDataText: 'No data',
		}

		this.selectRowProp = {
			mode: 'radio',
			bgColor: '#c1f291',
			onSelect: props.handleRowSelect,
			clickToSelect: true,
			hideSelectColumn: true,
		}
	}

	render() {
		return (
			<BootstrapTable
				data={this.props.trades}
				selectRow={this.selectRowProp}
				options={this.options}
				bordered={false}
				striped
				hover
				condensed
			>
				<TableHeaderColumn dataField='id' isKey hidden>
					Id
				</TableHeaderColumn>

				<TableHeaderColumn
					dataField='ticker'
					dataSort
					caretRender={getCaret}
					filter={{ type: 'TextFilter', delay: 0 }}
				>
					Ticker
				</TableHeaderColumn>

				<TableHeaderColumn
					dataField='type'
					dataSort
					caretRender={getCaret}
					filter={{ type: 'TextFilter', delay: 0 }}
				>
					Type
				</TableHeaderColumn>

				<TableHeaderColumn
					dataField='price'
					dataSort
					caretRender={getCaret}
					filter={{ type: 'TextFilter', delay: 0 }}
				>
					Price
				</TableHeaderColumn>

				<TableHeaderColumn
					dataField='currentPrice'
					dataSort
					caretRender={getCaret}
					filter={{ type: 'TextFilter', delay: 0 }}
				>
					Current Price
				</TableHeaderColumn>

				<TableHeaderColumn
					dataField='quantity'
					dataSort
					caretRender={getCaret}
					filter={{ type: 'TextFilter', delay: 0 }}
				>
					Quantity
				</TableHeaderColumn>

				<TableHeaderColumn
					dataField='currency'
					dataSort
					caretRender={getCaret}
					filter={{ type: 'TextFilter', delay: 0 }}
				>
					Currency
				</TableHeaderColumn>

				<TableHeaderColumn
					dataField='dateValid'
					dataSort
					caretRender={getCaret}
					filter={{ type: 'TextFilter', delay: 0 }}
				>
					Expires at
				</TableHeaderColumn>

				<TableHeaderColumn
					dataField='datePlaced'
					dataSort
					caretRender={getCaret}
					filter={{ type: 'TextFilter', delay: 0 }}
				>
					Created
				</TableHeaderColumn>
			</BootstrapTable>
		)
	}
}

TradeList.propTypes = {
	trades: PropTypes.array.isRequired,
	handleRowSelect: PropTypes.func.isRequired,
}

export default TradeList
