import React from 'react';
import { Provider } from 'react-redux';
import { configureStore } from '@reduxjs/toolkit';
import { BrowserRouter as Router } from 'react-router-dom';
import Home from './Home';
import postReducer from "../redux/slices/postSlice";
import commentReducer from "../redux/slices/commentSlice";
import authReducer from "../redux/slices/auth";
import userReducer from "../redux/slices/userSlice";
import adminReducer from "../redux/slices/adminSlice";
import { mount } from "cypress/react18";
import '../styles/Home.css';

describe('<Home />', () => {
  beforeEach(() => {
    cy.intercept('GET', 'http://localhost:8080/post/highest', {
      statusCode: 200,
      body: {
        highestIndex: 100
      }
    }).as('getHighestIndex');
  });

  it('renders', () => {
    const store = configureStore({
      reducer: {
        // add your reducers here
        posts: postReducer,
        comments: commentReducer,
        auth: authReducer,
        users: userReducer,
        admin: adminReducer,
      },
    });

    // stub the network request
    cy.intercept('GET', 'http://localhost:8080/post/gets?postIDStart=10000&count=9&reverse=true', { fixture: 'home/getPosts.json' }).as('getHomePosts');

    mount(
        <Router> {/* wrap your components with the MemoryRouter */}
          <Provider store={store}>
            <Home />
          </Provider>
        </Router>
    );

    // wait for the network request to complete
    cy.wait('@getHomePosts');
    cy.wait('@getHighestIndex');
    cy.get('.carousel-image').should('have.length', 3);
    cy.get('.next-button').click({timeout: 5000});
  });
});




