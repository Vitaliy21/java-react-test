import React from 'react';
import { Link, withRouter } from 'react-router-dom';

class Update3 extends React.Component {
  constructor(props) {
    super(props);
    this.state = {username: '', merchant: '', category: ''};
    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  componentDidMount() {
    this.setState({
        username: this.props.match.params.username,
        merchant: this.props.match.params.merchant,
        category: this.props.match.params.category
    })

  }
  handleChange(event) {
	  const state = this.state
	  state[event.target.name] = event.target.value
	  this.setState(state);
  }
  handleSubmit(event) {
	  event.preventDefault();
	  fetch(process.env.REACT_APP_BACKEND_URL + '/user/updateCategory', {
			method: 'POST',
			body: JSON.stringify({
							username:this.state.username,
							merchant: this.state.merchant,
							category: this.state.category
			}),
			headers: {
							"Content-type": "application/json; charset=UTF-8"
			}
		}).then(response => {
				if(response.status === 200) {
					alert("Category update successfully.");
				}
			});
  }

  render() {
    return (
			<div id="container">
			  <Link to="/">Websites</Link>
				  <p/>
				  <form onSubmit={this.handleSubmit}>
					<input type="hidden" name="username" value={this.state.username}/>
						<p>
						<label>Merchant Name:</label>
							<input type="label" name="merchant" value={this.state.merchant} placeholder="Merchant" />
						</p>
						<p>
							<label>Category Type:</label>
							<select name="category" value={this.state.category} onChange={this.handleChange} placeholder="Category">
                                        <option value="UNDEFINED">Undefined</option>
                                        <option value="OTHER">Other</option>
                                        <option value="RESTAURANTS">Restaurants</option>
                                        <option value="SUPERMARKETS">Supermarkets</option>
                                        <option value="TAXI">Taxi</option>
                                        <option value="FUN">Fun</option>
                                      </select>
						</p>
						<p>
							<input type="submit" value="Submit" />
						</p>
				  </form>
			   </div>
    );
  }
}

export default Update3;