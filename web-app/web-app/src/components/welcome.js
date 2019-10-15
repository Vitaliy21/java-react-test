import React from 'react';
import { Link } from 'react-router-dom';


class Welcome extends React.Component {
    constructor(props) {
        super(props);
        this.headers = [
                    { key: 'category', label: 'Category'},
                	{ key: 'previous month', label: 'Previous month' },
                	{ key: 'current month', label: 'Current month' }
                ];
        this.state = {currentDate: new Date(),
                      user: {username: '', token: '', password: ''},
                      details: [],
                      totalPrevious: 0,
                      totalCurrent: 0
                      }
      }


    componentDidMount() {
        this.state.user = this.props.location.state.detail;
        this.timerID = setInterval(
          () => this.tick(),
          1000
        );
        fetch(process.env.REACT_APP_BACKEND_URL + '/user/details/' + this.state.user.username)
        	.then(response => {
        		return response.json();
        	}).then(result => {
        		console.log(result);
        		this.setState({
        			details:result
        		});

        	});
      }

      componentWillUnmount() {
        clearInterval(this.timerID);
      }

      tick() {
        this.setState({
          currentDate: new Date()
        });
      }

      calculateTotal() {
            var sumPrevious = 0;
            var sumCurrent = 0;
            this.state.details.forEach(function(obj){
              sumPrevious += obj.previousAmount;
              sumCurrent += obj.currentAmount;
            });
            this.state.totalPrevious = Math.round(sumPrevious);
            this.state.totalCurrent = Math.round(sumCurrent);
      }

    render() {
        this.calculateTotal()
        return (
                <div id='head' align='center'>
                    <Link to="/">Home</Link>
                    <h1>Привет, {this.state.user.username}!</h1>
                    <h1>Сегодня {this.state.currentDate.toLocaleDateString()}.   Сейчас {this.state.currentDate.toLocaleTimeString()}.</h1>

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
                                this.state.details.map(function(item, key) {
                                return (
                                    <tr key = {key}>
                                      <td>{item.categoryType}</td>
                                      <td>{item.previousAmount}</td>
                                      <td>{item.currentAmount}</td>
                                      <td><Link to={`/update2/${this.state.user.username}/${item.categoryType}`}>Edit</Link></td>
                                    </tr>
                                                )
                                }.bind(this))
                            }
                            <tr>
                              <td><b>TOTAL:</b></td>
                              <td><b>{this.state.totalPrevious}</b></td>
                              <td><b>{this.state.totalCurrent}</b></td>
                            </tr>
                        </tbody>
                    </table>

                    <Link to={`/create/${this.state.user.username}`}>Create category</Link>

                </div>
        );
    }
}


export default Welcome;