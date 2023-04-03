import React from 'react';
import { Provider } from 'react-redux';
import { BrowserRouter as Router } from 'react-router-dom';
import { configureStore } from '@reduxjs/toolkit';
import authSlice from '../redux/slices/auth';
import userSlice from '../redux/slices/userSlice';
import Header from './Header';
import '@testing-library/cypress/add-commands';


const createStore = (isAuthenticated, userLevel) => {
    return configureStore({
        reducer: {
            auth: authSlice,
            user: userSlice,
        },
        preloadedState: {
            auth: { isAuthenticated },
            user: { currentUser: { userLevel } },
        },
    });
};

describe('<Header />', () => {

    it('renders the header with LOGIN link when not authenticated', () => {
        const store = createStore(false);

        cy.mount(
            <Provider store={store}>
                <Router>
                    <Header />
                </Router>
            </Provider>
        );

        cy.get('a[href="/login"]').should('contain', 'LOGIN');
    });

    it('renders the header with LOGOUT link when authenticated', () => {
        const store = createStore(true);

        cy.mount(
            <Provider store={store}>
                <Router>
                    <Header />
                </Router>
            </Provider>
        );

        cy.get('a[href="/logout"]').should('contain', 'LOGOUT');
    });

    it('renders the header with ADMIN link when userLevel is ADMIN', () => {
        // Stub the network request
        cy.intercept('GET', 'http://localhost:8080/user/get', { fixture: 'header/getAdminUser.json' }).as('getAdminUser');

        const store = createStore(true, 'ADMIN');

        cy.mount(
            <Provider store={store}>
                <Router>
                    <Header />
                </Router>
            </Provider>
        );

        // Wait for the network request to complete
        cy.wait('@getAdminUser');

        // Find the ADMIN link with a longer timeout
        cy.findByRole('link', { name: /admin/i }, { timeout: 10000 }).should('be.visible');
    });

    it('does not render the header with ADMIN link when userLevel is not ADMIN', () => {
        // Stub the network request
        cy.intercept('GET', 'http://localhost:8080/user/get', { fixture: 'header/getReaderUser.json' }).as('getReaderUser');

        const store = createStore(true, 'USER');

        cy.mount(
            <Provider store={store}>
                <Router>
                    <Header />
                </Router>
            </Provider>
        );

        // Wait for the network request to complete
        cy.wait('@getReaderUser');

        cy.get('a[href="/admin"]').should('not.exist');
    });
});


