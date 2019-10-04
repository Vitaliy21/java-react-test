import React from 'react';
import { Link } from 'react-router-dom';

class Websites extends React.Component {
	constructor(props) {
		super(props);
	}

	render() {
		return (
			<div id="container">
				<Link to="/register">Register user</Link>
				<p/>
				<Link to="/login">Login</Link>
			</div>
		)
	}
}

export default Websites;