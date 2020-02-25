package siap.siep.misurasicurezza.action;


/**
* <p>Title: ActModificaMisuraSicurezza</p>
* <p>Description: Classe Action per la modifica di MisuraSicurezza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActModificaMisuraSicurezza extends ActionSiap implements ICostantiMisuraSicurezza
{
/**
* Azione di Modifica del MisuraSicurezza
* @return Nome della pagina JSP da visualizzare
* al termine dell'elaborazione
* @throws F3BException
*/
  public String processRequest() throws F3BException
  {
     String lId = getRequestStringParameter(CAMPO_ID_MISURA_SICUREZZA);

     IMisuraSicurezza lCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
     MisuraSicurezzaModel lMisMod = new MisuraSicurezzaModel ();

     lMisMod = lCtrl.ExRicercaMisuraSicurezzaByKey(new BigDecimal(lId));
     if(lMisMod == null || lMisMod.getIdMisuraSicurezza() == null)
     {
    	 throw new F3BException(F3BException.USER_MESSAGE, "Misura di Sicurezza Non Trovata in archivio");
     }
     
     lMisMod.setCodNatura( getRequestStringParameter( CAMPO_COD_NATURA) );
     lMisMod.setCodTipo( getRequestStringParameter( CAMPO_COD_TIPO) );
     lMisMod.setNumAnni( getRequestBigDecimalParameter( CAMPO_NUM_ANNI) );
     lMisMod.setNumMesi( getRequestBigDecimalParameter( CAMPO_NUM_MESI) );
     lMisMod.setNumGiorni( getRequestBigDecimalParameter( CAMPO_NUM_GIORNI) );
     lMisMod.setDataFineValidita(getRequestDateParameter( CAMPO_ANNO_DATA_FINE_VALIDITA,CAMPO_MESE_DATA_FINE_VALIDITA,CAMPO_GIORNO_DATA_FINE_VALIDITA) );

     UtenteModel lUtenteMod = new UtenteModel((UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
     lMisMod.setCodOperatoreAggiornamento(lUtenteMod.getUserId());
     lMisMod.setCodUfficioAggiornamento(lUtenteMod.getUfficioUtente().getCodUfficio());
     lMisMod.setDataAggiornamento(DateUtils.getSysDate());
     
     MisuraSicurezzaModel llMisModRet = lCtrl.ExModificaMisuraSicurezza(lMisMod);
     setRequestAttribute("misurasicurezza", llMisModRet);
     String lPage = "";
     lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.misurasicurezza.action.ActLoadDettaglioMisuraSicurezza&"+CAMPO_ID_MISURA_SICUREZZA+"="+llMisModRet.getIdMisuraSicurezza().toString();
     return lPage;
  }
}