package siap.siep.statis.action;

import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioAccorpatoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.statis.controller.StatisController;
import siap.siep.statis.controller.StatisControllerCPP;
import siap.siep.statis.model.StatoFascicoloResModel;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActLoadCreaRiepilogo_CPP
 * </p>
 * <p>
 * Description: Classe Action per la load ricerca di StatoFascicoloRes
 * </p>
 * <p>
 * per i procedimenti di classe VII (CPP Conversione Pene pecuniarie)
 * </p>
 */
public class ActLoadCreaRiepilogo_CPP extends ActionSiap implements ICostantiStatis {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		String lRetPage = PG_LOAD_CREARIEPILOGO_CPP;

		// Lock
		LockModel lck = LockController.lockIfNotLocked(getServletContext(), "STATISTICA_PROC_PENDENTI_CPP",
				"1", getCodUtenteConnesso(), getSession().getId());

		if (lck != null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Questa funzione non può essere attivata contemporaneamente da più utenti !<BR>Riprovare più tardi !");
			return IWebConstants.PG_MESSAGE;
		}

		String sysDate = DateUtils.getSysDate("dd/MM/yyyy");
		setRequestAttribute("sysdate", sysDate);

		setRequestAttribute("ggIni", getRequestStringParameter(CAMPO_GIORNO_INIZIALE));
		setRequestAttribute("mmIni", getRequestStringParameter(CAMPO_MESE_INIZIALE));
		setRequestAttribute("aaIni", getRequestStringParameter(CAMPO_ANNO_INIZIALE));
		setRequestAttribute("ggFin", getRequestStringParameter(CAMPO_GIORNO_FINALE));
		setRequestAttribute("mmFin", getRequestStringParameter(CAMPO_MESE_FINALE));
		setRequestAttribute("aaFin", getRequestStringParameter(CAMPO_ANNO_FINALE));
		setRequestAttribute("soloaaIni", getRequestStringParameter(CAMPO_SOLO_ANNO_INIZIALE));
		setRequestAttribute("soloaaFin", getRequestStringParameter(CAMPO_SOLO_ANNO_FINALE));

		if (isRequestParameterNullObj("vai")) {
			setRequestAttribute("UffScelto", getRequestStringParameter(CAMPO_COD_ACCORPATO_1));
			// Pagina di attesa (rotelline)
			lRetPage = PG_ATTESA;
			setRequestAttribute("titolo", "STATISTICHE UFFICIO - RIEPILOGO PROCEDIMENTI PENDENTI");
			setRequestAttribute("next_action", getClass().getName() /* "siap.siep.statis.action.ActLoadCreaRiepilogo_CPP" */);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Attesa :" + getClass().getName());
		} else {
			// RECUPERO SYSDATE
			String dataVerifica = DateUtils.getSysDate("dd/MM/yyyy");
			// NGG recupero Ufficio Accorpante o Ufficio Accorpato, a seconda della selezione effettuata a
			// monte
			UtenteModel lUtenteMod = new UtenteModel((UtenteModel) getSession().getAttribute(
					ICostantiSecurity.SESSION_UTENTE_CONNESSO));
			String lUffUtente = lUtenteMod.getUfficioUtente().getCodUfficio();
			String lTipoUffUte = lUtenteMod.getUfficioUtente().getCodTipoUfficio();

			IUfficio lCtrlUff = SICOLookupRemote.getUfficioRemote();
			Vector lListaUffici = lCtrlUff.ListaUfficiAccorpati(lTipoUffUte, lUffUtente);

			UfficioModel UffMod = new UfficioModel();
			UffMod.setUfficiAccorpati(lListaUffici);
			UffMod.setCodTipoUfficio(lTipoUffUte);
			UffMod.setCodUfficio(lUffUtente);
			UffMod.setDescrComune(lUtenteMod.getUfficioUtente().getDescrComune());

			setRequestAttribute("ufficiomod", UffMod);
			setRequestAttribute("ListaUffAcc", lListaUffici);
			setRequestAttribute("UffScelto", getRequestStringParameter(CAMPO_COD_ACCORPATO_1));

			String lCodAcco = getRequestStringParameter(CAMPO_COD_ACCORPATO_1);

			String Accorpato1 = "-";
			String Accorpato2 = "-";
			String Accorpato3 = "-";
			String UfficioConnesso = "";

			Vector<String> lVec = new Vector<String>();
			int i = 0;
			if (lListaUffici.size() == 0) {

			} else {
				Iterator itx = lListaUffici.iterator();
				while (itx.hasNext()) {
					UfficioAccorpatoModel lUff = (UfficioAccorpatoModel) itx.next();
					String uff1 = lUff.getCodUfficio();
					lVec.add(i, uff1);
					i++;
				}
			}

			if (lCodAcco.equals("-")) {
				UfficioConnesso = this.getCodUfficioUtenteConnesso();
				if (i == 1) {
					Accorpato1 = lVec.get(0);
				}
				if (i == 2) {
					Accorpato1 = lVec.get(0);
					Accorpato2 = lVec.get(1);
				}
				if (i == 3) {
					Accorpato1 = lVec.get(0);
					Accorpato2 = lVec.get(1);
					Accorpato3 = lVec.get(2);
				}
			} else {
				UfficioConnesso = lCodAcco;
			}
			// END NGG

			// RICHIAMO STORED PROCEDURE --
			StatisController lCtrl = new StatisController();

			// RICHIAMO STORED PROCEDURE_CPP --
			StatisControllerCPP lCtrlC = new StatisControllerCPP();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger
					.debug("--XX-- ActLoadCreaRiepilogo_CPP RICHIAMO STORE PROCEDURE_CPP - StatisControllerCPP");
			lCtrlC.ExStatProvvedimenti_CPP_StoredProcedure(UfficioConnesso, Accorpato1, Accorpato2,
					Accorpato3, dataVerifica);

			// RICERCA PER RIEMPIRE LA COMBOBOX
			StatoFascicoloResModel lSfrMod = new StatoFascicoloResModel();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("--XX-- ActLoadCreaRiepilogo_CPP - ExRicercaStatoFascicoloRes");
			Vector lSfrModVect = lCtrl.ExRicercaStatoFascicoloRes(lSfrMod);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger
					.debug("--XX-- ActLoadCreaRiepilogo_CPP - ExRicercaStatoFascicoloRes eseguita - size =>"
							+ lSfrModVect.size() + "<");
			setRequestAttribute("statifascicolo", lSfrModVect);

		}
		return lRetPage; // restituisce la jsp di VIEW
	}

}