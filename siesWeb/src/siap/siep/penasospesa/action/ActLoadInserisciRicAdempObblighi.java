package siap.siep.penasospesa.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActLoadInserisciRicAdempObblighi
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di una Richiesta Estinzione Reato.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011
 * </p>
 * <p>
 * Company: Agile
 * </p>
 * <p>
 * @author: Luigi
 * </p>
 * 
 * @version 1.0
 */

public class ActLoadInserisciRicAdempObblighi extends ActLoadInserisciRichiesta {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {
		mNomeAction = "siap.siep.penasospesa.action.ActLoadInserisciRicAdempObblighi";
		mNomeJsp = IWebConstants.ROOT_DIR + "files/siap/siep/penasospesa/LoadInserisciRicAdempObblighi.jsp"; // PG_LOAD_INSERISCIRICHIESTAREVOCA;

		return super.processRequest();

	}

	@SuppressWarnings("rawtypes")
	protected String preparazioneForm() throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActLoadInserisciRicAdempObblighi: inizio");

		setRequestAttribute("modalita", "I");

		String lCodSospCondizionale = ricercaBeneficio();

		Option lOptionTipoObbligo = new Option(DecodificheManager.getInstance().getTipoSospSubordinata(),
				lCodSospCondizionale);
		setRequestAttribute("tipoObbligo", "" + lOptionTipoObbligo);

		DecodificheModel lModel = new DecodificheModel();
		IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
		lModel.setContesto("MOTIVO_PROVVEDIMENTO");
		lModel.setCodiceAlternativo("DETERMINAZIONE_TERMINI");
		Collection lColMotivo = lDecodifiche.ExRicercaDecodificheOrdinatePerCodice(lModel);
		Option lOptionOggetto = new Option(lColMotivo);

		setRequestAttribute("oggetto", lOptionOggetto.toString());

		// AVVOCATO
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();
		IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote();
		Vector lAvvocati = new Vector();
		try {
			lAvvocati = lAvvCtrl.ExRicercaAvvocatiByFascicolo(lIdFascicolo);
		} catch (Exception ex) {
			// setRequestAttribute("avvocati", lAvvocati);
		}
		setRequestAttribute("avvocati", lAvvocati);
		Option lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "-");
		setRequestAttribute("autoritaEsternaN", "" + lOption);

		// ==========================================================================
		// Caricamento combo
		// ==========================================================================
		// Decodifica TIPO_AUTORITA (DESTINATARIO)
		Collection lTipoAutorita = DecodificheManager.getInstance().getTipoAutorita();
		lOption = new Option(lTipoAutorita);

		// Lista Destinatario Polizia
		String[] lStringFilter1 = { "-", "92", "59", "60", "61", "28", "58", "19" };
		lOption.setFilter(lStringFilter1);
		setRequestAttribute("tipoAutoritaPolizia", "" + lOption);

		// Lista Destinatario Altri Destinatari
		String[] lStringFilter2 = {};
		lOption.setFilter(lStringFilter2);
		setRequestAttribute("autoritaEsternaAltra", "" + lOption);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActLoadInserisciRicAdempObblighi: fine");

		return mNomeJsp; // restituisce la jsp di VIEW
	}

}