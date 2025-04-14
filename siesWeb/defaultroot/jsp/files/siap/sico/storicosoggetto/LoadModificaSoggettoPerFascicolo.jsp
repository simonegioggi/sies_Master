<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.siep.jms.action.ICostantiSiepJMS" %>
<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>
<%@ page import="siap.sius.fascicolo.model.FascicoloSiusModel" %>
<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel" %>
<%@ page import="f3b.security.model.ProfileModel" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sico.ufficio.model.UfficioModel" %>

<jsp:useBean id="fascicoliUfficio" scope="request" class="java.util.Vector" />
<jsp:useBean id="fascicoliAltriUffici" scope="request" class="java.util.Vector" />
<jsp:useBean id="fascicoli" scope="request" class="java.util.Vector" />

<jsp:useBean id="soggettonuovo" scope="request" class="siap.sico.soggetto.model.SoggettoModel" />
<jsp:useBean id="soggettovecchio" scope="request" class="siap.sico.soggetto.model.SoggettoModel" />

<jsp:useBean id="chiaveUfficio" scope="request" class="java.lang.String" />
<jsp:useBean id="contaUffici" scope="request" class="java.lang.String" />
<jsp:useBean id="UtenteConnesso"       scope="session" class="siap.sico.utente.model.UtenteModel"/>

<%


// Si ricava il profilo dell'utente connesso
ProfileModel lProfilo =(ProfileModel) UtenteConnesso.getUserProfile();

BigDecimal profilo = (BigDecimal) request.getAttribute("profilo");

int disabilitaFascicoli = Integer.parseInt(contaUffici);
String disabilita= null;
if( disabilitaFascicoli == fascicoli.size()){

disabilita="disabled";

}else
{

disabilita="";

}
%>


<html>

  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Scelta Procedimenti al quale associare la modifica del soggetto</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>> </script>

<script language="JavaScript">


function Verify()
{

   var conta=false;
 <%
int h=0;
  while (h<fascicoliUfficio.size())
   {%>
      if(document.LoadModificaSoggettoPerFascicolo.<%="fascicolo"%><%=h%>.checked == true)
       {
          conta = true;

       }

   <%
h++;
}%>
   if(conta == false)
   {

        alert("Selezionare almeno un fascicolo al quale associare la modifica");
         return false;
    }

return true;
}

</script>

  </head>
<%    int contUffici = 0;%>
  <BODY class="corpo">

  <FORM method="POST" name="LoadModificaSoggettoPerFascicolo" id="LoadModificaSoggettoPerFascicolo" action="<%=IWebConstants.PG_MAIN%>">
  <table>
     <input type="hidden" name="IdSogg" value="<%=soggettonuovo.getIdSoggetto()%>">

  <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo">Elenco Procedimenti</font></td>
    </tr>
  </table>
<br>
<table width="100%">
    <tr>
    <td class="Titolo">Il Soggetto </td>
    </tr>
    <tr><td>&nbsp;</td></tr>
    <tr>
      <td class="l">
      <font class="label">Nome:&nbsp;&nbsp;</font>
      <font class="campo"><%=soggettovecchio.getCognome() %>&nbsp;&nbsp;<%=soggettovecchio.getNome() %>
      </font>
      <font class="label">
      &nbsp;&nbsp;
      <%if (soggettovecchio.getSesso() == "F") { %>
      Nata il:&nbsp;&nbsp;
      <% } else {%>
      Nato il:&nbsp;&nbsp;
      <%}%>
      </font>
      <font class="campo">
      <%
        if(soggettovecchio.getDataNascita() != null)
        {
%>
          <%=DateUtils.getDateToString(soggettovecchio.getDataNascita(),"dd-MM-yyyy")%>&nbsp;
<%
        }
        else
        {
%>
          <%="**-"+StringUtils.toStringJSP(soggettovecchio.getMeseNascita(), "**")+"-"+StringUtils.toStringJSP(soggettovecchio.getAnnoNascita())%>&nbsp;
<%
        }
%>
<% if (soggettonuovo.getDataNascitaPresunta().equals("S")) { %>
&nbsp;&nbsp;(Data Presunta)
<% } %>

<% if (soggettovecchio.getCodStatoNascita().equals("039")) { %>
  &nbsp;&nbsp;</font><font class="label">a</font><font class="campo">&nbsp;&nbsp;<%=StringUtils.toStringJSP(soggettovecchio.getDescrComuneNascita() )%>
  <%        if( (soggettovecchio.getDescrComuneNascita() != null)
                && (!(soggettovecchio.getDescrComuneNascita().equals("")))
                && (!(soggettovecchio.getDescrComuneNascita().equals("-"))) )
            {
  %>
              (<%=soggettovecchio.getCodProvinciaNascita()%>)
  <%
            }
  %>
<% } else { %>
  &nbsp;&nbsp;</font><font class="label">a</font><font class="campo">&nbsp;&nbsp;<%=StringUtils.toStringJSP(soggettovecchio.getDescComuneNascitaEstero()).toUpperCase()%>
<% } %>
&nbsp;&nbsp;<%=StringUtils.toStringJSP(soggettovecchio.getDescrStatoNascita())%>
</font></td></tr>
	<tr>
	  <td class="l"><font class="label">Stato Cittadinanza:&nbsp;&nbsp;</font><font class="campo">
    <%=StringUtils.toStringJSP(soggettovecchio.getDescrNazionalita())%>&nbsp;</font></td>
  </tr>
<%if (((soggettovecchio.getPaternita()!=null) && (!soggettovecchio.getPaternita().equals("")))
|| ((soggettovecchio.getNomeMadre()!= null) && (!soggettovecchio.getNomeMadre().equals("")))
|| ((soggettovecchio.getCognomeMadre()!= null) && (!soggettovecchio.getCognomeMadre().equals("")))) { %>
	  <tr>
      <td class="l">
      <%if ((soggettovecchio.getPaternita()!=null) && (!soggettovecchio.getPaternita().equals(""))) {%>
      <font class="label">Paternita:&nbsp;&nbsp;</font><font class="campo">
      <%=StringUtils.toStringJSP(soggettovecchio.getPaternita())%>&nbsp;&nbsp;</font><font class="label">
      <% } %>
      <%if (((soggettovecchio.getNomeMadre()!= null) && (!soggettovecchio.getNomeMadre().equals("")))
      || ((soggettovecchio.getCognomeMadre()!= null) && (!soggettovecchio.getCognomeMadre().equals("")))) { %>
      Madre:&nbsp;&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(soggettovecchio.getNomeMadre())%>&nbsp;
      <%=StringUtils.toStringJSP(soggettovecchio.getCognomeMadre())%>
      <% } %>
    </font></td></tr>
<% } %>
<%if (((soggettovecchio.getCodFiscale()!=null)&&(!soggettovecchio.getCodFiscale().equals("")))
  || ((soggettovecchio.getAttoNascita()!=null)&&(!soggettovecchio.getAttoNascita().equals("")))
  || ((soggettovecchio.getCodCs()!=null)&&(!soggettovecchio.getCodCs().equals("")))
  || ((soggettovecchio.getCodAfis()!=null)&&(!soggettovecchio.getCodAfis().equals("")))) { %>
    <tr>
      <td class="l">
      <%if((soggettovecchio.getCodFiscale()!=null)&&(!soggettovecchio.getCodFiscale().equals(""))) {%>
      <font class="label">Codice Fiscale:&nbsp;</font><font class="campo">
      <%=StringUtils.toStringJSP(soggettovecchio.getCodFiscale())%>&nbsp;&nbsp;
      </font>
      <% } %>
      <%if((soggettovecchio.getAttoNascita()!=null)&&(!soggettovecchio.getAttoNascita().equals(""))) {%>
      <font class="label">
      Atto Nascita:&nbsp;</font><font class="campo">
      <%=StringUtils.toStringJSP(soggettovecchio.getAttoNascita())%>&nbsp;&nbsp;
      </font>
      <%}%>
      <%if((soggettovecchio.getCodCs()!=null)&&(!soggettovecchio.getCodCs().equals(""))) {%>
      <font class="label">
      Codice CS:&nbsp;</font><font class="campo">
      <%=StringUtils.toStringJSP(soggettovecchio.getCodCs())%>&nbsp;&nbsp;
      </font>
      <%}%>
      <%if((soggettovecchio.getCodAfis()!=null)&&(!soggettovecchio.getCodAfis().equals(""))) {%>
      <font class="label">
      Codice Afis:&nbsp;</font><font class="campo">
      <%=StringUtils.toStringJSP(soggettovecchio.getCodAfis())%>
      </font>
      <%}%>
      </td>
    </tr>
<% } %>
<%if ((soggettovecchio.getNote()!=null) && (!soggettovecchio.getNote().equals(""))) {%>
    <tr>
      <td class="l"><font  class="label">Note:&nbsp;</font><font class="campo">
     <%=StringUtils.toStringJSP(soggettovecchio.getNote())%>&nbsp;</font></td>
    </tr>
<% } %>
  <br>
  </table>
<table width="100%">
    <tr>
    <td class="Titolo">Modificato In </td>
    </tr>
    <tr><td>&nbsp;</td></tr>
    <tr>
      <td class="l">
      <font class="label">Nome:&nbsp;&nbsp;</font>
      <font class="campo"><%=soggettonuovo.getCognome() %>&nbsp;&nbsp;<%=soggettonuovo.getNome() %>
      </font>
      <font class="label">
      &nbsp;&nbsp;
      <%if (soggettonuovo.getSesso() == "F") { %>
      Nata il:&nbsp;&nbsp;
      <% } else {%>
      Nato il:&nbsp;&nbsp;
      <%}%>
      </font>
      <font class="campo">
      <%
        if(soggettonuovo.getDataNascita() != null)
        {
%>
          <%=DateUtils.getDateToString(soggettonuovo.getDataNascita(),"dd-MM-yyyy")%>&nbsp;
<%
        }
        else
        {
%>
          <%="**-"+StringUtils.toStringJSP(soggettonuovo.getMeseNascita(), "**")+"-"+StringUtils.toStringJSP(soggettonuovo.getAnnoNascita())%>&nbsp;
<%
        }
%>
<% if (soggettonuovo.getDataNascitaPresunta().equals("S")) { %>
&nbsp;&nbsp;(Data Presunta)
<% } %>
<% if (soggettonuovo.getCodStatoNascita().equals("039")) { %>
  			&nbsp;&nbsp;</font><font class="label">a</font>
  					  <font class="campo">&nbsp;&nbsp;<%=StringUtils.toStringJSP(soggettonuovo.getDescrComuneNascita() )%>
  <%        if( (soggettonuovo.getDescrComuneNascita() != null)
                && (!(soggettonuovo.getDescrComuneNascita().equals("")))
                && (!(soggettonuovo.getDescrComuneNascita().equals("-"))) )
            {
  %>
              (<%=soggettonuovo.getCodProvinciaNascita()%>)
  <%
            }
  %>
<% } 
   else 
   { %>
  			&nbsp;&nbsp;</font>
  			<font class="label">a</font>
  			<font class="campo">&nbsp;&nbsp;<%=StringUtils.toStringJSP(soggettonuovo.getDescComuneNascitaEstero()).toUpperCase()%>
<% } %>

&nbsp;&nbsp;<%=StringUtils.toStringJSP(soggettonuovo.getDescrStatoNascita())%>
</font></td></tr>
	
	<tr>
	  	<td class="l">
	  		<font class="label">Stato Cittadinanza:&nbsp;&nbsp;</font>
	  		<font class="campo">&nbsp;&nbsp;<%=StringUtils.toStringJSP(soggettonuovo.getDescrNazionalita()).toUpperCase()%></font>
    	</td>
  	</tr>
  	
<%if (((soggettonuovo.getPaternita()!=null) && (!soggettonuovo.getPaternita().equals("")))
|| ((soggettonuovo.getNomeMadre()!= null) && (!soggettonuovo.getNomeMadre().equals("")))
|| ((soggettonuovo.getCognomeMadre()!= null) && (!soggettonuovo.getCognomeMadre().equals("")))) { %>
	  <tr>
      <td class="l">
      <%if ((soggettonuovo.getPaternita()!=null) && (!soggettonuovo.getPaternita().equals(""))) {%>
      <font class="label">Paternita:&nbsp;&nbsp;</font><font class="campo">
      <%=StringUtils.toStringJSP(soggettonuovo.getPaternita())%>&nbsp;&nbsp;</font><font class="label">
      <% } %>
      <%if (((soggettonuovo.getNomeMadre()!= null) && (!soggettonuovo.getNomeMadre().equals("")))
      || ((soggettonuovo.getCognomeMadre()!= null) && (!soggettonuovo.getCognomeMadre().equals("")))) { %>
      Madre:&nbsp;&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(soggettonuovo.getNomeMadre())%>&nbsp;
      <%=StringUtils.toStringJSP(soggettonuovo.getCognomeMadre())%>
      <% } %>
    </font></td></tr>
<% } %>
<%if (((soggettonuovo.getCodFiscale()!=null)&&(!soggettonuovo.getCodFiscale().equals("")))
  || ((soggettonuovo.getAttoNascita()!=null)&&(!soggettonuovo.getAttoNascita().equals("")))
  || ((soggettonuovo.getCodCs()!=null)&&(!soggettonuovo.getCodCs().equals("")))
  || ((soggettonuovo.getCodAfis()!=null)&&(!soggettonuovo.getCodAfis().equals("")))) { %>
    <tr>
      <td class="l">
      <%if((soggettonuovo.getCodFiscale()!=null)&&(!soggettonuovo.getCodFiscale().equals(""))) {%>
      <font class="label">Codice Fiscale:&nbsp;</font><font class="campo">
      <%=StringUtils.toStringJSP(soggettonuovo.getCodFiscale())%>&nbsp;&nbsp;
      </font>
      <% } %>
      <%if((soggettonuovo.getAttoNascita()!=null)&&(!soggettonuovo.getAttoNascita().equals(""))) {%>
      <font class="label">
      Atto Nascita:&nbsp;</font><font class="campo">
      <%=StringUtils.toStringJSP(soggettonuovo.getAttoNascita())%>&nbsp;&nbsp;
      </font>
      <%}%>
      <%if((soggettonuovo.getCodCs()!=null)&&(!soggettonuovo.getCodCs().equals(""))) {%>
      <font class="label">
      Codice CS:&nbsp;</font><font class="campo">
      <%=StringUtils.toStringJSP(soggettonuovo.getCodCs())%>&nbsp;&nbsp;
      </font>
      <%}%>
      <%if((soggettonuovo.getCodAfis()!=null)&&(!soggettonuovo.getCodAfis().equals(""))) {%>
      <font class="label">
      Codice Afis:&nbsp;</font><font class="campo">
      <%=StringUtils.toStringJSP(soggettonuovo.getCodAfis())%>
      </font>
      <%}%>
      </td>
    </tr>
<% } %>
<%if ((soggettonuovo.getNote()!=null) && (!soggettonuovo.getNote().equals(""))) {%>
    <tr>
      <td class="l"><font  class="label">Note:&nbsp;</font><font class="campo">
     <%=StringUtils.toStringJSP(soggettonuovo.getNote())%>&nbsp;</font></td>
    </tr>
<% } %>
  <br>
  </table>

  <table cellspacing=2 cellpadding=2>

<tr> <td class ="Titolo" colspan =7>Selezionare i procedimenti ai quali associare la modifica</td></tr>
<tr> <td >&nbsp;</td></tr>

<% if( lProfilo.isSige()) { %>
<tr> <td class ="Titolo" colspan = 7 >Procedimenti SIGE modificabili</td></tr>
<tr><td>&nbsp;</td></tr>

   	 <jsp:include page="/jsp/files/siap/sico/storicosoggetto/IncFasSigeUff.jsp"/>
 <%}else { %>
 <tr> <td class ="Titolo" colspan =7>Procedimenti dell'ufficio</td></tr>
<tr><td>&nbsp;</td></tr>
 
   <tr>
<%   if( lProfilo.isSius()) 
	// SIUS
{%>

      <td class="int">Numero SIUS</td>
      <td class="int">Descr.Ufficio</td>
      <td class="int">Data Richiesta Proc.</td>
      <td class="int">Contenuto</td>
      <td class="int">Data Iscriz. Proc.</td>
<%}else{%>
      <td class="int">Data Titolo Esecutivo</td>
      <td class="int">Autorità Titolo Esecutivo</td>
      <td class="int">Data Irrevocabilità</td>
      <td class="int">Numero SIEP</td>
      <td class="int">Data di Iscrizione</td>
<%}%>
      <td class="int">Stato del Procedimento</td>
      <td class="int">Modifica</td>
    </tr>

<%
    Iterator itx = fascicoliUfficio.iterator();
	int i = 0; 
	while ( itx.hasNext())
    {
    	if( lProfilo.isSius()) 
	   // SIUS
{

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

      <td class="c">
  <%if(fascicolo.getChiaveUfficio().equals(chiaveUfficio)){
%>
        <input type="checkbox" name="<%="fascicolo"+i%>" value="<%=fascicolo.getIdFascicoloSius()+""%>"  />
<%}else{%>
        <input type="checkbox" name="<%="fascicolo"+i%>" value="<%=fascicolo.getIdFascicoloSius()+"" %>" disabled  />

<%}%>
  </td>
    </tr>

<%
}
else
{
   FascicoloSiepModel fascicolo = (FascicoloSiepModel)itx.next();

%>
    <tr>

      <td class="c">
      <font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getSentenza().getDataProvvedimento(),"dd-MM-yyyy"),"")%></font></td>
      <td class="c"><font class="label"><%=StringUtils.toStringJSP(fascicolo.getSentenza().getDescrTipoAutoritaEmittente(),"")%></font> di <font class="label"><%=fascicolo.getSentenza().getDescrLuogoEmittente()%></font></td>
      <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getDataIrrevocabilita(),"dd-MM-yyyy"),"")%></font></td>
      <td class="c"><font class="label"><%=StringUtils.toStringJSP(fascicolo.getChiaveAnno(),"")%>/<%=StringUtils.toStringJSP(fascicolo.getChiaveProgr(),"")%></font></td>
      <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getDataIscrizione(),"dd-MM-yyyy"))%>&nbsp;</font></td>
      <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
      <%--td class="c"><font class="label"><--%=DateUtils.getDateToString(fascicolo.getSentenza().getDataProvvedimento(),"dd-MM-yyyy")%></font></td--%>
      <%--td class="c"><font class="label"><--%=fascicolo.getSentenza().getDescrTipoProvvedimento()%></font></td--%>
      <%--td class="c"><font class="label"><--%=fascicolo.getSentenza().getDescrLuogoEmittente()%></font></td--%>
      <td class="c"><font class="label"><%=StringUtils.toStringJSP(fascicolo.getDescrStatoProcedimento())%>&nbsp;</font></td>

      <td class="c">
  <%if(fascicolo.getChiaveUfficio().equals(chiaveUfficio)){
%>
        <input type="checkbox" name="<%="fascicolo"+i%>" value="<%=fascicolo.getIdFascicoloSiep()+""%>"  />
<%}else{%>
        <input type="checkbox" name="<%="fascicolo"+i%>" value="<%=fascicolo.getIdFascicoloSiep()+"" %>" disabled  />

<%} %>
  </td>
  </tr>
<%
}  // end if - else ( lProfilo.isSius())
  i++;
 }  // end while
 } // end if - else ( lProfilo.isSige())
%>
<%if(fascicoliAltriUffici.size()>0){
 if( lProfilo.isSige()) { %>
<tr> <td class ="Titolo" colspan =7>Procedimenti SIGE non modificabili</td></tr>
<tr><td>&nbsp;</td></tr>

   	 <jsp:include page="/jsp/files/siap/sico/storicosoggetto/IncFasSigeAltriUff.jsp"/>
 <%}else { %>
 <tr><td>&nbsp;</td></tr>
<tr> <td class ="Titolo" colspan =7>Procedimenti di altri uffici</td></tr>
<tr><td>&nbsp;</td></tr>
   <tr>
<%
if( lProfilo.isSius()) 
  // SIUS
{%>

      <td class="int">Numero SIUS</td>
      <td class="int">Descr.Ufficio</td>
      <td class="int">Data Richiesta Proc.</td>
      <td class="int">Contenuto</td>
      <td class="int">Data Iscriz. Proc.</td>
<%}else{%>

<td class="int">Data Titolo Esecutivo</td>
      <td class="int">Autorità Titolo Esecutivo</td>
      <td class="int">Data Irrevocabilità</td>
      <td class="int">Numero SIEP</td>
      <td class="int">Data di Iscrizione</td>
<%}%>
        <td class="int">Stato del Procedimento</td>

       <td class="int">Modifica</td>
    </tr>

<%


    Iterator itxaltri = fascicoliAltriUffici.iterator();
    int y = 0;
    while ( itxaltri.hasNext())
    {
    	if( lProfilo.isSius()) 
  // SIus
{


      FascicoloGPModel FascSiusGP = (FascicoloGPModel) itxaltri.next();
      FascicoloSiusModel fascicoloAltriUff = (FascicoloSiusModel)FascSiusGP.getFascicoloSiusModel();


%>
    <tr>
      <td class="c"><font class="label"><%=StringUtils.toStringJSP(fascicoloAltriUff.getChiaveAnno(),"")%>/<%=StringUtils.toStringJSP(fascicoloAltriUff.getChiaveProgr(),"")%></font></td>
      <td class="c"><font class="label"><%=StringUtils.toStringJSP(fascicoloAltriUff.getDescrTipoUfficio(),"")%>/<%=StringUtils.toStringJSP(fascicoloAltriUff.getDescrComuneUfficio(),"")%></font></td>
      <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(FascSiusGP.getGeneraleProcedimentoModel().getDataRichiesta(),"dd-MM-yyyy"),"-")%></font></td>
      <td class="c"><font class="label"><%=StringUtils.toStringJSP(FascSiusGP.getGeneraleProcedimentoModel().getDescrOggettoProcedimento())%></font></td>
      <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicoloAltriUff.getDataIscrizione(),"dd-MM-yyyy"),"-")%></font></td>
      <td class="c"><font class="label"><%=StringUtils.toStringJSP(FascSiusGP.getFascicoloSiusModel().getDescrStatoFascicolo(),"")%>&nbsp;</font></td>
      <td class="c">
        <input type="checkbox" name="<%="fascicolo"%>" value="<%=fascicoloAltriUff.getIdFascicoloSius()+"" %>" disabled  />
  </td>

    </tr>
<%
}else{
      FascicoloSiepModel fascicoloAltriUff = (FascicoloSiepModel)itxaltri.next();

%>
    <tr>

      <td class="c">
      <font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicoloAltriUff.getSentenza().getDataProvvedimento(),"dd-MM-yyyy"),"")%></font></td>
      <td class="c"><font class="label"><%=StringUtils.toStringJSP(fascicoloAltriUff.getSentenza().getDescrTipoAutoritaEmittente(),"")%></font> di <font class="label"><%=fascicoloAltriUff.getSentenza().getDescrLuogoEmittente()%></font></td>
      <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicoloAltriUff.getDataIrrevocabilita(),"dd-MM-yyyy"),"")%></font></td>
      <td class="c"><font class="label"><%=StringUtils.toStringJSP(fascicoloAltriUff.getChiaveAnno(),"")%>/<%=StringUtils.toStringJSP(fascicoloAltriUff.getChiaveProgr(),"")%></font></td>
      <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicoloAltriUff.getDataIscrizione(),"dd-MM-yyyy"))%>&nbsp;</font></td>
      <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
      <%--td class="c"><font class="label"><--%=DateUtils.getDateToString(fascicolo.getSentenza().getDataProvvedimento(),"dd-MM-yyyy")%></font></td--%>
      <%--td class="c"><font class="label"><--%=fascicolo.getSentenza().getDescrTipoProvvedimento()%></font></td--%>
      <%--td class="c"><font class="label"><--%=fascicolo.getSentenza().getDescrLuogoEmittente()%></font></td--%>
      <td class="c"><font class="label"><%=StringUtils.toStringJSP(fascicoloAltriUff.getDescrStatoProcedimento())%>&nbsp;</font></td>

      <td class="c">
        <input type="checkbox" name="<%="fascicolo"%>" value="<%=fascicoloAltriUff.getIdFascicoloSiep()+"" %>" disabled  />
  </td>

    </tr>
<%
}
  y++;
  }
 } // end if - else ( lProfilo.isSige())   
}
%>

    </table>
    <br>
    <br>
    <input class=bottone  type="submit" value="Conferma" <%=disabilita%>>
    <input type="HIDDEN" name="numerofascicoli" value="<%=fascicoliUfficio.size()%>">
   <input type="HIDDEN" name="numerofascicoliAltriUff" value="<%=fascicoliAltriUffici.size()%>">
    <input type="HIDDEN" name="<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>" value="<%=soggettonuovo.getIdSoggetto()%>">
    <input type="HIDDEN" name="<%=ICostantiSoggetto.CAMPO_COD_FISCALE%>" value="<%=soggettonuovo.getCodFiscale()%>">
    <input type="HIDDEN" name="<%=ICostantiSoggetto.CAMPO_COD_CS%>" value="<%=soggettonuovo.getCodCs()%>">
    <input type="HIDDEN" name="<%=ICostantiSoggetto.CAMPO_COD_AFIS%>" value="<%=soggettonuovo.getCodAfis()%>">
    <input type="HIDDEN" name="<%=ICostantiSoggetto.CAMPO_COGNOME%>" value="<%=soggettonuovo.getCognome()%>">
    <input type="HIDDEN" name="<%=ICostantiSoggetto.CAMPO_NOME%>" value="<%=soggettonuovo.getNome()%>">
    <input type="HIDDEN" name="<%=ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>" value="<%=StringUtils.toStringJSP(soggettonuovo.getAnnoNascita())%>">
    <input type="HIDDEN" name="<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>" value="<%=StringUtils.toStringJSP(soggettonuovo.getMeseNascita())%>">
    <input type="HIDDEN" name="<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(soggettonuovo.getDataNascita(),"dd")) %>">
    <input type="HIDDEN" name="<%=ICostantiSoggetto.CAMPO_DATA_NASCITA_PRESUNTA%>" value="<%=soggettonuovo.getDataNascitaPresunta()%>">
    <!-- 20210830 MEV_21 Valorizzazione Codice Comune di Nascita -->
    <input type="HIDDEN" name="<%=ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA%>" value="<%=soggettonuovo.getCodComuneNascita()%>">
    <input type="HIDDEN" name="DescrComuneNascita" value="<%=soggettonuovo.getDescrComuneNascita()%>">

    <input type="HIDDEN" name="<%=ICostantiSoggetto.CAMPO_COD_STATO_NASCITA%>" value="<%=soggettonuovo.getCodStatoNascita()%>">
    <input type="HIDDEN" name="<%=ICostantiSoggetto.CAMPO_DESC_COMUNE_NASCITA_ESTERO%>" value="<%=soggettonuovo.getDescComuneNascitaEstero()%>">
    <input type="HIDDEN" name="<%=ICostantiSoggetto.CAMPO_NAZIONALITA%>" value="<%=soggettonuovo.getNazionalita()%>">
    <input type="HIDDEN" name="<%=ICostantiSoggetto.CAMPO_PATERNITA%>" value="<%=soggettonuovo.getPaternita() %>">
    <input type="HIDDEN" name="<%=ICostantiSoggetto.CAMPO_COGNOME_MADRE%>" value="<%=soggettonuovo.getCognomeMadre()%>">
    <input type="HIDDEN" name="<%=ICostantiSoggetto.CAMPO_NOME_MADRE%>" value="<%=soggettonuovo.getNomeMadre()%>">
    <input type="HIDDEN" name="<%=ICostantiSoggetto.CAMPO_SESSO%>" value="<%=soggettonuovo.getSesso()%>">
    <input type="HIDDEN" name="<%=ICostantiSoggetto.CAMPO_ATTO_NASCITA%>" value="<%=soggettonuovo.getAttoNascita()%>">
    <input type="HIDDEN" name="<%=ICostantiSoggetto.CAMPO_NOTE%>" value="<%=soggettonuovo.getNote()%>">
    <input type="HIDDEN" name="<%=ICostantiSoggetto.CAMPO_COD_COMUNE_CASELLARIO%>" value="<%=soggettonuovo.getCodComuneCasellario()%>">
    <input type="HIDDEN" name="<%=ICostantiSoggetto.CAMPO_FLAG_PRESENZA_FASCICOLO%>" value="<%=soggettonuovo.getFlagPresenzaFascicolo()%>">
    <input type="HIDDEN" name="<%=ICostantiSoggetto.CAMPO_ETA_PRESUNTA_ANNI%>" value="<%=StringUtils.toStringJSP(soggettonuovo.getEtaPresuntaAnni())%>">
    <input type="HIDDEN" name="<%=ICostantiSoggetto.CAMPO_ETA_PRESUNTA_MESI%>" value="<%=StringUtils.toStringJSP(soggettonuovo.getEtaPresuntaMesi())%>">
    <%-- MERGE v10 COLLAUDO: aggiunti campi nascosti --%>
    <input type="HIDDEN" name="<%=ICostantiSoggetto.CAMPO_ANNO_DATA_COMMESSO_REATO%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(soggettonuovo.getDataReatoSius(),"yyyy"))%>">
    <input type="HIDDEN" name="<%=ICostantiSoggetto.CAMPO_MESE_DATA_COMMESSO_REATO%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(soggettonuovo.getDataReatoSius(),"MM"))%>">
    <input type="HIDDEN" name="<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_COMMESSO_REATO%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(soggettonuovo.getDataReatoSius(),"dd")) %>">
    <input type="HIDDEN" name="profilo" value="<%=profilo%>">
	<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.storicosoggetto.action.ActModificaSoggettoeStorici">

  </FORM>
  <br>
  <script language="JavaScript" type="text/javascript">
      var frmvalidator  = new Validator("LoadModificaSoggettoPerFascicolo");

      frmvalidator.setAddnlValidationFunction("Verify");
    </script>

  </body>
</html>