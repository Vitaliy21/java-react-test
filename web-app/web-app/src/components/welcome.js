import React from 'react';
import { Link } from 'react-router-dom';

class Welcome extends React.Component {
    constructor(props) {
        super(props);
        this.state = {date: new Date()}
      }


    componentDidMount() {
        this.timerID = setInterval(
          () => this.tick(),
          1000
        );
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
        return (<div>
                    <h1>Привет, {this.props.location.state.detail}!</h1>
                    <h2>Сейчас {this.state.date.toLocaleTimeString()}.</h2>
                </div>
        );
    }
}


export default Welcome;