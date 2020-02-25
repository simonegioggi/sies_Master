package siap.siep.archiviazione.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import f3b.util.DateUtils;
import f3b.util.F3BException;

/**
 * Azione Helper di tutte le Action dell'Archiviazione.
 * <p>
 * Title: ActArchiviazione
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActArchiviazione extends ActionSiap implements ICostantiArchiviazione {

	/**
	 * Imposta tutte le notifiche associate all'evento prodotto
	 * 
	 * @param lKey
	 * @return
	 * @throws F3BException
	 */
	protected NotificaModel[] loadNotifiche() throws F3BException {

		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();
		Date lDataEmissione = getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
				ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO);
		ArrayList lNotifiche = new ArrayList();

		// SETTO NOTIFICA UFFICIO RECUPERO CREDITI
		if (!isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E)
				&& getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E) != null
				&& !getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E)
						.equals("-")) {
			String lUfficio = getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E);
			String lSedeUfficio = getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E);
			NotificaModel lNotModPol = new NotificaModel();
			lNotModPol.setCodEsito("-");
			lNotModPol.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModPol.setDataInserimento(DateUtils.getSysDate());
			lNotModPol.setCodUfficioInserimento(lCodiceUfficio);
			lNotModPol.setCodTipoNotifica("E");
			lNotModPol.setDataInvio(lDataEmissione);
			String lUff = getCodUfficioByCodTipoUfficioDescrComune(lUfficio, lSedeUfficio);
			lNotModPol.setUffCodUfficio(lUff);
			lNotifiche.add(lNotModPol);
		}

		// SETTO NOTIFICA AUTORITA DI POLIZIA
		if (!isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA)
				&& getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA) != null
				&& !getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA).equals("-")) {
			String lPolizia = getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA);
			String lSedePolizia = getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE);
			NotificaModel lNotModPol = new NotificaModel();
			if (!isRequestParameterNullObj(ICostantiNotifica.CAMPO_NOTE_E)) {
				String lNotePolizia = getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE_E);
				lNotModPol.setNote(lNotePolizia);
			}
			lNotModPol.setCodEsito("-");
			lNotModPol.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModPol.setDataInserimento(DateUtils.getSysDate());
			lNotModPol.setCodUfficioInserimento(lCodiceUfficio);
			lNotModPol.setCodTipoNotifica("N");
			lNotModPol.setDataInvio(lDataEmissione);
			AutoritaEsternaModel lAut = new AutoritaEsternaModel();
			lAut.setCodTipoAutorita(lPolizia);
			ComuneModel lComMod = new ComuneModel(getCodComuneByDescrFlagVal(lSedePolizia));
			lAut.setCodSede(lComMod.getCodComune());
			lAut.setCodOperatoreInserimento(lCodiceOperatore);
			lAut.setCodUfficioInserimento(lCodiceUfficio);
			lAut.setDataInserimento(DateUtils.getSysDate());
			lNotModPol.setAutoritaEsterna(lAut);
			lNotifiche.add(lNotModPol);
		}

		// SETTO TDS
		if (!isRequestParameterNullObj(ICostantiNotifica.CAMPO_SEDE_TDS)
				&& getRequestStringParameter(ICostantiNotifica.CAMPO_SEDE_TDS) != null
				&& !getRequestStringParameter(ICostantiNotifica.CAMPO_SEDE_TDS).equals("")) {
			NotificaModel lNotModTDS = new NotificaModel();
			String lSedeTribunale = getRequestStringParameter(ICostantiNotifica.CAMPO_SEDE_TDS);
			lNotModTDS.setCodEsito("-");
			lNotModTDS.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModTDS.setDataInserimento(DateUtils.getSysDate());
			lNotModTDS.setCodUfficioInserimento(lCodiceUfficio);
			lNotModTDS.setCodTipoNotifica("C");
			lNotModTDS.setDataInvio(lDataEmissione);
			// MEV_66: cambiata gestione uds + tds
			String codTipoUfficio = "TDS";
			if (!isRequestParameterNullObj("ufficioTds")
					&& getRequestStringParameter("ufficioTds") != null
					&& !"".equals(getRequestStringParameter("ufficioTds")))
				codTipoUfficio = getRequestStringParameter("ufficioTds");
			String lCodiceUff = getCodUfficioByCodTipoUfficioDescrComune(codTipoUfficio, lSedeTribunale);
			lNotModTDS.setUffCodUfficio(lCodiceUff);
			lNotifiche.add(lNotModTDS);
		}

		// SETTO UDS
		if (!isRequestParameterNullObj(ICostantiNotifica.CAMPO_SEDE_MDS)
				&& getRequestStringParameter(ICostantiNotifica.CAMPO_SEDE_MDS) != null
				&& !getRequestStringParameter(ICostantiNotifica.CAMPO_SEDE_MDS).equals("")) {
			NotificaModel lNotModTDS = new NotificaModel();
			// MEV_66: cambiata gestione uds + tds
			String codTipoUfficio = "UDS";
			if (!isRequestParameterNullObj("ufficioMdS")
					&& getRequestStringParameter("ufficioMdS") != null
					&& !"".equals(getRequestStringParameter("ufficioMdS")))
				codTipoUfficio = getRequestStringParameter("ufficioMdS");
			String lUDS = getCodUfficioByCodTipoUfficioDescrComune(codTipoUfficio,
					getRequestStringParameter(ICostantiNotifica.CAMPO_SEDE_MDS));
			lNotModTDS.setCodEsito("-");
			lNotModTDS.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModTDS.setDataInserimento(DateUtils.getSysDate());
			lNotModTDS.setCodUfficioInserimento(lCodiceUfficio);
			lNotModTDS.setCodTipoNotifica("C");
			lNotModTDS.setDataInvio(lDataEmissione);
			lNotModTDS.setUffCodUfficio(lUDS);
			lNotifiche.add(lNotModTDS);
		}

		// SETTO NOTIFICA ALTRA AUTORITA
		if (!isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C)
				&& getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C) != null
				&& !getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C)
						.equals("-")) {
			String lPolizia = getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C);
			String lSedePolizia = getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C);
			NotificaModel lNotModPol = new NotificaModel();
			if (!isRequestParameterNullObj(ICostantiNotifica.CAMPO_NOTE_C)) {
				String lNotePolizia = getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE_C);
				lNotModPol.setNote(lNotePolizia);
			}
			lNotModPol.setCodEsito("-");
			lNotModPol.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModPol.setDataInserimento(DateUtils.getSysDate());
			lNotModPol.setCodUfficioInserimento(lCodiceUfficio);
			lNotModPol.setCodTipoNotifica("C");
			lNotModPol.setDataInvio(lDataEmissione);
			AutoritaEsternaModel lAut = new AutoritaEsternaModel();
			lAut.setCodTipoAutorita(lPolizia);
			ComuneModel lComMod = new ComuneModel(getCodComuneByDescrFlagVal(lSedePolizia));
			lAut.setCodSede(lComMod.getCodComune());
			lAut.setCodOperatoreInserimento(lCodiceOperatore);
			lAut.setCodUfficioInserimento(lCodiceUfficio);
			lAut.setDataInserimento(DateUtils.getSysDate());
			lNotModPol.setAutoritaEsterna(lAut);
			lNotifiche.add(lNotModPol);
		}

		// SETTO NOTIFICA ALTRA AUTORITA TESTO LIBERO
		if (!isRequestParameterNullObj(ICostantiNotifica.CAMPO_NOTE)
				&& getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE) != null
				&& !getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE).equals("")) {
			NotificaModel lNotModPol = new NotificaModel();
			lNotModPol.setNote(getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE));
			lNotModPol.setCodEsito("-");
			lNotModPol.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModPol.setDataInserimento(DateUtils.getSysDate());
			lNotModPol.setCodUfficioInserimento(lCodiceUfficio);
			lNotModPol.setCodTipoNotifica("C");
			lNotModPol.setDataInvio(lDataEmissione);
			lNotifiche.add(lNotModPol);
		}

		return (NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]);
	}

	/**
	 * calcolaMagistrato
	 * 
	 * @return
	 */
	protected String calcolaMagistrato() throws F3BException {

		String lCodiceMagistrato = getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO);
		if (lCodiceMagistrato.compareTo("") == 0) {
			MagistratoModel lMagMod = new MagistratoModel();
			lMagMod.setCognome(getRequestStringParameter(ICostantiMagistrato.CAMPO_COGNOME.toUpperCase()));
			lMagMod.setNome(getRequestStringParameter(ICostantiMagistrato.CAMPO_NOME.toUpperCase()));

			IMagistrato lCtrl = SICOLookupRemote.getMagistratoRemote();
			Vector lVect = new Vector();
			try {
				lVect = lCtrl.ExRicercaMagistrato(lMagMod);
			} catch (F3BException exF3b) {
				throw new F3BException(F3BException.USER_MESSAGE, "Magistrato Inesistente");
			}

			lMagMod = (MagistratoModel) lVect.firstElement();
			lCodiceMagistrato = lMagMod.getCodMagistrato();
		}

		return lCodiceMagistrato;
	}

}