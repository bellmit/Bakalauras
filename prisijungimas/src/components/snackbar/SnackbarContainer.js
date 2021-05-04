import React from 'react';
import PropTypes from 'prop-types';
import Snackbar from '@material-ui/core/Snackbar';
import SnackbarContentWrapper from './SnackbarContent';

function SnackbarContainer(props) {
  const { handleClose, snackbarContents } = props;
  return (
    <Snackbar
      anchorOrigin={{ vertical: 'bottom', horizontal: 'left' }}
      open={typeof snackbarContents.message !== 'undefined'}
      autoHideDuration={6000}
      onClose={handleClose}
    >
      <SnackbarContentWrapper onClose={handleClose} snackbarContents={snackbarContents} />
    </Snackbar>
  );
}

SnackbarContainer.propTypes = {
  handleClose: PropTypes.func.isRequired,
  snackbarContents: PropTypes.shape().isRequired
};

export default SnackbarContainer;
