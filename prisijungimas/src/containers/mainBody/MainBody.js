import React from 'react';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import Paper from '@material-ui/core/Paper';
import Styles from './Styles';
import { SnackbarContainer } from '../../components/snackbar';
import { snackbarMessages } from '../../utils/constants';
import { logoutUser } from '../../actions/authentication';
import { HomeWindow } from '../homeWindow';

class MainBody extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      window: false,
      hover: false,
      snackbarContents: {}
    };
  }

  switchHover = () => {
    const { hover } = this.state;
    this.setState({ hover: !hover });
  };

  switchWindow = () => {
    const { window, hover } = this.state;
    this.setState({ window: !window, hover: !hover });
  };

  handleLogout = e => {
    e.preventDefault();
    const { logoutUserProp } = this.props;
    logoutUserProp();
    this.openSnackbar({ message: snackbarMessages.logoutSuccess, variant: 'neutral' });
  };

  setError = err => {
    const errors = err
      ? { message: `${err.Status} : ${err.Message}` }
      : { message: snackbarMessages.unidentified };
    this.setState({ snackbarContents: { message: errors.message, variant: 'error' } });
  };

  openSnackbar = snackbarContents => {
    this.setState({ snackbarContents });
  };

  handleSnackbarClose = () => {
    const { snackbarContents } = this.state;
    this.setState({ snackbarContents: { ...snackbarContents, message: undefined } });
  };

  render() {
    const { classes, auth } = this.props;
    const { snackbarContents, window, hover } = this.state;
    return (
      <Paper className={classes.paper} elevation={24}>
        <HomeWindow
          window={window}
          auth={auth}
          handleLogout={this.handleLogout}
          setError={this.setError}
          openSnackbar={this.openSnackbar}
          switchWindow={this.switchWindow}
          switchHover={this.switchHover}
          hover={hover}
        />
        <SnackbarContainer
          snackbarContents={snackbarContents}
          handleClose={this.handleSnackbarClose}
        />
      </Paper>
    );
  }
}

MainBody.propTypes = {
  classes: PropTypes.shape().isRequired,
  auth: PropTypes.shape().isRequired,
  logoutUserProp: PropTypes.func.isRequired
};

const mapStateToProps = state => ({
  auth: state.auth
});

const mapDispatchToProps = dispatch => {
  return { logoutUserProp: () => logoutUser(dispatch) };
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withStyles(Styles)(MainBody));
