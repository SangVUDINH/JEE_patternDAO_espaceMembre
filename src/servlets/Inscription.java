package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.Utilisateur;
import dao.DAOFactory;
import dao.UtilisateurDao;
import forms.InscriptionForm;

@WebServlet( "/inscription" )
public class Inscription extends HttpServlet {
    public static final String VUE              = "/WEB-INF/signUp.jsp";
    public static final String CONF_DAO_FACTORY = "daofactory";

    public static final String ATT_USER         = "utilisateur";
    public static final String ATT_FORM         = "form";

    private UtilisateurDao     utilisateurDao;

    // recuperation du DAO

    public void init() throws ServletException {
        this.utilisateurDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getUtilisateurDao();
    }

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        /* Affichage de la page d'inscription */
        this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
    }

    public void doPost( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {

        InscriptionForm form = new InscriptionForm( this.utilisateurDao );
        Utilisateur utilisateur = form.inscrireUtilisateur( request );

        request.setAttribute( ATT_FORM, form );
        request.setAttribute( ATT_USER, utilisateur );

        System.out.println( form.getErreurs() );

        /* Transmission de la paire d'objets request/response à notre JSP */
        this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
    }

}