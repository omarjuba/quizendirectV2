describe('test création enseignant',() =>{
	beforeEach(() => {

	cy.visit('localhost:8081/')
	Cypress.on('uncaught:exception', (err, runnable) => {
  
  	return false
	
	})


	})

	//quand on click directement sur submit
	it('message erreur mail doit être univ anger', () =>{
		
		cy.get('.modal-content').should('exist')
    		cy.get('button.btn:nth-child(3)').click()
		

		cy.get('.col-md-6').should('exist')
		cy.get('#register-form-link').click()
		
		cy.get('.col-md-6').should('be.visible')
		cy.get('#register-submit').click()
		cy.on('window:alert', (str) => {
        		expect(str).to.equal('l\'email doit étre une adresse de l\'université d\'Angers')
		})



	})
	
	//ce test n'est vrai qu'au 1er lancement de l'appli
	it('creer un enseignant inexistant', () => {
   
		cy.get('.modal-content').should('exist')
    		cy.get('button.btn:nth-child(3)').click()
		

		cy.get('.col-md-6').should('exist')
		cy.get('#register-form-link').click()
		
		cy.get('.col-md-6').should('be.visible')
		cy.get('#register-prenom').type('Dupont')
		cy.get('#register-name').type('Martin')
		cy.get('#register-email').type('dupont.martin@univ-angers.fr')
		cy.get('#register-password').type('Dupont45Martin78')
		cy.get('#register-confirm-password').type('Dupont45Martin78')
		
		cy.get('#register-submit').click()
		cy.get('.modal-content').should('exist')

		

       })

	it('creer un enseignant existant', () => {
   
		cy.get('.modal-content').should('exist')
    		cy.get('button.btn:nth-child(3)').click()
		

		cy.get('.col-md-6').should('exist')
		cy.get('#register-form-link').click()
		
		cy.get('.col-md-6').should('be.visible')
		cy.get('#register-prenom').type('Dupont')
		cy.get('#register-name').type('Martin')
		cy.get('#register-email').type('dupont.martin@univ-angers.fr')
		cy.get('#register-password').type('Dupont45Martin78')
		cy.get('#register-confirm-password').type('Dupont45Martin78')
		
		cy.get('#register-submit').click()
		cy.on('window:alert', (str) => {
     			expect(str).to.equal('Cet enseignant existe déjà.')
		})

		

       })

	it('connexion enseignant avec les bons identifiant et deconnexion', () => {
   
		cy.get('.modal-content').should('exist')
    		cy.get('button.btn:nth-child(3)').click()
		

		cy.get('.col-md-6').should('exist')
		cy.get('#login-email').type('dupont.martin@univ-angers.fr')
		cy.get('#login-password').type('Dupont45Martin78')
		//obligé de cliquer deux fois à cause bug		
		cy.get('#login-submit').click()
		cy.get('#login-submit').click()

		cy.get('button.btn').should('exist')
		cy.get('button.btn').click()

       })

	//faire le test de connexion avec des mauvais id une fois le bug corrigé


})
