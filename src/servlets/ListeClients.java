package servlets;

import static dao.DAOUtilitaire.listToMapClient;

import java.io.IOException;
import java.util.List;
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
 * Servlet implementation class ListeClients
 */
@WebServlet( "/listeClients" )
public class ListeClients extends HttpServlet {
    private static final long   serialVersionUID  = 1L;
    public static final String  VUE               = "/WEB-INF/listerClients.jsp";

    public static final String  CONF_DAO_FACTORY  = "daofactory";
    private static final String ATT_LISTE_CLIENTS = "clients";
    private ClientDao           clientDao;

    public void init() throws ServletException {
        this.clientDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) )
                .getClientDao();
    }

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ListeClients() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {

        List<Client> listClients = clientDao.lister();

        HttpSession session = request.getSession();
        Map<Long, Client> clients = listToMapClient( listClients );

        if ( !listClients.isEmpty() ) {
            session.setAttribute( ATT_LISTE_CLIENTS, clients );
        } else {
            System.out.println( "lister() a echoué" );
        }

        this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
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

}
