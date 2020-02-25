package siap.siep.certificatostatoesecuzione.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import siap.sico.web.ActionSiap;
import siap.siep.certificatostatoesecuzione.controller.ICertificatoStatoEsec;
import siap.siep.certificatostatoesecuzione.model.CertificatoStatoEsecModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>Title: ActUploadCertSE</p>
 * <p>Description: salvataggio del certificato di stato esecuzione</p> * 
 * 
 */
public class ActUploadCertSE extends ActionSiap
							implements ICostantiCertificatoStatoEsec
{
	public String processRequest() throws Exception
	{
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		CertificatoStatoEsecModel lModel = new CertificatoStatoEsecModel();
		//lModel.setIdCertificatoStatoEsec(getRequestBigDecimalParameter( CAMPO_ID_CERTIFICATO_STATO_ESEC));

		InputStream lInput = getFile(ICostantiCertificatoStatoEsec.CAMPO_BLOB);

		if(lInput != null)
		{
			byte[] lBuffer = new byte[lInput.available()];

			lInput.read(lBuffer);
			ByteArrayInputStream lSt = new ByteArrayInputStream(lBuffer);
			lModel.setDocBlobIn(lSt);			
		}

		lModel.setDataInserimento(DateUtils.getSysDate());
		//COD_UFFICIO_INSERIMENTO
		lModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso() );
		//COD_OPERATORE_INSERIMENTO
		lModel.setCodOperatoreInserimento(getCodUtenteConnesso() );
		//ANNOTAZIONI
		lModel.setAnnotazioni(getRequestStringParameter(CAMPO_ANNOTAZIONI));
		//FLAG_UPLOAD
		lModel.setFlagUpload(getRequestStringParameter(CAMPO_FLAG_UPLOAD));
		//ID FASCICOLO SIEP
		lModel.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
				
		//chiamo il controller per effettuare l'insert	

		ICertificatoStatoEsec lCtrl = SIEPLookupRemote.getCertificatoStatoEsecRemote();
		//effettuo l'insert
		lCtrl.ExInserisciCertificatoStatoEsec(lModel);
		
		
		//Prepara la "pagina" di destinazione
		setRequestAttribute(IWebConstants.MESSAGE_TEXT, "L'inserimento del Certificato è Avvenuto Correttamente!");

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
