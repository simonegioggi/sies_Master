package siap.siep.misuraalternativa.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.template.controller.ITemplate;
import siap.sico.template.model.TemplateModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActStampaMAArrestiDomiciliari
 * </p>
 * <p>
 * Description: Produce il documento
 * </p>
 * <p>
 * Company: Engineering S.p.A.
 * </p>
 * 
 * @version 1.0
 */
public class ActStampaMAArrestiDomiciliari extends ActionSiap implements ICostantiMisuraAlternativa {

	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		UtenteModel lUtenteMod = this.getUtenteConnesso();
		UfficioModel lUff = this.getUfficioUtenteConnesso();

		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		PosizioneGiuridicaModel lPosMod = lPosCtrl
				.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lFascicoloModel.getIdFascicoloSiep());

		String lIdEvento = getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		EventoModel lEventoModel = lCtrl.ExRicercaEventoByKey(new BigDecimal(lIdEvento));
		String lTipoProvv = "";

		EventoNotificaModel lEveMod = new EventoNotificaModel();

		lEveMod.getEvento().setIdEvento(new BigDecimal(lIdEvento));
		lEveMod.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

		lEveMod.getEvento().setDescrLuogoEmittente(lUff.getDescrComune());
		lEveMod.getEvento().setDescrUfficioEmittente(lUff.getDescrTipoUfficio());

		lEveMod.getEvento().setDataAggiornamento(DateUtils.getSysDate());
		lEveMod.getEvento().setCodUfficioAggiornamento(lUff.getCodUfficio());
		lEveMod.getEvento().setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
		lEveMod.getEvento().setFlagDocumentoRegistrato("N");

		// Ricerca Misura Alternativa
		IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
		MisuraAlternativaModel lMisAlModConcessa = new MisuraAlternativaModel();
		lMisAlModConcessa = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lEventoModel.getEveIdEvento());
		String lflagScarcerato = lMisAlModConcessa.getCodTipoUfficioScarcerazione();

		ITemplate lCtrlTem = SICOLookupRemote.getTemplateRemote();
		TemplateModel lTemMod = new TemplateModel();
		String flagTemplate = null;

		// Il Tipo Provvedimento viene settato uguale a "02" in Sospensione,
		// "03" in Revoca e nel caso di Ripristino viene recuperato nel campo
		// COD_TIPO_DECISIONE della tabella MISURA_ALTERNATIVA
		if (lMisAlModConcessa.getCodTipoMisura() != null
				&& (lMisAlModConcessa.getCodTipoMisura().equals("2744")
						|| lMisAlModConcessa.getCodTipoMisura().equals("2757")
						|| lMisAlModConcessa.getCodTipoMisura().equals("2746") || lMisAlModConcessa
						.getCodTipoMisura().equals("2747"))) {
			lTipoProvv = "03";
		} else if (lMisAlModConcessa.getCodTipoMisura() != null
				&& (lMisAlModConcessa.getCodTipoMisura().equals("2756")
						|| lMisAlModConcessa.getCodTipoMisura().equals("2741")
						|| lMisAlModConcessa.getCodTipoMisura().equals("2742") || lMisAlModConcessa
						.getCodTipoMisura().equals("2743")
				// Inizio MAC 2016/10/21
				// Gestione codice MOTIVO_PROVVEDIMENTO = 2291
				// Il codice 2291 sostituisce i codici (2741,2742,2743,2756)
				// eliminati dalla base dati

				 // MAC 2017/04/01 Ripristinati i codici (2741,2742,2743,2756)
				 //PEC m_dg.DOG07.01-10-2018.0030206.U (deve essere censito anche i codice 2291)		
				 || lMisAlModConcessa.getCodTipoMisura().equals("2291")
				// Fine MAC 2016/10/21
				)) {
			lTipoProvv = "02";
		} else {
			lTipoProvv = lMisAlModConcessa.getCodTipoDecisione();
		}

		// Nel caso di Revoca Arresti il flagTemplate assume valore 0 oppure 1
		// in base al codice della Posizione Giuridica
		if (lMisAlModConcessa.getCodTipoMisura() != null
				&& (lMisAlModConcessa.getCodTipoMisura().equals("2744")
						|| lMisAlModConcessa.getCodTipoMisura().equals("2757")
						|| lMisAlModConcessa.getCodTipoMisura().equals("2746") || lMisAlModConcessa
						.getCodTipoMisura().equals("2747"))) {
			if (lPosMod != null && lPosMod.getCodPosizioneGiuridica() != null) {
				if (lPosMod.getCodPosizioneGiuridica().equals("62")
						|| lPosMod.getCodPosizioneGiuridica().equals("63")
						|| lPosMod.getCodPosizioneGiuridica().equals("64")
						|| lPosMod.getCodPosizioneGiuridica().equals("65")) {
					flagTemplate = "1";
				} else {
					flagTemplate = "0";
				}
			}
		} else {
			// Nel caso di Sospensione e Ripristino verifico il campo COD_TIPO_UFFICIO_SCARCERAZIONE
			// presente sulla tabella MISURA_ALTERNATIVA, che assume il valore PROC (Procura)
			// oppure SORV (Magistrato di Sorveglianza)
			if (lflagScarcerato != null && lflagScarcerato.equals("SORV")) {
				flagTemplate = "1";
			} else if (lflagScarcerato != null && lflagScarcerato.equals("PROC")) {
				flagTemplate = "0";
			}
		}

		lTemMod = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate("01", lTipoProvv,
				lMisAlModConcessa.getCodTipoMisura(), flagTemplate);
		lEveMod.setNomeTemplate(lTemMod.getIdTemplate());

		ByteArrayOutputStream lReport = lCtrl.ExStampaDocumento(lEveMod, lUtenteMod); // setta la risposta
																						// nella request

		// Prepara la pagina di destinazione
		setRequestAttribute("report", lReport);

		return IWebConstants.PG_DOWNLOAD;
	}

}