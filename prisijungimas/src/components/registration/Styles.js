const styles = theme => ({
  container: {
    fontSize: 18,
    fontWeight: 'bold',
    color: '#3f51b5',
    textAlign: 'center'
  },
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
  textFields: {
    width: '20vw',
    height: '5vh',
    marginTop: '3vh'
  },
  button: {
    marginTop: '2vh'
  },
  button1: {
    marginTop: '1vh',
    fontSize: 14,
    color: '#551A8B',
    width: '20vw',
    textDecoration: 'underline',
    textTransform: 'none'
  },
  button2: {
    cursor: 'pointer',
    marginTop: '1vh',
    fontSize: 14,
    color: '#551A8B',
    width: '20vw',
    textDecoration: 'underline',
    textTransform: 'none',
    fontWeight: 'bold'
  },
  layout: {
    fontSize: 18,
    color: '#3f51b5'
  }
});

export default styles;
