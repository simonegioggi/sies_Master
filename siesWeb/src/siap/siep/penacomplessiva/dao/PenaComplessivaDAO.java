package siap.siep.penacomplessiva.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
* <p>Title: PenaComplessivaDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella PenaComplessiva</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class PenaComplessivaDAO extends SIAPTableDAO
{
  public PenaComplessivaDAO (Connection con)
  {
    super(con);

    setTable("PENA_COMPLESSIVA");

    setSequenceField("ID_PENA_COMPLESSIVA", "PEN_COM_SEQ");

    setFieldKey("ID_PENA_COMPLESSIVA", BIG_DECIMAL);

    setField("ID_PENA_COMPLESSIVA", BIG_DECIMAL);
    setField("COD_TIPO_PENA_DETENTIVA", STRING);
    setField("NUM_ANNI_RECLUSIONE", BIG_DECIMAL);
    setField("NUM_MESI_RECLUSIONE", BIG_DECIMAL);
    setField("NUM_GIORNI_RECLUSIONE", BIG_DECIMAL);
    setField("IMPORTO_MULTA", BIG_DECIMAL);
    setField("NUM_ANNI_ARRESTO", BIG_DECIMAL);
    setField("NUM_MESI_ARRESTO", BIG_DECIMAL);
    setField("NUM_GIORNI_ARRESTO", BIG_DECIMAL);
    setField("IMPORTO_AMMENDA", BIG_DECIMAL);
    setField("DATA_INIZIO", DATE);
    setField("DATA_FINE", DATE);
    setField("COD_TIPO_RITO", STRING);
    setField("FLAG_PENA_IN_CONTINUAZIONE", STRING);
    setField("COD_OPERATORE_INSERIMENTO", STRING);
    setField("DATA_INSERIMENTO", DATE);
    setField("COD_UFFICIO_INSERIMENTO", STRING);
    setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
    setField("DATA_AGGIORNAMENTO", DATE);
    setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
    setField("NUM_ANNI_CONDONATI", BIG_DECIMAL);
    setField("NUM_MESI_CONDONATI", BIG_DECIMAL);
    setField("NUM_GIORNI_CONDONATI", BIG_DECIMAL);
    setField("IMPORTO_CONDONATO", BIG_DECIMAL);
    setField("FAS_SIE_ID_FASCICOLO_SIEP", BIG_DECIMAL);
    setField("DATA_INIZIO_ISOLAMENTO_DIURNO", DATE);
    setField("DATA_FINE_ISOLAMENTO_DIURNO", DATE);
    setField("NUM_ANNI_ISOLAMENTO_DIURNO", BIG_DECIMAL);
    setField("NUM_MESI_ISOLAMENTO_DIURNO", BIG_DECIMAL);
    setField("NUM_GIORNI_ISOLAMENTO_DIURNO", BIG_DECIMAL);
    setField("DATA_PRESCRIZIONE", DATE);
  }

  //
  // METODI GET()
  //

  public BigDecimal getIdPenaComplessiva() 	        throws DAOException	     { return getBigDecimal("ID_PENA_COMPLESSIVA"); }
  public String     getCodTipoPenaDetentiva() 	        throws DAOException	 { return getString("COD_TIPO_PENA_DETENTIVA"); }
  public BigDecimal getNumAnniReclusione() 	        throws DAOException	     { return getBigDecimal("NUM_ANNI_RECLUSIONE"); }
  public BigDecimal getNumMesiReclusione() 	        throws DAOException	     { return getBigDecimal("NUM_MESI_RECLUSIONE"); }
  public BigDecimal getNumGiorniReclusione() 	        throws DAOException	   { return getBigDecimal("NUM_GIORNI_RECLUSIONE"); }
  public BigDecimal getImportoMulta() 		        throws DAOException	       { return getBigDecimal("IMPORTO_MULTA"); }
  public BigDecimal getNumAnniArresto() 	        throws DAOException	       { return getBigDecimal("NUM_ANNI_ARRESTO"); }
  public BigDecimal getNumMesiArresto() 	        throws DAOException	       { return getBigDecimal("NUM_MESI_ARRESTO"); }
  public BigDecimal getNumGiorniArresto() 	        throws DAOException	     { return getBigDecimal("NUM_GIORNI_ARRESTO"); }
  public BigDecimal getImportoAmmenda() 	        throws DAOException	       { return getBigDecimal("IMPORTO_AMMENDA"); }
  public Date 	    getDataInizio() 		        throws DAOException	         { return getDate("DATA_INIZIO"); }
  public Date 	    getDataFine() 		        throws DAOException	           { return getDate("DATA_FINE"); }
  public String     getCodTipoRito() 		        throws DAOException	         { return getString("COD_TIPO_RITO"); }
  public String     getFlagPenaInContinuazione()        throws DAOException	 { return getString("FLAG_PENA_IN_CONTINUAZIONE"); }
  public String     getCodOperatoreInserimento() 	throws DAOException	       { return getString("COD_OPERATORE_INSERIMENTO"); }
  public Date 	    getDataInserimento() 		throws DAOException	             { return getDate("DATA_INSERIMENTO"); }
  public String     getCodUfficioInserimento() 		throws DAOException	       { return getString("COD_UFFICIO_INSERIMENTO"); }
  public String     getCodOperatoreAggiornamento() 	throws DAOException	     { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
  public Date 	    getDataAggiornamento() 		throws DAOException	           { return getDate("DATA_AGGIORNAMENTO"); }
  public String     getCodUfficioAggiornamento() 	throws DAOException	       { return getString("COD_UFFICIO_AGGIORNAMENTO"); }
  public BigDecimal getNumAnniCondonati() 		throws DAOException	           { return getBigDecimal("NUM_ANNI_CONDONATI"); }
  public BigDecimal getNumMesiCondonati() 		throws DAOException	           { return getBigDecimal("NUM_MESI_CONDONATI"); }
  public BigDecimal getNumGiorniCondonati() 		throws DAOException	         { return getBigDecimal("NUM_GIORNI_CONDONATI"); }
  public BigDecimal getImportoCondonato() 		throws DAOException	           { return getBigDecimal("IMPORTO_CONDONATO"); }
  public BigDecimal getFasSieIdFascicoloSiep() 		throws DAOException	       { return getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"); }
  public Date 			getDataInizioIsolamentoDiurno() 		throws DAOException	 { return getDate("DATA_INIZIO_ISOLAMENTO_DIURNO"); }
  public Date 			getDataFineIsolamentoDiurno() 		throws DAOException	   { return getDate("DATA_FINE_ISOLAMENTO_DIURNO"); }
  public BigDecimal getNumAnniIsolamentoDiurno() 		throws DAOException	     { return getBigDecimal("NUM_ANNI_ISOLAMENTO_DIURNO"); }
  public BigDecimal getNumMesiIsolamentoDiurno() 		throws DAOException	     { return getBigDecimal("NUM_MESI_ISOLAMENTO_DIURNO"); }
  public BigDecimal getNumGiorniIsolamentoDiurno() 		throws DAOException	   { return getBigDecimal("NUM_GIORNI_ISOLAMENTO_DIURNO"); }
  public Date       getDataPrescrizione()           throws DAOException      { return getDate("DATA_PRESCRIZIONE"); }

  //
  // METODI SET()
  //

  public void setIdPenaComplessiva(BigDecimal aValore ) 	      { setBigDecimal("ID_PENA_COMPLESSIVA", aValore); }
  public void setCodTipoPenaDetentiva(String aValore ) 		      { setString("COD_TIPO_PENA_DETENTIVA", aValore); }
  public void setNumAnniReclusione(BigDecimal aValore ) 	      { setBigDecimal("NUM_ANNI_RECLUSIONE", aValore); }
  public void setNumMesiReclusione(BigDecimal aValore ) 	      { setBigDecimal("NUM_MESI_RECLUSIONE", aValore); }
  public void setNumGiorniReclusione(BigDecimal aValore ) 	    { setBigDecimal("NUM_GIORNI_RECLUSIONE", aValore); }
  public void setImportoMulta(BigDecimal aValore ) 		          { setBigDecimal("IMPORTO_MULTA", aValore); }
  public void setNumAnniArresto(BigDecimal aValore ) 		        { setBigDecimal("NUM_ANNI_ARRESTO", aValore); }
  public void setNumMesiArresto(BigDecimal aValore ) 		        { setBigDecimal("NUM_MESI_ARRESTO", aValore); }
  public void setNumGiorniArresto(BigDecimal aValore ) 		      { setBigDecimal("NUM_GIORNI_ARRESTO", aValore); }
  public void setImportoAmmenda(BigDecimal aValore ) 		        { setBigDecimal("IMPORTO_AMMENDA", aValore); }
  public void setDataInizio(Date aValore ) 			                { setDate("DATA_INIZIO", aValore); }
  public void setDataFine(Date aValore ) 			                  { setDate("DATA_FINE", aValore); }
  public void setCodTipoRito(String aValore ) 			            { setString("COD_TIPO_RITO", aValore); }
	public void setFlagPenaInContinuazione(String aValore ) 			{ setString("FLAG_PENA_IN_CONTINUAZIONE", aValore); }
  public void setCodOperatoreInserimento(String aValore ) 	    { setString("COD_OPERATORE_INSERIMENTO", aValore); }
  public void setDataInserimento(Date aValore ) 		            { setDate("DATA_INSERIMENTO", aValore); }
  public void setCodUfficioInserimento(String aValore ) 	      { setString("COD_UFFICIO_INSERIMENTO", aValore); }
  public void setCodOperatoreAggiornamento(String aValore ) 	  { setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
  public void setDataAggiornamento(Date aValore ) 	          	{ setDate("DATA_AGGIORNAMENTO", aValore); }
  public void setCodUfficioAggiornamento(String aValore ) 	    { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }
  public void setNumAnniCondonati(BigDecimal aValore ) 		      { setBigDecimal("NUM_ANNI_CONDONATI", aValore); }
  public void setNumMesiCondonati(BigDecimal aValore ) 		      { setBigDecimal("NUM_MESI_CONDONATI", aValore); }
  public void setNumGiorniCondonati(BigDecimal aValore ) 	      { setBigDecimal("NUM_GIORNI_CONDONATI", aValore); }
  public void setImportoCondonato(BigDecimal aValore ) 		      { setBigDecimal("IMPORTO_CONDONATO", aValore); }
  public void setFasSieIdFascicoloSiep(BigDecimal aValore ) 	  { setBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP", aValore); }
  public void setDataInizioIsolamentoDiurno(Date aValore ) 			{ setDate("DATA_INIZIO_ISOLAMENTO_DIURNO", aValore); }
  public void setDataFineIsolamentoDiurno(Date aValore ) 			  { setDate("DATA_FINE_ISOLAMENTO_DIURNO", aValore); }
  public void setNumAnniIsolamentoDiurno(BigDecimal aValore ) 	{ setBigDecimal("NUM_ANNI_ISOLAMENTO_DIURNO", aValore); }
  public void setNumMesiIsolamentoDiurno(BigDecimal aValore ) 	{ setBigDecimal("NUM_MESI_ISOLAMENTO_DIURNO", aValore); }
  public void setNumGiorniIsolamentoDiurno(BigDecimal aValore ) { setBigDecimal("NUM_GIORNI_ISOLAMENTO_DIURNO", aValore); }
  public void setDataPrescrizione(Date aValore)                 { setDate("DATA_PRESCRIZIONE", aValore); }

  public GenericModel getModel() throws DAOException
  {
    return new PenaComplessivaModel( getIdPenaComplessiva() ,
                                     getCodTipoPenaDetentiva() ,
                                     "",
                                     getNumAnniReclusione() ,
                                     getNumMesiReclusione() ,
                                     getNumGiorniReclusione() ,
                                     getImportoMulta() ,
                                     getNumAnniArresto() ,
                                     getNumMesiArresto() ,
                                     getNumGiorniArresto() ,
                                     getImportoAmmenda() ,
                                     getDataInizio() ,
                                     getDataFine() ,
                                     getCodTipoRito() ,
                                     "",
                                     getFlagPenaInContinuazione() ,
                                     getCodOperatoreInserimento() ,
                                     getDataInserimento() ,
                                     getCodUfficioInserimento() ,
                                     "",
                                     getCodOperatoreAggiornamento() ,
                                     getDataAggiornamento() ,
                                     getCodUfficioAggiornamento() ,
                                     "",
                                     getNumAnniCondonati() ,
                                     getNumMesiCondonati() ,
                                     getNumGiorniCondonati() ,
                                     getImportoCondonato() ,
                                     getFasSieIdFascicoloSiep() ,
                     								 getDataInizioIsolamentoDiurno() ,
                                     getDataFineIsolamentoDiurno() ,
                                     getNumAnniIsolamentoDiurno() ,
                                     getNumMesiIsolamentoDiurno() ,
                                     getNumGiorniIsolamentoDiurno() ,
                                     getDataPrescrizione());
  }

  public void setDAOFromModel(PenaComplessivaModel aModel) throws DAOException
  {
    setIdPenaComplessiva( aModel.getIdPenaComplessiva() );
    setCodTipoPenaDetentiva( aModel.getCodTipoPenaDetentiva() );
    setNumAnniReclusione( aModel.getNumAnniReclusione() );
    setNumMesiReclusione( aModel.getNumMesiReclusione() );
    setNumGiorniReclusione( aModel.getNumGiorniReclusione() );
    setImportoMulta( aModel.getImportoMulta() );
    setNumAnniArresto( aModel.getNumAnniArresto() );
    setNumMesiArresto( aModel.getNumMesiArresto() );
    setNumGiorniArresto( aModel.getNumGiorniArresto() );
    setImportoAmmenda( aModel.getImportoAmmenda() );
    setDataInizio( aModel.getDataInizio() );
    setDataFine( aModel.getDataFine() );
    setCodTipoRito( aModel.getCodTipoRito() );
    setFlagPenaInContinuazione( aModel.getFlagPenaInContinuazione() );
    setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
    setDataInserimento( aModel.getDataInserimento() );
    setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
    setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
    setDataAggiornamento( aModel.getDataAggiornamento() );
    setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
    setNumAnniCondonati( aModel.getNumAnniCondonati() );
    setNumMesiCondonati( aModel.getNumMesiCondonati() );
    setNumGiorniCondonati( aModel.getNumGiorniCondonati() );
    setImportoCondonato( aModel.getImportoCondonato() );
    setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );
    setDataInizioIsolamentoDiurno( aModel.getDataInizioIsolamentoDiurno() );
    setDataFineIsolamentoDiurno( aModel.getDataFineIsolamentoDiurno() );
    setNumAnniIsolamentoDiurno( aModel.getNumAnniIsolamentoDiurno() );
    setNumMesiIsolamentoDiurno( aModel.getNumMesiIsolamentoDiurno() );
    setNumGiorniIsolamentoDiurno( aModel.getNumGiorniIsolamentoDiurno() );
    setDataPrescrizione(aModel.getDataPrescrizione() );
  }

  public void setDAOFromModelForUpdate(PenaComplessivaModel aModel) throws DAOException
  {
    //setIdPenaComplessiva( aModel.getIdPenaComplessiva() );
    setCodTipoPenaDetentiva( aModel.getCodTipoPenaDetentiva() );
    setNumAnniReclusione( aModel.getNumAnniReclusione() );
    setNumMesiReclusione( aModel.getNumMesiReclusione() );
    setNumGiorniReclusione( aModel.getNumGiorniReclusione() );
    setImportoMulta( aModel.getImportoMulta() );
    setNumAnniArresto( aModel.getNumAnniArresto() );
    setNumMesiArresto( aModel.getNumMesiArresto() );
    setNumGiorniArresto( aModel.getNumGiorniArresto() );
    setImportoAmmenda( aModel.getImportoAmmenda() );
    //setDataInizio( aModel.getDataInizio() );
    //setDataFine( aModel.getDataFine() );
    //setCodTipoRito( aModel.getCodTipoRito() );
    // * setFlagPenaInContinuazione( aModel.getFlagPenaInContinuazione() ); *
    setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
    setDataAggiornamento( aModel.getDataAggiornamento() );
    setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
    setNumAnniCondonati( aModel.getNumAnniCondonati() );
    setNumMesiCondonati( aModel.getNumMesiCondonati() );
    setNumGiorniCondonati( aModel.getNumGiorniCondonati() );
    setImportoCondonato( aModel.getImportoCondonato() );
    //setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );
    setDataInizioIsolamentoDiurno( aModel.getDataInizioIsolamentoDiurno() );
    setDataFineIsolamentoDiurno( aModel.getDataFineIsolamentoDiurno() );
    setNumAnniIsolamentoDiurno( aModel.getNumAnniIsolamentoDiurno() );
    setNumMesiIsolamentoDiurno( aModel.getNumMesiIsolamentoDiurno() );
    setNumGiorniIsolamentoDiurno( aModel.getNumGiorniIsolamentoDiurno() );
    setDataPrescrizione( aModel.getDataPrescrizione() );

    setCondizioneUpdate(aModel.getIdPenaComplessiva());
  }

  public void setCondizione(PenaComplessivaModel aModel)
  {
    String lCondizioni = new String();

    boolean lInserito = false;
    if ( lInserito ) setCondition(lCondizioni);
  }

  public void setCondizioneUpdate(BigDecimal key)
  {
   setCondition(" ID_PENA_COMPLESSIVA = " + key );
  }
}
