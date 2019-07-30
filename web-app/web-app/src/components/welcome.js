import React from 'react';
import { Link } from 'react-router-dom';

class Welcome extends React.Component {
    constructor(props) {
        super(props);
        this.state = {date: new Date(), name: 'World'}
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
//        return <h1>Hello World</h1>
//        return <h1>Привет, {this.state.name}</h1>;
        return (<div>
                    <h1>Привет, {this.props.location.state.detail.username}!</h1>
                    <h2>Сейчас {this.state.date.toLocaleTimeString()}.</h2>
                </div>
        );
    }
}


export default Welcome;