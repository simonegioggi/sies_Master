<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>
<%@ page import="java.math.BigDecimal" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>

<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.siep.misurasicurezza.model.MisuraSicurezzaModel" %>
<%@ page import="siap.siep.misurasicurezza.model.MisuraSicurezzaFascicoloModel" %>
<%@ page import="siap.siep.misurasicurezza.action.ICostantiMisuraSicurezza" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />

<jsp:useBean id="fascicolimisurasicurezza" scope="request" class="java.util.ArrayList" />

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Elenco Procedimenti con Misura di Sicurezza</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  </head>

  <body class="corpo">
  <FORM method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;
        <font class="campo">Elenco Procedimenti con Misura di Sicurezza</font></td>
      </tr>
    </table>
    <br>
      <jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
    <br>
    <br>
    <table cellpadding=2 cellspacing=2>
      <tr>
        <td class="int">Numero SIEP</td>
        <td class="int">Natura Misura</td>
        <td class="int">Tipo Misura</td>
        <td class="int">Durata Misura</td>
        <td class="int">Azioni</td>
      </tr>

<%
    int lIndiceMisure =0;
    Iterator itx = fascicolimisurasicurezza.iterator();
    while ( itx.hasNext())
    {
      MisuraSicurezzaFascicoloModel lFasMis = (MisuraSicurezzaFascicoloModel)itx.next();
      FascicoloSiepModel lFas = lFasMis.getFascicoloSiep();
      MisuraSicurezzaModel lMis = new MisuraSicurezzaModel();
      if( !lFasMis.getMisureSicurezza().isEmpty() )
      {
        lMis = (MisuraSicurezzaModel)lFasMis.getMisureSicurezza().get(0);
      }
      
      String lIsMigratoSenzaValori = "N";
      
      if (lMis.isDurataZero() && "-".equals(lMis.getCodTipo()) && lMis.getCodOperatoreInserimento().startsWith("res-"))
        lIsMigratoSenzaValori = "S";
      else
        lIsMigratoSenzaValori = "N";
%>
      <tr>
        <td class="c"><%=StringUtils.toStringJSP(lFas.getChiaveAnno())%>/<%=StringUtils.toStringJSP(lFas.getChiaveProgr())%></td>
        <td class="c"><%=StringUtils.toStringJSP(lMis.getDescrNatura())%>&nbsp;</td>
        <td class="c"><%=StringUtils.toStringJSP(lMis.getDescrTipo())%>&nbsp;</td>
        <td class="c">&nbsp;
<%
        if(lMis.getNumAnni() != null && (new BigDecimal(0)).compareTo(lMis.getNumAnni()) != 0 )
        {
%>
          AA: <%=StringUtils.toStringJSP(lMis.getNumAnni())%> 
<%
        }

        if(lMis.getNumMesi() != null && (new BigDecimal(0)).compareTo(lMis.getNumMesi()) != 0 )
        {
%>
          MM: <%=StringUtils.toStringJSP(lMis.getNumMesi())%> 
<%
        }

        if(lMis.getNumGiorni() != null && (new BigDecimal(0)).compareTo(lMis.getNumGiorni()) != 0 )
        {
%>
          GG: <%=StringUtils.toStringJSP(lMis.getNumGiorni())%> 
<%
        }
%>
        </td>
        <td class="c">
          <jsp:include page="<%=ISIAPCostantiWeb.PG_BUTTONS_MISURA_SICUREZZA%>">
            <jsp:param name="CampoIdEntita" value="<%=ICostantiMisuraSicurezza.CAMPO_ID_MISURA_SICUREZZA%>"/>
            <jsp:param name="ValoreIdEntita" value="<%=lMis.getIdMisuraSicurezza()%>"/>
            <jsp:param name="FlagValidato" value="<%=lFas.getFlagValidato()%>" />
            <jsp:param name="CampoAzioneChiamante" value="<%=IWebConstants.LINK_RITORNO%>" />
            <jsp:param name="ValoreAzioneChiamante" value="<%="siap.siep.misurasicurezza.action.ActRicercaFascicoliMisuraSicurezza"%>" />
            <jsp:param name="IsMigratoSenzaValori" value="<%=lIsMigratoSenzaValori%>" />  
          </jsp:include>
        </td>
        <% if ("S".equals(lIsMigratoSenzaValori)) { %>
        <td>
          <img src="/images/attenzione.jpg" width="12" height="12" alt="Attenzione" border="0" title="Il dato risulta migrato RES ma  senza dati significativi">
        </td>
        <% } %>
      </tr>     
<%
    if( lFasMis.getMisureSicurezza() != null && lFasMis.getMisureSicurezza().size() > 1 )
    {
        MisuraSicurezzaModel lAltraMis = new MisuraSicurezzaModel();
        List lListaAltreMisure = lFasMis.getMisureSicurezza();
        for(int i=1; i < lListaAltreMisure.size(); i++)
        {
          lAltraMis = (MisuraSicurezzaModel)lListaAltreMisure.get(i);
          
          if (lAltraMis.isDurataZero() && "-".equals(lAltraMis.getCodTipo()) && lAltraMis.getCodOperatoreInserimento().startsWith("res-"))
            lIsMigratoSenzaValori = "S";
          else
            lIsMigratoSenzaValori = "N";
          
          
%>
            <tr>
              <td>&nbsp;</td>
              <td class="c"><%=StringUtils.toStringJSP(lAltraMis.getDescrNatura())%>&nbsp;</td>
              <td class="c"><%=StringUtils.toStringJSP(lAltraMis.getDescrTipo())%>&nbsp;</td>
              <td class="c">&nbsp;
<%
                if(lAltraMis.getNumAnni() != null && (new BigDecimal(0)).compareTo(lAltraMis.getNumAnni()) != 0 )
                {
%>
                  AA: <%=StringUtils.toStringJSP(lAltraMis.getNumAnni())%> 
<%
                }
        
                if(lAltraMis.getNumMesi() != null && (new BigDecimal(0)).compareTo(lAltraMis.getNumMesi()) != 0 )
                {
%>
                  MM: <%=StringUtils.toStringJSP(lAltraMis.getNumMesi())%> 
<%
                }
        
                if(lAltraMis.getNumGiorni() != null && (new BigDecimal(0)).compareTo(lAltraMis.getNumGiorni()) != 0 )
                {
%>
                  GG: <%=StringUtils.toStringJSP(lAltraMis.getNumGiorni())%> 
<%
                }
                
                lIndiceMisure++;
%>
              </td>
              <td class="c">
                <jsp:include page="<%=ISIAPCostantiWeb.PG_BUTTONS_MISURA_SICUREZZA%>">
                  <jsp:param name="CampoIdEntita" value="<%=ICostantiMisuraSicurezza.CAMPO_ID_MISURA_SICUREZZA%>"/>
                  <jsp:param name="ValoreIdEntita" value="<%=lAltraMis.getIdMisuraSicurezza()%>"/>
                  <jsp:param name="FlagValidato" value="<%=lFas.getFlagValidato()%>" />
                  <jsp:param name="CampoAzioneChiamante" value="<%=IWebConstants.LINK_RITORNO%>" />
                  <jsp:param name="ValoreAzioneChiamante" value="<%="siap.siep.misurasicurezza.action.ActRicercaFascicoliMisuraSicurezza"%>" />
                  <jsp:param name="IsMigratoSenzaValori" value="<%=lIsMigratoSenzaValori%>" />  
                </jsp:include>
              </td>
              <% if ("S".equals(lIsMigratoSenzaValori)) { %>
              <td>
                <img src="/images/attenzione.jpg" width="12" height="12" alt="Attenzione" border="0" title="Il dato risulta migrato RES ma  senza dati significativi">
              </td>
              <% } %>              
            </tr> 
<%
          }
      }
    }
%>
    </table>
  </FORM>
</body>
</html>