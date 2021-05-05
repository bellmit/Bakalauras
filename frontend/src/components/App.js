import React from 'react'
import { BrowserRouter, Route, Switch } from 'react-router-dom'
import axios from 'axios'
import toastr from 'toastr'
import PageNotFound from './common/PageNotFound'
import Home from './landing/Home'
import { TradeListContainer } from './trade/TradeListContainer'
import { AddOrEditTradeContainer } from './trade/AddOrEditTradeContainer'
import About from './About'
import { HeaderNavContainer } from './landing/HeaderNavContainer'

export const setCurrentUser = (decoded) => ({
	type: 'SET_CURRENT_USER',
	payload: decoded,
})

// toastr.options.positionClass = 'toast-bottom-left';
const pre = `http://localhost:8080/api`

class App extends React.Component {
	componentDidMount() {
		const user = {
			email: 'investor@email.com',
			password: 'investor',
		}

		this.loginUser(user)
	}

	loginUser = (user) => {
		axios.post(`${pre}/Users/login`, user).then((res) => {
			localStorage.setItem('jwtToken', res.data)
			// toastr.success(`jwt created: ${JSON.stringify(jwtDecode(res.data))}`)
			toastr.success(`login Success, JWT created`)
		})
	}

	render() {
		return (
			<div>
				{/* <BrowserRouter history={history}> */}
				<BrowserRouter>
					<div>
						<HeaderNavContainer />
						<Switch>
							<Route exact path='/' component={Home} />
							<Route path='/about' component={About} />
							<Route path='/Trades' component={TradeListContainer} />
							<Route exact path='/Trade' component={AddOrEditTradeContainer} />
							<Route path='/Trade/:id' component={AddOrEditTradeContainer} />
							<Route component={PageNotFound} />
						</Switch>
					</div>
				</BrowserRouter>
			</div>
		)
	}
}

// const mapDispatchToProps = dispatch => ({
//     action: bindActionCreators({ ...loginUser }, dispatch)
// });

// export default connect(null, mapDispatchToProps)(App);
export default App
