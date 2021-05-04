import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import IconButton from '@material-ui/core/IconButton';
import CloseIcon from '@material-ui/icons/Close';
import CheckCircleIcon from '@material-ui/icons/CheckCircle';
import ErrorIcon from '@material-ui/icons/Error';
import SnackbarContent from '@material-ui/core/SnackbarContent';
import Styles from './Styles';

const iconSelect = variant => {
  if (variant === 'error') {
    return ErrorIcon;
  }
  return CheckCircleIcon;
};

function SnackContentWrapper(props) {
  const {
    classes,
    onClose,
    snackbarContents: { message, variant },
    ...other
  } = props;

  const Icon = iconSelect(variant);

  return (
    <SnackbarContent
      className={classes[variant]}
      message={[
        <span className={classes.message} key={new Date().getTime()}>
          <Icon className={classes.iconVariant} />
          {message}
        </span>
      ]}
      action={[
        <IconButton key="close" color="inherit" className={classes.close} onClick={onClose}>
          <CloseIcon className={classes.icon} />
        </IconButton>
      ]}
      {...other}
    />
  );
}

SnackContentWrapper.propTypes = {
  classes: PropTypes.shape().isRequired,
  onClose: PropTypes.func.isRequired,
  snackbarContents: PropTypes.shape().isRequired
};

export default withStyles(Styles)(SnackContentWrapper);
