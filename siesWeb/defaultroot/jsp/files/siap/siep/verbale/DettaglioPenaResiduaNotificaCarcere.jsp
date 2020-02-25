<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siep.verbale.model.VerbaleModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel"%>

<jsp:useBean id="penaresidua"               scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="verbale"                   scope="request" class="siap.siep.verbale.model.VerbaleModel"/>
<jsp:useBean id="luogodetenzione"           scope="request" class="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"/>
<jsp:useBean id="istitutodetenzione"        scope="request" class="siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel"/>
<jsp:useBean id="vedoDataIntermedia"        scope="request" class="java.lang.String" />
<jsp:useBean id="DettaglioDaElenco"         scope="request" class="java.lang.String" />


<%
 // IstitutoDetenzioneModel lIstitutoDetenzione = luogodetenzione.getIstitutoDetenzione();

BigDecimal totalegiornilibanticipata = (BigDecimal) request.getAttribute("totalegiornilibanticipata");


IstitutoDetenzioneModel lIstitutoDetenzione = istitutodetenzione;

// if(lIstitutoDetenzione == null)
//   lIstitutoDetenzione = new IstitutoDetenzioneModel();
%>

<html>
<head>
<title>[S.I.E.S.] - Dettaglio Notifica Carcere </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
<script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
<script language="JavaScript" src="/html/conferma.js"></script>
<script language="JavaScript">
  function Verify()
	{
    if(document.DettaglioNotificaCarcereFinePena.GPV.value!="" && document.DettaglioNotificaCarcereFinePena.MPV.value!="" && document.DettaglioNotificaCarcereFinePena.APV.value!="")
    {
      if (document.DettaglioNotificaCarcereFinePena.GPV.value.length==1)
        document.DettaglioNotificaCarcereFinePena.GPV.value='0'+document.DettaglioNotificaCarcereFinePena.GPV.value;
      if (document.DettaglioNotificaCarcereFinePena.MPV.value.length==1)
        document.DettaglioNotificaCarcereFinePena.MPV.value='0'+document.DettaglioNotificaCarcereFinePena.MPV.value;

      var data_to_verify = document.DettaglioNotificaCarcereFinePena.GPV.value+'/'+document.DettaglioNotificaCarcereFinePena.MPV.value+'/'+document.DettaglioNotificaCarcereFinePena.APV.value;
      if (data_to_verify.length>4)
      {
        if (!ControllaData(data_to_verify) )
        {
          alert('Data di Decorrenza non valida');
          return false;
        }
        else
          return true;
      }
    }
    else
      return true;
  }
 </script>
</head>

<body class="corpo">
<FORM name="comandi" >
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Dettaglio Notifica Carcere</font>
      </td>
   </tr>

 </table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    <br>
</FORM>
<form name="DettaglioNotificaCarcereFinePena" method="POST" action="/jsp/Main.jsp">
		 <table cellspacing=4 cellpadding=4>


		<tr>
				<td class="l">Data Pervenimento Notifica</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(verbale.getDataPervenimento(),"dd-MM-yyyy"))%> </font></td>
		</tr>
		<tr>
				<td class="l">Data Scadenza altra pena</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(verbale.getDataEmissione(),"dd-MM-yyyy"))%> </font></td>
		</tr>
		<tr>
      <td class="l">Istituto di Detenzione</td>
<%
    //modifica relativa al tipo istituto
%>
			<td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(lIstitutoDetenzione.getDescrTipoIstituto())%></font>&nbsp;
<!--
      </td>
		</tr>
		<tr>
      <td class="l">Luogo Detenzione</td>
      <td class="l">
-->
<%
          if(lIstitutoDetenzione.getDescrTipoIstituto()!=null
            && !lIstitutoDetenzione.getDescrTipoIstituto().equals("-")
            && !lIstitutoDetenzione.getDescrTipoIstituto().equals("")
            )
          {
%>
            di
           &nbsp;
<%
          }
%>
          <font class="campo">
           <%=StringUtils.toStringJSP(lIstitutoDetenzione.getDescrComune())%>
          </font>
        </td>
<%
    //fine modifica relativa al tipo istituto
%>
      </tr>
<!---------------------------------------FINE PENA--------------------------------------------------------------->

  <table>
     <tr>
      <td class="Titolo" colspan=16 ><font  class="label">Pena da Espiare</font></td>
	   </tr>
     <tr>
      <td class="l">
        <font  class="label">Reclusione : </font>
      </td>
<%
      if(penaresidua != null && (penaresidua.getFlagErgastolo() == null || penaresidua.getFlagErgastolo().equals("N")))
      {
%>
        <td class="l"><font class="label">Anni</font></td>
        <td class="r"><font class="campo"><%=penaresidua.getNumAnniReclusione()%></font></td>
        <td class="l"><font class="label">Mesi</font></td>
        <td class="r"><font class="campo"><%=penaresidua.getNumMesiReclusione()%></font></td>
        <td class="l"><font class="label">Giorni</font></td>
        <td class="r"><font class="campo"><%=penaresidua.getNumGiorniReclusione()%></font></td>
    <!--/tr>
    <tr-->
        <td class="l"><font  class="label">Arresto :</font></td>
        <td class="l"><font class="label">Anni</font></td>
        <td class="r"><font class="campo"><%=penaresidua.getNumAnniArresto()%></font></td>
        <td class="l"><font class="label">Mesi</font></td>
        <td class="r"><font class="campo"><%=penaresidua.getNumMesiArresto()%></font></td>
        <td class="l"><font class="label">Giorni</font></td>
        <td class="r"><font class="campo"><%=penaresidua.getNumGiorniArresto()%></font></td>
    </tr>
<%
  }
  else
  {
%>
      <%if(penaresidua.getFlagErgastolo().equals("S"))
       {%>

         <td class="l"><font class="campo">ERGASTOLO</font></td>
      <%}else if(penaresidua.getFlagErgastolo().equals("D"))
       {%>
         <td class="l"><font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO</font></td>
      <%}%>
    </tr>
<%
  }
%>
</table>
<table>
      <tr>
        <td class="l">Data Decorrenza Pena: </td>
         <td class="l"> <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%>
          </font></td>
<%if (vedoDataIntermedia.equals("S"))
	{%>
      <td class="l"><font  class="label">Data Fine Reclusione : </font></td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFineReclusione(),"dd-MM-yyyy"))%>
        </font>
      </td>
		</tr>
		<tr>
      <td class="l"><font  class="label">Data Inizio Arresto : </font></td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizioArresto(),"dd-MM-yyyy"))%>
        </font>
      </td>
<%
    }

    if(penaresidua != null && (penaresidua.getFlagErgastolo() == null || penaresidua.getFlagErgastolo().equals("N")))
    {
%>
      <td class="l"><font  class="label">Data Fine Pena Automatica : </font></td>
        <td class="l">
          <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(),"dd-MM-yyyy"))%>
          </font>
        </td>
      </tr>
      <% /* REWORK DETTAGLIO */
      String lType="text";
      if(DettaglioDaElenco!=null && DettaglioDaElenco.equals("SI"))
      {
        %>
      <tr>
        <td colspan=4><font  class="label"></font>
          <input type="hidden" name="GPV" maxlength="2" size="2" value="<%=DateUtils.getDayToString(penaresidua.getDataFinePresunta())%>">
          <input type="hidden" name="MPV" maxlength="2" size="2" value="<%=DateUtils.getMonthToString(penaresidua.getDataFinePresunta())%>">
          <input  type="hidden" name="APV" maxlength="4" size="4" value="<%=DateUtils.getYearToString(penaresidua.getDataFinePresunta())%>">
        </td>
      </tr>
  <%
      }
      else
      {%>
      <tr>
        <td class="l"colspan=4><font  class="label">Data Fine Pena Manuale : </font>
          <input type="text" name="GPV" maxlength="2" size="2" value="<%=DateUtils.getDayToString(penaresidua.getDataFinePresunta())%>">
          /
          <input type="text" name="MPV" maxlength="2" size="2" value="<%=DateUtils.getMonthToString(penaresidua.getDataFinePresunta())%>">
          /
          <input  type="text" name="APV" maxlength="4" size="4" value="<%=DateUtils.getYearToString(penaresidua.getDataFinePresunta())%>">
          &nbsp;&nbsp;&nbsp;
        </td>
      </tr>
  <%}
    if(totalegiornilibanticipata.compareTo(new BigDecimal(0)) != 0)
    {
%>
      <tr>
        <td class="l" colspan=9>
          <font class="label">Giorni di Liberazione Anticipata Concessi già detratti:</font>
          <font class="campo"><%=totalegiornilibanticipata%></font>
        </td>
      </tr>
<%
    }
  if(!(DettaglioDaElenco!=null && DettaglioDaElenco.equals("SI")))
      {%>
    <tr>
        <td class="l"colspan=4><INPUT class="bottone" type="submit" name="conferma" value="Validazione Fine Pena"></td>
      </tr>
<%    }
    }
%>
    </table>
<!-----------------------------------------FINE PENA------------------------------------------------------------->
		</table>
     <input type="hidden" name="idpenaresidua" value="<%=penaresidua.getIdPenaResidua()%>">
     <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.verbale.action.ActRegistraPenaVerbaleArresto">
	 <input type="hidden" name="flagVerbaleNotifica" value="notifica">
	 	
  </form>
  </body>

<%
   if(penaresidua != null && (penaresidua.getFlagErgastolo() == null || penaresidua.getFlagErgastolo().equals("N")))
   {
%>
<script language="JavaScript" type="text/javascript">

  var frmvalidator  = new Validator("DettaglioNotificaCarcereFinePena");

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
<%}%>

</html>