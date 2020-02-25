<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Collection"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.siep.posizionematerialefasc.model.PosizioneMaterialeFascModel"%>
<%@ page import="siap.siep.posizionematerialefasc.action.ICostantiPosizioneMaterialeFasc"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>

<jsp:useBean id="UtenteConnesso"           scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="posizioni"                scope="request" class="java.util.Vector"/>
<jsp:useBean id="fascicolo"                scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel" />
<jsp:useBean id="fascicoloSiusGP"          scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel" />
<jsp:useBean id="FascicoloSigeEsteso"      scope="session" class="siap.sige.fascicolo.model.FascicoloSigeEstesoModel" />
<jsp:useBean id="tipo_posizione_materiale" scope="request" class="java.lang.String"/>
<jsp:useBean id="isModificabile"           scope="request" class="java.lang.String" />

<%
// Viene valorizzato il flag per distinguere il caso Posizione Materiale fascicolo SIUS/SIGE
// da quello SIEP di default.
  boolean lCasoSius = false;
  boolean lCasoSige = false;
  if (tipo_posizione_materiale != null && tipo_posizione_materiale.equalsIgnoreCase("SIUS")){
      lCasoSius = true;
  } else if (tipo_posizione_materiale != null && tipo_posizione_materiale.equalsIgnoreCase("SIGE")){
	  lCasoSige = true;
  }
   // ID Fascicolo SIEP, SIUS o SIGE in dipendenza del caso.
   BigDecimal lIdFascicolo;
   if (lCasoSius){
     lIdFascicolo = fascicoloSiusGP.getFascicoloSiusModel().getIdFascicoloSius();
   } else if (lCasoSige){
	 lIdFascicolo  = FascicoloSigeEsteso.getFascicoloSige().getIdFascicoloSige();
   } else {
     lIdFascicolo = fascicolo.getIdFascicoloSiep();
   }
%>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Lista Posizioni Materiale associate ad un Fascicolo</title>
    <script language="JavaScript" src="/html/conferma.js"></script>
  </head>

  <body class="corpo">

    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class=label>Funzione :</font> <font class=campo> Posizioni materiali associate al fascicolo</font> </td>
          <td class="LBG">
<% if (lCasoSius) { %>
           <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>">
           <jsp:param name="CampoIdEntita" value="<%=ICostantiPosizioneMaterialeFasc.CAMPO_FAS_SIE_ID_FASCICOLO_SIEP%>" />
           <jsp:param name="ValoreIdEntita" value="<%=lIdFascicolo.toString()%>" />
           <jsp:param name="Modificabile" value="<%=isModificabile%>"/>
           <jsp:param name="tipo_posizione_materiale" value="<%=tipo_posizione_materiale%>"/>
           </jsp:include>

<% } else if (lCasoSige) { %>
           <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>">
           <jsp:param name="CampoIdEntita" value="<%=ICostantiPosizioneMaterialeFasc.CAMPO_FAS_SIE_ID_FASCICOLO_SIEP%>" />
           <jsp:param name="ValoreIdEntita" value="<%=lIdFascicolo.toString()%>" />
           <jsp:param name="Modificabile" value="<%=isModificabile%>"/>
           <jsp:param name="tipo_posizione_materiale" value="<%=tipo_posizione_materiale%>"/>
           </jsp:include>
<% } else { %>
            <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>">
            <jsp:param name="CampoIdEntita" value="<%=ICostantiPosizioneMaterialeFasc.CAMPO_FAS_SIE_ID_FASCICOLO_SIEP%>" />
            <jsp:param name="ValoreIdEntita" value="<%=lIdFascicolo.toString()%>" />
            </jsp:include>
<% } %>
          </td>
         <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
      </tr>
    </table>
    <br>
<%
   if (lCasoSius){
%>
       <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
<%
   } else if (lCasoSige){
%>
	   <jsp:include page="<%=ICostantiFascicoloSige.PG_LOAD_SINTESIPROCEDIMENTOSIGE%>"/>
<% } else { %>
      <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<% } %>
    <br>
<% if (posizioni != null && posizioni.size() > 0)
{ %>
  <table>
     <div align=center>
        <tr>
          <td class="int" width=8%>Codice</td>
          <td class="int" width=20%>Descrizione</td>
          <td class="int" width=20%>Stato del Procedimento</td>
          <td class="int" width=16%>Data Inizio</td>
          <td class="int" width=16%>Data Fine</td>
          <td class="int" width=5%>Azioni</td>
        </tr>
      </div>
<%
  boolean bottone = true;
  Iterator itx = posizioni.iterator();
  while ( itx.hasNext())
  {
    PosizioneMaterialeFascModel lPosMatFasc = (PosizioneMaterialeFascModel)itx.next();
%>
    <tr>
      <td class=c><%=lPosMatFasc.getCodPosizioneMateriale()%></td>
      <td class=c><%=lPosMatFasc.getDescrPosizioneMateriale()%></td>
      <td class=c><%=lPosMatFasc.getDescrStatoProcedimento()%></td>
      <td class=c><%=DateUtils.getDateToString(lPosMatFasc.getDataInizio(),"dd-MM-yyyy")%></td>
      <td class=c><%=StringUtils.toStringJSP(DateUtils.getDateToString(lPosMatFasc.getDataFine(),"dd-MM-yyyy"), "-")%></td>
     <td class=c>
<% if (bottone)
   {
      if (lCasoSius){
%>
        <jsp:include page="<%=ICostantiPosizioneMaterialeFasc.PG_BUTTONS%>">
            <jsp:param name="CampoIdEntita" value="<%=ICostantiPosizioneMaterialeFasc.CAMPO_FAS_SIE_ID_FASCICOLO_SIEP%>" />
            <jsp:param name="ValoreIdEntita" value="<%=lIdFascicolo.toString()%>" />
            <jsp:param name="Modificabile" value="<%=isModificabile%>"/>
        </jsp:include>
<%    
      } else if (lCasoSige){
%>
        <jsp:include page="<%=ICostantiPosizioneMaterialeFasc.PG_BUTTONS%>">
            <jsp:param name="CampoIdEntita" value="<%=ICostantiPosizioneMaterialeFasc.CAMPO_FAS_SIE_ID_FASCICOLO_SIEP%>" />
            <jsp:param name="ValoreIdEntita" value="<%=lIdFascicolo.toString()%>" />
            <jsp:param name="Modificabile" value="<%=isModificabile%>"/>
        </jsp:include>
<%   } else { %>
        <jsp:include page="<%=ICostantiPosizioneMaterialeFasc.PG_BUTTONS%>">
            <jsp:param name="CampoIdEntita" value="<%=ICostantiPosizioneMaterialeFasc.CAMPO_FAS_SIE_ID_FASCICOLO_SIEP%>" />
            <jsp:param name="ValoreIdEntita" value="<%=lIdFascicolo.toString()%>" />
        </jsp:include>
<%   }
     bottone = false;
  } %>
      </td>
    </tr>
<%
  }
%>
    </table>
<% } else { %>
  <br>
<font class="campo"> Nessuna posizione materiale associata al fascicolo </font>
<% } %>
  </body>
</html>