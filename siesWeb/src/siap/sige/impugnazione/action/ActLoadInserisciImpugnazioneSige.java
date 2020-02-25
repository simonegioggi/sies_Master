package siap.sige.impugnazione.action;

import java.math.BigDecimal;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sige.provvedimento.action.ICostantiProvvedimentoSige;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;
import f3b.web.html.Option;

//import siap.sico.web.ActionSiap;

/**
* <p>Title: ActLoadInserisciImpugnazioneSige</p>
* <p>Description: Classe Action per la load di inserisci Impugnazione Sige</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class ActLoadInserisciImpugnazioneSige extends ActionSige implements ICostantiImpugnazioneSige {
  public String processRequest() throws Exception {
      this.setLinkRitorno();    
      // Recupero del Provvedimento Sige
      IProvvedimentoSige lCtrlPS = SIGELookupRemote.getProvvedimentoRemote();
      BigDecimal lIdProv = getRequestBigDecimalParameter( ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE );
      ProvvedimentoSigeEventoModel lPSMod  = lCtrlPS.ExRicercaProvvedimentoById(lIdProv);
      setRequestAttribute("provvedimento", lPSMod);
  
      // Imposta ComboBOX Soggetto Impugnante.
      Option lOption = new Option( DecodificheManager.getInstance().getSoggettoImpugnanteSige());
      //String[] lFilterSoImp = {"01","02","03","05","06"};
      //lOption.setFilter( lFilterSoImp );
      setRequestAttribute("soggettoImpugnante", "" + lOption );
  
      // Imposta ComboBOX Autorita Destinataria
      lOption = new Option( DecodificheManager.getInstance().getTipoUfficio());
      String[] lFilterCSS = {"CSS"};
      lOption.setFilter( lFilterCSS );
 
      setRequestAttribute("ListaUffici", ""+ lOption);
      return PG_LOAD_INSERISCIIMPUGNAZIONESIGE;  //restituisce la jsp di VIEW
  }

}
