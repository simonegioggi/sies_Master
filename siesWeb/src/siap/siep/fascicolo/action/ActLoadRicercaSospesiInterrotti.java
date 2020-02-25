package siap.siep.fascicolo.action;

import java.util.Collection;
import java.util.Vector;

import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;

/**
 * <p>
 * Title: ActLoadRicercaSospesiInterrotti
 * </p>
 * <p>
 * Description: Ricerca procedimenti Sospesi/Interrotti
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: Bull
 * </p>
 */

public class ActLoadRicercaSospesiInterrotti extends ActionSiap implements ICostantiFascicoloSiep {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		// TIPO SOSPESI/INTERROTTI
		Option lOption = new Option(DecodificheManager.getInstance().getTipoIntSosp(), "0001");
		setRequestAttribute("tipo", "" + lOption);

		// MOTIVO SOSPESI/INTERROTTI
		Collection lColMotivo = (Collection) DecodificheManager.getInstance().getMotivoIntSosp();
		setRequestAttribute("motivo", lColMotivo);

		// MOTIVO
		Collection lOggetto = (Collection) DecodificheManager.getInstance().getMotivoProvvedimento();
		setRequestAttribute("oggetto", lOggetto);

		// model degli elementi del vettore
		// UfficioAccorpatoModel lUAMod = new UfficioAccorpatoModel();
		String codUfficioUtente = getCodUfficioUtenteConnesso();

		// esegue la query per recuperare l'elenco degli uffici accorpati di tipo PM ed appartenenti al
		// distretto dell'utente loggato
		IUfficio lUACon = SICOLookupRemote.getUfficioRemote();
		Vector lUffAcc = lUACon.ListaUfficiAccorpati("PM", codUfficioUtente);

		// imposta sulla request la lista degli uffici accorpati
		setRequestAttribute("elencoUfficiAccorpati", lUffAcc);

		return PG_LOAD_RICERCA_SOSPESI_INTERROTTI; // restituisce la jsp di VIEW
	}
}
