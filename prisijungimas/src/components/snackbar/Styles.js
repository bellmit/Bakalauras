import green from '@material-ui/core/colors/green';
import blue from '@material-ui/core/colors/blue';

const Styles = theme => ({
  success: {
    backgroundColor: green[600]
  },
  error: {
    backgroundColor: theme.palette.error.dark
  },
  neutral: {
    backgroundColor: blue[600]
  },
  iconVariant: {
    fontSize: 20,
    opacity: 0.9,
    marginRight: theme.spacing.unit
  },
  message: {
    display: 'flex',
    alignItems: 'center'
  },
  close: {
    padding: theme.spacing.unit / 2
  }
});

export default Styles;
