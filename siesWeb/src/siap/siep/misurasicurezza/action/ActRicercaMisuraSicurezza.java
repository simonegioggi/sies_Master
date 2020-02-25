package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.rifasiep.controller.IRiferimentoFascicoloSiep;
import siap.sius.rifasiep.model.RiferimentoFascicoloSiepModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActRicercaMisuraSicurezza
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di MisuraSicurezza
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
public class ActRicercaMisuraSicurezza extends ActionSiap implements ICostantiMisuraSicurezza {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		List lListMis = new ArrayList();
		if (!isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP)) {
			BigDecimal lIdFasc = getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP);
			IMisuraSicurezza lCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();

			// 17-12-2014 Misure Sicurezza con DATA_FINE_VALIDITA
			// lListMis = lCtrl.ExRicercaMisuraSicurezzaByIdFascicolo(lIdFasc);
			lListMis = lCtrl.ExRicercaTutteMisureSicurezzaByIdFascicoloOrd(lIdFasc);
		}

		// per ogni Misura della lista, cerco eventuale Titolo Esecutivo Associato
		List Mis_e_Rif = new ArrayList();
		if (lListMis.size() > 0) {
			IRiferimentoFascicoloSiep lCtrlS = SIUSLookupRemote.getRiferimentoFascicoloSiepRemote();
			for (int i = 0; i < lListMis.size(); i++) {
				MisuraSicurezzaModel lMisMod = (MisuraSicurezzaModel) lListMis.get(i);
				if (lMisMod.getFasSieIdFascicoloSiepRif() != null) {
					RiferimentoFascicoloSiepModel lRifMod = null;
					lRifMod = (RiferimentoFascicoloSiepModel) lCtrlS
							.ExRicercaRiferimentoFascicoloSiepByKey(lMisMod.getFasSieIdFascicoloSiepRif());
					if (lRifMod != null && lRifMod.getIdRiferimentoFascicoloSiep() != null) {
						lMisMod.setRiferimentoFascicoloSiep(lRifMod);
					}

					Mis_e_Rif.add(lMisMod);
				} else {
					Mis_e_Rif.add(lMisMod);
				}
			}
		}

		if (Mis_e_Rif.size() == 1) {
			MisuraSicurezzaModel lMisSicMod = (MisuraSicurezzaModel) Mis_e_Rif.get(0);
			setRequestAttribute("misurasicurezza", lMisSicMod);
			setFunctionsAvailableToRequest("siap.siep.misurasicurezza.action.ActLoadDettaglioMisuraSicurezza");
			return PG_LOAD_DETTAGLIOMISURASICUREZZA;
		} else {
			setRequestAttribute("misurasicurezza", Mis_e_Rif);
			return PG_RICERCAMISURASICUREZZA;
		}
	}

}