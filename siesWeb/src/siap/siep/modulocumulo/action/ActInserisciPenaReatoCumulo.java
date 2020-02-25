package siap.siep.modulocumulo.action;

/**
* <p>Title: ActInserisciPenaReatoCumulo 	</p>
* <p>Description: Classe Action per l'inserimento della Pena
* <p>			 sul singolo Reato Cumulato	</p>
*/

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import siap.siep.modulocumulo.controller.IReatoCumulo;
import siap.siep.modulocumulo.model.ReatoCumuloModel;
import siap.siep.util.SIEPLookupRemote;

public class ActInserisciPenaReatoCumulo extends ActionModuloCumulo implements ICostantiReatoCumulo {

	public String processRequest() throws Exception {

		// preparazione dei dati
		ReatoCumuloModel lReaMod = LetturaDati();

		ReatoCumuloModel lReaModRet = inserimento(lReaMod);

		// Prepara la pagina di destinazione
		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.modulocumulo.action.ActLoadDettaglioReatoCumulo&" + CAMPO_ID_REATO_CUM + "="
				+ lReaModRet.getIdReatoCum().toString();

		return lPage;
	}

	// Lettura dei dati dalla request e valorizzazione del ReatoModel
	protected ReatoCumuloModel LetturaDati() throws F3BException {
		// Si istanzia ReatoModel
		ReatoCumuloModel lReaMod = new ReatoCumuloModel();

		lReaMod.setIdReatoCum(getRequestBigDecimalParameter(CAMPO_ID_REATO_CUM));
		lReaMod.setCodTipoPenaDetentiva(getRequestStringParameter(CAMPO_COD_TIPO_PENA_DETENTIVA));
		lReaMod.setNumAnni(getRequestBigDecimalParameter(CAMPO_NUM_ANNI));
		lReaMod.setNumMesi(getRequestBigDecimalParameter(CAMPO_NUM_MESI));
		lReaMod.setNumGiorni(getRequestBigDecimalParameter(CAMPO_NUM_GIORNI));
		lReaMod.setNumAnniIsolamentoDiurno(getRequestBigDecimalParameter(CAMPO_ANNI_ISOLAMENTO_DIURNO));
		lReaMod.setNumMesiIsolamentoDiurno(getRequestBigDecimalParameter(CAMPO_MESI_ISOLAMENTO_DIURNO));
		lReaMod.setNumGiorniIsolamentoDiurno(getRequestBigDecimalParameter(CAMPO_GIORNI_ISOLAMENTO_DIURNO));
		lReaMod.setCodTipoSanzione(getRequestStringParameter(CAMPO_COD_TIPO_SANZIONE));

		lReaMod.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
		lReaMod.setCodUfficioAggiornamento(this.getCodUfficioUtenteConnesso());
		lReaMod.setDataAggiornamento(DateUtils.getSysDate());

		if (getRequestStringParameter(CAMPO_FLAG_STATO).compareTo("I") != 0)
			lReaMod.setFlagStato("M");
		else
			lReaMod.setFlagStato(getRequestStringParameter(CAMPO_FLAG_STATO));

		lReaMod.setMotivoModificaNote(getRequestStringParameter(ICostantiReatoCumulo.CAMPO_MOTIVO_MODIFICA));

		////// MODIFICATO PER TENERE ALLINEATI I DATI DI TUTTE LE NORME
		// Carico tutte le norme
		lReaMod.setTitIdTitoloCumulato(
				getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO));
		lReaMod.setProgrReato(getRequestBigDecimalParameter(CAMPO_PROGR_REATO));
		// -->
		// lReaMod.setFasSieIdFascicoloSiep(getRequestBigDecimalParameter(CAMPO_FAS_SIE_ID_FASCICOLO_SIEP));
		// <--
		if (getRequestStringParameter(CAMPO_SANZIONE_PECUNIARIA_INT) != null
				&& !getRequestStringParameter(CAMPO_SANZIONE_PECUNIARIA_INT).equals("")) {
			if (getRequestStringParameter("Valuta").compareTo("LIT") == 0) {
				lReaMod.setSanzionePecuniaria(
						Utils.toEuro(getRequestStringParameter(CAMPO_SANZIONE_PECUNIARIA_INT)));
			} else {
				if (!getRequestStringParameter(CAMPO_SANZIONE_PECUNIARIA_DEC).equals("")) {
					lReaMod.setSanzionePecuniaria(
							new BigDecimal(getRequestStringParameter(CAMPO_SANZIONE_PECUNIARIA_INT) + "."
									+ getRequestStringParameter(CAMPO_SANZIONE_PECUNIARIA_DEC)));
				} else {
					lReaMod.setSanzionePecuniaria(
							new BigDecimal(getRequestStringParameter(CAMPO_SANZIONE_PECUNIARIA_INT)));
				}
			}
		} else {
			lReaMod.setSanzionePecuniaria(new BigDecimal(0));
		}

		return lReaMod;
	}

	/**
	 * Inserimento Pena Reato Cumulato
	 * 
	 * @return
	 * @throws F3BException
	 */
	@SuppressWarnings("rawtypes")
	private ReatoCumuloModel inserimento(ReatoCumuloModel aReaMod) throws F3BException {

		IReatoCumulo lCtrl = SIEPLookupRemote.getReatoCumuloRemote();
		Vector lNorme = lCtrl.ExRicercaReatoCumulo(aReaMod);

		ReatoCumuloModel lReaModRet = lCtrl.ExModificaPenaReatoCumulo(aReaMod, lNorme);
		////// FINE
		setRequestAttribute("reato", lReaModRet);

		return lReaModRet;
	}

}