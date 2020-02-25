<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="siap.siepe.fascicolo.model.FascicoloSiepeModel"%>
<%@ page import="siap.siepe.fascicolo.action.ICostantiFascicoloSiepe"%>
<%@ page import="siap.siepe.attivita.action.ICostantiAttivita"%>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto"%>

<jsp:useBean id="FascicoloSiepeEsteso" scope="session" class="siap.siepe.fascicolo.model.FascicoloSiepeEstesoModel"/>
<jsp:useBean id="fascicolosiepe" scope="request" class="siap.siepe.fascicolo.model.FascicoloSiepeModel"/>
<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel"/>

<%
String isVALIGN = "top";
String isBorder = "0";
String lWidth = "96%";
%>



<html>
<head>
<title>[S.I.A.P.] - Dettaglio FascicoloSiepe </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="/html/conferma.js"></script>

</head>

<body class="corpo">
<FORM name="comandi" >
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Dettaglio Procedimento SIEPE</font>
      </td>
      <td class="LBG">
          <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiFascicoloSiepe.CAMPO_ID_FASCICOLO_SIEPE%>" />
          <jsp:param name="ValoreIdEntita" value="<%=fascicolosiepe.getIdFascicoloSiepe()%>" />
       </jsp:include>
     </td>
    <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
   </tr>
 </table>
</FORM>
 <table cellspacing=1 cellpadding=1  width="95%" border=<%=isBorder%>>
  <tr>
    <td class="label" width=17% valign=<%=isVALIGN%>> Procedimento </td>
      <td class="L" width=83%><font class="label">Numero </font>
          <font class="campo"><%=fascicolosiepe.getChiaveAnno()%>/<%=fascicolosiepe.getChiaveProgr()%>
<%        if(!(UtenteConnesso.getUfficioUtente().getCodUfficio().compareTo(fascicolosiepe.getChiaveUfficio())==0))
          {
%>
            &nbsp;-&nbsp;<%=fascicolosiepe.getDescrUfficioInserimento()%>
<%
          }
%>

          </font>
      </td>
    </tr>
  <tr>
    <td class="label" valign=<%=isVALIGN%>> </td>
      <td class="L"> <font class="label">Data Iscrizione:</font>
          <font class="campo"><%=DateUtils.getDateToString(fascicolosiepe.getDataIscrizione(),"dd-MM-yyyy")%></font></td>
  </tr>
  <tr>
    <td class="label" valign=<%=isVALIGN%>> </td>
      <td class="L"> 
      		<font class="label">Stato: &nbsp;</font>
          <font class="cRosso"><%=StringUtils.toStringJSP(fascicolosiepe.getDescrStatoFascicolo(),"-")%></font>
      		&nbsp; 
      		<font class="label">Data Definizione: &nbsp;</font>
          <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString( fascicolosiepe.getDataDefinizione(),"dd-MM-yyyy"),"-")%></font>
      </td>
  </tr>

  <tr>
    <td class="label" valign=<%=isVALIGN%>> </td>
      <td class="L"> 
      	<font class="label">Tipo Definizione: &nbsp;</font>
        <font class="campo"><%=StringUtils.toStringJSP( fascicolosiepe.getDescrTipoDefinizione(), "-")%></font>
      	&nbsp; 
      	<font class="label">Descrizione Definizione: &nbsp;</font>
        <font class="campo"><%=StringUtils.toStringJSP( fascicolosiepe.getDescrDefinizione(),"-")%></font>
      </td>
      
  </tr>

  <tr>
    <td class="label" valign=<%=isVALIGN%>> Rif. UEPE </td>
      <td class="L"> <font class="label">Numero </font>
          <font class="campo"><%=StringUtils.toStringJSP(fascicolosiepe.getAnnoUepe(),"-")%>/<%=StringUtils.toStringJSP(fascicolosiepe.getNumUepe(),"-")%>/<%=StringUtils.toStringJSP(fascicolosiepe.getProgrUepe(),"-")%></font></td>
  </tr>

  <tr>
    <td class="label" valign=<%=isVALIGN%>>Incarico</td>
      <td class="L" width=29%><font class="campo"><%=fascicolosiepe.getDescrIncarico()%></font></td>
  </tr>

  <tr>
    <td class="label" valign=<%=isVALIGN%>>Note</td>
<%		String lNote="-";
			if (fascicolosiepe.getNote()!=null) lNote=fascicolosiepe.getNote();
%>
      <td class="L" width=29%><font class="campo"><%=lNote%></font></td>
  </tr>
  <tr>
    <td class="label" valign=<%=isVALIGN%>>Ufficio Mittente Atto</td>
      <td class="L" width=29%><font class="campo"><%=StringUtils.toStringJSP(fascicolosiepe.getDescrUfficioMittente(),"-")%></font></td>
  </tr>
</table>
<br>
    <jsp:include page="<%=ICostantiFascicoloSiepe.PG_SINTESI_SOGG_FASCICOLI%>"/>
<br>
    <jsp:include page="<%=ICostantiAttivita.PG_ELENCO_ATTIIVITA%>"/>

</body>
</html>