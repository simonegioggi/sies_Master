package siap.siep.misurasicurezza.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.web.ActionSiap;
import siap.siep.decretoordinanza.controller.IDecretoOrdinanzaSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>Title: ActUploadAnnotazioneDecisioneGiudiceCassazione</p>
 * <p>Description:  Validazione del provvedimento di Annotazione e dell'Ordinanza/Decreto  </p>
 * <p>		del Giudice Cassazione/Riesane su faascicoli di Misure di Sicurezza Provvisorie o Fuori sentenza </p>
 */

public class ActUploadAnnotazioneDecisioneGiudiceCassazione extends ActionSiap
implements ICostantiMisuraSicurezza
{
	public String processRequest() throws Exception
	{

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		EventoModel lModel = new EventoModel();
		lModel.setIdEvento( getRequestBigDecimalParameter( ICostantiEvento.CAMPO_ID_EVENTO) );

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

/* 24/11/2014  La validazione della Annotazione decisione della sorveglianza non contempla il checkBox CAMPO_VALIDA  */

		lModel.setFlagDocumentoRegistrato("S");
		IDecretoOrdinanzaSiep lCtrl = SIEPLookupRemote.getDecretoOrdinanzaSiepRemote();
		lCtrl.ExValidaAnnotazioneDecisioneGiudiceCassazioneRiesame(lModel, lFascMod);

		//Prepara la "pagina" di destinAction
		setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Aggiornamento Documento Avvenuto Correttamente!");

		if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_AZIONE_DETTAGLIO))
		{
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			lRedirigi.setAction(getRequestStringParameter(ICostantiEvento.CAMPO_AZIONE_DETTAGLIO) + "&" + ICostantiEvento.CAMPO_ID_EVENTO + "=" + getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO) );
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
		}

		return IWebConstants.PG_MESSAGE;
	}
}
