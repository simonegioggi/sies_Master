<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="java.util.Date" %>

<%@ page import="siap.sico.libertaanticipata.action.ICostantiLicenzaLibanticipata" %>


<jsp:useBean id="DataFineAbInitioSenzaLA" scope="request" class="java.util.Date" />
<jsp:useBean id="DataFineAbFineSenzaLA"   scope="request" class="java.util.Date" />
<jsp:useBean id="DataFineAbInitio"        scope="request" class="java.util.Date" />
<jsp:useBean id="DataFineAbFine"          scope="request" class="java.util.Date" />
<jsp:useBean id="lEveIdEventoOrdinanza"   scope="request" class="java.lang.String" />

<jsp:useBean id="isDL92"   scope="request" class="java.lang.String" />

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
    <title>[S.I.E.S.] - Conferma Data Fine Pena</title>
    <style type="text/css">
      .messaggio 
      {
        font-family: 'Tahoma';
        font-size: 12px;
        font-weight : bold;
      }
    </style>
  </head>

  <body class="corpo">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class=LBG>
          <font  class="label">Funzione :&nbsp;</font><font class="campo">Conferma Data Fine Pena Liberazione Anticipata</font>
        </td>
      </tr>
    </table>
    
    <br>
      <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    <br>
    
    <form method="POST" name="f" action="<%= IWebConstants.PG_MAIN%>">
      <% if ("S".equals(isDL92)) { %>
      <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.libertaanticipata.action.ActCalcoloPenaRimediRisarcitori">
      <% } else { %>
      <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.libertaanticipata.action.ActConfermaDataFineLA">
      <% } %>
      
      <input type="HIDDEN" name="lEveIdEventoOrdinanza" value="<%=StringUtils.toStringJSP(lEveIdEventoOrdinanza)%>">
      <input type="HIDDEN" name="<%=ICostantiLicenzaLibanticipata.CAMPO_EVE_ID_EVENTO%>" value="<%=StringUtils.toStringJSP(lEveIdEventoOrdinanza)%>">
      <input type="HIDDEN" name="isDL92" value="<%=StringUtils.toStringJSP(isDL92)%>">
      <input type="HIDDEN" name="isConfermaDataFine" value="S">

      <input type="HIDDEN" name="GiornoDataFineAbInitio" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(DataFineAbInitio, "dd"))%>">
      <input type="HIDDEN" name="MeseDataFineAbInitio"   value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(DataFineAbInitio, "MM"))%>">
      <input type="HIDDEN" name="AnnoDataFineAbInitio"   value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(DataFineAbInitio, "yyyy"))%>">

      <input type="HIDDEN" name="GiornoDataFineAbFine" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(DataFineAbFine, "dd"))%>">
      <input type="HIDDEN" name="MeseDataFineAbFine"   value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(DataFineAbFine, "MM"))%>">
      <input type="HIDDEN" name="AnnoDataFineAbFine"   value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(DataFineAbFine, "yyyy"))%>">
      
      <% 
        Date dataScarcerazione = (Date)request.getAttribute("dataScarcerazione");

        if (dataScarcerazione!=null)
        {
        %>
        <input type="HIDDEN" name="GiornoDataScarcerazione" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataScarcerazione, "dd"))%>">
        <input type="HIDDEN" name="MeseDataScarcerazione"   value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataScarcerazione, "MM"))%>">
        <input type="HIDDEN" name="AnnoDataScarcerazione"   value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataScarcerazione, "yyyy"))%>">
        <% 
        } 
      %>    
    
    
    
    
    
    
      <table width="100%">
        <tr>
          <td class="messaggio">
            <font color="red">
              Attenzione: il precedente metodo di calcolo presenta una discrepanza col nuovo. <br>
              L'utente può scegliere se fare il calcolo partendo dal precedente fine pena a sistema
            </font> 
          </td>
        </tr>
      </table>
      <table>
        <tr>
          <td class="l">
            Precedente fine pena a sistema           
          </td>
          <td class="l">
            <font class="campo">
              <%=StringUtils.toStringJSP(DateUtils.getDateToString(DataFineAbFineSenzaLA, "dd-MM-yyyy"))%>
            </font>
          </td>
          <td class="l">
            arretramento (ab Fine)
          </td>
          <td class="l">
            <font class="campo">
              <%=StringUtils.toStringJSP(DateUtils.getDateToString(DataFineAbFine, "dd-MM-yyyy"))%>
            </font>
            <input type="radio" name="DataFine" value="DataFineArretrato">
          </td>
        </tr>
        <tr>
          <td class="messaggio" colspan="3">
            <font color="red">
              o dal fine pena del nuovo metodo di calcolo ab Initio 
            </font> 
          </td>
        </tr>
        <tr>
          <td class="l">
            Nuovo fine pena
          </td>
          <td class="l">
            <font class="campo">
              <%=StringUtils.toStringJSP(DateUtils.getDateToString(DataFineAbInitioSenzaLA, "dd-MM-yyyy"))%>
            </font>
          </td>
          <td class="l">
            rideterminazione (ab Initio)
          </td>
          <td class="l">
            <font class="campo">
              <%=StringUtils.toStringJSP(DateUtils.getDateToString(DataFineAbInitio, "dd-MM-yyyy"))%>
            </font>
            <input type="radio" name="DataFine" value="DataFineAbInitio" checked>
          </td>
        </tr>
        <tr><td>&nbsp;</td></tr>
        <tr>
          <td>
            <input class="bottone" type="submit" name="conferma" value="Conferma">
          </td>
        </tr>
      </table>
      
      

    </form>
    
  <script language="JavaScript" type="text/javascript">
  // n.b. serve solo per disabilitare il tasto di submit
    var frmvalidator  = new Validator("f");

  </script>
    
  </body>
</html>