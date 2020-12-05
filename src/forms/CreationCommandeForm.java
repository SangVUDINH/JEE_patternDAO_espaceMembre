package forms;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.joda.time.DateTime;

import beans.Client;
import beans.Commande;
import dao.ClientDao;
import dao.CommandeDao;
import dao.DAOException;

public class CreationCommandeForm {
    CommandeDao commandeDao;
    ClientDao   clientDao;

    public CreationCommandeForm( CommandeDao commandeDao, ClientDao clientDao ) {
        this.commandeDao = commandeDao;
        this.clientDao = clientDao;
    }

    private static final String CHAMP_DATE             = "dateCommande";

    private static final String CHAMP_MONTANT          = "montantCommande";
    private static final String CHAMP_MODE_PAIEMENT    = "modePaiementCommande";
    private static final String CHAMP_STATUT_PAIEMENT  = "statutPaiementCommande";
    private static final String CHAMP_MODE_LIVRAISON   = "modeLivraisonCommande";
    private static final String CHAMP_STATUT_LIVRAISON = "statutLivraisonCommande";

    private static final String ANCIEN_CLIENT          = "ancienClient";
    private static final String FORMAT_DATE            = "dd/MM/yyyy HH:mm:ss";

    private static final String CHAMP_CHOIX_CLIENT     = "choixNouveauClient";
    private static final String CHAMP_LISTE_CLIENTS    = "listeClients";
    private static final String SESSION_CLIENTS        = "clients";

    private String              resultat;
    private Map<String, String> erreurs                = new HashMap<String, String>();

    public String getResultat() {
        return resultat;
    }

    public void setResultat( String resultat ) {
        this.resultat = resultat;
    }

    public Map<String, String> getErreurs() {
        return erreurs;
    }

    public void setErreur( String key, String value ) {
        this.erreurs.put( key, value );
    }

    public Commande creerCommande( HttpServletRequest request ) {

        Client client = new Client();
        Commande commande = new Commande();

        String choixClient = getValeurChamp( request, CHAMP_CHOIX_CLIENT );
        if ( ANCIEN_CLIENT.equals( choixClient ) ) {

            String idAncienClientStr = getValeurChamp( request, CHAMP_LISTE_CLIENTS );
            Long idAncienClient = Long.parseLong( idAncienClientStr );

            HttpSession session = request.getSession();
            client = ( (Map<Long, Client>) session.getAttribute( SESSION_CLIENTS ) ).get( idAncienClient );

            if ( client != null ) {
                commande.setClient( client );
            }

        } else {
            CreationClientForm formClient = new CreationClientForm( clientDao );
            client = formClient.creerClient( request );
            erreurs = formClient.getErreurs();
        }

        String montantCommande = getValeurChamp( request, CHAMP_MONTANT );
        String modePaiementCommande = getValeurChamp( request, CHAMP_MODE_PAIEMENT );
        String statutPaiementCommande = getValeurChamp( request, CHAMP_STATUT_PAIEMENT );
        String modeLivraisonCommande = getValeurChamp( request, CHAMP_MODE_LIVRAISON );
        String statutLivraisonCommande = getValeurChamp( request, CHAMP_STATUT_LIVRAISON );

        // recuperer un format date
        DateTime dt = new DateTime();

        // validation de données
        try {

            commande.setDateCommande( dt );

            traiterClient( client, commande );

            traiterMontantCommande( montantCommande, commande );
            traiterModePaiement( modePaiementCommande, commande );
            traiterModeLivraison( modeLivraisonCommande, commande );
            traiterStatutPaiement( statutPaiementCommande, commande );
            traiterStatutLivraison( statutLivraisonCommande, commande );

            if ( erreurs.isEmpty() ) {
                resultat = " Succes de la creation commande !";
                commandeDao.creer( commande );
            } else {
                resultat = "Echec de la creation commande";
            }
        } catch ( DAOException e ) {
            resultat = " echec de la creation commande";
            e.printStackTrace();
        }

        return commande;
    }

    private void traiterClient( Client client, Commande commande ) {
        if ( client != null ) {
            commande.setClient( client );
            commande.setId_client( client.getId() );
        } else {
            setErreur( CHAMP_CHOIX_CLIENT, "CLIENT inconnu !" );
        }

    }

    private void traiterStatutLivraison( String statutLivraisonCommande, Commande commande ) {
        try {
            validationAuMoins2Carac( statutLivraisonCommande );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_STATUT_LIVRAISON, e.getMessage() );
        }
        commande.setStatutLivraisonCommande( statutLivraisonCommande );

    }

    private void traiterModeLivraison( String modeLivraisonCommande, Commande commande ) {
        try {
            validationAuMoins2Carac( modeLivraisonCommande );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_MODE_LIVRAISON, e.getMessage() );
        }
        commande.setModeLivraisonCommande( modeLivraisonCommande );

    }

    private void traiterStatutPaiement( String statutPaiementCommande, Commande commande ) {
        try {
            validationAuMoins2Carac( statutPaiementCommande );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_STATUT_PAIEMENT, e.getMessage() );
        }
        commande.setStatutPaiementCommande( statutPaiementCommande );

    }

    private void traiterModePaiement( String modePaiementCommande, Commande commande ) {
        try {
            validationAuMoins2Carac( modePaiementCommande );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_MODE_PAIEMENT, e.getMessage() );
        }
        commande.setModePaiementCommande( modePaiementCommande );
    }

    private void traiterMontantCommande( String montantCommande, Commande commande ) {
        try {
            validationMontant( montantCommande );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_MONTANT, e.getMessage() );
        }
        commande.setMontantCommande( Double.parseDouble( montantCommande ) );
    }

    private void validationAuMoins2Carac( String str ) throws FormValidationException {
        if ( str.length() < 2 ) {
            throw new FormValidationException( "le champs doit contenir au moins 2 caracteres !" );
        }

    }

    private void validationMontant( String montantCommande ) throws FormValidationException {

        if ( montantCommande == null ) {
            throw new FormValidationException( "rentrez une valeur !" );
        } else {
            double montant = Double.parseDouble( montantCommande );
            if ( montant < 0 ) {
                throw new FormValidationException( "montant commande doit etre positif !" );
            }
        }

    }

    private static String getValeurChamp( HttpServletRequest request, String champ ) {
        String valeur = request.getParameter( champ );

        if ( valeur == null || valeur.trim().length() == 0 ) {
            return null;
        } else {
            return valeur.trim();
        }
    }

}
