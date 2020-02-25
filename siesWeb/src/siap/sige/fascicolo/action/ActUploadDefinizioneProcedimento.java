package siap.sige.fascicolo.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
* <p>Title: ActUploadDefinizioneProcedimento </p>
* <p>Description: Classe Action per validare una Definizione Procedimento</p>
* <p>Company: Engineering S.p.A.</p>
* @version 1.0
*/
public class ActUploadDefinizioneProcedimento extends ActionSige implements ICostantiEvento {

	  public String processRequest() throws Exception
	  {

		FascicoloSigeEstesoModel mFasEsteso = this.getFascicoloSigeEstesoInSessione();
		EventoModel lModel = new EventoModel();
	    lModel.setIdEvento(getRequestBigDecimalParameter(CAMPO_ID_EVENTO));

	    InputStream lInput= null;
	    if(this.isRequestParameterNullObj("noblob"))
	    {
	      lInput = getFile(ICostantiEvento.CAMPO_BLOB);
	    }

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

	    mFasEsteso.getFascicoloSige().setCodStatoFascicolo( ICostantiFascicoloSige.COD_DEFINITO );
	    mFasEsteso.getFascicoloSige().setCodOperatoreAggiornamento(getCodUtenteConnesso());
	    mFasEsteso.getFascicoloSige().setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
	    mFasEsteso.getFascicoloSige().setDataAggiornamento(DateUtils.getSysDate());
	    
	    if (isRequestChecked(ICostantiEvento.CAMPO_VALIDA))
	    {
	      lModel.setFlagDocumentoRegistrato("S");
	      IFascicoloSige lCtrl = SIGELookupRemote.getFascicoloSigeRemote();
	      lCtrl.ExUpdateValidaDefinizioneProcedimento(lModel, mFasEsteso.getFascicoloSige());
	    }
	    else
	    {
	      lModel.setFlagDocumentoRegistrato("N");
	      IEvento lCtrl = SICOLookupRemote.getEventoRemote();
	      lCtrl.ExUpdateDocument(lModel);
	    }

	    //Prepara la "pagina" di destinAction
	    setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Validazione Avvenuta Correttamente!");

	    // dopo aver validato l'evento e quindi archiviato il fascicolo si ha bisogno di
	    // una nuova ricerca del fascicolo per settare il nuovo model del fascicolo in sessione
	    IFascicoloSige lCtrlFasc = SIGELookupRemote.getFascicoloSigeRemote();
	    FascicoloSigeEstesoModel lFascicoloMod = lCtrlFasc.ExRicercaEstesaFascicoloSigeByKey(mFasEsteso.getFascicoloSige().getIdFascicoloSige());
	    this.setSessionAttribute("FascicoloSigeEsteso",lFascicoloMod);

	    if (!isRequestParameterNullObj(CAMPO_AZIONE_DETTAGLIO))
	    {
	      RedirectTo lRedirigi = new RedirectTo();
	      lRedirigi.setPage(IWebConstants.PG_MAIN);
	      lRedirigi.setAction(getRequestStringParameter(CAMPO_AZIONE_DETTAGLIO));
	      setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
	    }

	    return IWebConstants.PG_MESSAGE;
	  }

}