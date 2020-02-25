<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="siap.bdmc.sbviewreat.model.SbViewReatModel"%>
<%@ page import="siap.bdmc.sbviewreat.action.ICostantiSbViewReat"%>

<jsp:useBean id="sbviewreat" scope="request" class="java.util.Vector"/>
<jsp:useBean id="tipo_ricerca" scope="request" class="java.lang.String"/>

<html>
<head>
  <title> Ricerca SbViewReat </title>
  <link rel="STYLESHEET" type="text/css" href="<%=ISIAPCostantiWeb.PG_STYLE%>">
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONFIRM%>"></script>
</head>

<body class="corpo">
<FORM method="POST" action="Main.jsp" name="RicercaSbViewReat">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=ISIAPCostantiWeb.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
                      <font class="campo">Elenco SbViewReat</font>
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
      <td class="int">Nume Prog Capo Impu</td>
      <td class="int">Nume Prog Reat</td>
      <td class="int">Codi Font Giur</td>
      <td class="int">Anno Font Giur</td>
      <td class="int">Nume Font Giur</td>
      <td class="int">Arti Font Giur</td>
      <td class="int">Commi Arti Font</td>
      <td class="int">Lett Arti Font</td>
      <td class="int">Nume Arti Font</td>
      <td class="int">Arti Qual Font</td>
      <td class="int">Id Pren</td>
      <td class="int">Anno Fasc Bdmc</td>
      <td class="int">Nume Fasc Bdmc</td>
      <td class="int">Codi Sede Inst</td>
      <td class="int">Azioni</td>
    </tr>
    <%
      Iterator itx = sbviewreat.iterator();
      while ( itx.hasNext()) {
        SbViewReatModel lSbViewReat = (SbViewReatModel)itx.next();
    %>
    <tr>
      <%// Inserire qui le get dei campi da visualizzare %>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewReat.getNumeProgCapoImpu(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewReat.getNumeProgReat(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewReat.getCodiFontGiur(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewReat.getAnnoFontGiur(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewReat.getNumeFontGiur(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewReat.getArtiFontGiur(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewReat.getCommiArtiFont(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewReat.getLettArtiFont(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewReat.getNumeArtiFont(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewReat.getArtiQualFont(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewReat.getIdPren(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewReat.getAnnoFascBdmc(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewReat.getNumeFascBdmc(),"&nbsp;")%></td>
      <td class=c>&nbsp;<%=StringUtils.toStringJSP(lSbViewReat.getCodiSedeInst(),"&nbsp;")%></td>
      <td class=c>
      <%
       String modificabile = "SI";
       // inserire qui i criteri in base ai quali il campo è modificabile 

      %>
      <table>
       <tr>
 
                 <td>
               
                   <a href="Main.jsp?Action=siap.bdmc.sbviewreat.action.ActLoadDettaglioSbViewReat&IdSbViewReat=<%=lSbViewReat.getIdPren()%>&TornaQui=20">
                     <img src="/images/dettagli.gif" width="12" height="12" alt="Dettagli" border="0">
                   </a>
 
               
                 </td>
 
                 <td>
                   <a href="Main.jsp?Action=siap.bdmc.sbviewreat.action.ActLoadModificaSbViewReat&IdSbViewReat=<%=lSbViewReat.getIdPren()%>&TornaQui=20">
                     <img src="/images/modifica.gif" alt="Modifica" width="12" height="12" border="0">
                   </a>
                 </td>
 
                 <td>
                   <a href="Javascript:conferma('siap.bdmc.sbviewreat.action.ActLoadCancellaSbViewReat','IdSbViewReat','<%=lSbViewReat.getIdPren()%>');">
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