import React, { Component } from 'react';
import PropTypes from 'prop-types';
import Button from '@material-ui/core/Button';
import TextField from '@material-ui/core/TextField';
import { validateEmail, validatePassword } from '../../utils/validation';

function notEmpty(myString) {
  if (myString !== '') {
    return true;
  }
  return false;
}

class Form extends Component {
  constructor(props) {
    super(props);
    this.initialstate = {
      email: '',
      password: '',
      emailError: '',
      passwordError: '',
      disable: true
    };
    this.state = this.initialstate;
  }

  validate = (e, method) => {
    this.setState({ [`${e.target.name}Error`]: method(e.target.value) });
  };

  buttonDisable = () => {
    const { email, password } = this.state;
    if (notEmpty(email) && notEmpty(password)) {
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
    const { onSubmit } = this.props;
    const { email, password, passwordError, emailError } = this.state;
    const userData = {
      email,
      password
    };
    if (!notEmpty(emailError) && !notEmpty(passwordError)) {
      onSubmit(userData);
    }
  };

  render() {
    const { email, password, emailError, passwordError, disable } = this.state;
    const { classes } = this.props;

    const labelNames = {
      password: 'password'
    };

    return (
      <form onSubmit={this.handleSubmit} className={classes.layout}>
        <TextField
          className={classes.textFields}
          name="email"
          label="Email"
          value={email}
          error={notEmpty(emailError)}
          onChange={this.handleChange}
          onBlur={e => this.validate(e, validateEmail)}
          helperText={emailError}
          margin="normal"
        />
        <TextField
          className={classes.textFields}
          name={labelNames.password}
          label="Password"
          type={labelNames.password}
          value={password}
          error={notEmpty(passwordError)}
          onChange={this.handleChange}
          onBlur={e => this.validate(e, validatePassword)}
          helperText={passwordError}
          margin="normal"
        />
        <Button className={classes.button} disabled={disable} variant="outlined" type="submit">
          Log in
        </Button>
      </form>
    );
  }
}

Form.propTypes = {
  onSubmit: PropTypes.func.isRequired,
  classes: PropTypes.shape().isRequired
};

export default Form;
