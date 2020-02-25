package siap.siep.penasospesa.action;

import java.math.BigDecimal;
import java.util.Collection;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActLoadInserisciRicEstinzioneReato
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

public class ActLoadInserisciRicDeterminazioneTermini extends ActLoadInserisciRichiesta {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {
		mNomeAction = "siap.siep.penasospesa.action.ActLoadInserisciRicDeterminazioneTermini";
		mNomeJsp = PG_LOAD_INSERISCIRICHIESTADETERMINAZIONETERMINI;

		return super.processRequest();

	}

	@SuppressWarnings("rawtypes")
	protected String preparazioneForm() throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActLoadInserisciRicDeterminazioneTermini: inizio");

		setRequestAttribute("modalita", "I");

		String lCodSospCondizionale = ricercaBeneficio();

		Option lOptionTipoObbligo = new Option(DecodificheManager.getInstance().getTipoSospSubordinata(),
				lCodSospCondizionale);
		setRequestAttribute("tipoObbligo", "" + lOptionTipoObbligo);

		DecodificheModel lModel = new DecodificheModel();
		IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
		lModel.setContesto("MOTIVO_PROVVEDIMENTO");
		// lModel.setCodiceAlternativo("DETERMINAZIONE_TERMINI");
		lModel.setCode("1113");
		Collection lColMotivo = lDecodifiche.ExRicercaDecodificheOrdinatePerCodice(lModel);
		Option lOptionOggetto = new Option(lColMotivo);

		setRequestAttribute("oggetto", lOptionOggetto.toString());

		// Dettaglio Fascicolo SIEP
		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal aId = lFascicoloModel.getIdFascicoloSiep();
		IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
		DettaglioFascicoloModel lDettaglio = null;
		lDettaglio = lCtrl.ExDettaglioFascicoloSiep(aId);
		if (lDettaglio == null)
			throw new F3BException(F3BException.USER_MESSAGE, "Fascicolo non presente");
		setRequestAttribute("dettagliofascicolo", lDettaglio);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActLoadInserisciRicDeterminazioneTermini: fine");

		return mNomeJsp; // restituisce la jsp di VIEW
	}

}