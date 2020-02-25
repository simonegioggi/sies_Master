package siap.siep.penacumulo.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.siep.penacumulo.model.PenaCumuloModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: PenaCumuloDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella PenaCumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class PenaCumuloDAO extends TableDAO
{
	public PenaCumuloDAO (Connection con)
	{
			 super(con);
			 setTable("PENA_CUMULO");

       setSequenceField("ID_PENA_CUMULO", "PEN_CUM_SEQ");

       setFieldKey("ID_PENA_CUMULO", BIG_DECIMAL);

			 setField("ID_PENA_CUMULO", BIG_DECIMAL);
			 setField("COD_TIPO_PENA_DETENTIVA", STRING);
			 setField("NUM_ANNI_RECLUSIONE", BIG_DECIMAL);
			 setField("NUM_MESI_RECLUSIONE", BIG_DECIMAL);
			 setField("NUM_GIORNI_RECLUSIONE", BIG_DECIMAL);
			 setField("IMPORTO_MULTA", BIG_DECIMAL);
			 setField("NUM_ANNI_ARRESTO", BIG_DECIMAL);
			 setField("NUM_MESI_ARRESTO", BIG_DECIMAL);
			 setField("NUM_GIORNI_ARRESTO", BIG_DECIMAL);
			 setField("IMPORTO_AMMENDA", BIG_DECIMAL);
			 setField("DATA_DECORRENZA_PENA", DATE);
			 setField("MOTIVAZIONI", STRING);
			 setField("NUM_ANNI_RECLUSIONE_SOSP", BIG_DECIMAL);
			 setField("NUM_MESI_RECLUSIONE_SOSP", BIG_DECIMAL);
			 setField("NUM_GIORNI_RECLUSIONE_SOSP", BIG_DECIMAL);
			 setField("NUM_ANNI_ARRESTO_SOSP", BIG_DECIMAL);
			 setField("NUM_MESI_ARRESTO_SOSP", BIG_DECIMAL);
			 setField("NUM_GIORNI_ARRESTO_SOSP", BIG_DECIMAL);
			 setField("ESTREMI_ORDINANZA", STRING);
			 setField("FLAG_ERGASTOLO", STRING);
			 setField("NOTE", STRING);
			 setField("COD_OPERATORE_INSERIMENTO", STRING);
			 setField("DATA_INSERIMENTO", DATE);
			 setField("COD_UFFICIO_INSERIMENTO", STRING);
			 setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
			 setField("DATA_AGGIORNAMENTO", DATE);
			 setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
			 setField("CUM_ID_CUMULO", BIG_DECIMAL);

			 setField("NUM_ANNI_ISOLAMENTO_DIURNO", BIG_DECIMAL);
			 setField("NUM_MESI_ISOLAMENTO_DIURNO", BIG_DECIMAL);
			 setField("NUM_GIORNI_ISOLAMENTO_DIURNO", BIG_DECIMAL);

       setField("MISURA_SICUREZZA", STRING);
       setField("PENA_ACCESSORIA", STRING);
       setField("NUM_GIORNI_LIB_ANTICIPATA", BIG_DECIMAL);
    // 20/05/2014	- Nuova L.A. - decreto 2013/146	-
       setField("NUM_GIORNI_LIB_ANTICIPATA_LA", BIG_DECIMAL);
       setField("NUM_GIORNI_LIB_ANTICIPATA_SPE", BIG_DECIMAL);
       setField("NUM_GIORNI_LIB_ANTICIPATA_INT", BIG_DECIMAL);
       
       setField("NUM_GIORNI_RIDUZIONE_PENA", BIG_DECIMAL);

	}


  //
  // METODI GET()
  //

			public BigDecimal 		 getIdPenaCumulo() 		throws DAOException	 { return getBigDecimal("ID_PENA_CUMULO"); }
			public String 				 getCodTipoPenaDetentiva() 		throws DAOException	 { return getString("COD_TIPO_PENA_DETENTIVA"); }
			public BigDecimal 		 getNumAnniReclusione() 		throws DAOException	 { return getBigDecimal("NUM_ANNI_RECLUSIONE"); }
			public BigDecimal 		 getNumMesiReclusione() 		throws DAOException	 { return getBigDecimal("NUM_MESI_RECLUSIONE"); }
			public BigDecimal 		 getNumGiorniReclusione() 		throws DAOException	 { return getBigDecimal("NUM_GIORNI_RECLUSIONE"); }
			public BigDecimal 		 getImportoMulta() 		throws DAOException	 { return getBigDecimal("IMPORTO_MULTA"); }
			public BigDecimal 		 getNumAnniArresto() 		throws DAOException	 { return getBigDecimal("NUM_ANNI_ARRESTO"); }
			public BigDecimal 		 getNumMesiArresto() 		throws DAOException	 { return getBigDecimal("NUM_MESI_ARRESTO"); }
			public BigDecimal 		 getNumGiorniArresto() 		throws DAOException	 { return getBigDecimal("NUM_GIORNI_ARRESTO"); }
			public BigDecimal 		 getImportoAmmenda() 		throws DAOException	 { return getBigDecimal("IMPORTO_AMMENDA"); }
			public Date 					 getDataDecorrenzaPena() 		throws DAOException	 { return getDate("DATA_DECORRENZA_PENA"); }
			public String 				 getMotivazioni() 		throws DAOException	 { return getString("MOTIVAZIONI"); }
			public BigDecimal 		 getNumAnniReclusioneSosp() 		throws DAOException	 { return getBigDecimal("NUM_ANNI_RECLUSIONE_SOSP"); }
			public BigDecimal 		 getNumMesiReclusioneSosp() 		throws DAOException	 { return getBigDecimal("NUM_MESI_RECLUSIONE_SOSP"); }
			public BigDecimal 		 getNumGiorniReclusioneSosp() 		throws DAOException	 { return getBigDecimal("NUM_GIORNI_RECLUSIONE_SOSP"); }
			public BigDecimal 		 getNumAnniArrestoSosp() 		throws DAOException	 { return getBigDecimal("NUM_ANNI_ARRESTO_SOSP"); }
			public BigDecimal 		 getNumMesiArrestoSosp() 		throws DAOException	 { return getBigDecimal("NUM_MESI_ARRESTO_SOSP"); }
			public BigDecimal 		 getNumGiorniArrestoSosp() 		throws DAOException	 { return getBigDecimal("NUM_GIORNI_ARRESTO_SOSP"); }
			public String 				 getEstremiOrdinanza() 		throws DAOException	 { return getString("ESTREMI_ORDINANZA"); }
			public String 				 getFlagErgastolo() 		throws DAOException	 { return getString("FLAG_ERGASTOLO"); }
			public String 				 getNote() 		throws DAOException	 { return getString("NOTE"); }
			public String 				 getCodOperatoreInserimento() 		throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); }
			public Date 					 getDataInserimento() 		throws DAOException	 { return getDate("DATA_INSERIMENTO"); }
			public String 				 getCodUfficioInserimento() 		throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); }
			public String 				 getCodOperatoreAggiornamento() 		throws DAOException	 { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
			public Date 					 getDataAggiornamento() 		throws DAOException	 { return getDate("DATA_AGGIORNAMENTO"); }
			public String 				 getCodUfficioAggiornamento() 		throws DAOException	 { return getString("COD_UFFICIO_AGGIORNAMENTO"); }
			public BigDecimal 		 getCumIdCumulo() 		throws DAOException	 { return getBigDecimal("CUM_ID_CUMULO"); }

      public BigDecimal	     getNumAnniIsolamentoDiurno()  throws DAOException { return getBigDecimal("NUM_ANNI_ISOLAMENTO_DIURNO"); }
      public BigDecimal	     getNumMesiIsolamentoDiurno()  throws DAOException { return getBigDecimal("NUM_MESI_ISOLAMENTO_DIURNO"); }
      public BigDecimal	     getNumGiorniIsolamentoDiurno() throws DAOException { return getBigDecimal("NUM_GIORNI_ISOLAMENTO_DIURNO"); }

      public String	         getMisuraSicurezza()  throws DAOException { return getString("MISURA_SICUREZZA"); }
      public String	         getPenaAccessoria()  throws DAOException { return getString("PENA_ACCESSORIA"); }
      public BigDecimal	     getNumGiorniLibAnticipata() throws DAOException { return getBigDecimal("NUM_GIORNI_LIB_ANTICIPATA"); }
// 20/05/2014	- Nuova L.A. - decreto 2013/146	-
      public BigDecimal	     getNumGiorniLibAnticipataLA() throws DAOException { return getBigDecimal("NUM_GIORNI_LIB_ANTICIPATA_LA"); }
      public BigDecimal	     getNumGiorniLibAnticipataSPE() throws DAOException { return getBigDecimal("NUM_GIORNI_LIB_ANTICIPATA_SPE"); }
      public BigDecimal	     getNumGiorniLibAnticipataINT() throws DAOException { return getBigDecimal("NUM_GIORNI_LIB_ANTICIPATA_INT"); }

      public BigDecimal      getNumGiorniRiduzionePena() throws DAOException { return getBigDecimal("NUM_GIORNI_RIDUZIONE_PENA"); }

  //
  // METODI SET()
  //

			public void  	 setIdPenaCumulo(BigDecimal aValore ) 			 { setBigDecimal("ID_PENA_CUMULO", aValore); }
			public void  	 setCodTipoPenaDetentiva(String aValore ) 			 { setString("COD_TIPO_PENA_DETENTIVA", aValore); }
			public void  	 setNumAnniReclusione(BigDecimal aValore ) 			 { setBigDecimal("NUM_ANNI_RECLUSIONE", aValore); }
			public void  	 setNumMesiReclusione(BigDecimal aValore ) 			 { setBigDecimal("NUM_MESI_RECLUSIONE", aValore); }
			public void  	 setNumGiorniReclusione(BigDecimal aValore ) 			 { setBigDecimal("NUM_GIORNI_RECLUSIONE", aValore); }
			public void  	 setImportoMulta(BigDecimal aValore ) 			 { setBigDecimal("IMPORTO_MULTA", aValore); }
			public void  	 setNumAnniArresto(BigDecimal aValore ) 			 { setBigDecimal("NUM_ANNI_ARRESTO", aValore); }
			public void  	 setNumMesiArresto(BigDecimal aValore ) 			 { setBigDecimal("NUM_MESI_ARRESTO", aValore); }
			public void  	 setNumGiorniArresto(BigDecimal aValore ) 			 { setBigDecimal("NUM_GIORNI_ARRESTO", aValore); }
			public void  	 setImportoAmmenda(BigDecimal aValore ) 			 { setBigDecimal("IMPORTO_AMMENDA", aValore); }
			public void  	 setDataDecorrenzaPena(Date aValore ) 			 { setDate("DATA_DECORRENZA_PENA", aValore); }
			public void  	 setMotivazioni(String aValore ) 			 { setString("MOTIVAZIONI", aValore); }
			public void  	 setNumAnniReclusioneSosp(BigDecimal aValore ) 			 { setBigDecimal("NUM_ANNI_RECLUSIONE_SOSP", aValore); }
			public void  	 setNumMesiReclusioneSosp(BigDecimal aValore ) 			 { setBigDecimal("NUM_MESI_RECLUSIONE_SOSP", aValore); }
			public void  	 setNumGiorniReclusioneSosp(BigDecimal aValore ) 			 { setBigDecimal("NUM_GIORNI_RECLUSIONE_SOSP", aValore); }
			public void  	 setNumAnniArrestoSosp(BigDecimal aValore ) 			 { setBigDecimal("NUM_ANNI_ARRESTO_SOSP", aValore); }
			public void  	 setNumMesiArrestoSosp(BigDecimal aValore ) 			 { setBigDecimal("NUM_MESI_ARRESTO_SOSP", aValore); }
			public void  	 setNumGiorniArrestoSosp(BigDecimal aValore ) 			 { setBigDecimal("NUM_GIORNI_ARRESTO_SOSP", aValore); }
			public void  	 setEstremiOrdinanza(String aValore ) 			 { setString("ESTREMI_ORDINANZA", aValore); }
			public void  	 setFlagErgastolo(String aValore ) 			 { setString("FLAG_ERGASTOLO", aValore); }
			public void  	 setNote(String aValore ) 			 { setString("NOTE", aValore); }
			public void  	 setCodOperatoreInserimento(String aValore ) 			 { setString("COD_OPERATORE_INSERIMENTO", aValore); }
			public void  	 setDataInserimento(Date aValore ) 			 { setDate("DATA_INSERIMENTO", aValore); }
			public void  	 setCodUfficioInserimento(String aValore ) 			 { setString("COD_UFFICIO_INSERIMENTO", aValore); }
			public void  	 setCodOperatoreAggiornamento(String aValore ) 			 { setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
			public void  	 setDataAggiornamento(Date aValore ) 			 { setDate("DATA_AGGIORNAMENTO", aValore); }
			public void  	 setCodUfficioAggiornamento(String aValore ) 			 { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }
			public void  	 setCumIdCumulo(BigDecimal aValore ) 			 { setBigDecimal("CUM_ID_CUMULO", aValore); }

      public void	   setNumAnniIsolamentoDiurno(BigDecimal aValore)   { setBigDecimal("NUM_ANNI_ISOLAMENTO_DIURNO", aValore); }
      public void	   setNumMesiIsolamentoDiurno(BigDecimal aValore)   { setBigDecimal("NUM_MESI_ISOLAMENTO_DIURNO", aValore); }
      public void	   setNumGiorniIsolamentoDiurno(BigDecimal aValore) { setBigDecimal("NUM_GIORNI_ISOLAMENTO_DIURNO", aValore); }
      public void	   setMisuraSicurezza(String aValore) { setString("MISURA_SICUREZZA", aValore); }
      public void	   setPenaAccessoria(String aValore) { setString("PENA_ACCESSORIA", aValore); }
      public void	   setNumGiorniLibAnticipata(BigDecimal aValore) { setBigDecimal("NUM_GIORNI_LIB_ANTICIPATA", aValore); }
// 20/05/2014	- Nuova L.A. - decreto 2013/146	-
      public void	   setNumGiorniLibAnticipataLA(BigDecimal aValore) { setBigDecimal("NUM_GIORNI_LIB_ANTICIPATA_LA", aValore); }
      public void	   setNumGiorniLibAnticipataSPE(BigDecimal aValore) { setBigDecimal("NUM_GIORNI_LIB_ANTICIPATA_SPE", aValore); }
      public void	   setNumGiorniLibAnticipataINT(BigDecimal aValore) { setBigDecimal("NUM_GIORNI_LIB_ANTICIPATA_INT", aValore); }
      
      public void    setNumGiorniRiduzionePena (BigDecimal aValore) { setBigDecimal("NUM_GIORNI_RIDUZIONE_PENA", aValore); }

      
	public GenericModel getModel() throws DAOException
  			 {
 				 return new PenaCumuloModel(
								 getIdPenaCumulo() ,
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
								 getDataDecorrenzaPena() ,
								 getMotivazioni() ,
								 getNumAnniReclusioneSosp() ,
								 getNumMesiReclusioneSosp() ,
								 getNumGiorniReclusioneSosp() ,
								 getNumAnniArrestoSosp() ,
								 getNumMesiArrestoSosp() ,
								 getNumGiorniArrestoSosp() ,
								 getEstremiOrdinanza() ,
								 getFlagErgastolo() ,
								 getNote() ,
								 getCodOperatoreInserimento() ,
								 getDataInserimento() ,
								 getCodUfficioInserimento() ,
								 "",
								 getCodOperatoreAggiornamento() ,
								 getDataAggiornamento() ,
								 getCodUfficioAggiornamento() ,
								 "",
								 getCumIdCumulo(),

                 getMisuraSicurezza(),
                 getPenaAccessoria(),
                 getNumGiorniLibAnticipata(),

                 getNumAnniIsolamentoDiurno(),
                 getNumMesiIsolamentoDiurno(),
                 getNumGiorniIsolamentoDiurno(),
// 20/05/2014	- Nuova L.A. - decreto 2013/146	- 
                 getNumGiorniLibAnticipataLA(),
                 getNumGiorniLibAnticipataSPE(),
                 getNumGiorniLibAnticipataINT(),
                 getNumGiorniRiduzionePena()

								);
		}


	 public void 	 setDAOFromModel(PenaCumuloModel aModel) throws DAOException
  		{
				 setIdPenaCumulo( aModel.getIdPenaCumulo() );
				 setCodTipoPenaDetentiva( aModel.getCodTipoPenaDetentiva() );
				 setNumAnniReclusione( aModel.getNumAnniReclusione() );
				 setNumMesiReclusione( aModel.getNumMesiReclusione() );
				 setNumGiorniReclusione( aModel.getNumGiorniReclusione() );
				 setImportoMulta( aModel.getImportoMulta() );
				 setNumAnniArresto( aModel.getNumAnniArresto() );
				 setNumMesiArresto( aModel.getNumMesiArresto() );
				 setNumGiorniArresto( aModel.getNumGiorniArresto() );
				 setImportoAmmenda( aModel.getImportoAmmenda() );
				 setDataDecorrenzaPena( aModel.getDataDecorrenzaPena() );
				 setMotivazioni( aModel.getMotivazioni() );
				 setNumAnniReclusioneSosp( aModel.getNumAnniReclusioneSosp() );
				 setNumMesiReclusioneSosp( aModel.getNumMesiReclusioneSosp() );
				 setNumGiorniReclusioneSosp( aModel.getNumGiorniReclusioneSosp() );
				 setNumAnniArrestoSosp( aModel.getNumAnniArrestoSosp() );
				 setNumMesiArrestoSosp( aModel.getNumMesiArrestoSosp() );
				 setNumGiorniArrestoSosp( aModel.getNumGiorniArrestoSosp() );
				 setEstremiOrdinanza( aModel.getEstremiOrdinanza() );
				 setFlagErgastolo( aModel.getFlagErgastolo() );
				 setNote( aModel.getNote() );
				 setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
				 setDataInserimento( aModel.getDataInserimento() );
				 setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
				 setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
				 setDataAggiornamento( aModel.getDataAggiornamento() );
				 setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
				 setCumIdCumulo( aModel.getCumIdCumulo() );

         setNumAnniIsolamentoDiurno(aModel.getNumAnniIsolamentoDiurno() );
         setNumMesiIsolamentoDiurno(aModel.getNumMesiIsolamentoDiurno() );
         setNumGiorniIsolamentoDiurno(aModel.getNumGiorniIsolamentoDiurno() );

         setMisuraSicurezza(aModel.getMisuraSicurezza() );
         setPenaAccessoria(aModel.getPenaAccessoria() );
         setNumGiorniLibAnticipata(aModel.getNumGiorniLibAnticipata() );
      // 20/05/2014	- Nuova L.A. - decreto 2013/146	- 
         setNumGiorniLibAnticipataLA(aModel.getNumGiorniLibAnticipataLA() );
         setNumGiorniLibAnticipataSPE(aModel.getNumGiorniLibAnticipataSPE() );
         setNumGiorniLibAnticipataINT(aModel.getNumGiorniLibAnticipataINT() );
         
         setNumGiorniRiduzionePena (aModel.getNumGiorniRiduzionePena() );

		}


	 public void 	 setDAOFromModelForUpdate(PenaCumuloModel aModel) throws DAOException
  		{
				 //setIdPenaCumulo( aModel.getIdPenaCumulo() );
				 setCodTipoPenaDetentiva( aModel.getCodTipoPenaDetentiva() );
				 setNumAnniReclusione( aModel.getNumAnniReclusione() );
				 setNumMesiReclusione( aModel.getNumMesiReclusione() );
				 setNumGiorniReclusione( aModel.getNumGiorniReclusione() );
				 setImportoMulta( aModel.getImportoMulta() );
				 setNumAnniArresto( aModel.getNumAnniArresto() );
				 setNumMesiArresto( aModel.getNumMesiArresto() );
				 setNumGiorniArresto( aModel.getNumGiorniArresto() );
				 setImportoAmmenda( aModel.getImportoAmmenda() );
				 setDataDecorrenzaPena( aModel.getDataDecorrenzaPena() );
				 setMotivazioni( aModel.getMotivazioni() );
				 setNumAnniReclusioneSosp( aModel.getNumAnniReclusioneSosp() );
				 setNumMesiReclusioneSosp( aModel.getNumMesiReclusioneSosp() );
				 setNumGiorniReclusioneSosp( aModel.getNumGiorniReclusioneSosp() );
				 setNumAnniArrestoSosp( aModel.getNumAnniArrestoSosp() );
				 setNumMesiArrestoSosp( aModel.getNumMesiArrestoSosp() );
				 setNumGiorniArrestoSosp( aModel.getNumGiorniArrestoSosp() );
				 setEstremiOrdinanza( aModel.getEstremiOrdinanza() );
				 setFlagErgastolo( aModel.getFlagErgastolo() );
				 setNote( aModel.getNote() );
				 setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
				 setDataAggiornamento( aModel.getDataAggiornamento() );
				 setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
				 setCumIdCumulo( aModel.getCumIdCumulo() );
         setNumAnniIsolamentoDiurno(aModel.getNumAnniIsolamentoDiurno() );
         setNumMesiIsolamentoDiurno(aModel.getNumMesiIsolamentoDiurno() );
         setNumGiorniIsolamentoDiurno(aModel.getNumGiorniIsolamentoDiurno() );
         setMisuraSicurezza(aModel.getMisuraSicurezza() );
         setPenaAccessoria(aModel.getPenaAccessoria() );
         setNumGiorniLibAnticipata(aModel.getNumGiorniLibAnticipata() );
         // 20/05/2014	- Nuova L.A. - decreto 2013/146	- 
         setNumGiorniLibAnticipataLA(aModel.getNumGiorniLibAnticipataLA() );
         setNumGiorniLibAnticipataSPE(aModel.getNumGiorniLibAnticipataSPE() );
         setNumGiorniLibAnticipataINT(aModel.getNumGiorniLibAnticipataINT() );
         
         setNumGiorniRiduzionePena (aModel.getNumGiorniRiduzionePena() );
         // End 
		     setCondizioneUpdate(aModel.getIdPenaCumulo());
		}


	public void setCondizione(PenaCumuloModel aModel)
		 {
		 String lCondizioni = new String();

		 boolean lInserito = false;
 		 if ( lInserito ) setCondition(lCondizioni);
		 }


	public void setCondizioneUpdate(BigDecimal key)
 			 {
	 setCondition(" ID_PENA_CUMULO = " + key );
		 }

}
