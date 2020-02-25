<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="siap.bdmc.sbviewprocpena.model.SbViewProcpenaModel"%>
<%@ page import="siap.bdmc.sbviewprocpena.action.ICostantiSbViewProcpena"%>

<jsp:useBean id="sbviewprocpena" scope="request" class="java.util.Vector"/>
<jsp:useBean id="tipo_ricerca" scope="request" class="java.lang.String"/>

<html>
<head>
  <title> Ricerca SbViewProcpena </title>
  <link rel="STYLESHEET" type="text/css" href="<%=ISIAPCostantiWeb.PG_STYLE%>">
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONFIRM%>"></script>
</head>

<body class="corpo">
<FORM method="POST" action="Main.jsp" name="RicercaSbViewProcpena">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=ISIAPCostantiWeb.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
                      <font class="campo">Elenco SbViewProcpena</font>
      </td>
    </tr>
  </table>

  <% //=============================================== 
     // Include della jsp che gestisce la paginazione 
     //=============================================== %>
  <%
  if (tipo_ricerca.equals("paginata")) { %>
    <jsp:include page="<%=ISIAPCostantiWeb.PAGINAZIONE_RICERCA%>"></jsp:include>
  <%}
  %>

<div>
  <table align="center">
    <tr>
      <%// Inserire qui le intestazioni delle colonne che si vogliono visualizzare %>
      <td class="int">Codi Uffi Pmpm</td>
      <td class="int">Anno Regi Pmpm</td>
      <td class="int">Nume Regi Pmpm</td>
      <td class="int">Codi Uffi Gipp</td>
      <td class="int">Anno Regi Gipp</td>
      <td class="int">Nume Regi Gipp</td>
      <td class="int">Codi Uffi Dibb</td>
      <td class="int">Anno Regi Dibb</td>
      <td class="int">Nume Regi Dibb</td>
      <td class="int">Codi Uffi Coap</td>
      <td class="int">Nume Regi Coap</td>
      <td class="int">Anno Regi Coap</td>
      <td class="int">Data Pass Giud</td>
      <td class="int">Flag Recl Arre Gigu</td>
      <td class="int">Anni Pena Gigu</td>
      <td class="int">Mesi Pena Gigu</td>
      <td class="int">Gior Pena Gigu</td>
      <td class="int">Data Sent 1gra</td>
      <td class="int">Anno Sent 1gra</td>
      <td class="int">Nume Sent 1gra</td>
      <td class="int">Data Sent 2gra</td>
      <td class="int">Anno Sent 2gra</td>
      <td class="int">Nume Sent 2gra</td>
      <td class="int">Data Sent Gipp Gupp</td>
      <td class="int">Nume Sent Gipp Gupp</td>
      <td class="int">Anno Sent Gipp Gupp</td>
      <td class="int">Anni Pena Diba</td>
      <td class="int">Mesi Pena Diba</td>
      <td class="int">Gior Pena Diba</td>
      <td class="int">Anni Pena Appe</td>
      <td class="int">Mesi Pena Appe</td>
      <td class="int">Gior Pena Appe</td>
      <td class="int">Flag Recl Arre Diba</td>
      <td class="int">Flag Recl Arre Appe</td>
      <td class="int">Flag Arti 0089</td>
      <td class="int">Flag Arti 0090</td>
      <td class="int">Flag Arti 0091</td>
      <td class="int">Flag Arti 0092</td>
      <td class="int">Flag Arti 0093</td>
      <td class="int">Flag Arti 0094</td>
      <td class="int">Flag Arti 0095</td>
      <td class="int">Flag Arti 0096</td>
      <td class="int">Flag Arti 0097</td>
      <td class="int">Flag Arti 0098</td>
      <td class="int">Flag Arti 0099</td>
      <td class="int">Flag Arti 62</td>
      <td class="int">Arti 0062 Comm</td>
      <td class="int">Flag Art 62bi</td>
      <td class="int">Codi Misu Cust</td>
      <td class="int">Codi Isti Pena</td>
      <td class="int">Desc Luog</td>
      <td class="int">Id Pren</td>
      <td class="int">Codi Sede Inst</td>
      <td class="int">Nume Fasc Bdmc</td>
      <td class="int">Anno Fasc Bdmc</td>
      <td class="int">Flag Info Sele</td>
      <td class="int">Azioni</td>
    </tr>
    <%
      Iterator itx = sbviewprocpena.iterator();
      while ( itx.hasNext()) {
        SbViewProcpenaModel lSbViewProcpena = (SbViewProcpenaModel)itx.next();
    %>
    <tr>
      <%// Inserire qui le get dei campi da visualizzare %>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewProcpena.getCodiUffiPmpm(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewProcpena.getAnnoRegiPmpm(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewProcpena.getNumeRegiPmpm(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewProcpena.getCodiUffiGipp(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewProcpena.getAnnoRegiGipp(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewProcpena.getNumeRegiGipp(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewProcpena.getCodiUffiDibb(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewProcpena.getAnnoRegiDibb(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewProcpena.getNumeRegiDibb(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewProcpena.getCodiUffiCoap(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewProcpena.getNumeRegiCoap(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewProcpena.getAnnoRegiCoap(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbViewProcpena.getDataPassGiud(),"dd-MM-yyyy"),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewProcpena.getFlagReclArreGigu(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewProcpena.getAnniPenaGigu(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewProcpena.getMesiPenaGigu(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewProcpena.getGiorPenaGigu(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbViewProcpena.getDataSent1gra(),"dd-MM-yyyy"),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewProcpena.getAnnoSent1gra(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewProcpena.getNumeSent1gra(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbViewProcpena.getDataSent2gra(),"dd-MM-yyyy"),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewProcpena.getAnnoSent2gra(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewProcpena.getNumeSent2gra(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbViewProcpena.getDataSentGippGupp(),"dd-MM-yyyy"),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewProcpena.getNumeSentGippGupp(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewProcpena.getAnnoSentGippGupp(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewProcpena.getAnniPenaDiba(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewProcpena.getMesiPenaDiba(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewProcpena.getGiorPenaDiba(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewProcpena.getAnniPenaAppe(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewProcpena.getMesiPenaAppe(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewProcpena.getGiorPenaAppe(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewProcpena.getFlagReclArreDiba(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewProcpena.getFlagReclArreAppe(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewProcpena.getFlagArti0089(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewProcpena.getFlagArti0090(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewProcpena.getFlagArti0091(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewProcpena.getFlagArti0092(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewProcpena.getFlagArti0093(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewProcpena.getFlagArti0094(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewProcpena.getFlagArti0095(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewProcpena.getFlagArti0096(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewProcpena.getFlagArti0097(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewProcpena.getFlagArti0098(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewProcpena.getFlagArti0099(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewProcpena.getFlagArti62(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewProcpena.getArti0062Comm(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewProcpena.getFlagArt62bi(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewProcpena.getCodiMisuCust(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewProcpena.getCodiIstiPena(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewProcpena.getDescLuog(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewProcpena.getIdPren(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewProcpena.getCodiSedeInst(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewProcpena.getNumeFascBdmc(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewProcpena.getAnnoFascBdmc(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewProcpena.getFlagInfoSele(),"&nbsp;")%></td>
      <td class=c>
      <%
       String modificabile = "SI";
       // inserire qui i criteri in base ai quali il campo è modificabile 

      %>
      <table>
       <tr>
 
                 <td>
               
                   <a href="Main.jsp?Action=siap.bdmc.sbviewprocpena.action.ActLoadDettaglioSbViewProcpena&IdSbViewProcpena=<%=lSbViewProcpena.getIdPren()%>&TornaQui=20">
                     <img src="/images/dettagli.gif" width="12" height="12" alt="Dettagli" border="0">
                   </a>
 
               
                 </td>
 
                 <td>
                   <a href="Main.jsp?Action=siap.bdmc.sbviewprocpena.action.ActLoadModificaSbViewProcpena&IdSbViewProcpena=<%=lSbViewProcpena.getIdPren()%>&TornaQui=20">
                     <img src="/images/modifica.gif" alt="Modifica" width="12" height="12" border="0">
                   </a>
                 </td>
 
                 <td>
                   <a href="Javascript:conferma('siap.bdmc.sbviewprocpena.action.ActLoadCancellaSbViewProcpena','IdSbViewProcpena','<%=lSbViewProcpena.getIdPren()%>');">
                     <img src="/images/delete.gif" width="12" height="12" alt="Cancella" border="0">
                   </a>
                 </td>
 
       </tr>
     </table>
       
      </td>
    </tr>
    <% } // end while su iterator %>
  </table>
</div>
</FORM>
</body>
</html>