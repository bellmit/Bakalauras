import {applyMiddleware, createStore, compose} from 'redux';
import thunk from 'redux-thunk';
import rootReducer from './reducer';


const composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;


const configureStore = initialState => {
    return createStore(
        rootReducer,
        initialState,
//         applyMiddleware(thunk)
        composeEnhancers(applyMiddleware(thunk))
    );
};



export default configureStore;