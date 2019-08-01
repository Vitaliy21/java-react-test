import React from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter as Router, Route } from 'react-router-dom';
import './website.css';
import Websites from './components/websites';
import Create from './components/create';
import Register from './components/register';
import Login from './components/login';
import Update from './components/update';
import Welcome from './components/welcome';
import Users from './components/users';
import * as serviceWorker from './serviceWorker';

ReactDOM.render(
	<Router>
		<div>
			<Route exact path='/' component={Websites} />
			<Route path='/create' component={Create} />
			<Route path='/register' component={Register} />
			<Route path='/login' component={Login} />
			<Route path='/update/:id' component={Update} />
			<Route path='/welcome' component={Welcome} />
			<Route path='/users' component={Users} />
		</div>
	</Router>,
document.getElementById('root'));

serviceWorker.unregister();
