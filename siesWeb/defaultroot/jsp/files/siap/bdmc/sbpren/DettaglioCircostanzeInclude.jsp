<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.bdmc.sbviewprocpena.model.SbViewProcpenaModel"%>
<%@ page import="siap.bdmc.sbviewprocpena.action.ICostantiSbViewProcpena"%>

<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="sbviewprocpena" scope="request" class="siap.bdmc.sbviewprocpena.model.SbViewProcpenaModel"/>

<html>
<head>
  <%  if( modalita.equals("D") )  {%>
  <title> Dettaglio SbViewProcpena </title>
  <%} else if( modalita.equals("C") ) { %> 
  <title> Cancellazione SbViewProcpena </title>
  <%}%> 
  <link rel="STYLESHEET" type="text/css" href="<%=ISIAPCostantiWeb.PG_STYLE%>">
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONFIRM%>"></script>
  <script language="JavaScript" >
    function Verify() { 
      var msgConfirm = "Si vuole procedere con la cancellazione dei dati?"; 
      if (window.confirm(msgConfirm)) 
        return true; 
      else 
        return false; 
    } 
  </script>
</head>

<body class="corpo">

<FORM name="comandi" >
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();"> 
          <img align="middle" src="<%=ISIAPCostantiWeb.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0> 
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
      <%  if( modalita.equals("D") )  {%> 
        <font class="campo">Dettaglio SbViewProcpena</font>
      <%} else if( modalita.equals("C") ) { %> 
        <font class="campo">Cancellazione SbViewProcpena</font>
      <%}%> 
      </td>
      <td class="LBG">
         <a href="Main.jsp?Action=siap.bdmc.sbviewprocpena.action.ActLoadInserisciSbViewProcpena&TornaQui=10">
       <img align="middle" src="/images/new24.gif" alt="Inserisci" width="24" height="24" border="0">
    </a>
 
       <a href="Main.jsp?Action=siap.bdmc.sbviewprocpena.action.ActLoadModificaSbViewProcpena&IdSbViewProcpena=<%=sbviewprocpena.getIdPren() %>&TornaQui=10">
         <img align="middle" src="/images/modifica24.gif" alt="Modifica" width="24" height="24" border="0">
        </a>
 
         <a href="Javascript:conferma('siap.bdmc.sbviewprocpena.action.ActLoadCancellaSbViewProcpena','IdSbViewProcpena','<%=sbviewprocpena.getIdPren()%>');">
         <img align="middle" src="/images/delete24.gif" alt="Cancella" width="24" height="24" border="0">
          </a>
 
      </td>
    </tr>
  </table>
</FORM>

<%  if( modalita.equals("C") )  {%> 
<FORM method="POST" action="Main.jsp" name="CancellaSbViewProcpena">
  <input type="HIDDEN" name="<%=ISIAPCostantiWeb.ACTION_FIELD%>" value="siap.bdmc.sbviewprocpena.action.ActCancellaSbViewProcpena">
  <input type="HIDDEN" name="<%=ICostantiSbViewProcpena.CAMPO_ID_PREN%>" value="<%=StringUtils.toStringJSP(sbviewprocpena.getIdPren()) %>">
  <%
    //==================================================
    // Aggiungere qui eventuali altri campi hidden      
    //==================================================
  %>
<%}%> 
<table cellspacing=4 cellpadding=4>
  <tr>
    <td class="l">Codi Uffi Pmpm</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getCodiUffiPmpm()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Anno Regi Pmpm</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getAnnoRegiPmpm()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Nume Regi Pmpm</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getNumeRegiPmpm()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Codi Uffi Gipp</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getCodiUffiGipp()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Anno Regi Gipp</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getAnnoRegiGipp()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Nume Regi Gipp</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getNumeRegiGipp()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Codi Uffi Dibb</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getCodiUffiDibb()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Anno Regi Dibb</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getAnnoRegiDibb()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Nume Regi Dibb</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getNumeRegiDibb()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Codi Uffi Coap</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getCodiUffiCoap()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Nume Regi Coap</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getNumeRegiCoap()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Anno Regi Coap</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getAnnoRegiCoap()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Data Pass Giud</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(sbviewprocpena.getDataPassGiud(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Flag Recl Arre Gigu</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getFlagReclArreGigu()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Anni Pena Gigu</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getAnniPenaGigu()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Mesi Pena Gigu</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getMesiPenaGigu()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Gior Pena Gigu</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getGiorPenaGigu()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Data Sent 1gra</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(sbviewprocpena.getDataSent1gra(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Anno Sent 1gra</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getAnnoSent1gra()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Nume Sent 1gra</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getNumeSent1gra()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Data Sent 2gra</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(sbviewprocpena.getDataSent2gra(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Anno Sent 2gra</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getAnnoSent2gra()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Nume Sent 2gra</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getNumeSent2gra()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Data Sent Gipp Gupp</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(sbviewprocpena.getDataSentGippGupp(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Nume Sent Gipp Gupp</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getNumeSentGippGupp()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Anno Sent Gipp Gupp</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getAnnoSentGippGupp()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Anni Pena Diba</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getAnniPenaDiba()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Mesi Pena Diba</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getMesiPenaDiba()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Gior Pena Diba</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getGiorPenaDiba()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Anni Pena Appe</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getAnniPenaAppe()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Mesi Pena Appe</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getMesiPenaAppe()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Gior Pena Appe</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getGiorPenaAppe()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Flag Recl Arre Diba</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getFlagReclArreDiba()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Flag Recl Arre Appe</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getFlagReclArreAppe()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Flag Arti 0089</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getFlagArti0089()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Flag Arti 0090</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getFlagArti0090()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Flag Arti 0091</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getFlagArti0091()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Flag Arti 0092</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getFlagArti0092()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Flag Arti 0093</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getFlagArti0093()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Flag Arti 0094</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getFlagArti0094()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Flag Arti 0095</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getFlagArti0095()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Flag Arti 0096</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getFlagArti0096()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Flag Arti 0097</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getFlagArti0097()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Flag Arti 0098</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getFlagArti0098()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Flag Arti 0099</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getFlagArti0099()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Flag Arti 62</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getFlagArti62()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Arti 0062 Comm</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getArti0062Comm()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Flag Art 62bi</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getFlagArt62bi()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Codi Misu Cust</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getCodiMisuCust()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Codi Isti Pena</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getCodiIstiPena()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Desc Luog</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getDescLuog()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Id Pren</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getIdPren()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Codi Sede Inst</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getCodiSedeInst()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Nume Fasc Bdmc</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getNumeFascBdmc()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Anno Fasc Bdmc</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getAnnoFascBdmc()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Flag Info Sele</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getFlagInfoSele()) %></font>&nbsp;</td>
  </tr>

<%  if( modalita.equals("C") )  {%> 
  <tr>
    <td align="center">
      <input class="bottone" type="submit" name="conferma" value="Conferma"  onclick="Javascript: return Verify();">
    </td>
  </tr>
<%}%>

</table>
<%  if( modalita.equals("C") )  {%> 
</form>
<%}%>
</body>
</html>