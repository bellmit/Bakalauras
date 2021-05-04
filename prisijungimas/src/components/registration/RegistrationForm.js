import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import Form from './Form';
import Styles from './Styles';

function RegistrationForm(props) {
  const { classes, openSnackbar, setError, switchWindow, hover, switchHover } = props;
  const login = 'Already have an account?';

  return (
    <div className={classes.paper}>
      <div className={classes.container}>Sign Up</div>
      <Form
        classes={classes}
        setError={setError}
        openSnackbar={openSnackbar}
        switchWindow={switchWindow}
      />
      <div
        role="button"
        onClick={() => {
          switchWindow();
        }}
        className={hover ? classes.button2 : classes.button1}
        onMouseEnter={switchHover}
        onMouseLeave={switchHover}
      >
        {login}
      </div>
    </div>
  );
}

RegistrationForm.propTypes = {
  classes: PropTypes.shape().isRequired,
  openSnackbar: PropTypes.func.isRequired,
  switchWindow: PropTypes.func.isRequired,
  setError: PropTypes.func.isRequired,
  hover: PropTypes.bool.isRequired,
  switchHover: PropTypes.func.isRequired
};

export default withStyles(Styles)(RegistrationForm);
