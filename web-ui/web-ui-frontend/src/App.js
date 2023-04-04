import React from 'react';
import BuildList from './components/BuildList';
import './App.css';

function App() {
  return (
    <div className="App">
      <header className="App-header">
        <h1>Google Cloud Build Notifications</h1>
      </header>
      <main>
        <BuildList />
      </main>
    </div>
  );
}

export default App;
