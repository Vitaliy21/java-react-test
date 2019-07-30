import React from 'react';
import { Link } from 'react-router-dom';

class Login extends React.Component {
  constructor(props) {
    super(props);
    this.state = {username: '', password:''};
    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  handleChange(event) {
	  const state = this.state
	  state[event.target.name] = event.target.value
	  this.setState(state);
  }
  handleSubmit(event) {
	  event.preventDefault();
	  fetch('http://localhost:9999/user/login', {
			method: 'POST',
			body: JSON.stringify({
							title: this.state.title,
							url: this.state.url
			}),
			headers: {
							"Content-type": "application/json; charset=UTF-8"
			}
		}).then(response => {
        		if(response.status === 200) {
                    return response.json();
        		} else {
        			alert("wrong login or password");
        			return null;
        		}
        }).then(result => {
                if (result == null) {
                    console.log(result);
                } else {
                    console.log(result);
                    this.props.history.push({
                        pathname: '/welcome',
                        search: '?query=abc',
                        state: { detail: result}
                    })
                }
        });

  }
  render() {
    return (
		<div id="container">
		  <Link to="/">Websites</Link>
			  <p/>
			  <form onSubmit={this.handleSubmit}>
				<p>
					<label>Username:</label>
					<input type="text" name="username" value={this.state.title} onChange={this.handleChange} placeholder="Username" />
				</p>
				<p>
					<label>Password:</label>
					<input type="text" name="password" value={this.state.url} onChange={this.handleChange} placeholder="Password" />
				</p>
				<p>
					<input type="submit" value="Login"/>
				</p>
			  </form>
		   </div>
    );
  }
}

export default Login;