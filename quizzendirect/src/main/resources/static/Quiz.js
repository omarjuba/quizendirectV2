/* WebSocket */
var stompClient = null;
var laquestion = null;
var allQuestion =[];
// Boolean used as a workaround to avoid the fact that the click event is launch twice when a answer is chosen
var boolQuery = true;

/* Connecte le webSocket dés l'arrivée de la page */
(function connect() {
    let environement = window.location.hostname
    if (environement == "localhost"){
        environement = "://" + environement + ":20020";
    }
    else {
        environement = "s://" +environement
    }
    var socket = new SockJS('http'+ environement + '/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {

        // ajout du code d'accés selon la variable en get dans l'url
        stompClient.subscribe('/quiz/salon/' + getQueryVariable("codeAcces"), function (question) {
            getQuestion(JSON.parse(question.body));
        });

		stompClient.subscribe('/quiz/salon/closed/' + getQueryVariable("codeAcces"), function (text) {
            	closeQuizz(text.body);
        });
    });
})();


/*************************************************************************************/
/**				REDIRIGER UN ETUDIANT A LA FIN DU QUIZZ								**/
/************************************************************************************/
const sleep = ms => new Promise(res => setTimeout(res, ms));
async function closeQuizz(message){
	if (message == "closed"){
		
		
				
		
		$( ".modal-header" ).html( "<h3> Ce quiz est terminé </h3> " );	
		if(getCookie("studentmail") !=="" ){	
			let MailBody ="";
				MailBody = MailBody + "<fieldset><legend>Récapitulatif de votre Quiz</legend>";
			
			for(var i = 0; i < allQuestion.length; ++i){
				
				
				MailBody = MailBody + "<h3>N°"+(i+1).toString()+" "+allQuestion[i].question+":</h3>";
				MailBody = 	MailBody + "<ul>";
				MailBody = 	MailBody + "<li>Votre réponse: "+allQuestion[i].studentAnswer+"</li>";
				
				var propositions =(laquestion.reponsesBonnes).concat(laquestion.reponsesFausses);
	    		propositions.sort(() => Math.random() - 0.5);
				
				MailBody = MailBody + "<li> la bonne réponse :";
				for(var j = 0; j<propositions.length;++j){
					if(reponseIsGood(laquestion.reponsesBonnes,propositions[j])) {
						if(j == propositions.length -1 ) MailBody = 	MailBody +propositions[j]+" ";
						else MailBody = MailBody +propositions[j]+", ";
					}
				}
				MailBody = 	MailBody + "</li></ul>";
			}
			MailBody = 	MailBody + "</fieldset>";
			
			// envoi du mail au controlleur				 
			$.ajax({
			  method: "POST",
			  url: "getMail",
			  contentType : "application/json",
			  data: JSON.stringify(getCookie("studentmail"))
			})
			  .done(function( msg ) {
			    console.log( "Mail Status: " + msg );
			  });
	
	
			// envoi du contenu du mail au controlleur				 
			$.ajax({
			  method: "POST",
			  url: "getBody",
			  contentType : "application/json",
			  data: JSON.stringify(MailBody)
			})
			  .done(function( msg ) {
			    console.log( "Mail Status: " + msg );
			  });
	
			await sleep(3500);
			document.location.href = "/";

		}
		
	}
}

/*************************************************************************************/

var numero_question = 1;

function getQuestion(question) {

    laquestion = question;
    /* Cache le chargement et affiche la question */
    $('#loadbar').hide();
    $("#quiz").fadeIn();

    /* Remplis les informations des questions */
	$("#qid").html(numero_question);
    console.log(question.intitule);
       //creation du conteur QUill JS en lecture seulement
       let quillLectire = new Quill('#enonce', {
        theme: 'bubble',
        readOnly: true
    });
    document.getElementById("enonce").firstChild.innerHTML=question.intitule;
    //$("#enonce").html(question.intitule);
    $("#timer").html(question.time);

	numero_question = ++numero_question;

    /* Récupére toutes les réponses (bonnes et mauvaises) dans un tableau de propositions */
    var propositions = (question.reponsesBonnes).concat(question.reponsesFausses);
    /* Shuffle le tableau de propositions afin de ne pas avoir toujours l'ordre des bonnes réponses suivis des mauvaises réponses  */
    propositions.sort(() => Math.random() - 0.5);

    /* Remplis les propositions des questions */
    for (var i = 0; i < propositions.length; i++) {
        if (propositions[i] != "")
            $("#proposition" + (i + 1) + "").html(propositions[i]);
    }

    /* décrémente le timer */
    var time = question.time;

    function sleep(ms) {
        return new Promise(resolve => setTimeout(resolve, ms));
    }

    async function reduceTime() {
        while (time != 0) {
            await sleep(1000);
            $("#timer").html(time);
            time--;
        }
        $('#loadbar').show();
        $("#quiz").fadeOut();

    }

    reduceTime();
}

/* Au chargement */
$(function () {
    /* Au démarrage, en attente d'une question */
    $('#loadbar').show();
    $("#quiz").fadeOut();

    /* Quand un étudiant clique sur une réponse, le chargement s'affiche */
    $("label").click(function () {
        if(boolQuery) {
            boolQuery=false
            var reponseValue = $(this).children(4)[2].innerHTML
            sendReponse(reponseValue);
            $('#loadbar').show();
            $("#quiz").fadeOut();

        }else boolQuery =true
    });
});

function sendReponse(reponseVal) {
    if (laquestion != null) {
        //TODO ajouter IF() ELSE
        let query = "mutation{\n" +
            "  updateReponse(reponse : \"" + reponseVal.replaceAll("\\", "\\\\") + "\" , id_quest: " + laquestion.id_quest + " ){\n" +
            "  ... on Question {\n" +
            "    id_quest\n" +
            "  }" +
            "  }\n" +
            "}"

        const donnee = callAPI(query);
		
		var alldata = {"question" : laquestion.intitule,"choix" : laquestion.choixUnique ,"bonneReponse" : laquestion.reponsesBonnes,"bonneFausse" : laquestion.reponsesFausses  ,"studentAnswer" : reponseVal};
		allQuestion.push(alldata);
    }


}

function reponseIsGood(listeReponsesBonnes, reponseVal) {
    for (var i = 0; i < listeReponsesBonnes.length; i++) {
        if (reponseVal === listeReponsesBonnes[i]) {
            return true
        }
    }
    return false
}

$.getScript("callAPI.js", function () {
});

// fonction qui récupére une variable get pour récupérer le code d'accés entré par l'étudiant
function getQueryVariable(variable) {
    var query = window.location.search.substring(1);
    var vars = query.split("&");
    for (var i = 0; i < vars.length; i++) {
        var pair = vars[i].split("=");
        if (pair[0] == variable) {
            return pair[1];
        }
    }
    return (false);
}

function getCookie(name) {
    if (document.cookie.length == 0) return null;

    var regSepCookie = new RegExp('(; )', 'g');
    var cookies = document.cookie.split(regSepCookie);

    for (var i = 0; i < cookies.length; i++) {
        if (cookies[i].startsWith(name)) {
            return cookies[i].split("=")[1];
        }
    }
    return null;
}
