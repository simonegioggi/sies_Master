<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>

<jsp:useBean id="UtenteConnesso" 	scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="AzioneChiamante" 	scope="request" class="java.lang.String" />
<jsp:useBean id="lTipoFunzione"     scope="request" class="java.lang.String"/>

<jsp:useBean id="fascicoli" scope="request" class="java.util.Vector" />
<jsp:useBean id="soggetto" 	scope="request" class="siap.sico.soggetto.model.SoggettoModel"/>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] Inserimento Soggetto - Presenza omonimie di cod. AFIS</title>
    <script language="JavaScript" src="/html/conferma.js"></script>

	<script language="JavaScript">
  //==========================================================================
  // Richiama l'opportuna azione
  //==========================================================================
    function eseguiAzione(aTipoAzione, aIdSog)
    {
      	if (aTipoAzione=='Dettaglio')
      	{
        	lAzione = "siap.sico.soggetto.action.ActLoadDettaglioSoggetto";
        	document.InserimentoSoggettoCodAfis.<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO %>.value = aIdSog;
        	document.InserimentoSoggettoCodAfis.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        	document.InserimentoSoggettoCodAfis.submit();
      	}
	}
	</script>
  
  </head>

  <body class="corpo">

  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"> <font class="label"> Funzione :</font> <font class=campo> Inserimento Soggetto - Presenza Codice AFIS</font></td>
<%
       FascicoloSiepModel fascicoloUno = (FascicoloSiepModel) fascicoli.get(0);
%>    
    
    </tr>
  </table>

  <br>
  <div align=center>
  <table>
    <tr>
      <td class="LBG"> <font class=campo> Soggetti con lo stesso Cod. CUI già presenti in archivio</font></td>
    </tr>
  </table>
	<br>
  <table>
    <tr>
      <td class="int">Cognome Nome</td>
      <td class="int" width=5%>Sesso</td>
      <td class="int" width=15%>Data Nascita</td>
      <td class="int" width=15%>Luogo Nascita</td>
      <td class="int">Paternità</td>
      <td class="int" width=10%>Cod Afis</td>
      <td class="int" width=15%>N. proc</td>
    </tr>

<%

  	Iterator itx = fascicoli.iterator();
	String sUfficio = "Procura";
  	while ( itx.hasNext())
  	{
  		FascicoloSiepModel fascicolo = (FascicoloSiepModel)itx.next();
  //	SoggettoModel soggOmonimo =  new SoggettoModel((SoggettoModel)SoggOmonimi.firstElement()); 
%>
    <tr>
    	<%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
      <td class=l><%=fascicolo.getSoggetto().getCognome()%>&nbsp;<%=fascicolo.getSoggetto().getNome()%></td>
      <td class=l><%=fascicolo.getSoggetto().getSesso()%></td>
      <td class=l>
  
<%
        if(fascicolo.getSoggetto().getDataNascita() != null)
        {
%>
          <%=DateUtils.getDateToString(fascicolo.getSoggetto().getDataNascita(),"dd-MM-yyyy")%>&nbsp;
<%
        }
        else
        {
%>
          <%="**-"+StringUtils.toStringJSP(fascicolo.getSoggetto().getMeseNascita(), "**")+"-"+StringUtils.toStringJSP(fascicolo.getSoggetto().getAnnoNascita())%>&nbsp;

<%
        }

       if(fascicolo.getSoggetto().getDataNascitaPresunta() != null && fascicolo.getSoggetto().getDataNascitaPresunta().equals("S"))
        {
%>
          <font class="campo"> (Data Presunta)</font>
<%      }

%>

      </td>

<% 		if (fascicolo.getSoggetto().getDescrComuneNascita().compareTo("-")==0)
        {

          	if (fascicolo.getSoggetto().getDescComuneNascitaEstero().compareTo("------------------------------")==0 || fascicolo.getSoggetto().getDescComuneNascitaEstero().equals("") )
        	{
%>
        		<td class="l">-</td>
  <%		}
          	else
          	{
  %>
           		<td class="l">
          			<%=StringUtils.pulisciCampo(fascicolo.getSoggetto().getDescComuneNascitaEstero(),fascicolo.getSoggetto().getDescrStatoNascita().toUpperCase())%>  (<%=fascicolo.getSoggetto().getDescrStatoNascita().toUpperCase()%>)&nbsp;
        		</td>
 <%			}
      	}
		else
        {
 %>
        	<td class="l"><%=fascicolo.getSoggetto().getDescrComuneNascita()%> (<%=fascicolo.getSoggetto().getCodProvinciaNascita()%>)&nbsp;</td>
 <%
 		}


        if((fascicolo.getSoggetto().getPaternita() != null) ||
       	   (!fascicolo.getSoggetto().getPaternita().equals("") ))
        {
%>      
      		<td class=l><%=fascicolo.getSoggetto().getPaternita()%></td>
 <%
 		}
 		else
 		{
%>  
        	<td class="l">-</td>
  <%	
  		}
   %>     
      	<td class=l><%=fascicolo.getSoggetto().getCodAfis() %></td>
      	<td class=l><%=fascicolo.getNumFascicoli() %></td>
     

    </tr>
<%
  }
%>
    </table>


</div>
  <br>

  <table>
	<tr>
		<td>
 			<font class="cRosso"> SOGGETTO INSERITO CORRETTAMENTE! Per proseguire, clickare sulla icona dettaglio</font>
		</td>
        <td class=c>
        	<a href="javascript:eseguiAzione('Dettaglio',<%=soggetto.getIdSoggetto() %> )">
        		<img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio Soggetto" border="0">
        	</a>
      	</td>       
	</tr>    
  </table>
  
  <form  method="POST" action="<%=IWebConstants.PG_MAIN%>" name="InserimentoSoggettoCodAfis">
  	<input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
   
    <!-- Campi valorizzati dinamicamente dalla eseguiAzione() -->
  	<input type="hidden" name="<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>" value="">
  	
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="l"><font class="label">Cognome e Nome</font></td>
      <td class="l"><font class="campo"><%=soggetto.getCognome() %>&nbsp;&nbsp;<%=soggetto.getNome() %></font></td>
    </tr>
    <tr>
      <td class="l"><font class="label">Sesso</font></td>
      <td class="l"><font class="campo"><%=soggetto.getSesso()%>&nbsp;</font></td>
    </tr>
	<tr>
      <td class="l" width="25%">
        <font  class="label">Data di nascita</font>
      </td>
      <td class="l" width="25%">
        <font class="campo">
<%
        if(soggetto.getDataNascita() != null)
        {
%>
          <%=DateUtils.getDateToString(soggetto.getDataNascita(),"dd-MM-yyyy")%>&nbsp;
<%
        }
        else
        {
%>
          <%="**-"+StringUtils.toStringJSP(soggetto.getMeseNascita(), "**")+"-"+StringUtils.toStringJSP(soggetto.getAnnoNascita())%>&nbsp;
<%
        }
%>
        </font>
      </td>
      <td class="l" width="25%">
        <font class="label">Presunta</font>
      </td>
      <td class="l" width="25%">
        <font class="campo"><%=StringUtils.toStringJSP(soggetto.getDataNascitaPresunta())%></font>
      </td>
    </tr>
    <tr>
      <td class="l"><font  class="label">Comune Nascita</font></td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(soggetto.getDescrComuneNascita() )%>
<%        if( (soggetto.getDescrComuneNascita() != null)
              && (!(soggetto.getDescrComuneNascita().equals("")))
              && (!(soggetto.getDescrComuneNascita().equals("-"))) )
          {
%>
            (<%=soggetto.getCodProvinciaNascita()%>)
<%
          }
%>
          &nbsp;
        </font>
      </td>
    </tr>
	<tr>
	  <td class="l"><font class="label">Stato Cittadinanza</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getDescrNazionalita())%>&nbsp;</font></td>
      <td class="l"><font  class="label">Stato Nascita</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getDescrStatoNascita())%>&nbsp;</font></td>
    </tr>
	 <tr>
      <td class="l"><font class="label">Comune Di Nascita Estero</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getDescComuneNascitaEstero()).toUpperCase()%>&nbsp;</font></td>
    </tr>
	  <tr>
      <td class="l"><font class="label">Paternità</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getPaternita())%>&nbsp;</font></td>
    </tr>
	 <tr>
      <td class="l"><font class="label">Nome Madre</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getNomeMadre())%>
      <%=StringUtils.toStringJSP(soggetto.getCognomeMadre())%>&nbsp;</font></td>
    </tr>
	<tr><td colspan=4 class=l>&nbsp;</td></tr>
    <tr>
      <td class="l"><font class="label">Codice Fiscale</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getCodFiscale())%>&nbsp;</font></td>
      <td class="l"><font class="label">Atto Nascita</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getAttoNascita()) %>&nbsp;</font></td>
    </tr>
    <tr>
      <td class="l"><font class="label">Codice Fascicolo Rosso</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getCodCs())%>&nbsp;</font></td>
      <td class="l"><font class="label">Codice CUI</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getCodAfis() )%>&nbsp;</font></td>
    </tr>
    <tr>
      <td class="l"><font  class="label">Note</font></td>
      <td class="l" colspan=3><font class="campo"><%=StringUtils.toStringJSP(soggetto.getNote())%>&nbsp;</font></td>
    </tr>
</table>

</form>
  <br>



  </body>
</html>