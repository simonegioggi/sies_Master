package siap.siep.penasospesa.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.report.ReportGenerator;
import f3b.util.xml.TreeModel;
import siap.controller.SiapController;
import siap.sico.camponota.dao.CampoNotaDAO;
import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistratocompetente.dao.MagistratoCompetenteDAO;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.magistratocompetente.model.MagistratoCompetenteModel;
import siap.sico.residenza.dao.ResidenzaDAO;
import siap.sico.residenza.dao.ResidenzaFascicoloSiepDAO;
import siap.sico.residenza.model.ResidenzaFascicoloSiepModel;
import siap.sico.residenza.model.ResidenzaModel;
import siap.sico.soggetto.dao.SoggettoDAO;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.stampa.controller.IStampa;
import siap.sico.template.controller.TemplateManager;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.annotazionemanuale.dao.AnnotazioneManualeDAO;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.autoritaesterna.dao.AutoritaEsternaDAO;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.beneficio.dao.BeneficioDAO;
import siap.siep.beneficio.model.BeneficioModel;
import siap.siep.circostanza.dao.CircostanzaDAO;
import siap.siep.circostanza.model.CircostanzaModel;
import siap.siep.fascicolo.dao.FascicoloSiepDAO;
import siap.siep.fascicolo.dao.FascicoloSiepSqlDAO;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misuracautelare.dao.MisuraCautelareDAO;
import siap.siep.misuracautelare.model.MisuraCautelareModel;
import siap.siep.notifica.dao.NotificaDAO;
import siap.siep.penaaccessoria.dao.PenaAccessoriaDAO;
import siap.siep.penaaccessoria.model.PenaAccessoriaModel;
import siap.siep.penacomplessiva.dao.PenaComplessivaDAO;
import siap.siep.penacomplessiva.dao.PenaComplessivaSqlDAO;
import siap.siep.penacomplessiva.model.DettaglioPenaComplessivaModel;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penacomplessiva.model.PenaComplessivaSanzioneSostitutivaModel;
import siap.siep.penasospesa.dao.AnnmanPenacomplDAO;
import siap.siep.penasospesa.dao.AnnmanPenacomplSqlDAO;
import siap.siep.penasospesa.dao.AnnmanReatoDAO;
import siap.siep.penasospesa.dao.AnnmanReatoSqlDAO;
import siap.siep.penasospesa.model.AnnmanPenacomplModel;
import siap.siep.penasospesa.model.AnnmanReatoModel;
import siap.siep.posizione.dao.PosizioneGiuridicaDAO;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.reato.dao.ReatoDAO;
import siap.siep.reato.dao.ReatoSqlDAO;
import siap.siep.reato.model.ReatoCircostanzaModel;
import siap.siep.reato.model.ReatoModel;
import siap.siep.sanzionesostitutiva.dao.SanzioneSostitutivaDAO;
import siap.siep.sanzionesostitutiva.dao.SanzioneSostitutivaSqlDAO;
import siap.siep.sanzionesostitutiva.model.SanzioneSostitutivaModel;
import siap.siep.scadenzario.dao.ScadenzarioDAO;
import siap.siep.scadenzario.dao.ScadenzarioSqlDAO;
import siap.siep.scadenzario.model.ScadenzarioModel;
import siap.siep.statoprocedimento.dao.StatoProcedimentoDAO;
import siap.siep.statoprocedimento.model.StatoProcedimentoModel;

/**
 * <p>
 * Title: PenaAccessoriaController
 * </p>
 * <p>
 * Description: Classe Controller per PenaAccessoria
 * </p>
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
public class PenaSospesaController extends SiapController implements IPenaSospesa {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public EventoModel ExInserisciAnnotazioneRevoca(EventoModel aPenaSospesa) throws F3BException {

		Connection lConn = null;
		EventoDAO lPenDao = null;

		try {
			lConn = getDBConnection();
			lPenDao = new EventoDAO(lConn);
			lPenDao.setDAOFromModel(aPenaSospesa);
			aPenaSospesa.setEveIdEvento(lPenDao.insert());

			commit(lConn);
			return aPenaSospesa;
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("PenaAccessoriaController.ExInserisci: Non posso inserire: " + ex);
		} catch (Exception sqe) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + sqe);
			throw new F3BException(
					"PenaAccessoriaController.ExInserisciPenaAccessoria: Non posso inserire -> " + sqe);
		} finally {
			cleanup(lPenDao);
			cleanup(lConn);
		}
	}

	/**
	 * controller utilizzato per l'inserimento di un fascicolo di pena detentiva (CLasse I) a partire da un
	 * Fascicolo Pena Sospesa (CLasse III) duplica il soggetto per non incorrere nell'errore sulla chiave
	 * univoca fascicolo-soggetto-sentenza e comunque in linea con le nuove disposizione del SuperSoggetto.
	 * inserisce evento di Annotazione Revoca Pena sospesa sul Fascicolo di classe III archivia il Fascicolo
	 * di classe III inserisce il nuovo fascicolo di pena detentiva (Classe I) AMBROSINO -- 04/2013 Duplica le
	 * PeneAccessorie e i Benefici ( dal procedimento Classe III al Classe I)
	 *
	 * @param: aSoggetto
	 *             Model con i dati da inserire aEvento Model con i dati da inserire aFascicoloSiep Model con
	 *             i dati da inserire
	 * @return il model aFascicolo con i dati inseriti e l'aggiunta dell'id del record inserito
	 * @throws F3BException
	 */
	public FascicoloSiepModel ExInserisciFascicolodaClasseIII(SoggettoModel aSoggetto, EventoModel aEvento,
			FascicoloSiepModel aFascicoloSiep, DettaglioFascicoloModel aDettaglioFascicolo,
			FascicoloSiepModel aFascicoloClasseIIISiep, AnnotazioneManualeModel AnnMan) throws F3BException {

		Connection lConn = null;

		SoggettoDAO lSogDao = null;
		EventoDAO lEveDao = null;
		EventoDAO lEveDaoRevo = null;
		FascicoloSiepDAO lFasDao = null;
		FascicoloSiepSqlDAO lFasDaoSql = null;
		ResidenzaDAO lResDao = null;
		ResidenzaFascicoloSiepDAO lResFasDao = null;
		PosizioneGiuridicaDAO lPosDao = null;
		MagistratoCompetenteDAO lMagComDao = null;
		ReatoDAO lReaDao = null;
		CircostanzaDAO lCirDao = null;
		PenaComplessivaDAO lPenDao = null;
		// 19/04/2019 - MEV70 Aggiunto il passaggio della Misura Cautelare da Classe III a Classe I.
		MisuraCautelareDAO lMCDao = null;
		AnnotazioneManualeDAO lAnnotazManClasseIDao = null;
		PenaAccessoriaDAO lPenAccDao = null;
		BeneficioDAO lBeneDao = null;

		try {
			lConn = getDBTransaction();
			// duplico il soggetto
			lSogDao = new SoggettoDAO(lConn);
			lSogDao.setDAOFromModel(aSoggetto);
			BigDecimal lSequence = lSogDao.insert();
			aSoggetto.setIdSoggetto(lSequence);

			// ========================================================================
			// preparo il nuovo fascicolo classe I collegandolo al nuovo soggetto
			// ========================================================================
			aFascicoloSiep.setSogIdSoggetto(lSequence);

			lFasDao = new FascicoloSiepDAO(lConn);
			// Cerco il Progressivo rispettivamente al tipo progressivo impostato
			lFasDaoSql = new FascicoloSiepSqlDAO(lConn);
			int nTipo = 1;
			aFascicoloSiep.setTipoProgressivo(nTipo);
			aFascicoloSiep.setCodStatoFascicolo("02"); // Iscritto
			lFasDaoSql.getProgressivoFascicoloSiep(aFascicoloSiep);
			lFasDaoSql.start();
			int lMaxProgr = 0;

			if (lFasDaoSql.next() && (lFasDaoSql.getInt("aMAX") > 0))
				lMaxProgr = lFasDaoSql.getInt("aMAX");
			lFasDaoSql.stop();

			// Setto la ChiaveProgressivo del Model con il MAX + 1 a seconda del tipo...
			int lTipoProgr = aFascicoloSiep.getTipoProgressivo();
			if (lMaxProgr == 0) {
				if (lTipoProgr == 1)
					aFascicoloSiep.setChiaveProgr(new BigDecimal(1));
				else
					aFascicoloSiep.setChiaveProgr(new BigDecimal(lTipoProgr * 10000 + 1));
			} else {
				aFascicoloSiep.setChiaveProgr(new BigDecimal(lMaxProgr + 1));
			}

			// inserisco il nuovo fascicolo di classe I
			lFasDao.setDAOFromModel(aFascicoloSiep);
			lSequence = lFasDao.insert();
			aFascicoloSiep.setIdFascicoloSiep(lSequence);

			lPenDao = new PenaComplessivaDAO(lConn);
			if (aDettaglioFascicolo.getPenaComplessivaSanzioneSostitutiva() != null) {
				if (aDettaglioFascicolo.getPenaComplessivaSanzioneSostitutiva()
						.getPenaComplessiva() != null) {
					PenaComplessivaModel lPenMod = aDettaglioFascicolo.getPenaComplessivaSanzioneSostitutiva()
							.getPenaComplessiva();
					lPenMod.setCodOperatoreInserimento(aFascicoloSiep.getCodOperatoreInserimento());
					lPenMod.setCodUfficioInserimento(aFascicoloSiep.getCodUfficioInserimento());
					lPenMod.setDataInserimento(DateUtils.getSysDate());
					lPenMod.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
					lPenDao.setDAOFromModel(lPenMod);
					lSequence = lPenDao.insert();
				}
			}

			// stato_procedimento prendo l'ultimo statoprocedimento della lista (size-1)
			StatoProcedimentoDAO lStaDao = null;
			lStaDao = new StatoProcedimentoDAO(lConn);
			StatoProcedimentoModel lStaMod = (StatoProcedimentoModel) aDettaglioFascicolo
					.getStatoProcedimento().get(aDettaglioFascicolo.getStatoProcedimento().size() - 1);
			lStaMod.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
			lStaMod.setProgressivo(new BigDecimal(1));
			lStaMod.setCodStatoProcedimento("0108"); // iscritto
			lStaMod.setData(null);
			lStaDao.setDAOFromModel(lStaMod);
			lStaDao.insert();

			// residenza se sono vuoti???
			if (aDettaglioFascicolo.getResidenza() != null) {
				lResDao = new ResidenzaDAO(lConn);
				ResidenzaModel lResMod = aDettaglioFascicolo.getResidenza();
				lResMod.setSogIdSoggetto(aFascicoloSiep.getSogIdSoggetto());
				lResDao.setDAOFromModel(lResMod);
				lSequence = lResDao.insert();

				// collego la residenza al nuovo fascicolo
				lResFasDao = new ResidenzaFascicoloSiepDAO(lConn);
				ResidenzaFascicoloSiepModel lResFasMod = new ResidenzaFascicoloSiepModel();
				lResFasMod.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
				lResFasMod.setResIdResidenza(lSequence);
				lResFasDao.setDAOFromModel(lResFasMod);
				lSequence = lResFasDao.insert();
			}

			// duplico domicilio collegandolo al nuovo soggetto
			if (aDettaglioFascicolo.getDomicilio() != null) {
				lResDao = new ResidenzaDAO(lConn);
				ResidenzaModel lResMod = aDettaglioFascicolo.getDomicilio();
				lResMod.setSogIdSoggetto(aFascicoloSiep.getSogIdSoggetto());
				lResDao.setDAOFromModel(lResMod);
				lSequence = lResDao.insert();

				// collego il domicilio al nuovo fascicolo
				lResFasDao = new ResidenzaFascicoloSiepDAO(lConn);
				ResidenzaFascicoloSiepModel lResFasMod = new ResidenzaFascicoloSiepModel();
				lResFasMod.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
				lResFasMod.setResIdResidenza(lSequence);
				lResFasDao.setDAOFromModel(lResFasMod);
				lSequence = lResFasDao.insert();
			}
			/*
			 * // duplico posizione giuridica collegandolo al nuovo fascicolo if
			 * (aDettaglioFascicolo.getPosizioneGiuridica() != null){ lPosDao = new
			 * PosizioneGiuridicaDAO(lConn); PosizioneGiuridicaModel lPosMod =
			 * aDettaglioFascicolo.getPosizioneGiuridica();
			 * lPosMod.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
			 * lPosDao.setDAOFromModel(lPosMod ); lSequence = lPosDao.insert(); }
			 */
			// creo posizione giuridica a libero
			lPosDao = new PosizioneGiuridicaDAO(lConn);
			PosizioneGiuridicaModel lPosMod = new PosizioneGiuridicaModel();
			lPosMod.setCodPosizioneGiuridica("07");
			lPosMod.setCodPosizioneProcessuale("-");
			lPosMod.setCodOperatoreInserimento(aFascicoloSiep.getCodOperatoreInserimento());
			lPosMod.setCodUfficioInserimento(aFascicoloSiep.getCodUfficioInserimento());
			lPosMod.setDataInserimento(DateUtils.getSysDate());
			lPosMod.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
			lPosDao.setDAOFromModel(lPosMod);
			lSequence = lPosDao.insert();

			// duplico magistrato collegandolo al nuovo fascicolo
			if (aDettaglioFascicolo.getMagistratoCompetente() != null) {
				if (aDettaglioFascicolo.getMagistratoCompetente().getMagistratoCompetente() != null) {
					lMagComDao = new MagistratoCompetenteDAO(lConn);

					MagistratoCompetenteMagistratoModel lMagistrato = aDettaglioFascicolo
							.getMagistratoCompetente();
					MagistratoCompetenteModel lMagMod = lMagistrato.getMagistratoCompetente();

					lMagMod = lMagistrato.getMagistratoCompetente();
					lMagMod.setMagCodMagistrato(lMagistrato.getMagistrato().getCodMagistrato());
					lMagMod.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
					lMagComDao.setDAOFromModel(lMagMod);
					lMagComDao.insert();
				}
			}

			// Insert Reato e le circostanze sempre e solo da tabella REATO
			if (aDettaglioFascicolo.getReatiCircostanze() != null) {
				// prendo la lista dei reati e la metto nel model comune
				lReaDao = new ReatoDAO(lConn);
				List lLisReaCir = aDettaglioFascicolo.getReatiCircostanze();
				ReatoModel lReaMod = new ReatoModel();

				// ciclo su questa lista
				for (int i = 0; i <= lLisReaCir.size() - 1; i++) {
					// prendo il reato per scriverlo
					lReaMod = ((ReatoCircostanzaModel) lLisReaCir.get(i)).getReato();
					// scrivo reato
					lReaMod.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
					lReaDao.setDAOFromModel(lReaMod);
					lSequence = lReaDao.insert();

					// prendo la lista delle circostanze affogate nella tabella reati
					ReatoModel[] lLisCir = ((ReatoCircostanzaModel) lLisReaCir.get(i)).getCircostanze();
					// Ciclo sulle n° circostanze afffogate nella tabella REATI
					for (int j = 0; j <= lLisCir.length - 1; j++) {
						// prendo la circostanza per scriverla
						lReaMod = lLisCir[j];
						// scrivo la circostanza
						lReaMod.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
						lReaDao.setDAOFromModel(lReaMod);
						lSequence = lReaDao.insert();
					}
				}
			}

			// circostanze
			if (aDettaglioFascicolo.getCircostanze() != null) {
				CircostanzaModel lCirMod = new CircostanzaModel();
				List lLisCir = aDettaglioFascicolo.getCircostanze();
				lCirDao = new CircostanzaDAO(lConn);
				for (int i = 0; i <= lLisCir.size() - 1; i++) {
					lCirMod = (CircostanzaModel) lLisCir.get(i);
					lCirMod.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
					lCirDao.setDAOFromModel(lCirMod);
					lSequence = lCirDao.insert();
				}
			}

			// AMBROSINO 04/2013 - Inizio duplicazione PeneAccessorie e Benefici
			// MEV PACK 1 - Che possono essere legati tra loro

			// PENE ACCESSORIE

			int Kcont = 0;
			BigDecimal lSequenceBen = null;

			BigDecimal[] lidBeneficio;
			lidBeneficio = new BigDecimal[10];

			BigDecimal[] lidBeneficioNEW;
			lidBeneficioNEW = new BigDecimal[10];

			if (aDettaglioFascicolo.getPeneAccessorie() != null) {
				List listaPeneAcc = aDettaglioFascicolo.getPeneAccessorie();
				for (int i = 0; i <= listaPeneAcc.size() - 1; i++) {
					lPenAccDao = null;
					PenaAccessoriaModel lPenAccMod = new PenaAccessoriaModel(
							(PenaAccessoriaModel) listaPeneAcc.get(i));

					lPenAccMod.setCodOperatoreInserimento(aFascicoloSiep.getCodOperatoreInserimento());
					lPenAccMod.setCodUfficioInserimento(aFascicoloSiep.getCodUfficioInserimento());
					lPenAccMod.setDataInserimento(aFascicoloSiep.getDataInserimento());
					lPenAccMod.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
					lPenAccMod.setCodOperatoreAggiornamento(null);
					lPenAccMod.setCodUfficioAggiornamento(null);
					lPenAccMod.setDataAggiornamento(null);
					// lPenAccMod.setNumeroEventiCorrelati(aValore); ????

					// controllo che P.Acc. non sia legata ad un Beneficio : Se si
					// devo scrivere prima il Beneficio
					if (lPenAccMod.getBenIdBeneficio() != null) {
						List listaBenefici = aDettaglioFascicolo.getBenefici();
						// BeneficioModel lBeneMod = new BeneficioModel();
						// cerco il Beneficio al quale è legata la P.Acc
						for (int j = 0; j <= listaBenefici.size() - 1; j++) {
							lBeneDao = null;
							BeneficioModel lBeneMod = new BeneficioModel(
									(BeneficioModel) listaBenefici.get(j));
							if (lBeneMod.getIdBeneficio().equals(lPenAccMod.getBenIdBeneficio())) {
								// controllo che il Beneficio non sia stato già scritto
								String ScrivoSiNo = "SI";
								for (int ic = 0; ic <= lidBeneficio.length - 1; ic++) {
									if (lBeneMod.getIdBeneficio().equals(lidBeneficio[ic])) {
										ScrivoSiNo = "NO";
										lSequenceBen = lidBeneficioNEW[ic];
									}
								}

								if (ScrivoSiNo.equals("SI")) {
									lBeneMod.setCodOperatoreInserimento(
											aFascicoloSiep.getCodOperatoreInserimento());
									lBeneMod.setCodUfficioInserimento(
											aFascicoloSiep.getCodUfficioInserimento());
									lBeneMod.setDataInserimento(DateUtils.getSysDate());
									lBeneMod.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
									lBeneMod.setCodOperatoreAggiornamento(null);
									lBeneMod.setCodUfficioAggiornamento(null);
									lBeneMod.setDataAggiornamento(null);

									lBeneDao = new BeneficioDAO(lConn);
									lBeneDao.setDAOFromModel(lBeneMod);
									lSequence = lBeneDao.insert();
									lBeneDao.stop();
									// annoto id del beneficio di classe III e l'id appena scritto del
									// Beneficio in classe I
									lidBeneficio[Kcont] = lBeneMod.getIdBeneficio();
									lidBeneficioNEW[Kcont] = lSequence;
									Kcont++;
									// scrivo PenaAccess legata al Beneficio
									lPenAccMod.setBenIdBeneficio(lSequence);
									lPenAccDao = new PenaAccessoriaDAO(lConn);
									lPenAccDao.setDAOFromModel(lPenAccMod);
									lSequence = lPenAccDao.insert();
									lPenAccDao.stop();
								} else {
									// scrivo Solo la P.Acc legata ad un Beneficio già scritto in precedenza
									lPenAccMod.setBenIdBeneficio(lSequenceBen);
									lPenAccDao = new PenaAccessoriaDAO(lConn);
									lPenAccDao.setDAOFromModel(lPenAccMod);
									lSequence = lPenAccDao.insert();
									lPenAccDao.stop();
								}
							} // END if(lBeneMod.getIdBeneficio().equals(lPenAccMod.getBenIdBeneficio()
						} // End ciclo for j
					} // End if(lPenAccMod.getBenIdBeneficio() != null)
					else {
						lPenAccDao = new PenaAccessoriaDAO(lConn);
						lPenAccDao.setDAOFromModel(lPenAccMod);
						lSequence = lPenAccDao.insert();
						lPenAccDao.stop();
					}
				} // End ciclo for (int i=0
			} // End if (aDettaglioFascicolo.getPeneAccessorie() != null)

			// --> BENEFICI -- (solo quelli che non erano legati alle PeneAccessorie
			if (aDettaglioFascicolo.getBenefici() != null) {
				List listaBenefici = aDettaglioFascicolo.getBenefici();
				for (int i = 0; i <= listaBenefici.size() - 1; i++) {
					lBeneDao = null;
					BeneficioModel lBeneMod = new BeneficioModel((BeneficioModel) listaBenefici.get(i));
					lBeneMod.setCodOperatoreInserimento(aFascicoloSiep.getCodOperatoreInserimento());
					lBeneMod.setCodUfficioInserimento(aFascicoloSiep.getCodUfficioInserimento());
					lBeneMod.setDataInserimento(DateUtils.getSysDate());
					lBeneMod.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
					lBeneMod.setCodOperatoreAggiornamento(null);
					lBeneMod.setCodUfficioAggiornamento(null);
					lBeneMod.setDataAggiornamento(null);

					String ScrivoSiNo = "SI";
					for (int ic = 0; ic <= lidBeneficio.length - 1; ic++) {
						// Controllo che non è un Beneficio legato alle P.Acc. e che quindi ho già scritto
						if (lBeneMod.getIdBeneficio().equals(lidBeneficio[ic]))
							ScrivoSiNo = "NO";
					}

					if (ScrivoSiNo.equals("SI")) {
						lBeneDao = new BeneficioDAO(lConn);
						lBeneDao.setDAOFromModel(lBeneMod);
						lSequence = lBeneDao.insert();
						lBeneDao.stop();
					}
				} // End ciclo for i
			} // End if Dettaglio.getbenefici
				// FINE AMBROSINO 04/2013 - duplicazione PeneAccessorie e Benefici da Classe III a classe I

			// 19/04/2019 - MEV70 Aggiunto il passaggio delle Misure Cautelari da Classe III a Classe I.
			if (aDettaglioFascicolo.getMisureCautelari() != null) {
				MisuraCautelareModel lMCMod = new MisuraCautelareModel();
				List lListaMC = aDettaglioFascicolo.getMisureCautelari();
				lMCDao = new MisuraCautelareDAO(lConn);
				for (int i = 0; i <= lListaMC.size() - 1; i++) {
					lMCMod = (MisuraCautelareModel) lListaMC.get(i);
					lMCMod.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
					lMCDao.setDAOFromModel(lMCMod);
					lSequence = lMCDao.insert();
				}
			}

			// ========================================================================
			// Archivio il vecchio fascicolo di classe III
			// ========================================================================
			FascicoloSiepDAO lFascDao = new FascicoloSiepDAO(lConn);
			lFascDao.selCondizioneUpdate(aFascicoloClasseIIISiep.getIdFascicoloSiep());
			lFascDao.setCodStatoFascicolo("01"); // 01 - Archiviato/Definito
			lFascDao.setDataArchiviazione(DateUtils.getSysDate());
			lFascDao.setCodMotivoArchiviazione("12"); // 12 - Revoca Beneficio ex artt 168 c.p.-674 c.p.p.
			lFascDao.setFlagValidato("S");
			lFascDao.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
			lFascDao.setCodOperatoreAggiornamento(aFascicoloSiep.getCodOperatoreAggiornamento());
			lFascDao.setCodUfficioAggiornamento(aFascicoloSiep.getCodUfficioAggiornamento());
			lFascDao.setDataAggiornamento(aFascicoloSiep.getDataAggiornamento());

			lFascDao.update();
			lFascDao.stop();

			// =============================================================================================
			// Inserisco l'evento di Annotazione Revoca Beneficio ex artt 168 c.p.-674 c.p.p. in Classe III
			// =============================================================================================
			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModel(aEvento);
			BigDecimal lSequenceEveIII = lEveDao.insert();

			// ========================================================================
			// Inserisco i dati della sentenza di revoca nella tab ANNOTAZIONE_MANUALE classe III se non sono
			// stati trovati nel distretto ma inseriti a mano dall'utente
			// ========================================================================
			if (!AnnMan.equals(null)) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("ANNOTAZIONE per i dati della sentenza->" + AnnMan);
				AnnotazioneManualeDAO lAnnotazioneManualeDao = new AnnotazioneManualeDAO(lConn);
				AnnMan.setEveIdEvento(lSequenceEveIII);
				lAnnotazioneManualeDao.setDAOFromModel(AnnMan);
				BigDecimal lSeqAnn = lAnnotazioneManualeDao.insert();

				lEveDao = new EventoDAO(lConn);
				lEveDao.setAnnIdAnnotazioneManuale(lSeqAnn);
				lEveDao.selCondizioneUpdate(lSequenceEveIII);
				lEveDao.update();
			}

			// ================================================================
			// 10/05/2014 -MEV PACK 1
			// Se è REVOCA BENEFICIO, Duplico anche in Classe I
			// EVENTO e ANNOTAZIONE_MANUALE
			// ================================================================

			BigDecimal lSequenceEveI = null;
			if (aEvento.getCodTipoEvento().equals("01") && aEvento.getCodTipoProvvedimento().equals("25")
					&& (aEvento.getCodMotivo().equals("1100") || aEvento.getCodMotivo().equals("1101")
							|| aEvento.getCodMotivo().equals("1102") || aEvento.getCodMotivo().equals("1103")
							|| aEvento.getCodMotivo().equals("1104") || aEvento.getCodMotivo().equals("1105")
							|| aEvento.getCodMotivo().equals("1106") || aEvento.getCodMotivo().equals("1107")
							|| aEvento.getCodMotivo().equals("1108"))) {
				lEveDaoRevo = new EventoDAO(lConn);
				lEveDaoRevo.setDAOFromModel(aEvento);
				lEveDaoRevo.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
				lSequenceEveI = lEveDaoRevo.insert();
			}

			// ========================================================================
			// Annotazione_Manuale
			// ========================================================================
			if (!AnnMan.equals(null) && lSequenceEveI != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("ANNOTAZIONE per i dati della sentenza in Classe I -> ");
				lAnnotazManClasseIDao = new AnnotazioneManualeDAO(lConn);

				AnnMan.setEveIdEvento(lSequenceEveI);
				AnnMan.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());

				lAnnotazManClasseIDao.setDAOFromModel(AnnMan);
				BigDecimal lSeqAnnClI = lAnnotazManClasseIDao.insert();

				// Lego EVENTO Claase I all'ANNOTAZIONE_MANUALE
				lEveDaoRevo = new EventoDAO(lConn);
				lEveDaoRevo.setAnnIdAnnotazioneManuale(lSeqAnnClI);
				lEveDaoRevo.selCondizioneUpdate(lSequenceEveI);
				lEveDaoRevo.update();
			}
			// END 10/05/2014 -MEV PACK 1
			// ================================================================

			// ========================================================================
			// Inserisco lo Stato del procedimento per il fascicolo di classe III
			// ========================================================================
			StatoProcedimentoDAO lStatoProcDao = null;
			lStatoProcDao = new StatoProcedimentoDAO(lConn);
			lStatoProcDao.setCondizioneByIdFascicolo(aFascicoloClasseIIISiep.getIdFascicoloSiep());
			lStatoProcDao.delete();
			lStatoProcDao.stop();

			StatoProcedimentoModel lStat = new StatoProcedimentoModel();
			lStat.setFasSieIdFascicoloSiep(aFascicoloClasseIIISiep.getIdFascicoloSiep());
			lStat.setProgressivo(new BigDecimal(1));
			lStat.setDataInserimento(DateUtils.getSysDate());
			lStat.setCodUfficioInserimento(aFascicoloSiep.getCodUfficioInserimento());
			lStat.setCodOperatoreInserimento(aFascicoloSiep.getCodOperatoreInserimento());
			lStat.setCodStatoProcedimento("0349"); // Definito - Archiviazione per Revoca Beneficio ex artt
													// 168 c.p.-674 c.p.p.
			lStatoProcDao.setDAOFromModel(lStat);
			lStatoProcDao.insert();

			// ========================================================================
			// Cancello gli scadenzari di tipo "Termine Sospensione Condizionale" (16) e
			// "Termine Ottemperanza Obblighi (17) per il fascicolo di classe III
			// che ho archiviato
			// ========================================================================
			ScadenzarioSqlDAO lScaSqlDao = new ScadenzarioSqlDAO(lConn);
			ScadenzarioDAO lScaDao = new ScadenzarioDAO(lConn);
			ScadenzarioModel lScaMod = null;
			// Tipo di scadenzari da cancellare
			String[] lTipoScad = { "16", "17" };

			for (int i = 0; i < 2; i++) {
				lScaSqlDao.ricercaScadenzarioByTipoScadenzarioIdFascicolo(lTipoScad[i],
						aFascicoloClasseIIISiep.getIdFascicoloSiep());
				lScaMod = (ScadenzarioModel) lScaSqlDao.getModelByKey();
				if (lScaMod != null && lScaMod.getIdScadenzario() != null) {
					lScaDao.setCondizioneDelete(lScaMod.getIdScadenzario());
					lScaDao.delete();
					lScaDao.stop();
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Cancellazione Scadenzario tipo " + lTipoScad[i]);
				}
			}
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);
			throw new F3BException(
					"PenaSospesaController.ExInserisciFascicolodaClasseIII: Non posso inserire: " + ex);
		} finally {
			cleanup(lSogDao);
			cleanup(lEveDao);
			cleanup(lFasDao);
			cleanup(lFasDaoSql);
			cleanup(lResDao);
			cleanup(lResFasDao);
			cleanup(lPosDao);
			cleanup(lMagComDao);
			cleanup(lReaDao);
			cleanup(lCirDao);
			cleanup(lMCDao); // MEV70
			cleanup(lPenDao);
			cleanup(lEveDaoRevo);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lBeneDao);
			cleanup(lPenAccDao);
			cleanup(lAnnotazManClasseIDao);

			cleanup(lConn);
		}
		return aFascicoloSiep;
	}

	/**
	 * Inserisce l'Evento,l'annotazione Manuale, il camponote e le notifiche
	 */
	public EventoNotificaModel ExInserisciRichiestaRevoca(AnnotazioneManualeModel aAnnotazioneManuale,
			EventoNotificaModel aEventoNotifica, CampoNotaModel aCampoNote, Vector aReati,
			DettaglioPenaComplessivaModel aDettaglioPenaComplessiva) throws F3BException {

		Connection lConn = null;

		AnnotazioneManualeDAO lAnnDao = null;
		AnnotazioneManualeModel lAnnMod = null;
		AutoritaEsternaDAO lAutDao = null;
		NotificaDAO lNotDao = null;
		EventoDAO lEveDao = null;
		CampoNotaDAO lCampoNoteDAO = null;
		ReatoDAO lReaDao = null;
		AnnmanReatoDAO lannman_reatoDAo = null;
		PenaComplessivaDAO lPenDao = null;
		SanzioneSostitutivaDAO lSanzDao = null;
		AnnmanPenacomplDAO lannman_penacomplDAo = null;

		EventoNotificaModel lEveRet = new EventoNotificaModel(aEventoNotifica);

		try {
			lConn = getDBTransaction();

			lAutDao = new AutoritaEsternaDAO(lConn);
			lNotDao = new NotificaDAO(lConn);
			lAnnDao = new AnnotazioneManualeDAO(lConn);

			// ==================================================
			// Inserisco l'evento EVENTO
			// ==================================================
			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModel(aEventoNotifica.getEvento());
			BigDecimal lKeyEvento = null;
			lKeyEvento = lEveDao.insert();
			lEveDao.stop();

			lEveRet.getEvento().setIdEvento(lKeyEvento);
			// ==================================================
			// Inserisco le NOTIFICHE
			// ==================================================
			BigDecimal lKeyAutorita = null;
			int count = 0;

			while (count < aEventoNotifica.getNotifiche().length) {
				if (aEventoNotifica.getNotifiche()[count] != null) {
					if (aEventoNotifica.getNotifiche()[count].getAutoritaEsterna() != null) {
						lAutDao.setRicercaByAutSede(
								aEventoNotifica.getNotifiche()[count].getAutoritaEsterna());
						AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
						lAutMod = (AutoritaEsternaModel) lAutDao.getModelByKey();

						if (lAutMod == null) {
							lAutDao.setDAOFromModel(
									aEventoNotifica.getNotifiche()[count].getAutoritaEsterna());
							lKeyAutorita = lAutDao.insert();
							aEventoNotifica.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						} else {
							lKeyAutorita = lAutMod.getIdAutoritaEsterna();
							aEventoNotifica.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						}
					}

					aEventoNotifica.getNotifiche()[count].setEveIdEvento(lKeyEvento);

					lNotDao.setDAOFromModel(aEventoNotifica.getNotifiche()[count]);
					lNotDao.insert();
					lNotDao.stop();
				}
				count++;
			}

			// ===========================
			// Inserisco le ANNOTAZIONI se presenti
			// ===========================
			BigDecimal lKey = null;
			if (aAnnotazioneManuale != null) {
				aAnnotazioneManuale.setEveIdEvento(lKeyEvento);
				lAnnMod = new AnnotazioneManualeModel(aAnnotazioneManuale);

				lAnnDao.setDAOFromModel(aAnnotazioneManuale);
				lKey = lAnnDao.insert();
				lAnnMod.setIdAnnotazioneManuale(lKey);
				lAnnDao.stop();

				lEveDao = new EventoDAO(lConn);
				lEveDao.setAnnIdAnnotazioneManuale(lKey);
				lEveDao.selCondizioneUpdate(lKeyEvento);
				lEveDao.update();
			}
			// =====================================
			// Inserisco il campo note
			// =====================================
			if (aCampoNote != null && aCampoNote.getDescr() != null && !aCampoNote.getDescr().equals("")) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Inserisco il campo note....");
				aCampoNote.setEveIdEvento(lKeyEvento);

				lCampoNoteDAO = new CampoNotaDAO(lConn);
				lCampoNoteDAO.setDAOFromModel(aCampoNote);
				lCampoNoteDAO.insert();
				lCampoNoteDAO.stop();
			}

			// =====================================
			// Inserisco i reati se presenti
			// =====================================
			if (aReati.size() > 0) {
				ReatoModel lReaPrincipale = new ReatoModel((ReatoModel) aReati.get(0));

				// Gestione Progressivo Reato
				// BigDecimal lKeyFascicolo = lReaPrincipale.getFasSieIdFascicoloSiep();
				// BigDecimal lProgrReato = lReaSqlDAo.getProgressivoReato(lKeyFascicolo);
				lReaPrincipale.setProgrReato(new BigDecimal(1));
				lReaPrincipale.setProgrCircostanza(new BigDecimal(1));
				// lReaPrincipale.setFasSieIdFascicoloSiep(lReaPrincipale.getFasSieIdFascicoloSiep());
				lReaPrincipale.setFasSieIdFascicoloSiep(null); // 04/07/2011 Non va tenuto il collegamento al
																// Fascicolo SIEP.

				// Inserimento primo Reato
				lReaDao = new ReatoDAO(lConn);
				lReaDao.setDAOFromModel(lReaPrincipale);

				BigDecimal lKeyReato = null;
				lKeyReato = lReaDao.insert();
				lReaDao.stop();

				// inserisco l'associazione annotazione_manuale-reato
				AnnmanReatoModel lannman_reato = new AnnmanReatoModel();
				lannman_reato.setAnnotazionemanualeId(lKey);
				lannman_reato.setReatoId(lKeyReato);
				lannman_reatoDAo = new AnnmanReatoDAO(lConn);
				lannman_reatoDAo.setDAOFromModel(lannman_reato);
				lannman_reatoDAo.insert();
				lannman_reatoDAo.stop();

				// Inserimento successivi
				ReatoModel lReaMod = null;
				BigDecimal lProgrCircostanza = null;

				if (aReati.size() > 0) {
					lProgrCircostanza = (new BigDecimal(0)); // lReaSqlDAo.getProgressivoCircostanza(lReaPrincipale.getProgrReato(),
																// lKeyFascicolo);
					int lProgrCirc = lProgrCircostanza.intValue() + 1;

					for (int i = 1; i < aReati.size(); i++) {
						lReaMod = new ReatoModel();
						lReaMod = (ReatoModel) aReati.get(i);
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.debug("-------------------------------------------------------------");
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.debug(lReaMod);
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.debug("-------------------------------------------------------------");
						lReaMod.setProgrReato(lReaPrincipale.getProgrReato());
						// lReaMod.setFasSieIdFascicoloSiep(lReaPrincipale.getFasSieIdFascicoloSiep());
						lReaMod.setFasSieIdFascicoloSiep(null); // 04/07/2011 Non va tenuto il collegamento al
																// Fascicolo SIEP.

						// Gestione Progressivo Circostanza
						// GDV spostato fuori dal ciclo
						// lProgrCircostanza =
						// lReaSqlDAo.getProgressivoCircostanza(lReaPrincipale.getProgrReato(),
						// lReaMod.getFasSieIdFascicoloSiep());
						lReaMod.setProgrCircostanza(new BigDecimal(lProgrCirc));
						lReaDao.setDAOFromModel(lReaMod);
						lKeyReato = lReaDao.insert();
						lReaDao.stop();
						lProgrCirc++;

						// inserisco l'associazione annotazione_manuale-reato
						lannman_reatoDAo = null;
						lannman_reato = new AnnmanReatoModel();
						lannman_reato.setAnnotazionemanualeId(lKey);
						lannman_reato.setReatoId(lKeyReato);
						lannman_reatoDAo = new AnnmanReatoDAO(lConn);
						lannman_reatoDAo.setDAOFromModel(lannman_reato);
						lannman_reatoDAo.insert();
						lannman_reatoDAo.stop();

					}
				}
			}

			// =====================================
			// Inserisco la penacomplessiva se presente
			// =====================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("aDettaglioPenaComplessiva=" + aDettaglioPenaComplessiva);
			if (aDettaglioPenaComplessiva.getPenaComplessivaSanzioneSostitutiva() != null) {
				PenaComplessivaModel aPenaComplessiva = aDettaglioPenaComplessiva
						.getPenaComplessivaSanzioneSostitutiva().getPenaComplessiva();

				SanzioneSostitutivaModel aSanzioneSostitutiva = new SanzioneSostitutivaModel();
				if (aDettaglioPenaComplessiva.getPenaComplessivaSanzioneSostitutiva()
						.getSanzioneSostitutiva() != null)
					aSanzioneSostitutiva = aDettaglioPenaComplessiva.getPenaComplessivaSanzioneSostitutiva()
							.getSanzioneSostitutiva();

				// Inserimento Pena Complessiva
				lPenDao = new PenaComplessivaDAO(lConn);

				lPenDao.setDAOFromModel(aPenaComplessiva);
				BigDecimal lKeyPenaComplessiva = null;
				lKeyPenaComplessiva = lPenDao.insert();

				// Inserimento Sanzione Sostitutiva
				if (aSanzioneSostitutiva.getCodTipoSanzione() != null) {
					lSanzDao = new SanzioneSostitutivaDAO(lConn);
					aSanzioneSostitutiva.setPenComIdPenaComplessiva(lKeyPenaComplessiva);
					lSanzDao.setDAOFromModel(aSanzioneSostitutiva);

					BigDecimal lKeySanzioneSostitutiva = null;
					lKeySanzioneSostitutiva = lSanzDao.insert();
					aSanzioneSostitutiva.setIdSanzioneSostitutiva(lKeySanzioneSostitutiva);
				}

				// eventulamnete inserisco l'associazione annotazione_manuale-penacomplessiva
				if (lKey != null) {
					AnnmanPenacomplModel lannman_penacompl = new AnnmanPenacomplModel();
					lannman_penacompl.setAnnotazionemanualeId(lKey);
					lannman_penacompl.setPenacomplessivaId(lKeyPenaComplessiva);
					lannman_penacomplDAo = new AnnmanPenacomplDAO(lConn);
					lannman_penacomplDAo.setDAOFromModel(lannman_penacompl);
					lannman_penacomplDAo.insert();
					lannman_penacomplDAo.stop();
				}
			}

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: ", ex);
			throw new F3BException("PenaSospesaController.ExInserisciRichiestaRevoca: " + ex);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: ", ex);
			throw new F3BException("PenaSospesaController.ExInserisciRichiestaRevoca: " + ex);
		} finally {
			cleanup(lAnnDao);
			cleanup(lEveDao);
			cleanup(lAutDao);
			cleanup(lNotDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lCampoNoteDAO);
			cleanup(lReaDao);
			cleanup(lannman_reatoDAo);
			cleanup(lSanzDao);
			cleanup(lPenDao);
			cleanup(lannman_penacomplDAo);

			cleanup(lConn);
		}

		return lEveRet;
	}

	public Vector ExRicercaReatiByAnnotazioneMan(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		Vector lReati = new Vector();
		AnnmanReatoSqlDAO lannman_reatoDAo = null;
		ReatoSqlDAO lReaDao = null;

		try {
			lConn = getDBConnection();

			// verifico l'esistenza dell'associazione annotazione_manuale-reato
			Vector ann_reati = new Vector();

			lannman_reatoDAo = new AnnmanReatoSqlDAO(lConn);
			AnnmanReatoModel lannman_reato = new AnnmanReatoModel();
			lannman_reato.setAnnotazionemanualeId(aKey);
			lannman_reatoDAo.ricercaAnnmanReato(lannman_reato);
			ann_reati = new Vector(lannman_reatoDAo.getModels());

			if (ann_reati.size() > 0) {
				lReaDao = new ReatoSqlDAO(lConn);
				lannman_reato = new AnnmanReatoModel();
				ReatoModel lRea = null;
				for (int i = 0; i < ann_reati.size(); i++) {
					lannman_reato = (AnnmanReatoModel) ann_reati.elementAt(i);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.error("reato: " + lannman_reato);
					lReaDao.ricercaReatoByKey(lannman_reato.getReatoId());
					lRea = (ReatoModel) (lReaDao.getModelByKey());
					lReati.add(lRea);
				}
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("PenaSospesaController.ExRicercaReatiByAnnotazioneMan: " + daoEx);
		} finally {
			cleanup(lannman_reatoDAo);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lReaDao);
			cleanup(lConn);
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		siesLogger.error("size del vettore di reati: " + lReati.size());
		return lReati;
	}

	public PenaComplessivaSanzioneSostitutivaModel ExRicercaPenaComplessivaByAnnotazioneMan(BigDecimal aKey)
			throws F3BException {

		Connection lConn = null;

		AnnmanPenacomplSqlDAO lannman_pcomplDAo = null;
		PenaComplessivaSqlDAO lPenDao = null;
		SanzioneSostitutivaSqlDAO lSanDao = null;

		PenaComplessivaSanzioneSostitutivaModel lPenSanMod = null;
		PenaComplessivaModel lPenMod = null;
		SanzioneSostitutivaModel lSanMod = null;

		try {
			lConn = getDBConnection();

			// verifico l'esistenza dell'associazione annotazione_manuale-penacomplessiva

			lannman_pcomplDAo = new AnnmanPenacomplSqlDAO(lConn);
			AnnmanPenacomplModel lannman_pc = new AnnmanPenacomplModel();
			lannman_pc.setAnnotazionemanualeId(aKey);
			lannman_pcomplDAo.ricercaAnnmanPenacompl(lannman_pc);

			Vector totali = new Vector(lannman_pcomplDAo.getModels());

			if (totali.size() > 0) {
				lannman_pc = (AnnmanPenacomplModel) totali.elementAt(0);
				lPenDao = new PenaComplessivaSqlDAO(lConn);
				lPenDao.ricercaPenaComplessivaByKey(lannman_pc.getPenacomplessivaId());

				lPenMod = (PenaComplessivaModel) (lPenDao.getModelByKey());

				if (lPenMod != null) {
					lSanDao = new SanzioneSostitutivaSqlDAO(lConn);
					lSanDao.ricercaSanzioneSostitutivaByIdPenaComplessiva(lPenMod.getIdPenaComplessiva());

					lSanDao.start();
					if (lSanDao.next()) {
						lSanMod = (SanzioneSostitutivaModel) lSanDao.getModel();
					}
					lSanDao.stop();

					lPenSanMod = new PenaComplessivaSanzioneSostitutivaModel(lPenMod, lSanMod);
				}
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("PenaSospesaController.ExRicercaReatiByAnnotazioneMan: " + daoEx);
		} finally {
			cleanup(lannman_pcomplDAo);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lPenDao);
			cleanup(lSanDao);
			cleanup(lConn);
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		siesLogger.error("Pena complessiva: " + lPenSanMod);
		return lPenSanMod;
	}

	/**
	 * Stampa trasferimento Nuova Istanza
	 *
	 * @param aEvento
	 * @return
	 * @throws F3BException
	 */
	public ByteArrayOutputStream ExStampaRichiestaRevoca(EventoNotificaModel aEvento, UtenteModel aUtente)
			throws F3BException {

		Connection lConn = null;
		EventoDAO lEveDao = null;

		ByteArrayOutputStream lByteArrayOut = null;
		try {
			IEvento lEvCrtl = SICOLookupRemote.getEventoRemote();
			EventoNotificaModel lEventoModel = lEvCrtl
					.ExRicercaEventoNotificaByKey(aEvento.getEvento().getIdEvento());

			IStampa lStampa = SICOLookupRemote.getStampaRemote();
			TreeModel lTree = lStampa.prelevaDatiPenaSospesa(lEventoModel, aUtente);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("XML >>> " + lTree.toString());

			ReportGenerator lReport = new ReportGenerator();
			String lNomeTemplate = TemplateManager.getInstance().getTemplateName(aEvento.getNomeTemplate());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("NOME TEMPLATE >>>" + lNomeTemplate);
			lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lTree, lNomeTemplate);

			ByteArrayInputStream lByteArrayInput = new ByteArrayInputStream(lByteArrayOut.toByteArray());

			aEvento.getEvento().setFlagDocumentoRegistrato("N");
			aEvento.getEvento().setDocBlobIn(lByteArrayInput);
			lConn = getDBConnection();
			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModelForUpdateBlob(aEvento.getEvento());

			lEveDao.selCondizioneUpdate(aEvento.getEvento().getIdEvento());
			lEveDao.update();
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("NuovaIstanzaController.ExStampaTrasmissioneNuovaIstanza: " + daoEx);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}

		return lByteArrayOut;
	}

	/**
	 * Inserisce l'Annotazione Manuale, l'Evento. Lo Scadenzario di tipo "16" viene inserito o aggiornato solo
	 * se il parametro aCancellaScadenzario è false ed è valorizzata la data di irrevocabilità. Se invece
	 * aCancellaScadenzario è true lo scadenzario viene cancellato.
	 *
	 * @param boolean
	 *            aCancellaScadenzario richiede la cancellazione dello scadenzario
	 * @param AnnotazioneManualeModel
	 *            model dell'annotazione manuale
	 * @param EventoModel
	 *            model contenente l'evento
	 * @return il model dell'annotazione con valorizzato l'id di inserimento
	 */
	public AnnotazioneManualeModel ExInserisciAnnotazioneEventoScadenzario(
			AnnotazioneManualeModel aAnnotazioneManuale, EventoModel aEvento, Date aDataIrrevocabilita,
			boolean aCancellaScadenzario) throws F3BException {

		Connection lConn = null;

		AnnotazioneManualeDAO lAnnDao = null;
		AnnotazioneManualeModel lAnnMod = null;

		EventoDAO lEveDao = null;

		try {
			lConn = getDBTransaction();
			lAnnDao = new AnnotazioneManualeDAO(lConn);

			// Inserimento Evento
			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModel(aEvento);
			BigDecimal lKeyEvento = null;
			lKeyEvento = lEveDao.insert();
			lEveDao.stop();

			// Inserimento Annotazione
			aAnnotazioneManuale.setEveIdEvento(lKeyEvento);
			lAnnMod = new AnnotazioneManualeModel(aAnnotazioneManuale);
			lAnnDao.setDAOFromModel(aAnnotazioneManuale);
			BigDecimal lKey = null;
			lKey = lAnnDao.insert();
			lAnnMod.setIdAnnotazioneManuale(lKey);
			lAnnDao.stop();

			// Se il flag aCancellaScadenzario vale true viene cancellato
			// lo scadenzario di tipo Termini Ottemperanza Obblighi.
			// Se invece valorizzata la data di irrevocabilità lo scadenzario
			// viene inserito o aggiornato.

			if (aCancellaScadenzario)
				CancellazioneScadenzario(lAnnMod.getFasSieIdFascicoloSiep(), "16", lConn);
			else if (aDataIrrevocabilita != null)
				InsScadenzarioOttemperanzaTermini(lAnnMod, aDataIrrevocabilita, lConn);
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: ", ex);
			throw new F3BException(
					"PenaSospesaController.ExInserisciAnnotazioneManualeEventoNotifica: " + ex);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: ", ex);
			throw new F3BException(
					"PenaSospesaController.ExInserisciAnnotazioneManualeEventoNotifica: " + ex);
		} finally {
			cleanup(lAnnDao);
			cleanup(lEveDao);
			cleanup(lConn);
		}

		return lAnnMod;
	}

	/**
	 * Inserisce Scadenzario di Ottemperanza Termini
	 *
	 * @param aAnnMod
	 * @param lConn
	 */
	void InsScadenzarioOttemperanzaTermini(AnnotazioneManualeModel aAnnMod, Date aDataIrrevocabilita,
			Connection aConn) throws Exception {

		ScadenzarioDAO lScaDao = null;
		ScadenzarioSqlDAO lScaSqlDao = null;

		try {
			if (aDataIrrevocabilita == null)
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug("Scadenzario non inserito perchè data di irrevocabilità assente");
			else {
				if (aAnnMod.getNumAnniReclusione().intValue() == 0
						&& aAnnMod.getNumMesiReclusione().intValue() == 0
						&& aAnnMod.getNumGiorniReclusione().intValue() == 0) {
					// non viene inserito lo scadenzario
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.debug("Scadenzario non inserito perchè Termini non definiti");
				} else {
					// Ricerca Scadenzario per decidere se fare un update o un nuovo inserimento
					lScaSqlDao = new ScadenzarioSqlDAO(aConn);
					lScaSqlDao.ricercaScadenzarioByTipoScadenzarioIdFascicolo("16",
							aAnnMod.getFasSieIdFascicoloSiep());
					ScadenzarioModel lScaMod = new ScadenzarioModel();
					lScaMod = (ScadenzarioModel) lScaSqlDao.getModelByKey();
					lScaSqlDao.stop();

					// Inizializzazione Scadenzario
					Date lSommaAnni = null;
					Date lSommaMesi = null;
					Date lFineScadenza = null;
					String lDataInizio = DateUtils.getDateToString(aDataIrrevocabilita, "dd/MM/yyyy");
					lSommaAnni = DateUtils.moveDateTo(DateUtils.getDate(lDataInizio, "dd/MM/yyyy"),
							java.util.Calendar.YEAR, aAnnMod.getNumAnniReclusione().intValue());
					lSommaMesi = DateUtils.moveDateTo(lSommaAnni, java.util.Calendar.MONTH,
							aAnnMod.getNumMesiReclusione().intValue());
					lFineScadenza = DateUtils.moveDateTo(lSommaMesi, java.util.Calendar.DAY_OF_MONTH,
							aAnnMod.getNumGiorniReclusione().intValue());

					lScaDao = new ScadenzarioDAO(aConn);
					lScaDao.setCodTipoScadenzario("16");
					lScaDao.setDataInizioScadenza(DateUtils.getDate(lDataInizio, "dd/MM/yyyy"));
					lScaDao.setDataFineScadenza(lFineScadenza);
					lScaDao.setFasSieIdFascicoloSiep(aAnnMod.getFasSieIdFascicoloSiep());
					lScaDao.setEveIdEvento(aAnnMod.getEveIdEvento());
					lScaDao.setFlagVisto("N");
					lScaDao.setCodStatoNotifica("N");

					if (lScaMod != null && lScaMod.getIdScadenzario() != null) {
						// Update
						lScaDao.setCodOperatoreAggiornamento(aAnnMod.getCodOperatoreInserimento());
						lScaDao.setCodUfficioAggiornamento(aAnnMod.getCodUfficioInserimento());
						lScaDao.setDataAggiornamento(DateUtils.getSysDate());
						lScaDao.setCondizioneUpdate(lScaMod.getIdScadenzario());
						lScaDao.update();
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.debug("Aggiornamento di scadenzario già esistente.");
					} else {
						lScaDao.setCodOperatoreInserimento(aAnnMod.getCodOperatoreInserimento());
						lScaDao.setCodUfficioInserimento(aAnnMod.getCodUfficioInserimento());
						lScaDao.setDataInserimento(DateUtils.getSysDate());
						lScaDao.insert();
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.debug("Inserito nuovo cadenzario.");
					}
					lScaDao.stop();
				}
			}
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: ", e);
		} finally {
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lScaDao);
			cleanup(lScaSqlDao);
		}
	}

	void CancellazioneScadenzario(BigDecimal aIdFascicoloSiep, String aTipoScadenzario, Connection aConn)
			throws Exception {

		ScadenzarioSqlDAO lScaSqlDao = null;
		ScadenzarioDAO lScaDao = null;

		ScadenzarioModel lScaMod = null;

		try {
			lScaSqlDao = new ScadenzarioSqlDAO(aConn);
			lScaDao = new ScadenzarioDAO(aConn);

			lScaSqlDao.ricercaScadenzarioByTipoScadenzarioIdFascicolo(aTipoScadenzario, aIdFascicoloSiep);
			lScaMod = (ScadenzarioModel) lScaSqlDao.getModelByKey();
			if (lScaMod != null && lScaMod.getIdScadenzario() != null) {
				lScaDao.setCondizioneDelete(lScaMod.getIdScadenzario());
				lScaDao.delete();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Cancellazione Scadenzario tipo " + aTipoScadenzario);
			}
			lScaDao.stop();
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: ", e);
		} finally {
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lScaSqlDao);
			cleanup(lScaDao);
		}
	}

}