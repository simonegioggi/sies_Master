package siap.siep.altrigradigiudizio.action;


/**
* <p>Title: ActModificaAltriGradiGiudizio</p>
* <p>Description: Classe Action per la modifica di AltriGradiGiudizio</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.siep.altrigradigiudizio.controller.IAltriGradiGiudizio;
import siap.siep.altrigradigiudizio.model.AltriGradiGiudizioModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActModificaAltriGradiGiudizio extends ActionSiap implements ICostantiAltriGradiGiudizio
{
/**
* Azione di Modifica del AltriGradiGiudizio
* @return Nome della pagina JSP da visualizzare
* al termine dell'elaborazione
* @throws F3BException
*/
public String processRequest() throws F3BException 
{

 		 String lId = getRequestStringParameter(CAMPO_ID_ALTRIGRADIGIUDIZIO);
 		 // riempie il model
 		 AltriGradiGiudizioModel lAltMod = new AltriGradiGiudizioModel ();

 		 lAltMod.setIdAltrigradigiudizio(new BigDecimal(lId));
		 lAltMod.setIdAltrigradigiudizio( getRequestBigDecimalParameter( CAMPO_ID_ALTRIGRADIGIUDIZIO) );
		 lAltMod.setDataSentenzaIGrado( getRequestDateParameter( CAMPO_ANNO_DATA_SENTENZA_I_GRADO,CAMPO_MESE_DATA_SENTENZA_I_GRADO,CAMPO_GIORNO_DATA_SENTENZA_I_GRADO) );
		 lAltMod.setAnnoSentenzaIGrado( getRequestBigDecimalParameter( CAMPO_ANNO_SENTENZA_I_GRADO) );
		 lAltMod.setNumeroSentenzaIGrado( getRequestStringParameter( CAMPO_NUMERO_SENTENZA_I_GRADO) );
		 lAltMod.setCodAutEmittSentIGrado( getRequestStringParameter( CAMPO_COD_AUT_EMITT_SENT_I_GRADO) );
		 
		 if (!isRequestParameterNullObj(CAMPO_COD_LUO_EMITT_SENT_I_GRADO))	
			{
				ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(getRequestStringParameter( CAMPO_COD_LUO_EMITT_SENT_I_GRADO )) );
				lAltMod.setCodLuoEmittSentIGrado( lComMod.getCodComune());
			}
		 else
			lAltMod.setCodLuoEmittSentIGrado( "-");
		 //lAltMod.setCodLuoEmittSentIGrado( getRequestStringParameter( CAMPO_COD_LUO_EMITT_SENT_I_GRADO) );
		 
		 lAltMod.setNumSezEmittSentIGrado( getRequestStringParameter( CAMPO_NUM_SEZ_EMITT_SENT_I_GRADO) );
		 lAltMod.setCodTipoSentenzaIiGrado( getRequestStringParameter( CAMPO_COD_TIPO_SENTENZA_II_GRADO) );
		 
		 if (getRequestStringParameter(CAMPO_COD_TIPO_SENTENZA_II_GRADO).compareTo("-") != 0)
			{
				String lCodTipo = getRequestStringParameter(CAMPO_COD_AUT_EMITT_SENT_I_GRADO);			
				
//					MODIFICA 01-03-2006 -- DARIO -- VIVIANA
//					AGGIUNGO ALTRI UFFICI A QUELLI ESISTENTI E SOSTITUISCO CAS CON CASAP
//					if (lCodTipo.equals("CAP") || lCodTipo.equals("CAPSM") || lCodTipo.equals("CAS") || lCodTipo.equals("PGCAP"))
				if (lCodTipo.equals("CAP") || lCodTipo.equals("CAPSM") || lCodTipo.equals("CASAP") || lCodTipo.equals("PGCAP") || lCodTipo.equals("PGMI") || lCodTipo.equals("PGMID"))
				{
					if ( getRequestStringParameter( CAMPO_COD_TIPO_SENTENZA_II_GRADO).equals("01"))
						lAltMod.setCodTipoSentenzaIiGrado("03");
					else
						lAltMod.setCodTipoSentenzaIiGrado("04");
				}
				
			}
		 
		 
		 lAltMod.setDataSentenzaIiGrado( getRequestDateParameter( CAMPO_ANNO_DATA_SENTENZA_II_GRADO,CAMPO_MESE_DATA_SENTENZA_II_GRADO,CAMPO_GIORNO_DATA_SENTENZA_II_GRADO) );
		 lAltMod.setAnnoSentenzaIiGrado( getRequestBigDecimalParameter( CAMPO_ANNO_SENTENZA_II_GRADO) );
		 lAltMod.setNumeroSentenzaIiGrado( getRequestStringParameter( CAMPO_NUMERO_SENTENZA_II_GRADO) );
		 lAltMod.setCodAutEmittSentIiGrado( getRequestStringParameter( CAMPO_COD_AUT_EMITT_SENT_II_GRADO) );
		 
		 lAltMod.setCodLuoEmittSentIiGrado( getRequestStringParameter( CAMPO_COD_LUO_EMITT_SENT_II_GRADO) );
		 if (!isRequestParameterNullObj(CAMPO_COD_LUO_EMITT_SENT_II_GRADO))	
			{
				ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(getRequestStringParameter( CAMPO_COD_LUO_EMITT_SENT_II_GRADO )) );
				lAltMod.setCodLuoEmittSentIiGrado(lComMod.getCodComune());
			}
		 else
			lAltMod.setCodLuoEmittSentIiGrado( "-");
		 //lAltMod.setCodLuoEmittSentIiGrado( getRequestStringParameter( CAMPO_COD_LUO_EMITT_SENT_II_GRADO) );
		 
		 lAltMod.setNumSezEmittSentIiGrado( getRequestStringParameter( CAMPO_NUM_SEZ_EMITT_SENT_II_GRADO) );
		 lAltMod.setAnnoRegGenCassaz( getRequestBigDecimalParameter( CAMPO_ANNO_REG_GEN_CASSAZ) );
		 lAltMod.setNumeroRegGenCassaz( getRequestStringParameter( CAMPO_NUMERO_REG_GEN_CASSAZ) );
		 lAltMod.setAnnoSentenzaCassaz( getRequestBigDecimalParameter( CAMPO_ANNO_SENTENZA_CASSAZ) );
		 lAltMod.setNumeroSentenzaCassaz( getRequestStringParameter( CAMPO_NUMERO_SENTENZA_CASSAZ) );
		 lAltMod.setAnnoRaccGenealeIiGrado( getRequestBigDecimalParameter( CAMPO_ANNO_RACC_GENEALE_II_GRADO) );
		 lAltMod.setNumeroRaccGenealeIiGrado( getRequestStringParameter( CAMPO_NUMERO_RACC_GENEALE_II_GRADO) );
		 lAltMod.setCodTipoDecisioneCassazione( getRequestStringParameter( CAMPO_COD_TIPO_DECISIONE_CASSAZIONE) );
		 lAltMod.setSenIdSentenza( getRequestBigDecimalParameter( CAMPO_SEN_ID_SENTENZA) );
		 //lAltMod.setDataInserimento( getRequestDateParameter( CAMPO_ANNO_DATA_INSERIMENTO,CAMPO_MESE_DATA_INSERIMENTO,CAMPO_GIORNO_DATA_INSERIMENTO) );
		 UtenteModel lUtenteMod = new UtenteModel((UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		 lAltMod.setCodOperatoreAggiornamento(lUtenteMod.getUserId());
		 lAltMod.setCodUfficioAggiornamento(lUtenteMod.getUfficioUtente().getCodUfficio());		
		 if (!isRequestParameterNullObj(CAMPO_COD_TIPO_RITO)) {
				
				lAltMod.setCodTipoRito(getRequestStringParameter(CAMPO_COD_TIPO_RITO));
				
				if (!isRequestParameterNullObj(CAMPO_COD_TIPO_RITO_RIF)) {
					
					if (!getRequestStringParameter(CAMPO_COD_TIPO_RITO_RIF).equals("-"))
						lAltMod.setCodTipoRito(getRequestStringParameter(CAMPO_COD_TIPO_RITO_RIF));
					
				}
			}
			else {
				
				lAltMod.setCodTipoRito("-");
			}
		 
		 
		 lAltMod.setDataAggiornamento(DateUtils.getSysDate());

		 // chiama il controller
		 IAltriGradiGiudizio lCtrl = SIEPLookupRemote.getAltriGradiGiudizioRemote();
		 AltriGradiGiudizioModel llAltModRet = lCtrl.ExModificaAltriGradiGiudizio(lAltMod);

		 setRequestAttribute("modalita", "M");
		 setRequestAttribute("altrigradigiudizio", llAltModRet);

		 String lPage = "";
		 lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.altrigradigiudizio.action.ActLoadDettaglioAltriGradiGiudizio&"+CAMPO_ID_ALTRIGRADIGIUDIZIO+"="+llAltModRet.getIdAltrigradigiudizio().toString();
		 return lPage;
	 }



}