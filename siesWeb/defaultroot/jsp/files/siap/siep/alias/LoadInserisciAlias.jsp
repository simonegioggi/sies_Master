<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="siap.siep.alias.action.ICostantiAlias"%>
<%@ page import="siap.siep.alias.model.AliasModel"%>

<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="lAlias" scope="request" class="siap.siep.alias.model.AliasModel"/>


<html>
<head>
<title>[S.I.E.S.] - GestioneAlias </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>" ></script>

</head>


		<body class="corpo">
			<table>
			<tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
			 <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
 <%
			 AliasModel lModel = new AliasModel();
			 String lAzione = new String();
			 if( modalita.equals("I") )
			 {
			   lAzione = "siap.siep.alias.action.ActInserisciAlias";

%>

			<font class="campo">Inserimento di un Alias</font>
			 <%

			   }

			   else if( modalita.equals("M") )

			   {

			   lAzione = "siap.sico.alias.action.ActModificaAlias";
			   //lModel = alias;
			  %>			     <font class="campo">Modifica di un Alias</font>
			  <%}%>			 </td>
</tr>
</table>
		<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciAlias">
		 <table cellspacing=4 cellpadding=4>		<tr>
				<td class="l">IdAlias</td>
				<td class="l"><input value="<%=lAlias.getIdAlias() %>" type="text" name="<%= ICostantiAlias.CAMPO_ID_ALIAS %>"  ></td>
		</tr>
		<tr>
				<td class="l">Cognome</td>
				<td class="l"><input value="<%=lAlias.getCognome() %>" type="text" name="<%= ICostantiAlias.CAMPO_COGNOME %>"  ></td>
		</tr>
		<tr>
				<td class="l">Nome</td>
				<td class="l"><input value="<%=lAlias.getNome() %>" type="text" name="<%= ICostantiAlias.CAMPO_NOME %>"  ></td>
		</tr>
		<tr>
				<td class="l">Paternita</td>
				<td class="l"><input value="<%=lAlias.getPaternita() %>" type="text" name="<%= ICostantiAlias.CAMPO_PATERNITA %>"  ></td>
		</tr>
		<tr>
				<td class="l">CodFiscale</td>
				<td class="l"><input value="<%=lAlias.getCodFiscale() %>" type="text" name="<%= ICostantiAlias.CAMPO_COD_FISCALE %>"  ></td>
		</tr>
		<tr>
				<td class="l">CodCs</td>
				<td class="l"><input value="<%=lAlias.getCodCs() %>" type="text" name="<%= ICostantiAlias.CAMPO_COD_CS %>"  ></td>
		</tr>
		<tr>
				<td class="l">CodAfis</td>
				<td class="l"><input value="<%=lAlias.getCodAfis() %>" type="text" name="<%= ICostantiAlias.CAMPO_COD_AFIS %>"  ></td>
		</tr>
		<tr>
				<td class="l">AttoNascita</td>
				<td class="l"><input value="<%=lAlias.getAttoNascita() %>" type="text" name="<%= ICostantiAlias.CAMPO_ATTO_NASCITA %>"  ></td>
		</tr>
		<tr>
				<td class="l">Sesso</td>
				<td class="l"><input value="<%=lAlias.getSesso() %>" type="text" name="<%= ICostantiAlias.CAMPO_SESSO %>"  ></td>
		</tr>
		<tr>
				<td class="l">CodComuneNascita</td>
				<td class="l"><input value="<%=lAlias.getCodComuneNascita() %>" type="text" name="<%= ICostantiAlias.CAMPO_COD_COMUNE_NASCITA %>"  ></td>
		</tr>
		<tr>
				<td class="l">CodProvinciaNascita</td>
				<td class="l"><input value="<%=lAlias.getCodProvinciaNascita() %>" type="text" name="<%= ICostantiAlias.CAMPO_COD_PROVINCIA_NASCITA %>"  ></td>
		</tr>
		<tr>
				<td class="l">CodStatoNascita</td>
				<td class="l"><input value="<%=lAlias.getCodStatoNascita() %>" type="text" name="<%= ICostantiAlias.CAMPO_COD_STATO_NASCITA %>"  ></td>
		</tr>
		<tr>
				<td class="l">DataNascita</td>
				<td class="l"><input value="<%=lAlias.getDataNascita() %>" type="text" name=""  ></td>
		</tr>
		<tr>
				<td class="l"><input value="" type="text" size="2" maxlength="2" name="<%= ICostantiAlias.CAMPO_GIORNO_DATA_NASCITA %>"  ></td>
				<td class="l"><input value="" type="text" size="2" maxlength="2" name="<%= ICostantiAlias.CAMPO_MESE_DATA_NASCITA %>"  ></td>
				<td class="l"><input value="" type="text" size="4" maxlength="4" name="<%= ICostantiAlias.CAMPO_ANNO_DATA_NASCITA %>"  ></td>
		</tr>
		<tr>
				<td class="l">Note</td>
				<td class="l"><input value="<%=lAlias.getNote() %>" type="text" name="<%= ICostantiAlias.CAMPO_NOTE %>"  ></td>
		</tr>
		<tr>
				<td class="l">CodOperatoreInserimento</td>
				<td class="l"><input value="<%=lAlias.getCodOperatoreInserimento() %>" type="text" name="<%= ICostantiAlias.CAMPO_COD_OPERATORE_INSERIMENTO %>"  ></td>
		</tr>
		<tr>
				<td class="l">CodUfficioInserimento</td>
				<td class="l"><input value="<%=lAlias.getCodUfficioInserimento() %>" type="text" name="<%= ICostantiAlias.CAMPO_COD_UFFICIO_INSERIMENTO %>"  ></td>
		</tr>
		<tr>
				<td class="l">CodOperatoreAggiornamento</td>
				<td class="l"><input value="<%=lAlias.getCodOperatoreAggiornamento() %>" type="text" name="<%= ICostantiAlias.CAMPO_COD_OPERATORE_AGGIORNAMENTO %>"  ></td>
		</tr>
		<tr>
				<td class="l">CodUfficioAggiornamento</td>
				<td class="l"><input value="<%=lAlias.getCodUfficioAggiornamento() %>" type="text" name="<%= ICostantiAlias.CAMPO_COD_UFFICIO_AGGIORNAMENTO %>"  ></td>
		</tr>
		<tr>
				<td class="l">SogIdSoggetto</td>
				<td class="l"><input value="<%=lAlias.getSogIdSoggetto() %>" type="text" name="<%= ICostantiAlias.CAMPO_SOG_ID_SOGGETTO %>"  ></td>
		</tr>

</table>
		</form>
	</body>
</html>