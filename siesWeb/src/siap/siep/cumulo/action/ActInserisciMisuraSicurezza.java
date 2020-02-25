package siap.siep.cumulo.action;

import java.math.BigDecimal;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misurasicurezza.action.ICostantiMisuraSicurezza;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActInserisciMisuraSicurezza
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento di MisuraSicurezza
 * </p>
 * <p>
 * Company: Engineering S.p.A.
 * </p>
 * 
 * @version 1.0
 */
public class ActInserisciMisuraSicurezza extends ActionSiap implements ICostantiMisuraSicurezza {

	public String processRequest() throws Exception {
		// Nuova Misura di Sicurezza da inserire
		MisuraSicurezzaModel lMisMod = null;

		if (!this.isRequestParameterNullObj("lTipoFun")) // paramentro passato solo nel caso di iscrizione
															// guidata
		{
			this.setRequestAttribute("lTipoFunzione", this.getRequestStringParameter("lTipoFun"));
		}

		FascicoloSiepModel lFasc = (FascicoloSiepModel) this.getSessionAttribute("fascicolo");
		// Controllo Fascicolo archiviato
		if (lFasc == null || "01".equals(lFasc.getCodStatoFascicolo())) {
			throw new F3BException(F3BException.USER_MESSAGE,
					"Il Procedimento non esiste o risulta archiviato");
		}

		lMisMod = letturaDatiMisura(lFasc.getIdFascicoloSiep());

		IMisuraSicurezza lCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
		/* MisuraSicurezzaModel llMisModRet = */lCtrl.ExInserisciMisuraSicurezza(lMisMod);

		// Prepara la pagina di destinazione
		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.cumulo.action.ActLoadDettaglioApplicazioneBenefici";
		return lPage;
	}

	protected MisuraSicurezzaModel letturaDatiMisura(BigDecimal aIdFascicoloSiep) throws Exception {
		MisuraSicurezzaModel lMisMod = new MisuraSicurezzaModel();

		lMisMod.setCodNatura(getRequestStringParameter(CAMPO_COD_NATURA));
		lMisMod.setCodTipo(getRequestStringParameter(CAMPO_COD_TIPO));
		lMisMod.setNumAnni(getRequestBigDecimalParameter(CAMPO_NUM_ANNI));
		lMisMod.setNumMesi(getRequestBigDecimalParameter(CAMPO_NUM_MESI));
		lMisMod.setNumGiorni(getRequestBigDecimalParameter(CAMPO_NUM_GIORNI));
		lMisMod.setAnnoReg38(new BigDecimal(DateUtils.getSysDate("yyyy")));

		lMisMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
		lMisMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		lMisMod.setDataInserimento(DateUtils.getSysDate());
		lMisMod.setFasSieIdFascicoloSiep(aIdFascicoloSiep);

		return lMisMod;
	}

}