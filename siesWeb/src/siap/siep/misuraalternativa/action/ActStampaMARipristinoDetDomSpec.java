package siap.siep.misuraalternativa.action;

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
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActStampaMARipristinoDetDomSpec
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */

public class ActStampaMARipristinoDetDomSpec extends ActionSiap implements ICostantiMisuraAlternativa {

	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		UtenteModel lUtenteMod = this.getUtenteConnesso();
		UfficioModel lUff = this.getUfficioUtenteConnesso();

//		BigDecimal lIdEvento = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

//		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPosAltra = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
//		PosizioneGiuridicaModel lPosGiuModificata = new PosizioneGiuridicaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();

		/*lPosAltra = */lPosCtrl
				.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lFascicoloModel
						.getIdFascicoloSiep());
//		String lPosizioneGiu = lPosAltra.getPosizioneGiuridica().getCodPosizioneGiuridica();

		/*
		 * IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
		 * MisuraAlternativaModel lMisAlModConcessa = new MisuraAlternativaModel(); lMisAlModConcessa =
		 * lMisAltCtrl
		 * .ExRicercaMisuraAlternativaConcessaCorrenteByIdFascicolo(lFascicoloModel.getIdFascicoloSiep());
		 * String lUffScarc= null; if (lMisAlModConcessa != null) { if (
		 * lMisAlModConcessa.getCodTipoDecisione() == null ||
		 * !lMisAlModConcessa.getCodTipoDecisione().equals("03") || lMisAlModConcessa.getCodNaturaDecisione()
		 * == null || !lMisAlModConcessa.getCodNaturaDecisione().equals("RI") ||
		 * lMisAlModConcessa.getCodTipoMisura() == null ||
		 * !lMisAlModConcessa.getCodTipoMisura().equals("0194") ) {
		 * lUffScarc=lMisAlModConcessa.getCodTipoUfficioScarcerazione(); } }
		 */
		// if(verbale != null && !verbale.equals(""))
		// verbale=null;

		String lId = getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		/*EventoModel lEventoModel = */lCtrl.ExRicercaEventoByKey(new BigDecimal(lId));
//		String lMotivo = lEventoModel.getCodMotivo();

		EventoNotificaModel lEveMod = new EventoNotificaModel();

		lEveMod.getEvento().setIdEvento(new BigDecimal(lId));
		lEveMod.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

		lEveMod.getEvento().setDescrLuogoEmittente(lUff.getDescrComune());
		lEveMod.getEvento().setDescrUfficioEmittente(lUff.getDescrTipoUfficio());

		lEveMod.getEvento().setDataAggiornamento(DateUtils.getSysDate());
		lEveMod.getEvento().setCodUfficioAggiornamento(lUff.getCodUfficio());
		lEveMod.getEvento().setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
		lEveMod.getEvento().setFlagDocumentoRegistrato("N");

		// ricerca posizione precedente

//		PosizioneGiuridicaModel lPos = new PosizioneGiuridicaModel();
//		PosizioneGiuridicaModel lPosPrec = new PosizioneGiuridicaModel();
		/*lPosPrec = */lPosCtrl.ExRicercaPosizioneGiuridicaPrecedenteByIdFascicolo(lFascicoloModel
				.getIdFascicoloSiep());

		// libero

		/*
		 * if (lEventoModel.getCodMotivo().equals("0194")) { lEveMod.setNomeTemplate(TEMPLATE_SCARCERATO); }
		 * if (lUffScarc.equals("SORV")) { lEveMod.setNomeTemplate(TEMPLATE_SCARCERE); }
		 */
		lEveMod.setNomeTemplate(TEMPLATE_SCARCERATO);

		ByteArrayOutputStream lReport = lCtrl.ExStampaDocumento(lEveMod, lUtenteMod); // setta la risposta
																						// nella request

		// Prepara la pagina di destinazione
		// if (lReport != null)
		setRequestAttribute("report", lReport);
		// setRequestAttribute("fc", getRequestStringParameter("fc"));

		return IWebConstants.PG_DOWNLOAD;
	}

}