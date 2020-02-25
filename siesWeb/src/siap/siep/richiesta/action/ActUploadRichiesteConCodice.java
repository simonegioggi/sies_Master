package siap.siep.richiesta.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
//import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.calcolopena.action.ActCalcoloPenaMain;
import siap.siep.calcolopena.model.CalcoloPenaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.richiesta.controller.IRichiesta;
import siap.siep.util.SIEPLookupRemote;
//import f3b.log.LogF3B;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;


/**
 * <p>Title: ActUploadRichiesteConCodice</p>
 * <p>Description: Classe Action per l'upload o la validazione delle richieste
 *    al GE di Amnistia/Indulto</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */
public class ActUploadRichiesteConCodice extends ActionSiap
implements ICostantiEvento {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	public String processRequest() throws Exception {
	  
	  // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	  siesLogger.debug(getClass().getName() + ".processRequest : inizio");

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		//==========================================================================
		// Recupero l'evento da aggiornare
		//==========================================================================
		EventoModel lModel = new EventoModel();
		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		lModel = lCtrlEvento.ExRicercaEventoByKey(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));
		lModel.setIdEvento(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));

		//========================================================================
		// Recupero l'evento al quale è agganciata l'annotazione manuale
		//========================================================================
		EventoModel lEveModRic = null;
		if(lModel != null ) {
			IAnnotazioneManuale lCtrlAnn = SIEPLookupRemote.getAnnotazioneManualeRemote();
			AnnotazioneManualeModel lAnnManMod = lCtrlAnn.ExRicercaAnnotazioneManualeByKey(lModel.getAnnIdAnnotazioneManuale());

			if(lAnnManMod != null) {
				lEveModRic = lCtrlEvento.ExRicercaEventoByKey(lAnnManMod.getEveIdEvento());

				if(lEveModRic != null && lEveModRic.getAnnIdAnnotazioneManuale() != null) {
					lEveModRic = null;
				}

				if( lEveModRic != null) {
					lEveModRic.setDataAggiornamento         (DateUtils.getSysDate());
					lEveModRic.setCodUfficioAggiornamento   (getCodUfficioUtenteConnesso());
					lEveModRic.setCodOperatoreAggiornamento (getCodUtenteConnesso());
				}
			}
		}

		// Recupero il documento
		InputStream lInput = getFile(ICostantiEvento.CAMPO_BLOB);
		if (lInput != null) {
			byte[] lBuffer = new byte[lInput.available()];

			lInput.read(lBuffer);
			ByteArrayInputStream lSt = new ByteArrayInputStream(lBuffer);
			lModel.setDocBlobIn(lSt);
		}

		lModel.setDataAggiornamento(DateUtils.getSysDate());
		lModel.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		lModel.setCodOperatoreAggiornamento(getCodUtenteConnesso());

		String codice = lModel.getCodMotivo();
		String lCodiceNomProvv = null;

		int codiceFlag = Integer.parseInt(codice);

		switch (codiceFlag)
		{
		case 296:
			lCodiceNomProvv = "NP088";
			break;
		case 294:
			lCodiceNomProvv = "NP084";

			break;
		case  290:
			lCodiceNomProvv = "NP081";

			break;
		case 288:
			lCodiceNomProvv = "NP085";

			break;
		case 287:
			lCodiceNomProvv = "NP086";

			break;
		case 291:
			lCodiceNomProvv = "NP079";

			break;
		case 292:
			lCodiceNomProvv = "NP080";

			break;
		}

		IRichiesta lCtrlRich = SIEPLookupRemote.getRichiestaRemote();

    //==========================================================================
    // Recupero le LA da passare alla funzione di validazione in caso di 
    // Interruzione della pena per Scarcerazione Provvisoria Indulto 
    //==========================================================================
    BigDecimal lGiorniLA = null;
    if (   (   lModel.getCodTipoEvento().equals("01")
            && lModel.getCodTipoProvvedimento().equals("09")   // Ordine di scarcerazione
            && lModel.getCodMotivo().equals("0367")            // Provvisorio per concessione Indulto
           )
        || (   lModel.getCodTipoEvento().equals("01")
            && lModel.getCodTipoProvvedimento().equals("26") // Richiesta
            && lModel.getCodMotivo().equals("0290")          // Applicazione Benefici - ex art. 174 c.p. e 672 c.p.p.
           )
       )
    {
      ActCalcoloPenaMain lActCalcoloPenaMain = new ActCalcoloPenaMain();
      CalcoloPenaModel lCalcPenaModel = lActCalcoloPenaMain.calcoloPena(lFascMod.getIdFascicoloSiep(),null);
      lGiorniLA = new BigDecimal (lCalcPenaModel.getLiberazioneAnticipata());
    }

    
    
		if (isRequestChecked(ICostantiEvento.CAMPO_VALIDA)){
		  lModel.setFlagDocumentoRegistrato("S");			
			lCtrlRich.ExUpdateValidaRichiesteConCodice(lEveModRic,lModel,lCodiceNomProvv,lFascMod, lGiorniLA);
		}else{
			lModel.setFlagDocumentoRegistrato("N");
			IEvento lCtrl = SICOLookupRemote.getEventoRemote();
			lCtrl.ExUpdateDocument(lModel);
		}

//		Prepara la "pagina" di destinAction
		setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Aggiornamento Documento Avvenuto Correttamente!");

		if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_AZIONE_DETTAGLIO)){
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			lRedirigi.setAction(getRequestStringParameter(ICostantiEvento.CAMPO_AZIONE_DETTAGLIO) + "&" + ICostantiEvento.CAMPO_ID_EVENTO + "=" +
					getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO));
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
		}
		
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : inizio");
		
		return IWebConstants.PG_MESSAGE;
	}
}