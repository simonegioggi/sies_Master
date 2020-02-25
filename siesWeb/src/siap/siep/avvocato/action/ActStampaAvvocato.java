package siap.siep.avvocato.action;

/**
* <p>Title: ActStampaAvvocato</p>
* <p>Description: Classe Action per l'inserimento di Avvocato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.template.controller.ITemplate;
import siap.sico.template.model.TemplateModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.avvocato.model.AvvocatoFascicoloSiepModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActStampaAvvocato extends ActionSiap
                               implements ICostantiAvvocato
{
  /**
  * Azione di Inserimento del Avvocato
  * @return Nome della pagina JSP da visualizzare
  * al termine dell'elaborazione
  * <p>
  * @throws F3BException
  */
  public String processRequest() throws F3BException
  {
	AvvocatoFascicoloSiepModel lAvvFasc= new AvvocatoFascicoloSiepModel();
	IAvvocato lCtrlAvv = SIEPLookupRemote.getAvvocatoRemote();
	BigDecimal idAvvocatoFascicoloSiep =this.getRequestBigDecimalParameter("IdAvvocatoFascicoloSiep");
	BigDecimal idAvvocato =this.getRequestBigDecimalParameter("IdAvvocato");
	String lCodMotivo = this.getRequestStringParameter("codMotivo");
	lAvvFasc.setIdAvvocatoFascicoloSiep(idAvvocatoFascicoloSiep);
	lAvvFasc.setAvvIdAvvocato(idAvvocato);
	
	// scelgo il tipo di template a seconda del motivo scelto
	String flagTemplate= null;
	int lMotivo = Integer.parseInt(lCodMotivo);
	switch (lMotivo)
	{
		case 2:{flagTemplate = "0";	break;}
		case 3:{flagTemplate = "1";	break;}
		case 4:{flagTemplate = "2";	break;}
		case 5:{flagTemplate = "3";	break;}
		case 6:{flagTemplate = "4";	break;}
		case 7:{flagTemplate = "5";	break;}
		case 8:{flagTemplate = "6";	break;}
	}
 
   BigDecimal lIdEvento = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
 //ricerca evento
   IEvento lCtrl = SICOLookupRemote.getEventoRemote();
   EventoModel lEventoModel = lCtrl.ExRicercaEventoByKey(lIdEvento);
   
   ITemplate lCtrlTem = SICOLookupRemote.getTemplateRemote();
   TemplateModel lTemMod = new TemplateModel();
   //lTemMod = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate("99","99", lCodMotivo,  flagTemplate);
   lTemMod = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate(lEventoModel.getCodTipoEvento(),lEventoModel.getCodTipoProvvedimento(),lEventoModel.getCodMotivo(),flagTemplate);

//evento
   FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
   UfficioModel lUff = this.getUfficioUtenteConnesso();
   lEventoModel.setIdEvento(lIdEvento);
   lEventoModel.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
   lEventoModel.setDescrLuogoEmittente(lUff.getDescrComune());
   lEventoModel.setDescrUfficioEmittente(lUff.getDescrTipoUfficio());
   lEventoModel.setDataAggiornamento(DateUtils.getSysDate());
   lEventoModel.setCodUfficioAggiornamento(lUff.getCodUfficio());
   lEventoModel.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
   lEventoModel.setFlagDocumentoRegistrato("N");
   
//produce la stampa    
   String nomeTemplate = lTemMod.getIdTemplate();
   UtenteModel lUtenteMod = this.getUtenteConnesso();
   ByteArrayOutputStream lReport = lCtrlAvv.ExStampaDocumentoAvvocato(lEventoModel,lAvvFasc, nomeTemplate, lUtenteMod); // setta la risposta nella request
   setRequestAttribute("report", lReport);

   return IWebConstants.PG_DOWNLOAD;
  }
}