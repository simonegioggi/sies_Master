package siap.siep.calcolopena.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.sico.calendar.model.CalendarModel;
import siap.sico.camponota.dao.CampoNotaDAO;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.libertaanticipata.dao.LicenzaLibanticipataSqlDAO;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.misuraalternativa.dao.MisuraAlternativaDAO;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.utente.model.DatiOperazioneModel;
import siap.sico.util.CalendarUtil;
import siap.siep.SIEPException;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.annotazionemanuale.dao.AnnotazioneManualeSqlDAO;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.autoritaesterna.dao.AutoritaEsternaDAO;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.beneficio.dao.BeneficioSqlDAO;
import siap.siep.beneficio.model.BeneficioModel;
import siap.siep.calcolopena.action.ICostantiCalcoloPena;
import siap.siep.calcolopena.model.CalcoloPenaModel;
import siap.siep.cumulo.dao.CumuloDAO;
import siap.siep.cumulo.model.CumuloModel;
import siap.siep.fascicolo.dao.FascicoloSiepDAO;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.fungibilita.action.ICostantiFungibilita;
import siap.siep.fungibilita.dao.FungibilitaDAO;
import siap.siep.fungibilita.dao.FungibilitaSqlDAO;
import siap.siep.fungibilita.model.FungibilitaModel;
import siap.siep.misuracautelare.dao.MisuraCautelareSqlDAO;
import siap.siep.misuracautelare.model.MisuraCautelareModel;
import siap.siep.notifica.dao.NotificaDAO;
import siap.siep.notifica.dao.NotificaEventoSqlDAO;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.dao.PenaResiduaDAO;
import siap.siep.penaresidua.dao.PenaResiduaSqlDAO;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.dao.PosizioneGiuridicaSqlDAO;
import siap.siep.scadenzario.dao.ScadenzarioDAO;
import siap.siep.scadenzario.dao.ScadenzarioSqlDAO;
import siap.siep.sospensione.dao.SospensioneDAO;
import siap.siep.sospensione.model.SospensioneModel;
import siap.siep.statoprocedimento.dao.StatoProcedimentoDAO;
import siap.siep.statoprocedimento.dao.StatoProcedimentoSqlDAO;
import siap.siep.statoprocedimento.model.StatoProcedimentoModel;
import siap.siep.util.SIEPLookupRemote;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class CalcoloPenaController extends SiapController implements ICalcoloPena {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/*****************************************************************************
	 * Effettua la ricerca dei benefici Concessi per Reclusione per il fascicolo corrente. Somma i valori e
	 * restituisce il totale in un CalendarModel
	 *
	 * @param BigDecimal
	 *            - id del fascicolo
	 * @return CalendarModel - somma dei benefici
	 */
	public CalendarModel exGetBeneficiConcessiReclusione(BigDecimal lFascID) throws F3BException {

		Connection lConn = null;

		BeneficioSqlDAO lBenDao = null;

		CalendarModel lCalMod = new CalendarModel();
		CalendarModel lBenConc = new CalendarModel();
		CalendarUtil lCalCon = new CalendarUtil();

		BeneficioModel lBenMod = new BeneficioModel();

		lBenMod.setCodNaturaBeneficio("C"); // concesso
		lBenMod.setFasSieIdFascicoloSiep(lFascID);

		try {
			lConn = getDBConnection();

			lBenDao = new BeneficioSqlDAO(lConn);
			lBenDao.ricercaBeneficio(lBenMod);
			lBenDao.start();

			// Sommo i benefici
			while (lBenDao.next()) {
				lBenMod = (BeneficioModel) lBenDao.getModel();
				lCalMod = new CalendarModel();

				lCalMod.setNumAnni(lBenMod.getNumAnniReclusione());
				lCalMod.setNumMesi(lBenMod.getNumMesiReclusione());
				lCalMod.setNumGiorni(lBenMod.getNumGiorniReclusione());

				if (lBenMod.getImportoMulta() != null)
					lCalMod.setImportoMulta(lBenMod.getImportoMulta().doubleValue());
				else
					lCalMod.setImportoMulta(0);

				lBenConc = lCalCon.sommaGiornieValute(lBenConc, lCalMod);
			}

			// normalizza i dati ! non serve vengono già normalizzati dalla sommaGiornieValute
			lBenConc = lCalCon.ricalcolaGAM(lBenConc);

			lBenDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"CalcoloPenaController.exGetBeneficiConcessiReclusione: Non posso leggere : " + daoEx);
		} catch (Exception ex) {
			throw new F3BException(
					"CalcoloPenaController.exGetBeneficiConcessiReclusione: Non posso leggere  : " + ex);
		} finally {
			cleanup(lBenDao);
			cleanup(lConn);
		}

		return lBenConc;
	}

	/*****************************************************************************
	 * Effettua la ricerca dei benefici Concessi per Arresto per il fascicolo corrente. Somma i valori e
	 * restituisce il totale in un CalendarModel
	 *
	 * @param BigDecimal
	 *            - id del fascicolo
	 * @return CalendarModel - somma dei benefici
	 */
	public CalendarModel exGetBeneficiConcessiArresto(BigDecimal lFascID) throws F3BException {

		Connection lConn = null;
		BeneficioSqlDAO lBenDao = null;

		CalendarModel lCalMod = new CalendarModel();
		CalendarModel lBenConc = new CalendarModel();
		CalendarUtil lCalCon = new CalendarUtil();

		BeneficioModel lBenMod = new BeneficioModel();

		lBenMod.setCodNaturaBeneficio("C"); // concessi
		lBenMod.setFasSieIdFascicoloSiep(lFascID);

		try {
			lConn = getDBConnection();
			lBenDao = new BeneficioSqlDAO(lConn);
			lBenDao.ricercaBeneficio(lBenMod);
			lBenDao.start();

			// Sommo i benefici
			while (lBenDao.next()) {
				lBenMod = (BeneficioModel) lBenDao.getModel();

				lCalMod.setNumAnni(lBenMod.getNumAnniArresto());
				lCalMod.setNumMesi(lBenMod.getNumMesiArresto());
				lCalMod.setNumGiorni(lBenMod.getNumGiorniArresto());

				/**
				 * TODO ??? così li somma 2 volte? verificare (SI!!!)
				 */
				if (lBenMod.getImportoAmmenda() != null)
					lCalMod.setImportoAmmenda(lBenMod.getImportoAmmenda().doubleValue());
				else
					lCalMod.setImportoAmmenda(0);

				lBenConc = lCalCon.sommaGiornieValute(lBenConc, lCalMod);
			}
			// normalizza i dati ! non serve vengono già normalizzati dalla sommaGiornieValute
			lBenConc = lCalCon.ricalcolaGAM(lBenConc);
			lBenDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"CalcoloPenaController.exGetBeneficiConcessiArresto: Non posso leggere : " + daoEx);
		} catch (Exception ex) {
			throw new F3BException(
					"CalcoloPenaController.exGetBeneficiConcessiArresto: Non posso leggere  : " + ex);
		} finally {
			cleanup(lBenDao);
			cleanup(lConn);
		}

		return lBenConc;
	}

	/*****************************************************************************
	 * Effettua la ricerca dei benefici Revocati per Reclusione per il fascicolo corrente. Somma i valori e
	 * restituisce il totale in un CalendarModel
	 *
	 * @param lFascID
	 * @return CalendarModel - somma dei benefici
	 * @throws F3BException
	 */
	public CalendarModel exGetBeneficiRevocatiReclusione(BigDecimal lFascID) throws F3BException {

		Connection lConn = null;
		BeneficioSqlDAO lBenDao = null;

		CalendarModel lCalMod = new CalendarModel();
		CalendarModel lBenConc = new CalendarModel();
		CalendarUtil lCalCon = new CalendarUtil();

		BeneficioModel lBenMod = new BeneficioModel();

		lBenMod.setCodNaturaBeneficio("R"); // revocati
		lBenMod.setFasSieIdFascicoloSiep(lFascID);

		try {
			lConn = getDBConnection();
			lBenDao = new BeneficioSqlDAO(lConn);
			lBenDao.ricercaBeneficio(lBenMod);
			lBenDao.start();

			while (lBenDao.next()) {
				lBenMod = (BeneficioModel) lBenDao.getModel();
				lCalMod.setNumAnni(lBenMod.getNumAnniReclusione());
				lCalMod.setNumMesi(lBenMod.getNumMesiReclusione());
				lCalMod.setNumGiorni(lBenMod.getNumGiorniReclusione());

				/**
				 * TODO ??? così li somma 2 volte? verificare
				 */
				if (lBenMod.getImportoMulta() != null)
					lCalMod.setImportoMulta(
							lCalMod.getImportoMulta() + lBenMod.getImportoMulta().doubleValue());

				lBenConc = lCalCon.sommaGiornieValute(lBenConc, lCalMod);
			}

			lBenConc = lCalCon.ricalcolaGAM(lBenConc);
			lBenDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"CalcoloPenaController.exGetBeneficiRevocatiReclusione: Non posso leggere : " + daoEx);
		} catch (Exception ex) {
			throw new F3BException(
					"CalcoloPenaController.exGetBeneficiRevocatiReclusione: Non posso leggere  : " + ex);
		} finally {
			cleanup(lBenDao);
			cleanup(lConn);
		}

		return lBenConc;
	}

	/*****************************************************************************
	 * Effettua la ricerca dei benefici Revocati per Arresto per il fascicolo corrente. Somma i valori e
	 * restituisce il totale in un CalendarModel
	 *
	 * @param lFascID
	 * @return CalendarModel - somma dei benefici
	 * @throws F3BException
	 */
	public CalendarModel exGetBeneficiRevocatiArresto(BigDecimal lFascID) throws F3BException {

		Connection lConn = null;
		BeneficioSqlDAO lBenDao = null;

		CalendarModel lCalMod = new CalendarModel();
		CalendarModel lBenConc = new CalendarModel();
		CalendarUtil lCalCon = new CalendarUtil();

		BeneficioModel lBenMod = new BeneficioModel();

		lBenMod.setCodNaturaBeneficio("R"); // revocati
		lBenMod.setFasSieIdFascicoloSiep(lFascID);

		try {
			lConn = getDBConnection();
			lBenDao = new BeneficioSqlDAO(lConn);
			lBenDao.ricercaBeneficio(lBenMod);
			lBenDao.start();

			while (lBenDao.next()) {
				lBenMod = (BeneficioModel) lBenDao.getModel();

				lCalMod.setNumAnni(lBenMod.getNumAnniArresto());
				lCalMod.setNumMesi(lBenMod.getNumMesiArresto());
				lCalMod.setNumGiorni(lBenMod.getNumGiorniArresto());

				/**
				 * TODO ??? così li somma 2 volte? verificare
				 */
				if (lBenMod.getImportoAmmenda() != null)
					lCalMod.setImportoAmmenda(
							lCalMod.getImportoAmmenda() + lBenMod.getImportoAmmenda().doubleValue());

				lBenConc = lCalCon.sommaGiornieValute(lBenConc, lCalMod);
			}

			lBenConc = lCalCon.ricalcolaGAM(lBenConc);
			lBenDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"CalcoloPenaController.exGetBeneficiRevocatiArresto: Non posso leggere : " + daoEx);
		} catch (Exception ex) {
			throw new F3BException(
					"CalcoloPenaController.exGetBeneficiRevocatiArresto: Non posso leggere  : " + ex);
		} finally {
			cleanup(lBenDao);
			cleanup(lConn);
		}

		return lBenConc;
	}

	/*****************************************************************************
	 * Effettua la ricerca delle Misure Cautelari computabili ai fini della Reclusione per il fascicolo
	 * corrente. Somma i valori e restituisce il totale in un CalendarModel
	 *
	 * @param lFascID
	 *            - Id del fascicolo
	 * @return CalendarModel - somma delle Misure Cautelari
	 * @throws F3BException
	 ****************************************************************************/
	public CalendarModel exGetMisureCautelariComputabiliReclusione(BigDecimal lFascID) throws F3BException {

		// Retrieve Misura Cautelare (PS=Presofferto Computabile)
		Connection lConn = null;
		MisuraCautelareSqlDAO lMCDao = null;

		CalendarModel lCalMod = new CalendarModel();
		CalendarModel lMCTot = new CalendarModel();
		CalendarUtil lCalCon = new CalendarUtil();

		MisuraCautelareModel lMisCau = new MisuraCautelareModel();
		lMisCau.setFasSieIdFascicoloSiep(lFascID);
		lMisCau.setFlagComputabile("S");
		lMisCau.setCodTipoMisura("CA"); // Custodia cautelare in carcere (Reclusione)

		try {
			lConn = getDBConnection();
			lMCDao = new MisuraCautelareSqlDAO(lConn);
			lMCDao.ricercaMisuraCautelare(lMisCau);
			lMCDao.start();

			while (lMCDao.next()) {
				lMisCau = (MisuraCautelareModel) lMCDao.getModel();

				if (lMisCau.getDataInizio() != null && lMisCau.getDataFine() != null) {
					lCalMod.setDataInizio(lMisCau.getDataInizio());
					lCalMod.setDataFine(lMisCau.getDataFine());
					lCalMod = lCalCon.CalcolaNumGiorniMesiAnni(lCalMod);

					lMCTot = lCalCon.sommaGiorni(lMCTot, lCalMod);
				}

				if (lMisCau.getDataInizio() == null && lMisCau.getDataFine() == null) {
					lCalMod.setNumAnni(lMisCau.getNumAnni());
					lCalMod.setNumMesi(lMisCau.getNumMesi());
					lCalMod.setNumGiorni(lMisCau.getNumGiorni());

					lMCTot = lCalCon.sommaGiorni(lMCTot, lCalMod);
				}
			}

			lMCTot = lCalCon.ricalcolaGAM(lMCTot);
			lMCDao.stop();
			return lMCTot;
		} catch (DAOException daoEx) {
			throw new F3BException(
					"CalcoloPenaController.exGetMisureCautelariComputabiliReclusione: Non posso leggere : "
							+ daoEx);
		} catch (Exception ex) {
			throw new F3BException(
					"CalcoloPenaController.exGetMisureCautelariComputabiliReclusione: Non posso leggere  : "
							+ ex);
		} finally {
			cleanup(lMCDao);
			cleanup(lConn);
		}

	}

	/*****************************************************************************
	 * Effettua la ricerca delle Misure Cautelari computabili ai fini dell' Arresto per il fascicolo corrente.
	 * Somma i valori e restituisce il totale in un CalendarModel
	 *
	 * @param lFascID
	 *            - Id del fascicolo
	 * @return CalendarModel - somma delle Misure Cautelari
	 * @throws F3BException
	 ****************************************************************************/
	public CalendarModel exGetMisureCautelariComputabiliArresto(BigDecimal lFascID) throws F3BException {

		// Retrieve Misura Cautelare (PS=Presofferto Computabile)
		Connection lConn = null;
		MisuraCautelareSqlDAO lMCDao = null;

		CalendarModel lCalMod = new CalendarModel();
		CalendarModel lMCTot = new CalendarModel();
		CalendarUtil lCalCon = new CalendarUtil();

		MisuraCautelareModel lMisCau = new MisuraCautelareModel();

		lMisCau.setFasSieIdFascicoloSiep(lFascID);
		lMisCau.setFlagComputabile("S");
		lMisCau.setCodTipoMisura("AD"); // Custodia cautelare in Arresti domiciliari

		try {
			lConn = getDBConnection();
			lMCDao = new MisuraCautelareSqlDAO(lConn);
			lMCDao.ricercaMisuraCautelare(lMisCau);
			lMCDao.start();

			while (lMCDao.next()) {
				lMisCau = (MisuraCautelareModel) lMCDao.getModel();

				if (lMisCau.getDataInizio() != null && lMisCau.getDataFine() != null) {
					lCalMod.setDataInizio(lMisCau.getDataInizio());
					lCalMod.setDataFine(lMisCau.getDataFine());

					lCalMod = lCalCon.CalcolaNumGiorniMesiAnni(lCalMod);

					lMCTot = lCalCon.sommaGiorni(lMCTot, lCalMod);
				}

				if (lMisCau.getDataInizio() == null && lMisCau.getDataFine() == null) {
					lCalMod.setNumAnni(lMisCau.getNumAnni());
					lCalMod.setNumMesi(lMisCau.getNumMesi());
					lCalMod.setNumGiorni(lMisCau.getNumGiorni());

					lMCTot = lCalCon.sommaGiorni(lMCTot, lCalMod);
				}
			}

			lMCTot = lCalCon.ricalcolaGAM(lMCTot);
			lMCDao.stop();
			return lMCTot;
		} catch (DAOException daoEx) {
			throw new F3BException(
					"CalcoloPenaController.exGetMisureCautelariComputabiliArresto: Non posso leggere : "
							+ daoEx);
		} catch (Exception ex) {
			throw new F3BException(
					"CalcoloPenaController.exGetMisureCautelariComputabiliArresto: Non posso leggere  : "
							+ ex);
		} finally {
			cleanup(lMCDao);
			cleanup(lConn);
		}

	}

	/*****************************************************************************
	 * Effettua la ricerca delle Misure Cautelari NON computabili ai fini della Reclusione per il fascicolo
	 * corrente. Somma i valori e restituisce il totale in un CalendarModel
	 *
	 * @param lFascID
	 *            - Id del fascicolo
	 * @return CalendarModel - somma delle Misure Cautelari
	 * @throws F3BException
	 ****************************************************************************/
	public CalendarModel exGetMisureCautelariNONComputabiliReclusione(BigDecimal lFascID)
			throws F3BException {

		// Retrieve Misura Cautelare (NC=Presofferto NON Computabile)
		Connection lConn = null;
		MisuraCautelareSqlDAO lMCDao = null;

		CalendarModel lCalMod = new CalendarModel();
		CalendarModel lMCTot = new CalendarModel();
		CalendarUtil lCalCon = new CalendarUtil();

		MisuraCautelareModel lMisCau = new MisuraCautelareModel();

		lMisCau.setFasSieIdFascicoloSiep(lFascID);
		lMisCau.setFlagComputabile("N");
		lMisCau.setCodTipoMisura("CA"); // Custodia cautelare in carcere (Reclusione)

		try {
			lConn = getDBConnection();
			lMCDao = new MisuraCautelareSqlDAO(lConn);
			lMCDao.ricercaMisuraCautelare(lMisCau);
			lMCDao.start();

			while (lMCDao.next()) {
				lMisCau = (MisuraCautelareModel) lMCDao.getModel();

				if (lMisCau.getDataInizio() != null && lMisCau.getDataFine() != null) {
					lCalMod.setDataInizio(lMisCau.getDataInizio());
					lCalMod.setDataFine(lMisCau.getDataFine());

					lCalMod = lCalCon.CalcolaNumGiorniMesiAnni(lCalMod);

					lMCTot = lCalCon.sommaGiorni(lMCTot, lCalMod);
				}
				if (lMisCau.getDataInizio() == null && lMisCau.getDataFine() == null) {
					lCalMod.setNumAnni(lMisCau.getNumAnni());
					lCalMod.setNumMesi(lMisCau.getNumMesi());
					lCalMod.setNumGiorni(lMisCau.getNumGiorni());

					lMCTot = lCalCon.sommaGiorni(lMCTot, lCalMod);
				}
			}

			lMCTot = lCalCon.ricalcolaGAM(lMCTot);
			lMCDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"CalcoloPenaController.exGetMisureCautelariNONComputabiliReclusione: Non posso leggere : "
							+ daoEx);
		} catch (Exception ex) {
			throw new F3BException(
					"CalcoloPenaController.exGetMisureCautelariNONComputabiliReclusione: Non posso leggere  : "
							+ ex);
		} finally {
			cleanup(lMCDao);
			cleanup(lConn);
		}

		return lMCTot;
	}

	/*****************************************************************************
	 * Effettua la ricerca delle Misure Cautelari NON computabili ai fini dell' Arresto per il fascicolo
	 * corrente. Somma i valori e restituisce il totale in un CalendarModel
	 *
	 * @param lFascID
	 *            - Id del fascicolo
	 * @return CalendarModel - somma delle Misure Cautelari
	 * @throws F3BException
	 ****************************************************************************/
	public CalendarModel exGetMisureCautelariNONComputabiliArresto(BigDecimal lFascID) throws F3BException {

		// Retrieve Misura Cautelare (NC=Presofferto NON Computabile)
		Connection lConn = null;
		MisuraCautelareSqlDAO lMCDao = null;

		CalendarModel lCalMod = new CalendarModel();
		CalendarModel lMCTot = new CalendarModel();
		CalendarUtil lCalCon = new CalendarUtil();

		MisuraCautelareModel lMisCau = new MisuraCautelareModel();

		lMisCau.setFasSieIdFascicoloSiep(lFascID);
		lMisCau.setFlagComputabile("N");
		lMisCau.setCodTipoMisura("AD"); // Custodia cautelare in Arresti domiciliari

		try {
			lConn = getDBConnection();
			lMCDao = new MisuraCautelareSqlDAO(lConn);
			lMCDao.ricercaMisuraCautelare(lMisCau);
			lMCDao.start();

			while (lMCDao.next()) {
				lMisCau = (MisuraCautelareModel) lMCDao.getModel();

				if (lMisCau.getDataInizio() != null && lMisCau.getDataFine() != null) {
					lCalMod.setDataInizio(lMisCau.getDataInizio());
					lCalMod.setDataFine(lMisCau.getDataFine());

					lCalMod = lCalCon.CalcolaNumGiorniMesiAnni(lCalMod);

					lMCTot = lCalCon.sommaGiorni(lMCTot, lCalMod);
				}
				if (lMisCau.getDataInizio() == null && lMisCau.getDataFine() == null) {
					lCalMod.setNumAnni(lMisCau.getNumAnni());
					lCalMod.setNumMesi(lMisCau.getNumMesi());
					lCalMod.setNumGiorni(lMisCau.getNumGiorni());

					lMCTot = lCalCon.sommaGiorni(lMCTot, lCalMod);
				}
			}
			lMCTot = lCalCon.ricalcolaGAM(lMCTot);
			lMCDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"CalcoloPenaController.exGetMisureCautelariNONComputabiliArresto: Non posso leggere : "
							+ daoEx);
		} catch (Exception ex) {
			throw new F3BException(
					"CalcoloPenaController.exGetMisureCautelariNONComputabiliArresto: Non posso leggere  : "
							+ ex);
		} finally {
			cleanup(lMCDao);
			cleanup(lConn);
		}

		return lMCTot;
	}

	/*****************************************************************************
	 * Effettua la ricerca dei periodi di misura cautelare Fungibili. Somma i valori e restituisce il totale
	 * in un CalendarModel
	 *
	 * @param lFascID
	 *            - Id del fascicolo
	 * @return CalendarModel - somma dei periodi Fungibili
	 * @throws F3BException
	 ****************************************************************************/
	public CalendarModel exGetFungibilita(BigDecimal lFascID) throws F3BException {

		// Retrieve Misura Cautelare (FU=Fungibilita)
		Connection lConn = null;
		MisuraCautelareSqlDAO lMCDao = null;

		CalendarModel lCalMod = new CalendarModel();
		CalendarModel lMCTot = new CalendarModel();
		CalendarUtil lCalCon = new CalendarUtil();

		MisuraCautelareModel lMisCau = new MisuraCautelareModel();

		lMisCau.setFasSieIdFascicoloSiep(lFascID);
		lMisCau.setCodTipoMisura("FU"); // Fungibilità (no dominio)

		try {
			lConn = getDBConnection();
			lMCDao = new MisuraCautelareSqlDAO(lConn);
			lMCDao.ricercaMisuraCautelare(lMisCau);
			lMCDao.start();

			while (lMCDao.next()) {
				lMisCau = (MisuraCautelareModel) lMCDao.getModel();

				if (lMisCau.getDataInizio() != null && lMisCau.getDataFine() != null) {
					lCalMod.setDataInizio(lMisCau.getDataInizio());
					lCalMod.setDataFine(lMisCau.getDataFine());

					lCalMod = lCalCon.CalcolaNumGiorniMesiAnni(lCalMod);

					lMCTot = lCalCon.sommaGiorni(lMCTot, lCalMod);
				} else {
					lCalMod.setNumAnni(lMisCau.getNumAnni());
					lCalMod.setNumMesi(lMisCau.getNumMesi());
					lCalMod.setNumGiorni(lMisCau.getNumGiorni());

					lMCTot = lCalCon.sommaGiorni(lMCTot, lCalMod);
				}
			}

			lMCTot = lCalCon.ricalcolaGAM(lMCTot);
			lMCDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"CalcoloPenaController.exGetMisureCautelariNONComputabili: Non posso leggere : " + daoEx);
		} catch (Exception ex) {
			throw new F3BException(
					"CalcoloPenaController.exGetMisureCautelariNONComputabili: Non posso leggere  : " + ex);
		} finally {
			cleanup(lMCDao);
			cleanup(lConn);
		}

		return lMCTot;
	}

	/*****************************************************************************
	 * Metodo che calcola la pena residua scorporando dalla pena complessiva a sistema (in sentenza) le parti
	 * dovute ai benefici e alle misure cautelari.<br>
	 * Questo metodo va in update del record PENA_RESIDUA con flag validato = N se presente, altrimenti va in
	 * insert.<br>
	 * <br>
	 *
	 * n.b. il metodo non agisce sulle date (inizio, fine..) ma solo su gg, mm, aa e importi (multa e ammenda)
	 *
	 * @param lFascID
	 *            - id fascicolo
	 * @param lPenMod
	 *            - model pena complessiva punto di partenza per il calcolo
	 * @param lBenConcessiReclusione
	 *            - Tot Benefici concessi reclusione
	 * @param lBenRevocatiReclusione
	 *            - Tot Benefici revocati reclusione
	 * @param lBenConcessiArresto
	 *            - Tot Benefici concessi Arresto
	 * @param lBenRevocatiArresto
	 *            - Tot Benefici revocati Arresto
	 * @param lMCTotRec
	 *            - Tot Misure Cautelari Reclusione
	 * @param lMCTotArr
	 *            - Tot Misure Cautelari Arresto
	 * @param CodOperatore
	 * @param CodUffOperatore
	 * @return Model della PENA_RESIDUA aggiornata o inserita
	 * @throws F3BException
	 */
	public PenaResiduaModel exCalcolaQuantumPenaComplessivaIniziale(BigDecimal lFascID,
			PenaComplessivaModel lPenMod, CalendarModel lBenConcessiReclusione,
			CalendarModel lBenRevocatiReclusione, CalendarModel lBenConcessiArresto,
			CalendarModel lBenRevocatiArresto, CalendarModel lMCTotRec, CalendarModel lMCTotArr,
			String CodOperatore, String CodUffOperatore) throws F3BException {

		// CALCOLO DEL QUANTUM DI PENA
		Connection lConn = null;

		CalendarModel lPCCalReclusione = new CalendarModel();
		CalendarModel lPCCalArresto = new CalendarModel();

		CalendarModel lPCTmpReclusione = new CalendarModel();
		CalendarModel lPCTmpArresto = new CalendarModel();

		CalendarUtil lCalCon = new CalendarUtil();

		PenaResiduaDAO lPenResiduaDao = null;

		PenaResiduaModel lPenResMod = new PenaResiduaModel();

		boolean lProceedForErg = false;

		// false se ergastolo
		lProceedForErg = !(lPenMod.getCodTipoPenaDetentiva().equals("03")
				|| lPenMod.getCodTipoPenaDetentiva().equals("04"));

		// =============================================================================
		// Il cacolo viene effettuato solo se la pena in sentenza non è un ergastolo
		// =============================================================================
		if (lProceedForErg) // non ergastolo
		{
			// Scarico i dati del Model di Pena Complessiva nei CalendarModel per poter
			// effettuare somme e sottrazioni
			CalendarModel lPenaComplessivaReclusione = new CalendarModel();
			CalendarModel lPenaComplessivaArresto = new CalendarModel();

			lPenaComplessivaReclusione.setNumAnni(lPenMod.getNumAnniReclusione());
			lPenaComplessivaReclusione.setNumMesi(lPenMod.getNumMesiReclusione());
			lPenaComplessivaReclusione.setNumGiorni(lPenMod.getNumGiorniReclusione());
			if (lPenMod.getImportoMulta() != null) {
				lPenaComplessivaReclusione.setImportoMulta(lPenMod.getImportoMulta().doubleValue());
			} else
				lPenaComplessivaReclusione.setImportoMulta(0);

			lPenaComplessivaArresto.setNumAnni(lPenMod.getNumAnniArresto());
			lPenaComplessivaArresto.setNumMesi(lPenMod.getNumMesiArresto());
			lPenaComplessivaArresto.setNumGiorni(lPenMod.getNumGiorniArresto());
			if (lPenMod.getImportoAmmenda() != null) {
				lPenaComplessivaArresto.setImportoAmmenda(lPenMod.getImportoAmmenda().doubleValue());
			} else
				lPenaComplessivaArresto.setImportoAmmenda(0);

			// =====================================================================================
			// Calcolo il saldo dei benefici concessi/revocati per Reclusione (concessi-revocati)
			// =====================================================================================
			lPCCalReclusione = lCalCon.BeneficisottraiGiornieValute(lBenConcessiReclusione,
					lBenRevocatiReclusione);

			// lPCCal = lCalCon.ricalcolaGAM(lPCCal);
			// =======================================================================================================
			// Se Revocati > Concessi (saldo negativo,swapped) sommo alla pena complessiva il saldo
			// n.b. Sto depurando la pena complessiva dai benefici, per ottenere la pena residua
			// quindi se i benefici sono negativi, devo sommare, altrimenti sottrarre
			// =======================================================================================================
			if (lPCCalReclusione.getErrorMsg().equals("Swapped")) {
				lPCTmpReclusione = lCalCon.sommaGiorni(lPCCalReclusione, lPenaComplessivaReclusione);
				lPCTmpReclusione.setImportoMulta(
						lPenaComplessivaReclusione.getImportoMulta() - lPCCalReclusione.getImportoMulta());
			} else {
				lPCTmpReclusione = lCalCon.sottraiGiorni(lPenaComplessivaReclusione, lPCCalReclusione);
				lPCTmpReclusione.setImportoMulta(
						lPenaComplessivaReclusione.getImportoMulta() - lPCCalReclusione.getImportoMulta());
			}

			// Se i benefici per Reclusione superano la pena complessiva per reclusione,
			// quest'ultima viene azzerata e la rimanente parte dei benefici di reclusione
			// non goduti vengono convertiti in benefici per arresto
			// es: PCr = 5gg BEr = 7gg, azzero la PCr ma restano ancora 2gg di BEr che
			// converto in BEarr
			if (!lCalCon.isPositiveTime(lPCTmpReclusione)) {
				/**
				 * TODO n.b. in questo modo perdo l'informazione sugli importi (Ammenda) dei benefici concessi
				 * per arresto. Infatti il metodo sottraiGiorni restituisce un model con solo gg.mm.aa
				 * valorizzati Questa operazione è la stessa del metodo exCalcolaQuantumPenaComplessivaNuovo
				 * solo che in quel caso non viene persa la valuta in quanto viene richiamato in metodo
				 * sottraiGiornieValute invece che sottraiGiorni
				 */
				lBenConcessiArresto = lCalCon.sottraiGiorni(lBenConcessiArresto, lPCTmpReclusione);
				lPCTmpReclusione.setNumAnni(0);
				lPCTmpReclusione.setNumMesi(0);
				lPCTmpReclusione.setNumGiorni(0);
			}

			// Se i benefici Reclusione Multa superano la Multa Reclusione pena complessiva,
			// azzero quest'ultima e sommo la differenza ai benefici Ammenda, ma solo se è
			// prevista una un'ammenda in Pena Complessiva
			if (lPCTmpReclusione.getImportoMulta() < 0) {
				if (lPenaComplessivaArresto.getImportoAmmenda() > 0)
					lBenConcessiArresto.setImportoAmmenda(
							lBenConcessiArresto.getImportoAmmenda() - lPCTmpReclusione.getImportoMulta());
				lPCTmpReclusione.setImportoMulta(0);
			}

			// =====================================================================================
			// Calcolo il saldo dei benefici concessi/revocati per ARRESTO (concessi-revocati)
			// n.b. lBenConcessiArresto a questo punto tiene conto anche dei benefici
			// per Reclusione non goduti in quanto > della pena complessiva, per
			// cui potrebbe differire dal valore passato in input
			// =====================================================================================
			lPCCalArresto = lCalCon.BeneficisottraiGiornieValute(lBenConcessiArresto, lBenRevocatiArresto);

			if (lPCCalArresto.getErrorMsg().equals("Swapped")) { // il periodo di benefici revocati supera
																	// quelli concessi (per Arresto)
				lPCTmpArresto = lCalCon.sommaGiorni(lPCCalArresto, lPenaComplessivaArresto);
			} else {
				lPCTmpArresto = lCalCon.sottraiGiornieValute(lPenaComplessivaArresto, lPCCalArresto);
			}
			lPCTmpArresto.setImportoAmmenda(
					lPenaComplessivaArresto.getImportoAmmenda() - lPCCalArresto.getImportoAmmenda());

			// ==================================================
			// Prendo in considerazione le MISURE CAUTELARI
			// ==================================================
			// 15-04-2004 - Da oggi, tutte le misure cautelari convergono in reclusione
			// Tengo l'arresto a 0, per eventuali altre sottrazioni che mi portano cmq in negativo.
			CalendarModel lMC = new CalendarModel(lCalCon.sommaGiornieValute(lMCTotRec, lMCTotArr));
			CalendarModel lMArr = new CalendarModel();

			// Sottraggo alla PC depurata dei benefici anche le Misure Cautelari
			lPCTmpReclusione = lCalCon.sottraiGiornieValute(lPCTmpReclusione, lMC);
			lPCTmpArresto = lCalCon.sottraiGiornieValute(lPCTmpArresto, lMArr);

			if (lPCTmpArresto.getImportoAmmenda() < 0)
				lPCTmpArresto.setImportoAmmenda(0);

			// Se presenti Benefici o Misure Cautelari deve normalizzare la durata
			// n.b nel metodo exCalcolaQuantumPenaComplessivaNuovo questo if manca
			// viene fatta la normalizzazione secca
			if (!lCalCon.isZero(lPCCalReclusione) || !lCalCon.isZero(lPCCalArresto) || !lCalCon.isZero(lMC)) {
				lPCTmpArresto = lCalCon.ricalcolaGAM(lPCTmpArresto);
				lPCTmpReclusione = lCalCon.ricalcolaGAM(lPCTmpReclusione);
			}

			lPenResMod.setImportoMulta(new BigDecimal("" + lPCTmpReclusione.getImportoMulta()));
			lPenResMod.setImportoAmmenda(new BigDecimal("" + lPCTmpArresto.getImportoAmmenda()));

			// =========================================================================================
			// Se la pena residua per Reclusione calcolata sottraendo le Misure Cautelari risulta < 0
			// Azzero la Reclusione e sottraggo la differenza alla pena residua per arresto.
			// =========================================================================================
			if (!lCalCon.isPositiveTime(lPCTmpReclusione)) // negativa la reclusione
			{
				lPCTmpReclusione.setNumAnni(Math.abs(lPCTmpReclusione.getNumAnni()));
				lPCTmpReclusione.setNumMesi(Math.abs(lPCTmpReclusione.getNumMesi()));
				lPCTmpReclusione.setNumGiorni(Math.abs(lPCTmpReclusione.getNumGiorni()));

				lPCTmpArresto = lCalCon.BeneficisottraiGiornieValute(lPCTmpArresto, lPCTmpReclusione); // sottraggo
																										// da
																										// Arresto
																										// (reclusione
																										// ha
																										// i -
																										// davanti)
				lPCTmpReclusione = new CalendarModel(); // azzero la reclusione
			}

			// ========================================================================
			// Fine calcolo
			// ========================================================================
			// =======================================================================
			// Effettuo l'inserimento/aggiornamento della PENA_RESIDUA con i dati
			// appena calcolati
			// =======================================================================
			try {
				lConn = getDBConnection();

				lPenResiduaDao = new PenaResiduaDAO(lConn);

				lPenResMod.setFasSieIdFascicoloSiep(lFascID);

				lPenResMod.setNumAnniReclusione(new BigDecimal("" + lPCTmpReclusione.getNumAnni()));
				lPenResMod.setNumMesiReclusione(new BigDecimal("" + lPCTmpReclusione.getNumMesi()));
				lPenResMod.setNumGiorniReclusione(new BigDecimal("" + lPCTmpReclusione.getNumGiorni()));
				lPenResMod.setNumAnniArresto(new BigDecimal("" + lPCTmpArresto.getNumAnni()));
				lPenResMod.setNumMesiArresto(new BigDecimal("" + lPCTmpArresto.getNumMesi()));
				lPenResMod.setNumGiorniArresto(new BigDecimal("" + lPCTmpArresto.getNumGiorni()));

				lPenResMod.setFlagValidato("N");
				lPenResMod.setDiesAQuo("S"); // a he serve se non ho fatto il calcolo delle date?
				lPenResMod.setFlagErgastolo("N");
				// multa e ammenda già caricati

				// Cerco un record pena residua non validato da aggiornare se esiste
				// altrimenti vado in inserimento
				// Questa operazione è la stessa effettuata dal metodo
				// lPenResMod = lPenResSqlDao.inserisciOModificaPenaResidua(lPenResMod);

				BigDecimal lIndicePenaResidua = null;

				PenaResiduaModel lPenRes = new PenaResiduaModel();
				lPenRes.setFasSieIdFascicoloSiep(lFascID);

				IPenaResidua IPenRes = SIEPLookupRemote.getPenaResiduaRemote();
				Vector lVect = IPenRes.ExRicercaPenaResidua(lPenRes);
				boolean noMore = false;

				if (lVect.size() > 0) {
					for (int i = 0; i < lVect.size(); i++) {
						lPenRes = (PenaResiduaModel) lVect.get(i);
						if (lPenRes.getFlagValidato() != null && lPenRes.getFlagValidato().equals("N")) { // n.b.
																											// esiste
																											// al
																											// più
																											// un
																											// record
																											// non
																											// validato
							lIndicePenaResidua = lPenRes.getIdPenaResidua();
							noMore = true;
						}
					}

					if (noMore) { // Record PENA_RESIDUA non validato presente, lo aggiorno
						lPenResMod.setIdPenaResidua(lIndicePenaResidua);

						lPenResMod.setCodOperatoreAggiornamento(CodOperatore);
						lPenResMod.setCodUfficioAggiornamento(CodUffOperatore);
						lPenResMod.setDataAggiornamento(DateUtils.getSysDate());

						lPenResiduaDao.setDAOFromModelForUpdate(lPenResMod);
						lPenResiduaDao.update();
					} else { // Record PENA_RESIDUA non validato assente, lo inserisco
						lPenResMod.setCodOperatoreInserimento(CodOperatore);
						lPenResMod.setCodUfficioInserimento(CodUffOperatore);
						lPenResMod.setDataInserimento(DateUtils.getSysDate());

						lPenResiduaDao.setDAOFromModel(lPenResMod);

						lPenResMod.setIdPenaResidua(lPenResiduaDao.insert());
					}
				} else { // Non è presente alcune record PENA_RESIDUA, lo inserisco
					lPenResMod.setCodOperatoreInserimento(CodOperatore);
					lPenResMod.setCodUfficioInserimento(CodUffOperatore);
					lPenResMod.setDataInserimento(DateUtils.getSysDate());

					lPenResiduaDao.setDAOFromModel(lPenResMod);
					lPenResMod.setIdPenaResidua(lPenResiduaDao.insert());
				}

				commit(lConn);
			} catch (DAOException daoEx) {
				throw new F3BException(
						"CalcoloPenaController.exCalcolaQuantumPenaComplessivaIniziale: Non posso leggere : "
								+ daoEx);
			} catch (Exception ex) {
				throw new F3BException(
						"CalcoloPenaController.exCalcolaQuantumPenaComplessivaIniziale: Non posso leggere  : "
								+ ex);
			} finally {
				cleanup(lPenResiduaDao);

				cleanup(lConn);
			}
		} // ergastolo

		return lPenResMod;
	}

	/*****************************************************************************
	 * Metodo che calcola la pena residua scorporando dalla pena complessiva a sistema (in sentenza o residua)
	 * le parti dovute ai benefici e alle misure cautelari.<br>
	 * Questo metodo va in update del record PENA_RESIDUA con flag validato = N se presente, altrimenti va in
	 * insert.<br>
	 * <br>
	 * Se ForzaFungibilita = true, prima di inserire la pena residua calcolata, verifica se <>0 e > della pena
	 * già espiata. In questo caso non inserisce la pena residua calcolata bensì la differenza tra questo
	 * valore e la pena già espiata. Altrimenti non inserisce nulla.
	 *
	 * Le grandezze da in gioco potrebbero portare a risultati negativi in quanto le misure Cautelari devono
	 * essere sempre sottratte e in genere anche i benefici, per cui può accadere che si ottengano saldi
	 * negativi. Nel calcolo si procede nel seguente modo: Viene calcolato prima il saldo del quantum di
	 * Reclusione, se viene negativo, quantum da scalare > quantum residuo, viene azzerata la reclusione e la
	 * differenza viene scalata degli arresti. Viene quindi effettuato il calcolo degli arresti, sono quindi
	 * solo questi a poter essere negativi
	 *
	 * @param lFascID
	 * @param lPenMod
	 * @param lBenConcessiReclusione
	 * @param lBenRevocatiReclusione
	 * @param lBenConcessiArresto
	 * @param lBenRevocatiArresto
	 * @param lMCTotRec
	 * @param lMCTotArr
	 * @param CodOperatore
	 * @param CodUffOperatore
	 * @param ForzaFungibilita
	 *            - ????????????
	 * @param lPenaGiaEspiata
	 * @return
	 * @throws F3BException
	 */
	public PenaResiduaModel exCalcolaQuantumPenaComplessivaNuovo(BigDecimal lFascID,
			PenaComplessivaModel lPenMod, CalendarModel lBenConcessiReclusione,
			CalendarModel lBenRevocatiReclusione, CalendarModel lBenConcessiArresto,
			CalendarModel lBenRevocatiArresto, CalendarModel lMCTotRec, CalendarModel lMCTotArr,
			String CodOperatore, String CodUffOperatore, boolean ForzaFungibilita,
			CalendarModel lPenaGiaEspiata) throws F3BException {

		// CALCOLO DEL QUANTUM DI PENA
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Inizio calcolo del quantum Rideterminato...");
		Connection lConn = null;

		CalendarModel lPCCal = new CalendarModel();
		CalendarModel lPCTmpReclusione = new CalendarModel();
		CalendarModel lPCTmpArresto = new CalendarModel();

		CalendarUtil lCalCon = new CalendarUtil();

		PenaResiduaModel lPenResMod = new PenaResiduaModel();
		// PenaResiduaDAO lPenResiduaDao=null;
		PenaResiduaSqlDAO lPenResSqlDao = null;

		boolean lProceedForErg = false;

		lProceedForErg = !(lPenMod.getCodTipoPenaDetentiva().equals("03")
				|| lPenMod.getCodTipoPenaDetentiva().equals("04"));

		// ==========================================================================
		// se lProceedForErg=true, NON c'è una riga tipo codice = ergastolo...proseguo!
		// ==========================================================================
		if (lProceedForErg) {

			// ======================================================================
			// Sezione di calcolo del quantum di pena da inserire a sistema, le date
			// vengono ignorate. Il calcolo procede nello stesso identico modo
			// del metodo exCalcolaQuantumPenaComplessivaInizio.
			// ======================================================================

			// Scarico nei CalendarModel di appoggio i dati della Pena Complessiva
			CalendarModel lPenaComplessivaReclusione = new CalendarModel();
			CalendarModel lPenaComplessivaArresto = new CalendarModel();

			// Reclusione
			lPenaComplessivaReclusione.setNumAnni(lPenMod.getNumAnniReclusione());
			lPenaComplessivaReclusione.setNumMesi(lPenMod.getNumMesiReclusione());
			lPenaComplessivaReclusione.setNumGiorni(lPenMod.getNumGiorniReclusione());
			if (lPenMod.getImportoMulta() != null) {
				lPenaComplessivaReclusione.setImportoMulta(lPenMod.getImportoMulta().doubleValue());
			} else
				lPenaComplessivaReclusione.setImportoMulta(0);

			// Arresto
			lPenaComplessivaArresto.setNumAnni(lPenMod.getNumAnniArresto());
			lPenaComplessivaArresto.setNumMesi(lPenMod.getNumMesiArresto());
			lPenaComplessivaArresto.setNumGiorni(lPenMod.getNumGiorniArresto());
			if (lPenMod.getImportoAmmenda() != null) {
				lPenaComplessivaArresto.setImportoAmmenda(lPenMod.getImportoAmmenda().doubleValue());
			} else
				lPenaComplessivaArresto.setImportoAmmenda(0);

			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Tot Reclusione di Partenza :" + lPenaComplessivaReclusione);
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Tot Arresto di Partenza :" + lPenaComplessivaArresto);

			/*
			 * //Sottraggo Benefici Concessi Arresto
			 * lPCTmpArresto=lCalCon.BeneficisottraiGiornieValute(lPenaComplessivaArresto
			 * ,lBenConcessiArresto); //Aggiungo Benefici Revocati Arresto
			 * lPCTmpArresto=lCalCon.sommaGiornieValute(lPCTmpArresto,lBenRevocatiArresto); //Sottraggo
			 * Benefici Concessi Reclusione
			 * lPCTmpReclusione=lCalCon.BeneficisottraiGiornieValute(lPenaComplessivaReclusione
			 * ,lBenConcessiReclusione); //Aggiungo Benefici Revocati Reclusione
			 * lPCTmpReclusione=lCalCon.sommaGiornieValute(lPCTmpReclusione,lBenRevocatiReclusione);
			 */
			// =====================================================================================
			// Calcolo il saldo dei benefici concessi/revocati per Reclusione (concessi-revocati)
			// n.b. nella variabile sono presenti anche le Annotazioni Manuali
			// =====================================================================================
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("-------------------------------------------");
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug(" CALCOLO SALDO BENEFICI RECLUSIONE ");
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("-------------------------------------------");

			lPCCal = lCalCon.BeneficisottraiGiornieValute(lBenConcessiReclusione, lBenRevocatiReclusione);
			lPCCal = lCalCon.ricalcolaGAM(lPCCal);

			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Totale benefici Reclusione (concessi/revocati): "+lPCCal);

			// ======================================================================
			// Se Revocati > concessi (saldo negativo,swapped) sommo alla pena
			// complessiva il saldo
			// n.b. Sto depurando la pena complessiva finale dai benefici, per
			// ottenere la pena complessiva iniziale quindi se i benefici
			// sono negativi, devo sommare, altrimenti sottrarre
			// ======================================================================
			if (lPCCal.getErrorMsg().equals("Swapped")) {
				lPCTmpReclusione = lCalCon.sommaGiorni(lPCCal, lPenaComplessivaReclusione);
				lPCTmpReclusione.setImportoMulta(
						lPenaComplessivaReclusione.getImportoMulta() - lPCCal.getImportoMulta());
			} else { // Concessi > revocati, saldo positivo, sottraggo i benefici alla pena
						// complessiva
				lPCTmpReclusione = lCalCon.sottraiGiorniNew(lPenaComplessivaReclusione, lPCCal);
				lPCTmpReclusione.setImportoMulta(
						lPenaComplessivaReclusione.getImportoMulta() - lPCCal.getImportoMulta());
			}

			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Tot Reclusione Rideterminata sottraendo i benefici = "+lPCTmpReclusione);

			// Se i benefici per Reclusione superano la pena complessiva per
			// reclusione, quest'ultima viene azzerata e la rimanente parte dei
			// benefici di reclusione non goduti vengono convertiti in benefici per
			// arresto
			// es: PCr = 5gg BEr = 7gg, azzero la PCr ma restano ancora 2gg di BEr che
			// converto in BEarr
			if (!lCalCon.isPositiveTime(lPCTmpReclusione)) {
				/*
				 * Attenzione. Viene utilizzata la sottraiGiornieValute sebbene sia necessario sottrarre solo
				 * i Giorni. Questo per evitare di utilizzare la sottraiGiorni che azzera il contenuto degli
				 * importi. In questo caso la sottraiGiornieValute, non altera gli importi significativi
				 * (Ammenda) dei benefici concessi in quanto sottrae all'ammenda, il valore di ammenda
				 * previsto per la Reclusione che vale però 0. n.b. Valorizza
				 * lBenConcessiArresto.mImportoMulta che però non è significativo.
				 */
				// n.b. in questo modo non perdo l'informazione sulla valuta
				// n.b. sottraggo una quantità negativa, quindi la sto sommando
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug("Benefici Reclusione > Reclusione a sistema: azzero la reclusione e carico
				// l'eccesso sui benefici per Arresti");
				lBenConcessiArresto = lCalCon.sottraiGiornieValute(lBenConcessiArresto, lPCTmpReclusione);
				lPCTmpReclusione.setNumAnni(0);
				lPCTmpReclusione.setNumMesi(0);
				lPCTmpReclusione.setNumGiorni(0);
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug("Benefici concessi per arresto Aggiornati: "+lBenConcessiArresto);
			}

			// Se i benefici Reclusione Multa superano la Multa Reclusione pena
			// complessiva, azzero quest'ultima e sommo la differenza ai benefici
			// Ammenda, ma solo se è prevista un'ammenda in Pena Complessiva
			if (lPCTmpReclusione.getImportoMulta() < 0) {
				if (lPenaComplessivaArresto.getImportoAmmenda() > 0)
					lBenConcessiArresto.setImportoAmmenda(
							lBenConcessiArresto.getImportoAmmenda() - lPCTmpReclusione.getImportoMulta());
				lPCTmpReclusione.setImportoMulta(0);
			}

			// ======================================================================
			// Calcolo il saldo dei benefici concessi/revocati per ARRESTO
			// (concessi-revocati)
			// n.b. lBenConcessiArresto a questo punto tiene conto anche dei benefici
			// per Reclusione non goduti in quanto > della pena complessiva,
			// per cui potrebbe differire dal valore passato in input
			// ======================================================================
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("-------------------------------------------");
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug(" CALCOLO SALDO BENEFICI ARRESTI ");
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("-------------------------------------------");

			lPCCal = lCalCon.BeneficisottraiGiornieValute(lBenConcessiArresto, lBenRevocatiArresto);

			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Saldo benefici Arresti (concessi/revocati: "+lPCCal);

			if (lPCCal.getErrorMsg().equals("Swapped")) { // il periodo di benefici revocati supera quelli
															// concessi (per Arresto)
															// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la
															// variabile di istanza siesLogger al posto di
															// LogF3B.getLogger()
															// siesLogger.debug("Saldo Benefici arresti
															// negativo");
				lPCTmpArresto = lCalCon.sommaGiorni(lPCCal, lPenaComplessivaArresto);
			} else {
				lPCTmpArresto = lCalCon.sottraiGiorniNew(lPenaComplessivaArresto, lPCCal);
			}

			lPCTmpArresto.setImportoAmmenda(
					lPenaComplessivaArresto.getImportoAmmenda() - lPCCal.getImportoAmmenda());

			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Saldo pena complessiva Arresti dopo somma benefici: "+lPCTmpArresto);

			// lPCTmpArresto potrebbe essere negativo
			// ==================================================
			// Prendo in considerazione le MISURE CAUTELARI
			// ==================================================
			// 15-04-2004 - Da oggi, tutte le misure cautelari convergono in reclusione
			// Tengo l'arresto a 0, per eventuali altre sottrazioni che mi portano cmq in negativo.
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("-------------------------------------------");
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug(" CALCOLO MISURE CAUTELARI ");
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("-------------------------------------------");
			CalendarModel lMC = new CalendarModel(lCalCon.sommaGiornieValute(lMCTotRec, lMCTotArr));

			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Tot misure cautelari (reclusione+Arresti) ="+lMC);
			// Sottraggo alla PC depurata dei benefici anche le Misure Cautelari
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Sottraggo le misure cautelari TUTTE dalla reclusione");
			lPCTmpReclusione = lCalCon.sottraiGiornieValute(lPCTmpReclusione, lMC);
			// lPCTmpArresto = lCalCon.sottraiGiornieValute(lPCTmpArresto, lMArr);
			/**
			 * TODO Attenzione lPCTmpReclusione potrebbe valere 0 o essere < 0
			 *
			 */
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Tot reclusione dopo sottrazione delle misure Cautelari: "+lPCTmpReclusione);
			if (lPCTmpArresto.getImportoAmmenda() < 0)
				lPCTmpArresto.setImportoAmmenda(0);

			// Normalizzo i dati calcolati (n.b. se <0 la normalizzazione lascia i dati inalterati)
			lPCTmpArresto = lCalCon.ricalcolaGAM(lPCTmpArresto);
			lPCTmpReclusione = lCalCon.ricalcolaGAM(lPCTmpReclusione);

			//
			lPenResMod.setImportoMulta(new BigDecimal("" + lPCTmpReclusione.getImportoMulta()));
			lPenResMod.setImportoAmmenda(new BigDecimal("" + lPCTmpArresto.getImportoAmmenda()));

			// =========================================================================================
			// Se la pena residua per Reclusione calcolata sottraendo le Misure Cautelari risulta < 0
			// Azzero la Reclusione e sottraggo la differenza alla pena residua per arresto.
			// =========================================================================================
			if (!lCalCon.isPositiveTime(lPCTmpReclusione)) // negativa la reclusione
			{
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug("Tot Reclusione negativo dopo sottrazione misure cautelari");
				lPCTmpReclusione.setNumAnni(Math.abs(lPCTmpReclusione.getNumAnni()));
				lPCTmpReclusione.setNumMesi(Math.abs(lPCTmpReclusione.getNumMesi()));
				lPCTmpReclusione.setNumGiorni(Math.abs(lPCTmpReclusione.getNumGiorni()));

				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug("Sottraggo la reclusione dall'arresto");

				// Attenzione: la BeneficisottraiGiornieValute restituisce il
				// quantum in valore assoluto
				// lPCTmpArresto = lCalCon.BeneficisottraiGiornieValute(lPCTmpArresto, lPCTmpReclusione);
				// //sottraggo da Arresto (reclusione ha i - davanti)
				lPCTmpArresto = lCalCon.sottraiGiorniValuteNew(lPCTmpArresto, lPCTmpReclusione); // sottraggo
																									// da
																									// Arresto
																									// (reclusione
																									// ha i -
																									// davanti)
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug("TOT Arresto ricalcolato: "+lPCTmpArresto);
				/**
				 * TODO azzerando lPCTmpReclusione mi perdo le imformazioni anche sulle Multe benefici
				 */
				lPCTmpReclusione = new CalendarModel(); // azzero la reclusione
			}

			/**
			 * TODO n.b. tutta la parte di codice precedente effettua le stesse operazioni del metodo
			 * exCalcolaQuantumPenaComplessivaNuovo Con la differenza che nei model dei Benefici sono stati
			 * caricati anche le Annotazioni Manuali
			 */
			// ======================================================================
			// Effettuo l'iserimento dei dati calcolati. Se forza funz
			//
			// ======================================================================
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("-------------------------------------------");
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Totale reclusione e arresto rideterminati:");
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Reclusione: "+lPCTmpReclusione);
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Arresti: "+lPCTmpArresto);
			try {
				lConn = getDBConnection();

				// lPenResiduaDao = new PenaResiduaDAO(lConn);
				lPenResSqlDao = new PenaResiduaSqlDAO(lConn);

				lPenResMod.setFasSieIdFascicoloSiep(lFascID);

				lPenResMod.setNumAnniReclusione(new BigDecimal("" + lPCTmpReclusione.getNumAnni()));
				lPenResMod.setNumMesiReclusione(new BigDecimal("" + lPCTmpReclusione.getNumMesi()));
				lPenResMod.setNumGiorniReclusione(new BigDecimal("" + lPCTmpReclusione.getNumGiorni()));
				lPenResMod.setNumAnniArresto(new BigDecimal("" + lPCTmpArresto.getNumAnni()));
				lPenResMod.setNumMesiArresto(new BigDecimal("" + lPCTmpArresto.getNumMesi()));
				lPenResMod.setNumGiorniArresto(new BigDecimal("" + lPCTmpArresto.getNumGiorni()));

				// n.b. multa e ammenda già caricati

				lPenResMod.setFlagValidato("N");
				lPenResMod.setDiesAQuo("S");
				lPenResMod.setFlagErgastolo("N");

				PenaResiduaModel lPenRes = new PenaResiduaModel();
				lPenRes.setFasSieIdFascicoloSiep(lFascID);

				lPenResMod.setCodOperatoreInserimento(CodOperatore);
				lPenResMod.setCodUfficioInserimento(CodUffOperatore);
				lPenResMod.setDataInserimento(DateUtils.getSysDate());

				if (!ForzaFungibilita) // ForzaFungibilita==false
				{
					// lPenResiduaDao.setDAOFromModel(lPenResMod);
					// lPenResMod.setIdPenaResidua(lPenResiduaDao.insert());
					// Inserisco o aggiorno i dati della PENA_RESIDUA con quanto calcolato
					// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
					// di LogF3B.getLogger()
					// siesLogger.debug("ForzaFungibilita = false, inserisco la pena residua calcolata anche
					// se <0");
					// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
					// di LogF3B.getLogger()
					// siesLogger.debug(lPenResMod);

					lPenResMod = lPenResSqlDao.inserisciOModificaPenaResidua(lPenResMod);
				} else { // Inserisco i di pena residua calcolata solo se il totale giorni
							// da espiare (reclusione+arresti) è > della pena già espiata
							// E IN QUESTO CASO INSERISCO A SISTEMA LA DIFFERENZA
							// In caso contrario non inserisco i dati
					CalendarModel nuovoQuantum = new CalendarModel();

					// Sommo arresti e reclusione della PENA_RESIDUA fin quì calcolata
					// potrebbro essere negativi
					nuovoQuantum.setNumAnni(
							lPenResMod.getNumAnniArresto().add(lPenResMod.getNumAnniReclusione()));
					nuovoQuantum.setNumMesi(
							lPenResMod.getNumMesiArresto().add(lPenResMod.getNumMesiReclusione()));
					nuovoQuantum.setNumGiorni(
							lPenResMod.getNumGiorniArresto().add(lPenResMod.getNumGiorniReclusione()));

					nuovoQuantum = lCalCon.ricalcolaGAM(nuovoQuantum); // normalizzo

					// ================================================================
					// Inserisco la pena residua solo se <> 0 e > della pena già espiata
					// In questo caso sottraggo alla pena residua calcolata il periodo
					// già espiato e carico tutto sulla pena residua Reclusione.
					// ================================================================
					/** TODO è corretto ??? */
					if (lCalCon.isGreater(nuovoQuantum, lPenaGiaEspiata) && !lCalCon.isZero(nuovoQuantum)) {
						CalendarModel diff = new CalendarModel(
								lCalCon.BeneficisottraiGiornieValute(nuovoQuantum, lPenaGiaEspiata));

						PenaResiduaModel lPenResMod2 = new PenaResiduaModel();

						lPenResMod2.setFasSieIdFascicoloSiep(lFascID);

						lPenResMod2.setNumAnniReclusione(new BigDecimal(diff.getNumAnni() + "").abs());
						lPenResMod2.setNumMesiReclusione(new BigDecimal(diff.getNumMesi() + "").abs());
						lPenResMod2.setNumGiorniReclusione(new BigDecimal(diff.getNumGiorni() + "").abs());
						lPenResMod2.setNumAnniArresto(new BigDecimal(0));
						lPenResMod2.setNumMesiArresto(new BigDecimal(0));
						lPenResMod2.setNumGiorniArresto(new BigDecimal(0));

						lPenResMod2.setCodOperatoreInserimento(CodOperatore);
						lPenResMod2.setCodUfficioInserimento(CodUffOperatore);
						lPenResMod2.setDataInserimento(DateUtils.getSysDate());

						// lPenResiduaDao.setDAOFromModel(lPenResMod2);
						// lPenResMod2.setIdPenaResidua(lPenResiduaDao.insert());

						lPenResMod = lPenResSqlDao.inserisciOModificaPenaResidua(lPenResMod2);
					}
				}

				commit(lConn);
			} catch (DAOException daoEx) {
				throw new F3BException(
						"CalcoloPenaController.exCalcolaQuantumPenaComplessivaNuovo: " + daoEx);
			} catch (Exception ex) {
				throw new F3BException("CalcoloPenaController.exCalcolaQuantumPenaComplessivaNuovo: " + ex);
			} finally {
				// cleanup(lPenResiduaDao);
				cleanup(lPenResSqlDao);

				cleanup(lConn);
			}
		} // ergastolo

		return lPenResMod;
	}

	/*****************************************************************************
	 * Calcola la data fine pena a partire dalla data inizio e dalla durata della pena.<br>
	 * Restituisce un vettore di Date contenente 0,1 o 2 record:<br>
	 * - 0 record se il quantum di reclusione o arresti risulta non positivo (minore o uguale a 0)<br>
	 * - 1 record = data fine pena se è previsto un solo periodo (Reclusione o Arresti)<br>
	 * - 2 record: se previsto sia Reclusione che Arresti con:<br>
	 * -- record 1 = DataFineReclusione <br>
	 * -- record 2 = DataFinePena (Reclusione+Arresti)<br>
	 *
	 * @param aDataInizio
	 * @param aPenResMod
	 * @param diesaquo
	 *            - indica se calcolare nel computo anche la datainizio
	 * @return
	 * @throws F3BException
	 */
	public Vector exCalcolaDataFinePena(Date aDataInizio, PenaResiduaModel aPenResMod, boolean diesaquo) {

		Date lDataFinePena = new Date();
		Date lDataFineRecl = new Date();
		Vector Result = new Vector(0);

		int GGInizio = Integer.parseInt(DateUtils.getDayToString(aDataInizio));
		int MMInizio = Integer.parseInt(DateUtils.getMonthToString(aDataInizio));
		int AAInizio = Integer.parseInt(DateUtils.getYearToString(aDataInizio));
		int NumGiorni;
		int NumMesi;
		int NumAnni;

		CalendarModel lCalModDurata = new CalendarModel();
		CalendarModel lCalInizio = new CalendarModel();
		CalendarModel ltmp = new CalendarModel();
		CalendarUtil lCalUtil = new CalendarUtil();

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("Pena Residua Reclusione :
		// "+aPenResMod.getNumAnniReclusione()+"-"+aPenResMod.getNumMesiReclusione()+"-"+aPenResMod.getNumGiorniReclusione());

		ltmp.setNumAnni(aPenResMod.getNumAnniReclusione());
		ltmp.setNumMesi(aPenResMod.getNumMesiReclusione());
		ltmp.setNumGiorni(aPenResMod.getNumGiorniReclusione());

		// ========================================================================
		// Se è presente e non negativo un periodo di reclusione da scontare,
		// calcolo la data fine reclusione
		if (lCalUtil.isPositiveTime(ltmp) && !lCalUtil.isZero(ltmp)) {
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("__________________________________________");

			// Calcolo data fine Reclusione
			NumGiorni = aPenResMod.getNumGiorniReclusione().intValue();
			NumMesi = aPenResMod.getNumMesiReclusione().intValue();
			NumAnni = aPenResMod.getNumAnniReclusione().intValue();

			lCalModDurata.setNumAnni(NumAnni);
			lCalModDurata.setNumMesi(NumMesi);
			lCalModDurata.setNumGiorni(NumGiorni);

			// normalizza
			lCalModDurata = lCalUtil.ricalcolaGAM(lCalModDurata);

			lCalInizio.setNumAnni(AAInizio);
			lCalInizio.setNumMesi(MMInizio);
			lCalInizio.setNumGiorni(GGInizio);

			lDataFineRecl = exCalcolaNuovaDataFine(lCalInizio, lCalModDurata, diesaquo);

			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("DATA FINE RECLUSIONE : "+lDataFineRecl);

			Result.add(lDataFineRecl);

			// ============================
			// Calcolo data fine Arresto
			// ============================
			ltmp.setNumAnni(aPenResMod.getNumAnniArresto());
			ltmp.setNumMesi(aPenResMod.getNumMesiArresto());
			ltmp.setNumGiorni(aPenResMod.getNumGiorniArresto());

			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Pena Residua Arresto :
			// "+aPenResMod.getNumAnniArresto()+"-"+aPenResMod.getNumMesiArresto()+"-"+aPenResMod.getNumGiorniArresto());

			if (lCalUtil.isPositiveTime(ltmp) && !lCalUtil.isZero(ltmp)) {
				// NumGiorni+= aPenResMod.getNumGiorniArresto().intValue();
				// NumMesi+= aPenResMod.getNumMesiArresto().intValue();
				// NumAnni+= aPenResMod.getNumAnniArresto().intValue();
				NumGiorni = aPenResMod.getNumGiorniArresto().intValue();
				NumMesi = aPenResMod.getNumMesiArresto().intValue();
				NumAnni = aPenResMod.getNumAnniArresto().intValue();

				lCalModDurata.setNumAnni(NumAnni);
				lCalModDurata.setNumMesi(NumMesi);
				lCalModDurata.setNumGiorni(NumGiorni);
				lCalModDurata = lCalUtil.ricalcolaGAM(lCalModDurata);

				// la data di inizio coincide con la data fine Reclusione + 1
				lDataFineRecl = DateUtils.getDayAfter(lDataFineRecl);

				lCalInizio.setNumAnni(Integer.parseInt(DateUtils.getYearToString(lDataFineRecl)));
				lCalInizio.setNumMesi(Integer.parseInt(DateUtils.getMonthToString(lDataFineRecl)));
				lCalInizio.setNumGiorni(Integer.parseInt(DateUtils.getDayToString(lDataFineRecl)));
				lDataFinePena = exCalcolaNuovaDataFine(lCalInizio, lCalModDurata, diesaquo);
				Result.add(lDataFinePena);
			} else { // non è previsto l'arresto
						// NumGiorni-= aPenResMod.getNumGiorniArresto().intValue();
						// NumMesi-= aPenResMod.getNumMesiArresto().intValue();
						// NumAnni-= aPenResMod.getNumAnniArresto().intValue();
						// lCalModDurata.setNumAnni(NumAnni);
						// lCalModDurata.setNumMesi(NumMesi);
						// lCalModDurata.setNumGiorni(NumGiorni);
						// if (lCalUtil.isPositiveTime(lCalModDurata) && !lCalUtil.isZero(lCalModDurata))
						// {
						// lCalInizio.setNumAnni(AAInizio);
						// lCalInizio.setNumMesi(MMInizio);
						// lCalInizio.setNumGiorni(GGInizio);
						// lDataFinePena = exCalcolaNuovaDataFine(lCalInizio, lCalModDurata, true);
						// Result.add(lDataFinePena);
						// }
			}
		} else { // non è prevista la reclusione o è non positiva procedo al calcolo
					// degli arresti
			NumGiorni = aPenResMod.getNumGiorniArresto().intValue();
			NumMesi = aPenResMod.getNumMesiArresto().intValue();
			NumAnni = aPenResMod.getNumAnniArresto().intValue();

			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Pena Residua Arresto :
			// "+aPenResMod.getNumAnniArresto()+"-"+aPenResMod.getNumMesiArresto()+"-"+aPenResMod.getNumGiorniArresto());
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("anni , mesi , giorni " + NumAnni+","+NumMesi+","+NumGiorni);

			lCalModDurata.setNumAnni(NumAnni);
			lCalModDurata.setNumMesi(NumMesi);
			lCalModDurata.setNumGiorni(NumGiorni);

			if (lCalUtil.isPositiveTime(lCalModDurata) && !lCalUtil.isZero(lCalModDurata)) {
				lCalInizio.setNumAnni(AAInizio);
				lCalInizio.setNumMesi(MMInizio);
				lCalInizio.setNumGiorni(GGInizio);
				lDataFinePena = exCalcolaNuovaDataFine(lCalInizio, lCalModDurata, diesaquo);
				Result.add(lDataFinePena);
			}
		}

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("CALCOLO FINE PENA : TORNO "+Result.size()+ " risultati");

		return Result;
	}

	/*****************************************************************************
	 * Calcola la data fine a partire dalla data inizio e dalla durata. <br>
	 * La data inizio viene passata come CalendarModel e specificata nei campi mGG, mMM, mAA, <b>NON nel campo
	 * DataInizio</b> Il quantum <b>deve essere normalizzato</b> altrimenti il risultato finale potrebbe non
	 * essere corretto
	 *
	 * @param aDataInizio
	 *            - CalendarModel con data inizio specificata come mGG, mMM, mAA non vengono presi in
	 *            considerazione gli altri campi del model (DataInizio e DataFine)
	 * @param aDurata
	 *            - specificata come mGG, mMM e mAA
	 * @param diesaquo
	 *            - se true viene considerato anche il gg data inizio come facente parte del periodo
	 * @return data fine calcolata
	 * @throws F3BException
	 */
	public Date exCalcolaNuovaDataFine(CalendarModel aDataInizio, CalendarModel aDurata, boolean diesaquo) {

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("Data Inizio : " + aDataInizio);
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("Durata : " + aDurata);

		Date lDataFine = new Date();

		int GGInizio = aDataInizio.getNumGiorni();
		int MMInizio = aDataInizio.getNumMesi();
		int AAInizio = aDataInizio.getNumAnni();

		int GGScad;
		int MMScad;
		int AAScad;

		int GGSomma;

		if (diesaquo) {
			GGSomma = GGInizio + aDurata.getNumGiorni() - 1;
		} else {
			GGSomma = GGInizio + aDurata.getNumGiorni();
		}
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("GGSomma = "+GGSomma);

		if (GGSomma <= Integer
				.parseInt(DateUtils.getDayToString(DateUtils.getEndOfMonth(AAInizio, MMInizio)))) {
			GGScad = GGSomma;
		} else {
			GGScad = GGSomma
					- Integer.parseInt(DateUtils.getDayToString(DateUtils.getEndOfMonth(AAInizio, MMInizio)));
			MMInizio++;
		}

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("GGScad = " + GGScad);

		int MMSomma = MMInizio + aDurata.getNumMesi();
		if (MMSomma <= 12) {
			MMScad = MMSomma;
		} else {
			MMScad = MMSomma - 12;
			AAInizio++;
		}

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("mm : " + MMScad);

		AAScad = AAInizio + aDurata.getNumAnni();

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("aa : " + AAScad);

		// La data calcolata potrebbe cadere in un GG non presente per il mese
		// finale (n.b. il calcolo è stato inizialmente fatto relativamente a
		// anno e mese inizio, prima di sommare mm e aa)
		// GGScad viene corretto per MM e AA finali.
		// n.b. viene corretto solo il GG, non vengono aggiornati mm e aa
		int lEndOfMonthDataFine = Integer
				.parseInt(DateUtils.getDayToString(DateUtils.getEndOfMonth(AAScad, MMScad)));
		if (GGScad > lEndOfMonthDataFine) {
			lDataFine = DateUtils.getDate(AAScad, MMScad, lEndOfMonthDataFine);
		} else {
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("AAScad, MMScad, GGScad:"+AAScad+","+MMScad+","+GGScad);
			lDataFine = DateUtils.getDate(AAScad, MMScad, GGScad);
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("lDataFine = "+lDataFine );
		}

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("data fine : " + lDataFine);

		return lDataFine;
	}

	public Date exCalcolaNuovaDataFine(Date aDataInizio, CalendarModel aDurata, boolean diesaquo) {

		CalendarModel ltmp = new CalendarModel();

		ltmp.setNumAnni(new BigDecimal(DateUtils.getYearToString(aDataInizio)));
		ltmp.setNumMesi(new BigDecimal(DateUtils.getMonthToString(aDataInizio)));
		ltmp.setNumGiorni(new BigDecimal(DateUtils.getDayToString(aDataInizio)));

		return exCalcolaNuovaDataFine(ltmp, aDurata, diesaquo);
	}

	/*****************************************************************************
	 * Calcolo del Quantum di pena ancora da espiare. UC SIEP-UC-017-LV-AA
	 *
	 * Recupera l'ultimo record pena residua inserito (indipendentemente dallo stato) e, se presente
	 * data_fine, calcola il periodo ancora da espiare (da data fine a data corrente) restituendo il risultato
	 * in un CalendarModel.
	 *
	 * @param aFascId
	 * @return
	 * @throws F3BException
	 ****************************************************************************/
	public CalendarModel exGetQuantumPenaAncoraDaEspiare(BigDecimal aFascId) throws F3BException {

		/**
		 * TODO verificare il metodo. Non viene mai referenziato nel codice e non è dichiarato
		 * nell'interfaccia ICalcoloPena
		 */
		Connection lConn = null;
		CalendarUtil lCalUtil = new CalendarUtil();
		PenaResiduaModel lPenRes = new PenaResiduaModel();
		lPenRes.setFasSieIdFascicoloSiep(aFascId);
		PenaResiduaSqlDAO lPR = null;

		CalendarModel lModRet = new CalendarModel();
		try {
			lConn = getDBConnection();
			lPR = new PenaResiduaSqlDAO(lConn);
			lPR.ricercaPenaResiduaCorrenteByFascicoloSiep(aFascId);
			lPR.start();
			if (lPR.getModels().isEmpty())
				throw new F3BException(F3BException.USER_MESSAGE,
						"Impossibile calcolare Pena Residua da Espiare : Data Inizio Pena Residua non presente in archivio");
			while (lPR.next())
				lPenRes = (PenaResiduaModel) lPR.getModel();

			lPR.stop();
			lModRet.setDataInizio(lPenRes.getDataFine());
			if (lModRet.getDataInizio() == null)
				throw new F3BException(F3BException.USER_MESSAGE,
						"Impossibile calcolare Pena Residua da Espiare : Data Inizio Pena Residua non valida");
			lModRet.setDataFine(DateUtils.getSysDate());
			lModRet = lCalUtil.CalcolaNumGiorniMesiAnni(lModRet);
		} catch (DAOException daoEx) {
			throw new F3BException(
					"CalcoloPenaController.exGetQuantumPenaAncoraDaEspiare: Non posso leggere : " + daoEx);
		} catch (Exception ex) {
			throw new F3BException(
					"CalcoloPenaController.exGetQuantumPenaAncoraDaEspiare: Non posso leggere  : " + ex);
		} finally {
			cleanup(lPR);
			cleanup(lConn);
		}

		return lModRet;
	}

	/*****************************************************************************
	 * Aggiorna/Inserisce la data fine pena della pena residua sottraendo i giorni di liberazione dalla data
	 * fine a sistema. Vengono aggiornate anche le date intermedie di Reclusione/Arresto. Non aggiorna il
	 * quantum. La sottrazione viene fatta in modo 'esatto' (data fine - giorni) senza le approssimazioni
	 * legate ai quantum.<br>
	 * <br>
	 *
	 * Se data fine ricalcolata minore data inserimento viene calcolata e inserita la fungibilità.<br>
	 * <br>
	 *
	 * Aggiorna il flag dei record Liberazione Anticipata presi in considerazione nel calcolo
	 *
	 * @param aCalcoloPenaModel
	 * @param aFascId
	 * @param aIdEventoOrdinanza
	 * @param aDataSistemaPerCalcoli
	 * @param aDatiOperazione
	 * @param aDataFinePena
	 *            (eventuale data fine pena manuale)
	 * @param aTipoLicenza
	 *            'RD' in caso di DL92
	 * @return
	 * @throws F3BException
	 ****************************************************************************/
	public CalcoloPenaModel exCalcoloLiberazioneAnticipata(CalcoloPenaModel aCalcoloPenaModel,
			BigDecimal aFascId, BigDecimal aIdEventoOrdinanza, Date aDataSistemaPerCalcoli,
			Date aDataFinePena, String aTipoLicenza, DatiOperazioneModel aDatiOperazione)
			throws F3BException {

		Connection lConn = null;

		PenaResiduaModel lPenRes = new PenaResiduaModel();
		PenaResiduaModel lModRet = new PenaResiduaModel();

		FungibilitaSqlDAO lFunSqlDao = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;
		LicenzaLibanticipataSqlDAO lLicSqlDao = null;
		FungibilitaDAO lFunDAO = null;

		Date lDataFineRicalcolata = null;

		try {
			lConn = getDBTransaction();

			CalendarUtil lCalUtil = new CalendarUtil();

			lLicSqlDao = new LicenzaLibanticipataSqlDAO(lConn);

			if ("RD".equals(aTipoLicenza))
				lLicSqlDao.ricercaRimediRisarcitoriConcessiByEve(aIdEventoOrdinanza);
			else
				lLicSqlDao.ricercaLicenzaLibanticipataConcessaByEve(aIdEventoOrdinanza);

			List lLicenze = new ArrayList(lLicSqlDao.getModels());

			if (lLicenze != null && !lLicenze.isEmpty()) {
				int lTotGGConcessi = 0;
				for (int i = 0; i < lLicenze.size(); i++) {
					LicenzaLibAnticipataModel lLicenzaModel = (LicenzaLibAnticipataModel) lLicenze.get(i);
					if ((lLicenzaModel.getCodTipoLicenza().equals("RD")
							|| lLicenzaModel.getCodTipoLicenza().equals("LA"))
							&& lLicenzaModel.getFlagConcesso().equals("C")) {
						lTotGGConcessi += lLicenzaModel.getNumeroGiorni().intValue();
					}
				}
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug("Totale GG Concessi = "+lTotGGConcessi);

				// Aggiungo per conteggiarli nel calcolo le LA concesse con l'ordinanza
				// selezionata/inserita
				aCalcoloPenaModel.getLibAnticipate().addAll(lLicenze);

				lPenResSqlDao = new PenaResiduaSqlDAO(lConn);

				// -- Ricerca l'ultima PENA RESIDUA VALIDATA
				lPenResSqlDao.ricercaPenaResiduaCorrenteByFascicoloSiepUltimaValidata(aFascId);
				lPenRes = (PenaResiduaModel) lPenResSqlDao.getModelByKey();

				// Controllo esistenza PENA_RESIDUA
				if (lPenRes == null)
					throw new F3BException(F3BException.USER_MESSAGE,
							"Impossibile calcolare Liberazione Anticipata : Pena Residua non presente in archivio");

				// Controllo esistenza DATA_INIZIO pena
				if (lPenRes.getDataInizio() == null)
					throw new F3BException(F3BException.USER_MESSAGE,
							"Impossibile calcolare Liberazione Anticipata : Data Inizio Pena Residua non presente in archivio");

				// Controllo esistenza DATA_FINE pena
				if (lPenRes.getDataFine() == null)
					throw new F3BException(F3BException.USER_MESSAGE,
							"Impossibile calcolare Liberazione Anticipata : Data Fine Pena Residua non presente in archivio");

				// ==========================================================================
				// Effettuo i calcoli
				// Calcolo nuova data fine pena sottraendo i gg di di LA
				// ==========================================================================
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug("Effettuo il calcolo della pena con tutti i dati...");
				PenaResiduaModel lPenaRideterminata = aCalcoloPenaModel.getPenaDaEspiare(
						lPenRes.getDataInizio(), aDataSistemaPerCalcoli, "all", aDataFinePena);
				FungibilitaModel lFungModel = aCalcoloPenaModel.getFungibilitaCalcolata();

				lDataFineRicalcolata = lPenaRideterminata.getDataFine();

				// ======================================================================
				// Se data fine ricalcolata <= data di sistema, il soggetto ha già espiato
				// più di quanto dovuto, calcolo e inserisco la fungibilità
				// ======================================================================
				if (lFungModel != null && !lCalUtil.isZero(lFungModel.getQuantumFungibilita())) {
					// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
					// di LogF3B.getLogger()
					// siesLogger.debug("Presente Fungibilità...");

					lFungModel.setFasSieIdFascicoloSiep(aFascId);

					lFungModel.setCodTipoFungibilita(ICostantiFungibilita.PENA_ESPIATA_IN_ECCESSSO);
					lFungModel.setFlagValidato("N");

					lFungModel.setCodOperatoreInserimento(aDatiOperazione.getCodOperatore());
					lFungModel.setDataInserimento(aDatiOperazione.getData());
					lFungModel.setCodUfficioInserimento(aDatiOperazione.getCodUfficio());

					// La fungibilità dovrebbe essere qui legata all'evento provvedimento,
					// poichè questo non è stato ancora inserito
					// viene legata all'ordinanza per ritrovarla nella successiva pagina
					// di "conferma fungibilità".
					// Una volta emesso e validato il provvedimento la fungibilità
					// viene correttamente legata a quest'ultimo.
					lFungModel.setEveIdEvento(aIdEventoOrdinanza);

					int lGiorniFruiti = DateUtils.getDaysBetween(lPenRes.getDataFine(),
							aDataSistemaPerCalcoli);
					int lGiorniNonFruiti = lTotGGConcessi - lGiorniFruiti;

					lFungModel.setNumGiorniFruiti(new BigDecimal(lGiorniFruiti));
					lFungModel.setNumGiorniNonFruiti(new BigDecimal(lGiorniNonFruiti));

					lFunSqlDao = new FungibilitaSqlDAO(lConn);

					lFunSqlDao.inserisciOModificaFungibilita(lFungModel);
					lFunSqlDao.stop();

					// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
					// di LogF3B.getLogger()
					// siesLogger.debug("FungibilitaModel : " + lFungModel);
				} else { // Fungibilità non presente. n.b. potrebbe essere un secondo calcolo
							// della pena avendo cambiato la data di scarcerazione. Quindi sul
							// primo calcolo potrei aver inserito una fungibilità che ovviamente
							// devo cancellare.
					lFunDAO = new FungibilitaDAO(lConn);
					lFunDAO.setCondizioneUpdateEveIdEvento(aIdEventoOrdinanza);
					lFunDAO.delete();
					lFunDAO.stop();
				}

				lModRet.setIdPenaResidua(lPenRes.getIdPenaResidua());
				lModRet.setDataInizio(lPenaRideterminata.getDataInizio());
				lModRet.setDataFine(lDataFineRicalcolata);
				lModRet.setDataFinePresunta(lPenaRideterminata.getDataFinePresunta());

				// I quantum Reclusione e Arresto e gli importi restano invariati
				lModRet.setNumAnniReclusione(lPenaRideterminata.getNumAnniReclusione());
				lModRet.setNumMesiReclusione(lPenaRideterminata.getNumMesiReclusione());
				lModRet.setNumGiorniReclusione(lPenaRideterminata.getNumGiorniReclusione());
				lModRet.setImportoMulta(lPenaRideterminata.getImportoMulta());

				lModRet.setNumAnniArresto(lPenaRideterminata.getNumAnniArresto());
				lModRet.setNumMesiArresto(lPenaRideterminata.getNumMesiArresto());
				lModRet.setNumGiorniArresto(lPenaRideterminata.getNumGiorniArresto());
				lModRet.setImportoAmmenda(lPenaRideterminata.getImportoAmmenda());

				lModRet.setDiesAQuo(lPenRes.getDiesAQuo());

				lModRet.setFasSieIdFascicoloSiep(aFascId);
				lModRet.setFlagValidato("N"); // Inserito non validato

				// Le date intermedie di reclusione/arresto vengono anticipate della LA
				lModRet.setDataFineReclusione(lPenaRideterminata.getDataFineReclusione());
				lModRet.setDataInizioArresto(lPenaRideterminata.getDataInizioArresto());

				lModRet.setFlagErgastolo(lPenRes.getFlagErgastolo());

				lModRet.setCodOperatoreInserimento(aDatiOperazione.getCodOperatore());
				lModRet.setDataInserimento(aDatiOperazione.getData());
				lModRet.setCodUfficioInserimento(aDatiOperazione.getCodUfficio());

				// Inserisco la nuova pena residua
				lModRet = lPenResSqlDao.inserisciOModificaPenaResidua(lModRet);
				aCalcoloPenaModel.getPenaResiduaRicalcolata().setIdPenaResidua(lModRet.getIdPenaResidua());

				// Aggiorna ad "E" i record legati soltanto all'ordinanza selezionata
				lLicSqlDao.updateFlagElaboratoByEveIdEvento(aIdEventoOrdinanza, "E", aDatiOperazione);
			}

			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException:", daoEx);
			rollback(lConn);
			throw new F3BException(
					"CalcoloPenaController.exCalcoloLiberazioneAnticipata: Non posso leggere : " + daoEx);
		} catch (F3BException ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("F3BException:", ex);
			rollback(lConn);
			throw ex;
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception:", ex);
			rollback(lConn);
			throw new F3BException("CalcoloPenaController.exCalcoloLiberazioneAnticipata: " + ex);
		} finally {
			cleanup(lPenResSqlDao);
			cleanup(lFunSqlDao);
			cleanup(lLicSqlDao);
			cleanup(lFunDAO);

			cleanup(lConn);
		}

		return aCalcoloPenaModel;
	}

	/*****************************************************************************
	 * Calcola la data fine pena della pena residua sottraendo i giorni di liberazione dalla data fine a
	 * l'ultima pena residua VALIDATA presente sul DB.
	 *
	 * @param aTotLA
	 * @param aFascId
	 * @return Data Fine ricalcolata
	 * @throws F3BException
	 ****************************************************************************/
	public Date exCalcoloLiberazioneAnticipataSuDataFineUltimaPena(int aTotLA, BigDecimal aFascId)
			throws F3BException {

		Connection lConn = null;

		PenaResiduaSqlDAO lPenResSqlDao = null;

		PenaResiduaModel lPenRes = null;
		Date lDataFineRicalcolata = null;

		try {
			// Controllo esistenza giorni di LA (se non trovati non effettua il calcolo)
			if (aTotLA == 0)
				return lDataFineRicalcolata;

			lConn = getDBConnection();

			lPenResSqlDao = new PenaResiduaSqlDAO(lConn);

			// Ricerca l'ultima PENA RESIDUA VALIDATA
			lPenResSqlDao.ricercaPenaResiduaCorrenteByFascicoloSiepUltimaValidata(aFascId);
			lPenRes = (PenaResiduaModel) lPenResSqlDao.getModelByKey();

			// Controllo esistenza PENA_RESIDUA (se non trovata non effettua il calcolo)
			// Controllo esistenza DATA_FINE pena (se non trovata non effettua il calcolo)
			if ((lPenRes == null) || (lPenRes.getDataFine() == null))
				return lDataFineRicalcolata;

			lDataFineRicalcolata = DateUtils.moveDateTo(lPenRes.getDataFine(), Calendar.DAY_OF_MONTH,
					-(aTotLA));
		} catch (DAOException daoEx) {
			throw new F3BException(
					"CalcoloPenaController.exCalcoloLiberazioneAnticipataSuDataFineUltimaPena: " + daoEx);
		} catch (F3BException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new F3BException(
					"CalcoloPenaController.exCalcoloLiberazioneAnticipataSuDataFineUltimaPena: " + ex);
		} finally {
			cleanup(lPenResSqlDao);

			cleanup(lConn);
		}

		return lDataFineRicalcolata;
	}

	/**
	 * Recupera il quantum totale Reclusione e le multe, di tutte le annotazioni di concessione
	 * (FlagPiuMeno='-') dello stesso tipo del parametro passato in input non VALIDATE. Se isAbInitio = true
	 * vengono prese in considerazione anche tutte le annotazioni di qualunque tipo purchè VALIDATE. Delle
	 * annotazioni NON Validate vengono prese in considerazione solo quelle del tipo passato in input (n.b.
	 * Amnistia e indulto vengono calcolati insieme)
	 *
	 * Vengono ignorate le anticipazioni con FLAG_APP_PROVVISORIA = A o R, quelle cioè inserite con richiesta
	 * al GE.
	 *
	 * @param lFascID
	 * @param aCodTipoAnnotazione
	 * @param isAbInitio
	 *            - se true nel calcolo vengono considerate tutte le Annotazioni manuali VALIDATE, altrimenti
	 *            vengono prese in considerazione solo quelle non validate del tipo passato in input.
	 * @return CalendarModel contenete la somma dei quantum (gg,mm,aa) e gli importi concessi (Reclusione)
	 *         NORMALIZZATI
	 * @throws F3BException
	 */
	public CalendarModel exGetAnnotazioniManualiConcessiReclusione(BigDecimal lFascID,
			String aCodTipoAnnotazione, boolean isAbInitio) throws F3BException {

		Connection lConn = null;

		AnnotazioneManualeSqlDAO lBenDao = null;

		CalendarModel lBenConc = new CalendarModel();

		CalendarUtil lCalCon = new CalendarUtil();

		AnnotazioneManualeModel lAnnMan = new AnnotazioneManualeModel();

		lAnnMan.setFasSieIdFascicoloSiep(lFascID);

		try {
			lConn = getDBConnection();

			// Recupero TUTTE le annotazioni manuali del fascicolo
			lBenDao = new AnnotazioneManualeSqlDAO(lConn);
			lBenDao.ricercaAnnotazioneManualeByIdFascicolo(lFascID);
			lBenDao.start();

			while (lBenDao.next()) {
				lAnnMan = (AnnotazioneManualeModel) lBenDao.getModel();

				// getFlagPiuMeno = '-' cioè concessi (quantum da sottrarre alla pena)
				// getFlagAppProvvisoria <> A e R (quelli provvisori)
				if (lAnnMan.getFlagPiuMeno() != null && lAnnMan.getFlagPiuMeno().equals("-")
						&& lAnnMan.getFlagAppProvvisoria() != null
						&& !lAnnMan.getFlagAppProvvisoria().equals("A")
						&& !lAnnMan.getFlagAppProvvisoria().equals("R")
						&& lAnnMan.getCodTipoAnnotazione() != null) {
					// Se AB INITIO, vengono considerate solo le occorrenze validate
					// e quelle non validate dello stesso tipo di aCodTipoAnnotazione
					// con l'eccezione di AMNISTIA/INDULTO che devono essere considerate
					// insieme
					boolean lAggiungi = true;
					// Scarta le annotazioni che non sono dello stesso tipo di quelle
					// in input (aCodTipoAnnotazione) non validate
					// Quelle validate sono state
					// Di quelle non validate vengono prese in considerazione SOLO quelle
					// dello stesso tipo passato in input, mentre quelle VALIDATE vengono
					// prese in considerazione indipendentemente dal tipo, ma solo se il
					// calcolo è abInizio
					// 002 = Indulto
					// 003 = Amnistia
					if (!lAnnMan.isValidato()) {
						if ("002".equals(aCodTipoAnnotazione) || "003".equals(aCodTipoAnnotazione)) {
							if (!"002".equals(lAnnMan.getCodTipoAnnotazione())
									&& !"003".equals(lAnnMan.getCodTipoAnnotazione())) {
								lAggiungi = false; // scartate
							}
						} else if (!lAnnMan.getCodTipoAnnotazione().equals(aCodTipoAnnotazione)) {
							lAggiungi = false; // scartate
						}
					}

					// Se NON AB INITIO, vengono considerate solo le occorrenze
					// non validate dello stesso tipo di aCodTipoAnnotazione
					// con l'eccezione di AMNISTIA/INDULTO che devono essere considerate
					// insieme
					// Quelle già validate vengono prese in considerazione solo se AbInizio,
					// e in questo caso non importa il tipo annotazione.
					if (!isAbInitio && lAnnMan.isValidato()) {
						lAggiungi = false;
					}

					if (lAggiungi) {
						CalendarModel lCalMod = new CalendarModel();

						lCalMod.setNumAnni(lAnnMan.getNumAnniReclusione());
						lCalMod.setNumMesi(lAnnMan.getNumMesiReclusione());
						lCalMod.setNumGiorni(lAnnMan.getNumGiorniReclusione());

						if (lAnnMan.getImportoMulta() != null)
							lCalMod.setImportoMulta(lAnnMan.getImportoMulta().doubleValue());

						lBenConc = lCalCon.sommaGiornieValute(lBenConc, lCalMod);
					}
				}

				// Se NON ABINITIO Gestisce l'opzione in Conformità/Difformità
				// sulle Annotazioni con Anticipazione degli effetti
				// (cambiando segno a seconda dei casi)
				// getFlagAppProvvisoria = "A" anticipazione
				/*
				 * Da verificare Se il calcolo è abInizio, si parte dall'ultima pena validata a sistema e si
				 * sottraggono i benefici concessi non ancora validati. Se sono presenti benefici Revocati (+)
				 * validati (quindi computati nella pena residua, e questo è tutto da verificare) vengono
				 * sottratti in modo tale che non vengano computati sul quantum finale.
				 *
				 * Questa porzione di codice non tiene conto del fatto che amnistia e indulto vanno di pari
				 * passo
				 *
				 * Il fatto che l'annotazione sia una anticipazione, validata, e il calcolo sia ab inizio,
				 * dovrebbe garantire che tale anticipazione sia stata computata nella pena residua e va
				 * quaindi scomputata.
				 */
				/*
				 * Sommo ai concessi(-) anche i revocati(+) con anticipazione. In questo modo da annullare se
				 * avevo revocato 2 mesi, ne concedo 2 in modo da annullare gli effetti della concessione
				 * 'provvisoria'.
				 */
				if (!isAbInitio && lAnnMan.isAnticipazione() && lAnnMan.isValidato()
						&& lAnnMan.getCodTipoAnnotazione() != null
						&& lAnnMan.getCodTipoAnnotazione().equals(aCodTipoAnnotazione)) {
					if (lAnnMan.getFlagPiuMeno() != null && lAnnMan.getFlagPiuMeno().equals("+")) {
						CalendarModel lCalMod = new CalendarModel();

						lCalMod.setNumAnni(lAnnMan.getNumAnniReclusione());
						lCalMod.setNumMesi(lAnnMan.getNumMesiReclusione());
						lCalMod.setNumGiorni(lAnnMan.getNumGiorniReclusione());

						if (lAnnMan.getImportoMulta() != null)
							lCalMod.setImportoMulta(lAnnMan.getImportoMulta().doubleValue());

						lBenConc = lCalCon.sommaGiornieValute(lBenConc, lCalMod);
					}
				}
			}

			lBenConc = lCalCon.ricalcolaGAM(lBenConc);

			lBenDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"CalcoloPenaController.exGetAnnotazioniManualiConcessiReclusione: " + daoEx);
		} catch (Exception ex) {
			throw new F3BException("CalcoloPenaController.exGetAnnotazioniManualiConcessiReclusione: " + ex);
		} finally {
			cleanup(lBenDao);

			cleanup(lConn);
		}

		return lBenConc;
	}

	/**
	 * <p>
	 * Recupera le annotazioni manuali concesse (FLAG_PIU_MENO = '-') e con FLAG_ANNOTAZIONE_PROVVISORIA = A o
	 * R e <b>VALIDATE</b>.
	 * </p>
	 * Vengono aggiunte quelle non VALIDATE solo dello stesso tipo di quelle passate in input. Se
	 * aCodTipoAnnotazione = 002 o 003 (amnistia/indulto) vengono considerate delle stesso tipo. Vale a dire
	 * che se aCodTipoAnnotazione = indulto, viene prese in considerazione anche l'amnistia e viceversa.
	 * Recupera i soli dati inseriti come richieste al GE sia con anticipazione degli effetti (A) che senza
	 * (R).
	 *
	 */
	public CalendarModel exGetAnnotazioniManualiConcessiAnticipazioneReclusione(BigDecimal lFascID,
			String aCodTipoAnnotazione, boolean isAbInitio) throws F3BException {

		Connection lConn = null;

		AnnotazioneManualeSqlDAO lBenDao = null;

		CalendarModel lBenConc = new CalendarModel();
		AnnotazioneManualeModel lAnnMan = new AnnotazioneManualeModel();
		CalendarUtil lCalCon = new CalendarUtil();

		lAnnMan.setFasSieIdFascicoloSiep(lFascID);

		try {
			lConn = getDBConnection();

			lBenDao = new AnnotazioneManualeSqlDAO(lConn);
			lBenDao.ricercaAnnotazioneManualeByIdFascicolo(lFascID);
			lBenDao.start();

			while (lBenDao.next()) {
				lAnnMan = (AnnotazioneManualeModel) lBenDao.getModel();

				if (lAnnMan.getFlagPiuMeno() != null && lAnnMan.getFlagPiuMeno().equals("-") // concessi
						&& lAnnMan.getFlagAppProvvisoria() != null
						&& (lAnnMan.getFlagAppProvvisoria().equals("A") // con anticipazione
						// || lAnnMan.getFlagAppProvvisoria().equals("R")
						) && lAnnMan.getCodTipoAnnotazione() != null) {
					// Vengono considerate solo le occorrenze validate
					// e quelle non validate dello stesso tipo di aCodTipoAnnotazione
					// con l'eccezione di AMNISTIA/INDULTO che devono essere considerate
					// insieme
					// 002 = Indulto
					// 003 = Amnistia
					boolean lAggiungi = true;

					if (!lAnnMan.isValidato()) {
						if ("002".equals(aCodTipoAnnotazione) || "003".equals(aCodTipoAnnotazione)) {
							if (!"002".equals(lAnnMan.getCodTipoAnnotazione())
									&& !"003".equals(lAnnMan.getCodTipoAnnotazione())) {
								lAggiungi = false;
							}
						} else if (!lAnnMan.getCodTipoAnnotazione().equals(aCodTipoAnnotazione)) { // se
																									// diverso
																									// dal
																									// tipo
																									// annotazione
																									// in
																									// input
																									// lo
																									// scarto
																									// se non
																									// validato
							lAggiungi = false;
						}
					}

					// ====================================================================
					// Se il calcolo NON è ab inizio, tutte le annotazioni validate sono
					// già computate nella pena corrente per cui non vanno ricalcolate
					// ====================================================================
					if (!isAbInitio && lAnnMan.isValidato()) {
						lAggiungi = false; // scarto l'annotazione
					}

					//
					if (lAggiungi) {
						CalendarModel lCalMod = new CalendarModel();

						lCalMod.setNumAnni(lAnnMan.getNumAnniReclusione());
						lCalMod.setNumMesi(lAnnMan.getNumMesiReclusione());
						lCalMod.setNumGiorni(lAnnMan.getNumGiorniReclusione());

						if (lAnnMan.getImportoMulta() != null)
							lCalMod.setImportoMulta(lAnnMan.getImportoMulta().doubleValue());

						lBenConc = lCalCon.sommaGiornieValute(lBenConc, lCalMod);
					}
				}
			}

			lBenConc = lCalCon.ricalcolaGAM(lBenConc);
			lBenDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"CalcoloPenaController.exGetAnnotazioniManualiConcessiAnticipazioneReclusione: " + daoEx);
		} catch (Exception ex) {
			throw new F3BException(
					"CalcoloPenaController.exGetAnnotazioniManualiConcessiAnticipazioneReclusione: " + ex);
		} finally {
			cleanup(lBenDao);

			cleanup(lConn);
		}

		return lBenConc;
	}

	public CalendarModel exGetAnnotazioniManualiRevocatiReclusione(BigDecimal lFascID,
			String aCodTipoAnnotazione, boolean isAbInitio) throws F3BException {

		Connection lConn = null;

		AnnotazioneManualeSqlDAO lBenDao = null;
		CalendarModel lBenConc = new CalendarModel();
		AnnotazioneManualeModel lAnnMan = new AnnotazioneManualeModel();
		CalendarUtil lCalCon = new CalendarUtil();

		lAnnMan.setFasSieIdFascicoloSiep(lFascID);

		try {
			lConn = getDBConnection();
			lBenDao = new AnnotazioneManualeSqlDAO(lConn);
			lBenDao.ricercaAnnotazioneManualeByIdFascicolo(lFascID);

			lBenDao.start();
			while (lBenDao.next()) {
				lAnnMan = (AnnotazioneManualeModel) lBenDao.getModel();
				if (lAnnMan.getFlagPiuMeno() != null && lAnnMan.getFlagPiuMeno().equals("+")
						&& lAnnMan.getFlagAppProvvisoria() != null
						&& !lAnnMan.getFlagAppProvvisoria().equals("A")
						&& !lAnnMan.getFlagAppProvvisoria().equals("R")
						&& lAnnMan.getCodTipoAnnotazione() != null) {
					// Se AB INITIO, vengono considerate solo le occorrenze validate
					// e quelle non validate dello stesso tipo di aCodTipoAnnotazione
					// con l'eccezione di AMNISTIA/INDULTO che devono essere considerate
					// insieme
					boolean lAggiungi = true;
					if (!lAnnMan.isValidato()) {
						if ("002".equals(aCodTipoAnnotazione) || "003".equals(aCodTipoAnnotazione)) {
							if (!"002".equals(lAnnMan.getCodTipoAnnotazione())
									&& !"003".equals(lAnnMan.getCodTipoAnnotazione())) {
								lAggiungi = false;
							}
						} else if (!lAnnMan.getCodTipoAnnotazione().equals(aCodTipoAnnotazione)) {
							lAggiungi = false;
						}
					}

					// Se NON AB INITIO, vengono considerate solo le occorrenze
					// non validate dello stesso tipo di aCodTipoAnnotazione
					// con l'eccezione di AMNISTIA/INDULTO che devono essere considerate
					// insieme
					if (!isAbInitio && lAnnMan.isValidato()) {
						lAggiungi = false;
					}

					if (lAggiungi) {
						CalendarModel lCalMod = new CalendarModel();

						lCalMod.setNumAnni(lAnnMan.getNumAnniReclusione());
						lCalMod.setNumMesi(lAnnMan.getNumMesiReclusione());
						lCalMod.setNumGiorni(lAnnMan.getNumGiorniReclusione());
						if (lAnnMan.getImportoMulta() != null)
							lCalMod.setImportoMulta(lAnnMan.getImportoMulta().doubleValue());

						lBenConc = lCalCon.sommaGiornieValute(lBenConc, lCalMod);
					}
				}

				// Se NON ABINITIO Gestisce l'opzione in Conformità/Difformità
				// sulle Annotazioni con Anticipazione degli effetti
				// (cambiando segno a seconda dei casi)
				if (!isAbInitio && lAnnMan.isAnticipazione() && lAnnMan.isValidato()
						&& lAnnMan.getCodTipoAnnotazione() != null
						&& lAnnMan.getCodTipoAnnotazione().equals(aCodTipoAnnotazione)) {
					if (lAnnMan.getFlagPiuMeno() != null && lAnnMan.getFlagPiuMeno().equals("-")) {
						CalendarModel lCalMod = new CalendarModel();

						lCalMod.setNumAnni(lAnnMan.getNumAnniReclusione());
						lCalMod.setNumMesi(lAnnMan.getNumMesiReclusione());
						lCalMod.setNumGiorni(lAnnMan.getNumGiorniReclusione());

						if (lAnnMan.getImportoMulta() != null)
							lCalMod.setImportoMulta(lAnnMan.getImportoMulta().doubleValue());

						lBenConc = lCalCon.sommaGiornieValute(lBenConc, lCalMod);
					}
				}
			}
			lBenConc = lCalCon.ricalcolaGAM(lBenConc);
			lBenDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"CalcoloPenaController.exGetAnnotazioniManualiRevocatiReclusione: " + daoEx);
		} catch (Exception ex) {
			throw new F3BException("CalcoloPenaController.exGetAnnotazioniManualiRevocatiReclusione: " + ex);
		} finally {
			cleanup(lBenDao);

			cleanup(lConn);
		}

		return lBenConc;
	}

	public CalendarModel exGetAnnotazioniManualiRevocatiAnticipazioneReclusione(BigDecimal lFascID,
			String aCodTipoAnnotazione, boolean isAbInitio) throws F3BException {

		Connection lConn = null;

		AnnotazioneManualeSqlDAO lBenDao = null;
		AnnotazioneManualeModel lAnnMan = new AnnotazioneManualeModel();

		CalendarUtil lCalCon = new CalendarUtil();
		CalendarModel lBenConc = new CalendarModel();

		lAnnMan.setFasSieIdFascicoloSiep(lFascID);

		try {
			lConn = getDBConnection();
			lBenDao = new AnnotazioneManualeSqlDAO(lConn);
			lBenDao.ricercaAnnotazioneManualeByIdFascicolo(lFascID);

			lBenDao.start();
			while (lBenDao.next()) {
				lAnnMan = (AnnotazioneManualeModel) lBenDao.getModel();

				if (lAnnMan.getFlagPiuMeno() != null && lAnnMan.getFlagPiuMeno().equals("+")
						&& lAnnMan.getFlagAppProvvisoria() != null
						&& (lAnnMan.getFlagAppProvvisoria().equals("A")
						// || lAnnMan.getFlagAppProvvisoria().equals("R")
						) && lAnnMan.getCodTipoAnnotazione() != null) {
					// Vengono considerate solo le occorrenze validate
					// e quelle non validate dello stesso tipo di aCodTipoAnnotazione
					// con l'eccezione di AMNISTIA/INDULTO che devono essere considerate
					// insieme
					boolean lAggiungi = true;
					if (!lAnnMan.isValidato()) {
						if ("002".equals(aCodTipoAnnotazione) || "003".equals(aCodTipoAnnotazione)) {
							if (!"002".equals(lAnnMan.getCodTipoAnnotazione())
									&& !"003".equals(lAnnMan.getCodTipoAnnotazione())) {
								lAggiungi = false;
							}
						} else if (!lAnnMan.getCodTipoAnnotazione().equals(aCodTipoAnnotazione)) {
							lAggiungi = false;
						}
					}

					// ====================================================================
					// Se il calcolo NON è ab inizio, tutte le annotazioni validate sono
					// già computate nella pena corrente per cui non vanno ricalcolate
					// ====================================================================
					if (!isAbInitio && lAnnMan.isValidato()) {
						lAggiungi = false; // scarto l'annotazione
					}

					if (lAggiungi) {
						CalendarModel lCalMod = new CalendarModel();

						lCalMod.setNumAnni(lAnnMan.getNumAnniReclusione());
						lCalMod.setNumMesi(lAnnMan.getNumMesiReclusione());
						lCalMod.setNumGiorni(lAnnMan.getNumGiorniReclusione());

						if (lAnnMan.getImportoMulta() != null)
							lCalMod.setImportoMulta(lAnnMan.getImportoMulta().doubleValue());

						lBenConc = lCalCon.sommaGiornieValute(lBenConc, lCalMod);
					}
				}
			}
			lBenConc = lCalCon.ricalcolaGAM(lBenConc);
			lBenDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"CalcoloPenaController.exGetAnnotazioniManualiRevocatiAnticipazioneReclusione: " + daoEx);
		} catch (Exception ex) {
			throw new F3BException(
					"CalcoloPenaController.exGetAnnotazioniManualiRevocatiAnticipazioneReclusione: " + ex);
		} finally {
			cleanup(lBenDao);

			cleanup(lConn);
		}

		return lBenConc;
	}

	public CalendarModel exGetAnnotazioniManualiRevocatiArresto(BigDecimal lFascID,
			String aCodTipoAnnotazione, boolean isAbInitio) throws F3BException {

		Connection lConn = null;
		AnnotazioneManualeSqlDAO lBenDao = null;
		CalendarModel lBenConc = new CalendarModel();
		AnnotazioneManualeModel lAnnMan = new AnnotazioneManualeModel();
		CalendarUtil lCalCon = new CalendarUtil();

		lAnnMan.setFasSieIdFascicoloSiep(lFascID);

		try {
			lConn = getDBConnection();
			lBenDao = new AnnotazioneManualeSqlDAO(lConn);
			lBenDao.ricercaAnnotazioneManualeByIdFascicolo(lFascID);
			lBenDao.start();
			while (lBenDao.next()) {
				lAnnMan = (AnnotazioneManualeModel) lBenDao.getModel();
				if (lAnnMan.getFlagPiuMeno() != null && lAnnMan.getFlagPiuMeno().equals("+")
						&& lAnnMan.getFlagAppProvvisoria() != null
						&& !lAnnMan.getFlagAppProvvisoria().equals("A")
						&& !lAnnMan.getFlagAppProvvisoria().equals("R")
						&& lAnnMan.getCodTipoAnnotazione() != null) {
					// Se AB INITIO, vengono considerate solo le occorrenze validate
					// e quelle non validate dello stesso tipo di aCodTipoAnnotazione
					// con l'eccezione di AMNISTIA/INDULTO che devono essere considerate
					// insieme
					boolean lAggiungi = true;
					if (!lAnnMan.isValidato()) {
						if ("002".equals(aCodTipoAnnotazione) || "003".equals(aCodTipoAnnotazione)) {
							if (!"002".equals(lAnnMan.getCodTipoAnnotazione())
									&& !"003".equals(lAnnMan.getCodTipoAnnotazione())) {
								lAggiungi = false;
							}
						} else if (!lAnnMan.getCodTipoAnnotazione().equals(aCodTipoAnnotazione)) {
							lAggiungi = false;
						}
					}

					// Se NON AB INITIO, vengono considerate solo le occorrenze
					// non validate dello stesso tipo di aCodTipoAnnotazione
					// con l'eccezione di AMNISTIA/INDULTO che devono essere considerate
					// insieme
					if (!isAbInitio && lAnnMan.isValidato()) {
						lAggiungi = false;
					}

					if (lAggiungi) {
						CalendarModel lCalMod = new CalendarModel();

						lCalMod.setNumAnni(lAnnMan.getNumAnniArresto());
						lCalMod.setNumMesi(lAnnMan.getNumMesiArresto());
						lCalMod.setNumGiorni(lAnnMan.getNumGiorniArresto());
						if (lAnnMan.getImportoAmmenda() != null)
							lCalMod.setImportoAmmenda(lAnnMan.getImportoAmmenda().doubleValue());

						lBenConc = lCalCon.sommaGiornieValute(lBenConc, lCalMod);
					}
				}

				// Se NON ABINITIO Gestisce l'opzione in Conformità/Difformità
				// sulle Annotazioni con Anticipazione degli effetti
				// (cambiando segno a seconda dei casi)
				if (!isAbInitio && lAnnMan.isAnticipazione() && lAnnMan.isValidato()
						&& lAnnMan.getCodTipoAnnotazione() != null
						&& lAnnMan.getCodTipoAnnotazione().equals(aCodTipoAnnotazione)) {
					if (lAnnMan.getFlagPiuMeno() != null && lAnnMan.getFlagPiuMeno().equals("-")) {
						CalendarModel lCalMod = new CalendarModel();

						lCalMod.setNumAnni(lAnnMan.getNumAnniArresto());
						lCalMod.setNumMesi(lAnnMan.getNumMesiArresto());
						lCalMod.setNumGiorni(lAnnMan.getNumGiorniArresto());

						if (lAnnMan.getImportoAmmenda() != null)
							lCalMod.setImportoAmmenda(lAnnMan.getImportoAmmenda().doubleValue());

						lBenConc = lCalCon.sommaGiornieValute(lBenConc, lCalMod);
					}
				}
			}

			lBenConc = lCalCon.ricalcolaGAM(lBenConc);
			lBenDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"CalcoloPenaController.exGetAnnotazioniManualiRevocatiArresto: Non posso leggere : "
							+ daoEx);
		} catch (Exception ex) {
			throw new F3BException(
					"CalcoloPenaController.exGetAnnotazioniManualiRevocatiArresto: Non posso leggere  : "
							+ ex);
		} finally {
			cleanup(lBenDao);
			cleanup(lConn);
		}

		return lBenConc;
	}

	public CalendarModel exGetAnnotazioniManualiRevocatiAnticipazioneArresto(BigDecimal lFascID,
			String aCodTipoAnnotazione, boolean isAbInitio) throws F3BException {

		Connection lConn = null;

		AnnotazioneManualeSqlDAO lBenDao = null;
		CalendarModel lBenConc = new CalendarModel();
		AnnotazioneManualeModel lAnnMan = new AnnotazioneManualeModel();
		CalendarUtil lCalCon = new CalendarUtil();

		lAnnMan.setFasSieIdFascicoloSiep(lFascID);

		try {
			lConn = getDBConnection();
			lBenDao = new AnnotazioneManualeSqlDAO(lConn);
			lBenDao.ricercaAnnotazioneManualeByIdFascicolo(lFascID);
			lBenDao.start();

			while (lBenDao.next()) {
				lAnnMan = (AnnotazioneManualeModel) lBenDao.getModel();
				if (lAnnMan.getFlagPiuMeno() != null && lAnnMan.getFlagPiuMeno().equals("+")
						&& lAnnMan.getFlagAppProvvisoria() != null
						&& (lAnnMan.getFlagAppProvvisoria().equals("A")
						// || lAnnMan.getFlagAppProvvisoria().equals("R")
						) && lAnnMan.getCodTipoAnnotazione() != null) {
					// Vengono considerate solo le occorrenze validate
					// e quelle non validate dello stesso tipo di aCodTipoAnnotazione
					// con l'eccezione di AMNISTIA/INDULTO che devono essere considerate
					// insieme
					boolean lAggiungi = true;
					if (!lAnnMan.isValidato()) {
						if ("002".equals(aCodTipoAnnotazione) || "003".equals(aCodTipoAnnotazione)) {
							if (!"002".equals(lAnnMan.getCodTipoAnnotazione())
									&& !"003".equals(lAnnMan.getCodTipoAnnotazione())) {
								lAggiungi = false;
							}
						} else if (!lAnnMan.getCodTipoAnnotazione().equals(aCodTipoAnnotazione)) {
							lAggiungi = false;
						}
					}

					// ====================================================================
					// Se il calcolo NON è ab inizio, tutte le annotazioni validate sono
					// già computate nella pena corrente per cui non vanno ricalcolate
					// ====================================================================
					if (!isAbInitio && lAnnMan.isValidato()) {
						lAggiungi = false; // scarto l'annotazione
					}

					if (lAggiungi) {
						CalendarModel lCalMod = new CalendarModel();

						lCalMod.setNumAnni(lAnnMan.getNumAnniArresto());
						lCalMod.setNumMesi(lAnnMan.getNumMesiArresto());
						lCalMod.setNumGiorni(lAnnMan.getNumGiorniArresto());

						if (lAnnMan.getImportoAmmenda() != null)
							lCalMod.setImportoAmmenda(lAnnMan.getImportoAmmenda().doubleValue());

						lBenConc = lCalCon.sommaGiornieValute(lBenConc, lCalMod);
					}
				}
			}
			lBenConc = lCalCon.ricalcolaGAM(lBenConc);
			lBenDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"CalcoloPenaController.exGetAnnotazioniManualiRevocatiAnticipazioneArresto: " + daoEx);
		} catch (Exception ex) {
			throw new F3BException(
					"CalcoloPenaController.exGetAnnotazioniManualiRevocatiAnticipazioneArresto: " + ex);
		} finally {
			cleanup(lBenDao);
			cleanup(lConn);
		}

		return lBenConc;
	}

	public CalendarModel exGetAnnotazioniManualiConcessiArresto(BigDecimal lFascID,
			String aCodTipoAnnotazione, boolean isAbInitio) throws F3BException {

		Connection lConn = null;
		AnnotazioneManualeSqlDAO lBenDao = null;
		CalendarModel lBenConc = new CalendarModel();
		AnnotazioneManualeModel lAnnMan = new AnnotazioneManualeModel();
		CalendarUtil lCalCon = new CalendarUtil();

		lAnnMan.setFasSieIdFascicoloSiep(lFascID);

		try {
			lConn = getDBConnection();
			lBenDao = new AnnotazioneManualeSqlDAO(lConn);
			lBenDao.ricercaAnnotazioneManualeByIdFascicolo(lFascID);
			lBenDao.start();
			while (lBenDao.next()) {
				lAnnMan = (AnnotazioneManualeModel) lBenDao.getModel();
				if (lAnnMan.getFlagPiuMeno() != null && lAnnMan.getFlagPiuMeno().equals("-")
						&& lAnnMan.getFlagAppProvvisoria() != null
						&& !lAnnMan.getFlagAppProvvisoria().equals("A") // Con anticipazione
						&& !lAnnMan.getFlagAppProvvisoria().equals("R") // Senza Anticipazione
						&& lAnnMan.getCodTipoAnnotazione() != null) {
					// Se AB INITIO, vengono considerate solo le occorrenze validate
					// e quelle non validate dello stesso tipo di aCodTipoAnnotazione
					// con l'eccezione di AMNISTIA/INDULTO che devono essere considerate
					// insieme
					boolean lAggiungi = true;
					if (!lAnnMan.isValidato()) {
						if ("002".equals(aCodTipoAnnotazione) || "003".equals(aCodTipoAnnotazione)) {
							if (!"002".equals(lAnnMan.getCodTipoAnnotazione())
									&& !"003".equals(lAnnMan.getCodTipoAnnotazione())) {
								lAggiungi = false;
							}
						} else if (!lAnnMan.getCodTipoAnnotazione().equals(aCodTipoAnnotazione)) {
							lAggiungi = false;
						}
					}

					// Se NON AB INITIO, vengono considerate solo le occorrenze
					// non validate dello stesso tipo di aCodTipoAnnotazione
					// con l'eccezione di AMNISTIA/INDULTO che devono essere considerate
					// insieme
					if (!isAbInitio && lAnnMan.isValidato()) {
						lAggiungi = false;
					}

					if (lAggiungi) {
						CalendarModel lCalMod = new CalendarModel();

						lCalMod.setNumAnni(lAnnMan.getNumAnniArresto());
						lCalMod.setNumMesi(lAnnMan.getNumMesiArresto());
						lCalMod.setNumGiorni(lAnnMan.getNumGiorniArresto());

						if (lAnnMan.getImportoAmmenda() != null)
							lCalMod.setImportoAmmenda(lAnnMan.getImportoAmmenda().doubleValue());

						lBenConc = lCalCon.sommaGiornieValute(lBenConc, lCalMod);
					}
				}

				// Se NON ABINITIO Gestisce l'opzione in Conformità/Difformità
				// sulle Annotazioni con Anticipazione degli effetti
				// (cambiando segno a seconda dei casi)
				if (!isAbInitio && lAnnMan.isAnticipazione() && lAnnMan.isValidato()
						&& lAnnMan.getCodTipoAnnotazione() != null
						&& lAnnMan.getCodTipoAnnotazione().equals(aCodTipoAnnotazione)) {
					if (lAnnMan.getFlagPiuMeno() != null && lAnnMan.getFlagPiuMeno().equals("+")) {
						CalendarModel lCalMod = new CalendarModel();

						lCalMod.setNumAnni(lAnnMan.getNumAnniArresto());
						lCalMod.setNumMesi(lAnnMan.getNumMesiArresto());
						lCalMod.setNumGiorni(lAnnMan.getNumGiorniArresto());

						if (lAnnMan.getImportoAmmenda() != null)
							lCalMod.setImportoAmmenda(lAnnMan.getImportoAmmenda().doubleValue());

						lBenConc = lCalCon.sommaGiornieValute(lBenConc, lCalMod);
					}
				}
			}
			lBenConc = lCalCon.ricalcolaGAM(lBenConc);
			lBenDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"CalcoloPenaController.exGetAnnotazioniManualiRevocatiArresto: Non posso leggere : "
							+ daoEx);
		} catch (Exception ex) {
			throw new F3BException(
					"CalcoloPenaController.exGetAnnotazioniManualiRevocatiArresto: Non posso leggere  : "
							+ ex);
		} finally {
			cleanup(lBenDao);
			cleanup(lConn);
		}

		return lBenConc;
	}

	/**
	 *
	 */
	public CalendarModel exGetAnnotazioniManualiConcessiAnticipazioneArresto(BigDecimal lFascID,
			String aCodTipoAnnotazione, boolean isAbInitio) throws F3BException {

		Connection lConn = null;

		AnnotazioneManualeSqlDAO lBenDao = null;
		AnnotazioneManualeModel lAnnMan = new AnnotazioneManualeModel();

		CalendarModel lBenConc = new CalendarModel();

		CalendarUtil lCalCon = new CalendarUtil();

		lAnnMan.setFasSieIdFascicoloSiep(lFascID);

		try {
			lConn = getDBConnection();
			lBenDao = new AnnotazioneManualeSqlDAO(lConn);
			lBenDao.ricercaAnnotazioneManualeByIdFascicolo(lFascID);
			lBenDao.start();

			while (lBenDao.next()) {
				lAnnMan = (AnnotazioneManualeModel) lBenDao.getModel();

				//
				if (lAnnMan.getFlagPiuMeno() != null && lAnnMan.getFlagPiuMeno().equals("-")
						&& lAnnMan.getFlagAppProvvisoria() != null
						&& (lAnnMan.getFlagAppProvvisoria().equals("A")
						// || lAnnMan.getFlagAppProvvisoria().equals("R")
						) && lAnnMan.getCodTipoAnnotazione() != null) {
					// TODO perchè prende in considerazione anche quelle con flag R??
					// Vengono considerate solo le occorrenze validate
					// e quelle non validate dello stesso tipo di aCodTipoAnnotazione
					// con l'eccezione di AMNISTIA/INDULTO che devono essere considerate
					// insieme
					boolean lAggiungi = true;
					if (!lAnnMan.isValidato()) {
						if ("002".equals(aCodTipoAnnotazione) || "003".equals(aCodTipoAnnotazione)) {
							if (!"002".equals(lAnnMan.getCodTipoAnnotazione())
									&& !"003".equals(lAnnMan.getCodTipoAnnotazione())) {
								lAggiungi = false;
							}
						} else if (!lAnnMan.getCodTipoAnnotazione().equals(aCodTipoAnnotazione)) {
							lAggiungi = false;
						}
					}

					// ====================================================================
					// Se il calcolo NON è ab inizio, tutte le annotazioni validate sono
					// già computate nella pena corrente per cui non vanno ricalcolate
					// ====================================================================
					if (!isAbInitio && lAnnMan.isValidato()) {
						lAggiungi = false; // scarto l'annotazione
					}

					if (lAggiungi) {
						CalendarModel lCalMod = new CalendarModel();

						lCalMod.setNumAnni(lAnnMan.getNumAnniArresto());
						lCalMod.setNumMesi(lAnnMan.getNumMesiArresto());
						lCalMod.setNumGiorni(lAnnMan.getNumGiorniArresto());

						if (lAnnMan.getImportoAmmenda() != null)
							lCalMod.setImportoAmmenda(lAnnMan.getImportoAmmenda().doubleValue());

						lBenConc = lCalCon.sommaGiornieValute(lBenConc, lCalMod);
					}
				}
			}
			lBenConc = lCalCon.ricalcolaGAM(lBenConc);
			lBenDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"CalcoloPenaController.exGetAnnotazioniManualiConcessiAnticipazioneArresto: " + daoEx);
		} catch (Exception ex) {
			throw new F3BException(
					"CalcoloPenaController.exGetAnnotazioniManualiConcessiAnticipazioneArresto: " + ex);
		} finally {
			cleanup(lBenDao);
			cleanup(lConn);
		}

		return lBenConc;
	}

	/*
	 * ------------------------------------------------------------------------------ Funzione
	 * exCalcoloRevocheMisureAlternative Per definizione, o arriva una data (aDataDal) o un quantum
	 * (aQuantumRevocato) il flag a30giorni se true, fa scattare una detrazione di 30 gg dal quantum
	 * **************************************
	 * ------------------------------------------------------------------------------
	 */
	/**
	 * Effettua il calcolo della pena residua in caso di Revoca di una Misura Alternativa. Ricalcola i quantum
	 * e se detenuto anche i periodi di espiazione. Se i quantum sono negativi lPenRes.setMessage("Errore").
	 *
	 * Il ricalcolo dei quantum parte o dal parametro aDataDAL o da aQuantumRevocatoReclusione,
	 * aQuantumRevocatoArresto.
	 *
	 * Se è specificato il parametro aDataDAL si rieffettuano i calcoli a partire dall'ultima pena Validata in
	 * decorrenza (data fine!=null), altrimenti dall'ultima pena validata. I quantum vengono ricalcolati come
	 * se la pena dovesse ripartire dalla data specificata.
	 *
	 * Il parametro a30giorni viene detratto dai quantum ricalcolati in base alla aDataDAL in quanto giorni da
	 * considerare già espiati. Le modalità di calcolo variano in funzione dello aStatoDetenuto e dei dati in
	 * ingresso. In particolare: aStatoDetenuto = 0 o 1 si comportano nello stesso identico modo. Vengono
	 * ricalcolati i quantum se specificata aDataDAL, o altrimenti registrati quelli imputati
	 * (aQuantumRevocatoReclusione, aQuantumRevocatoArresto). Vengono scalati a30giorni (prima da reclusione e
	 * quindi da arresto). Vengono ricalcolate le date di espiazione a partire dalla data aNuovoInizioPena
	 * solo se aDetenuto = S. aStatoDetenuto = 2 Vengono rideterminati solo i quantum dalla aDataDAL o da
	 * quanto imputato aQuantumRevocatoReclusione, aQuantumRevocatoArresto Non vengono ricalcolate le date di
	 * espiazione, ne considerati i a30giorni
	 *
	 * @param lFascID
	 *            = id del fascicolo
	 * @param aDataDAL
	 *            = data dalla quale è stato revocata la Misura
	 * @param aQuantumRevocatoReclusione
	 *            = quantum revocato di reclusione
	 * @param aQuantumRevocatoArresto
	 *            = quantum revocato di arresto
	 * @param a30giorni
	 *            = periodo espiato in carcere da detrarre (in giorni)
	 * @param aStatoDetenuto
	 *            : 0 = Sospensione provvisoria 1 = Perdita Efficacia 2 = Affidamento concluso
	 * @param aNuovoInizioPena
	 *            = nuova data inizio pena per effettuare i calcoli
	 * @param aDetenuto
	 *            = S-N
	 */
	public CalcoloPenaModel exCalcoloRevocheMisureAlternative(BigDecimal lFascID, Date aDataDAL,
			CalendarModel aQuantumRevocatoReclusione, CalendarModel aQuantumRevocatoArresto, String a30giorni,
			int aStatoDetenuto, Date aNuovoInizioPena, String aDetenuto, CalcoloPenaModel aCalcoloPenaModel)
			throws F3BException {

		/*
		 * ----------------------------------------------------------------------------- Preliminare : carico
		 * la PenaResidua del Fascicolo Si da per scontato che esista un record VALIDATO di PenaResidua !!!
		 * -----------------------------------------------------------------------------
		 */

		siesLogger.debug("aDataDAL:" + aDataDAL);
		siesLogger.debug("aQuantumRevocatoReclusione:" + aQuantumRevocatoReclusione);
		siesLogger.debug("aQuantumRevocatoArresto:" + aQuantumRevocatoArresto);
		siesLogger.debug("a30giorni:" + a30giorni);
		siesLogger.debug("aStatoDetenuto:" + aStatoDetenuto);
		siesLogger.debug("aNuovoInizioPena:" + aNuovoInizioPena);
		siesLogger.debug("aDetenuto:" + aDetenuto);

		BigDecimal Zero = new BigDecimal("0");

		PenaResiduaModel lPenRes = new PenaResiduaModel();
		IPenaResidua lPen = SIEPLookupRemote.getPenaResiduaRemote();

		if (aDataDAL != null) { // Ultima validata con data fine pena valorizzata (soggetto in espiazione?)
			lPenRes = lPen.ExRicercaPenaResiduaUltimaValidataDataFinePena(lFascID);
		} else { // Ultima validata
			lPenRes = lPen.ExRicercaPenaResiduaUltimaValidata(lFascID);
		}

		String lFlagErgastolo = "N";
		String lFlagDiesAQuo = "S";

		CalendarModel lTmp = new CalendarModel();
		CalendarModel lTmp2 = new CalendarModel();
		CalendarUtil lCal = new CalendarUtil();
		boolean QuantumNegativo_error = false;

		/*
		 * --------------------------------------------------------------- A secondo del Flag di stato del
		 * detenuto !! aStatoDetenuto=0 : Sospensione provvisoria aStatoDetenuto=1 : Perdita Efficacia
		 * aStatoDetenuto=2 : Affidamento concluso ------------
		 * ---------------------------------------------------
		 */

		/*
		 * TODO attenzione, con la nuova versione della gestione delle LA, si potrebbero avere sia reclusione
		 * che arresto ma data fine reclusione a null
		 */
		switch (aStatoDetenuto) {
		case 0:
		case 1:
			if (aDataDAL != null) { // CASO DataDAL
				if (lPenRes == null) {
					throw new SIEPException(SIEPException.USER_MESSAGE,
							"Impossibile emettere provvedimento. Data Fine Pena non valorizzata.");
				}

				if (lPenRes.getDataFine().before(aDataDAL)) {
					throw new SIEPException(SIEPException.USER_MESSAGE,
							"Impossibile emettere provvedimento. Data Revoca Maggiore data fine pena.");
				}

				lFlagErgastolo = lPenRes.getFlagErgastolo();
				lFlagDiesAQuo = lPenRes.getDiesAQuo();

				try {
					// Torno indietro di un giorno per considerare il giorno di revoca
					// come giorno da espiare nel pena residua
					aDataDAL = DateUtils.moveDateTo(aDataDAL, Calendar.DAY_OF_MONTH, -1);

					// ------------ GESTIONE DEI QUANTUM -------------------------------
					// 1- Effettuo un precalcolo della pena per verificare quale è la pena di
					// partenza.
					PenaResiduaModel lPenaDiPartenza = aCalcoloPenaModel
							.getPenaDaEspiare(lPenRes.getDataInizio(), null, null);
					// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
					// di LogF3B.getLogger()
					// siesLogger.debug("**************************************************");
					// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
					// di LogF3B.getLogger()
					// siesLogger.debug("lPenaDiPartenza = "+lPenaDiPartenza);
					// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
					// di LogF3B.getLogger()
					// siesLogger.debug("**************************************************");

					aCalcoloPenaModel.calcolaPenaDaSospensione(lPenaDiPartenza, aDataDAL);
					lPenaDiPartenza = aCalcoloPenaModel.getPenaResiduaRicalcolata();

					CalendarModel lPenaEspiata = aCalcoloPenaModel.getPenaEspiata();
					// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
					// di LogF3B.getLogger()
					// siesLogger.debug("**************************************************");
					// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
					// di LogF3B.getLogger()
					// siesLogger.debug("lPenaEspiata Senza a30Giorni= "+lPenaEspiata);
					// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
					// di LogF3B.getLogger()
					// siesLogger.debug("**************************************************");

					// 2- Viene utilizzato un Model di appoggio per sottrarre
					// gli eventuali giorni specificati dai quantum espiati
					// (prima da reclusione e poi da arresto)
					CalcoloPenaModel lCalcoloModelApp = new CalcoloPenaModel();
					lCalcoloModelApp.setTipoPenaIniziale(ICostantiCalcoloPena.PENA_MANUALE);

					Date lDataInizioPena = null;
					if (aDetenuto != null && aDetenuto.equals("S")) {
						lDataInizioPena = aNuovoInizioPena;
					}

					lCalcoloModelApp.setPenaResiduaManuale(lPenaDiPartenza);

					// 3- Se specificato il parametro occorre togliere i giorni
					// dai quantum di pena. Questi vengono simulati
					// da un'annotazione manuale.
					// Vengono tolti alla pena da espiare e vengono aggiunti all'espiato.
					if (a30giorni != null) {
						AnnotazioneManualeModel lAnnRichiesta = new AnnotazioneManualeModel();
						lAnnRichiesta.setNumGiorniReclusione(new BigDecimal(a30giorni));
						lAnnRichiesta.setFlagPiuMeno("-");

						lCalcoloModelApp.getIndultoR().add(lAnnRichiesta);

						CalendarModel aCalendarAppo = new CalendarModel();
						aCalendarAppo.setNumGiorni(new BigDecimal(a30giorni));
						lPenaEspiata = lCal.sommaGiorni(lPenaEspiata, aCalendarAppo);

						aCalcoloPenaModel.setPenaEspiata(lPenaEspiata);

						// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
						// posto di LogF3B.getLogger()
						// siesLogger.debug("**************************************************");
						// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
						// posto di LogF3B.getLogger()
						// siesLogger.debug("lPenaEspiata CON a30Giorni= "+lPenaEspiata);
						// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
						// posto di LogF3B.getLogger()
						// siesLogger.debug("**************************************************");
					}

					PenaResiduaModel lPenaRicalcolata = lCalcoloModelApp.getPenaDaEspiare(lDataInizioPena,
							null, "all");

					lPenaRicalcolata = lCalcoloModelApp.getPenaResiduaRicalcolata();

					lPenaRicalcolata.setIdPenaResidua(lPenRes.getIdPenaResidua());

					// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
					// di LogF3B.getLogger()
					// siesLogger.debug("**************************************************");
					// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
					// di LogF3B.getLogger()
					// siesLogger.debug("lPenaRicalcolata = "+lPenaRicalcolata);
					// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
					// di LogF3B.getLogger()
					// siesLogger.debug("**************************************************");

					lPenRes = lPenaRicalcolata;
				} catch (Exception ex) {
					ex.printStackTrace();
					throw new SIEPException(SIEPException.USER_MESSAGE, ex.getMessage());
				}
			} else { // Quantum
				// non è stata specificata la aDataDal (data revoca) ma solo i quantum
				lPenRes.setNumAnniArresto(new BigDecimal(aQuantumRevocatoArresto.getNumAnni() + ""));
				lPenRes.setNumMesiArresto(new BigDecimal(aQuantumRevocatoArresto.getNumMesi() + ""));
				lPenRes.setNumGiorniArresto(new BigDecimal(aQuantumRevocatoArresto.getNumGiorni() + ""));

				lPenRes.setNumAnniReclusione(new BigDecimal(aQuantumRevocatoReclusione.getNumAnni() + ""));
				lPenRes.setNumMesiReclusione(new BigDecimal(aQuantumRevocatoReclusione.getNumMesi() + ""));
				lPenRes.setNumGiorniReclusione(
						new BigDecimal(aQuantumRevocatoReclusione.getNumGiorni() + ""));

				if (a30giorni != null) {
					lTmp = new CalendarModel(aQuantumRevocatoReclusione);
					lTmp2.setNumGiorni(new BigDecimal(a30giorni));
					lTmp = lCal.sottraiGiorni(lTmp, lTmp2);

					// 20/12/2004
					lPenRes.setNumAnniReclusione(new BigDecimal(lTmp.getNumAnni() + ""));
					lPenRes.setNumMesiReclusione(new BigDecimal(lTmp.getNumMesi() + ""));
					lPenRes.setNumGiorniReclusione(new BigDecimal(lTmp.getNumGiorni() + ""));

					if (!lCal.isPositiveTime(lTmp)) {
						lPenRes.setNumAnniReclusione(Zero);
						lPenRes.setNumMesiReclusione(Zero);
						lPenRes.setNumGiorniReclusione(Zero);

						lTmp = new CalendarModel(aQuantumRevocatoArresto);
						lTmp2.setNumGiorni(new BigDecimal(a30giorni));
						lTmp = lCal.sottraiGiorni(lTmp, lTmp2);

						lPenRes.setNumAnniArresto(new BigDecimal(lTmp.getNumAnni() + ""));
						lPenRes.setNumMesiArresto(new BigDecimal(lTmp.getNumMesi() + ""));
						lPenRes.setNumGiorniArresto(new BigDecimal(lTmp.getNumGiorni() + ""));
					}
				}

				// 04-10-2004 dario
				// si calcolano le date decorrenza scadenza
				if (aDetenuto != null && aDetenuto.equals("S")) {
					lPenRes.setDataInizio(aNuovoInizioPena);
					if (!(lPenRes.getNumAnniReclusione().equals(Zero)
							&& lPenRes.getNumMesiReclusione().equals(Zero)
							&& lPenRes.getNumGiorniReclusione().equals(Zero))
							&& (lPenRes.getNumAnniArresto().equals(Zero)
									&& lPenRes.getNumMesiArresto().equals(Zero)
									&& lPenRes.getNumGiorniArresto().equals(Zero))) {
						// Solo Reclusione
						lTmp = new CalendarModel();
						lTmp.setNumAnni(lPenRes.getNumAnniReclusione());
						lTmp.setNumMesi(lPenRes.getNumMesiReclusione());
						lTmp.setNumGiorni(lPenRes.getNumGiorniReclusione());

						// lPenRes.setDataFine(exCalcolaNuovaDataFine(aNuovoInizioPena,lTmp,true));
						lPenRes.setDataFinePresunta(exCalcolaNuovaDataFine(aNuovoInizioPena, lTmp, true));
						lPenRes.setDataFine(null);

						lPenRes.setDataFineReclusione(null);
						lPenRes.setDataInizioArresto(null);
					}

					if ((lPenRes.getNumAnniReclusione().equals(Zero)
							&& lPenRes.getNumMesiReclusione().equals(Zero)
							&& lPenRes.getNumGiorniReclusione().equals(Zero))
							&& !(lPenRes.getNumAnniArresto().equals(Zero)
									&& lPenRes.getNumMesiArresto().equals(Zero)
									&& lPenRes.getNumGiorniArresto().equals(Zero))) {
						// Solo arresto
						lTmp = new CalendarModel();
						lTmp.setNumAnni(lPenRes.getNumAnniArresto());
						lTmp.setNumMesi(lPenRes.getNumMesiArresto());
						lTmp.setNumGiorni(lPenRes.getNumGiorniArresto());

						// lPenRes.setDataFine(exCalcolaNuovaDataFine(aNuovoInizioPena,lTmp,true));
						lPenRes.setDataFinePresunta(exCalcolaNuovaDataFine(aNuovoInizioPena, lTmp, true));
						lPenRes.setDataFine(null);

						lPenRes.setDataFineReclusione(null);
						lPenRes.setDataInizioArresto(null);
					}

					if (!(lPenRes.getNumAnniReclusione().equals(Zero)
							&& lPenRes.getNumMesiReclusione().equals(Zero)
							&& lPenRes.getNumGiorniReclusione().equals(Zero))
							&& !(lPenRes.getNumAnniArresto().equals(Zero)
									&& lPenRes.getNumMesiArresto().equals(Zero)
									&& lPenRes.getNumGiorniArresto().equals(Zero))) {
						// Reclusione + Arresto
						lTmp = new CalendarModel();
						lTmp.setNumAnni(lPenRes.getNumAnniReclusione());
						lTmp.setNumMesi(lPenRes.getNumMesiReclusione());
						lTmp.setNumGiorni(lPenRes.getNumGiorniReclusione());
						lPenRes.setDataFineReclusione(exCalcolaNuovaDataFine(aNuovoInizioPena, lTmp, true));
						lPenRes.setDataInizioArresto(DateUtils.getDayAfter(lPenRes.getDataFineReclusione()));

						lTmp = new CalendarModel();
						lTmp.setNumAnni(lPenRes.getNumAnniArresto());
						lTmp.setNumMesi(lPenRes.getNumMesiArresto());
						lTmp.setNumGiorni(lPenRes.getNumGiorniArresto());

						// lPenRes.setDataFine(exCalcolaNuovaDataFine(lPenRes.getDataInizioArresto(),lTmp,true));
						lPenRes.setDataFinePresunta(
								exCalcolaNuovaDataFine(lPenRes.getDataInizioArresto(), lTmp, true));
						lPenRes.setDataFine(null);
					}
				} else {
					lPenRes.setDataInizio(null);
					lPenRes.setDataFineReclusione(null);
					lPenRes.setDataInizioArresto(null);
					lPenRes.setDataFinePresunta(null);
					lPenRes.setDataFine(null);
				}
			}

			// ERR!! Se sono presenti sia reclusione che arresto, l'unico quantum
			// che può diventare negativo per effetto della sottrazione dei
			// a30giorni è l'arresto. In questo caso il primo blocco imposta
			// QuantumNegativo_error = true, mentre il secondo lo riporta a
			// false in quanto la reclusione è 0
			lTmp = new CalendarModel();
			lTmp.setNumAnni(lPenRes.getNumAnniArresto());
			lTmp.setNumMesi(lPenRes.getNumMesiArresto());
			lTmp.setNumGiorni(lPenRes.getNumGiorniArresto());
			QuantumNegativo_error = !(lCal.isPositiveTime(lTmp));

			lTmp = new CalendarModel();
			lTmp.setNumAnni(lPenRes.getNumAnniReclusione());
			lTmp.setNumMesi(lPenRes.getNumMesiReclusione());
			lTmp.setNumGiorni(lPenRes.getNumGiorniReclusione());
			QuantumNegativo_error = !(lCal.isPositiveTime(lTmp));

			break;
		case 2:
			// ====================================================================
			// Affidamento concluso
			// ====================================================================
			if (aDataDAL != null) { // CASO DataDAL
				if (lPenRes == null) {
					throw new SIEPException(SIEPException.USER_MESSAGE,
							"Impossibile emettere provvedimento.Data fine pena non valorizzata.");
				}

				if (lPenRes.getDataFine().before(aDataDAL)) {
					throw new SIEPException(SIEPException.USER_MESSAGE,
							"Impossibile emettere provvedimento.Data revoca Maggiore data fine pena.");
				}

				lFlagErgastolo = lPenRes.getFlagErgastolo();
				lFlagDiesAQuo = lPenRes.getDiesAQuo();

				try {
					// Torno indietro di un giorno per considerare il giorno di revoca
					// come giorno da espiare nel pena residua
					aDataDAL = DateUtils.moveDateTo(aDataDAL, Calendar.DAY_OF_MONTH, -1);

					// ------------ GESTIONE DEI QUANTUM -------------------------------
					// 1- Effettuo un precalcolo della pena per verificare quale è la pena di
					// partenza.
					PenaResiduaModel lPenaDiPartenza = aCalcoloPenaModel
							.getPenaDaEspiare(lPenRes.getDataInizio(), null, null);
					// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
					// di LogF3B.getLogger()
					// siesLogger.debug("**************************************************");
					// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
					// di LogF3B.getLogger()
					// siesLogger.debug("lPenaDiPartenza CASE 2= "+lPenaDiPartenza);
					// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
					// di LogF3B.getLogger()
					// siesLogger.debug("**************************************************");

					aCalcoloPenaModel.calcolaPenaDaSospensione(lPenaDiPartenza, aDataDAL);
					lPenaDiPartenza = aCalcoloPenaModel.getPenaResiduaRicalcolata();

					// CalendarModel lPenaEspiata = aCalcoloPenaModel.getPenaEspiata();
					// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
					// di LogF3B.getLogger()
					// siesLogger.debug("**************************************************");
					// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
					// di LogF3B.getLogger()
					// siesLogger.debug("lPenaEspiata CASE 2= "+lPenaEspiata);
					// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
					// di LogF3B.getLogger()
					// siesLogger.debug("**************************************************");

					lPenaDiPartenza.setIdPenaResidua(lPenRes.getIdPenaResidua());

					lPenRes = lPenaDiPartenza;
				} catch (Exception ex) {
					ex.printStackTrace();
					throw new SIEPException(SIEPException.USER_MESSAGE, ex.getMessage());
				}
			} else { // Quantum
				// Specificati i soli quantum
				lPenRes.setNumAnniReclusione(new BigDecimal(aQuantumRevocatoReclusione.getNumAnni() + ""));
				lPenRes.setNumMesiReclusione(new BigDecimal(aQuantumRevocatoReclusione.getNumMesi() + ""));
				lPenRes.setNumGiorniReclusione(
						new BigDecimal(aQuantumRevocatoReclusione.getNumGiorni() + ""));

				lPenRes.setNumAnniArresto(new BigDecimal(aQuantumRevocatoArresto.getNumAnni() + ""));
				lPenRes.setNumMesiArresto(new BigDecimal(aQuantumRevocatoArresto.getNumMesi() + ""));
				lPenRes.setNumGiorniArresto(new BigDecimal(aQuantumRevocatoArresto.getNumGiorni() + ""));
			}

			lPenRes.setDataFine(null);
			lPenRes.setDataFinePresunta(null);
			lPenRes.setDataInizio(null);

			break;
		}
		// Fine case

		if (QuantumNegativo_error) {
			lPenRes.setMessage("Errore");
		} else {
			lPenRes.setFlagValidato("N");
			lPenRes.setEveIdEvento(null);
			lPenRes.setFlagErgastolo(lFlagErgastolo);
			lPenRes.setDiesAQuo(lFlagDiesAQuo);
			lPenRes.setDataInserimento(DateUtils.getSysDate());
		}

		siesLogger.debug("Pena residua rideterminata a seguito della Revoca: " + lPenRes);

		lPenRes.setFasSieIdFascicoloSiep(lFascID);
		aCalcoloPenaModel.setPenaResiduaRicalcolata(lPenRes);

		return aCalcoloPenaModel;
	}

	public boolean ExIsCalcoloPenaAbInizio(BigDecimal aIdFascicoloSiep) throws F3BException {

		boolean lIsAbInitio = true;

		Connection lConn = null;

		SospensioneDAO lSosDao = null;
		LicenzaLibanticipataSqlDAO lLicDao = null;
		CumuloDAO lCumDao = null;
		MisuraAlternativaDAO lMisDao = null;
		FascicoloSiepDAO lFasDao = null;

		try {
			lConn = getDBConnection();

			// SOSPENSIONE
			lSosDao = new SospensioneDAO(lConn);
			lSosDao.setCondizioneIdFascicolo(aIdFascicoloSiep);
			SospensioneModel lSosMod = (SospensioneModel) lSosDao.getModelByKey();

			if (lSosMod != null)
				return false;

			// LIBERAZIONE ANTICIPATA
			// In questo caso il calcolo non è ab inizio in quanto i gg di liberazione
			// anticipata vengono caricati sulla data fine pena e non sul quantum
			// per cui si perderebbe l'informazione
			// MERGE v10: cambiata query di estrazione
			lLicDao = new LicenzaLibanticipataSqlDAO(lConn);
			lLicDao.ricercaLicenzaLibanticipataByIdFascicoloSiep(aIdFascicoloSiep);
			// lLicDao = new LicenzaLibanticipataDAO(lConn);
			// lLicDao.setCondizioneIdFascicolo(aIdFascicoloSiep);
			LicenzaLibAnticipataModel lLicMod = (LicenzaLibAnticipataModel) lLicDao.getModelByKey();

			if (lLicMod != null)
				return false;

			// CUMULO
			lCumDao = new CumuloDAO(lConn);
			lCumDao.setCondizioneUpdateByFasc(aIdFascicoloSiep);
			CumuloModel lCumMod = (CumuloModel) lCumDao.getModelByKey();

			if (lCumMod != null)
				return false;

			// MISURA ALTERNATIVA
			lMisDao = new MisuraAlternativaDAO(lConn);
			lMisDao.setCondizioneIdFascicolo(aIdFascicoloSiep);
			MisuraAlternativaModel lMisMod = (MisuraAlternativaModel) lMisDao.getModelByKey();

			if (lMisMod != null)
				return false;

			// MIGRATO DA RES
			lFasDao = new FascicoloSiepDAO(lConn);
			lFasDao.selCondizioneUpdate(aIdFascicoloSiep);
			FascicoloSiepModel lFasMod = (FascicoloSiepModel) lFasDao.getModelByKey();

			if (lFasMod != null && lFasMod.getCodOperatoreInserimento() != null
					&& lFasMod.getCodOperatoreInserimento().startsWith("res-"))
				return false;
		} catch (DAOException daoEx) {
			throw new F3BException("CalcoloPenaController.ExIsCalcoloPenaAbInizio: " + daoEx);
		} catch (Exception e) {
			throw new F3BException("CalcoloPenaController.ExIsCalcoloPenaAbInizio: " + e);
		} finally {
			cleanup(lSosDao);
			cleanup(lLicDao);
			cleanup(lCumDao);
			cleanup(lMisDao);
			cleanup(lFasDao); // sca
			cleanup(lConn);
		}

		return lIsAbInitio;
	}

	/**
	 * Questo metodo effettua il calcolo della pena residua alla data interruzione passata in unput.
	 *
	 * @param aPenaResidua
	 *            - Pena residua di partenza
	 * @param aDataSospensione
	 *            - Data sospensione/interruzione
	 * @return
	 * @throws Exception
	 */
	public PenaResiduaModel exCalcolaPenaResiduaAl(PenaResiduaModel aPenaResidua, Date aDataSospensione)
			throws Exception {

		// Se la data di sospensione è null viene considerato come posizione libero
		if (aPenaResidua.getDataInizio() == null || aDataSospensione == null) {
			throw new F3BException(
					"calcolaPenaResiduaAl: dati in input incongruenti, data inizio pena o data calcolo assenti");
		}

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("Pena in espiazione = "+aPenaResidua);
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("Data al = "+aDataSospensione);
		// PenaResiduaModel lPenaResidua = new PenaResiduaModel(aPenaResidua);

		/*
		 * verificare i due casi limite: - data sospensione < data inizio ==> pena residua al = intera pena -
		 * data sospensione >= data fine ==> pena residua al = 0
		 */
		PenaResiduaModel lPenaResidua = new PenaResiduaModel();

		CalendarUtil lCalUtil = new CalendarUtil();

		// Carico il Calendar RECLUSIONE
		CalendarModel lCalReclusione = new CalendarModel();

		lCalReclusione.setNumAnni(aPenaResidua.getNumAnniReclusione());
		lCalReclusione.setNumMesi(aPenaResidua.getNumMesiReclusione());
		lCalReclusione.setNumGiorni(aPenaResidua.getNumGiorniReclusione());

		if (!lCalUtil.isZero(lCalReclusione)) {
			lCalReclusione.setDataInizio(aPenaResidua.getDataInizio());

			if (aPenaResidua.getDataFineReclusione() != null)
				lCalReclusione.setDataFine(aPenaResidua.getDataFineReclusione());
			else
				lCalReclusione.setDataFine(aPenaResidua.getDataFine());
		}

		// Carico il Calendar ARRESTO
		CalendarModel lCalArresto = new CalendarModel();

		lCalArresto.setNumAnni(aPenaResidua.getNumAnniArresto());
		lCalArresto.setNumMesi(aPenaResidua.getNumMesiArresto());
		lCalArresto.setNumGiorni(aPenaResidua.getNumGiorniArresto());

		if (!lCalUtil.isZero(lCalArresto)) {
			Date lDataInizioArresto = null;

			if (aPenaResidua.getDataInizioArresto() == null)
				lDataInizioArresto = aPenaResidua.getDataInizio();
			else
				lDataInizioArresto = aPenaResidua.getDataInizioArresto();

			if (lDataInizioArresto == null)
				throw new SIEPException(SIEPException.USER_MESSAGE,
						"La pena residua non ha la data inizio arresto. Impossibile procedere.");

			lCalArresto.setDataInizio(lDataInizioArresto);
			lCalArresto.setDataFine(aPenaResidua.getDataFine());
		}

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("lCalReclusione = "+lCalReclusione);
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("lCalArresto = "+lCalArresto);

		// ======================================================================
		// Ricalcolo i nuovi quantum di Reclusione e Arresto in base alla data
		// di sospensione e alle date fine pena previste
		// ======================================================================
		CalendarModel lCalReclusioneNew = new CalendarModel();
		CalendarModel lCalArrestoNew = new CalendarModel();

		if (!lCalUtil.isZero(lCalReclusione) && !lCalUtil.isZero(lCalArresto)) {
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug(" Reclusione e arresto <> 0");

			if (aDataSospensione.before(lCalReclusione.getDataFine())) {
				// Reclusione ancora in espiazione, aggiorno solo il quantum di reclusione
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug("Reclusione ancora in espiazione, aggiorno solo il quantum di
				// reclusione");

				lCalReclusioneNew.setDataInizio(DateUtils.getDayAfter(aDataSospensione));
				lCalReclusioneNew.setDataFine(lCalReclusione.getDataFine());

				lPenaResidua.setDataInizio(lCalReclusioneNew.getDataInizio());

				// lCalReclusioneNew = lCalUtil.CalcolaNumGiorniMesiAnni(lCalReclusioneNew);
				lCalReclusioneNew = lCalUtil.CalcolaNumGiorniMesiAnni(lCalReclusioneNew, false);
				lCalReclusioneNew = lCalUtil.ricalcolaGAM(lCalReclusioneNew);

				lPenaResidua.setNumAnniReclusione(new BigDecimal(lCalReclusioneNew.getNumAnni()));
				lPenaResidua.setNumMesiReclusione(new BigDecimal(lCalReclusioneNew.getNumMesi()));
				lPenaResidua.setNumGiorniReclusione(new BigDecimal(lCalReclusioneNew.getNumGiorni()));

				// ARRESTO RIMANE INVARIATO
				lPenaResidua.setNumAnniArresto(new BigDecimal(lCalArresto.getNumAnni()));
				lPenaResidua.setNumMesiArresto(new BigDecimal(lCalArresto.getNumMesi()));
				lPenaResidua.setNumGiorniArresto(new BigDecimal(lCalArresto.getNumGiorni()));
			} else if (aDataSospensione.after(lCalReclusione.getDataFine())
					|| aDataSospensione.equals(lCalReclusione.getDataFine())) {
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug("Reclusione interamente espiata:");
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug("Data fine reclusione = "+lCalReclusione.getDataFine());
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug("Data al = "+aDataSospensione);
				// RECLUSIONE = 0
				lPenaResidua.setNumAnniReclusione(new BigDecimal(0));
				lPenaResidua.setNumMesiReclusione(new BigDecimal(0));
				lPenaResidua.setNumGiorniReclusione(new BigDecimal(0));

				lCalArrestoNew.setDataInizio(aDataSospensione);
				lCalArrestoNew.setDataFine(lCalArresto.getDataFine());

				lPenaResidua.setDataInizioArresto(lCalArrestoNew.getDataInizio());

				lCalArrestoNew = lCalUtil.CalcolaNumGiorniMesiAnni(lCalArrestoNew, true);
				lCalArrestoNew = lCalUtil.ricalcolaGAM(lCalArrestoNew);

				lPenaResidua.setNumAnniArresto(new BigDecimal(lCalArrestoNew.getNumAnni()));
				lPenaResidua.setNumMesiArresto(new BigDecimal(lCalArrestoNew.getNumMesi()));
				lPenaResidua.setNumGiorniArresto(new BigDecimal(lCalArrestoNew.getNumGiorni()));
			}
		} else if (!lCalUtil.isZero(lCalReclusione) && lCalUtil.isZero(lCalArresto)) { // Presente solo la
																						// Reclusione
																						// // [FT] -
																						// 03/08/2016 -
																						// MAC_LOG - Utilizzo
																						// la variabile di
																						// istanza siesLogger
																						// al posto di
																						// LogF3B.getLogger()
																						// siesLogger.debug("Presente
																						// solo la
																						// Reclusione");
			lCalReclusioneNew.setDataInizio(aDataSospensione);
			lCalReclusioneNew.setDataFine(lCalReclusione.getDataFine());

			lPenaResidua.setDataInizio(lCalReclusioneNew.getDataInizio());

			// lCalReclusioneNew = lCalUtil.CalcolaNumGiorniMesiAnni(lCalReclusioneNew);
			lCalReclusioneNew = lCalUtil.CalcolaNumGiorniMesiAnni(lCalReclusioneNew, true);
			lCalReclusioneNew = lCalUtil.ricalcolaGAM(lCalReclusioneNew);

			lPenaResidua.setNumAnniReclusione(new BigDecimal(lCalReclusioneNew.getNumAnni()));
			lPenaResidua.setNumMesiReclusione(new BigDecimal(lCalReclusioneNew.getNumMesi()));
			lPenaResidua.setNumGiorniReclusione(new BigDecimal(lCalReclusioneNew.getNumGiorni()));

			// ARRESTO = 0
			lPenaResidua.setNumAnniArresto(new BigDecimal(0));
			lPenaResidua.setNumMesiArresto(new BigDecimal(0));
			lPenaResidua.setNumGiorniArresto(new BigDecimal(0));
		} else if (lCalUtil.isZero(lCalReclusione) && !lCalUtil.isZero(lCalArresto)) { // presente solo
																						// Arresto
																						// RECLUSIONE = 0
																						// // [FT] -
																						// 03/08/2016 -
																						// MAC_LOG - Utilizzo
																						// la variabile di
																						// istanza siesLogger
																						// al posto di
																						// LogF3B.getLogger()
																						// siesLogger.debug("Presente
																						// solo arresto");
			lPenaResidua.setNumAnniReclusione(new BigDecimal(0));
			lPenaResidua.setNumMesiReclusione(new BigDecimal(0));
			lPenaResidua.setNumGiorniReclusione(new BigDecimal(0));

			lCalArrestoNew.setDataInizio(aDataSospensione);
			lCalArrestoNew.setDataFine(lCalArresto.getDataFine());

			lPenaResidua.setDataInizioArresto(lCalArrestoNew.getDataInizio());

			lCalArrestoNew = lCalUtil.CalcolaNumGiorniMesiAnni(lCalArrestoNew, true);
			lCalArrestoNew = lCalUtil.ricalcolaGAM(lCalArrestoNew);

			lPenaResidua.setNumAnniArresto(new BigDecimal(lCalArrestoNew.getNumAnni()));
			lPenaResidua.setNumMesiArresto(new BigDecimal(lCalArrestoNew.getNumMesi()));
			lPenaResidua.setNumGiorniArresto(new BigDecimal(lCalArrestoNew.getNumGiorni()));
		}

		return lPenaResidua;
	}

	/**
	 * Questo metodo effettua il calcolo della pena residua alla data interruzione passata in unput.
	 *
	 * @param aPenaResidua
	 *            - Pena residua di partenza
	 * @param aDataSospensione
	 *            - Data sospensione/interruzione
	 * @return
	 * @throws Exception
	 */
	public PenaResiduaModel exCalcolaPenaEspiataAl(PenaResiduaModel aPenaResidua, Date aDataComputo)
			throws Exception {

		// Se la data di sospensione è null viene considerato come posizione libero
		if (aPenaResidua.getDataInizio() == null || aDataComputo == null) {
			throw new F3BException(
					"calcolaPenaEspiataAl: dati in input incongruenti, data inizio pena o data calcolo assenti");
		}

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("Pena in espiazione = "+aPenaResidua);
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("Data Computo = "+aDataComputo);
		// PenaResiduaModel lPenaResidua = new PenaResiduaModel(aPenaResidua);

		/*
		 * verificare i due casi limite: - data sospensione < data inizio ==> pena residua al = intera pena -
		 * data sospensione >= data fine ==> pena residua al = 0
		 */
		PenaResiduaModel lPenaEspiata = new PenaResiduaModel();

		CalendarUtil lCalUtil = new CalendarUtil();

		// Carico il Calendar RECLUSIONE
		CalendarModel lCalReclusione = new CalendarModel();

		lCalReclusione.setNumAnni(aPenaResidua.getNumAnniReclusione());
		lCalReclusione.setNumMesi(aPenaResidua.getNumMesiReclusione());
		lCalReclusione.setNumGiorni(aPenaResidua.getNumGiorniReclusione());

		if (!lCalUtil.isZero(lCalReclusione)) {
			lCalReclusione.setDataInizio(aPenaResidua.getDataInizio());

			if (aPenaResidua.getDataFineReclusione() != null)
				lCalReclusione.setDataFine(aPenaResidua.getDataFineReclusione());
			else
				lCalReclusione.setDataFine(aPenaResidua.getDataFine());
		}

		// Carico il Calendar ARRESTO
		CalendarModel lCalArresto = new CalendarModel();

		lCalArresto.setNumAnni(aPenaResidua.getNumAnniArresto());
		lCalArresto.setNumMesi(aPenaResidua.getNumMesiArresto());
		lCalArresto.setNumGiorni(aPenaResidua.getNumGiorniArresto());

		if (!lCalUtil.isZero(lCalArresto)) {
			Date lDataInizioArresto = null;

			if (aPenaResidua.getDataInizioArresto() == null)
				lDataInizioArresto = aPenaResidua.getDataInizio();
			else
				lDataInizioArresto = aPenaResidua.getDataInizioArresto();

			if (lDataInizioArresto == null)
				throw new SIEPException(SIEPException.USER_MESSAGE,
						"La pena residua non ha la data inizio arresto. Impossibile procedere.");

			lCalArresto.setDataInizio(lDataInizioArresto);
			lCalArresto.setDataFine(aPenaResidua.getDataFine());
		}

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("lCalReclusione = "+lCalReclusione);
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("lCalArresto = "+lCalArresto);

		// ======================================================================
		// Ricalcolo i nuovi quantum di Reclusione e Arresto in base alla data
		// di Computo e alla data inizio pena previsto
		// ======================================================================
		CalendarModel lCalReclusioneNew = new CalendarModel();
		CalendarModel lCalArrestoNew = new CalendarModel();

		if (!lCalUtil.isZero(lCalReclusione) && !lCalUtil.isZero(lCalArresto)) {
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug(" Reclusione e arresto <> 0");

			if (aDataComputo.before(lCalReclusione.getDataFine())) {
				// Reclusione ancora in espiazione, aggiorno solo il quantum di reclusione
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug("Reclusione ancora in espiazione, aggiorno solo il quantum di
				// reclusione");

				lCalReclusioneNew.setDataInizio(aPenaResidua.getDataInizio());
				lCalReclusioneNew.setDataFine(aDataComputo);

				lCalReclusioneNew = lCalUtil.CalcolaNumGiorniMesiAnni(lCalReclusioneNew, false);
				lCalReclusioneNew = lCalUtil.ricalcolaGAM(lCalReclusioneNew);

				lPenaEspiata.setNumAnniReclusione(new BigDecimal(lCalReclusioneNew.getNumAnni()));
				lPenaEspiata.setNumMesiReclusione(new BigDecimal(lCalReclusioneNew.getNumMesi()));
				lPenaEspiata.setNumGiorniReclusione(new BigDecimal(lCalReclusioneNew.getNumGiorni()));

				// ARRESTO RIMANE Tutto da espiare
				lPenaEspiata.setNumAnniArresto(new BigDecimal(0));
				lPenaEspiata.setNumMesiArresto(new BigDecimal(0));
				lPenaEspiata.setNumGiorniArresto(new BigDecimal(0));
			} else if (aDataComputo.after(lCalReclusione.getDataFine())
					|| aDataComputo.equals(lCalReclusione.getDataFine())) {
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug("Reclusione interamente espiata:");
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug("Data fine reclusione = "+lCalReclusione.getDataFine());

				// RECLUSIONE = interamente espiata
				lPenaEspiata.setNumAnniReclusione(aPenaResidua.getNumAnniReclusione());
				lPenaEspiata.setNumMesiReclusione(aPenaResidua.getNumMesiReclusione());
				lPenaEspiata.setNumGiorniReclusione(aPenaResidua.getNumGiorniReclusione());

				lCalArrestoNew.setDataInizio(lCalArresto.getDataInizio());
				lCalArrestoNew.setDataFine(aDataComputo);

				lCalArrestoNew = lCalUtil.CalcolaNumGiorniMesiAnni(lCalArrestoNew, false);
				lCalArrestoNew = lCalUtil.ricalcolaGAM(lCalArrestoNew);

				lPenaEspiata.setNumAnniArresto(new BigDecimal(lCalArrestoNew.getNumAnni()));
				lPenaEspiata.setNumMesiArresto(new BigDecimal(lCalArrestoNew.getNumMesi()));
				lPenaEspiata.setNumGiorniArresto(new BigDecimal(lCalArrestoNew.getNumGiorni()));
			}
		} else if (!lCalUtil.isZero(lCalReclusione) && lCalUtil.isZero(lCalArresto)) { // Presente solo la
																						// Reclusione
																						// // [FT] -
																						// 03/08/2016 -
																						// MAC_LOG - Utilizzo
																						// la variabile di
																						// istanza siesLogger
																						// al posto di
																						// LogF3B.getLogger()
																						// siesLogger.debug("Presente
																						// solo la
																						// Reclusione");
			lCalReclusioneNew.setDataInizio(lCalReclusione.getDataInizio());
			lCalReclusioneNew.setDataFine(aDataComputo);

			lCalReclusioneNew = lCalUtil.CalcolaNumGiorniMesiAnni(lCalReclusioneNew, false);
			lCalReclusioneNew = lCalUtil.ricalcolaGAM(lCalReclusioneNew);

			lPenaEspiata.setNumAnniReclusione(new BigDecimal(lCalReclusioneNew.getNumAnni()));
			lPenaEspiata.setNumMesiReclusione(new BigDecimal(lCalReclusioneNew.getNumMesi()));
			lPenaEspiata.setNumGiorniReclusione(new BigDecimal(lCalReclusioneNew.getNumGiorni()));

			// ARRESTO = 0
			lPenaEspiata.setNumAnniArresto(new BigDecimal(0));
			lPenaEspiata.setNumMesiArresto(new BigDecimal(0));
			lPenaEspiata.setNumGiorniArresto(new BigDecimal(0));
		} else if (lCalUtil.isZero(lCalReclusione) && !lCalUtil.isZero(lCalArresto)) { // presente solo
																						// Arresto
																						// RECLUSIONE = 0
																						// // [FT] -
																						// 03/08/2016 -
																						// MAC_LOG - Utilizzo
																						// la variabile di
																						// istanza siesLogger
																						// al posto di
																						// LogF3B.getLogger()
																						// siesLogger.debug("Presente
																						// solo arresto");
			lPenaEspiata.setNumAnniReclusione(new BigDecimal(0));
			lPenaEspiata.setNumMesiReclusione(new BigDecimal(0));
			lPenaEspiata.setNumGiorniReclusione(new BigDecimal(0));

			lCalArrestoNew.setDataInizio(lCalArresto.getDataInizio());
			lCalArrestoNew.setDataFine(aDataComputo);

			lCalArrestoNew = lCalUtil.CalcolaNumGiorniMesiAnni(lCalArrestoNew, false);
			lCalArrestoNew = lCalUtil.ricalcolaGAM(lCalArrestoNew);

			lPenaEspiata.setNumAnniArresto(new BigDecimal(lCalArrestoNew.getNumAnni()));
			lPenaEspiata.setNumMesiArresto(new BigDecimal(lCalArrestoNew.getNumMesi()));
			lPenaEspiata.setNumGiorniArresto(new BigDecimal(lCalArrestoNew.getNumGiorni()));
		}

		return lPenaEspiata;
	}

	/**
	 * Effettua l'inserimento delle comunicazione nuovo residuo pena nel caso di Ridetermiazione Pena - ALtro
	 * Duplica la pena rideterminata sul provvedimento di computo.
	 *
	 * @param aEveNotModel
	 * @return Model Inserito
	 * @since 4.0
	 * @throws F3BException
	 */
	public EventoModel ExInserisciCOMRidetPenaAltro(EventoNotificaModel aEveNotModel) throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		EventoSqlDAO lEveSqlDAO = null;
		NotificaDAO lNotDao = null;
		AutoritaEsternaDAO lAutDao = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;
		PenaResiduaDAO lPenResDAO = null;

		EventoModel lEveComunicazione = aEveNotModel.getEvento();

		try {
			lConn = getDBTransaction();

			// ========================================================================
			// Determino il protocollo e inserisco l'evento
			// ========================================================================
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Inserisco Comunicazione...");

			lEveSqlDAO = new EventoSqlDAO(lConn);
			BigDecimal lProgProtocollo = lEveSqlDAO.getProgressivo(lEveComunicazione);
			lEveComunicazione.setProgrProtocollo(new BigDecimal(lProgProtocollo.intValue() + 1));

			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModel(lEveComunicazione);
			BigDecimal lIdEventoCom = lEveDao.insert();
			lEveComunicazione.setIdEvento(lIdEventoCom);

			// ========================================================================
			// Inserisco le Notifiche
			// ========================================================================
			BigDecimal lKeyAutorita = null;

			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Inizio inserimento notifiche ");

			lNotDao = new NotificaDAO(lConn);
			lAutDao = new AutoritaEsternaDAO(lConn);
			int count = 0;
			while (count < aEveNotModel.getNotifiche().length) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Notifica[" + count + "] = " + aEveNotModel.getNotifiche()[count]);

				if (aEveNotModel.getNotifiche()[count] != null) {
					if (aEveNotModel.getNotifiche()[count].getAutoritaEsterna() != null) {
						lAutDao.setRicercaByAutSede(aEveNotModel.getNotifiche()[count].getAutoritaEsterna());
						AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
						lAutMod = (AutoritaEsternaModel) lAutDao.getModelByKey();

						if (lAutMod == null) { // Autorità non presente, la inserisco
							lAutDao.setDAOFromModel(aEveNotModel.getNotifiche()[count].getAutoritaEsterna());
							lKeyAutorita = lAutDao.insert();
							aEveNotModel.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						} else { // Autorità già presente aggiorno solo l'id
							lKeyAutorita = lAutMod.getIdAutoritaEsterna();
							aEveNotModel.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						}
					}

					// Inserisco il record Notifica
					aEveNotModel.getNotifiche()[count].setEveIdEvento(lIdEventoCom);

					lNotDao.setDAOFromModel(aEveNotModel.getNotifiche()[count]);
					lNotDao.insert();
					lNotDao.stop();
				}
				count++;
			}

			// ========================================================================
			// Recupero la pena residua da eseguire dal Provvedimento di Computo che
			// ha rideterminato la pena e la duplico agganciandola alla Comunicazione
			// ========================================================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Recupero la pena residua associata al computo");
			BigDecimal lIdEveComputo = lEveComunicazione.getEveIdEvento();

			lPenResSqlDao = new PenaResiduaSqlDAO(lConn);

			lPenResSqlDao.ricercaPenaResiduaByKeyEvento(lIdEveComputo);
			PenaResiduaModel lPenaResidua = (PenaResiduaModel) lPenResSqlDao.getModelByKey();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Pena Da duplicare: " + lPenaResidua);

			// Ripulisco i campi non necessari
			lPenaResidua.setIdPenaResidua(null);
			lPenaResidua.setEveIdEvento(lIdEventoCom);
			lPenaResidua.setFlagValidato("N"); // quella che sto duplicando potrebbe essere validata

			lPenaResidua.setCodOperatoreInserimento(lEveComunicazione.getCodOperatoreInserimento());
			lPenaResidua.setCodUfficioInserimento(lEveComunicazione.getCodUfficioInserimento());
			lPenaResidua.setDataInserimento(lEveComunicazione.getDataInserimento());

			lPenaResidua.setCodOperatoreAggiornamento(null);
			lPenaResidua.setCodUfficioAggiornamento(null);
			lPenaResidua.setDataAggiornamento(null);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Pena da inserire: " + lPenaResidua);

			// Inserisco la pena
			lPenResDAO = new PenaResiduaDAO(lConn);
			lPenResDAO.setDAOFromModel(lPenaResidua);
			BigDecimal lIdPenaResNew = lPenResDAO.insert();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Id Pena Res Inserita: " + lIdPenaResNew);

			// rollback(lConn);
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException", daoEx);
			rollback(lConn);
			throw new F3BException("CalcoloPenaController.ExInserisciCOMRidetPenaAltro: " + daoEx);
			// } catch (SQLException sqe) {
			// rollback(lConn);
			// throw new F3BException("CalcoloPenaController.ExInserisciCOMRidetPenaAltro: " + sqe);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception", ex);

			ex.printStackTrace();
			rollback(lConn);
			throw new F3BException("CalcoloPenaController.ExInserisciCOMRidetPenaAltro: " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lEveSqlDAO);
			cleanup(lNotDao);
			cleanup(lAutDao);
			cleanup(lPenResSqlDao);
			cleanup(lPenResDAO);

			cleanup(lConn);
		}

		return lEveComunicazione;

	}

	/**
	 * Metodo che effettua la validazione della Comunicazione nuovo residuo pena a seguito di rideterminazione
	 * pena altro ed eventuale validazione del computo
	 *
	 * @param aEvento
	 * @param aFascicolo
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExUpdateValidaComNuovoResPenaRidetPenaAltro(EventoModel aEvento,
			FascicoloSiepModel aFascicolo) throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		PenaResiduaDAO lPenResDao = null;

		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		StatoProcedimentoDAO lStatoDao = null;

		ScadenzarioSqlDAO lScadSqlDao = null;
		ScadenzarioDAO lScaDao = null;

		NotificaEventoSqlDAO lNotEveSqlDao = null;

		EventoDAO lEveDaoBlob = null;

		EventoModel lEveMod = null;

		PenaResiduaSqlDAO lPenResSqlDao = null;
		StatoProcedimentoSqlDAO lStatoSqlDao = null;

		try {
			lConn = getDBTransaction();

			// ===================================================
			// Recupera l'evento da validare (Comunicazione)
			// ===================================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Recupero l'evento da validare (Comunicazione)");

			lEveDao = new EventoDAO(lConn);
			lEveDao.setIdEvento(aEvento.getIdEvento());
			lEveDao.selByKey();
			lEveMod = (EventoModel) lEveDao.getModelByKey();
			lEveDao.stop();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lEveMod = " + lEveMod);

			// ========================================================================
			// Recupero il provvedimneto di computo e verifico se devo validarlo
			// contestualmente alla Comunicazione.
			// n.b. potrebbe essere già stato validato
			// ========================================================================
			if (lEveMod.getEveIdEvento() != null) {
				EventoModel lEveComputo = null;
				lEveDao = new EventoDAO(lConn);
				lEveDao.setIdEvento(lEveMod.getEveIdEvento());
				lEveDao.selByKey();
				lEveComputo = (EventoModel) lEveDao.getModelByKey();
				lEveDao.stop();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Evento di Computo = " + lEveComputo);

				if (lEveComputo.getFlagDocumentoRegistrato() == null
						|| (lEveComputo.getFlagDocumentoRegistrato() != null
								&& lEveComputo.getFlagDocumentoRegistrato().equals("N"))) { // Procedo alla
																							// validazione del
																							// Provvedimento
																							// di Computo
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Procedo alla validazione del Provvedimento di Computo");
					lEveComputo.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lEveComputo.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
					lEveComputo.setDataAggiornamento(aEvento.getDataAggiornamento());
					lEveComputo.setFlagDocumentoRegistrato(aEvento.getFlagDocumentoRegistrato());

					IAnnotazioneManuale lCtrl = SIEPLookupRemote.getAnnotazioneManualeRemote();
					lCtrl.ExValidaRideterminazionePenaAltro(lEveComputo, aFascicolo, lConn);
				} else {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Evento di Computo già validato");
				}
			}

			// ========================================================================
			// Valido la Pena Residua.
			// La pena da eseguire è quella associata al provvedimento di computo che
			// ha rideterminato la pena duplicata in fase di inserimento dell'OE
			// ========================================================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Valido Pena Residua su Comunicazione");
			lPenResDao = new PenaResiduaDAO(lConn);

			lPenResDao.setFlagValidato("S");

			lPenResDao.setDataAggiornamento(aEvento.getDataAggiornamento());
			lPenResDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
			lPenResDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());

			lPenResDao.setCondizioneByIdEvento(aEvento.getIdEvento());

			lPenResDao.update();
			lPenResDao.stop();

			// ======================================================================
			// Aggiorno Stato Procedimento
			// Per ora commentato
			// ======================================================================

			// ========================================================================
			// MEV 29 - 06/2015
			// Aggiorno lo stato procedimento se il provvedimento corrente ha rideterminato
			// il fine pena e se il procedente stato procedimento prevedeva il codice
			// 0010 = Pena in Esecuzione Fino al. In questo caso infatti se non viene
			// aggiornato lo stato proc, la data fine pena e 'Pena in Esecuzione Fino al'
			// non coincidono sul dettaglio procedimento
			// ========================================================================
			// Recupero la Pena residua, mi serve il fine pena per lo stato procedimento
			PenaResiduaModel lPenaResModel = null;
			lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
			lPenResSqlDao.ricercaPenaResiduaByKeyEvento(aEvento.getIdEvento());
			lPenResSqlDao.start();
			if (lPenResSqlDao.next())
				lPenaResModel = (PenaResiduaModel) lPenResSqlDao.getModel();
			lPenResSqlDao.stop();
			// Se la pena legata all'evento è in decorrenza (fine pena <> null)
			// e lo stato procedimento attuale prevede la dicitura
			// "0010" = Pena in Esecuzione Fino al
			// Allora devo aggiornare lo stato procedimento per tenere allinea
			// Ergastolo???????
			if (lPenaResModel != null && lPenaResModel.getDataFine() != null
					&& !lPenaResModel.isErgastolo()) {
				// Recupero lo stato procedimento attuale per verificare se prevede
				// l'informazione: Pena in Esecuzione Fino al
				lStatoSqlDao = new StatoProcedimentoSqlDAO(lConn);
				lStatoSqlDao.ricercaStatoProcedimentoByFascicoloSiep(aFascicolo.getIdFascicoloSiep());
				Vector lElencoStatoProc = new Vector(lStatoSqlDao.getModels());

				for (int i = 0; i < lElencoStatoProc.size(); i++) {
					StatoProcedimentoModel lStatoModel = (StatoProcedimentoModel) lElencoStatoProc
							.elementAt(i);
					if ("0010".equals(lStatoModel.getCodStatoProcedimento())
							&& !DateUtils.isEquals(lStatoModel.getData(), lPenaResModel.getDataFine())) {
						// Cancella eventuali record prima di inserire un nuovo STATO_PROCEDIMENTO
						lStatoDao = new StatoProcedimentoDAO(lConn);
						lStatoDao.setCondizioneByIdFascicolo(aFascicolo.getIdFascicoloSiep());
						lStatoDao.delete();

						// Inserisce lo stato procedimento
						StatoProcedimentoModel lStatoProcMod = new StatoProcedimentoModel();

						lStatoProcMod.setProgressivo(new BigDecimal(1));
						lStatoProcMod.setCodStatoProcedimento("0452"); // Emessa comunicazione per
																		// Rideterminazione Pena il
						lStatoProcMod.setData(lEveMod.getDataEmissione());
						lStatoProcMod.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
						// lStatoProcMod.setEveIdEvento(aValore);

						lStatoProcMod.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
						lStatoProcMod.setDataInserimento(aEvento.getDataAggiornamento());
						lStatoProcMod.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());

						lStatoDao.setDAOFromModel(lStatoProcMod);
						lStatoDao.insert();
						lStatoDao.stop();

						// Pena in Esecuzione Fino al
						lStatoProcMod.setProgressivo(new BigDecimal(2));
						lStatoProcMod.setCodStatoProcedimento("0010"); // Pena in Esecuzione Fino al
						lStatoProcMod.setData(lPenaResModel.getDataFine());

						lStatoDao.setDAOFromModel(lStatoProcMod);
						lStatoDao.insert();
						lStatoDao.stop();

					}
				}
			}

			// ========================================================================

			// ====================
			// Update del Blob
			// ====================
			// lConnBlob = getDBConnection();

			lEveDaoBlob = new EventoDAO(lConn);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);

			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();

			// commit(lConnBlob);
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: ", daoEx);
			daoEx.printStackTrace();

			rollback(lConn);
			throw new F3BException(
					"CalcoloPenaController.ExUpdateValidaComNuovoResPenaRidetPenaAltro : " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: ", ex);
			ex.printStackTrace();

			rollback(lConn);
			throw new F3BException(
					"CalcoloPenaController.ExUpdateValidaComNuovoResPenaRidetPenaAltro : " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lPenResDao);
			cleanup(lPosSqlDao);
			cleanup(lStatoDao);

			cleanup(lScadSqlDao);
			cleanup(lScaDao);

			cleanup(lNotEveSqlDao);

			cleanup(lPenResSqlDao);
			cleanup(lStatoSqlDao);

			cleanup(lConn);

			cleanup(lEveDaoBlob);
			// cleanup(lConnBlob);
		}

		return lEveMod;
	}

	/**
	 * Effettua l'inserimento dell'Ordine di Scarcerazione nuovo residuo pena nel caso di Ridetermiazione Pena
	 * - ALtro Duplica la pena rideterminata sul provvedimento di computo.
	 *
	 * @param aEveNotModel
	 * @return Model Inserito
	 * @since 4.0
	 * @throws F3BException
	 */
	public EventoModel ExInserisciOSRidetPenaAltro(EventoNotificaModel aEveNotModel) throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		EventoSqlDAO lEveSqlDAO = null;
		NotificaDAO lNotDao = null;
		AutoritaEsternaDAO lAutDao = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;
		PenaResiduaDAO lPenResDAO = null;
		CampoNotaDAO lCampoNotaDao = null;

		EventoModel lEventoModel = aEveNotModel.getEvento();

		try {
			lConn = getDBTransaction();

			// ========================================================================
			// Determino il protocollo e inserisco l'evento
			// ========================================================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Inserisco l'evento...");

			lEveSqlDAO = new EventoSqlDAO(lConn);
			BigDecimal lProgProtocollo = lEveSqlDAO.getProgressivo(lEventoModel);
			lEventoModel.setProgrProtocollo(new BigDecimal(lProgProtocollo.intValue() + 1));

			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModel(lEventoModel);
			BigDecimal lIdEvento = lEveDao.insert();
			lEventoModel.setIdEvento(lIdEvento);

			// ========================================================================
			// Inserisco le Notifiche
			// ========================================================================
			BigDecimal lKeyAutorita = null;

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Inizio inserimento notifiche ");

			lNotDao = new NotificaDAO(lConn);
			lAutDao = new AutoritaEsternaDAO(lConn);
			int count = 0;
			while (count < aEveNotModel.getNotifiche().length) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Notifica[" + count + "] = " + aEveNotModel.getNotifiche()[count]);

				if (aEveNotModel.getNotifiche()[count] != null) {
					if (aEveNotModel.getNotifiche()[count].getAutoritaEsterna() != null) {
						lAutDao.setRicercaByAutSede(aEveNotModel.getNotifiche()[count].getAutoritaEsterna());
						AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
						lAutMod = (AutoritaEsternaModel) lAutDao.getModelByKey();

						if (lAutMod == null) { // Autorità non presente, la inserisco
							lAutDao.setDAOFromModel(aEveNotModel.getNotifiche()[count].getAutoritaEsterna());
							lKeyAutorita = lAutDao.insert();
							aEveNotModel.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						} else { // Autorità già presente aggiorno solo l'id
							lKeyAutorita = lAutMod.getIdAutoritaEsterna();
							aEveNotModel.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						}
					}

					// Inserisco il record Notifica
					aEveNotModel.getNotifiche()[count].setEveIdEvento(lIdEvento);

					lNotDao.setDAOFromModel(aEveNotModel.getNotifiche()[count]);
					lNotDao.insert();
					lNotDao.stop();
				}
				count++;
			}

			// Paolo Cherubini 26/05/2011
			// Inserimento delle eventuali note aggiuntive.
			lCampoNotaDao = new CampoNotaDAO(lConn);
			if (aEveNotModel.getCampoNote() != null) {
				count = 0;
				while (count < aEveNotModel.getCampoNote().length) {
					aEveNotModel.getCampoNote()[count].setEveIdEvento(lIdEvento);
					aEveNotModel.getCampoNote()[count].setProgressivo(new BigDecimal((double) count + 1));
					lCampoNotaDao.setDAOFromModel(aEveNotModel.getCampoNote()[count]);
					lCampoNotaDao.insert();
					lCampoNotaDao.stop();
					count++;
				}
			}

			// ========================================================================
			// Recupero la pena residua da eseguire dal Provvedimento di Computo che
			// ha rideterminato la pena e la duplico agganciandola alla Comunicazione
			// ========================================================================
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Recupero la pena residua associata al computo");
			BigDecimal lIdEveComputo = lEventoModel.getEveIdEvento();

			lPenResSqlDao = new PenaResiduaSqlDAO(lConn);

			lPenResSqlDao.ricercaPenaResiduaByKeyEvento(lIdEveComputo);
			PenaResiduaModel lPenaResidua = (PenaResiduaModel) lPenResSqlDao.getModelByKey();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Pena Da duplicare: " + lPenaResidua);

			// Ripulisco i campi non necessari
			lPenaResidua.setIdPenaResidua(null);
			lPenaResidua.setEveIdEvento(lIdEvento);
			lPenaResidua.setFlagValidato("N"); // quella che sto duplicando potrebbe essere validata

			lPenaResidua.setCodOperatoreInserimento(lEventoModel.getCodOperatoreInserimento());
			lPenaResidua.setCodUfficioInserimento(lEventoModel.getCodUfficioInserimento());
			lPenaResidua.setDataInserimento(lEventoModel.getDataInserimento());

			lPenaResidua.setCodOperatoreAggiornamento(null);
			lPenaResidua.setCodUfficioAggiornamento(null);
			lPenaResidua.setDataAggiornamento(null);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Pena da inserire: " + lPenaResidua);

			// Inserisco la pena
			lPenResDAO = new PenaResiduaDAO(lConn);
			lPenResDAO.setDAOFromModel(lPenaResidua);
			BigDecimal lIdPenaResNew = lPenResDAO.insert();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Id Pena Res Inserita: " + lIdPenaResNew);

			// rollback(lConn);
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException", daoEx);
			rollback(lConn);
			throw new F3BException("CalcoloPenaController.ExInserisciOSRidetPenaAltro: " + daoEx);
			// } catch (SQLException sqe) {
			// rollback(lConn);
			// throw new F3BException("CalcoloPenaController.ExInserisciOSRidetPenaAltro: " + sqe);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception", ex);

			ex.printStackTrace();
			rollback(lConn);
			throw new F3BException("CalcoloPenaController.ExInserisciOSRidetPenaAltro: " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lEveSqlDAO);
			cleanup(lNotDao);
			cleanup(lAutDao);
			cleanup(lPenResSqlDao);
			cleanup(lPenResDAO);
			cleanup(lCampoNotaDao);

			cleanup(lConn);
		}

		return lEventoModel;
	}

	/**
	 * Metodo che effettua la validazione della Rideterminazione Pena nuovo residuo pena a seguito di
	 * rideterminazione pena altro ed eventuale validazione del computo.
	 *
	 * Attenzione il metodo viene invocato dalla ActUploadOSRidetPenaAltro che gestisce oltre alla
	 * rideteminazione pena altro anche: - Ridimensionamento LA (01-09-0998) - Revoca LA (01-09-1009) -
	 * Scomputo permesso (0958, 0996) - Reclamo su Scomputo Permesso (0994,0997)
	 *
	 * @param aEvento
	 * @param aFascicolo
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExUpdateValidaOSNuovoResPenaRidetPenaAltro(EventoModel aEvento,
			FascicoloSiepModel aFascicolo) throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		PenaResiduaDAO lPenResDao = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;

		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		StatoProcedimentoDAO lStatoDao = null;
		StatoProcedimentoSqlDAO lStatoSqlDao = null;

		ScadenzarioSqlDAO lScadSqlDao = null;
		ScadenzarioDAO lScaDao = null;

		NotificaEventoSqlDAO lNotEveSqlDao = null;

		// Connection lConnBlob = null;
		EventoDAO lEveDaoBlob = null;

		EventoModel lEveMod = null;

		try {
			lConn = getDBTransaction();

			// ===================================================
			// Recupera l'evento da validare (Comunicazione)
			// ===================================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Recupero l'evento da validare (Ridet Pena)");

			lEveDao = new EventoDAO(lConn);
			lEveDao.setIdEvento(aEvento.getIdEvento());
			lEveDao.selByKey();
			lEveMod = (EventoModel) lEveDao.getModelByKey();
			lEveDao.stop();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lEveMod = " + lEveMod);

			// ========================================================================
			// Recupero il provvedimneto di computo e verifico se devo validarlo
			// contestualmente alla Rideterminazione Pena.
			// n.b. potrebbe essere già stato validato
			// ========================================================================
			if (lEveMod.getEveIdEvento() != null) {
				EventoModel lEveComputo = null;
				lEveDao = new EventoDAO(lConn);
				lEveDao.setIdEvento(lEveMod.getEveIdEvento());
				lEveDao.selByKey();
				lEveComputo = (EventoModel) lEveDao.getModelByKey();
				lEveDao.stop();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Evento di Computo = " + lEveComputo);

				if (lEveComputo.getFlagDocumentoRegistrato() == null
						|| (lEveComputo.getFlagDocumentoRegistrato() != null
								&& lEveComputo.getFlagDocumentoRegistrato().equals("N"))) { // Procedo alla
																							// validazione del
																							// Provvedimento
																							// di Computo
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Procedo alla validazione del Provvedimento di Computo");
					lEveComputo.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lEveComputo.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
					lEveComputo.setDataAggiornamento(aEvento.getDataAggiornamento());
					lEveComputo.setFlagDocumentoRegistrato(aEvento.getFlagDocumentoRegistrato());

					IAnnotazioneManuale lCtrl = SIEPLookupRemote.getAnnotazioneManualeRemote();
					lCtrl.ExValidaRideterminazionePenaAltro(lEveComputo, aFascicolo, lConn);
				} else {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Evento di Computo già validato");
				}
			}

			// ========================================================================
			// Valido la Pena Residua.
			// La pena da eseguire è quella associata al provvedimento di computo che
			// ha rideterminato la pena duplicata in fase di inserimento dell'OE
			// ========================================================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Valido Pena Residua su Comunicazione");
			lPenResDao = new PenaResiduaDAO(lConn);

			lPenResDao.setFlagValidato("S");

			lPenResDao.setDataAggiornamento(aEvento.getDataAggiornamento());
			lPenResDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
			lPenResDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());

			lPenResDao.setCondizioneByIdEvento(aEvento.getIdEvento());

			lPenResDao.update();
			lPenResDao.stop();

			// Recupero la Pena residua, mi serve il fine pena per lo stato procedimento
			PenaResiduaModel lPenaResModel = null;
			lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
			lPenResSqlDao.ricercaPenaResiduaByKeyEvento(aEvento.getIdEvento());
			lPenResSqlDao.start();
			if (lPenResSqlDao.next())
				lPenaResModel = (PenaResiduaModel) lPenResSqlDao.getModel();
			lPenResSqlDao.stop();

			// ======================================================================
			// Aggiorno Stato Procedimento
			// Per ora commentato
			// ======================================================================
			// Cerca POSIZIONE_GIURIDICA corrente

			// lPosSqlDao = new PosizioneGiuridicaSqlDAO(lConn);
			// lPosSqlDao.ricercaPosGiuCorrenteByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			//
			// PosizioneGiuridicaModel lPosMod = (PosizioneGiuridicaModel) lPosSqlDao.getModelByKey();
			// String lCodPosizione = lPosMod.getCodPosizioneGiuridica();

			if (lEveMod.getCodMotivo().equals("0998") || lEveMod.getCodMotivo().equals("1009")) {
				String lCodStatoProcedimento1 = null;
				if (lEveMod.getCodMotivo().equals("0998")) { // Ridimensionamento LA
					//
					lCodStatoProcedimento1 = "0260"; // Emesso Ordine di Scarcerazione per Ridimensionamento
														// Liberazione Anticipata il
				} else if (lEveMod.getCodMotivo().equals("1009")) { // Revoca LA
					lCodStatoProcedimento1 = "0261"; // Emesso Ordine di Scarcerazione per Revoca Liberazione
														// Anticipata il
				}

				// Cancella eventuali record prima di inserire un nuovo STATO_PROCEDIMENTO
				lStatoDao = new StatoProcedimentoDAO(lConn);
				lStatoDao.setCondizioneByIdFascicolo(aFascicolo.getIdFascicoloSiep());
				lStatoDao.delete();

				// Inserisce lo stato procedimento
				StatoProcedimentoModel lStatoProcMod = new StatoProcedimentoModel();

				lStatoProcMod.setProgressivo(new BigDecimal(1));
				lStatoProcMod.setCodStatoProcedimento(lCodStatoProcedimento1);
				lStatoProcMod.setData(lEveMod.getDataEmissione());
				lStatoProcMod.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
				// lStatoProcMod.setEveIdEvento(aValore);

				lStatoProcMod.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
				lStatoProcMod.setDataInserimento(aEvento.getDataAggiornamento());
				lStatoProcMod.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());

				lStatoDao.setDAOFromModel(lStatoProcMod);
				lStatoDao.insert();
				lStatoDao.stop();

				// Pena in Esecuzione Fino al
				if (lPenaResModel != null && lPenaResModel.getDataFine() != null) {
					lStatoProcMod.setProgressivo(new BigDecimal(2));
					lStatoProcMod.setCodStatoProcedimento("0010"); // Pena in Esecuzione Fino al
					lStatoProcMod.setData(lPenaResModel.getDataFine());

					lStatoDao.setDAOFromModel(lStatoProcMod);
					lStatoDao.insert();
					lStatoDao.stop();
				}
			} else {
				// Rideterminazione pena altro MEV 29 - 06/2015 aggiunta variazione
				// Stato procedimento se fine pena rideterminato

				String lCodStatoProc = null;
				if (lEveMod.getCodMotivo().equals("0958") || lEveMod.getCodMotivo().equals("0996")) {
					// Scomputo permesso (0958, 0996)
					lCodStatoProc = "0456"; // Emesso Ordine di scarcerazione per Scomputo Permesso il
				} else if (lEveMod.getCodMotivo().equals("0994") || lEveMod.getCodMotivo().equals("0997")) {
					// Reclamo su Scomputo Permesso (0994, 0997)
					lCodStatoProc = "0457"; // Emesso Ordine di scarcerazione per Reclamo Scomputo Permesso il
				} else {
					lCodStatoProc = "0451"; // Emesso Ordine di scarcerazione per Rideterminazione Pena il
				}

				// Se la pena legata all'evento è in decorrenza (fine pena <> null)
				// e lo stato procedimento attuale prevede la dicitura
				// "0010" = Pena in Esecuzione Fino al
				// Allora devo aggiornare lo stato procedimento per tenere allinea
				// Ergastolo???????
				if (lPenaResModel != null && lPenaResModel.getDataFine() != null
						&& !lPenaResModel.isErgastolo()) {
					// Recupero lo stato procedimento attuale per verificare se prevede
					// l'informazione: Pena in Esecuzione Fino al
					lStatoSqlDao = new StatoProcedimentoSqlDAO(lConn);
					lStatoSqlDao.ricercaStatoProcedimentoByFascicoloSiep(aFascicolo.getIdFascicoloSiep());
					Vector lElencoStatoProc = new Vector(lStatoSqlDao.getModels());

					for (int i = 0; i < lElencoStatoProc.size(); i++) {
						StatoProcedimentoModel lStatoModel = (StatoProcedimentoModel) lElencoStatoProc
								.elementAt(i);
						if ("0010".equals(lStatoModel.getCodStatoProcedimento())
								&& !DateUtils.isEquals(lStatoModel.getData(), lPenaResModel.getDataFine())) {
							// Cancella eventuali record prima di inserire un nuovo STATO_PROCEDIMENTO
							lStatoDao = new StatoProcedimentoDAO(lConn);
							lStatoDao.setCondizioneByIdFascicolo(aFascicolo.getIdFascicoloSiep());
							lStatoDao.delete();

							// Inserisce lo stato procedimento
							StatoProcedimentoModel lStatoProcMod = new StatoProcedimentoModel();

							lStatoProcMod.setProgressivo(new BigDecimal(1));
							lStatoProcMod.setCodStatoProcedimento(lCodStatoProc);
							lStatoProcMod.setData(lEveMod.getDataEmissione());

							lStatoProcMod.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());

							lStatoProcMod.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
							lStatoProcMod.setDataInserimento(aEvento.getDataAggiornamento());
							lStatoProcMod.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());

							lStatoDao.setDAOFromModel(lStatoProcMod);
							lStatoDao.insert();
							lStatoDao.stop();

							// Pena in Esecuzione Fino al
							lStatoProcMod.setProgressivo(new BigDecimal(2));
							lStatoProcMod.setCodStatoProcedimento("0010"); // Pena in Esecuzione Fino al
							lStatoProcMod.setData(lPenaResModel.getDataFine());

							lStatoDao.setDAOFromModel(lStatoProcMod);
							lStatoDao.insert();
							lStatoDao.stop();

						}
					}
				}
			}

			// ========================================================================

			// ====================
			// Update del Blob
			// ====================
			// lConnBlob = getDBConnection();

			lEveDaoBlob = new EventoDAO(lConn);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);

			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();

			// commit(lConnBlob);
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: ", daoEx);
			rollback(lConn);
			daoEx.printStackTrace();

			throw new F3BException(
					"CalcoloPenaController.ExUpdateValidaOSNuovoResPenaRidetPenaAltro : " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: ", ex);
			rollback(lConn);
			ex.printStackTrace();
			throw new F3BException(
					"CalcoloPenaController.ExUpdateValidaOSNuovoResPenaRidetPenaAltro : " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lPenResDao);
			cleanup(lPenResSqlDao);
			cleanup(lPosSqlDao);
			cleanup(lStatoDao);

			cleanup(lScadSqlDao);
			cleanup(lScaDao);

			cleanup(lNotEveSqlDao);
			cleanup(lStatoSqlDao);

			cleanup(lConn);

			cleanup(lEveDaoBlob);
			// cleanup(lConnBlob);
		}

		return lEveMod;
	}

	/*****************************************************************************
	 * Effettua la ricerca delle Misure Cautelari computabili ai fini della Misura di Sicurezza Applicata in
	 * via Provvisoria per il fascicolo corrente. Somma i valori e restituisce il totale in un CalendarModel
	 *
	 * @param lFascID
	 *            - Id del fascicolo
	 * @return CalendarModel - somma delle Misure Cautelari
	 * @throws F3BException
	 ****************************************************************************/
	public CalendarModel exGetMisureCautelariComputabiliMisSicApplicata(BigDecimal lFascID)
			throws F3BException {

		Connection lConn = null;
		MisuraCautelareSqlDAO lMCDao = null;

		CalendarModel lCalMod = new CalendarModel();
		CalendarModel lMCTot = new CalendarModel();
		CalendarUtil lCalCon = new CalendarUtil();

		MisuraCautelareModel lMisCau = new MisuraCautelareModel();

		lMisCau.setFasSieIdFascicoloSiep(lFascID);
		lMisCau.setFlagComputabile("S");
		lMisCau.setCodTipoMisura("CD"); // Custodia Cautelare in Misura di Sicurezza Applicata in via
										// Provvisoria

		try {
			lConn = getDBConnection();
			lMCDao = new MisuraCautelareSqlDAO(lConn);
			lMCDao.ricercaMisuraCautelare(lMisCau);
			lMCDao.start();

			while (lMCDao.next()) {
				lMisCau = (MisuraCautelareModel) lMCDao.getModel();

				if (lMisCau.getDataInizio() != null && lMisCau.getDataFine() != null) {
					lCalMod.setDataInizio(lMisCau.getDataInizio());
					lCalMod.setDataFine(lMisCau.getDataFine());

					lCalMod = lCalCon.CalcolaNumGiorniMesiAnni(lCalMod);

					lMCTot = lCalCon.sommaGiorni(lMCTot, lCalMod);
				}

				if (lMisCau.getDataInizio() == null && lMisCau.getDataFine() == null) {
					lCalMod.setNumAnni(lMisCau.getNumAnni());
					lCalMod.setNumMesi(lMisCau.getNumMesi());
					lCalMod.setNumGiorni(lMisCau.getNumGiorni());

					lMCTot = lCalCon.sommaGiorni(lMCTot, lCalMod);
				}
			}

			lMCTot = lCalCon.ricalcolaGAM(lMCTot);
			lMCDao.stop();

			return lMCTot;
		} catch (DAOException daoEx) {
			throw new F3BException(
					"CalcoloPenaController.exGetMisureCautelariComputabiliMisSicApplicata: Non posso leggere : "
							+ daoEx);
		} catch (Exception ex) {
			throw new F3BException(
					"CalcoloPenaController.exGetMisureCautelariComputabiliMisSicApplicata: Non posso leggere  : "
							+ ex);
		} finally {
			cleanup(lMCDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Effettua la ricerca delle Misure Cautelari computabili ai fini degli Arresti Domiciliari per il
	 * fascicolo corrente. Somma i valori e restituisce il totale in un CalendarModel
	 *
	 * @param lFascID
	 *            - Id del fascicolo
	 * @return CalendarModel - somma delle Misure Cautelari
	 * @throws F3BException
	 ****************************************************************************/
	public CalendarModel exGetMisureCautelariComputabiliArrestiDomiciliari(BigDecimal lFascID)
			throws F3BException {

		Connection lConn = null;
		MisuraCautelareSqlDAO lMCDao = null;

		CalendarModel lCalMod = new CalendarModel();
		CalendarModel lMCTot = new CalendarModel();
		CalendarUtil lCalCon = new CalendarUtil();

		MisuraCautelareModel lMisCau = new MisuraCautelareModel();

		lMisCau.setFasSieIdFascicoloSiep(lFascID);
		lMisCau.setFlagComputabile("S");
		lMisCau.setCodTipoMisura("CM"); // Custodia Cautelare in Regime di Arresti Domiciliari ex art 89 dpr
										// 309/90

		try {
			lConn = getDBConnection();
			lMCDao = new MisuraCautelareSqlDAO(lConn);
			lMCDao.ricercaMisuraCautelare(lMisCau);
			lMCDao.start();

			while (lMCDao.next()) {
				lMisCau = (MisuraCautelareModel) lMCDao.getModel();

				if (lMisCau.getDataInizio() != null && lMisCau.getDataFine() != null) {
					lCalMod.setDataInizio(lMisCau.getDataInizio());
					lCalMod.setDataFine(lMisCau.getDataFine());

					lCalMod = lCalCon.CalcolaNumGiorniMesiAnni(lCalMod);

					lMCTot = lCalCon.sommaGiorni(lMCTot, lCalMod);
				}

				if (lMisCau.getDataInizio() == null && lMisCau.getDataFine() == null) {
					lCalMod.setNumAnni(lMisCau.getNumAnni());
					lCalMod.setNumMesi(lMisCau.getNumMesi());
					lCalMod.setNumGiorni(lMisCau.getNumGiorni());

					lMCTot = lCalCon.sommaGiorni(lMCTot, lCalMod);
				}
			}

			lMCTot = lCalCon.ricalcolaGAM(lMCTot);
			lMCDao.stop();

			return lMCTot;
		} catch (DAOException daoEx) {
			throw new F3BException(
					"CalcoloPenaController.exGetMisureCautelariComputabiliArrestiDomiciliari: Non posso leggere : "
							+ daoEx);
		} catch (Exception ex) {
			throw new F3BException(
					"CalcoloPenaController.exGetMisureCautelariComputabiliArrestiDomiciliari: Non posso leggere  : "
							+ ex);
		} finally {
			cleanup(lMCDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Effettua la ricerca delle Misure Cautelari computabili ai fini della Permanenza in Casa per il
	 * fascicolo corrente. Somma i valori e restituisce il totale in un CalendarModel
	 *
	 * @param lFascID
	 *            - Id del fascicolo
	 * @return CalendarModel - somma delle Misure Cautelari
	 * @throws F3BException
	 ****************************************************************************/
	public CalendarModel exGetMisureCautelariComputabiliPermanenzaInCasa(BigDecimal lFascID)
			throws F3BException {

		Connection lConn = null;
		MisuraCautelareSqlDAO lMCDao = null;

		CalendarModel lCalMod = new CalendarModel();
		CalendarModel lMCTot = new CalendarModel();
		CalendarUtil lCalCon = new CalendarUtil();

		MisuraCautelareModel lMisCau = new MisuraCautelareModel();

		lMisCau.setFasSieIdFascicoloSiep(lFascID);
		lMisCau.setFlagComputabile("S");
		lMisCau.setCodTipoMisura("CB"); // Custodia Cautelare in Regime di Permanenza in Casa

		try {
			lConn = getDBConnection();
			lMCDao = new MisuraCautelareSqlDAO(lConn);
			lMCDao.ricercaMisuraCautelare(lMisCau);
			lMCDao.start();

			while (lMCDao.next()) {
				lMisCau = (MisuraCautelareModel) lMCDao.getModel();

				if (lMisCau.getDataInizio() != null && lMisCau.getDataFine() != null) {
					lCalMod.setDataInizio(lMisCau.getDataInizio());
					lCalMod.setDataFine(lMisCau.getDataFine());

					lCalMod = lCalCon.CalcolaNumGiorniMesiAnni(lCalMod);

					lMCTot = lCalCon.sommaGiorni(lMCTot, lCalMod);
				}

				if (lMisCau.getDataInizio() == null && lMisCau.getDataFine() == null) {
					lCalMod.setNumAnni(lMisCau.getNumAnni());
					lCalMod.setNumMesi(lMisCau.getNumMesi());
					lCalMod.setNumGiorni(lMisCau.getNumGiorni());

					lMCTot = lCalCon.sommaGiorni(lMCTot, lCalMod);
				}
			}

			lMCTot = lCalCon.ricalcolaGAM(lMCTot);
			lMCDao.stop();

			return lMCTot;
		} catch (DAOException daoEx) {
			throw new F3BException(
					"CalcoloPenaController.exGetMisureCautelariComputabiliPermanenzaInCasa: Non posso leggere : "
							+ daoEx);
		} catch (Exception ex) {
			throw new F3BException(
					"CalcoloPenaController.exGetMisureCautelariComputabiliPermanenzaInCasa: Non posso leggere  : "
							+ ex);
		} finally {
			cleanup(lMCDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Effettua la ricerca delle Misure Cautelari computabili ai fini del Collocamento in Comunità per il
	 * fascicolo corrente. Somma i valori e restituisce il totale in un CalendarModel
	 *
	 * @param lFascID
	 *            - Id del fascicolo
	 * @return CalendarModel - somma delle Misure Cautelari
	 * @throws F3BException
	 ****************************************************************************/
	public CalendarModel exGetMisureCautelariComputabiliCollocamentoInComunita(BigDecimal lFascID)
			throws F3BException {

		Connection lConn = null;
		MisuraCautelareSqlDAO lMCDao = null;

		CalendarModel lCalMod = new CalendarModel();
		CalendarModel lMCTot = new CalendarModel();
		CalendarUtil lCalCon = new CalendarUtil();

		MisuraCautelareModel lMisCau = new MisuraCautelareModel();

		lMisCau.setFasSieIdFascicoloSiep(lFascID);
		lMisCau.setFlagComputabile("S");
		lMisCau.setCodTipoMisura("CC"); // Custodia Cautelare in Collocamento in Comunita'

		try {
			lConn = getDBConnection();
			lMCDao = new MisuraCautelareSqlDAO(lConn);
			lMCDao.ricercaMisuraCautelare(lMisCau);
			lMCDao.start();

			while (lMCDao.next()) {
				lMisCau = (MisuraCautelareModel) lMCDao.getModel();

				if (lMisCau.getDataInizio() != null && lMisCau.getDataFine() != null) {
					lCalMod.setDataInizio(lMisCau.getDataInizio());
					lCalMod.setDataFine(lMisCau.getDataFine());

					lCalMod = lCalCon.CalcolaNumGiorniMesiAnni(lCalMod);

					lMCTot = lCalCon.sommaGiorni(lMCTot, lCalMod);
				}

				if (lMisCau.getDataInizio() == null && lMisCau.getDataFine() == null) {
					lCalMod.setNumAnni(lMisCau.getNumAnni());
					lCalMod.setNumMesi(lMisCau.getNumMesi());
					lCalMod.setNumGiorni(lMisCau.getNumGiorni());

					lMCTot = lCalCon.sommaGiorni(lMCTot, lCalMod);
				}
			}

			lMCTot = lCalCon.ricalcolaGAM(lMCTot);
			lMCDao.stop();

			return lMCTot;
		} catch (DAOException daoEx) {
			throw new F3BException(
					"CalcoloPenaController.exGetMisureCautelariComputabiliCollocamentoInComunita: Non posso leggere : "
							+ daoEx);
		} catch (Exception ex) {
			throw new F3BException(
					"CalcoloPenaController.exGetMisureCautelariComputabiliCollocamentoInComunita: Non posso leggere  : "
							+ ex);
		} finally {
			cleanup(lMCDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Effettua la ricerca delle Misure Cautelari computabili ai fini della Camera di Sicurezza per il
	 * fascicolo corrente. Somma i valori e restituisce il totale in un CalendarModel
	 *
	 * @param lFascID
	 *            - Id del fascicolo
	 * @return CalendarModel - somma delle Misure Cautelari
	 * @throws F3BException
	 ****************************************************************************/
	public CalendarModel exGetMisureCautelariComputabiliCameraDiSicurezza(BigDecimal lFascID)
			throws F3BException {

		Connection lConn = null;
		MisuraCautelareSqlDAO lMCDao = null;

		CalendarModel lCalMod = new CalendarModel();
		CalendarModel lMCTot = new CalendarModel();
		CalendarUtil lCalCon = new CalendarUtil();

		MisuraCautelareModel lMisCau = new MisuraCautelareModel();

		lMisCau.setFasSieIdFascicoloSiep(lFascID);
		lMisCau.setFlagComputabile("S");
		lMisCau.setCodTipoMisura("CE"); // Custodia cautelare in Camera di Sicurezza

		try {
			lConn = getDBConnection();
			lMCDao = new MisuraCautelareSqlDAO(lConn);
			lMCDao.ricercaMisuraCautelare(lMisCau);
			lMCDao.start();

			while (lMCDao.next()) {
				lMisCau = (MisuraCautelareModel) lMCDao.getModel();

				if (lMisCau.getDataInizio() != null && lMisCau.getDataFine() != null) {
					lCalMod.setDataInizio(lMisCau.getDataInizio());
					lCalMod.setDataFine(lMisCau.getDataFine());

					lCalMod = lCalCon.CalcolaNumGiorniMesiAnni(lCalMod);

					lMCTot = lCalCon.sommaGiorni(lMCTot, lCalMod);
				}

				if (lMisCau.getDataInizio() == null && lMisCau.getDataFine() == null) {
					lCalMod.setNumAnni(lMisCau.getNumAnni());
					lCalMod.setNumMesi(lMisCau.getNumMesi());
					lCalMod.setNumGiorni(lMisCau.getNumGiorni());

					lMCTot = lCalCon.sommaGiorni(lMCTot, lCalMod);
				}
			}

			lMCTot = lCalCon.ricalcolaGAM(lMCTot);
			lMCDao.stop();

			return lMCTot;
		} catch (DAOException daoEx) {
			throw new F3BException(
					"CalcoloPenaController.exGetMisureCautelariComputabiliCameraDiSicurezza: Non posso leggere : "
							+ daoEx);
		} catch (Exception ex) {
			throw new F3BException(
					"CalcoloPenaController.exGetMisureCautelariComputabiliCameraDiSicurezza: Non posso leggere  : "
							+ ex);
		} finally {
			cleanup(lMCDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Effettua la ricerca delle Misure Cautelari computabili ai fini del Periodo di Messa alla Prova per il
	 * fascicolo corrente. Somma i valori e restituisce il totale in un CalendarModel
	 *
	 * @param lFascID
	 *            - Id del fascicolo
	 * @return CalendarModel - somma delle Misure Cautelari
	 * @throws F3BException
	 ****************************************************************************/
	public CalendarModel exGetMisureCautelariComputabiliPeriodoMessaAllaProva(BigDecimal lFascID)
			throws F3BException {

		Connection lConn = null;
		MisuraCautelareSqlDAO lMCDao = null;

		CalendarModel lCalMod = new CalendarModel();
		CalendarModel lMCTot = new CalendarModel();
		CalendarUtil lCalCon = new CalendarUtil();

		MisuraCautelareModel lMisCau = new MisuraCautelareModel();

		lMisCau.setFasSieIdFascicoloSiep(lFascID);
		lMisCau.setFlagComputabile("S");
		lMisCau.setCodTipoMisura("CL"); // Computo periodo di messa alla prova

		try {
			lConn = getDBConnection();
			lMCDao = new MisuraCautelareSqlDAO(lConn);
			lMCDao.ricercaMisuraCautelare(lMisCau);
			lMCDao.start();

			while (lMCDao.next()) {
				lMisCau = (MisuraCautelareModel) lMCDao.getModel();

				if (lMisCau.getDataInizio() != null && lMisCau.getDataFine() != null) {
					lCalMod.setDataInizio(lMisCau.getDataInizio());
					lCalMod.setDataFine(lMisCau.getDataFine());

					lCalMod = lCalCon.CalcolaNumGiorniMesiAnni(lCalMod);

					lMCTot = lCalCon.sommaGiorni(lMCTot, lCalMod);
				}

				if (lMisCau.getDataInizio() == null && lMisCau.getDataFine() == null) {
					lCalMod.setNumAnni(lMisCau.getNumAnni());
					lCalMod.setNumMesi(lMisCau.getNumMesi());
					lCalMod.setNumGiorni(lMisCau.getNumGiorni());

					lMCTot = lCalCon.sommaGiorni(lMCTot, lCalMod);
				}
			}

			lMCTot = lCalCon.ricalcolaGAM(lMCTot);
			lMCDao.stop();

			return lMCTot;
		} catch (DAOException daoEx) {
			throw new F3BException(
					"CalcoloPenaController.exGetMisureCautelariComputabiliPeriodoMessaAllaProva: Non posso leggere : "
							+ daoEx);
		} catch (Exception ex) {
			throw new F3BException(
					"CalcoloPenaController.exGetMisureCautelariComputabiliPeriodoMessaAllaProva: Non posso leggere  : "
							+ ex);
		} finally {
			cleanup(lMCDao);
			cleanup(lConn);
		}
	}

	/**
	 *
	 * @param aPenaResidua
	 * @param aFungModel
	 * @throws F3BException
	 */
	public void ExInserisciAggiornaPenaResiduaFungibilita(PenaResiduaModel aPenaResidua,
			FungibilitaModel aFungModel) throws F3BException {

		Connection lConn = null;

		FungibilitaSqlDAO lFunSqlDao = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;

		try {
			lConn = getDBTransaction();

			// ==========================================
			// Inserisco/modifico la nuova pena residua
			// ==========================================
			lPenResSqlDao = new PenaResiduaSqlDAO(lConn);

			aPenaResidua = lPenResSqlDao.inserisciOModificaPenaResidua(aPenaResidua);
			lPenResSqlDao.stop();

			// ===================================
			// Inserisco/modifico la fungibilità
			// ===================================
			if (aFungModel != null) {
				lFunSqlDao = new FungibilitaSqlDAO(lConn);

				lFunSqlDao.inserisciOModificaFungibilita(aFungModel);
				lFunSqlDao.stop();
			}

			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException:", daoEx);
			rollback(lConn);
			throw new F3BException(
					"CalcoloPenaController.ExInserisciAggiornaPenaResiduaFungibilita: Non posso leggere : "
							+ daoEx);
		} catch (F3BException ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("F3BException:", ex);
			rollback(lConn);
			throw ex;
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception:", ex);
			rollback(lConn);
			throw new F3BException("CalcoloPenaController.ExInserisciAggiornaPenaResiduaFungibilita: " + ex);
		} finally {
			cleanup(lPenResSqlDao);
			cleanup(lFunSqlDao);

			cleanup(lConn);
		}
	}

}