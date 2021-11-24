describe('test reception des questions sur le salon de quiz',() =>{
	beforeEach(() => {

		cy.visit('localhost:8081/')
	
		Cypress.on('uncaught:exception', (err, runnable) => {
  
		  	return false
	
		})


	})



	it('conexion au salon', () => {
		
		cy.get('button.btn:nth-child(1)').click()
		cy.get('.btn').should('exist')

		cy.get('#idSalon').type('6317')
		cy.get('#studentmail').type('aristoteondongi10@gmail.com')
		cy.get('input.form-control:nth-child(4)').type('etudiant')

		cy.get('.btn').click()
		cy.get('#loadbar').should('exist')
		cy.wait(15000)
		
		cy.get('.ql-editor').should('exist')
		cy.get('#quizUnique > label:nth-child(2)').should('exist')
		cy.get('#quizUnique > label:nth-child(1)').should('exist')
		cy.get('#quizUnique > label:nth-child(3)').should('exist')
		cy.get('#quizUnique > label:nth-child(4)').should('exist')

		cy.get('#quizUnique > label:nth-child(2)').click()
		cy.get('#loadbar').should('exist')
		cy.wait(15000)

		cy.get('#btnReponseLibre').should('be.visible')
		cy.get('.col-8').should('be.visible')
		
		cy.get('.col-8').type('A')
		cy.get('#btnReponseLibre').click()
		cy.get('#loadbar').should('exist')

		cy.wait(15000)

		cy.get('#quizMultiple > label:nth-child(1)').should('exist')
		cy.get('#quizMultiple > label:nth-child(2)').should('exist')
		cy.get('#quizMultiple > label:nth-child(3)').should('exist')
		cy.get('#quizMultiple > label:nth-child(4)').should('exist')
		cy.get('#btnValiderMultiple').should('exist')

		cy.get('#quizMultiple > label:nth-child(2)').click()
		cy.get('#quizMultiple > label:nth-child(3)').click()

		cy.get('#btnValiderMultiple').click()

		cy.get('#loadbar').should('exist')


   
		
       })

	

})
