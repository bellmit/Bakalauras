// This is to ensure that we can see the entirety of the store

export default {
	authorReducer: {
		authors: [],
	},

	tradesReducer: {
		trades: [],
	},

	selectedTradeReducer: {
		trade: undefined,
	},

	apiReducer: {
		apiCallsInProgress: 0,
	},
}
