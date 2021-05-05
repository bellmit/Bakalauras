import { combineReducers } from 'redux';
import authReducer from '../../../frontend/src/reducer/authReducer';

export default combineReducers({
  auth: authReducer
});
