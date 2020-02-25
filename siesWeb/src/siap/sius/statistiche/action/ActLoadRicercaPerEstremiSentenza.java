package siap.sius.statistiche.action;

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
public class ActLoadRicercaPerEstremiSentenza extends ActionSiap implements ICostantiStatistiche {

	public String processRequest() throws Exception {

		setRequestAttribute("modalitaRicerca", RICERCA_ESTREMI_SENTENZA);

		Option lMagistratiOpt = new Option(
				MagistratoUtils.getElencoMagistratiPerRicercaSige(getCodUfficioUtenteConnesso()));
		lMagistratiOpt.setAddBlankItem(true);
		lMagistratiOpt.setValueBlankItem("-");
		setRequestAttribute("magistrato", lMagistratiOpt.toString());

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

		return PG_LOAD_RICERCHE_ESTREMI_SENTENZA;
	}

}