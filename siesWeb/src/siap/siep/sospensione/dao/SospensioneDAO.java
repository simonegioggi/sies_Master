package siap.siep.sospensione.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.siep.sospensione.model.SospensioneModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: SospensioneDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella Sospensione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class SospensioneDAO extends TableDAO
{
	public SospensioneDAO (Connection con)
	{
    super(con);
    setTable("SOSPENSIONE");

    setSequenceField("ID_SOSPENSIONE", "SOS_SEQ");

    setFieldKey("ID_SOSPENSIONE", BIG_DECIMAL);

    setField("ID_SOSPENSIONE", BIG_DECIMAL);
    setField("DATA_INIZIO", DATE);
    setField("DATA_FINE", DATE);
    setField("NUM_ANNI_RINVIO", BIG_DECIMAL);
    setField("NUM_MESI_RINVIO", BIG_DECIMAL);
    setField("NUM_GIORNI_RINVIO", BIG_DECIMAL);
    setField("COD_OPERATORE_INSERIMENTO", STRING);
    setField("DATA_INSERIMENTO", DATE);
    setField("COD_UFFICIO_INSERIMENTO", STRING);
    setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
    setField("DATA_AGGIORNAMENTO", DATE);
    setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
    setField("PEN_RES_ID_PENA_RESIDUA", BIG_DECIMAL);
    setField("NUM_ANNI_PENA_ESPIATA", BIG_DECIMAL);
    setField("NUM_MESI_PENA_ESPIATA", BIG_DECIMAL);
    setField("NUM_GIORNI_PENA_ESPIATA", BIG_DECIMAL);
    setField("NUM_ANNI_PENA_RESIDUA_RECLUS", BIG_DECIMAL);
    setField("NUM_MESI_PENA_RESIDUA_RECLUS", BIG_DECIMAL);
    setField("NUM_GIORNI_PENA_RESIDUA_RECLUS", BIG_DECIMAL);
    setField("NUM_ANNI_PENA_RESIDUA_ARRES", BIG_DECIMAL);
    setField("NUM_MESI_PENA_RESIDUA_ARRES", BIG_DECIMAL);
    setField("NUM_GIORNI_PENA_RESIDUA_ARRES", BIG_DECIMAL);
    setField("NUM_ANNI_INTERRUZIONE", BIG_DECIMAL);
    setField("NUM_MESI_INTERRUZIONE", BIG_DECIMAL);
    setField("NUM_GIORNI_INTERRUZIONE", BIG_DECIMAL);
    setField("MULTA_ESPIATA", BIG_DECIMAL);
    setField("AMMENDA_ESPIATA", BIG_DECIMAL);
    setField("MULTA_RESIDUA", BIG_DECIMAL);
    setField("AMMENDA_RESIDUA", BIG_DECIMAL);
    setField("FAS_SIE_ID_FASCICOLO_SIEP", BIG_DECIMAL);
    setField("FLAG_INTERRUZIONE", STRING);
    setField("NUM_GIORNI_LIBANTICIPATA", BIG_DECIMAL);
	}

  //
  // METODI GET()
  //

  public BigDecimal getIdSospensione() 		throws DAOException	           { return getBigDecimal("ID_SOSPENSIONE"); }
  public Date 			getDataInizio() 		throws DAOException	             { return getDate("DATA_INIZIO"); }
  public Date 			getDataFine() 		throws DAOException	               { return getDate("DATA_FINE"); }
  public BigDecimal getNumAnniRinvio() 		throws DAOException	           { return getBigDecimal("NUM_ANNI_RINVIO"); }
  public BigDecimal getNumMesiRinvio() 		throws DAOException	           { return getBigDecimal("NUM_MESI_RINVIO"); }
  public BigDecimal getNumGiorniRinvio() 		throws DAOException	         { return getBigDecimal("NUM_GIORNI_RINVIO"); }
  public String 		getCodOperatoreInserimento() 		throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); }
  public Date 			getDataInserimento() 		throws DAOException	         { return getDate("DATA_INSERIMENTO"); }
  public String 		getCodUfficioInserimento() 		throws DAOException	   { return getString("COD_UFFICIO_INSERIMENTO"); }
  public String 		getCodOperatoreAggiornamento() 		throws DAOException	 { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
  public Date 			getDataAggiornamento() 		throws DAOException	       { return getDate("DATA_AGGIORNAMENTO"); }
  public String 		getCodUfficioAggiornamento() 		throws DAOException	 { return getString("COD_UFFICIO_AGGIORNAMENTO"); }
  public BigDecimal getPenResIdPenaResidua() 		throws DAOException	     { return getBigDecimal("PEN_RES_ID_PENA_RESIDUA"); }
  public BigDecimal getNumAnniPenaEspiata() 		throws DAOException	     { return getBigDecimal("NUM_ANNI_PENA_ESPIATA"); }
  public BigDecimal getNumMesiPenaEspiata() 		throws DAOException	     { return getBigDecimal("NUM_MESI_PENA_ESPIATA"); }
  public BigDecimal getNumGiorniPenaEspiata() 		throws DAOException	   { return getBigDecimal("NUM_GIORNI_PENA_ESPIATA"); }
  public BigDecimal getNumAnniPenaResiduaReclus() 		throws DAOException	 { return getBigDecimal("NUM_ANNI_PENA_RESIDUA_RECLUS"); }
  public BigDecimal getNumMesiPenaResiduaReclus() 		throws DAOException	 { return getBigDecimal("NUM_MESI_PENA_RESIDUA_RECLUS"); }
  public BigDecimal getNumGiorniPenaResiduaReclus() 		throws DAOException	 { return getBigDecimal("NUM_GIORNI_PENA_RESIDUA_RECLUS"); }
  public BigDecimal getNumAnniPenaResiduaArres() 		throws DAOException	 { return getBigDecimal("NUM_ANNI_PENA_RESIDUA_ARRES"); }
  public BigDecimal getNumMesiPenaResiduaArres() 		throws DAOException	 { return getBigDecimal("NUM_MESI_PENA_RESIDUA_ARRES"); }
  public BigDecimal getNumGiorniPenaResiduaArres() 		throws DAOException	 { return getBigDecimal("NUM_GIORNI_PENA_RESIDUA_ARRES"); }
  public BigDecimal getNumAnniInterruzione() 		throws DAOException	     { return getBigDecimal("NUM_ANNI_INTERRUZIONE"); }
  public BigDecimal getNumMesiInterruzione() 		throws DAOException	     { return getBigDecimal("NUM_MESI_INTERRUZIONE"); }
  public BigDecimal getNumGiorniInterruzione() 		throws DAOException	   { return getBigDecimal("NUM_GIORNI_INTERRUZIONE"); }
  public BigDecimal getMultaEspiata() 		throws DAOException	           { return getBigDecimal("MULTA_ESPIATA"); }
  public BigDecimal getAmmendaEspiata() 		throws DAOException	         { return getBigDecimal("AMMENDA_ESPIATA"); }
  public BigDecimal getMultaResidua() 		throws DAOException	           { return getBigDecimal("MULTA_RESIDUA"); }
  public BigDecimal getAmmendaResidua() 		throws DAOException	         { return getBigDecimal("AMMENDA_RESIDUA"); }
  public BigDecimal getFasSieIdFascicoloSiep() 		throws DAOException	   { return getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"); }
  public String 		getFlagInterruzione() 		throws DAOException	       { return getString("FLAG_INTERRUZIONE"); }
  public BigDecimal getNumGiorniLibanticipata() 		throws DAOException	 { return getBigDecimal("NUM_GIORNI_LIBANTICIPATA"); }

  //
  // METODI SET()
  //

  public void setIdSospensione(BigDecimal aValore ) 			        { setBigDecimal("ID_SOSPENSIONE", aValore); }
  public void setDataInizio(Date aValore ) 			                  { setDate("DATA_INIZIO", aValore); }
  public void setDataFine(Date aValore ) 			                    { setDate("DATA_FINE", aValore); }
  public void setNumAnniRinvio(BigDecimal aValore ) 			        { setBigDecimal("NUM_ANNI_RINVIO", aValore); }
  public void setNumMesiRinvio(BigDecimal aValore ) 			        { setBigDecimal("NUM_MESI_RINVIO", aValore); }
  public void setNumGiorniRinvio(BigDecimal aValore ) 			      { setBigDecimal("NUM_GIORNI_RINVIO", aValore); }
  public void setCodOperatoreInserimento(String aValore ) 			  { setString("COD_OPERATORE_INSERIMENTO", aValore); }
  public void setDataInserimento(Date aValore ) 			            { setDate("DATA_INSERIMENTO", aValore); }
  public void setCodUfficioInserimento(String aValore ) 			    { setString("COD_UFFICIO_INSERIMENTO", aValore); }
  public void setCodOperatoreAggiornamento(String aValore ) 			{ setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
  public void setDataAggiornamento(Date aValore ) 			          { setDate("DATA_AGGIORNAMENTO", aValore); }
  public void setCodUfficioAggiornamento(String aValore ) 			  { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }
  public void setPenResIdPenaResidua(BigDecimal aValore ) 			  { setBigDecimal("PEN_RES_ID_PENA_RESIDUA", aValore); }
  public void setNumAnniPenaEspiata(BigDecimal aValore ) 			    { setBigDecimal("NUM_ANNI_PENA_ESPIATA", aValore); }
  public void setNumMesiPenaEspiata(BigDecimal aValore ) 			    { setBigDecimal("NUM_MESI_PENA_ESPIATA", aValore); }
  public void setNumGiorniPenaEspiata(BigDecimal aValore ) 			  { setBigDecimal("NUM_GIORNI_PENA_ESPIATA", aValore); }
  public void setNumAnniPenaResiduaReclus(BigDecimal aValore ) 		{ setBigDecimal("NUM_ANNI_PENA_RESIDUA_RECLUS", aValore); }
  public void setNumMesiPenaResiduaReclus(BigDecimal aValore ) 		{ setBigDecimal("NUM_MESI_PENA_RESIDUA_RECLUS", aValore); }
  public void setNumGiorniPenaResiduaReclus(BigDecimal aValore ) 	{ setBigDecimal("NUM_GIORNI_PENA_RESIDUA_RECLUS", aValore); }
  public void setNumAnniPenaResiduaArres(BigDecimal aValore ) 		{ setBigDecimal("NUM_ANNI_PENA_RESIDUA_ARRES", aValore); }
  public void setNumMesiPenaResiduaArres(BigDecimal aValore ) 		{ setBigDecimal("NUM_MESI_PENA_RESIDUA_ARRES", aValore); }
  public void setNumGiorniPenaResiduaArres(BigDecimal aValore ) 	{ setBigDecimal("NUM_GIORNI_PENA_RESIDUA_ARRES", aValore); }
  public void setNumAnniInterruzione(BigDecimal aValore ) 			  { setBigDecimal("NUM_ANNI_INTERRUZIONE", aValore); }
  public void setNumMesiInterruzione(BigDecimal aValore ) 			  { setBigDecimal("NUM_MESI_INTERRUZIONE", aValore); }
  public void setNumGiorniInterruzione(BigDecimal aValore ) 			{ setBigDecimal("NUM_GIORNI_INTERRUZIONE", aValore); }
  public void setMultaEspiata(BigDecimal aValore ) 			          { setBigDecimal("MULTA_ESPIATA", aValore); }
  public void setAmmendaEspiata(BigDecimal aValore ) 			        { setBigDecimal("AMMENDA_ESPIATA", aValore); }
  public void setMultaResidua(BigDecimal aValore ) 			          { setBigDecimal("MULTA_RESIDUA", aValore); }
  public void setAmmendaResidua(BigDecimal aValore ) 			        { setBigDecimal("AMMENDA_RESIDUA", aValore); }
  public void setFasSieIdFascicoloSiep(BigDecimal aValore ) 			{ setBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP", aValore); }
  public void setFlagInterruzione(String aValore ) 			          { setString("FLAG_INTERRUZIONE", aValore); }
  public void setNumGiorniLibanticipata(BigDecimal aValore ) 			{ setBigDecimal("NUM_GIORNI_LIBANTICIPATA", aValore); }

	public GenericModel getModel() throws DAOException
  {
    return new SospensioneModel(
                                 getIdSospensione() ,
                                 getDataInizio() ,
                                 getDataFine() ,
                                 getNumAnniRinvio() ,
                                 getNumMesiRinvio() ,
                                 getNumGiorniRinvio() ,
                                 getCodOperatoreInserimento() ,
                                 getDataInserimento() ,
                                 getCodUfficioInserimento() ,
                                 "",
                                 getCodOperatoreAggiornamento() ,
                                 getDataAggiornamento() ,
                                 getCodUfficioAggiornamento() ,
                                 "",
                                 getPenResIdPenaResidua() ,
                                 getNumAnniPenaEspiata() ,
                                 getNumMesiPenaEspiata() ,
                                 getNumGiorniPenaEspiata() ,
                                 getNumAnniPenaResiduaReclus() ,
                                 getNumMesiPenaResiduaReclus() ,
                                 getNumGiorniPenaResiduaReclus() ,
                                 getNumAnniPenaResiduaArres() ,
                                 getNumMesiPenaResiduaArres() ,
                                 getNumGiorniPenaResiduaArres(),
                                 getNumAnniInterruzione() ,
                                 getNumMesiInterruzione() ,
                                 getNumGiorniInterruzione() ,
                                 getMultaEspiata() ,
                                 getAmmendaEspiata() ,
                                 getMultaResidua() ,
                                 getAmmendaResidua(),
                                 getFasSieIdFascicoloSiep() ,
                                 getFlagInterruzione(),
                                 getNumGiorniLibanticipata()
                                );
  }

  public void setDAOFromModel(SospensioneModel aModel) throws DAOException
  {
    setIdSospensione( aModel.getIdSospensione() );
    setDataInizio( aModel.getDataInizio() );
    setDataFine( aModel.getDataFine() );
    setNumAnniRinvio( aModel.getNumAnniRinvio() );
    setNumMesiRinvio( aModel.getNumMesiRinvio() );
    setNumGiorniRinvio( aModel.getNumGiorniRinvio() );
    setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
    setDataInserimento( aModel.getDataInserimento() );
    setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
    setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
    setDataAggiornamento( aModel.getDataAggiornamento() );
    setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
    setPenResIdPenaResidua( aModel.getPenResIdPenaResidua() );
    setNumAnniPenaEspiata( aModel.getNumAnniPenaEspiata() );
    setNumMesiPenaEspiata( aModel.getNumMesiPenaEspiata() );
    setNumGiorniPenaEspiata( aModel.getNumGiorniPenaEspiata() );
    setNumAnniPenaResiduaReclus( aModel.getNumAnniPenaResiduaReclus() );
    setNumMesiPenaResiduaReclus( aModel.getNumMesiPenaResiduaReclus() );
    setNumGiorniPenaResiduaReclus( aModel.getNumGiorniPenaResiduaReclus() );
    setNumAnniPenaResiduaArres( aModel.getNumAnniPenaResiduaArres() );
    setNumMesiPenaResiduaArres( aModel.getNumMesiPenaResiduaArres() );
    setNumGiorniPenaResiduaArres( aModel.getNumGiorniPenaResiduaArres() );
    setNumAnniInterruzione( aModel.getNumAnniInterruzione() );
    setNumMesiInterruzione( aModel.getNumMesiInterruzione() );
    setNumGiorniInterruzione( aModel.getNumGiorniInterruzione() );
    setMultaEspiata( aModel.getMultaEspiata() );
    setAmmendaEspiata( aModel.getAmmendaEspiata() );
    setMultaResidua( aModel.getMultaResidua() );
    setAmmendaResidua( aModel.getAmmendaResidua() );
    setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );
    setFlagInterruzione( aModel.getFlagInterruzione() );
    setNumGiorniLibanticipata( aModel.getNumGiorniLibanticipata() );
  }

  public void setDAOFromModelForUpdate(SospensioneModel aModel) throws DAOException
  {
    //setIdSospensione( aModel.getIdSospensione() );
    setDataInizio( aModel.getDataInizio() );
    setDataFine( aModel.getDataFine() );
    setNumAnniRinvio( aModel.getNumAnniRinvio() );
    setNumMesiRinvio( aModel.getNumMesiRinvio() );
    setNumGiorniRinvio( aModel.getNumGiorniRinvio() );
    setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
    setDataAggiornamento( aModel.getDataAggiornamento() );
    setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
    setPenResIdPenaResidua( aModel.getPenResIdPenaResidua() );
    setNumAnniPenaEspiata( aModel.getNumAnniPenaEspiata() );
    setNumMesiPenaEspiata( aModel.getNumMesiPenaEspiata() );
    setNumGiorniPenaEspiata( aModel.getNumGiorniPenaEspiata() );
    setNumAnniPenaResiduaReclus( aModel.getNumAnniPenaResiduaReclus() );
    setNumMesiPenaResiduaReclus( aModel.getNumMesiPenaResiduaReclus() );
    setNumGiorniPenaResiduaReclus( aModel.getNumGiorniPenaResiduaReclus() );
    setNumAnniPenaResiduaArres( aModel.getNumAnniPenaResiduaArres() );
    setNumMesiPenaResiduaArres( aModel.getNumMesiPenaResiduaArres() );
    setNumGiorniPenaResiduaArres( aModel.getNumGiorniPenaResiduaArres() );
    setNumAnniInterruzione( aModel.getNumAnniInterruzione() );
    setNumMesiInterruzione( aModel.getNumMesiInterruzione() );
    setNumGiorniInterruzione( aModel.getNumGiorniInterruzione() );
    setMultaEspiata( aModel.getMultaEspiata() );
    setAmmendaEspiata( aModel.getAmmendaEspiata() );
    setMultaResidua( aModel.getMultaResidua() );
    setAmmendaResidua( aModel.getAmmendaResidua() );
    setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );
    setFlagInterruzione( aModel.getFlagInterruzione() );
    setNumGiorniLibanticipata( aModel.getNumGiorniLibanticipata() );

    setCondizioneUpdate(aModel.getIdSospensione());
  }

	public void setCondizione(SospensioneModel aModel)
  {
		 String lCondizioni = new String();

		 boolean lInserito = false;
 		 if ( lInserito ) setCondition(lCondizioni);
  }

	public void setCondizioneUpdate(BigDecimal key)
  {
    setCondition(" ID_SOSPENSIONE = " + key );
  }

  public void setCondizioneIdPenaResidua(BigDecimal aIdPenaResidua)
  {
    setCondition(" PEN_RES_ID_PENA_RESIDUA = " + aIdPenaResidua );
  }

  public void setCondizioneIdFascicolo(BigDecimal aIdFascicoloSiep)
  {
    setCondition(" FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicoloSiep );
  }
}
