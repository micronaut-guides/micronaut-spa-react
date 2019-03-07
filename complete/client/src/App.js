import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';
import {SERVER_URL} from "./config";

class App extends Component {

  //<1>
  state = {
    name: 'World',
    message: null
  };

  //<2>
  setName = e => this.setState({name: e.target.value});

  //<3>
  getMessage = e => {
    e.preventDefault();
    fetch(`${SERVER_URL}/${this.state.name}`)
        .then(r => r.text())
        .then(message => this.setState({message}))
        .catch(e => console.error(e))
  };

  render() {
    return (
      <div className="App">
        <header className="App-header">
          <img src={logo} className="App-logo" alt="logo" />
          {/* <4> */}
          <form onSubmit={this.getMessage}>
            <label>Enter your name: </label>
            <input type="text" value={this.state.name} onChange={this.setName} />
            <input type="submit" value="Submit" />
          </form>

          {/* <5> */}
          <p>
            { this.state.message ?
                <strong>{this.state.message}</strong> :
                <span>Edit <code>src/App.js</code> and save to reload.</span>
            }
          </p>
          <a
            className="App-link"
            href="https://reactjs.org"
            target="_blank"
            rel="noopener noreferrer"
          >
            Learn React
          </a>
        </header>
      </div>
    );
  }
}

export default App;
