package siap.siep.sospensione.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.sospensione.controller.ISospensione;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>Title: ActUploadSospensionePena</p>
 * <p>Description: Classe Action per l' Upload  di Sospensione Pena</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public class ActUploadSospensionePena extends ActionSiap
										implements ICostantiEvento
{
	public String processRequest() throws Exception
	{

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		EventoModel lModel = new EventoModel();
		lModel.setIdEvento( getRequestBigDecimalParameter( CAMPO_ID_EVENTO) );

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
		
		IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
		
		if(isRequestChecked( ICostantiEvento.CAMPO_VALIDA) )
		{
			
			// paolo cherubini 6 giugno 2010 
			// solo se l'evento è collegato ad un precedente evento "0840" 
			// ossia "SOSPENSIONE ESECUZIONE DELLA PENA DETENTIVA EX ARTT. 90 E 91 DPR 309/90","0840", "G019"
			// metto la posizione giuridica 47 
			
			String lPosGiu = "46"; // posizione giuridica libero in sospensione
			
			if (lModel.getIdEvento() != null)
			{		
				EventoModel lEveMod = new EventoModel();
				lEveMod = lCtrlEve.ExRicercaEventoByKey(lModel.getIdEvento());
				if (lEveMod != null && lEveMod.getEveIdEvento() != null)
				{				
					lEveMod = lCtrlEve.ExRicercaEventoByKey(lEveMod.getEveIdEvento());
					if (lEveMod.getCodMotivo().equals("0840"))
					{
						lPosGiu = "47"; // posizione giuridica libero in sospensione dpr 309/90
					}
				}
			}
			lModel.setFlagDocumentoRegistrato("S");
			ISospensione lCtrlSosp = SIEPLookupRemote.getSospensioneRemote();
			lCtrlSosp.ExUpdateValidaSospensione(lModel, lFascMod,lPosGiu,"NP106","0084");
		}
		else
		{
			lModel.setFlagDocumentoRegistrato("N");
			lCtrlEve.ExUpdateDocument(lModel);
		}

		//Prepara la "pagina" di destinazione
		setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Aggiornamento Documento Avvenuto Correttamente!");

		if (!isRequestParameterNullObj(CAMPO_AZIONE_DETTAGLIO))
		{
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			lRedirigi.setAction(getRequestStringParameter(CAMPO_AZIONE_DETTAGLIO) + "&" + CAMPO_ID_EVENTO + "=" + getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO));
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
		}

		return IWebConstants.PG_MESSAGE;
	}
}
