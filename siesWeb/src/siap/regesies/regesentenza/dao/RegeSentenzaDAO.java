package siap.regesies.regesentenza.dao;

import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.regesies.regesentenza.model.RegeSentenzaModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
* <p>Title: RegeSentenzaDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella RegeSentenza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class RegeSentenzaDAO extends SIAPTableDAO
{
	public RegeSentenzaDAO (Connection con)
	{
			 super(con);
			 setTable("rege_sentenza");

			 //Settare la Sequence e i campi chiave

			 setField("ID_FILE", STRING);
			 setField("COD_TIPO_PROVVEDIMENTO", STRING);
			 setField("ANNO_REGE_PM", INT);
			 setField("NUMERO_REGE_PM", STRING);
			 setField("DATA_ARRIVO_ATTO", DATE);
			 setField("DATA_PROVVEDIMENTO", DATE);
			 setField("COD_TIPO_AUTORITA_EMITTENTE", STRING);
			 setField("COD_LUOGO_EMITTENTE", STRING);
			 setField("NUM_SEZIONE_AUTORITA_EMITTENTE", STRING);
			 setField("ANNO_SENTENZA", INT);
			 setField("NUMERO_SENTENZA", STRING);
			 setField("DATA_IRREVOCABILITA", DATE);
			 setField("FLAG_SENTENZA_APPLICAZ_PENA", STRING);
			 setField("COD_TIPO_PROVV_RIF", STRING);
			 setField("DATA_PROVV_RIF", DATE);
			 setField("COD_TIPO_AUTORITA_PROVV_RIF", STRING);
			 setField("ANNO_PROVV_RIF", INT);
			 setField("NUMERO_PROVV_RIF", STRING);
			 setField("COD_LUOGO_PROVV_RIF", STRING);
			 setField("NUM_SEZIONE_AUTORITA_PROVV_RIF", STRING);
			 setField("COD_TIPO_DECISIONE_CASSAZIONE", STRING);
			 setField("ANNO_SENTENZA_CASSAZIONE", INT);
			 setField("NUMERO_SENTENZA_CASSAZIONE", STRING);
			 setField("ANNO_RACCOLTA_GENERALE", INT);
			 setField("NUMERO_RACCOLTA_GENERALE", STRING);
			 setField("ANNO_REGISTRO_35", INT);
			 setField("NUM_REGISTRO_35", STRING);
			 setField("NOTE", STRING);
			 setField("DESCR_NUM_CAMPIONE_PENALE", STRING);
			 setField("ANNO_REGE_GIP", INT);
			 setField("NUMERO_REGE_GIP", STRING);
			 setField("ANNO_REGE_DIB", INT);
			 setField("NUMERO_REGE_DIB", STRING);
			 setField("ANNO_REGE_CAS", INT);
			 setField("NUMERO_REGE_CAS", STRING);
			 setField("ANNO_REGE_CAP", INT);
			 setField("NUMERO_REGE_CAP", STRING);
			 setField("ANNO_REGE_CASAP", INT);
			 setField("NUMERO_REGE_CASAP", STRING);
			 setField("NOTA_DISPOSITIVO", STRING);
			 setField("COD_TIPO_RITO", STRING);
			 setField("COD_OPERATORE_INSERIMENTO", STRING);
			 setField("DATA_INSERIMENTO", DATE);
			 setField("COD_UFFICIO_INSERIMENTO", STRING);
			 setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
			 setField("DATA_AGGIORNAMENTO", DATE);
			 setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
			 setField("FLAG_GIUDIZIO_ABBREVIATO", STRING);
	}


  //
  // METODI GET()
  //

			public String 				 getIdFile() 		throws DAOException	 { return getString("ID_FILE"); }
			public String 				 getCodTipoProvvedimento() 		throws DAOException	 { return getString("COD_TIPO_PROVVEDIMENTO"); }
			public int 		 getAnnoRegePm() 		throws DAOException	 { return getInt("ANNO_REGE_PM"); }
			public String 				 getNumeroRegePm() 		throws DAOException	 { return getString("NUMERO_REGE_PM"); }
			public Date 					 getDataArrivoAtto() 		throws DAOException	 { return getDate("DATA_ARRIVO_ATTO"); }
			public Date 					 getDataProvvedimento() 		throws DAOException	 { return getDate("DATA_PROVVEDIMENTO"); }
			public String 				 getCodTipoAutoritaEmittente() 		throws DAOException	 { return getString("COD_TIPO_AUTORITA_EMITTENTE"); }
			public String 				 getCodLuogoEmittente() 		throws DAOException	 { return getString("COD_LUOGO_EMITTENTE"); }
			public String 				 getNumSezioneAutoritaEmittente() 		throws DAOException	 { return getString("NUM_SEZIONE_AUTORITA_EMITTENTE"); }
			public int 		 getAnnoSentenza() 		throws DAOException	 { return getInt("ANNO_SENTENZA"); }
			public String 				 getNumeroSentenza() 		throws DAOException	 { return getString("NUMERO_SENTENZA"); }
			public Date 					 getDataIrrevocabilita() 		throws DAOException	 { return getDate("DATA_IRREVOCABILITA"); }
			public String 				 getFlagSentenzaApplicazPena() 		throws DAOException	 { return getString("FLAG_SENTENZA_APPLICAZ_PENA"); }
			public String 				 getCodTipoProvvRif() 		throws DAOException	 { return getString("COD_TIPO_PROVV_RIF"); }
			public Date 					 getDataProvvRif() 		throws DAOException	 { return getDate("DATA_PROVV_RIF"); }
			public String 				 getCodTipoAutoritaProvvRif() 		throws DAOException	 { return getString("COD_TIPO_AUTORITA_PROVV_RIF"); }
			public int 		 getAnnoProvvRif() 		throws DAOException	 { return getInt("ANNO_PROVV_RIF"); }
			public String 				 getNumeroProvvRif() 		throws DAOException	 { return getString("NUMERO_PROVV_RIF"); }
			public String 				 getCodLuogoProvvRif() 		throws DAOException	 { return getString("COD_LUOGO_PROVV_RIF"); }
			public String 				 getNumSezioneAutoritaProvvRif() 		throws DAOException	 { return getString("NUM_SEZIONE_AUTORITA_PROVV_RIF"); }
			public String 				 getCodTipoDecisioneCassazione() 		throws DAOException	 { return getString("COD_TIPO_DECISIONE_CASSAZIONE"); }
			public int 		 getAnnoSentenzaCassazione() 		throws DAOException	 { return getInt("ANNO_SENTENZA_CASSAZIONE"); }
			public String 				 getNumeroSentenzaCassazione() 		throws DAOException	 { return getString("NUMERO_SENTENZA_CASSAZIONE"); }
			public int 		 getAnnoRaccoltaGenerale() 		throws DAOException	 { return getInt("ANNO_RACCOLTA_GENERALE"); }
			public String 				 getNumeroRaccoltaGenerale() 		throws DAOException	 { return getString("NUMERO_RACCOLTA_GENERALE"); }
			public int 		 getAnnoRegistro35() 		throws DAOException	 { return getInt("ANNO_REGISTRO_35"); }
			public String 				 getNumRegistro35() 		throws DAOException	 { return getString("NUM_REGISTRO_35"); }
			public String 				 getNote() 		throws DAOException	 { return getString("NOTE"); }
			public String 				 getDescrNumCampionePenale() 		throws DAOException	 { return getString("DESCR_NUM_CAMPIONE_PENALE"); }
			public int 		 getAnnoRegeGip() 		throws DAOException	 { return getInt("ANNO_REGE_GIP"); }
			public String 				 getNumeroRegeGip() 		throws DAOException	 { return getString("NUMERO_REGE_GIP"); }
			public int 		 getAnnoRegeDib() 		throws DAOException	 { return getInt("ANNO_REGE_DIB"); }
			public String 				 getNumeroRegeDib() 		throws DAOException	 { return getString("NUMERO_REGE_DIB"); }
			public int 		 getAnnoRegeCas() 		throws DAOException	 { return getInt("ANNO_REGE_CAS"); }
			public String 				 getNumeroRegeCas() 		throws DAOException	 { return getString("NUMERO_REGE_CAS"); }
			public int 		 getAnnoRegeCap() 		throws DAOException	 { return getInt("ANNO_REGE_CAP"); }
			public String 				 getNumeroRegeCap() 		throws DAOException	 { return getString("NUMERO_REGE_CAP"); }
			public int 		 getAnnoRegeCasap() 		throws DAOException	 { return getInt("ANNO_REGE_CASAP"); }
			public String 				 getNumeroRegeCasap() 		throws DAOException	 { return getString("NUMERO_REGE_CASAP"); }
			public String 				 getNotaDispositivo() 		throws DAOException	 { return getString("NOTA_DISPOSITIVO"); }
			public String 				 getCodTipoRito() 		throws DAOException	 { return getString("COD_TIPO_RITO"); }
			public String 				 getCodOperatoreInserimento() 		throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); }
			public Date 					 getDataInserimento() 		throws DAOException	 { return getDate("DATA_INSERIMENTO"); }
			public String 				 getCodUfficioInserimento() 		throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); }
			public String 				 getCodOperatoreAggiornamento() 		throws DAOException	 { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
			public Date 					 getDataAggiornamento() 		throws DAOException	 { return getDate("DATA_AGGIORNAMENTO"); }
			public String 				 getCodUfficioAggiornamento() 		throws DAOException	 { return getString("COD_UFFICIO_AGGIORNAMENTO"); }
			public String 				 getFlagGiudizioAbbreviato() 		throws DAOException	 { return getString("FLAG_GIUDIZIO_ABBREVIATO"); }


  //
  // METODI SET()
  //

			public void  	 setIdFile(String aValore ) 			 { setString("ID_FILE", aValore); }
			public void  	 setCodTipoProvvedimento(String aValore ) 			 { setString("COD_TIPO_PROVVEDIMENTO", aValore); }
			public void  	 setAnnoRegePm(int aValore ) 			 { setInt("ANNO_REGE_PM", aValore); }
			public void  	 setNumeroRegePm(String aValore ) 			 { setString("NUMERO_REGE_PM", aValore); }
			public void  	 setDataArrivoAtto(Date aValore ) 			 { setDate("DATA_ARRIVO_ATTO", aValore); }
			public void  	 setDataProvvedimento(Date aValore ) 			 { setDate("DATA_PROVVEDIMENTO", aValore); }
			public void  	 setCodTipoAutoritaEmittente(String aValore ) 			 { setString("COD_TIPO_AUTORITA_EMITTENTE", aValore); }
			public void  	 setCodLuogoEmittente(String aValore ) 			 { setString("COD_LUOGO_EMITTENTE", aValore); }
			public void  	 setNumSezioneAutoritaEmittente(String aValore ) 			 { setString("NUM_SEZIONE_AUTORITA_EMITTENTE", aValore); }
			public void  	 setAnnoSentenza(int aValore ) 			 { setInt("ANNO_SENTENZA", aValore); }
			public void  	 setNumeroSentenza(String aValore ) 			 { setString("NUMERO_SENTENZA", aValore); }
			public void  	 setDataIrrevocabilita(Date aValore ) 			 { setDate("DATA_IRREVOCABILITA", aValore); }
			public void  	 setFlagSentenzaApplicazPena(String aValore ) 			 { setString("FLAG_SENTENZA_APPLICAZ_PENA", aValore); }
			public void  	 setCodTipoProvvRif(String aValore ) 			 { setString("COD_TIPO_PROVV_RIF", aValore); }
			public void  	 setDataProvvRif(Date aValore ) 			 { setDate("DATA_PROVV_RIF", aValore); }
			public void  	 setCodTipoAutoritaProvvRif(String aValore ) 			 { setString("COD_TIPO_AUTORITA_PROVV_RIF", aValore); }
			public void  	 setAnnoProvvRif(int aValore ) 			 { setInt("ANNO_PROVV_RIF", aValore); }
			public void  	 setNumeroProvvRif(String aValore ) 			 { setString("NUMERO_PROVV_RIF", aValore); }
			public void  	 setCodLuogoProvvRif(String aValore ) 			 { setString("COD_LUOGO_PROVV_RIF", aValore); }
			public void  	 setNumSezioneAutoritaProvvRif(String aValore ) 			 { setString("NUM_SEZIONE_AUTORITA_PROVV_RIF", aValore); }
			public void  	 setCodTipoDecisioneCassazione(String aValore ) 			 { setString("COD_TIPO_DECISIONE_CASSAZIONE", aValore); }
			public void  	 setAnnoSentenzaCassazione(int aValore ) 			 { setInt("ANNO_SENTENZA_CASSAZIONE", aValore); }
			public void  	 setNumeroSentenzaCassazione(String aValore ) 			 { setString("NUMERO_SENTENZA_CASSAZIONE", aValore); }
			public void  	 setAnnoRaccoltaGenerale(int aValore ) 			 { setInt("ANNO_RACCOLTA_GENERALE", aValore); }
			public void  	 setNumeroRaccoltaGenerale(String aValore ) 			 { setString("NUMERO_RACCOLTA_GENERALE", aValore); }
			public void  	 setAnnoRegistro35(int aValore ) 			 { setInt("ANNO_REGISTRO_35", aValore); }
			public void  	 setNumRegistro35(String aValore ) 			 { setString("NUM_REGISTRO_35", aValore); }
			public void  	 setNote(String aValore ) 			 { setString("NOTE", aValore); }
			public void  	 setDescrNumCampionePenale(String aValore ) 			 { setString("DESCR_NUM_CAMPIONE_PENALE", aValore); }
			public void  	 setAnnoRegeGip(int aValore ) 			 { setInt("ANNO_REGE_GIP", aValore); }
			public void  	 setNumeroRegeGip(String aValore ) 			 { setString("NUMERO_REGE_GIP", aValore); }
			public void  	 setAnnoRegeDib(int aValore ) 			 { setInt("ANNO_REGE_DIB", aValore); }
			public void  	 setNumeroRegeDib(String aValore ) 			 { setString("NUMERO_REGE_DIB", aValore); }
			public void  	 setAnnoRegeCas(int aValore ) 			 { setInt("ANNO_REGE_CAS", aValore); }
			public void  	 setNumeroRegeCas(String aValore ) 			 { setString("NUMERO_REGE_CAS", aValore); }
			public void  	 setAnnoRegeCap(int aValore ) 			 { setInt("ANNO_REGE_CAP", aValore); }
			public void  	 setNumeroRegeCap(String aValore ) 			 { setString("NUMERO_REGE_CAP", aValore); }
			public void  	 setAnnoRegeCasap(int aValore ) 			 { setInt("ANNO_REGE_CASAP", aValore); }
			public void  	 setNumeroRegeCasap(String aValore ) 			 { setString("NUMERO_REGE_CASAP", aValore); }
			public void  	 setNotaDispositivo(String aValore ) 			 { setString("NOTA_DISPOSITIVO", aValore); }
			public void  	 setCodTipoRito(String aValore ) 			 { setString("COD_TIPO_RITO", aValore); }
			public void  	 setCodOperatoreInserimento(String aValore ) 			 { setString("COD_OPERATORE_INSERIMENTO", aValore); }
			public void  	 setDataInserimento(Date aValore ) 			 { setDate("DATA_INSERIMENTO", aValore); }
			public void  	 setCodUfficioInserimento(String aValore ) 			 { setString("COD_UFFICIO_INSERIMENTO", aValore); }
			public void  	 setCodOperatoreAggiornamento(String aValore ) 			 { setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
			public void  	 setDataAggiornamento(Date aValore ) 			 { setDate("DATA_AGGIORNAMENTO", aValore); }
			public void  	 setCodUfficioAggiornamento(String aValore ) 			 { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }
			public void  	 setFlagGiudizioAbbreviato(String aValore ) 			 { setString("FLAG_GIUDIZIO_ABBREVIATO", aValore); }


	public GenericModel getModel() throws DAOException
  			 {
 				 return new RegeSentenzaModel(
								 getIdFile() ,
								 getCodTipoProvvedimento() ,
								 "",
								 getAnnoRegePm() ,
								 getNumeroRegePm() ,
								 getDataArrivoAtto() ,
								 null,
								 getDataProvvedimento() ,
								 getCodTipoAutoritaEmittente() ,
								 "",
								 getCodLuogoEmittente() ,
								 "",
								 getNumSezioneAutoritaEmittente() ,
								 getAnnoSentenza() ,
								 getNumeroSentenza() ,
								 getDataIrrevocabilita() ,
								 getFlagSentenzaApplicazPena() ,
								 getCodTipoProvvRif() ,
								 "",
								 getDataProvvRif() ,
								 getCodTipoAutoritaProvvRif() ,
								 "",
								 getAnnoProvvRif() ,
								 getNumeroProvvRif() ,
								 getCodLuogoProvvRif() ,
								 "",
								 getNumSezioneAutoritaProvvRif() ,
								 getCodTipoDecisioneCassazione() ,
								 "",
								 getAnnoSentenzaCassazione() ,
								 getNumeroSentenzaCassazione() ,
								 getAnnoRaccoltaGenerale() ,
								 getNumeroRaccoltaGenerale() ,
								 getAnnoRegistro35() ,
								 getNumRegistro35() ,
								 getNote() ,
								 getDescrNumCampionePenale() ,
								 getAnnoRegeGip() ,
								 getNumeroRegeGip() ,
								 getAnnoRegeDib() ,
								 getNumeroRegeDib() ,
								 getAnnoRegeCas() ,
								 getNumeroRegeCas() ,
								 getAnnoRegeCap() ,
								 getNumeroRegeCap() ,
								 getAnnoRegeCasap() ,
								 getNumeroRegeCasap() ,
								 getNotaDispositivo() ,
								 getCodTipoRito() ,
								 "",
								 getCodOperatoreInserimento() ,
								 getDataInserimento() ,
								 getCodUfficioInserimento() ,
								 "",
								 getCodOperatoreAggiornamento() ,
								 getDataAggiornamento() ,
								 getCodUfficioAggiornamento() ,
								 "",
								 getFlagGiudizioAbbreviato()
								);
		}


	 public void 	 setDAOFromModel(RegeSentenzaModel aModel) throws DAOException
  		{
				 setIdFile( aModel.getIdFile() );
				 setCodTipoProvvedimento( aModel.getCodTipoProvvedimento() );
				 setAnnoRegePm( aModel.getAnnoRegePm() );
				 setNumeroRegePm( aModel.getNumeroRegePm() );
				 setDataArrivoAtto( aModel.getDataArrivoAtto() );
				 setDataProvvedimento( aModel.getDataProvvedimento() );
				 setCodTipoAutoritaEmittente( aModel.getCodTipoAutoritaEmittente() );
				 setCodLuogoEmittente( aModel.getCodLuogoEmittente() );
				 setNumSezioneAutoritaEmittente( aModel.getNumSezioneAutoritaEmittente() );
				 setAnnoSentenza( aModel.getAnnoSentenza() );
				 setNumeroSentenza( aModel.getNumeroSentenza() );
				 setDataIrrevocabilita( aModel.getDataIrrevocabilita() );
				 setFlagSentenzaApplicazPena( aModel.getFlagSentenzaApplicazPena() );
				 setCodTipoProvvRif( aModel.getCodTipoProvvRif() );
				 setDataProvvRif( aModel.getDataProvvRif() );
				 setCodTipoAutoritaProvvRif( aModel.getCodTipoAutoritaProvvRif() );
				 setAnnoProvvRif( aModel.getAnnoProvvRif() );
				 setNumeroProvvRif( aModel.getNumeroProvvRif() );
				 setCodLuogoProvvRif( aModel.getCodLuogoProvvRif() );
				 setNumSezioneAutoritaProvvRif( aModel.getNumSezioneAutoritaProvvRif() );
				 setCodTipoDecisioneCassazione( aModel.getCodTipoDecisioneCassazione() );
				 setAnnoSentenzaCassazione( aModel.getAnnoSentenzaCassazione() );
				 setNumeroSentenzaCassazione( aModel.getNumeroSentenzaCassazione() );
				 setAnnoRaccoltaGenerale( aModel.getAnnoRaccoltaGenerale() );
				 setNumeroRaccoltaGenerale( aModel.getNumeroRaccoltaGenerale() );
				 setAnnoRegistro35( aModel.getAnnoRegistro35() );
				 setNumRegistro35( aModel.getNumRegistro35() );
				 setNote( aModel.getNote() );
				 setDescrNumCampionePenale( aModel.getDescrNumCampionePenale() );
				 setAnnoRegeGip( aModel.getAnnoRegeGip() );
				 setNumeroRegeGip( aModel.getNumeroRegeGip() );
				 setAnnoRegeDib( aModel.getAnnoRegeDib() );
				 setNumeroRegeDib( aModel.getNumeroRegeDib() );
				 setAnnoRegeCas( aModel.getAnnoRegeCas() );
				 setNumeroRegeCas( aModel.getNumeroRegeCas() );
				 setAnnoRegeCap( aModel.getAnnoRegeCap() );
				 setNumeroRegeCap( aModel.getNumeroRegeCap() );
				 setAnnoRegeCasap( aModel.getAnnoRegeCasap() );
				 setNumeroRegeCasap( aModel.getNumeroRegeCasap() );
				 setNotaDispositivo( aModel.getNotaDispositivo() );
				 setCodTipoRito( aModel.getCodTipoRito() );
				 setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
				 setDataInserimento( aModel.getDataInserimento() );
				 setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
				 setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
				 setDataAggiornamento( aModel.getDataAggiornamento() );
				 setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
				 setFlagGiudizioAbbreviato( aModel.getFlagGiudizioAbbreviato() );
		}


	 public void 	 setDAOFromModelForUpdate(RegeSentenzaModel aModel) throws DAOException
  		{
				 //setIdFile( aModel.getIdFile() );
				 setCodTipoProvvedimento( aModel.getCodTipoProvvedimento() );
				 setAnnoRegePm( aModel.getAnnoRegePm() );
				 setNumeroRegePm( aModel.getNumeroRegePm() );
				 setDataArrivoAtto( aModel.getDataArrivoAtto() );
				 setDataProvvedimento( aModel.getDataProvvedimento() );
				 setCodTipoAutoritaEmittente( aModel.getCodTipoAutoritaEmittente() );
				 setCodLuogoEmittente( aModel.getCodLuogoEmittente() );
				 setNumSezioneAutoritaEmittente( aModel.getNumSezioneAutoritaEmittente() );
				 setAnnoSentenza( aModel.getAnnoSentenza() );
				 setNumeroSentenza( aModel.getNumeroSentenza() );
				 setDataIrrevocabilita( aModel.getDataIrrevocabilita() );
				 setFlagSentenzaApplicazPena( aModel.getFlagSentenzaApplicazPena() );
				 setCodTipoProvvRif( aModel.getCodTipoProvvRif() );
				 setDataProvvRif( aModel.getDataProvvRif() );
				 setCodTipoAutoritaProvvRif( aModel.getCodTipoAutoritaProvvRif() );
				 setAnnoProvvRif( aModel.getAnnoProvvRif() );
				 setNumeroProvvRif( aModel.getNumeroProvvRif() );
				 setCodLuogoProvvRif( aModel.getCodLuogoProvvRif() );
				 setNumSezioneAutoritaProvvRif( aModel.getNumSezioneAutoritaProvvRif() );
				 setCodTipoDecisioneCassazione( aModel.getCodTipoDecisioneCassazione() );
				 setAnnoSentenzaCassazione( aModel.getAnnoSentenzaCassazione() );
				 setNumeroSentenzaCassazione( aModel.getNumeroSentenzaCassazione() );
				 setAnnoRaccoltaGenerale( aModel.getAnnoRaccoltaGenerale() );
				 setNumeroRaccoltaGenerale( aModel.getNumeroRaccoltaGenerale() );
				 setAnnoRegistro35( aModel.getAnnoRegistro35() );
				 setNumRegistro35( aModel.getNumRegistro35() );
				 setNote( aModel.getNote() );
				 setDescrNumCampionePenale( aModel.getDescrNumCampionePenale() );
				 setAnnoRegeGip( aModel.getAnnoRegeGip() );
				 setNumeroRegeGip( aModel.getNumeroRegeGip() );
				 setAnnoRegeDib( aModel.getAnnoRegeDib() );
				 setNumeroRegeDib( aModel.getNumeroRegeDib() );
				 setAnnoRegeCas( aModel.getAnnoRegeCas() );
				 setNumeroRegeCas( aModel.getNumeroRegeCas() );
				 setAnnoRegeCap( aModel.getAnnoRegeCap() );
				 setNumeroRegeCap( aModel.getNumeroRegeCap() );
				 setAnnoRegeCasap( aModel.getAnnoRegeCasap() );
				 setNumeroRegeCasap( aModel.getNumeroRegeCasap() );
				 setNotaDispositivo( aModel.getNotaDispositivo() );
				 setCodTipoRito( aModel.getCodTipoRito() );
				 setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
				 setDataAggiornamento( aModel.getDataAggiornamento() );
				 setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
				 setFlagGiudizioAbbreviato( aModel.getFlagGiudizioAbbreviato() );

         setCondizioneUpdate(aModel.getIdFile());
		}


    public void setCondizione(RegeSentenzaModel aModel)
    {
      String lCondizioni = new String();

      boolean lInserito = false;
      if (lInserito)
        setCondition(lCondizioni);
    }

    public void setCondizioneUpdate(String key)
    {
      setCondition(" ID_FILE = '" + key + "'");
    }

    /* Setta la condizione per la cancellazione
     * @param String
     */
    public void setCondizioneDelete(String aKey)
    {
      String lCondizioni = new String();
      lCondizioni = " ID_FILE = '" + aKey + "'";

      setCondition(lCondizioni);
    }

}
