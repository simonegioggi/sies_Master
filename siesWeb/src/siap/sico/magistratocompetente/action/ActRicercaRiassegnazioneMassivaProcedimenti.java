package siap.sico.magistratocompetente.action;

import java.math.BigDecimal;

import f3b.util.F3BException;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.util.SIEPLookupRemote;

public class ActRicercaRiassegnazioneMassivaProcedimenti extends ActionSiap
		implements ICostantiMagistratoCompetente {

	private String param(String aName) throws F3BException {
		return isRequestParameterNullObj(aName) ? "" : getRequestStringParameter(aName).trim();
	}

	/**
	 * Stima dei procedimenti pendenti dell'intero ufficio o di un singolo Magistrato e di quelli
	 * riassegnabili in base al criterio scelto (lettera iniziale del condannato o cifra finale del progressivo)
	 *
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		String lAmbito = param(CAMPO_AMBITO_RIASSEGNAZIONE);
		String lCodMagistrato = param(ICostantiMagistrato.CAMPO_COD_MAGISTRATO + "_OLD");
		String lCognome = param(ICostantiMagistrato.CAMPO_COGNOME + "_OLD");
		String lNome = param(ICostantiMagistrato.CAMPO_NOME + "_OLD");
		if (lCodMagistrato.length() == 0) {
			lCodMagistrato = param(ICostantiMagistrato.CAMPO_COD_MAGISTRATO);
			lCognome = param(ICostantiMagistrato.CAMPO_COGNOME);
			lNome = param(ICostantiMagistrato.CAMPO_NOME);
		}
		if (AMBITO_RIASSEGNAZIONE_MAGISTRATO.equals(lAmbito)) {
			if (lCodMagistrato.length() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE,
						"Selezionare il Magistrato di cui riassegnare i procedimenti");
			}
		} else {
			lAmbito = AMBITO_RIASSEGNAZIONE_UFFICIO;
			lCodMagistrato = "";
			lCognome = "";
			lNome = "";
		}

		String lTipoCriterio = param(CAMPO_TIPO_CRITERIO_RIASSEGNAZIONE);
		String lValoreCriterio = RiassegnazioneMassivaUtil.normalizzaValore(lTipoCriterio,
				param(CAMPO_VALORE_CRITERIO_RIASSEGNAZIONE));

		String lCodUfficio = getCodUfficioUtenteConnesso();
		String lStato[] = { "02", "03" };
		IFascicoloSiep ifs = SIEPLookupRemote.getFascicoloSiepRemote();

		BigDecimal lTotalePendenti = ifs.ExGetCountProcedimentiRiassegnazioneMassiva(lCodMagistrato,
				lCodUfficio, lStato, null, null, null);
		BigDecimal lTotaleRiassegnabili = ifs.ExGetCountProcedimentiRiassegnazioneMassiva(lCodMagistrato,
				lCodUfficio, lStato, lTipoCriterio, lValoreCriterio, null);

		setRequestAttribute(CAMPO_AMBITO_RIASSEGNAZIONE, lAmbito);
		setRequestAttribute("CodMagistratoOrigine", lCodMagistrato);
		setRequestAttribute("CognomeMagistratoOrigine", lCognome);
		setRequestAttribute("NomeMagistratoOrigine", lNome);
		setRequestAttribute(CAMPO_TIPO_CRITERIO_RIASSEGNAZIONE, lTipoCriterio);
		setRequestAttribute(CAMPO_VALORE_CRITERIO_RIASSEGNAZIONE, lValoreCriterio);
		setRequestAttribute("totalePendenti", "" + lTotalePendenti);
		setRequestAttribute("totaleRiassegnabili", "" + lTotaleRiassegnabili);

		return PG_ESITO_RIASSEGNAZIONEMASSIVA_PROCEDIMENTI;
	}
}
