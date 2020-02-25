<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.lang.String" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Vector" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.siep.verbale.action.ICostantiVerbale" %>
<%@ page import="siap.siep.rinnovo.action.ICostantiRinnovo" %>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>

<jsp:useBean id="verbale" scope="request" class="siap.siep.verbale.model.VerbaleModel"  />
<jsp:useBean id="rinnovo" scope="request" class="siap.siep.rinnovo.model.RinnovoModel"  />
<jsp:useBean id="Notifica" scope="request" class="java.lang.String"  />
<jsp:useBean id="Ufficiali" scope="request" class="java.lang.String"  />
<jsp:useBean id="Rinnovo" scope="request" class="java.lang.String"  />

<html>
<head>
<title>[S.I.E.S.] - Omessa Notifica</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
<script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
</head>
<body class="corpo">
<form name="LoadDTOEOmessaNotifica" method="POST" action="/jsp/Main.jsp">
<table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
  <%if(Notifica != null && Notifica.equals("FP"))
    {%>
        <font class="campo">Dettaglio Rinnovo Ricerche per Omessa Notifica/Ordine Esecuziuone Forza di Polizia</font>
  <%}else if(Notifica != null && Notifica.equals("UG")){%>
        <font class="campo">Dettaglio Rinnovo Ricerche per Omessa Notifica/ Ufficiali Giudiziari</font>
     <%}%>
      </td>
<%if ((rinnovo.getFlagDocumentoRegistrato()!=null && rinnovo.getFlagDocumentoRegistrato().compareTo("N")==0)
       || rinnovo.getFlagDocumentoRegistrato()==null )
     {%>
     <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
      <%--td class="LBG">
        <a href="/jsp/Main.jsp?Action=siap.siep.notifica.action.ActStampaOmessaNotifica&idrinnovo=<%=rinnovo.getIdRinnovo()%>&notifica=<%=Notifica%>&ufficiali=<%=Ufficiali%>" onclick="javascript:lookUpload();">
           <img  align="middle" src="/images/print24.gif" alt="Generazione Stampa" width="24" height="24" border="0">
        </a>
      </td--%>
<!-- BOTTONE DI STAMPA -->
   <jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
     <jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.notifica.action.ActStampaOEOmessaNotifica&idrinnovo="+rinnovo.getIdRinnovo()+"&notifica="+Notifica+"&ufficiali="+Ufficiali%>"/>
   </jsp:include>
     <%}%>
     </tr>
</table>
 <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>

<table width="90%">
  <%if(Notifica != null && Notifica.equals("FP"))
    {%>
     <tr>
      <td class="l" width="30%">Data pervenimento del verbale</td>
      <td class="l"><font class="campo"><%=DateUtils.getDateToString(verbale.getDataPervenimento(),"dd-MM-yyyy")%></font> </td>
	 	</tr>

    <tr>
       <td class="l" >Data verbale</td>
       <td class="l"><font class="campo"><%=DateUtils.getDateToString(verbale.getDataEmissione(),"dd-MM-yyyy")%></font> </td>
    </tr>

      <tr>
        <td class="l">Autorità che ha redatto il verbale</td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(verbale.getDescrTipoUfficioFirmatario())%></font> di
         <font class="campo"><%=StringUtils.toStringJSP(verbale.getDescrLuogoUfficioFirmatario())%></font>
      </td>
      </tr>

     <%if(Rinnovo != null && Rinnovo.equals("RS"))
      {%>
      <tr>
       <td class="l" >Rinnovo stessa autorità in data </td>
              <td class="l"><font class="campo"><%=DateUtils.getDateToString(rinnovo.getDataRinnovo(),"dd-MM-yyyy")%></font> </td>
       </tr>
      <%}else if(Rinnovo != null && Rinnovo.equals("RA"))
        {%>
          <tr>
           <td class="l">Rinnovo altra autorità in data</td>
           <td class="l"><font class="campo"><%=DateUtils.getDateToString(rinnovo.getDataRinnovo(),"dd-MM-yyyy")%></font> </td>
         </tr>
         <tr>
          <td class="l">Autorità di polizia delegata</td>
          <td class="l"><font class="campo"><%=StringUtils.toStringJSP(rinnovo.getDescrTipoAutoritaRinnovo())%></font> di
           <font class="campo"><%=StringUtils.toStringJSP(rinnovo.getDescrLuogoRinnovo())%></font>
          </td>
         </tr>
     <%if(rinnovo.getNote() != null)
       {%>
         <tr>
           <td class="l">Indirizzo</td>
           <td class="l"><font class="campo"><%=StringUtils.toStringJSP(rinnovo.getNote())%></font> </td>
         </tr>
      <%}
    }%>


<%}else if(Notifica != null && Notifica.equals("UG"))
{%>

      <tr>
         <td class="l" width="30%">Relata di notifica da Ufficiali Giudiziari di</td>
         <td class="L"><font class="campo"><%=StringUtils.toStringJSP(verbale.getDescrLuogoUfficioFirmatario())%></font></td>
         <td class="l" >in data :</td>
         <td class="l"><font class="campo"><%=DateUtils.getDateToString(verbale.getDataEmissione(),"dd-MM-yyyy")%></font> </td>
      </tr>

     <%if(Ufficiali != null && Ufficiali.equals("RN"))
      {%>
        <tr>
          <td class="l" >Rinnovo notifica in data</td>
          <td class="l"><font class="campo"><%=DateUtils.getDateToString(rinnovo.getDataRinnovo(),"dd-MM-yyyy")%></font> </td>
        <tr>
        <tr>
         <td class="l">Ufficiali Giudiziari delagati in</td>
         <td class="l"><font class="campo"><%=StringUtils.toStringJSP(rinnovo.getDescrLuogoRinnovo())%></font></td>
        </tr>

       <%}else if(Ufficiali != null && Ufficiali.equals("AR"))
         {%>
           <tr>
            <td class="l">Attivazione ricerca in data</td>
            <td class="l"><font class="campo"><%=DateUtils.getDateToString(rinnovo.getDataRinnovo(),"dd-MM-yyyy")%></font> </td>
           </tr>
           <tr>
            <td class="l">Autorità di polizia delegata</td>
            <td class="l"><font class="campo"><%=StringUtils.toStringJSP(rinnovo.getDescrTipoAutoritaRinnovo())%></font> di
             <font class="campo"><%=StringUtils.toStringJSP(rinnovo.getDescrLuogoRinnovo())%></font>
           </td>
          </tr>
      <%}
       if(rinnovo.getNuovoLuogoNotifica() != null)
       {%>

         <tr>
           <td class="l">Luogo Nuova Notifica</td>
           <td class="l"><font class="campo"><%=StringUtils.toStringJSP(rinnovo.getNuovoLuogoNotifica())%></font> </td>
         </tr>
<%    }
 }%>
</table>
</form>
 <br>
  <div align=left style="visibility:hidden" id="upld">
    <FORM name="comandi" enctype="multipart/form-data" method="post" onSubmit="return controllaUpload();">
      <table>
        <tr>
          <td class="L">Valida Documento</td>
          <td class="L">
            <input type=checkbox name="<%=ICostantiRinnovo.CAMPO_VALIDA%>" value=1>
          </td>
        </tr>
        <tr>
          <td class="l" rowspan=2>Richiesta Certificato da Salvare</td>
          <td class="L">
            <font class="campo">
            <input type=file size="35" name="<%=ICostantiRinnovo.CAMPO_DOC_BLOB%>"></font>
          </td>
        </tr>
        <tr>
          <td class="L">
            <input  class=bottone  type="submit" value="Conferma">
            <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.notifica.action.ActUploadOmessaNotifica">
            <input type="HIDDEN" name="<%=ICostantiRinnovo.CAMPO_ID_RINNOVO%>" value="<%=rinnovo.getIdRinnovo()%>">
            <input type="HIDDEN" name="Notifica"  value="<%=Notifica%>">
            <input type="HIDDEN" name="Ufficiali" value="<%=Ufficiali%>">
            <input type="HIDDEN" name="Rinnovo"   value="<%=Rinnovo%>">
            <input type="HIDDEN" name="<%=ICostantiNotifica.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.notifica.action.ActLoadDettaglioOEOmessaNotifica">
          </td>
        </tr>
      </table>
</form>
</div>
</body>
</html>