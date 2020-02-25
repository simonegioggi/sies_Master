<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<%@ page import="java.math.BigDecimal" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.log.LogF3B"%>
<%@ page import="java.util.Date" %>

<%@ page import="siap.siep.annotazionemanuale.model.AnnotazioneManualeModel"%>
<%@ page import="siap.siep.annotazionemanuale.model.AnnotazioneOrdinanzaModel"%>
<%@ page import="siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<jsp:useBean id="OrdinanzaGE"     scope="request" class="siap.siep.annotazionemanuale.model.AnnotazioneOrdinanzaModel" />
<jsp:useBean id="lPageGE"         scope="request" class="java.lang.String" />
<jsp:useBean id="ProcedimentoGE"  scope="request" class="siap.sige.fascicolo.model.FascicoloSigeModel"/>

<%
//==============================================================================
// Dettaglio dell'Annotazione inserita con una Ordinanza del GE legata
//  ad una richiesta del PM:
// ============================================================================
 
  
  if(OrdinanzaGE.getAnnotazioneManuale() != null && OrdinanzaGE.getEvento() != null)
  {
    AnnotazioneManualeModel OrdinanzaGEAnn = OrdinanzaGE.getAnnotazioneManuale();
    EventoModel OrdinanzaGEEve = OrdinanzaGE.getEvento();
 
    // Default AMNISTIA/INDULTO
    if (lPageGE == null || lPageGE.trim().length() == 0)
      lPageGE = "AMNI";
   
    
    %>
  <table width="80%">
    <tr><td colspan=3 class="Titolonocap">Decisione del Giudice dell' Esecuzione</td></tr>
    <tr>  
      <td class="l">Declaratoria :</td>
      
      <%if(ProcedimentoGE!=null && ProcedimentoGE.getIdFascicoloSige()!=null) {  %>
      <td class="l">
        Anno/Numero Procedimento SIGE:
        &nbsp;&nbsp;<font class="campo">
            <%=StringUtils.toStringJSP( ProcedimentoGE.getChiaveAnno() )%> / <%=StringUtils.toStringJSP(ProcedimentoGE.getChiaveProgr())%>
        </font>
      </td>
      <input type="HIDDEN" name="<%=ICostantiAnnotazioneManuale.CAMPO_CHIAVE_ANNO_SIGE %>" value="<%=StringUtils.toStringJSP(ProcedimentoGE.getChiaveAnno() )%>">
      <input type="HIDDEN" name="<%=ICostantiAnnotazioneManuale.CAMPO_CHIAVE_PROGR_SIGE %>" value="<%=StringUtils.toStringJSP(ProcedimentoGE.getChiaveProgr())%>">     
      <% 
      } else {
      %>
      <td class="l">
        Anno/Numero Ordinanza:
        &nbsp;&nbsp;<font class="campo">
          <%=StringUtils.toStringJSP( OrdinanzaGEAnn.getAnnoGe() )%> / <%=StringUtils.toStringJSP(OrdinanzaGEAnn.getNumeroGe())%>
        </font>
      </td>
      <% } %>
      <td class="l">
        <font class="label">in data </font>
        &nbsp;&nbsp;
        <font class="campo">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(OrdinanzaGEAnn.getDataGE(), "dd-MM-yyyy"))%>&nbsp;
        </font>
      </td>
    </tr>
      
    <tr>
      <td class="l">Ufficio :</td>
      <td class="l" colspan=2>
        <font class="campo">
          <%=StringUtils.toStringJSP(OrdinanzaGEEve.getDescrUfficioEmittente())%>&nbsp;
        </font>
      </td>
    </tr>
    <tr>
      <td class="l">Sede :</td>
      <td class="l" colspan=2>
        <font class="campo">
          <%=StringUtils.toStringJSP(OrdinanzaGEEve.getDescrLuogoEmittente())%>&nbsp;
        </font>
      </td>
    </tr>
    <tr>
      <td class="l">      <font class="campo">
          <% 
          String lFlagConf = OrdinanzaGEAnn.getFlagConforme();
          if (lFlagConf.equalsIgnoreCase("-")){ %>
            senza richiesta del PM
         <%}else if (lFlagConf.equalsIgnoreCase("C")){ %>
         in conformita' alla richiesta del PM
          <%}else if (lFlagConf.equalsIgnoreCase("D")){ %>
         in difformita' alla richiesta del PM
         <%} %>
         </font>
      </td>
    </tr>
  </table>

  <%
  //============================================================================
  // Beneficio 
  //============================================================================
  %>
  <br>
  <table style="width: 95%;">
    <tr>
      <td colspan=9 class="Titolonocap">Contenuto Decisione</td>
    </tr>
    <%

      if(lPageGE.equals("AMNI")) { %>
      <tr>
        <td class="l">Computo beneficio :</td>
        <td class="l" >
          <font class="campo">
            <%=StringUtils.toStringJSP( OrdinanzaGEAnn.getDescrTipoAnnotazione(),"-")%>
          </font>
        </td>
        <td class="l">DPR :</td>
        <td class="l" >
          <font class="campo">
            <%=StringUtils.toStringJSP( OrdinanzaGEAnn.getDescrDpr(),"-")%>
          </font>
        </td>
      </tr>
      
      
      
      <%
      }

      if(lPageGE.equals("INCOST")) { %>
      <tr>
        <td class="l" width="45%">Sentenza Corte Costituzionale</td>
        <td class="l" >
          Anno/Numero
          <font class="campo">
          <%if(OrdinanzaGEAnn.getAnnoCc() == null || OrdinanzaGEAnn.getAnnoCc().compareTo(new BigDecimal(0))==0)  {%>
          -
          <% } else { %>
          <%=StringUtils.toStringJSP(OrdinanzaGEAnn.getAnnoCc() )%>
          <%} %>
          / <%=StringUtils.toStringJSP(OrdinanzaGEAnn.getNumeroCc(), "-")%>
         </font>
        &nbsp; in data
          &nbsp;
          <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(OrdinanzaGEAnn.getDataCC(), "dd-MM-yyyy"))%>&nbsp;
          </font>
        </td>
      </tr>
    <%
    }

    if(lPageGE.equals("DEPEN")) { %>
      <tr>
        <td class="l" width="20%">Fonte </td>
        <td class="l">
          <font class="campo">
            <%=StringUtils.toStringJSP(OrdinanzaGEAnn.getDescrFonte(),"-")%>&nbsp;
          </font>
        </td>
        <td class="l">
        Anno
        <%if(OrdinanzaGEAnn.getAnnoFonte() == null || OrdinanzaGEAnn.getAnnoFonte().compareTo(new BigDecimal(0))==0){%>
            -
        <%}else{%>
          <font class="campo"><%=StringUtils.toStringJSP(OrdinanzaGEAnn.getAnnoFonte())%></font>
        <%}%>
          Num.
          <font class="campo"><%=StringUtils.toStringJSP(OrdinanzaGEAnn.getNumeroFonte(),"-")%></font>
          Art.
          <font class="campo"><%=StringUtils.toStringJSP(OrdinanzaGEAnn.getArticolo(),"-")%></font>
        </td>
      </tr>
      <tr>
        <td class="l" width="20%">Art. Qualificante </td>
        <td class="l">
          <font class="campo">
            <%=StringUtils.toStringJSP(OrdinanzaGEAnn.getDescrSottonumerazione(),"-")%>&nbsp;</font>
        </td>
        <td class="l">
          Comma
          <font class="campo"><%=StringUtils.toStringJSP(OrdinanzaGEAnn.getComma(),"-")%></font>
          Let.
          <font class="campo"><%=StringUtils.toStringJSP(OrdinanzaGEAnn.getLettera(),"-")%></font>
          Num.
          <font class="campo"><%=StringUtils.toStringJSP(OrdinanzaGEAnn.getNumero(),"-")%></font>
        </td>
      </tr>
      <%}%>
      
      
      <tr>
<%if(lPageGE.equals("DEPEN")){%>
        <td class="l" colspan="2">Reclusione :
<%}else{%>
        <td class="l">Reclusione :
<%}%>
          Anni
          <font class="campo"><%=StringUtils.toStringJSP(OrdinanzaGEAnn.getNumAnniReclusione(),"-")%></font>
          Mesi
          <font class="campo"><%=StringUtils.toStringJSP(OrdinanzaGEAnn.getNumMesiReclusione(),"-")%></font>
          Giorni
          <font class="campo"><%=StringUtils.toStringJSP(OrdinanzaGEAnn.getNumGiorniReclusione(),"-")%></font>
        </td>
        <td class="l">Multa :
          <font class="campo">
            <%=StringUtils.toStringJSP(OrdinanzaGEAnn.getImportoMulta(),"-")%>&nbsp;
          </font>
        </td>

      </tr>
      <tr>
<%if(lPageGE.equals("DEPEN")){%>

        <td class="l" colspan="2">Arresto :
<%}else{%>
        <td class="l">Arresto :
<%}%>
          Anni
          <font class="campo"><%=StringUtils.toStringJSP(OrdinanzaGEAnn.getNumAnniArresto(),"-")%></font>
          Mesi
          <font class="campo"><%=StringUtils.toStringJSP(OrdinanzaGEAnn.getNumMesiArresto(),"-")%></font>
          Giorni
          <font class="campo"><%=StringUtils.toStringJSP(OrdinanzaGEAnn.getNumGiorniArresto(),"-")%></font>
        </td>
        <td class="l">Ammenda :
          <font class="campo">
            <%=StringUtils.toStringJSP(OrdinanzaGEAnn.getImportoAmmenda(),"-")%>&nbsp;
          </font>
        </td>

      </tr>
    </table>
  <br>
  
  <input type="HIDDEN" name="IdAnnGE" value="<%=StringUtils.toStringJSP(OrdinanzaGEAnn.getIdAnnotazioneManuale())%>">
  <input type="HIDDEN" name="annoGE" value="<%=StringUtils.toStringJSP(OrdinanzaGEAnn.getAnnoGe())%>">
  <input type="HIDDEN" name="numGE" value="<%=StringUtils.toStringJSP(OrdinanzaGEAnn.getNumeroGe())%>">
  <input type="HIDDEN" name="DaAnArr" value="<%=DateUtils.getDateToString(OrdinanzaGEAnn.getDataGE(), "yyyy")%>">
  <input type="HIDDEN" name="DaGiArr" value="<%=DateUtils.getDateToString(OrdinanzaGEAnn.getDataGE(), "dd")%>">
  <input type="HIDDEN" name="DaMeArr" value="<%=DateUtils.getDateToString(OrdinanzaGEAnn.getDataGE(), "MM")%>">
  <% } %>

</html>