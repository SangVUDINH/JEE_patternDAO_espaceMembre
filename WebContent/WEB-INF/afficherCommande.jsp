<%@ page pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Affichage d'une commande</title>
        <link type="text/css" rel="stylesheet" href="<c:url value="/inc/style.css"/>" />
    </head>
    <body>
        <c:import url="/inc/menu.jsp" />
        <div id="corps">
            <p class="info">${ form.resultat }</p>
            <p>Client</p>
            	<p>ID_CLIENT  : <c:out value="${ commande.id_client }"/></p> 
            <p>Commande</p>
            <p>Date  : <c:out value="${ commande.dateCommande }"/></p> 
            <p>Montant  : <c:out value="${ commande.montantCommande }"/></p> 
            <p>Mode de paiement  : <c:out value="${ commande.modePaiementCommande }"/></p> 
            <p>Statut du paiement  : <c:out value="${ commande.statutPaiementCommande }"/></p> 
            <p>Mode de livraison  : <c:out value="${ commande.modeLivraisonCommande }"/></p> 
            <p>Statut de la livraison  : <c:out value="${ commande.statutLivraisonCommande }"/></p> 
        </div>
    </body>
</html>