package siap.siep.istruttoria.action;

import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoria.controller.IIstruttoria;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * Stampa un intervallo di copertine per i fascicoli
 * @author Giselda De Vita
 *
 */
public class ActStampaMultiInizioEsecuzione extends ActionSiap implements ICostantiIstruttoria
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

		String lPageReturn = IWebConstants.PG_MESSAGE;

		try{
			/*Metodo che stampa 
			IIstruttoria lCtrl = SIEPLookupRemote.getIstruttoriaRemote();
			ByteArrayOutputStream lReport = lCtrl.ExStampaInizioEsecuzioneMultiple( lFasMod, lUtenteMod, this.getUfficioUtenteConnesso() );
			setRequestAttribute("report", lReport);
			*/
			
			IIstruttoria lCtrl = SIEPLookupRemote.getIstruttoriaRemote();
			lCtrl.ExInserisciRichiestaJmsStampaInizioEsecuzioneMultiple (lFasMod, lUtenteMod, this.getUfficioUtenteConnesso() );
		
			RedirectTo lRedirigi = new RedirectTo();
		    lRedirigi.setPage( IWebConstants.PG_MAIN );
		    lRedirigi.setAction("siap.siep.istruttoria.action.ActIstruttorieGriglia" );
		 
		    setRequestAttribute( IWebConstants.GOTO_PAGE, "" + lRedirigi );

			lPageReturn = IWebConstants.PG_MESSAGE;
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Richiesta di Stampa Inizio Esecuzione Multipla sottomessa al sistema.");
		
		}catch(F3BException ex)
		{
			
			lPageReturn = IWebConstants.PG_MESSAGE;
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Servizio JMS non attivo. \n Rivolgersi all'amministratore del sistema.");
		}

		return lPageReturn;
	}
}