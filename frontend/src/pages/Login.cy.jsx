import { configureStore } from '@reduxjs/toolkit';
import { store as appStore } from '../redux/store';
import { Provider } from 'react-redux';
import { BrowserRouter as Router } from 'react-router-dom';
import Login from './Login';
import authReducer from '../redux/slices/auth';
import {mount} from "cypress/react18";
import '../styles/Login.css';

describe('<Login />', () => {
  // Create a test store before each test
  beforeEach(() => {
    // Create a test store with only the authReducer
    const testStore = configureStore({
      reducer: {
        auth: authReducer,
      },
      preloadedState: appStore.getState(), // To keep the initial state same as the app store
    });

    mount(
        // Wrap Login with Provider, Router, and pass in the test store
        <Provider store={testStore}>
          <Router>
            <Login />
          </Router>
        </Provider>
    );
  });

  it('renders the login component', () => {
    cy.get('.login-container').should('be.visible');
    cy.get('.login-card').should('be.visible');
    cy.get('.login-card-title').should('contain', 'Welcome to Zenith Blog');
  });

});


