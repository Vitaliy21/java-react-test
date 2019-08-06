import React from 'react';
import { Link } from 'react-router-dom';

class Welcome extends React.Component {
    constructor(props) {
        super(props);
        this.state = {date: new Date(),
                      user: {username: '', token: '', password: ''},
                      details: {RESTAURANTS: 0, SUPERMARKETS: 0, TAXI: 0, FUN: 0, OTHER: 0, UNDEFINED: 0}
                      }
      }


    componentDidMount() {
        this.state.user = this.props.location.state.detail
        this.timerID = setInterval(
          () => this.tick(),
          1000
        );
        fetch('http://localhost:9999/user/details/' + this.state.user.username)
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
          date: new Date()
        });
      }

    render() {
        return (<div id='head' align='center'>
                    <h1>Привет, {this.state.user.username}!</h1>
                    <h1>Сегодня {this.state.date.toLocaleDateString()}.   Сейчас {this.state.date.toLocaleTimeString()}.</h1>
                    <p>&nbsp;</p>
                    <p>&nbsp;</p>
                    <p>&nbsp;</p>
                    <p>&nbsp;</p>
                    <p>&nbsp;</p>
                    <h2>RESTAURANTS -> {this.state.details.RESTAURANTS}</h2>
                    <h2>SUPERMARKETS -> {this.state.details.SUPERMARKETS}</h2>
                    <h2>TAXI -> {this.state.details.TAXI}</h2>
                    <h2>FUN -> {this.state.details.FUN}</h2>
                    <h2>OTHER -> {this.state.details.OTHER}</h2>
                    <h2>UNDEFINED -> {this.state.details.UNDEFINED}</h2>
                </div>
        );
    }
}


export default Welcome;