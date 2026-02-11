package siap.siep.istruttoriacumulo.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoriacumulo.controller.IIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action che effettua la ricerca del procedimento su cui trasferire i dati dell'istruttoria
 *
 * @author difiorlett
 * @since MEV_2025-48 – 2.14 Caricamento Istruttoria Annullata
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActRicercaFascPerTrasferisciIstruttoria extends ActionSiap
		implements ICostantiIstruttoriaCumulo {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		siesLogger.debug("ActRicercaFascPerTrasferisciIstruttoria");
		BigDecimal lIdIstruttoriaCumulo = getRequestBigDecimalParameter(CAMPO_ID_ISTRUTTORIA_CUMULO);
		IIstruttoriaCumulo lIstrCtrl = SIEPLookupRemote.getIstruttoriaCumuloRemote();
		IstruttoriaCumuloModel lIstruttoriaModel = null;
		lIstruttoriaModel = lIstrCtrl.ExRicercaIstruttoriaCumuloById(lIdIstruttoriaCumulo);
		setRequestAttribute("IstruttoriaCumulo", lIstruttoriaModel);

		// Ricerco i titoli dell'istruttoria corrente
		String lOrdinamento = lIstruttoriaModel.getOrdinamentoTitoli();
		Vector lListaTitoliCorrenti = lIstrCtrl.ExRicercaTitoliByIstruttoriaOrderBy(lIdIstruttoriaCumulo,
				lOrdinamento);
		setRequestAttribute("ListaTitoli", lListaTitoliCorrenti);

		// Ricerco il fascicolo
		FascicoloSiepModel lFasMod = new FascicoloSiepModel();

        BigDecimal lChiaveAnno = getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO);
		BigDecimal lChiaveProgr = getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR);

		FascicoloSiepModel lFas = (FascicoloSiepModel) getSession().getAttribute("fascicolo");
		if (lFas.getChiaveAnno().compareTo(lChiaveAnno)==0 && lFas.getChiaveProgr().compareTo(lChiaveProgr)==0)
	          throw new SIEPException(F3BException.USER_MESSAGE,
	                    "Non si può trasferire l'istruttoria sullo stesso fascicolo");
		
		lFasMod.setChiaveUfficio(getCodUfficioUtenteConnesso());
		lFasMod.setChiaveProgr(lChiaveProgr);
		lFasMod.setChiaveAnno(lChiaveAnno);

		IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
		FascicoloSiepModel lFasRet = lCtrl.ExRicercaFascicoloSiepByProgrAnnoCodUfficio(lFasMod);
		if (lFasRet == null)
			throw new SIEPException(F3BException.USER_MESSAGE,
					"Nessun Fascicolo con Anno " + lChiaveAnno + " e Progressivo " + lChiaveProgr);

		setRequestAttribute("fascicoloTrovato", lFasRet);

		// ==============================================================
		// Cerco le istruttorie se presenti sul fascicolo di destinazione
		// ==============================================================
		IstruttoriaCumuloModel lIstMod = null;
		Vector<IstruttoriaCumuloModel> lVectIstruttorie = new Vector<>();
		lIstrCtrl = SIEPLookupRemote.getIstruttoriaCumuloRemote();

		// ==============================================================
		// Cerco una istruttoria aperta se presente o in alternativa
		// l'ultimo provvedimento di cumulo.
		// I titoli presenti nell'istruttoria aperta o nell'ultimo cumulo
		// potrebbero andare in conflitto con quelli del fascicolo corrente
		// ==============================================================

		// Cerco una istruttoria aperta se presente per visualizzare i titoli già in istruttoria
		lIstMod = lIstrCtrl.ExRicercaIstruttoriaCumuloApertaByIdFasSiep(lFasRet.getIdFascicoloSiep());

		if (lIstMod == null) {
			// Nessuna istruttoria aperta cerco quella collegata all'ultimo provvedimento di cumulo
			// per recuperare l'elenco dei titoli già in istruttoria
			lIstrCtrl = SIEPLookupRemote.getIstruttoriaCumuloRemote();
			lIstMod = lIstrCtrl.ExRicercaIstruttoriaUltimoCumulo(lFasRet.getIdFascicoloSiep());

			if (lIstMod != null) {
				IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
				EventoModel lEvento = lCtrlEvento.ExRicercaEventoByKey(lIstMod.getEveIdEventoProv());
				lIstMod.setProvvedimentoCumulo(lEvento);
			}
		}

		// Se Istruttoria trovata carico i titoli già in istruttoria
		// n.b. se chiusa, tali titoli verranno comuqneu ribaltati nella nuova istruttoria
		if (lIstMod != null) {
			lIstrCtrl = SIEPLookupRemote.getIstruttoriaCumuloRemote();
			Vector lListaTitoli = lIstrCtrl.ExRicercaTitoliByIstruttoriaOrderBy(
					lIstMod.getIdIstruttoriaCumulo(),
					ICostantiIstruttoriaCumulo.ORDER_BY_DATA_IRREVOCABILITA_ASC);

			lIstMod.setTitoliCumulati(lListaTitoli);
			lVectIstruttorie.add(lIstMod);
		}

		setRequestAttribute("ListaIstruttorieCumulo", lVectIstruttorie);

		return PG_ESITO_CERCA_FASCICOLO_PER_TRASFERIMENTO;
	}

}