package siap.jms.messaggio.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import javax.jms.ObjectMessage;

import siap.jms.ICostantiJMS;
import siap.siep.misurasicurezza.model.FascMsToFascSiepModel;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.xml.TreeModel;

/**
 * <p>
 * Title: MessaggioModel
 * </p>
 * <p>
 * Description: Classe Model che rappresenta il Messaggio
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
@SuppressWarnings("rawtypes")
public class MessaggioModel extends GenericModel implements ICostantiJMS {

	/**
	 * 
	 */
	private static final long serialVersionUID = -186997136829118933L;

	private BigDecimal mIdMessaggio;
	private String mCodTipoMessaggio;
	private String mDescrTipoMessaggio;

	private String mCodTipoOperazione;
	private String mDescrTipoOperazione;

	private String mCodUfficioMittente;
	private String mDescrUfficioMittente;
	private String mCodBdiMittente;
	private String mDescrBdiMittente;
	private String mCodUfficioDestinatario;
	private String mDescrSedeUfficioDestinatario;
	private String mDescrUfficioDestinatario;
	private String mCodBdiDestinataria;
	private String mDescrBdiDestinataria;
	private String mJmsIdMessaggio;
	private String mFlagVisto;
	private String mJmsCorrelationIdMessage;
	private Date mDataInvio;
	private Date mDataEsito;
	private String mCodiceUtenteMittente;
	private String mDescriceUtenteMittente;
	private String mDescrSedeUfficioMittente;
	private String mCodEsito;
	private String mDescrEsito;
	//
	private BigDecimal mChiaveAnnoSiep;
	private BigDecimal mChiaveProgrSiep;
	private String mChiaveUfficioSiep; // d.f. per gestire gli inoltri
	private String mDescrUfficioSiep;
	private String mDescrSedeUfficioSiep;

	private BigDecimal mChiaveAnnoSius;
	private BigDecimal mChiaveProgrSius;

	private ByteArrayInputStream mBlobIn;
	private ByteArrayOutputStream mBlobOut;
	private TreeModel mTreeModel;
	private boolean mStessaBDI;
	private MessaggioModel mMessaggioCorrelato;
	private Vector mMessaggiCorrelati;
	private ContatoreEsitiModel mContatoreEsiti;
	private String mRapportoEsito;

	private BigDecimal mChiaveAnnoSiepe; // UEPE
	private BigDecimal mChiaveProgrSiepe; // UEPE
	private String mNomeSoggetto; // UEPE - SIEP Trasmissione Competenza
	private String mCognomeSoggetto; // UEPE - SIEP Trasmissione Competenza
	private Date mDataNascita; // UEPE - SIEP Trasmissione Competenza
	private String mCodStatoNascita; // UEPE - SIEP Trasmissione Competenza
	private String mCodComuneNascita; // UEPE - SIEP Trasmissione Competenza
	private BigDecimal mChiaveAnnoFasCumulante; // SIEP Trasmissione Competenza
	private BigDecimal mChiaveProgrFasCumulante; // SIEP Trasmissione Competenza

	private String mChiaveUfficioFasCumulante; // d.f. per gestire correttamente la chiave naturale del
												// fascicolo cumulante
	private String mDescrUfficioFasCumulante;
	private String mDescrSedeUfficioFasCumulante;

	private String mNote; // SIEP Trasmissione Competenza

	private FascMsToFascSiepModel mFascMsToFascSiepModel = null;

	private BigDecimal mIdRichiesta;

	// Nuovi campi per la gestione dell'inoltro
	private String mDeliveryMode;

	private String mCodUfficioInoltro;
	private String mDescrUfficioInoltro;
	private String mDescrSedeUfficioInoltro;
	private String mCodBdiInoltro;

	private String mCodUfficioReplyTo;
	private String mDescrUfficioReplyTo;
	private String mCodBdiReplyTo;
	private String mDescrSedeUfficioReplyTo;
	private String mJmsCorrelationReplyTo;
	private String mIdMessaggioSollecitato;

	private boolean mIsErroreParser;
	private Vector<MessaggioModel> mMessaggiSollecito;
	private Date mDataUltimoSollecito;
	// mev 39 (assorbimento in cumulo)
	private Date mDataEmissioneCumulo;

	// private Blob mBlobEsito;

	// Ticket#20220111018 - Aggiunta canta Solleciti 
	private BigDecimal mContaSolleciti;
	
	
	// COSTRUTTORE DI DEFAULT
	public MessaggioModel() {
		this.mIdMessaggio = null;
		this.mCodTipoMessaggio = "";
		this.mCodTipoOperazione = "";
		this.mDescrTipoOperazione = "";
		this.mDescrTipoMessaggio = "";
		this.mCodUfficioMittente = "";
		this.mDescrUfficioMittente = "";
		this.mCodBdiMittente = "";
		this.mDescrBdiMittente = "";
		this.mCodUfficioDestinatario = "";
		this.mDescrUfficioDestinatario = "";
		this.mCodBdiDestinataria = "";
		this.mDescrBdiDestinataria = "";
		this.mDataInvio = null;
		this.mDataEsito = null;
		this.mCodiceUtenteMittente = "";
		this.mDescriceUtenteMittente = "";
		this.mCodEsito = "";
		this.mDescrEsito = "";
		this.mJmsIdMessaggio = "";
		this.mJmsCorrelationIdMessage = "";
		this.mFlagVisto = "";
		this.mMessaggioCorrelato = null;
		this.mChiaveAnnoSiep = null;
		this.mChiaveProgrSiep = null;
		this.mChiaveUfficioSiep = null;
		this.mDescrSedeUfficioSiep = null;
		this.mDescrUfficioSiep = null;
		this.mChiaveAnnoSius = null;
		this.mChiaveProgrSius = null;
		this.mStessaBDI = false;
		this.mRapportoEsito = "";
		this.mMessaggiCorrelati = null;
		this.mContatoreEsiti = null;
		this.mChiaveAnnoSiepe = null; // UEPE
		this.mChiaveProgrSiepe = null; // UEPE
		this.mNomeSoggetto = ""; // UEPE - SIEP Trasmissione Competenza
		this.mCognomeSoggetto = ""; // UEPE - SIEP Trasmissione Competenza
		this.mDataNascita = null; // UEPE - SIEP Trasmissione Competenza
		this.mCodStatoNascita = "-"; // UEPE - SIEP Trasmissione Competenza
		this.mCodComuneNascita = "-"; // UEPE - SIEP Trasmissione Competenza
		this.mChiaveAnnoFasCumulante = null; // SIEP Trasmissione Competenza
		this.mChiaveProgrFasCumulante = null; // SIEP Trasmissione Competenza
		this.mChiaveUfficioFasCumulante = null;
		this.mNote = ""; // SIEP Trasmissione Competenza

		this.mFascMsToFascSiepModel = null;
		this.mIdRichiesta = null;

		this.mDeliveryMode = null;

		this.mCodUfficioInoltro = null;
		this.mDescrUfficioInoltro = null;
		this.mDescrSedeUfficioInoltro = null;
		this.mCodBdiInoltro = null;

		this.mCodUfficioReplyTo = null;
		this.mDescrUfficioReplyTo = null;
		this.mCodBdiReplyTo = null;
		this.mDescrSedeUfficioReplyTo = null;

		this.mJmsCorrelationReplyTo = null;
		this.mIdMessaggioSollecitato = null;

		this.mIsErroreParser = false;
		this.mMessaggiSollecito = null;
		this.mDataUltimoSollecito = null;
		// this.mBlobEsito = null;
		this.mDataEmissioneCumulo = null;
	}

	// COSTRUTTORE DI COPIA
	public MessaggioModel(MessaggioModel aModel) {
		this.mIdMessaggio = aModel.mIdMessaggio;
		this.mCodTipoMessaggio = aModel.mCodTipoMessaggio;
		this.mDescrTipoMessaggio = aModel.mDescrTipoMessaggio;
		this.mCodTipoOperazione = aModel.mCodTipoOperazione;
		this.mDescrTipoOperazione = aModel.mDescrTipoOperazione;
		this.mCodUfficioMittente = aModel.mCodUfficioMittente;
		this.mDescrUfficioMittente = aModel.mDescrUfficioMittente;
		this.mCodBdiMittente = aModel.mCodBdiMittente;
		this.mDescrBdiMittente = aModel.mDescrBdiMittente;
		this.mCodUfficioDestinatario = aModel.mCodUfficioDestinatario;
		this.mDescrUfficioDestinatario = aModel.mDescrUfficioDestinatario;
		this.mCodBdiDestinataria = aModel.mCodBdiDestinataria;
		this.mDescrBdiDestinataria = aModel.mDescrBdiDestinataria;
		this.mDataInvio = aModel.mDataInvio;
		this.mDataEsito = aModel.mDataEsito;
		this.mCodiceUtenteMittente = aModel.mCodiceUtenteMittente;
		this.mDescriceUtenteMittente = aModel.mDescriceUtenteMittente;
		this.mCodEsito = aModel.mCodEsito;
		this.mDescrEsito = aModel.mDescrEsito;
		this.mJmsIdMessaggio = aModel.mJmsIdMessaggio;
		this.mJmsCorrelationIdMessage = aModel.mJmsCorrelationIdMessage;
		this.mTreeModel = aModel.mTreeModel;
		this.mMessaggioCorrelato = aModel.mMessaggioCorrelato;
		this.mFlagVisto = aModel.mFlagVisto;

		this.mChiaveAnnoSiep = aModel.mChiaveAnnoSiep;
		this.mChiaveProgrSiep = aModel.mChiaveProgrSiep;
		this.mChiaveUfficioSiep = aModel.mChiaveUfficioSiep;
		this.mDescrUfficioSiep = aModel.mDescrUfficioSiep;
		this.mDescrSedeUfficioSiep = aModel.mDescrSedeUfficioSiep;

		this.mChiaveAnnoSius = aModel.mChiaveAnnoSius;
		this.mChiaveProgrSius = aModel.mChiaveProgrSius;
		this.mStessaBDI = aModel.mStessaBDI;
		this.mRapportoEsito = aModel.mRapportoEsito;
		this.mMessaggiCorrelati = aModel.mMessaggiCorrelati;
		this.mContatoreEsiti = aModel.mContatoreEsiti;
		this.mChiaveAnnoSiepe = aModel.mChiaveAnnoSiepe; //
		this.mChiaveProgrSiepe = aModel.mChiaveProgrSiepe; //
		this.mNomeSoggetto = aModel.mNomeSoggetto; // UEPE - SIEP Trasmissione Competenza
		this.mCognomeSoggetto = aModel.mCognomeSoggetto; // UEPE - SIEP Trasmissione Competenza
		this.mDataNascita = aModel.mDataNascita; // UEPE - SIEP Trasmissione Competenza
		this.mCodStatoNascita = aModel.mCodStatoNascita; // UEPE - SIEP Trasmissione Competenza
		this.mCodComuneNascita = aModel.mCodComuneNascita; // UEPE - SIEP Trasmissione Competenza
		this.mChiaveAnnoFasCumulante = aModel.mChiaveAnnoFasCumulante; // SIEP Trasmissione Competenza
		this.mChiaveProgrFasCumulante = aModel.mChiaveProgrFasCumulante; // SIEP Trasmissione Competenza
		this.mChiaveUfficioFasCumulante = aModel.mChiaveUfficioFasCumulante; // SIEP Trasmissione Competenza

		this.mNote = aModel.mNote; // SIEP Trasmissione Competenza

		this.mFascMsToFascSiepModel = aModel.mFascMsToFascSiepModel;
		this.mIdRichiesta = aModel.mIdRichiesta;

		this.mDeliveryMode = aModel.mDeliveryMode;

		this.mCodUfficioInoltro = aModel.mCodUfficioInoltro;
		this.mDescrUfficioInoltro = aModel.mDescrUfficioInoltro;
		this.mDescrSedeUfficioInoltro = aModel.mDescrSedeUfficioInoltro;
		this.mCodBdiInoltro = aModel.mCodBdiInoltro;

		this.mCodUfficioReplyTo = aModel.mCodUfficioReplyTo;
		this.mDescrUfficioReplyTo = aModel.mDescrUfficioReplyTo;
		this.mDescrSedeUfficioReplyTo = aModel.mDescrSedeUfficioReplyTo;
		this.mCodBdiReplyTo = aModel.mCodBdiReplyTo;

		this.mJmsCorrelationReplyTo = aModel.mJmsCorrelationReplyTo;
		this.mIdMessaggioSollecitato = aModel.mIdMessaggioSollecitato;
		this.mMessaggiSollecito = aModel.mMessaggiSollecito;
		// this.mDataUltimoSollecito = aModel.mDataUltimoSollecito;
		// this.mBlobEsito = aModel.mBlobEsito;
		this.mDataEmissioneCumulo= aModel.mDataEmissioneCumulo;
	}

	public MessaggioModel(ObjectMessage aMessage) throws Exception {
		this.mCodTipoMessaggio = aMessage.getStringProperty(TIPO_MESSAGGIO);
		this.mCodTipoOperazione = aMessage.getStringProperty(TIPO_OPERAZIONE);
		this.mCodUfficioMittente = aMessage.getStringProperty(UFFICIO_MITTENTE);
		this.mCodBdiMittente = aMessage.getStringProperty(COD_BDI_MITTENTE);
		this.mCodUfficioDestinatario = aMessage.getStringProperty(UFFICIO_DESTINATARIO);
		this.mCodBdiDestinataria = aMessage.getStringProperty(COD_BDI_DESTINATARIA);
		this.mJmsIdMessaggio = aMessage.getJMSMessageID();
		this.mCodiceUtenteMittente = aMessage.getStringProperty(UTENTE_MITTENTE);
		this.mJmsCorrelationIdMessage = aMessage.getStringProperty(CORRELATION_ID_MESSAGGIO);
		this.mCodEsito = aMessage.getStringProperty(COD_ESITO);
		this.mNote = aMessage.getStringProperty(NOTE); // SIEP Trasmissione Competenza

		if (aMessage.getStringProperty(CHIAVE_ANNO_SIEP) != null
				&& aMessage.getStringProperty(CHIAVE_ANNO_SIEP).length() >= 1)
			this.mChiaveAnnoSiep = new BigDecimal(aMessage.getStringProperty(CHIAVE_ANNO_SIEP));
		if (aMessage.getStringProperty(CHIAVE_PROGR_SIEP) != null
				&& aMessage.getStringProperty(CHIAVE_PROGR_SIEP).length() >= 1)
			this.mChiaveProgrSiep = new BigDecimal(aMessage.getStringProperty(CHIAVE_PROGR_SIEP));
		if (aMessage.getStringProperty(CHIAVE_UFFICIO_SIEP) != null
				&& aMessage.getStringProperty(CHIAVE_UFFICIO_SIEP).length() >= 1)
			this.mChiaveUfficioSiep = aMessage.getStringProperty(CHIAVE_UFFICIO_SIEP);

		if (aMessage.getStringProperty(CHIAVE_ANNO_SIUS) != null
				&& aMessage.getStringProperty(CHIAVE_ANNO_SIUS).length() >= 1)
			this.mChiaveAnnoSius = new BigDecimal(aMessage.getStringProperty(CHIAVE_ANNO_SIUS));
		if (aMessage.getStringProperty(CHIAVE_PROGR_SIUS) != null
				&& aMessage.getStringProperty(CHIAVE_PROGR_SIUS).length() >= 1)
			this.mChiaveProgrSius = new BigDecimal(aMessage.getStringProperty(CHIAVE_PROGR_SIUS));

		if (aMessage.getStringProperty(CHIAVE_ANNO_SIEPE) != null
				&& aMessage.getStringProperty(CHIAVE_ANNO_SIEPE).length() >= 1)
			this.mChiaveAnnoSiepe = new BigDecimal(aMessage.getStringProperty(CHIAVE_ANNO_SIEPE)); // UEPE
		if (aMessage.getStringProperty(CHIAVE_PROGR_SIEPE) != null
				&& aMessage.getStringProperty(CHIAVE_PROGR_SIEPE).length() >= 1)
			this.mChiaveProgrSiepe = new BigDecimal(aMessage.getStringProperty(CHIAVE_PROGR_SIEPE)); // UEPE
		if (aMessage.getStringProperty(NOME_SOGGETTO) != null
				&& aMessage.getStringProperty(NOME_SOGGETTO).length() >= 1)
			this.mNomeSoggetto = new String(aMessage.getStringProperty(NOME_SOGGETTO)); // UEPE - SIEP
																						// Trasmissione
																						// Competenza
		if (aMessage.getStringProperty(COGNOME_SOGGETTO) != null
				&& aMessage.getStringProperty(COGNOME_SOGGETTO).length() >= 1)
			this.mCognomeSoggetto = new String(aMessage.getStringProperty(COGNOME_SOGGETTO)); // UEPE - SIEP
																								// Trasmissione
																								// Competenza
		/*
		 * Per il momento commentato Luigi 11-08-2006 if(aMessage.getObjectProperty(DATA_NASCITA)!=null )
		 * this.mDataNascita = (Date)(aMessage.getObjectProperty(DATA_NASCITA)); // UEPE
		 */
		if (aMessage.getStringProperty(DATA_NASCITA) != null
				&& aMessage.getStringProperty(DATA_NASCITA).length() == 10)
			this.mDataNascita = DateUtils.getDate(aMessage.getStringProperty(DATA_NASCITA).substring(6, 10),
					aMessage.getStringProperty(DATA_NASCITA).substring(3, 5),
					aMessage.getStringProperty(DATA_NASCITA).substring(0, 2)); // UEPE

		if (aMessage.getStringProperty(COD_STATO_NASCITA) != null
				&& aMessage.getStringProperty(COD_STATO_NASCITA).length() >= 1)
			this.mCodStatoNascita = new String(aMessage.getStringProperty(COD_STATO_NASCITA)); // UEPE - SIEP
																								// Trasmissione
																								// Competenza
		if (aMessage.getStringProperty(COD_COMUNE_NASCITA) != null
				&& aMessage.getStringProperty(COD_COMUNE_NASCITA).length() >= 1)
			this.mCodComuneNascita = new String(aMessage.getStringProperty(COD_COMUNE_NASCITA)); // UEPE -
																									// SIEP
																									// Trasmissione
																									// Competenza

		if (aMessage.getStringProperty(CHIAVE_ANNO_FAS_CUMULANTE) != null
				&& aMessage.getStringProperty(CHIAVE_ANNO_FAS_CUMULANTE).length() >= 1)
			this.mChiaveAnnoFasCumulante = new BigDecimal(
					aMessage.getStringProperty(CHIAVE_ANNO_FAS_CUMULANTE)); // SIEP Trasmissione Competenza
		if (aMessage.getStringProperty(CHIAVE_PROGR_FAS_CUMULANTE) != null
				&& aMessage.getStringProperty(CHIAVE_PROGR_FAS_CUMULANTE).length() >= 1)
			this.mChiaveProgrFasCumulante = new BigDecimal(
					aMessage.getStringProperty(CHIAVE_PROGR_FAS_CUMULANTE)); // SIEP Trasmissione Competenza
		if (aMessage.getStringProperty(CHIAVE_UFFICIO_FAS_CUMULANTE) != null
				&& aMessage.getStringProperty(CHIAVE_UFFICIO_FAS_CUMULANTE).length() >= 1)
			this.mChiaveUfficioFasCumulante = aMessage.getStringProperty(CHIAVE_UFFICIO_FAS_CUMULANTE);
		
//		MEV_39: TRASMETTO ANCHE DATA_EMISSIONE_CUMULO
			if (aMessage.getStringProperty(DATA_EMISSIONE_CUMULO) != null
					&& aMessage.getStringProperty(DATA_EMISSIONE_CUMULO).length() == 10)
				this.mDataEmissioneCumulo = DateUtils.getDate(aMessage.getStringProperty(DATA_EMISSIONE_CUMULO).substring(6, 10),
						aMessage.getStringProperty(DATA_EMISSIONE_CUMULO).substring(3, 5),
						aMessage.getStringProperty(DATA_EMISSIONE_CUMULO).substring(0, 2)); // DATA_EMISSIONE_CUMULO

		// =============================================================================================================
		if (aMessage.getStringProperty(ID_RICHIESTA) != null
				&& aMessage.getStringProperty(ID_RICHIESTA).length() >= 1)
			this.mIdRichiesta = new BigDecimal(aMessage.getStringProperty(ID_RICHIESTA));
		// =========================================================================
		if (aMessage.getStringProperty(DELIVERY_MODE) != null
				&& aMessage.getStringProperty(DELIVERY_MODE).length() >= 1)
			this.mDeliveryMode = aMessage.getStringProperty(DELIVERY_MODE);

		if (aMessage.getStringProperty(COD_UFFICIO_INOLTRO) != null
				&& aMessage.getStringProperty(COD_UFFICIO_INOLTRO).length() >= 1)
			this.mCodUfficioInoltro = aMessage.getStringProperty(COD_UFFICIO_INOLTRO);

		if (aMessage.getStringProperty(COD_BDI_INOLTRO) != null
				&& aMessage.getStringProperty(COD_BDI_INOLTRO).length() >= 1)
			this.mCodBdiInoltro = aMessage.getStringProperty(COD_BDI_INOLTRO);

		if (aMessage.getStringProperty(COD_UFFICIO_REPLY_TO) != null
				&& aMessage.getStringProperty(COD_UFFICIO_REPLY_TO).length() >= 1)
			this.mCodUfficioReplyTo = aMessage.getStringProperty(COD_UFFICIO_REPLY_TO);

		if (aMessage.getStringProperty(COD_BDI_REPLY_TO) != null
				&& aMessage.getStringProperty(COD_BDI_REPLY_TO).length() >= 1)
			this.mCodBdiReplyTo = aMessage.getStringProperty(COD_BDI_REPLY_TO);

		if (aMessage.getStringProperty(JMS_CORRELATION_REPLY_TO) != null
				&& aMessage.getStringProperty(JMS_CORRELATION_REPLY_TO).length() >= 1)
			this.mJmsCorrelationReplyTo = aMessage.getStringProperty(JMS_CORRELATION_REPLY_TO);

		if (aMessage.getStringProperty(ID_MESSAGGIO_SOLLECITATO) != null
				&& aMessage.getStringProperty(ID_MESSAGGIO_SOLLECITATO).length() >= 1)
			this.mIdMessaggioSollecitato = aMessage.getStringProperty(ID_MESSAGGIO_SOLLECITATO);

		// =========================================================================

		this.mFlagVisto = "N";
		this.setDataInvio(DateUtils.getSysDate());
		this.mMessaggiCorrelati = null;
		this.mMessaggiSollecito = null;

		Object lObj = aMessage.getObject();
		TreeModel lTree;
		if (lObj instanceof TreeModel) {
			lTree = (TreeModel) lObj;
			this.setTreeModel(lTree);
		}
	}
	
	
	/**
	 * AGGIUNGO METODO PER SEGNALAZIONE m_dg.DOG07.06-08-2018.0025591.U per versione sies 11.3 (introduco il catch per InvalidClassException)
	 *
	 * @param aMessage
	 * @param er
	 * @throws Exception
	 */
	public MessaggioModel(ObjectMessage aMessage, String er) throws Exception {
		this.mCodTipoMessaggio = aMessage.getStringProperty(TIPO_MESSAGGIO);
		this.mCodTipoOperazione = aMessage.getStringProperty(TIPO_OPERAZIONE);
		this.mCodUfficioMittente = aMessage.getStringProperty(UFFICIO_MITTENTE);
		this.mCodBdiMittente = aMessage.getStringProperty(COD_BDI_MITTENTE);
		this.mCodUfficioDestinatario = aMessage.getStringProperty(UFFICIO_DESTINATARIO);
		this.mCodBdiDestinataria = aMessage.getStringProperty(COD_BDI_DESTINATARIA);
		this.mJmsIdMessaggio = aMessage.getJMSMessageID();
		this.mCodiceUtenteMittente = aMessage.getStringProperty(UTENTE_MITTENTE);
		this.mJmsCorrelationIdMessage = aMessage.getStringProperty(CORRELATION_ID_MESSAGGIO);
		this.mCodEsito = aMessage.getStringProperty(COD_ESITO);
		this.mNote = aMessage.getStringProperty(NOTE); // SIEP Trasmissione Competenza

		if (aMessage.getStringProperty(CHIAVE_ANNO_SIEP) != null
				&& aMessage.getStringProperty(CHIAVE_ANNO_SIEP).length() >= 1)
			this.mChiaveAnnoSiep = new BigDecimal(aMessage.getStringProperty(CHIAVE_ANNO_SIEP));
		if (aMessage.getStringProperty(CHIAVE_PROGR_SIEP) != null
				&& aMessage.getStringProperty(CHIAVE_PROGR_SIEP).length() >= 1)
			this.mChiaveProgrSiep = new BigDecimal(aMessage.getStringProperty(CHIAVE_PROGR_SIEP));
		if (aMessage.getStringProperty(CHIAVE_UFFICIO_SIEP) != null
				&& aMessage.getStringProperty(CHIAVE_UFFICIO_SIEP).length() >= 1)
			this.mChiaveUfficioSiep = aMessage.getStringProperty(CHIAVE_UFFICIO_SIEP);

		if (aMessage.getStringProperty(CHIAVE_ANNO_SIUS) != null
				&& aMessage.getStringProperty(CHIAVE_ANNO_SIUS).length() >= 1)
			this.mChiaveAnnoSius = new BigDecimal(aMessage.getStringProperty(CHIAVE_ANNO_SIUS));
		if (aMessage.getStringProperty(CHIAVE_PROGR_SIUS) != null
				&& aMessage.getStringProperty(CHIAVE_PROGR_SIUS).length() >= 1)
			this.mChiaveProgrSius = new BigDecimal(aMessage.getStringProperty(CHIAVE_PROGR_SIUS));

		if (aMessage.getStringProperty(CHIAVE_ANNO_SIEPE) != null
				&& aMessage.getStringProperty(CHIAVE_ANNO_SIEPE).length() >= 1)
			this.mChiaveAnnoSiepe = new BigDecimal(aMessage.getStringProperty(CHIAVE_ANNO_SIEPE)); // UEPE
		if (aMessage.getStringProperty(CHIAVE_PROGR_SIEPE) != null
				&& aMessage.getStringProperty(CHIAVE_PROGR_SIEPE).length() >= 1)
			this.mChiaveProgrSiepe = new BigDecimal(aMessage.getStringProperty(CHIAVE_PROGR_SIEPE)); // UEPE
		if (aMessage.getStringProperty(NOME_SOGGETTO) != null
				&& aMessage.getStringProperty(NOME_SOGGETTO).length() >= 1)
			this.mNomeSoggetto = new String(aMessage.getStringProperty(NOME_SOGGETTO)); // UEPE - SIEP
																						// Trasmissione
																						// Competenza
		if (aMessage.getStringProperty(COGNOME_SOGGETTO) != null
				&& aMessage.getStringProperty(COGNOME_SOGGETTO).length() >= 1)
			this.mCognomeSoggetto = new String(aMessage.getStringProperty(COGNOME_SOGGETTO)); // UEPE - SIEP
																								// Trasmissione
																								// Competenza
		/*
		 * Per il momento commentato Luigi 11-08-2006 if(aMessage.getObjectProperty(DATA_NASCITA)!=null )
		 * this.mDataNascita = (Date)(aMessage.getObjectProperty(DATA_NASCITA)); // UEPE
		 */
		if (aMessage.getStringProperty(DATA_NASCITA) != null
				&& aMessage.getStringProperty(DATA_NASCITA).length() == 10)
			this.mDataNascita = DateUtils.getDate(aMessage.getStringProperty(DATA_NASCITA).substring(6, 10),
					aMessage.getStringProperty(DATA_NASCITA).substring(3, 5),
					aMessage.getStringProperty(DATA_NASCITA).substring(0, 2)); // UEPE

		if (aMessage.getStringProperty(COD_STATO_NASCITA) != null
				&& aMessage.getStringProperty(COD_STATO_NASCITA).length() >= 1)
			this.mCodStatoNascita = new String(aMessage.getStringProperty(COD_STATO_NASCITA)); // UEPE - SIEP
																								// Trasmissione
																								// Competenza
		if (aMessage.getStringProperty(COD_COMUNE_NASCITA) != null
				&& aMessage.getStringProperty(COD_COMUNE_NASCITA).length() >= 1)
			this.mCodComuneNascita = new String(aMessage.getStringProperty(COD_COMUNE_NASCITA)); // UEPE -
																									// SIEP
																									// Trasmissione
																									// Competenza

		if (aMessage.getStringProperty(CHIAVE_ANNO_FAS_CUMULANTE) != null
				&& aMessage.getStringProperty(CHIAVE_ANNO_FAS_CUMULANTE).length() >= 1)
			this.mChiaveAnnoFasCumulante = new BigDecimal(
					aMessage.getStringProperty(CHIAVE_ANNO_FAS_CUMULANTE)); // SIEP Trasmissione Competenza
		if (aMessage.getStringProperty(CHIAVE_PROGR_FAS_CUMULANTE) != null
				&& aMessage.getStringProperty(CHIAVE_PROGR_FAS_CUMULANTE).length() >= 1)
			this.mChiaveProgrFasCumulante = new BigDecimal(
					aMessage.getStringProperty(CHIAVE_PROGR_FAS_CUMULANTE)); // SIEP Trasmissione Competenza
		if (aMessage.getStringProperty(CHIAVE_UFFICIO_FAS_CUMULANTE) != null
				&& aMessage.getStringProperty(CHIAVE_UFFICIO_FAS_CUMULANTE).length() >= 1)
			this.mChiaveUfficioFasCumulante = aMessage.getStringProperty(CHIAVE_UFFICIO_FAS_CUMULANTE);
		
		//	MEV_39: TRASMETTO ANCHE DATA_EMISSIONE_CUMULO
		if (aMessage.getStringProperty(DATA_EMISSIONE_CUMULO) != null
				&& aMessage.getStringProperty(DATA_EMISSIONE_CUMULO).length() == 10)
			this.mDataEmissioneCumulo = DateUtils.getDate(aMessage.getStringProperty(DATA_EMISSIONE_CUMULO).substring(6, 10),
					aMessage.getStringProperty(DATA_EMISSIONE_CUMULO).substring(3, 5),
					aMessage.getStringProperty(DATA_EMISSIONE_CUMULO).substring(0, 2)); // DATA_EMISSIONE_CUMULO

		// =============================================================================================================
		if (aMessage.getStringProperty(ID_RICHIESTA) != null
				&& aMessage.getStringProperty(ID_RICHIESTA).length() >= 1)
			this.mIdRichiesta = new BigDecimal(aMessage.getStringProperty(ID_RICHIESTA));
		// =========================================================================
		if (aMessage.getStringProperty(DELIVERY_MODE) != null
				&& aMessage.getStringProperty(DELIVERY_MODE).length() >= 1)
			this.mDeliveryMode = aMessage.getStringProperty(DELIVERY_MODE);

		if (aMessage.getStringProperty(COD_UFFICIO_INOLTRO) != null
				&& aMessage.getStringProperty(COD_UFFICIO_INOLTRO).length() >= 1)
			this.mCodUfficioInoltro = aMessage.getStringProperty(COD_UFFICIO_INOLTRO);

		if (aMessage.getStringProperty(COD_BDI_INOLTRO) != null
				&& aMessage.getStringProperty(COD_BDI_INOLTRO).length() >= 1)
			this.mCodBdiInoltro = aMessage.getStringProperty(COD_BDI_INOLTRO);

		if (aMessage.getStringProperty(COD_UFFICIO_REPLY_TO) != null
				&& aMessage.getStringProperty(COD_UFFICIO_REPLY_TO).length() >= 1)
			this.mCodUfficioReplyTo = aMessage.getStringProperty(COD_UFFICIO_REPLY_TO);

		if (aMessage.getStringProperty(COD_BDI_REPLY_TO) != null
				&& aMessage.getStringProperty(COD_BDI_REPLY_TO).length() >= 1)
			this.mCodBdiReplyTo = aMessage.getStringProperty(COD_BDI_REPLY_TO);

		if (aMessage.getStringProperty(JMS_CORRELATION_REPLY_TO) != null
				&& aMessage.getStringProperty(JMS_CORRELATION_REPLY_TO).length() >= 1)
			this.mJmsCorrelationReplyTo = aMessage.getStringProperty(JMS_CORRELATION_REPLY_TO);

		if (aMessage.getStringProperty(ID_MESSAGGIO_SOLLECITATO) != null
				&& aMessage.getStringProperty(ID_MESSAGGIO_SOLLECITATO).length() >= 1)
			this.mIdMessaggioSollecitato = aMessage.getStringProperty(ID_MESSAGGIO_SOLLECITATO);

		// =========================================================================

		this.mFlagVisto = "N";
		this.setDataInvio(DateUtils.getSysDate());
		this.mMessaggiCorrelati = null;
		this.mMessaggiSollecito = null;
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdMessaggio() {
		return mIdMessaggio;
	}

	public String getCodTipoMessaggio() {
		return mCodTipoMessaggio;
	}

	public String getDescrTipoMessaggio() {
		return mDescrTipoMessaggio;
	}

	public String getCodTipoOperazione() {
		return mCodTipoOperazione;
	}

	public String getDescrTipoOperazione() {
		return mDescrTipoOperazione;
	}

	public String getCodUfficioMittente() {
		return mCodUfficioMittente;
	}

	public String getDescrUfficioMittente() {
		return mDescrUfficioMittente;
	}

	public String getCodBdiMittente() {
		return mCodBdiMittente;
	}

	public String getDescrBdiMittente() {
		return mDescrBdiMittente;
	}

	public String getCodUfficioDestinatario() {
		return mCodUfficioDestinatario;
	}

	public String getDescrUfficioDestinatario() {
		return mDescrUfficioDestinatario;
	}

	public String getCodBdiDestinataria() {
		return mCodBdiDestinataria;
	}

	public String getDescrBdiDestinataria() {
		return mDescrBdiDestinataria;
	}

	public Date getDataInvio() {
		return mDataInvio;
	}

	public Date getDataEsito() {
		return mDataEsito;
	}

	public String getCodiceUtenteMittente() {
		return mCodiceUtenteMittente;
	}

	public String getDescriceUtenteMittente() {
		return mDescriceUtenteMittente;
	}

	public String getCodEsito() {
		return mCodEsito;
	}

	public String getDescrEsito() {
		return mDescrEsito;
	}

	public String getJmsIdMessaggio() {
		return mJmsIdMessaggio;
	}

	public String getDescrSedeUfficioDestinatario() {
		return mDescrSedeUfficioDestinatario;
	}

	public String getDescrSedeUfficioMittente() {
		return mDescrSedeUfficioMittente;
	}

	public String getJmsCorrelationIdMessage() {
		return mJmsCorrelationIdMessage;
	}

	public String getFlagVisto() {
		return mFlagVisto;
	}

	public boolean getStessaBDI() {
		return mStessaBDI;
	}

	public BigDecimal getChiaveAnnoSiep() {
		return mChiaveAnnoSiep;
	}

	public BigDecimal getChiaveProgrSiep() {
		return mChiaveProgrSiep;
	}

	public String getChiaveUfficioSiep() {
		return mChiaveUfficioSiep;
	}

	public String getDescrUfficioSiep() {
		return mDescrUfficioSiep;
	}

	public String getDescrSedeUfficioSiep() {
		return mDescrSedeUfficioSiep;
	}

	public BigDecimal getChiaveAnnoSius() {
		return mChiaveAnnoSius;
	}

	public BigDecimal getChiaveProgrSius() {
		return mChiaveProgrSius;
	}

	// public Blob getBlobEsito() { return mBlobEsito; }
	public ByteArrayInputStream getBlobIn() {
		return mBlobIn;
	}

	public ByteArrayOutputStream getBlobOut() {
		return mBlobOut;
	}

	public TreeModel getTreeModel() {
		return mTreeModel;
	}

	public MessaggioModel getMessaggioCorrelato() {
		return mMessaggioCorrelato;
	}

	public String getRapportoEsito() {
		return mRapportoEsito;
	}

	public Vector getMessaggiCorrelati() {
		return mMessaggiCorrelati;
	}

	public ContatoreEsitiModel getContatoreEsiti() {
		return mContatoreEsiti;
	}

	public BigDecimal getChiaveAnnoSiepe() {
		return mChiaveAnnoSiepe;
	} // UEPE

	public BigDecimal getChiaveProgrSiepe() {
		return mChiaveProgrSiepe;
	} // UEPE

	public String getNomeSoggetto() {
		return mNomeSoggetto;
	} // UEPE - SIEP Trasmissione Competenza

	public String getCognomeSoggetto() {
		return mCognomeSoggetto;
	} // UEPE - SIEP Trasmissione Competenza

	public Date getDataNascita() {
		return mDataNascita;
	} // UEPE - SIEP Trasmissione Competenza

	public String getCodStatoNascita() {
		return mCodStatoNascita;
	} // UEPE - SIEP Trasmissione Competenza

	public String getCodComuneNascita() {
		return mCodComuneNascita;
	} // UEPE - SIEP Trasmissione Competenza

	public BigDecimal getChiaveAnnoFasCumulante() {
		return mChiaveAnnoFasCumulante;
	} // SIEP Trasmissione Competenza

	public BigDecimal getChiaveProgrFasCumulante() {
		return mChiaveProgrFasCumulante;
	} // SIEP Trasmissione Competenza

	public String getChiaveUfficioFasCumulante() {
		return mChiaveUfficioFasCumulante;
	}

	public String getDescrUfficioFasCumulante() {
		return mDescrUfficioFasCumulante;
	}

	public String getDescrSedeUfficioFasCumulante() {
		return mDescrSedeUfficioFasCumulante;
	}

	public String getNote() {
		return mNote;
	} // SIEP Trasmissione Competenza

	public FascMsToFascSiepModel getFascMsToFascSiepModel() {
		return mFascMsToFascSiepModel;
	}

	public BigDecimal getIdRichiesta() {
		return mIdRichiesta;
	}

	public String getDeliveryMode() {
		return mDeliveryMode;
	}

	public String getCodUfficioInoltro() {
		return mCodUfficioInoltro;
	}

	public String getDescrUfficioInoltro() {
		return mDescrUfficioInoltro;
	}

	public String getDescrSedeUfficioInoltro() {
		return mDescrSedeUfficioInoltro;
	}

	public String getCodBdiInoltro() {
		return mCodBdiInoltro;
	}

	public String getCodUfficioReplyTo() {
		return mCodUfficioReplyTo;
	}

	public String getDescrUfficioReplyTo() {
		return mDescrUfficioReplyTo;
	}

	public String getDescrSedeUfficioReplyTo() {
		return mDescrSedeUfficioReplyTo;
	}

	public String getCodBdiReplyTo() {
		return mCodBdiReplyTo;
	}

	public String getJmsCorrelationReplyTo() {
		return mJmsCorrelationReplyTo;
	}

	public String getIdMessaggioSollecitato() {
		return mIdMessaggioSollecitato;
	}

	public boolean getIsErroreParser() {
		return mIsErroreParser;
	}

	public Vector<MessaggioModel> getMessaggiSollecito() {
		return mMessaggiSollecito;
	}

	public Date getDataUltimoSollecito() {
		return mDataUltimoSollecito;
	}
	//
	// METODI SET()
	//

	public void setIdMessaggio(BigDecimal aValore) {
		mIdMessaggio = aValore;
	}

	public void setCodTipoMessaggio(String aValore) {
		mCodTipoMessaggio = aValore;
	}

	public void setDescrTipoMessaggio(String aValore) {
		mDescrTipoMessaggio = aValore;
	}

	public void setCodTipoOperazione(String aValore) {
		mCodTipoOperazione = aValore;
	}

	public void setDescrTipoOperazione(String aValore) {
		mDescrTipoOperazione = aValore;
	}

	public void setCodUfficioMittente(String aValore) {
		mCodUfficioMittente = aValore;
	}

	public void setDescrUfficioMittente(String aValore) {
		mDescrUfficioMittente = aValore;
	}

	public void setCodBdiMittente(String aValore) {
		mCodBdiMittente = aValore;
	}

	public void setDescrBdiMittente(String aValore) {
		mDescrBdiMittente = aValore;
	}

	public void setCodUfficioDestinatario(String aValore) {
		mCodUfficioDestinatario = aValore;
	}

	public void setDescrUfficioDestinatario(String aValore) {
		mDescrUfficioDestinatario = aValore;
	}

	public void setCodBdiDestinataria(String aValore) {
		mCodBdiDestinataria = aValore;
	}

	public void setDescrBdiDestinataria(String aValore) {
		mDescrBdiDestinataria = aValore;
	}

	public void setDataInvio(Date aValore) {
		mDataInvio = aValore;
	}

	public void setDataEsito(Date aValore) {
		mDataEsito = aValore;
	}

	public void setCodiceUtenteMittente(String aValore) {
		mCodiceUtenteMittente = aValore;
	}

	public void setDescriceUtenteMittente(String aValore) {
		mDescriceUtenteMittente = aValore;
	}

	public void setCodEsito(String aValore) {
		mCodEsito = aValore;
	}

	public void setDescrEsito(String aValore) {
		mDescrEsito = aValore;
	}

	public void setJmsIdMessaggio(String aValore) {
		mJmsIdMessaggio = aValore;
	}

	public void setDescrSedeUfficioDestinatario(String aValore) {
		mDescrSedeUfficioDestinatario = aValore;
	}

	public void setDescrSedeUfficioMittente(String aValore) {
		mDescrSedeUfficioMittente = aValore;
	}

	public void setChiaveAnnoSiep(BigDecimal aValore) {
		mChiaveAnnoSiep = aValore;
	}

	public void setChiaveProgrSiep(BigDecimal aValore) {
		mChiaveProgrSiep = aValore;
	}

	public void setChiaveUfficioSiep(String aValore) {
		mChiaveUfficioSiep = aValore;
	}

	public void setDescrUfficioSiep(String aValore) {
		mDescrUfficioSiep = aValore;
	}

	public void setDescrSedeUfficioSiep(String aValore) {
		mDescrSedeUfficioSiep = aValore;
	}

	public void setChiaveAnnoSius(BigDecimal aValore) {
		mChiaveAnnoSius = aValore;
	}

	public void setChiaveProgrSius(BigDecimal aValore) {
		mChiaveProgrSius = aValore;
	}

	// public void setBlobEsito(Blob aValore ) { mBlobEsito = aValore; }
	public void setBlobIn(ByteArrayInputStream aValore) {
		mBlobIn = aValore;
	}

	public void setBlobOut(ByteArrayOutputStream aValore) {
		mBlobOut = aValore;
	}

	public void setTreeModel(TreeModel aValore) {
		mTreeModel = aValore;
	}

	public void setJmsCorrelationIdMessage(String aValore) {
		mJmsCorrelationIdMessage = aValore;
	}

	public void setMessaggioCorrelato(MessaggioModel aValore) {
		mMessaggioCorrelato = aValore;
	}

	public void setFlagVisto(String aValore) {
		mFlagVisto = aValore;
	}

	public void setStessaBDI(boolean aValore) {
		mStessaBDI = aValore;
	}

	public void setRapportoEsito(String aValore) {
		mRapportoEsito = aValore;
	}

	public void setMessaggiCorrelati(Vector aValore) {
		mMessaggiCorrelati = aValore;
	}

	public void setContatoreEsiti(ContatoreEsitiModel aValore) {
		mContatoreEsiti = aValore;
	}

	public void setChiaveAnnoSiepe(BigDecimal aValore) {
		mChiaveAnnoSiepe = aValore;
	} // UEPE

	public void setChiaveProgrSiepe(BigDecimal aValore) {
		mChiaveProgrSiepe = aValore;
	} // UEPE

	public void setNomeSoggetto(String aValore) {
		mNomeSoggetto = aValore;
	} // UEPE - SIEP Trasmissione Competenza

	public void setCognomeSoggetto(String aValore) {
		mCognomeSoggetto = aValore;
	} // UEPE - SIEP Trasmissione Competenza

	public void setDataNascita(Date aValore) {
		mDataNascita = aValore;
	} // UEPE - SIEP Trasmissione Competenza

	public void setCodStatoNascita(String aValore) {
		mCodStatoNascita = aValore;
	} // UEPE - SIEP Trasmissione Competenza

	public void setCodComuneNascita(String aValore) {
		mCodComuneNascita = aValore;
	} // UEPE - SIEP Trasmissione Competenza

	public void setChiaveAnnoFasCumulante(BigDecimal aValore) {
		mChiaveAnnoFasCumulante = aValore;
	} // SIEP Trasmissione Competenza

	public void setChiaveProgrFasCumulante(BigDecimal aValore) {
		mChiaveProgrFasCumulante = aValore;
	} // SIEP Trasmissione Competenza

	public void setChiaveUfficioFasCumulante(String aValore) {
		mChiaveUfficioFasCumulante = aValore;
	}

	public void setDescrUfficioFasCumulante(String aValore) {
		mDescrUfficioFasCumulante = aValore;
	}

	public void setDescrSedeUfficioFasCumulante(String aValore) {
		mDescrSedeUfficioFasCumulante = aValore;
	}

	public void setNote(String aValore) {
		mNote = aValore;
	} // SIEP Trasmissione Competenza

	public void setFascMsToFascSiepModel(FascMsToFascSiepModel aValore) {
		mFascMsToFascSiepModel = aValore;
	}

	public void setIdRichiesta(BigDecimal aValore) {
		mIdRichiesta = aValore;
	}

	public void setDeliveryMode(String aValore) {
		mDeliveryMode = aValore;
	}

	public void setCodUfficioInoltro(String aValore) {
		mCodUfficioInoltro = aValore;
	}

	public void setDescrUfficioInoltro(String aValore) {
		mDescrUfficioInoltro = aValore;
	}

	public void setDescrSedeUfficioInoltro(String aValore) {
		mDescrSedeUfficioInoltro = aValore;
	}

	public void setCodBdiInoltro(String aValore) {
		mCodBdiInoltro = aValore;
	}

	public void setCodUfficioReplyTo(String aValore) {
		mCodUfficioReplyTo = aValore;
	}

	public void setDescrUfficioReplyTo(String aValore) {
		mDescrUfficioReplyTo = aValore;
	}

	public void setDescrSedeUfficioReplyTo(String aValore) {
		mDescrSedeUfficioReplyTo = aValore;
	}

	public void setCodBdiReplyTo(String aValore) {
		mCodBdiReplyTo = aValore;
	}

	public void setJmsCorrelationReplyTo(String aValore) {
		mJmsCorrelationReplyTo = aValore;
	}

	public void setIdMessaggioSollecitato(String aValore) {
		mIdMessaggioSollecitato = aValore;
	}

	public void setIsErroreParser(boolean aValore) {
		mIsErroreParser = aValore;
	}

	public void setDataUltimoSollecito(Date aValore) {
		mDataUltimoSollecito = aValore;
	}

	public void setMessaggiSollecito(Vector<MessaggioModel> aValore) {
		mMessaggiSollecito = aValore;
	}
		
    // mev 39
	public Date getDataEmissioneCumulo() {
		return mDataEmissioneCumulo;
	}

	public void setDataEmissioneCumulo(Date mDataEmissioneCumulo) {
		this.mDataEmissioneCumulo = mDataEmissioneCumulo;
	}
	// mev 39

	// Ticket#20220111018 - Aggiunta canta Solleciti 
	public BigDecimal getContaSolleciti() {
		return mContaSolleciti;
	}

	public void setContaSolleciti(BigDecimal aContaSolleciti) {
		this.mContaSolleciti = aContaSolleciti;
	}
	// Ticket#20220111018 - Aggiunta canta Solleciti - FINE

	
	public String toString() {
		String lStr = new String();

		lStr = "" + "[ mIdMessaggio                = " + mIdMessaggio + "]\n"
				+ "[ mCodTipoMessaggio           = " + mCodTipoMessaggio + "]\n"
				+ "[ mDescrTipoMessaggio         = " + mDescrTipoMessaggio + "]\n"
				+ "[ mCodTipoOperazione          = " + mCodTipoOperazione + "]\n"
				+ "[ mDescrTipoOperazione        = " + mDescrTipoOperazione + "]\n"
				+ "[ mCodUfficioMittente         = " + mCodUfficioMittente + "]\n"
				+ "[ mDescrUfficioMittente       = " + mDescrUfficioMittente + "]\n"
				+ "[ mCodBdiMittente             = " + mCodBdiMittente + "]\n"
				+ "[ mDescrBdiMittente           = " + mDescrBdiMittente + "]\n"
				+ "[ mCodUfficioDestinatario     = " + mCodUfficioDestinatario + "]\n"
				+ "[ mDescrUfficioDestinatario   = " + mDescrUfficioDestinatario + "]\n"
				+ "[ mCodBdiDestinataria         = " + mCodBdiDestinataria + "]\n"
				+ "[ mDescrBdiDestinataria       = " + mDescrBdiDestinataria + "]\n"
				+ "[ mDataInvio                  = " + mDataInvio + "]\n" + "[ mDataEsito                  = "
				+ mDataEsito + "]\n" + "[ mCodiceUtenteMittente       = " + mCodiceUtenteMittente + "]\n"
				+ "[ mDescriceUtenteMittente     = " + mDescriceUtenteMittente + "]\n"
				+ "[ mCodEsito                   = " + mCodEsito + "]\n" + "[ mJmsIdMessaggio             = "
				+ mJmsIdMessaggio + "]\n" + "[ mFlagVisto                  = " + mFlagVisto + "]\n"
				+ "[ mDescrEsito                 = " + mDescrEsito + "]\n"
				+ "[ mChiaveAnnoSiep             = " + mChiaveAnnoSiep + "]\n"
				+ "[ mChiaveProgrSiep            = " + mChiaveProgrSiep + "]\n"
				+ "[ mChiaveUfficioSiep          = " + mChiaveUfficioSiep + "]\n"
				+ "[ mDescrUfficioSiep           = " + mDescrUfficioSiep + "]\n"
				+ "[ mDescrSedeUfficioSiep       = " + mDescrSedeUfficioSiep + "]\n"
				+ "[ mChiaveAnnoSius             = " + mChiaveAnnoSius + "]\n"
				+ "[ mChiaveProgrSius            = " + mChiaveProgrSius + "]\n"
				+ "[ mChiaveAnnoSiepe            = " + mChiaveAnnoSiepe + "]\n" + // UEPE
				"[ mChiaveProgrSiepe           = " + mChiaveProgrSiepe + "]\n" + // UEPE
				"[ mNomeSoggetto               = " + mNomeSoggetto + "]\n" + // UEPE - SIEP Trasmissione
																				// Competenza
				"[ mCognomeSoggetto            = " + mCognomeSoggetto + "]\n" + // UEPE - SIEP Trasmissione
																				// Competenza
				"[ mDataNascita                = " + mDataNascita + "]\n" + // UEPE - SIEP Trasmissione
																			// Competenza
				"[ mCodStatoNascita            = " + mCodStatoNascita + "]\n" + // UEPE - SIEP Trasmissione
																				// Competenza
				"[ mCodComuneNascita           = " + mCodComuneNascita + "]\n" + // UEPE - SIEP Trasmissione
																					// Competenza
				"[ mChiaveAnnoFasCumulante     = " + mChiaveAnnoFasCumulante + "]\n" + // SIEP Trasmissione
																						// Competenza
				"[ mChiaveProgrFasCumulante    = " + mChiaveProgrFasCumulante + "]\n" + // SIEP Trasmissione
																						// Competenza
				"[ mChiaveUfficioFasCumulante   = " + mChiaveUfficioFasCumulante + "]\n" + // SIEP
																							// Trasmissione
																							// Competenza
				"[ mDescrUfficioFasCumulante    = " + mDescrUfficioFasCumulante + "]\n" + // SIEP Trasmissione
																							// Competenza
				"[ mDescrSedeUfficioFasCumulante= " + mDescrSedeUfficioFasCumulante + "]\n" + // SIEP
																								// Trasmissione
																								// Competenza
					"[ mDataEmissioneCumulo= " + mDataEmissioneCumulo + "]\n" + // SIEP	// Trasmissione			// Competenza																								

				"[ mNote                       = " + mNote + "]\n" + // SIEP Trasmissione Competenza

				"[ mIdRichiesta                = " + mIdRichiesta + "]\n" + "[ mDeliveryMode               = "
				+ mDeliveryMode + "]\n" + "[ mCodUfficioInoltro          = " + mCodUfficioInoltro + "]\n"
				+ "[ mDescrUfficioInoltro        = " + mDescrUfficioInoltro + "]\n"
				+ "[ mDescrSedeUfficioInoltro    = " + mDescrSedeUfficioInoltro + "]\n"
				+ "[ mCodBdiInoltro              = " + mCodBdiInoltro + "]\n" +

				"[ mCodUfficioReplyTo          = " + mCodUfficioReplyTo + "]\n"
				+ "[ mDescrUfficioReplyTo        = " + mDescrUfficioReplyTo + "]\n"
				+ "[ mDescrSedeUfficioReplyTo    = " + mDescrSedeUfficioReplyTo + "]\n"
				+ "[ mCodBdiReplyTo              = " + mCodBdiReplyTo + "]\n" +

				"[ mJmsCorrelationReplyTo      = " + mJmsCorrelationReplyTo + "]\n"
				+ "[ mIdMessaggioSollecitato     = " + mIdMessaggioSollecitato + "]\n" +

				"[ mIsErroreParser             = " + mIsErroreParser + "]\n"
				+ "[ mStessaBDI                  = " + mStessaBDI + "]";

		if (mMessaggiCorrelati != null) {
			lStr += "[ mMessaggiCorrelati (numero) = " + mMessaggiCorrelati.size() + "]";
		}
		if (mMessaggiSollecito != null) {
			lStr += "[ mMessaggiSollecito (numero) = " + mMessaggiSollecito.size() + "]";
		}

		return lStr;
	}

	/**
	 * Verifica la correttezza del messaggio che deve essere inviato per la ricerca su diverse BDI infatti non
	 * viene controllato il settaggio della BDI destinataria e dell'Ufficio di destinazione. La funzione di
	 * ricerca su varie BDI infatti setta il destinatario a TUTTE
	 * 
	 * @throws F3BException
	 */
	public void VerifyMessage() throws F3BException {
		// Verifica Messaggio da inviare
		if (this.getCodTipoMessaggio() == null || this.getCodTipoMessaggio().equals(""))
			throw new F3BException(F3BException.USER_MESSAGE,
					"Impossibile spedire un messaggio senza aver impostato il Tipo Messaggio");
		if (this.getCodTipoOperazione() == null || this.getCodTipoOperazione().equals(""))
			throw new F3BException(F3BException.USER_MESSAGE,
					"Impossibile spedire un messaggio senza aver impostato il Tipo Operazione");
		if (this.getCodBdiMittente() == null || this.getCodBdiMittente().equals(""))
			throw new F3BException(F3BException.USER_MESSAGE,
					"Impossibile spedire un messaggio senza aver impostato la BDI Mittente");
		if (this.getTreeModel() == null)
			throw new F3BException(F3BException.USER_MESSAGE,
					"Impossibile spedire un messaggio senza avere contenuto di Dati");
		if (this.getCodUfficioMittente() == null || this.getCodUfficioMittente().equals(""))
			throw new F3BException(F3BException.USER_MESSAGE,
					"Impossibile spedire un messaggio senza avere impostato l'Ufficio Mittente");
	}

	/**
	 * Verifica la correttezza formale del messggio da inviare ad una singola destinazione
	 * 
	 * @throws F3BException
	 */
	public void VerifySingleMessage() throws F3BException {
		this.VerifyMessage();

		if (this.getCodBdiDestinataria() == null || this.getCodBdiDestinataria().equals(""))
			throw new F3BException(F3BException.USER_MESSAGE,
					"Impossibile spedire un messaggio senza aver impostato la BDI Destinataria");
		if (this.getCodUfficioDestinatario() == null || this.getCodUfficioDestinatario().equals(""))
			throw new F3BException(F3BException.USER_MESSAGE,
					"Impossibile spedire un messaggio senza avere impostato l'Ufficio Destinatario");

	}

	
	
	
}