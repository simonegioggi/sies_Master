package siap.siep.ordineesecuzione.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>Title: ActStampaComunicazioneL78del2013</p>
 * <p>Description:  </p>
 * <p>Copyright: Copyright (c) 2013</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ActStampaComunicazioneL78del2013
    extends ActionSiap
    implements ICostantiOrdineEsecuzione
{
	  public String processRequest() throws F3BException
	  {
		    String lId = getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		    
		    String lIdPos = "";
		    if (!isRequestParameterNullObj("IdPosizioneGiuridica"))
		    	lIdPos = getRequestStringParameter("IdPosizioneGiuridica");
		
		    FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		    UtenteModel lUtenteMod = this.getUtenteConnesso();
		    UfficioModel lUff = this.getUfficioUtenteConnesso();
		
		    EventoNotificaModel lEveMod = new EventoNotificaModel();
		
		    lEveMod.getEvento().setIdEvento(new BigDecimal(lId));
		    lEveMod.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
		
		    lEveMod.getEvento().setDescrLuogoEmittente(lUff.getDescrComune());
		    lEveMod.getEvento().setDescrUfficioEmittente(lUff.getDescrTipoUfficio());
		
		    lEveMod.getEvento().setDataAggiornamento(DateUtils.getSysDate());
		    lEveMod.getEvento().setCodUfficioAggiornamento(lUff.getCodUfficio());
		    lEveMod.getEvento().setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
		    lEveMod.getEvento().setFlagDocumentoRegistrato("N");
		    
		    //MEV 10 S3 gestione per nuove posizioni giuridiche 70,71,72
		    if(lIdPos.equals("02") || lIdPos.equals("70") || lIdPos.equals("71") || lIdPos.equals("72")){
		    	lEveMod.setNomeTemplate(TEMPLATE_COMUNICAZIONE_LEGGE_78_2013_02);
		    } else {
		    	lEveMod.setNomeTemplate(TEMPLATE_COMUNICAZIONE_LEGGE_78_2013_01);
		    }
		    
		    IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		    ByteArrayOutputStream lReport = lCtrl.ExStampaDocumento(lEveMod, lUtenteMod); // setta la risposta nella request
		
		    setRequestAttribute("report", lReport);
		 //  setRequestAttribute("fc", getRequestStringParameter("fc"));
		
		    return IWebConstants.PG_DOWNLOAD;
	  }
}