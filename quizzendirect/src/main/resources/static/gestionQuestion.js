$(document).on('click', '.panel-heading span.clickable', function(e){
    var $this = $(this);
    if(!$this.hasClass('panel-collapsed')) {
        $this.parents('.panel').find('.panel-body').slideUp();
        $this.addClass('panel-collapsed');
        $this.find('i').removeClass('glyphicon-chevron-up').addClass('glyphicon-chevron-down');
    } else {
        $this.parents('.panel').find('.panel-body').slideDown();
        $this.removeClass('panel-collapsed');
        $this.find('i').removeClass('glyphicon-chevron-down').addClass('glyphicon-chevron-up');
    }
});

$.getScript("callAPI.js",function (){
});
function getCookie(name){
    if(document.cookie.length == 0) return null;

    var regSepCookie = new RegExp('(; )', 'g');
    var cookies = document.cookie.split(regSepCookie);

    for(var i = 0; i < cookies.length; i++){
        if(cookies[i].startsWith(name)){
            return cookies[i].split("=")[1];
        }
    }
    return null;
}

/*******************Fonction pour l'API ********************************/
$(document).ready(function () {
    let userId_ens = getCookie("userId_ens")
    let token = getCookie("token")
    if(userId_ens == null) return
    let query = "{" +
        "   getEnseignantById(token : \""+ token +"\",  id_ens : \""+userId_ens+"\" ){" +
        "... on Enseignant{" +
        "       id_ens" +
        "       repertoires{" +
        "           nom" +
        "           questions{" +
        "               intitule" +
        "               time" +
        "           }" +
        "       }" +
        "   }" +
        "}" +
        "}"
    const donnees = callAPI(query)
    donnees.then((object) => {
        afficherRepertoires(object.data.getEnseignantById, userId_ens)
    });
})

function afficherRepertoires(data, userId_ens){
    if(data.id_ens == userId_ens){
        for(let j = 0; j < data.repertoires.length; j++){
            ajouterRepertoire(data.repertoires[j].nom);
            for(let k = 0; k < data.repertoires[j].questions.length; k++){
                ajouteQuestion(data.repertoires[j].nom,     data.repertoires[j].questions[k].intitule );
            }
        }
    }
}

function createRepertoire(nomRepertoire) {
    let email = getCookie("userEmail") ;
    let token = getCookie("token")
    let query = "mutation{\n" +
        "  createRepertoire(token : \""+token +"\", nom: \""+nomRepertoire+"\",enseignant:{mail:\""+email+"\"}){\n" +
        "  ...on Repertoire\n" +
        "    {\n" +
        "      nom:nom \n" +
        "    } ... on Error{ " +
        "message " +
        "}\n" +
        "  }\n" +
        "}"
    const donnee = callAPI(query);
}
function questionadded(id_rep,questions,enonce,choix,reponseBonnes,reponseFausses,time) {
    let token = getCookie("token")
    let query = "mutation{updateRepertoire(token : \""+ token +"\" , id_rep:"+id_rep+", questions:["
    if(questions.length > 0) {
        for (let i = 0; i < questions.length; i++) {
            query += '{intitule:"' + manageDoubleQuote(questions[i].intitule) + '",choixUnique:' + questions[i].choixUnique + ',reponsesBonnes:[' + questions[i].reponsesBonnes.map(rep => "\"" + manageDoubleQuote(rep) + "\"") + '],reponsesFausses:[' + questions[i].reponsesFausses.map(rep => "\"" + manageDoubleQuote(rep) + "\"") + '],time:' + questions[i].time + '}'
        }
        query += ","
    }
    query += '{intitule:\"' + manageDoubleQuote(enonce) + '",choixUnique:'+choix+',reponsesBonnes:['+reponseBonnes.map(rep => "\"" +manageDoubleQuote(rep) + "\"" )+'],reponsesFausses:['+reponseFausses.map(rep => "\"" + manageDoubleQuote(rep) + "\"")+'],time:'+ time + '}])' +
        '   {' +
        '     __typename' +
        '     ...on Error{' +
        '       message' +
        '     }' +
        '   }' +
        ' }'
    const donnee =  callAPI(query);
}
function getIdRepertory(data,userId_ens,nomrepository){

        if(data.id_ens == userId_ens){
            for(let j = 0; j < data.repertoires.length; j++){
               if(data.repertoires[j].nom.replace(/\s+/,'') == nomrepository) {
                   return data.repertoires[j].id_rep;
               }
            }
        }
    return -1;
}

function getQuestionByrepertoire(data,userId_ens,nomrepository) {

    if(data.id_ens == userId_ens){
        for(let j = 0; j < data.repertoires.length; j++){
            if(data.repertoires[j].nom.replace(/\s+/,'') == nomrepository ) {
                return data.repertoires[j].questions;
            }
        }
    }

    return [];
}

function enregistrementQuestion(enonce,choix,reponseBonnes,reponseFausses,time, nomRepertoire) {
    let token = getCookie("token")
    let enregistrementQuestion = "mutation{\n" +
        "  createQuestion(token : \""+ token +"\" , nomRepertoire: \""+ nomRepertoire+"\" intitule:\"" + manageDoubleQuote(enonce) + "\",choixUnique:"+choix+",reponsesBonnes:["+reponseBonnes+"],reponsesFausses:["+reponseFausses+"],time:"+time+"){\n" +
        "    __typename\n" +
        "  ...on Error{" +
        "message " +
        "}" +
        "}\n" +
        "}"
    callAPI(enregistrementQuestion);
}

function getIdQuestion(data,intitule) {
    for(let i=0 ; i < data.length ; i++)
    {
        if( deleteSpace(data[i].intitule) == deleteSpace(intitule)) {
            return data[i].id_quest;
        }
    }
    return -1;
}

function supprimeelement(id_quest) {
    let token = getCookie("token")
    let query = "mutation{\n" +
        "  removeQuestion(token : \""+ token +"\" , id_quest:"+id_quest+"){\n" +
        "    __typename\n" +
        "  }\n" +
        "}\n";
    callAPI(query);
}
//Renvoie true si une question existe , false sinon
function questionExiste(question)  {
    let exist = false;
    $('.col-md-7').each(function (){
        nomRepertoire = $(this).find('h3').html();
        let list_question = "#list_" + nomRepertoire.replace(/\s+/,'');
        let eachbutton = list_question + " button";
        $(eachbutton).each(function(){
            if($(this).html() == question) {
                exist = true;
            }
        })
    })

    return exist;

}

/***********************Fonction *******************************/
//Ajout des questions à un repertoire
function ajouteQuestion(nomRepertoire,enonce) {
    let query = '{\n' +
        '   getQuestionByIntitule(intitule : "'+ manageDoubleQuote(enonce) +'" ){' +
        '       id_quest\n' +
        '   }\n' +
        '}'
    const donnee = callAPI(query);
    donnee.then(object => {
        let question = object.data.getQuestionByIntitule
        let button = "<div class=\"btn-repertoire\"  style='margin-top: 2px;'><button id='ModifierQuestion_"+question.id_quest+"_"+nomRepertoire+"' type=\"button\" class=\"btn btn-lg btn-info btn-block\" data-toggle='modal' data-target='#modalPoll-1' style=\"width: 79%\">" + enonce + "</button> \ " +
            "<button class='btn btn-danger' id='sup' style='width: 20%; height: 45px;'>supprimer</button></div>";
        let list_question = "#list_" + nomRepertoire.replace(/\s+/,'');

        $(button).appendTo(list_question);
    })
}

function deleteSpace(string) {
    let i=0;
    while(string[i] ==' ') {
        i++;
    }
    return string.substr(i);
}
function ExistRep(nomNouveauRep) {
    let repexist = false;
    $('h3').each(function (){
        if($(this).html() == nomNouveauRep) {
            $('#NomRepertoire').css('border-color','red');
            $('#NomRepertoire').val(' ');
            repexist=true;
        }
    });
    return repexist;
}
function ajouterRepertoire(nomNouveauRep) {
    $('#NomRepertoire').css('border-color','black');
    let rep = "<div class=\"col-md-7\" id=\"nouveauRep\">\n" +
        "            <div class=\"panel panel-success\">\n" +
        "                <div class=\"panel-heading\">\n" +
        "                    <h3 class=\"panel-title\">Questions MongoDB</h3>\n" +
        "                    <span class=\"pull-right clickable\"><i class=\"glyphicon glyphicon-chevron-up\"></i></span>\n" +
        "                </div>\n" +
        "                <div class=\"panel-body\" id=\"listQuestion\">\n" +
        "                    <button type=\"button\" class=\"btn btn-sm btn-secondary\" id=\"AjouterQuestion_"+nomNouveauRep+ "\">+ Question</button>\n" +
        "                </div>\n" +
        "            </div>\n" +
        "        </div>";

    $('.row').append(rep);
    $("#nouveauRep h3").html(nomNouveauRep);
    createRepertoire(nomNouveauRep);

    let cpt = $('.row').children().length;
    let mod = cpt % 3;
    if (mod == 2) $("#nouveauRep > div").attr('class', "panel panel-primary");
    else if (mod == 1) $("#nouveauRep > div").attr('class', "panel panel-success");
    else if (mod == 0) $("#nouveauRep > div").attr('class', "panel panel-warning");


    $("#nouveauRep button").attr("data-target", "#modalPoll-1");
    $("#nouveauRep button").attr("data-toggle", "modal");

    let repositoryname = nomNouveauRep.replace(/\s+/,'');
    let id_rep = "id" + repositoryname.toString();
    let id_rep_quest = "AjouterQuestion_" + repositoryname.toString();
    let id_list_quest = "list_" + repositoryname.toString();

    $("#nouveauRep").attr("id", id_rep);
    $("#AjouterQuestion_" + nomNouveauRep).attr("id", id_rep_quest);
    $("#listQuestion").attr("id", id_list_quest);

    $(id_rep).val('');
}
function isChecked(checked, value) {
    for(let i=0 ; i < checked.length ; i++) {
        if( checked[i] == value ) return  true;
    }
    return false;
}
function nomRepCorrect(nomNouveauRep) {
    let correct = true;
    for(let i=0;i < nomNouveauRep.length; i++){
        if(nomNouveauRep[i] == '+') correct = false;
        if(nomNouveauRep[i] == '-') correct = false;
    }
    return correct;
}

function isGoodForm(){
    let isgood = true;
    if( $("#enonceQuestion").val().toString() == ''){ isgood=false; }
    $('input[name="group1"]').each(function () {
        let label_next = $(this).next();
        let input_into_the_label = label_next.children().val().toString();
        if (input_into_the_label == '') {
                isgood = false;
        }
    });
    return isgood;
}

function manageDoubleQuote(stringToManage) {
    return stringToManage.replaceAll("\\", "\\\\").replaceAll('"', '\\\"')
}

/***********************Gestion evénements clique sur la page *******************************/
$(document).on('click','#AjoutQuestion',function () {
    let enonce = $("#enonceQuestion").val().toString();
    let choix = true;
    if( $('#TypeChoix').val().toString() == "multiple") choix = false;
    let answerschecked =  $('input:checked').map(function (){ return $(this).val();}).get();
    let reponsesFausse = [];
    let reponsesBonnes = [];
    let nomRepertoire = $("#NomRepertoiremodal").text();
    if (questionExiste(enonce)) {
        alert("Question existe déjà dans un répertoire ");
    }
    else if(!isGoodForm()){
        alert("Formulaire mal rempli ! ");
    } else {
        //Fonction qui remplie le tableau de reponsesBonnes et Fausse en fonction des réponses sélectionnées
        $('input[name="group1"]').each(function () {

            let label_next = $(this).next();
            let input_into_the_label = label_next.children().val().toString();
            if (answerschecked.indexOf($(this).val()) != -1) {
                reponsesBonnes.push("\"" + manageDoubleQuote(input_into_the_label) + "\"");
            } else {
                reponsesFausse.push("\"" + manageDoubleQuote(input_into_the_label) + "\"");
            }
        });
        enregistrementQuestion(enonce, choix, reponsesBonnes, reponsesFausse, 10, nomRepertoire);
        let token = getCookie("token");
        let userId_ens = getCookie("userId_ens")
        let query = "{" +
            "   getEnseignantById(token : \""+ token +"\",  id_ens : \""+userId_ens+"\" ){" +
            "          ... on Enseignant{" +
            "           id_ens" +
            "           repertoires{" +
            "               nom" +
            "               id_rep" +
            "               questions{" +
            "                intitule\n" +
            "                choixUnique\n" +
            "                reponsesBonnes\n" +
            "                reponsesFausses\n" +
            "                    time\n" +
            "               }" +
            "           }" +
            "       }" +
            "   }" +
            "}"
        const donnee = callAPI(query);

        donnee.then((object) => {
            ajouteQuestion(nomRepertoire, enonce);
            let id_rep = getIdRepertory(object.data.getEnseignantById, userId_ens, nomRepertoire);
            let questions = getQuestionByrepertoire(object.data.getEnseignantById, userId_ens, nomRepertoire);
            questionadded(id_rep, questions, enonce, choix, reponsesBonnes, reponsesFausse, 10);
            $('#modalPoll-1').modal('hide');
        });
    }

});

$(document).on('click','#ModifierQuestion',function () {
    let enonce = $("#enonceQuestion").val().toString();
    let choix = true;
    if( $('#TypeChoix').val().toString() === "multiple") choix = false;

    let answerschecked =  $('input:checked').map(function (){ return $(this).val();}).get();
    let reponsesFausse = [];
    let reponsesBonnes = [];

    if(!isGoodForm()){
        alert("Formulaire mal rempli ! ");
    } else {
        //Fonction qui rempli le tableau de reponsesBonnes et Fausse en fonction des réponses sélectionnées
        $('input[name="group1"]').each(function () {
            let label_next = $(this).next();
            let input_into_the_label = label_next.children().val().toString();
            if (answerschecked.indexOf($(this).val()) !== -1) {
                reponsesBonnes.push("\"" + input_into_the_label + "\"");
            } else {
                reponsesFausse.push("\"" + input_into_the_label + "\"");
            }
        });

        let token = getCookie("token")
        let idQuest = $('#ModifierQuestion').attr('name');
        let updateQuery = "mutation{\n" +
            "  updateQuestion\n" +
            "  (token : \""+ token +"\" , id_quest: " + idQuest + ",\n" +
            "  intitule: \"" + manageDoubleQuote(enonce) + "\",\n" +
            "    choixUnique: "+ choix +",\n" +
            "    reponsesBonnes: [\n";
        reponsesBonnes.forEach(reponse => {
                updateQuery += "      " + reponse.replaceAll("\\", "\\\\") + "\n"
            }
        )

        updateQuery += "    ],\n" +
            "    reponsesFausses: [\n";
        reponsesFausse.forEach(reponse => {
            updateQuery += "      " + reponse.replaceAll("\\", "\\\\") + "\n"
        })

        updateQuery += "    ]\n" +
            "  ){\n" +
            "    ...on Question{\n" +
            "    \tid_quest\n" +
            "    }\n" +
            "    ... on Error{\n" +
            "      \tmessage\n" +
            "    }\n" +
            "\t}\n" +
            "}"

        callAPI(updateQuery);

        let nomRepertoire = $('#NomRepertoiremodal')[0].innerHTML;
        const id = 'ModifierQuestion_'+idQuest+'_'+nomRepertoire.replace(/\s+/,'');
        $('#'+id)[0].innerHTML = enonce;
        $('#modalPoll-1').modal('hide');
    }
});

//Création d'un répertoire
$(document).on('click','#modalRep',function (){

    let value = deleteSpace($('#NomRepertoire').val().toString());
    let nomNouveauRep = value.substr(0,1).toUpperCase() + value.substr(1);

    //Erreur si creation d'un repertoire existant
    if(ExistRep(nomNouveauRep) || nomNouveauRep ==''){
        let parent = $('#NomRepertoire').parent();
        $(parent).append("<Label id=\"error\" style=\"color: #8b0000; font-size:10px;\">Nom de répertoire existant : Choisissez-en un autre</Label>");
    }
    else if(nomRepCorrect(nomNouveauRep) == false)
    {
        let parent = $('#NomRepertoire').parent();
        $(parent).append("<Label id=\"error\" style=\"color: #8b0000; font-size:10px;\">Nom de répertoire incorrect : ne pas utiliser les caractères +,-</Label>");
    }
    else
    {
        ajouterRepertoire(nomNouveauRep);
        $('#error').remove();
    }
    $('#NomRepertoire').val('');

});

//Récupération des données lors de l'ouverture de la modale des questions (création ou modification)
$(document).on('click','.row button',function () {
    let id_bouton_cliquer= $(this).attr('id');
    let tab = id_bouton_cliquer.split("_");
    let idrep = "#id" + tab[1] + "> div";
    let classe = $(idrep).attr('class');

    let parent = $('#NomRepertoiremodal').parent();

    $("#NomRepertoiremodal").html(tab[2]);
    $("#NomRepertoiremodal").css('text-align', 'center');
    $("#NomRepertoiremodal").css('font-size', '30px');
    $("#NomRepertoiremodal").css('margin-top', '20px');

    // Changement de la couleur du titre de la modal
    if (classe == "panel panel-success") $(parent).css('background-color', '#58d68d');
    else if (classe == "panel panel-warning") $(parent).css('background-color', '#fcf3cf');
    else $(parent).css('background-color', '#3498db');
    if (tab[0] == "AjouterQuestion") {
        $('#NomRepertoiremodal').text(tab[1])
        $('#ModifierQuestion').hide();
        $('#AjoutQuestion').show();
        // Initialisation des différents champs dans la modale
        $('#enonceQuestion').val('');
        $('#TypeChoix').val('unique');
        $('#TypeChoix').click();
        let i = 0;
        $('input[name="group1"]').each(function () {
            let label_next = $(this).next();
            let input_into_the_label = label_next.children();
            input_into_the_label.val('');
            $(this).prop('checked', false);
            if (i==2) {
                $(this).prop('checked', true);
                input_into_the_label.css('background-color', '#22d0ae');
            } else {
                input_into_the_label.css('background-color', 'white');
            }
            i++;
        });
    } else if (tab[0] == "ModifierQuestion") {
        $('#AjoutQuestion').hide();
        $('#ModifierQuestion').show();

        let query = "{\n" +
            "  getQuestionById(id_quest : " + tab[1] +"){       \n" +
            "    ... on Question{\n" +
            "    id_quest\n" +
            "    intitule\n" +
            "    choixUnique\n" +
            "    reponsesBonnes\n" +
            "    reponsesFausses\n" +
            "  }\n" +
            "  }\n" +
            "}"
        const donnee = callAPI(query);
        donnee.then((object) => {
            let question = object.data.getQuestionById;
            $('#ModifierQuestion').attr('name', question.id_quest);
            $('#enonceQuestion').val(question.intitule);
            question.choixUnique ? $('#TypeChoix').val('unique') : $('#TypeChoix').val('multiple');
            $('#TypeChoix').click();
            let numberOfGoodAnswers = question.reponsesBonnes.length;
            for (let j=1; j<=numberOfGoodAnswers; j++) {
                $('#Choix'+j).val(question.reponsesBonnes[j-1]);
                $('#radio-'+j+'79').prop('checked', true);
            }
            for (let k=numberOfGoodAnswers+1; k<=4; k++) {
                $('#Choix'+k).val(question.reponsesFausses[4-k]);
                $('#radio-'+k+'79').prop('checked', false);
            }
        });



    }
});

//Fonction qui change le type de box pour les réponses : Checkbox où RadioButton
$(document).on('click',"#TypeChoix",function () {
    $choix = $(this).val().toString();
    if( $choix == "multiple") {
        $('.form-check-input').attr('type','checkbox');
    }
    else
    {
        $('.form-check-input').attr('type','radio');
        let i=0;
        $('input[name="group1"]').each(function (){
            let label_next = $(this).next();
            if( i == 2)
                $(this).attr('checked','true');
            else
                $(this).attr('checked','false');

            i++;
        })
    }
});
//Function qui gère le changement de couleur des reponses : Vert => Bonne reponse ,Rouge => mauvaise réponse
$(document).on('click','input[name="group1"]',function (){
    $choix = $('#TypeChoix').val().toString();
    if( $choix == "multiple") {
        let checked = $('input:checked').map(function (){ return $(this).val();}).get();
        $('input[name="group1"]').each(function () {
            let label_next = $(this).next();
            let input_into_the_label = label_next.children();
            if (  isChecked(checked,$(this).val()) ) {
                input_into_the_label.css('background-color', '#22d0ae');
            }
            else {
                $(this).attr('checked', 'false');
                input_into_the_label.css('background-color', 'red');
            }
        });
    }
    else
    {
        $(this).attr('checked', 'true');
        let input_selected = $(this);
        $('input[name="group1"]').each(function () {
            let label_next = $(this).next();
            let input_into_the_label = label_next.children();
            if ($(this).val() == input_selected.val() ) {
                $(this).attr('checked', 'true');
                input_into_the_label.css('background-color', '#22d0ae');
            }
            else {
                $(this).attr('checked', 'false');
                input_into_the_label.css('background-color', 'red');
            }
        });
    }
});

$(document).on('click','.btn.btn-danger', function (){
    let parent = $(this).parent();
    let IntituleQuestion = $(parent).children(':first-child').html();
    let query = "{\n" +
        "  allQuestions{\n" +
        "    intitule\n" +
        "    id_quest\n" +
        "  }\n" +
        "}"

    const donnee = callAPI(query);
    donnee.then((object) => {
        let id = getIdQuestion(object.data.allQuestions, IntituleQuestion);
        supprimeelement(id);
    });
    parent.remove();
} )

