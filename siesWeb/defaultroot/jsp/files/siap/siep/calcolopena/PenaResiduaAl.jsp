<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page import="java.util.Iterator" %>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.log.LogF3B" %>


<%@ page import="siap.siep.notifica.model.NotificaModel" %>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.evento.model.EventoNotificaModel"%>
<%@ page import="siap.siep.autoritaesterna.model.AutoritaEsternaModel"%>
<%@ page import="siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa"%>


<jsp:useBean id="PenaResiduaAl"     scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel" />
<jsp:useBean id="DataScarcerazione" scope="request" class="java.util.Date" />
<jsp:useBean id="FormName"          scope="request" class="java.lang.String" />
<jsp:useBean id="Message"           scope="request" class="java.lang.String" />

<%
//==============================================================================
// Form di visualizzazione del risultato del calcolo della Pena Residua Al.
// Viene invocata in due situazioni:
// - dalle form dei computi per determinare il quantum da imputare per ottenere 
//   una opportuna data di scarcerazine
// - dalle form di dettaglio per conoscere la pena residua alla data di systema
// Nel primo caso, i dati calcolati vanno caricati nelle form chiamanti.
// Nel secondo caso i dati vanno semplicemente visualizzati (FormName="")
//==============================================================================
%>


<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Calcolo Pena Residua </title>

    <script language="JavaScript" src="/html/conferma.js"></script>
    <script language="JavaScript">
    <% if (!FormName.equals("") ) {%>
      function insertIT () {
        //= Reclusione
        window.parent.opener.document.<%=FormName%>.ARec.value=<%=StringUtils.toStringJSP(PenaResiduaAl.getNumAnniReclusione(),"0")%>;
        window.parent.opener.document.<%=FormName%>.MRec.value=<%=StringUtils.toStringJSP(PenaResiduaAl.getNumMesiReclusione(),"0")%>;
        window.parent.opener.document.<%=FormName%>.GRec.value=<%=StringUtils.toStringJSP(PenaResiduaAl.getNumGiorniReclusione(),"0")%>;

        //= Arresto
        window.parent.opener.document.<%=FormName%>.AArr.value=<%=StringUtils.toStringJSP(PenaResiduaAl.getNumAnniArresto(),"0")%>;
        window.parent.opener.document.<%=FormName%>.MArr.value=<%=StringUtils.toStringJSP(PenaResiduaAl.getNumMesiArresto(),"0")%>;
        window.parent.opener.document.<%=FormName%>.GArr.value=<%=StringUtils.toStringJSP(PenaResiduaAl.getNumGiorniArresto(),"0")%>;
          
        window.parent.close();
      }
    <% } %>
  	</script>
  </head>

<body class="corpo" >
    <table>
      <tr>
        <td class="LBG">
          <a href="Javascript:window.print();">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
          </a>
        </td>
        <td class="LBG">
          <font class="label">Funzione :</font>
          <font class="campo"> Calcolo Pena Residua Al </font>
        </td>
      </tr>
    </table>

    <br>
  
    <table>
      <tr>
        <td class="Titolo" colspan=9><font  class="label">Pena Residua Da Espiare al </font><font class="cRossoCumulo"><%=DateUtils.getDateToString(DataScarcerazione,"dd-MM-yyyy") %></font></td>
      </tr>
      <tr>
        <td class="l"><font class="label">Reclusione / Multa :</font></td>
        <td class="l"><font class="label">Anni</font></td>
        <td class="r"><font class="campo"><%=StringUtils.toStringJSP(PenaResiduaAl.getNumAnniReclusione(),"0")%></font></td>
        <td class="l"><font class="label">Mesi</font></td>
        <td class="r"><font class="campo"><%=StringUtils.toStringJSP(PenaResiduaAl.getNumMesiReclusione(),"0")%></font></td>
        <td class="l"><font class="label">Giorni</font></td>
        <td class="r"><font class="campo"><%=StringUtils.toStringJSP(PenaResiduaAl.getNumGiorniReclusione(),"0")%></font></td>
      </tr>
      <tr>
        <td class="l"><font class="label">Arresto / Ammenda :</font></td>
        <td class="l"><font class="label">Anni</font></td>
        <td class="r"><font class="campo"><%=StringUtils.toStringJSP(PenaResiduaAl.getNumAnniArresto(),"0")%></font></td>
        <td class="l"><font class="label">Mesi</font></td>
        <td class="r"><font class="campo"><%=StringUtils.toStringJSP(PenaResiduaAl.getNumMesiArresto(),"0")%></font></td>
        <td class="l"><font class="label">Giorni</font></td>
        <td class="r"><font class="campo"><%=StringUtils.toStringJSP(PenaResiduaAl.getNumGiorniArresto(),"0")%></font></td>
      </tr>
    </table>
    
    <% if (!Message.equals("")){ %>
    <table>
      <tr>
        <td class="l"><font color="red"><%=Message%></font></td>
      </tr>
    </table>
    <% } %>
    
    <% if (!FormName.equals("") ) {%>
    <table>
      <tr>
        <td class="lNoBord" colspan="7">
          <INPUT class="bottone" type="button" name="conferma" value="Carica dati in maschera" onclick="javascript:insertIT();">
        </td>
      </tr>
    </table>
    <% } %>

</body>
</html>