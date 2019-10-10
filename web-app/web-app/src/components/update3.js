import React from 'react';
import { Link, withRouter } from 'react-router-dom';
import Select from 'react-select';

class Update3 extends React.Component {
  constructor(props) {
    super(props);
    this.state = {username: '', merchant: '', category: '', categories: []};
    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  componentDidMount() {
    this.setState({
        username: this.props.match.params.username,
        merchant: this.props.match.params.merchant,
        category: this.props.match.params.category
    });
    fetch(process.env.REACT_APP_BACKEND_URL + '/user/categories/' + this.props.match.params.username)
        .then(response => {
            return response.json();
        }).then(result => {
            console.log(result);
            this.setState({
                categories:result
            });
    });

  }
  handleChange(selection, action) {
      this.state.category = this.state.categories[selection.value];
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
			  <button onClick={this.props.history.goBack}>Back</button>
				  <p/>
				  <form onSubmit={this.handleSubmit}>
					<input type="hidden" name="username" value={this.state.username}/>
						<p>
						<label>Merchant Name:</label>
							<input type="label" name="merchant" value={this.state.merchant} placeholder="Merchant" />
						</p>
						<p>
							<label>Category Type:</label>
							<Select
                                name="selectedWires"
                                options={this.state.categories.map((option, idx) => ({
                                    value: idx,
                                    label: option
                                }))}
                                onChange={(selection, action) => this.handleChange(selection, action)}/>
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