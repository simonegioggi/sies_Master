package siap.sige.richiestaatti.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.template.action.ICostantiTemplate;
import siap.sico.ufficio.model.UfficioModel;
import siap.sige.SIGEException;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.provvedimento.action.ICostantiProvvedimentoSige;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.richiestaatti.controller.IRichiestaAttiSige;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActStampaRichiestaAtti extends ActionSige implements ICostantiProvvedimentoSige
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	/**
  * Azione di Stampa dell'Atto
  * @return Nome della pagina JSP da visualizzare
  * al termine dell'elaborazione
  * @throws Exception
  */
  public String processRequest() throws Exception
  {

    // Ufficio connesso.
    UfficioModel lUff = new UfficioModel(this.getUfficioUtenteConnesso());

    // Fascicolo Sige Esteso in sessione.
  	FascicoloSigeEstesoModel lFasEsteso = (FascicoloSigeEstesoModel)getSessionAttribute("FascicoloSigeEsteso");

    // Stampa Atti Istruttori pilotata dall'IdEvento.
  	if (isRequestParameterNullObj(ICostantiEvento.CAMPO_ID_EVENTO))
				throw new F3BException(F3BException.USER_MESSAGE, "Attenzione: il parametro ID_EVENTO non è disponibile!");
  			
  	String lId = getRequestStringParameter( ICostantiEvento.CAMPO_ID_EVENTO);
    EventoModel lEveMod = new EventoModel();
    lEveMod.setIdEvento(new BigDecimal(lId));
   
    // Si chiama il controller x leggere il ProvvedimentoSigeEventoModel.
    IProvvedimentoSige lCtrl = SIGELookupRemote.getProvvedimentoRemote();
    ProvvedimentoSigeEventoModel lProEveMod = lCtrl.ExRicercaProvvedimentoByIdEvento(lEveMod.getIdEvento());
    
    lEveMod.setDescrLuogoEmittente(lUff.getDescrComune());
    lEveMod.setDescrUfficioEmittente(lUff.getDescrTipoUfficio());

    lEveMod.setDataAggiornamento(DateUtils.getSysDate());
    lEveMod.setCodUfficioAggiornamento(lUff.getCodUfficio());
    lEveMod.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
    lEveMod.setFlagDocumentoRegistrato("N");

    if(!isRequestParameterNullObj(ICostantiTemplate.CAMPO_ID_TEMPLATE))
    	lEveMod.setTemIdTemplate(this.getRequestStringParameter(ICostantiTemplate.CAMPO_ID_TEMPLATE));
    else
    	throw new SIGEException(SIGEException.USER_MESSAGE, "Non esiste il Template!");
    	//lEveMod.setTemIdTemplate("SIGE_IS_001") ;
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(">>> ID TEMPLATE da codmotivo : " + getRequestStringParameter(ICostantiTemplate.CAMPO_ID_TEMPLATE));
        
    IRichiestaAttiSige lCtrlRicAtti = SIGELookupRemote.getRichiestaAttiSigeRemote();

    ByteArrayOutputStream lReport = lCtrlRicAtti.ExStampaRichiestaAtti(lEveMod, lFasEsteso.getFascicoloSige().getIdFascicoloSige(), getCodUfficioUtenteConnesso(), super.getUtenteConnesso() );

   //Si Prepara la pagina di destinazione
   if (lReport != null)
       setRequestAttribute("report", lReport);
   else
       throw new SIGEException(SIGEException.USER_MESSAGE, "Nessun documento è stato generato!");

    setRequestAttribute("ProvvEvento", lProEveMod);

    return IWebConstants.PG_DOWNLOAD;
  }

}