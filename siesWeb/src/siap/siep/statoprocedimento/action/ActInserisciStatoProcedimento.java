package siap.siep.statoprocedimento.action;

import java.math.BigDecimal;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.statoprocedimento.controller.IStatoProcedimento;
import siap.siep.statoprocedimento.model.StatoProcedimentoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActInserisciStatoProcedimento
 * </p>
 * <p>
 * Description: Classe Action
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
public class ActInserisciStatoProcedimento extends ActionSiap implements ICostantiStatoProcedimento {

	public String processRequest() throws F3BException {

		FascicoloSiepModel lFasc = (FascicoloSiepModel) this.getSessionAttribute("fascicolo");
		StatoProcedimentoModel lStatoMod = new StatoProcedimentoModel();

		lStatoMod.setCodStatoProcedimento(this.getRequestStringParameter(CAMPO_COD_STATO_PROCEDIMENTO));
		lStatoMod.setData(getRequestDateParameter(CAMPO_ANNO_DATA, CAMPO_MESE_DATA, CAMPO_GIORNO_DATA));
		lStatoMod.setFasSieIdFascicoloSiep(lFasc.getIdFascicoloSiep());
		lStatoMod.setDataInserimento(DateUtils.getSysDate());
		lStatoMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		lStatoMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
		lStatoMod.setProgressivo(new BigDecimal(1));

		// StatoProcedimentoModel lStatoModel = new StatoProcedimentoModel();
		IStatoProcedimento lCtrl = SIEPLookupRemote.getStatoProcedimentoRemote();
		/* lStatoModel = */lCtrl.ExCancellaInserisciStatoProcedimento(lStatoMod);

		return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.statoprocedimento.action.ActLoadDettaglioStatoProcedimento";
	}

}