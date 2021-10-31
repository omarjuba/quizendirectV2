describe('test connexion salon',() =>{
	beforeEach(() => {

	cy.visit('localhost:8081/')
	Cypress.on('uncaught:exception', (err, runnable) => {
  
  	return false
	
	})


	})


	it('Création d\'un salon', () => {
   
		cy.get('.modal-content').should('exist')
    		cy.get('button.btn:nth-child(1)').click()
		cy.get('.modal-dialog').should('exist')

		cy.get('.btn').click()
		cy.on('window:alert', (str) => {
     			expect(str).to.equal('Aucun Salon n\'existe avec ce code ')
		})
		
		cy.get('#idSalon').type(5685)

		cy.get('.btn').click()
		cy.on('window:alert', (str) => {
     			expect(str).to.equal('Aucun Salon n\'existe avec ce code ')
		})
			
		cy.get('[pattern="[A-Za-z]{1,50}"]').type('random')
		cy.get('#studentmail').type('random.student@etud.univ-angers.fr')

		cy.get('.btn').click()
		cy.on('window:alert', (str) => {
     			expect(str).to.equal('Aucun Salon n\'existe avec ce code ')
		})
       })
})
