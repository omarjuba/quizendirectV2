# Deployer localement sur sa machine:

	Nécessite docker et docker-compose

## 0) Récupérer les sources du projet
	git clone https://github.com/quizzendirect-dev/quizzendirect.git


## 1) modifier son fichier host
	sudo gedit /etc/hosts
	ajouter à la fin du fichier 
	127.0.0.1 quizendirect.info-univ-angers.fr

## 2) Lancer le docker compose, il build maintenant les images pour vous:
	docker-compose up -d --build

## 3) Aller sur son navigateur internet favori et acceder à quizendirect.info-univ-angers.fr
	(sachant que l'on peut toujours acceder à l'application par le localhost)


# Deployer l'application sur les VPS du LERIA

## 0.1) Se connecter en ssh au cloud pédagogique et à la VM de type KVM
	ssh {loginENT}@leria-etud.univ-angers.fr -p 2019
	mv kvm.stopped kvm.start
	patienter quelques instants

## 0.2) Nécessite les installations suivantes : 
	curl :
		apt-get install curl

	apt-add-repository:
		apt-get install software-properties-common

	docker :
		suivre la documentation officielle disponible à l'adresse : https://docs.docker.com/engine/install/debian/

	docker-compose :
		curl -L "https://github.com/docker/compose/releases/download/1.28.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
		chmod +x /usr/local/bin/docker-compose

## 1) Récupérer les sources du projet
	git clone https://github.com/quizzendirect-dev/quizzendirect.git
	cd quizzendirect

## 2) Lancer le docker-compose
	docker-compose up -d --build

## 3) Acceder à l'application par l'adresse :
	https://etud-kvm-{loginENT}.leria-etud.univ-angers.fr

# Commandes pratiques :
	docker-compose logs -f
	docker-compose restart
	docker-compose up -d --build ( api | frontapp | proxy )
