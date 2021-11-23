# Deployer localement sur sa machine:

	Nécessite l'installations de docker  et docker-compose
	
## 0) Récupérer les sources du projet

	git clone https://github.com/omarjuba/quizendirectV2.git

        cd quizzendirectV2

## 1) configurations de fichier docker-compose.yml
 
  en ajoutant les trois envirenement:
      frontapp
      api
      Bd: postgres 

## 2) modifier le  fichier config.inginx 

     en ajoutant quizendirect.info-univ-angers.fr dans le serveur local 


## 3) Lancer le docker compose, il build maintenant les images pour vous:

       docker-compose up -d --build
	
## 4) Aller sur son navigateur  et acceder à quizendirect.info-univ-angers.fr


(sachant que l'on peut toujours acceder à l'application par le localhost)



# Deployer l'application sur les VPS du LERIA

## 0.1) Se connecter en ssh au cloud pédagogique et à la VM de type KVM
	ssh {loginENT}@leria-etud.univ-angers.fr -p 2019
	kvm.start
         patienter quelques instants
       kvm.connect
	

## 0.2) après la connection sur la VM DE Leria il nécessite les installations suivantes : 

	apt upgrade 
	
	curl :
		apt-get install curl

	apt-add-repository:
		apt-get install software-properties-common

	docker :
		suivre la documentation officielle disponible à l'adresse : https://docs.docker.com/engine/install/debian/

	docker-compose :
		curl -L "https://github.com/docker/compose/releases/download/1.28.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
		chmod +x /usr/local/bin/docker-compose
	github:
  
                apt-get install git 

## 1) Récupérer les sources du projet
	git clone https://github.com/omarjuba/quizendirectV2.git

        cd quizzendirectV2


## 2) Lancer le docker-compose
	docker-compose up -d --build

## 3) Acceder à l'application par l'adresse :

	https://etud-kvm-{loginENT}.leria-etud.univ-angers.fr

# Commandes pratiques :
	docker-compose down 
        docker-compose up -d
