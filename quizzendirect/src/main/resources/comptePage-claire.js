
/*

$(function () {
    // Pour gérer le visuel login/sign in
    $('#login-form-link, #login-form-link2').click(function (e) {
        $("#login-form").delay(100).fadeIn(100);
        $("#register-form").fadeOut(100);
        $('#register-form-link').removeClass('active');
        $('#login-form-link').addClass('active');
        e.preventDefault();
    });
    $('#register-form-link').click(function (e) {
        $("#register-form").delay(100).fadeIn(100);
        $("#login-form").fadeOut(100);
        $('#login-form-link').removeClass('active');
        $(this).addClass('active');
        e.preventDefault();
    });


    // Quand l'enseignant clique sur le boutton pour créer un compte
    document.getElementById("register-submit").addEventListener("click", function (e) {

        e.preventDefault();
        // on recupére ses informations
        var prenom = document.getElementById("register-prenom");
        var prenom_value = prenom.value;
        var nom = document.getElementById("register-name");
        var nom_value = nom.value;
        var email = document.getElementById("register-email");
        var email_value = email.value;
        var mdp1 = document.getElementById("register-password");
        var mdp1_value = mdp1.value;
        var mdp2 = document.getElementById("register-confirm-password");
        var mdp2_value = mdp2.value;

        // on impose le mail doit etre un mail de l'université d'Angers
        if (!email_value.endsWith("@univ-angers.fr")) {
            e.preventDefault();
            alert("l'email doit étre une adresse de l'université d'Angers");
        }

        // on impose que le mot de passe ait une longueur supérieur a 6 caractères
        else {
            if (mdp1_value.length < 6) {
                e.preventDefault();
                alert("Le mot de passe doit contenir au minimum 6 caractéres");
            } else {
                // on vérifie que les 2 mots de passes entrés sont égaux
                if (mdp1_value != mdp2_value) {
                    e.preventDefault();
                    alert("Les mots de passe ne correspondent pas");
                } else {
                    // On recupére tous les enseignants avec un appel à l'API
                    let nom_value_mutation = nom_value.toString();
                    let email_value_mutation = email_value.toString();
                    var mpd1_value_mutation = sha256(mdp1_value.toString());

                    let query_allEns = "{" +
                        "  getMailProf( mail : \"" + email_value_mutation + "\")" +
                        "}";
                    const donnees_ens = callAPI(query_allEns)
                    donnees_ens.then((object0) => {
                        var enseignantExist = false;
                        if (object0.data.getMailProf == email_value) {
                            enseignantExist = true;
                        }
                        mpd1_value_mutation = mdpCrypto
                        // si l email n'existe pas, on crée l'enseignant et les cookies/sessions
                        if (enseignantExist == false) {
                            let mutation = "mutation{ createEnseignant(nom:\"" + nom_value_mutation + "\"" +
                                "  mail:\"" + email_value_mutation + "\"" +
                                "  motdepasse:\"" + mpd1_value_mutation + "\")" +
                                "  {" +
                                "            ... on Enseignant{ " +
                                "                   id_ens " +
                                "                   nom" +
                                "                   mail" +
                                "   }" +
                                "  }" +
                                "}";
                            const donnees = callAPI(mutation);
                            donnees.then((objectcreatEns) => {
                                document.cookie = "userName= " + objectcreatEns.data.createEnseignant.nom
                                document.cookie = "userEmail=" + objectcreatEns.data.createEnseignant.mail
                                document.cookie = "userId_ens=" + objectcreatEns.data.createEnseignant.id_ens;
                                var id_ens = objectcreatEns.data.createEnseignant.id_ens
                                let tokenQuery = "query{ getToken(ens_Id : " + id_ens + "," +
                                    "  mdp:\"" + mpd1_value_mutation + "\")" +
                                    "}";
                                var token = callAPI(tokenQuery);
                                token.then((object) => {
                                    document.cookie = "token= " + object.data.getToken;
                                    verifyCookie()
                                    window.location.href = "connection";
                                })

                            })
                        }
                        // sinon on notifie que cet enseignant existe déjà avec une alerte
                        else {
                            alert("Cet enseignant existe déjà.");
                        }
                    })
                }
            }
        }
    });

    // Quand l'enseignant clique sur le boutton pour se connecter
    document.getElementById("login-submit").addEventListener("click", function (e) {

        e.preventDefault();
        // on récupére les informations saisies
        var email_connexion = document.getElementById("login-email");
        var email_connexion_value = email_connexion.value;
        var mdp_email = document.getElementById("login-password");
        sha256(mdp_email.value);
        mail = email_connexion_value
        mdpValue = mdp_email.value
        if (mdpCrypto != undefined) {
            doConnection()
        }
        else inDoConnect = true;
    });


});
var mdpCrypto;
var mail;
var mdpValue;
var inDoConnect=false;

function doConnection() {

    // on fait appel à l'API pour récupérer tous les enseignants
    let query_Ens = "{" +
        "  EnseignantVerification(mail : \"" + mail + "\", mdp : \"" + mdpCrypto + "\"){" +
        "    id_ens" +
        "    mail" +
        "    motdepasse" +
        "    nom" +
        "  }" +
        "}";

    const donnees_ens = callAPI(query_Ens)
    if (mdpCrypto != undefined)
        donnees_ens.then((object0) => {

            var enseignantExist = false;
            var mdpTrue = false;
            // si on trouve que le mail existe
            if (object0.data.EnseignantVerification.mail == mail) {
                enseignantExist = true;
                if (object0.data.EnseignantVerification.motdepasse == mdpCrypto) {
                    mdpTrue = true;
                    var id_ens = object0.data.EnseignantVerification.id_ens
                    var nom_user = object0.data.EnseignantVerification.nom
                    var user_mail = object0.data.EnseignantVerification.mail
                    let query_token = "{" +
                        "  getToken(ens_Id : \"" + id_ens + "\", mdp : \"" + mdpCrypto + "\")}";

                    var token = callAPI(query_token);

                    token.then((object) => {
                        document.cookie = "token= " + object.data.getToken;
                        document.cookie = "userName= " + nom_user;
                        document.cookie = "userEmail=" + user_mail;
                        document.cookie = "userId_ens=" + id_ens;
                        verifyCookie()
                        window.location.href = "connection";
                    })
                }
            }

            // On gére les erreurs
            if ((enseignantExist == true) && (mdpTrue == false)) {
                alert("Le mot de passe ou le mail son mauvais.");
            }
            if (enseignantExist == false) {
                alert("Cet enseignant n'existe pas");
            }


        })
}

function sha256(str) {
    // We transform the string into an arraybuffer.
    var buffer = new TextEncoder("utf-8").encode(str);
    var tmp;
    crypto.subtle.digest("SHA-256", buffer).then(function (hash) {
        mdpCrypto = hex(hash);
        if(inDoConnect) doConnection()
    });
    return mdpCrypto

}

function hex(buffer) {
    var hexCodes = [];
    var view = new DataView(buffer);
    for (var i = 0; i < view.byteLength; i += 4) {
        // Using getUint32 reduces the number of iterations needed (we process 4 bytes each time)
        var value = view.getUint32(i)
        // toString(16) will give the hex representation of the number without padding
        var stringValue = value.toString(16)
        // We use concatenation and slice for padding
        var padding = '00000000'
        var paddedValue = (padding + stringValue).slice(-padding.length)
        hexCodes.push(paddedValue);
    }

    // Join all the hex strings into one
    return hexCodes.join("");
}
*/