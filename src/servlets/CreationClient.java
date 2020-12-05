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
import forms.CreationClientForm;

/**
 * Servlet implementation class CreationClient
 */
@WebServlet( "/CreationClient" )
public class CreationClient extends HttpServlet {
    private static final long  serialVersionUID  = 1L;

    public static final String VUE_FORM          = "/WEB-INF/creerClient.jsp";
    public static final String VUE_LISTE_CLIENTS = "/listeClients";

    public static final String ATT_CLIENT        = "client";
    public static final String ATT_FORM          = "form";
    public static final String ATT_LISTE_CLIENTS = "clients";

    public static final String CONF_DAO_FACTORY  = "daofactory";

    private ClientDao          clientDao;

    public void init() throws ServletException {
        this.clientDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getClientDao();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {
        this.getServletContext().getRequestDispatcher( VUE_FORM ).forward( request, response );
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {
        CreationClientForm form = new CreationClientForm( this.clientDao );
        Client client = form.creerClient( request );

        request.setAttribute( ATT_FORM, form );
        request.setAttribute( ATT_CLIENT, client );

        HttpSession session = request.getSession();

        Map<Long, Client> clients = (HashMap<Long, Client>) session.getAttribute( ATT_LISTE_CLIENTS );

        if ( clients == null ) {

            clients = new HashMap<Long, Client>();
            clients.put( client.getId(), client );
            session.setAttribute( ATT_LISTE_CLIENTS, clients );
        } else {

            clients.put( client.getId(), client );
            session.setAttribute( ATT_LISTE_CLIENTS, clients );
        }

        clients.put( client.getId(), client );
        session.setAttribute( ATT_LISTE_CLIENTS, clients );

        if ( form.getErreurs().isEmpty() ) {
            this.getServletContext().getRequestDispatcher( VUE_LISTE_CLIENTS ).forward( request, response );
        } else {
            this.getServletContext().getRequestDispatcher( VUE_FORM ).forward( request, response );
        }
    }

}
