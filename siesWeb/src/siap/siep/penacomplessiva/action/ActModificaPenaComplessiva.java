package siap.siep.penacomplessiva.action;


/**
* <p>Title: ActModificaPenaComplessiva</p>
* <p>Description: Classe Action per la modifica di PenaComplessiva</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penacomplessiva.model.PenaComplessivaSanzioneSostitutivaModel;
import siap.siep.sanzionesostitutiva.action.ICostantiSanzioneSostitutiva;
import siap.siep.sanzionesostitutiva.model.SanzioneSostitutivaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.Utils;
import f3b.web.IWebConstants;

public class ActModificaPenaComplessiva extends ActionSiap implements ICostantiPenaComplessiva,
                                                                      ICostantiSanzioneSostitutiva
{
  /**
   * Azione di Modifica del PenaComplessiva
   * @return Nome della pagina JSP da visualizzare
   * al termine dell'elaborazione
   * @throws Exception
   */
  public String processRequest() throws Exception
  {

    BigDecimal lIdPena = getRequestBigDecimalParameter(CAMPO_ID_PENA_COMPLESSIVA);

    PenaComplessivaModel lPenMod = new PenaComplessivaModel ();

    lPenMod.setIdPenaComplessiva(lIdPena);
    lPenMod.setCodTipoPenaDetentiva( getRequestStringParameter( CAMPO_COD_TIPO_PENA_DETENTIVA) );
    lPenMod.setNumAnniReclusione( getRequestBigDecimalParameter( CAMPO_NUM_ANNI_RECLUSIONE) );
    lPenMod.setNumMesiReclusione( getRequestBigDecimalParameter( CAMPO_NUM_MESI_RECLUSIONE) );
    lPenMod.setNumGiorniReclusione( getRequestBigDecimalParameter( CAMPO_NUM_GIORNI_RECLUSIONE) );

    lPenMod.setNumAnniIsolamentoDiurno( getRequestBigDecimalParameter( CAMPO_NUM_ANNI_ISOLAMENTO_DIURNO) );
    lPenMod.setNumMesiIsolamentoDiurno( getRequestBigDecimalParameter( CAMPO_NUM_MESI_ISOLAMENTO_DIURNO) );
    lPenMod.setNumGiorniIsolamentoDiurno( getRequestBigDecimalParameter( CAMPO_NUM_GIORNI_ISOLAMENTO_DIURNO) );


    if( getRequestStringParameter(CAMPO_INTERO_IMPORTO_MULTA)!=null
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
    lPenMod.setNumAnniArresto( getRequestBigDecimalParameter( CAMPO_NUM_ANNI_ARRESTO) );
    lPenMod.setNumMesiArresto( getRequestBigDecimalParameter( CAMPO_NUM_MESI_ARRESTO) );
    lPenMod.setNumGiorniArresto( getRequestBigDecimalParameter( CAMPO_NUM_GIORNI_ARRESTO) );
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
/*************************************************************** /
    // CONTINUAZIONE CON ALTRI REATI
    if(isRequestChecked(CAMPO_FLAG_PENA_IN_CONTINUAZIONE))
      lPenMod.setFlagPenaInContinuazione( "S" );
    else
      lPenMod.setFlagPenaInContinuazione( "N" );
//**************************************************************/

    // ? lPenMod.setDataInizio( getRequestDateParameter( CAMPO_ANNO_DATA_INIZIO,CAMPO_MESE_DATA_INIZIO,CAMPO_GIORNO_DATA_INIZIO) );
    // ? lPenMod.setDataFine( getRequestDateParameter( CAMPO_ANNO_DATA_FINE,CAMPO_MESE_DATA_FINE,CAMPO_GIORNO_DATA_FINE) );
    // ? lPenMod.setCodTipoRito( getRequestStringParameter( CAMPO_COD_TIPO_RITO) );

    // lPenMod.setDataInizioIsolamentoDiurno(getRequestDateParameter( CAMPO_ANNO_DATA_INIZIO_ISOLAMENTO_DIURNO, CAMPO_MESE_DATA_INIZIO_ISOLAMENTO_DIURNO, CAMPO_GIORNO_DATA_INIZIO_ISOLAMENTO_DIURNO));
    // lPenMod.setDataFineIsolamentoDiurno(getRequestDateParameter( CAMPO_ANNO_DATA_FINE_ISOLAMENTO_DIURNO, CAMPO_MESE_DATA_FINE_ISOLAMENTO_DIURNO, CAMPO_GIORNO_DATA_FINE_ISOLAMENTO_DIURNO));
    lPenMod.setDataPrescrizione(getRequestDateParameter(CAMPO_ANNO_DATA_PRESCRIZIONE, CAMPO_MESE_DATA_PRESCRIZIONE, CAMPO_GIORNO_DATA_PRESCRIZIONE));

    lPenMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
    lPenMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
    lPenMod.setDataAggiornamento(DateUtils.getSysDate());
    
    //==========================================================================
    // Recupero i dati delle Sanzioni Sostitutive 
    //==========================================================================
    SanzioneSostitutivaModel lSanSostMod = null;

    boolean flagSanzioneSostitutiva = isRequestChecked(CAMPO_FLAG_SANZIONE_SOSTITUTIVA);
    
    if(flagSanzioneSostitutiva)
    {
      lSanSostMod = new SanzioneSostitutivaModel();

      String lIdSanSosString = getRequestStringParameter(CAMPO_ID_SANZIONE_SOSTITUTIVA);
      BigDecimal lIdSanSos = null;
      if(lIdSanSosString != null && !lIdSanSosString.equals(""))
      {
        lIdSanSos = new BigDecimal(lIdSanSosString);
      }

      lSanSostMod.setIdSanzioneSostitutiva(lIdSanSos);
      lSanSostMod.setCodTipoSanzione(getRequestStringParameter(CAMPO_COD_TIPO_SANZIONE));
      if (!this.isRequestParameterNullObj(CAMPO_NUM_ANNI))
        lSanSostMod.setNumAnni(getRequestBigDecimalParameter(CAMPO_NUM_ANNI));
      
      if (!this.isRequestParameterNullObj(CAMPO_NUM_MESI))
        lSanSostMod.setNumMesi(getRequestBigDecimalParameter(CAMPO_NUM_MESI));
      
      if (!this.isRequestParameterNullObj(CAMPO_NUM_GIORNI))
        lSanSostMod.setNumGiorni(getRequestBigDecimalParameter(CAMPO_NUM_GIORNI));
      
      if( !this.isRequestParameterNullObj(CAMPO_INTERO_SANZIONE_PECUNIARIA_MULTA)
         && getRequestStringParameter(CAMPO_INTERO_SANZIONE_PECUNIARIA_MULTA)!=null
         && !(getRequestStringParameter(CAMPO_INTERO_SANZIONE_PECUNIARIA_MULTA)).equals("") )
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
      
      if( !this.isRequestParameterNullObj(CAMPO_INTERO_SANZIONE_PECUNIARIA_AMMENDA)
         && getRequestStringParameter(CAMPO_INTERO_SANZIONE_PECUNIARIA_AMMENDA)!=null
         && !(getRequestStringParameter(CAMPO_INTERO_SANZIONE_PECUNIARIA_AMMENDA)).equals("") )
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
      
      lSanSostMod.setPenComIdPenaComplessiva(lIdPena);

      lSanSostMod.setCodOperatoreInserimento(getCodUtenteConnesso());
      lSanSostMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
      lSanSostMod.setDataInserimento(DateUtils.getSysDate());

      lSanSostMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
      lSanSostMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
      lSanSostMod.setDataAggiornamento(DateUtils.getSysDate());
    }
    else
    {
      // Il check SS non è selezionato, verifico se era presente una SS, in questo
      // recupero l'id per cancellarla
      lSanSostMod = new SanzioneSostitutivaModel();
      BigDecimal lIdSanSos = null;
      if(!isRequestParameterNullObj(CAMPO_ID_SANZIONE_SOSTITUTIVA) &&
          getRequestStringParameter(CAMPO_ID_SANZIONE_SOSTITUTIVA).length()>0
        )
      {
        lIdSanSos = getRequestBigDecimalParameter(CAMPO_ID_SANZIONE_SOSTITUTIVA);
      }

      lSanSostMod.setIdSanzioneSostitutiva(lIdSanSos);

    }

      PenaComplessivaSanzioneSostitutivaModel lPenComSanzSostMod = null;

      IPenaComplessiva lCtrl = SIEPLookupRemote.getPenaComplessivaRemote();
      lPenComSanzSostMod = lCtrl.ExModificaPenaComplessivaSanzioneSostitutiva(lPenMod, lSanSostMod,flagSanzioneSostitutiva);
      lPenMod = lPenComSanzSostMod.getPenaComplessiva();

    String lPage = "";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.penacomplessiva.action.ActLoadDettaglioPenaComplessiva&"+CAMPO_ID_PENA_COMPLESSIVA+"="+lPenMod.getIdPenaComplessiva().toString();

    return lPage;
  }
}