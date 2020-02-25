package siap.sico.utente.action;

import java.util.Collection;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.web.ActionSiap;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 *
 * @author unascribed
 * @version 1.0
 */
public class ActLoadRicercaUtentiPerUfficio extends ActionSiap {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		Collection lTipiUffici = DecodificheManager.getInstance().getTipoUfficio();
		Option lOption = null;

		// Verifica che l'utente sia un super Admin ossia che abbia profilo 99
		// if( getUtenteConnesso().getUserProfile().getProfileId().compareTo(
		// ProfiloModel.COD_PROFILO_SYS_ADMIN ) == 0
		// )
		if (getUtenteConnesso().isSysAdmin()) {
			// MEV10-s3: elimino un elemento dalla lista vettoriale: "Tribunale per i minorenni"
			// Iterator it = lTipiUffici.iterator();
			// SU SEGNALAZIONE DI N.A. gli amministatori devono vedere tutti i tipi uffici,
			// così come era già stato fatto in ActLoadInserisciUtente
			// while (it.hasNext()) {
			// DecodificheModel dm = (DecodificheModel) it.next();
			// if ("DIBM".equals(dm.getCode()))
			// it.remove();
			// }
			lOption = new Option(lTipiUffici); // Crea comboBox per tutti gli uffici
			setRequestAttribute("isSysAdmin", "S");
		} else {
			// Preleva dalla sessione il codice tipo uffcio, per il quale filtrare la lista.
			String[] lCodTipoUfficioFilter = { getUfficioUtenteConnesso().getCodTipoUfficio() };
			// Crea combobox solo per l'ufficio interessato.
			lOption = new Option(DecodificheUtils.getDecodesWithCodes(lTipiUffici, lCodTipoUfficioFilter));
			// Preleva la sede dell'uffcio dell'utente connesso.
			setRequestAttribute("sedeUffcio", getUfficioUtenteConnesso().getDescrComune());
		}

		setRequestAttribute("tipoUfficio", "" + lOption);

		return IWebConstants.ROOT_DIR + "files/siap/sico/utente/LoadRicercaUtentiPerUfficio.jsp";
	}

}