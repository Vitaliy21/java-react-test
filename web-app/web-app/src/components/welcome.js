import React from 'react';
import { Link } from 'react-router-dom';


class Welcome extends React.Component {
    constructor(props) {
        super(props);
        this.state = {currentDate: new Date(),
                      firstDay: new Date(),
                      firstDayPrevMonth: new Date(),
                      lastDayPrevMonth: new Date(),
                      user: {username: '', token: '', password: ''},
                      details: {RESTAURANTS: 0, SUPERMARKETS: 0, TAXI: 0, FUN: 0, OTHER: 0, UNDEFINED: 0}
                      }
      }


    componentDidMount() {
        this.state.user = this.props.location.state.detail;
        this.state.firstDay = new Date(this.state.currentDate.getFullYear(), this.state.currentDate.getMonth(), 1);
        this.state.firstDayPrevMonth = new Date(this.state.currentDate.getFullYear(), this.state.currentDate.getMonth() - 1, 1);
        this.state.lastDayPrevMonth = new Date(this.state.currentDate.getFullYear(), this.state.currentDate.getMonth(), 0);
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
          currentDate: new Date()
        });
      }

    render() {
        return (<div id='head' align='center'>
                    <h1>Привет, {this.state.user.username}!</h1>
                    <h1>Сегодня {this.state.currentDate.toLocaleDateString()}.   Сейчас {this.state.currentDate.toLocaleTimeString()}.</h1>

                    <h1>Сегодняшняя дата: {this.state.currentDate.toLocaleDateString()}.   Первая дата месяца: {this.state.firstDay.toLocaleDateString()}.</h1>
                    <h1>Первая дата прошлого месяца: {this.state.firstDayPrevMonth.toLocaleDateString()}.
                        Последняя дата прошлого месяца: {this.state.lastDayPrevMonth.toLocaleDateString()}.</h1>
                    <p>&nbsp;</p>
                    <p>&nbsp;</p>
                    <p>&nbsp;</p>
                    <p>&nbsp;</p>

                    <table>
                      <tr>
                        <th>Category type</th>
                        <th>Last month spent</th>
                        <th>Current month spent</th>
                      </tr>
                      <tr>
                        <td>RESTAURANTS -> </td>
                        <td> ? </td>
                        <td>{this.state.details.RESTAURANTS}</td>
                      </tr>
                      <tr>
                        <td>SUPERMARKETS -> </td>
                        <td> ? </td>
                        <td>{this.state.details.SUPERMARKETS}</td>
                      </tr>
                      <tr>
                        <td>TAXI -> </td>
                        <td> ? </td>
                        <td>{this.state.details.TAXI}</td>
                      </tr>
                      <tr>
                        <td>FUN -> </td>
                        <td> ? </td>
                        <td>{this.state.details.FUN}</td>
                      </tr>
                      <tr>
                        <td>OTHER -> </td>
                        <td> ? </td>
                        <td>{this.state.details.OTHER}</td>
                      </tr>
                      <tr>
                        <td>UNDEFINED -> </td>
                        <td> ? </td>
                        <td>{this.state.details.UNDEFINED}</td>
                      </tr>
                    </table>

                </div>
        );
    }
}


export default Welcome;