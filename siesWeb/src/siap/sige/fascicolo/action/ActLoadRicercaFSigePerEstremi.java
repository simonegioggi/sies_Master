package siap.sige.fascicolo.action;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.magistrato.util.MagistratoUtils;
import siap.sige.sezione.util.SezioneUtils;
import siap.sige.web.ActionSige;

/**
 * <p>
 * Title: ActLoadRicercaFSigePerEstremi
 * </p>
 * <p>
 * Description: Classe Action per la visualizzazione della maschera di Ricerca Fascicolo SIGE.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2009
 * </p>
 * <p>
 * Company:
 * </p>
 *
 * @version 1.0
 */
public class ActLoadRicercaFSigePerEstremi extends ActionSige implements ICostantiFascicoloSige {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// Imposta Tipo Ufficio SIGE.
		Option lOption = new Option(DecodificheManager.getInstance().getTipiUfficioSige());

		String strCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();
		lOption.setSelected(strCodTipoUfficio);
		setRequestAttribute("tipoUfficioSige", lOption.toString());

		setRequestAttribute("TipoUfficioConnesso", strCodTipoUfficio);
		String strDescrComune = getUfficioUtenteConnesso().getDescrComune();
		setRequestAttribute("ComuneUfficioConnesso", strDescrComune);

		// Imposta Tipo Atto SIGE.
		Option lTipoAttoOpt = new Option(DecodificheManager.getInstance().getTipoAttoSige());
		setRequestAttribute("tipoAtto", "" + lTipoAttoOpt);

		// Imposta Codici Oggetto SIGE.
		Option lTipoOggOpt = new Option(DecodificheManager.getInstance().getOggettoSige());
		lTipoOggOpt.setAddBlankItem(true);
		lTipoOggOpt.setValueBlankItem("Tutti");
		setRequestAttribute("oggettoSige", "" + lTipoOggOpt);

		// Elenco delle Sezioni previste per l'Ufficio
		Option lSezioniOpt = null;
		lSezioniOpt = new Option(SezioneUtils.getElencoSezioniPerRicerca(getCodUfficioUtenteConnesso()));
		lSezioniOpt.setAddBlankItem(true);
		lSezioniOpt.setValueBlankItem("-");
		setRequestAttribute("elencoSezioni", "" + lSezioniOpt);

		// Carica Combo x TipoRito.
		Option lRitiOpt = new Option(getElencoTipiRito(), Option.BLANK_ITEM);
		lRitiOpt.setAddBlankItem(true);
		lRitiOpt.setValueBlankItem("-");
		setRequestAttribute("tipoRito", "" + lRitiOpt);

		// Imposta l'elenco magistrati.
		Option lMagistratiOpt = null;
		lMagistratiOpt = new Option(
				MagistratoUtils.getElencoMagistratiPerRicercaSige(getCodUfficioUtenteConnesso()));
		// @emma 13072018 intervento post COLLAUDO 11.2 (modifico le lMagistratiOpt; il trattino è già
		// presente)
		// lMagistratiOpt.setAddBlankItem(true);
		lMagistratiOpt.setSelected("-");

		setRequestAttribute("magistrato", "" + lMagistratiOpt);

		return PG_LOAD_RICERCAFSIGE_PERESTREMI; // restituisce la jsp di VIEW
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Collection getElencoTipiRito() throws Exception {

		Collection lCollRet = new ArrayList();
		try {
			ArrayList lColl = new ArrayList(DecodificheManager.getInstance().getTipoGiudizioSige());
			DecodificheModel lDecMod = new DecodificheModel();
			lDecMod.setCode("Mancante");
			lDecMod.setDescription("Tipo rito mancante");
			lCollRet.add(lDecMod);
			lDecMod = new DecodificheModel();
			lDecMod.setCode("Tutti");
			lDecMod.setDescription("Monocratico e Collegiale");
			lCollRet.addAll(lColl);
			lCollRet.add(lDecMod);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Nessun Elemento trovato");
		}
		return lCollRet;
	}

}