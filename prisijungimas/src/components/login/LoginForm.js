import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import { connect } from 'react-redux';
import Form from './Form';
import Styles from './Styles';
import { loginUser } from '../../actions/authentication';

function LoginForm(props) {
  const {
    loginUserProp,
    setError,
    openSnackbar,
    classes,
    switchWindow,
    hover,
    switchHover
  } = props;
  const register = "Don't have an account?";

  const handleLogin = user => {
    loginUserProp(user, openSnackbar, setError);
  };

  return (
    <div className={classes.paper}>
      <div className={classes.container}>Sign In</div>
      <Form classes={classes} onSubmit={handleLogin} />
      <div
        role="button"
        onClick={() => {
          switchWindow();
        }}
        className={hover ? classes.button2 : classes.button1}
        onMouseEnter={switchHover}
        onMouseLeave={switchHover}
      >
        {register}
      </div>
    </div>
  );
}

LoginForm.propTypes = {
  classes: PropTypes.shape().isRequired,
  loginUserProp: PropTypes.func.isRequired,
  openSnackbar: PropTypes.func.isRequired,
  switchWindow: PropTypes.func.isRequired,
  setError: PropTypes.func.isRequired,
  hover: PropTypes.bool.isRequired,
  switchHover: PropTypes.func.isRequired
};

const mapStateToProps = state => ({
  errors: state.errors
});

const mapDispatchToProps = dispatch => {
  return {
    loginUserProp: (user, openSnackbar, setError) =>
      loginUser(user, openSnackbar, setError)(dispatch)
  };
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withStyles(Styles)(LoginForm));
