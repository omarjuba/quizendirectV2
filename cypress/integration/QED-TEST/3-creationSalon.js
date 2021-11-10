describe('test création salon + lancement question + fermeture salon',() =>{
	beforeEach(() => {

	cy.visit('localhost:8081/')
	Cypress.on('uncaught:exception', (err, runnable) => {
  
  	return false
	
	})


	})


	it('test création salon', () => {
   
		cy.get('.modal-content').should('exist')
    		cy.get('button.btn:nth-child(3)').click()
		

		cy.get('.col-md-6').should('exist')
		cy.get('#login-email').type('dupont.martin@univ-angers.fr')
		cy.get('#login-password').type('Dupont45Martin78')
		//obligé de cliquer deux fois à cause bug		
		cy.get('#login-submit').click()
		cy.get('#login-submit').click()

		cy.get('.element-animation1').click()
		cy.get('#connect').should('exist')
		cy.get('.repertoire').should('exist')
		cy.get('.repertoire').click()
		
		cy.get(':nth-child(1) > .button-ajouter').click()
		cy.get(':nth-child(2) > .button-ajouter').click()
		cy.get(':nth-child(3) > .button-ajouter').click()

		cy.get('.selected-questions > :nth-child(1)').should('exist')
		cy.get('.selected-questions > :nth-child(2)').should('exist')
		cy.get('.selected-questions > :nth-child(3)').should('exist')
		cy.get('.button-supprAll').should('exist')

		cy.get('.button-supprAll').click()
				
		cy.get(':nth-child(1) > .button-ajouter').should('exist')
		cy.get(':nth-child(2) > .button-ajouter').should('exist')
		cy.get(':nth-child(3) > .button-ajouter').should('exist')

		cy.get(':nth-child(1) > .button-ajouter').click()
		cy.get(':nth-child(2) > .button-ajouter').click()
		cy.get(':nth-child(3) > .button-ajouter').click()

		cy.get(':nth-child(1) > .question-buttons > .button-supprimer').click()
		cy.get(':nth-child(1) > .button-ajouter').should('exist')
		cy.get(':nth-child(1) > .button-ajouter').click()

		cy.get('#connect').click()
		cy.get('.code-acces').should('exist')
		cy.get('#disconnect').should('exist')
		cy.get('#disconnect').click()

		cy.get('#connect').should('exist')
		cy.get('#connect').click()
		
		cy.get(':nth-child(1) > .info-question > .time-info > #time').clear()
		cy.get(':nth-child(1) > .info-question > .time-info > #time').type(15)
		cy.get(':nth-child(1) > .question-buttons > .button-lancer').click()
		cy.wait(1500)
		
		cy.get(':nth-child(1) > .question-buttons > .button-stat').click()
		cy.get('#myChart').should('exist')
		cy.get('#closeModalStat').click()
		
		
       })
})
