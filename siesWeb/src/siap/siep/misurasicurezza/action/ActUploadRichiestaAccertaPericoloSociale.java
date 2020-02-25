package siap.siep.misurasicurezza.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.action.ICostantiPenaResidua;
import siap.siep.richiesta.controller.IRichiesta;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>Title: ActUploadRichiestaAccertaPericoloSociale</p>
 * <p>Description: Validazione della misura sicurezza</p>
 * <p>				Accertamento Pericolo Sociale </p>
 * <p>Copyright: Copyright (c) 2013</p>
 * <p>Company: </p>
 * @author AMBROS
 * @version 1.0
 */

public class ActUploadRichiestaAccertaPericoloSociale extends ActionSiap
implements ICostantiMisuraSicurezza
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	//private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	public String processRequest() throws Exception
	{

		//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		//siesLogger.debug("--XXXX-- ActUpLOad PericoloSociale -- > Sart " );
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		EventoModel lModel = new EventoModel();
		lModel.setIdEvento( getRequestBigDecimalParameter( ICostantiEvento.CAMPO_ID_EVENTO) );
	//	BigDecimal idPenaRes =null;
	//	if(!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA) &&
	//		getRequestStringParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA) != null	)
	//			idPenaRes = new BigDecimal(getRequestStringParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA));

		//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		//siesLogger.debug("--XXXX-- ActUpLOad PericoloSociale -- > idPenaRes = "+idPenaRes );
		InputStream lInput = getFile(ICostantiEvento.CAMPO_BLOB);

		if(lInput != null)
		{
			byte[] lBuffer = new byte[lInput.available()];

			lInput.read(lBuffer);
			ByteArrayInputStream lSt = new ByteArrayInputStream(lBuffer);
			lModel.setDocBlobIn(lSt);
		}

		lModel.setDataAggiornamento( DateUtils.getSysDate());

		lModel.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso() );
		lModel.setCodOperatoreAggiornamento(getCodUtenteConnesso() );

		if(isRequestChecked( ICostantiEvento.CAMPO_VALIDA) )
		{
			lModel.setFlagDocumentoRegistrato("S");
			IRichiesta lCtrlRich = SIEPLookupRemote.getRichiestaRemote();


			lCtrlRich.ExUpdateValidaRichiestaGenerica(lModel,lFascMod);
		}
		else
		{
			lModel.setFlagDocumentoRegistrato("N");

			IEvento lCtrl = SICOLookupRemote.getEventoRemote();
			lCtrl.ExUpdateDocument(lModel);
		}

		//Prepara la "pagina" di destinAction
		setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Aggiornamento Documento Avvenuto Correttamente!");

		if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_AZIONE_DETTAGLIO))
		{
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			if(!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA) &&
				getRequestStringParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA) != null)
			{	
				lRedirigi.setAction(getRequestStringParameter(ICostantiEvento.CAMPO_AZIONE_DETTAGLIO) + "&" + ICostantiEvento.CAMPO_ID_EVENTO + "=" + getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO) +
																									"&" + ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA + "=" + getRequestStringParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA)  );
			}
			else
			{
				lRedirigi.setAction(getRequestStringParameter(ICostantiEvento.CAMPO_AZIONE_DETTAGLIO) + "&" + ICostantiEvento.CAMPO_ID_EVENTO + "=" + getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO) );
			}
				setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
		}

		return IWebConstants.PG_MESSAGE;
	}
}