import React, { Component } from 'react';

import logo from './logo.svg';

import './App.css';

class App extends Component {
  state = {
    response: '',
    post: '',
    responseToPost: '',
    prefixSize: 1,
    maxOutputSize: 10
  };

  componentDidMount() {
    this.callApi()
      .then(res => this.setState({ response: res.express }))
      .catch(err => console.log(err));
  }
  
  callApi = async () => {
    const response = await fetch('/hello');
    const body = await response.json();
    if (response.status !== 200) throw Error(body.message);
    
    return body;
  };
  
  handleSubmit = async e => {
    e.preventDefault();
    const response = await fetch(`/text?prefixSize=${this.state.prefixSize >= 0 ? encodeURIComponent(this.state.prefixSize) : 1}&maxOutputSize=${this.state.maxOutputSize >= 0 ? encodeURIComponent(this.state.maxOutputSize) : 10}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'text/plain',
      },
      body: this.state.post});
    console.log(response);
    const body = await response.text();
    
    this.setState({ responseToPost: body });
  };

  readFile = async e => {
    e.preventDefault()
    const reader = new FileReader()
    reader.onload = async (e) => { 
        const text = (e.target.result)
        text = text.length >= 2147483647 ? text.substring(0, 2147483645) : text; // 2147483647 is max length of string in java
        this.setState({ post: text })
      };
    reader.readAsText(e.target.files[0])
  }
  
render() {
    return (
      <div className="App">
        <p>{this.state.response}</p>
        <form onSubmit={this.handleSubmit}>
          <p style={{fontSize: '3rem'}}>
            <strong>Markov Chain Algorithm Text Parser</strong>
          </p>
          <p style={{fontSize: '2rem'}}> <strong>Inputs</strong></p>
          <p>Input text:</p>
          <input type="file"
            accept='.txt'
            style={{fontSize: '1rem'}}
            onChange={e => this.readFile(e)} />
          <input
            type="text"
            maxLength={2147483646}
            style={{fontSize: '1rem'}}
            value={this.state.post}
            onChange={e => this.setState({ post: e.target.value })}
          />
          <p>Prefix Size:</p>
          <input type="number"
          onChange={e => this.setState({ prefixSize: e.target.value })}/>
          <p>Max Output Size:</p>
          <input type="number" 
          onChange={e => this.setState({ maxOutputSize: e.target.value })}/>
          <p><button type="submit">Parse My Text</button></p>
        </form>
        <p style={{fontSize: '2rem'}}> <strong>Your Text</strong></p>
        <p style={{fontSize: '1.5rem'}}>{this.state.post}</p>
        <p style={{fontSize: '2rem'}}><strong>Parsed Text</strong></p>
        <p style={{fontSize: '1.5rem'}}>{this.state.response}</p>
        <p style={{fontSize: '1.5rem'}}>{this.state.responseToPost}</p>
      </div>
    );
  }
}

export default App;