import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import Button from '@material-ui/core/Button';
import Styles from './Styles';
import { GuestWindow } from '../guestWindow';

function HomeWindow(props) {
  const {
    classes,
    window,
    auth,
    handleLogout,
    setError,
    openSnackbar,
    switchWindow,
    switchHover,
    hover
  } = props;
  if (auth.isAuthenticated) {
    return (
      <div className={classes.paper}>
        <div className={classes.email}>Hello, {auth.user.email}!</div>
        <Button className={classes.button} variant="outlined" onClick={e => handleLogout(e)}>
          Logout
        </Button>
      </div>
    );
  }
  return (
    <GuestWindow
      window={window}
      IsAdmin={auth.user.isAdmin !== undefined ? auth.user.isAdmin : false}
      setError={setError}
      openSnackbar={openSnackbar}
      switchWindow={switchWindow}
      switchHover={switchHover}
      hover={hover}
    />
  );
}

HomeWindow.propTypes = {
  classes: PropTypes.shape().isRequired,
  window: PropTypes.bool.isRequired,
  auth: PropTypes.shape().isRequired,
  handleLogout: PropTypes.func.isRequired,
  setError: PropTypes.func.isRequired,
  openSnackbar: PropTypes.func.isRequired,
  switchWindow: PropTypes.func.isRequired,
  hover: PropTypes.bool.isRequired,
  switchHover: PropTypes.func.isRequired
};

export default withStyles(Styles)(HomeWindow);
