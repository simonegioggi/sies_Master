<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page language="java" import="f3b.web.IWebConstants" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="f3b.log.LogF3B" %>
<%@ page import="java.util.ListIterator" %>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="java.util.Collection" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Vector"%>

<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.altracausa.action.ICostantiAltraCausa"%>
<%@ page import="siap.siep.misurasicurezza.action.ICostantiMisuraSicurezza" %>
<%@ page import="siap.siep.misurasicurezza.model.MisuraSicurezzaModel" %>
<%@ page import="siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriFascicoloSiusModel" %>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>

<jsp:useBean id="lSogg"						scope="request" class="siap.sico.soggetto.model.SoggettoModel"/>
<jsp:useBean id="ListaOrd"					scope="request" class="java.util.Vector"/>

<%
  boolean lFlagPopUp = false;
  if( (request.getParameter("PopUp") != null)&& (request.getParameter("PopUp").equals("Y")) )
  {
    lFlagPopUp = true;
  }
%>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Provvedimenti Archiviazione della Sorveglianza</title>
    <script language="JavaScript" src="/html/conferma.js"></script>
    <script language="JavaScript">

      function insertIT(id_Eve, id_FSius, 
    		  			anno_provv, 
    		  			numero_provv,
						annoSius,
						numeroSius,                                         
						descrUfficioEmittente,
						comuneUfficioEmittente,
						descrTipoProvv,
						DescrEsito,
						codEsito,
						codEsitoAlt3,
						g_DataEmissione,
						m_DataEmissione,
						a_DataEmissione,
						g_DataRicezione,
						m_DataRicezione,
						a_DataRicezione
					)
      {

    	  window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiEvento.CAMPO_ID_EVENTO%>.value=id_Eve;
    	  window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>.value=id_FSius;
//
			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiEvento.CAMPO_ANNO_PROTOCOLLO %>.value=anno_provv;
			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiEvento.CAMPO_PROGR_PROTOCOLLO %>.value=numero_provv;

			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO %>.value=annoSius;
			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR %>.value=numeroSius;
//
			for(var k=0;k<window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE%>.options.length;k++)
			{
			    if(window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE %>.options[k].text==descrUfficioEmittente)
			    {
			      window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE%>.options[k].selected=true;
			      break;
			  	}
			}
			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE%>.value=comuneUfficioEmittente;
			
//ordinanza
			for(var k=0;k< window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>.options.length;k++)
			{
			    if(window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>.options[k].text==descrTipoProvv )
			    {
			    	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>.options[k].selected=true;
			      	break;
			  	}
			}

// Esito SIUS / Provvedimento Archivizione SIEP (ComboBox)
			for(var k=0;k< window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraSicurezza.CAMPO_COD_RICH_MISURA_SIC%>.options.length;k++)
			{
				if(window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraSicurezza.CAMPO_COD_RICH_MISURA_SIC%>[k].value == codEsitoAlt3)	
				{	
					window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraSicurezza.CAMPO_COD_RICH_MISURA_SIC%>.options[k].selected=true;
					break;
				}	
			}

// Esito SIUS / Provvedimento Archivizione SIEP (Codici)	
			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiEvento.CAMPO_COD_ESITO%>.value=codEsito;
			window.parent.opener.document.<%=request.getParameter("formname")%>.CodEsitoAlt3Value.value=codEsitoAlt3;
//
			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value=g_DataEmissione;
			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value=m_DataEmissione;
			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value=a_DataEmissione;

			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>.value=g_DataRicezione;
			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI%>.value=m_DataRicezione;
			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiEvento.CAMPO_ANNO_DATA_RICEZIONE_ATTI%>.value=a_DataRicezione;

			
          	window.parent.close();

      }

      function controlla()
      {
	        if(document.elenco.numeroLiberazioni.value==0)
	        {
		          alert("Nessun Provvedimento Presente");
		          window.parent.close();
	        }
      }

    </script>

  </head>

  <body class="corpo" onload="controlla();">
  <form method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
  <table>
    <input type="hidden" name="numeroLiberazioni" value="<%=ListaOrd.size()%>">
      <tr>
        <td class="LBG">
          <a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a>
        </td>
        <td class="LBG">
          <font class=label>Funzione :</font>
          <font class=campo>Elenco Provvedimenti della Sorveglianza </font>
        </td>
      </tr>
    </table>
    <br>
    
     <!--Ordinanze Sorveglianza -->  

   	<table align="center" cellspacing=2 cellpadding=2 width="100%">
   		<tr><td class="Titolo" colspan=6> Dati ordinanza Ufficio di Sorveglianza</td></tr>
    	<tr>
    		<td class="int" width="10%">Anno / Numero SIUS</td>
    		<td class="int" width="10%">Anno / Numero Provv</td>
    		<td class="int" width="20%">Autorità emittente</td>
    		<td class="int" width="10%"> Data </td>
    		<td class="int" width="20%"> Oggetto </td>
    		<td class="int" width="25%"> Esito</td>
    		<td class="int" width="5%">Azioni</td>

    	</tr>
<%
		int idR = 0;
		Iterator Itx1 = ListaOrd.iterator();
		while (Itx1.hasNext() )
		{
			idR = idR + 1;
			OrdinanzaEventoTenoriFascicoloSiusModel lOrdMod = (OrdinanzaEventoTenoriFascicoloSiusModel)Itx1.next();
%>
		<tr>
			<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
			<%--  td class="l"><%=idR %></td --%>
			<td class="l"><%=StringUtils.toStringJSP(lOrdMod.getFascicoloSiusModel().getChiaveAnno())%>
						/
						<%=StringUtils.toStringJSP(lOrdMod.getFascicoloSiusModel().getChiaveProgr())%>
			</td>
			<td class="l"><%=StringUtils.toStringJSP(lOrdMod.getEvento().getAnnoProtocollo() ) %>
						/
						<%=StringUtils.toStringJSP(lOrdMod.getEvento().getProgrProtocollo()) %>
			</td>
			<td class="l"><%=lOrdMod.getDescrTipoUfficio()+" di "+lOrdMod.getDescrComuneUfficio() %></td>
			<td class="l"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lOrdMod.getEvento().getDataEmissione(),"dd-MM-yyyy")) %></td>
			<td class="l"><%=lOrdMod.getDescrOggetto() %></td>
			<td class="l"><%=lOrdMod.getDescrEsito() %></td>
 			<td class="c">
	         <a href="Javascript:insertIT(
	            <%= lOrdMod.getEvento().getIdEvento() %>,
	            <%= lOrdMod.getFascicoloSiusModel().getIdFascicoloSius() %>,
	            '<%=StringUtils.toStringJSP(lOrdMod.getEvento().getAnnoProtocollo() ,"-")%>',
	            '<%=StringUtils.toStringJSP(lOrdMod.getEvento().getProgrProtocollo() ,"-")%>',
	            '<%=StringUtils.toStringJSP(lOrdMod.getFascicoloSiusModel().getChiaveAnno() ,"-")%>',
	            '<%=StringUtils.toStringJSP(lOrdMod.getFascicoloSiusModel().getChiaveProgr() ,"-")%>',
	            '<%=StringUtils.cStrForJS(lOrdMod.getDescrTipoUfficio())%>',
	            '<%=StringUtils.cStrForJS(lOrdMod.getDescrComuneUfficio())%>',
	            '<%=StringUtils.cStrForJS(lOrdMod.getDescrProvvedimento())%>',
	            '<%=StringUtils.cStrForJS(lOrdMod.getDescrEsito())%>',
	            '<%=StringUtils.toStringJSP(lOrdMod.getEvento().getCodEsito(),"-")%>',
	            '<%=StringUtils.toStringJSP(lOrdMod.getCodEsitoAlt3(),"-")%>',
	            '<%=StringUtils.toStringJSP(DateUtils.getDateToString(lOrdMod.getEvento().getDataEmissione(),"dd"),"-")%>',
	            '<%=StringUtils.toStringJSP(DateUtils.getDateToString(lOrdMod.getEvento().getDataEmissione(),"MM"),"-")%>',
	            '<%=StringUtils.toStringJSP(DateUtils.getDateToString(lOrdMod.getEvento().getDataEmissione(),"yyyy"),"-")%>',
	            '<%=StringUtils.toStringJSP(DateUtils.getDateToString(lOrdMod.getEvento().getDataRicezioneAtti(),"dd"),"")%>',
	            '<%=StringUtils.toStringJSP(DateUtils.getDateToString(lOrdMod.getEvento().getDataRicezioneAtti(),"MM"),"")%>',
	            '<%=StringUtils.toStringJSP(DateUtils.getDateToString(lOrdMod.getEvento().getDataRicezioneAtti(),"yyyy"),"")%>' );">
	              <img align="middle" src="/images/fileselected.gif" border=0>
	            </a>
			</td>
		</tr>
<%		} %>				
    </table>
  </form>
  </body>
</html>