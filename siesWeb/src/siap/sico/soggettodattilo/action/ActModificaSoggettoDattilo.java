package siap.sico.soggettodattilo.action;

import java.math.BigDecimal;

import siap.sico.soggettodattilo.controller.ISoggettoDattilo;
import siap.sico.soggettodattilo.model.SoggettoDattiloModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
* <p>Title: ActModificaSoggettoDattilo</p>
* <p>Description: Classe Action per la modifica di SoggettoDattilo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class ActModificaSoggettoDattilo extends ActionSiap implements ICostantiSoggettoDattilo {

	/**
	* Azione di Modifica del SoggettoDattilo
	* @return Nome della pagina JSP da visualizzare
	* al termine dell'elaborazione
	* @throws F3BException
	*/
	public String processRequest() throws F3BException {

		String lId = getRequestStringParameter(CAMPO_ID_DATTILO);
		// riempie il model
		SoggettoDattiloModel lSogMod = new SoggettoDattiloModel();

		lSogMod.setIdDattilo(new BigDecimal(lId));
		lSogMod.setIdDattilo(getRequestBigDecimalParameter(CAMPO_ID_DATTILO));
		lSogMod.setCodSoggetto(getRequestBigDecimalParameter(CAMPO_COD_SOGGETTO));
		lSogMod.setDocTipo(getRequestStringParameter(CAMPO_DOC_TIPO));
		lSogMod.setDocNome(getRequestStringParameter(CAMPO_DOC_NOME));
		lSogMod.setDataInserimento(getRequestDateParameter(CAMPO_ANNO_DATA_INSERIMENTO, CAMPO_MESE_DATA_INSERIMENTO, CAMPO_GIORNO_DATA_INSERIMENTO));
		// lSogMod.setDocBlob(getRequestBlobParameter(CAMPO_DOC_BLOB));

		// chiama il controller
		ISoggettoDattilo lCtrl = SICOLookupRemote.getSoggettoDattiloRemote();
		SoggettoDattiloModel llSogModRet = lCtrl.ExModificaSoggettoDattilo(lSogMod);

		setRequestAttribute("modalita", "M");
		setRequestAttribute("soggettodattilo", llSogModRet);

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sico.soggettodattilo.action.ActLoadDettaglioSoggettoDattilo&" + CAMPO_ID_DATTILO + "=" + llSogModRet.getIdDattilo().toString();
		return lPage;
	}

}