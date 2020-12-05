package servlets;

import static dao.DAOUtilitaire.listToMapCommande;

import java.io.IOException;
import java.util.List;
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
 * Servlet implementation class ListeCommandes
 */
@WebServlet( "/listeCommandes" )
public class ListeCommandes extends HttpServlet {
    private static final long  serialVersionUID  = 1L;
    public static final String VUE               = "/WEB-INF/listerCommandes.jsp";

    public static final String CONF_DAO_FACTORY  = "daofactory";
    public static final String ATT_LIST_COMMANDE = "commandes";
    private CommandeDao        commandeDao;

    public void init() throws ServletException {
        this.commandeDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) )
                .getCommandeDao();
    }

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ListeCommandes() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {

        List<Commande> listCommandes = commandeDao.lister();

        HttpSession session = request.getSession();

        Map<Long, Commande> mapCommandes = listToMapCommande( listCommandes );

        if ( !mapCommandes.isEmpty() ) {
            session.setAttribute( ATT_LIST_COMMANDE, mapCommandes );
        } else {
            System.out.print( "Lister () a échoué" );
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
