package siap.sius.udienza.action;


/**
* <p>Title: ActModificaUdienza</p>
* <p>Description: Classe Action per la modifica di Udienza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.sius.udienza.controller.IUdienza;
import siap.sius.udienza.model.UdienzaModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActModificaUdienza extends ActionSiap implements ICostantiUdienza
{
  /**
   * Azione di Modifica del Udienza
   * @return Nome della pagina JSP da visualizzare
   * al termine dell'elaborazione
   * @throws F3BException propaga eroore di eccezione
   */
  public String processRequest() throws F3BException
  {
    String lId = getRequestStringParameter(CAMPO_ID_UDIENZA);
  
    // Imposta i dati inseriti nella form e popola il model.
    UdienzaModel lUdiMod = new UdienzaModel ();
  
    lUdiMod.setIdUdienza(new BigDecimal(lId));
    lUdiMod.setCodPresidente( getRequestStringParameter( CAMPO_COD_PRESIDENTE) );
    lUdiMod.setNumCollegio(getRequestBigDecimalParameter(CAMPO_NUM_COLLEGIO));
    lUdiMod.setCodGiudice1( getRequestStringParameter( CAMPO_COD_GIUDICE_1) );
    lUdiMod.setCodGiudice2( getRequestStringParameter( CAMPO_COD_GIUDICE_2) );
    lUdiMod.setCodPg( getRequestStringParameter( CAMPO_COD_PG ) );
    lUdiMod.setCodIdEsperto1( getRequestBigDecimalParameter( CAMPO_COD_ID_ESPERTO_1) );
    lUdiMod.setCodIdEsperto2( getRequestBigDecimalParameter( CAMPO_COD_ID_ESPERTO_2) );
    lUdiMod.setCodIdAssistente( getRequestBigDecimalParameter( CAMPO_COD_ID_ASSISTENTE) );

    lUdiMod.setOraInizio( getRequestStringParameter(CAMPO_ORA_INIZIO) ); 
    lUdiMod.setMinInizio( getRequestStringParameter(CAMPO_MIN_INIZIO) );
    lUdiMod.setOraFine( getRequestStringParameter(CAMPO_ORA_FINE) ); 
    lUdiMod.setMinFine( getRequestStringParameter(CAMPO_MIN_FINE) );
    // Impostazione orario di fine Camera di Consiglio
    lUdiMod.setOraFineCC( getRequestStringParameter(CAMPO_ORA_FINE_CC) ); 
    lUdiMod.setMinFineCC( getRequestStringParameter(CAMPO_MIN_FINE_CC) );

    lUdiMod.setNumeroMaxFascicoli( getRequestBigDecimalParameter( CAMPO_NUMERO_MAX_FASCICOLI) );
    //lUdiMod.setLuogoUdienza(StringUtils.convertSqlString(getRequestStringParameter( CAMPO_LUOGO_UDIENZA)));
    lUdiMod.setLuogoUdienza(getRequestStringParameter( CAMPO_LUOGO_UDIENZA));
    lUdiMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
    lUdiMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
    lUdiMod.setDataAggiornamento(DateUtils.getSysDate());

    // Chiama il controller
    IUdienza lCtrl = SIUSLookupRemote.getUdienzaRemote();
    UdienzaModel llUdiModRet = lCtrl.ExModificaUdienza(lUdiMod);

    setRequestAttribute("modalita", "M");
    setRequestAttribute("udienza", llUdiModRet);

    String lPage = "";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sius.udienza.action.ActLoadDettaglioUdienza&"+CAMPO_ID_UDIENZA+"="+llUdiModRet.getIdUdienza().toString();
    return lPage;
  }
}