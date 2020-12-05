package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import beans.Client;
import beans.Commande;
import beans.Utilisateur;

public class DAOUtilitaire {

    // REQUETE PREPARE
    public static PreparedStatement initialisationRequetePreparee( Connection connexion, String sql,
            boolean returnGeneratedKeys, Object... objets ) throws SQLException {

        PreparedStatement preparedStatement = connexion.prepareStatement( sql,
                returnGeneratedKeys ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS );

        for ( int i = 0; i < objets.length; i++ ) {
            preparedStatement.setObject( i + 1, objets[i] );
        }

        return preparedStatement;

    }

    // MAPPING result SET
    public static Utilisateur mapUtilisateur( ResultSet resultSet ) throws SQLException {

        Utilisateur utilisateur = new Utilisateur();

        utilisateur.setId( resultSet.getLong( "id" ) );
        utilisateur.setEmail( resultSet.getString( "email" ) );
        utilisateur.setMotDePasse( resultSet.getString( "mot_de_passe" ) );
        utilisateur.setNom( resultSet.getString( "nom" ) );
        utilisateur.setDateInscription( resultSet.getTimestamp( "date_inscription" ) );

        return utilisateur;
    }

    // MAPPING result SET
    public static Client mapClient( ResultSet resultSet ) throws SQLException {

        Client client = new Client();

        client.setId( resultSet.getLong( "id" ) );
        client.setEmailClient( resultSet.getString( "email" ) );
        client.setAdresseClient( resultSet.getString( "adresse" ) );
        client.setNomClient( resultSet.getString( "nom" ) );
        client.setTelephoneClient( resultSet.getString( "telephone" ) );

        return client;
    }

    // DIFFERENT FERMETURE + POLYMORPHISME
    public static void fermetureSilencieuse( ResultSet resultSet ) {
        if ( resultSet != null ) {
            try {
                resultSet.close();
            } catch ( SQLException e ) {
                System.out.println( "Echec de la fermeture du ResultSet : " + e.getMessage() );
            }
        }
    }

    public static Map<Long, Client> listToMapClient( List<Client> listClient ) {
        Map<Long, Client> mapClients = listClient.stream()
                .collect( Collectors.toMap( Client::getId, Client -> Client ) );
        return mapClients;
    }

    public static Map<Long, Commande> listToMapCommande( List<Commande> commandes ) {
        Map<Long, Commande> mapCommande = commandes.stream()
                .collect( Collectors.toMap( Commande::getId, Commande -> Commande ) );
        return mapCommande;
    }

    public static void fermetureSilencieuse( Statement statement ) {
        if ( statement != null ) {
            try {
                statement.close();
            } catch ( SQLException e ) {
                System.out.println( "Echec de la fermeture du statement : " + e.getMessage() );
            }
        }
    }

    public static void fermetureSilencieuse( Connection connexion ) {
        if ( connexion != null ) {
            try {
                connexion.close();
            } catch ( SQLException e ) {
                System.out.println( "Echec de la fermeture du connexion : " + e.getMessage() );
            }
        }
    }

    public static void fermeturesSilencieuses( Statement statement, Connection connexion ) {
        fermetureSilencieuse( statement );
        fermetureSilencieuse( connexion );
    }

    public static void fermeturesSilencieuses( ResultSet resultSet, Statement statement, Connection connexion ) {
        fermetureSilencieuse( resultSet );
        fermetureSilencieuse( statement );
        fermetureSilencieuse( connexion );

    }
}
