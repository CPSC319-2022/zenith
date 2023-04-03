describe('SinglePost component', () => {
  it('renders single post', () => {
    cy.intercept('GET', 'http://localhost:8080/post/get?postID=40', {fixture: 'singlePost/singlePost.json'}).as('getSinglePost');
    cy.visit('http://localhost:3000/single-post/40');
    cy.wait('@getSinglePost');
  });
});
