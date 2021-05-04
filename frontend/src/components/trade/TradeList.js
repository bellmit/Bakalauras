import React, { PropTypes } from 'react';
import { BootstrapTable, TableHeaderColumn } from 'react-bootstrap-table';



const getCaret = direction => {
    if (direction === 'asc') {
        return (
            <span> <i className="fa fa-sort-asc" aria-hidden="true"/></span>
        );
    }

    if (direction === 'desc') {
        return (
            <span> <i className="fa fa-sort-desc" aria-hidden="true"/></span>
        );
    }

    return (
        <span> <i className="fa fa-sort" aria-hidden="true" /></span>
    );
};



class TradeList extends React.Component {

    constructor(props) {
        super(props);

        this.options = {
            sortIndicator: true,
            noDataText: 'No data'
        };

        this.selectRowProp = {
            mode: 'radio',
            bgColor: '#c1f291',
            onSelect: props.handleRowSelect,
            clickToSelect: true, 
            hideSelectColumn: true            
        };
    }



    render() {


        return (
            <BootstrapTable data={this.props.trades}  selectRow={this.selectRowProp}  options={this.options} bordered={false} striped hover condensed>
                <TableHeaderColumn dataField="id" isKey hidden>Id</TableHeaderColumn>

                <TableHeaderColumn 
                    dataField="ticker"
                    dataSort={true}
                    caretRender={getCaret}
                    filter={{type: 'TextFilter', delay: 0 }}
                >
                    Ticker
                </TableHeaderColumn>

                <TableHeaderColumn
                    dataField="type"
                    dataSort={true}
                    caretRender={getCaret}
                    filter={{type: 'TextFilter', delay: 0 }}
                >
                    Type
                </TableHeaderColumn>

                <TableHeaderColumn
                    dataField="price"
                    dataSort={true}
                    caretRender={getCaret}
                    filter={{type: 'TextFilter', delay: 0 }}
                >
                    Price
                </TableHeaderColumn>

                <TableHeaderColumn
                    dataField="currentPrice"
                    dataSort={true}
                    caretRender={getCaret}
                    filter={{type: 'TextFilter', delay: 0 }}
                >
                    Current Price
                </TableHeaderColumn>

                <TableHeaderColumn
                    dataField="quantity"
                    dataSort={true}
                    caretRender={getCaret}
                    filter={{type: 'TextFilter', delay: 0 }}
                >
                    Quantity
                </TableHeaderColumn>

                <TableHeaderColumn
                    dataField="currency"
                    dataSort={true}
                    caretRender={getCaret}
                    filter={{type: 'TextFilter', delay: 0 }}
                >
                    Currency
                </TableHeaderColumn>

                <TableHeaderColumn
                    dataField="dateValid"
                    dataSort={true}
                    caretRender={getCaret}
                    filter={{type: 'TextFilter', delay: 0 }}
                >
                    Expires at
                </TableHeaderColumn>

                <TableHeaderColumn
                    dataField="datePlaced"
                    dataSort={true}
                    caretRender={getCaret}
                    filter={{type: 'TextFilter', delay: 0 }}
                >
                    Created
                </TableHeaderColumn>

            </BootstrapTable>
        );
    }

}



TradeList.propTypes = {
    trades: PropTypes.array.isRequired,
    handleRowSelect: PropTypes.func.isRequired
};



export default TradeList;
