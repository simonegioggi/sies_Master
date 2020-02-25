package siap.sius.statistiche.action;

/**
 * <p>Title: ActLoadRicercaPerDecreto</p>
 * <p>Description: Visualizza la form di "Ricerca procedimeno per estremi decreto".
 * Poichè la jsp utilizzata è la stessa per la "Ricerca procedimento per estremi ordinanza" 
 * il titolo della funzione viene passato nella requesy.</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.magistrato.util.MagistratoUtils;
import siap.sico.web.ActionSiap;
import siap.sius.esperto.controller.IEsperto;
import siap.sius.util.SIUSLookupRemote;
import f3b.web.html.Option;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActLoadRicercaPerDecreto extends ActionSiap implements ICostantiStatistiche {

	public String processRequest() throws Exception {

		setRequestAttribute("modalitaRicerca", RICERCA_DECRETO);

		// STUB 28/07/2009 Imposta l'elenco magistrati come in SIGE

		Option lMagistratiOpt = null;
		lMagistratiOpt = new Option(
				MagistratoUtils.getElencoMagistratiPerRicercaSige(getCodUfficioUtenteConnesso()));
		lMagistratiOpt.setAddBlankItem(true);
		lMagistratiOpt.setValueBlankItem("-");

		setRequestAttribute("magistrato", "" + lMagistratiOpt);

		Option lEspertiOpt = null;
		IEsperto lCtrl = SIUSLookupRemote.getEspertoRemote();
		ArrayList lColl = new ArrayList(lCtrl.ExElencoCbxEspertiByCodUfficio(getCodUfficioUtenteConnesso()));
		Collection lCollRet = new ArrayList();
		DecodificheModel lDecMod = new DecodificheModel();
		// lDecMod.setCode("TuttiEsperti");
		lDecMod.setCode((new BigDecimal(9999)).toString());
		lDecMod.setDescription("Tutti gli esperti");
		lCollRet.add(lDecMod);

		lCollRet.addAll(lColl);
		lEspertiOpt = new Option(lCollRet);

		setRequestAttribute("esperto", "" + lEspertiOpt);

		String strCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();
		setRequestAttribute("tipoUfficio", strCodTipoUfficio);

		return PG_LOAD_RICERCHE_ORDINANZA;
	}

}