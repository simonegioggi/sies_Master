package siap.siep.calcolopena.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.camponota.controller.ICampoNota;
import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.libertaanticipata.controller.ILicenzaPeriodiLibAnticipata;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.fungibilita.controller.IFungibilita;
import siap.siep.fungibilita.model.FungibilitaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;
import siap.sius.documentoallegato.controller.IDocumentoAllegato;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActLoadDettaglioRidetPenaNew
 * </p>
 * <p>
 * Description: Classe action per la Load del Dettaglio dopo l'inserimento del Provvedimento o Comunicazione
 * per la Rideterminazione Pena - Decisioni del PM - Altro Nuova versione
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 *
 * @since 4.0
 */
public class ActLoadDettaglioRidetPenaNew extends ActSIESDettaglioProvvedimento
		implements ICostantiEvento, ICostantiAnnotazioneManuale {

	/**
	 * Recupera i dati dell'evento inserito e li passa alla jsp di visualizzazione del dettaglio. - Evento -
	 * Dati provvedimento altra autorità (se presente) - Annotazioni Manuali (nella nuova versione ne può
	 * esistere più di una) - Pena Residua (rideterminata associata all'evento) se presente - Posizione
	 * Giuridica
	 *
	 * @return jsp di visualizzazione del detteglio
	 */
	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		// DocumentoAllegatoModel mDocAll = null;
		IDocumentoAllegato mDocAllCtrl = null;

		// id dell'evento di computo
		BigDecimal lIdEvento = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// ============================================
		// Ricerca evento di computo inserito
		// ============================================
		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		EventoModel lEveComputo = lCtrlEvento.ExRicercaEventoByKey(lIdEvento);

		this.setRequestAttribute("aEventoComputo", lEveComputo);

		// recupero codiceMotivo="0959" da EventoComputo
		this.setRequestAttribute("codMotivoEventoComputo", lEveComputo.getCodMotivo());

		// Controller del DocumentoAllegato
		mDocAllCtrl = SIUSLookupRemote.getDocumentoAllegatoRemote();
		/* mDocAll = */mDocAllCtrl.ExRicercaDocumentoAllegatoByIdEventoCodTipo(lIdEvento, "06");

		// // Leggo l'evento
		// EventoModel lEveMod = new EventoModel();
		// IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		// lEveMod = lCtrl.ExRicercaEventoByKey(lIdEvento);

		// // 25/09/2007 Anomalia SIUS - Compilazione Foglio Complementare inibito per provvedimento
		// annullato.
		// if (lEveComputo.getFlagDocumentoRegistrato()!=null &&
		// lEveComputo.getFlagDocumentoRegistrato().compareTo("A")==0)
		// throw new SIUSException(F3BException.USER_MESSAGE,
		// "Non è possibile emettere il Foglio Complementare per un provvedimento annullato");

		// Valorizzazione del Documento Allegato
		DocumentoAllegatoModel aDocAllegato = new DocumentoAllegatoModel();
		aDocAllegato.setDataEmissione(DateUtils.getDate((DateUtils.getSysDate("dd/MM/yyyy")), "dd/MM/yyyy"));
		aDocAllegato.setCodTipoDocumento("06");
		aDocAllegato.setAnnoFoglioComplementare(new BigDecimal(DateUtils.getSysDate("yyyy")));
		aDocAllegato.setDataTrasmissione(null);
		aDocAllegato.setEveIdEvento(lIdEvento);
		aDocAllegato.setCodOperatoreInserimento(this.getCodUtenteConnesso());
		aDocAllegato.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		aDocAllegato.setDataInserimento(DateUtils.getSysDate());
		aDocAllegato.setComuneSedeGiudiziaria(null);

		// Imposta la Combo contenente le Motivazioni non Inviato FC.
		Option lOption = new Option(DecodificheManager.getInstance().getMotivoNonInvio());
		setRequestAttribute("motivoNonInvio", "" + lOption);
		setRequestAttribute("documentoAllegato", aDocAllegato);

		// ===============================================
		// Ricerco eventuale provvedimeto altra autorità
		// ===============================================
		if (lEveComputo.getEveIdEvento() != null) {
			EventoModel lEveAltraAut = lCtrlEvento.ExRicercaEventoByKey(lEveComputo.getEveIdEvento());
			this.setRequestAttribute("aEventoAltraAut", lEveAltraAut);
		}

		// ============================================
		// Recupero il campo nota
		// ============================================
		ICampoNota lCtrlCampoNota = SICOLookupRemote.getCampoNotaRemote();
		CampoNotaModel lCampoNotaModel = lCtrlCampoNota
				.ExRicercaCampoNotaByIdEvento(lEveComputo.getIdEvento());

		this.setRequestAttribute("aCampoNota", lCampoNotaModel);

		// ============================================
		// ricerca annotazioni manuali
		// ============================================
		IAnnotazioneManuale lCtrlAnn = SIEPLookupRemote.getAnnotazioneManualeRemote();
		Vector lVecAnnMod = new Vector();
		try {
			lVecAnnMod = lCtrlAnn.ExRicercaAnnotazioneManualeByIdEvento(lIdEvento);
		} catch (F3BException e) {
			if (e.getErrorCode() != F3BException.USER_MESSAGE)
				throw e;
		}

		this.setRequestAttribute("annotazioni", lVecAnnMod);

		// ===================================================================
		// Ricerca LA (nel caso di ridimensionamento LA o scomputo permesso)
		// ===================================================================
		ILicenzaPeriodiLibAnticipata lLicCtrl = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();
		Vector lVectLic = new Vector();
		try {
			lVectLic = lLicCtrl.ExRicercaLicenzeByEve(lIdEvento);
		} catch (F3BException e) {
			if (e.getErrorCode() != F3BException.USER_MESSAGE)
				throw e;
		}
		this.setRequestAttribute("licenze", lVectLic);

		// ============================================
		// ricerca posizione giuridica
		// ============================================
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		lPos = lPosCtrl
				.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lIdFascicolo);

		setRequestAttribute("posizioneluogoaltra", lPos);

		// ==============================================
		// Ricerca la pena residua collegata al computo
		// ==============================================
		IPenaResidua lCtrlPenaRes = SIEPLookupRemote.getPenaResiduaRemote();
		PenaResiduaModel lPenRes = lCtrlPenaRes.ExRicercaPenaResiduaByIdEvento(lIdEvento);

		setRequestAttribute("penaresidua", lPenRes);

		// ==============================================
		// Ricerca l'eventuale fungibilità
		// ==============================================
		IFungibilita lFungCtrl = SIEPLookupRemote.getFungibilitaRemote();
		FungibilitaModel lFunMod = lFungCtrl.ExRicercaFungibilitaByKeyEvento(lIdEvento);

		setRequestAttribute("fungibilita", lFunMod);

		// ============================================
		// Ricerca Magistrato
		// ============================================
		IMagistrato lCtrlM = SICOLookupRemote.getMagistratoRemote();
		MagistratoModel lMagi = lCtrlM.ExRicercaMagistratoByCod(lEveComputo.getCodMagistrato());

		setRequestAttribute("magistrato", lMagi);

		// ==============================================
		// Ricerca la pena residua ultima validata
		// ==============================================
		// IPenaResidua lCtrlPenaRes = SIEPLookupRemote.getPenaResiduaRemote();
		PenaResiduaModel lPenResUltimaVal = lCtrlPenaRes.ExRicercaPenaResiduaUltimaValidata(lIdFascicolo);

		setRequestAttribute("penaResUltimaValidata", lPenResUltimaVal);

		// ========================================================
		// Restituisce la pagina di visualizzazione del Dettaglio
		// ========================================================
		return PG_LOAD_DETTAGLIO_RIDETERMINAZIONE_PENA_ALTRO_NEW;
	}

}