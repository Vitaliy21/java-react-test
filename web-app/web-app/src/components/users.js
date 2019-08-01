import React from 'react';
import { Link } from 'react-router-dom';

class Users extends React.Component {
    constructor(props) {
        super(props);
        this.state = {users: []}
        this.headers = [
            { key: 'username', label: 'Username'},
        	{ key: 'token', label: 'Token' },
        	{ key: 'password', label: 'Password' }
        ];
    }

    componentDidMount() {
		fetch('http://localhost:9999/user/users')
			.then(response => {
				return response.json();
			}).then(result => {
				console.log(result);
				this.setState({
					users:result
				});
			});
	}

	render() {
    		return (
    			<div id="container">
    			<Link to="/">Websites</Link>
                <p/>
    				<table>
    					<thead>
    						<tr>
    						{
    							this.headers.map(function(h) {
    								return (
    									<th key = {h.key}>{h.label}</th>
    								)
    							})
    						}
    						</tr>
    					</thead>
    					<tbody>
    						{
    							this.state.users.map(function(item, key) {
    							return (
    								<tr key = {key}>
    								  <td>{item.username}</td>
    								  <td>{item.token}</td>
    								  <td>{item.password}</td>
    								</tr>
    											)
    							}.bind(this))
    						}
    					</tbody>
    				</table>
    			</div>
    		)
    	}

}

export default Users;