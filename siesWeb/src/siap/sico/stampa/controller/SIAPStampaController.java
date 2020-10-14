package siap.sico.stampa.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.model.DecodeModel;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.Utils;
import f3b.util.xml.TreeModel;
import siap.controller.SiapController;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.dao.EventoPrecedenteSqlDAO;
import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.evento.model.EventoPrecedenteModel;
import siap.sico.evento.model.XModel;
import siap.sico.libertaanticipata.dao.LicenzaLibanticipataSqlDAO;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.misuraalternativa.model.MisuraAlternativaAggregatoModel;
import siap.sico.residenza.dao.ResidenzaSqlDAO;
import siap.sico.residenza.model.ResidenzaModel;
import siap.sico.soggetto.dao.SoggettoSqlDAO;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.alias.dao.AliasSqlDAO;
import siap.siep.alias.model.AliasModel;
import siap.siep.altracausa.controller.IAltraCausa;
import siap.siep.altracausa.dao.AltraCausaSqlDAO;
import siap.siep.altracausa.model.AltraCausaModel;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.annotazionemanuale.controller.IUltimaAnnotazioneManuale;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.annotazionemanuale.model.UltimaAnnotazioneModel;
import siap.siep.beneficio.dao.BeneficioSqlDAO;
import siap.siep.beneficio.model.BeneficioModel;
import siap.siep.circostanza.dao.CircostanzaSqlDAO;
import siap.siep.circostanza.model.CircostanzaModel;
import siap.siep.circostanza.util.CircostanzaUtil;
import siap.siep.continuazione.dao.ContinuazioneSqlDAO;
import siap.siep.continuazione.model.ContinuazioneModel;
import siap.siep.cumulo.dao.CumuloSqlDAO;
import siap.siep.cumulo.model.CumuloModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.fascicolo.model.FascicoloSiepPadreModel;
import siap.siep.fungibilita.dao.FungibilitaSqlDAO;
import siap.siep.fungibilita.model.FungibilitaModel;
import siap.siep.istitutodetenzione.controller.IIstitutoDetenzione;
import siap.siep.istitutodetenzione.dao.IstitutoDetenzioneSqlDAO;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.luogodetenzione.controller.ILuogoDetenzione;
import siap.siep.luogodetenzione.dao.LuogoDetenzioneSqlDAO;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
import siap.siep.misuracautelare.dao.MisuraCautelareSqlDAO;
import siap.siep.misuracautelare.model.MisuraCautelareModel;
import siap.siep.misuracautelare.model.TotaleMisureModel;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.misurasicurezza.dao.FascMsToFascSiepSqlDAO;
import siap.siep.misurasicurezza.dao.MisuraSicurezzaSqlDAO;
import siap.siep.misurasicurezza.model.FascMsToFascSiepModel;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.modulocumulo.dao.PenaRideterminataCumuloSqlDAO;
import siap.siep.modulocumulo.model.PenaRideterminataCumuloModel;
import siap.siep.penaaccessoria.dao.PenaAccessoriaSqlDAO;
import siap.siep.penaaccessoria.model.PenaAccessoriaModel;
import siap.siep.penacomplessiva.dao.PenaComplessivaSqlDAO;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penacomplessiva.model.PenaComplessivaSanzioneSostitutivaModel;
import siap.siep.penacumulo.dao.PenaCumuloSqlDAO;
import siap.siep.penacumulo.model.PenaCumuloModel;
import siap.siep.penaresidua.dao.PenaResiduaSqlDAO;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.dao.PosizioneGiuridicaSqlDAO;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.reato.controller.IReato;
import siap.siep.reato.controller.ReatoContinuazioneController;
import siap.siep.reato.dao.ReatoSqlDAO;
import siap.siep.reato.model.ContinuazioneReatiModel;
import siap.siep.reato.model.ReatoCircostanzaModel;
import siap.siep.reato.model.ReatoModel;
import siap.siep.sanzionesostitutiva.dao.SanzioneSostitutivaSqlDAO;
import siap.siep.sanzionesostitutiva.model.SanzioneSostitutivaModel;
import siap.siep.sentenza.dao.SentenzaSqlDAO;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.sentenzariunita.dao.SentenzaRiunitaFascSiepSqlDAO;
import siap.siep.sentenzariunita.model.SentenzaRiunitaFascSiepModel;
import siap.siep.sentenzariunita.model.SentenzaRiunitaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.rifasiep.controller.IRiferimentoFascicoloSiep;
import siap.sius.rifasiep.model.RiferimentoFascicoloSiepModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * Title:SIAPStampaController
 * Description: Classe padre della stampa. Riunisce tutti i metodi comuni alle
 * 				varie classi specializzate di stampa 
 * Copyright: Copyright (c) 2004 
 * Company: Bull Italia S.p.A.
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SIAPStampaController extends SiapController {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	protected StampaEventoUtils mEventoUtils = new StampaEventoUtils();

	protected XModel createRoot(EventoNotificaModel aEveModel) throws F3BException {
		return createRoot(aEveModel, null);
	}

	/**
	 * Crea la root del Documento recuperando i dati dell'ufficio dell'utente passato in input
	 *
	 * @param aEveModel
	 * @param aUtenteModel
	 * @return
	 */
	protected XModel createRoot(EventoNotificaModel aEveModel, UtenteModel aUtenteModel) throws F3BException {

		XModel lStampa = new XModel();

		String descrTipoUff = aEveModel.getEvento().getDescrUfficioEmittente().toUpperCase();

		lStampa.setUfficio(aEveModel.getEvento().getDescrLuogoEmittente().toUpperCase());
		lStampa.setTipoUfficio(descrTipoUff.toUpperCase());
		lStampa.setDataElaborazione(DateUtils.getSysDate());

		if (aUtenteModel != null && aUtenteModel.getUfficioUtente() != null) {
			UfficioModel lUffMod = aUtenteModel.getUfficioUtente();

			lStampa.setCap(lUffMod.getCap());
			lStampa.setFax(lUffMod.getFax());
			lStampa.setIndirizzo(lUffMod.getIndirizzo());
			lStampa.setTelefono(lUffMod.getTelefono());
			lStampa.setEMail(lUffMod.getEMail());
		}

		if (descrTipoUff != null) {
			if (descrTipoUff.indexOf("PRESSO") > 1) {
				lStampa.setTipoUfficioT1(descrTipoUff.substring(0, descrTipoUff.indexOf("PRESSO")));
				lStampa.setTipoUfficioT2(descrTipoUff.substring(descrTipoUff.indexOf("PRESSO")));
			}
			// STUB 01/02/2005 Patch x Valorizzare TipoUfficioT1.
			else
				lStampa.setTipoUfficioT1(descrTipoUff);
		}

		if (descrTipoUff.indexOf("GENERALE") > 0) { // GDV modifica
													// lStampa.setFirmatario("Il Sostituto Procuratore
													// Generale");
			lStampa.setFirmatario("Il Procuratore Generale");
		} else {
			lStampa.setFirmatario("Il Pubblico Ministero");
		}

		return lStampa;
	}

	/**
	 * Metodo per aggiungere tutte le entita' del Fascicolo SIEP
	 *
	 * @param lConn
	 * @param lKeyFascicolo
	 * @param aTreeFasMod
	 * @param aAltraCausa
	 * @throws F3BException
	 */
	protected void appendTableToFascicoloSiep(Connection lConn, BigDecimal lKeyFascicolo,
			TreeModel aTreeFasMod, String aAltraCausa) throws F3BException {

		ReatoSqlDAO lReaDao = null;
		MisuraCautelareSqlDAO lMisDao = null;
		PenaComplessivaSqlDAO lPenDao = null;
		PenaAccessoriaSqlDAO lPenAccDao = null;
		PosizioneGiuridicaSqlDAO lPosGiuDao = null;
		BeneficioSqlDAO lBenDao = null;
		LuogoDetenzioneSqlDAO lLuoDao = null;
		MisuraSicurezzaSqlDAO lMisSicDao = null;
		CircostanzaSqlDAO lCircDao = null;
		SanzioneSostitutivaSqlDAO lSanDao = null;
		ContinuazioneSqlDAO lContSqlDAO = null;
		EventoSqlDAO lEveSqlDAO = null;

		PenaComplessivaSanzioneSostitutivaModel lPenSanMod = null;
		Vector lContinuazioni = null;

		try {
			// Pena Complessiva
			lPenDao = new PenaComplessivaSqlDAO(lConn);
			lPenDao.ricercaPenaComplessivaByIdFascicolo(lKeyFascicolo);

			PenaComplessivaModel lPenMod = (PenaComplessivaModel) lPenDao.getModelByKey();

			if (lPenMod != null) {
				lSanDao = new SanzioneSostitutivaSqlDAO(lConn);
				lSanDao.ricercaSanzioneSostitutivaByIdPenaComplessiva(lPenMod.getIdPenaComplessiva());
				SanzioneSostitutivaModel lSanMod = (SanzioneSostitutivaModel) lSanDao.getModelByKey();
				// Sanzione Sostituviva
				if (lSanMod != null) {
					lSanMod.calcolaStringaSanzione();
					lSanMod.calcolaPeriodoSanzione();
				}

				lPenSanMod = new PenaComplessivaSanzioneSostitutivaModel(lPenMod, lSanMod);

				// Stringa Arresto - Reclusione
				if (lPenMod != null) {
					lPenSanMod.getPenaComplessiva().calcolaStringaReclusione();
					lPenSanMod.getPenaComplessiva().calcolaStringaArresto();
					lPenSanMod.getPenaComplessiva().calcolaStringaIsolamento();
				}

				// Continuazione
				lContSqlDAO = new ContinuazioneSqlDAO(lConn);
				lContSqlDAO.ricercaContinuazioneByIdPenaComplessiva(lPenMod.getIdPenaComplessiva());
				lContinuazioni = new Vector(lContSqlDAO.getModels());
			}

			// Pena Accessoria
			lPenAccDao = new PenaAccessoriaSqlDAO(lConn);
			lPenAccDao.ricercaPenaAccessoriaByFascicolo(lKeyFascicolo);
			Vector lPeneAccessorie = new Vector(lPenAccDao.getModels());

			// Reati
			IReato lReaCtr = SIEPLookupRemote.getReatoRemote();
			Vector lReati = lReaCtr.ExRicercaReatoCircostanzaByFascicolo(lKeyFascicolo);

			// Aggravanti Attenuanti
			lCircDao = new CircostanzaSqlDAO(lConn);
			lCircDao.ricercaCircostanzeByIdFascicolo(lKeyFascicolo);
			Vector lCircostanze = new Vector(lCircDao.getModels());
			// Serena rework generale - manipola il vettore di circostanze
			// mettendo come ultimo elemento la
			// circostanza avente Art 442 e CodFonte 25
			Vector lNewVectCirc = CircostanzaUtil.creaVectorCircostanze(lCircostanze);

			// Misura Cutelare
			lMisDao = new MisuraCautelareSqlDAO(lConn);
			lMisDao.ricercaMisuraCautelareByFascicoloForStampa(lKeyFascicolo);
			Vector lMisure = new Vector(lMisDao.getModels());

			// MEV_10_S3
			// Quando 2 o piu' misure cautelari computabili sono continuative il
			// sistema
			// non deve visualizzare nell'elenco delle misure il
			// "totale dei giorni"
			Vector lMisureApp = new Vector();
			Date lDateFinePrec = null;
			// identifica il numero di riga del vettore
			int inc = -1;
			// indica i giorni che intercorrono tra 2 misure cautelari
			// consecutiva
			int giorni = -1;

			Iterator lItMisure = lMisure.iterator();
			while (lItMisure.hasNext()) {
				MisuraCautelareModel lMisCautModel = new MisuraCautelareModel(
						(MisuraCautelareModel) lItMisure.next());

				inc += 1;

				// misure cautelari computabili
				// se le misure sono continuative non deve essere visibile il
				// totale dei giorni
				if (lMisCautModel.getFlagComputabile() != null
						&& !lMisCautModel.getFlagComputabile().equals("")
						&& lMisCautModel.getFlagComputabile().equals("S")) {

					// aggiungo la misura cautelare al vettore di appoggio
					lMisureApp.add(lMisCautModel);

					// sono sul primo elemento del vettore, pertanto
					// recupero la data fine della misura cautelare corrente
					if (inc == 0) {
						lDateFinePrec = lMisCautModel.getDataFine();
						lMisCautModel.setMisCautContinuativa("N");
					} else {
						// quando sono sugli elementi successivi al primo, devo
						// recuperare la data fine della misura cautelare
						// precedente
						MisuraCautelareModel lMisCaut = (MisuraCautelareModel) lMisureApp.get(inc - 1);
						lDateFinePrec = lMisCaut.getDataFine();
					}

					// si effettua la differenza tra la data fine della misura
					// cautelare
					// precedente con la data inizio della misura corrente
					// (tranne per il primo elemento del vettore)
					if (inc > 0) {
						giorni = DateUtils.getIntervallo(lDateFinePrec, lMisCautModel.getDataInizio());
					}

					// se la differenza tra la data fine e la data inizio e'
					// maggiore
					// di 1, significa che le 2 misure cautelari non sono
					// continuative
					if (giorni > 1) {
						// misure cautelari non continuative
						lMisCautModel.setMisCautContinuativa("N");

						// Recupero la misura cautelare precedente e imposto il
						// flag
						// MisCautContinuativa uguale ad 'N' solo se
						// quest'ultimo e' diverso
						// da 'S'
						MisuraCautelareModel lMisCaut = (MisuraCautelareModel) lMisureApp.get(inc - 1);
						if (lMisCaut.getMisCautContinuativa() == null
								|| lMisCaut.getMisCautContinuativa().equals("")
								|| !lMisCaut.getMisCautContinuativa().equals("S")) {
							lMisCaut.setMisCautContinuativa("N");
						}

						// se la differenza tra la data fine e la data inizio e'
						// uguale a 0 oppure uguale a 1 le misure cautelari
						// sono continuative
					} else if (giorni == 0 || giorni == 1) {
						// misure cautelari continuative
						lMisCautModel.setMisCautContinuativa("S");

						// Recupero la misura cautelare precedente e imposto il
						// flag
						// MisCautContinuativa uguale ad 'S'
						MisuraCautelareModel lMisCaut = (MisuraCautelareModel) lMisureApp.get(inc - 1);
						lMisCaut.setMisCautContinuativa("S");
					} else if (inc > 0 && giorni < 0) {
						// caso in cui i periodi tra le misure si sovrappongono
						// es. periodo prima misura dal 01/01/2015 al 20/02/2015
						// periodo seconda misura dal 20/01/2015 al 25/01/2015

						// misure cautelari non continuative
						lMisCautModel.setMisCautContinuativa("N");
					}

					/*
					 * } else if (lMisCautModel.getFlagComputabile() != null &&
					 * !lMisCautModel.getFlagComputabile().equals("") &&
					 * lMisCautModel.getFlagComputabile().equals("S") && lMisCautModel.getCodTipoMisura() !=
					 * null && !lMisCautModel.getCodTipoMisura().equals("") &&
					 * !lMisCautModel.getCodTipoMisura().equals("CL")){ // misure cautelari computabili //
					 * imposto il flag MisCautContinuativa uguale ad 'N' // perche' il totale dei giorni deve
					 * essere visibile lMisCautModel.setMisCautContinuativa("N");
					 * lMisureApp.add(lMisCautModel);
					 */
				} else {
					// misure cautelari non computabili
					// imposto il flag MisCautContinuativa uguale ad 'S'
					// perche' il totale dei giorni non deve essere visibile
					lMisCautModel.setMisCautContinuativa("S");

					// aggiungo la misura cautelare al vettore di appoggio
					lMisureApp.add(lMisCautModel);

				}

			}

			// aggiorno il vettore delle misure cautelari
			lMisure = new Vector(lMisureApp);

			// Misura Sicurezza
			lMisSicDao = new MisuraSicurezzaSqlDAO(lConn);
			// 17-12-2014 - sostituisco la ricerca -
			// lMisSicDao.ricercaMisuraSicurezzaByIdFascicoloOrd(lKeyFascicolo);
			lMisSicDao.ricercaTutteMisureSicurezzaByIdFascicoloOrd(lKeyFascicolo);
			Vector lMisureSic = new Vector(lMisSicDao.getModels());

			// mev56 INIZIO ***************************
			FascMsToFascSiepSqlDAO lFasMsToFascSiepSqlDao = null;
			Vector<FascMsToFascSiepModel> lLista = new Vector<>();
			// Vector vectFasIV = new Vector();
			BigDecimal fascColl = null;
			if (lMisureSic != null && lMisureSic.size() == 0) {

				lFasMsToFascSiepSqlDao = new FascMsToFascSiepSqlDAO(lConn);

				lFasMsToFascSiepSqlDao.ricercaCollegamentiSiep(lKeyFascicolo);

				lLista = new Vector<FascMsToFascSiepModel>(lFasMsToFascSiepSqlDao.getModels());
				// vectFasIV =
				// lCtrMis.ExRicercaFascicoliCollegati(lKeyFascicolo);

				if (lLista.size() > 0) {
					Iterator iteIV = lLista.iterator();
					while (iteIV.hasNext()) {
						FascMsToFascSiepModel FascMSMod = (FascMsToFascSiepModel) iteIV.next();
						fascColl = FascMSMod.getFasSieIdFascicoloCollegato();

						lMisSicDao.ricercaTutteMisureSicurezzaByIdFascicoloOrd(fascColl);
						lMisureSic = new Vector(lMisSicDao.getModels());
					}
				} else if (lLista.size() == 0) {
					// rifaccio la ricerca in modo da cercarlo però con FascMSMod.getFasSieIdFascicoloSiep();
					// e non con FascMSMod.getFasSieIdFascicoloCollegato();
					lFasMsToFascSiepSqlDao.ricercaCollegamentiSiepSorv(lKeyFascicolo);

					lLista = new Vector<FascMsToFascSiepModel>(lFasMsToFascSiepSqlDao.getModels());

					if (lLista.size() > 0) {
						Iterator iteIV = lLista.iterator();
						while (iteIV.hasNext()) {
							FascMsToFascSiepModel FascMSMod = (FascMsToFascSiepModel) iteIV.next();
							fascColl = FascMSMod.getFasSieIdFascicoloSiep();

							lMisSicDao.ricercaTutteMisureSicurezzaByIdFascicoloOrd(fascColl);
							lMisureSic = new Vector(lMisSicDao.getModels());
						}
					}
				}
			}
			// mev56 FINE ***********************************

			// Posizione Giuridica

			/*
			 *
			 * lPosGiuDao = new PosizioneGiuridicaSqlDAO(lConn);
			 * lPosGiuDao.ricercaPosGiuCorrenteByIdFascicolo(lKeyFascicolo); PosizioneGiuridicaModel lPosMod =
			 * (PosizioneGiuridicaModel) lPosGiuDao.getModelByKey();
			 */

			// 24-01-2015 Fasc_MS_to_Fasc_SIEP
			FascMsToFascSiepModel lFascMS = new FascMsToFascSiepModel();
			IMisuraSicurezza CtrlM = SIEPLookupRemote.getMisuraSicurezzaRemote();
			lFascMS.setFasSieIdFascicoloSiep(lKeyFascicolo);

			// if(lFasModel.getChiaveProgr().intValue() >= 39999 &&
			// lFasModel.getChiaveProgr().intValue() <= 49999 )
			// {
			// lFascMS.setFasSieIdFascicoloClasseIV(lKeyFascicolo);
			// }
			// else
			// {
			// lFascMS.setFasSieIdFascicoloSiep(lKeyFascicolo);
			// }

			Vector lVecFascMS = new Vector(CtrlM.ExRicercaFascMsToFascSiepByFascSiep(lFascMS));

			// Ricerca Benefici
			lBenDao = new BeneficioSqlDAO(lConn);
			lBenDao.ricercaBeneficioByKeyFascicolo(lKeyFascicolo);
			Vector lBenefici = new Vector(lBenDao.getModels());

			// Totali Misure Cautelari
			// Nei totali vengono calcolate solo le Misuare Cautelari
			// Computabili
			// e vengono escluse dal calcolo le NON COMPUTABILI
			TotaleMisureModel lTotaleMisure = new TotaleMisureModel();
			lTotaleMisure.calcolaTotaleMisure(lMisure);

			lMisure = lTotaleMisure.calcolaTotaleParzialeMisure(lMisure);
			/*
			 * Luogo Detenzione if (aAltraCausa != null) { if (aAltraCausa.compareTo("S") != 0) {
			 * ILuogoDetenzione lLuogo = SIEPLookupRemote.getLuogoDetenzioneRemote(); LuogoDetenzioneModel
			 * lLuoMod = lLuogo.ExRicercaLuogoDetenzioneCorrenteByFascicoloSiep(lKeyFascicolo);
			 *
			 * if (lLuoMod != null) { if (lLuoMod.getIstitutoDetenzione() != null) {
			 * lLuoMod.setDescrTipoIstituto(lLuoMod.getIstitutoDetenzione().getDescrTipoIstituto());
			 * lLuoMod.setDescrLuogo(lLuoMod.getIstitutoDetenzione().getDescrComune() + ", " +
			 * lLuoMod.getIstitutoDetenzione().getIndirizzo()); } else
			 * lLuoMod.setDescrTipoIstituto(lLuoMod.getAltroLuogo());
			 *
			 * aTreeFasMod.add(new TreeModel(lLuoMod)); } } else { //Altra Causa IAltraCausa lAltraCausa =
			 * SIEPLookupRemote.getAltraCausa(); AltraCausaModel lAltCau =
			 * lAltraCausa.ExRicercaAltraCausaIstitutoByFascicolo(lKeyFascicolo);
			 *
			 * if (lAltCau != null) { if (lAltCau.getIstitutoDetenzione() != null) {
			 * lAltCau.setDescrTipoIstituto(lAltCau.getIstitutoDetenzione().getDescrTipoIstituto());
			 * lAltCau.setDescrLuogo(lAltCau.getIstitutoDetenzione().getDescrComune() + ", " +
			 * lAltCau.getIstitutoDetenzione().getIndirizzo()); } //Add Luogo di Detenzione
			 * aTreeFasMod.add(new TreeModel(lAltCau)); } } }
			 *
			 * if (lPosMod != null) //Posizione Giuridica aTreeFasMod.add(new TreeModel(lPosMod));
			 */

			this.addPosizioneGiuridica(lConn, lKeyFascicolo, aTreeFasMod, aAltraCausa);

			Iterator lItx = null;

			// Reati
			if (lReati != null) {
				Vector lReatiPerCont = new Vector();
				lItx = lReati.iterator();
				while (lItx.hasNext()) {
					// Add gerarchia dei Reati - Circostanza
					ReatoCircostanzaModel lReatoModel = new ReatoCircostanzaModel(
							(ReatoCircostanzaModel) lItx.next());
					TreeModel lTreeReatoMod = new TreeModel(lReatoModel.getReato());

					// Creo Vettore dei Reati per le Continuazioni
					ReatoModel lReato = lReatoModel.getReato();
					if (lReato != null) {
						lReato.calcolaPenaReato();
						lReato.calcolaStringaPenaPecuniaria();
					}

					lReatiPerCont.add(lReato);

					int count = 0; // Circostanze
					while (count < lReatoModel.getCircostanze().length) {
						TreeModel lTreeCirc = new TreeModel(lReatoModel.getCircostanze()[count]);
						lTreeReatoMod.add(lTreeCirc);
						count++;
					}
					aTreeFasMod.add(lTreeReatoMod);
				}
				// Continuazione
				ReatoContinuazioneController lRCtrl = new ReatoContinuazioneController();
				Hashtable lTable = lRCtrl.getTableContinuazioni(lReatiPerCont);

				Vector lContinuazioniReati = lRCtrl.getContinuazioniReati(lTable);
				if (lContinuazioniReati != null && lContinuazioniReati.size() > 0) {
					Iterator lContItx = lContinuazioniReati.iterator();
					while (lContItx.hasNext()) {
						ContinuazioneReatiModel lCont = (ContinuazioneReatiModel) lContItx.next();
						aTreeFasMod.add(new TreeModel(lCont));
					}
				}
			}

			// Add Circostanze
			if (lNewVectCirc != null && lNewVectCirc.size() > 0) {
				lItx = lNewVectCirc.iterator();
				while (lItx.hasNext()) {
					// Add Circostanza
					CircostanzaModel lCircModel = new CircostanzaModel((CircostanzaModel) lItx.next());
					TreeModel lTreeCircMod = new TreeModel(lCircModel);
					aTreeFasMod.add(lTreeCircMod);
				}

			}

			// Add Benefici
			if (lBenefici != null) {
				lItx = lBenefici.iterator();
				while (lItx.hasNext()) {
					BeneficioModel lBen = new BeneficioModel((BeneficioModel) lItx.next());
					// Stringa Arresto - REclusione
					if (lBen != null) {
						lBen.calcolaStringaReclusione();
						lBen.calcolaStringaArresto();
					}
					TreeModel lTreeBenMod = new TreeModel(lBen);
					aTreeFasMod.add(lTreeBenMod);
				}
			}
			// Add Misure Cautelari
			if (lMisure != null) {
				lItx = lMisure.iterator();
				while (lItx.hasNext()) {
					MisuraCautelareModel lMisureTemp = (MisuraCautelareModel) lItx.next();
					if (lMisureTemp != null && lMisureTemp.getNumGiorni() != null
							&& (lMisureTemp.getNumGiorni().intValue() == 0)) {
						lMisureTemp.setNumGiorni(null);
					}

					if (lMisureTemp.getAutoritaCompetente() != null) {
						lMisureTemp.setAutoritaCompetenteDesc(Utils.getDescItem(
								DecodificheManager.getInstance().getAutoritaCompetentePerTerritorio(),
								lMisureTemp.getAutoritaCompetente()));
						// setRequestAttribute("autoritaCompetenteCautelareDesc",
						// Utils.getDescItem(DecodificheManager.getInstance().getAutoritaCompetentePerTerritorio(),
						// lPosLuoAltMod.getMisuraCautelare().getAutoritaCompetente()));
					}

					TreeModel lTreeMisureMod = new TreeModel(lMisureTemp);
					aTreeFasMod.add(lTreeMisureMod);
					// AMBROS ---> L78 del 2013

					if (lMisureTemp.getIstDetIdIstitutoDetenzione() != null
							&& !lMisureTemp.getIstDetIdIstitutoDetenzione().equals("")) {
						IIstitutoDetenzione lIst = SIEPLookupRemote.getIstitutoDetenzioneRemote();
						IstitutoDetenzioneModel IstMod = new IstitutoDetenzioneModel();
						IstMod = lIst.ExRicercaIstitutoDetenzioneByKey(
								lMisureTemp.getIstDetIdIstitutoDetenzione());
						if (IstMod != null && IstMod.getIdIstitutoDetenzione() != null)
							lTreeMisureMod.add(new TreeModel(IstMod));
					}
					// ------------>
					ILuogoDetenzione lLuogo = SIEPLookupRemote.getLuogoDetenzioneRemote();
					LuogoDetenzioneModel lLuoMod = lLuogo
							.ExRicercaLuogoDetenzioneCorrenteByFascicoloSiep(lKeyFascicolo);

					if (lLuoMod != null) {
						if (lLuoMod.getIstitutoDetenzione() != null) {
							lLuoMod.setDescrTipoIstituto(
									lLuoMod.getIstitutoDetenzione().getDescrTipoIstituto());
							if (lLuoMod.getIstitutoDetenzione().getIndirizzo() != null) {
								lLuoMod.setDescrLuogo(lLuoMod.getIstitutoDetenzione().getDescrComune() + ", "
										+ lLuoMod.getIstitutoDetenzione().getIndirizzo());
							} else {
								lLuoMod.setDescrLuogo(lLuoMod.getIstitutoDetenzione().getDescrComune());
							}
						} else
							lLuoMod.setDescrTipoIstituto(lLuoMod.getAltroLuogo());

						lTreeMisureMod.add(new TreeModel(lLuoMod));
					}
				}
				aTreeFasMod.add(new TreeModel(lTotaleMisure));
			}

			// Add Misure Sicurezza
			MisuraAlternativaAggregatoModel lAgg = null;
			if (lMisureSic != null) {
				lItx = lMisureSic.iterator();
				while (lItx.hasNext()) {
					MisuraSicurezzaModel lMis = (MisuraSicurezzaModel) lItx.next();

					// mev56 INIZIO ***********************
					// verifico se per il Fasciclo Siep, esiste un
					// ordinanza/decreto (COD_TIPO_PROVVEDIMENTO=02/03)
					// sulla tabella EVENTO emessa dal Magistrato di
					// Sorveglianza o Tribunale
					// legato alla Misura di Sicurezza
					EventoModel lEventoMs = new EventoModel();
					lEventoMs.setFlagDocumentoRegistrato("S");
					lEventoMs.setFasSieIdFascicoloSiep(lMis.getFasSieIdFascicoloSiep());
					lEventoMs.setCodTipoEvento("01");

					// Di seguito i MOTIVO_PROVVEDIMENTO legati ai seguenti
					// contenuti:
					// OGGETTO_PROCEDIMENTO ('C029', 'C036')
					// OGGETTO_PROCEDIMENTO ('U023', 'U077', 'U082', 'U088', 'U086'
					// 'U089')
					String[] lCodMotivoProvvedimento = { "0258", "0259", "0260", "0428", "0429", "0430",
							"0431", "0432", "0433", "2110", "2111", "2112", "2113", "2114", "2116", "2550",
							"2551", "2552", "2553", "2554", "2555", "2660", "2670", "2404", "2405", "2406",
							"2407", "2409", "2408", "2422", "2416",
							// EC[14062018] : 2440,2441,2442,9073,9074,9075 (questi motivi sono relativi ai
							// contenuti 'U114', 'U067' e cioè del Riesame della Pericolosità sociale)
							// è in dubbio se devono essere inclusi o meno.
							"2440", "2441", "2442", "9073", "9074", "9075" };

					String[] lCodTipoProvvedimento = { "02", "03" };

					lEveSqlDAO = new EventoSqlDAO(lConn);
					lEveSqlDAO.ricercaEventoPerMotivoProvv(lCodMotivoProvvedimento, lCodTipoProvvedimento,
							lEventoMs);

					lEveSqlDAO.start();

					if (lEveSqlDAO.next()) {
						lAgg = lEveSqlDAO.getModelDecretoOrdinanzaUfficio();

					}

					// ripeto la ricerca per il collegato
					if ((lAgg == null || lAgg.getEventoNotifica() == null)
							&& lMis.getFasSieIdFascicoloSiepRif() != null) {

						lEventoMs.setFasSieIdFascicoloSiep(lMis.getFasSieIdFascicoloSiepRif());
						lEveSqlDAO.ricercaEventoPerMotivoProvv(lCodMotivoProvvedimento, lCodTipoProvvedimento,
								lEventoMs);

						lEveSqlDAO.start();
						if (lEveSqlDAO.next()) {
							lAgg = lEveSqlDAO.getModelDecretoOrdinanzaUfficio();

						}
					}

					// ripeto ancora se lAgg è null sul collegato di classe IV (anomalia 2 del VERBALE
					// ANNOTAZIONI SORVEGLIANZA IN CLASSE I), PAR. 9.3 DELLA MEV39
					if ((lAgg == null || lAgg.getEventoNotifica() == null)
							&& (lVecFascMS != null && lVecFascMS.size() > 0)) {
						BigDecimal collegato = ((FascMsToFascSiepModel) lVecFascMS.get(0))
								.getFasSieIdFascicoloCollegato();
						if (collegato != null) {
							lEventoMs.setFasSieIdFascicoloSiep(collegato);
							lEveSqlDAO.ricercaEventoPerMotivoProvv(lCodMotivoProvvedimento,
									lCodTipoProvvedimento, lEventoMs);

							lEveSqlDAO.start();
							if (lEveSqlDAO.next()) {
								lAgg = lEveSqlDAO.getModelDecretoOrdinanzaUfficio();

							}
						}
					}

					EventoModel lEveMS = null;
					if (lAgg != null && lAgg.getEventoNotifica() != null
							&& lAgg.getEventoNotifica().getEvento() != null) {
						lEveMS = lAgg.getEventoNotifica().getEvento();
					}

					if (lEveMS != null && lEveMS.getIdEvento() != null) {
						if (lEveMS.getCodTipoProvvedimento().equals("03")
								&& lAgg.getDepositoOrdinanzaPc() != null) {
							// 01/10/2020 Ticket#20200930012 - Possono esistere ordinanza non depositate per
							// cui mancanti di anno e numero. Andavano in nullpointer sul toString().
							// Aggiunto test su != null
							// NUMERO ORDINANZA
							if (lAgg.getDepositoOrdinanzaPc().getNumS3() != null)
								lMis.setNumOrdDec(lAgg.getDepositoOrdinanzaPc().getNumS3().toString());
							// ANNO ORDINANZA
							if (lAgg.getDepositoOrdinanzaPc().getAnnoS3() != null)
								lMis.setAnnoOrdDec(lAgg.getDepositoOrdinanzaPc().getAnnoS3().toString());
							// END 01/10/2020 Ticket#20200930012
						} else if (lEveMS.getCodTipoProvvedimento().equals("02")
								&& lAgg.getDepositoDecreto() != null) {
							// NUMERO DECRETO
							if (lAgg.getDepositoDecreto().getNumS72() != null)
								lMis.setNumOrdDec(lAgg.getDepositoDecreto().getNumS72().toString());
							// ANNO DECRETO
							if (lAgg.getDepositoDecreto().getAnnoS72() != null)
								lMis.setAnnoOrdDec(lAgg.getDepositoDecreto().getAnnoS72().toString());
						}

						// LUOGO EMITTENTE
						lMis.setLuogoEmittente(lEveMS.getDescrLuogoEmittente());
						// DATA EMISSIONE
						lMis.setDataEmissione(lEveMS.getDataEmissione());
						// COD ESITO
						lMis.setCodEsito(lEveMS.getCodEsito());
						// DESC ESITO
						lMis.setDescEsitoTemplate(lEveMS.getDescEsitoTemplate());
						// AUTORITA EMITTENTE (aggiungo post collaudo 11.3 per MEV 39)
						lMis.setDescrUfficioInserimento(lEveMS.getDescrUfficioEmittente());
						// TIPO PROVVEDIMENTO (DECRETO O ORDINANZA)
						lMis.setDescTipoOrdDec(lEveMS.getDescrTipoProvvedimento());
						// MOTIVO PROVVEDIMENTO
						lMis.setDescrMotivoOrdDec(lEveMS.getDescrMotivo());

					} else {
						// NON ESISTE DECISIONE DEL MAGISTRATO DI SORVEGLIANZA
					}
					// mev56 FINE ****************************

					TreeModel lTreeMisureSicMod = new TreeModel(lMis);
					aTreeFasMod.add(lTreeMisureSicMod);

					// 15-01-2015 - Add Eventuale RiferimentoFascicoloSIEP
					if (lMis.getFasSieIdFascicoloSiepRif() != null) {
						RiferimentoFascicoloSiepModel lRifMod = null;
						IRiferimentoFascicoloSiep lCtrlS = SIUSLookupRemote
								.getRiferimentoFascicoloSiepRemote();
						lRifMod = lCtrlS
								.ExRicercaRiferimentoFascicoloSiepByKey(lMis.getFasSieIdFascicoloSiepRif());
						if (lRifMod != null && lRifMod.getIdRiferimentoFascicoloSiep() != null) {
							lTreeMisureSicMod.add(new TreeModel(lRifMod));
						}
					}

					// 03-03-2015 - Add Eventuale IstitutoDetenzione per Misura
					// Detentiva
					if (lMis.getIstDetIdIstitutoDetenzione() != null) {
						IIstitutoDetenzione lIst = SIEPLookupRemote.getIstitutoDetenzioneRemote();
						IstitutoDetenzioneModel IstModel = new IstitutoDetenzioneModel();
						IstModel = lIst
								.ExRicercaIstitutoDetenzioneByKey(lMis.getIstDetIdIstitutoDetenzione());
						if (IstModel != null && IstModel.getIdIstitutoDetenzione() != null) {
							lTreeMisureSicMod.add(new TreeModel(IstModel));
						}
					}

				}
			}

			// Add Fasc_MS_to_Fasc_SIEP

			if (lVecFascMS != null && lVecFascMS.size() > 0) {
				lItx = lVecFascMS.iterator();
				while (lItx.hasNext()) {
					FascMsToFascSiepModel lFascMS_SIEP = (FascMsToFascSiepModel) lItx.next();
					TreeModel lTreeFascMS_SIEP = new TreeModel(lFascMS_SIEP);
					aTreeFasMod.add(lTreeFascMS_SIEP);
				}
			}

			// Add Pene Accessorie
			if (lPeneAccessorie != null) {
				lItx = lPeneAccessorie.iterator();
				while (lItx.hasNext()) {
					PenaAccessoriaModel lPen = (PenaAccessoriaModel) lItx.next();
					if (lPen != null) {
						TreeModel lTreePenAccMod = new TreeModel(lPen);
						aTreeFasMod.add(lTreePenAccMod);
					}
				}
			}

			if (lPenSanMod != null && lPenSanMod.getPenaComplessiva() != null) {
				TreeModel lTreePenMod = new TreeModel(lPenSanMod.getPenaComplessiva());

				if (lContinuazioni != null) {
					Iterator lItxContinua = lContinuazioni.iterator();
					while (lItxContinua.hasNext()) {
						ContinuazioneModel lCont = (ContinuazioneModel) lItxContinua.next();
						if (lCont != null)
							lTreePenMod.add(new TreeModel(lCont));
					}
				}

				aTreeFasMod.add(lTreePenMod);
			}
			if (lPenSanMod != null && lPenSanMod.getSanzioneSostitutiva() != null) {
				TreeModel lTreeSanMod = new TreeModel(lPenSanMod.getSanzioneSostitutiva());
				aTreeFasMod.add(lTreeSanMod);
			}
		} catch (DAOException daoEx) {
			daoEx.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("StampaController.appendTableToFascicoloSiep: " + daoEx);
		} catch (SQLException sqe) {
			sqe.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("SQLException: " + sqe);
			throw new F3BException("StampaController.appendTableToFascicoloSiep: " + sqe);
		} catch (Exception sqe) {
			sqe.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("Exception: " + sqe, sqe);
			throw new F3BException("StampaController.appendTableToFascicoloSiep: Eccezione Generica: " + sqe);
		} finally {
			cleanup(lReaDao);
			cleanup(lBenDao);
			cleanup(lMisDao);
			cleanup(lPenDao);
			cleanup(lLuoDao);
			cleanup(lPosGiuDao);
			cleanup(lPenAccDao);
			cleanup(lSanDao);
			cleanup(lMisSicDao);
			cleanup(lCircDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lContSqlDAO);
			cleanup(lEveSqlDAO);
		}
	}

	/**
	 * getTreeUltimeAnnotazioniManuali
	 *
	 * @param aKeyFascicolo
	 * @param aTreeFasc
	 * @return
	 * @throws F3BException
	 */
	protected TreeModel getTreeUltimeAnnotazioniManuali(BigDecimal aKeyFascicolo, TreeModel aTreeFasc,
			EventoModel aEventoCorrente) throws F3BException {

		Connection lConn = null;

		FungibilitaSqlDAO lFungDAO = null;
		PenaResiduaSqlDAO lPenaResDao = null;

		try {
			lConn = getDBConnection();

			// Annotazioni Manuali
			IUltimaAnnotazioneManuale lUlt = SIEPLookupRemote.getUltimaAnnotazioneManuale();
			Vector lVectUlti = lUlt.ExRicercaUltimeAnnotazioniManuali(aKeyFascicolo, aEventoCorrente);
			Iterator lItx;

			if (lVectUlti != null) {
				lItx = lVectUlti.iterator();
				while (lItx.hasNext()) {
					UltimaAnnotazioneModel lUltiModel = (UltimaAnnotazioneModel) lItx.next();

					TreeModel lTreeUltiAnnotazione = new TreeModel(lUltiModel);
					int count = 0;
					if (lUltiModel != null && lUltiModel.getVectAnnotazioneManuale() != null) {
						lUltiModel.calcolaStringaTotPeriodiRecArr();

						while (count < lUltiModel.getVectAnnotazioneManuale().size()) {
							AnnotazioneManualeModel lAnnoModel = (AnnotazioneManualeModel) lUltiModel
									.getVectAnnotazioneManuale().get(count);
							lAnnoModel.calcolaStringaArresto();
							lAnnoModel.calcolaStringaReclusione();
							TreeModel lTreeAnno = new TreeModel(lAnnoModel);

							// Se esiste aggiungo il reato all'Annotazione
							// Manuale
							if (lAnnoModel.getReaIdReato() != null) {

								/*
								 * ISSUE MEV : eseguito merge tra 15 MEV: interpretazione con codice
								 * commentato Numero MEV : SIES v10 Autore : gioggi Data : 15/feb/2016 Branch
								 * : MEV_SIES v10
								 */
								// IReato lReaCtr =
								// SIEPLookupRemote.getReatoRemote();
								// Vector lReati =
								// lReaCtr.ExRicercaReatoCircostanzaByFascicolo(aKeyFascicolo);
								// if (lReati != null) {
								// Vector lReatiPerCont = new Vector();
								// lItx = lReati.iterator();
								// while (lItx.hasNext()) {
								// // Add gerarchia del Reato collegato
								// all'Annotazione_Manuale
								// ReatoCircostanzaModel lReatoModel = new
								// ReatoCircostanzaModel(
								// (ReatoCircostanzaModel) lItx.next());
								// if (lReatoModel.getReato().getIdReato()
								// .equals(lAnnoModel.getReaIdReato())) {
								// TreeModel lTreeReatoMod = new
								// TreeModel(lReatoModel.getReato());
								// ReatoModel lReato = lReatoModel.getReato();
								// if (lReato != null) {
								// lReato.calcolaPenaReato();
								// lReato.calcolaStringaPenaPecuniaria();
								// }
								// lReatiPerCont.add(lReato);
								// int countCir = 0; // Circostanze
								// while (countCir <
								// lReatoModel.getCircostanze().length) {
								// TreeModel lTreeCirc = new TreeModel(
								// lReatoModel.getCircostanze()[countCir]);
								// lTreeReatoMod.add(lTreeCirc);
								// countCir++;
								// }
								// lTreeAnno.add(lTreeReatoMod);
								// }
								// }
								// }
								// }
								// ***** FINE INTERVENTO MEV_SIES v10 *****//

								ReatoSqlDAO lReaDao = new ReatoSqlDAO(lConn);
								lReaDao.ricercaReatoByKey(lAnnoModel.getReaIdReato());
								ReatoModel lReato = (ReatoModel) lReaDao.getModelByKey();
								if (lReato != null)
									lTreeAnno.add(new TreeModel(lReato));
							}
							lTreeUltiAnnotazione.add(lTreeAnno);
							count++;
						}

						// Fungibilita
						lFungDAO = new FungibilitaSqlDAO(lConn);
						lFungDAO.ricercaFungibilitaByKeyEvento(lUltiModel.getIdEvento());
						FungibilitaModel lFung = (FungibilitaModel) lFungDAO.getModelByKey();
						if (lFung != null) {
							lFung.calcolaStringaFungibilita();
							lTreeUltiAnnotazione.add(new TreeModel(lFung));
						}

						// Pena Residua
						lPenaResDao = new PenaResiduaSqlDAO(lConn);
						lPenaResDao.ricercaPenaResiduaByKeyEvento(lUltiModel.getIdEvento());

						PenaResiduaModel lPenResMod = (PenaResiduaModel) lPenaResDao.getModelByKey();
						// Stringa Arresto - Reclusione
						if (lPenResMod != null) {
							lPenResMod.calcolaStringaReclusione();
							lPenResMod.calcolaStringaArresto();
							lPenResMod.calcolaStringaIsolamento();

							if (lPenResMod.getDataFine() != null
									&& lPenResMod.getDataFine().compareTo(DateUtils.getSysDate()) <= 0
									// GDV 23/10/2006 a6-rr-328
									&& lPenResMod != null && !lPenResMod.getFlagErgastolo().equals("S")
									&& !lPenResMod.getFlagErgastolo().equals("D"))
								lPenResMod.setImmediataScarcerazione("S");
							else {
								if (lPenResMod != null)
									lPenResMod.setImmediataScarcerazione("N");
							}
						}

						if (lPenResMod != null)
							lTreeUltiAnnotazione.add(new TreeModel(lPenResMod));

						lPenaResDao.stop();

						IEvento lEve = SICOLookupRemote.getEventoRemote();
						EventoNotificaModel aEveModel = lEve
								.ExRicercaEventoNotificaByKey(lUltiModel.getIdEvento());
						mEventoUtils.appendNotifiche(lTreeUltiAnnotazione, aEveModel, null, null);

						if (lUltiModel != null)
							aTreeFasc.add(lTreeUltiAnnotazione);
					}
				}
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("StampaController.getTreeUltimeAnnotazioniManuali: " + daoEx);
		} finally {
			cleanup(lFungDAO);
			cleanup(lPenaResDao);

			cleanup(lConn);
		}
		return aTreeFasc;
	}

	/**
	 * getTreeEventoPrecedenteAnnotazioniManuali
	 *
	 * @param aKeyFascicolo
	 * @param aEveModel
	 * @return
	 * @throws F3BException
	 */
	protected TreeModel getTreeEventoPrecedenteAnnotazioniManuali(BigDecimal aKeyFascicolo, String aCodMotivo)
			throws F3BException {

		Connection lConn = null;

		FungibilitaSqlDAO lFungDAO = null;
		PenaResiduaSqlDAO lPenaResDao = null;
		EventoPrecedenteSqlDAO lEveDAO = null;

		TreeModel lTreeAnno = null;
		TreeModel lTreeEventoPrecedente = null;
		AnnotazioneManualeModel lAnnoModel = null;
		EventoNotificaModel EveModel = null;

		try {
			lConn = getDBConnection();

			// Annotazioni Manuali
			lEveDAO = new EventoPrecedenteSqlDAO(lConn);
			// String codiceMotivo = null;
			EventoModel lEveModRic = new EventoModel();
			lEveModRic.setFasSieIdFascicoloSiep(aKeyFascicolo);
			lEveModRic.setCodTipoProvvedimento("04");
			lEveModRic.setCodTipoEvento("01");
			lEveModRic.setFlagDocumentoRegistrato("S");

			lEveModRic.setCodMotivo(aCodMotivo);
			// STUB 24/10/2005 REWORK STATO ESECUZIONE
			// lEveDAO.ricercaEvento(lEveModRic);
			String[] lProvv = { "04", "26" };
			lEveDAO.ricercaEventoPerProvvedimenti(lProvv, lEveModRic);

			EventoPrecedenteModel lEveMod = (EventoPrecedenteModel) lEveDAO.getModelByKey();
			lTreeEventoPrecedente = new TreeModel(lEveMod);

			if (lEveMod != null) {
				IAnnotazioneManuale lUlt = SIEPLookupRemote.getAnnotazioneManualeRemote();
				Vector lVectUlti = lUlt.ExRicercaAnnotazioneManualeByIdEvento(lEveMod.getIdEvento());

				if (lVectUlti.size() > 0) {
					for (int i = 0; i < lVectUlti.size(); i++) {
						lAnnoModel = (AnnotazioneManualeModel) lVectUlti.get(0);

						lAnnoModel.calcolaStringaArresto();
						lAnnoModel.calcolaStringaReclusione();
						lTreeAnno = new TreeModel(lAnnoModel);

						// Se esiste aggiungo il reato all'Annotazione Manuale
						if (lAnnoModel.getReaIdReato() != null) {
							ReatoSqlDAO lReaDao = new ReatoSqlDAO(lConn);
							lReaDao.ricercaReatoByKey(lAnnoModel.getReaIdReato());
							ReatoModel lReato = (ReatoModel) lReaDao.getModelByKey();
							if (lReato != null)
								lTreeAnno.add(new TreeModel(lReato));
						}
					}

					// Fungibilita
					lFungDAO = new FungibilitaSqlDAO(lConn);
					lFungDAO.ricercaFungibilitaByKeyEvento(lAnnoModel.getEveIdEvento());
					FungibilitaModel lFung = (FungibilitaModel) lFungDAO.getModelByKey();
					if (lFung != null) {
						lFung.calcolaStringaFungibilita();
						lTreeAnno.add(new TreeModel(lFung));
					}

					// Pena Residua
					lPenaResDao = new PenaResiduaSqlDAO(lConn);
					lPenaResDao.ricercaPenaResiduaByKeyEvento(lAnnoModel.getEveIdEvento());

					PenaResiduaModel lPenResMod = (PenaResiduaModel) lPenaResDao.getModelByKey();
					// Stringa Arresto - Reclusione
					if (lPenResMod != null) {
						lPenResMod.calcolaStringaReclusione();
						lPenResMod.calcolaStringaArresto();
						lPenResMod.calcolaStringaIsolamento();

						if (lPenResMod.getDataFine() != null
								&& lPenResMod.getDataFine().compareTo(DateUtils.getSysDate()) <= 0
								// GDV 23/10/2006 a6-rr-328
								&& lPenResMod != null && !lPenResMod.getFlagErgastolo().equals("S")
								&& !lPenResMod.getFlagErgastolo().equals("D"))
							lPenResMod.setImmediataScarcerazione("S");
						else {
							if (lPenResMod != null)
								lPenResMod.setImmediataScarcerazione("N");
						}
					}

					if (lPenResMod != null)
						lTreeAnno.add(new TreeModel(lPenResMod));

					lPenaResDao.stop();

					IEvento lEve = SICOLookupRemote.getEventoRemote();
					EveModel = lEve.ExRicercaEventoNotificaByKey(lAnnoModel.getEveIdEvento());
					mEventoUtils.appendNotifiche(lTreeAnno, EveModel, null, null);

					if (lAnnoModel != null)
						lTreeEventoPrecedente.add(lTreeAnno);
				}
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("StampaController.getTreeEventoPrecedenteAnnotazioniManuali: " + daoEx);
		} finally {
			cleanup(lFungDAO);
			cleanup(lPenaResDao);
			cleanup(lEveDAO);

			cleanup(lConn);
		}

		return lTreeEventoPrecedente;
	}

	/**
	 * getTreeEventoPrecedenteOEPerIstanza
	 *
	 * @param aKeyFascicolo
	 * @param aEveModel
	 * @return
	 * @throws F3BException
	 */
	protected TreeModel getTreeEventoPrecedenteOEPerIstanza(BigDecimal aKeyFascicolo) throws F3BException {

		Connection lConn = null;
		EventoPrecedenteSqlDAO lEveDAO = null;
		TreeModel lTreeEventoPrecedente = null;

		try {
			lConn = getDBConnection();
			lEveDAO = new EventoPrecedenteSqlDAO(lConn);
			EventoModel lEveModRic = new EventoModel();
			lEveModRic.setFasSieIdFascicoloSiep(aKeyFascicolo);
			lEveModRic.setCodTipoProvvedimento("06");
			lEveModRic.setCodTipoEvento("01");
			lEveModRic.setFlagDocumentoRegistrato("S");

			// lEveModRic.setCodMotivo(aEveModel.getCodMotivo());
			lEveDAO.ricercaEvento(lEveModRic);
			EventoPrecedenteModel lEveMod = (EventoPrecedenteModel) lEveDAO.getModelByKey();
			lTreeEventoPrecedente = new TreeModel(lEveMod);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("StampaController.getTreeEventoPrecedenteOEPerIstanza: " + daoEx);
		} finally {
			cleanup(lEveDAO);

			cleanup(lConn);
		}
		return lTreeEventoPrecedente;
	}

	/**
	 * Ricava l'albero del soggetto con Alias e Residenza
	 *
	 * @param lKeySoggetto
	 * @return
	 */
	protected TreeModel getTreeSoggetto(BigDecimal lKeySoggetto, BigDecimal lKeyFascicolo, Connection lConn,
			FascicoloSiepModel lFasModel) throws F3BException {

		SoggettoSqlDAO lSogDao = null;
		ResidenzaSqlDAO lResDao = null;
		AliasSqlDAO lAliasSqlDAO = null;
		SentenzaSqlDAO lSenDao = null;
		Iterator lItx = null;

		try {
			// Soggetto
			lSogDao = new SoggettoSqlDAO(lConn);
			lSogDao.ricercaSoggettoByKey(lKeySoggetto);
			SoggettoModel lSogModel = (SoggettoModel) lSogDao.getModelByKey();

			// Indica se il soggetto e' Maggiorenne o Minorenne
			String minorMagg = "";

			// Sentenza
			SentenzaModel lSentenza = null;
			if (lFasModel != null && lFasModel.getSenIdSentenza() != null) {
				lSenDao = new SentenzaSqlDAO(lConn);
				lSenDao.ricercaSentenzaBykey(lFasModel.getSenIdSentenza());
				lSentenza = (SentenzaModel) lSenDao.getModelByKey();
			}

			// Reati
			IReato lReaCtr = SIEPLookupRemote.getReatoRemote();
			Vector lReati = lReaCtr.ExRicercaReatoCircostanzaByFascicolo(lKeyFascicolo);

			if (lFasModel != null) {
				minorMagg = mEventoUtils.checkMinorMagg(lReati, lSogModel, lFasModel, lSentenza);
			}

			lSogModel.setStatoMinorMagg(minorMagg);

			// Alias
			lAliasSqlDAO = new AliasSqlDAO(lConn);
			lAliasSqlDAO.ricercaAliasByIdSoggetto(lKeySoggetto);
			Vector lAlias = new Vector(lAliasSqlDAO.getModels());

			// Residenza
			lResDao = new ResidenzaSqlDAO(lConn);
			lResDao.ricercaResidenzaByFascicoloXStampa(lKeyFascicolo);
			Vector lResidenze = new Vector(lResDao.getModels());

			TreeModel lTreeSogMod = new TreeModel(lSogModel);
			// Aggiungere Residenze e DOmicili
			if (lResidenze != null) {
				lItx = lResidenze.iterator();
				while (lItx.hasNext()) {
					ResidenzaModel lResModel = new ResidenzaModel((ResidenzaModel) lItx.next());
					lTreeSogMod.add(new TreeModel(lResModel));
				}
			}
			// Aggiungere Alias
			if (lAlias != null) {
				lItx = lAlias.iterator();
				while (lItx.hasNext()) {
					AliasModel lAliasModel = new AliasModel((AliasModel) lItx.next());
					lTreeSogMod.add(new TreeModel(lAliasModel));
				}
			}

			return lTreeSogMod;
		} catch (DAOException daoEx) {
			daoEx.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("SIAPStampaController.getTreeSoggetto: " + daoEx);
		} catch (Exception sqe) {
			sqe.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("Exception: " + sqe);
			throw new F3BException("SIAPStampaController.getTreeSoggetto: " + sqe);
		} finally {
			cleanup(lAliasSqlDAO);
			cleanup(lResDao);
			cleanup(lSogDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lSenDao);
		}
	}

	/**
	 * getPenaCumulo
	 *
	 * @param lKeyFascicolo
	 * @param lConn
	 * @return
	 * @throws F3BException
	 */
	protected TreeModel getPenaCumulo(BigDecimal lKeyFascicolo, Connection lConn) throws F3BException {

		LicenzaLibanticipataSqlDAO lLibDAO = null;
		PenaResiduaSqlDAO lPenResDao = null;
		CumuloSqlDAO lCumuloSQl = null;
		EventoSqlDAO lEveSqlDAO = null;
		PenaCumuloSqlDAO lPenaCumuloSQl = null;
		PenaRideterminataCumuloSqlDAO lPenaRidetermSqlDao = null;

		PenaCumuloModel lPenCumMod = new PenaCumuloModel();
		TreeModel lPenCumModTree = null;

		try {
			lPenResDao = new PenaResiduaSqlDAO(lConn);
			lCumuloSQl = new CumuloSqlDAO(lConn);
			lPenaCumuloSQl = new PenaCumuloSqlDAO(lConn);
			lEveSqlDAO = new EventoSqlDAO(lConn);

			EventoModel lEveCumuloRic = new EventoModel();
			lEveCumuloRic.setFlagDocumentoRegistrato("S");
			lEveCumuloRic.setFasSieIdFascicoloSiep(lKeyFascicolo);
			// lEveCumuloRic.setCodMotivo("0277");
			lEveCumuloRic.setCodTipoProvvedimento("04");
			lEveCumuloRic.setCodTipoEvento("01");

			String[] lCodMotivoProvvedimentoOld = { "0222", "0223", "0224", "0277" };

			// Codici che caratterizzano la nuova versione del Cumulo
			Collection<DecodeModel> lListaCodiciCumNew = DecodificheManager.getInstance()
					.getMotiviProvvCumuloNew();
			List<String> lListaCodici = new ArrayList<>();
			for (DecodeModel lDecode : lListaCodiciCumNew) {
				lListaCodici.add(lDecode.getCode());
			}
			// String[] lCodMotivoProvvedimentoNewCumulo = lListaCodici.toArray(new String[0]);
			List<String> lListaCodiciAll = new ArrayList<>();
			lListaCodiciAll.add("0222");
			lListaCodiciAll.add("0223");
			lListaCodiciAll.add("0224");
			lListaCodiciAll.add("0277");
			lListaCodiciAll.addAll(lListaCodici);
			String[] lCodMotivoProvvedimento = lListaCodiciAll.toArray(new String[0]);

			lEveSqlDAO.ricercaEventoPerMotivoOrderDesc(lCodMotivoProvvedimento, lEveCumuloRic);
			EventoModel lEveCumulo = (EventoModel) lEveSqlDAO.getModelByKey();
			if (lEveCumulo != null && lEveCumulo.getIdEvento() != null) {
				// Evento del Vecchio Cumulo
				if (Arrays.asList(lCodMotivoProvvedimentoOld).contains(lEveCumulo.getCodMotivo())) {
					// ricerca del cumulo validato
					CumuloModel lCumMod = new CumuloModel();
					PenaCumuloModel lPenaCumuloLA = new PenaCumuloModel();

					lCumuloSQl
							.ricercaFascicoliCumulobyIdFascicoloSiepValidatoDataCumuloNotNull(lKeyFascicolo);
					Vector lVectCum = new Vector(lCumuloSQl.getModels());

					if (lVectCum != null && lVectCum.size() > 0) {
						lCumMod = (CumuloModel) lVectCum.get(0);

						// ricerca penacumulo per prendere i giorni di
						// liberazine anticipata
						if (lCumMod != null && lCumMod.getIdCumulo() != null) {
							lPenaCumuloSQl.ricercaPenaCumuloByIdCumulo(lCumMod.getIdCumulo());
							lPenaCumuloLA = (PenaCumuloModel) lPenaCumuloSQl.getModelByKey();
						}
					}

					// campo num giorni liberazione anticipata di pena cumulo
					if (lPenaCumuloLA != null && lPenaCumuloLA.getNumGiorniLibAnticipata() != null) {
						// n.b. sui template per i test servono i dati a 0 e non
						// a null
						if (lPenaCumuloLA.getNumGiorniLibAnticipata() != null)
							lPenCumMod.setNumGiorniLibAnticipata(lPenaCumuloLA.getNumGiorniLibAnticipata());
						else
							lPenCumMod.setNumGiorniLibAnticipata(new BigDecimal(0));

						if (lPenaCumuloLA.getNumGiorniLibAnticipataLA() != null)
							lPenCumMod
									.setNumGiorniLibAnticipataLA(lPenaCumuloLA.getNumGiorniLibAnticipataLA());
						else
							lPenCumMod.setNumGiorniLibAnticipataLA(new BigDecimal(0));

						if (lPenaCumuloLA.getNumGiorniLibAnticipataSPE() != null)
							lPenCumMod.setNumGiorniLibAnticipataSPE(
									lPenaCumuloLA.getNumGiorniLibAnticipataSPE());
						else
							lPenCumMod.setNumGiorniLibAnticipataSPE(new BigDecimal(0));

						if (lPenaCumuloLA.getNumGiorniLibAnticipataINT() != null)
							lPenCumMod.setNumGiorniLibAnticipataINT(
									lPenaCumuloLA.getNumGiorniLibAnticipataINT());
						else
							lPenCumMod.setNumGiorniLibAnticipataINT(new BigDecimal(0));
					}

					if (lPenaCumuloLA != null && lPenaCumuloLA.getNumGiorniRiduzionePena() != null) {
						if (lPenaCumuloLA.getNumGiorniRiduzionePena() != null)
							lPenCumMod.setNumGiorniRiduzionePena(lPenaCumuloLA.getNumGiorniRiduzionePena());
						else
							lPenCumMod.setNumGiorniRiduzionePena(new BigDecimal(0));
					}

					lPenResDao.ricercaPenaResiduaByKeyEvento(lEveCumulo.getIdEvento());
					PenaResiduaModel lPenResModCumulo = (PenaResiduaModel) lPenResDao.getModelByKey();

					if (lPenResModCumulo != null) {
						lPenResModCumulo.calcolaStringaArresto();
						lPenResModCumulo.calcolaStringaReclusione();
						lPenResModCumulo.calcolaStringaIsolamento();

						if (lPenResModCumulo.getStringaIsolamentoDiurno() != null)
							lPenCumMod.setStringaIsolamentoDiurno(
									lPenResModCumulo.getStringaIsolamentoDiurno());
						if (lPenResModCumulo.getStringaReclusione() != null)
							lPenCumMod.setStringaReclusione(lPenResModCumulo.getStringaReclusione());
						if (lPenResModCumulo.getStringaArresto() != null)
							lPenCumMod.setStringaArresto(lPenResModCumulo.getStringaArresto());
						if (lPenResModCumulo.getImportoAmmenda().compareTo(new BigDecimal(0)) != 0)
							lPenCumMod.setImportoAmmenda(lPenResModCumulo.getImportoAmmenda());

						if (lPenResModCumulo.getImportoMulta().compareTo(new BigDecimal(0)) != 0)
							lPenCumMod.setImportoMulta(lPenResModCumulo.getImportoMulta());

						if (lPenResModCumulo.getFlagErgastolo() != null)
							lPenCumMod.setFlagErgastolo(lPenResModCumulo.getFlagErgastolo());

					}

					if (lEveCumulo != null && lEveCumulo.getDataEmissione() != null) {
						lPenCumMod.setDataEmissione(lEveCumulo.getDataEmissione());
					}

					lPenCumModTree = new TreeModel(lPenCumMod);
					lLibDAO = new LicenzaLibanticipataSqlDAO(lConn);
					lLibDAO.ricercaLicenzaLibanticipataByEve(lEveCumulo.getIdEvento());

					// DL 146 + DL92 i record LA possono esser più di uno
					// (LA,LS,LI,RD)
					Vector<LicenzaLibAnticipataModel> lListaLicenze = new Vector<LicenzaLibAnticipataModel>(
							lLibDAO.getModels());

					if (lListaLicenze != null && lListaLicenze.size() > 0) {
						for (int i = 0; i < lListaLicenze.size(); i++) {
							LicenzaLibAnticipataModel lLicenzaModel = lListaLicenze.elementAt(i);
							if (lLicenzaModel != null && lLicenzaModel.getNumeroGiorni().intValue() != 0) {
								TreeModel lLibCumuloTree = new TreeModel(lLicenzaModel);
								lPenCumModTree.add(lLibCumuloTree);
							}
						}
					}
				} else {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di
					// istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug(
							"SIAPStampaController - getPenaCumulo() - Nella parte nuova... lEveCumulo = "
									+ lEveCumulo);

					// Recupero La Pena rideterminata in cumulo
					lPenaRidetermSqlDao = new PenaRideterminataCumuloSqlDAO(lConn);
					lPenaRidetermSqlDao
							.ricercaPenaResiduaCumulByIdIstr(lEveCumulo.getIstruIdIstruttoriaCumulo());
					PenaRideterminataCumuloModel lPenRidet = (PenaRideterminataCumuloModel) lPenaRidetermSqlDao
							.getModelByKey();

					lPenCumMod = lPenRidet.getPenaCumuloModel();

					lPenCumMod.calcolaStringaIsolamento();
					lPenCumMod.calcolaStringaReclusione();
					lPenCumMod.calcolaStringaArresto();

					// Completo con la data Emissione
					if (lEveCumulo != null && lEveCumulo.getDataEmissione() != null) {
						lPenCumMod.setDataEmissione(lEveCumulo.getDataEmissione());
					}

					lPenCumModTree = new TreeModel(lPenCumMod);

				}
			} // end if (lEveCumulo != null && lEveCumulo.getIdEvento()!=null)
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("SIAPStampaController.getPenaCumulo: " + daoEx, daoEx);
			throw new F3BException("SIAPStampaController.getPenaCumulo: " + daoEx);
		} catch (Exception sqe) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("SIAPStampaController.getPenaCumulo: " + sqe, sqe);
			throw new F3BException("SIAPStampaController.getPenaCumulo: Eccezione Generica: " + sqe);
		} finally {
			cleanup(lPenResDao);
			cleanup(lLibDAO);
			cleanup(lCumuloSQl);
			cleanup(lPenaCumuloSQl);
			cleanup(lEveSqlDAO);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lPenaRidetermSqlDao);
		}
		return lPenCumModTree;
	}

	/**
	 * Add la posizione giuridca al TreeModel del fascicolo Siep
	 */
	protected void addPosizioneGiuridica(Connection lConn, BigDecimal lKeyFascicolo, TreeModel aTreeFasMod,
			String aAltraCausa) throws F3BException {

		// 20170927: [SG] risolve allegato A pec 25/09 (cambio la query come
		// dettaglio fascicolo)
		// se e' libero non deve stampare il luogo detenzione!!!
		// PosizioneGiuridicaSqlDAO lPosGiuDao = null;
		AltraCausaSqlDAO lAltCauDao = null;

		try {
			// Posizione Giuridica
			// lPosGiuDao = new PosizioneGiuridicaSqlDAO(lConn);
			// lPosGiuDao.ricercaPosGiuCorrenteByIdFascicolo(lKeyFascicolo);
			// PosizioneGiuridicaModel lPosMod = (PosizioneGiuridicaModel)
			// lPosGiuDao.getModelByKey();
			IPosizioneGiuridica ipg = SIEPLookupRemote.getPosizioneGiuridicaRemote();
			PosizioneGiuridicaLuogoDetenzioneAltraCausaModel pgldacm = ipg
					.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaMisuraCautelareCorrentiByIdFascicolo(
							lKeyFascicolo);
			PosizioneGiuridicaModel lPosMod = pgldacm.getPosizioneGiuridica();

			if (lPosMod == null)
				return;

			if (aAltraCausa == null || (aAltraCausa != null && aAltraCausa.compareTo("S") != 0)) {
				// ILuogoDetenzione lLuogo =
				// SIEPLookupRemote.getLuogoDetenzioneRemote();
				// LuogoDetenzioneModel lLuoMod = lLuogo
				// .ExRicercaLuogoDetenzioneCorrenteByFascicoloSiep(lKeyFascicolo);
				LuogoDetenzioneModel lLuoMod = pgldacm.getLuogoDetenzione();
				if (lLuoMod != null) {
					if (lLuoMod.getIstitutoDetenzione() != null) {
						lLuoMod.setDescrTipoIstituto(lLuoMod.getIstitutoDetenzione().getDescrTipoIstituto());
						if (lLuoMod.getIstitutoDetenzione().getIndirizzo() != null) {
							lLuoMod.setDescrLuogo(lLuoMod.getIstitutoDetenzione().getDescrComune() + ", "
									+ lLuoMod.getIstitutoDetenzione().getIndirizzo());
						} else {
							lLuoMod.setDescrLuogo(lLuoMod.getIstitutoDetenzione().getDescrComune());
						}
						lLuoMod.setDescr(lLuoMod.getIstitutoDetenzione().getDescrizione());
					} else
						lLuoMod.setDescrTipoIstituto(lLuoMod.getAltroLuogo());
					aTreeFasMod.add(new TreeModel(lLuoMod));
				}
			} else {
				// Altra Causa
				// Modifica del 26/10/2015
				// Nota: per i fascicoli inseriti dopo la MEV 10 la ricerca di
				// Altra Causa
				// si ottiene per chiave (dopo aver recuperato il valore dal
				// campo
				// ALT_CAU_ID_ALTRA_CAUSA presente sulla tabella
				// POSIZIONE_GIURIDICA)
				// per i fascicoli precedenti alla MEV 10, non essendo
				// valorizzato il campo
				// ALT_CAU_ID_ALTRA_CAUSA, la ricerca si ottiene per
				// FAS_SIE_ID_FASCICOLO_SIEP
				// In quest'ultimo caso la ricerca potrebbe restituire piu' di
				// un record, ma viene
				// visualizzato il primo record della lista.
				IAltraCausa lAltraCausa = SIEPLookupRemote.getAltraCausa();
				AltraCausaModel lAltCau = null;
				if (lPosMod.getAltCauIdAltraCausa() != null
						&& !lPosMod.getAltCauIdAltraCausa().toString().equals("")) {
					lAltCauDao = new AltraCausaSqlDAO(lConn);
					lAltCauDao.ricercaAltraCausaByKey(lPosMod.getAltCauIdAltraCausa());
					lAltCau = (AltraCausaModel) lAltCauDao.getModelByKey();
				} else {
					lAltCau = lAltraCausa.ExRicercaAltraCausaIstitutoByFascicolo(lKeyFascicolo);
				}

				if (lAltCau != null) {
					if (lAltCau.getIstitutoDetenzione() != null) {
						lAltCau.setDescrTipoIstituto(lAltCau.getIstitutoDetenzione().getDescrTipoIstituto());

						if (lAltCau.getIstitutoDetenzione().getIndirizzo() != null) {
							lAltCau.setDescrLuogo(lAltCau.getIstitutoDetenzione().getDescrComune() + ", "
									+ lAltCau.getIstitutoDetenzione().getIndirizzo());
						} else {
							lAltCau.setDescrLuogo(lAltCau.getIstitutoDetenzione().getDescrComune());
						}
						lAltCau.setDescrizioneTipoIstituto(lAltCau.getIstitutoDetenzione().getDescrizione());
					} else if (lAltCau.getIstDetIdIstitutoDetenzione() != null
							&& !lAltCau.getIstDetIdIstitutoDetenzione().equals("")) {
						IstitutoDetenzioneModel lIstModel = null;
						IstitutoDetenzioneSqlDAO lIstDAO = null;
						lIstDAO = new IstitutoDetenzioneSqlDAO(lConn);
						lIstDAO.ricercaIstitutoDetenzioneByKey(lAltCau.getIstDetIdIstitutoDetenzione());
						lIstModel = (IstitutoDetenzioneModel) lIstDAO.getModelByKey();

						if (lIstModel != null) {
							lAltCau.setDescrTipoIstituto(lIstModel.getDescrTipoIstituto());
						}

						if (lIstModel.getIndirizzo() != null) {
							lAltCau.setDescrLuogo(
									lIstModel.getDescrComune() + ", " + lIstModel.getIndirizzo());
						} else {
							lAltCau.setDescrLuogo(lIstModel.getDescrComune());
						}
						lAltCau.setDescrizioneTipoIstituto(lIstModel.getDescrizione());
					}
				}

				// Add Luogo di Detenzione
				aTreeFasMod.add(new TreeModel(lAltCau));
			}
			// }

			// modifica integrazione REGE-SIES
			// avvaloro il campo mFlagIsDetenuto
			// verifico che non sia deceduto (???? ==> CHIEDERE A GIS)

			if (lPosMod != null) {
				boolean flagDead = false;

				if (lPosMod.getCodPosizioneGiuridica().equals("48"))
					flagDead = true;
				if (!lPosMod.isMisSosp() && !lPosMod.isLibero() && !flagDead)
					lPosMod.setFlagIsDetenuto("S");

				// if (lPosMod != null) //Posizione Giuridica
				aTreeFasMod.add(new TreeModel(lPosMod));
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("SIAPStampaController.addPosizioneGiuridica: " + daoEx, daoEx);
			throw new F3BException("SIAPStampaController.addPosizioneGiuridica: " + daoEx);
		} catch (Exception sqe) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("SIAPStampaController.addPosizioneGiuridica: " + sqe, sqe);
			throw new F3BException("SIAPStampaController.addPosizioneGiuridica: Eccezione Generica: " + sqe);
		} finally {
			// cleanup(lPosGiuDao);
			cleanup(lAltCauDao);
		}
	}

	/**
	 * Ricava la sentenza e le sentenze riunite utilizzando il model FascicoloSiepPadreModel
	 *
	 * @param FascicoloSiepPadreModel
	 * @return
	 */
	protected TreeModel getTreeSentenza(FascicoloSiepPadreModel lFasModel, Connection lConn)
			throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
		// siesLogger al posto di mLog
		siesLogger.debug("SIAPStampaController.getTreeSentenza con FascicoloSiepPadreModel:");

		SentenzaSqlDAO lSenDao = null;
		// SentenzaRiunitaSqlDAO lSenRiuDao = null;
		SentenzaRiunitaFascSiepSqlDAO lSenRiuDao = null;

		// Iterator lItx = null;
		TreeModel lTreeSenMod = null;
		TreeModel LTreeSR = null;

		try {
			// modifico questo controller poiche' le sentenze riunite non si
			// trovano piu' partendo dalla
			// sentenza
			// ma si trovano partendo dal fascicolo-siep poiche' anche se
			// agganciate alla sentenza non sono
			// necessariamente
			// collegate al fascicolo. Paolo Cherubini 01/01/2010

			// Sentenza -----------------------------
			lSenDao = new SentenzaSqlDAO(lConn);
			lSenDao.ricercaSentenzaBykey(lFasModel.getSenIdSentenza());
			// lSenDao.ricercaSentenzaBykey(lKeySentenza);
			SentenzaModel lSenModel = (SentenzaModel) lSenDao.getModelByKey();

			// Nel caso il numero sentenza cominci per NC non viene considerato
			// il resto del dato
			if (lSenModel != null && lSenModel.getNumeroSentenza() != null
					&& lSenModel.getNumeroSentenza().toUpperCase().startsWith("NC")) {
				lSenModel.setNumeroSentenza("NC");
			}

			// MEV_39
			// in base al codice tipo autorità emittente riesco a capire se è
			// una misura disposta fuori sentenza (TDS, TDSM, UDS, UDSM) oppure
			// NO!
			String[] autoritaFS = { "TDS", "TDSM", "UDS", "UDSM" };
			if (lSenModel != null && lSenModel.getCodTipoAutoritaEmittente() != null) {
				String autoritaEmittente = lSenModel.getCodTipoAutoritaEmittente();
				if (Arrays.binarySearch(autoritaFS, autoritaEmittente) >= 0) {
					lSenModel.setTipologiaProcMS("FS");
				} else {
					lSenModel.setTipologiaProcMS("PR");
				}
			}

			lTreeSenMod = new TreeModel(lSenModel);

			// Sentenza Riunita -----------------------
			SentenzaRiunitaModel lSenRiuMod = new SentenzaRiunitaModel();
			// lSenRiuMod.setSenIdSentenza(lSenModel.getIdSentenza());

			// lSenRiuDao = new SentenzaRiunitaSqlDAO(lConn);
			// lSenRiuDao.ricercaSentenzaRiunita(lSenRiuMod);
			SentenzaRiunitaFascSiepModel lSenRiuModFas = new SentenzaRiunitaFascSiepModel();
			lSenRiuModFas.setFasSieIdFascicoloSiep(lFasModel.getIdFascicoloSiep());
			lSenRiuMod.setSenIdSentenza(lSenModel.getIdSentenza());
			lSenRiuModFas.setSentenzaRiunitaModel(lSenRiuMod);
			lSenRiuDao = new SentenzaRiunitaFascSiepSqlDAO(lConn);
			lSenRiuDao.ricercaSentenzaRiunitaFascSiep(lSenRiuModFas);

			// Vector<SentenzaRiunitaModel> lVectSR = new
			// Vector<SentenzaRiunitaModel>(lSenRiuDao.getModels());
			Vector<SentenzaRiunitaFascSiepModel> lVectSR = new Vector<SentenzaRiunitaFascSiepModel>(
					lSenRiuDao.getModels());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.debug("lVectSR!=null");

			if (lVectSR != null) {
				// Iterator<SentenzaRiunitaModel> lItSR = lVectSR.iterator();
				Iterator<SentenzaRiunitaFascSiepModel> lItSR = lVectSR.iterator();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di
				// istanza siesLogger al posto di mLog
				siesLogger.debug("Iterator");
				while (lItSR.hasNext()) {
					// lSenRiuMod = (SentenzaRiunitaModel) lItSR.next();
					lSenRiuModFas = lItSR.next();
					if (lSenRiuModFas.getFasSieIdFascicoloSiep() != null) {
						lSenRiuMod = lSenRiuModFas.getSentenzaRiunitaModel();
						LTreeSR = new TreeModel(lSenRiuMod);
						lTreeSenMod.add(LTreeSR);
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile
						// di istanza siesLogger al posto
						// di mLog
						siesLogger.debug("lSenRiuMod = " + lSenRiuMod);
					}
				}
			}
		} catch (Exception sqe) {
			sqe.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("Exception: " + sqe);
			throw new F3BException("SIAPStampaController.getTreeSentenza con FascicoloSiepModel: " + sqe);
		} finally {
			cleanup(lSenDao);
			cleanup(lSenRiuDao);
		}

		return lTreeSenMod;
	}

	/**
	 * Ricava la sentenza e le sentenze riunite utilizzando il model FascicoloSiepModel
	 *
	 * @param FascicoloSiepModel
	 * @return
	 */
	protected TreeModel getTreeSentenza(FascicoloSiepModel lFasModel, Connection lConn) throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
		// siesLogger al posto di mLog
		siesLogger.debug("SIAPStampaController.getTreeSentenza con FascicoloSiepModel:");

		SentenzaSqlDAO lSenDao = null;
		// SentenzaRiunitaSqlDAO lSenRiuDao = null;
		SentenzaRiunitaFascSiepSqlDAO lSenRiuDao = null;

		// Iterator lItx = null;
		TreeModel lTreeSenMod = null;
		TreeModel LTreeSR = null;

		try {
			// modifico questo controller poiche' le sentenze riunite non si
			// trovano piu' partendo dalla
			// sentenza
			// ma si trovano partendo dal fascicolo-siep poiche' anche se
			// agganciate alla sentenza non sono
			// necessariamente
			// collegate al fascicolo. Paolo Cherubini 01/01/2010

			// Sentenza -----------------------------
			lSenDao = new SentenzaSqlDAO(lConn);
			lSenDao.ricercaSentenzaBykey(lFasModel.getSenIdSentenza());
			// lSenDao.ricercaSentenzaBykey(lKeySentenza);
			SentenzaModel lSenModel = (SentenzaModel) lSenDao.getModelByKey();

			// Nel caso il numero sentenza cominci per NC non viene considerato
			// il resto del dato
			if (lSenModel != null && lSenModel.getNumeroSentenza() != null
					&& lSenModel.getNumeroSentenza().toUpperCase().startsWith("NC")) {
				lSenModel.setNumeroSentenza("NC");
			}

			// MEV_39
			// in base al codice tipo autorità emittente riesco a capire se è
			// una misura disposta fuori sentenza (TDS, TDSM, UDS, UDSM) oppure
			// NO!
			String[] autoritaFS = { "TDS", "TDSM", "UDS", "UDSM" };
			if (lSenModel != null && lSenModel.getCodTipoAutoritaEmittente() != null) {
				String autoritaEmittente = lSenModel.getCodTipoAutoritaEmittente();
				if (Arrays.binarySearch(autoritaFS, autoritaEmittente) >= 0) {
					lSenModel.setTipologiaProcMS("FS");
				} else {
					lSenModel.setTipologiaProcMS("PR");
				}
			}

			lTreeSenMod = new TreeModel(lSenModel);

			// Sentenza Riunita -----------------------
			SentenzaRiunitaModel lSenRiuMod = new SentenzaRiunitaModel();
			// lSenRiuMod.setSenIdSentenza(lSenModel.getIdSentenza());

			// lSenRiuDao = new SentenzaRiunitaSqlDAO(lConn);
			// lSenRiuDao.ricercaSentenzaRiunita(lSenRiuMod);
			SentenzaRiunitaFascSiepModel lSenRiuModFas = new SentenzaRiunitaFascSiepModel();
			lSenRiuModFas.setFasSieIdFascicoloSiep(lFasModel.getIdFascicoloSiep());
			lSenRiuMod.setSenIdSentenza(lSenModel.getIdSentenza());
			lSenRiuModFas.setSentenzaRiunitaModel(lSenRiuMod);
			lSenRiuDao = new SentenzaRiunitaFascSiepSqlDAO(lConn);
			lSenRiuDao.ricercaSentenzaRiunitaFascSiep(lSenRiuModFas);

			// Vector<SentenzaRiunitaModel> lVectSR = new
			// Vector<SentenzaRiunitaModel>(lSenRiuDao.getModels());
			Vector<SentenzaRiunitaFascSiepModel> lVectSR = new Vector<SentenzaRiunitaFascSiepModel>(
					lSenRiuDao.getModels());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.debug(
					"lSenRiuModFas.setFasSieIdFascicoloSiep =" + lSenRiuModFas.getFasSieIdFascicoloSiep());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.debug(
					"lSenRiuModFas.setSentenzaRiunitaModel = " + lSenRiuModFas.getSentenzaRiunitaModel());

			if (lVectSR != null) {
				// Iterator<SentenzaRiunitaModel> lItSR = lVectSR.iterator();
				Iterator<SentenzaRiunitaFascSiepModel> lItSR = lVectSR.iterator();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di
				// istanza siesLogger al posto di mLog
				siesLogger.debug("Iterator");
				while (lItSR.hasNext()) {
					// lSenRiuMod = (SentenzaRiunitaModel) lItSR.next();
					lSenRiuModFas = lItSR.next();
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di
					// istanza siesLogger al posto di
					// mLog
					siesLogger.debug("dentro iter = " + lSenRiuModFas);
					if (lSenRiuModFas.getFasSieIdFascicoloSiep() != null) {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile
						// di istanza siesLogger al posto
						// di mLog
						siesLogger.debug("!= null");
						lSenRiuMod = lSenRiuModFas.getSentenzaRiunitaModel();
						LTreeSR = new TreeModel(lSenRiuMod);
						lTreeSenMod.add(LTreeSR);
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile
						// di istanza siesLogger al posto
						// di mLog
						siesLogger.debug("lSenRiuMod = " + lSenRiuMod);
					}
				}
			}
		} catch (Exception sqe) {
			sqe.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("Exception: " + sqe);
			throw new F3BException("SIAPStampaController.getTreeSentenza con FascicoloSiepModel: " + sqe);
		} finally {
			cleanup(lSenDao);
			cleanup(lSenRiuDao);
		}

		return lTreeSenMod;
	}

}