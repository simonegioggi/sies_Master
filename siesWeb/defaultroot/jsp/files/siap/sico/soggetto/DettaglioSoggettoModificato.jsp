<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="java.math.BigDecimal" %>

<%@ page import="siap.siep.jms.action.ICostantiSiepJMS" %>
<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>
<%@ page import="siap.sius.fascicolo.model.FascicoloSiusModel" %>
<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sico.ufficio.model.UfficioModel" %>
<%@ page import="f3b.security.model.ProfileModel"%>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="soggetto" scope="request" class="siap.sico.soggetto.model.SoggettoModel"/>
<jsp:useBean id="TornaQui" scope="request" class="java.lang.String" />
<jsp:useBean id="lTipoFunzione"       scope="request" class="java.lang.String"/>
<jsp:useBean id="NomeAzione" scope="request" class="java.lang.String" />
<jsp:useBean id="fascicoli" scope="request" class="java.util.Vector" />

<%

// Si ricava il profilo dell'utente connesso
ProfileModel lProfilo =(ProfileModel) UtenteConnesso.getUserProfile();
BigDecimal profilo = (BigDecimal) request.getAttribute("profilo");
String lDataNascita = "DataNascita";
String DataNascita = DateUtils.getDateToString(soggetto.getDataNascita(),"dd-MM-yyyy");
%>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Gestione Soggetto - </title>
    <script language="JavaScript" src="/html/conferma.js"></script>

<script language="JavaScript">
</script>

  </head>

  <BODY class="corpo" >

  <FORM name="comandi" >
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
          <font  class="label">Funzione :&nbsp;</font><font class="campo">Dettaglio Soggetto</font>
       </td>
      </tr>
    </table>
  </FORM>
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="l"><font class="label">Cognome e Nome</font></td>
      <td class="l">  <font class="campo">
        <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.soggetto.action.ActLoadDettaglioSoggetto&<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>=<%=soggetto.getIdSoggetto()%>&<%=ICostantiSoggetto.CAMPO_COGNOME%>=<%=soggetto.getCognome()%>&<%=ICostantiSoggetto.CAMPO_NOME%>=<%=soggetto.getNome()%>&<%=lDataNascita%>=<%=DataNascita%>" title="Soggetto">
          <%=StringUtils.toStringJSP(soggetto.getCognome())%>&nbsp;<%=StringUtils.toStringJSP(soggetto.getNome())%>
        </a>
      </font>&nbsp;</td>
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
    
    <%-- MERGE v10 COLLAUDO: modifica alla gestione del codice --%>
    <%
	  // Si ricava il profilo dell'utente connesso 	  
	  UfficioModel lUfficio = UtenteConnesso.getUfficioUtente();
	  String codTipoUfficio = lUfficio.getCodTipoUfficio();

	// MEV_57: esclusi anche sige minorenni
	boolean isSigeMinorenni = lProfilo.isSige() && ("CAPSM".equals(codTipoUfficio) || "DIBM".equals(codTipoUfficio) || "GIPM".equals(codTipoUfficio) || "GUPM".equals(codTipoUfficio) || "PMM".equals(codTipoUfficio));
	if (!lProfilo.isSius() && !isSigeMinorenni) {
%>
	    <tr>
	      <td class="l"><font class="label">Età Presunta</font></td>
	      <td class="l"><font class="campo">
	<% if (soggetto.getEtaPresuntaAnni()!=null){%> 
	      	<%=StringUtils.toStringJSP(soggetto.getEtaPresuntaAnni())%>&nbsp;</font> anni
	<% if (soggetto.getEtaPresuntaMesi()!=null){%> 
	      	e <font class="campo"><%=StringUtils.toStringJSP(soggetto.getEtaPresuntaMesi())%>&nbsp;</font> mesi
	<%} %> 
	<%} %>
			&nbsp;
	      </td>
	    </tr> 
<%   } %>
 
<%
	// Data commesso reato deve essere visibile solo per 
	// gli utenti SIUS (Minorenni), no per gli utenti SIEP
	// MEV_57: aggiunti anche 3 uffici sige minorenni
	if ((!lProfilo.isSiep() && ("TDSM".equals(codTipoUfficio) || "UDSM".equals(codTipoUfficio))) || isSigeMinorenni) {
%> 
		<tr>
		      <td class="l"><font class="label">Età Presunta</font></td>
		      <td class="l"><font class="campo">
		<% if (soggetto.getEtaPresuntaAnni()!=null){%> 
		      	<%=StringUtils.toStringJSP(soggetto.getEtaPresuntaAnni())%>&nbsp;</font> anni
		<% if (soggetto.getEtaPresuntaMesi()!=null){%> 
		      	e <font class="campo"><%=StringUtils.toStringJSP(soggetto.getEtaPresuntaMesi())%>&nbsp;</font> mesi
		<%} %> 
		<%} %>
				&nbsp;
		      </td>
	      	  
	      	  <td class="l" width="25%">
	        	<font  class="label">Data commesso reato</font>
	      	  </td>
	      	  <td class="l" width="25%">
	        	<font class="campo">
	        	<%
				        if(soggetto.getDataReatoSius() != null)
				        {
				%>
				          <%=DateUtils.getDateToString(soggetto.getDataReatoSius(),"dd-MM-yyyy")%>&nbsp;
				<%
				        }
				        else
				        {
				%>
				          <%="**-"+StringUtils.toStringJSP(soggetto.getDataReatoSius(), "**")+"-"+StringUtils.toStringJSP(soggetto.getDataReatoSius(), "****")%>&nbsp;
				<%
				        }
				%>
	        	</font>
	    	  </td>
		</tr>
    <%
	}
	%>
	
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


  <br>


  </table>
  <table cellspacing=2 cellpadding=2>

<tr> <td class ="Titolo" colspan =7>Procedimenti  associati  al Soggetto</td></tr>
<tr><td>&nbsp;</td></tr>
<% if( lProfilo.isSige()) { %>
   	 <jsp:include page="/jsp/files/siap/sico/storicosoggetto/IncFasSigeUff.jsp">
   	    <jsp:param name="checkbox_no" value="N" />
        </jsp:include> 	 
 <%}else { %>

   <tr>
      <td class="int">Data Titolo Esecutivo</td>
      <td class="int">Autorità Titolo Esecutivo</td>
      <td class="int">Data Irrevocabilità</td>
      <td class="int">Numero SIEP</td>
      <td class="int">Data di Iscrizione</td>
      <td class="int">Stato del Procedimento</td>
    </tr>

<%
    Iterator itx = fascicoli.iterator();
    int i = 0;
    while ( itx.hasNext())
    {
if(profilo.compareTo(new BigDecimal(4))== 0  ||
   profilo.compareTo(new BigDecimal(30))== 0 ||
   profilo.compareTo(new BigDecimal(40))== 0 ||
   profilo.compareTo(new BigDecimal(50))== 0
) // SIEP
{
  FascicoloSiepModel fascicolo = (FascicoloSiepModel)itx.next();
%>
    <tr>
      <td class="c"><font class="label"><%=DateUtils.getDateToString(fascicolo.getSentenza().getDataProvvedimento(),"dd-MM-yyyy")%></font></td>
      <td class="c"><font class="label"><%=fascicolo.getSentenza().getDescrTipoAutoritaEmittente()%></font> di <font class="label"><%=fascicolo.getSentenza().getDescrLuogoEmittente()%></font></td>
      <td class="c"><font class="label"><%=DateUtils.getDateToString(fascicolo.getDataIrrevocabilita(),"dd-MM-yyyy")%></font></td>
      <td class="c"><font class="label"><%=fascicolo.getChiaveAnno()%>/<%=fascicolo.getChiaveProgr()%></font></td>
      <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getDataIscrizione(),"dd-MM-yyyy"))%>&nbsp;</font></td>
      <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
      <%--td class="c"><font class="label"><--%=DateUtils.getDateToString(fascicolo.getSentenza().getDataProvvedimento(),"dd-MM-yyyy")%></font></td--%>
      <%--td class="c"><font class="label"><--%=fascicolo.getSentenza().getDescrTipoProvvedimento()%></font></td--%>
      <%--td class="c"><font class="label"><--%=fascicolo.getSentenza().getDescrLuogoEmittente()%></font></td--%>
      <td class="c"><font class="label"><%=StringUtils.toStringJSP(fascicolo.getDescrStatoProcedimento())%>&nbsp;</font></td>
    </tr>

<%
}else{
          FascicoloGPModel FascSiusGP = (FascicoloGPModel) itx.next();
      FascicoloSiusModel fascicolo = (FascicoloSiusModel)FascSiusGP.getFascicoloSiusModel();


%>
    <tr>
      <td class="c"><font class="label"><%=StringUtils.toStringJSP(fascicolo.getChiaveAnno(),"")%>/<%=StringUtils.toStringJSP(fascicolo.getChiaveProgr(),"")%></font></td>
      <td class="c"><font class="label"><%=StringUtils.toStringJSP(fascicolo.getDescrTipoUfficio(),"")%>/<%=StringUtils.toStringJSP(fascicolo.getDescrComuneUfficio(),"")%></font></td>
      <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(FascSiusGP.getGeneraleProcedimentoModel().getDataRichiesta(),"dd-MM-yyyy"),"-")%></font></td>
      <td class="c"><font class="label"><%=StringUtils.toStringJSP(FascSiusGP.getGeneraleProcedimentoModel().getDescrOggettoProcedimento())%></font></td>
      <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getDataIscrizione(),"dd-MM-yyyy"),"-")%></font></td>
      <td class="c"><font class="label"><%=StringUtils.toStringJSP(FascSiusGP.getFascicoloSiusModel().getDescrStatoFascicolo(),"")%>&nbsp;</font></td>
    </tr>

<%}
  i++;

  }
 }
%>
    </table>

  </body>
</html>