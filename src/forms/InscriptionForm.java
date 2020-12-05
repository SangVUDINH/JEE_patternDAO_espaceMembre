package forms;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jasypt.util.password.ConfigurablePasswordEncryptor;

import beans.Utilisateur;
import dao.DAOException;
import dao.UtilisateurDao;

public final class InscriptionForm {

    private UtilisateurDao utilisateurDao;

    public InscriptionForm( UtilisateurDao utilisateurDao ) {
        this.utilisateurDao = utilisateurDao;
    }

    private static final String CHAMP_EMAIL      = "email";
    private static final String CHAMP_PASS       = "motdepasse";
    private static final String CHAMP_CONF       = "confirmation";
    private static final String CHAMP_NOM        = "nom";
    private static final String ALGO_CHIFFREMENT = "SHA-256";

    private String              resultat;
    private Map<String, String> erreurs          = new HashMap<String, String>();

    public Map<String, String> getErreurs() {
        return erreurs;
    }

    public void setErreur( String key, String value ) {
        this.erreurs.put( key, value );
    }

    public String getResultat() {
        return resultat;
    }

    public void setResultat( String resultat ) {
        this.resultat = resultat;
    }

    public beans.Utilisateur inscrireUtilisateur( HttpServletRequest request ) {

        String email = getValeurChamp( request, CHAMP_EMAIL );
        String motDePasse = getValeurChamp( request, CHAMP_PASS );
        String confirmation = getValeurChamp( request, CHAMP_CONF );
        String nom = getValeurChamp( request, CHAMP_NOM );

        Utilisateur utilisateur = new Utilisateur();

        try {

            traiterEmail( email, utilisateur );
            traiterMotsDePasse( motDePasse, confirmation, utilisateur );
            traiterNom( nom, utilisateur );

            if ( erreurs.isEmpty() ) {
                utilisateurDao.creer( utilisateur );
                resultat = "Succès de l'inscription.";
            } else {
                resultat = "Échec de l'inscription.";
            }
        } catch ( DAOException e ) {
            resultat = "Echec de l'inscription ";
            e.printStackTrace();
        }

        return utilisateur;
    }

    private void traiterEmail( String email, Utilisateur utilisateur ) {
        try {
            validationEmail( email );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_EMAIL, e.getMessage() );
        }
        utilisateur.setEmail( email );
    }

    private void traiterMotsDePasse( String motDePasse, String confirmation, Utilisateur utilisateur ) {
        try {
            validationMotsDePasse( motDePasse, confirmation );

        } catch ( FormValidationException e ) {

            setErreur( CHAMP_PASS, e.getMessage() );
            setErreur( CHAMP_CONF, null );

        }

        ConfigurablePasswordEncryptor passwordEncryptor = new ConfigurablePasswordEncryptor();
        passwordEncryptor.setAlgorithm( ALGO_CHIFFREMENT );
        passwordEncryptor.setPlainDigest( false );

        String motDePasseChiffre = passwordEncryptor.encryptPassword( motDePasse );

        utilisateur.setMotDePasse( motDePasseChiffre );

    }

    private void traiterNom( String nom, Utilisateur utilisateur ) {
        try {
            validationNom( nom );

        } catch ( FormValidationException e ) {

            setErreur( CHAMP_NOM, e.getMessage() );

        }

        utilisateur.setNom( nom );

    }

    private void validationMotsDePasse( String motDePasse, String confirmation ) throws FormValidationException {
        if ( motDePasse != null && confirmation != null ) {
            if ( !motDePasse.equals( confirmation ) ) {
                throw new FormValidationException(
                        "Les mots de passe entrés sont différents, merci de les saisir à nouveau." );
            } else if ( motDePasse.length() < 3 ) {
                throw new FormValidationException( "Les mots de passe doivent contenir au moins 3 caractères." );
            }
        } else {
            throw new FormValidationException( "Merci de saisir et confirmer votre mot de passe." );
        }
    }

    private void validationNom( String nom ) throws FormValidationException {
        if ( nom != null && nom.length() < 3 ) {
            throw new FormValidationException( "Le nom d'utilisateur doit contenir au moins 3 caractères." );
        }
    }

    private void validationEmail( String email ) throws FormValidationException {
        if ( email != null ) {
            if ( !email.matches( "([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)" ) ) {
                throw new FormValidationException( "Merci de saisir une adresse mail valide." );
            } else if ( utilisateurDao.trouver( email ) != null ) {
                throw new FormValidationException( " cette adresse email est déja utilisée !" );

            }
        } else {
            throw new FormValidationException( "Merci de saisir une adresse mail." );
        }

    }

    private static String getValeurChamp( HttpServletRequest request, String champNom ) {
        String valeur = request.getParameter( champNom );
        if ( valeur == null || valeur.trim().length() == 0 ) {
            return null;
        } else {
            return valeur.trim();
        }
    }

}
