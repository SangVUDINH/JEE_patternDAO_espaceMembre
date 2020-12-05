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

import beans.Client;
import dao.ClientDao;
import dao.DAOFactory;

/**
 * Servlet implementation class SuppressionClient
 */
@WebServlet( "/suppressionClient" )
public class SuppressionClient extends HttpServlet {
    private static final long  serialVersionUID  = 1L;

    public static final String VUE_LISTE_CLIENTS = "/listeClients";
    public static final String ATT_LISTE_CLIENTS = "clients";
    public static final String PARAM_ID          = "idClient";

    public static final String CONF_DAO_FACTORY  = "daofactory";

    private ClientDao          clientDao;

    public void init() throws ServletException {
        this.clientDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getClientDao();
    }

    protected void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        Map<Long, Client> clients = (HashMap<Long, Client>) session.getAttribute( ATT_LISTE_CLIENTS );

        String idClientStr = getValeurChamp( request, PARAM_ID );

        Long idClient = null;
        if ( idClientStr != null ) {

            idClient = Long.parseLong( idClientStr );

        }

        if ( idClient != null && clients != null ) {

            clientDao.supprimer( clients.get( idClient ) );

            clients.remove( idClient );

            session.setAttribute( ATT_LISTE_CLIENTS, clients );
        }

        response.sendRedirect( request.getContextPath() + VUE_LISTE_CLIENTS );

    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet( request, response );
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
