package siap.sius.misurasicurezza.action;

/**
* <p>Title: ActLoadPopupDettaglioMisuraSic</p>
* <p>Description: Classe Action per la Load della POPUP Dettaglio </p>
* <p> 			Misure Sicurezza SiuS;		</p>
* <p>Copyright: Copyright (c) 2015</p>
* <p>Company: IntersistemiItalia</p>
* @version 8.3
*/

import java.math.BigDecimal;
import java.util.Vector;

import siap.siep.misurasicurezza.controller.MisuraSicurezzaController;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.sius.ActionSius;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;

public class ActLoadPopupDettaglioMisuraSic extends ActionSius implements ICostantiSiusMisuraSicurezza {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// Attivazione punto di Ritorno
		setLinkRitorno();

		// Lettura elenco Misure di Sicurezza Collegate al Fascicolo SIUS.
		MisuraSicurezzaModel aMisuraSicurezza = new MisuraSicurezzaModel();
		BigDecimal lIdFascicoloSIUS = getRequestBigDecimalParameter(
				ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS);
		aMisuraSicurezza.setFasSiuIdFascicoloSius(lIdFascicoloSIUS);

		MisuraSicurezzaController lCtrl = new MisuraSicurezzaController();
		Vector lVect = lCtrl.ExRicercaMisuraSicurezzaAndRifTitoloEsec(aMisuraSicurezza);

		setRequestAttribute("misureSicurezza", lVect);

		return PG_LISTA_POPUP_SIUS_MISURASICUREZZA;
	}

}