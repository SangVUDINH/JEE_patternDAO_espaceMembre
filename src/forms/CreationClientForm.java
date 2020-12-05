package forms;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import beans.Client;
import dao.ClientDao;
import dao.DAOException;

public class CreationClientForm {

    ClientDao clientDao;

    public CreationClientForm( ClientDao clientDao ) {
        this.clientDao = clientDao;
    }

    private static final String CHAMP_PRENOM  = "prenomClient";
    private static final String CHAMP_NOM     = "nomClient";
    private static final String CHAMP_ADRESSE = "adresseClient";
    private static final String CHAMP_TEL     = "telephoneClient";
    private static final String CHAMP_EMAIL   = "emailClient";

    private String              resultat;
    private Map<String, String> erreurs       = new HashMap<String, String>();

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

    public Client creerClient( HttpServletRequest request ) {

        String prenomClient = getValeurChamp( request, CHAMP_PRENOM );
        String nomClient = getValeurChamp( request, CHAMP_NOM );
        String adresseClient = getValeurChamp( request, CHAMP_ADRESSE );
        String telClient = getValeurChamp( request, CHAMP_TEL );
        String emailClient = getValeurChamp( request, CHAMP_EMAIL );

        Client client = new Client();

        try {
            traiterEmail( emailClient, client );
            traiterNom( nomClient, client );
            traiterPrenom( prenomClient, client );
            traiterAdresse( adresseClient, client );
            traiterTel( telClient, client );

            if ( erreurs.isEmpty() ) {
                clientDao.creer( client );
                resultat = "Succès de la creation Client !";
            } else {
                resultat = "Echec de la creation";
            }
        } catch ( DAOException e ) {
            resultat = "echec de la creation client ";
            e.printStackTrace();
        }

        return client;
    }

    private void traiterTel( String telClient, Client client ) {
        try {
            validationTel( telClient );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_TEL, e.getMessage() );
        }
        client.setTelephoneClient( telClient );
    }

    private void traiterAdresse( String adresseClient, Client client ) {
        try {
            validationAdresse( adresseClient );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_ADRESSE, e.getMessage() );
        }
        client.setAdresseClient( adresseClient );
    }

    private void traiterPrenom( String prenomClient, Client client ) {
        try {
            validationPrenom( prenomClient );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_PRENOM, e.getMessage() );
        }
        client.setPrenomClient( prenomClient );
    }

    private void traiterNom( String nomClient, Client client ) {
        try {
            validationNom( nomClient );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_NOM, e.getMessage() );
        }
        client.setNomClient( nomClient );

    }

    private void traiterEmail( String email, Client client ) {
        try {
            validationEmail( email );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_EMAIL, e.getMessage() );
        }

        client.setEmailClient( email );
    }

    private void validationPrenom( String prenomClient ) throws FormValidationException {
        if ( prenomClient != null && prenomClient.length() < 3 ) {
            throw new FormValidationException( "le prenom client doit contenir au moins 3 caractères." );
        }
    }

    private void validationNom( String prenomClient ) throws FormValidationException {
        if ( prenomClient != null && prenomClient.length() < 3 ) {
            throw new FormValidationException( "le nom client doit contenir au moins 3 caractères." );
        }
    }

    private void validationAdresse( String adresseClient ) throws FormValidationException {
        if ( adresseClient != null && adresseClient.length() < 11 ) {
            throw new FormValidationException( "l'adresse client doit contenir au moins 10 caractères." );
        }
    }

    private void validationTel( String telClient ) throws FormValidationException {
        if ( telClient != null && telClient.length() < 5 ) {
            throw new FormValidationException( "tel client doit contenir au moins 4 numéros." );
        }
    }

    private void validationEmail( String email ) throws FormValidationException {
        if ( email != null ) {
            if ( !email.matches( "([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)" ) ) {
                throw new FormValidationException( "Merci de saisir une adresse mail valide." );
            } else if ( clientDao.trouver( email ) != null ) {
                throw new FormValidationException( "cette adresse email est déja utilisée !" );
            }
        }

        else {
            throw new FormValidationException( "Merci de saisir une adresse mail." );
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
