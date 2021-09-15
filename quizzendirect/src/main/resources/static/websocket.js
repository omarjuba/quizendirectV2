var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
};

function connect() {
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
        setConnected(true);
        stompClient.subscribe('/quiz/salon', function (question) {
            getQuestion(JSON.parse(question.body));
        });
    });
};

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
};

function sendQuestion() {
    stompClient.send("/app/salon", {}, JSON.stringify(
        {
                'id_quest': 1,
                'intitule': 'questionX',
                'choixUnique': false,
                'reponsesBonnes': [],
                'reponsesFausses': [],
                'time': 15,
                'nbBonneReponse': 0,
                'nbMauvaiseReponse': 0
            }
        )
    );
};


function getQuestion(message) {
    $("#greetings").append("<p> Question en cours" + message.id_quest + "</p>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendQuestion(); });
});
