/* WebSocket */
var stompClient = null;
var laquestion = null;
// Boolean used as a workaround to avoid the fact that the click event is launch twice when a answer is chosen
var boolQuery = true ;
// type de question envoyé par le prof. 0 pour une question unique, 1 pour une question multiple et 2 pour une question ouverte
var typeQuestion = -1;

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
		stompClient.subscribe('/quiz/salon/gettype/' + getQueryVariable("codeAcces"), function (text) {
			typeQuestion = text.body;
        });
    });
})();

var numero_question = 1;

function getQuestion(question) {
	
    laquestion = question;
    /* Cache le chargement et affiche la question */
    $('#loadbar').hide();
    $("#quiz").fadeIn();

    /* Remplis les informations des questions */
	$("#qid").html(numero_question);
    $("#enonce").html(question.intitule);
    $("#timer").html(question.time);


	numero_question = ++numero_question;

	
    /* Récupére toutes les réponses (bonnes et mauvaises) dans un tableau de propositions */
    var propositions = (question.reponsesBonnes).concat(question.reponsesFausses);
    /* Shuffle le tableau de propositions afin de ne pas avoir toujours l'ordre des bonnes réponses suivis des mauvaises réponses  */
    propositions.sort(() => Math.random() - 0.5);

	
	/* Remplis les propositions des questions */
	/* Les choix pour les questions unique et multiple*/
	/* Un input type text pour les questions ouvertes*/
	if (typeQuestion < 2) {
		$(".quizLibre").attr('style', 'display:none');
		$(".quiz").attr('style', 'display:unset');
		$("label").attr('style','');
		if (typeQuestion == 1) {
			$('input[name="q_answer"]').attr('type','checkbox');
		} 
	    for (var i = 0; i < propositions.length; i++) {
	        if (propositions[i] != "") 
	            $("#proposition" + (i + 1) + "").html(propositions[i]);
	    }
 	} else {
		$(".quizLibre").attr('style', 'display:unset');
		$(".quiz").attr('style', 'display:none');
		$('input[name="answer_ouverte"]').val('');
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
		$("#quizLibre").fadeOut();

    }

    reduceTime();
}

/* Au chargement */
$(function () {
    /* Au démarrage, en attente d'une question */
    $('#loadbar').show();
    $("#quiz").fadeOut();
	$("#quizLibre").fadeOut();
    /* Quand un étudiant clique sur une réponse, le chargement s'affiche */
    $("label").click(function () {
		var allInputs = $(":input[name='q_answer']").attr('type'); //afficher le type de input 
        if(boolQuery) {
			if (allInputs == "checkbox")
			{
				boolQuery=false
				$(this).css('background-color','#1BADCD');
				var reponseValue = $(this).children(4)[2].innerHTML
	            sendReponse(reponseValue);
			} else {
				 boolQuery=false
	            var reponseValue = $(this).children(4)[2].innerHTML
	            sendReponse(reponseValue);
	            $('#loadbar').show();
	            $("#quiz").fadeOut();
			}
        }else boolQuery = true 
    });
	
    $('#btnReponseLibre').click(function () {
        if(boolQuery) {
            boolQuery=false
            var reponseValue = $('.reponse').val();
            sendReponse(reponseValue);
            $('#loadbar').show();
            $("#quizLibre").fadeOut();	
        } else boolQuery = true
    });
});



function sendReponse(reponseVal) {
    if (laquestion != null) {
        //TODO ajouter IF() ELSE
		// la requête pour les questions unique + multiple
		if (typeQuestion  < 2) {
	        let query = "mutation{\n" +
	            "  updateReponse(reponse : \"" + reponseVal.replaceAll("\\", "\\\\") + "\" , id_quest: " + laquestion.id_quest + " ){\n" +
	            "  ... on Question {\n" +
	            "    id_quest\n" +
	            "  }" +
	            "  }\n" +
	            "}"	
       	 	const donnee = callAPI(query);

		}  else { // la requête pour les questions ouvertes 
			 let query = "mutation{\n" +
	            "  updateReponse(reponse : \"" + reponseVal.replaceAll("\\") + "\" , id_quest: " + laquestion.id_quest + " ){\n" +
	            "  ... on Question {\n" +
	            "    id_quest\n" +
	            "  }" +
	            "  }\n" +
	            "}"	
       	 	const donnee = callAPI(query);
		} 
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
