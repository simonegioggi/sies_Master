package siap.siep.sentenza.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import f3b.web.html.Option;

public class ActLoadInserisciSentenza extends ActionSiap implements ICostantiSentenza
{
  public String processRequest() throws Exception
  {
    // Parse della request.
    // Riempie il model.
    // Chiama il controller.
    Option lOptionProvv = new Option( DecodificheManager.getInstance().getTipoProvvedimenti(), "-");
    lOptionProvv.setFilter(new String[]{"-", "01", "53"});    
    setRequestAttribute("tipoProvvedimenti", "" + lOptionProvv );
    setRequestAttribute("tipoProvvedimentiAltro", "" + lOptionProvv );
    
    Option lOption = new Option( DecodificheManager.getInstance().getTipoDecisioneCassazione(), "-");
    // Imposta la decisione cassazione
    setRequestAttribute("tipoDecisioneCassazione", "" + lOption );




    //UtenteModel lUteMod = new UtenteModel((UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));

    // Imposta i provvedimenti Rif.

   /* if (lUteMod.getUfficioUtente().getDescrTipoUfficio().startsWith("PROCURA GENERALE"))
      {
        lOption = new Option( DecodificheManager.getInstance().getTipoProvvedimentiRifPG(), "-");
      }
      else
      {
        lOption = new Option( DecodificheManager.getInstance().getTipoProvvedimentiRifP(), "-");
      }
		*/
  	lOption = new Option( DecodificheManager.getInstance().getTipoProvvedimentiRif(), "-");

    setRequestAttribute("tipoProvvedimentiRif", "" + lOption );

    lOption = new Option( DecodificheManager.getInstance().getTipoAutoritaEmittente(), "-");
    // Imposta i provvedimenti Rif.
    //setRequestAttribute("autoritaEmi", "" + lOption );
    setRequestAttribute("autoritaProvRif", "" + lOption );
    lOption.setFilter( new String[] {"-", "CAP", "CAPMI", "CAPMID", "CAPSM", "CAS", "CASAP", "CSS", "DIB", "DIBM", "GIP", "GIPM", "GIPMI", "GIPP", "GIPPSD", "GP", "GUP", "GUPM", "GUPMI", "PT", "PTC", "PTCSD", "TMI", "TRIBSD"} );
    setRequestAttribute("autoritaEmi", "" + lOption );
    
    lOption = new Option( DecodificheManager.getInstance().getFlagSN(), "N");
    setRequestAttribute("flagSN", "" + lOption );
    
    lOption = new Option( DecodificheManager.getInstance().getTipoRitoSentenza(), "-");
    setRequestAttribute("tipoRito1", "" + lOption );
    setRequestAttribute("tipoRito2", "" + lOption );

    // Imposta Modalità.
    setRequestAttribute("modalita", "I");

    if(!this.isRequestParameterNullObj("lTipoFunzione")) // paramentro passato solo nel caso di iscrizione guidata
    {
      this.setRequestAttribute("lTipoFunzione", this.getRequestStringParameter("lTipoFunzione"));
    }
    
    setRequestAttribute("pageInclude", "IncludeSentenza.jsp");
    
    return PG_LOAD_INSERIMENTO_TITOLO;
  }
}
