package siap.sico.soggettodattilo.action;

import java.util.Vector;

import siap.sico.soggettodattilo.controller.ISoggettoDattilo;
import siap.sico.soggettodattilo.model.SoggettoDattiloModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActRicercaSoggettoDattilo
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di SoggettoDattilo
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ActRicercaSoggettoDattilo extends ActionSiap implements ICostantiSoggettoDattilo {

	public String processRequest() throws F3BException {

		SoggettoDattiloModel lSogMod = new SoggettoDattiloModel();

		lSogMod.setIdDattilo(getRequestBigDecimalParameter(CAMPO_ID_DATTILO));
		lSogMod.setCodSoggetto(getRequestBigDecimalParameter(CAMPO_COD_SOGGETTO));
		lSogMod.setDocTipo(getRequestStringParameter(CAMPO_DOC_TIPO));
		lSogMod.setDocNome(getRequestStringParameter(CAMPO_DOC_NOME));
		lSogMod.setDataInserimento(getRequestDateParameter(CAMPO_ANNO_DATA_INSERIMENTO,
				CAMPO_MESE_DATA_INSERIMENTO, CAMPO_GIORNO_DATA_INSERIMENTO));
		lSogMod.setCodOperatoreInserimento(getRequestStringParameter(CAMPO_COD_OPERATORE_INSERIMENTO));
		lSogMod.setCodUfficioInserimento(getRequestStringParameter(CAMPO_COD_UFFICIO_INSERIMENTO));

		ISoggettoDattilo lCtrl = SICOLookupRemote.getSoggettoDattiloRemote();
		Vector lVect = lCtrl.ExRicercaSoggettoDattilo(lSogMod);
		setRequestAttribute("soggettodattilo", lVect);

		return PG_RICERCASOGGETTODATTILO;
	}

}