const styles = theme => ({
  paper: {
    position: 'absolute',
    backgroundColor: theme.palette.background.paper,
    boxShadow: theme.shadows[5],
    padding: theme.spacing.unit * 4,
    outline: 'none',
    height: '80vh%',
    width: '20vw',
    top: `64px`,
    left: `${40}%`
  },
  email: {
    width: '100px'
  },
  button: {
    marginTop: '2vh'
  }
});

export default styles;
