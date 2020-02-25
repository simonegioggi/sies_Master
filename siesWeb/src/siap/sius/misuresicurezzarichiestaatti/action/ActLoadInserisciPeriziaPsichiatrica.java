package siap.sius.misuresicurezzarichiestaatti.action;

import java.util.Date;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.richiestaatti.action.ICostantiRichiestaAtti;
import f3b.util.DateUtils;
import f3b.web.html.Option;

public class ActLoadInserisciPeriziaPsichiatrica extends ActionSiap
implements ICostantiRichiestaAtti,ICostantiMisureSicurezza
{
  public String processRequest() throws Exception
  {
    gestioneRitorno();

    // Data Fascicolo SIUS
    Date lDataInserimento = ((FascicoloGPModel)getSessionAttribute("fascicoloSiusGP")).getFascicoloSiusModel().getDataInserimento();
    String lDataInserimentoString = DateUtils.getDateToString(lDataInserimento, "dd/MM/yyyy");
    setRequestAttribute("dataInsFS",lDataInserimentoString);

 // Preleva elenco delle altre autorità giudiziarie.
    Option lOptionAltreAut = new Option( DecodificheManager.getInstance().getTipoUfficioCumuloRifSiep(), "-");
    String[] lStringFilter = {"-","CAP","CASAP","CAPMI","CAPMID","CSS","GIPMI","GIP","GIPM","GP","GUPMI","GUP","GUPM","PT","TRIBSD","CAPSM","TMI","DIB","DIBM"};
    lOptionAltreAut.setFilter(lStringFilter);
    
   
   /* Collection lTipoIstituto = DecodificheManager.getInstance().getTipoUfficioS();
    Option lOption = new Option( lTipoIstituto );
    String[] lStringFilter = {"CAP","CAPSM","CAS","DIB","DIBM","GIPM","GUP","TRIBSD","GUPM","GP","GIP","CASAP","CSS"};
    lOption.setFilter(lStringFilter);*/

    setRequestAttribute("TipiIstituti1", "" + lOptionAltreAut);
    
    
 

    return PG_LOAD_RICHIESTAPERIZIAPSICHIATRICA; //restituisce la jsp di VIEW
  }
}