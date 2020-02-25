<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="java.math.BigDecimal" %>

<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>

<%@ page import="siap.sico.calendar.model.CalendarModel" %>
<%@ page import="siap.siep.penaresidua.model.PenaResiduaModel" %>
<%@ page import="siap.siep.penacomplessiva.model.PenaComplessivaModel" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>
<%@ page import="siap.sico.util.CalendarUtil" %>

<jsp:useBean id="fascicolo" scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel" />
<jsp:useBean id="pena" scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel" />


<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
    <script language="JavaScript">
    function Verify()
	{
            if   (document.f.GPV.value!="" && document.f.MPV.value!="" && document.f.APV.value!="")
            {
		if (document.f.GPV.value.length==1)
			document.f.GPV.value='0'+document.f.GPV.value;
		if (document.f.MPV.value.length==1)
			document.f.MPV.value='0'+document.f.MPV.value;

		var data_to_verify = document.f.GPV.value+'/'+document.f.MPV.value+'/'+document.f.APV.value;
                if (data_to_verify.length>4)
                {
		  if (!ControllaData(data_to_verify) )
		  {
                    alert('Data di Decorrenza non valida');
                    return false;
		  } else
                    return true;
                }
            }else
             return true;
	 }
    </script>
    <title>[S.I.E.S.] - Valida Pena</title>
  </head>
 <% CalendarUtil lCal=new CalendarUtil(); %>
  <body class="corpo">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class=LBG>
          <font  class="label">Funzione :&nbsp;</font><font class="campo">Valida Pena</font>
        </td>
      </tr>
    </table>
    <br>
      <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    <br>
  <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="f">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misuraalternativa.action.ActValidaPenaResiduaMA">
  <input type="HIDDEN" name="<%=ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE%>" value="<%=request.getParameter(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE)%>">


  <INPUT type="hidden" name="ipenaresidua" value="<%=pena.getIdPenaResidua()%>">
<table>
      <tr>
      <td width=100% colspan=2>

         <table width=100%>
       <tr>
        <td class="Titolo" colspan=9><font  class="label">Pena Residua</font></td>
	  </tr>
       <% if (pena.getNumAnniReclusione().intValue()!=0 && pena.getNumMesiReclusione().intValue()!=0 && pena.getNumGiorniReclusione().intValue()!=0 && pena.getNumAnniArresto().intValue()!=0 && pena.getNumMesiArresto().intValue()!=0 && pena.getNumGiorniArresto().intValue()!=0)
       { %>
          <tr>

            <td class="l"><font  class="label">Reclusione / Multa : </font></td>
            <td class="l"><font class="label">Anni</font></td>
			<td class="lRosso"><font class="campo"><%=pena.getNumAnniReclusione()%></font></td>
            <td class="l"><font class="label">Mesi</font></td>
			<td class="lRosso"><font class="campo"><%=pena.getNumMesiReclusione()%></font></td>
            <td class="l"><font class="label">Giorni</font></td>
			<td class="lRosso"><font class="campo"><%=pena.getNumGiorniReclusione()%></font></td>
            <td class="l"><font class="label">Importo</font></td>
			<td class="lRosso"><font class="campo"><%=StringUtils.toEuroFormat(pena.getImportoMulta())%></font></td>

        </tr>
         <tr>
            <td class="l"><font  class="label">Arresto / Ammenda :</font></td>
            <td class="l"><font class="label">Anni</font></td>
			<td class="lRosso"><font class="campo"><%=pena.getNumAnniArresto()%></font></td>
            <td class="l"><font class="label">Mesi</font></td>
			<td class="lRosso"><font class="campo"><%=pena.getNumMesiArresto()%></font></td>
            <td class="l"><font class="label">Giorni</font></td>
			<td class="lRosso"><font class="campo"><%=pena.getNumGiorniArresto()%></font></td>
            <td class="l"><font class="label">Importo</font></td>
                        <td class="lRosso"><font class="campo"><%=StringUtils.toEuroFormat(pena.getImportoAmmenda())%></font></td>

        </tr>
       <%}%>

        </table>
      </td>
      </tr>
      </table>

    <table>
    <tr>
<% if (pena.getDataInizio() != null)
{ %>

        <td class="l">Data Decorrenza Pena: </font></td>
         <td class="l"> <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(pena.getDataInizio(),"dd-MM-yyyy"))%>
          </font></td>
<%}%>

<% if (pena.getDataFineReclusione() != null)
{ %>
        <td class="l"><font  class="label">Data Fine Reclusione : </font></td>
         <td class="l"> <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(pena.getDataFineReclusione(),"dd-MM-yyyy"))%>
          </font></td>
<%}%>

  </tr>
  <tr>
<% if (pena.getDataInizioArresto() != null)
{ %>
        <td class="l"><font  class="label">Data Inizio Arresto : </font></td>
         <td class="l"> <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(pena.getDataInizioArresto(),"dd-MM-yyyy"))%>
          </font></td>
<%}%>
<% if (pena.getDataFinePresunta() != null)
{ %>
        <td class="l"><font  class="label">Data Fine Pena Automatica : </font></td>
          <td class="l"><font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(pena.getDataFinePresunta(),"dd-MM-yyyy"),"-")%>
          </font></td>
<%}%>
</tr>
<tr>
<% if (pena.getFlagValidato() != null &&  pena.getFlagValidato().equals("S")  && pena.getDataFine() != null)
{ %>

        <td class="l"><font  class="label">Data Fine Pena Manuale : </font></td>
          <td class="l"><font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(pena.getDataFine(),"dd-MM-yyyy"),"-")%>
          </font></td>
<%}else if(pena.getDataFinePresunta() != null)
{%>
        <td class="l"colspan=4><font  class="label">Data Fine Pena Manuale : </font>
           <input type="text" name="GPV" maxlength="2" size="2" value="<%=DateUtils.getDayToString(pena.getDataFinePresunta())%>">
            /
            <input type="text" name="MPV" maxlength="2" size="2" value="<%=DateUtils.getMonthToString(pena.getDataFinePresunta())%>">
            /
            <input  type="text" name="APV" maxlength="4" size="4" value="<%=DateUtils.getYearToString(pena.getDataFinePresunta())%>">
        &nbsp;&nbsp;&nbsp;</td>
<%}%>
      </tr>

      <tr>
       <td class="l"colspan=4><INPUT class="bottone" type="submit" name="conferma" value="Validazione Fine Pena"></td>
      </tr>

    </table>
    </form>
  </body>
<% if ((pena.getFlagValidato() == null ||  pena.getFlagValidato().equals("N") ) && pena.getDataFinePresunta() != null)
{ %>
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
<%}%>
</html>