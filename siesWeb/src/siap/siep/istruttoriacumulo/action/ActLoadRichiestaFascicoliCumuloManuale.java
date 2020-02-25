package siap.siep.istruttoriacumulo.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.controller.IEventoSimeone;
import siap.sico.evento.model.EventoModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.SIEPException;
import siap.siep.cumulo.action.ICostantiCumulo;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoriacumulo.controller.IIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.luogodetenzione.controller.ILuogoDetenzione;
import siap.siep.modulocumulo.action.ActionModuloCumulo;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 *
 * @deprecated mai utilizzata: probabilmente refuso del vecchio prototipo
 */

public class ActLoadRichiestaFascicoliCumuloManuale extends ActionModuloCumulo
		implements ICostantiCumulo, ICostantiIstruttoriaCumulo {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		BigDecimal lFascID = null;
		String lStatoFasc = null;
		String lFlagVal = null;

		// ---------------------------------------------------------
		if (isRequestParameterNullObj(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO)
				|| getRequestBigDecimalParameter(
						ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO) == null)

		{
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Istruttoria non selezionata");

			RedirectTo lRedirigi = new RedirectTo();

			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Nessuna istruttoria selezionata");
			lRedirigi.setAction("siap.siep.istruttoriacumulo.action.ActLoadGrigliaCumulo");
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
			throw new F3BException(F3BException.USER_MESSAGE, "Nessuna istruttoria selezionata");
		}

		BigDecimal lIdIstruttoriaCorrente = this
				.getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);

		IstruttoriaCumuloModel lIstruttoriaCumModel = null;
		IIstruttoriaCumulo lIstrCtrl = SIEPLookupRemote.getIstruttoriaCumuloRemote();
		lIstruttoriaCumModel = lIstrCtrl.ExRicercaIstruttoriaCumuloById(lIdIstruttoriaCorrente);
		setRequestAttribute("IstruttoriaCumulo", lIstruttoriaCumModel);

		// --------------------------------------------------------------

		if (!this.isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP)) {
			FascicoloSiepModel lFascMod = new FascicoloSiepModel();
			lFascID = new BigDecimal(
					getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP));

			IFascicoloSiep lCtrlFasc = SIEPLookupRemote.getFascicoloSiepRemote();
			lFascMod = lCtrlFasc.ExRicercaFascicoloByKey(lFascID);

			lStatoFasc = lFascMod.getCodStatoFascicolo();
			lFlagVal = lFascMod.getFlagValidato();

			this.setSessionAttribute("fascicolo", lFascMod);
		} else {
			if (this.isSessionAttributeNullObj("fascicolo"))
				return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_CUMULANTE + getClass().getName();

			lFascID = ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep();
			lStatoFasc = ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getCodStatoFascicolo();
			lFlagVal = ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getFlagValidato();

		}

		if (lStatoFasc.equals("01"))
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Fascicolo Archiviato/Definito. Impossibile effettuare una operazione di cumulo");

		if (lFlagVal.equals("N"))
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Fascicolo non Validato. Impossibile effettuare una operazione di cumulo");

		PosizioneGiuridicaModel lPG = new PosizioneGiuridicaModel();
		IPosizioneGiuridica iPG = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPG = iPG.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lFascID);

		if (lPG == null)
			throw new SIEPException(SIEPException.USER_MESSAGE, "Posizione Giuridica Inesistente");

		boolean libero = lPG.getCodPosizioneGiuridica().equals("07")
				|| lPG.getCodPosizioneGiuridica().equals("10") || lPG.getCodPosizioneGiuridica().equals("16")
				|| lPG.getCodPosizioneGiuridica().equals("17");

		// ==========================================================================
		//
		// ==========================================================================
		if ((libero
				&& ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getFlagAltraCausa().equals("S"))
				|| !libero) {
			// LuogoDetenzioneModel lDtMod = new LuogoDetenzioneModel();
			ILuogoDetenzione lIld = SIEPLookupRemote.getLuogoDetenzioneRemote();
			/* lDtMod = */lIld.ExRicercaLuogoDetenzioneCorrenteByFascicoloSiep(lFascID);
		}

		// ==========================================================================
		//
		// ==========================================================================
		IPenaResidua lPen = SIEPLookupRemote.getPenaResiduaRemote();
		PenaResiduaModel lPenRes = null;
		lPenRes = lPen.ExRicercaPenaResiduaUltimaValidata(lFascID);
		FascicoloSiepModel lFasCumulato = new FascicoloSiepModel();

		if (!isRequestParameterNullObj("AnnoCumulato")) {
			// ========================================================================
			// Tasto Carica - Acquisizione da stesso ufficio o distretto
			// ========================================================================
			BigDecimal AnnoCumulato = getRequestBigDecimalParameter("AnnoCumulato");
			BigDecimal NumeroCumulato = getRequestBigDecimalParameter("NumeroCumulato");

			IUfficio lUff = SICOLookupRemote.getUfficioRemote();
			UfficioModel lUffMod = new UfficioModel();
			lUffMod = lUff.getUfficioByCodTipoUffDescrComune(getRequestStringParameter("AutoritaInt"),
					getRequestStringParameter("LuogoFas"));

			IFascicoloSiep lFasc = SIEPLookupRemote.getFascicoloSiepRemote();
			lFasCumulato.setChiaveAnno(AnnoCumulato);
			lFasCumulato.setChiaveProgr(NumeroCumulato);
			lFasCumulato.setChiaveUfficio(lUffMod.getCodUfficio());

			// Perchè due ricerche?
			Vector lFasCumulati = ((Vector) lFasc.ExRicercaFascicoloOnView(lFasCumulato));
			if (lFasCumulati.size() == 0)
				throw new F3BException(F3BException.USER_MESSAGE,
						"Attenzione : Il fascicolo soggetto a cumulo non esiste! Impossibile procedere.");

			lFasCumulato = (FascicoloSiepModel) lFasCumulati.get(0);
			lFasCumulato = lFasc.ExRicercaFascicoloSiepByProgrAnnoCodUfficio(lFasCumulato);

			if (lFasCumulato.getCodStatoFascicolo().equals("02"))
				throw new F3BException(F3BException.USER_MESSAGE,
						"Attenzione : Il fascicolo soggetto a cumulo non è validato! Impossibile procedere.");

			if (lFasCumulato.getCodStatoFascicolo().equals("01"))
				throw new F3BException(F3BException.USER_MESSAGE,
						"Attenzione : Il fascicolo soggetto a cumulo risulta archiviato! Impossibile procedere.");

			this.isEventoNonValidatoPerFascicoloCumulato(lFasCumulato.getIdFascicoloSiep());
		} else {
			// 07-06-2006 -- Dario -- Viviana
			// this.isEventoNonValidatoPerCumulo();
			this.isEventoNonValidato();
		}

		Option lOption = new Option(DecodificheManager.getInstance().getTipoProvvedimentoCumulo(), "01");
		setRequestAttribute("TipoProvv", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getTipoUfficioCumulo(), "-");
		setRequestAttribute("autoritaEmiCumulo", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getTipoUfficioCumuloSentenzaDecreto(), "-");
		setRequestAttribute("autoritaEmiCumuloSentenzaDecreto", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getUfficioLogin(),
				getUfficioUtenteConnesso().getCodTipoUfficio());
		setRequestAttribute("autorita", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getTipoAutoritaCumulo(), "-");
		setRequestAttribute("autoritaCumulo", "" + lOption);

		setRequestAttribute("PosizioneGiuridica", lPG);
		setRequestAttribute("PenaResidua", lPenRes);
		setRequestAttribute("Cumulato", lFasCumulato);
		setRequestAttribute("LuogoUtenteConnesso", this.getUfficioUtenteConnesso().getDescrComune());

		return PG_CARICAFASCICOLI_MANUALE; // restituisce la jsp di VIEW
	}

	/*
	 * metodo specializzato per gli eventi del fascicolo cumulato -- 19-05-05 -- Dario -- Luciana SE
	 * CodTipoEvento = 05(Richiesta Istruttoria) NON DEVE FARE IL CONTROLLO
	 */
	protected void isEventoNonValidatoPerFascicoloCumulato(BigDecimal aIdFascCumulato) throws F3BException {
		IEventoSimeone lCtrl = SICOLookupRemote.getEventoSimeoneRemote();
		EventoModel lEveMod = new EventoModel();
		lEveMod = lCtrl.ExRicercaEventoByFascicoloSiepDescUfficioConnesso(aIdFascCumulato,
				this.getCodUfficioUtenteConnesso());

		if (lEveMod != null && lEveMod.getCodTipoEvento() != null
				&& !lEveMod.getCodTipoEvento().equals("05")) {
			if (lEveMod.getFlagDocumentoRegistrato() == null
					|| "N".equals(lEveMod.getFlagDocumentoRegistrato())) {
				throw new F3BException(F3BException.USER_MESSAGE,
						"Esiste un evento NON validato del fascicolo cumulato. Validarlo o cancellarlo e rieseguire la funzione.");
			}
		}
	}

}