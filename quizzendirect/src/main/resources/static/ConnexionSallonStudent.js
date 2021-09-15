

// on ready
$(function(){

    // lors de la soumission du formulaire, faire les appels à l'api nécéssaire pour vérifier que le code entrée est bon
    $("#form_connex_salon_student").submit(function(event){
        // query qui récupére les codes d'accés de tous les salons
        let query = "query{" +
            "  allSalons{" +
            "    codeAcces" +
            "  }" +
            "}";
        const donnees = callAPI(query);
        donnees.then(object => {
            // récupére tous les salons
            let allSalon = object.data.allSalons;

            // récupérer le code d'accés entré par l'étudiant
            let codeAccesInput = $("#idSalon").val();

            // bool pour savoir si le code d'accés à été trouvé ou non
            let bonCodeAcces = false;
            for (const salon of allSalon) {
                if(salon.codeAcces==codeAccesInput) bonCodeAcces = true;
            }

            // si le code d'accés est le bon, on renvoie l'étudiant sur la page quiz avec le CODEACCES en GET
            if(bonCodeAcces) {

                window.location.href="/quiz?codeAcces="+codeAccesInput+"";
            }
        });

        // arrivé ici, le code entré est mauvais, donc on empéche l'envoie du formulaire
        event.preventDefault();

    });
});