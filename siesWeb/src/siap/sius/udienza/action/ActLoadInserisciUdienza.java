package siap.sius.udienza.action;

import siap.sico.assistentegiudiziario.controller.IAssistenteGiudiziario;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sius.esperto.controller.IEsperto;
import siap.sius.util.SIUSLookupRemote;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadInserisciUdienza
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di Udienza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ActLoadInserisciUdienza extends ActionSiap implements ICostantiUdienza {

	public String processRequest() throws Exception {

		String lCodUfficio = getCodUfficioUtenteConnesso();
		String lCodComune = getCodComuneUtenteConnesso();
		// MEV10-s3: modificato il codice tipo ufficio: lo prelevo dalla tipologia di utente connesso
		String lCodTipoUfficioPGCAP = "PGCAP";
		String lCodTipoUfficio = getCodTipoUfficioConnesso();
		setRequestAttribute("codTipoUfficio", "" + lCodTipoUfficio);

		// Inserimento ComboBOX.
		// Crea lista elenco magistrati nelle 3 diverse figure.
		IMagistrato lMagCtrl = SICOLookupRemote.getMagistratoRemote();
		Option lOption = new Option(lMagCtrl.ExElencoCbxMagistratiByCodUfficio(lCodUfficio));
		setRequestAttribute("elencoPresidenti", "" + lOption);
		setRequestAttribute("elencoGiudici1", "" + lOption);
		setRequestAttribute("elencoGiudici2", "" + lOption);

		// 20170904: [SG] per i procuratori e gli esperti reimposto il codice tipo ufficio a PGCAP (vedi
		// sopra) x2
		// , Option.BLANK_ITEM
		// Crea lista elenco procuratori.
		lOption = new Option(lMagCtrl.ExElencoCbxMagByCodComuneCodTipoUff(lCodComune, lCodTipoUfficioPGCAP));
		setRequestAttribute("elencoProcuratori", "" + lOption);

		// Crea lista elenco esperti.
		IEsperto lEspertoCtrl = SIUSLookupRemote.getEspertoRemote();
		// MERGE v10: cambiata query per aggiunta inserimento riga vuota per tipo ufficio
		// lOption = new Option(lEspertoCtrl.ExElencoCbxEspertiByCodUfficio(lCodUfficio) /* ,
		// Option.BLANK_ITEM */);
		lOption = new Option(
				lEspertoCtrl.ExElencoCbxEspertiByCodAndTipoUff(lCodUfficio, lCodTipoUfficioPGCAP));
		setRequestAttribute("elencoEsperti1", "" + lOption);
		setRequestAttribute("elencoEsperti2", "" + lOption);

		// Crea lista elenco assistenti.
		IAssistenteGiudiziario lAssistenteCtrl = SICOLookupRemote.getAssistenteGiudiziarioRemote();
		lOption = new Option(lAssistenteCtrl.ExElencoCbxAssistenteGiudiziarioByCodUfficio(lCodUfficio));
		setRequestAttribute("elencoAssistenti", "" + lOption);

		// Modifica del 04/10/2013 mev "Revisione Misure di Sicurezza SIUS"
		// In fase di Fissazione Udienza, dare la possibilità all'utente di definire
		// una nuova udienza direttamente dalla pagina "Inserimento Fissazione Udienza"
		// senza passare dalle Funzioni Amministrative
		String checkInsFissUdienza = null;
		if (!this.isRequestParameterNullObj(ICostantiUdienza.CAMPO_CHECK_INS_FISS_UDIENZA)) {
			checkInsFissUdienza = this
					.getRequestStringParameter(ICostantiUdienza.CAMPO_CHECK_INS_FISS_UDIENZA);
		}
		setRequestAttribute("checkInsFissUdienza", checkInsFissUdienza);

		// Imposta Modalità.
		setRequestAttribute("modalita", "I");

		return PG_LOAD_INSERISCIUDIENZA; // restituisce la jsp di VIEW
	}

}