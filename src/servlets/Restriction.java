package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Restriction
 */
@WebServlet("/Restriction")
public class Restriction extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public static final String ACCES_PUBLIC="/accesPublic.jsp";
	public static final String ACCES_RESTREINT="WEB-INF/accesRestreint.jsp";
	public static final String ATT_SESSION_USER="/sessionUtilisateur";
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Restriction() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		
		/**
		 * si objet session est null on fait une REDIRECTION si non on forward vers notre page JSP
		 */
		if( session.getAttribute(ATT_SESSION_USER) == null) {
			response.sendRedirect(request.getContextPath()+ ACCES_PUBLIC);
		} else {
			this.getServletContext().getRequestDispatcher(ACCES_RESTREINT).forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
