

var callAPI = async function (query) {
    let result;
    let environement = window.location.hostname
    if (environement == "localhost"){
        environement = "://" + environement + ":20020";
    }
    else {
        environement = "s://" +environement
    }
    try {
        result = await $.ajax({
            method: "POST",
            url: "http"+ environement + "/graphql",
            headers: {'Content-Type': 'application/json'},
            data: JSON.stringify({
                query: query
            })

        });

        return result;
    }
    catch(error) {
        console.error(error);
    }
};

//Function to check a cookie
function verifyCookie(){

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
/********** REMARQUE **************/
/* Pour accéder aux données (avec l'API) :
1) Ajouter la balise <script src="CHEMIN/callAPI.js" ></script> dans le fichier html
2) Faire appel à la fonction callAPI(query), qui prend en parametre une variable query, qui contiendra votre requete
 */





