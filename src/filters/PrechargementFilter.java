package filters;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.Client;
import beans.Commande;
import dao.ClientDao;
import dao.CommandeDao;
import dao.DAOFactory;

public class PrechargementFilter implements Filter {

    public static final String CONF_DAO_FACTORY     = "daofactory";
    public static final String ATT_SESSION_CLIENT   = "clients";
    public static final String ATT_SESSION_COMMANDE = "commandes";

    private ClientDao          clientDao;
    private CommandeDao        commandeDao;

    public void init( FilterConfig config ) throws ServletException {
        this.clientDao = ( (DAOFactory) config.getServletContext().getAttribute( CONF_DAO_FACTORY ) )
                .getClientDao();
        this.commandeDao = ( (DAOFactory) config.getServletContext().getAttribute( CONF_DAO_FACTORY ) )
                .getCommandeDao();
    }

    public void doFilter( ServletRequest req, ServletResponse res, FilterChain chain )
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        HttpSession session = request.getSession();

        if ( session.getAttribute( ATT_SESSION_CLIENT ) == null ) {
            List<Client> listeClients = clientDao.lister();
            Map<Long, Client> mapClients = new HashMap<Long, Client>();

            for ( Client client : listeClients ) {
                mapClients.put( client.getId(), client );
            }
            session.setAttribute( ATT_SESSION_CLIENT, mapClients );
        }

        if ( session.getAttribute( ATT_SESSION_COMMANDE ) == null ) {
            List<Commande> listeCommandes = commandeDao.lister();
            Map<Long, Commande> mapCommande = new HashMap<Long, Commande>();

            for ( Commande commande : listeCommandes ) {
                mapCommande.put( commande.getId(), commande );
            }
            session.setAttribute( ATT_SESSION_COMMANDE, mapCommande );
        }

        chain.doFilter( request, res );
    }

    public void destroy() {

    }

}
