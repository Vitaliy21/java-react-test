import React from 'react';
import { Link, withRouter } from 'react-router-dom';

class Update2 extends React.Component {
  constructor(props) {
          super(props);
          this.state = {merchants: []}
          this.headers = [
          	{ key: 'merchantName', label: 'Merchant name' },
//          	{ key: 'username', label: 'Username' },
//          	{ key: 'category', label: 'Category' },
          ];
      }

      componentDidMount() {
  		fetch('http://localhost:9999/user/search/' + this.props.match.params.username + '/' + this.props.match.params.category)
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
      							this.state.merchants.map(function(item, key) {
      							return (
      								<tr key = {key}>
      								  <td>{item}</td>
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