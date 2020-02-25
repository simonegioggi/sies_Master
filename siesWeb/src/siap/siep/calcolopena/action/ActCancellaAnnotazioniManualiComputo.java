package siap.siep.calcolopena.action;

import java.util.Vector;

import siap.sico.web.ActionSiap;
import siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.misuracautelarebdmc.controller.IMisuraCautelareBdmc;
import siap.siep.misuracautelarebdmc.model.MisuraCautelareBdmcModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.web.IWebConstants;

/**
 * Classe Action per la cancellazione delle annotazioni manuali - Rideterminazione Pena - Provvedimenti del PM
 * (presofferto stesso titolo, fungibilità Altro titolo, fungibilità senza titolo)
 *
 */
@SuppressWarnings("rawtypes")
public class ActCancellaAnnotazioniManualiComputo extends ActionSiap implements ICostantiAnnotazioneManuale {

	/*****************************************************************************
	 * Metodo per la cancellazione delle Annotazioni Manuali. Al termine della cancellazione viene invocata la
	 * action di LoadInserimento della tipo annotazione appena cancellata
	 * 
	 * @return Action Load Inserimento @throws
	 */
	public String processRequest() throws Exception {

		IAnnotazioneManuale lCtrlAnnMan = SIEPLookupRemote.getAnnotazioneManualeRemote();

		// Ricerco tutte le annotazioni dello stesso tipo legate ala fascicolo
		Vector eleAnnoManu = new Vector();

		AnnotazioneManualeModel lAnnManCorrente = lCtrlAnnMan.ExRicercaAnnotazioneManualeByKey(
				getRequestBigDecimalParameter(CAMPO_ID_ANNOTAZIONE_MANUALE));
		if (lAnnManCorrente != null && lAnnManCorrente.getEveIdEvento() != null) {
			eleAnnoManu = lCtrlAnnMan.ExRicercaAnnotazioneManualeByIdEvento(lAnnManCorrente.getEveIdEvento());

		}
		// fine ricerca

		AnnotazioneManualeModel lAnnMan = lCtrlAnnMan.ExCancellaAnnotazioneManualeComputo(
				getRequestBigDecimalParameter(CAMPO_ID_ANNOTAZIONE_MANUALE), false);

		// Effettuo la cancellazione logica dell'annotazione dalla tabella misura_cautelare_bdmc in
		// in modo che la store procedure notifichi a BDMC l'avvenuta cancellazione
		if (eleAnnoManu != null && eleAnnoManu.size() != 0) {
			for (int i = 0; i < eleAnnoManu.size(); i++) {
				AnnotazioneManualeModel lAnnManuale = (AnnotazioneManualeModel) eleAnnoManu.get(i);
				MisuraCautelareBdmcModel lModBdmc = new MisuraCautelareBdmcModel();
				lModBdmc.setIdAnnotazioneManuale(lAnnManuale.getIdAnnotazioneManuale());
				lModBdmc.setFlagStato("I");
				IMisuraCautelareBdmc lCtrlBdmc = SIEPLookupRemote.getMisuraCautelareBdmcRemote();
				Vector misCautBdmc = lCtrlBdmc.ExRicercaMisuraCautelareBdmc(lModBdmc);
				if (misCautBdmc != null && misCautBdmc.size() != 0) {
					lModBdmc = new MisuraCautelareBdmcModel();
					lModBdmc = (MisuraCautelareBdmcModel) misCautBdmc.get(0);
					lModBdmc.setFlagStato("C");
					if (lModBdmc.getStatoTrasmissioneIsc().compareTo("S") == 0)
						lModBdmc.setStatoTrasmissioneIsc("N");
					else
						lModBdmc.setStatoTrasmissioneIsc("S");
					lCtrlBdmc.ExModificaMisuraCautelareBdmc(lModBdmc, null);
				}
			}
		}
		String lPage = "";
		if (lAnnMan.getCodTipoAnnotazione() != null && lAnnMan.getCodTipoAnnotazione().equals("005")) // STESSO
																										// TITOLO
		{
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.calcolopena.action.ActLoadAnnotazioniManualiMC";
		} else if (lAnnMan.getCodTipoAnnotazione() != null && lAnnMan.getCodTipoAnnotazione().equals("006")) // ALTRO
																												// TITOLO
		{
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.calcolopena.action.ActLoadAnnotazioniManualiCompAltroTitolo";
		} else // SENZA TITOLO
		{
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.calcolopena.action.ActLoadAnnotazioniManualiCompSenzaTitolo";
		}

		return lPage;
	}

}