package siap.sico.webservice.action;

import it.mig.sippi.service.richiestacertificato.type.DATIRISPOSTACERTIFICATO;

import java.io.ByteArrayInputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.webservice.controller.IWebServices;
import siap.sico.webservice.model.OmonimiModel;
import siap.siep.SIEPException;
import siap.siep.fascicolo.controller.FascicoloSiepController;
import siap.siep.fascicolo.model.FascicoloSiepCertBlobModel;
import siap.sius.fascicolo.controller.FascicoloSiusController;
import siap.sius.fascicolo.model.FascicoloSiusCertBlobModel;
import f3b.log.LogF3B;
import f3b.model.DecodeModel;
import f3b.util.DateUtils;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActRispostaDatiRichiestaCertificato extends ActWsBase {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public Vector processRequest(DATIRISPOSTACERTIFICATO risposta, FascicoloSiepCertBlobModel fascicoloSIEP,
			FascicoloSiusCertBlobModel fascicoloSIUS, UtenteModel lUteMod) throws Exception {

		Vector lListaOmonimi = new Vector();

		Collection lNazioni = DecodificheManager.getInstance().getNazioni();

		// Caso 1 - Soggetto non Trovato senza Sinonimi - Stampa Certificato Nullo
		// Caso 3 - Soggetto Trovato senza Sinonimi - Stampa Certificato Penale
		if (risposta.getCERTIFICATOPENALE() != null
				&& !"".equals(risposta.getCERTIFICATOPENALE().toString())) {
			// Salvare il certificato penale sulla tabella FASCICOLO_SIEP
			if (fascicoloSIEP != null) {
				FascicoloSiepController ctrlSiep = new FascicoloSiepController();
				fascicoloSIEP.setCodUfficioAggiornamento(lUteMod.getUfficioUtente().getCodUfficio());
				fascicoloSIEP.setCodOperatoreAggiornamento(lUteMod.getUserId());
				fascicoloSIEP.setDataAggiornamento(DateUtils.getSysDate());

				// Caricamento CERTIFICATO (Campo BLOB) nel Model
				ByteArrayInputStream lByteArrayInput = new ByteArrayInputStream(
						risposta.getCERTIFICATOPENALE());
				fascicoloSIEP.caricaCertPenaleBlobIn(lByteArrayInput);

				ctrlSiep.ExInsertCertificatoPenale(fascicoloSIEP);

				// Salvare il certificato penale sulla tabella FASCICOLO_SIUS
			} else {
				FascicoloSiusController ctrlSius = new FascicoloSiusController();
				fascicoloSIUS.setCodUfficioAggiornamento(lUteMod.getUfficioUtente().getCodUfficio());
				fascicoloSIUS.setCodOperatoreAggiornamento(lUteMod.getUserId());
				fascicoloSIUS.setDataAggiornamento(DateUtils.getSysDate());

				ByteArrayInputStream lByteArrayInput = new ByteArrayInputStream(
						risposta.getCERTIFICATOPENALE());
				fascicoloSIUS.caricaCertPenaleBlobIn(lByteArrayInput);

				ctrlSius.ExInsertCertificatoPenale(fascicoloSIUS);
			}
		}

		// Presenza Omonimi/Sinonimi
		if (risposta.getArrayOmonimi() != null && risposta.getArrayOmonimi().getOMONIMO().size() > 0) {

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("--------------------------------------------------------------------------");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("Lunghezza Array OMONIMI: " + risposta.getArrayOmonimi().getOMONIMO().size());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("--------------------------------------------------------------------------");

			for (int i = 0; i < risposta.getArrayOmonimi().getOMONIMO().size(); i++) {
				// Descrizione COMUNE di NASCITA
				ComuneModel lComuneModel = new ComuneModel();
				if (risposta.getArrayOmonimi().getOMONIMO().get(i).getDATIANAGRAFICI()
						.getCODILUOGONASCITA() != null) {
					lComuneModel = CercaDescrizioneComuneNascita(risposta.getArrayOmonimi().getOMONIMO()
							.get(i).getDATIANAGRAFICI().getCODILUOGONASCITA());
				}

				// Descrizione STATO ESTERO di NASCITA
				String lDescNazione = "";
				if (!risposta.getArrayOmonimi().getOMONIMO().get(i).getDATIANAGRAFICI()
						.getCODISTATOESTERONAS().equals(""))
					;
				{
					Iterator itx = lNazioni.iterator();
					while (itx.hasNext()) {
						DecodeModel ldecodeModel;
						ldecodeModel = (DecodeModel) itx.next();
						if (ldecodeModel.getCode().equals(risposta.getArrayOmonimi().getOMONIMO().get(i)
								.getDATIANAGRAFICI().getCODISTATOESTERONAS())) {
							lDescNazione = ldecodeModel.getDescription();
						}
					}
				}

				OmonimiModel mOmonimiModel = new OmonimiModel();
				mOmonimiModel.setOMONIMO(risposta.getArrayOmonimi().getOMONIMO().get(i));
				mOmonimiModel.setDescLuogoNascita(lComuneModel.getDescrizione());
				mOmonimiModel.setProvNascita(lComuneModel.getCodProvincia());
				mOmonimiModel.setDescStatoEstero(lDescNazione);

				lListaOmonimi.add(mOmonimiModel);
			}
		}

		return lListaOmonimi;
	}

	private ComuneModel CercaDescrizioneComuneNascita(String aCodIstatComuneNascita) throws Exception {
		IWebServices lCtrlComune = SICOLookupRemote.getWebServicesRemote();
		ComuneModel lComuneModel = new ComuneModel();

		try {
			lComuneModel = lCtrlComune.ExRicercaProvinciaSedeGiudiziaria(aCodIstatComuneNascita);
		} catch (SIEPException e) {
			throw e;
		}
		return lComuneModel;
	}

}