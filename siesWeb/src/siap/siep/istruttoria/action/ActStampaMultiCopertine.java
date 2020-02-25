package siap.siep.istruttoria.action;

import java.io.ByteArrayOutputStream;

import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoria.controller.IIstruttoria;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * Stampa un intervallo di copertine per i fascicoli
 * @author Giselda De Vita
 *
 */
public class ActStampaMultiCopertine extends ActionSiap implements ICostantiIstruttoria
{
	public String processRequest() throws F3BException
	{
		FascicoloSiepModel lFasMod = new FascicoloSiepModel();

		if( ! isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE)  )
			lFasMod.setChiaveAnnoIniziale(getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE));

		if( ! isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_INIZIALE) )
			lFasMod.setChiaveProgrIniziale(getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_INIZIALE));

		if( ! isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE) )
			lFasMod.setChiaveAnnoFinale(getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE));

		if( ! isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_FINALE) )
			lFasMod.setChiaveProgrFinale(getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_FINALE));


		UtenteModel lUtenteMod = this.getUtenteConnesso();

		lFasMod.setChiaveUfficio(this.getCodUfficioUtenteConnesso());
		lFasMod.setDescrTipoUfficio(this.getUfficioByCodUfficio(getCodUfficioUtenteConnesso()).getDescrTipoUfficio());
		lFasMod.setDescrComuneUfficio(this.getUfficioByCodUfficio(getCodUfficioUtenteConnesso()).getDescrComune());

		String lPageReturn = IWebConstants.PG_DOWNLOAD;

		try{
			IIstruttoria lCtrl = SIEPLookupRemote.getIstruttoriaRemote();
			ByteArrayOutputStream lReport = lCtrl.ExStampaCopertineMultiple( lFasMod, lUtenteMod );
			setRequestAttribute("report", lReport);
		}catch(F3BException ex)
		{
			lPageReturn = IWebConstants.PG_MESSAGE;
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Nessun procedimento nell'intervallo selezionato!");
		}

		return lPageReturn;
	}
}