package siap.siep.cumulo.action;



import java.math.BigDecimal;

import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.siep.cumulo.controller.ICumulo;
import siap.siep.cumulo.model.CumuloModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
* <p>Title: ActModificaCumulo</p>
* <p>Description: Classe Action per la modifica di Cumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @deprecated - Classe mai utilizzata (06/12/2007)
* @version 1.0
*/

public class ActModificaCumulo extends ActionSiap implements ICostantiCumulo
{
/**
* Azione di Modifica del Cumulo
* @return Nome della pagina JSP da visualizzare
* al termine dell'elaborazione
* @throws F3BException
*/
public String processRequest() throws F3BException
{

 		 String lId = getRequestStringParameter(CAMPO_ID_CUMULO);
 		 // riempie il model
 		 CumuloModel lCumMod = new CumuloModel ();

 		 lCumMod.setIdCumulo(new BigDecimal(lId));
		 lCumMod.setIdCumulo( getRequestBigDecimalParameter( CAMPO_ID_CUMULO) );
		 lCumMod.setIdFascicoloSiepCumulato( getRequestBigDecimalParameter( CAMPO_ID_FASCICOLO_SIEP_CUMULATO) );
		 lCumMod.setChiaveAnnoFasCumulato( getRequestBigDecimalParameter( CAMPO_CHIAVE_ANNO_FAS_CUMULATO) );
		 lCumMod.setChiaveProgrFasCumulato( getRequestBigDecimalParameter( CAMPO_CHIAVE_PROGR_FAS_CUMULATO) );
		 lCumMod.setCodTipoUfficioFasCumulato( getRequestStringParameter( CAMPO_COD_TIPO_UFFICIO_FAS_CUMULATO) );
		 lCumMod.setCodLuogoUfficioFasCumulato( getRequestStringParameter( CAMPO_COD_LUOGO_UFFICIO_FAS_CUMULATO) );
		 lCumMod.setCodUfficioFasCumulato( getRequestStringParameter( CAMPO_COD_UFFICIO_FAS_CUMULATO) );
		 lCumMod.setCodTipoCumulo( getRequestStringParameter( CAMPO_COD_TIPO_CUMULO) );
		 lCumMod.setDataRichiestaFascicolo( getRequestDateParameter( CAMPO_ANNO_DATA_RICHIESTA_FASCICOLO,CAMPO_MESE_DATA_RICHIESTA_FASCICOLO,CAMPO_GIORNO_DATA_RICHIESTA_FASCICOLO) );
		 lCumMod.setDataPervenimentoFascicolo( getRequestDateParameter( CAMPO_ANNO_DATA_PERVENIMENTO_FASCICOLO,CAMPO_MESE_DATA_PERVENIMENTO_FASCICOLO,CAMPO_GIORNO_DATA_PERVENIMENTO_FASCICOLO) );
		 lCumMod.setDataCumulo( getRequestDateParameter( CAMPO_ANNO_DATA_CUMULO,CAMPO_MESE_DATA_CUMULO,CAMPO_GIORNO_DATA_CUMULO) );
		 lCumMod.setCodMotivoSospensioneCumulo( getRequestStringParameter( CAMPO_COD_MOTIVO_SOSPENSIONE_CUMULO) );
		 lCumMod.setDataSospensioneCumulo( getRequestDateParameter( CAMPO_ANNO_DATA_SOSPENSIONE_CUMULO,CAMPO_MESE_DATA_SOSPENSIONE_CUMULO,CAMPO_GIORNO_DATA_SOSPENSIONE_CUMULO) );
		// lCumMod.setCodTipoPenaDetentiva( getRequestStringParameter( CAMPO_COD_TIPO_PENA_DETENTIVA) );
		 /*lCumMod.setNumAnniReclusione( getRequestBigDecimalParameter( CAMPO_NUM_ANNI_RECLUSIONE) );
		 lCumMod.setNumMesiReclusione( getRequestBigDecimalParameter( CAMPO_NUM_MESI_RECLUSIONE) );
		 lCumMod.setNumGiorniReclusione( getRequestBigDecimalParameter( CAMPO_NUM_GIORNI_RECLUSIONE) );
		 lCumMod.setImportoMulta( getRequestBigDecimalParameter( CAMPO_IMPORTO_MULTA) );
		 lCumMod.setNumAnniArresto( getRequestBigDecimalParameter( CAMPO_NUM_ANNI_ARRESTO) );
		 lCumMod.setNumMesiArresto( getRequestBigDecimalParameter( CAMPO_NUM_MESI_ARRESTO) );
		 lCumMod.setNumGiorniArresto( getRequestBigDecimalParameter( CAMPO_NUM_GIORNI_ARRESTO) );
		 lCumMod.setImportoAmmenda( getRequestBigDecimalParameter( CAMPO_IMPORTO_AMMENDA) );*/
		 lCumMod.setNote( getRequestStringParameter( CAMPO_NOTE) );
		 lCumMod.setDataInserimento( getRequestDateParameter( CAMPO_ANNO_DATA_INSERIMENTO,CAMPO_MESE_DATA_INSERIMENTO,CAMPO_GIORNO_DATA_INSERIMENTO) );
		 UtenteModel lUtenteMod = new UtenteModel((UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		 lCumMod.setCodOperatoreAggiornamento(lUtenteMod.getUserId());
		 lCumMod.setCodUfficioAggiornamento(lUtenteMod.getUfficioUtente().getCodUfficio());		 lCumMod.setDataAggiornamento(DateUtils.getSysDate());
		 lCumMod.setDataAggiornamento(DateUtils.getSysDate());
		 lCumMod.setFasSieIdFascicoloSiep( getRequestBigDecimalParameter( CAMPO_FAS_SIE_ID_FASCICOLO_SIEP) );

		 // chiama il controller
		 ICumulo lCtrl = SIEPLookupRemote.getCumuloRemote();
		 CumuloModel llCumModRet = lCtrl.ExModificaCumulo(lCumMod);

		 setRequestAttribute("modalita", "M");
		 setRequestAttribute("cumulo", llCumModRet);

		 String lPage = "";
		 lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.cumulo.action.ActLoadDettaglioCumulo&"+CAMPO_ID_CUMULO+"="+llCumModRet.getIdCumulo().toString();
		 return lPage;
	 }



}