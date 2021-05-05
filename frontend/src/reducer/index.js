import { combineReducers } from 'redux'
import { reducer as formReducer } from 'redux-form'
import tradesReducer from './tradesReducer'
import selectedTradeReducer from './selectedTradeReducer'
import authorReducer from './authorReducer'
import apiReducer from './apiReducer'

export default combineReducers({
	tradesReducer,
	selectedTradeReducer,
	authorReducer,
	apiReducer,
	form: formReducer,
})
