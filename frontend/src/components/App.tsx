import React from 'react';
import { ApiService } from '../services/ApiService';
import './App.css';
import logo from './logo.svg';

class App extends React.Component<{},{}> {

  private readonly apiService = new ApiService();

  public async componentDidMount() {
    const result = await this.apiService.login({email: 'derp@mail.com', password: '1234'});
    console.log(result.data);
  }
  
  public render() { 
    return (
      <div className="App">
        <header className="App-header">
          <img src={logo} className="App-logo" alt="logo" />
        </header>
      </div>
    );
  }
}

export default App;
