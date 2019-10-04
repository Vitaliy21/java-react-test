import React from 'react';
import { Link, withRouter } from 'react-router-dom';

class Update2 extends React.Component {
  constructor(props) {
          super(props);
          this.state = {merchants: []}
          this.headers = [
          	{ key: 'merchantName', label: 'Merchant name' },
          ];
      }

      componentDidMount() {
  		fetch(process.env.REACT_APP_BACKEND_URL + '/user/search/' + this.props.match.params.username + '/' + this.props.match.params.category)
  			.then(response => {
  				return response.json();
  			}).then(result => {
  				console.log(result);
  				this.setState({
  					merchants:result
  				});
  			});
  	}

  	render() {
      		return (
      			<div id="container">
      			<button onClick={this.props.history.goBack}>Back</button>
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
      							this.state.merchants.map(function(item, key) {
      							return (
      								<tr key = {key}>
      								  <td>{item}</td>
      								  <td><Link to={`/update3/${this.props.match.params.username}/${this.props.match.params.category}/${item}`}>Edit</Link></td>
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

export default Update2;