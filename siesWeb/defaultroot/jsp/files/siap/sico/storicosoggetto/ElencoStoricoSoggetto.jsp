<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Vector" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.siep.jms.action.ICostantiSiepJMS" %>
<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sico.ufficio.model.UfficioModel" %>
<%@ page import="siap.sico.storicosoggetto.model.SoggettoStoricoSoggettoModel" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.sico.storicosoggetto.model.StoricoSoggettoModel" %>


<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="soggetto" scope="request" class="siap.sico.soggetto.model.SoggettoModel"/>
<jsp:useBean id="TornaQui" scope="request" class="java.lang.String" />
<jsp:useBean id="lTipoFunzione"       scope="request" class="java.lang.String"/>
<jsp:useBean id="NomeAzione" scope="request" class="java.lang.String" />
<jsp:useBean id="storiciNuovi" scope="request" class="java.util.Vector"/>
<jsp:useBean id="storici" scope="request" class="java.util.Vector"/>
<jsp:useBean id="soggettoVariato" scope="request" class="siap.sico.soggetto.model.SoggettoModel"/>
<jsp:useBean id="fascicoliNuovi" scope="request" class="java.util.Vector"/>
<jsp:useBean id="fascicoliUfficio" scope="request" class="java.util.Vector" />
<jsp:useBean id="fascicoliAltriUffici" scope="request" class="java.util.Vector" />

<jsp:useBean id="soggettoSessione" scope="request" class="siap.sico.soggetto.model.SoggettoModel"/>

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
          <font  class="label">Funzione :&nbsp;</font><font class="campo">Elenco Storico Soggetto</font>
       </td>
      </tr>
    </table>
  </FORM>
  <table >
<tr>
      <td class="l" width="30%"><font class="label">Cognome e Nome</font></td>
      <td class="l">      <font class="campo">
        <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.soggetto.action.ActLoadDettaglioSoggetto&<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>=<%=soggettoSessione.getIdSoggetto()%>" title="Soggetto">
          <%=StringUtils.toStringJSP(soggettoSessione.getCognome())%>&nbsp;<%=StringUtils.toStringJSP(soggettoSessione.getNome())%>
        </a>
      </font>&nbsp;
	  </td>

      <td class="l" ><font class="label">Sesso</font></td>
      <td class="l" width="10%"><font class="campo"><%=soggettoSessione.getSesso()%>&nbsp;</font></td>

      <td class="l" width="25%">
        <font  class="label">Data di nascita</font>
      </td>
      <td class="l" width="25%">
        <font class="campo">
<%
        if(soggettoSessione.getDataNascita() != null)
        {
%>
          <%=DateUtils.getDateToString(soggettoSessione.getDataNascita(),"dd-MM-yyyy")%>&nbsp;
<%
        }
        else
        {
%>
          <%="**-"+StringUtils.toStringJSP(soggettoSessione.getMeseNascita(), "**")+"-"+StringUtils.toStringJSP(soggettoSessione.getAnnoNascita(), "****")%>&nbsp;
<%
        }
%>
        </font>
      </td>
    </tr>
    
    <tr>
      <td class="l"><font class="label">Data Presunta</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggettoSessione.getDataNascitaPresunta())%>&nbsp;</font></td>
      <td class="l"><font  class="label">Età Presunta</font></td>
      <td class="l" colspan=3><font class="campo">
      <% if (soggettoSessione.getEtaPresuntaAnni()!=null){%> 
      	<%=StringUtils.toStringJSP(soggettoSessione.getEtaPresuntaAnni())%>&nbsp;</font> anni
		<% if (soggettoSessione.getEtaPresuntaMesi()!=null){%> 
      		e <font class="campo"><%=StringUtils.toStringJSP(soggettoSessione.getEtaPresuntaMesi())%>&nbsp;</font> mesi
		<% } %> 
	  <% } %>
		&nbsp;</font>
	  </td>
    </tr>
    
    <tr>
      <td class="l"><font  class="label">Comune Nascita</font></td>
      <td class="l">
        <font class="campo">
<%        if( (soggettoSessione.getDescrComuneNascita() != null)
              && (!(soggettoSessione.getDescrComuneNascita().equals("")))
              && (!(soggettoSessione.getDescrComuneNascita().equals("-"))) )
          {
%>

          <%=StringUtils.toStringJSP(soggettoSessione.getDescrComuneNascita() )%>
            (<%=soggettoSessione.getCodProvinciaNascita()%>)
<%
          }else if( (soggettoSessione.getDescComuneNascitaEstero() != null)
              && (!(soggettoSessione.getDescComuneNascitaEstero().equals("")))
              && (!(soggettoSessione.getDescComuneNascitaEstero().equals("-"))) )
          {
%>
          <%=StringUtils.toStringJSP(soggettoSessione.getDescComuneNascitaEstero() )%>

<%}%>
          &nbsp;
        </font>
      </td>
	  <td class="l"><font class="label">Nazionalità</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggettoSessione.getDescrNazionalita())%>&nbsp;</font></td>
      <td class="l"><font  class="label">Stato Nascita</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggettoSessione.getDescrStatoNascita())%>&nbsp;</font></td>
    </tr>
 <tr>
      <td class="l"><font class="label">Comune Di Nascita Estero</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggettoSessione.getDescComuneNascitaEstero()).toUpperCase()%>&nbsp;</font></td>
      <td class="l"><font class="label">Paternità</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggettoSessione.getPaternita())%>&nbsp;</font></td>

      <td class="l"><font class="label">Nome Madre</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggettoSessione.getNomeMadre())%>
      <%=StringUtils.toStringJSP(soggettoSessione.getCognomeMadre())%>&nbsp;</font></td>
    </tr>
    <tr>
      <td class="l"><font class="label">Codice Fiscale</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggettoSessione.getCodFiscale())%>&nbsp;</font></td>
      <td class="l"><font class="label">Atto Nascita</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggettoSessione.getAttoNascita()) %>&nbsp;</font></td>
      <td class="l"><font class="label">Codice CUI</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggettoSessione.getCodAfis())%>&nbsp;</font></td>
</tr>
    <tr>
      <td class="l"><font class="label">Codice AFIS</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggettoSessione.getCodCs())%>&nbsp;</font></td>
      <td class="l"><font  class="label">Note</font></td>
      <td class="l" colspan=3><font class="campo"><%=StringUtils.toStringJSP(soggettoSessione.getNote())%>&nbsp;</font></td>

   </tr>
	  <tr><td>&nbsp;</td></tr>
  </table >
  <table >

<%
 SoggettoStoricoSoggettoModel lSoggStoricoModel = (SoggettoStoricoSoggettoModel)storici.get(0);

if(lSoggStoricoModel.getStoricoSoggetto()!= null && lSoggStoricoModel.getStoricoSoggetto().size()>0){%>
<tr> <td class ="Titolo" colspan =8>Elenco storico soggetto </td></tr>
<%}%>
<%

Vector storiciVectModel = null;
//Vector fascicoli = null;
for(int i=0;i<storici.size();i++)
{
 SoggettoStoricoSoggettoModel lSoggStoricoMod = (SoggettoStoricoSoggettoModel)storici.get(i);
if(lSoggStoricoMod.getStoricoSoggetto()!= null)
{
 //STORICI DEL SOGGETTO IN SESSIONE-->
 storiciVectModel =  new Vector (lSoggStoricoMod.getStoricoSoggetto());
}

 // fascicoli = new Vector(lSoggStoricoMod.getFascicoloSiep());
}
if(storiciVectModel!= null && storiciVectModel.size()>0)
{
  for(int y=0;y<storiciVectModel.size();y++)
{
    StoricoSoggettoModel lStorico = (StoricoSoggettoModel)storiciVectModel.get(y);
%>
    <tr>
      <td class="l"><font class="label">Data Variazione</font></td>
      <td class="l" width="15%"><font class="campo"><%=DateUtils.getDateToString(lStorico.getDataVariazione() ,"dd-MM-yyyy")%></font></td>

      <td class="l" width="15%"><font class="label">Cognome e Nome</font></td>
      <td class="l"><font class="campo"><%=lStorico.getCognome() %>&nbsp;&nbsp;<%=lStorico.getNome() %></font></td>

      <td class="l"><font class="label">Sesso</font></td>
      <td class="l"><font class="campo"><%=lStorico.getSesso()%>&nbsp;</font></td>

      <td class="l" width="25%">
        <font  class="label">Data di nascita</font>
      </td>
      <td class="l" width="25%">
        <font class="campo">
<%
        if(lStorico.getDataNascita() != null)
        {
%>
          <%=DateUtils.getDateToString(lStorico.getDataNascita(),"dd-MM-yyyy")%>&nbsp;
<%
        }
        else
        {
%>
          <%="**-"+StringUtils.toStringJSP(lStorico.getMeseNascita(), "**")+"-"+StringUtils.toStringJSP(lStorico.getAnnoNascita(), "****")%>&nbsp;
<%
        }
%>
        </font>
      </td>
    </tr>
 
     <tr>
		<td>&nbsp;</td><td>&nbsp;</td>

      	<td class="l"><font class="label">Data Presunta</font></td>
      	<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lStorico.getDataNascitaPresunta())%>&nbsp;</font></td>
      	<td class="l"><font  class="label">Età Presunta</font></td>
      	<td class="l" colspan=3><font class="campo">
      	<% if (lStorico.getEtaPresuntaAnni()!=null){%> 
      		<%=StringUtils.toStringJSP(lStorico.getEtaPresuntaAnni())%>&nbsp;</font> anni
			<% if (lStorico.getEtaPresuntaMesi()!=null){%> 
      			e <font class="campo"><%=StringUtils.toStringJSP(lStorico.getEtaPresuntaMesi())%>&nbsp;</font> mesi
			<% } %> 
	  	<% } %>&nbsp;</font></td>
     </tr>
       
    <tr>
      <td >&nbsp;</td><td>&nbsp;</td>

      <td class="l"><font  class="label">Comune Nascita</font></td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(lStorico.getDescrComuneNascita() )%>
<%        if( (lStorico.getDescrComuneNascita() != null)
              && (!(lStorico.getDescrComuneNascita().equals("")))
              && (!(lStorico.getDescrComuneNascita().equals("-"))) )
          {
%>
            (<%=lStorico.getCodProvinciaNascita()%>)
<%
          }
%>
          &nbsp;
        </font>
      </td>
	  <td class="l"><font class="label">Nazionalità</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lStorico.getDescrNazionalita())%>&nbsp;</font></td>
      <td class="l"><font  class="label">Stato Nascita</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lStorico.getDescrStatoNascita())%>&nbsp;</font></td>
    </tr>
 <tr>
<td>&nbsp;</td><td>&nbsp;</td>

      <td class="l"><font class="label">Comune Di Nascita Estero</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lStorico.getDescComuneNascitaEstero()).toUpperCase()%>&nbsp;</font></td>
      <td class="l"><font class="label">Paternità</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lStorico.getPaternita())%>&nbsp;</font></td>

      <td class="l"><font class="label">Nome Madre</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lStorico.getNomeMadre())%>
      <%=StringUtils.toStringJSP(lStorico.getCognomeMadre())%>&nbsp;</font></td>
    </tr>
    <tr>
<td>&nbsp;</td><td>&nbsp;</td>
      <td class="l"><font class="label">Codice Fiscale</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lStorico.getCodFiscale())%>&nbsp;</font></td>
      <td class="l"><font class="label">Atto Nascita</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lStorico.getAttoNascita()) %>&nbsp;</font></td>
      <td class="l"><font class="label">Codice CUI</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lStorico.getCodAfis())%>&nbsp;</font></td>
</tr>
    <tr>
<td>&nbsp;</td><td>&nbsp;</td>

      <td class="l"><font class="label">Codice AFIS</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lStorico.getCodCs())%>&nbsp;</font></td>
      <td class="l"><font  class="label">Note</font></td>
      <td class="l" colspan=3><font class="campo"><%=StringUtils.toStringJSP(lStorico.getNote())%>&nbsp;</font></td>

   </tr>
<%if(y<storiciVectModel.size()-1){%>
<tr><td colspan=8 ><hr></td></tr>

<%}}
}%>
  </table>
  <table WIDTH="100%">
	  <tr><td>&nbsp;</td></tr>
<%if(fascicoliUfficio.size()>0){%>
<tr> <td class ="Titolo" colspan =4>Procedimenti dell' ufficio associati  al Soggetto </td></tr>
<tr><td>&nbsp;</td></tr>
   <tr>
   <td class="int">Numero SIEP</td>
      <td class="int">Data Irrevocabilità</td>
      <td class="int">Data di Iscrizione</td>
      <td class="int">Stato del Procedimento</td>
    </tr>
 <!--Procedimenti  associati  al Soggetto-->
<%
    Iterator itxf = fascicoliUfficio.iterator();
    int x = 0;
    while ( itxf.hasNext())
    {
       FascicoloSiepModel fascicolo = (FascicoloSiepModel)itxf.next();

%>
    <tr>
      <td class="c"><font class="label"><%=fascicolo.getChiaveAnno()%>/<%=fascicolo.getChiaveProgr()%></font></td>
      <td class="c"><font class="label"><%=DateUtils.getDateToString(fascicolo.getDataIrrevocabilita(),"dd-MM-yyyy")%></font></td>
      <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getDataIscrizione(),"dd-MM-yyyy"))%>&nbsp;</font></td>
      <td class="c"><font class="label"><%=StringUtils.toStringJSP(fascicolo.getDescrStatoProcedimento())%>&nbsp;</font></td>
    </tr>

<%
  x++;

  }
}
%>
<tr><td>&nbsp;</td></tr>

<%if(fascicoliAltriUffici.size()>0){%>
<tr> <td class ="Titolo" colspan =4>Procedimenti di altri uffici associati  al Soggetto </td></tr>
<tr><td>&nbsp;</td></tr>
   <tr>
   <td class="int">Numero SIEP</td>
      <td class="int">Data Irrevocabilità</td>
      <td class="int">Data di Iscrizione</td>
      <td class="int">Stato del Procedimento</td>
    </tr>
 <!--Procedimenti  associati  al Soggetto-->
<%
    Iterator itxfaltri = fascicoliAltriUffici.iterator();
    int z = 0;
    while ( itxfaltri.hasNext())
    {
       FascicoloSiepModel fascicoloAltriUff = (FascicoloSiepModel)itxfaltri.next();

%>
    <tr>
      <td class="c"><font class="label"><%=fascicoloAltriUff.getChiaveAnno()%>/<%=fascicoloAltriUff.getChiaveProgr()%></font></td>
      <td class="c"><font class="label"><%=DateUtils.getDateToString(fascicoloAltriUff.getDataIrrevocabilita(),"dd-MM-yyyy")%></font></td>
      <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicoloAltriUff.getDataIscrizione(),"dd-MM-yyyy"))%>&nbsp;</font></td>
      <td class="c"><font class="label"><%=StringUtils.toStringJSP(fascicoloAltriUff.getDescrStatoProcedimento())%>&nbsp;</font></td>
    </tr>

<%
  z++;

  }
}
%>

    </table>
<!--Soggetto DUPLICATO-->
<%if(soggettoVariato != null && soggettoVariato.getIdSoggetto().compareTo(new BigDecimal(0))!=0  )
{
%>
  <table width="100%">
	  <tr><td>&nbsp;</td></tr>

<tr> <td class ="Titolo"  colspan=6>Il soggetto è stato variato e associato ai seguenti fascicoli</td></tr>
<tr><td>&nbsp;</td></tr>

 <tr>
      <td class="l"><font class="label">Cognome e Nome</font></td>
      <td class="l"><font class="campo"><%=soggettoVariato.getCognome() %>&nbsp;&nbsp;<%=soggettoVariato.getNome() %></font></td>

      <td class="l"><font class="label">Sesso</font></td>
      <td class="l"><font class="campo"><%=soggettoVariato.getSesso()%>&nbsp;</font></td>
      <td class="l" width="25%">
        <font  class="label">Data di nascita</font>
      </td>
      <td class="l" width="25%">
        <font class="campo">
<%
        if(soggettoVariato.getDataNascita() != null)
        {
%>
          <%=DateUtils.getDateToString(soggettoVariato.getDataNascita(),"dd-MM-yyyy")%>&nbsp;
<%
        }
        else
        {
%>
          <%="**-"+StringUtils.toStringJSP(soggettoVariato.getMeseNascita(), "**")+"-"+StringUtils.toStringJSP(soggettoVariato.getAnnoNascita())%>&nbsp;
<%
        }
%>
        </font>
      </td>
    </tr>
    <tr>
      <td class="l"><font  class="label">Comune Nascita</font></td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(soggettoVariato.getDescrComuneNascita() )%>
<%        if( (soggettoVariato.getDescrComuneNascita() != null)
              && (!(soggettoVariato.getDescrComuneNascita().equals("")))
              && (!(soggettoVariato.getDescrComuneNascita().equals("-"))) )
          {
%>
            (<%=soggettoVariato.getCodProvinciaNascita()%>)
<%
          }
%>
          &nbsp;
        </font>
</td>
	  <td class="l"><font class="label">Nazionalità</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggettoVariato.getDescrNazionalita())%>&nbsp;</font></td>
      <td class="l"><font  class="label">Stato Nascita</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggettoVariato.getDescrStatoNascita())%>&nbsp;</font></td>
    </tr>
  </table>
  <table WIDTH="100%">
<tr><td>&nbsp;</td></tr>
   <tr>
      <td class="int">Numero SIEP</td>
      <td class="int">Data Irrevocabilità</td>
      <td class="int">Data di Iscrizione</td>
      <td class="int">Stato del Procedimento</td>
    </tr>

<%if(fascicoliNuovi != null && fascicoliNuovi.size()>0)
{

    Iterator itxFasc = fascicoliNuovi.iterator();

    while ( itxFasc.hasNext())
    {
      FascicoloSiepModel fascicoloNew = (FascicoloSiepModel)itxFasc.next();
%>
    <tr>
      <td class="c"><font class="label"><%=fascicoloNew.getChiaveAnno()%>/<%=fascicoloNew.getChiaveProgr()%></font></td>
      <td class="c"><font class="label"><%=DateUtils.getDateToString(fascicoloNew.getDataIrrevocabilita(),"dd-MM-yyyy")%></font></td>
      <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicoloNew.getDataIscrizione(),"dd-MM-yyyy"))%>&nbsp;</font></td>
      <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
      <%--td class="c"><font class="label"><--%=DateUtils.getDateToString(fascicolo.getSentenza().getDataProvvedimento(),"dd-MM-yyyy")%></font></td--%>
      <%--td class="c"><font class="label"><--%=fascicolo.getSentenza().getDescrTipoProvvedimento()%></font></td--%>
      <%--td class="c"><font class="label"><--%=fascicolo.getSentenza().getDescrLuogoEmittente()%></font></td--%>
      <td class="c"><font class="label"><%=StringUtils.toStringJSP(fascicoloNew.getDescrStatoProcedimento())%>&nbsp;</font></td>
    </tr>

<%
  }}
%>

  </table>
<%}%>

  </body>
</html>