package siap.siep.provvedimentogenerico.action;

import java.util.Collection;

import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;

/**
 * <p>
 * Title: ActLoadInserisciProvvGenericoDecGE
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci provvedimento generico derivato da Decisioni del GE
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ActLoadInserisciProvvGenericoDecGE extends ActProvvedimentoGenerico {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		// tutti i controlli e la maggior parte delle request si trovano nel padre
		String lRitorno = getProvvedimentoGenerico();
		if (!lRitorno.equals(""))
			return lRitorno;

		// carico il tipo provvedimento
		Option lOptionTipProv = new Option(DecodificheManager.getInstance().getTipoProvvedimenti());
		lOptionTipProv.setFilter(new String[] { "-", "02", "03" }); // DECRETO o ORDINANZA
		lOptionTipProv.setSelected("-");
		setRequestAttribute("tipoprovvedimento", "" + lOptionTipProv);

		// AUTORITA' EMITTENTE
		Option lOptionAE = new Option(DecodificheManager.getInstance().getTipoUfficio());
		// MEV_66: aggiunti 5 uffici che possono emettere la declaratoria
		lOptionAE.setFilter(new String[] { "CAP", "DIB", "GUP", "GIP", "CAS", "CASAP", "TRIBSD", "GUPM",
				"CAPSM", "DIBM", "GIPM", "GP" });
		setRequestAttribute("autorita", "" + lOptionAE);

		// CONTENUTO SIEP
		Collection lContenuto = DecodificheManager.getInstance().getTipoContenuto();
		setRequestAttribute("contenuto", lContenuto);

		// MOTIVO PROVVEDIMENTO
		Collection lOggetto = DecodificheManager.getInstance().getMotivoProvvedimento();
		setRequestAttribute("oggetto", lOggetto);

		// ESITO SIEP
		// Collection lEsito = (Collection)DecodificheManager.getInstance().getEsitoSiep();
		Collection lEsito = DecodificheManager.getInstance().getEsitoTenore();
		setRequestAttribute("esito", lEsito);

		setRequestAttribute("tiporegistro", "0002"); // RPE

		return PG_LOAD_INSERICI_PROVVEDIMENTO_GENERICO;
	}

}