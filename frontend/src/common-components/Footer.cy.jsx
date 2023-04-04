import React from 'react';
import Footer from './Footer'
import {mount} from "cypress/react18";

describe('<Footer />', () => {
  it('renders the footer text', () => {
    const text = 'Team Zenith CPSC 319 version 0.12.0';

    mount(<Footer />);

    cy.contains('footer', text);
  });
});
