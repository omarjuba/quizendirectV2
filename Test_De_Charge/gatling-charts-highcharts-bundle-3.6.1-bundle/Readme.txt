---------------------------- GATLING -------------------

Qu'est ce que c'est ?
- Gatling est un outil open-source pour les tests de performance et de stress.
- prendre en charge des protocoles comme HTTP, WebSockets, et JMS
- En utilisant ce mode Gatling peut simuler plusieurs utilisateurs virtuels avec un seul thread


Lancer le test 
- allez dans le répertoire "gatling-charts-highcharts-bundle-3.6.1/bin/" => exécutez le fichier "gatling.sh" (sous Linux) ou "gatling.bat" (sous Windows)
- sélectionnez le fichier de test que vous voulez tester 
- une fois que vous avez sélectionné le fichier, le test sera lancé.



Les testes de simulation
- Les fichiers de tests sont dans le répertoire "gatling-charts-highcharts-bundle-3.6.1-bundle\gatling-charts-highcharts-bundle-3.6.1\user-files\simulations"
- Le 1er fichiers "nombreEtudiants_Profs" teste un nombre donnée des professseurs qui se connectent et créent des questions + les étudiants qui se connectent dans un salon 
- Le 2eme fichiers "TestAffichage" teste le temps d'affichage quand un profs lance une question pour un nombre donnée des étudiants qui se connectent dans un salon 



Obtenir les résultats
- Les résultats sont stockés dans le répertoire "gatling-charts-highcharts-bundle-3.6.1/résults/ -> le nom de répertoire de votre test/ -> index.html".


