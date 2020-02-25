package siap.siep.modulocumulo.action;

/**
* <p>Title: ActModificaPenaComplessivaCumulo</p>
* <p>Description: Classe Action per la modifica di PenaComplessiva</p>
* <p>   in ambito Cumulo (Pena_complessiva_Cumulo) </p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.siep.modulocumulo.controller.IPenaComplessivaCumulo;
import siap.siep.modulocumulo.model.PenaComplessivaCumuloModel;
import siap.siep.modulocumulo.model.PenaComplessivaSanzioneSostitutivaCumuloModel;
import siap.siep.modulocumulo.model.SanzioneSostitutivaCumuloModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.Utils;
import f3b.web.IWebConstants;

public class ActModificaPenaComplessivaCumulo extends ActionModuloCumulo implements ICostantiPenaComplessivaCumulo,
                                                                      ICostantiSanzioneSostitutivaCumulo
{
  /**
   * Azione di Modifica del PenaComplessivaCumulo e Sanzione Sostitutiva
   * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
   * @throws Exception
   */
  
  public String processRequest() throws Exception
  {

    BigDecimal lIdPena = getRequestBigDecimalParameter(CAMPO_ID_PENA_COMPLESSIVA_CUM);

    PenaComplessivaCumuloModel lPenMod = new PenaComplessivaCumuloModel ();

    lPenMod.setIdPenaComplessivaCum (lIdPena);
    
    lPenMod.setCodTipoPenaDetentiva ( getRequestStringParameter( CAMPO_COD_TIPO_PENA_DETENTIVA) );

    // RECLUSIONE e MULTA
    lPenMod.setNumAnniReclusione    ( getRequestBigDecimalParameter( CAMPO_NUM_ANNI_RECLUSIONE) );
    lPenMod.setNumMesiReclusione    ( getRequestBigDecimalParameter( CAMPO_NUM_MESI_RECLUSIONE) );
    lPenMod.setNumGiorniReclusione  ( getRequestBigDecimalParameter( CAMPO_NUM_GIORNI_RECLUSIONE) );

    if(     getRequestStringParameter(CAMPO_INTERO_IMPORTO_MULTA)!=null
       && !(getRequestStringParameter(CAMPO_INTERO_IMPORTO_MULTA)).equals("") )
    {
      if(getRequestStringParameter(CAMPO_VALUTA_IMPORTO_MULTA).compareTo("LIT")==0)
      {
        lPenMod.setImportoMulta(Utils.toEuro(getRequestStringParameter(CAMPO_INTERO_IMPORTO_MULTA)));
      }
      else
      {
        lPenMod.setImportoMulta(new BigDecimal(getRequestStringParameter(CAMPO_INTERO_IMPORTO_MULTA)+"."+getRequestStringParameter(CAMPO_DECIMALE_IMPORTO_MULTA)));
      }
    }
    
    // Arresto e Ammenda
    lPenMod.setNumAnniArresto   ( getRequestBigDecimalParameter( CAMPO_NUM_ANNI_ARRESTO) );
    lPenMod.setNumMesiArresto   ( getRequestBigDecimalParameter( CAMPO_NUM_MESI_ARRESTO) );
    lPenMod.setNumGiorniArresto ( getRequestBigDecimalParameter( CAMPO_NUM_GIORNI_ARRESTO) );
    
    if( getRequestStringParameter(CAMPO_INTERO_IMPORTO_AMMENDA)!=null
       && !(getRequestStringParameter(CAMPO_INTERO_IMPORTO_AMMENDA)).equals("") )
    {
      if(getRequestStringParameter(CAMPO_VALUTA_IMPORTO_AMMENDA).compareTo("LIT")==0)
      {
        lPenMod.setImportoAmmenda(Utils.toEuro(getRequestStringParameter(CAMPO_INTERO_IMPORTO_AMMENDA)));
      }
      else
      {
        lPenMod.setImportoAmmenda(new BigDecimal(getRequestStringParameter(CAMPO_INTERO_IMPORTO_AMMENDA)+"."+getRequestStringParameter(CAMPO_DECIMALE_IMPORTO_AMMENDA)));
      }
    }
    
    // ERGASTOLO
    lPenMod.setNumAnniIsolamentoDiurno   ( getRequestBigDecimalParameter( CAMPO_NUM_ANNI_ISOLAMENTO_DIURNO) );
    lPenMod.setNumMesiIsolamentoDiurno   ( getRequestBigDecimalParameter( CAMPO_NUM_MESI_ISOLAMENTO_DIURNO) );
    lPenMod.setNumGiorniIsolamentoDiurno ( getRequestBigDecimalParameter( CAMPO_NUM_GIORNI_ISOLAMENTO_DIURNO) );

    
    // CONTINUAZIONE CON ALTRI REATI
    lPenMod.setFlagPenaInContinuazione(getRequestStringParameter(ICostantiPenaComplessivaCumulo.CAMPO_FLAG_PENA_IN_CONTINUAZIONE));

    lPenMod.setDataPrescrizione (getRequestDateParameter(CAMPO_ANNO_DATA_PRESCRIZIONE, CAMPO_MESE_DATA_PRESCRIZIONE, CAMPO_GIORNO_DATA_PRESCRIZIONE));

    lPenMod.setCodOperatoreAggiornamento (getCodUtenteConnesso());
    lPenMod.setCodUfficioAggiornamento   (getCodUfficioUtenteConnesso());
    lPenMod.setDataAggiornamento         (DateUtils.getSysDate());


    String lFlagStato = getRequestStringParameter(ICostantiPenaComplessivaCumulo.CAMPO_FLAG_STATO);
    if(lFlagStato.equals("E"))
      lPenMod.setFlagStato("M");
    else
      lPenMod.setFlagStato(getRequestStringParameter(ICostantiPenaComplessivaCumulo.CAMPO_FLAG_STATO));
    
    lPenMod.setMotivoModifica(getRequestStringParameter(ICostantiPenaComplessivaCumulo.CAMPO_MOTIVO_MODIFICA));
    
    //==========================================================================
    // Recupero i dati delle Sanzioni Sostitutive 
    //==========================================================================   
    SanzioneSostitutivaCumuloModel lSanSostMod = null;

    boolean flagSanzioneSostitutiva = isRequestChecked(CAMPO_FLAG_SANZIONE_SOSTITUTIVA);
    
    if(flagSanzioneSostitutiva)
    {
      lSanSostMod = new SanzioneSostitutivaCumuloModel();

      String lIdSanSosString = getRequestStringParameter(CAMPO_ID_SANZIONE_SOSTITUTIVA_CUM);
      BigDecimal lIdSanSos = null;
      if(lIdSanSosString != null && !lIdSanSosString.equals(""))
      {
        lIdSanSos = new BigDecimal(lIdSanSosString);
      }
      lSanSostMod.setIdSanzioneSostitutivaCum(lIdSanSos);
      
      lSanSostMod.setCodTipoSanzione(getRequestStringParameter(CAMPO_COD_TIPO_SANZIONE));
      if (!this.isRequestParameterNullObj(CAMPO_NUM_ANNI))
        lSanSostMod.setNumAnni(getRequestBigDecimalParameter(CAMPO_NUM_ANNI));
      
      if (!this.isRequestParameterNullObj(CAMPO_NUM_MESI))
        lSanSostMod.setNumMesi(getRequestBigDecimalParameter(CAMPO_NUM_MESI));
      
      if (!this.isRequestParameterNullObj(CAMPO_NUM_GIORNI))
        lSanSostMod.setNumGiorni(getRequestBigDecimalParameter(CAMPO_NUM_GIORNI));
      
      //  Sanzione Pecuniaria MULTA       
      if(   !this.isRequestParameterNullObj(CAMPO_INTERO_SANZIONE_PECUNIARIA_MULTA)
         && (   !getRequestStringParameter(CAMPO_INTERO_SANZIONE_PECUNIARIA_MULTA).equals("")
             || !getRequestStringParameter(CAMPO_DECIMALE_SANZIONE_PECUNIARIA_MULTA).equals("")
            )
        )
      {
        if(getRequestStringParameter(CAMPO_VALUTA_SANZIONE_PECUNIARIA).compareTo("LIT")==0)
        {
          lSanSostMod.setSanzionePecuniariaMulta(Utils.toEuro(getRequestStringParameter(CAMPO_INTERO_SANZIONE_PECUNIARIA_MULTA)));
        }
        else
        {
          lSanSostMod.setSanzionePecuniariaMulta(new BigDecimal(getRequestStringParameter(CAMPO_INTERO_SANZIONE_PECUNIARIA_MULTA)+"."+getRequestStringParameter(CAMPO_DECIMALE_SANZIONE_PECUNIARIA_MULTA)));
        }
      }

      //  Sanzione Pecuniaria AMMENDA      
      if( !this.isRequestParameterNullObj(CAMPO_INTERO_SANZIONE_PECUNIARIA_AMMENDA)
          && (   !getRequestStringParameter(CAMPO_INTERO_SANZIONE_PECUNIARIA_AMMENDA).equals("")
              || !getRequestStringParameter(CAMPO_DECIMALE_SANZIONE_PECUNIARIA_AMMENDA).equals("")
             )
         )
      {
        if(getRequestStringParameter(CAMPO_VALUTA_SANZIONE_PECUNIARIA).compareTo("LIT")==0)
        {
          lSanSostMod.setSanzionePecuniariaAmmenda(Utils.toEuro(getRequestStringParameter(CAMPO_INTERO_SANZIONE_PECUNIARIA_AMMENDA)));
        }
        else
        {
          lSanSostMod.setSanzionePecuniariaAmmenda(new BigDecimal(getRequestStringParameter(CAMPO_INTERO_SANZIONE_PECUNIARIA_AMMENDA)+"."+getRequestStringParameter(CAMPO_DECIMALE_SANZIONE_PECUNIARIA_AMMENDA)));
        }
      }

      // 
      lSanSostMod.setPcIdPenaComplessivaCum (lIdPena);     


      lFlagStato = getRequestStringParameter(ICostantiSanzioneSostitutivaCumulo.CAMPO_FLAG_STATO);
      if (lFlagStato.equals("")) // Se non presente in form allora sto in inserimento delle SS
        lSanSostMod.setFlagStato ("I"); 
      else if (lFlagStato.equals("E"))
        lSanSostMod.setFlagStato("M");
      else  // Se M resta M
        lSanSostMod.setFlagStato (getRequestStringParameter(ICostantiSanzioneSostitutivaCumulo.CAMPO_FLAG_STATO));

      
      lSanSostMod.setMotivoModifica      ( getRequestStringParameter     (ICostantiSanzioneSostitutivaCumulo.CAMPO_MOTIVO_MODIFICA_NOTE));
      
      lSanSostMod.setTitIdTitoloCumulato ( getRequestBigDecimalParameter (ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO));
       
      lSanSostMod.setCodOperatoreInserimento (getCodUtenteConnesso());
      lSanSostMod.setCodUfficioInserimento   (getCodUfficioUtenteConnesso());
      lSanSostMod.setDataInserimento         (DateUtils.getSysDate());

      lSanSostMod.setCodOperatoreAggiornamento (getCodUtenteConnesso());
      lSanSostMod.setCodUfficioAggiornamento   (getCodUfficioUtenteConnesso());
      lSanSostMod.setDataAggiornamento         (DateUtils.getSysDate());
    }
    else
    {
      // Il check SS non è selezionato, verifico se era presente una SS, in questo caso
      // recupero l'id per cancellarla
      lSanSostMod = new SanzioneSostitutivaCumuloModel();
      BigDecimal lIdSanSos = null;
      if( !isRequestParameterNullObj(CAMPO_ID_SANZIONE_SOSTITUTIVA_CUM) &&
          getRequestStringParameter(CAMPO_ID_SANZIONE_SOSTITUTIVA_CUM).length()>0 )
      {
        lIdSanSos = getRequestBigDecimalParameter(CAMPO_ID_SANZIONE_SOSTITUTIVA_CUM);
      }

      lSanSostMod.setIdSanzioneSostitutivaCum(lIdSanSos);

    }

    PenaComplessivaSanzioneSostitutivaCumuloModel lPenComSanzSostMod = null;

    IPenaComplessivaCumulo lCtrl = SIEPLookupRemote.getPenaComplessivaCumuloRemote();
    lPenComSanzSostMod = lCtrl.ExModificaPenaComplessivaSanzioneSostitutivaCum(lPenMod, lSanSostMod, flagSanzioneSostitutiva);
    lPenMod = lPenComSanzSostMod.getPenaComplessivaCumulo(); 

    String lPage = "";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.modulocumulo.action.ActLoadDettaglioPenaComplessivaCumulo&"+CAMPO_ID_PENA_COMPLESSIVA_CUM+"="+lPenMod.getIdPenaComplessivaCum().toString();

    return lPage;
 
  } // chiude Proces
  
}