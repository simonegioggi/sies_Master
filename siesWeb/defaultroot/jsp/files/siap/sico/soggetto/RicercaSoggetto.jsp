<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>

<jsp:useBean id="UtenteConnesso" 	scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="soggetti" 			scope="request" class="java.util.Vector" />
<jsp:useBean id="AzioneChiamante" 	scope="request" class="java.lang.String" />

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - RicercaSoggetto</title>
    <script language="JavaScript" src="/html/conferma.js"></script>
    <script language="JavaScript">
      var desktop;
      function ElencoAlias(a_id_soggetto)
      {
        desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.siep.alias.action.ActRicercaAliasSoggetto&<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>="+a_id_soggetto, "Elenco_Alias","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=600,height=250");
      }
  </script>

  </head>

  <body class="corpo">

  <form method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>" >
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class=label>Funzione :</font> <font class=campo>Elenco Soggetti</font></td>
    </tr>
  </table>


  <jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>

  <br>

  <div align=center>
  <table>
    <tr>
      <td class="int">Cognome Nome</td>
      <td class="int" width=5%>Sesso</td>
      <td class="int">Data Nascita</td>
      <td class="int">Eta' Presunta</td>
      <td class="int">Luogo Nascita</td>
      <td class="int">Paternità</td>
      <td class="int">Codice CUI</td>
      <td class="int">Anno Numero</td>
      <td class="int">Ufficio</td>
      <td class="int" width=5%>Azioni</td>
    </tr>

<%
  Iterator itx = soggetti.iterator();

  while ( itx.hasNext())
  {
   // SoggettoModel soggetto = (SoggettoModel)itx.next();
      FascicoloSiepModel lSogFas = (FascicoloSiepModel)itx.next();
%>
    <tr>
    	<%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
      	<td class=l><%=lSogFas.getSoggetto().getCognome()%>&nbsp;<%=lSogFas.getSoggetto().getNome()%>&nbsp;
          <!-- Link alla pop-up che visualizza l'elenco degli alias -->
          <%
          if(lSogFas.getSoggetto().getSogIdSoggetto() != null && lSogFas.getSoggetto().getSogIdSoggetto().length() > 0)
          {
          %>
              <a href="Javascript:ElencoAlias('<%= lSogFas.getSoggetto().getIdSoggetto() %>');">
                  <img src="/images/expand.gif" alt="Elenco Alias" border="0">
              </a>
          <%
          }
          %>
      </td>
      <td class=c><%=lSogFas.getSoggetto().getSesso()%></td>
      <td class=c>

      <font class="campo">
<%
        if(lSogFas.getSoggetto().getDataNascita() != null)
        {
%>
          <%=DateUtils.getDateToString(lSogFas.getSoggetto().getDataNascita(),"dd-MM-yyyy")%>&nbsp;
<%
        }
        else
        {
%>
          <%="**-"+StringUtils.toStringJSP(lSogFas.getSoggetto().getMeseNascita(), "**")+"-"+StringUtils.toStringJSP(lSogFas.getSoggetto().getAnnoNascita(), "****")%>&nbsp;

<%
        }
%>
        </font>
<%
       if(lSogFas.getSoggetto().getDataNascita() != null && lSogFas.getSoggetto().getDataNascitaPresunta() != null && lSogFas.getSoggetto().getDataNascitaPresunta().equals("S"))
        {
%>
          <font class="campo"> (Data Presunta)</font>
<%      }

%>

      </td>

      <td class=c>
	      <font  class=c>
			<%
	        if(lSogFas.getSoggetto().getEtaPresuntaAnni() != null || lSogFas.getSoggetto().getEtaPresuntaMesi() != null)
	        {
			%>
	        	<%="Anni "+StringUtils.toStringJSP(lSogFas.getSoggetto().getEtaPresuntaAnni(), "0")+"  Mesi "+StringUtils.toStringJSP(lSogFas.getSoggetto().getEtaPresuntaMesi(), "0")%>
			<%
	        }
	        else
	        {
			%>
	          	<%="-"%>	
			<%
	        }
			%>
	        </font>
      </td>

      <%if (lSogFas.getSoggetto().getDescrComuneNascita().compareTo("-")==0)
        {%>
        <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
        <%--td class="l"><-%=lSogFas.getSoggetto().getDescComuneNascitaEstero()%>  (<-%=lSogFas.getSoggetto().getDescrStatoNascita().toUpperCase()%>)&nbsp; </td--%>
         <%if (lSogFas.getSoggetto().getDescComuneNascitaEstero().compareTo("------------------------------")==0 || lSogFas.getSoggetto().getDescComuneNascitaEstero().equals("") )
        {%>
        <td class="l">
          -
        </td>
         <%}else{%>
           <td class="l">
          <%=StringUtils.pulisciCampo(lSogFas.getSoggetto().getDescComuneNascitaEstero(),lSogFas.getSoggetto().getDescrStatoNascita().toUpperCase())%>  (<%=lSogFas.getSoggetto().getDescrStatoNascita().toUpperCase()%>)&nbsp;
        </td>

          <%}%>
      <%}else
        {%>
        <td class="l"><%=lSogFas.getSoggetto().getDescrComuneNascita()%> (<%=lSogFas.getSoggetto().getCodProvinciaNascita()%>)&nbsp;</td>
      <%}%>
      <td class=c><%=lSogFas.getSoggetto().getPaternita()%>&nbsp;</td>
     
      <td class=c>            
	        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.soggetto.action.ActRicercaSoggettoCodCui&<%=ICostantiSoggetto.CAMPO_COD_AFIS%>=<%=lSogFas.getSoggetto().getCodAfis()%>" title="Soggetto">
	          <%=StringUtils.toStringJSP(lSogFas.getSoggetto().getCodAfis())%>&nbsp;
	        </a>      	
	  </td>
 
<%
        if(lSogFas.getChiaveProgr() != null)
        {
%> 
 				<td class=c><%=StringUtils.toStringJSP(lSogFas.getChiaveAnno())%>/<%=StringUtils.toStringJSP(lSogFas.getChiaveProgr())%></td>
<% 		}
 		else			
 		{ 	
%>		
 		        <td class="l">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; - </td>
   <%	} %>     	
      	<td class=c><%=lSogFas.getDescrTipoUfficio()%>&nbsp;<%=lSogFas.getDescrComuneUfficio()%></td>
 
      <td class=c>
      <%
      String modificabile = "";
      if (UtenteConnesso.getUfficioUtente().getCodUfficio().equals(lSogFas.getSoggetto().getCodUfficioInserimento()))
       {modificabile = "SI";}
       else
        {modificabile = "NO";}
      %>
		<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
        <%--jsp:include page="<%=ICostantiSoggetto.PG_BUTTONS%>"--%>
        <jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
           <jsp:param name="CampoAzioneChiamante" value="NomeAzione" />
           <jsp:param name="ValoreAzioneChiamante" value="<%=AzioneChiamante%>" />
           <jsp:param name="CampoIdEntita" value="<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>" />
           <jsp:param name="ValoreIdEntita" value="<%=lSogFas.getSoggetto().getIdSoggetto()%>" />
           <jsp:param name="Modificabile" value="<%=modificabile%>" />

        </jsp:include>
      </td>
    </tr>
<%
  }
%>
    </table>


</div>
</form>
  <br>


  </body>
</html>