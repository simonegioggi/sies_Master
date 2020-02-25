<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.math.BigDecimal" %>

<%@ page import="siap.sico.calendar.model.CalendarModel" %>
<%@ page import="siap.sico.util.CalendarUtil" %>

<%@ page import="siap.siep.penacomplessiva.action.ICostantiPenaComplessiva"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.penaresidua.model.PenaResiduaModel" %>

<jsp:useBean id="valute"             scope="request" class="java.lang.String"/>
<jsp:useBean id="PosizioneGiuridica" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaModel" />

<jsp:useBean id="PenaModelDaF5" scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel" />
<jsp:useBean id="caricaDati"    scope="request" class="java.lang.String"/>
<jsp:useBean id="LibAntGiorni"  scope="request" class="java.lang.String"/>

<html>
<head>
  <title>[S.I.E.S.] - Pena Manuale </title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript">
  //============================================================================
  // 
  //============================================================================
    function Verify()
    {
      //========================================================================
      //
      //========================================================================
      if (   (   document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value.length!=0
              && document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value.length!=0
              && document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>.value.length!=0
             )
          && (   document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_DECORRENZA_PENA%>.value.length!=0
              && document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_DECORRENZA_PENA%>.value.length!=0
              && document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_DECORRENZA_PENA%>.value.length!=0
             ) 
         )
      {
        if(   (document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_NUM_ANNI_RECLUSIONE%>.value.length==0
            && document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_NUM_MESI_RECLUSIONE%>.value.length==0
            && document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_NUM_GIORNI_RECLUSIONE%>.value.length==0
            && document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_NUM_ANNI_ARRESTO%>.value.length==0
            && document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_NUM_MESI_ARRESTO%>.value.length==0
            && document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_NUM_GIORNI_ARRESTO%>.value.length==0)
            && document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_FLAG_ERGASTOLO%>.checked ==false
          )
        {
          alert('Arresto o Reclusione obbligatori');
          document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_NUM_ANNI_RECLUSIONE%>.focus();

          return false;
        }
      }

      //========================================================================
      // Se presente solo Reclusione (quantum) verifico che non siano state 
      // inserite fine reclusione e inizio arresto
      //========================================================================
      if(   (   document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_NUM_ANNI_RECLUSIONE%>.value.length!=0
             || document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_NUM_MESI_RECLUSIONE%>.value.length!=0
             || document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_NUM_GIORNI_RECLUSIONE%>.value.length!=0
            )
         &&
            (   document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_NUM_ANNI_ARRESTO%>.value.length==0
             && document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_NUM_MESI_ARRESTO%>.value.length==0
             && document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_NUM_GIORNI_ARRESTO%>.value.length==0
            )
        )
      {
        
        if(    document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE_RECLUSIONE%>.value.length!=0
            || document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE_RECLUSIONE%>.value.length!=0
            || document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE_RECLUSIONE%>.value.length!=0
          )
        {
          alert('In caso di sola reclusione Data Fine Reclusione non deve essere inserita');
          document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE_RECLUSIONE%>.focus();

          return false;
        }
        
        if(    document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_INIZIO_ARRESTO%>.value.length!=0
            || document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_INIZIO_ARRESTO%>.value.length!=0
            || document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_INIZIO_ARRESTO%>.value.length!=0
          )
        {
          alert('In caso di sola reclusione Data Inizio Arresto non deve essere inserita');
          document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_INIZIO_ARRESTO%>.focus();

          return false;
        }
      }

      //========================================================================
      // CHECK Data Inizio Isolamento Diurno
      //========================================================================
      if (document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_GIORNO_DATA_INIZIO_ISOLAMENTO_DIURNO%>.value.length==1)
        document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_GIORNO_DATA_INIZIO_ISOLAMENTO_DIURNO%>.value='0'+document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_GIORNO_DATA_INIZIO_ISOLAMENTO_DIURNO%>.value;
      if (document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_MESE_DATA_INIZIO_ISOLAMENTO_DIURNO%>.value.length==1)
        document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_MESE_DATA_INIZIO_ISOLAMENTO_DIURNO%>.value='0'+document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_MESE_DATA_INIZIO_ISOLAMENTO_DIURNO%>.value;

      var d1_inizio_isol=document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_GIORNO_DATA_INIZIO_ISOLAMENTO_DIURNO%>.value+'/'+document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_MESE_DATA_INIZIO_ISOLAMENTO_DIURNO%>.value+'/'+document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_ANNO_DATA_INIZIO_ISOLAMENTO_DIURNO%>.value;
      if (! ControllaData(d1_inizio_isol) && d1_inizio_isol.length > 2)
      {
        alert('Data Inizio Isolamento Diurno non valida');
        document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_GIORNO_DATA_INIZIO_ISOLAMENTO_DIURNO%>.focus();

        return false;
      }

      //========================================================================
      // CHECK Data Fine Isolamento Diurno
      //========================================================================
      if (document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_GIORNO_DATA_FINE_ISOLAMENTO_DIURNO%>.value.length==1)
        document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_GIORNO_DATA_FINE_ISOLAMENTO_DIURNO%>.value='0'+document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_GIORNO_DATA_FINE_ISOLAMENTO_DIURNO%>.value;
      if (document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_MESE_DATA_FINE_ISOLAMENTO_DIURNO%>.value.length==1)
        document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_MESE_DATA_FINE_ISOLAMENTO_DIURNO%>.value='0'+document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_MESE_DATA_FINE_ISOLAMENTO_DIURNO%>.value;

      var d1_fine_isol=document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_GIORNO_DATA_FINE_ISOLAMENTO_DIURNO%>.value+'/'+document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_MESE_DATA_FINE_ISOLAMENTO_DIURNO%>.value+'/'+document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_ANNO_DATA_FINE_ISOLAMENTO_DIURNO%>.value;
      if (! ControllaData(d1_fine_isol) && d1_fine_isol.length > 2)
      {
        alert('Data Fine Isolamento Diurno non valida');
        document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_GIORNO_DATA_FINE_ISOLAMENTO_DIURNO%>.focus();

        return false;
      }

      //========================================================================
      // Check DATA INIZIO PENA
      //========================================================================
      if (document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_DECORRENZA_PENA%>.value.length==1)
        document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_DECORRENZA_PENA%>.value="0"+document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_DECORRENZA_PENA%>.value;
      if (document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_DECORRENZA_PENA%>.value.length==1)
        document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_DECORRENZA_PENA%>.value="0"+document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_DECORRENZA_PENA%>.value;

      var data_to_verify_dec=document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_DECORRENZA_PENA%>.value+'/'+document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_DECORRENZA_PENA%>.value+'/'+document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_DECORRENZA_PENA%>.value;
      if (!ControllaDataPassaVuota(data_to_verify_dec))
      {
        alert('Data Decorrenza Pena non valida');
        document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_DECORRENZA_PENA%>.focus();

        return false;
      }

      //========================================================================
      // Check DATA FINE PENA
      //========================================================================
      if (document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value.length==1)
        document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value="0"+document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value;
      if (document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value.length==1)
        document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value="0"+document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value;

      var data_to_verify_fine_pena=document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value+'/'+document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value+'/'+document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>.value;
      if (!ControllaDataPassaVuota(data_to_verify_fine_pena))
      {
        alert('Data Fine Pena non valida');
        document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.focus();

        return false;
      }

      //========================================================================
      // Check DATA INIZIO ARRESTO
      //========================================================================
      if (document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_INIZIO_ARRESTO%>.value.length==1)
        document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_INIZIO_ARRESTO%>.value="0"+document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_INIZIO_ARRESTO%>.value;
      if (document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_INIZIO_ARRESTO%>.value.length==1)
        document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_INIZIO_ARRESTO%>.value="0"+document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_INIZIO_ARRESTO%>.value;

      var data_to_verify_inizio_arr=document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_INIZIO_ARRESTO%>.value+'/'+document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_INIZIO_ARRESTO%>.value+'/'+document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_INIZIO_ARRESTO%>.value;
      if (!ControllaDataPassaVuota(data_to_verify_inizio_arr))
      {
        alert('Data Inizio Arresto non valida');
        document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_INIZIO_ARRESTO%>.focus();

        return false;
      }

      //========================================================================
      // Check DATA FINE RECLUSIONE
      //========================================================================
      if (document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE_RECLUSIONE%>.value.length==1)
        document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE_RECLUSIONE%>.value="0"+document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE_RECLUSIONE%>.value;
      if (document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE_RECLUSIONE%>.value.length==1)
        document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE_RECLUSIONE%>.value="0"+document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE_RECLUSIONE%>.value;

      var data_to_verify_fine_recl=document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE_RECLUSIONE%>.value+'/'+document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE_RECLUSIONE%>.value+'/'+document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE_RECLUSIONE%>.value;
      if (!ControllaDataPassaVuota(data_to_verify_fine_recl))
      {
        alert('Data Fine Reclusione non valida');
        document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE_RECLUSIONE%>.focus();

        return false;
      }
      
      //========================================================================
      // controlli se NO ERGATOLO
      // - 
      //========================================================================
      if(document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_FLAG_ERGASTOLO%>.checked ==false)
      {
        //---------------------------------------------
        // quantum o importi senza date di decorrenza
        //---------------------------------------------
        if(   (   document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_NUM_ANNI_RECLUSIONE%>.value.length!=0
               || document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_NUM_MESI_RECLUSIONE%>.value.length!=0
               || document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_NUM_GIORNI_RECLUSIONE%>.value.length!=0
               || document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_NUM_ANNI_ARRESTO%>.value.length!=0
               || document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_NUM_MESI_ARRESTO%>.value.length!=0
               || document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_NUM_GIORNI_ARRESTO%>.value.length!=0
               || document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_DECIMALE_IMPORTO_AMMENDA%>.value.length!=0
               || document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_DECIMALE_IMPORTO_MULTA%>.value.length!=0
               || document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_INTERO_IMPORTO_AMMENDA%>.value.length!=0
               || document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_INTERO_IMPORTO_MULTA%>.value.length!=0
              )
           && (   (   document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value.length==0 
                   && document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value.length==0
                   && document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>.value.length==0
                  ) 
               && (   document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE_RECLUSIONE%>.value.length==0 
                   && document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE_RECLUSIONE%>.value.length==0
                   && document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE_RECLUSIONE%>.value.length==0
                  ) 
               && (   document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_INIZIO_ARRESTO%>.value.length==0 
                   && document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_INIZIO_ARRESTO%>.value.length==0
                   && document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_INIZIO_ARRESTO%>.value.length==0
                  ) 
               && (   document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_DECORRENZA_PENA%>.value.length==0 
                   && document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_DECORRENZA_PENA%>.value.length==0
                   && document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_DECORRENZA_PENA%>.value.length==0 
                  ) 
              )
          )
        {
          return true;
        }

        //----------------------------------------------------------------------
        // Se presente una data intermedia deve essere specificata l'inizio pena
        //----------------------------------------------------------------------
        if(  (   (   document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value.length!=0 
                  && document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value.length!=0
                  && document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>.value.length!=0
                 )
              || (   document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE_RECLUSIONE%>.value.length!=0 
                  && document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE_RECLUSIONE%>.value.length!=0
                  && document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE_RECLUSIONE%>.value.length!=0
                 )
              || (   document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_INIZIO_ARRESTO%>.value.length!=0 
                  && document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_INIZIO_ARRESTO%>.value.length!=0
                  && document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_INIZIO_ARRESTO%>.value.length!=0
                 ) 
             ) 
           &&
             (   document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_DECORRENZA_PENA%>.value.length==0 
              && document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_DECORRENZA_PENA%>.value.length==0
              && document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_DECORRENZA_PENA%>.value.length==0 
             ) 
          )
        {
          alert('Data  Decorrenza Pena  obbligatoria ');
          document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_DECORRENZA_PENA%>.focus();

          return false;
        }

        //----------------------------------------------------------------------
        // Se presente data inizio obbligatorio 
        //----------------------------------------------------------------------
        if( (   (    document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_DECORRENZA_PENA%>.value.length!=0 
                  && document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_DECORRENZA_PENA%>.value.length!=0
                  && document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_DECORRENZA_PENA%>.value.length!=0 
                 )
              || (   document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE_RECLUSIONE%>.value.length!=0 
                  && document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE_RECLUSIONE%>.value.length!=0
                  && document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE_RECLUSIONE%>.value.length!=0
                 )
              || (   document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_INIZIO_ARRESTO%>.value.length!=0 
                  && document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_INIZIO_ARRESTO%>.value.length!=0
                  && document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_INIZIO_ARRESTO%>.value.length!=0
                 ) 
              ) 
            &&
              (   document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value.length==0 
               && document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value.length==0
               && document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>.value.length==0
              )
          )
        {
          alert('Data  Fine Pena obbligatoria');
          document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.focus();

          return false;
        }


        //----------------------------------------------------------------------
        // Data Fine > data inizio
        //----------------------------------------------------------------------
        if (   (   document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_DECORRENZA_PENA%>.value.length!=0 
                && document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_DECORRENZA_PENA%>.value.length!=0
                && document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_DECORRENZA_PENA%>.value.length!=0 
               )
            && (   document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value.length!=0 
                && document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value.length!=0
                && document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>.value.length!=0
               )
           )
        {
          if(CompareDate(data_to_verify_fine_pena,data_to_verify_dec))
          {
            alert('Data Fine Pena deve essere maggiore alla Data Decorrenza Pena ');

            return false;
          }
        }

        //----------------------------------------------------------------------
        // Se presente data fine reclusione deve essere presenti inizio arresto
        //----------------------------------------------------------------------
        if (   document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE_RECLUSIONE%>.value.length!=0 
            && document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE_RECLUSIONE%>.value.length!=0
            && document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE_RECLUSIONE%>.value.length!=0
           )
        { // data fine reclusione valorizzato
          if (   document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_INIZIO_ARRESTO%>.value.length==0 
              && document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_INIZIO_ARRESTO%>.value.length==0
              && document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_INIZIO_ARRESTO%>.value.length==0
             )
          {
            alert('Data  inizio Arresto obbligatoria ');

            return false;
          }

          if(CompareDate(data_to_verify_inizio_arr,data_to_verify_fine_recl))
          {
            alert('Data  inizio Arresto  deve essere maggiore alla Data Fine Reclusione ');

            return false;
          }
        }

  
        //----------------------------------------------------------------------
        // Se presente data inizio arresto oppure
        // deve essere presente anche la data fine reclusione
        // Data fine reclusione obbligatoria se
        //----------------------------------------------------------------------
        if (   (   document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_INIZIO_ARRESTO%>.value.length!=0 
                && document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_INIZIO_ARRESTO%>.value.length!=0
                && document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_INIZIO_ARRESTO%>.value.length!=0 
               )
            || (   (   (   document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_NUM_ANNI_RECLUSIONE%>.value.length!=0
                        || document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_NUM_MESI_RECLUSIONE%>.value.length!=0
                        || document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_NUM_GIORNI_RECLUSIONE%>.value.length!=0
//                        || document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_INTERO_IMPORTO_MULTA%>.value.length!=0
//                        || document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_DECIMALE_IMPORTO_MULTA%>.value.length!=0
                       )
                    && (   document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_NUM_ANNI_ARRESTO%>.value.length!=0
                        || document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_NUM_MESI_ARRESTO%>.value.length!=0
                        || document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_NUM_GIORNI_ARRESTO%>.value.length!=0
                       )
//                    || document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_DECIMALE_IMPORTO_AMMENDA%>.value.length!=0
//                    || document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_INTERO_IMPORTO_AMMENDA%>.value.length!=0
                   )
                && (   (   document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_DECORRENZA_PENA%>.value.length!=0 
                        && document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_DECORRENZA_PENA%>.value.length!=0
                        && document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_DECORRENZA_PENA%>.value.length!=0
                       ) 
                    && (    document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value.length!=0 
                        && document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value.length!=0
                        && document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>.value.length!=0
                       )
                   ) 
               )
           )
        { // data inizio arresto valorizzata
          if (   document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE_RECLUSIONE%>.value.length==0 
              && document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE_RECLUSIONE%>.value.length==0
              && document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE_RECLUSIONE%>.value.length==0
             )
          {
            alert('Data  Fine Reclusione obbligatoria ');
            document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE_RECLUSIONE%>.focus();

            return false;
          }

          if(CompareDate(data_to_verify_inizio_arr,data_to_verify_fine_recl))
          {
            alert('Data  inizio Arresto  deve essere maggiore alla Data Fine Reclusione ');
            document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_INIZIO_ARRESTO%>.focus();

            return false;
          }
        }
      }
      else
      {
        // ERGASTOLO
        if (   document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_DECORRENZA_PENA%>.value.length==0 
            && document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_DECORRENZA_PENA%>.value.length==0
            && document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_DECORRENZA_PENA%>.value.length==0 
           )
        {
          alert('Data  Decorrenza Pena  obbligatoria ');
          document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_DECORRENZA_PENA%>.focus();

          return false;
        }

        if (   document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_GIORNO_DATA_INIZIO_ISOLAMENTO_DIURNO%>.value.length!=0 
            && document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_MESE_DATA_INIZIO_ISOLAMENTO_DIURNO%>.value.length!=0
            && document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_ANNO_DATA_INIZIO_ISOLAMENTO_DIURNO%>.value.length!=0
           )
        { // DATA INIZIO ISOLAMENTO DIURNO VALORIZZATA
           if (   document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_GIORNO_DATA_FINE_ISOLAMENTO_DIURNO%>.value.length==0 
               && document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_ANNO_DATA_FINE_ISOLAMENTO_DIURNO%>.value.length==0
               && document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_ANNO_DATA_FINE_ISOLAMENTO_DIURNO%>.value.length==0
              )
            {
               alert('Data Fine Isolamento Diurno obbligatoria');
               document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_GIORNO_DATA_FINE_ISOLAMENTO_DIURNO%>.focus();

               return false;
            }
            
            if (   (   document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_GIORNO_DATA_INIZIO_ISOLAMENTO_DIURNO%>.value.length!=0 
                    && document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_MESE_DATA_INIZIO_ISOLAMENTO_DIURNO%>.value.length!=0
                    && document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_ANNO_DATA_INIZIO_ISOLAMENTO_DIURNO%>.value.length!=0
                   ) 
                && (   document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_GIORNO_DATA_FINE_ISOLAMENTO_DIURNO%>.value.length!=0 
                    && document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_MESE_DATA_FINE_ISOLAMENTO_DIURNO%>.value.length!=0
                    && document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_ANNO_DATA_FINE_ISOLAMENTO_DIURNO%>.value.length!=0
                   )
               )
            {
              if(CompareDate(d1_fine_isol,d1_inizio_isol))
              {
                alert('Data  Fine Isolamento Diurno   deve essere maggiore alla Data Inizio Isolamento Diurno ');
                document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_GIORNO_DATA_INIZIO_ISOLAMENTO_DIURNO%>.focus();

                return false;
              }
            }
          }


          if (    document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_GIORNO_DATA_FINE_ISOLAMENTO_DIURNO%>.value.length!=0 
               && document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_MESE_DATA_FINE_ISOLAMENTO_DIURNO%>.value.length!=0
               && document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_ANNO_DATA_FINE_ISOLAMENTO_DIURNO%>.value.length!=0
             )
          {// fine isolamento diurno valorizzato
            if (   document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_GIORNO_DATA_INIZIO_ISOLAMENTO_DIURNO%>.value.length==0 
                && document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_MESE_DATA_INIZIO_ISOLAMENTO_DIURNO%>.value.length==0
                && document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_ANNO_DATA_INIZIO_ISOLAMENTO_DIURNO%>.value.length==0
               )
            {
              alert('Data Inizio Isolamento Diurno obbligatoria');
              document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_GIORNO_DATA_INIZIO_ISOLAMENTO_DIURNO%>.focus();

              return false;
            }
            
            if(CompareDate(d1_fine_isol,d1_inizio_isol))
            {
              alert('Data  Fine Isolamento Diurno deve essere maggiore alla Data Inizio Isolamento Diurno ');
              document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_GIORNO_DATA_FINE_ISOLAMENTO_DIURNO%>.focus();
              return false;
            }
          }
        }

        //======================================================================
        // Controllo valorizzazione di almeno uno dei campi della sezione Pena
        //======================================================================
        if(document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_FLAG_ERGASTOLO%>.checked ==false)
        {
          if(   document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_NUM_ANNI_RECLUSIONE%>.value.length==0
             && document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_NUM_MESI_RECLUSIONE%>.value.length==0
             && document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_NUM_GIORNI_RECLUSIONE%>.value.length==0
             && document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_INTERO_IMPORTO_MULTA%>.value.length==0
             && document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_NUM_ANNI_ARRESTO%>.value.length==0
             && document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_NUM_MESI_ARRESTO%>.value.length==0
             && document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_NUM_GIORNI_ARRESTO%>.value.length==0
             && document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_INTERO_IMPORTO_AMMENDA%>.value.length==0
            )
        {
          alert('Almeno un campo della sezione pena deve essere valorizzato');
          document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_NUM_ANNI_RECLUSIONE%>.focus();

          return false;
        }
      }

      return true;
    }

    //==========================================================================
    // Abilita/Disabilita i campi in funzione del FLAG ERGASTOLO
    //==========================================================================
    function cambia()
    {
      if(document.LoadInserisciPenaMauale.<%=ICostantiPenaResidua.CAMPO_FLAG_ERGASTOLO%>.checked ==true)
      {
        document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_NUM_ANNI_RECLUSIONE%>.disabled=true;
        document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_NUM_MESI_RECLUSIONE%>.disabled=true;
        document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_NUM_GIORNI_RECLUSIONE%>.disabled=true;
        document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_NUM_ANNI_ARRESTO%>.disabled=true;
        document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_NUM_MESI_ARRESTO%>.disabled=true;
        document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_NUM_GIORNI_ARRESTO%>.disabled=true;
        document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_INTERO_IMPORTO_MULTA%>.disabled=true;
        document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_INTERO_IMPORTO_AMMENDA%>.disabled=true;
        document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_DECIMALE_IMPORTO_MULTA%>.disabled=true;
        document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_DECIMALE_IMPORTO_AMMENDA%>.disabled=true;
        //document.LoadInserisciPenaMauale.LibAntGiorni.disabled=true;
      }
      else
      {
        document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_NUM_ANNI_RECLUSIONE%>.disabled=false;
        document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_NUM_MESI_RECLUSIONE%>.disabled=false;
        document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_NUM_GIORNI_RECLUSIONE%>.disabled=false;
        document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_NUM_ANNI_ARRESTO%>.disabled=false;
        document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_NUM_MESI_ARRESTO%>.disabled=false;
        document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_NUM_GIORNI_ARRESTO%>.disabled=false;
        document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_INTERO_IMPORTO_MULTA%>.disabled=false;
        document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_INTERO_IMPORTO_AMMENDA%>.disabled=false;
        document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_DECIMALE_IMPORTO_MULTA%>.disabled=false;
        document.LoadInserisciPenaMauale.<%=ICostantiPenaComplessiva.CAMPO_DECIMALE_IMPORTO_AMMENDA%>.disabled=false;
        //document.LoadInserisciPenaMauale.LibAntGiorni.disabled=false;
      }
    }
  </script>
</head>
  <body class="corpo">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
          <font class="label">Funzione :</font>&nbsp;&nbsp;
<%
            String lAzione = new String();
            lAzione = "siap.siep.penaresidua.action.ActInserisciPenaResiduaManuale";
%>
            <font class="campo">Inserimento Pena Residua Manuale</font>
        </td>
      </tr>
    </table>
    <br>
      <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    <br>
    <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciPenaMauale">
      <table cellspacing=4 cellpadding=4>
        <tr>
          <td class=l colspan=2>Posizione Giuridica : <font class="campo"><%=PosizioneGiuridica.getDescrPosizioneGiuridica()%></font></td>
        </tr>
        <tr><td>&nbsp;</td></tr>
        <tr><td class="Titolo" colspan=7>Pena Residua</td></tr>
        <tr>
          <td class="l" width="25%">Reclusione</td>
          <td class="l">
            <%
            CalendarModel lRecPenaF5 = PenaModelDaF5.getQuantumReclusione();
            
            if (CalendarUtil.getTotGiorni(lRecPenaF5)>0)
            {
            %>
            Anni&nbsp;  <input Title="Anni Reclusione"   value="<%=StringUtils.toStringJSP(PenaModelDaF5.getNumAnniReclusione(), "")%>"   type="text" name="<%=ICostantiPenaComplessiva.CAMPO_NUM_ANNI_RECLUSIONE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
            Mesi&nbsp;  <input Title="Mesi Reclusione"   value="<%=StringUtils.toStringJSP(PenaModelDaF5.getNumMesiReclusione(), "")%>"   type="text" name="<%=ICostantiPenaComplessiva.CAMPO_NUM_MESI_RECLUSIONE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
            Giorni&nbsp;<input Title="Giorni Reclusione" value="<%=StringUtils.toStringJSP(PenaModelDaF5.getNumGiorniReclusione(), "")%>" type="text" name="<%=ICostantiPenaComplessiva.CAMPO_NUM_GIORNI_RECLUSIONE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
            <% 
            } else {
            %>
            Anni&nbsp;  <input Title="Anni Reclusione"   value="" type="text" name="<%=ICostantiPenaComplessiva.CAMPO_NUM_ANNI_RECLUSIONE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
            Mesi&nbsp;  <input Title="Mesi Reclusione"   value="" type="text" name="<%=ICostantiPenaComplessiva.CAMPO_NUM_MESI_RECLUSIONE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
            Giorni&nbsp;<input Title="Giorni Reclusione" value="" type="text" name="<%=ICostantiPenaComplessiva.CAMPO_NUM_GIORNI_RECLUSIONE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
            <% } %>
          </td>
          <td class="l">Multa</td>
          <td class="l" width="25%">
            <%
            if (PenaModelDaF5.getImportoMulta()!=null && PenaModelDaF5.getImportoMulta().compareTo(new BigDecimal("0"))!=0) {
            %>
            <input Title="Multa" size=7 maxlength=7 value="<%=StringUtils.getParteIntera   (PenaModelDaF5.getImportoMulta())%>" type="text" name="<%=ICostantiPenaComplessiva.CAMPO_INTERO_IMPORTO_MULTA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
            ,
            <input Title="Multa" size=2 maxlength=2 value="<%=StringUtils.getParteDecimale (PenaModelDaF5.getImportoMulta())%>" type="text" name="<%=ICostantiPenaComplessiva.CAMPO_DECIMALE_IMPORTO_MULTA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
            <% } else { %>
            <input Title="Multa" size=7 maxlength=7 value="" type="text" name="<%=ICostantiPenaComplessiva.CAMPO_INTERO_IMPORTO_MULTA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
            ,
            <input Title="Multa" size=2 maxlength=2 value="" type="text" name="<%=ICostantiPenaComplessiva.CAMPO_DECIMALE_IMPORTO_MULTA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
            <% } %>
            <select name="<%=ICostantiPenaComplessiva.CAMPO_VALUTA_IMPORTO_MULTA%>">
              <%=valute%>
            </select>
          </td>
        </tr>
        <tr>
          <td class="l">Arresto</td>
          <td class="l">
            <%
            CalendarModel lArrPenaF5 = PenaModelDaF5.getQuantumArresto();
            if (CalendarUtil.getTotGiorni(lArrPenaF5)>0)
            {
            %>
            Anni&nbsp;  <input Title="Anni Arresto"   value="<%=StringUtils.toStringJSP(PenaModelDaF5.getNumAnniArresto(), "")%>"   type="text" name="<%=ICostantiPenaComplessiva.CAMPO_NUM_ANNI_ARRESTO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
            Mesi&nbsp;  <input Title="Mesi Arresto"   value="<%=StringUtils.toStringJSP(PenaModelDaF5.getNumMesiArresto(), "")%>"   type="text" name="<%=ICostantiPenaComplessiva.CAMPO_NUM_MESI_ARRESTO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
            Giorni&nbsp;<input Title="Giorni Arresto" value="<%=StringUtils.toStringJSP(PenaModelDaF5.getNumGiorniArresto(), "")%>" type="text" name="<%=ICostantiPenaComplessiva.CAMPO_NUM_GIORNI_ARRESTO%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
            <% 
            } else {
            %>
            Anni&nbsp;  <input Title="Anni Arresto"   value="" type="text" name="<%=ICostantiPenaComplessiva.CAMPO_NUM_ANNI_ARRESTO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
            Mesi&nbsp;  <input Title="Mesi Arresto"   value="" type="text" name="<%=ICostantiPenaComplessiva.CAMPO_NUM_MESI_ARRESTO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
            Giorni&nbsp;<input Title="Giorni Arresto" value="" type="text" name="<%=ICostantiPenaComplessiva.CAMPO_NUM_GIORNI_ARRESTO%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
            <% } %>
          </td>
          <td class="l">Ammenda</td>
          <td class="l">
            <%
            if (PenaModelDaF5.getImportoAmmenda()!=null && PenaModelDaF5.getImportoAmmenda().compareTo(new BigDecimal("0"))!=0) {
            %>
            <input Title="Ammenda" size=7 maxlength=7 value="<%=StringUtils.getParteIntera   (PenaModelDaF5.getImportoAmmenda())%>" type="text" name="<%=ICostantiPenaComplessiva.CAMPO_INTERO_IMPORTO_AMMENDA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
            ,
            <input Title="Ammenda" size=2 maxlength=2 value="<%=StringUtils.getParteDecimale (PenaModelDaF5.getImportoAmmenda())%>" type="text" name="<%=ICostantiPenaComplessiva.CAMPO_DECIMALE_IMPORTO_AMMENDA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
            <% } else { %>
            <input Title="Ammenda" size=7 maxlength=7 value="" type="text" name="<%=ICostantiPenaComplessiva.CAMPO_INTERO_IMPORTO_AMMENDA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
            ,
            <input Title="Ammenda" size=2 maxlength=2 value="" type="text" name="<%=ICostantiPenaComplessiva.CAMPO_DECIMALE_IMPORTO_AMMENDA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
            <% } %>
            <select name="<%=ICostantiPenaComplessiva.CAMPO_VALUTA_IMPORTO_AMMENDA%>">
              <%=valute%>
            </select>
          </td>
        </tr>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--          
<tr>
  <td class=l>
    <font class="label">Totale Liberazione Anticipata</font>
  </td>
  <td class=l>
    <% if (!LibAntGiorni.equals("")) { %>
    Giorni  <input type=text size=3 name="LibAntGiorni" value="<%=LibAntGiorni%>">
    <% } else { %>
    Giorni  <input type=text size=3 name="LibAntGiorni" value="0">
    <% } %>
  </td>
</tr>
 --%>        
     </table>
<!-- 
// 20/05/2014 - Nuova Ordinanza L.A. - >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Inseriti in questa form  i gg. di L.A. , L.A. Speciale, e di Integrazione L.A.
 -->     
  <table>
    <tr>
      <td class=l>
        <font class="label"> Totale Liberazione Anticipata </font>
      </td>
      <td class=l>&nbsp;&nbsp;
        Giorni&nbsp;  <input type=text size="4" maxlength="4" name="<%=ICostantiPenaResidua.CAMPO_NUM_GIORNI_LIBER_ANT_ORDINARIA%>" value="" 
                             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
      </td>
    </tr>
    <tr>
      <td class=l>
        <font class="label"> Totale Liberazione Anticipata Speciale</font>
      </td>
      <td class=l>&nbsp;&nbsp;
        Giorni&nbsp;  <input type=text size="4"  maxlength="4" name="<%=ICostantiPenaResidua.CAMPO_NUM_GIORNI_LIBER_ANT_SPECIALE%>" value="" 
                             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
      </td>
    </tr>
    <tr>
      <td class=l>
        <font class="label"> Totale Integrazione Liberazione Anticipata</font>
      </td>
      <td class=l>&nbsp;&nbsp;
        Giorni&nbsp;  <input type=text size="4" maxlength="4" name="<%=ICostantiPenaResidua.CAMPO_NUM_GIORNI_LIBER_ANT_INTEGRAZIONE %>" value="" 
                             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
      </td>
    </tr>  
    
    <tr>
      <td class=l>
        <font class="label"> Totale Riduzione pena Risarcimento Danni</font>
      </td>
      <td class=l>&nbsp;&nbsp;
        Giorni&nbsp;  <input type="text" size="4" maxlength="4" value=""
                             name="<%=ICostantiPenaResidua.CAMPO_NUM_GIORNI_RISARCIMENTO_DANNI_DL92 %>"
                             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  >
      </td>
    </tr>  
                        
  </table>
<!--    End Nuova Ordinanza L.A.   --> 
     
      <table>
        <tr>
          <td class=l>
            <font class="label">Data Decorrenza Pena </font>
          </td>
          <td class=l>
          <% if (PenaModelDaF5.getDataInizio() != null) {%>
            <input type=text size=2 maxlength="2" name=<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_DECORRENZA_PENA%> value="<%=DateUtils.getDayToString   (PenaModelDaF5.getDataInizio())%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">-
            <input type=text size=2 maxlength="2" name=<%=ICostantiPenaResidua.CAMPO_MESE_DATA_DECORRENZA_PENA%>   value="<%=DateUtils.getMonthToString (PenaModelDaF5.getDataInizio())%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">-
            <input type=text size=4 maxlength="4" name=<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_DECORRENZA_PENA%>   value="<%=DateUtils.getYearToString  (PenaModelDaF5.getDataInizio())%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
          <%} else {%>
            <input type=text size=2 maxlength="2" name=<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_DECORRENZA_PENA%> value="" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">-
            <input type=text size=2 maxlength="2" name=<%=ICostantiPenaResidua.CAMPO_MESE_DATA_DECORRENZA_PENA%>   value="" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">-
            <input type=text size=4 maxlength="4" name=<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_DECORRENZA_PENA%>   value="" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
          <%}%>
          </td>
        </tr>
        
        <tr>
          <td class=l>
            <font class="label">Data Fine Reclusione </font>
          </td>
          <td class=l>
          <% if (PenaModelDaF5.getDataFineReclusione() != null) {%>
            <input type=text size=2 maxlength="2" name=<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE_RECLUSIONE%> value="<%=DateUtils.getDayToString   (PenaModelDaF5.getDataFineReclusione())%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">-
            <input type=text size=2 maxlength="2" name=<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE_RECLUSIONE%>   value="<%=DateUtils.getMonthToString (PenaModelDaF5.getDataFineReclusione())%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">-
            <input type=text size=4 maxlength="4" name=<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE_RECLUSIONE%>   value="<%=DateUtils.getYearToString  (PenaModelDaF5.getDataFineReclusione())%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
          <%} else {%>
            <input type=text size=2 maxlength="2" name=<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE_RECLUSIONE%> value="" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">-
            <input type=text size=2 maxlength="2" name=<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE_RECLUSIONE%> value="" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">-
            <input type=text size=4 maxlength="4" name=<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE_RECLUSIONE%> value="" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
          <%}%>
          </td>
          <td class=l>
            <font class="label">Data Inizio Arresto </font>
          </td>
          <td class=l>
          <% if (PenaModelDaF5.getDataInizioArresto() != null) {%>
            <input type=text size=2 maxlength="2" name=<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_INIZIO_ARRESTO%> value="<%=DateUtils.getDayToString   (PenaModelDaF5.getDataInizioArresto())%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">-
            <input type=text size=2 maxlength="2" name=<%=ICostantiPenaResidua.CAMPO_MESE_DATA_INIZIO_ARRESTO%>   value="<%=DateUtils.getMonthToString (PenaModelDaF5.getDataInizioArresto())%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">-
            <input type=text size=4 maxlength="4" name=<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_INIZIO_ARRESTO%>   value="<%=DateUtils.getYearToString  (PenaModelDaF5.getDataInizioArresto())%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
          <%} else {%>
            <input type=text size=2 maxlength="2" name=<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_INIZIO_ARRESTO%> value="" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">-
            <input type=text size=2 maxlength="2" name=<%=ICostantiPenaResidua.CAMPO_MESE_DATA_INIZIO_ARRESTO%>   value="" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">-
            <input type=text size=4 maxlength="4" name=<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_INIZIO_ARRESTO%>   value="" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
          <%}%>
          </td>
        </tr>
        
        <tr>
          <td class=l>
            <font class="label">Data Fine Pena </font>
          </td>
          <td class=l>
          <% if (PenaModelDaF5.getDataFine() != null) {%>
            <input type=text size=2 maxlength="2" name=<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%> value="<%=DateUtils.getDayToString   (PenaModelDaF5.getDataFine())%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">-
            <input type=text size=2 maxlength="2" name=<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>   value="<%=DateUtils.getMonthToString (PenaModelDaF5.getDataFine())%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">-
            <input type=text size=4 maxlength="4" name=<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>   value="<%=DateUtils.getYearToString  (PenaModelDaF5.getDataFine())%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
          <%} else {%>
            <input type=text size=2 maxlength="2" name=<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%> value="" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">-
            <input type=text size=2 maxlength="2" name=<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%> value="" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">-
            <input type=text size=4 maxlength="4" name=<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%> value="" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
          <%}%>
          </td>
        </tr>
        
        <tr>
          <td class=l>
            <font class="label">Ergastolo </font>
          </td>
          <td class="l">
            <input type="checkbox" name="<%=ICostantiPenaResidua.CAMPO_FLAG_ERGASTOLO%>" value="S" onchange="cambia();">
          </td>
        </tr>
      </table>
      
      <table>
        <tr>
          <td class="l">Data Inizio Isolamento Diurno</td>
          <td class="l">
            <input Title="Giorno Data Inizio Isol." type="text" value="" name="<%=ICostantiPenaComplessiva.CAMPO_GIORNO_DATA_INIZIO_ISOLAMENTO_DIURNO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
            -
            <input Title="Mese Data Inizio Isol." type="text" value="" name="<%=ICostantiPenaComplessiva.CAMPO_MESE_DATA_INIZIO_ISOLAMENTO_DIURNO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
            -
            <input Title="Anno Data Inizio Isol." type="text" value="" name="<%=ICostantiPenaComplessiva.CAMPO_ANNO_DATA_INIZIO_ISOLAMENTO_DIURNO%>"maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
          </td>
          <td class="l">Data Fine Isolamento Diurno</td>
          <td class="l">
            <input Title="Giorno Data Inizio Isol." type="text" value="" name="<%=ICostantiPenaComplessiva.CAMPO_GIORNO_DATA_FINE_ISOLAMENTO_DIURNO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
            -
            <input Title="Mese Data Inizio Isol." type="text" value="" name="<%=ICostantiPenaComplessiva.CAMPO_MESE_DATA_FINE_ISOLAMENTO_DIURNO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
            -
            <input Title="Anno Data Inizio Isol." type="text" value="" name="<%=ICostantiPenaComplessiva.CAMPO_ANNO_DATA_FINE_ISOLAMENTO_DIURNO%>"maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
          </td>
        </tr>

        <tr>
          <td class="l"> Isolamento Diurno</td>
          <td class="l">Anni
            <input Title="Anni Isolamento Diurno" type="text" value="" name="<%=ICostantiPenaComplessiva.CAMPO_NUM_ANNI_ISOLAMENTO_DIURNO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
            Mesi
            <input Title="Mesi Isolamento Diurno" type="text" value="" name="<%=ICostantiPenaComplessiva.CAMPO_NUM_MESI_ISOLAMENTO_DIURNO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
            Giorni
            <input Title="Giorni Isolamento Diurno" type="text" value="" name="<%=ICostantiPenaComplessiva.CAMPO_NUM_GIORNI_ISOLAMENTO_DIURNO%>"maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" > 
          </td>
        </tr>
      </table>
      
      
      <table>
        <tr><td>&nbsp;</td></tr>
        <tr>
          <td class="l">
            <input type="radio" name="tipo" value="I" >
          Pena Interrotta &nbsp;</td>
          <td class="l">
            <input type="radio" name="tipo" value="D" >
          Pena Differita &nbsp;</td>
          <td class="l">
            <input type="radio" name="tipo" value="S" >
          Pena Sospesa &nbsp;</td>
          <td class="l">
            <input type="radio" name="tipo" value="E" >
          Espulso &nbsp;</td>
          <td class="l">
            <input type="radio" name="tipo" value=""  checked>
          Pena Validata &nbsp;</td>
        </tr>
        <tr>
          <td colspan=2>
            <br>
            <input class="bottone" type="submit" name="INSERISCI" value="Conferma">
          </td>
        </tr>
      </table>
      
      <input type="HIDDEN" name="Action" value="<%=lAzione%>" >
 </form>
 
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInserisciPenaMauale");

  frmvalidator.addValidation("<%=ICostantiPenaComplessiva.CAMPO_NUM_ANNI_RECLUSIONE%>","numeric");
  frmvalidator.addValidation("<%=ICostantiPenaComplessiva.CAMPO_NUM_MESI_RECLUSIONE%>","numeric");
  frmvalidator.addValidation("<%=ICostantiPenaComplessiva.CAMPO_NUM_GIORNI_RECLUSIONE%>","numeric");
  frmvalidator.addValidation("<%=ICostantiPenaComplessiva.CAMPO_INTERO_IMPORTO_MULTA%>","numeric");
  frmvalidator.addValidation("<%=ICostantiPenaComplessiva.CAMPO_DECIMALE_IMPORTO_MULTA%>","numeric");
  frmvalidator.addValidation("<%=ICostantiPenaComplessiva.CAMPO_NUM_ANNI_ARRESTO%>","numeric");
  frmvalidator.addValidation("<%=ICostantiPenaComplessiva.CAMPO_NUM_MESI_ARRESTO%>","numeric");
  frmvalidator.addValidation("<%=ICostantiPenaComplessiva.CAMPO_NUM_GIORNI_ARRESTO%>","numeric");
  frmvalidator.addValidation("<%=ICostantiPenaComplessiva.CAMPO_INTERO_IMPORTO_AMMENDA%>","numeric");
  frmvalidator.addValidation("<%=ICostantiPenaComplessiva.CAMPO_DECIMALE_IMPORTO_AMMENDA%>","numeric");

  frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
</html>