<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Arrays"%>


<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato" %>
<%@ page import="siap.siep.modulocumulo.action.ICostantiProcedimentoCumulato" %>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo" %>

<%@ page import="siap.siep.modulocumulo.model.ProcedimentoCumulatoModel" %>

<jsp:useBean id="TitoloInCumulo" scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>

<%
//==============================================================================
// Form di visualizzazione del dettaglio del singolo titolo cumulato.
// Viene importata nelle finestre di gestione dei dati analitici
//==============================================================================
%>
<%
  ProcedimentoCumulatoModel lProcedimentoCumulatoModel = TitoloInCumulo.getProcedimentoCumulato();
%>

  <table cellspacing="1" cellpadding="1" width="95%" align="center">
    <%
    //============================================================================
    // DATI DEL TITOLO SENTENZA - DECRETO PENALE - DECRETO - ORDINANZA - SENTENZA STRANIERA - CUMULO
    //============================================================================
    %>
    <tr>
      <td width="90%" class="Titolo">Titolo &nbsp;&nbsp;      
      <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.modulocumulo.action.ActLoadGrigliaDatiAnalitici&<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>=<%=TitoloInCumulo.getIdTitoloCumulato()%>&<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>=<%=TitoloInCumulo.getIstrIdIstruttoriaCumulo()%>" title="Titolo">
        (dettaglio)</a>&nbsp;
      </td>
    </tr>
    <tr>
      <td class="L">
        <%
        String lDescrTitolo = TitoloInCumulo.getDescrTipoProvvedimento();
        if ("02".equals (TitoloInCumulo.getCodTipoProvvedimento())) {
          String [] lUfficiSorv = new String[] {"UDS","TDS","UDSM"};
          if (!Arrays.asList(lUfficiSorv).contains(TitoloInCumulo.getCodTipoAutoritaEmittente())){
            // Rimappo il codice per poterlo gestire nella jsp
            lDescrTitolo = "Decreto Penale";
          }
        }          
        
        %>
        <font class="label"><%=lDescrTitolo.substring(0,1).toUpperCase()+lDescrTitolo.substring(1).toLowerCase()%></font>&nbsp;:<font class="label"> N.</font>
        <font class="campo">
          <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.modulocumulo.action.ActLoadDettaglioTitoloCumulato&<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>=<%=TitoloInCumulo.getIdTitoloCumulato()%>&<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>=<%=TitoloInCumulo.getIstrIdIstruttoriaCumulo()%>" title="Titolo">
            <%=TitoloInCumulo.getAnnoSentenza()%> / <%=TitoloInCumulo.getNumeroSentenza()%> 
          </a>&nbsp;
        </font>
        <font class="label"> del</font>&nbsp;
        <font class="campo"><%=DateUtils.getDateToString(TitoloInCumulo.getDataProvvedimento(), "dd-MM-yyyy")%></font>
        
        <% if(!TitoloInCumulo.getCodTipoProvvedimento().equals("02")) { %> 
        &nbsp;<font class="label"> Emessa da: </font> 
        <% } else {%>
        &nbsp;<font class="label"> Emesso da: </font>
        <% } %>
        <font class="campo"><%=TitoloInCumulo.getDescrTipoAutoritaEmittente()%></font>&nbsp;
        
        <% if (TitoloInCumulo.getNumSezioneAutoritaEmittente() != null) { %>
          <font class="label">(Sez.</font> <font class="campo"><%=TitoloInCumulo.getNumSezioneAutoritaEmittente()%> </font> <font class="label">) </font>
        <% } %>
        
        <font class="label"> di </font>
        <font class="campo"><%=TitoloInCumulo.getDescrLuogoEmittente()%></font>
      </td>
    </tr>
    
    <% if (TitoloInCumulo.getDataIrrevocabilita()!=null) { %>
    <tr>
        <td class="L">
          <font class="label">Data irrevocabilità : </font>&nbsp;
          <!-- N.B. la data irrevocabilità è quella del fascicolo in quanto sulla sentenza potrebbero essere coinvolti + soggetti -->
          <font class="campo"><%=DateUtils.getDateToString(TitoloInCumulo.getDataIrrevocabilita(), "dd-MM-yyyy")%></font> 
        </td>
    </tr>
    <% } %>  
    
    <%
    //============================================================================
    // DATI DEL FASCICOLO (SE TITOLO <> CUMULO)
    //============================================================================
    %>
    <% if (lProcedimentoCumulatoModel!=null) { %>
    <tr>
      <td class="L">
        <font class="label">Iscritta all'Anno/Numero Siep</font>
        <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.modulocumulo.action.ActLoadDettaglioProcedimentoCumulato&<%=ICostantiProcedimentoCumulato.CAMPO_ID_PROCEDIMENTO_CUMULATO%>=<%=lProcedimentoCumulatoModel.getIdProcedimentoCumulato()%>&<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>=<%=TitoloInCumulo.getIstrIdIstruttoriaCumulo()%>" title="Procedimento">
          <%=lProcedimentoCumulatoModel.getChiaveAnnoFasCumulato()%>
          /
          <% if (!"S".equals (lProcedimentoCumulatoModel.getFlagAccorpato()) ) { %>
          <%=lProcedimentoCumulatoModel.getChiaveProgrFasCumulato()%></a>
          <% } else { %>
          <%=lProcedimentoCumulatoModel.getChiaveProgrOrigine()%></a>
          <% } %>
        <% if ("S".equals (lProcedimentoCumulatoModel.getFlagAccorpato()) ) { %>
        <font class="cRosso">(Ex <%=lProcedimentoCumulatoModel.getUfficioOrigine().getCodTipoUfficio()%> 
        di <%=lProcedimentoCumulatoModel.getUfficioOrigine().getDescrComune()%>)</font>
        <% } %>
        &nbsp;
        <%
        String AutoritaSiep = StringUtils.toStringJSP(lProcedimentoCumulatoModel.getDescrTipoUfficioFasCumulato())+" di "+StringUtils.toStringJSP(lProcedimentoCumulatoModel.getDescrLuogoUfficioFasCumulato());
        %>  
        <font class="label"> di </font>
        <font class="campo"><%=AutoritaSiep%></font>        
      </td>
    </tr>
    <% } %>
  </table>