package siap.siep.statis.action;

/**
* <p>Title: ActLoadTempiEmissione</p>
* <p>Description: Classe Action per la load della ricerca per tempi Emissione Provvedimenti</p>
* <p>Copyright: Copyright (c) 2006</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.util.Vector;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.statis.controller.MagistratoFirmatarioController;

public class ActLoadTempiEmissione extends ActionSiap implements ICostantiStatis {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		// Lock
		LockModel lck = LockController.lockIfNotLocked(getServletContext(), "TEMPI_EMISSIONE_PROVVEDIMENTI",
				"1", getCodUtenteConnesso(), getSession().getId());

		if (lck != null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Questa funzione non può essere attivata contemporaneamente da più utenti !<BR>Riprovare più tardi !");
			return IWebConstants.PG_MESSAGE;
		}
		// NGG - Statistiche SIEP - Recupero Ufficio Utente
		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSession().getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		String lUffUtente = lUtenteMod.getUfficioUtente().getCodUfficio();
		String lTipoUffUte = lUtenteMod.getUfficioUtente().getCodTipoUfficio();
		// recupero eventuali uffici accorpati
		IUfficio lCtrlUf = SICOLookupRemote.getUfficioRemote();
		Vector lListaUffici = lCtrlUf.ListaUfficiAccorpati(lTipoUffUte, lUffUtente);

		UfficioModel UffMod = new UfficioModel();
		UffMod.setUfficiAccorpati(lListaUffici);
		UffMod.setCodTipoUfficio(lTipoUffUte);
		UffMod.setCodUfficio(lUffUtente);

		setRequestAttribute("ufficiomod", UffMod);
		setRequestAttribute("ListaUffAcc", lListaUffici);
		// END NGG

		// Entro nella if se è SECONDO GIRO
		if (!this.isRequestParameterNullObj(CAMPO_ANNO_INIZIALE)) {
			int annoIni = getRequestIntParameter(CAMPO_ANNO_INIZIALE);
			setRequestAttribute("AnnoI", getRequestStringParameter(CAMPO_ANNO_INIZIALE));
			String meseIni = getRequestStringParameter(CAMPO_MESE_INIZIALE);
			setRequestAttribute("MeseI", meseIni);
			String giornoIni = getRequestStringParameter(CAMPO_GIORNO_INIZIALE);
			setRequestAttribute("GiornoI", giornoIni);

			int annoFin = getRequestIntParameter(CAMPO_ANNO_FINALE);
			setRequestAttribute("AnnoF", getRequestStringParameter(CAMPO_ANNO_FINALE));
			String meseFin = getRequestStringParameter(CAMPO_MESE_FINALE);
			setRequestAttribute("MeseF", meseFin);
			String giornoFin = getRequestStringParameter(CAMPO_GIORNO_FINALE);
			setRequestAttribute("GiornoF", giornoFin);

			String dataIni = giornoIni + "/" + meseIni + "/" + annoIni;
			String dataFin = giornoFin + "/" + meseFin + "/" + annoFin;
			// NGG - Statistiche SIEP
			String Coduff = getRequestStringParameter(CAMPO_COD_ACCORPATO_1);
			if (Coduff.equals("-"))
				Coduff = this.getCodUfficioUtenteConnesso();
			// setRequestAttribute("UffScelto", Coduff);
			setRequestAttribute("UffScelto", getRequestStringParameter(CAMPO_COD_ACCORPATO_1));
			// Magistrato CONTROLLER
			MagistratoFirmatarioController lCtrl = new MagistratoFirmatarioController();

			// RICERCA PER RIEMPIRE LA COMBOBOX
			Vector lMagModVect = lCtrl.ExRicercaMagistratiFirmatari(Coduff, dataIni, dataFin);

			setRequestAttribute("magistrati", lMagModVect);
			setRequestAttribute("SecondoGiro", "SI");

		} else {
			setRequestAttribute("SecondoGiro", "NO");
		}

		// RECUPERO SYSDATE
		String sysDate = DateUtils.getSysDate("dd/MM/yyyy");
		setRequestAttribute("sysdate", sysDate);
		return PG_LOAD_TEMPIEMISSIONE; // restituisce la jsp di VIEW
	}

}