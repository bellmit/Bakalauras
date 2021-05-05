import React from 'react'
import { Button, TextField } from '@material-ui/core'

const Header = () => (
	<div>
		<header>
			<div className='jumbotron jumbotron-fluid bg-secondary text-dark text-center'>
				<div className='container'>
					<h1 className='display-4'>
						Investment portfolio tracking and financial market analysis system
					</h1>
					<p className='lead'>by iMckify</p>
				</div>
			</div>
		</header>

		<form>
			<TextField
				// className={classes.textFields}
				name='email'
				label='Email'
				// value={email}
				// error={notEmpty(emailError)}
				// onChange={this.handleChange}
				// onBlur={e => this.validate(e, validateEmail)}
				// helperText={emailError}
				margin='normal'
			/>
			<TextField
				// className={classes.textFields}
				// name={labelNames.password}
				label='Password'
				// type={labelNames.password}
				// value={password}
				// error={notEmpty(passwordError)}
				// onChange={this.handleChange}
				// onBlur={e => this.validate(e, validatePassword)}
				// helperText={passwordError}
				margin='normal'
			/>
			<Button variant='outlined' type='submit'>
				Log in
			</Button>
		</form>
	</div>
)

export default Header
