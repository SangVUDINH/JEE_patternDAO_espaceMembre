package dao;

import static dao.DAOUtilitaire.fermeturesSilencieuses;
import static dao.DAOUtilitaire.initialisationRequetePreparee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import beans.Commande;

public class CommandeDaoImpl implements CommandeDao {

    private static final String SQL_SELECT        = "SELECT id, id_client, date, montant, mode_paiement, statut_paiement, mode_livraison, statut_livraison "
            + "FROM Commande ORDER BY id";
    private static final String SQL_SELECT_PAR_ID = "SELECT id, id_client, date, montant, mode_paiement, statut_paiement, mode_livraison, statut_livraison"
            + " FROM Commande WHERE id = ?";
    private static final String SQL_INSERT        = "INSERT INTO Commande (id_client, date, montant, mode_paiement, statut_paiement, mode_livraison, statut_livraison) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_DELETE_PAR_ID = "DELETE FROM Commande WHERE id = ?";

    private DAOFactory          daoFactory;

    CommandeDaoImpl( DAOFactory daoFactory ) {
        this.daoFactory = daoFactory;
    }

    @Override
    public void creer( Commande commande ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet valeursAutoGenerees = null;

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_INSERT, true, commande.getId_client(),
                    new Timestamp( commande.getDateCommande().getMillis() ), commande.getMontantCommande(),
                    commande.getModePaiementCommande(),
                    commande.getStatutPaiementCommande(),
                    commande.getModeLivraisonCommande(), commande.getStatutLivraisonCommande() );

            int statut = preparedStatement.executeUpdate();

            if ( statut == 0 ) {
                throw new DAOException( "Echec de la creation commande!" );
            }

            valeursAutoGenerees = preparedStatement.getGeneratedKeys();

            if ( valeursAutoGenerees.next() ) {
                commande.setId( valeursAutoGenerees.getLong( 1 ) );
            } else {
                throw new DAOException( "Echec de la creation commande, aucun ID generé !" );
            }

        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( valeursAutoGenerees, preparedStatement, connexion );
        }

    }

    @Override
    public Commande trouver( long id ) throws DAOException {
        return null;
    }

    @Override
    public List<Commande> lister() throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        List<Commande> commandes = new ArrayList<Commande>();

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_SELECT, false );
            resultSet = preparedStatement.executeQuery();

            while ( resultSet.next() ) {
                commandes.add( mapCommande( resultSet ) );
            }

        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }

        return commandes;
    }

    private Commande mapCommande( ResultSet resultSet ) throws SQLException {
        Commande commande = new Commande();

        commande.setId( resultSet.getLong( "id" ) );
        commande.setId_client( resultSet.getLong( "id_client" ) );
        commande.setDateCommande( new DateTime( resultSet.getDate( "date" ) ) );
        commande.setMontantCommande( resultSet.getDouble( "montant" ) );
        commande.setModePaiementCommande( resultSet.getString( "mode_paiement" ) );
        commande.setModeLivraisonCommande( resultSet.getString( "mode_livraison" ) );
        commande.setStatutPaiementCommande( resultSet.getString( "statut_paiement" ) );
        commande.setStatutLivraisonCommande( resultSet.getString( "statut_livraison" ) );

        return commande;
    }

    @Override
    public void supprimer( Commande commande ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_DELETE_PAR_ID, true,
                    commande.getId() );

            int statut = preparedStatement.executeUpdate();

            if ( statut == 0 ) {
                throw new DAOException( "Echec de la suppression commande!" );
            }

        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( preparedStatement, connexion );
        }

    }

}
