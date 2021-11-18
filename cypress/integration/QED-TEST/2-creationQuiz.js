describe('test connexion, création repertoire + question',() =>{
	beforeEach(() => {

	cy.visit('localhost:8081/')
	Cypress.on('uncaught:exception', (err, runnable) => {
  
  	return false
	
	})


	})

	
	it('connexion enseignant et navigation sur le hub ', () => {
   
		cy.get('.modal-content').should('exist')
    		cy.get('button.btn:nth-child(3)').click()
		

		cy.get('.col-md-6').should('exist')
		cy.get('#login-email').type('dupont.martin@univ-angers.fr')
		cy.get('#login-password').type('Dupont45Martin78')
		//obligé de cliquer deux fois à cause bug		
		cy.get('#login-submit').click()
		

		cy.get('button.btn').should('exist')
		cy.get('.element-animation1').should('exist')
		cy.get('.element-animation2').should('exist')
		cy.get('.element-animation3').should('exist')

		cy.get('.element-animation1').click()
		cy.get('#connect').should('exist')
		cy.go('back')

		cy.get('.element-animation2').click()
		cy.get('#CreationRepertoire').should('exist')
		cy.go('back')

		cy.get('.element-animation3').click()
		cy.get('#form_connex_salon_student').should('exist')
		cy.go('back')
       })

	it('creation des repertoires et questions', () => {
   
		cy.get('.modal-content').should('exist')
    		cy.get('button.btn:nth-child(3)').click()
		

		cy.get('.col-md-6').should('exist')
		cy.get('#login-email').type('dupont.martin@univ-angers.fr')
		cy.get('#login-password').type('Dupont45Martin78')
		//obligé de cliquer deux fois à cause bug		
		cy.get('#login-submit').click()
		


		cy.get('.element-animation2').click()
		cy.get('#CreationRepertoire').click()
		cy.get('.md-form').should('exist')
		
		cy.get('#NomRepertoire').type('POO')
		cy.get('#modalRep').click()
		cy.get('.modal-side > div:nth-child(1) > div:nth-child(1) > button:nth-child(1) > span:nth-child(1)').click()
		cy.get('.row:last-child').should('exist')
		cy.get('#AjouterQuestion_POO').should('exist')

		cy.get('#AjouterQuestion_POO').click()
		cy.get('.modal-full-height > div:nth-child(1)').should('be.visible')		
       })

	it('creation des repertoires existants', () => {
   
		cy.get('.modal-content').should('exist')
    		cy.get('button.btn:nth-child(3)').click()
		

		cy.get('.col-md-6').should('exist')
		cy.get('#login-email').type('dupont.martin@univ-angers.fr')
		cy.get('#login-password').type('Dupont45Martin78')
		//obligé de cliquer deux fois à cause bug		
		cy.get('#login-submit').click()
		


		cy.get('.element-animation2').click()
		cy.get('#CreationRepertoire').click()
		cy.get('.md-form').should('exist')
		
		cy.get('#NomRepertoire').type('POO')
		cy.get('#modalRep').click()
		cy.get('#error').should('exist')
		
       })


	it('creation question ouverte', () => {
   
		cy.get('.modal-content').should('exist')
    		cy.get('button.btn:nth-child(3)').click()
		

		cy.get('.col-md-6').should('exist')
		cy.get('#login-email').type('dupont.martin@univ-angers.fr')
		cy.get('#login-password').type('Dupont45Martin78')
		//obligé de cliquer deux fois à cause bug		
		cy.get('#login-submit').click()
	


		cy.get('.element-animation2').click()
		cy.get('#AjouterQuestion_POO').click()
		cy.get('.modal-full-height > div:nth-child(1)').should('be.visible')
		

		// QUESTION OUVERTE 1 
		cy.get('#TypeChoix').select('Ouverte').should('have.value', 'ouverte')
		cy.get('#btn-plus-reponse').click()
		cy.get('#btn-plus-reponse').click()
		cy.get('#btn-plus-reponse').click()
		cy.get('#reponses-container').children().should('have.length', 4)
		cy.get('div.form-inline:nth-child(1) > input:nth-child(2)').type('A')
		cy.get('div.form-inline:nth-child(2) > input:nth-child(2)').type('B')
		cy.get('div.form-inline:nth-child(3) > input:nth-child(2)').type('C')
		cy.get('div.form-inline:nth-child(4) > input:nth-child(2)').type('D')
		cy.get('.ql-editor').type('Question ouverte 1')
		cy.get('#AjoutQuestion').click()		
		
       })
	
	it('creation question unique', () => {
   
		cy.get('.modal-content').should('exist')
    		cy.get('button.btn:nth-child(3)').click()
		

		cy.get('.col-md-6').should('exist')
		cy.get('#login-email').type('dupont.martin@univ-angers.fr')
		cy.get('#login-password').type('Dupont45Martin78')
		//obligé de cliquer deux fois à cause bug		
		cy.get('#login-submit').click()
		


		cy.get('.element-animation2').click()
		cy.get('#AjouterQuestion_POO').should('exist')

		// QUESTION UNIQUE
		cy.get('#AjouterQuestion_POO').click()
		cy.get('#TypeChoix').select('Unique').should('have.value','unique')
		cy.get('#radio-179').check()
		cy.get('#Choix1').type('A')
		cy.get('#Choix2').type('B')
		cy.get('#Choix3').type('C')
		cy.get('#Choix4').type('D')
		cy.get('.ql-editor').type('Question unique 1')
		cy.get('#AjoutQuestion').click()

		


		
		
       })
	it('creation question multiple', () => {
   
		cy.get('.modal-content').should('exist')
    		cy.get('button.btn:nth-child(3)').click()
		

		cy.get('.col-md-6').should('exist')
		cy.get('#login-email').type('dupont.martin@univ-angers.fr')
		cy.get('#login-password').type('Dupont45Martin78')
		//obligé de cliquer deux fois à cause bug		
		cy.get('#login-submit').click()
		


		cy.get('.element-animation2').click()
		cy.get('#AjouterQuestion_POO').should('exist')

		// QUESTION MULTIPLE
		cy.get('#AjouterQuestion_POO').click()
		cy.get('#TypeChoix').select('Ouverte')
		cy.get('#TypeChoix').select('Multiple')
		cy.get('#radio-179').check()
		cy.get('#radio-379').check()		
		cy.get('#Choix1').type('A')
		cy.get('#Choix2').type('B')
		cy.get('#Choix3').type('C')
		cy.get('#Choix4').type('D')
		cy.get('.ql-editor').type('Question multiple 1')
		cy.get('#AjoutQuestion').click()

		


		
		
       })

	it('creation d\'une question existante should alert', () => {
   
		cy.get('.modal-content').should('exist')
    		cy.get('button.btn:nth-child(3)').click()
		

		cy.get('.col-md-6').should('exist')
		cy.get('#login-email').type('dupont.martin@univ-angers.fr')
		cy.get('#login-password').type('Dupont45Martin78')
		//obligé de cliquer deux fois à cause bug		
		
		cy.get('#login-submit').click()


		cy.get('.element-animation2').click()
		cy.get('#AjouterQuestion_POO').click()
		cy.get('.modal-full-height > div:nth-child(1)').should('be.visible')
		

		cy.get('#TypeChoix').select('Ouverte').should('have.value', 'ouverte')
		cy.get('#btn-plus-reponse').click()
		cy.get('#btn-plus-reponse').click()
		cy.get('#btn-plus-reponse').click()
		cy.get('#reponses-container').children().should('have.length', 4)
		cy.get('div.form-inline:nth-child(1) > input:nth-child(2)').type('A')
		cy.get('div.form-inline:nth-child(2) > input:nth-child(2)').type('B')
		cy.get('div.form-inline:nth-child(3) > input:nth-child(2)').type('C')
		cy.get('div.form-inline:nth-child(4) > input:nth-child(2)').type('D')
		cy.get('.ql-editor').type('Question ouverte 1')
		cy.get('#AjoutQuestion').click()
		cy.on('window:alert', (str) => {
     			expect(str).to.equal('Question existe déjà dans un répertoire ')
		})
	
       })

	it('test double click pour se connecter', () => {
   
		cy.get('.modal-content').should('exist')
    		cy.get('button.btn:nth-child(3)').click()
		

		cy.get('.col-md-6').should('exist')
		cy.get('#login-email').type('dupont.martin@univ-angers.fr')
		cy.get('#login-password').type('Dupont45Martin78')

	
		cy.get('#login-submit').click()


		cy.get('.element-animation2').click()
		cy.get('#list_POO').children().should('have.length', 4)
	
       })


	it('test sauvegarde question', () => {
   
		cy.get('.modal-content').should('exist')
    		cy.get('button.btn:nth-child(3)').click()
		

		cy.get('.col-md-6').should('exist')
		cy.get('#login-email').type('dupont.martin@univ-angers.fr')
		cy.get('#login-password').type('Dupont45Martin78')
		//obligé de cliquer deux fois à cause bug		
		
		cy.get('#login-submit').click()


		cy.get('.element-animation2').click()
		cy.get('#list_POO').children().should('have.length', 4)
	
       })


	it('test formulaire mal rempli', () => {
   
		cy.get('.modal-content').should('exist')
    		cy.get('button.btn:nth-child(3)').click()
		

		cy.get('.col-md-6').should('exist')
		cy.get('#login-email').type('dupont.martin@univ-angers.fr')
		cy.get('#login-password').type('Dupont45Martin78')
		
		//obligé de cliquer deux fois à cause bug		
		
		cy.get('#login-submit').click()


		cy.get('.element-animation2').click()
		cy.get('#AjouterQuestion_POO').click()
		cy.get('.modal-full-height > div:nth-child(1)').should('be.visible')
		
		cy.get('#AjoutQuestion').click()
		cy.on('window:alert', (str) => {
     			expect(str).to.equal('Formulaire mal rempli ! ')
		})
	
       })

	
})

