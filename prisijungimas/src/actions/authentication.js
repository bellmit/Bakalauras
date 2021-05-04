import axios from 'axios';
import jwtDecode from 'jwt-decode';
import { userLogin, userRegister, snackbarMessages, headers } from '../utils/constants';
import { SET_CURRENT_USER, LOGOUT } from './types';
import { setAuthToken, validateUsername } from './usersAction';

axios.defaults.baseURL = process.env.HOST;

export const setCurrentUser = decoded => {
  return {
    type: SET_CURRENT_USER,
    payload: decoded
  };
};

export const loginUser = (user, openSnackbar, setError) => dispatch => {
  axios
    .post(userLogin, user, headers)
    // logged in
    .then(res => {
      localStorage.setItem('jwtToken', res.data);
      setAuthToken(res.data);
      validateUsername(jwtDecode(res.data).username).then(userData => {
        dispatch(setCurrentUser(userData));
      });
      openSnackbar({
        message: snackbarMessages.loginSuccess,
        variant: 'success'
      });
    })
    // not logged in
    .catch(err => {
      const errorData = {
        Status: err.response.status,
        Message: err.response.data
      };
      setError(errorData);
    });
};

export const logoutUser = dispatch => {
  localStorage.removeItem('jwtToken');
  setAuthToken(false);
  dispatch({ type: LOGOUT });
};

export const registerUser = (user, openSnackbar, setError, switchWindow) => {
  axios
    .post(userRegister, user)
    .then(() => {
      openSnackbar({ message: snackbarMessages.registrationSuccess, variant: 'success' });
      switchWindow();
    })
    .catch(err => {
      const errorData = {
        Status: err.response.status,
        Message: err.response.data
      };
      setError(errorData);
    });
};
