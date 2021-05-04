import React from 'react';
import { Provider } from 'react-redux';
import Store from '../utils/redux/store';
import MainBody from '../containers/mainBody/MainBody';

export default () => (
  <Provider store={Store}>
    <MainBody />
  </Provider>
);
