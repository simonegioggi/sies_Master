package siap.siep.calcolopena.action;

import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.evento.model.EventoModel;
import siap.siep.SIEPException;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.annotazionemanuale.model.AnnotazioneOrdinanzaSigeModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeModel;
import siap.sige.util.SIGELookupRemote;

/*******************************************************************************
 * Action utilizzata per il caricamento della pagina di inserimento delle DECISIONI del GE -> Applicazione
 * Benefici -> Amnistia/Indulto.
 *
 * Questa action estende la ActLoadInserisciAnnotazioniManuali che espone il metodo
 * loadRichiestaAnnotazioniManuali che effettua tutte le operazioni in comune alle action di caricamento delle
 * pagine di inserimento delle annotazioni manuali.
 *
 */
public class ActLoadNuovoCalcoloPenaBenefici extends ActLoadInserisciAnnotazioniManuali {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/*****************************************************************************
	 *
	 *
	 * @return Nome della pagina JSP su cui posizionarsi al termine dell'elaborazione
	 * @throws F3BException
	 */
	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		String lPageErr = loadRichiestaAnnotazioniManuali("0284"); // AMNISTIA/INDULTO

		if (lPageErr != null)
			return lPageErr; // ritorno la pagina di errore restituita dalla funzione

		/// Ricerca eventuale Ordinanza GE
		ricercaOrdinanzaGE();

		// Caricamento combo specifiche della pagina di inserimento Amnistia/Indulto
		// non presenti sulle altre pagine
		// 27/10/2006 Aggiunto posizionamento combo Tipo beneficio su Indulto.
		Option lOption = new Option(DecodificheManager.getInstance().getTipoAnnotazioneManualeBenefici(),
				"002");
		setRequestAttribute("TipoAnnotazioneManuale", "" + lOption);

		// ==========================================================================
		// Caricamento Combo DPR
		// ==========================================================================
		lOption = new Option(DecodificheManager.getInstance().getDPR());
		// 27/10/2006 Aggiunto posizionamento combo DPR all'ultimo elemento.
		Vector lVect = (Vector) DecodificheManager.getInstance().getDPR();
		DecodificheModel lDecMod = (DecodificheModel) lVect.lastElement();
		lOption.setSelected(lDecMod.getCode());
		setRequestAttribute("listaDPR", "" + lOption);

		setRequestAttribute("tipoBeneficio", "AMNI");
		String lPage = IWebConstants.ROOT_DIR
				+ "/files/siap/siep/calcolopena/LoadAnnotazioniManualiBenefici.jsp";

		return lPage;
	}

	/**
	 * Ricerca eventuale Ordinanza del GE legata al Fascicolo SIEP, ovvero Decisione del GE con
	 * AnnotazioneManuale non Validata.
	 *
	 */

	private boolean ricercaOrdinanzaGE() throws Exception {
		boolean lRet = false;

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		if (lFascMod == null || lFascMod.getIdFascicoloSiep() == null)
			throw new SIEPException(SIEPException.EX_NOT_FOUND, "Dati delFascicolo non in sessione !");

		// ==========================================================================
		// Nel caso delle decisioni del GE si ricerca l'eventuale Ordinanza del GE
		// e annotazione associata
		// ==========================================================================
		EventoModel lEveMod = new EventoModel();

		lEveMod.setCodTipoEvento("01");
		lEveMod.setCodTipoProvvedimento("03"); // ordinanza
		lEveMod.setCodMotivo("0284");
		lEveMod.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
		lEveMod.setFlagDocumentoRegistrato("S");

		// Ricerca Ordinanza GE e relativa annotazione emessa da Ufficio SIGE
		AnnotazioneOrdinanzaSigeModel lAnnOrdSigeMod = null;
		IAnnotazioneManuale lCtrlAnnMan = SIEPLookupRemote.getAnnotazioneManualeRemote();
		lAnnOrdSigeMod = lCtrlAnnMan.ExRicercannotazioneManualeOrdinanzaSigeByIdFascicolo(lEveMod);

		if (lAnnOrdSigeMod != null) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Trovata l'Ordinanza emessa dal GE");
			if (lAnnOrdSigeMod.getAnnotazioneManuale().getFlagValidato() == null
					|| !lAnnOrdSigeMod.getAnnotazioneManuale().getFlagValidato().equalsIgnoreCase("S")) {
				lRet = true;

				lAnnOrdSigeMod.setAnnoNumGeAnnotazione();
				// setRequestAttribute("OrdinanzaGEAnn", lAnnOrdSigeMod.getAnnotazioneManuale());
				setRequestAttribute("OrdinanzaGE", lAnnOrdSigeMod);
				// setRequestAttribute("OrdinanzaGEEve", lAnnOrdSigeMod.getEvento());

				// MEV 29 punto 11 - portare anche Anno e Numero Fascicolo SIGE
				FascicoloSigeModel lFaSige = null;
				IFascicoloSige lCtrlFSG = SIGELookupRemote.getFascicoloSigeRemote();
				lFaSige = (FascicoloSigeModel) lCtrlFSG
						.ExRicercaFascicoloSigeByKey(lAnnOrdSigeMod.getProvSige().getFasIdFascicoloSige());
				setRequestAttribute("ProcedimentoGE", lFaSige);
			}

		}

		return lRet;
	}

}