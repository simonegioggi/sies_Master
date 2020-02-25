package siap.siep.ripristino.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.ripristino.controller.IRipristino;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

public class ActUploadProvvedimentoRipristino extends ActionSiap
												implements ICostantiEvento
{
	public String processRequest() throws Exception
	{
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		this.isFascicoloSiepDiCompetenza();

		EventoModel lModel = new EventoModel();
		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();

		lModel = lCtrlEvento.ExRicercaEventoByKey(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));
		//lModel.setIdEvento(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));

		InputStream lInput = getFile(ICostantiEvento.CAMPO_BLOB);

		if (lInput != null)
		{
			byte[] lBuffer = new byte[lInput.available()];

			lInput.read(lBuffer);
			ByteArrayInputStream lSt = new ByteArrayInputStream(lBuffer);
			lModel.setDocBlobIn(lSt);
		}

		lModel.setDataAggiornamento(DateUtils.getSysDate());
		lModel.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		lModel.setCodOperatoreAggiornamento(getCodUtenteConnesso());

		IRipristino lCtrlRipristino = SIEPLookupRemote.getRipristinoRemote();

		if (isRequestChecked(ICostantiEvento.CAMPO_VALIDA))
		{
			lModel.setFlagDocumentoRegistrato("S");

			lCtrlRipristino.ExUpdateValidaProvvedimentoRipristino(lModel, lFascMod);
		}
		else
		{
			lModel.setFlagDocumentoRegistrato("N");

			lCtrlEvento.ExUpdateDocument(lModel);
		}

		IPosizioneGiuridica lCtrlPosGiu = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		PosizioneGiuridicaModel lPosizione  = lCtrlPosGiu.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lFascMod.getIdFascicoloSiep());

		String lTestoMessaggio = "Aggiornamento Documento Avvenuto Correttamente!";

		String lAzioneRitorno ="";

		if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_AZIONE_DETTAGLIO))
		{
			lAzioneRitorno = getRequestStringParameter(ICostantiEvento.CAMPO_AZIONE_DETTAGLIO);
		}   

		if (lPosizione != null
				&& lPosizione.getCodPosizioneGiuridica() != null
				&& "20".equals(lPosizione.getCodPosizioneGiuridica()) )
		{
			lTestoMessaggio = "Attenzione: è necessario cambiare la posizione giuridica";
			lAzioneRitorno  = "siap.siep.posizione.action.ActLoadInserisciPosizioneGiuridica";
		}

		//Prepara la "pagina" di destinazione
		setRequestAttribute(IWebConstants.MESSAGE_TEXT, lTestoMessaggio);

		if (!lAzioneRitorno.equals(""))
		{

			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			lRedirigi.setAction(lAzioneRitorno + "&" + ICostantiEvento.CAMPO_ID_EVENTO + "=" +
					getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO));
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
		}

		return IWebConstants.PG_MESSAGE;
	}
}