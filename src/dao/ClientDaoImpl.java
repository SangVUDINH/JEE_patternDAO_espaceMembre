package dao;

import static dao.DAOUtilitaire.fermeturesSilencieuses;
import static dao.DAOUtilitaire.initialisationRequetePreparee;
import static dao.DAOUtilitaire.mapClient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import beans.Client;

public class ClientDaoImpl implements ClientDao {

    private DAOFactory          daoFactory;
    private static final String SQL_INSERT           = "INSERT INTO Client(nom, prenom, adresse, telephone, email) VALUES (?,?,?,?,?)";
    private static final String SQL_SELECT_PAR_EMAIL = "SELECT id, nom , prenom, adresse, telephone, email FROM Client WHERE email = ?";
    private static final String SQL_SELECT           = "SELECT id, nom , prenom, adresse, telephone, email FROM Client ORDER BY id";
    private static final String SQL_DELETE           = "DELETE FROM Client WHERE id = ?";
    private static final String SQL_SELECT_PAR_ID    = "SELECT id, nom , prenom, adresse, telephone, email FROM Client WHERE id = ?";;

    ClientDaoImpl( DAOFactory daoFactory ) {
        this.daoFactory = daoFactory;
    }

    @Override
    public void creer( Client client ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet valeursAutoGenerees = null;

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_INSERT, true, client.getNomClient(),
                    client.getPrenomClient(), client.getAdresseClient(), client.getTelephoneClient(),
                    client.getEmailClient() );

            int statut = preparedStatement.executeUpdate();

            if ( statut == 0 ) {
                throw new DAOException( "Echec de la creation client!" );
            }

            valeursAutoGenerees = preparedStatement.getGeneratedKeys();

            if ( valeursAutoGenerees.next() ) {
                client.setId( valeursAutoGenerees.getLong( 1 ) );
            } else {
                throw new DAOException( "Echec de la creation client, aucun ID generé !" );
            }

        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( valeursAutoGenerees, preparedStatement, connexion );
        }

    }

    @Override
    public Client trouver( long id ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Client client = null;

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_SELECT_PAR_ID, false, id );
            resultSet = preparedStatement.executeQuery();

            if ( resultSet.next() ) {
                client = mapClient( resultSet );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }

        return client;
    }

    @Override
    public Client trouver( String email ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Client client = null;

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_SELECT_PAR_EMAIL, false, email );
            resultSet = preparedStatement.executeQuery();

            if ( resultSet.next() ) {
                client = mapClient( resultSet );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }

        return client;
    }

    @Override
    public List<Client> lister() throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        List<Client> clients = new ArrayList<Client>();
        Client client = new Client();

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_SELECT, false );
            resultSet = preparedStatement.executeQuery();

            while ( resultSet.next() ) {
                client = mapClient( resultSet );
                clients.add( client );
            }

        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }

        return clients;
    }

    @Override
    public void supprimer( Client client ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_DELETE, true,
                    client.getId() );

            int statut = preparedStatement.executeUpdate();

            if ( statut == 0 ) {
                throw new DAOException( "Echec de la suppression client!" );
            }

        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( preparedStatement, connexion );
        }

    }

}
