<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>

<%@ page import="siap.siep.penaresidua.model.PenaResiduaModel" %>

<jsp:useBean id="PenaResidua"    scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel" />
<jsp:useBean id="DateIntermedie" scope="request" class="java.lang.String" />
<jsp:useBean id="MustConfirm"    scope="request" class="java.lang.String" />

<jsp:useBean id="PenaCumuloMod"  scope="request" class="siap.siep.penacumulo.model.PenaCumuloModel" />

<%
//==============================================================================
// Form invocata due volte
// - una prima volta in seguito al calcolo della pena per chiedere conferma
//   del finepena calcolato
// - una seconda volta subito dopo la conferma per visualizzare i dati confermati
// Nel primo caso non viene passato il parametro MustConfirm, nel secondo
// caso viene passato con valore 'no'
//==============================================================================

%>


<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
    <script language="JavaScript">
    function Verify()
    {
    <% if(PenaResidua != null && PenaResidua.getDataFinePresunta() != null) {%>
      if(document.f.GPV.value!="" && document.f.MPV.value!="" && document.f.APV.value!="")
      {
        if (document.f.GPV.value.length==1)
          document.f.GPV.value='0'+document.f.GPV.value;
        if (document.f.MPV.value.length==1)
          document.f.MPV.value='0'+document.f.MPV.value;

        var data_to_verify = document.f.GPV.value+'/'+document.f.MPV.value+'/'+document.f.APV.value;

        if (data_to_verify.length>4)
        {
          if (!ControllaData(data_to_verify) ) {
            alert('Data Fine Pena non valida');
            return false;
          }
          else
            return true;
        }
      }
      else
        return true;
     <%}%>
    }
    
    // richiamata funzione "Richiesta Applicazione Benefici"
    function RichiestaAB()
    {
     	document.f.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.cumulo.action.ActLoadRichiestaApplicazioneBenefici";
   		document.f.submit();
    }

    </script>
    <title>[S.I.E.S.] - Calcolo Pena</title>
  </head>

<body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class=LBG>
        <font  class="label">Funzione :&nbsp;</font><font class="campo">Validazione Pena Cumulo</font>
      </td>
      <td class="LBG">
        <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>" />
      </td>
    </tr>
  </table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>


<%
//==============================================================================
//       Sezione con i quantum imputati in cumulo (solo se non ergastolo)
//==============================================================================
%>
<%if(    PenaResidua.getFlagErgastolo() != null
     && !PenaResidua.getFlagErgastolo().equals("S")
     && !PenaResidua.getFlagErgastolo().equals("D")
    )
{%>
  <table>
    <tr>
      <td width=100% colspan="2">
        <table width=100%>
          <tr>
            <td class="Titolo" colspan=9><font  class="label">Pena Residua</font></td>
          </tr>
<%
          if (   PenaResidua.getNumAnniReclusione().intValue()!=0   && PenaResidua.getNumMesiReclusione().intValue()!=0
              && PenaResidua.getNumGiorniReclusione().intValue()!=0 && PenaResidua.getNumAnniArresto().intValue()!=0
              && PenaResidua.getNumMesiArresto().intValue()!=0      && PenaResidua.getNumGiorniArresto().intValue()!=0
             ) // che rappresenta questa condizione????
          {
%>
          <tr>
            <td class="l"><font  class="label">Reclusione / Multa : </font></td>
            <td class="l"><font class="label">Anni</font></td>
            <td class="lRosso"><font class="lRosso"><%=PenaResidua.getNumAnniReclusione()%></font></td>
            <td class="l"><font class="label">Mesi</font></td>
            <td class="lRosso"><font class="lRosso"><%=PenaResidua.getNumMesiReclusione()%></font></td>
            <td class="l"><font class="label">Giorni</font></td>
            <td class="lRosso"><font class="lRosso"><%=PenaResidua.getNumGiorniReclusione()%></font></td>
            <td class="l"><font class="label">Importo</font></td>
            <td class="lRosso"><font class="lRosso"><%=StringUtils.toEuroFormat(PenaResidua.getImportoMulta())%></font></td>
          </tr>
          <tr>
            <td class="l"><font  class="label">Arresto / Ammenda :</font></td>
            <td class="l"><font class="label">Anni</font></td>
            <td class="lRosso"><font class="lRosso"><%=PenaResidua.getNumAnniArresto()%></font></td>
            <td class="l"><font class="label">Mesi</font></td>
            <td class="lRosso"><font class="lRosso"><%=PenaResidua.getNumMesiArresto()%></font></td>
            <td class="l"><font class="label">Giorni</font></td>
            <td class="lRosso"><font class="lRosso"><%=PenaResidua.getNumGiorniArresto()%></font></td>
            <td class="l"><font class="label">Importo</font></td>
            <td class="lRosso"><font class="lRosso"><%=StringUtils.toEuroFormat(PenaResidua.getImportoAmmenda())%></font></td>
          </tr>
          <%} else {%>
          <tr>
            <td class="l"><font  class="label">Reclusione / Multa : </font></td>
            <td class="l"><font class="label">Anni</font></td>
            <td class="r"><font class="campo"><%=PenaResidua.getNumAnniReclusione()%></font></td>
            <td class="l"><font class="label">Mesi</font></td>
            <td class="r"><font class="campo"><%=PenaResidua.getNumMesiReclusione()%></font></td>
            <td class="l"><font class="label">Giorni</font></td>
            <td class="r"><font class="campo"><%=PenaResidua.getNumGiorniReclusione()%></font></td>
            <td class="l"><font class="label">Importo</font></td>
            <td class="r"><font class="campo"><%=StringUtils.toEuroFormat(PenaResidua.getImportoMulta())%></font></td>
          </tr>
          <tr>
            <td class="l"><font  class="label">Arresto / Ammenda :</font></td>
            <td class="l"><font class="label">Anni</font></td>
            <td class="r"><font class="campo"><%=PenaResidua.getNumAnniArresto()%></font></td>
            <td class="l"><font class="label">Mesi</font></td>
            <td class="r"><font class="campo"><%=PenaResidua.getNumMesiArresto()%></font></td>
            <td class="l"><font class="label">Giorni</font></td>
            <td class="r"><font class="campo"><%=PenaResidua.getNumGiorniArresto()%></font></td>
            <td class="l"><font class="label">Importo</font></td>
            <td class="r"><font class="campo"><%=StringUtils.toEuroFormat(PenaResidua.getImportoAmmenda())%></font></td>
          </tr>
          <%}%>
        </table>
      </td>
    </tr>
  </table>
<%}%>

<FORM method="POST" name="f" action="<%= IWebConstants.PG_MAIN%>">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.cumulo.action.ActInserisciPenaValidataCumulo">

  <input type="HIDDEN" name="IdPenaResidua" value="<%=PenaResidua.getIdPenaResidua()%>">
<%
  if (   PenaCumuloMod!=null && PenaCumuloMod.getIdPenaCumulo()!=null )
  {
%>
    <input type="HIDDEN" name="IdPenaCumulo" value="<%=PenaCumuloMod.getIdPenaCumulo()%>">
<%
  }

  if(PenaResidua != null && PenaResidua.getDataInizio() != null)
  {
%>
    <input type="HIDDEN" name="Gdatainiziopena" value="<%=DateUtils.getDayToString(PenaResidua.getDataInizio())%>">
    <input type="HIDDEN" name="Mdatainiziopena" value="<%=DateUtils.getMonthToString(PenaResidua.getDataInizio())%>">
    <input type="HIDDEN" name="Adatainiziopena" value="<%=DateUtils.getYearToString(PenaResidua.getDataInizio())%>">
<%
  }

  if(PenaResidua != null && PenaResidua.getDataFinePresunta() != null)
  {
%>
    <input type="HIDDEN" name="Gdatafinepenapresunta" value="<%=DateUtils.getDayToString(PenaResidua.getDataFinePresunta())%>">
    <input type="HIDDEN" name="Mdatafinepenapresunta" value="<%=DateUtils.getMonthToString(PenaResidua.getDataFinePresunta())%>">
    <input type="HIDDEN" name="Adatafinepenapresunta" value="<%=DateUtils.getYearToString(PenaResidua.getDataFinePresunta())%>">
<%
  }
%>
  <table>
    <tr>
<%
      if(PenaResidua != null && PenaResidua.getDataInizio() != null)
      {
%>
        <td class="l">Data Decorrenza Pena: </td>
        <td class="l">
          <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(PenaResidua.getDataInizio(),"dd-MM-yyyy"))%></font>
        </td>
<%
      }

      if (PenaResidua.getFlagErgastolo() != null && (PenaResidua.getFlagErgastolo().equals("S") || PenaResidua.getFlagErgastolo().equals("D")))
      {
%>
        <td class="l">Data Fine Pena: </td>
        <td class="lRosso">
          <font class="lRosso"> MAI </font>
        </td>
      </tr>
<%
      }
      else
      {
         if (!DateIntermedie.equals("no") )
         { // presenti fine reclusione e inizio arresto
           if(PenaResidua.getDataFineReclusione() != null)
           {%>
           <input type="HIDDEN" name="Gdatafinereclusione" value="<%=DateUtils.getDayToString(PenaResidua.getDataFineReclusione())%>">
           <input type="HIDDEN" name="Mdatafinereclusione" value="<%=DateUtils.getMonthToString(PenaResidua.getDataFineReclusione())%>">
           <input type="HIDDEN" name="Adatafinereclusione" value="<%=DateUtils.getYearToString(PenaResidua.getDataFineReclusione())%>">

           <td class="l"><font  class="label">Data Fine Reclusione : </font></td>
           <td class="l">
             <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(PenaResidua.getDataFineReclusione(),"dd-MM-yyyy"))%> </font>
           </td>
          </tr>
<%
          }
%>
          <tr>
<%
           if(PenaResidua.getDataInizioArresto() != null)
           {
%>
             <input type="HIDDEN" name="Gdatainizioarresto" value="<%=DateUtils.getDayToString(PenaResidua.getDataInizioArresto())%>">
             <input type="HIDDEN" name="Mdatainizioarresto" value="<%=DateUtils.getMonthToString(PenaResidua.getDataInizioArresto())%>">
             <input type="HIDDEN" name="Adatainizioarresto" value="<%=DateUtils.getYearToString(PenaResidua.getDataInizioArresto())%>">

             <td class="l"><font  class="label">Data Inizio Arresto : </font></td>
             <td class="l">
              <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(PenaResidua.getDataInizioArresto(),"dd-MM-yyyy"))%></font>
             </td>
<%
           }
         }  // fine date intermedie

         if (!MustConfirm.equals("no"))
         {
           if(PenaResidua != null && PenaResidua.getDataFinePresunta() != null)
           {
%>
             <td class="l"><font  class="label">Data Fine Pena Automatica : </font></td>
             <td class="l">
               <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(PenaResidua.getDataFinePresunta(),"dd-MM-yyyy"),"-")%></font>
             </td>
           </tr>
<%
           }
         }

         if (MustConfirm.equals("no"))
         { // sto visualizzando il dettaglio DOPO la conferma del fine pena
           if(PenaResidua != null && PenaResidua.getDataFine() != null)
           {
           %>
           <tr>
             <td class="l"><font  class="label">Data Fine Pena: </font></td>
             <td class="l">
              &nbsp;&nbsp;&nbsp;
               <font  class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(PenaResidua.getDataFine(),"dd-MM-yyyy"),"-")%></font>
             </td>
           </tr>
          <%}%>
         
	    <tr><td>&nbsp;</td></tr>
	    <tr>
	      <td class="l"><INPUT class="bottone" type="button" name="richiestaAB" value="Richiesta Applicazione Benefici" onClick="Javascript:RichiestaAB();"></td>
	    </tr>

<%       }
         else
         { // sto ancora in fase di conferma, visualizzo l campo imput del
           // fine pena manuale (se presente un fine pena calcolato)
           if(PenaResidua != null && PenaResidua.getDataFinePresunta() != null)
           {%>
           <tr>
            <td class="l"colspan=4><font  class="label">Data Fine Pena Manuale : </font>
              <input type="text" name="GPV" maxlength="2" size="2" value="<%=DateUtils.getDayToString(PenaResidua.getDataFinePresunta())%>">
              /
              <input type="text" name="MPV" maxlength="2" size="2" value="<%=DateUtils.getMonthToString(PenaResidua.getDataFinePresunta())%>">
              /
              <input  type="text" name="APV" maxlength="4" size="4" value="<%=DateUtils.getYearToString(PenaResidua.getDataFinePresunta())%>">
              &nbsp;&nbsp;&nbsp;</td>
           </tr>
<%
           }
        }
      } // end non ergastolo
%>
<%

//==============================================================================
//             Se sono presenti LA detratte o da detrarre le visualizzo
//==============================================================================
// 20/05/2014 Nuova L.A.  Decreto 2013/146  : Distinguo i gg di L.A, di L.A. Speciale, Integrazione L.A.
//=============================================================================================================
  if (   PenaCumuloMod!=null && PenaCumuloMod.getIdPenaCumulo()!=null
      && (   (PenaCumuloMod.getNumGiorniLibAnticipata()!=null && PenaCumuloMod.getNumGiorniLibAnticipata().intValue()>0)
          || (PenaCumuloMod.getNumGiorniRiduzionePena()!=null && PenaCumuloMod.getNumGiorniRiduzionePena().intValue()>0)
         )
     )
  {
      if(PenaResidua != null && PenaResidua.getDataInizio() != null)
      {
%>
          <tr>
            <td class="L" colspan="4">
              <font class="label">Liberazione Anticipata Concessa già detratta in giorni:</font>&nbsp;
              <font class="campo"><%=PenaCumuloMod.getNumGiorniLibAnticipataLA()%></font>
            </td>
          </tr>
          <tr>
            <td class="L" colspan="4">
              <font class="label">Liberazione Anticipata Speciale Concessa già detratta in giorni:</font>&nbsp;
              <font class="campo"><%=PenaCumuloMod.getNumGiorniLibAnticipataSPE()%></font>
            </td>
          </tr>
          <tr>
            <td class="L" colspan="4">
              <font class="label">Integrazione Liberazione Anticipata Concessa già detratta in giorni:</font>&nbsp;
              <font class="campo"><%=PenaCumuloMod.getNumGiorniLibAnticipataINT()%></font>
            </td>
          </tr>
          <tr>
            <td class="L" colspan="4">
              <font class="label">Riduzione pena per risarcimento danni Concessa già detratta in giorni:</font>&nbsp;
              <font class="campo"><%=PenaCumuloMod.getNumGiorniRiduzionePena()%></font>
            </td>
          </tr>
<%
      }
      else
      {
%>
          <tr>
            <td class="L" colspan="4">
              <font class="label">Liberazione Anticipata Concessa da detrarre in giorni:</font>&nbsp;
              <font class="cVerde"><%=PenaCumuloMod.getNumGiorniLibAnticipataLA()%></font>
            </td>
          </tr>
          <tr>
            <td class="L" colspan="4">
              <font class="label">Liberazione Anticipata Speciale Concessa da detrarre in giorni:</font>&nbsp;
              <font class="cVerde"><%=PenaCumuloMod.getNumGiorniLibAnticipataSPE()%></font>
            </td>
          </tr>
          <tr>
            <td class="L" colspan="4">
              <font class="label">Integrazione Liberazione Anticipata Concessa da detrarre in giorni:</font>&nbsp;
              <font class="cVerde"><%=PenaCumuloMod.getNumGiorniLibAnticipataINT()%></font>
            </td>
          </tr>
          <tr>
            <td class="L" colspan="4">
              <font class="label">Riduzione pena per risarcimento danni Concessa da detrarre in giorni:</font>&nbsp;
              <font class="cVerde"><%=PenaCumuloMod.getNumGiorniRiduzionePena()%></font>
            </td>
          </tr>
<%
      }
  }

  if ( !(PenaResidua.getFlagErgastolo() != null
        && (PenaResidua.getFlagErgastolo().equals("S")
            || PenaResidua.getFlagErgastolo().equals("D")))
      && !(MustConfirm.equals("no")) )
  {
     if(PenaResidua != null && PenaResidua.getDataFinePresunta() != null)
     {
%>
    <tr><td>&nbsp;</td></tr>
    <tr>
      <td class="l"><INPUT class="bottone" type="submit" name="conferma" value="Validazione Fine Pena"></td>
    </tr>
<%
    }
  }
%>
    </table>
    </form>
  </body>
<%
  if(PenaResidua.getFlagErgastolo() != null && !PenaResidua.getFlagErgastolo().equals("S") && !PenaResidua.getFlagErgastolo().equals("D"))
  {
    if (!MustConfirm.equals("no"))
    {
      if(PenaResidua != null && PenaResidua.getDataFinePresunta() != null)
      {
%>
        <script language="JavaScript" type="text/javascript">

          var frmvalidator  = new Validator("f");

          frmvalidator.addValidation("GPV","maxlen=2","La lunghezza massima per il Giorno Pena Validata è di 2 caratteri");
          frmvalidator.addValidation("GPV","numeric","Il campo Giorno Pena Validata deve essere numerico");
          frmvalidator.addValidation("GPV","gt=1","Il campo Giorno Pena Validata deve essere maggiore di 0");
          frmvalidator.addValidation("GPV","lt=31","Il campo Giorno Pena Validata deve essere minore di 31");
          frmvalidator.addValidation("MPV","maxlen=2","La lunghezza massima per il Mese Pena Validata è di 2 caratteri");
          frmvalidator.addValidation("MPV","numeric","Il campo Mese Pena Validata deve essere numerico");
          frmvalidator.addValidation("MPV","gt=1","Il campo Mese Pena Validata deve essere maggiore di 0");
          frmvalidator.addValidation("MPV","lt=12","Il campo Mese Pena Validata deve essere minore di 12");
          frmvalidator.addValidation("APV","maxlen=4","La lunghezza massima per l'Anno Pena Validata è di 4 caratteri");
          frmvalidator.addValidation("APV","minlen=4","La lunghezza minima per l'Anno Pena Validata è di 4 caratteri");
          frmvalidator.addValidation("APV","numeric","Il campo Anno Pena Validata deve essere numerico");
          frmvalidator.addValidation("APV","gt=1900","Il campo Anno Pena Validata deve essere maggiore di 1900");
          frmvalidator.addValidation("APV","lt=2100","Il campo Anno Pena Validata deve essere minore di 2100");
          frmvalidator.setAddnlValidationFunction("Verify");
        </script>
<%
    }
  }
}
%>
</html>