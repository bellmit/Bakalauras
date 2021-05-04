import axios from 'axios';
// eslint-disable-next-line no-unused-vars
import {userLogin, getUser, headers} from '../utils/constants/api';

export const setAuthToken = token => {
  if (token) {
    axios.defaults.headers.common.Authorization = token;
  } else {
    delete axios.defaults.headers.common.Authorization;
  }
};

// export const getUserLogin = () => {
//   return axios.get(userLogin).then(res => res.data);
// };

export const validateUsername = username => {
  return axios
    .get(`${getUser}${username}`, headers)
    .then(res => res.data)
    .catch(err => {
      return err;
    });
};
