<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page language="java" import="f3b.web.IWebConstants" %>
<%@ page import="java.util.Iterator" %>

<%@ page import="java.util.ListIterator" %>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.util.DateUtils" %>

<%@ page import="java.util.Collection" %>
<%@ page import="f3b.util.StringUtils" %>
<%--@ page import="siap.sico.decodifiche.model.DecodificheModel" --%>
<%@ page import="siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel" %>
<%@ page import="siap.sico.libertaanticipata.action.ICostantiLicenzaLibanticipata" %>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc" %>

<%@ page import="siap.sico.evento.action.ICostantiEvento" %>

<jsp:useBean id="licenze" scope="request" class="java.util.ArrayList" />

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
    <title>[S.I.E.S.] - Ricerca Permessi</title>
    <script language="JavaScript" src="/html/conferma.js"></script>
    <script language="JavaScript">

      function insertIT(id, id_lic, elaborato, conProvvValidato,
						annoSius,
						numeroSius,                                         
						descrUfficioEmittente,
						comuneUfficioEmittente,
						g_DataEmissione,
						m_DataEmissione,
						a_DataEmissione,
						numGiorniReclusione,
						codTipoDecisione)
      {
        if( elaborato=='S' || conProvvValidato == 'true')
        {
          alert("Permesso già elaborato");
        }
        else
        {
			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiLicenzaLibanticipata.CAMPO_ANNO_SIUS%>.value=annoSius;
			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiLicenzaLibanticipata.CAMPO_NUMERO_SIUS%>.value=numeroSius;
			  
			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE%>_AA.value=comuneUfficioEmittente;

			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>_AA.value=g_DataEmissione;
			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>_AA.value=m_DataEmissione;
			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>_AA.value=a_DataEmissione;
			        
			window.parent.opener.document.<%=request.getParameter("formname")%>.GiorniComputo.value=numGiorniReclusione;

			window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiEvento.CAMPO_ID_EVENTO%>.value=id;
          	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiLicenzaLibanticipata.CAMPO_ID_LICENZA_LIBANTICIPATA%>.value=id_lic;

			for(var k=0;k<window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE%>_AA.options.length;k++){
			    if(window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE%>_AA.options[k].text==descrUfficioEmittente){
			      window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE%>_AA.options[k].selected=true;
			      break;
			  	}
			}
			
			if(codTipoDecisione=="EP"){
				for(var k=0;k<window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>_AA.options.length;k++){
				    if(window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>_AA.options[k].value=="2250"){
				      	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>_AA.options[k].selected=true;

						for(var kk=0;kk<window.parent.opener.document.f.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>_AB.options.length;kk++){
						    if(window.parent.opener.document.f.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>_AB.options[kk].value=="0958"){
						    	window.parent.opener.document.f.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>_AB.options[kk].selected=true;
						      	break;
						  	}
						}
				      	break;
				  	}
				}
			} else {
				for(var k=0;k<window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>_AA.options.length;k++){
				    if(window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>_AA.options[k].value=="0039"){
				      	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>_AA.options[k].selected=true;

						for(var kk=0;kk<window.parent.opener.document.f.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>_AB.options.length;kk++){
						    if(window.parent.opener.document.f.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>_AB.options[kk].value=="0994"){
						    	window.parent.opener.document.f.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>_AB.options[kk].selected=true;
						      	break;
						  	}
						}
				      	break;
				  	}
				}
			}

          	window.parent.close();
        }
      }
      
      function controlla()
      {
        if(document.elenco.numeroLiberazioni.value==0)
        {
          alert("Nessuno Permesso Presente");

          window.parent.close();
        }
      }

    </script>

  </head>

  <body class="corpo" onload="controlla();">
  <form method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
  <table>
    <input type="hidden" name="numeroLiberazioni" value="<%=licenze.size()%>">
      <tr>
        <td class="LBG">
          <a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a>
        </td>
        <td class="LBG">
          <font class=label>Funzione :</font>
          <font class=campo>Elenco Permessi</font>
        </td>
      </tr>
    </table>
    <br>
    <table>
      <tr>
        <td class="int">Descrizione</td>
        <td class="int">Anno Sius</td>
        <td class="int">Numero Sius</td>
        <td class="int">Autorità Emittente</td>
        <td class="int">Data Emissione</td>
<!--
        <td class="int">Giorni concessi</td>
-->
        <td class="int" width=5%>Azioni</td>
      </tr>
<%
  if(licenze.size()>0)
  {
    BigDecimal lIdlicenza = ((LicenzaLibAnticipataModel)licenze.get(0)).getIdLicenzaLibanticipata();

    Iterator itx = licenze.iterator();
    for (int i = 0; itx.hasNext(); i++)
    {
      LicenzaLibAnticipataModel licenzeMod = (LicenzaLibAnticipataModel)itx.next();

      // Confronto per evitare ripetizioni licenze appartenenti alla stessa ordinanza
      boolean lRipetizione = false;
      for(int j = 0; j < i && !lRipetizione; j++)
      {
        LicenzaLibAnticipataModel licVecchia = (LicenzaLibAnticipataModel)licenze.get(j);
        if (licVecchia.getEveIdEvento() != null && licenzeMod.getEveIdEvento() != null && licVecchia.getEveIdEvento().equals(licenzeMod.getEveIdEvento()))
          lRipetizione = true;
      }

      if (!lRipetizione)
      {
%>
        <tr>
          <td class="l"><%=StringUtils.toStringJSP(licenzeMod.getDescrTipoLicenza(),"-")%></td>
          <td class="l"><%=StringUtils.toStringJSP(licenzeMod.getAnnoSius(),"-")%></td>
          <td class="l"><%=StringUtils.toStringJSP(licenzeMod.getNumeroSius(),"-")%></td>
          <td class="l"><%=StringUtils.toStringJSP(licenzeMod.getDescrUfficioEmittente(),"-")%> di <%=StringUtils.toStringJSP(licenzeMod.getDescrLuogoEmittente(),"-")%></td>
          <td class="l"><%=StringUtils.toStringJSP(DateUtils.getDateToString(licenzeMod.getDataEmissioneOrdinanza(),"dd-MM-yyyy"),"-")%></td>

          <td class="l"><%=StringUtils.toStringJSP(licenzeMod.getNumeroGiorni(),"-")%></td>

          <input type="HIDDEN" name="<%=ICostantiLicenzaLibanticipata.CAMPO_ID_LICENZA_LIBANTICIPATA%>" value="<%=licenzeMod.getIdLicenzaLibanticipata()%>">
          <input type="HIDDEN" name="FlagElaborato" value="<%=StringUtils.toStringJSP(licenzeMod.getFlagElaborato())%>">

          <td class="c">
            <a href="Javascript:insertIT(<%=licenzeMod.getEveIdEvento()%>,<%=licenzeMod.getIdLicenzaLibanticipata()%>,
            	'<%=StringUtils.toStringJSP(licenzeMod.getFlagElaborato())%>', 
            	'<%=licenzeMod.isConProvvedimentoValidato()%>',
                 '<%=StringUtils.toStringJSP(licenzeMod.getAnnoSius(),"-")%>',
                 '<%=StringUtils.toStringJSP(licenzeMod.getNumeroSius(),"-")%>',                                         
                 '<%=StringUtils.toStringJSP(StringUtils.cStrForJS(licenzeMod.getDescrUfficioEmittente() ),"-")%>',
                 '<%=StringUtils.toStringJSP(StringUtils.cStrForJS(licenzeMod.getDescrLuogoEmittente() ),"-")%>',
                 '<%=StringUtils.toStringJSP(DateUtils.getDateToString(licenzeMod.getDataEmissioneOrdinanza(),"dd"),"-")%>',
                 '<%=StringUtils.toStringJSP(DateUtils.getDateToString(licenzeMod.getDataEmissioneOrdinanza(),"MM"),"-")%>',
                 '<%=StringUtils.toStringJSP(DateUtils.getDateToString(licenzeMod.getDataEmissioneOrdinanza(),"yyyy"),"-")%>',
                 '<%=StringUtils.toStringJSP(licenzeMod.getNumeroGiorni(),"")%>',
                 '<%=StringUtils.toStringJSP(licenzeMod.getCodTipoLicenza(),"-")%>');">
              <img align="middle" src="/images/fileselected.gif" border=0>
            </a>
<%
            if( "S".equals(licenzeMod.getFlagElaborato())
                || licenzeMod.isConProvvedimentoValidato()
               )
            {
%>
              <font class="cRosso"> Elaborato </font>
<%
            }
%>
          </td>
        </tr>
<%
      } // endif ripetizione
    }
  }
%>
    </table>
  </form>
  </body>
</html>