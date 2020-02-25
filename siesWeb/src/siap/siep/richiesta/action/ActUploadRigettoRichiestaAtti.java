package siap.siep.richiesta.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.competenza.controller.ICompetenza;
import siap.siep.competenza.model.CompetenzaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.richiesta.controller.IRichiesta;

/**
 * <p>Title: ActUploadRigettoRichiestaAtti</p>
 * <p>Description: Classe Action per la Validazione della Comunicazione </p>
 * <p> di RIGETTO RICHIESTA Trasmissione Atti per Competenza			</p>
 */
public class ActUploadRigettoRichiestaAtti extends ActionSiap
implements ICostantiRichiesta
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

		// Competenza
		 ICompetenza lCompCtrl = SIEPLookupRemote.getCompetenzaRemote();	
		 CompetenzaModel mComp = lCompCtrl.ExRicercaCompetenzaByEveIdEvento(getRequestBigDecimalParameter( ICostantiEvento.CAMPO_ID_EVENTO));     
		//
		
		if(isRequestChecked( ICostantiEvento.CAMPO_VALIDA) )
		{
			lModel.setFlagDocumentoRegistrato("S");
			IRichiesta lCtrlRich = SIEPLookupRemote.getRichiestaRemote();

			lCtrlRich.ExValidaRigettoRichiestaAttiPerCompetenza(lModel,lFascMod,mComp);
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
			lRedirigi.setAction(getRequestStringParameter(ICostantiEvento.CAMPO_AZIONE_DETTAGLIO) + "&" + ICostantiEvento.CAMPO_ID_EVENTO + "=" + getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO));
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
		}

		return IWebConstants.PG_MESSAGE;
	}
}
