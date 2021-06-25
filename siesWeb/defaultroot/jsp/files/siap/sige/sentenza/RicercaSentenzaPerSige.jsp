<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.math.BigDecimal" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.web.Action" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.log.LogF3B"%>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza" %>
<%@ page import="siap.siep.sentenza.model.SentenzaModel" %>
<%@ page import="siap.sige.sentenza.model.FasSigeSentenzaModel" %>
<%@ page import="siap.sige.sentenza.model.FasSigeSentenzaRicercaModel" %>
<%@ page import="siap.sige.sentenza.action.ICostantiFasSigeSentenza" %>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="sentenzeSige" scope="request" class="java.util.Vector" />
<jsp:useBean id="senMod" scope="request" class="siap.siep.sentenza.model.SentenzaModel" />
<jsp:useBean id="flagRicercaData" scope="request" class="java.lang.String" />
<jsp:useBean id="tipoRegistro" scope="request" class="java.lang.String" />
<jsp:useBean id="AzioneChiamante" scope="request" class="java.lang.String" />
<jsp:useBean id="RequestForPaging" scope="request" class="java.lang.String" />
<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>
<jsp:useBean id="ambitoRicerca" scope="request" class="java.lang.String" />

<%
  // presenza del Link per il bottone di ritorno
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
%>

<%@page import="siap.sige.sentenza.action.ICostantiFasSigeSentenza"%>
<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Sentenza</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  </head>

  <BODY class="corpo">
  <FORM method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">

    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;<font class="campo">Elenco Titoli Esecutivi Con Procedimenti SIGE</font></td>
  			<jsp:include page="/jsp/files/siap/siep/sentenza/BottoneInserimentoSige.jsp"></jsp:include>
       </tr>
    </table>

<%--
   if(!(senMod.getAnnoSentenza()==null && senMod.getNumeroSentenza().length()<1 &&
    		senMod.getDataProvvedimento()==null &&
    		senMod.getDataProvvedimentoIniziale()==null && senMod.getDataProvvedimentoFinale()==null &&
    		senMod.getNumeroRegeGip().length()<2 && senMod.getNumeroRegeDib().length()<2 && senMod.getNumeroRegeGup().length()<2 &&
    		senMod.getNumeroRegeCas().length()<2 && senMod.getNumeroRegeCap().length()<2 &&
    		senMod.getNumeroRegeCasap().length()<2 && senMod.getAnnoRegeGip()==null && senMod.getAnnoRegeGup()==null &&
    		senMod.getAnnoRegeDib()==null && senMod.getAnnoRegeCas()==null &&
    		senMod.getAnnoRegeCap()==null && senMod.getAnnoRegeCasap()==null &&
    		senMod.getNumeroRegePm().length()<1 && senMod.getAnnoRegePm()==null))
    {
--%>
  	<table>
      <tr>
        <td class="Cliccabile">Criteri di Ricerca selezionati:</td>
      </tr>
<%
			if(senMod.getCodUfficioInserimento()!=null )
			{%>
		  	<tr>
		    	<td class="lVerdeNB">Ambito di Ricerca: Solo Ufficio</td>
		  	</tr>
		<%}else{%>
	  		<tr>
    			<td class="lVerdeNB">Ambito di Ricerca: Intero Distretto</td>
  			</tr>
		<%}			
      if(senMod.getAnnoSentenza()!=null )
      {%>
        <tr>
          <td class="lVerdeNB">Anno Sentenza: <%=senMod.getAnnoSentenza().toString()%></td>
        </tr>
		<%}
			if(senMod.getNumeroSentenza().length()>1 )
			{%>
				<tr>
    			<td class="lVerdeNB">Numero Sentenza: <%=senMod.getNumeroSentenza().toString()%></td>
  			</tr>
		<%}
      if(senMod.getAnnoRegePm()!=null )
      {%>
        <tr>
          <td class="lVerdeNB">Anno R.G.N.R.: <%=senMod.getAnnoRegePm().toString()%></td>
        </tr>
		<%}
			if(senMod.getNumeroRegePm().length()>1 )
			{%>
				<tr>
    			<td class="lVerdeNB">Numero R.G.N.R.: <%=senMod.getNumeroRegePm().toString()%></td>
  			</tr>
		<%}
      if(senMod.getAnnoRegeCap()!=null)
      {%>
        <tr>
          <td class="lVerdeNB">Anno Reg.Gen.: <%=senMod.getAnnoRegeCap().toString()%></td>
        </tr>
		<%}
			if(senMod.getNumeroRegeCap().length()>1 )
			{%>
				<tr>
    			<td class="lVerdeNB">Numero Reg.Gen.: <%=senMod.getNumeroRegeCap().toString()%></td>
  			</tr>
		<%}
      if(senMod.getAnnoRegeCas()!=null)
      {%>
        <tr>
          <td class="lVerdeNB">Anno Reg.Gen.: <%=senMod.getAnnoRegeCas().toString()%></td>
        </tr>
		<%}
			if(senMod.getNumeroRegeCas().length()>1 )
			{%>
				<tr>
    			<td class="lVerdeNB">Numero Reg.Gen.: <%=senMod.getNumeroRegeCas().toString()%></td>
  			</tr>
		<%}
      if(senMod.getAnnoRegeCasap()!=null)
      {%>
        <tr>
          <td class="lVerdeNB">Anno Reg.Gen.: <%=senMod.getAnnoRegeCasap().toString()%></td>
        </tr>
		<%}
			if(senMod.getNumeroRegeCasap().length()>1 )
			{%>
				<tr>
    			<td class="lVerdeNB">Numero Reg.Gen.: <%=senMod.getNumeroRegeCasap().toString()%></td>
  			</tr>
		<%}
      if(senMod.getAnnoRegeDib()!=null)
      {%>
        <tr>
          <td class="lVerdeNB">Anno Reg.Gen.: <%=senMod.getAnnoRegeDib().toString()%></td>
        </tr>
		<%}
			if(senMod.getNumeroRegeDib().length()>1 )
			{%>
				<tr>
    			<td class="lVerdeNB">Numero Reg.Gen.: <%=senMod.getNumeroRegeDib().toString()%></td>
  			</tr>
<%
}
if (senMod.getAnnoRegeGip() != null) {
%>
	<tr>
		<td class="lVerdeNB">Anno Reg.Gen.: <%=senMod.getAnnoRegeGip().toString()%></td>
	</tr>
<%
}
if (senMod.getNumeroRegeGip().length() > 1) {
%>
	<tr>
		<td class="lVerdeNB">Numero Reg.Gen.: <%=senMod.getNumeroRegeGip().toString()%></td>
	</tr>
<%
}
// MEV_66: aggiunte quattro nuove proprietà
if (senMod.getAnnoRegeGup() != null) {
%>
	<tr>
		<td class="lVerdeNB">Anno Reg.Gen.: <%=senMod.getAnnoRegeGup().toString()%></td>
	</tr>
<%
}
// Ticket#20210514015: aggiunto test != null perchè andava in nullpointer
if (senMod.getNumeroRegeGup()!=null && senMod.getNumeroRegeGup().length() > 1) {
%>
	<tr>
		<td class="lVerdeNB">Numero Reg.Gen.: <%=senMod.getNumeroRegeGup().toString()%></td>
	</tr>
<%
}

if (senMod.getAnnoRegeCapsm() != null) {
%>
	<tr>
		<td class="lVerdeNB">Anno Reg.Gen.: <%=senMod.getAnnoRegeCapsm().toString()%></td>
	</tr>
<%
}
//Ticket#20210514015: aggiunto test != null perchè andava in nullpointer
if (senMod.getNumeroRegeCapsm()!=null && senMod.getNumeroRegeCapsm().length() > 1) {
%>
	<tr>
		<td class="lVerdeNB">Numero Reg.Gen.: <%=senMod.getNumeroRegeCapsm().toString()%></td>
	</tr>
<%
}

if (!(senMod.getDataProvvedimentoIniziale() == null) || !(senMod.getDataProvvedimentoFinale() == null)) {
%>
	<tr>
		<td class="lVerdeNB">Data Sentenza :&nbsp;&nbsp;
<%
          if(!(senMod.getDataProvvedimentoIniziale()==null))
          {
%>
            Dal <%=DateUtils.getDateToString(senMod.getDataProvvedimentoIniziale(), "dd/MM/yyyy")%>&nbsp;&nbsp;
<%        }
					if(!(senMod.getDataProvvedimentoFinale()==null))
          {%>
            &nbsp;Al&nbsp;&nbsp;<%=DateUtils.getDateToString(senMod.getDataProvvedimentoFinale(), "dd/MM/yyyy")%>
            </td>
				<%}%>
				</tr>
		<%}
      if(!(senMod.getDataProvvedimento()==null))
      {%>
        <tr>
          <td class="lVerdeNB">Data Sentenza :&nbsp;&nbsp;<%=DateUtils.getDateToString(senMod.getDataProvvedimento(), "dd/MM/yyyy")%></td>
				</tr>
		<%}%>

  </table>


<%if (!(RequestForPaging.equals("NO"))) {%>
	<jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
<%}%>

<br>
  <table cellpadding=2 cellspacing=2>

    <tr>
<%
      if(flagRicercaData.equals("S"))
      {%>
        <%--td class="int">Data Irrevocabilità</td--%>
        <td class="int">Anno/Numero</td>
        <td class="int">Tipo Tit.Esecutivo</td>
        <td class="int">Autorità</td>
        <td class="int">Data Tit.Esecutivo</td>
        <td class="int">Altro grado di giudizio</td>
        <td class="int">Sentenza della cassazione</td>
		<%}else{%>
        <td class="int">Data Tit.Esecutivo</td>
        <td class="int">Anno/Numero</td>
        <td class="int">Tipo Tit.Esecutivo</td>
        <td class="int">Autorità</td>
        <%--td class="int">Data Irrevocabilità</td--%>
        <td class="int">Altro grado di giudizio</td>
        <td class="int">Sentenza della cassazione</td>
        <td class="int">N.ro Proc. SIGE</td>
		<%}%>
      <td class="int">Azioni</td>
    </tr>
<%
    Iterator itx = sentenzeSige.iterator();
    while ( itx.hasNext())
    {
    	FasSigeSentenzaRicercaModel sentenza = (FasSigeSentenzaRicercaModel)itx.next();
			SentenzaModel lSenMod = new SentenzaModel(sentenza.getSentenzaModel());
%>
      <tr>
<%
      if(flagRicercaData.equals("S"))
      {%>
        <%--td class=C><font class="label">
          <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sentenza.action.ActLoadDettaglioSentenza&<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>=<%=lSenMod.getIdSentenza()%> <%=retParam%>" Title="Dettaglio Sentenza" >
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(lSenMod.getDataIrrevocabilitaFinale(),"dd-MM-yyyy"))%>
          </a>
        </font></td--%>
        <td class=C>
          <%=StringUtils.toStringJSP(lSenMod.getNumeroSentenza()).length()>1
           ? StringUtils.toStringJSP(lSenMod.getAnnoSentenza())+"/"+StringUtils.toStringJSP(lSenMod.getNumeroSentenza()) : "-"%>
        </td>
        <td class=C>
          <%=StringUtils.toStringJSP(lSenMod.getDescrTipoProvvedimento())%>
        </td>
        <td class=C>
          <%=StringUtils.toStringJSP(lSenMod.getDescrTipoAutoritaEmittente())%> di <%=StringUtils.toStringJSP(lSenMod.getDescrLuogoEmittente())%>
        </td>
        <td class=C>
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(lSenMod.getDataProvvedimento(),"dd-MM-yyyy"))%>
        </td>
        <td class=C>
<%
          if (lSenMod.isAltroGiudizio())
          {%>
            <img src="/images/TickRed.gif">
				<%}else{%>
            &nbsp;
				<%}%>
        </td>
        <td class=C>
<%
          if (lSenMod.isSentenzaCassazione())
          {%>
            <img src="/images/TickRed.gif">
				<%}else{%>
            &nbsp;
				<%}%>
        </td>
		<%}
      else
      {%>
        <td class=C><font class="label">
          <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sentenza.action.ActLoadDettaglioSentenza&<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>=<%=lSenMod.getIdSentenza()%> <%=retParam%>" Title="Dettaglio Sentenza" >
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(lSenMod.getDataProvvedimento(),"dd-MM-yyyy"))%>
          </a>
        </font></td>
        <td class=C>
          <%=StringUtils.toStringJSP(lSenMod.getNumeroSentenza()).length()>1
           ? StringUtils.toStringJSP(lSenMod.getAnnoSentenza())+"/"+StringUtils.toStringJSP(lSenMod.getNumeroSentenza()) : "-"%>
        </td>
        <td class=C>
          <%=StringUtils.toStringJSP(lSenMod.getDescrTipoProvvedimento())%>
        </td>
        <td class=C>
          <%=StringUtils.toStringJSP(lSenMod.getDescrTipoAutoritaEmittente())%> di <%=StringUtils.toStringJSP(lSenMod.getDescrLuogoEmittente())%>
        </td>
        <%--td class=C>&nbsp;
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(lSenMod.getDataIrrevocabilitaFinale(),"dd-MM-yyyy")).length()>2
           ? StringUtils.toStringJSP(DateUtils.getDateToString(lSenMod.getDataIrrevocabilitaFinale(),"dd-MM-yyyy")) : "-"%>
        </td--%>
        <td class=C>
<%
          if (lSenMod.isAltroGiudizio())
          {%>
            <img src="/images/TickRed.gif">
				<%}else{%>
            &nbsp;
				<%}%>
        </td>
        <td class=C>
<%
          if (lSenMod.isSentenzaCassazione())
          {%>
            <img src="/images/TickRed.gif">
				<%}else{%>
            &nbsp;
				<%}%>
        </td>
        <td class=C>
          <%=StringUtils.toStringJSP(sentenza.getNumFascicoliSige())%>
        </td>
		<%}%>
    <td class=C>
        <jsp:include page="<%=ICostantiFasSigeSentenza.PG_BUTTONS_SENTENZA%>">
           <jsp:param name="CampoAzioneChiamante" value="NomeAzione" />
           <jsp:param name="ValoreAzioneChiamante" value="<%=AzioneChiamante%>" />
           <jsp:param name="CampoIdEntita" value="<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>"/>
           <jsp:param name="ValoreIdEntita" value="<%=lSenMod.getIdSentenza()%>"/>
           <jsp:param name="CampoIdEntitaProvv" value="<%=ICostantiSentenza.CAMPO_COD_TIPO_PROVVEDIMENTO%>"/>
           <jsp:param name="ValoreIdEntitaProvv" value="<%=lSenMod.getCodTipoProvvedimento()%>"/>
           <jsp:param name="ambitoRicerca" value="<%=ambitoRicerca%>"/>
        </jsp:include>
    </td>
    </tr>
<%  }%>
    </table>
  </FORM>
  <br>

</body>
</html>