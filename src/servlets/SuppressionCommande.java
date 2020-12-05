package servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.Commande;
import dao.CommandeDao;
import dao.DAOFactory;

/**
 * Servlet implementation class SuppressionCommande
 */
@WebServlet( "/suppressionCommande" )
public class SuppressionCommande extends HttpServlet {
    private static final long  serialVersionUID    = 1L;
    public static final String VUE_LISTE_COMMANDES = "/listeCommandes";
    public static final String ATT_LISTE_COMMANDES = "commandes";
    public static final String PARAM_KEY           = "id_commande";

    public static final String CONF_DAO_FACTORY    = "daofactory";

    private CommandeDao        commandeDao;

    public void init() throws ServletException {
        this.commandeDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getCommandeDao();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Map<Long, Commande> commandes = (HashMap<Long, Commande>) session.getAttribute( ATT_LISTE_COMMANDES );

        Long id_Commande = Long.parseLong( getValeurChamp( request, PARAM_KEY ) );

        if ( id_Commande != null && commandes != null ) {

            commandeDao.supprimer( commandes.get( id_Commande ) );
            commandes.remove( id_Commande );
            session.setAttribute( ATT_LISTE_COMMANDES, commandes );
        }

        response.sendRedirect( request.getContextPath() + VUE_LISTE_COMMANDES );

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
