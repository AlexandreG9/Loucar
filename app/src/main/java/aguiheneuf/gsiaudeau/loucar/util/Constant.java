package aguiheneuf.gsiaudeau.loucar.util;

/**
 * Created by aguiheneuf2015 on 26/06/2017.
 */

public class Constant {

    public final static String URL_BASE = "http://10.1.139.1:8080";
    public final static String URL_ALL_VOITURE = URL_BASE + "/voiture/findAll";
    public final static String URL_ALL_MARQUE = URL_BASE + "/marque/findAll";
    public final static String URL_ID_VOITURE = URL_BASE + "/voiture/getVoiture/id/%s";
    public final static String URL_ADD_VOITURE = URL_BASE + "/voiture/save";
    public final static String URL_ALL_AGENCE = URL_BASE + "/agence/findAll";
    public final static String URL_RENDRE_VOITURE = URL_BASE + "/voiture/rendre/id/%s"; // POST
    public final static String URL_ALL_VOITURE_BY_AGENCE = URL_BASE + "/voiture/findAll/idAgence/%s";
}
