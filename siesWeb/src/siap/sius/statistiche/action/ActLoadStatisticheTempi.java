package siap.sius.statistiche.action;

/**
* <p>Title: ActLoadTempiEmissione</p>
* <p>Description: Action adibita al caricamento della form di Statistiche su
* "Movimenti Provvedimenti distinti per oggetti".
* <p>Copyright: Copyright (c) 2007</p>
* <p>Company: Eunics</p>
* @version 3.0
*/

import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * Azione che apre la maschera di Statistiche Numero Procedimenti per intervalli di tempo.
 * 
 * @author Lesposito
 *
 */
public class ActLoadStatisticheTempi extends ActLoadStatisticheOggetti {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		// Lock
		// LockModel lck =
		// LockController.lockIfNotLocked(getServletContext(),"STATISTICHE_TEMPI","1",getCodUtenteConnesso(),getSession().getId());
		// mod. michele 5/12/2008
		LockModel lck = LockController.lockIfNotLocked(getServletContext(), "STATISTICHE",
				getCodUfficioUtenteConnesso(), getCodUtenteConnesso(), getSession().getId());

		if (lck != null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Questa funzione non può essere attivata contemporaneamente da più utenti !<BR>Riprovare più tardi !");
			return IWebConstants.PG_MESSAGE;
		}

		if (!isRequestParameterNullObj(CAMPO_ANNO_INIZIALE)) {
			// Lettura Intervallo date
			letturaDate();

			// lettura eventuale Cancelleria Assegnataria di Filtro
			String lCodCancelleria = ricercaCancelleriaAssegnataria();

			// lettura eventuale filtro su Collaboratore di Giustizia
			String lFiltroCollab = getFiltroCollaboratore();

			// lettura eventuale filtro su Posizione Giuridica
			String lPosizioneGiuridica = getPosizioneGiuridica();

			// Controller per l'attivazione della STORED PROCEDURE adibita alla
			// creazione delle Statistiche.
			mCtrl = SIUSLookupRemote.getStatisticheSiusRemote();
			mCtrl.ExEstraiProcedimentiDepositatiStProc(getCodUfficioUtenteConnesso(), mDataIni, mDataFin,
					lCodCancelleria, lFiltroCollab, lPosizioneGiuridica);

			// RICERCA Magistrati PER RIEMPIRE LA COMBOBOX
			// CHIAMO IL NUOVO METODO per anomalia 7 del verbale collaudo sies 11.3 (terza sessione)
			Vector lMagModVect = mCtrl.ExRicercaMagistratiProcIntervalli(
					getCodUfficioUtenteConnesso()); 
			setRequestAttribute("magistrati", lMagModVect);

			// RICERCA Oggetti PER RIEMPIRE LA COMBOBOX
			ricercaOggettiEstratti(TABELLA_ESTRAZIONE_INTERVALLI, getCodUfficioUtenteConnesso());

			setRequestAttribute("SecondoGiro", "SI");
		} else {
			ricercaCancellerieAssegnatarie();
			filtroCollaboratore();
		}

		return PG_LOAD_ESTRAZIONE_TEMPI; // restituisce la jsp di VIEW
	}

}