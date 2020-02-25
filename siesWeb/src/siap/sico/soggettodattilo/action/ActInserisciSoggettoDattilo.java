package siap.sico.soggettodattilo.action;

import siap.sico.security.action.ICostantiSecurity;
import siap.sico.soggettodattilo.controller.ISoggettoDattilo;
import siap.sico.soggettodattilo.model.SoggettoDattiloModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
* <p>Title: ActInserisciSoggettoDattilo</p>
* <p>Description: Classe Action per l'inserimento di SoggettoDattilo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class ActInserisciSoggettoDattilo extends ActionSiap implements ICostantiSoggettoDattilo {

	/**
	* Azione di Inserimento del SoggettoDattilo
	* @return Nome della pagina JSP da visualizzare
	* al termine dell'elaborazione
	* @throws F3BException
	*/
	public String processRequest() throws F3BException {
		SoggettoDattiloModel lSogMod = new SoggettoDattiloModel();

		lSogMod.setIdDattilo(getRequestBigDecimalParameter(CAMPO_ID_DATTILO));
		lSogMod.setCodSoggetto(getRequestBigDecimalParameter(CAMPO_COD_SOGGETTO));
		lSogMod.setDocTipo(getRequestStringParameter(CAMPO_DOC_TIPO));
		lSogMod.setDocNome(getRequestStringParameter(CAMPO_DOC_NOME));
		lSogMod.setDataInserimento(DateUtils.getSysDate());
		UtenteModel lUtenteMod = new UtenteModel((UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		lSogMod.setCodOperatoreInserimento(lUtenteMod.getUserId());
		lSogMod.setCodUfficioInserimento(lUtenteMod.getUfficioUtente().getCodUfficio());
		lSogMod.setDataInserimento(DateUtils.getSysDate());

		ISoggettoDattilo lCtrl = SICOLookupRemote.getSoggettoDattiloRemote();
		SoggettoDattiloModel llSogModRet = lCtrl.ExInserisciSoggettoDattilo(lSogMod); // setta la risposta nella request
		setRequestAttribute("soggettodattilo", llSogModRet);

		// Prepara la pagina di destinazione
		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sico.soggettodattilo.action.ActLoadDettaglioSoggettoDattilo&" + CAMPO_ID_DATTILO + "=" + llSogModRet.getIdDattilo().toString();
		return lPage;
	}

}