package siap.siep.riepilogoprovvedimento.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.siep.riepilogoprovvedimento.model.RiepilogoProvvedimentoModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: RiepilogoProvvedimentoDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella RiepilogoProvvedimento</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class RiepilogoProvvedimentoDAO extends TableDAO
{
	public RiepilogoProvvedimentoDAO (Connection con)
	{
			 super(con);
			 setTable("RIEPILOGO_PROVVEDIMENTO");

			 //Settare la Sequence e i campi chiave
       setSequenceField("ID_RIEPILOGO_PROVVEDIMENTO", "RIE_PRO_SEQ");
			 setField("ID_RIEPILOGO_PROVVEDIMENTO", BIG_DECIMAL);
			 setField("NUM_RES", BIG_DECIMAL);
			 setField("NUM_PROGRESSIVO_RES", BIG_DECIMAL);
			 setField("NUM_PROTOCOLLO_RES", BIG_DECIMAL);
			 setField("FLAG_ERGASTOLO", STRING);
			 setField("NUM_ANNI_RECLUSIONE", BIG_DECIMAL);
			 setField("NUM_MESI_RECLUSIONE", BIG_DECIMAL);
			 setField("NUM_GIORNI_RECLUSIONE", BIG_DECIMAL);
			 setField("IMPORTO_MULTA", BIG_DECIMAL);
			 setField("NUM_ANNI_ARRESTO", BIG_DECIMAL);
			 setField("NUM_MESI_ARRESTO", BIG_DECIMAL);
			 setField("NUM_GIORNI_ARRESTO", BIG_DECIMAL);
			 setField("IMPORTO_AMMENDA", BIG_DECIMAL);
			 setField("NUM_ANNI_PRESOFFERTO", BIG_DECIMAL);
			 setField("NUM_MESI_PRESOFFERTO", BIG_DECIMAL);
			 setField("NUM_GIORNI_PRESOFFERTO", BIG_DECIMAL);
			 setField("NUM_ANNI_INTERRUZIONE", BIG_DECIMAL);
			 setField("NUM_MESI_INTERRUZIONE", BIG_DECIMAL);
			 setField("NUM_GIORNI_INTERRUZIONE", BIG_DECIMAL);
			 setField("NUM_GIORNI_LIB_ANTICIPATA", BIG_DECIMAL);
			 setField("NUM_ANNI_RECLUSIONE_BENEFICI", BIG_DECIMAL);
			 setField("NUM_MESI_RECLUSIONE_BENEFICI", BIG_DECIMAL);
			 setField("NUM_GIORNI_RECLUSIONE_BENEFICI", BIG_DECIMAL);
			 setField("IMPORTO_MULTA_BENEFICI", BIG_DECIMAL);
			 setField("NUM_ANNI_ARRESTO_BENEFICI", BIG_DECIMAL);
			 setField("NUM_MESI_ARRESTO_BENEFICI", BIG_DECIMAL);
			 setField("NUM_GIORNI_ARRESTO_BENEFICI", BIG_DECIMAL);
			 setField("IMPORTO_AMMENDA_BENEFICI", BIG_DECIMAL);
			 setField("NUM_ANNI_AUMENTI_PENA_RECLUS", BIG_DECIMAL);
			 setField("NUM_MESI_AUMENTI_PENA_RECLUS", BIG_DECIMAL);
			 setField("NUM_GIORNI_AUMENTI_PENA_RECLUS", BIG_DECIMAL);
			 setField("IMPORTO_MULTA_AUMENTI_PENA", BIG_DECIMAL);
			 setField("NUM_ANNI_AUMENTI_PENA_ARRES", BIG_DECIMAL);
			 setField("NUM_MESI_AUMENTI_PENA_ARRES", BIG_DECIMAL);
			 setField("NUM_GIORNI_AUMENTI_PENA_ARRES", BIG_DECIMAL);
			 setField("IMPORTO_AMMENDA_AUMENTI_PENA", BIG_DECIMAL);
			 setField("DIES_A_QUO", STRING);
			 setField("DATA_INIZIO_PENA", DATE);
			 setField("DATA_FINE_PENA", DATE);
			 setField("DATA_FINE_RECLUSIONE", DATE);
			 setField("DATA_FINE_PRECEDENTE", DATE);
			 setField("DATA_FINE_DET_DOMICILIARE", DATE);
			 setField("NUM_ANNI_PENA_RESIDUA_RECLUS", BIG_DECIMAL);
			 setField("NUM_MESI_PENA_RESIDUA_RECLUS", BIG_DECIMAL);
			 setField("NUM_GIORNI_PENA_RESIDUA_RECLUS", BIG_DECIMAL);
			 setField("IMPORTO_MULTA_RESIDUA", BIG_DECIMAL);
			 setField("NUM_ANNI_PENA_RESIDUA_ARRES", BIG_DECIMAL);
			 setField("NUM_MESI_PENA_RESIDUA_ARRES", BIG_DECIMAL);
			 setField("NUM_GIORNI_PENA_RESIDUA_ARRES", BIG_DECIMAL);
			 setField("IMPORTO_AMMENDA_RESIDUA", BIG_DECIMAL);
			 setField("NUM_ANNI_FUNGIBILITA", BIG_DECIMAL);
			 setField("NUM_MESI_FUNGIBILITA", BIG_DECIMAL);
			 setField("NUM_GIORNI_FUNGIBILITA", BIG_DECIMAL);
			 setField("COD_TIPO_PROVVEDIMENTO", BIG_DECIMAL);
			 setField("COD_OPERATORE_INSERIMENTO", STRING);
			 setField("DATA_INSERIMENTO", DATE);
			 setField("COD_UFFICIO_INSERIMENTO", STRING);
			 setField("EVE_ID_EVENTO", BIG_DECIMAL);
			 setField("FAS_SIE_ID_FASCICOLO_SIEP", BIG_DECIMAL);
	}


  //
  // METODI GET()
  //

			public BigDecimal 		 getIdRiepilogoProvvedimento() 		throws DAOException	 { return getBigDecimal("ID_RIEPILOGO_PROVVEDIMENTO"); }
			public BigDecimal 		 getNumRes() 		throws DAOException	 { return getBigDecimal("NUM_RES"); }
			public BigDecimal 		 getNumProgressivoRes() 		throws DAOException	 { return getBigDecimal("NUM_PROGRESSIVO_RES"); }
			public BigDecimal 		 getNumProtocolloRes() 		throws DAOException	 { return getBigDecimal("NUM_PROTOCOLLO_RES"); }
			public String 				 getFlagErgastolo() 		throws DAOException	 { return getString("FLAG_ERGASTOLO"); }
			public BigDecimal 		 getNumAnniReclusione() 		throws DAOException	 { return getBigDecimal("NUM_ANNI_RECLUSIONE"); }
			public BigDecimal 		 getNumMesiReclusione() 		throws DAOException	 { return getBigDecimal("NUM_MESI_RECLUSIONE"); }
			public BigDecimal 		 getNumGiorniReclusione() 		throws DAOException	 { return getBigDecimal("NUM_GIORNI_RECLUSIONE"); }
			public BigDecimal 		 getImportoMulta() 		throws DAOException	 { return getBigDecimal("IMPORTO_MULTA"); }
			public BigDecimal 		 getNumAnniArresto() 		throws DAOException	 { return getBigDecimal("NUM_ANNI_ARRESTO"); }
			public BigDecimal 		 getNumMesiArresto() 		throws DAOException	 { return getBigDecimal("NUM_MESI_ARRESTO"); }
			public BigDecimal 		 getNumGiorniArresto() 		throws DAOException	 { return getBigDecimal("NUM_GIORNI_ARRESTO"); }
			public BigDecimal 		 getImportoAmmenda() 		throws DAOException	 { return getBigDecimal("IMPORTO_AMMENDA"); }
			public BigDecimal 		 getNumAnniPresofferto() 		throws DAOException	 { return getBigDecimal("NUM_ANNI_PRESOFFERTO"); }
			public BigDecimal 		 getNumMesiPresofferto() 		throws DAOException	 { return getBigDecimal("NUM_MESI_PRESOFFERTO"); }
			public BigDecimal 		 getNumGiorniPresofferto() 		throws DAOException	 { return getBigDecimal("NUM_GIORNI_PRESOFFERTO"); }
			public BigDecimal 		 getNumAnniInterruzione() 		throws DAOException	 { return getBigDecimal("NUM_ANNI_INTERRUZIONE"); }
			public BigDecimal 		 getNumMesiInterruzione() 		throws DAOException	 { return getBigDecimal("NUM_MESI_INTERRUZIONE"); }
			public BigDecimal 		 getNumGiorniInterruzione() 		throws DAOException	 { return getBigDecimal("NUM_GIORNI_INTERRUZIONE"); }
			public BigDecimal 		 getNumGiorniLibAnticipata() 		throws DAOException	 { return getBigDecimal("NUM_GIORNI_LIB_ANTICIPATA"); }
			public BigDecimal 		 getNumAnniReclusioneBenefici() 		throws DAOException	 { return getBigDecimal("NUM_ANNI_RECLUSIONE_BENEFICI"); }
			public BigDecimal 		 getNumMesiReclusioneBenefici() 		throws DAOException	 { return getBigDecimal("NUM_MESI_RECLUSIONE_BENEFICI"); }
			public BigDecimal 		 getNumGiorniReclusioneBenefici() 		throws DAOException	 { return getBigDecimal("NUM_GIORNI_RECLUSIONE_BENEFICI"); }
			public BigDecimal 		 getImportoMultaBenefici() 		throws DAOException	 { return getBigDecimal("IMPORTO_MULTA_BENEFICI"); }
			public BigDecimal 		 getNumAnniArrestoBenefici() 		throws DAOException	 { return getBigDecimal("NUM_ANNI_ARRESTO_BENEFICI"); }
			public BigDecimal 		 getNumMesiArrestoBenefici() 		throws DAOException	 { return getBigDecimal("NUM_MESI_ARRESTO_BENEFICI"); }
			public BigDecimal 		 getNumGiorniArrestoBenefici() 		throws DAOException	 { return getBigDecimal("NUM_GIORNI_ARRESTO_BENEFICI"); }
			public BigDecimal 		 getImportoAmmendaBenefici() 		throws DAOException	 { return getBigDecimal("IMPORTO_AMMENDA_BENEFICI"); }
			public BigDecimal 		 getNumAnniAumentiPenaReclus() 		throws DAOException	 { return getBigDecimal("NUM_ANNI_AUMENTI_PENA_RECLUS"); }
			public BigDecimal 		 getNumMesiAumentiPenaReclus() 		throws DAOException	 { return getBigDecimal("NUM_MESI_AUMENTI_PENA_RECLUS"); }
			public BigDecimal 		 getNumGiorniAumentiPenaReclus() 		throws DAOException	 { return getBigDecimal("NUM_GIORNI_AUMENTI_PENA_RECLUS"); }
			public BigDecimal 		 getImportoMultaAumentiPena() 		throws DAOException	 { return getBigDecimal("IMPORTO_MULTA_AUMENTI_PENA"); }
			public BigDecimal 		 getNumAnniAumentiPenaArres() 		throws DAOException	 { return getBigDecimal("NUM_ANNI_AUMENTI_PENA_ARRES"); }
			public BigDecimal 		 getNumMesiAumentiPenaArres() 		throws DAOException	 { return getBigDecimal("NUM_MESI_AUMENTI_PENA_ARRES"); }
			public BigDecimal 		 getNumGiorniAumentiPenaArres() 		throws DAOException	 { return getBigDecimal("NUM_GIORNI_AUMENTI_PENA_ARRES"); }
			public BigDecimal 		 getImportoAmmendaAumentiPena() 		throws DAOException	 { return getBigDecimal("IMPORTO_AMMENDA_AUMENTI_PENA"); }
			public String 				 getDiesAQuo() 		throws DAOException	 { return getString("DIES_A_QUO"); }
			public Date 					 getDataInizioPena() 		throws DAOException	 { return getDate("DATA_INIZIO_PENA"); }
			public Date 					 getDataFinePena() 		throws DAOException	 { return getDate("DATA_FINE_PENA"); }
			public Date 					 getDataFineReclusione() 		throws DAOException	 { return getDate("DATA_FINE_RECLUSIONE"); }
			public Date 					 getDataFinePrecedente() 		throws DAOException	 { return getDate("DATA_FINE_PRECEDENTE"); }
			public Date 					 getDataFineDetDomiciliare() 		throws DAOException	 { return getDate("DATA_FINE_DET_DOMICILIARE"); }
			public BigDecimal 		 getNumAnniPenaResiduaReclus() 		throws DAOException	 { return getBigDecimal("NUM_ANNI_PENA_RESIDUA_RECLUS"); }
			public BigDecimal 		 getNumMesiPenaResiduaReclus() 		throws DAOException	 { return getBigDecimal("NUM_MESI_PENA_RESIDUA_RECLUS"); }
			public BigDecimal 		 getNumGiorniPenaResiduaReclus() 		throws DAOException	 { return getBigDecimal("NUM_GIORNI_PENA_RESIDUA_RECLUS"); }
			public BigDecimal 		 getImportoMultaResidua() 		throws DAOException	 { return getBigDecimal("IMPORTO_MULTA_RESIDUA"); }
			public BigDecimal 		 getNumAnniPenaResiduaArres() 		throws DAOException	 { return getBigDecimal("NUM_ANNI_PENA_RESIDUA_ARRES"); }
			public BigDecimal 		 getNumMesiPenaResiduaArres() 		throws DAOException	 { return getBigDecimal("NUM_MESI_PENA_RESIDUA_ARRES"); }
			public BigDecimal 		 getNumGiorniPenaResiduaArres() 		throws DAOException	 { return getBigDecimal("NUM_GIORNI_PENA_RESIDUA_ARRES"); }
			public BigDecimal 		 getImportoAmmendaResidua() 		throws DAOException	 { return getBigDecimal("IMPORTO_AMMENDA_RESIDUA"); }
			public BigDecimal 		 getNumAnniFungibilita() 		throws DAOException	 { return getBigDecimal("NUM_ANNI_FUNGIBILITA"); }
			public BigDecimal 		 getNumMesiFungibilita() 		throws DAOException	 { return getBigDecimal("NUM_MESI_FUNGIBILITA"); }
			public BigDecimal 		 getNumGiorniFungibilita() 		throws DAOException	 { return getBigDecimal("NUM_GIORNI_FUNGIBILITA"); }
			public BigDecimal 		 getCodTipoProvvedimento() 		throws DAOException	 { return getBigDecimal("COD_TIPO_PROVVEDIMENTO"); }
			public String 				 getCodOperatoreInserimento() 		throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); }
			public Date 					 getDataInserimento() 		throws DAOException	 { return getDate("DATA_INSERIMENTO"); }
			public String 				 getCodUfficioInserimento() 		throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); }
			public BigDecimal 		 getEveIdEvento() 		throws DAOException	 { return getBigDecimal("EVE_ID_EVENTO"); }
			public BigDecimal 		 getFasSieIdFascicoloSiep() 		throws DAOException	 { return getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"); }


  //
  // METODI SET()
  //

			public void  	 setIdRiepilogoProvvedimento(BigDecimal aValore ) 			 { setBigDecimal("ID_RIEPILOGO_PROVVEDIMENTO", aValore); }
			public void  	 setNumRes(BigDecimal aValore ) 			 { setBigDecimal("NUM_RES", aValore); }
			public void  	 setNumProgressivoRes(BigDecimal aValore ) 			 { setBigDecimal("NUM_PROGRESSIVO_RES", aValore); }
			public void  	 setNumProtocolloRes(BigDecimal aValore ) 			 { setBigDecimal("NUM_PROTOCOLLO_RES", aValore); }
			public void  	 setFlagErgastolo(String aValore ) 			 { setString("FLAG_ERGASTOLO", aValore); }
			public void  	 setNumAnniReclusione(BigDecimal aValore ) 			 { setBigDecimal("NUM_ANNI_RECLUSIONE", aValore); }
			public void  	 setNumMesiReclusione(BigDecimal aValore ) 			 { setBigDecimal("NUM_MESI_RECLUSIONE", aValore); }
			public void  	 setNumGiorniReclusione(BigDecimal aValore ) 			 { setBigDecimal("NUM_GIORNI_RECLUSIONE", aValore); }
			public void  	 setImportoMulta(BigDecimal aValore ) 			 { setBigDecimal("IMPORTO_MULTA", aValore); }
			public void  	 setNumAnniArresto(BigDecimal aValore ) 			 { setBigDecimal("NUM_ANNI_ARRESTO", aValore); }
			public void  	 setNumMesiArresto(BigDecimal aValore ) 			 { setBigDecimal("NUM_MESI_ARRESTO", aValore); }
			public void  	 setNumGiorniArresto(BigDecimal aValore ) 			 { setBigDecimal("NUM_GIORNI_ARRESTO", aValore); }
			public void  	 setImportoAmmenda(BigDecimal aValore ) 			 { setBigDecimal("IMPORTO_AMMENDA", aValore); }
			public void  	 setNumAnniPresofferto(BigDecimal aValore ) 			 { setBigDecimal("NUM_ANNI_PRESOFFERTO", aValore); }
			public void  	 setNumMesiPresofferto(BigDecimal aValore ) 			 { setBigDecimal("NUM_MESI_PRESOFFERTO", aValore); }
			public void  	 setNumGiorniPresofferto(BigDecimal aValore ) 			 { setBigDecimal("NUM_GIORNI_PRESOFFERTO", aValore); }
			public void  	 setNumAnniInterruzione(BigDecimal aValore ) 			 { setBigDecimal("NUM_ANNI_INTERRUZIONE", aValore); }
			public void  	 setNumMesiInterruzione(BigDecimal aValore ) 			 { setBigDecimal("NUM_MESI_INTERRUZIONE", aValore); }
			public void  	 setNumGiorniInterruzione(BigDecimal aValore ) 			 { setBigDecimal("NUM_GIORNI_INTERRUZIONE", aValore); }
			public void  	 setNumGiorniLibAnticipata(BigDecimal aValore ) 			 { setBigDecimal("NUM_GIORNI_LIB_ANTICIPATA", aValore); }
			public void  	 setNumAnniReclusioneBenefici(BigDecimal aValore ) 			 { setBigDecimal("NUM_ANNI_RECLUSIONE_BENEFICI", aValore); }
			public void  	 setNumMesiReclusioneBenefici(BigDecimal aValore ) 			 { setBigDecimal("NUM_MESI_RECLUSIONE_BENEFICI", aValore); }
			public void  	 setNumGiorniReclusioneBenefici(BigDecimal aValore ) 			 { setBigDecimal("NUM_GIORNI_RECLUSIONE_BENEFICI", aValore); }
			public void  	 setImportoMultaBenefici(BigDecimal aValore ) 			 { setBigDecimal("IMPORTO_MULTA_BENEFICI", aValore); }
			public void  	 setNumAnniArrestoBenefici(BigDecimal aValore ) 			 { setBigDecimal("NUM_ANNI_ARRESTO_BENEFICI", aValore); }
			public void  	 setNumMesiArrestoBenefici(BigDecimal aValore ) 			 { setBigDecimal("NUM_MESI_ARRESTO_BENEFICI", aValore); }
			public void  	 setNumGiorniArrestoBenefici(BigDecimal aValore ) 			 { setBigDecimal("NUM_GIORNI_ARRESTO_BENEFICI", aValore); }
			public void  	 setImportoAmmendaBenefici(BigDecimal aValore ) 			 { setBigDecimal("IMPORTO_AMMENDA_BENEFICI", aValore); }
			public void  	 setNumAnniAumentiPenaReclus(BigDecimal aValore ) 			 { setBigDecimal("NUM_ANNI_AUMENTI_PENA_RECLUS", aValore); }
			public void  	 setNumMesiAumentiPenaReclus(BigDecimal aValore ) 			 { setBigDecimal("NUM_MESI_AUMENTI_PENA_RECLUS", aValore); }
			public void  	 setNumGiorniAumentiPenaReclus(BigDecimal aValore ) 			 { setBigDecimal("NUM_GIORNI_AUMENTI_PENA_RECLUS", aValore); }
			public void  	 setImportoMultaAumentiPena(BigDecimal aValore ) 			 { setBigDecimal("IMPORTO_MULTA_AUMENTI_PENA", aValore); }
			public void  	 setNumAnniAumentiPenaArres(BigDecimal aValore ) 			 { setBigDecimal("NUM_ANNI_AUMENTI_PENA_ARRES", aValore); }
			public void  	 setNumMesiAumentiPenaArres(BigDecimal aValore ) 			 { setBigDecimal("NUM_MESI_AUMENTI_PENA_ARRES", aValore); }
			public void  	 setNumGiorniAumentiPenaArres(BigDecimal aValore ) 			 { setBigDecimal("NUM_GIORNI_AUMENTI_PENA_ARRES", aValore); }
			public void  	 setImportoAmmendaAumentiPena(BigDecimal aValore ) 			 { setBigDecimal("IMPORTO_AMMENDA_AUMENTI_PENA", aValore); }
			public void  	 setDiesAQuo(String aValore ) 			 { setString("DIES_A_QUO", aValore); }
			public void  	 setDataInizioPena(Date aValore ) 			 { setDate("DATA_INIZIO_PENA", aValore); }
			public void  	 setDataFinePena(Date aValore ) 			 { setDate("DATA_FINE_PENA", aValore); }
			public void  	 setDataFineReclusione(Date aValore ) 			 { setDate("DATA_FINE_RECLUSIONE", aValore); }
			public void  	 setDataFinePrecedente(Date aValore ) 			 { setDate("DATA_FINE_PRECEDENTE", aValore); }
			public void  	 setDataFineDetDomiciliare(Date aValore ) 			 { setDate("DATA_FINE_DET_DOMICILIARE", aValore); }
			public void  	 setNumAnniPenaResiduaReclus(BigDecimal aValore ) 			 { setBigDecimal("NUM_ANNI_PENA_RESIDUA_RECLUS", aValore); }
			public void  	 setNumMesiPenaResiduaReclus(BigDecimal aValore ) 			 { setBigDecimal("NUM_MESI_PENA_RESIDUA_RECLUS", aValore); }
			public void  	 setNumGiorniPenaResiduaReclus(BigDecimal aValore ) 			 { setBigDecimal("NUM_GIORNI_PENA_RESIDUA_RECLUS", aValore); }
			public void  	 setImportoMultaResidua(BigDecimal aValore ) 			 { setBigDecimal("IMPORTO_MULTA_RESIDUA", aValore); }
			public void  	 setNumAnniPenaResiduaArres(BigDecimal aValore ) 			 { setBigDecimal("NUM_ANNI_PENA_RESIDUA_ARRES", aValore); }
			public void  	 setNumMesiPenaResiduaArres(BigDecimal aValore ) 			 { setBigDecimal("NUM_MESI_PENA_RESIDUA_ARRES", aValore); }
			public void  	 setNumGiorniPenaResiduaArres(BigDecimal aValore ) 			 { setBigDecimal("NUM_GIORNI_PENA_RESIDUA_ARRES", aValore); }
			public void  	 setImportoAmmendaResidua(BigDecimal aValore ) 			 { setBigDecimal("IMPORTO_AMMENDA_RESIDUA", aValore); }
			public void  	 setNumAnniFungibilita(BigDecimal aValore ) 			 { setBigDecimal("NUM_ANNI_FUNGIBILITA", aValore); }
			public void  	 setNumMesiFungibilita(BigDecimal aValore ) 			 { setBigDecimal("NUM_MESI_FUNGIBILITA", aValore); }
			public void  	 setNumGiorniFungibilita(BigDecimal aValore ) 			 { setBigDecimal("NUM_GIORNI_FUNGIBILITA", aValore); }
			public void  	 setCodTipoProvvedimento(BigDecimal aValore ) 			 { setBigDecimal("COD_TIPO_PROVVEDIMENTO", aValore); }
			public void  	 setCodOperatoreInserimento(String aValore ) 			 { setString("COD_OPERATORE_INSERIMENTO", aValore); }
			public void  	 setDataInserimento(Date aValore ) 			 { setDate("DATA_INSERIMENTO", aValore); }
			public void  	 setCodUfficioInserimento(String aValore ) 			 { setString("COD_UFFICIO_INSERIMENTO", aValore); }
			public void  	 setEveIdEvento(BigDecimal aValore ) 			 { setBigDecimal("EVE_ID_EVENTO", aValore); }
			public void  	 setFasSieIdFascicoloSiep(BigDecimal aValore ) 			 { setBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP", aValore); }


	public GenericModel getModel() throws DAOException
  			 {
 				 return new RiepilogoProvvedimentoModel(
								 getIdRiepilogoProvvedimento() ,
								 getNumRes() ,
								 getNumProgressivoRes() ,
								 getNumProtocolloRes() ,
								 getFlagErgastolo() ,
								 getNumAnniReclusione() ,
								 getNumMesiReclusione() ,
								 getNumGiorniReclusione() ,
								 getImportoMulta() ,
								 getNumAnniArresto() ,
								 getNumMesiArresto() ,
								 getNumGiorniArresto() ,
								 getImportoAmmenda() ,
								 getNumAnniPresofferto() ,
								 getNumMesiPresofferto() ,
								 getNumGiorniPresofferto() ,
								 getNumAnniInterruzione() ,
								 getNumMesiInterruzione() ,
								 getNumGiorniInterruzione() ,
								 getNumGiorniLibAnticipata() ,
								 getNumAnniReclusioneBenefici() ,
								 getNumMesiReclusioneBenefici() ,
								 getNumGiorniReclusioneBenefici() ,
								 getImportoMultaBenefici() ,
								 getNumAnniArrestoBenefici() ,
								 getNumMesiArrestoBenefici() ,
								 getNumGiorniArrestoBenefici() ,
								 getImportoAmmendaBenefici() ,
								 getNumAnniAumentiPenaReclus() ,
								 getNumMesiAumentiPenaReclus() ,
								 getNumGiorniAumentiPenaReclus() ,
								 getImportoMultaAumentiPena() ,
								 getNumAnniAumentiPenaArres() ,
								 getNumMesiAumentiPenaArres() ,
								 getNumGiorniAumentiPenaArres() ,
								 getImportoAmmendaAumentiPena() ,
								 getDiesAQuo() ,
								 getDataInizioPena() ,
								 getDataFinePena() ,
								 getDataFineReclusione() ,
								 getDataFinePrecedente() ,
								 getDataFineDetDomiciliare() ,
								 getNumAnniPenaResiduaReclus() ,
								 getNumMesiPenaResiduaReclus() ,
								 getNumGiorniPenaResiduaReclus() ,
								 getImportoMultaResidua() ,
								 getNumAnniPenaResiduaArres() ,
								 getNumMesiPenaResiduaArres() ,
								 getNumGiorniPenaResiduaArres() ,
								 getImportoAmmendaResidua() ,
								 getNumAnniFungibilita() ,
								 getNumMesiFungibilita() ,
								 getNumGiorniFungibilita() ,
								 getCodTipoProvvedimento() ,
								 "",
								 getCodOperatoreInserimento() ,
								 getDataInserimento() ,
								 getCodUfficioInserimento() ,
								 "",
								 getEveIdEvento() ,
								 getFasSieIdFascicoloSiep()
								);
		}


	 public void 	 setDAOFromModel(RiepilogoProvvedimentoModel aModel) throws DAOException
  		{
				 setIdRiepilogoProvvedimento( aModel.getIdRiepilogoProvvedimento() );
				 setNumRes( aModel.getNumRes() );
				 setNumProgressivoRes( aModel.getNumProgressivoRes() );
				 setNumProtocolloRes( aModel.getNumProtocolloRes() );
				 setFlagErgastolo( aModel.getFlagErgastolo() );
				 setNumAnniReclusione( aModel.getNumAnniReclusione() );
				 setNumMesiReclusione( aModel.getNumMesiReclusione() );
				 setNumGiorniReclusione( aModel.getNumGiorniReclusione() );
				 setImportoMulta( aModel.getImportoMulta() );
				 setNumAnniArresto( aModel.getNumAnniArresto() );
				 setNumMesiArresto( aModel.getNumMesiArresto() );
				 setNumGiorniArresto( aModel.getNumGiorniArresto() );
				 setImportoAmmenda( aModel.getImportoAmmenda() );
				 setNumAnniPresofferto( aModel.getNumAnniPresofferto() );
				 setNumMesiPresofferto( aModel.getNumMesiPresofferto() );
				 setNumGiorniPresofferto( aModel.getNumGiorniPresofferto() );
				 setNumAnniInterruzione( aModel.getNumAnniInterruzione() );
				 setNumMesiInterruzione( aModel.getNumMesiInterruzione() );
				 setNumGiorniInterruzione( aModel.getNumGiorniInterruzione() );
				 setNumGiorniLibAnticipata( aModel.getNumGiorniLibAnticipata() );
				 setNumAnniReclusioneBenefici( aModel.getNumAnniReclusioneBenefici() );
				 setNumMesiReclusioneBenefici( aModel.getNumMesiReclusioneBenefici() );
				 setNumGiorniReclusioneBenefici( aModel.getNumGiorniReclusioneBenefici() );
				 setImportoMultaBenefici( aModel.getImportoMultaBenefici() );
				 setNumAnniArrestoBenefici( aModel.getNumAnniArrestoBenefici() );
				 setNumMesiArrestoBenefici( aModel.getNumMesiArrestoBenefici() );
				 setNumGiorniArrestoBenefici( aModel.getNumGiorniArrestoBenefici() );
				 setImportoAmmendaBenefici( aModel.getImportoAmmendaBenefici() );
				 setNumAnniAumentiPenaReclus( aModel.getNumAnniAumentiPenaReclus() );
				 setNumMesiAumentiPenaReclus( aModel.getNumMesiAumentiPenaReclus() );
				 setNumGiorniAumentiPenaReclus( aModel.getNumGiorniAumentiPenaReclus() );
				 setImportoMultaAumentiPena( aModel.getImportoMultaAumentiPena() );
				 setNumAnniAumentiPenaArres( aModel.getNumAnniAumentiPenaArres() );
				 setNumMesiAumentiPenaArres( aModel.getNumMesiAumentiPenaArres() );
				 setNumGiorniAumentiPenaArres( aModel.getNumGiorniAumentiPenaArres() );
				 setImportoAmmendaAumentiPena( aModel.getImportoAmmendaAumentiPena() );
				 setDiesAQuo( aModel.getDiesAQuo() );
				 setDataInizioPena( aModel.getDataInizioPena() );
				 setDataFinePena( aModel.getDataFinePena() );
				 setDataFineReclusione( aModel.getDataFineReclusione() );
				 setDataFinePrecedente( aModel.getDataFinePrecedente() );
				 setDataFineDetDomiciliare( aModel.getDataFineDetDomiciliare() );
				 setNumAnniPenaResiduaReclus( aModel.getNumAnniPenaResiduaReclus() );
				 setNumMesiPenaResiduaReclus( aModel.getNumMesiPenaResiduaReclus() );
				 setNumGiorniPenaResiduaReclus( aModel.getNumGiorniPenaResiduaReclus() );
				 setImportoMultaResidua( aModel.getImportoMultaResidua() );
				 setNumAnniPenaResiduaArres( aModel.getNumAnniPenaResiduaArres() );
				 setNumMesiPenaResiduaArres( aModel.getNumMesiPenaResiduaArres() );
				 setNumGiorniPenaResiduaArres( aModel.getNumGiorniPenaResiduaArres() );
				 setImportoAmmendaResidua( aModel.getImportoAmmendaResidua() );
				 setNumAnniFungibilita( aModel.getNumAnniFungibilita() );
				 setNumMesiFungibilita( aModel.getNumMesiFungibilita() );
				 setNumGiorniFungibilita( aModel.getNumGiorniFungibilita() );
				 setCodTipoProvvedimento( aModel.getCodTipoProvvedimento() );
				 setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
				 setDataInserimento( aModel.getDataInserimento() );
				 setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
				 setEveIdEvento( aModel.getEveIdEvento() );
				 setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );
		}


	 public void 	 setDAOFromModelForUpdate(RiepilogoProvvedimentoModel aModel) throws DAOException
  		{
				 setIdRiepilogoProvvedimento( aModel.getIdRiepilogoProvvedimento() );
				 setNumRes( aModel.getNumRes() );
				 setNumProgressivoRes( aModel.getNumProgressivoRes() );
				 setNumProtocolloRes( aModel.getNumProtocolloRes() );
				 setFlagErgastolo( aModel.getFlagErgastolo() );
				 setNumAnniReclusione( aModel.getNumAnniReclusione() );
				 setNumMesiReclusione( aModel.getNumMesiReclusione() );
				 setNumGiorniReclusione( aModel.getNumGiorniReclusione() );
				 setImportoMulta( aModel.getImportoMulta() );
				 setNumAnniArresto( aModel.getNumAnniArresto() );
				 setNumMesiArresto( aModel.getNumMesiArresto() );
				 setNumGiorniArresto( aModel.getNumGiorniArresto() );
				 setImportoAmmenda( aModel.getImportoAmmenda() );
				 setNumAnniPresofferto( aModel.getNumAnniPresofferto() );
				 setNumMesiPresofferto( aModel.getNumMesiPresofferto() );
				 setNumGiorniPresofferto( aModel.getNumGiorniPresofferto() );
				 setNumAnniInterruzione( aModel.getNumAnniInterruzione() );
				 setNumMesiInterruzione( aModel.getNumMesiInterruzione() );
				 setNumGiorniInterruzione( aModel.getNumGiorniInterruzione() );
				 setNumGiorniLibAnticipata( aModel.getNumGiorniLibAnticipata() );
				 setNumAnniReclusioneBenefici( aModel.getNumAnniReclusioneBenefici() );
				 setNumMesiReclusioneBenefici( aModel.getNumMesiReclusioneBenefici() );
				 setNumGiorniReclusioneBenefici( aModel.getNumGiorniReclusioneBenefici() );
				 setImportoMultaBenefici( aModel.getImportoMultaBenefici() );
				 setNumAnniArrestoBenefici( aModel.getNumAnniArrestoBenefici() );
				 setNumMesiArrestoBenefici( aModel.getNumMesiArrestoBenefici() );
				 setNumGiorniArrestoBenefici( aModel.getNumGiorniArrestoBenefici() );
				 setImportoAmmendaBenefici( aModel.getImportoAmmendaBenefici() );
				 setNumAnniAumentiPenaReclus( aModel.getNumAnniAumentiPenaReclus() );
				 setNumMesiAumentiPenaReclus( aModel.getNumMesiAumentiPenaReclus() );
				 setNumGiorniAumentiPenaReclus( aModel.getNumGiorniAumentiPenaReclus() );
				 setImportoMultaAumentiPena( aModel.getImportoMultaAumentiPena() );
				 setNumAnniAumentiPenaArres( aModel.getNumAnniAumentiPenaArres() );
				 setNumMesiAumentiPenaArres( aModel.getNumMesiAumentiPenaArres() );
				 setNumGiorniAumentiPenaArres( aModel.getNumGiorniAumentiPenaArres() );
				 setImportoAmmendaAumentiPena( aModel.getImportoAmmendaAumentiPena() );
				 setDiesAQuo( aModel.getDiesAQuo() );
				 setDataInizioPena( aModel.getDataInizioPena() );
				 setDataFinePena( aModel.getDataFinePena() );
				 setDataFineReclusione( aModel.getDataFineReclusione() );
				 setDataFinePrecedente( aModel.getDataFinePrecedente() );
				 setDataFineDetDomiciliare( aModel.getDataFineDetDomiciliare() );
				 setNumAnniPenaResiduaReclus( aModel.getNumAnniPenaResiduaReclus() );
				 setNumMesiPenaResiduaReclus( aModel.getNumMesiPenaResiduaReclus() );
				 setNumGiorniPenaResiduaReclus( aModel.getNumGiorniPenaResiduaReclus() );
				 setImportoMultaResidua( aModel.getImportoMultaResidua() );
				 setNumAnniPenaResiduaArres( aModel.getNumAnniPenaResiduaArres() );
				 setNumMesiPenaResiduaArres( aModel.getNumMesiPenaResiduaArres() );
				 setNumGiorniPenaResiduaArres( aModel.getNumGiorniPenaResiduaArres() );
				 setImportoAmmendaResidua( aModel.getImportoAmmendaResidua() );
				 setNumAnniFungibilita( aModel.getNumAnniFungibilita() );
				 setNumMesiFungibilita( aModel.getNumMesiFungibilita() );
				 setNumGiorniFungibilita( aModel.getNumGiorniFungibilita() );
				 setCodTipoProvvedimento( aModel.getCodTipoProvvedimento() );
				 setEveIdEvento( aModel.getEveIdEvento() );
				 setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );
		 setCondizioneUpdate(aModel.getIdRiepilogoProvvedimento());
		}


	public void setCondizione(RiepilogoProvvedimentoModel aModel)
		 {
		 String lCondizioni = new String();

		 boolean lInserito = false;
 		 if ( lInserito ) setCondition(lCondizioni);
		 }


	public void setCondizioneUpdate(BigDecimal key)
 			 {
	 setCondition(" ID_RIEPILOGO_PROVVEDIMENTO = " + key );
		 }

}
