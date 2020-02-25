package siap.siep.reatopredisposto.action;

import java.util.Iterator;
import java.util.Vector;

import f3b.util.F3BException;
import f3b.util.StringUtils;
import siap.sico.web.ActionSiap;
import siap.siep.reatopredisposto.controller.ReatoPredispostoController;
import siap.siep.reatopredisposto.model.ReatoPredispostoModel;

/**
 * <p>
 * Title: ActRicercaReatoPredisposto
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di ReatoPredisposto
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
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActRicercaReatoPredisposto extends ActionSiap implements ICostantiReatoPredisposto {

	public String processRequest() throws F3BException {

		ReatoPredispostoModel lReaMod = new ReatoPredispostoModel();

		// if(!isRequestParameterNullObj(CAMPO_NOME_ELEMENTO) )
		// lReaMod.setNomeElemento( getRequestStringParameter( CAMPO_NOME_ELEMENTO) );

		lReaMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());

		// IReatoPredisposto lCtrl = SIEPLookupRemote.getReatoPredispostoRemote();
		ReatoPredispostoController lCtrl = new ReatoPredispostoController();
		Vector lVect = lCtrl.ExRicercaReatoPredisposto(lReaMod);

		setRequestAttribute("reatopredisposto", lVect);

		if (isRequestParameterNullObj("formname")) {
			return PG_RICERCAREATOPREDISPOSTO;
		} else {
			setRequestAttribute("stringacampi", getPopupVector(lVect));
			return PG_LOAD_POPUPREATIPREDISPOSTI;
		}
	}

	private Vector getPopupVector(Vector inVect) {

		Vector outVect = new Vector();
		String str = new String("");

		Iterator itx = inVect.iterator();
		while (itx.hasNext()) {

			ReatoPredispostoModel lReato = (ReatoPredispostoModel) itx.next();

			if (lReato.getProgrNorma().intValue() == 1) {

				if (str.length() != 0) {

					str = str.substring(0, str.length() - SEP_NORME.length());
					outVect.addElement(str);
					str = "";
				}
			}

			str += lReato.getCodFonte();
			str += SEP_CAMPI + StringUtils.toStringJSP(lReato.getAnnoFonte());
			str += SEP_CAMPI + StringUtils.toStringJSP(lReato.getNumeroFonte());
			str += SEP_CAMPI + StringUtils.toStringJSP(lReato.getArticolo());
			str += SEP_CAMPI + lReato.getCodSottonumerazione();
			str += SEP_CAMPI + StringUtils.toStringJSP(lReato.getComma());
			str += SEP_CAMPI + StringUtils.toStringJSP(lReato.getLettera());
			str += SEP_CAMPI + StringUtils.toStringJSP(lReato.getNumero());
			str += SEP_NORME;
		}

		str = str.substring(0, str.length() - SEP_NORME.length());
		outVect.addElement(str);

		return outVect;
	}

}