package siap.siep.misurasicurezza.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadTrasferisciRichiestaAccertaPericoloSociale
 * </p>
 * <p>
 * Description: Trasferisce la Richietsta Sccertamento Pericolosità sociale
 * <p>
 * verso gli uffici di sorveglianza (MDS)
 * </p>
 * <p>
 * Copyright: Copyright (c) 2013
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Ambrosino
 * @version 1.0
 */
public class ActLoadTrasferisciRichiestaAccertaPericoloSociale extends ActionSiap implements
		ICostantiMisuraSicurezza {

	public String processRequest() throws Exception {

		if (!isRequestParameterNullObj("IdEvento"))
			setRequestAttribute("IDEvento", this.getRequestStringParameter("IdEvento"));

		// EventoModel lEventoModel = lCtrl.ExRicercaEventoByKey(new BigDecimal(lId));

		if (!isRequestParameterNullObj("autorita"))
			setRequestAttribute("sedemagsor", this.getRequestStringParameter("autorita"));

		String lTipoUffSorv = "";
		if (!isRequestParameterNullObj("CodTipoSorv")) {
			lTipoUffSorv = this.getRequestStringParameter("CodTipoSorv");
			setRequestAttribute("codmagsor", this.getRequestStringParameter("CodTipoSorv"));
		}

		// Insieme degli uffici destinatari
		// Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioSIUS(), "UDS");
		// setRequestAttribute("MagSor", "" + lOption);
		// Oggetti per il caricamento della combo Autorità Emittente
		Option lAutoritaSORV = new Option(DecodificheManager.getInstance().getTipoUfficio());
		lAutoritaSORV.setFilter(new String[] { "TDS", "UDS", "UDSM", "TDSM" });
		if (!lTipoUffSorv.equals(""))
			lAutoritaSORV.setSelected(lTipoUffSorv);
		setRequestAttribute("MagSor", "" + lAutoritaSORV);

		return PG_TRASFERISCI_RICH_ACCERTA_PERICOLO_SOC;
	}

}