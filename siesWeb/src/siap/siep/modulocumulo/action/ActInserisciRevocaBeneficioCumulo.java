package siap.siep.modulocumulo.action;

import org.apache.log4j.Logger;
/**
* <p>Title: ActInserisciRevocaBeneficioCumulo</p>
* <p>Description: Classe Action per l'inserimento della Revoca Beneficio (Sospensione Condizionale/Non Menzione)</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
*/

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.model.ComuneModel;
import siap.siep.modulocumulo.controller.IBeneficioCumulo;
import siap.siep.modulocumulo.model.BeneficioCumuloModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Azione di Inserimento della Revoca Benefici relativi ad una Istruttoria (titolo CUMULATO) (In particolare
 * Benefici di Sospensione Condizionale e Non Menzione
 */

public class ActInserisciRevocaBeneficioCumulo extends ActionModuloCumulo implements ICostantiBeneficiCumulo {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	// private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		// BENEFICIO DA INSERIRE (REVOCA)
		BeneficioCumuloModel lBenMod = null;

		String lDeassocia = getRequestStringParameter(CAMPO_DEASSOCIA_TITOLO);
		String lModalita = getRequestStringParameter(ICostantiModuloCumulo.MODALITA);
		BigDecimal salvaTitoloRevoca = null;
		String salvaCodBeneficio = "";

		if (lModalita.compareTo("M") == 0) {
			// BENEFICO (Revoca) da Modificare
			BigDecimal lIdBenRevoca = getRequestBigDecimalParameter(CAMPO_ID_BENEFICIO_CUMULO);
			IBeneficioCumulo lCtrl = SIEPLookupRemote.getBeneficioCumuloRemote();
			BeneficioCumuloModel lBene = lCtrl.ExRicercaBeneficioCumuloByKey(lIdBenRevoca);
			if (lBene != null && lBene.getIdBeneficioCumulo() != null) {
				lBenMod = new BeneficioCumuloModel(lBene);

				salvaCodBeneficio = lBenMod.getCodTipoBeneficio();

				if (lBenMod.getTitIdTitoloCumulatoCollegato() != null) // Revoca collegata a Titolo del
																		// BeneficioConcesso
				{
					salvaTitoloRevoca = lBenMod.getTitIdTitoloCumulatoCollegato();
				}
			}
		} else {
			lBenMod = new BeneficioCumuloModel();
		}

		// ID del BENEFICIO_CUMULO da REVOCARE, CONCESSO in altro provvedimento
		BigDecimal lIdBeneficioConcesso = getRequestBigDecimalParameter(CAMPO_ID_BENEFICIO_DA_REVOCARE);

		lBenMod.setCodNaturaBeneficio("R");

		lBenMod.setCodTipoSospSubordinata("-");
		lBenMod.setCodSottotipoBeneficio("-");

		if (lModalita.compareTo("I") == 0) {
			lBenMod.setFlagStato("I");
			lBenMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
			lBenMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
			lBenMod.setDataInserimento(DateUtils.getSysDate());
		} else if (lModalita.compareTo("M") == 0) {
			lBenMod.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
			lBenMod.setCodUfficioAggiornamento(this.getCodUfficioUtenteConnesso());
			lBenMod.setDataAggiornamento(DateUtils.getSysDate());

			if (getRequestStringParameter(CAMPO_FLAG_STATO).compareTo("I") != 0)
				lBenMod.setFlagStato("M");
		}

		lBenMod.setMotivoModifica(getRequestStringParameter(ICostantiBeneficiCumulo.CAMPO_MOTIVO_MODIFICA));
		lBenMod.setTitIdTitoloCumulato(
				getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO));

		// Colonne di Riferimento al titolo che contiene il Beneficio Concesso da Revocare
		// ------------>>>>>>>
		lBenMod.setRifCodTipoProvvedimento(getRequestStringParameter(CAMPO_RIF_COD_TIPO_PROVVEDIMENTO));
		lBenMod.setRifCodTipoAutoEmittente(getRequestStringParameter(CAMPO_RIF_COD_TIPO_AUTORITA_EMITTENTE));

		ComuneModel lComMod = new ComuneModel(
				getCodComuneByDescr(getRequestStringParameter(CAMPO_RIF_COD_LUOGO_EMITTENTE)));
		lBenMod.setRifCodLuogoEmittente(lComMod.getCodComune());

		String SezioAuto = getRequestStringParameter(CAMPO_RIF_NUM_SEZIONE_AUTORITA_EMITTENTE);
		if (SezioAuto.compareTo("") != 0)
			lBenMod.setRifNumSezioneAutoEmittente(
					getRequestStringParameter(CAMPO_RIF_NUM_SEZIONE_AUTORITA_EMITTENTE));

		String AnnoProv = getRequestStringParameter(CAMPO_RIF_ANNO_PROVVEDIMENTO);
		String MeseProv = getRequestStringParameter(CAMPO_RIF_MESE_PROVVEDIMENTO);
		String GiornoProv = getRequestStringParameter(CAMPO_RIF_GIORNO_PROVVEDIMENTO);
		lBenMod.setRifDataProvvedimento(DateUtils.getDate(AnnoProv, MeseProv, GiornoProv));

		// se il Titolo � relativo ad un provvedimento di ORDINANZA(cod = 03), NON c'� nessuna sentenza di
		// riferimento:
		// Quindi non c'�: Data Irrevocabilit� - Id Riferimento Sentenza - Anno e Numero Sentenza di
		// Riferimento
		String TipoProv = getRequestStringParameter(CAMPO_RIF_COD_TIPO_PROVVEDIMENTO);
		if (lModalita.compareTo("I") == 0) {
			if (TipoProv.compareTo("03") == 0) // Ordinanza
			{
				lBenMod.setRifDataIrrevocabilita(null);
				lBenMod.setTitIdTitoloCumulatoCollegato(null);
				lBenMod.setRifAnnoProvvedimento(null);
				lBenMod.setRifNumeroProvvedimento(null);
			} else // Sentenza o Decreto
			{
				lBenMod.setRifDataIrrevocabilita(getRequestDateParameter(CAMPO_RIF_ANNO_IRREVOCABILITA,
						CAMPO_RIF_MESE_IRREVOCABILITA, CAMPO_RIF_GIORNO_IRREVOCABILITA));

				lBenMod.setRifAnnoProvvedimento(getRequestBigDecimalParameter(CAMPO_RIF_ANNO_SENTENZA));
				lBenMod.setRifNumeroProvvedimento(getRequestStringParameter(CAMPO_RIF_NUMERO_SENTENZA));

				if (!getRequestStringParameter(CAMPO_TIT_ID_TITOLO_CUMULO_COLLEGATO).equals("")) {
					lBenMod.setTitIdTitoloCumulatoCollegato(
							getRequestBigDecimalParameter(CAMPO_TIT_ID_TITOLO_CUMULO_COLLEGATO));
					// lBenMod.setRifAnnoProvvedimento(getRequestBigDecimalParameter(CAMPO_RIF_ANNO_SENTENZA));
					// lBenMod.setRifNumeroProvvedimento(getRequestStringParameter(CAMPO_RIF_NUMERO_SENTENZA));
				}
			}
		} else if (lModalita.compareTo("M") == 0) {
			if (lDeassocia.compareTo("SI") == 0) {
				lBenMod.setTitIdTitoloCumulatoCollegato(null);
				lBenMod.setRifAnnoProvvedimento(null);
				lBenMod.setRifNumeroProvvedimento(null);
			} else if (!getRequestStringParameter(CAMPO_TIT_ID_TITOLO_CUMULO_COLLEGATO).equals("")) {
				lBenMod.setTitIdTitoloCumulatoCollegato(
						getRequestBigDecimalParameter(CAMPO_TIT_ID_TITOLO_CUMULO_COLLEGATO));
				lBenMod.setRifAnnoProvvedimento(getRequestBigDecimalParameter(CAMPO_RIF_ANNO_SENTENZA));
				lBenMod.setRifNumeroProvvedimento(getRequestStringParameter(CAMPO_RIF_NUMERO_SENTENZA));
			} else {
				lBenMod.setRifAnnoProvvedimento(getRequestBigDecimalParameter(CAMPO_RIF_ANNO_SENTENZA));
				lBenMod.setRifNumeroProvvedimento(getRequestStringParameter(CAMPO_RIF_NUMERO_SENTENZA));
			}

			if (TipoProv.compareTo("03") == 0) {
				lBenMod.setRifDataIrrevocabilita(null);
			} else {
				lBenMod.setRifDataIrrevocabilita(getRequestDateParameter(CAMPO_RIF_ANNO_IRREVOCABILITA,
						CAMPO_RIF_MESE_IRREVOCABILITA, CAMPO_RIF_GIORNO_IRREVOCABILITA));
			}

		}

		// <<<<<<<------------

		if (getRequestStringParameter(ICostantiBeneficiCumulo.TIPO_FORM_BENEFICIO).compareTo("01") == 0) {
			lBenMod.setCodDpr("-");

			if (isRequestChecked(CAMPO_FLAG_SOSP_COND))
				lBenMod.setCodTipoBeneficio("01");
			if (isRequestChecked(CAMPO_FLAG_NON_MENZIONE))
				lBenMod.setCodTipoBeneficio("02");
		}

		if (getRequestStringParameter(ICostantiBeneficiCumulo.TIPO_FORM_BENEFICIO).compareTo("02") == 0) {
			lBenMod.setCodTipoBeneficio("03");
			lBenMod.setCodDpr(getRequestStringParameter(CAMPO_COD_DPR));

			// - - - Quantum Reclusione e Multa
			String AArecl = getRequestStringParameter(CAMPO_NUM_ANNI_RECLUSIONE);
			if (!AArecl.equals(""))
				lBenMod.setNumAnniReclusione(new BigDecimal(AArecl));
			else
				lBenMod.setNumAnniReclusione(new BigDecimal("0"));

			String MMrecl = getRequestStringParameter(CAMPO_NUM_MESI_RECLUSIONE);
			if (!MMrecl.equals(""))
				lBenMod.setNumMesiReclusione(new BigDecimal(MMrecl));
			else
				lBenMod.setNumMesiReclusione(new BigDecimal("0"));

			String GGrecl = getRequestStringParameter(CAMPO_NUM_GIORNI_RECLUSIONE);
			if (!GGrecl.equals(""))
				lBenMod.setNumGiorniReclusione(new BigDecimal(GGrecl));
			else
				lBenMod.setNumGiorniReclusione(new BigDecimal("0"));

			if (!getRequestStringParameter(CAMPO_IMPORTO_MULTA + "INT").equals("")) {
				if (!getRequestStringParameter(CAMPO_IMPORTO_MULTA + "DEC").equals(""))
					lBenMod.setImportoMulta(
							new BigDecimal(getRequestStringParameter(CAMPO_IMPORTO_MULTA + "INT") + "."
									+ getRequestStringParameter(CAMPO_IMPORTO_MULTA + "DEC")));
				else
					lBenMod.setImportoMulta(
							new BigDecimal(getRequestStringParameter(CAMPO_IMPORTO_MULTA + "INT")));
			} else {
				lBenMod.setImportoMulta(new BigDecimal("0"));
			}

			// - - - Quantum Arresto e Ammenda
			String AAarr = getRequestStringParameter(CAMPO_NUM_ANNI_ARRESTO);
			if (!AAarr.equals(""))
				lBenMod.setNumAnniArresto(new BigDecimal(AAarr));
			else
				lBenMod.setNumAnniArresto(new BigDecimal("0"));

			String MMarr = getRequestStringParameter(CAMPO_NUM_MESI_ARRESTO);
			if (!MMarr.equals(""))
				lBenMod.setNumMesiArresto(new BigDecimal(MMarr));
			else
				lBenMod.setNumMesiArresto(new BigDecimal("0"));

			String GGarr = getRequestStringParameter(CAMPO_NUM_GIORNI_ARRESTO);
			if (!GGarr.equals(""))
				lBenMod.setNumGiorniArresto(new BigDecimal(GGarr));
			else
				lBenMod.setNumGiorniArresto(new BigDecimal("0"));

			if (!getRequestStringParameter(CAMPO_IMPORTO_AMMENDA + "INT").equals("")) {
				if (!getRequestStringParameter(CAMPO_IMPORTO_AMMENDA + "DEC").equals(""))
					lBenMod.setImportoAmmenda(
							new BigDecimal(getRequestStringParameter(CAMPO_IMPORTO_AMMENDA + "INT") + "."
									+ getRequestStringParameter(CAMPO_IMPORTO_AMMENDA + "DEC")));
				else
					lBenMod.setImportoAmmenda(
							new BigDecimal(getRequestStringParameter(CAMPO_IMPORTO_AMMENDA + "INT")));
			} else {
				lBenMod.setImportoAmmenda(new BigDecimal("0"));
			}

			// Fine Arresto Ammenda , Reclusione Multa

			lBenMod.setNote(getRequestStringParameter(CAMPO_NOTE));
		}

		IBeneficioCumulo lCtrl = SIEPLookupRemote.getBeneficioCumuloRemote();
		if (lModalita.compareTo("I") == 0) {
			lCtrl.ExInserisciRevocaBeneficioCumulo(lBenMod, lIdBeneficioConcesso);
		} else if (lModalita.compareTo("M") == 0) {
			if (lDeassocia.compareTo("SI") == 0) {
				// Prima di Modificare la Revoca, la devo 'DEASSOCIARE' dal titolo del corrispondente
				// Beneficio Concesso;
				IBeneficioCumulo lCtrl1 = SIEPLookupRemote.getBeneficioCumuloRemote();
				BeneficioCumuloModel lBeneficioConcesso = new BeneficioCumuloModel();
				Vector lConcessi = new Vector();

				lBeneficioConcesso.setTitIdTitoloCumulato(salvaTitoloRevoca);
				lBeneficioConcesso.setCodTipoBeneficio(salvaCodBeneficio);
				lBeneficioConcesso.setCodNaturaBeneficio("C");
				lConcessi = lCtrl1.ExRicercaBeneficioCumulo(lBeneficioConcesso);

				//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				//// LogF3B.getLogger()
				// siesLogger.debug("--XX--ActInserisciRevocaBeneficioCumulo - Vector size
				//// "+lConcessi.size());
				if (lConcessi != null && lConcessi.size() > 0) {
					// Trover� comunque un SOLO Beneficio Corrispondente
					Iterator Itx = lConcessi.iterator();
					while (Itx.hasNext()) {
						lBeneficioConcesso = (BeneficioCumuloModel) Itx.next();
						if (lBeneficioConcesso.getTitIdTitoloCumulatoCollegato() != null && lBeneficioConcesso
								.getTitIdTitoloCumulatoCollegato().equals(lBenMod.getTitIdTitoloCumulato())) {
							break;
						}
					}

					if (lBeneficioConcesso != null && lBeneficioConcesso.getIdBeneficioCumulo() != null) {
						lBenMod = lCtrl.ExModificaRevocaBeneficioCumulo(lBenMod,
								lBeneficioConcesso.getIdBeneficioCumulo(), lDeassocia, lIdBeneficioConcesso);
					}
				}
			} else {

				lBenMod = lCtrl.ExModificaRevocaBeneficioCumulo(lBenMod, null, lDeassocia,
						lIdBeneficioConcesso);
			}
		}

		// Prepara la destinazione
		String lPage = "";

		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.modulocumulo.action.ActRicercaRevocheBeneficiCumulo";

		return lPage;
	}

}
