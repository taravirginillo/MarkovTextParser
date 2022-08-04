import React, { Component } from 'react';

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
    const response = await fetch('text', {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: {
        'text':this.state.post,
        'prefixSize':this.state.prefixSize,
        'maxOutputSize':this.state.maxOutputSize
      }});
    console.log(response);
    const body = await response.text();
    
    this.setState({ responseToPost: body });
  };

  readFile = async e => {
    e.preventDefault()
    const reader = new FileReader()
    reader.onload = async (e) => { 
        const text = (e.target.result).length >= 4000 ? (e.target.result).substring(0, 4000) : (e.target.result); // 4000 is max length according to endpoint
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
            onclick="this.value=null;"
            onChange={e => this.readFile(e)} />
          <input
            type="text"
            maxLength={4000}
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