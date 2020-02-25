package siap.siep.provvedimentogenerico.action;

import java.util.Collection;

import siap.sico.decodifiche.controller.DecodificheManager;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadInserisciProvvGenericoDecSorv
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci provvedimento generico derivato da Decisioni della
 * Sorveglianza
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
public class ActLoadInserisciProvvGenericoDecSorv extends ActProvvedimentoGenerico {
	
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		// tutti i controlli e la maggior parte delle request si trovano nel padre
		String lRitorno = this.getProvvedimentoGenerico();
		if (!lRitorno.equals(""))
			return lRitorno;

		// carico il tipo provvedimento
		Option lOptionTipProv = new Option(DecodificheManager.getInstance().getTipoProvvedimenti());
		lOptionTipProv.setFilter(new String[] { "-", "02", "03" }); // DECRETO o ORDINANZA
		lOptionTipProv.setSelected("-");
		setRequestAttribute("tipoprovvedimento", "" + lOptionTipProv);

		// AUTORITA' EMITTENTE
		Option lOptionAE = new Option(DecodificheManager.getInstance().getTipoUfficioSIUS());
		setRequestAttribute("autorita", "" + lOptionAE);

		// CONTENUTO SIEP
		Collection lContenuto = (Collection) DecodificheManager.getInstance().getTipoContenuto();
		setRequestAttribute("contenuto", lContenuto);

		// MOTIVO PROVVEDIMENTO
		Collection lOggetto = (Collection) DecodificheManager.getInstance().getMotivoProvvedimento();
		setRequestAttribute("oggetto", lOggetto);

		// ESITO SIEP
		// Collection lEsito = (Collection)DecodificheManager.getInstance().getEsitoSiep();
		Collection lEsito = (Collection) DecodificheManager.getInstance().getEsitoTenore();
		setRequestAttribute("esito", lEsito);

		setRequestAttribute("tiporegistro", "0001"); // SIUS

		// MEV 10 - filtro sui minorenni
		setRequestAttribute("filtroMinorenni", this.getFiltroMinorenni());

		return PG_LOAD_INSERICI_PROVVEDIMENTO_GENERICO;
	}

}