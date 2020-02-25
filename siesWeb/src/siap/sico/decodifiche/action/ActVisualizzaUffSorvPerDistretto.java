package siap.sico.decodifiche.action;

import java.util.Vector;

import siap.sico.ufficio.controller.IUfficio;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
import f3b.util.StringUtils;

@SuppressWarnings("rawtypes")
public class ActVisualizzaUffSorvPerDistretto extends ActionSiap implements ICostantiComune {

	public String processRequest() throws F3BException {

		IUfficio lComCtrel = SICOLookupRemote.getUfficioRemote();
		String codTipoUff = this.getRequestStringParameter("codTipoUff");
		String lDescrComune = StringUtils.convertSqlString(getRequestStringParameter("comune"));
		Vector lCom = lComCtrel.ExGetListaComuniUfficiPerDistretto(
				this.getRequestStringParameter("codicedistretto"), lDescrComune.toUpperCase(), codTipoUff);

		setRequestAttribute("comune", lDescrComune.toUpperCase());

		setRequestAttribute("Lista", lCom);

		return PG_RICERCACOMUNE_TIPOUFF_PER_DISTRETTO;
	}

}