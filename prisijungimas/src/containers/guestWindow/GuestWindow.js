import React from 'react';
import PropTypes from 'prop-types';
import { RegistrationForm } from '../../components/registration';
import { LoginForm } from '../../components/login';

function GuestWindow(props) {
  const { window, IsAdmin, setError, openSnackbar, switchWindow, switchHover, hover } = props;
  if (window) {
    return (
      <RegistrationForm
        setError={setError}
        openSnackbar={openSnackbar}
        switchWindow={switchWindow}
        switchHover={switchHover}
        hover={hover}
      />
    );
  }
  return (
    <LoginForm
      IsAdmin={IsAdmin}
      setError={setError}
      openSnackbar={openSnackbar}
      switchWindow={switchWindow}
      switchHover={switchHover}
      hover={hover}
    />
  );
}

GuestWindow.propTypes = {
  window: PropTypes.bool.isRequired,
  IsAdmin: PropTypes.bool.isRequired,
  setError: PropTypes.func.isRequired,
  openSnackbar: PropTypes.func.isRequired,
  switchWindow: PropTypes.func.isRequired,
  hover: PropTypes.bool.isRequired,
  switchHover: PropTypes.func.isRequired
};

export default GuestWindow;
