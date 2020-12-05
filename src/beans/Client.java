package beans;

public class Client {

    private Long   id;
    private String prenomClient;
    private String nomClient;
    private String adresseClient;
    private String telephoneClient;
    private String emailClient;

    public Long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public String getPrenomClient() {
        return prenomClient;
    }

    public void setPrenomClient( String prenomClient ) {
        this.prenomClient = prenomClient;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient( String nomClient ) {
        this.nomClient = nomClient;
    }

    public String getAdresseClient() {
        return adresseClient;
    }

    public void setAdresseClient( String adresseClient ) {
        this.adresseClient = adresseClient;
    }

    public String getTelephoneClient() {
        return telephoneClient;
    }

    public void setTelephoneClient( String telephoneClient ) {
        this.telephoneClient = telephoneClient;
    }

    public String getEmailClient() {
        return emailClient;
    }

    public void setEmailClient( String emailClient ) {
        this.emailClient = emailClient;
    }

}
