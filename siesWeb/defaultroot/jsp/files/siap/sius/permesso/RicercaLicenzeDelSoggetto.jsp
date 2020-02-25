<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sico.evento.action.ICostantiEvento" %>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>
<%@ page import="siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel" %>
<%@ page import="siap.sius.permesso.model.LicenzaModel" %>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />

<jsp:useBean id="licenze" scope="request" class="java.util.Vector" />

<jsp:useBean id="ufUtConnesso" scope="request" class="java.lang.String" />
<jsp:useBean id="ufOTribunale" scope="request" class="java.lang.String" />
<jsp:useBean id="codDistretto" scope="request" class="java.lang.String" />
<jsp:useBean id="lIncludeRigettati" scope="request" class="java.lang.String" />
<jsp:useBean id="codLicenza" scope="request" class="java.lang.String" />
<jsp:useBean id="descrLicenza" scope="request" class="java.lang.String" />
<jsp:useBean id="tipoUfficio" scope="request" class="java.lang.String" />
<jsp:useBean id="dataDalInCancelleria" scope="request" class="java.lang.String" />
<jsp:useBean id="dataAlInCancelleria" scope="request" class="java.lang.String" />
<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>

<%
  // presenza del Link per il bottone di ritorno
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
%>
<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Procedimenti di Sorveglianza</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  </head>

  <BODY class="corpo">

  <FORM method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo"> Elenco Licenze per Soggetto </font></td>
<%
       LicenzaModel licenzaUno = (LicenzaModel) licenze.get(0);
%>
      <td class="LBG">
        <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>" />
          <jsp:param name="ValoreIdEntita" value="<%=licenzaUno.getSogIdSoggetto()%>" />
        </jsp:include>
      </td>

      <!-- BOTTONE DI RITORNO -->
      <td class="LBG">
        <a href="javascript:history.go(-1);">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>

    </tr>
     <tr> </tr>
     <tr> </tr>

     <tr>
       <jsp:include page="/jsp/files/siap/sico/soggetto/SintesiSoggetto.jsp"/>
    </tr>

  </table>

  <br>

  <table cellspacing=2 cellpadding=2>
<%
    if(!(codDistretto.equals("") && ufOTribunale.equals("") &&
       lIncludeRigettati.equals("") && codLicenza.equals("-") &&
       (dataDalInCancelleria.equals("")) && (dataAlInCancelleria.equals("")) ))
    {
%>
      <tr>
        <td class="Cliccabile">Criteri di Ricerca selezionati:</td>
      </tr>
<%
      if(codDistretto.length()==1 )
      {
%>
        <tr>
          <td class="lVerdeNB">Visualizzazione di tutti i procedimenti, compresi quelli pervenuti da altri Distretti</td>
        </tr>
<%    
			}
      
			if(codDistretto.length()> 1 )
      {
%>
        <tr>
          <td class="lVerdeNB">Visualizzazione dei procedimenti dell'intero Distretto</td>
        </tr>
<%    }
      else if(!ufOTribunale.equals(""))
      {
        if (tipoUfficio.equals("UDS"))
        {
%>
         <tr>
            <td class="lVerdeNB">Anche i procedimenti del Tribunale</td>
         </tr>
<%
        }
        else if (tipoUfficio.equals("TDS"))
        {
%>
         <tr>
            <td class="lVerdeNB">Anche i procedimenti dell' Ufficio (Sede)</td>
         </tr>
<%      }
      }
      if(!lIncludeRigettati.equals(""))
      {
%>
        <tr>
          <td class="lVerdeNB">Anche le licenze rigettate</td>
        </tr>
<%    }
      if(!codLicenza.equals("-"))
      {
%>
        <tr>
          <td class="lVerdeNB">Tipo : <%=descrLicenza%></td>
        </tr>
<%    }
      if(!(dataDalInCancelleria.equals(""))||!(dataAlInCancelleria.equals("")))
      {
%>
        <tr>
          <td class="lVerdeNB">Con provvedimento emesso nel periodo:&nbsp;&nbsp;
<%
          if(!(dataDalInCancelleria.equals("")))
          {
%>
            Dal <%=dataDalInCancelleria%>&nbsp;&nbsp;
<%        }
          if(!(dataAlInCancelleria.equals("")))
          {
%>
            &nbsp;Al&nbsp;&nbsp;<%=dataAlInCancelleria%>
            </td>
<%        }
       %></tr><%
      }
    }
%>
  </table>
  <br>
<%
  Iterator itx = licenze.iterator();
  String sUfficio = "";
  String prevUfficio = "";
%>
  <table cellspacing="2" cellpadding="2" width=95%>
    <tr>
      <td class="int">Numero SIUS</td>
      <td class="int">Provvedimento</td>
      <td class="int">Data Emissione</td>
      <td class="int">Esito Provvedimento</td>
      <td class="int">Giorni/Ore concessi</td>
      <td class="int" width=10%>Giorni/Ore revocati - scomputati </td>
      <td class="int" width=10%>Giorni/Ore non fruiti </td>
      <td class="int">Esito</td>
      <td class="int">Azioni</td>
    </tr>
<%
    int sTotaleGC = 0;
    int sTotaleGS = 0;
    int sTotaleOC = 0;
    int sTotaleOS = 0;
    
    while ( itx.hasNext() )
    {
      LicenzaModel licenza = (LicenzaModel)itx.next();
      sUfficio = licenza.getDescrTipoUfficio()+licenza.getDescrComuneUfficio();
      if (!(sUfficio.compareTo(prevUfficio)==0))
      {
        prevUfficio=sUfficio;
%>
        <tr>
          <td class="lVerdeNB" colspan="7">&nbsp;</td>
        </tr>
        <tr>
        <%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
          <td class="lVerdeNB" colspan="7">Elenco Licenze di : <%=licenza.getCodTipoUfficio()%>&nbsp;<%=licenza.getDescrComuneUfficio()%></td>
        </tr>
<%
      }
%>
      <tr>
        <td class="c"><font class="label">
        <%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
          <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>=<%=licenza.getIdFascicoloSius()%><%=retParam%>" Title="<%=licenza.getDescrTipoUfficio()%>&nbsp;<%=licenza.getDescrComuneUfficio()%> - Dettaglio Procedimento" >
            <%=licenza.getChiaveAnno()%>
            /
            <%=licenza.getChiaveProgr()%>
          </a>

        </font></td>
        <td class="c"><font class="label"><%=licenza.getDescrTipoProvvedimento()%></font></td>
        <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(licenza.getDataEmissione(),"dd-MM-yyyy"),"-")%></font></td>
        <td class="c"><font class="label"><%=licenza.getDescrEsito()%></font></td>

				<!--  Parte di Visualizzazione Giorni/Ore concesse -->
        <td class="c"><font class="label">
<%
        // Giorni Concessi
				if (licenza.getNumeroGiorni() != null  && licenza.getDescrEsito().indexOf("oncede")>0 )
        {
	        sTotaleGC = sTotaleGC + licenza.getNumeroGiorni().intValue();
%>
          <%=licenza.getNumeroGiorni()%>&nbsp;/
<%
        }
        else
        {
%>
        	&nbsp; - &nbsp;/&nbsp;
<%
        }
        // Ore Concesse
				if (licenza.getNumeroOre() != null  && licenza.getDescrEsito().indexOf("oncede")>0 )
        {
	        sTotaleOC = sTotaleOC + licenza.getNumeroOre().intValue();
%>
          <%=licenza.getNumeroOre()%>
<%
        }
        else
        {
%>
        		- &nbsp;
<%
        }
%>

        </font></td>

				<!--  Parte di visualizzazione Giorni/Ore Revocate/Scomputate -->
        <td class="c"><font class="label">
<%
          // Giorni Revocati/Scomputati
					if (licenza.getNumeroGiorni() != null  && (licenza.getDescrEsito().indexOf("evoca")>0 || licenza.getDescrEsito().indexOf("Non Validamente Espiata la Pena")>0  ) )
          {
  	        sTotaleGS = sTotaleGS + licenza.getNumeroGiorni().intValue();
%>
            <%=licenza.getNumeroGiorni()%>&nbsp;/
<%
          }
          else
          {
%>
            &nbsp; - &nbsp;/&nbsp;
<%
          }
					// Ore Revocate/Scomputate 
          if (licenza.getNumeroOre() != null  && (licenza.getDescrEsito().indexOf("evoca")>0 || licenza.getDescrEsito().indexOf("Non Validamente Espiata la Pena")>0  ) )
          {
  	        sTotaleOS = sTotaleOS + licenza.getNumeroOre().intValue();
%>
            <%=licenza.getNumeroOre()%>
<%
          }
          else
          {
%>
            - &nbsp;
<%
          }
%>
        </font></td>
        
				<td class="c">
         	<font class="label">
<%
          if (licenza.getNumeroGiorniNoFruiti() != null)
          {
%>
            <%=licenza.getNumeroGiorniNoFruiti()%>&nbsp;/
<%
          }else{
%>
            &nbsp; - &nbsp;/&nbsp;
<%				}
          
					if (licenza.getNumeroOreNoFruite() != null)
          {
%>
            <%=licenza.getNumeroOreNoFruite()%>
<%
          }else{
%>
            - &nbsp;
<%				
					}
%>
        		</font>
        	</td>

        <td class="c">
        	<font class="label"><%=licenza.getDescrEsitoLicenza()%></font>
        </td>

<%      // STUB 10/05/2005 Bottone di dettaglio provvedimento.
        if (licenza.getIdEvento() != null )
        {
%>
          <td class="c">
		        <a href="/jsp/Main.jsp?Action=siap.sius.provvedimento.action.ActDettaglioProvvedimentoByIdEvento&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=licenza.getIdEvento()%>&TornaQui=<%=TornaQui%>">
		          <img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio provvedimento" border="0">
		        </a>
	        </td>
<%			
				}
%>
      </tr>
<%
  	}
%>
    </table>
<%
    if (sTotaleGC > 0 || sTotaleOC > 0 )
    {
%>
      <table cellspacing="2" cellpadding="2">
        <tr><td>&nbsp;</td></tr>
        <tr>
          <td class="label"><font class="label">Totale giorni/ore di Licenza concessi nel periodo : <%=sTotaleGC%>/<%=sTotaleOC%></font></td>
        </tr>
      </table>
  <%}%>
<%--
<%
    if (sTotaleGS > 0 )
    {
%>
      <table cellspacing="2" cellpadding="2">
        <tr><td>&nbsp;</td></tr>
        <tr>
          <td class="label"><font class="label">Totale giorni di revoca/scomputo nel periodo : <%=sTotaleGS%></font></td>
        </tr>
      </table>
  <%}%>
--%>
  </FORM>
  <br>

  </body>
</html>