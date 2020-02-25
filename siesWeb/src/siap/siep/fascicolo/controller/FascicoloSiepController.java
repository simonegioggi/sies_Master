package siap.siep.fascicolo.controller;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.StringUtils;
import siap.controller.SiapController;
import siap.sico.SICOException;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.controller.IEventoSimeone;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.libertaanticipata.controller.ILicenzaPeriodiLibAnticipata;
import siap.sico.libertaanticipata.dao.LicenzaLibanticipataSqlDAO;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.dao.MagistratoCompetenteMagistratoSqlDAO;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.dao.MisuraAlternativaSqlDAO;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.residenza.controller.IResidenza;
import siap.sico.residenza.dao.ResidenzaDAO;
import siap.sico.residenza.dao.ResidenzaFascicoloSiepDAO;
import siap.sico.residenza.dao.ResidenzaSqlDAO;
import siap.sico.residenza.model.ResidenzaAssociataModel;
import siap.sico.residenza.model.ResidenzaFascicoloSiepModel;
import siap.sico.residenza.model.ResidenzaModel;
import siap.sico.soggetto.dao.SoggettoAliasFascicoloSqlDao;
import siap.sico.soggetto.dao.SoggettoDAO;
import siap.sico.soggetto.dao.SoggettoFascicoloSqlDAO;
import siap.sico.soggetto.dao.SoggettoSqlDAO;
import siap.sico.soggetto.model.SoggettoAliasFascicoloModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.SIEPException;
import siap.siep.agdgfascicolosiep.controller.IAgdgFascicoloSiep;
import siap.siep.agdgfascicolosiep.dao.AgdgFascicoloSiepSqlDAO;
import siap.siep.alias.dao.AliasSqlDAO;
import siap.siep.alias.model.AliasModel;
import siap.siep.altrigradigiudizio.dao.AltriGradiGiudizioSqlDAO;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.avvocato.dao.AvvocatoFascicoloSiepSqlDAO;
import siap.siep.avvocato.dao.AvvocatoSqlDAO;
import siap.siep.avvocato.model.AvvocatoFascicoloSiepModel;
import siap.siep.avvocato.model.AvvocatoModel;
import siap.siep.avvocato.model.AvvocatoSiepModel;
import siap.siep.beneficio.dao.BeneficioSqlDAO;
import siap.siep.beneficio.model.BeneficioModel;
import siap.siep.calcolopena.controller.CalcoloPenaControllerF5;
import siap.siep.calcolopena.model.CalcoloPenaModel;
import siap.siep.circostanza.controller.ICircostanza;
import siap.siep.circostanza.dao.CircostanzaSqlDAO;
import siap.siep.circostanza.model.CircostanzaModel;
import siap.siep.competenza.controller.ICompetenza;
import siap.siep.cumulo.controller.ICumulo;
import siap.siep.cumulo.model.CumuloModel;
import siap.siep.decretoordinanza.controller.IDecretoOrdinanzaSiep;
import siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel;
import siap.siep.fascicolo.dao.FascicoloReatoSqlDAO;
import siap.siep.fascicolo.dao.FascicoloSiepAggregatoSqlDAO;
import siap.siep.fascicolo.dao.FascicoloSiepDAO;
import siap.siep.fascicolo.dao.FascicoloSiepOnViewSqlDAO;
import siap.siep.fascicolo.dao.FascicoloSiepSoggettoSqlDAO;
import siap.siep.fascicolo.dao.FascicoloSiepSqlDAO;
import siap.siep.fascicolo.model.DatiSiepPerTrasferimentoModel;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.fascicolo.model.FascicoloSiepAggregatoModel;
import siap.siep.fascicolo.model.FascicoloSiepCertBlobModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.fungibilita.controller.IFungibilita;
import siap.siep.misuracautelare.dao.MisuraCautelareSqlDAO;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.misurasicurezza.dao.MisuraSicurezzaSqlDAO;
import siap.siep.misurasicurezza.model.FascMsToFascSiepModel;
import siap.siep.modulocumulo.dao.PenaRideterminataCumuloSqlDAO;
import siap.siep.modulocumulo.model.PenaRideterminataCumuloModel;
import siap.siep.notefascicolo.dao.NoteFascicoloSqlDAO;
import siap.siep.notefascicolo.model.NoteFascicoloModel;
import siap.siep.nuovaistanza.controller.INuovaIstanza;
import siap.siep.nuovaistanza.dao.NuovaIstanzaSqlDAO;
import siap.siep.nuovaistanza.model.NuovaIstanzaModel;
import siap.siep.penaaccessoria.dao.PenaAccessoriaSqlDAO;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaSanzioneSostitutivaModel;
import siap.siep.penacumulo.controller.IPenaCumulo;
import siap.siep.penacumulo.model.PenaCumuloModel;
import siap.siep.penapecuniaria.controller.IRichiestaConversione;
import siap.siep.penapecuniaria.dao.RichiestaConversioneSqlDAO;
import siap.siep.penapecuniaria.model.RichiestaConversioneModel;
import siap.siep.penapresunta.controller.IPenaPresunta;
import siap.siep.penapresunta.dao.PenaPresuntaSqlDAO;
import siap.siep.penapresunta.model.PenaPresuntaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.dao.PenaResiduaDAO;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.dao.PosizioneGiuridicaSqlDAO;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.posizionematerialefasc.dao.PosizioneMaterialeFascSqlDAO;
import siap.siep.posizionematerialefasc.model.PosizioneMaterialeFascModel;
import siap.siep.reato.controller.IReato;
import siap.siep.reato.dao.ReatoSqlDAO;
import siap.siep.reato.model.ReatoModel;
import siap.siep.sanzionesostitutiva.controller.ISanzioneSostitutiva;
import siap.siep.scambiosanzione.dao.ScambioSanzioneSqlDAO;
import siap.siep.scambiosanzione.model.ScambioSanzioneModel;
import siap.siep.sentenza.dao.SentenzaSqlDAO;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.sentenzariunita.controller.ISentenzaRiunita;
import siap.siep.sospensione.controller.ISospensione;
import siap.siep.sospensione.model.SospensioneModel;
import siap.siep.statoprocedimento.dao.StatoProcedimentoDAO;
import siap.siep.statoprocedimento.dao.StatoProcedimentoSqlDAO;
import siap.siep.statoprocedimento.model.StatoProcedimentoModel;
import siap.siep.ulterioresanzionecumulo.controller.IUlterioreSanzioneCumulo;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: FascicoloSiepModel
 * </p>
 * <p>
 * Description: Realizza il controller del Fascicolo Siep
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 *
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class FascicoloSiepController extends SiapController implements IFascicoloSiep {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Inserisce il Fascicolo Siep. Sia per Assegnazione automatica che per assegnazione manuale.
	 *
	 * @param aFascicoloSiep
	 * @return FascicoloSiepModel
	 * @throws F3BException
	 */
	public FascicoloSiepModel ExInserisciFascicoloSiep(FascicoloSiepModel aFascicoloSiep)
			throws F3BException {

		Connection lConn = null;

		FascicoloSiepDAO lFasDao = null;
		FascicoloSiepSqlDAO lFasDaoSql = null;
		SoggettoDAO lSoggDao = null;
		StatoProcedimentoDAO lStatoProcDao = null;

		FascicoloSiepSqlDAO lFasDaoSql1 = null;

		AliasSqlDAO lAliDaoSql = null;

		SoggettoSqlDAO lSoggDaoSql = null;

		FascicoloSiepSqlDAO lFasDaoSqlSenSog = null;

		try {
			lConn = getDBTransaction();
			lFasDao = new FascicoloSiepDAO(lConn);
			lFasDaoSql = new FascicoloSiepSqlDAO(lConn);
			lFasDaoSql1 = new FascicoloSiepSqlDAO(lConn);
			BigDecimal chiaveProgrManuale = null;
			int annoCorrente = 0;

			// Se inserimento manuale verifico che non esista già a sistema un
			// fascicolo con la stessa numerazione
			if (aFascicoloSiep.getChiaveProgr() != null) {
				chiaveProgrManuale = aFascicoloSiep.getChiaveProgr();
				annoCorrente = new Integer(DateUtils.getSysDate("yyyy")).intValue();

				BigDecimal idFas = ExRicercaIDFascicoloSiepByProgrAnnoCodUfficio(aFascicoloSiep);

				// Se fascicolo già presente
				if (idFas != null) {
					throw new SIEPException(F3BException.USER_MESSAGE,
							"Impossibile inserire il fascicolo! Anno e Numero procedimento già presenti");
				}
			}

			// ========================================================================
			// Determino il progressivo di inserimento se assegnazione
			// automatica.
			// Se assegnazione manuale devo verificare che il progressivo
			// assegnato
			// sia minore del max progr a sistema per evitare buchi di
			// numerazione.
			// Questo controllo viene effettuato solo se anno manuale = anno
			// corrente
			// e fascicolo inquadrabile in una classe. Per i fascicoli a
			// numerazione
			// speciale (pretura o RES con lettera) il controllo è superfluo,
			// non è
			// possibile creare un buco.
			// ========================================================================
			// Se inserimento automatico o se inserimento manuale con anno
			// corrente
			// serve reuperare l'ultimo progressivo assegnato dal sistema
			if (aFascicoloSiep.getChiaveProgr() == null // assegnazione
														// automatica
					|| (aFascicoloSiep.getChiaveProgr() != null // Assegnazione
																// manuale
							&& aFascicoloSiep.getChiaveAnno().intValue() == annoCorrente // di
																							// un
																							// fascicolo
																							// dell'anno
																							// corrente
							&& aFascicoloSiep.getTipoProgressivo() != 0 // solo se il
																		// fascicoli è
																		// inquadrabile
																		// in una
																		// classe
					)) {

				// Cerco il Progressivo rispettivamente al tipo progressivo
				// impostato
				// solo se la classe è impostata
				lFasDaoSql.getProgressivoFascicoloSiep(aFascicoloSiep);
				lFasDaoSql.start();
				int lMaxProgr = 0;

				if (lFasDaoSql.next() && (lFasDaoSql.getInt("aMAX") > 0))
					lMaxProgr = lFasDaoSql.getInt("aMAX");

				lFasDaoSql.stop();

				// Setto la ChiaveProgressivo del Model con il MAX + 1 a seconda
				// del tipo...
				int lTipoProgr = aFascicoloSiep.getTipoProgressivo();
				if (lMaxProgr == 0) {
					if (lTipoProgr == 1)
						aFascicoloSiep.setChiaveProgr(new BigDecimal(1));
					else
						aFascicoloSiep.setChiaveProgr(new BigDecimal(lTipoProgr * 10000 + 1));
				} else
					aFascicoloSiep.setChiaveProgr(new BigDecimal(lMaxProgr + 1));

				// Se inserimemto manuale
				if (chiaveProgrManuale != null) {
					if (aFascicoloSiep.getChiaveAnno().intValue() == Integer
							.parseInt(DateUtils.getSysDate("yyyy"))
							&& aFascicoloSiep.getTipoProgressivo() == 4 // solo
																		// MS
					) { // d.f. 15/04/2015 solo per le MS la numerazione manuale
						// può partire
						// Se progressivo manuale > ultimo progressivo assegnato
						// dal sistema
						if (chiaveProgrManuale.intValue() > lMaxProgr && lMaxProgr > 0) {
							throw new SIEPException(F3BException.USER_MESSAGE,
									"Impossibile inserire il fascicolo! Il numero del procedimento è superiore all'ultimo assegnato dal sistema");
						}
					} else if (chiaveProgrManuale.intValue() > aFascicoloSiep.getChiaveProgr().intValue()
							- 1) {
						throw new SIEPException(F3BException.USER_MESSAGE,
								"Impossibile inserire il fascicolo! Il numero del procedimento è superiore all'ultimo assegnato dal sistema");
					}

					// Bisogna riassegnare perchè sovrascritta per il recupero
					// dell'ultimo progressivo
					// inserito
					aFascicoloSiep.setChiaveProgr(chiaveProgrManuale);
				}
			}

			// Controlla se ci sono Alias
			lAliDaoSql = new AliasSqlDAO(lConn);
			lAliDaoSql.ricercaAliasByIdSoggetto(aFascicoloSiep.getSogIdSoggetto());
			/* AliasModel lAliai = (AliasModel) */lAliDaoSql.getModelByKey();

			// Se il Soggetto ha degli Alias il soggetto viene duplicato
			// e il nuovo fascicolo viene assegnato al nuovo soggetto.

			// if(lAliai != null)
			// {

			// AMBROS 03/2010 - SuperSogg
			// devo sempre duplicare il Soggetto TRANNE QUANDO NON
			// C'E'procedimento associato:
			// In questo caso vuol dire che vengo dall'iscrizione
			// del soggetto e non dalle ricerche.

			FascicoloSiepModel FasModel = new FascicoloSiepModel();
			FasModel.setSogIdSoggetto(aFascicoloSiep.getSogIdSoggetto());

			lFasDaoSql1.ricercaFascicolo(FasModel);
			lFasDaoSql1.start();

			BigDecimal IdFas = null;
			if (lFasDaoSql1.next()) {
				IdFas = lFasDaoSql1.getBigDecimal("ID_FASCICOLO_SIEP");
			}

			lFasDao.stop();

			// fascicolo già presente --> Vengo da Ricerca e devo DUPLICARE
			// SOGGETTO
			if (IdFas != null) {
				BigDecimal lIdFascicolo = null;
				// esegue la ricerca nel fascicolo per controllare
				// Soggetto/Sentenza
				lFasDaoSqlSenSog = new FascicoloSiepSqlDAO(lConn);
				lFasDaoSqlSenSog.ricercaIDFascicoloByIDSoggettoIDSentenza(aFascicoloSiep);

				lFasDaoSqlSenSog.start();
				if (lFasDaoSqlSenSog.next()) {
					lIdFascicolo = lFasDaoSqlSenSog.getBigDecimal("ID_FASCICOLO_SIEP");
				}
				lFasDaoSqlSenSog.stop();

				// Se trova fascicoli visualizza l'alert
				if (lIdFascicolo != null) {
					throw new SIEPException(F3BException.USER_MESSAGE,
							"Impossibile inserire il fascicolo! Esiste un fascicolo per la sentenza ed il soggetto selezionato");
				} else {
					// Ricerca il soggetto
					lSoggDaoSql = new SoggettoSqlDAO(lConn);
					lSoggDaoSql.ricercaSoggettoByKey(aFascicoloSiep.getSogIdSoggetto());
					SoggettoModel lSoggettoi = (SoggettoModel) lSoggDaoSql.getModelByKey();

					// duplica il soggetto
					lSoggDao = new SoggettoDAO(lConn);
					lSoggDao.setDAOFromModel(lSoggettoi);
					BigDecimal lidSoggetto = lSoggDao.insert();

					// Assegna al nuovo fascicolo l'id del nuovo soggetto
					aFascicoloSiep.setSogIdSoggetto(lidSoggetto);
				}

			} // Chiude if IdFas != null

			// Inserimento fascicolo
			lFasDao.setDAOFromModel(aFascicoloSiep);
			// mlog.debug(aFascicoloSiep.toString());
			BigDecimal lChiave = lFasDao.insert();

			aFascicoloSiep.setIdFascicoloSiep(lChiave);
			// Aggiorna sul soggetto il flag di presenza fascicolo a 'SI'
			lSoggDao = new SoggettoDAO(lConn);
			lSoggDao.setIdSoggetto(aFascicoloSiep.getSogIdSoggetto());
			lSoggDao.setFlagPresenzaFascicolo("S");
			// Ambros 03/2010 SuperSogg
			lSoggDao.setDataInserimento(DateUtils.getSysDate());
			lSoggDao.setCodUfficioInserimento(aFascicoloSiep.getCodUfficioInserimento());
			lSoggDao.setCodOperatoreInserimento(aFascicoloSiep.getCodOperatoreInserimento());

			lSoggDao.selByKey();
			lSoggDao.update();

			// GDV Stato del procedimento
			StatoProcedimentoModel lStat = new StatoProcedimentoModel();
			lStat.setFasSieIdFascicoloSiep(lChiave);
			lStat.setProgressivo(new BigDecimal(1));
			lStat.setDataInserimento(DateUtils.getSysDate());
			lStat.setCodUfficioInserimento(aFascicoloSiep.getCodUfficioInserimento());
			lStat.setCodOperatoreInserimento(aFascicoloSiep.getCodOperatoreInserimento());
			lStat.setCodStatoProcedimento("0108"); // Iscritto
			lStatoProcDao = new StatoProcedimentoDAO(lConn);
			lStatoProcDao.setDAOFromModel(lStat);
			lStatoProcDao.insert();
			// GDV Stato del procedimento
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			ex.printStackTrace();
			if (ex.getMessage().indexOf("FAS_SIE_SEN_SOG_FK_I") != -1)
				throw new SIEPException(F3BException.USER_MESSAGE,
						"Impossibile inserire il fascicolo! Esiste un fascicolo per la sentenza ed il soggetto selezionato");

			if (ex.getMessage().indexOf("FAS_ANN_UFF_PRO_FK_I") != -1)
				throw new SIEPException(F3BException.USER_MESSAGE,
						"Impossibile inserire il fascicolo! Esiste un fascicolo per lo stesso numero SIEP");

			throw new SIEPException("FascicoloSiepController.ExInserisciFascicoloSiep: " + ex);
		} catch (SIEPException ex) {
			rollback(lConn);
			ex.printStackTrace();
			throw ex;
		} catch (Exception sqe) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error(sqe);
			sqe.printStackTrace();
			throw new F3BException("FascicoloSiepController.ExInserisciFascicoloSiep: " + sqe);
		} finally {
			cleanup(lFasDaoSql);
			cleanup(lFasDao);
			cleanup(lSoggDao);
			cleanup(lStatoProcDao);

			cleanup(lFasDaoSql1);

			cleanup(lConn);
		}

		return aFascicoloSiep;
	}

	/**
	 * Inserisci residenza associata al fascicolo SIEP Associa la Residenza al Fascicolo storicizzando quella
	 * eventualmente presente
	 *
	 * @param aResidenza
	 * @return ResidenzaAssociataModel
	 * @throws F3BException
	 */
	public ResidenzaAssociataModel ExInserisciResidenzaFascicoloSiep(ResidenzaAssociataModel aResidenza)
			throws F3BException {

		Connection lConn = null;

		ResidenzaDAO lResDao = null;
		ResidenzaFascicoloSiepDAO lResFascDao = null;
		ResidenzaSqlDAO lResSqlDAO = null;
		ResidenzaModel lResidenza = aResidenza.getResidenza();
		ResidenzaFascicoloSiepModel lResidenzaFascicolo = aResidenza.getResidenzaFascicoloSiep();

		try {
			lConn = getDBTransaction();
			// Inserisce una nuova Residenza per un dato Soggetto
			if (lResidenza.getIdResidenza().compareTo(new BigDecimal(0)) == 0) {
				lResDao = new ResidenzaDAO(lConn);
				lResDao.setDAOFromModel(lResidenza);
				BigDecimal lSequence = lResDao.insert();

				lResidenza.setIdResidenza(lSequence);
				lResidenzaFascicolo.setResIdResidenza(lSequence);
			}

			// 1- Storicizza l'ultima occorrenza eventualmente presente
			// GDV - MOdifica sulla residenza nel caso di domicilio che non
			// veniva storicizzato
			// BigDecimal lIdResFasCorrente =
			// this.getIdResidenzaFascicoloCorrente(lResidenzaFascicolo.getFasSieIdFascicoloSiep());

			lResSqlDAO = new ResidenzaSqlDAO(lConn);
			if (aResidenza.getResidenza().getCodTipoResidenza().equals("D"))
				lResSqlDAO.ricercaDomicilioByFascicolo(lResidenzaFascicolo.getFasSieIdFascicoloSiep());
			else
				lResSqlDAO.ricercaResidenzaByFascicolo(lResidenzaFascicolo.getFasSieIdFascicoloSiep());

			ResidenzaModel lResCorrente = (ResidenzaModel) lResSqlDAO.getModelByKey();

			if (lResCorrente != null) // Se esiste già una residenza o un
										// domicilio associato devo
										// storicizzare
			{
				BigDecimal lIdResFasCorrente = lResCorrente.getIdResidenza();
				lResFascDao = new ResidenzaFascicoloSiepDAO(lConn);
				if (lIdResFasCorrente != null) {
					lResFascDao.setDataFineValidita(lResidenzaFascicolo.getDataInizioValidita());
					lResFascDao.setCondizioneResFascCorrente(lResidenzaFascicolo.getFasSieIdFascicoloSiep(),
							lIdResFasCorrente);
					lResFascDao.update();
					lResFascDao.stop();
				}
			}

			// 2 - Inserisce la nuova occorrenza su Residenza Fascicolo Siep
			lResFascDao = new ResidenzaFascicoloSiepDAO(lConn);
			lResFascDao.setDAOFromModel(lResidenzaFascicolo);
			lResFascDao.insert();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("FascicoloSiepController.ExInserisciResidenzaFascicoloSiep", ex);
			throw new F3BException("FascicoloSiepController.ExInserisciResidenzaFascicoloSiep : " + ex);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error(ex);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("FascicoloSiepController.ExInserisciResidenzaFascicoloSiep", ex);
			throw new F3BException("FascicoloSiepController.ExInserisciResidenzaFascicoloSiep : " + ex);
		} finally {
			cleanup(lResDao);
			cleanup(lResSqlDAO);
			cleanup(lResFascDao);
			cleanup(lConn);
		}

		return aResidenza;
	}

	/**
	 * Ricerca la residenza corrente per il fascicolo SIEP.
	 *
	 * @param aIdFascicolo
	 * @return
	 * @throws F3BException
	 */
	public ResidenzaAssociataModel ExRicercaResidenzaFascicoloSiepCorrente(BigDecimal aIdFascicolo)
			throws F3BException {

		Connection lConn = null;
		ResidenzaAssociataModel lResAss = new ResidenzaAssociataModel();
		ResidenzaSqlDAO lResDao = null;

		try {
			lConn = getDBConnection();
			lResDao = new ResidenzaSqlDAO(lConn);

			// Residenza Corrente
			lResDao.ricercaResidenzaByFascicolo(aIdFascicolo);
			lResAss.setResidenza((ResidenzaModel) lResDao.getModelByKey());
		} catch (DAOException dex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("DAOException: ", dex);
			throw new SIEPException(
					"FascicoloSiepController.ExRicercaResidenzaFascicoloSiepCorrente: " + dex);
		} finally {
			cleanup(lResDao);
			cleanup(lConn);
		}
		return lResAss;
	}

	/**
	 * ExRicercaDomicilioFascicoloSiepCorrente
	 *
	 * @param aIdFascicolo
	 * @return
	 * @throws F3BException
	 */
	public ResidenzaAssociataModel ExRicercaDomicilioFascicoloSiepCorrente(BigDecimal aIdFascicolo)
			throws F3BException {

		Connection lConn = null;
		ResidenzaAssociataModel lResAss = new ResidenzaAssociataModel();
		ResidenzaSqlDAO lResDao = null;

		try {
			lConn = getDBConnection();
			lResDao = new ResidenzaSqlDAO(lConn);

			// Domicilio Corrente
			lResDao.ricercaDomicilioByFascicolo(aIdFascicolo);
			lResAss.setResidenza((ResidenzaModel) lResDao.getModelByKey());
		} catch (DAOException dex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("DAOException: ", dex);
			throw new SIEPException(
					"FascicoloSiepController.ExRicercaDomicilioFascicoloSiepCorrente: " + dex);
		} finally {
			cleanup(lResDao);
			cleanup(lConn);
		}
		return lResAss;
	}

	/**
	 * Ricerca Fascicolo Siep
	 *
	 * @param aFascicoloSiep
	 * @return Vettore di FascicoloSiepModel
	 * @throws F3BException
	 */
	public Vector ExRicercaFascicoloSiep(FascicoloSiepModel aFascicoloSiep) throws F3BException {

		Connection lConn = null;
		Vector lFascicoli = new Vector();
		FascicoloSiepSqlDAO lFasDao = null;
		SoggettoSqlDAO lSoggDao = null;
		SentenzaSqlDAO lSentDao = null;

		try {
			lConn = getDBConnection();

			lFasDao = new FascicoloSiepSqlDAO(lConn);
			lFasDao.ricercaFascicolo(aFascicoloSiep);
			lFascicoli = new Vector(lFasDao.getModels());
			lSoggDao = new SoggettoSqlDAO(lConn);
			lSentDao = new SentenzaSqlDAO(lConn);

			lFasDao.ricercaFascicoloPerUfficio(aFascicoloSiep);
			lFasDao.start();

			FascicoloSiepModel lFascicolo = null;
			SoggettoModel lSoggMod = null;
			SentenzaModel lSentMod = null;

			while (lFasDao.next()) {
				lFascicolo = (FascicoloSiepModel) lFasDao.getModel();
				lSoggDao.ricercaSoggettoByKey(lFascicolo.getSogIdSoggetto());
				lSoggMod = (SoggettoModel) lSoggDao.getModelByKey();
				lFascicolo.setSoggetto(lSoggMod);
				lSentDao.ricercaSentenzaBykey(lFascicolo.getSenIdSentenza());
				lSentMod = (SentenzaModel) lSentDao.getModelByKey();
				lFascicolo.setSentenza(lSentMod);
				lFascicoli.add(lFascicolo);
			}

			lFasDao.stop();

			if (lFascicoli.isEmpty())
				throw new SIEPException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: ", daoEx);
			throw new SIEPException(F3BException.USER_MESSAGE,
					"FascicoloSiepController.ExRicercaFascicoloSiep: Non posso leggere : " + daoEx);
		} catch (SIEPException sqe) {
			throw new SIEPException(F3BException.USER_MESSAGE,
					"FascicoloSiepController.ExRicercaFascicoloSiep: Non posso leggere  : " + sqe);
		} catch (Exception sqe) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("Exception: ", sqe);
			throw new SIEPException(F3BException.USER_MESSAGE,
					"FascicoloSiepController.ExRicercaFascicoloSiep: Non posso leggere  : " + sqe);
		} finally {
			cleanup(lFasDao);
			cleanup(lSoggDao);
			cleanup(lSentDao);
			cleanup(lConn);
		}
		return lFascicoli;
	}

	/**
	 * Effettua la ricerca per idSoggetto ed eventualmente per: ID_SENTENZA, ID_FASCICOLO_SIEP, CHIAVE_ANNO,
	 * CHIAVE_PROGR
	 *
	 * @param aFascicoloSiep
	 */
	public Vector ExRicercaFascicoloSiepSoggetto(FascicoloSiepModel aFascicoloSiep) throws F3BException {

		Connection lConn = null;
		Vector lFascicoli = new Vector();
		FascicoloSiepOnViewSqlDAO lFasDao = null;
		SentenzaSqlDAO lSentDao = null;
		FascicoloSiepModel lFascicolo = null;
		SentenzaModel lSentMod = null;
		Vector lFascicoliNuovi = new Vector();

		try {
			lConn = getDBConnection();
			lSentDao = new SentenzaSqlDAO(lConn);

			lFasDao = new FascicoloSiepOnViewSqlDAO(lConn);
			lFasDao.ricercaFascicoloSoggetto(aFascicoloSiep);
			lFascicoli = new Vector(lFasDao.getModels());
			for (int i = 0; i < lFascicoli.size(); i++) {
				lFascicolo = (FascicoloSiepModel) lFascicoli.get(i);
				lSentDao.ricercaSentenzaBykey(lFascicolo.getSenIdSentenza());

				lSentMod = (SentenzaModel) lSentDao.getModelByKey();

				// mlog.debug("sentenza------>"+lSentMod);
				lFascicolo.setSentenza(lSentMod);
				lFascicoliNuovi.add(lFascicolo);
			}

			lFasDao.stop();

			if (lFascicoli.isEmpty())
				throw new SIEPException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new SIEPException(F3BException.USER_MESSAGE,
					"FascicoloSiepController.ExRicercaFascicoloSiepSoggetto: Non posso leggere : " + daoEx);
		} catch (Exception sqe) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("Exception: " + sqe);
			throw new SIEPException(F3BException.USER_MESSAGE,
					"FascicoloSiepController.ExRicercaFascicoloSiepSoggetto: Non posso leggere  : " + sqe);
		} finally {
			cleanup(lFasDao);
			cleanup(lSentDao);

			cleanup(lConn);
		}
		return lFascicoliNuovi;
	}

	/**
	 * Ricerca Fascicolo Siep
	 *
	 * @param aFascicoloSiep
	 * @return Vettore di FascicoloSiepModel
	 * @throws F3BException
	 */
	public Vector ExRicercaFascicoloOnView(FascicoloSiepModel aFascicoloSiep) throws F3BException {

		Connection lConn = null;
		Vector lFascicoli = new Vector();
		FascicoloSiepOnViewSqlDAO lFasDao = null;
		SoggettoSqlDAO lSoggDao = null;
		SentenzaSqlDAO lSentDao = null;

		try {
			lConn = getDBConnection();
			lFasDao = new FascicoloSiepOnViewSqlDAO(lConn);
			lFasDao.ricercaFascicolo(aFascicoloSiep);
			lFasDao.start();
			FascicoloSiepModel lFascicolo = null;

			while (lFasDao.next()) {
				lFascicolo = (FascicoloSiepModel) lFasDao.getModel();
				lFascicoli.add(lFascicolo);
			}
			lFasDao.stop();
			if (lFascicoli.isEmpty())
				throw new SIEPException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			throw new SIEPException(F3BException.USER_MESSAGE,
					"FascicoloSiepController.ExRicercaFascicoloOnView: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lFasDao);
			cleanup(lSoggDao);
			cleanup(lSentDao);

			cleanup(lConn);
		}

		return lFascicoli;
	}

	/**
	 * Ricerca Paginata sul fascicolo
	 *
	 * @param aFascicoloSiep
	 * @param aPage
	 * @param majorOffice
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaFascicoloOnViewPaged(FascicoloSiepModel aFascicoloSiep, int aPage,
			String majorOffice, String tipoUfficioUtente) throws F3BException {

		Connection lConn = null;

		Vector lFascicoli = new Vector();

		FascicoloSiepOnViewSqlDAO lFasDao = null;
		SoggettoSqlDAO lSoggDao = null;
		SentenzaSqlDAO lSentDao = null;

		try {
			lConn = getDBConnection();

			lFasDao = new FascicoloSiepOnViewSqlDAO(lConn);
			if (StringUtils.checkValidValue(majorOffice)) {
				lFasDao.ricercaFascicoloMajorPaged(aFascicoloSiep, aPage, majorOffice, tipoUfficioUtente);
			} else {
				lFasDao.ricercaFascicoloPaged(aFascicoloSiep, aPage);
			}
			lFasDao.start();
			FascicoloSiepModel lFascicolo = null;

			while (lFasDao.next()) {
				lFascicolo = (FascicoloSiepModel) lFasDao.getModel();
				lFascicoli.add(lFascicolo);
			}

			lFasDao.stop();

			if (lFascicoli.isEmpty())
				throw new SIEPException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			throw new SIEPException(F3BException.USER_MESSAGE,
					"FascicoloSiepController.ExRicercaFascicoloSiep: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lFasDao);
			cleanup(lSoggDao);
			cleanup(lSentDao);

			cleanup(lConn);
		}

		return lFascicoli;
	}

	public Vector ExRicercaFascicoloOnViewPaged(FascicoloSiepModel aFascicoloSiep, int aPage)
			throws F3BException {

		// come ricerca di default preleva solo i fascicoli di soggetti
		// maggiorenni
		return ExRicercaFascicoloOnViewPaged(aFascicoloSiep, aPage, "", "");
	}

	/**
	 * Ricerca fascicolo paginato per soggetto
	 *
	 * @param aFascicoloSiep
	 * @param aPage
	 * @param TipoRicerca
	 * @param StrCodiceDistrettoUtente
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaFascicoloOnViewPagedSoggetti(FascicoloSiepModel aFascicoloSiep, int aPage,
			String TipoRicerca, String StrCodiceDistrettoUtente) throws F3BException {

		Connection lConn = null;

		Vector lFascicoli = null;
		FascicoloSiepOnViewSqlDAO lFasDao = null;
		SoggettoSqlDAO lSoggDao = null;
		SentenzaSqlDAO lSentDao = null;
		try {
			String aCodUff = "";
			lConn = getDBConnection();
			lFasDao = new FascicoloSiepOnViewSqlDAO(lConn);
			lFasDao.ricercaFascicoloPagedSoggetto(aFascicoloSiep, aPage, aCodUff, TipoRicerca,
					StrCodiceDistrettoUtente);
			lFascicoli = new Vector(lFasDao.getModels());

			if (lFascicoli.isEmpty())
				throw new SIEPException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			throw new SIEPException(F3BException.USER_MESSAGE,
					"FascicoloSiepController.ExRicercaFascicoloOnViewPagedSoggetti: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lFasDao);
			cleanup(lSoggDao);
			cleanup(lSentDao);

			cleanup(lConn);
		}
		return lFascicoli;
	}

	/**
	 * Ricerca Fascicolo Siep per Progressivo, Anno e Ufficio
	 *
	 * @param aFascicoloSiep
	 * @return Vettore di FascicoloSiepModel
	 * @throws F3BException
	 */
	public FascicoloSiepModel ExRicercaFascicoloSiepByProgrAnnoCodUfficio(FascicoloSiepModel aFascicoloSiep)
			throws F3BException {

		Connection lConn = null;

		FascicoloSiepModel lFascicolo = null;
		FascicoloSiepSqlDAO lFasDao = null;
		SoggettoSqlDAO lSoggDao = null;
		SentenzaSqlDAO lSentDao = null;

		try {
			lConn = getDBConnection();

			lFasDao = new FascicoloSiepSqlDAO(lConn);
			lSoggDao = new SoggettoSqlDAO(lConn);
			lSentDao = new SentenzaSqlDAO(lConn);

			lFasDao.ricercaFascicoloByProgrAnnoCodUfficio(aFascicoloSiep);
			lFasDao.start();

			SoggettoModel lSoggMod = null;
			SentenzaModel lSentMod = null;

			if (lFasDao.next()) {
				lFascicolo = (FascicoloSiepModel) lFasDao.getModel();

				lSoggDao.ricercaSoggettoByKey(lFascicolo.getSogIdSoggetto());
				lSoggMod = (SoggettoModel) lSoggDao.getModelByKey();

				lFascicolo.setSoggetto(lSoggMod);

				lSentDao.ricercaSentenzaBykey(lFascicolo.getSenIdSentenza());
				lSentMod = (SentenzaModel) lSentDao.getModelByKey();

				lFascicolo.setSentenza(lSentMod);
			}

			lFasDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new SIEPException(F3BException.USER_MESSAGE,
					"FascicoloSiepController.ExRicercaFascicoloSiep: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lFasDao);
			cleanup(lSoggDao);
			cleanup(lSentDao);

			cleanup(lConn);
		}

		return lFascicolo;
	}

	/**
	 * Ricerca Paginata Fascicolo Siep per Progressivo, Anno e Ufficio
	 *
	 * @param aFascicoloSiep
	 * @param aPageNum
	 * @return Vettore di FascicoloSiepModel
	 * @throws F3BException
	 */
	public Vector ExRicercaFascicoloSiepByProgrAnnoDescrComunePaged(FascicoloSiepModel aFascicoloSiep,
			int aPageNum) throws F3BException {
		return ExRicercaFascicoloSiepByProgrAnnoDescrComunePaged(aFascicoloSiep, aPageNum, "");
	}

	// MEV_57: aggiunto parametro di passaggio
	public Vector ExRicercaFascicoloSiepByProgrAnnoDescrComunePaged(FascicoloSiepModel aFascicoloSiep,
			int aPageNum, String majorOffice) throws F3BException {

		Connection lConn = null;

		Vector lFascicoli = new Vector();

		FascicoloSiepSqlDAO lFasDao = null;
		SoggettoSqlDAO lSoggDao = null;
		SentenzaSqlDAO lSentDao = null;
		StatoProcedimentoSqlDAO lStaDao = null;

		try {
			lConn = getDBConnection();

			lFasDao = new FascicoloSiepSqlDAO(lConn);
			lSoggDao = new SoggettoSqlDAO(lConn);
			lSentDao = new SentenzaSqlDAO(lConn);
			lStaDao = new StatoProcedimentoSqlDAO(lConn);

			// MEV_57: aggiunto parametro di passaggio
			if (StringUtils.checkValidValue(majorOffice)) {
				lFasDao.ricercaFascicoloByProgrAnnoDescrComune(aFascicoloSiep, majorOffice);
			} else {
				lFasDao.ricercaFascicoloByProgrAnnoDescrComune(aFascicoloSiep);
			}
			lFasDao.startPage1(aPageNum);

			FascicoloSiepModel lFascicolo = null;
			SoggettoModel lSoggMod = null;
			SentenzaModel lSentMod = null;

			while (lFasDao.next()) {
				lFascicolo = (FascicoloSiepModel) lFasDao.getModel();

				lSoggDao.ricercaSoggettoByKey(lFascicolo.getSogIdSoggetto());
				lSoggMod = (SoggettoModel) lSoggDao.getModelByKey();

				if (lSoggMod == null)
					throw new SIEPException(F3BException.USER_MESSAGE, "Soggetto associato non trovato");

				lFascicolo.setSoggetto(lSoggMod);

				lSentDao.ricercaSentenzaBykey(lFascicolo.getSenIdSentenza());
				lSentMod = (SentenzaModel) lSentDao.getModelByKey();

				if (lSentMod == null)
					throw new SIEPException(F3BException.USER_MESSAGE, "Sentenza associato non trovato");

				lStaDao.ricercaMaxStatoProcedimentoByFascicoloSiep(lFascicolo.getIdFascicoloSiep());
				lFascicolo.setCodStatoProcedimento(lStaDao.getCodStatoByKey());

				lFascicolo.setSentenza(lSentMod);
				lFascicoli.add(lFascicolo);
			}

			lFasDao.stop();

			if (lFascicoli.isEmpty())
				throw new SIEPException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			throw new SIEPException(F3BException.USER_MESSAGE,
					"FascicoloSiepController.ExRicercaFascicoloSiepByProgrAnnoDescrComunePaged: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lFasDao);
			cleanup(lSoggDao);
			cleanup(lSentDao);
			cleanup(lStaDao);

			cleanup(lConn);
		}

		return lFascicoli;
	}

	/**
	 * Ritorna n.ro di record risultato di una ExRicercaFascicoloSiepByProgrAnnoDescrComune.
	 *
	 * MEV_57: aggiunto parametro di passaggio
	 *
	 * @param FascicoloSiepModel
	 * @return BigDecimal n.ro di record
	 * @throws F3BException
	 */
	public BigDecimal ExGetNumFascicoloSiepByProgrAnnoDescrComune(FascicoloSiepModel aFascicoloSiep,
			String majorOffice) throws F3BException {

		Connection lConn = null;
		FascicoloSiepSqlDAO lFasDao = null;
		BigDecimal lCont = new BigDecimal(0);

		try {
			lConn = getDBConnection();

			lFasDao = new FascicoloSiepSqlDAO(lConn);
			// MEV_57: aggiunto parametro di passaggio
			if (StringUtils.checkValidValue(majorOffice)) {
				lFasDao.ricercaFascicoloByProgrAnnoDescrComune(aFascicoloSiep, majorOffice);
			} else {
				lFasDao.ricercaFascicoloByProgrAnnoDescrComune(aFascicoloSiep);
			}
			lCont = lFasDao.getNumRowsSelected();

		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new SIEPException(F3BException.USER_MESSAGE,
					"FascicoloSiepController.ExGetNumFascicoloSiepByProgrAnnoDescrComune: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lFasDao);
			cleanup(lConn);
		}
		return lCont;
	}

	/**
	 * Ricerca Fascicolo Siep per Soggetto, Anno e Ufficio
	 *
	 * @param aFascicoloSiep
	 * @return Vettore di FascicoloSiepModel
	 * @throws F3BException
	 */
	public Vector ExRicercaFascicoloSiepBySoggettoPaged(SoggettoModel aSogModel, int aPageNum)
			throws F3BException {

		return ExRicercaFascicoloSiepBySoggettoPaged(aSogModel, aPageNum, "");
	}

	public Vector ExRicercaFascicoloSiepBySoggettoPaged(SoggettoModel aSogModel, int aPageNum,
			String majorOffice) throws F3BException {

		Connection lConn = null;
		Vector lFascicoli = new Vector();
		FascicoloSiepSoggettoSqlDAO lFasSoggDao = null;
		StatoProcedimentoSqlDAO lStaDao = null;

		try {
			lConn = getDBConnection();
			lFasSoggDao = new FascicoloSiepSoggettoSqlDAO(lConn);
			lStaDao = new StatoProcedimentoSqlDAO(lConn);

			if (StringUtils.checkValidValue(majorOffice)) {
				lFasSoggDao.ricercaFascicoloMajorSoggetto(aSogModel, majorOffice);
			} else {
				lFasSoggDao.ricercaFascicoloSoggetto(aSogModel);
			}

			// lFasSoggDao.start();
			lFasSoggDao.startPage1(aPageNum);
			FascicoloSiepModel lFascicolo = null;

			while (lFasSoggDao.next()) {
				lFascicolo = (FascicoloSiepModel) lFasSoggDao.getModel();

				// Ricerca stato Luigi 28-9-2004
				lStaDao.ricercaMaxStatoProcedimentoByFascicoloSiep(lFascicolo.getIdFascicoloSiep());
				lFascicolo.setCodStatoProcedimento(lStaDao.getCodStatoByKey());

				lFascicoli.add(lFascicolo);
			}

			lFasSoggDao.stop();

			if (lFascicoli.isEmpty())
				throw new SIEPException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new SIEPException(F3BException.USER_MESSAGE,
					"FascicoloSiepController.ExRicercaFascicoloSiepBySoggettoPaged: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lFasSoggDao);
			cleanup(lStaDao);
			cleanup(lConn);
		}

		return lFascicoli;
	}

	/**
	 * Ricerca Fascicolo Siep per RGNR
	 *
	 * @param aFascicoloSiep
	 * @return Vettore di FascicoloSiepModel
	 * @throws F3BException
	 */
	public Vector ExRicercaFascicoloSiepByRGNRPaged(SentenzaModel aSentenza, int aPageNum, String majorOffice)
			throws F3BException {

		Connection lConn = null;
		Vector lFascicoli = new Vector();
		FascicoloSiepSoggettoSqlDAO lFasSoggDao = null;

		try {
			lConn = getDBConnection();
			lFasSoggDao = new FascicoloSiepSoggettoSqlDAO(lConn);

			lFasSoggDao.ricercaFascicoloRGNR(aSentenza, majorOffice);
			lFasSoggDao.startPage1(aPageNum);
			FascicoloSiepModel lFascicolo = null;

			while (lFasSoggDao.next()) {
				lFascicolo = (FascicoloSiepModel) lFasSoggDao.getModel();
				lFascicoli.add(lFascicolo);
			}

			lFasSoggDao.stop();

			if (lFascicoli.isEmpty())
				throw new SIEPException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new SIEPException(F3BException.USER_MESSAGE,
					"FascicoloSiepController.ExRicercaFascicoloSiepByRGNRPaged: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lFasSoggDao);
			cleanup(lConn);
		}

		return lFascicoli;
	}

	/**
	 * CONTA Fascicolo Siep per RGNR
	 *
	 * @param aFascicoloSiep
	 * @return Vettore di FascicoloSiepModel
	 * @throws F3BException
	 */
	public BigDecimal ExGetCountFascicoloSiepByRGNRPaged(SentenzaModel aSentenza, String majorOffice)
			throws F3BException {

		Connection lConn = null;
		FascicoloSiepSoggettoSqlDAO lFasSoggDao = null;
		BigDecimal HowManyRecords = null;

		try {
			lConn = getDBConnection();
			lFasSoggDao = new FascicoloSiepSoggettoSqlDAO(lConn);
			lFasSoggDao.ricercaFascicoloRGNR(aSentenza, majorOffice);
			lFasSoggDao.start();
			lFasSoggDao.next();
			HowManyRecords = lFasSoggDao.getNumRowsSelected();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new SIEPException(F3BException.USER_MESSAGE,
					"FascicoloSiepController.ExGetCountFascicoloSiepByRGNRPaged: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lFasSoggDao);
			cleanup(lConn);
		}

		return HowManyRecords;
	}

	@Override
	public Vector ExRicercaFascicoloSiepBySuperSoggettoPaged(SoggettoModel aSogModel, int aPageNum)
			throws F3BException {

		Connection lConn = null;
		Vector lFascicoli = new Vector();
		FascicoloSiepSoggettoSqlDAO lFasSoggDao = null;
		StatoProcedimentoSqlDAO lStaDao = null;

		try {
			lConn = getDBConnection();
			lFasSoggDao = new FascicoloSiepSoggettoSqlDAO(lConn);
			lStaDao = new StatoProcedimentoSqlDAO(lConn);

			lFasSoggDao.ricercaFascicoloSuperSoggetto(aSogModel, "");
			// lFasSoggDao.start();
			lFasSoggDao.startPage1(aPageNum);
			FascicoloSiepModel lFascicolo = null;

			while (lFasSoggDao.next()) {
				lFascicolo = (FascicoloSiepModel) lFasSoggDao.getModel();

				// Ricerca stato Luigi 28-9-2004
				lStaDao.ricercaMaxStatoProcedimentoByFascicoloSiep(lFascicolo.getIdFascicoloSiep());
				lFascicolo.setCodStatoProcedimento(lStaDao.getCodStatoByKey());

				lFascicoli.add(lFascicolo);
			}

			lFasSoggDao.stop();

			if (lFascicoli.isEmpty())
				throw new SIEPException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new SIEPException(F3BException.USER_MESSAGE,
					"FascicoloSiepController.ExRicercaFascicoloSiepBySuperSoggettoPaged: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lFasSoggDao);
			cleanup(lStaDao);
			cleanup(lConn);
		}

		return lFascicoli;
	}

	/**
	 * Aggiunto metodo ricerca soggetti e fascicoli
	 *
	 * @param aSogModel
	 * @param lCodUfficioUtenteConnesso
	 * @return
	 * @throws F3BException
	 */
	/*
	 * public Vector ExRicercaFascicoliBySoggettoPagina(SoggettoModel aSogModel, String
	 * lCodUfficioUtenteConnesso) throws F3BException { Connection lConn = null; Vector lFascicoli = new
	 * Vector();
	 *
	 * SoggettoFascicoloSqlDAO lSoggFasDao = null; SoggettoSqlDAO lSogDao = null; FascicoloSiepModel
	 * lFascicolo = null;
	 *
	 * try { lConn = getDBConnection();
	 *
	 * lSoggFasDao = new SoggettoFascicoloSqlDAO(lConn);
	 * lSoggFasDao.ricercaSoggettiFascicoliBySoggetto(aSogModel, lCodUfficioUtenteConnesso);
	 * lSoggFasDao.start(); while (lSoggFasDao.next()) { lFascicolo = (FascicoloSiepModel)
	 * lSoggFasDao.getSoggettoFascicoliModel(); lFascicoli.add(lFascicolo); } lSoggFasDao.stop();
	 *
	 * if (lFascicoli.isEmpty()) throw new SIEPException(F3BException.USER_MESSAGE,
	 * "Nessun Soggetto individuato con i criteri di ricerca selezionati! "); } catch (DAOException daoEx) {
	 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
	 * rollback(lConn); siesLogger.error("DAOException: " + daoEx); throw new
	 * SIEPException(F3BException.USER_MESSAGE, "FascicoloSiepController.ExRicercaFascicoliBySoggettoPagina: "
	 * + // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog +
	 * daoEx); } catch (SQLException sqe) { rollback(lConn); siesLogger.error("SQLException: " + sqe); throw
	 * new SIEPException(F3BException.USER_MESSAGE,
	 * "FascicoloSiepController.ExRicercaFascicoliBySoggettoPagina: " + sqe); } catch (Exception e) {
	 * rollback(lConn); throw new SIEPException(F3BException.USER_MESSAGE, e.getMessage()); } finally {
	 * cleanup(lSoggFasDao); cleanup(lSogDao); cleanup(lConn); }
	 *
	 * return lFascicoli; }
	 */

	/**
	 * Ricerca Fascicoli da soggetto paginati
	 *
	 * @param aSogModel
	 * @param lCodUfficioUtenteConnesso
	 * @param aPage
	 * @param lCodDistrettoUtenteConnesso
	 * @param TipoRicerca
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaFascicoliBySoggettoPaged(SoggettoModel aSogModel, String lCodUfficioUtenteConnesso,
			int aPage, String lCodDistrettoUtenteConnesso, String TipoRicerca) throws F3BException {

		Connection lConn = null;
		Vector lFascicoli = new Vector();

		SoggettoFascicoloSqlDAO lSoggFasDao = null;
		SoggettoSqlDAO lSogDao = null;
		FascicoloSiepModel lFascicolo = null;

		try {
			lConn = getDBConnection();

			lSoggFasDao = new SoggettoFascicoloSqlDAO(lConn);

			lSoggFasDao.ricercaSoggettiFascicoliBySoggettoPaged(aSogModel, lCodUfficioUtenteConnesso, aPage,
					lCodDistrettoUtenteConnesso, TipoRicerca);
			lSoggFasDao.start();
			while (lSoggFasDao.next()) {
				lFascicolo = (FascicoloSiepModel) lSoggFasDao.getSoggettoFascicoliModel();
				lFascicoli.add(lFascicolo);
			}

			lSoggFasDao.stop();

			if (lFascicoli.isEmpty())
				throw new SIEPException(F3BException.USER_MESSAGE,
						"Nessun Soggetto individuato con i criteri di ricerca selezionati! ");
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new SIEPException(F3BException.USER_MESSAGE,
					"FascicoloSiepController.ExRicercaFascicoliBySoggettoPaged: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("Exception: " + e);
			throw new SIEPException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lSoggFasDao);
			cleanup(lSogDao);
			cleanup(lConn);
		}

		return lFascicoli;
	}

	/**
	 * Ritorna n.ro di record risultato di una ExRicercaFascicoloSiepBySoggetto.
	 *
	 * MEV_57: aggiunto parametro di passaggio
	 *
	 * @param SoggettoModel
	 * @return BigDecimal n.ro di record
	 * @throws F3BException
	 */
	public BigDecimal ExGetNumFascicoloSiepBySoggetto(SoggettoModel aSogModel, String majorOffice)
			throws F3BException {

		Connection lConn = null;
		FascicoloSiepSoggettoSqlDAO lFasSoggDao = null;
		BigDecimal lCont = new BigDecimal(0);

		try {
			lConn = getDBConnection();

			lFasSoggDao = new FascicoloSiepSoggettoSqlDAO(lConn);
			// MEV_57: aggiunto parametro di passaggio
			if (StringUtils.checkValidValue(majorOffice)) {
				lFasSoggDao.ricercaFascicoloMajorSoggetto(aSogModel, majorOffice);
			} else {
				lFasSoggDao.ricercaFascicoloSoggetto(aSogModel);
			}
			lCont = lFasSoggDao.getNumRowsSelected();

		} catch (DAOException daoEx) {
			throw new SIEPException(F3BException.USER_MESSAGE,
					"FascicoloSiepController.ExGetNumFascicoloSiepBySoggetto: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lFasSoggDao);
			cleanup(lConn);
		}
		return lCont;
	}

	public BigDecimal ExGetNumFascicoloSiepBySuperSoggetto(SoggettoModel aSogModel) throws F3BException {

		Connection lConn = null;
		FascicoloSiepSoggettoSqlDAO lFasSoggDao = null;
		BigDecimal lCont = new BigDecimal(0);

		try {
			lConn = getDBConnection();

			lFasSoggDao = new FascicoloSiepSoggettoSqlDAO(lConn);

			// Modifica del 21/12/2016 MEV_15_S4
			// Come richiesto da Michele, e confermato da Vito,
			// la ricerca per Soggetto viene ristretta ai soli campi
			// Cognome, Nome,
			// lFasSoggDao.ricercaFascicoloSuperSoggetto(aSogModel, "");
			lFasSoggDao.ricercaFascicoloSuperSoggettoPerSige(aSogModel);
			lCont = lFasSoggDao.getNumRowsSelected();

		} catch (DAOException daoEx) {
			throw new SIEPException(F3BException.USER_MESSAGE,
					"FascicoloSiepController.ExGetNumFascicoloSiepBySuperSoggetto: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lFasSoggDao);
			cleanup(lConn);
		}
		return lCont;
	}

	/**
	 * Ricerca il Fascicolo in Banca Dati per primary key
	 *
	 * @param aModel
	 * @return FascicoloSiepModel
	 * @throws F3BException
	 */
	public FascicoloSiepModel ExRicercaFascicoloByKey(BigDecimal aKey) throws F3BException {

		Connection lConn = null;

		FascicoloSiepModel lFascicolo = null;
		SoggettoModel lSoggMod = null;
		SentenzaModel lSentMod = null;
		FascicoloSiepSqlDAO lFascDao = null;
		SoggettoSqlDAO lSoggDao = null;
		SentenzaSqlDAO lSentDao = null;

		try {
			lConn = getDBConnection();

			lFascDao = new FascicoloSiepSqlDAO(lConn);
			lSoggDao = new SoggettoSqlDAO(lConn);
			lSentDao = new SentenzaSqlDAO(lConn);

			// Cerca il fascicolo by key fascicolo
			lFascDao.ricercaFascicoloByKey(aKey);
			lFascicolo = (FascicoloSiepModel) lFascDao.getModelByKey();

			if (lFascicolo == null)
				throw new SIEPException(F3BException.USER_MESSAGE, "Fascicolo Siep non trovato");

			// Cerca il soggetto associato al fascicolo
			lSoggDao.ricercaSoggettoByKey(lFascicolo.getSogIdSoggetto());
			lSoggMod = (SoggettoModel) lSoggDao.getModelByKey();

			if (lSoggMod == null)
				throw new SIEPException(F3BException.USER_MESSAGE, "Soggetto associato non trovato");

			lFascicolo.setSoggetto(lSoggMod);

			// Cerca la sentenza associata al fascicolo
			lSentDao.ricercaSentenzaBykey(lFascicolo.getSenIdSentenza());
			lSentMod = (SentenzaModel) lSentDao.getModelByKey();

			if (lSentMod == null)
				throw new SIEPException(F3BException.USER_MESSAGE, "Sentenza associata non trovata");

			lFascicolo.setSentenza(lSentMod);
		} catch (DAOException dex) {
			throw new SIEPException("FascicoloSiepController.ExRicercaFascicoloByKey: " + dex);
		} finally {
			cleanup(lFascDao);
			cleanup(lSoggDao);
			cleanup(lSentDao);

			cleanup(lConn);
		}

		return lFascicolo;
	}

	/**
	 * Ricerca il Fascicolo in Banca Dati per primary key
	 *
	 * @param aModel
	 * @return FascicoloSiepModel
	 * @throws F3BException
	 */
	public FascicoloSiepModel ExRicercaFascicoloByKeyNoError(BigDecimal aKey) throws F3BException {

		Connection lConn = null;

		FascicoloSiepModel lFascicolo = null;
		SoggettoModel lSoggMod = null;
		SentenzaModel lSentMod = null;

		FascicoloSiepSqlDAO lFascDao = null;
		SoggettoSqlDAO lSoggDao = null;
		SentenzaSqlDAO lSentDao = null;

		try {
			lConn = getDBConnection();

			lFascDao = new FascicoloSiepSqlDAO(lConn);
			lSoggDao = new SoggettoSqlDAO(lConn);
			lSentDao = new SentenzaSqlDAO(lConn);

			// Cerca il fascicolo by key fascicolo
			lFascDao.ricercaFascicoloByKey(aKey);
			lFascicolo = (FascicoloSiepModel) lFascDao.getModelByKey();

			if (lFascicolo != null) {
				// Cerca il soggetto associato al fascicolo
				lSoggDao.ricercaSoggettoByKey(lFascicolo.getSogIdSoggetto());
				lSoggMod = (SoggettoModel) lSoggDao.getModelByKey();

				if (lSoggMod != null)
					lFascicolo.setSoggetto(lSoggMod);

				// Cerca la sentenza associata al fascicolo
				lSentDao.ricercaSentenzaBykey(lFascicolo.getSenIdSentenza());
				lSentMod = (SentenzaModel) lSentDao.getModelByKey();

				if (lSentMod != null)
					lFascicolo.setSentenza(lSentMod);
			}
		} catch (DAOException dex) {
			throw new SIEPException("FascicoloSiepController.ExRicercaFascicoloByKeyNoError: " + dex);
		} finally {
			cleanup(lFascDao);
			cleanup(lSoggDao);
			cleanup(lSentDao);
			cleanup(lConn);
		}

		return lFascicolo;
	}

	/**
	 * Ricerca Fascicolo By Reato Paged
	 *
	 * @param aModel
	 * @param aPage
	 * @return
	 * @throws F3BException
	 */
	// MEV Agosto 2014 - Aggiunto criterio di Ricerca - Cumulati
	// public Vector ExRicercaFascicoloByReatoPaged(ReatoModel aModel,int aPage)
	// throws F3BException
	public Vector ExRicercaFascicoloByReatoPaged(ReatoModel aModel, CircostanzaModel aModAggrava,
			String aQualeRic, Boolean solocumulati, int aPage) throws F3BException {

		Connection lConn = null;

		Vector fascicoli = new Vector();
		FascicoloReatoSqlDAO lFascReDao = null;

		try {
			lConn = getDBConnection();

			lFascReDao = new FascicoloReatoSqlDAO(lConn);

			// Cerca il fascicolo by Reato
			if (aQualeRic.compareTo("R") == 0)
				lFascReDao.ricercaFascicoloReatoPaged(aModel, solocumulati, aPage);
			else if (aQualeRic.compareTo("A") == 0)
				lFascReDao.ricercaFascicoloCircAggravaPaged(aModel, aModAggrava, solocumulati, aPage);
			else if (aQualeRic.compareTo("RA") == 0)
				lFascReDao.ricercaFascicoloReatoCircostanzeAggravaPaged(aModel, aModAggrava, solocumulati,
						aPage);
			else
				throw new SIEPException(F3BException.USER_MESSAGE, "Nessun Criterio di Ricerca");
			fascicoli = new Vector(lFascReDao.getModels());

			if (fascicoli == null || fascicoli.size() == 0)
				throw new SIEPException(F3BException.USER_MESSAGE, "Nessun Procedimento trovato");

		} catch (DAOException dex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("DAOException: " + dex);
			throw new SIEPException("FascicoloSiepController.ExRicercaFascicoloByReatoPaged: " + dex);
		} finally {
			cleanup(lFascReDao);

			cleanup(lConn);
		}

		return fascicoli;
	}

	public BigDecimal ExRicercaIDFascicoloSiepByProgrAnnoCodUfficio(FascicoloSiepModel aFascicoloSiep)
			throws F3BException {

		Connection lConn = null;

		FascicoloSiepSqlDAO lFasDao = null;
		BigDecimal idFascicolo = null;

		try {
			lConn = getDBConnection();

			lFasDao = new FascicoloSiepSqlDAO(lConn);

			lFasDao.ricercaIDFascicoloByProgrAnnoCodUfficio(aFascicoloSiep);
			lFasDao.start();

			if (lFasDao.next()) {
				idFascicolo = lFasDao.getBigDecimal("ID_FASCICOLO_SIEP");
			}

			lFasDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new SIEPException(F3BException.USER_MESSAGE,
					"FascicoloSiepController.ExRicercaIDFascicoloSiep: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lFasDao);
			cleanup(lConn);
		}

		return idFascicolo;
	}

	/**
	 * Count Reati
	 *
	 * @param aReato
	 * @return
	 * @throws F3BException
	 */
	public BigDecimal ExgetCountReati(ReatoModel aReato, Boolean solocumulati) throws F3BException {

		BigDecimal lCount = new BigDecimal(0);
		Connection lConn = null;

		ReatoSqlDAO lSqlDao = null;
		try {
			lConn = getDBConnection();
			lSqlDao = new ReatoSqlDAO(lConn);
			lSqlDao.getCountReati(aReato, solocumulati);
			lSqlDao.start();
			lSqlDao.next();
			lCount = lSqlDao.getBigDecimal("HowManyRecords");
			lSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error(daoEx.getLocalizedMessage());
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"ReatoController.ExgetCountReati: Non posso leggere i reati : " + daoEx);
		} finally {
			cleanup(lSqlDao);
			cleanup(lConn);
		}
		return lCount;
	}

	/**
	 * Count Reati e/o Circostanza Aggravante in 'Ricerca Procedimento X Reato'
	 *
	 * @param aCircostanza
	 * @return
	 * @throws F3BException
	 */
	public BigDecimal ExgetCountCircostanzeAggr(ReatoModel aReato, CircostanzaModel aCirco,
			Boolean solocumulati) throws F3BException {
		BigDecimal lCount = new BigDecimal(0);
		Connection lConn = null;

		CircostanzaSqlDAO lSqlDao = null;
		try {
			lConn = getDBConnection();
			lSqlDao = new CircostanzaSqlDAO(lConn);
			lSqlDao.getCountCircostanze(aCirco, aReato, solocumulati);
			lSqlDao.start();
			lSqlDao.next();
			lCount = lSqlDao.getBigDecimal("HowManyRecords");
			lSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error(daoEx.getLocalizedMessage());
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"ReatoController.ExgetCountCircostanzeAggr: Non posso leggere i reati : " + daoEx);
		} finally {
			cleanup(lSqlDao);
			cleanup(lConn);
		}
		return lCount;
	}

	public BigDecimal ExgetCountReatiCircostanzeAggr(ReatoModel aReato, CircostanzaModel aCirco,
			Boolean solocumulati) throws F3BException {
		BigDecimal lCount = new BigDecimal(0);
		Connection lConn = null;

		CircostanzaSqlDAO lSqlDao = null;
		try {
			lConn = getDBConnection();
			lSqlDao = new CircostanzaSqlDAO(lConn);
			lSqlDao.getCountReaCircos(aCirco, aReato, solocumulati);
			lSqlDao.start();
			lSqlDao.next();
			lCount = lSqlDao.getBigDecimal("HowManyRecords");
			lSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error(daoEx.getLocalizedMessage());
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"ReatoController.ExgetCountReatiCircostanzeAggr: Non posso leggere i reati : " + daoEx);
		} finally {
			cleanup(lSqlDao);
			cleanup(lConn);
		}
		return lCount;
	}

	/**
	 * Ricerca il Fascicolo in Banca Dati per primary key. Vengono recuperati i dati per: - residuenza -
	 * domicilio - posidzione giuridica - avvocati - reati e circostanze - circostanze - pena complessiva e
	 * Snazione Sostitutiva - pena accessorie - benefici - pena residua (ultima validata) - pena presunta -
	 * posizione materiale - Magistrato Competente - Misura Alternativa - Stato Procedimento - Eventi () -
	 * Misure Cautelari - Misure Sicurezza - liberazione Anticipata (concessa)
	 *
	 * @param aModel
	 * @return FascicoloSiepModel
	 * @throws F3BException
	 */
	public DettaglioFascicoloModel ExDettaglioFascicoloSiep(BigDecimal aIdFascicolo) throws F3BException {
		Connection lConn = null;

		DettaglioFascicoloModel lDettaglio = null;

		ResidenzaSqlDAO lResDao = null;
		AvvocatoSqlDAO lAvvDao = null;
		AvvocatoFascicoloSiepSqlDAO lAvvFasSieSqlDao = null;
		PenaAccessoriaSqlDAO lPenAccDao = null;
		MisuraCautelareSqlDAO lMisCauDao = null;
		BeneficioSqlDAO lBeneDao = null;
		CircostanzaSqlDAO lCircDao = null;
		PenaResiduaDAO lPenResDao = null;
		EventoDAO lEveDao = null;
		PenaPresuntaSqlDAO lPenPresDAO = null;
		StatoProcedimentoSqlDAO lStatProcDAO = null;
		MisuraSicurezzaSqlDAO lMisSicuDAO = null;
		MagistratoCompetenteMagistratoSqlDAO lMagDAO = null;
		LicenzaLibanticipataSqlDAO lLicSqlDao = null;
		PosizioneMaterialeFascModel lPosizioneMateriale = null;
		PosizioneMaterialeFascSqlDAO lPosMatDAO = null;
		MisuraAlternativaSqlDAO lMisDao = null;
		StatoProcedimentoSqlDAO lStaDao = null;
		ScambioSanzioneSqlDAO lScambioDao = null;
		// Note Fascicolo
		NoteFascicoloSqlDAO lNotFasDao = null;
		RichiestaConversioneSqlDAO lRCSqlDAO = null; // 13-03-2009
		NuovaIstanzaSqlDAO lNISqlDAO = null; // 30/08/2010
		AgdgFascicoloSiepSqlDAO lAGDGFasSiepSqlDAO = null; // 01/09/2010
		AltriGradiGiudizioSqlDAO lAGDGSqlDAO = null; // 01/09/2010

		PenaRideterminataCumuloSqlDAO lPenaRidetCumuloModSqlDao = null;
		NuovaIstanzaSqlDAO lNuovaIstanzaSqlDao = null;
		try {
			lConn = getDBConnection();

			lScambioDao = new ScambioSanzioneSqlDAO(lConn);
			lResDao = new ResidenzaSqlDAO(lConn);
			lPenPresDAO = new PenaPresuntaSqlDAO(lConn);
			lAvvDao = new AvvocatoSqlDAO(lConn);
			lAvvFasSieSqlDao = new AvvocatoFascicoloSiepSqlDAO(lConn);
			lPenAccDao = new PenaAccessoriaSqlDAO(lConn);
			lMisCauDao = new MisuraCautelareSqlDAO(lConn);
			lBeneDao = new BeneficioSqlDAO(lConn);
			lPenResDao = new PenaResiduaDAO(lConn);
			lEveDao = new EventoDAO(lConn);
			lStatProcDAO = new StatoProcedimentoSqlDAO(lConn);
			lMisSicuDAO = new MisuraSicurezzaSqlDAO(lConn);
			lMagDAO = new MagistratoCompetenteMagistratoSqlDAO(lConn);
			lPosMatDAO = new PosizioneMaterialeFascSqlDAO(lConn);
			lRCSqlDAO = new RichiestaConversioneSqlDAO(lConn);
			lNISqlDAO = new NuovaIstanzaSqlDAO(lConn); // 30/08/2010
			lAGDGFasSiepSqlDAO = new AgdgFascicoloSiepSqlDAO(lConn); // 01/09/2010
			lAGDGSqlDAO = new AltriGradiGiudizioSqlDAO(lConn); // 01/09/2010

			lDettaglio = new DettaglioFascicoloModel();
			// Dati del fascicolo
			lDettaglio.setFascicoloSiep(ExRicercaFascicoloByKey(aIdFascicolo));

			// Residenza (al massimo 1)
			lResDao.ricercaResidenzaByFascicolo(aIdFascicolo);
			lDettaglio.setResidenza((ResidenzaModel) lResDao.getModelByKey());

			// Domicilio (al massimo 1)
			lResDao.ricercaDomicilioByFascicolo(aIdFascicolo);
			lDettaglio.setDomicilio((ResidenzaModel) lResDao.getModelByKey());

			// Posizione Giuridica (l'ultima + Luogo Detenzione + Altra Causa)
			IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
			// PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPosLuoAltMod =
			// lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(aIdFascicolo);
			PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPosLuoAltMod = lPosCtrl
					.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaMisuraCautelareCorrentiByIdFascicolo(
							aIdFascicolo);

			lDettaglio.setPosizioneGiuridica(lPosLuoAltMod.getPosizioneGiuridica());
			lDettaglio.setLuogoDetenzione(lPosLuoAltMod.getLuogoDetenzione());
			lDettaglio.setAltraCausa(lPosLuoAltMod.getAltraCausa());

			// Avvocati (n)
			AvvocatoModel lAvvMod = new AvvocatoModel();
			AvvocatoFascicoloSiepModel lAvvFascMod = new AvvocatoFascicoloSiepModel();
			AvvocatoSiepModel lAvvSiepModel = new AvvocatoSiepModel();
			lAvvFascMod.setFasSieIdFascicoloSiep(aIdFascicolo);
			lAvvDao.ricercaAvvocatoAttualeFascicolo(lAvvMod, lAvvFascMod);
			lDettaglio.setAvvocati(new ArrayList(lAvvDao.getModels()));

			// AvvocatiSIEP.
			ArrayList lArrayAvvFascSiep = new ArrayList();
			for (int i = 0; i < lDettaglio.getAvvocati().size(); i++) {
				AvvocatoModel lAvv = (AvvocatoModel) lDettaglio.getAvvocati().get(i);
				lAvvFasSieSqlDao = new AvvocatoFascicoloSiepSqlDAO(lConn);

				lAvvFasSieSqlDao.ricercaAvvocatoFascicoloSiepByIdAvvocatoIdFascicolo(lAvv.getIdAvvocato(),
						lDettaglio.getFascicoloSiep().getIdFascicoloSiep());
				lAvvSiepModel = (AvvocatoSiepModel) lAvvFasSieSqlDao.getModelByKey();

				if (lAvvSiepModel != null && lAvvSiepModel.getAvvocatoFascicoloSiepModel() != null)
					lArrayAvvFascSiep.add(lAvvSiepModel.getAvvocatoFascicoloSiepModel());
			}
			lDettaglio.setAvvocatiSIEP(lArrayAvvFascSiep);

			// Reati e Circostanze dei reati (n)
			IReato lReaCtrl = SIEPLookupRemote.getReatoRemote();
			Vector lReati = lReaCtrl.ExRicercaReatoCircostanzaByFascicolo(aIdFascicolo);
			lDettaglio.setReatiCircostanze(lReati);

			// Circostanze (n)
			ICircostanza lCirCtr = SIEPLookupRemote.getCircostanzaRemote();
			Vector lCircostanze = lCirCtr.ExRicercaCircostanzaDescByIdFascicolo(aIdFascicolo);
			lDettaglio.setCircostanze(lCircostanze);

			// Pena Complessiva e Sanzione Sostitutiva (al massimo 1 e al
			// massimo 1)
			IPenaComplessiva lPenComCtr = SIEPLookupRemote.getPenaComplessivaRemote();
			PenaComplessivaSanzioneSostitutivaModel lPenCompSanzSost = lPenComCtr
					.ExRicercaPenaComplessivaSanzioneSostitutivaByIdFascicoloSiep(aIdFascicolo);
			lDettaglio.setPenaComplessivaSanzioneSostitutiva(lPenCompSanzSost);

			// Pene Accessorie (n)
			lPenAccDao.ricercaPenaAccessoriaByFascicolo(aIdFascicolo);
			lDettaglio.setPeneAccessorie(new ArrayList(lPenAccDao.getModels()));

			// Benefici (n)
			BeneficioModel lBeneMod = new BeneficioModel();
			lBeneMod.setFasSieIdFascicoloSiep(aIdFascicolo);
			lBeneDao.ricercaBeneficio(lBeneMod);
			lDettaglio.setBenefici(new ArrayList(lBeneDao.getModels()));

			// Pena Residua
			IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
			PenaResiduaModel lPenResMod = lPenResCtrl.ExRicercaPenaResiduaUltimaByDate(aIdFascicolo);
			lDettaglio.setPenaResidua(lPenResMod);

			// Pena Presunta
			IPenaPresunta lPenPresCtrl = SIEPLookupRemote.getPenaPresuntaRemote();
			PenaPresuntaModel lPenPresMod = lPenPresCtrl
					.ExRicercaPenaPresuntaCorrenteByFascicoloSiep(aIdFascicolo);
			lDettaglio.setPenaPresunta(lPenPresMod);

			// Pena In Cumulo
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Pena In Cumulo: "+lDettaglio.getFascicoloSiep().getFlagCumulante());
			if (lDettaglio.getFascicoloSiep().getFlagCumulante() != null
					&& lDettaglio.getFascicoloSiep().getFlagCumulante().equals("S")) {
				// Recupero l'ultima pena legata a un cumulo (se esiste)
				IPenaCumulo lCtrlPenaCumulo = SIEPLookupRemote.getPenaCumuloRemote();
				PenaCumuloModel lPenaCumulo = lCtrlPenaCumulo
						.ExRicercaUltimaPenaCumuloByIdFascicolo(aIdFascicolo);
				lDettaglio.setPenaCumulo(lPenaCumulo);

				// MEV26 - Provo a recuperare anche da PENA_RIDETERMINATA_CUMULO
				// se
				// nuovo cumulo
				lPenaRidetCumuloModSqlDao = new PenaRideterminataCumuloSqlDAO(lConn);
				lPenaRidetCumuloModSqlDao.ricercaPeneRideterminateCumuloByIdFascDataInsDesc(aIdFascicolo);
				PenaRideterminataCumuloModel lPenRidetCumuloNew = (PenaRideterminataCumuloModel) lPenaRidetCumuloModSqlDao
						.getModelByKey();

				if (lPenRidetCumuloNew != null) {
					lDettaglio.setPenaCumuloNew(lPenRidetCumuloNew);
				}

				lPenaRidetCumuloModSqlDao.stop();
			}

			// Posizione Materiale
			lPosizioneMateriale = new PosizioneMaterialeFascModel();
			lPosMatDAO.ricercaPosizioneMaterialeFascAttivaXFas(aIdFascicolo);
			lPosizioneMateriale = (PosizioneMaterialeFascModel) lPosMatDAO.getModelByKey();
			if (lPosizioneMateriale != null)
				lDettaglio.setPosizioneMateriale(lPosizioneMateriale);

			// Magistrato Competente
			lMagDAO.ricercaMagistratoCompetenteByFascicolo(aIdFascicolo);
			MagistratoCompetenteMagistratoModel lMagComMod = (MagistratoCompetenteMagistratoModel) lMagDAO
					.getModelByKey();
			lDettaglio.setMagistratoCompetente(lMagComMod);

			// Misura Alternativa
			lMisDao = new MisuraAlternativaSqlDAO(lConn);
			lMisDao.ricercaMisuraAlternativaByIdFascicolo(aIdFascicolo);
			MisuraAlternativaModel lMisMod = (MisuraAlternativaModel) lMisDao.getModelByKey();

			/*
			 * IMisuraAlternativa lMisCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
			 * MisuraAlternativaModel lMisMod = lMisCtrl.ExRicercaMisuraAlternativaCorrenteByIdFascicolo
			 * (aIdFascicolo);
			 */
			lDettaglio.setMisuraAlternativa(lMisMod);

			// scambio sanzioni sostitutive
			// 18/02/2016 Inizio
			// lScambioDao.ricercaByIdFascicolo(aIdFascicolo);
			String[] aTipoDecisione = null;
			// 14/03/2016 Inizio
			// String[] aNaturaSanzione = {"0156","0157"};
			// String[] aTipoSanzione = {"2470",};
			String[] aNaturaSanzione = { "0156", "0157", "0159" };
			String[] aTipoSanzione = { "2470", "2471" };
			// 14/03/2016 Fine
			lScambioDao.ricercaByIdFascicoloNaturaTipo(aIdFascicolo, aTipoDecisione, aNaturaSanzione,
					aTipoSanzione);
			// 18/02/2016 Fine
			ScambioSanzioneModel lScaSanMod = (ScambioSanzioneModel) lScambioDao.getModelByKey();
			lDettaglio.setScambioSanzione(lScaSanMod);

			// 30/08/2010 Nuova Istanza.
			DatiSiepPerTrasferimentoModel lDatiSiep = new DatiSiepPerTrasferimentoModel();
			lNISqlDAO.ricercaNuovaIstanzaByIdFascicolo(aIdFascicolo);
			Vector lNuovaIstanza = new Vector(lNISqlDAO.getModels());
			if (lNuovaIstanza != null && lNuovaIstanza.size() > 0)
				lDatiSiep.setListNuovaIstanza(lNuovaIstanza);

			// 01/09/2010 AGDG Fascicolo Siep.
			// AgdgFascicoloSiepModel lAGDGFasSiepModel = new
			// AgdgFascicoloSiepModel();
			lAGDGFasSiepSqlDAO.ricercaAgdgFascicoloSiepByIdFasSiep(aIdFascicolo);
			Vector lAltriGradiGiudizio = new Vector(lAGDGFasSiepSqlDAO.getModels());
			if (lAltriGradiGiudizio != null && lAltriGradiGiudizio.size() > 0)
				lDatiSiep.setListAltriGradiGiudizio(lAltriGradiGiudizio);

			// 01/09/2010 Altri Gradi Di Giudizio.
			// Vector lAltriGradiGiudizio = new Vector();
			// AltriGradiGiudizioModel lAGDGModel = new
			// AltriGradiGiudizioModel();
			// if (lAGDGFasSiep != null && lAGDGFasSiep.size() > 0 )
			// for(int k=0; k<lAGDGFasSiep.size(); k++)
			// {
			// lAGDGFasSiepModel = (AgdgFascicoloSiepModel)lAGDGFasSiep.get(k);
			// lAGDGSqlDAO.ricercaAltriGradiGiudizioByKey(lAGDGFasSiepModel.getAgdgIdAltrigradigiudizio());
			// lAGDGModel = (AltriGradiGiudizioModel) lAGDGSqlDAO.getModel();
			// lAltriGradiGiudizio.add(lAGDGModel);
			// }
			// if (lAltriGradiGiudizio != null && lAltriGradiGiudizio.size() > 0
			// )
			// lDatiSiep.setListAltriGradiGiudizio(lAltriGradiGiudizio);

			// 13-03-2009 Richiesta conversione Pene Pecuniarie.
			lRCSqlDAO.ricercaRichiestaConversioneByIdFasSIEP(aIdFascicolo);
			Vector lRicConversioni = new Vector(lRCSqlDAO.getModels());
			if (lRicConversioni != null && lRicConversioni.size() > 0)
				lDatiSiep.setListRichiesteConversioniPP(lRicConversioni);

			if (lDatiSiep != null)
				lDettaglio.setDatiSiepPerTrasferimento(lDatiSiep);

			// Stato Procedimento
			/*
			 * IStatoProcedimento lStatoProcCtrl = SIEPLookupRemote.getStatoProcedimentoRemote(); Vector
			 * lStatPrcVec = lStatoProcCtrl.ExRicercaStatoProcedimentoByFascicoloSiep(aIdFascicolo);
			 */
			lStaDao = new StatoProcedimentoSqlDAO(lConn);
			lStaDao.ricercaStatoProcedimentoByFascicoloSiep(aIdFascicolo);
			Vector lStatPrcVec = new Vector(lStaDao.getModels());

			lDettaglio.setStatoProcedimento(lStatPrcVec);

			// Eventi
			// IOrdineEsecuzione lOrdCtrl =
			// SIEPLookupRemote.getOrdineEsecuzioneRemote();
			IEventoSimeone lCtrl = SICOLookupRemote.getEventoSimeoneRemote();
			try {
				String[] lTipoEvento = { "01", "02", "03", "14" };
				String[] lTipoProv = { "02", "03" };
				Vector lEve = lCtrl.ExRicercaEventoNotificaByFascicoloSiepTipEventoNOTTipProvNONAnnullati(
						aIdFascicolo, lTipoEvento, lTipoProv);

				// ==============================================================================
				// ULTIMI EVENTI modifica per prendere il corretto contenuto
				// della nuova istanza
				// ================================================================================
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di
				// istanza siesLogger al posto di
				// mLog
				// siesLogger.debug( "<------ ultimi eventi ----->" );
				lNuovaIstanzaSqlDao = new NuovaIstanzaSqlDAO(lConn);
				NuovaIstanzaModel lNuovaIstanzaMod = new NuovaIstanzaModel();
				for (int i = 0; i < Math.min(lEve.size(), 2); i++) {
					EventoNotificaModel lEveNotificaMod = (EventoNotificaModel) lEve.get(i);
					EventoModel lEveMod = lEveNotificaMod.getEvento();
					// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di
					// istanza siesLogger al posto
					// di mLog
					// siesLogger.debug( "evento n = "+i );
					// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di
					// istanza siesLogger al posto
					// di mLog
					// siesLogger.debug(
					// "lEveMod.getCodMotivo() = "+lEveMod.getCodMotivo() );

					if (lEveMod != null && lEveMod.getCodMotivo() != null
							&& lEveMod.getCodMotivo().equals("0993")) {
						// mlog.debug( "evento id = "+lEveMod.getIdEvento());
						lNuovaIstanzaSqlDao.ricercaNuovaIstanzaByEveIdEvento(lEveMod.getIdEvento());
						lNuovaIstanzaMod = (NuovaIstanzaModel) lNuovaIstanzaSqlDao.getModelByKey();
						// 20170921: [SG] aggiunto controllo di consistenza
						if (lNuovaIstanzaMod != null && lNuovaIstanzaMod.getDescrContenuto() != null)
							lEveMod.setDescrMotivo(lNuovaIstanzaMod.getDescrContenuto());
					}
				}

				lDettaglio.setEventi(lEve);
			} catch (F3BException e) {
			}

			// Misure Cautelari (n)
			lMisCauDao.ricercaMisuraCautelareByFascicolo(aIdFascicolo);
			lDettaglio.setMisureCautelari(new ArrayList(lMisCauDao.getModels()));

			// Misure Sicurezza (n)
			lMisSicuDAO.ricercaMisuraSicurezzaByIdFascicoloOrd(aIdFascicolo);
			lDettaglio.setMisureSicurezza(new ArrayList(lMisSicuDAO.getModels()));

			// ========================================================================
			// Vengono recuperati i GG di LA concessi (presi in carico e
			// associati a
			// un evento SIES VALIDATI) già detratti o da detrarre
			// Nuova gestione LA (12/2006)
			// ========================================================================
			CalcoloPenaControllerF5 lCtrlF5 = new CalcoloPenaControllerF5();
			CalcoloPenaModel lCalcPenaModel = lCtrlF5.exGetPenaIniziale(aIdFascicolo, null);
			Vector lListaLA = lCtrlF5.exGetLiberazioneAnticipata(aIdFascicolo, lCalcPenaModel.getDataDal(),
					null);
			lCalcPenaModel.setLibAnticipate(lListaLA);

			lDettaglio.setCalcoloPenaModel(lCalcPenaModel);

			// 20/05/2014 Nuova L.A. - differenziare i giorni da concedere di
			// L.A. , L.A. Speciale , L.A.
			// Integrazione
			// - differenziare i giorni già concessi di L.A. , L.A. Speciale ,
			// L.A. Integrazione

			int totGiorniLAConcessi = lCalcPenaModel.getLiberazioneAnticipataGiaConcesse();
			int totGiorniLADaConcedere = lCalcPenaModel.getLiberazioneAnticipataDaConcedere();
			lDettaglio.setGiorniLibConcessa(new BigDecimal(totGiorniLAConcessi));
			lDettaglio.setGiorniLibNonConcessa(new BigDecimal(totGiorniLADaConcedere));

			// gg da concedere
			int totGiorniDaConcedereLA = lCalcPenaModel.getLiberazioneAnticipataDaConcedereLA();
			lDettaglio.setGiorniLibNONConcessaLA(new BigDecimal(totGiorniDaConcedereLA));
			int totGiorniDaConcedereLS = lCalcPenaModel.getLiberazioneAnticipataDaConcedereLS();
			lDettaglio.setGiorniLibNONConcessaLS(new BigDecimal(totGiorniDaConcedereLS));
			int totGiorniDaConcedereLI = lCalcPenaModel.getLiberazioneAnticipataDaConcedereLI();
			lDettaglio.setGiorniLibNONConcessaLI(new BigDecimal(totGiorniDaConcedereLI));

			// gg già concessi
			int totGiorniConcessiLA = lCalcPenaModel.getLiberazioneAnticipataGiaConcesseLA();
			lDettaglio.setGiorniLibConcessaLA(new BigDecimal(totGiorniConcessiLA));
			int totGiorniConcessiLS = lCalcPenaModel.getLiberazioneAnticipataGiaConcesseLS();
			lDettaglio.setGiorniLibConcessaLS(new BigDecimal(totGiorniConcessiLS));
			int totGiorniConcessiLI = lCalcPenaModel.getLiberazioneAnticipataGiaConcesseLI();
			lDettaglio.setGiorniLibConcessaLI(new BigDecimal(totGiorniConcessiLI));

			// End Nuova L.A.
			// ========================================================================
			// DL 92/2014
			// ========================================================================
			int totGiorniDL92Detratti = lCalcPenaModel.getRimediRisarcitoriGiaConcessi();
			lDettaglio.setGiorniDL92Detratti(new BigDecimal(totGiorniDL92Detratti));

			int totGiorniDL92NONDetratti = lCalcPenaModel.getRimediRisarcitoriDaConcedere();
			lDettaglio.setGiorniDL92NONDetratti(new BigDecimal(totGiorniDL92NONDetratti));
			// ========================================================================
			// Recupero i dati del differimento se presenti
			// ========================================================================
			lDettaglio = ricercaProvDifferimento(lDettaglio, aIdFascicolo);

			// Aggiunte le note dispositivo
			lNotFasDao = new NoteFascicoloSqlDAO(lConn);
			lNotFasDao.ricercaNoteFascicoloByKey(aIdFascicolo);
			NoteFascicoloModel lNoteMod = (NoteFascicoloModel) lNotFasDao.getModelByKey();
			if (lNoteMod != null && lNoteMod.getNotaDispositivo() != null
					&& lNoteMod.getNotaDispositivo().length() > 1) {
				lDettaglio.setNoteFascicolo(lNoteMod.getNotaDispositivo());
				// mlog.info(lDettaglio.getNoteFascicolo() + " --- " +
				// lNoteMod.getNotaDispositivo());
			}

		} catch (F3BException fex) {
			if (fex.getErrorCode() == F3BException.USER_MESSAGE) {
				throw fex;
			} else {
				throw new F3BException("FascicoloSiepController.ExDettaglioFascicoloSiep: " + fex);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("Errore in FascicoloSiepController.ExDettaglioFascicoloSiep", ex);
			throw new SIEPException("FascicoloSiepController.ExDettaglioFascicoloSiep: " + ex);
		} finally {
			cleanup(lResDao);
			cleanup(lAvvDao);
			cleanup(lPenAccDao);
			cleanup(lMisCauDao);
			cleanup(lBeneDao);
			cleanup(lCircDao);
			cleanup(lPenResDao);
			cleanup(lEveDao);
			cleanup(lPenPresDAO);
			cleanup(lStatProcDAO);
			cleanup(lMisSicuDAO);
			cleanup(lMagDAO);
			cleanup(lLicSqlDao);
			cleanup(lPosMatDAO);
			cleanup(lNotFasDao);
			cleanup(lMisDao);
			cleanup(lStaDao);
			cleanup(lScambioDao);
			cleanup(lRCSqlDAO);
			cleanup(lNuovaIstanzaSqlDao);
			cleanup(lAGDGFasSiepSqlDAO);
			cleanup(lAGDGSqlDAO);
			cleanup(lPenaRidetCumuloModSqlDao);

			cleanup(lConn);
		}

		return lDettaglio;
	}

	/**
	 * Recupera altri dati afferenti al FASCICOLO_SIEP (CUMULO, PENA_CUMULO, ULTERIORE_SANZIONE_CUMULO, ...) e
	 * li aggiunge al aDettaglio model.
	 *
	 * @param aDettaglio
	 * @param aIdFascicolo
	 * @return
	 * @throws Exception
	 */
	// 20/06/2008 Modificato in modo da valorizzare correttamente
	// DatiSiepPerTrasferimentoModel.
	public DettaglioFascicoloModel ExAltriDatiFascicoloSiep(DettaglioFascicoloModel aDettaglio,
			BigDecimal aIdFascicolo) throws F3BException {
		// mlog.debug( getClass().getName() +
		// ".ExAltriDatiFascicoloSiep: inizio" );
		DatiSiepPerTrasferimentoModel lDatiSiepXTrasfMod = new DatiSiepPerTrasferimentoModel();

		try {
			// Caricamento dati Cumulo x Trasferimento (CUMULO, PENA_CUMULO e
			// ULTERIORE_SANZIONE_CUMULO)
			Vector lCumuli = new Vector();
			ICumulo lCtrlCum = SIEPLookupRemote.getCumuloRemote();
			lCumuli = lCtrlCum
					.ExRicercaFascicoliCumulobyIdFascicoloSiepValidatoDataCumuloNotNull(aIdFascicolo);
			lDatiSiepXTrasfMod.setListCumulo(lCumuli);

			// Caricamento PENA_CUMULO e ULTERIORE_SANZIONE_CUMULO
			CumuloModel lCumMod = new CumuloModel();
			Vector lPeneCumuli = new Vector();
			Vector lUltSanCumuli = new Vector();

			for (int i = 0; i < lCumuli.size(); i++) {
				lCumMod = ((CumuloModel) (lCumuli).get(i));

				if (lCumMod != null && lCumMod.getIdCumulo() != null) {
					IPenaCumulo lCtrlPen = SIEPLookupRemote.getPenaCumuloRemote();
					lPeneCumuli.add(lCtrlPen.ExRicercaPenaCumuloByIdCumulo(lCumMod.getIdCumulo()));
					IUlterioreSanzioneCumulo lUlt = SIEPLookupRemote.getUlterioreSanzioneCumuloRemote();
					lUltSanCumuli.addAll(lUlt.ExRicercaUlterioreSanzioneCumuloByFascicoloIDCumul0(
							aIdFascicolo, lCumMod.getIdCumulo()));
				}
			}
			lDatiSiepXTrasfMod.setListPenaCumulo(lPeneCumuli);
			lDatiSiepXTrasfMod.setListUltSanCumulo(lUltSanCumuli);

			// Caricamento dati Licenza_Lib_Anticipata x Trasferimento.
			Vector lLicLibAnt = new Vector();
			ILicenzaPeriodiLibAnticipata lCtrlLLA = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();
			lLicLibAnt = lCtrlLLA.ExRicercaLicenzeLibanticipataByIdFascicoloSIEP(aIdFascicolo);
			lDatiSiepXTrasfMod.setListLicLibAnticipata(lLicLibAnt);

			// Caricamento dati Misura_Alternativa x Trasferimento.
			Vector lMisAlt = new Vector();
			IMisuraAlternativa lCtrlMA = SICOLookupRemote.getMisuraAlternativaRemote();
			lMisAlt = lCtrlMA.ExRicercaMisureAlternativeByIdFascicolo(aIdFascicolo);
			lDatiSiepXTrasfMod.setListMisuraAlternativa(lMisAlt);

			// Caricamento dati Residenze e Domicili x Trasferimento.
			Vector lResDom = new Vector();
			IResidenza lCtrlRes = SICOLookupRemote.getResidenzaRemote();
			lResDom = lCtrlRes.ExRicercaResidenzeDomiciliByIdFascicolo(aIdFascicolo);
			lDatiSiepXTrasfMod.setListResidenzaFasSiep(lResDom);

			// Caricamento dati Pena Residua x Trasferimento.
			Vector lPenRes = new Vector();
			IPenaResidua lCtrlPR = SIEPLookupRemote.getPenaResiduaRemote();
			lPenRes = lCtrlPR.ExRicercaPenaResiduaByIdFascicolo(aIdFascicolo);
			lDatiSiepXTrasfMod.setListPenaResidua(lPenRes);

			// Caricamento dati Fungibilità x Trasferimento.
			Vector lFungibilita = new Vector();
			IFungibilita lCtrlFun = SIEPLookupRemote.getFungibilitaRemote();
			lFungibilita = lCtrlFun.ExRicercaFungibilitaByIdFascicolo(aIdFascicolo);
			lDatiSiepXTrasfMod.setListFungibilita(lFungibilita);

			// Caricamento dati Magistrato Competente.
			MagistratoCompetenteMagistratoModel lMagistratoComp = null;
			IMagistratoCompetente lCtrlMagCom = SICOLookupRemote.getMagistratoCompetenteRemote();
			lMagistratoComp = lCtrlMagCom.ExRicercaMagistratoCompetenteByFascicolo(aIdFascicolo);
			lDatiSiepXTrasfMod.setMagistratoCompetente(lMagistratoComp);

			// Caricamento dati Sanzione Sost. Residua x Trasferimento.
			Vector lSSResidua = new Vector();
			ISanzioneSostitutiva lCtrlSS = SIEPLookupRemote.getSanzioneSostitutivaRemote();
			lSSResidua = lCtrlSS.ExRicercaSanzioneSostResiduaByIdFascicolo(aIdFascicolo);
			lDatiSiepXTrasfMod.setListSanzioneSostResidua(lSSResidua);

			// Caricamento dati Annotazioni Manuali x Trasferimento.
			Vector lAnnMan = new Vector();
			IAnnotazioneManuale lCtrlAM = SIEPLookupRemote.getAnnotazioneManualeRemote();
			lAnnMan = lCtrlAM.ExRicercaAnnotazioneManualeByIdFascicolo(aIdFascicolo);
			if (lAnnMan != null && lAnnMan.size() > 0) {
				lDatiSiepXTrasfMod.setListAnnotazioneManuale(lAnnMan);
			}

			// 16-03-2009 Caricamento dati Richieste Conversione Pene Pec.
			Vector lRicConversioni = new Vector();
			RichiestaConversioneModel aRichiestaConversione = new RichiestaConversioneModel();
			aRichiestaConversione.setFasSieIdFascicoloSiep(aIdFascicolo);
			IRichiestaConversione lCtrlRC = SIEPLookupRemote.getRichiestaConversioneRemote();
			lRicConversioni = lCtrlRC.ExRicercaRichiesteConversionePenePecuniarie(aRichiestaConversione);
			lDatiSiepXTrasfMod.setListRichiesteConversioniPP(lRicConversioni);

			// 30-08-2010 Caricamento dati Nuova Istanza.
			Vector lNuovaIstanza = new Vector();
			NuovaIstanzaModel aNuovaIstanza = new NuovaIstanzaModel();
			aNuovaIstanza.setFasSieIdFascicoloSiep(aIdFascicolo);
			INuovaIstanza lCtrlNI = SIEPLookupRemote.getNuovaIstanzaRemote();
			// [EC] 20180205 - intervento per PLO ANOMALIE SIUS
			// l'attuale metodo ExRicercaNuovaIstanzaByIdFascicolo seleziona le istanze collegate a fascicoli
			// annullati pertanto commento questa chiamata a favore dell'altro metodo
			// ExRicercaNuovaIstanzaNonAnnullataByIdFascicolo
			// che seleziona le istanze collegati a fascicoli non annullati
			// lNuovaIstanza = new Vector(lCtrlNI.ExRicercaNuovaIstanzaByIdFascicolo(aIdFascicolo));
			lNuovaIstanza = new Vector(lCtrlNI.ExRicercaNuovaIstanzaNonAnnullataByIdFascicolo(aIdFascicolo));
			lDatiSiepXTrasfMod.setListNuovaIstanza(lNuovaIstanza);

			// 01/09/2010 AGDG Fascicolo Siep.
			IAgdgFascicoloSiep lAGDGFasSiepCtrl = SIEPLookupRemote.getAgdgFascicoloSiepRemote();
			Vector lAltriGradiGiudizio = new Vector(
					lAGDGFasSiepCtrl.ExRicercaAgdgFascicoloSiepByIdFascicoloSiep(aIdFascicolo));
			if (lAltriGradiGiudizio != null && lAltriGradiGiudizio.size() > 0) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di
				// istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("lAltriGradiGiudizio.size() = " + lAltriGradiGiudizio.size());
				lDatiSiepXTrasfMod.setListAltriGradiGiudizio(lAltriGradiGiudizio);
			}
			// 26/09/2014 Sentenza Riunite Fascicolo Siep x trasmissione MS
			ISentenzaRiunita lSentRiunitaCtrl = SIEPLookupRemote.getSentenzaRiunitaRemote();
			Vector lSentenzeRiunite = new Vector(
					lSentRiunitaCtrl.ExRicercaSentenzeRiuniteByIdFascSiep(aIdFascicolo));
			if (lSentenzeRiunite != null && lSentenzeRiunite.size() > 0)
				lDatiSiepXTrasfMod.setListSentenzeRiunite(lSentenzeRiunite);
			// 26/01/2015 Aggiungo la tabella con i collegamenti con i fascicoli
			// di
			// esecuzione delle Misure di Sicurezza (FASC_MS_TO_FASC_SIEP)
			// n.b. posso solo trasmettere i record che hanno
			// FAS_SIE_ID_FASCICOLO_SIEP = aIdFascicolo
			// in quanto strettamente legati al fascicolo che sto trasmettendo
			IMisuraSicurezza lCtrMis = SIEPLookupRemote.getMisuraSicurezzaRemote();
			// FascMsToFascSiepModel FasMSMod = new FascMsToFascSiepModel();
			Vector lVectFasIV = lCtrMis.ExRicercaFascicoliCollegati(aIdFascicolo);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lVectFasIV.size() = " + lVectFasIV.size());
			lDatiSiepXTrasfMod.setListMsToFascSiep(lVectFasIV);

			// MEV26 Aggiungo i record COMPETENZA per trasmissione atti cumulo
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Aggiungo i record COMPETENZA per trasmissione atti cumulo");
			ICompetenza lCtrlCompetenza = SIEPLookupRemote.getCompetenzaRemote();
			Vector lVectCompetenze = lCtrlCompetenza.ExRicercaCompetenzaByIdFascicoloSiep(aIdFascicolo);
			lDatiSiepXTrasfMod.setListCompetenze(lVectCompetenze);
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("lVectCompetenze = "+lVectCompetenze);

			// Aggiungo i record SOSPENSIONE per trasmissione atti cumulo
			// MEV 42 - Cumulo
			siesLogger.debug("Aggiungo i record SOSPENSIONE per trasmissione atti cumulo");
			ISospensione lCtrlSos = SIEPLookupRemote.getSospensioneRemote();
			Vector<SospensioneModel> lVectSospensione = lCtrlSos
					.ExRicercaSospensioneByIdFascicoloSiep(aIdFascicolo);
			if (lVectSospensione != null && lVectSospensione.size() > 0)
				lDatiSiepXTrasfMod.setListSospensioni(lVectSospensione);

			// Aggiungo i record DECRETO_ORDINANZA_SIEP per trasmissione atti cumulo
			// MEV 42 - Cumulo
			siesLogger.debug("Aggiungo i record DECRETO_ORDINANZA_SIEP per trasmissione atti cumulo");
			IDecretoOrdinanzaSiep lCtrlDecrOrd = SIEPLookupRemote.getDecretoOrdinanzaSiepRemote();
			Vector<DecretoOrdinanzaSiepModel> lVectDecrOrd = lCtrlDecrOrd
					.ExRicercaDecretoOrdinanzaSiepByIdFasc(aIdFascicolo);
			if (lVectDecrOrd != null && lVectDecrOrd.size() > 0) {
				siesLogger.debug("lVectDecrOrd = " + lVectDecrOrd.size());
				lDatiSiepXTrasfMod.setListDecretiOrd(lVectDecrOrd);
			}

			// 01/09/2010 Altri Gradi Di Giudizio.
			// Vector lAltriGradiGiudizio = new Vector();
			// AltriGradiGiudizioModel lAGDGModel = new
			// AltriGradiGiudizioModel();
			// if (lAGDGFasSiep != null && lAGDGFasSiep.size() > 0 )
			// for(int k=0; k<lAGDGFasSiep.size(); k++)
			// {
			// lAGDGFasSiepModel = (AgdgFascicoloSiepModel)lAGDGFasSiep.get(k);
			// IAltriGradiGiudizio lAGGCtrl =
			// SIEPLookupRemote.getAltriGradiGiudizioRemote();
			// lAGDGModel =
			// lAGGCtrl.ExRicercaAltriGradiGiudizioByKey(lAGDGFasSiepModel.getAgdgIdAltrigradigiudizio());
			// lAltriGradiGiudizio.add(lAGDGModel);
			// }
			// if (lAltriGradiGiudizio != null && lAltriGradiGiudizio.size() > 0
			// )
			// lDatiSiepXTrasfMod.setListAltriGradiGiudizio(lAltriGradiGiudizio);

			// 28022018 [EC] intervento per PLO ANOMALIE SIUS
			// DEVO RICERCARE I COLLEGATI DEL FASCICOLO SIEP
			IFascicoloSiep lFascSiep = SIEPLookupRemote.getFascicoloSiepRemote();
			BigDecimal myIdFasc = null;
			StringBuffer collegati = new StringBuffer("");
			String tipoRicerca = "";
			int classeProc = 0;
			Vector vectFasIV = new Vector();

			if (aDettaglio.getFascicoloSiep() != null
					&& aDettaglio.getFascicoloSiep().getClasseProcedimento() != 0)
				classeProc = aDettaglio.getFascicoloSiep().getClasseProcedimento();
			siesLogger.debug("classeProc >>>" + classeProc);

			// 1° Poi faccio la ricerca su tutti i procedimenti che hanno l'Id di questo fascicolo nel campo
			// FAS_SIE_ID_FASCICOLO_SIEP; (classe I)
			if (aDettaglio.getFascicoloSiep().getIdFascicoloSiep() != null && classeProc == 1) {
				myIdFasc = aDettaglio.getFascicoloSiep().getIdFascicoloSiep();
				tipoRicerca = "I";
				String c = lFascSiep.ExRicercaFasCollegatiFascicoloByKey(myIdFasc, tipoRicerca,
						aDettaglio.getFascicoloSiep().getChiaveUfficio());
				if (c != null && !c.equals(""))
					collegati = collegati.append(c);
				siesLogger.debug("stringa dei collegati: >>>" + collegati + " per classe>>" + classeProc);
			}
			// 2° Poi faccio la ricerca per la (classe IV)
			if (aDettaglio.getFascicoloSiep().getIdFascicoloSiep() != null && classeProc == 4) {
				myIdFasc = aDettaglio.getFascicoloSiep().getIdFascicoloSiep();
				// devo ricercare il fascicoli collegato sulla tabella FASC_MS_TO_FASC_SIEP
				IMisuraSicurezza lCtrlMis = SIEPLookupRemote.getMisuraSicurezzaRemote();
				vectFasIV = lCtrlMis.ExRicercaFascicoliCollegati(myIdFasc);
				if (vectFasIV.size() > 0) {
					Iterator<FascMsToFascSiepModel> iteIV = vectFasIV.iterator();
					while (iteIV.hasNext()) {
						FascMsToFascSiepModel FascMSMod = iteIV.next();
						collegati = collegati.append(FascMSMod.getChiaveAnnoSiepCollegato() + "/"
								+ FascMSMod.getChiaveProgrSiepCollegato() + " - ");
					}
				}
				siesLogger.debug("stringa dei collegati: >>>" + collegati + " per classe>>" + classeProc);
			}
			// 1° eseguo una ricerca dei collegati per il valore di getFasSieIdFascicoloSiep (dovrebbero
			// essere quelli di classe VII)
			if (aDettaglio.getFascicoloSiep() != null
					&& aDettaglio.getFascicoloSiep().getFasSieIdFascicoloSiep() != null && classeProc != 1
					&& classeProc != 4) {
				// recupero il valore di getFasSieIdFascicoloSiep e faccio la ricerca per leggerne il
				// anno/numero di questo fascicolo e lo metto nella lista dei collegati
				myIdFasc = aDettaglio.getFascicoloSiep().getFasSieIdFascicoloSiep();
				tipoRicerca = "C";
				String c = lFascSiep.ExRicercaFasCollegatiFascicoloByKey(myIdFasc, tipoRicerca,
						aDettaglio.getFascicoloSiep().getChiaveUfficio());
				if (c != null && !c.equals(""))
					collegati = collegati.append(c);
				siesLogger.debug("stringa dei collegati: >>>" + collegati + " per classe>>" + classeProc);
			}

			if (collegati != null && collegati.length() > 1) {
				siesLogger.debug("stringa dei collegati: FINALE >>>" + collegati);
				String noteFascicolo = aDettaglio.getFascicoloSiep().getNote() != null
						? aDettaglio.getFascicoloSiep().getNote()
						: "";
				// appendo alle note la lista dei collegati
				noteFascicolo = noteFascicolo + " - PROCEDIMENTI COLLEGATI: " + collegati;
				aDettaglio.getFascicoloSiep().setNote(noteFascicolo);
				siesLogger.debug("NOTE FASCICOLO: >>>" + noteFascicolo);
				siesLogger.debug("NOTE FASCICOLO: aDettaglio.getNoteFascicolo() >>>"
						+ aDettaglio.getFascicoloSiep().getNote());
			}

		} catch (F3BException ex) {
			ex.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("Errore in FascicoloSiepController.ExAltriDatiFascicoloSiep  ", ex);
			// throw new
			// F3BException("FascicoloSiepController.ExAltriDatiFascicoloSiep: "
			// + ex);
		} finally {
			aDettaglio.setDatiSiepPerTrasferimento(lDatiSiepXTrasfMod);
		}
		return aDettaglio;
	}

	/**
	 * Recupera la lista dei procedimenti collegati al fascicoli SIEP e l'appende come string al campo NOTE
	 * del fascicolo
	 *
	 * @param aIdFascicolo
	 * @return
	 */
	public String ExRicercaFasCollegatiFascicoloByKey(BigDecimal aIdFascicolo, String tipoRicerca,
			String ufficio) throws F3BException {

		// metodo creato per PLO ANOMALIE SIUS
		Connection lConn = null;
		FascicoloSiepSqlDAO lFasDao = null;
		String collegati = "";
		try {
			lConn = getDBConnection();
			lFasDao = new FascicoloSiepSqlDAO(lConn);
			lFasDao.ricercaFasCollegatiFascicoloByKey(aIdFascicolo, tipoRicerca, ufficio);
			lFasDao.start();
			if (lFasDao.next()) {
				collegati = lFasDao.getString("collegati");
				lFasDao.stop();
			}
		} catch (DAOException daoEx) {
			System.out.println("DAOException: " + daoEx);
			throw new F3BException("FascicoloSiepController.ExRicercaFasCollegatiFascicoloByKey: " + daoEx);

		} finally {
			cleanup(lFasDao);
			cleanup(lConn);
		}
		return collegati;
	}

	/**
	 * Recupera i dati del Differimento dal DecretoOrdinanzaSiep e li aggiunge al aDettaglio model.
	 * Attenzione!! dalla nuova versione del differimento i dati da recuperare sono sulla tabella
	 * MisuraAlternativa
	 *
	 * @param aDettaglio
	 * @param aIdFascicolo
	 * @return
	 * @throws Exception
	 */
	private DettaglioFascicoloModel ricercaProvDifferimento(DettaglioFascicoloModel aDettaglio,
			BigDecimal aIdFascicolo) throws Exception {
		// mlog.debug( getClass().getName() + ".ricercaProvDifferimento: inizio"
		// );

		// evento della Notifica collegata al decreto ordinanza siep per il
		// differimento
		// ==========================================================================
		// Ricerco l'ultimo evento VALIDATO associato a una 'Notifica' di un
		// differimento e recupero i dati dalla tabella DecretoOrdinanzaSiep
		// ==========================================================================
		EventoModel lEveRic = new EventoModel();
		lEveRic.setCodMotivo("");
		lEveRic.setCodTipoEvento("01");
		lEveRic.setCodTipoProvvedimento("");
		lEveRic.setFasSieIdFascicoloSiep(aIdFascicolo);
		lEveRic.setFlagDocumentoRegistrato("S"); //

		// Preparazione delle possibbili coppie TipoProvvedimento, CodMotivo
		// da usare nella ricerca dell'Evento collegato alla Notifica
		// Differimento.
		// Luigi 22-09-2005
		String[] lTipoProv = { "04", "09", "09", "12", "12" };
		String[] lCodMotivo = { "0274", "0274", "0221", "0221", "0220" };
		// 0274 - Rinvio provvisorio dell'esecuzione ex art.684 c.2 c.p.p.
		// (diff. provvisorio)
		// 0221 - rinvio dell'esecuzione ex art. 684 c.1 c.p.p. (diff.
		// definitivo)
		// 0220 - rinvio dell'esecuzione

		// Ricerca
		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		Vector lVectEve = lCtrlEvento.ExRicercaEventoTipoProvTipoMot(lEveRic, lTipoProv, lCodMotivo);

		EventoModel lEve = null;
		if (lVectEve != null && lVectEve.size() > 0) {
			// mlog.debug( "Trovato Evento Notifica" );

			EventoModel lEveOrd = null;

			// Trovato l'Evento si cerca il Provvedimento
			lEve = (EventoModel) lVectEve.get(0); // prendo il più recente
			IDecretoOrdinanzaSiep lDepOrdCtrl = SIEPLookupRemote.getDecretoOrdinanzaSiepRemote();

			try { // recupero l'evento dell'ordinanaza
				lEveOrd = lCtrlEvento.ExRicercaEventoByKey(lEve.getEveIdEvento());
			} catch (Exception e) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di
				// istanza siesLogger al posto di mLog
				siesLogger.info("Evento collegato a Provvedimento di Differimento non trovato");
			}

			//
			if (lEveOrd != null) {
				try {
					DecretoOrdinanzaSiepModel lDecSiepMod = lDepOrdCtrl
							.ExRicercaDecretoOrdinanzaSiepByKey(lEveOrd.getDecIdDecretoOrdinanzaSiep());
					aDettaglio.setDecretoOrdinanzaSiep(lDecSiepMod);
				} catch (Exception e) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di
					// istanza siesLogger al posto di
					// mLog
					siesLogger.info("Provvedimento di Differimento non trovato");
				}
			}
		}
		// mlog.debug( getClass().getName() + ".ricercaProvDifferimento: fine"
		// );

		return aDettaglio;
	}

	/**
	 * Ricerca della Posizione Giuridica e Pena Residua in Banca Dati per primary key del Fascicolo
	 * <p>
	 *
	 * @param aIdFascicolo
	 *            Primary Key del fascicolo SIEP
	 * @return lDettaglio DettaglioFascicoloModel con i dati di PosizioneGiuridica e PenaResidua.
	 * @throws F3BException
	 */
	public DettaglioFascicoloModel ExDettaglioPosizionePerFascicoloSiep(BigDecimal aIdFascicolo)
			throws F3BException {

		Connection lConn = null;

		DettaglioFascicoloModel lDettaglio = null;

		PosizioneGiuridicaSqlDAO lPosGiuDao = null;

		try {
			lConn = getDBConnection();

			lPosGiuDao = new PosizioneGiuridicaSqlDAO(lConn);
			lDettaglio = new DettaglioFascicoloModel();

			lDettaglio.setFascicoloSiep(ExRicercaFascicoloByKey(aIdFascicolo));

			// Posizione Giuridica (l'ultima)
			lPosGiuDao.ricercaPosGiuCorrenteByIdFascicolo(aIdFascicolo);
			lDettaglio.setPosizioneGiuridica((PosizioneGiuridicaModel) lPosGiuDao.getModelByKey());

			// Pena Residua
			IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
			PenaResiduaModel lPenResMod = lPenResCtrl
					.ExRicercaPenaResiduaCorrenteByFascicoloSiep(aIdFascicolo);
			lDettaglio.setPenaResidua(lPenResMod);
		} catch (DAOException dex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("DAOException: " + dex);
			throw new SIEPException("FascicoloSiepController.ExDettaglioPosizionePerFascicoloSiep: " + dex);
		} finally {
			cleanup(lPosGiuDao);

			cleanup(lConn);
		}

		return lDettaglio;
	}

	/**
	 * Modifica Fascicolo Siep
	 *
	 * @param aFascicoloSiep
	 * @return
	 * @throws F3BException
	 */
	public void ExModificaFascicoloSiep(FascicoloSiepModel aFascicoloSiep) throws F3BException {

		Connection lConn = null;

		FascicoloSiepDAO lFasDao = null;

		try {
			lConn = getDBConnection();

			lFasDao = new FascicoloSiepDAO(lConn);

			lFasDao.setDAOFromModelForUpdate(aFascicoloSiep);
			lFasDao.selCondizioneUpdate(aFascicoloSiep.getIdFascicoloSiep());
			lFasDao.update();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new SIEPException("FascicoloSiepController.ExModificaFascicoloSiep: " + ex);
		} finally {
			cleanup(lFasDao);
			cleanup(lConn);
		}
	}

	/**
	 * Cancella Fascicolo Siep
	 *
	 * @param aFascicoloSiep
	 * @throws F3BException
	 */

	public void ExCancellaFascicoloSiep(FascicoloSiepModel aFascicoloSiep) throws F3BException {

		Connection lConn = null;
		FascicoloSiepDAO lFasDao = null;

		try {
			lConn = getDBConnection();

			lFasDao = new FascicoloSiepDAO(lConn);
			lFasDao.selCondizioneUpdate(aFascicoloSiep.getIdFascicoloSiep());

			lFasDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new SIEPException(
					"FascicoloSiepController.ExCancellaFascicoloSiep: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lFasDao);
			cleanup(lConn);
		}
	}

	/**
	 * Validazione del fascicolo SIEP
	 *
	 * @param aFasModel
	 * @return
	 * @throws F3BException
	 */
	public FascicoloSiepModel ExValidazione(FascicoloSiepModel aFasModel, StatoProcedimentoModel aStato)
			throws F3BException {

		Connection lConn = null;

		FascicoloSiepDAO lFasDao = null;
		StatoProcedimentoDAO lStatoProcDao = null;
		try {
			lConn = getDBTransaction();
			lFasDao = new FascicoloSiepDAO(lConn);

			lFasDao.setDAOFromModelForUpdate(aFasModel);
			lFasDao.setFlagValidato(aFasModel.getFlagValidato());
			lFasDao.setCodStatoFascicolo(aFasModel.getCodStatoFascicolo());
			lFasDao.selCondizioneUpdate(aFasModel.getIdFascicoloSiep());
			lFasDao.update();
			lFasDao.stop();

			// GDV Stato del procedimento
			/*
			 * StatoProcedimentoModel lStat = new StatoProcedimentoModel();
			 * lStat.setFasSieIdFascicoloSiep(aFasModel.getIdFascicoloSiep()); lStat.setProgressivo(new
			 * BigDecimal(1)); lStat.setDataInserimento(DateUtils.getSysDate());
			 * lStat.setCodUfficioInserimento(aFasModel.getCodUfficioInserimento());
			 * lStat.setCodOperatoreInserimento(aFasModel.getCodOperatoreInserimento());
			 * lStat.setCodStatoProcedimento("0109");
			 */
			// Validato

			lStatoProcDao = new StatoProcedimentoDAO(lConn);
			lStatoProcDao.setCondizioneByIdFascicolo(aFasModel.getIdFascicoloSiep());
			lStatoProcDao.delete();
			lStatoProcDao.stop();

			lStatoProcDao.setDAOFromModel(aStato);
			lStatoProcDao.insert();
			// GDV Stato del procedimento

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new SIEPException("FascicoloSiepController.ExValidazione: " + ex);
		} finally {
			cleanup(lFasDao);
			cleanup(lStatoProcDao);
			cleanup(lConn);
		}

		return aFasModel;
	}

	/**
	 *
	 * @param aIdFascicoloSiep
	 * @return
	 * @throws F3BException
	 *             private BigDecimal getIdResidenzaFascicoloCorrente(BigDecimal aIdFascicoloSiep) throws
	 *             F3BException { Connection lConn = null; BigDecimal lId = null; ResidenzaSqlDAO lSqlDao =
	 *             null; try { lConn = getDBConnection(); lSqlDao = new ResidenzaSqlDAO(lConn);
	 *             lSqlDao.ricercaResidenzaByFascicolo(aIdFascicoloSiep); ResidenzaModel lRes =
	 *             (ResidenzaModel)lSqlDao.getModelByKey(); if (lRes != null) { lId = lRes.getIdResidenza(); }
	 *             } // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	 *             mLog } catch (DAOException ex) { siesLogger.error("DAOException: " + ex); throw new
	 *             F3BException ("FascicoloSiepController.getIdPosizioneGiuridicaCorrente: " + ex); } catch
	 *             (// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	 *             mLog (SQLException sqe) { siesLogger.error("SQLException: " + sqe); throw new F3BException(
	 *             "FascicoloSiepController.getIdPosizioneGiuridicaCorrente: " + sqe); } finally {
	 *             cleanup(lSqlDao); cleanup(lConn); } return lId; }
	 */
	/**
	 * Conta il numero dei fascicoli trovati
	 *
	 * @param aFascicolo
	 * @return
	 * @throws F3BException
	 */
	public BigDecimal ExgetCountFascicoli(FascicoloSiepModel aFascicolo) throws F3BException {

		BigDecimal lCount = new BigDecimal(0);
		Connection lConn = null;

		FascicoloSiepOnViewSqlDAO lSqlDao = null;
		try {
			lConn = getDBConnection();
			lSqlDao = new FascicoloSiepOnViewSqlDAO(lConn);
			lSqlDao.getCountFascicoli(aFascicolo);
			lSqlDao.start();
			lSqlDao.next();
			lCount = lSqlDao.getBigDecimal("HowManyRecords");
			lSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error(daoEx.getLocalizedMessage());
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"FascicoloController.ExRicercaFascicolo: Non posso leggere i Fascicoli : " + daoEx);
		} finally {
			cleanup(lSqlDao);
			cleanup(lConn);
		}
		return lCount;
	}

	/**
	 * Conta il numero dei fascicoli trovati per singolo soggetto
	 *
	 * @param aFascicolo
	 * @return
	 * @throws F3BException
	 */
	public BigDecimal ExGetCountFascicoliSoggetto(FascicoloSiepModel aFascicolo) throws F3BException {

		BigDecimal lCount = new BigDecimal(0);
		Connection lConn = null;

		FascicoloSiepOnViewSqlDAO lSqlDao = null;

		try {
			lConn = getDBConnection();

			lSqlDao = new FascicoloSiepOnViewSqlDAO(lConn);
			lSqlDao.getCountFascicoliSoggetto(aFascicolo);

			lSqlDao.start();
			lSqlDao.next();
			lCount = lSqlDao.getBigDecimal("HowManyRecords");
			lSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error(daoEx.getLocalizedMessage());
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"FascicoloSiepController.ExgetCountFascicoliSoggetto: " + daoEx);
		} finally {
			cleanup(lSqlDao);
			cleanup(lConn);
		}
		return lCount;
	}

	/**
	 * Conta il numero dei fascicoli trovati per singolo soggetto, nell'ufficio indicato
	 *
	 * @param aFascicolo
	 * @param aCodUfficio
	 * @return
	 * @throws F3BException
	 */
	public BigDecimal ExGetCountFascicoliSoggettoUfficio(FascicoloSiepModel aFascicolo, String aCodUfficio)
			throws F3BException {

		BigDecimal lCount = new BigDecimal(0);
		Connection lConn = null;

		FascicoloSiepOnViewSqlDAO lSqlDao = null;

		try {
			lConn = getDBConnection();

			lSqlDao = new FascicoloSiepOnViewSqlDAO(lConn);
			lSqlDao.getCountFascicoliSoggettoUfficio(aFascicolo, aCodUfficio);

			lSqlDao.start();
			lSqlDao.next();
			lCount = lSqlDao.getBigDecimal("HowManyRecords");
			lSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error(daoEx.getLocalizedMessage());
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"FascicoloSiepController.ExgetCountFascicoliSoggettoUfficio: " + daoEx);
		} finally {
			cleanup(lSqlDao);
			cleanup(lConn);
		}
		return lCount;
	}

	/**
	 * Inserisce il fascicolo non assegnando una sequence. metodo usato per i trasferimenti JMS da altra BDI
	 *
	 * @param lFasc
	 * @param lConn
	 * @return Stringa che indica l'esito.
	 * @throws F3BException
	 */
	public String ExInserisciFascicoloWithoutSequence(FascicoloSiepModel lFasc, Connection lConn)
			throws F3BException {

		FascicoloSiepDAO lFasDao = null;
		String lCodEsito = "00000";

		try {
			lFasDao = new FascicoloSiepDAO(lConn);
			lFasDao.setDAOFromModel(lFasc);
			lFasDao.setWithoutSequence(true);
			lFasDao.insert();
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				lCodEsito = "00001";
				try {
					lFasDao.stop();
					lFasDao.setDAOFromModelForUpdateDaTrasferimento(lFasc);
					lFasDao.setDataAggiornamento(DateUtils.getSysDate());
					lFasDao.selCondizioneUpdate(lFasc.getIdFascicoloSiep());
					lFasDao.update();
				} catch (DAOException dex) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di
					// istanza siesLogger al posto di
					// mLog
					siesLogger.debug("Eccezione nell'Update del Fascicolo ---> " + dex);
				}
			} else {
				if ((ex.getMessage().indexOf("FAS_SIE_PK") != -1)
						|| (ex.getMessage().indexOf("FAS_ANN_UFF_PRO_FK_I") != -1)) {
					lCodEsito = "00001";
				} else
					throw new F3BException(F3BException.USER_MESSAGE,
							"Impossibile inserire il Fascicolo SIEP!");
			}
		} finally {
			cleanup(lFasDao);
		}
		return lCodEsito;
	}

	/**
	 * ExRidefinisciSentenzaFascicoloSiep
	 *
	 * @param idFasSiep
	 * @param idSentenza
	 * @throws F3BException
	 */
	public void ExRidefinisciSentenzaFascicoloSiep(BigDecimal idFasSiep, BigDecimal idSentenza)
			throws F3BException {

		Connection lConn = null;

		FascicoloSiepDAO lFasDao = null;

		try {
			lConn = getDBConnection();

			lFasDao = new FascicoloSiepDAO(lConn);
			lFasDao.setSenIdSentenza(idSentenza);
			lFasDao.selCondizioneUpdate(idFasSiep);
			lFasDao.update();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new SIEPException("FascicoloSiepController.ExRidefinisciSentenzaFascicoloSiep: " + ex);
		} finally {
			cleanup(lFasDao);
			cleanup(lConn);
		}
	}

	/**
	 * Ricerca Fascicoli da Soggetto e Alias paginati
	 *
	 * @param aSogModel
	 * @param lCodUfficioUtenteConnesso
	 * @param aPage
	 * @param lCodDistrettoUtenteConnesso
	 * @param TipoRicerca
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaSoggettoAliasFascicoloPaged(SoggettoModel aSogModel,
			String lCodUfficioUtenteConnesso, int aPage, String lCodDistrettoUtenteConnesso,
			String TipoRicerca) throws F3BException {

		return ExRicercaSoggettoAliasFascicoloPaged(aSogModel, lCodUfficioUtenteConnesso, aPage,
				lCodDistrettoUtenteConnesso, TipoRicerca, "");
	}

	public Vector ExRicercaSoggettoAliasFascicoloPaged(SoggettoModel aSogModel,
			String lCodUfficioUtenteConnesso, int aPage, String lCodDistrettoUtenteConnesso,
			String TipoRicerca, String majorOffice) throws F3BException {

		Connection lConn = null;

		Vector lSogAliFascicoli = new Vector();

		SoggettoAliasFascicoloSqlDao lSoggAliFasDao = null;
		SoggettoSqlDAO lSogDao = null;

		SoggettoAliasFascicoloModel lSogAliFascicolo = null;

		// paolo cherubini per supersoggetto 03/11/2011
		// modifico questo metodo per adeguare anche la ricerca per alias al
		// raggruppamento per supersoggetto
		// aggiunto il SoggettoSqlDAO per richiamare la ricerca del
		// supersoggetto
		// aggiunto il lAliasSqlDao per richiamare la ricerca del alias
		// "supersoggetto" nuovo metodo
		// appositamente creato
		// aggiunto il lFasDao per cercare l'ufficio del procedimento es: PM
		// ROMA poichè questo dato deve
		// essere cercato
		// in un secondo tempo e non può essere incluso nella ricerca
		// supersoggetto come oggetto aggregante
		SoggettoSqlDAO lSogSqlDao = null;
		AliasSqlDAO lAliasSqlDao = null;
		FascicoloSiepSqlDAO lFasDao = null;
		try {
			lConn = getDBConnection();

			lSoggAliFasDao = new SoggettoAliasFascicoloSqlDao(lConn);

			// paolo cherubini per supersoggetto 03/01/2011
			lSogSqlDao = new SoggettoSqlDAO(lConn);
			lAliasSqlDao = new AliasSqlDAO(lConn);
			AliasModel aAliModel = new AliasModel();
			lFasDao = new FascicoloSiepSqlDAO(lConn);
			FascicoloSiepModel aModFas = new FascicoloSiepModel();
			// fine

			lSoggAliFasDao.ricercaSoggettoAliasFascicoloPaged(aSogModel, lCodUfficioUtenteConnesso, aPage,
					lCodDistrettoUtenteConnesso, TipoRicerca, majorOffice);

			lSoggAliFasDao.start();
			BigDecimal idSoggetto = null;

			while (lSoggAliFasDao.next()) {
				lSogAliFascicolo = (SoggettoAliasFascicoloModel) lSoggAliFasDao
						.getSoggettoAliasFascicoliModel();
				if (TipoRicerca.equals("ufficio"))
					lSogAliFascicolo.getSoggetto().setCodUfficioInserimento(lCodUfficioUtenteConnesso);

				// paolo cherubini per supersoggetto 03/01/2011
				if (lSogAliFascicolo.getSogIdSoggettoAlias() != null
						&& lSogAliFascicolo.getSogIdSoggettoAlias().intValue() == 0) {
					// se il risultato è un alias ricerco il superAlias
					lAliasSqlDao.ricercaSuperAlias(lSogAliFascicolo.getSoggetto());
					aAliModel = (AliasModel) lAliasSqlDao.getModelByKey();
					idSoggetto = aAliModel.getSogIdSoggetto();
				} else {
					// se il risultato è un soggeto ricerco il supersoggetto
					lSogSqlDao.ricercaSuperSoggetto(TipoRicerca, lCodUfficioUtenteConnesso,
							lSogAliFascicolo.getSoggetto(), "FASCICOLO_SIEP", lCodDistrettoUtenteConnesso,
							majorOffice, false, "");
					aSogModel = (SoggettoModel) lSogSqlDao.getModelByKey();
					idSoggetto = aSogModel.getIdSoggetto();
				}

				if (lSogAliFascicolo.getNumeroFascicoliUfficioSedeCompetente().equals("1")) {
					// se il numero dei procedimenti è uguale a 1 vado a cercare
					// sul fascicolo il tipo di
					// ufficio es: PM ROMA
					aModFas.setSogIdSoggetto(idSoggetto);
					lFasDao.ricercaFascicoloProgFasc(aModFas, "FASCICOLO_SIEP");
					lFasDao.start();
					if (lFasDao.next()) {
						aModFas = (FascicoloSiepModel) lFasDao.getModelsPerRicUff();
						lFasDao.stop();
					}
					lSogAliFascicolo.setNumeroFascicoliUfficioSedeCompetente(
							aModFas.getDescrTipoUfficio() + " Di " + aModFas.getDescrComuneUfficio());
				}
				// aggiungo il primo ID dei soggetti appartenenti al
				// supersoggetto
				// sarà poi ridisaggregato nella ricerca successiva
				lSogAliFascicolo.getSoggetto().setIdSoggetto(idSoggetto);
				// fine paolo supersoggetto

				lSogAliFascicoli.add(lSogAliFascicolo);
			}

			lSoggAliFasDao.stop();

			if (lSogAliFascicoli.isEmpty())
				throw new SIEPException(F3BException.USER_MESSAGE,
						"Nessun Soggetto o Alias individuato con i criteri di ricerca selezionati! ");
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new SIEPException(F3BException.USER_MESSAGE,
					"FascicoloSiepController.ExRicercaSoggettoAliasFascicoloPaged: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("Exception: " + e);
			throw new SIEPException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lSoggAliFasDao);
			cleanup(lSogDao);

			cleanup(lConn);
		}

		return lSogAliFascicoli;
	} // FINE - Ricerca Fascicoli da Soggetto e Alias paginati

	/**
	 * Count di Ricerca Fascicoli da Soggetto e Alias
	 *
	 * @param aSogModel
	 * @param lCodUfficioUtenteConnesso
	 * @param lCodDistrettoUtenteConnesso
	 * @param TipoRicerca
	 * @return
	 * @throws F3BException
	 */
	public BigDecimal ExGetCountSoggettoAliasFascicolo(SoggettoModel aSogModel,
			String lCodUfficioUtenteConnesso, String lCodDistrettoUtenteConnesso, String TipoRicerca)
			throws F3BException {

		return ExGetCountSoggettoAliasFascicolo(aSogModel, lCodUfficioUtenteConnesso,
				lCodDistrettoUtenteConnesso, TipoRicerca, "");
	}

	public BigDecimal ExGetCountSoggettoAliasFascicolo(SoggettoModel aSogModel,
			String lCodUfficioUtenteConnesso, String lCodDistrettoUtenteConnesso, String TipoRicerca,
			String majorOffice) throws F3BException {

		BigDecimal lCount = new BigDecimal(0);

		Connection lConn = null;

		SoggettoAliasFascicoloSqlDao lSqlDao = null;

		try {
			lConn = getDBConnection();

			lSqlDao = new SoggettoAliasFascicoloSqlDao(lConn);
			lSqlDao.getCountSoggettoAliasFascicolo(aSogModel, lCodUfficioUtenteConnesso,
					lCodDistrettoUtenteConnesso, TipoRicerca, majorOffice);

			lSqlDao.start();
			lSqlDao.next();
			lCount = lSqlDao.getBigDecimal("HowManyRecords");
			lSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error(daoEx.getLocalizedMessage());
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"FascicoloSiepController.ExGetCountSoggettoAliasFascicolo: " + daoEx);
		} finally {
			cleanup(lSqlDao);

			cleanup(lConn);
		}

		return lCount;
	} // FINE - Count di Ricerca Fascicoli da Soggetto e Alias

	/**
	 * Modifica Note Fascicolo Siep
	 *
	 * @param aFascicoloSiep
	 * @return
	 * @throws F3BException
	 */
	public void ExModificaNoteFascicoloSiep(FascicoloSiepModel aFascicoloSiep) throws F3BException {

		Connection lConn = null;

		FascicoloSiepDAO lFasDao = null;

		try {
			lConn = getDBConnection();

			lFasDao = new FascicoloSiepDAO(lConn);

			lFasDao.setNote(aFascicoloSiep.getNote());
			lFasDao.setCodOperatoreAggiornamento(aFascicoloSiep.getCodOperatoreAggiornamento());
			lFasDao.setDataAggiornamento(aFascicoloSiep.getDataAggiornamento());
			lFasDao.setCodUfficioAggiornamento(aFascicoloSiep.getCodUfficioAggiornamento());

			lFasDao.selCondizioneUpdate(aFascicoloSiep.getIdFascicoloSiep());
			lFasDao.update();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new SIEPException("FascicoloSiepController.ExModificaNoteFascicoloSiep: " + ex);
		} finally {
			cleanup(lFasDao);
			cleanup(lConn);
		}
	}

	/**
	 * Modifica Note Fascicolo Siep
	 *
	 * @param aFascicoloSiep
	 * @return
	 * @throws F3BException
	 */
	public void ExModificaVisibilitaMinoreFascicoloSiep(FascicoloSiepModel aFascicoloSiep)
			throws F3BException {

		Connection lConn = null;
		FascicoloSiepDAO lFasDao = null;

		try {
			lConn = getDBConnection();
			lFasDao = new FascicoloSiepDAO(lConn);

			lFasDao.setVisibilitaMinorenne(aFascicoloSiep.getVisibilitaMinorenne());
			lFasDao.setCodOperatoreAggiornamento(aFascicoloSiep.getCodOperatoreAggiornamento());
			lFasDao.setDataAggiornamento(aFascicoloSiep.getDataAggiornamento());
			lFasDao.setCodUfficioAggiornamento(aFascicoloSiep.getCodUfficioAggiornamento());
			lFasDao.selCondizioneUpdate(aFascicoloSiep.getIdFascicoloSiep());
			lFasDao.update();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new SIEPException("FascicoloSiepController.ExModificaNoteFascicoloSiep: " + ex);
		} finally {
			cleanup(lFasDao);
			cleanup(lConn);
		}
	}

	/**
	 * ExRicercaFascicoloSospesiInterrotiOnViewPaged Ricerca Paginata sui fascicoli sospesi/interrotti
	 *
	 * @param aFascicoloSiep
	 * @param aPage
	 * @param aMotivo
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaFascicoloSospesiInterrotiOnViewPaged(FascicoloSiepModel aFascicoloSiep,
			String[] aMotivo, int aPage, String aAggiuntoUnion) throws F3BException {

		Connection lConn = null;
		Vector lFascicoli = new Vector();
		FascicoloSiepAggregatoSqlDAO lFasDao = null;

		try {
			lConn = getDBConnection();

			lFasDao = new FascicoloSiepAggregatoSqlDAO(lConn);
			lFasDao.ricercaFascicoloSiepSospesiInterrotti(aFascicoloSiep, aMotivo, aPage, aAggiuntoUnion);
			lFasDao.start();
			FascicoloSiepAggregatoModel lFascicolo = null;

			while (lFasDao.next()) {
				lFascicolo = (FascicoloSiepAggregatoModel) lFasDao.getModel();
				lFascicoli.add(lFascicolo);
			}

			lFasDao.stop();

			if (lFascicoli.isEmpty())
				throw new SIEPException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			throw new SIEPException(F3BException.USER_MESSAGE,
					"FascicoloSiepController.ExRicercaFascicoloSospesiINterrotiOnViewPaged: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lFasDao);
			cleanup(lConn);
		}

		return lFascicoli;
	}

	public BigDecimal ExGetCountFascicoliSospesiInterrotti(FascicoloSiepModel aFascicolo, String[] aMotivo,
			String aAggiuntoUnion) throws F3BException {

		BigDecimal lCount = new BigDecimal(0);
		Connection lConn = null;

		FascicoloSiepAggregatoSqlDAO lSqlDao = null;

		try {
			lConn = getDBConnection();

			lSqlDao = new FascicoloSiepAggregatoSqlDAO(lConn);
			lSqlDao.getCountFascicoliSospesiInterrotti(aFascicolo, aMotivo, aAggiuntoUnion);

			lSqlDao.start();
			lSqlDao.next();
			lCount = lSqlDao.getBigDecimal("HowManyRecords");
			lSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error(daoEx.getLocalizedMessage());
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"FascicoloSiepController.ExGetCountFascicoliSospesiInterrotti: " + daoEx);
		} finally {
			cleanup(lSqlDao);
			cleanup(lConn);
		}
		return lCount;
	}

	public Vector ExRicercaFascicoloSospesiInterrotiAll(FascicoloSiepModel aFascicoloSiep, String[] aMotivo,
			String aAggiuntoUnion) throws F3BException {

		Connection lConn = null;
		Vector lFascicoli = new Vector();
		FascicoloSiepAggregatoSqlDAO lFasDao = null;
		try {
			lConn = getDBConnection();

			lFasDao = new FascicoloSiepAggregatoSqlDAO(lConn);
			lFasDao.ricercaFascicoloSiepSospesiInterrottiAll(aFascicoloSiep, aMotivo, aAggiuntoUnion);
			lFasDao.start();
			FascicoloSiepAggregatoModel lFascicolo = null;

			while (lFasDao.next()) {
				lFascicolo = (FascicoloSiepAggregatoModel) lFasDao.getModel();
				lFascicoli.add(lFascicolo);
			}
			lFasDao.stop();
		} catch (DAOException daoEx) {
			throw new SIEPException(F3BException.USER_MESSAGE,
					"FascicoloSiepController.ExRicercaFascicoloSospesiInterrotiAll: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lFasDao);
			cleanup(lConn);
		}

		return lFascicoli;
	}

	/**
	 * Ricerca l'elenco dei fascicoli correntemente assegnati a un magistrato su un particolare ufficio in
	 * base allo stato del fascicolo
	 *
	 * @param aCodMagistrato
	 *            - Codice CSM del magistrato
	 * @param aCodUfficio
	 *            - Codice ufficio di appartenenza del Procedimento
	 * @param aStato
	 *            - Array di COD_STATO_FASCICOLO
	 * @return
	 */
	public Vector ExRicercaFascicoliByMagistratoAssegnatario(String aCodMagistrato, String aCodUfficio,
			String[] aStato) throws F3BException {

		Connection lConn = null;
		Vector lFascicoli = new Vector();

		FascicoloSiepSqlDAO lFasSqlDao = null;

		try {
			lConn = getDBConnection();

			lFasSqlDao = new FascicoloSiepSqlDAO(lConn);
			lFasSqlDao.ricercaFascicoloSiepByMagistratoAssegnatario(aCodMagistrato, aCodUfficio, aStato);

			lFascicoli = new Vector(lFasSqlDao.getModels());

			lFasSqlDao.stop();
		} catch (DAOException daoEx) {
			throw new SIEPException(F3BException.USER_MESSAGE,
					"FascicoloSiepController.ExRicercaFascicoliByMagistratoAssegnatario: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lFasSqlDao);
			cleanup(lConn);
		}

		return lFascicoli;

	}

	public void ExModificaKeyNscByKey(FascicoloSiepModel aFascicoloSiep) throws F3BException {

		Connection conn = null;
		FascicoloSiepDAO lFasDao = null;
		// FascicoloSiepModel lSog = null;

		try {
			conn = getDBConnection();
			lFasDao = new FascicoloSiepDAO(conn);
			lFasDao.setDAOFromModelForUpdateKeyNsc(aFascicoloSiep);
			lFasDao.selCondizioneUpdate(aFascicoloSiep.getIdFascicoloSiep());
			lFasDao.update();

			commit(conn);

		} catch (DAOException ex) {
			rollback(conn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.info("DAOException: " + ex);
			throw new SICOException("FascicoloSiepController.ExModificaKeyNSCByKey: " + ex);
		} finally {
			cleanup(lFasDao);
			cleanup(conn);
		}
	}

	public void ExModificaFasSiesIdFascicoloByKey(FascicoloSiepModel aFascicoloSiep) throws F3BException {

		Connection conn = null;
		FascicoloSiepDAO lFasDao = null;
		// FascicoloSiepModel lSog = null;

		try {
			conn = getDBConnection();
			lFasDao = new FascicoloSiepDAO(conn);
			lFasDao.setDAOFromModelForUpdateFasSiesIdFascicolo(aFascicoloSiep);
			lFasDao.selCondizioneUpdate(aFascicoloSiep.getIdFascicoloSiep());
			lFasDao.update();

			commit(conn);
		} catch (DAOException ex) {
			rollback(conn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.info("DAOException: " + ex);
			throw new SICOException("FascicoloSiepController.ExModificaFasSiesIdFascicoloByKey: " + ex);
		} finally {
			cleanup(lFasDao);
			cleanup(conn);
		}
	}

	/**
	 * paolo x supersoggetto 22 luglio 2009 Ricerca fascicolo paginato per SuperSoggetto
	 *
	 * @param aFascicoloSiep
	 * @param aPage
	 * @param TipoRicerca
	 * @param StrCodiceDistrettoUtente
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaFascicoloOnViewPagedSuperSoggetti(SoggettoModel aSogModel, String aCodUfficio,
			int aPage, String TipoRicerca, String StrCodiceDistrettoUtente) throws F3BException {

		return ExRicercaFascicoloOnViewPagedSuperSoggetti(aSogModel, aCodUfficio, aPage, TipoRicerca,
				StrCodiceDistrettoUtente, "", "", "");
	}

	public Vector ExRicercaFascicoloOnViewPagedSuperSoggetti(SoggettoModel aSogModel, String aCodUfficio,
			int aPage, String TipoRicerca, String StrCodiceDistrettoUtente, String majorOffice,
			String tipoUfficio, String campoDet) throws F3BException {

		Connection lConn = null;

		Vector lFascicoli = null;
		FascicoloSiepOnViewSqlDAO lFasDao = null;
		SoggettoSqlDAO lSoggDao = null;
		SentenzaSqlDAO lSentDao = null;
		try {
			lConn = getDBConnection();
			lFasDao = new FascicoloSiepOnViewSqlDAO(lConn);
			lFasDao.ricercaFascicoloOnViewPagedSuperSoggetti(aSogModel, aCodUfficio, aPage, TipoRicerca,
					StrCodiceDistrettoUtente, majorOffice, tipoUfficio, campoDet);
			lFascicoli = new Vector(lFasDao.getModels());

			if (lFascicoli.isEmpty())
				throw new SIEPException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			throw new SIEPException(F3BException.USER_MESSAGE,
					"FascicoloSiepController.ExRicercaFascicoloOnViewPagedSuperSoggetti: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lFasDao);
			cleanup(lSoggDao);
			cleanup(lSentDao);

			cleanup(lConn);
		}
		return lFascicoli;
	}

	/**
	 * paolo x supersoggetto 22 luglio 2009 Conta fascicolo paginato per SuperSoggetto
	 *
	 * @param aFascicoloSiep
	 * @param aPage
	 * @param TipoRicerca
	 * @param StrCodiceDistrettoUtente
	 * @return
	 * @throws F3BException
	 */
	public BigDecimal ExCountFascicoloOnViewPagedSuperSoggetti(SoggettoModel aSogModel, String aCodUfficio,
			int aPage, String TipoRicerca, String StrCodiceDistrettoUtente) throws F3BException {

		return ExCountFascicoloOnViewPagedSuperSoggetti(aSogModel, aCodUfficio, aPage, TipoRicerca,
				StrCodiceDistrettoUtente, "", "");
	}

	public BigDecimal ExCountFascicoloOnViewPagedSuperSoggetti(SoggettoModel aSogModel, String aCodUfficio,
			int aPage, String TipoRicerca, String StrCodiceDistrettoUtente, String majorOffice,
			String tipoUfficio) throws F3BException {

		Connection lConn = null;

		FascicoloSiepOnViewSqlDAO lFasDao = null;
		BigDecimal HowManyRecords = null;
		try {
			lConn = getDBConnection();
			lFasDao = new FascicoloSiepOnViewSqlDAO(lConn);
			lFasDao.ricercaFascicoloOnViewPagedSuperSoggetti(aSogModel, aCodUfficio, aPage, TipoRicerca,
					StrCodiceDistrettoUtente, majorOffice, tipoUfficio, "");
			lFasDao.start();
			lFasDao.next();
			HowManyRecords = lFasDao.getBigDecimal("HowManyRecords");

		} catch (DAOException daoEx) {
			throw new SIEPException(F3BException.USER_MESSAGE,
					"FascicoloSiepController.ExCountFascicoloOnViewPagedSuperSoggetti: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lFasDao);
			cleanup(lConn);
		}
		return HowManyRecords;
	}

	/**
	 * paolo x supersoggetto 22 luglio 2009 Vincenzo rivisto il 07/04/2010 x supersoggetto conto fascicoli
	 * paginato SuperSoggetto
	 *
	 * @param aFascicoloSiep
	 * @param aPage
	 * @param TipoRicerca
	 * @param StrCodiceDistrettoUtente
	 * @return
	 * @throws F3BException
	 */
	public BigDecimal ExGetCountFascicoliSuperSoggetto(SoggettoModel aSogModel,
			String lCodUfficioUtenteConnesso, String lCodDistrettoUtenteConnesso, String TipoRicerca)
			throws F3BException {

		int lCount = 0;
		Connection lConn = null;
		// SoggettoAliasFascicoloSqlDao lSqlDao = null;
		SoggettoFascicoloSqlDAO lSqlDao = null;

		try {
			lConn = getDBConnection();

			// lSqlDao = new SoggettoAliasFascicoloSqlDao(lConn);
			// lSqlDao.getCountSoggettoAliasFascicolo(aSogModel,
			// lCodUfficioUtenteConnesso,
			// lCodDistrettoUtenteConnesso, TipoRicerca);
			lSqlDao = new SoggettoFascicoloSqlDAO(lConn);
			lSqlDao.ricercaSuperSoggettoFascicoliBySoggetto(aSogModel, lCodUfficioUtenteConnesso, 0,
					lCodDistrettoUtenteConnesso, TipoRicerca);

			lSqlDao.start();
			while (lSqlDao.next())
				lCount++;

			lSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error(daoEx.getLocalizedMessage());
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"FascicoloSiepController.ExGetCountFascicoliSuperSoggetto: " + daoEx);
		} finally {
			cleanup(lSqlDao);

			cleanup(lConn);
		}

		return new BigDecimal(lCount);
	} // FINE

	// Ambros 08/2009 SuperSoggetto
	/**
	 * Ricerca Fascicoli Per SuperSoggetto
	 *
	 * @param aSogModel
	 * @param lCodUfficioUtenteConnesso
	 * @param aPage
	 * @param lCodDistrettoUtenteConnesso
	 * @param TipoRicerca
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaFascicoliBySuperSoggettoPaged(SoggettoModel aSogModel,
			String lCodUfficioUtenteConnesso, int aPage, String lCodDistrettoUtenteConnesso,
			String TipoRicerca) throws F3BException {

		return ExRicercaFascicoliBySuperSoggettoPaged(aSogModel, lCodUfficioUtenteConnesso, aPage,
				lCodDistrettoUtenteConnesso, TipoRicerca, "", false, "");
	}

	public Vector ExRicercaFascicoliBySuperSoggettoPaged(SoggettoModel aSogModel,
			String lCodUfficioUtenteConnesso, int aPage, String lCodDistrettoUtenteConnesso,
			String TipoRicerca, String majorOffice, boolean fromDetail, String tipoUfficio)
			throws F3BException {

		Connection lConn = null;
		Vector lFascicoli = new Vector();

		SoggettoFascicoloSqlDAO lSoggFasDao = null;
		SoggettoSqlDAO lSogDao = null;
		FascicoloSiepSqlDAO lFasDao = null;
		FascicoloSiepModel lFascicolo = null;
		// FascicoloSiepModel lFascicolo2 = null;

		// paolo cherubini per supersoggetto 30/07/2009
		SoggettoSqlDAO lSogSqlDao = null;
		// fine

		try {
			lConn = getDBConnection();
			lFasDao = new FascicoloSiepSqlDAO(lConn);
			lSoggFasDao = new SoggettoFascicoloSqlDAO(lConn);
			lSoggFasDao.ricercaSuperSoggettoFascicoliBySoggetto(aSogModel, lCodUfficioUtenteConnesso, aPage,
					lCodDistrettoUtenteConnesso, TipoRicerca, majorOffice, fromDetail, tipoUfficio);

			// paolo cherubini per supersoggetto 30/07/2009
			lSogSqlDao = new SoggettoSqlDAO(lConn);
			// fine

			lSoggFasDao.start();
			while (lSoggFasDao.next()) {
				if (TipoRicerca != null && TipoRicerca.equals("ufficio")) {
					lFascicolo = (FascicoloSiepModel) lSoggFasDao.getSoggettoFascicoliModel();
				} else if (TipoRicerca != null && TipoRicerca.equals("distretto")) {
					lFascicolo = (FascicoloSiepModel) lSoggFasDao.getSoggettoFascicoliModelDistre();
				}

				SoggettoModel lSogMod = new SoggettoModel();
				lSogMod = lFascicolo.getSoggetto();

				String Numero = lFascicolo.getNumFascicoli();
				// Ticket#20190904014 — Errore ricerca per anagrafica SIEP [SG]: gestione nullPointer
				// --> Esiste un soggetto con un unico fascicolo di classe compresa tra 700000 ed 800000
				boolean existUnico700000800000 = false;
				if (Numero.equals("1")) {
					// Ricerco IdSoggetto relativo alle accoppiate
					// Soggetto/procedimento trovate
					// lSoggFasDao.ricercaSuperSoggetto(lSogMod);
					lSogSqlDao.ricercaSuperSoggetto(TipoRicerca, lCodUfficioUtenteConnesso, lSogMod,
							"FASCICOLO_SIEP", lCodDistrettoUtenteConnesso, majorOffice, fromDetail,
							tipoUfficio);
					aSogModel = (SoggettoModel) lSogSqlDao.getModelByKey();
					lFascicolo.setSogIdSoggetto(aSogModel.getIdSoggetto());
					lFascicolo.getSoggetto().setIdSoggetto(aSogModel.getIdSoggetto());
					// Se l'accoppiata Soggetto/procedimento ha solo 1
					// procedimento a carico
					// cerco la descrizione dell'Uffico

					// paolo cherubini sostituisco con quella sotto
					// 28/09/2010 escludo dalla ricerca i fascicoli inseriti per
					// il cumulo
					/*
					 * lFasDao.ricercaFascicolo(lFascicolo); Vector lFascicoli2 = new
					 * Vector(lFasDao.getModels()); lFascicolo2 = (FascicoloSiepModel) lFascicoli2.get(0);
					 */
					FascicoloSiepModel lFascicolo3 = null;
					lFasDao.ricercaFascicoloProgFasc(lFascicolo, "FASCICOLO_SIEP");
					lFasDao.start();
					if (lFasDao.next()) {
						lFascicolo3 = (FascicoloSiepModel) lFasDao.getModelsPerRicUff();
						lFasDao.stop();
					}
					// paolo cherubini sostituisco con quella sotto
					// 28/09/2010 escludo dalla ricerca i fascicoli inseriti per
					// il cumulo
					// vedi anche sopra
					/*
					 * String Uff = lFascicolo2.getCodTipoUfficio(); String Di = " Di "; String Sed =
					 * lFascicolo2.getDescrComuneUfficio(); String Compl = Uff+Di+Sed;
					 */
					// Ticket#20190904014 — Errore ricerca per anagrafica SIEP [SG]: gestione nullPointer
					if (lFascicolo3 != null) {
						String Uff1 = lFascicolo3.getDescrTipoUfficio();
						String Di1 = " Di ";
						String Sed1 = lFascicolo3.getDescrComuneUfficio();
						String Compl1 = Uff1 + Di1 + Sed1;
						lFascicolo.setNumFascicoli(Compl1);
					} else
						existUnico700000800000 = true;
				} else {
					// Ricerco IdSoggetto relativo alle accoppiate
					// Soggetto/procedimento trovate
					lSogSqlDao.ricercaSuperSoggetto(TipoRicerca, lCodUfficioUtenteConnesso, lSogMod,
							"FASCICOLO_SIEP", lCodDistrettoUtenteConnesso, majorOffice, fromDetail,
							tipoUfficio);
					aSogModel = (SoggettoModel) lSogSqlDao.getModelByKey();
					lFascicolo.setSogIdSoggetto(aSogModel.getIdSoggetto());
					lFascicolo.getSoggetto().setIdSoggetto(aSogModel.getIdSoggetto());
					lFascicolo.getSoggetto().setEtaPresuntaAnni(aSogModel.getEtaPresuntaAnni());
					lFascicolo.getSoggetto().setEtaPresuntaMesi(aSogModel.getEtaPresuntaMesi());
				}
				// Ticket#20190904014 — Errore ricerca per anagrafica SIEP [SG]: gestione nullPointer
				if (!existUnico700000800000)
					lFascicoli.add(lFascicolo);
			}

			lSoggFasDao.stop();

			if (lFascicoli.isEmpty())
				throw new SIEPException(F3BException.USER_MESSAGE,
						"Nessun Soggetto individuato con i criteri di ricerca selezionati! ");
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new SIEPException(F3BException.USER_MESSAGE,
					"FascicoloSiepController.ExRicercaFascicoliBySuperSoggettoPaged: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("Exception: " + e);
			throw new SIEPException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lSoggFasDao);
			cleanup(lSogDao);
			cleanup(lFasDao);
			cleanup(lConn);

		}

		return lFascicoli;
	} // Chiude Ambros SuperSoggetto

	/**
	 * Conta Fascicoli Per SuperSoggetto
	 *
	 * @param aSogModel
	 * @param lCodUfficioUtenteConnesso
	 * @param aPage
	 * @param lCodDistrettoUtenteConnesso
	 * @param TipoRicerca
	 * @return
	 * @throws F3BException
	 */
	public BigDecimal ExCountFascicoliBySuperSoggettoPaged(SoggettoModel aSogModel,
			String lCodUfficioUtenteConnesso, int aPage, String lCodDistrettoUtenteConnesso,
			String TipoRicerca) throws F3BException {

		return ExCountFascicoliBySuperSoggettoPaged(aSogModel, lCodUfficioUtenteConnesso, aPage,
				lCodDistrettoUtenteConnesso, TipoRicerca, "", false, "");
	}

	public BigDecimal ExCountFascicoliBySuperSoggettoPaged(SoggettoModel aSogModel,
			String lCodUfficioUtenteConnesso, int aPage, String lCodDistrettoUtenteConnesso,
			String TipoRicerca, String majorOffice, boolean fromDetail, String tipoUffcio)
			throws F3BException {

		Connection lConn = null;
		SoggettoFascicoloSqlDAO lSoggFasDao = null;
		BigDecimal HowManyRecords = null;

		try {
			lConn = getDBConnection();
			lSoggFasDao = new SoggettoFascicoloSqlDAO(lConn);
			lSoggFasDao.ricercaSuperSoggettoFascicoliBySoggetto(aSogModel, lCodUfficioUtenteConnesso, aPage,
					lCodDistrettoUtenteConnesso, TipoRicerca, majorOffice, fromDetail, tipoUffcio);
			lSoggFasDao.start();
			lSoggFasDao.next();
			HowManyRecords = lSoggFasDao.getBigDecimal("HowManyRecords");

		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new SIEPException(F3BException.USER_MESSAGE,
					"FascicoloSiepController.ExCountFascicoliBySuperSoggettoPaged: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("Exception: " + e);
			throw new SIEPException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lSoggFasDao);
			cleanup(lConn);
		}

		return HowManyRecords;
	} // Chiude conta SuperSoggetto
		// Cherubini 03/2010

	/**
	 * Deassocia residenza al fascicolo SIEP DeAssocia la Residenza al Fascicolo
	 *
	 * @param aResidenza
	 * @return ResidenzaAssociataModel
	 * @throws F3BException
	 */
	public ResidenzaAssociataModel ExDeassociaResidenzaFascicoloSiep(ResidenzaAssociataModel aResidenza)
			throws F3BException {

		Connection lConn = null;

		ResidenzaFascicoloSiepDAO lResFascDao = null;
		ResidenzaFascicoloSiepModel lResidenzaFascicolo = aResidenza.getResidenzaFascicoloSiep();

		try {
			lConn = getDBTransaction();
			lResFascDao = new ResidenzaFascicoloSiepDAO(lConn);
			lResFascDao.setDataFineValidita(lResidenzaFascicolo.getDataInizioValidita());
			lResFascDao.setCondizioneResFascCorrente(lResidenzaFascicolo.getFasSieIdFascicoloSiep(),
					lResidenzaFascicolo.getResIdResidenza());
			lResFascDao.update();
			lResFascDao.stop();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("FascicoloSiepController.ExDeassociaResidenzaFascicoloSiep", ex);
			throw new F3BException("FascicoloSiepController.ExDeassociaResidenzaFascicoloSiep : " + ex);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error(ex);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("FascicoloSiepController.ExDeassociaResidenzaFascicoloSiep", ex);
			throw new F3BException("FascicoloSiepController.ExDeassociaResidenzaFascicoloSiep : " + ex);
		} finally {
			cleanup(lResFascDao);
			cleanup(lConn);
		}

		return aResidenza;
	}

	/**
	 * Archiviazione del fascicolo SIEP
	 *
	 * @param aFasModel
	 *            , StatoProcedimentoModel aStato.
	 * @return aFasModel
	 * @throws F3BException
	 * @author Luigi
	 */
	public FascicoloSiepModel ExArchiviazione(FascicoloSiepModel aFasModel, StatoProcedimentoModel aStato)
			throws F3BException {

		Connection lConn = null;

		FascicoloSiepDAO lFasDao = null;
		StatoProcedimentoDAO lStatoProcDao = null;
		try {
			lConn = getDBTransaction();

			// Aggiornamento FASCICOLO
			lFasDao = new FascicoloSiepDAO(lConn);
			lFasDao.setCodOperatoreAggiornamento(aFasModel.getCodOperatoreAggiornamento());
			lFasDao.setDataAggiornamento(aFasModel.getDataAggiornamento());
			lFasDao.setCodUfficioAggiornamento(aFasModel.getCodUfficioAggiornamento());
			lFasDao.setDataArchiviazione(aFasModel.getDataArchiviazione());
			lFasDao.setCodStatoFascicolo(aFasModel.getCodStatoFascicolo());
			lFasDao.setCodMotivoArchiviazione(aFasModel.getCodMotivoArchiviazione());
			lFasDao.selCondizioneUpdate(aFasModel.getIdFascicoloSiep());
			lFasDao.update();
			lFasDao.stop();

			// Inserimento nuovo record in STATO_PROCEDIMENTO
			lStatoProcDao = new StatoProcedimentoDAO(lConn);
			lStatoProcDao.setCondizioneByIdFascicolo(aFasModel.getIdFascicoloSiep());
			lStatoProcDao.delete();
			lStatoProcDao.stop();

			lStatoProcDao.setDAOFromModel(aStato);
			lStatoProcDao.insert();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new SIEPException("FascicoloSiepController.ExArchiviazione: " + ex);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("Exception: " + e);
			throw new SIEPException("FascicoloSiepController.ExArchiviazione: " + e);
		} finally {
			cleanup(lFasDao);
			cleanup(lStatoProcDao);
			cleanup(lConn);
		}

		return aFasModel;
	}

	public FascicoloSiepModel ExRicercaFascicoloSiepByIdSentenza(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		FascicoloSiepSqlDAO lFasDao = null;
		FascicoloSiepModel lFasMod = new FascicoloSiepModel();
		try {
			lConn = getDBConnection();
			lFasDao = new FascicoloSiepSqlDAO(lConn);
			lFasDao.ricercaFascicoloByIDSentenza(aKey);
			lFasDao.start();
			if (lFasDao.next()) {
				lFasMod = (FascicoloSiepModel) lFasDao.getModelsPerIdSentenza();
				lFasDao.stop();
			}
		} catch (DAOException daoEx) {
			throw new F3BException("FascicoloSiepController.ExRicercaFascicoloSiepIdSentenza: " + daoEx);
		} finally {
			cleanup(lFasDao);
			cleanup(lConn);
		}
		return lFasMod;
	}

	public ByteArrayOutputStream ExGetCertificatoPenale(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		FascicoloSiepSqlDAO lFasDao = null;
		ByteArrayOutputStream lByteArrayOut = null;
		try {
			lConn = getDBConnection();
			lFasDao = new FascicoloSiepSqlDAO(lConn);

			lFasDao.getCertificatoPenaleByIdFascicolo(aKey);
			lFasDao.start();

			if (lFasDao.next()) {
				lByteArrayOut = lFasDao.getBlob("CERTIFICATO_PENALE");
				lFasDao.stop();
			}

			if (lByteArrayOut == null)
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Certificato Giudiziale Associato");

			if (lByteArrayOut.size() == 0)
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Certificato Giudiziale Associato");
		} catch (F3BException eF3b) {
			throw eF3b;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			cleanup(lFasDao);
			cleanup(lConn);
		}
		return lByteArrayOut;
	}

	public void ExInsertCertificatoPenale(FascicoloSiepCertBlobModel fascicolo) throws F3BException {

		Connection lConn = null;
		FascicoloSiepDAO lFasDao = null;

		try {
			lConn = getDBConnection();
			lFasDao = new FascicoloSiepDAO(lConn);
			lFasDao.setDAOFromModelForUpdateBlob(fascicolo);
			lFasDao.selCondizioneUpdate(fascicolo.getFascicoloSiep().getIdFascicoloSiep());
			lFasDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException(
					"FascicoloSiepController.ExInsertCertificatoPenale: Non posso inserire: " + ex);
		} finally {
			cleanup(lFasDao);
			cleanup(lConn);
		}
	}

	public BigDecimal ExGetLengthCertPenaleByIdFascicolo(BigDecimal aIdFascicoloSius) throws F3BException {
		Connection lConn = null;

		FascicoloSiepSqlDAO lFasDao = null;
		BigDecimal lengthCertPenale = null;

		try {
			lConn = getDBConnection();

			lFasDao = new FascicoloSiepSqlDAO(lConn);

			lFasDao.getLengthCertPenaleByIdFascicolo(aIdFascicoloSius);
			lFasDao.start();

			if (lFasDao.next()) {
				lengthCertPenale = lFasDao.getBigDecimal("LEN_BLOB_CERT_PENALE");
			}

			lFasDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new SIEPException(F3BException.USER_MESSAGE,
					"FascicoloSiepController.ExRicercaLengthCertPenaleByIdFascicolo: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lFasDao);
			cleanup(lConn);
		}

		return lengthCertPenale;
	}

	/**
	 * Ricerca tutti i fascicoli associati a un soggetto avente lo stesso Nome e Cognome del soggetto passato
	 * in input
	 *
	 * @param aSoggettoModel
	 *            SoggettoModel con valorizzati nome e cognome per la ricerca
	 * @return Vector <FascicoloSiepModel> con valorizzato anche la property mSoggetto con i dati del soggetto
	 *         legato al fascicolo.
	 */
	public Vector<FascicoloSiepModel> ExRicercaFascicoliPerSoggetto(SoggettoModel aSoggettoModel)
			throws F3BException {

		Connection lConn = null;
		SoggettoFascicoloSqlDAO lSoggFasSqlDao = null;

		Vector<FascicoloSiepModel> lListaFascioli = new Vector();

		try {
			lConn = getDBConnection();

			lSoggFasSqlDao = new SoggettoFascicoloSqlDAO(lConn);

			lSoggFasSqlDao.ricercaFascicoliESoggettoPerSoggettoBDI(aSoggettoModel);

			lSoggFasSqlDao.start();

			while (lSoggFasSqlDao.next()) {
				FascicoloSiepModel lFasMod = (FascicoloSiepModel) lSoggFasSqlDao
						.getModelRicercaFascicoliESoggettoPerSoggettoBDI();
				lListaFascioli.add(lFasMod);
			}

			lSoggFasSqlDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException("FascicoloSiepController.ExRicercaFascicoliPerSoggetto: " + daoEx);
		} finally {
			cleanup(lSoggFasSqlDao);
			cleanup(lConn);
		}
		return lListaFascioli;

	}

	// MEV 15 - Revisione SIGE
	/**
	 * Ricerca Fascicoli SIEP di un Soggetto (Nel model aSogModel è valorizzato l'ID) in base ai parametri di
	 * ricerca selezionati.
	 * <p>
	 *
	 * @param aSogModel
	 * @param strCodUfficioUtenteConnesso
	 * @param lCodDistretto
	 * @return lFascicoli
	 * @throws F3BException
	 */
	public Vector ExRicercaFascSIEPDelSoggetto(SoggettoModel aSogModel, String strCodUfficioUtenteConnesso,
			String lCodDistretto) throws F3BException {

		Connection lConn = null;
		Vector lFascicoli = new Vector();
		SoggettoSqlDAO lSogDao = null;
		FascicoloSiepSqlDAO lFasSqlDao = null;
		SentenzaSqlDAO lSentDao = null;
		StatoProcedimentoSqlDAO lStaDao = null;

		SentenzaModel lSentMod = null;

		try {
			lConn = getDBConnection();

			lSentDao = new SentenzaSqlDAO(lConn);
			lStaDao = new StatoProcedimentoSqlDAO(lConn);

			// Si Popola di tutti i dati il model del soggetto.
			lSogDao = new SoggettoSqlDAO(lConn);
			lSogDao.ricercaSoggettoByKey(aSogModel.getIdSoggetto());
			aSogModel = (SoggettoModel) lSogDao.getModelByKey();

			if (aSogModel == null)
				throw new SIEPException(F3BException.USER_MESSAGE, "Soggetto associato non trovato");

			lFasSqlDao = new FascicoloSiepSqlDAO(lConn);

			lFasSqlDao.ricercaFascicoliDelSoggetto(aSogModel, strCodUfficioUtenteConnesso, lCodDistretto);
			lFasSqlDao.start();

			FascicoloSiepModel lFascicolo = null;

			while (lFasSqlDao.next()) {
				lFascicolo = ((FascicoloSiepModel) lFasSqlDao.getModel());

				lFascicolo.setSoggetto(aSogModel);

				lSentDao.ricercaSentenzaBykey(lFascicolo.getSenIdSentenza());
				lSentMod = (SentenzaModel) lSentDao.getModelByKey();

				if (lSentMod == null)
					throw new SIEPException(F3BException.USER_MESSAGE, "Sentenza associato non trovato");

				lStaDao.ricercaMaxStatoProcedimentoByFascicoloSiep(lFascicolo.getIdFascicoloSiep());
				lFascicolo.setCodStatoProcedimento(lStaDao.getCodStatoByKey());

				lFascicolo.setSentenza(lSentMod);
				lFascicoli.add(lFascicolo);
			}

			lFasSqlDao.stop();

			if (lFascicoli.isEmpty())
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (F3BException fE) {
			throw fE;
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("FascicoloSiepController.ExRicercaFascSIEPDelSoggetto: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("Exception: " + e);
			throw new F3BException("FascicoloSiepController.ExRicercaFascSIEPDelSoggetto: " + e);
		} finally {
			cleanup(lFasSqlDao);
			cleanup(lSogDao);
			cleanup(lSentDao);
			cleanup(lStaDao);
			cleanup(lConn);
		}

		return lFascicoli;
	}

	@Override
	public Vector<FascicoloSiepModel> ExRicercaFascicoloSiepBySuperSoggettoPerSIGEPaged(
			SoggettoModel aSogModel, int aPageNum) throws F3BException {
		Connection lConn = null;
		Vector<FascicoloSiepModel> lFascicoli = new Vector<>();
		FascicoloSiepSoggettoSqlDAO lFasSoggDao = null;
		StatoProcedimentoSqlDAO lStaDao = null;

		try {
			lConn = getDBConnection();
			lFasSoggDao = new FascicoloSiepSoggettoSqlDAO(lConn);
			lStaDao = new StatoProcedimentoSqlDAO(lConn);

			lFasSoggDao.ricercaFascicoloSuperSoggettoPerSige(aSogModel);
			// lFasSoggDao.start();
			lFasSoggDao.startPage1(aPageNum);
			FascicoloSiepModel lFascicolo = null;

			while (lFasSoggDao.next()) {
				lFascicolo = (FascicoloSiepModel) lFasSoggDao.getModel();

				// Ricerca stato Luigi 28-9-2004
				lStaDao.ricercaMaxStatoProcedimentoByFascicoloSiep(lFascicolo.getIdFascicoloSiep());
				lFascicolo.setCodStatoProcedimento(lStaDao.getCodStatoByKey());

				lFascicoli.add(lFascicolo);
			}

			lFasSoggDao.stop();

			if (lFascicoli.isEmpty())
				throw new SIEPException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new SIEPException(F3BException.USER_MESSAGE,
					"FascicoloSiepController.ExRicercaFascicoloSiepBySuperSoggettoPaged: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lFasSoggDao);
			cleanup(lStaDao);
			cleanup(lConn);
		}

		return lFascicoli;
	}

	// MEV_57: aggiunto parametro di passaggio
	public Vector<FascicoloSiepModel> ExRicercaFascicoloSiepBySoggettoPerSIGEPaged(SoggettoModel aSogModel,
			int aPageNum, String majorOffice) throws F3BException {
		Connection lConn = null;
		Vector lFascicoli = new Vector();
		FascicoloSiepSoggettoSqlDAO lFasSoggDao = null;
		StatoProcedimentoSqlDAO lStaDao = null;

		try {
			lConn = getDBConnection();
			lFasSoggDao = new FascicoloSiepSoggettoSqlDAO(lConn);
			lStaDao = new StatoProcedimentoSqlDAO(lConn);

			// MEV_57: aggiunto parametro di passaggio
			if (StringUtils.checkValidValue(majorOffice))
				lFasSoggDao.ricercaFascicoloMajorSoggettoPerSige(aSogModel, majorOffice);
			else
				lFasSoggDao.ricercaFascicoloSoggettoPerSIGE(aSogModel);
			// lFasSoggDao.start();
			lFasSoggDao.startPage1(aPageNum);
			FascicoloSiepModel lFascicolo = null;

			while (lFasSoggDao.next()) {
				lFascicolo = (FascicoloSiepModel) lFasSoggDao.getModel();

				// Ricerca stato Luigi 28-9-2004
				lStaDao.ricercaMaxStatoProcedimentoByFascicoloSiep(lFascicolo.getIdFascicoloSiep());
				lFascicolo.setCodStatoProcedimento(lStaDao.getCodStatoByKey());

				lFascicoli.add(lFascicolo);
			}

			lFasSoggDao.stop();

			if (lFascicoli.isEmpty())
				throw new SIEPException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new SIEPException(F3BException.USER_MESSAGE,
					"FascicoloSiepController.ExRicercaFascicoloSiepBySoggettoPaged: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lFasSoggDao);
			cleanup(lStaDao);
			cleanup(lConn);
		}

		return lFascicoli;
	}

	/**
	 * MERGE v10: aggiunta funzione di ricerca dettaglio Ricerca il Fascicolo in Banca Dati per primary key.
	 * Vengono recuperati i dati per: - residuenza - domicilio - posidzione giuridica - avvocati - reati e
	 * circostanze - circostanze - pena complessiva e Snazione Sostitutiva - pena accessorie - benefici - pena
	 * residua (ultima validata) - pena presunta - posizione materiale - Magistrato Competente - Misura
	 * Alternativa - Stato Procedimento - Eventi () - Misure Cautelari - Misure Sicurezza - liberazione
	 * Anticipata (concessa)
	 *
	 * @param aModel
	 * @return FascicoloSiepModel
	 * @throws F3BException
	 */
	public DettaglioFascicoloModel ExDettaglioFascicoloSiepNew(BigDecimal aIdFascicolo) throws F3BException {

		Connection lConn = null;

		DettaglioFascicoloModel lDettaglio = null;
		ResidenzaSqlDAO lResDao = null;
		AvvocatoSqlDAO lAvvDao = null;
		AvvocatoFascicoloSiepSqlDAO lAvvFasSieSqlDao = null;
		PenaAccessoriaSqlDAO lPenAccDao = null;
		MisuraCautelareSqlDAO lMisCauDao = null;
		BeneficioSqlDAO lBeneDao = null;
		CircostanzaSqlDAO lCircDao = null;
		PenaResiduaDAO lPenResDao = null;
		EventoDAO lEveDao = null;
		PenaPresuntaSqlDAO lPenPresDAO = null;
		StatoProcedimentoSqlDAO lStatProcDAO = null;
		MisuraSicurezzaSqlDAO lMisSicuDAO = null;
		MagistratoCompetenteMagistratoSqlDAO lMagDAO = null;
		LicenzaLibanticipataSqlDAO lLicSqlDao = null;
		PosizioneMaterialeFascModel lPosizioneMateriale = null;
		PosizioneMaterialeFascSqlDAO lPosMatDAO = null;
		MisuraAlternativaSqlDAO lMisDao = null;
		StatoProcedimentoSqlDAO lStaDao = null;
		ScambioSanzioneSqlDAO lScambioDao = null;
		// Note Fascicolo
		NoteFascicoloSqlDAO lNotFasDao = null;
		RichiestaConversioneSqlDAO lRCSqlDAO = null;
		NuovaIstanzaSqlDAO lNISqlDAO = null;
		AgdgFascicoloSiepSqlDAO lAGDGFasSiepSqlDAO = null;
		AltriGradiGiudizioSqlDAO lAGDGSqlDAO = null;

		// MEV26 - Cumulo
		PenaRideterminataCumuloSqlDAO lPenaRidetCumuloModSqlDao = null;

		NuovaIstanzaSqlDAO lNuovaIstanzaSqlDao = null;
		try {
			lConn = getDBConnection();

			lScambioDao = new ScambioSanzioneSqlDAO(lConn);
			lResDao = new ResidenzaSqlDAO(lConn);
			lPenPresDAO = new PenaPresuntaSqlDAO(lConn);
			lAvvDao = new AvvocatoSqlDAO(lConn);
			lAvvFasSieSqlDao = new AvvocatoFascicoloSiepSqlDAO(lConn);
			lPenAccDao = new PenaAccessoriaSqlDAO(lConn);
			lMisCauDao = new MisuraCautelareSqlDAO(lConn);
			lBeneDao = new BeneficioSqlDAO(lConn);
			lPenResDao = new PenaResiduaDAO(lConn);
			lEveDao = new EventoDAO(lConn);
			lStatProcDAO = new StatoProcedimentoSqlDAO(lConn);
			lMisSicuDAO = new MisuraSicurezzaSqlDAO(lConn);
			lMagDAO = new MagistratoCompetenteMagistratoSqlDAO(lConn);
			lPosMatDAO = new PosizioneMaterialeFascSqlDAO(lConn);
			lRCSqlDAO = new RichiestaConversioneSqlDAO(lConn);
			lNISqlDAO = new NuovaIstanzaSqlDAO(lConn);
			lAGDGFasSiepSqlDAO = new AgdgFascicoloSiepSqlDAO(lConn);
			lAGDGSqlDAO = new AltriGradiGiudizioSqlDAO(lConn);

			lDettaglio = new DettaglioFascicoloModel();
			// Dati del fascicolo
			lDettaglio.setFascicoloSiep(ExRicercaFascicoloByKey(aIdFascicolo));

			// Residenza (al massimo 1)
			lResDao.ricercaResidenzaByFascicolo(aIdFascicolo);
			lDettaglio.setResidenza((ResidenzaModel) lResDao.getModelByKey());

			// Domicilio (al massimo 1)
			lResDao.ricercaDomicilioByFascicolo(aIdFascicolo);
			lDettaglio.setDomicilio((ResidenzaModel) lResDao.getModelByKey());

			// Posizione Giuridica (l'ultima + Luogo Detenzione + Altra Causa)
			IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
			PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPosLuoAltMod = lPosCtrl
					.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaMisuraCautelareCorrentiByIdFascicolo(
							aIdFascicolo);

			lDettaglio.setPosizioneGiuridica(lPosLuoAltMod.getPosizioneGiuridica());
			lDettaglio.setLuogoDetenzione(lPosLuoAltMod.getLuogoDetenzione());
			lDettaglio.setAltraCausa(lPosLuoAltMod.getAltraCausa());

			// Avvocati (n)
			AvvocatoModel lAvvMod = new AvvocatoModel();
			AvvocatoFascicoloSiepModel lAvvFascMod = new AvvocatoFascicoloSiepModel();
			AvvocatoSiepModel lAvvSiepModel = new AvvocatoSiepModel();
			lAvvFascMod.setFasSieIdFascicoloSiep(aIdFascicolo);
			lAvvDao.ricercaAvvocatoAttualeFascicolo(lAvvMod, lAvvFascMod);
			lDettaglio.setAvvocati(new ArrayList(lAvvDao.getModels()));

			// AvvocatiSIEP.
			ArrayList lArrayAvvFascSiep = new ArrayList();
			for (int i = 0; i < lDettaglio.getAvvocati().size(); i++) {
				AvvocatoModel lAvv = (AvvocatoModel) lDettaglio.getAvvocati().get(i);
				lAvvFasSieSqlDao = new AvvocatoFascicoloSiepSqlDAO(lConn);
				lAvvFasSieSqlDao.ricercaAvvocatoFascicoloSiepByIdAvvocatoIdFascicolo(lAvv.getIdAvvocato(),
						lDettaglio.getFascicoloSiep().getIdFascicoloSiep());
				lAvvSiepModel = (AvvocatoSiepModel) lAvvFasSieSqlDao.getModelByKey();
				if (lAvvSiepModel != null && lAvvSiepModel.getAvvocatoFascicoloSiepModel() != null)
					lArrayAvvFascSiep.add(lAvvSiepModel.getAvvocatoFascicoloSiepModel());
			}
			lDettaglio.setAvvocatiSIEP(lArrayAvvFascSiep);

			// Reati e Circostanze dei reati (n)
			IReato lReaCtrl = SIEPLookupRemote.getReatoRemote();
			Vector lReati = lReaCtrl.ExRicercaReatoCircostanzaByFascicolo(aIdFascicolo);
			lDettaglio.setReatiCircostanze(lReati);

			// Circostanze (n)
			ICircostanza lCirCtr = SIEPLookupRemote.getCircostanzaRemote();
			Vector lCircostanze = lCirCtr.ExRicercaCircostanzaDescByIdFascicolo(aIdFascicolo);
			lDettaglio.setCircostanze(lCircostanze);

			// Pena Complessiva e Sanzione Sostitutiva (al massimo 1 e al
			// massimo 1)
			IPenaComplessiva lPenComCtr = SIEPLookupRemote.getPenaComplessivaRemote();
			PenaComplessivaSanzioneSostitutivaModel lPenCompSanzSost = lPenComCtr
					.ExRicercaPenaComplessivaSanzioneSostitutivaByIdFascicoloSiep(aIdFascicolo);
			lDettaglio.setPenaComplessivaSanzioneSostitutiva(lPenCompSanzSost);

			// Pene Accessorie (n)
			lPenAccDao.ricercaPenaAccessoriaByFascicolo(aIdFascicolo);
			lDettaglio.setPeneAccessorie(new ArrayList(lPenAccDao.getModels()));

			// Benefici (n)
			BeneficioModel lBeneMod = new BeneficioModel();
			lBeneMod.setFasSieIdFascicoloSiep(aIdFascicolo);
			lBeneDao.ricercaBeneficio(lBeneMod);
			lDettaglio.setBenefici(new ArrayList(lBeneDao.getModels()));

			// Pena Residua
			IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
			PenaResiduaModel lPenResMod = lPenResCtrl.ExRicercaPenaResiduaUltimaByDate(aIdFascicolo);
			lDettaglio.setPenaResidua(lPenResMod);

			// Pena Presunta
			IPenaPresunta lPenPresCtrl = SIEPLookupRemote.getPenaPresuntaRemote();
			PenaPresuntaModel lPenPresMod = lPenPresCtrl
					.ExRicercaPenaPresuntaCorrenteByFascicoloSiep(aIdFascicolo);
			lDettaglio.setPenaPresunta(lPenPresMod);

			// Pena In Cumulo
			if (lDettaglio.getFascicoloSiep().getFlagCumulante() != null
					&& lDettaglio.getFascicoloSiep().getFlagCumulante().equals("S")) {
				// Recupero l'ultima pena legata a un cumulo (se esiste)
				IPenaCumulo lCtrlPenaCumulo = SIEPLookupRemote.getPenaCumuloRemote();
				PenaCumuloModel lPenaCumulo = lCtrlPenaCumulo
						.ExRicercaUltimaPenaCumuloByIdFascicolo(aIdFascicolo);
				lDettaglio.setPenaCumulo(lPenaCumulo);

				// MEV26 - Provo a recuperare anche da PENA_RIDETERMINATA_CUMULO
				// se
				// nuovo cumulo
				lPenaRidetCumuloModSqlDao = new PenaRideterminataCumuloSqlDAO(lConn);
				lPenaRidetCumuloModSqlDao.ricercaPeneRideterminateCumuloByIdFascDataInsDesc(aIdFascicolo);
				PenaRideterminataCumuloModel lPenRidetCumuloNew = (PenaRideterminataCumuloModel) lPenaRidetCumuloModSqlDao
						.getModelByKey();

				if (lPenRidetCumuloNew != null) {
					lDettaglio.setPenaCumuloNew(lPenRidetCumuloNew);
				}
				lPenaRidetCumuloModSqlDao.stop();

			}

			// Posizione Materiale
			lPosizioneMateriale = new PosizioneMaterialeFascModel();
			lPosMatDAO.ricercaPosizioneMaterialeFascAttivaXFas(aIdFascicolo);
			lPosizioneMateriale = (PosizioneMaterialeFascModel) lPosMatDAO.getModelByKey();
			if (lPosizioneMateriale != null)
				lDettaglio.setPosizioneMateriale(lPosizioneMateriale);

			// Magistrato Competente
			lMagDAO.ricercaMagistratoCompetenteByFascicolo(aIdFascicolo);
			MagistratoCompetenteMagistratoModel lMagComMod = (MagistratoCompetenteMagistratoModel) lMagDAO
					.getModelByKey();
			lDettaglio.setMagistratoCompetente(lMagComMod);

			// Misura Alternativa
			lMisDao = new MisuraAlternativaSqlDAO(lConn);
			lMisDao.ricercaMisuraAlternativaByIdFascicolo(aIdFascicolo);
			MisuraAlternativaModel lMisMod = (MisuraAlternativaModel) lMisDao.getModelByKey();

			lDettaglio.setMisuraAlternativa(lMisMod);

			// scambio sanzioni sostitutive
			String[] aTipoDecisione = null;
			String[] aNaturaSanzione = { "0156", "0157", "0159" };
			String[] aTipoSanzione = { "2470", "2471" };
			lScambioDao.ricercaByIdFascicoloNaturaTipo(aIdFascicolo, aTipoDecisione, aNaturaSanzione,
					aTipoSanzione);
			ScambioSanzioneModel lScaSanMod = (ScambioSanzioneModel) lScambioDao.getModelByKey();
			lDettaglio.setScambioSanzione(lScaSanMod);

			DatiSiepPerTrasferimentoModel lDatiSiep = new DatiSiepPerTrasferimentoModel();
			lNISqlDAO.ricercaNuovaIstanzaByIdFascicolo(aIdFascicolo);
			Vector lNuovaIstanza = new Vector(lNISqlDAO.getModels());
			if (lNuovaIstanza != null && lNuovaIstanza.size() > 0)
				lDatiSiep.setListNuovaIstanza(lNuovaIstanza);

			lAGDGFasSiepSqlDAO.ricercaAgdgFascicoloSiepByIdFasSiep(aIdFascicolo);
			Vector lAltriGradiGiudizio = new Vector(lAGDGFasSiepSqlDAO.getModels());
			if (lAltriGradiGiudizio != null && lAltriGradiGiudizio.size() > 0)
				lDatiSiep.setListAltriGradiGiudizio(lAltriGradiGiudizio);

			// Richiesta conversione Pene Pecuniarie.
			lRCSqlDAO.ricercaRichiestaConversioneByIdFasSIEP(aIdFascicolo);
			Vector lRicConversioni = new Vector(lRCSqlDAO.getModels());
			if (lRicConversioni != null && lRicConversioni.size() > 0)
				lDatiSiep.setListRichiesteConversioniPP(lRicConversioni);

			if (lDatiSiep != null)
				lDettaglio.setDatiSiepPerTrasferimento(lDatiSiep);

			// Stato Procedimento
			lStaDao = new StatoProcedimentoSqlDAO(lConn);
			lStaDao.ricercaStatoProcedimentoByFascicoloSiep(aIdFascicolo);
			Vector lStatPrcVec = new Vector(lStaDao.getModels());

			lDettaglio.setStatoProcedimento(lStatPrcVec);

			// Eventi
			IEventoSimeone lCtrl = SICOLookupRemote.getEventoSimeoneRemote();
			try {
				String[] lTipoEvento = { "01", "02", "03", "14" };
				String[] lTipoProv = { "02", "03" };
				Vector lEve = lCtrl.ExRicercaEventoNotificaByFascicoloSiepTipEventoNOTTipProvNONAnnullati(
						aIdFascicolo, lTipoEvento, lTipoProv);

				// ==============================================================================
				// ULTIMI EVENTI modifica per prendere il corretto contenuto
				// della nuova istanza
				// ================================================================================
				lNuovaIstanzaSqlDao = new NuovaIstanzaSqlDAO(lConn);
				NuovaIstanzaModel lNuovaIstanzaMod = new NuovaIstanzaModel();
				for (int i = 0; i < Math.min(lEve.size(), 2); i++) {
					EventoNotificaModel lEveNotificaMod = (EventoNotificaModel) lEve.get(i);
					EventoModel lEveMod = lEveNotificaMod.getEvento();
					if (lEveMod != null && lEveMod.getCodMotivo() != null
							&& "0993".equals(lEveMod.getCodMotivo())) {
						lNuovaIstanzaSqlDao.ricercaNuovaIstanzaByEveIdEvento(lEveMod.getIdEvento());
						lNuovaIstanzaMod = (NuovaIstanzaModel) lNuovaIstanzaSqlDao.getModelByKey();
						// 20170912: [SG] aggiunto controllo di consistenza
						if (lNuovaIstanzaMod != null && lNuovaIstanzaMod.getDescrContenuto() != null)
							lEveMod.setDescrMotivo(lNuovaIstanzaMod.getDescrContenuto());
					}
				}

				lDettaglio.setEventi(lEve);
			} catch (F3BException e) {
			}

			// Misure Cautelari (n)
			lMisCauDao.newRicercaMisuraCautelareByFascicolo(aIdFascicolo);
			lDettaglio.setMisureCautelari(new ArrayList(lMisCauDao.getModels()));

			// Misure Sicurezza (n)
			lMisSicuDAO.ricercaMisuraSicurezzaByIdFascicoloOrd(aIdFascicolo);
			lDettaglio.setMisureSicurezza(new ArrayList(lMisSicuDAO.getModels()));

			// ========================================================================
			// Vengono recuperati i GG di LA concessi (presi in carico e
			// associati a
			// un evento SIES VALIDATI) già detratti o da detrarre
			// Nuova gestione LA (12/2006)
			// ========================================================================
			CalcoloPenaControllerF5 lCtrlF5 = new CalcoloPenaControllerF5();
			CalcoloPenaModel lCalcPenaModel = lCtrlF5.exGetPenaIniziale(aIdFascicolo, null);
			Vector lListaLA = lCtrlF5.exGetLiberazioneAnticipata(aIdFascicolo, lCalcPenaModel.getDataDal(),
					null);
			lCalcPenaModel.setLibAnticipate(lListaLA);

			lDettaglio.setCalcoloPenaModel(lCalcPenaModel);

			// Nuova L.A. - differenziare i giorni da concedere di L.A. , L.A.
			// Speciale , L.A.
			// Integrazione
			// - differenziare i giorni già concessi di L.A. , L.A. Speciale ,
			// L.A. Integrazione

			int totGiorniLAConcessi = lCalcPenaModel.getLiberazioneAnticipataGiaConcesse();
			int totGiorniLADaConcedere = lCalcPenaModel.getLiberazioneAnticipataDaConcedere();
			lDettaglio.setGiorniLibConcessa(new BigDecimal(totGiorniLAConcessi));
			lDettaglio.setGiorniLibNonConcessa(new BigDecimal(totGiorniLADaConcedere));

			// gg da concedere
			int totGiorniDaConcedereLA = lCalcPenaModel.getLiberazioneAnticipataDaConcedereLA();
			lDettaglio.setGiorniLibNONConcessaLA(new BigDecimal(totGiorniDaConcedereLA));
			int totGiorniDaConcedereLS = lCalcPenaModel.getLiberazioneAnticipataDaConcedereLS();
			lDettaglio.setGiorniLibNONConcessaLS(new BigDecimal(totGiorniDaConcedereLS));
			int totGiorniDaConcedereLI = lCalcPenaModel.getLiberazioneAnticipataDaConcedereLI();
			lDettaglio.setGiorniLibNONConcessaLI(new BigDecimal(totGiorniDaConcedereLI));

			// gg già concessi
			int totGiorniConcessiLA = lCalcPenaModel.getLiberazioneAnticipataGiaConcesseLA();
			lDettaglio.setGiorniLibConcessaLA(new BigDecimal(totGiorniConcessiLA));
			int totGiorniConcessiLS = lCalcPenaModel.getLiberazioneAnticipataGiaConcesseLS();
			lDettaglio.setGiorniLibConcessaLS(new BigDecimal(totGiorniConcessiLS));
			int totGiorniConcessiLI = lCalcPenaModel.getLiberazioneAnticipataGiaConcesseLI();
			lDettaglio.setGiorniLibConcessaLI(new BigDecimal(totGiorniConcessiLI));

			// End Nuova L.A.
			// ========================================================================
			// DL 92/2014
			// ========================================================================
			int totGiorniDL92Detratti = lCalcPenaModel.getRimediRisarcitoriGiaConcessi();
			lDettaglio.setGiorniDL92Detratti(new BigDecimal(totGiorniDL92Detratti));

			int totGiorniDL92NONDetratti = lCalcPenaModel.getRimediRisarcitoriDaConcedere();
			lDettaglio.setGiorniDL92NONDetratti(new BigDecimal(totGiorniDL92NONDetratti));
			// ========================================================================
			// Recupero i dati del differimento se presenti
			// ========================================================================
			lDettaglio = ricercaProvDifferimento(lDettaglio, aIdFascicolo);

			// Aggiunte le note dispositivo
			lNotFasDao = new NoteFascicoloSqlDAO(lConn);
			lNotFasDao.ricercaNoteFascicoloByKey(aIdFascicolo);
			NoteFascicoloModel lNoteMod = (NoteFascicoloModel) lNotFasDao.getModelByKey();
			if (lNoteMod != null && lNoteMod.getNotaDispositivo() != null
					&& lNoteMod.getNotaDispositivo().length() > 1) {
				lDettaglio.setNoteFascicolo(lNoteMod.getNotaDispositivo());
			}
		} catch (F3BException fex) {
			if (fex.getErrorCode() == F3BException.USER_MESSAGE) {
				throw fex;
			} else {
				throw new F3BException("FascicoloSiepController.ExDettaglioFascicoloSiepNew: " + fex);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("Errore in FascicoloSiepController.ExDettaglioFascicoloSiepNew", ex);
			throw new SIEPException("FascicoloSiepController.ExDettaglioFascicoloSiepNew: " + ex);
		} finally {
			cleanup(lResDao);
			cleanup(lAvvDao);
			cleanup(lPenAccDao);
			cleanup(lMisCauDao);
			cleanup(lBeneDao);
			cleanup(lCircDao);
			cleanup(lPenResDao);
			cleanup(lEveDao);
			cleanup(lPenPresDAO);
			cleanup(lStatProcDAO);
			cleanup(lMisSicuDAO);
			cleanup(lMagDAO);
			cleanup(lLicSqlDao);
			cleanup(lPosMatDAO);
			cleanup(lNotFasDao);
			cleanup(lMisDao);
			cleanup(lStaDao);
			cleanup(lScambioDao);
			cleanup(lRCSqlDAO);
			cleanup(lNuovaIstanzaSqlDao);
			cleanup(lAGDGFasSiepSqlDAO);
			cleanup(lAGDGSqlDAO);
			cleanup(lPenaRidetCumuloModSqlDao);

			cleanup(lConn);
		}
		// valore di ritorno
		return lDettaglio;
	}

	/**
	 * Ricerca Fascicolo Siep per Progressivo, Anno e Ufficio
	 *
	 * // [EC] - 16/01/2018: - ANOMALIA VISIBILITA MINORE SIEP: creo nuovo metodo passando anche il controllo
	 * su ufficio minorenne o meno
	 *
	 * @param aFascicoloSiep
	 * @return Vettore di FascicoloSiepModel
	 * @throws F3BException
	 */
	public FascicoloSiepModel ExRicercaFascicoloSiepByProgrAnnoCodUfficio(FascicoloSiepModel aFascicoloSiep,
			String checkMajor) throws F3BException {

		Connection lConn = null;

		FascicoloSiepModel lFascicolo = null;
		FascicoloSiepSqlDAO lFasDao = null;
		SoggettoSqlDAO lSoggDao = null;
		SentenzaSqlDAO lSentDao = null;

		try {
			lConn = getDBConnection();

			lFasDao = new FascicoloSiepSqlDAO(lConn);
			lSoggDao = new SoggettoSqlDAO(lConn);
			lSentDao = new SentenzaSqlDAO(lConn);

			lFasDao.ricercaFascicoloByProgrAnnoCodUfficio(aFascicoloSiep, checkMajor);
			lFasDao.start();

			SoggettoModel lSoggMod = null;
			SentenzaModel lSentMod = null;

			if (lFasDao.next()) {
				lFascicolo = (FascicoloSiepModel) lFasDao.getModel();

				lSoggDao.ricercaSoggettoByKey(lFascicolo.getSogIdSoggetto());
				lSoggMod = (SoggettoModel) lSoggDao.getModelByKey();

				lFascicolo.setSoggetto(lSoggMod);

				lSentDao.ricercaSentenzaBykey(lFascicolo.getSenIdSentenza());
				lSentMod = (SentenzaModel) lSentDao.getModelByKey();

				lFascicolo.setSentenza(lSentMod);
			}

			lFasDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new SIEPException(F3BException.USER_MESSAGE,
					"FascicoloSiepController.ExRicercaFascicoloSiep: Non posso leggere : " + daoEx);
			// } catch (SQLException sqe) {
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			// siesLogger.error("SQLException: " + sqe);
			// throw new SIEPException(F3BException.USER_MESSAGE,
			// "FascicoloSiepController.ExRicercaFascicoloSiep: Non posso leggere : " + sqe);
		} finally {
			cleanup(lFasDao);
			cleanup(lSoggDao);
			cleanup(lSentDao);

			cleanup(lConn);
		}

		return lFascicolo;
	}

	/**
	 * Ritorna n.ro di record risultato di una ExGetNumFascicoloSiepBySoggettoSIGE.
	 *
	 * @param SoggettoModel
	 * @return BigDecimal n.ro di record
	 * @throws F3BException
	 */
	public BigDecimal ExGetNumFascicoloSiepBySoggettoSIGE(SoggettoModel aSogModel) throws F3BException {

		Connection lConn = null;
		FascicoloSiepSoggettoSqlDAO lFasSoggDao = null;
		BigDecimal lCont = new BigDecimal(0);

		try {
			lConn = getDBConnection();

			lFasSoggDao = new FascicoloSiepSoggettoSqlDAO(lConn);
			lFasSoggDao.ricercaFascicoloSoggettoPerSIGE(aSogModel);
			lCont = lFasSoggDao.getNumRowsSelected();

		} catch (DAOException daoEx) {
			throw new SIEPException(F3BException.USER_MESSAGE,
					"FascicoloSiepController.ExGetNumFascicoloSiepBySoggettoSIGE: Non posso leggere : "
							+ daoEx);
			// } catch (SQLException sqe) {
			// throw new SIEPException(F3BException.USER_MESSAGE,
			// "FascicoloSiepController.ExGetNumFascicoloSiepBySoggetto: Non posso leggere : " + sqe);
		} finally {
			cleanup(lFasSoggDao);
			cleanup(lConn);
		}
		return lCont;
	}

	// MEV 26 CUMULO -step 2 - Ricerca Procedimento By reato
	public BigDecimal ExRicercaIstruttoriaCumuloByIdFascicoloSiep(BigDecimal aIdFasc) throws F3BException {
		Connection lConn = null;

		FascicoloSiepSqlDAO lFasDao = null;
		BigDecimal IdIstru = null;

		try {
			lConn = getDBConnection();

			lFasDao = new FascicoloSiepSqlDAO(lConn);

			lFasDao.ExRicercaIstruttoriaCumuloByIdFascicoloSiep(aIdFasc);
			lFasDao.start();

			if (lFasDao.next()) {
				IdIstru = lFasDao.getBigDecimal("ID_ISTRUTTORIA_CUMULO");
			}

			lFasDao.stop();
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new SIEPException(F3BException.USER_MESSAGE,
					"FascicoloSiepController.ExRicercaIstruttoriaCumuloByIdFascicoloSiep: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lFasDao);
			cleanup(lConn);
		}

		return IdIstru;
	}

}