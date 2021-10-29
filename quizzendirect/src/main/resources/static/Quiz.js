/* WebSocket */
var stompClient = null;
var laquestion = null;
var allQuestion =[];
// Boolean used as a workaround to avoid the fact that the click event is launch twice when a answer is chosen
var boolQuery = true ;
// type de question envoyé par le prof. 0 pour une question unique, 1 pour une question multiple et 2 pour une question ouverte
var typeQuestion = -1;
//tableau qui contient les réponses sélectionnées
var LesReponses = [];
// variable pour vérifier si une réponse est dans le tableau LesReponses ou pas
var ReponseDansTableau = false;
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
		stompClient.subscribe('/quiz/salon/gettype/' + getQueryVariable("codeAcces"), function (text) {
			typeQuestion = text.body;
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
	/* Les choix pour les questions unique et multiple*/
	/* Un input type text pour les questions ouvertes*/
	if (typeQuestion < 2) {
		$(".quizLibre").attr('style', 'display:none');
		$(".quiz").attr('style', 'display:unset');
		$(".btn-validerMultiple").attr('style', 'display:none');
		$("label").attr('style','');
		$("label").attr('checked', false);
		LesReponses = [];
		if (typeQuestion == 1) {
			$('input[name="q_answer"]').attr('type','checkbox');
			$(".btn-validerMultiple").attr('style', 'display:unset');
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



// fonction vérifie si la réponse r est dans le tableau LesReponses ou pas 
// true => si la réponse est dans le tableau
// false => si la réponse n'est pas dans le tableau
function verifierReponse(r) {
	for (const reponse of LesReponses) {
		if (reponse == r) {
			ReponseDansTableau = true;
		}
	}
}

// fonction ajouter la répoonse sélectionnée dans le tableau LesReponses si cette réponse n'est pas dans le tableau
function ajouterReponse(reponse) {
	verifierReponse(reponse);
	if (ReponseDansTableau == false) {
		LesReponses.push(reponse);
	}
	ReponseDansTableau = false;
}

//fonction supprimer la réponse sélectionnée dans le tableau LesReponses si cette réponse est dans le tableau
function supprimerReponse(reponse) {
	verifierReponse(reponse);
	if (ReponseDansTableau == true) {
		var index = LesReponses.indexOf(reponse);
		if (index > -1) {
			LesReponses.splice(index,1);
			ReponseDansTableau = false;
		}
	}
}

/* Au chargement */
$(function () {
    /* Au démarrage, en attente d'une question */
    $('#loadbar').show();
    $("#quiz").fadeOut();
	$("#quizLibre").fadeOut();
    /* Quand un étudiant clique sur une réponse, le chargement s'affiche */
	//$(":input[name='q_answer']").attr('checked', false); => OK
	$("label").attr('checked', false);
	$("label").click(function () {
		var allInputs = $(":input[name='q_answer']").attr('type'); //afficher le type de input 
		
        if(boolQuery) {
			if (allInputs == "checkbox")
			{
				boolQuery=false
				var checkVal = $(this).attr("checked");
				console.log(checkVal);
				if (checkVal == undefined) {
					$(this).attr("checked", true);
					$(this).css('background-color','#1BADCD');
					var reponseValue = $(this).children(4)[2].innerHTML
					ajouterReponse(reponseValue);
					console.log(LesReponses);
				} else if (checkVal == "checked") {
					$(this).attr("checked", false);
					$(this).attr('style','');
					var reponseValue = $(this).children(4)[2].innerHTML
					supprimerReponse(reponseValue);
					console.log(LesReponses);
				}
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
        var reponseValue = $('.reponse').val();
        sendReponse(reponseValue);
        $('#loadbar').show();
        $("#quizLibre").fadeOut();	
    });


	$('#btnValiderMultiple').click(function () {	
		for (let reponseValue of LesReponses) {
			sendReponse(reponseValue);
			$('#loadbar').show();
            $("#quiz").fadeOut();
		}
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