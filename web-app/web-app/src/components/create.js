import React from 'react';
import { Link } from 'react-router-dom';

class Create extends React.Component {
  constructor(props) {
    super(props);
    this.state = {username: '', category: ''};
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
	  fetch(process.env.REACT_APP_BACKEND_URL + '/user/createCategory', {
			method: 'POST',
			body: JSON.stringify({
			                username: this.props.match.params.username,
							category: this.state.category,
			}),
			headers: {
							"Content-type": "application/json; charset=UTF-8"
			}
		}).then(response => {
				if(response.status === 200) {
					return "New Category saved successfully";
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
		      <button onClick={this.props.history.goBack}>Back</button>
			  <form onSubmit={this.handleSubmit}>
				<p>
					<label>New Category:</label>
					<input type="text" name="category" value={this.state.category} onChange={this.handleChange} placeholder="Category" />
				</p>
				<p>
					<input type="submit" value="Create" />
				</p>
			  </form>
			  <p>&nbsp;</p>
		   </div>
    );
  }
}

export default Create;