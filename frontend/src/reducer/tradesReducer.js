import _ from 'lodash'
import * as ActionType from '../action/ActionType'
import initialState from './initialState'

const tradesReducer = (state = initialState.tradesReducer, action) => {
	switch (action.type) {
		case ActionType.GET_TRADES_RESPONSE: {
			// '...' spread operator clones the state
			// lodash Object assign simply clones action.trades into a new array.
			// The return object is a copy of state and overwrites the state.trades with a fresh clone of action.trades
			return {
				...state,
				trades: _.assign(action.trades),
			}
		}

		default: {
			return state
		}
	}
}

export default tradesReducer
