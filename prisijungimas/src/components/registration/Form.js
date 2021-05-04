import React, { Component } from 'react';
import Button from '@material-ui/core/Button';
import TextField from '@material-ui/core/TextField';
import PropTypes from 'prop-types';
import { MenuItem, Select } from '@material-ui/core';
import { registerUser } from '../../actions/authentication';
import {
  emailValidation,
  passwordValidation,
  confirmPasswordValidation,
  usernameValidation,
  isEmpty
} from '../../utils/validation';

class Form extends Component {
  constructor(props) {
    super(props);

    this.state = {
      username: '',
      email: '',
      password: '',
      confirmPassword: '',
      role: 'Investor',
      emailError: '',
      passwordError: '',
      confirmPasswordError: '',
      usernameError: '',
      disable: true
    };
  }

  validate = (e, method) => {
    this.setState({ [`${e.target.name}Error`]: method(e.target.value) });
  };

  confirmPasswordValidate = (e, method) => {
    const { password } = this.state;
    this.setState({
      [`${e.target.name}Error`]: method(e.target.value, password)
    });
  };

  buttonDisable = () => {
    const { email, password, confirmPassword } = this.state;
    if (!isEmpty(email) && !isEmpty(password) && !isEmpty(confirmPassword)) {
      this.setState({ disable: false });
    } else {
      this.setState({ disable: true });
    }
  };

  handleChange = e => {
    this.setState({ [`${e.target.name}Error`]: '' });
    this.setState({ [e.target.name]: e.target.value }, () => this.buttonDisable());
  };

  handleSubmit = e => {
    e.preventDefault();
    const {
      email,
      password,
      emailError,
      passwordError,
      confirmPasswordError,
      username,
      role,
      usernameError
    } = this.state;
    const { openSnackbar, setError, switchWindow } = this.props;
    const userData = {
      username,
      email,
      password,
      role
    };
    if (
      isEmpty(usernameError) &&
      isEmpty(emailError) &&
      isEmpty(passwordError) &&
      isEmpty(confirmPasswordError)
    ) {
      registerUser(userData, openSnackbar, setError, switchWindow);
    }
  };

  render() {
    const {
      username,
      email,
      password,
      confirmPassword,
      role,
      emailError,
      passwordError,
      confirmPasswordError,
      usernameError,
      disable
    } = this.state;
    const { classes } = this.props;

    const labelNames = {
      username: 'username',
      email: 'email',
      password: 'password',
      confirmPassword: 'confirmPassword',
      role: 'role'
    };

    return (
      <form onSubmit={this.handleSubmit} className={classes.layout}>
        <TextField
          className={classes.textFields}
          name={labelNames.username}
          label="User Name"
          type={labelNames.username}
          value={username}
          error={!isEmpty(usernameError)}
          onChange={this.handleChange}
          onBlur={e => this.validate(e, usernameValidation)}
          helperText={usernameError}
          margin="normal"
        />
        <TextField
          className={classes.textFields}
          name={labelNames.email}
          label="Email"
          type={labelNames.email}
          value={email}
          error={!isEmpty(emailError)}
          onChange={this.handleChange}
          onBlur={e => this.validate(e, emailValidation)}
          helperText={emailError}
          margin="normal"
        />
        <TextField
          className={classes.textFields}
          name={labelNames.password}
          label="Password"
          type={labelNames.password}
          value={password}
          error={!isEmpty(passwordError)}
          onChange={this.handleChange}
          onBlur={e => this.validate(e, passwordValidation)}
          helperText={passwordError}
          margin="normal"
        />
        <TextField
          className={classes.textFields}
          name={labelNames.confirmPassword}
          label="Confirm Password"
          type={labelNames.password}
          value={confirmPassword}
          error={!isEmpty(confirmPasswordError)}
          onChange={this.handleChange}
          onBlur={e => this.confirmPasswordValidate(e, confirmPasswordValidation)}
          helperText={confirmPasswordError}
          margin="normal"
        />
        <Select
          className={classes.textFields}
          name={labelNames.role}
          value={role}
          onChange={this.handleChange}
        >
          <MenuItem value="Investor">Investor</MenuItem>
          <MenuItem value="Analyst">Analyst</MenuItem>
        </Select>
        <div />
        <Button className={classes.button} disabled={disable} variant="outlined" type="submit">
          Sign up
        </Button>
      </form>
    );
  }
}

Form.propTypes = {
  openSnackbar: PropTypes.func.isRequired,
  classes: PropTypes.shape().isRequired,
  setError: PropTypes.func.isRequired,
  switchWindow: PropTypes.func.isRequired
};

export default Form;
