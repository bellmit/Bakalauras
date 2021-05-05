import * as ActionType from './ActionType'
import TradeApi from '../api/TradeApi'
import { ApiCallBeginAction, ApiCallErrorAction } from './ApiAction'

export const getTradesResponse = (trades) => ({
	type: ActionType.GET_TRADES_RESPONSE,
	trades,
})

export function getTradesAction() {
	return (dispatch) => {
		dispatch(ApiCallBeginAction())

		return TradeApi.getAllTrades()
			.then((trades) => {
				dispatch(getTradesResponse(trades))
			})
			.catch((error) => {
				throw error
			})
	}
}

export const addNewTradeResponse = () => ({
	type: ActionType.ADD_NEW_TRADE_RESPONSE,
})

export const updateExistingTradeResponse = () => ({
	type: ActionType.UPDATE_EXISTING_TRADE_RESPONSE,
})

export function saveTradeAction(tradeBeingAddedOrEdited) {
	return function (dispatch) {
		dispatch(ApiCallBeginAction())

		// if authorId exists, it means that the trade is being edited, therefore update it.
		// if authorId doesn't exist, it must therefore be new trade that is being added, therefore add it
		return TradeApi.saveTrade(tradeBeingAddedOrEdited)
			.then(() => {
				if (tradeBeingAddedOrEdited.id) {
					dispatch(updateExistingTradeResponse())
				} else {
					dispatch(addNewTradeResponse())
				}
			})
			.then(() => {
				dispatch(getTradesAction())
			})
			.catch((error) => {
				dispatch(ApiCallErrorAction())
				throw error
			})
	}
}

export const getTradeResponse = (tradeFound) => ({
	type: ActionType.GET_TRADE_RESPONSE,
	trade: tradeFound,
})

export function getTradeAction(tradeId) {
	return (dispatch) => {
		dispatch(ApiCallBeginAction())

		return TradeApi.getTrade(tradeId)
			.then((trade) => {
				dispatch(getTradeResponse(trade))
			})
			.catch((error) => {
				throw error
			})
	}
}

export const deleteTradeResponse = () => ({
	type: ActionType.DELETE_TRADE_RESPONSE,
})

export function deleteTradeAction(tradeId) {
	return (dispatch) => {
		dispatch(ApiCallBeginAction())

		return TradeApi.deleteTrade(tradeId)
			.then(() => {
				dispatch(deleteTradeResponse())
			})
			.then(() => {
				dispatch(getTradesAction())
			})
			.catch((error) => {
				throw error
			})
	}
}
