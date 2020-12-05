package dao;

import static dao.DAOUtilitaire.fermeturesSilencieuses;
import static dao.DAOUtilitaire.initialisationRequetePreparee;
import static dao.DAOUtilitaire.mapUtilisateur;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import beans.Utilisateur;

public class UtilisateurDaoImpl implements UtilisateurDao {

    private static final String SQL_SELECT_PAR_EMAIL = "SELECT id, email, nom, mot_de_passe, date_inscription FROM Utilisateur WHERE email = ?";
    private static final String SQL_INSERT           = " INSERT INTO Utilisateur (email, mot_de_passe, nom, date_inscription) VALUES (?,?,?,NOW())";
    private DAOFactory          daoFactory;

    UtilisateurDaoImpl( DAOFactory daoFactory ) {
        this.daoFactory = daoFactory;
    }

    @Override
    public void creer( Utilisateur utilisateur ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet valeursAutoGenerees = null;

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_INSERT, true, utilisateur.getEmail(),
                    utilisateur.getMotDePasse(), utilisateur.getNom() );

            int statut = preparedStatement.executeUpdate();
            if ( statut == 0 ) {
                throw new DAOException( "ECHEC de la creation d'utilisateur " );
            }

            valeursAutoGenerees = preparedStatement.getGeneratedKeys();
            if ( valeursAutoGenerees.next() ) {
                utilisateur.setId( valeursAutoGenerees.getLong( 1 ) );
            } else {
                throw new DAOException( "Echec de la creation utilisateur, Aucun ID auto-generé ! " );
            }

        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( valeursAutoGenerees, preparedStatement, connexion );
        }
    }

    @Override
    public Utilisateur trouver( String email ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Utilisateur utilisateur = null;

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_SELECT_PAR_EMAIL, false, email );
            resultSet = preparedStatement.executeQuery();

            if ( resultSet.next() ) {
                utilisateur = mapUtilisateur( resultSet );
            }

        } catch ( SQLException e ) {
            throw new DAOException( e );

        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }

        return utilisateur;
    }

}
