import React from 'react';
import { Link } from 'react-router-dom';

class Register extends React.Component {
  constructor(props) {
    super(props);
    this.state = {username: '', email: '', token:'', password:''};
    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  }
  handleChange(event) {
	  const state = this.state
	  state[event.target.name] = event.target.value
	  this.setState(state);
  }
  handleSubmit(event) {
	  event.preventDefault();
	  fetch(process.env.REACT_APP_BACKEND_URL + '/user/register', {
			method: 'POST',
			body: JSON.stringify({
							username: this.state.username,
							email: this.state.email,
							token: this.state.token,
							password: this.state.password,
			}),
			headers: {
							"Content-type": "application/json; charset=UTF-8"
			}
		}).then(response => {
				if(response.status === 200) {
					return "New User saved successfully";
				} else {
				    return response.text();
				}
			}).then(result => {
                alert(result)
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
					<input type="text" name="username" value={this.state.username} onChange={this.handleChange} placeholder="Username" />
				</p>
				<p>
					<label>Email:</label>
					<input type="text" name="email" value={this.state.email} onChange={this.handleChange} placeholder="email" />
				</p>
				<p>
                    <label>Token:</label>
                    <input type="text" name="token" value={this.state.token} onChange={this.handleChange} placeholder="Token" />
                    <a href="http://api.monobank.ua" target="_blank">find it there</a>
                </p>
				<p>
					<label>Password:</label>
					<input type="text" name="password" value={this.state.password} onChange={this.handleChange} placeholder="Password" />
				</p>
				<p>
					<input type="submit" value="Register" />
				</p>
			  </form>
		   </div>
    );
  }
}

export default Register;