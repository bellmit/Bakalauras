import * as ActionType from '../action/ActionType';
import initialState from './initialState';
import _ from 'lodash';


const selectedTradeReducer = (state = initialState.selectedTradeReducer, action) => {
    switch(action.type) {

        case ActionType.GET_TRADE_RESPONSE: {
            return {
                ...state,
                trade: _.assign(action.trade)
            };
        }


        default: { return state; }
    }
};


export default selectedTradeReducer;