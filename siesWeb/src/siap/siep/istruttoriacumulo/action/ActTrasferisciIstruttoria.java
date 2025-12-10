package siap.siep.istruttoriacumulo.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.istruttoriacumulo.controller.IIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action che effettua la ricerca del procedimento su cui trasferire i dati dell'istruttoria
 *
 * @author difiorlett
 * @since MEV_2025-48 – 2.14 Caricamento Istruttoria Annullata
 */
public class ActTrasferisciIstruttoria extends ActionSiap implements ICostantiIstruttoriaCumulo {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		siesLogger.debug("ActTrasferisciIstruttoria");
		BigDecimal lIdIstruttoriaCumulo = getRequestBigDecimalParameter(CAMPO_ID_ISTRUTTORIA_CUMULO);
		IIstruttoriaCumulo lIstrCtrl = SIEPLookupRemote.getIstruttoriaCumuloRemote();
		IstruttoriaCumuloModel lIstruttoriaModel = null;
		lIstruttoriaModel = lIstrCtrl.ExRicercaIstruttoriaCumuloById(lIdIstruttoriaCumulo);
		setRequestAttribute("IstruttoriaCumulo", lIstruttoriaModel);

		//
		BigDecimal lIdFascicoloTrasf = getRequestBigDecimalParameter(
				ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP);

		// ==========================================================================
		// Creo il model eventualmente per la creazione della nuova
		// istruttoria se non già aperta
		// ==========================================================================
		IstruttoriaCumuloModel lIstMod = new IstruttoriaCumuloModel();

		lIstMod.setFasSieIdFascicoloSiep(lIdFascicoloTrasf);
		lIstMod.setDataApertura(DateUtils.getSysDate());
		// n.b. mi servono anche hh:mm:ss perchè nell'arco della giornata potrebbero venir aperte e chiuse più
		// istruttorie
		lIstMod.setFlagStato(FLAG_STATO_APERTA); // Aperta
		lIstMod.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
		// lIstMod.setNumProtocollo (new BigDecimal(1));// assegnato dal controller
		lIstMod.setChiaveUfficio(getCodUfficioUtenteConnesso());
		lIstMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lIstMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lIstMod.setDataInserimento(DateUtils.getSysDate());
		// Ordinamento di default per data irrevocabilità crescente
		lIstMod.setOrdinamentoTitoli(ICostantiIstruttoriaCumulo.ORDER_BY_DATA_IRREVOCABILITA_ASC);

		IIstruttoriaCumulo lCtrl = SIEPLookupRemote.getIstruttoriaCumuloRemote();
		IstruttoriaCumuloModel lIstRetMod = new IstruttoriaCumuloModel();
		lIstRetMod = lCtrl.ExTrasferisciIstruttoriaCumulo(lIstMod, lIstruttoriaModel);

		setRequestAttribute("IstruttoriaCumuloNew", lIstRetMod);

		// Da decidere DOVE atterrare.
		// Andare sulla nuova istruttoria?
		return PG_ESITO_TRASFERIMENTO_ISTRUTTORIA;
	}

}