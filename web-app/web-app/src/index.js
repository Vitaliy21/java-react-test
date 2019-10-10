import React from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter as Router, Route } from 'react-router-dom';
import './website.css';
import Websites from './components/websites';
import Register from './components/register';
import Login from './components/login';
import Welcome from './components/welcome';
import Update2 from './components/update2';
import Update3 from './components/update3';
import Create from './components/create';
import * as serviceWorker from './serviceWorker';

ReactDOM.render(
	<Router>
		<div>
			<Route exact path='/' component={Websites} />
			<Route path='/register' component={Register} />
			<Route path='/login' component={Login} />
			<Route path='/welcome' component={Welcome} />
			<Route path='/create/:username' component={Create} />
			<Route path='/update2/:username/:category' component={Update2} />
			<Route path='/update3/:username/:category/:merchant' component={Update3} />
		</div>
	</Router>,
document.getElementById('root'));

serviceWorker.unregister();
