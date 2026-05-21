<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.security.model.ProfileModel"%>
<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoModel"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>
<%@ page import="siap.sige.fascicolo.model.FascicoloSigeEstesoModel" %>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>

<jsp:useBean id="foro"           scope="request" class="java.lang.String"/>
<jsp:useBean id="comune"         scope="request" class="java.lang.String"/>
<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel"/>
<jsp:useBean id="codFunzione"    scope="request" class="java.lang.String"/>
<jsp:useBean id="modalita"         scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaAvvocato" scope="request" class="java.lang.String"/>

<html>
<head>
<title>[S.I.E.S.] - GestioneAvvocato </title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript" src="/html/gen_validatorv2.js"></script>
<script language="JavaScript">
  var desktop;
   
  
  function Inserisci(lScelta)
  {
    if (lScelta == 0)
    {
      document.LoadInserisciAvvocato.associa.value = "";
    }else
    {
      document.LoadInserisciAvvocato.associa.value = "Associa";
      document.LoadInserisciAvvocato.Action.value="siap.siep.avvocato.action.ActInserisciDifensore";
    }
  }
      
  function ListaComuni(a_formname,a_fieldname)
  {
    desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  }

  function Verify()
  {
    if (document.LoadInserisciAvvocato.<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA%>.value.length==1)
      document.LoadInserisciAvvocato.<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA%>.value='0'+document.LoadInserisciAvvocato.<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA%>.value;
    if (document.LoadInserisciAvvocato.<%=ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA%>.value.length==1)
      document.LoadInserisciAvvocato.<%=ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA%>.value='0'+document.LoadInserisciAvvocato.<%=ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA%>.value;

    var data_to_verify=document.LoadInserisciAvvocato.<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA%>.value+'/'+document.LoadInserisciAvvocato.<%=ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA%>.value+'/'+document.LoadInserisciAvvocato.<%=ICostantiAvvocato.CAMPO_ANNO_DATA_NASCITA%>.value;
    if (! ControllaDataPassaVuota(data_to_verify))
    {
      alert('Data di nascita non valida');
      return false;
    }
    return true;
  }

  function calendario(a_formname,a_field_year,a_field_month,a_field_day)
  {
    desktop = 
        window.open("<%=IWebConstants.ROOT_DIR%>" + "files/siap/sico/Calendario.jsp?formname="+a_formname+"&fieldyear="+a_field_year+"&fieldmonth="+a_field_month+"&fieldday="+a_field_day, "Calendario","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=250");
  }
</script>

</head>


    <body class="corpo">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class=LBG><font class="label">Funzione :</font>&nbsp;&nbsp;
 <%
       AvvocatoModel lAvvocato = null;
       String lAction = new String();
       if( modalita.equals("I") )
       {
         lAction = "siap.siep.avvocato.action.ActInserisciDifensore";
         lAvvocato=new AvvocatoModel();
%>

      <font class="campo">Inserimento Difensore</font>
       <%

         }

         %>
</td>
</tr>
</table>

<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciAvvocato">
  <input type="hidden" name="associa" value="">

  <table cellspacing=2 cellpadding=2>
    <tr>
        <td class="l">Cognome <font class=ob>(*)</font></td>
        <td class="l"><input  size=35 maxlength=35 title="Campo Cognome" type="text" name="<%= ICostantiAvvocato.CAMPO_COGNOME %>"  ></td>
    </tr>
    <tr>
        <td class="l">Nome <font class=ob>(*)</font></td>
        <td class="l"><input size=35 maxlength=35  title="Campo Nome" type="text" name="<%= ICostantiAvvocato.CAMPO_NOME %>"  ></td>
    </tr>
    <tr>
      <td class="l">Luogo di  Nascita </td>
      <td class="L">
        <input title="Comune di Nascita"  value="" type="text" name="<%= ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA %>"  maxlength="35" size="35">
        <a href="Javascript:ListaComuni('LoadInserisciAvvocato','<%= ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA %>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
    </tr>

    <tr>
      <td class="l">Data di nascita </td>
        <td class="L">

          <input type="text" title="Giorno Data di nascita" name="<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA%>" value="" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
          -
          <input type="text" title="Mese Data di nascita" name="<%= ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA %>" value="" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
          -
          <input type="text" title="Anno Data di nascita" name="<%= ICostantiAvvocato.CAMPO_ANNO_DATA_NASCITA %>" value="" maxlength="4" size="4"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
<%-- MERGE v10 COLLAUDO: risolto errore di merge --%>
<%
	  // MEV 15 - Revisione SIGE
	  // Il calendario viene visualizzato solo quando la maschera viene richiamata da SIGE
	  // Funzione: Inserimento Difensore
	  if( codFunzione != null && 
	      (codFunzione.equals(ICostantiFascicoloSius.COD_FUNZIONE_90010000) || codFunzione.equals(ICostantiFascicoloSius.COD_FUNZIONE_90020000) || 
	       codFunzione.equals(ICostantiFascicoloSius.COD_FUNZIONE_90040000) || codFunzione.equals(ICostantiFascicoloSius.COD_FUNZIONE_90050000) ||
	       codFunzione.equals(ICostantiFascicoloSius.COD_FUNZIONE_90110000) )
	    ){
%>
			<a href="javascript:calendario('LoadInserisciAvvocato','<%=ICostantiAvvocato.CAMPO_ANNO_DATA_NASCITA%>','<%=ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA%>','<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA%>');">
       			<img src="/images/calendario.gif" border=0>
        	</a>
<%		  
	  }
%>
       </td>
</tr>

		<tr>
				<td class="l">Foro <font class=ob>(*)</font></td>
				<td class="l">
				<select name="<%=ICostantiAvvocato.CAMPO_FORO%>" size="1" >
        	 <%=foro%>
        </select>
        </td>
    </tr>
    <tr>
        <td class="l">Indirizzo</td>
        <td class="l"><input size=80 maxlength=200  title="Indirizzo" type="text" name="<%= ICostantiAvvocato.CAMPO_INDIRIZZO %>"  ></td>
    </tr>
    <tr>
      <td class="l">Con Studio in </td>
      <td class="L">
		<%-- 20210610 MEV_21 --%>
<%--    <input title="Comune di Residenza"  value="" type="text" name="<%= ICostantiAvvocato.CAMPO_COD_COMUNE_RESIDENZA %>"  maxlength="35" size="35">
        <a href="Javascript:ListaComuni('LoadInserisciAvvocato','<%= ICostantiAvvocato.CAMPO_COD_COMUNE_RESIDENZA %>');">  --%> 
		<input title="Comune Sede dello Studio" value="" type="text" name="<%=ICostantiAvvocato.CAMPO_DESC_COMUNE_STUDIO%>" maxlength="35" size="35">
        <a href="Javascript:ListaComuni('LoadInserisciAvvocato','<%= ICostantiAvvocato.CAMPO_DESC_COMUNE_STUDIO %>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
    </tr>
    <tr>
        <td class="l">Telefono</td>
        <td class="l"><input size=12 maxlength=12  title="Telefono" type="text" name="<%= ICostantiAvvocato.CAMPO_TELEFONO %>"  ></td>
    </tr>
    <tr>
        <td class="l">Fax</td>
        <td class="l"><input size=12 maxlength=12  title="Fax" type="text" name="<%= ICostantiAvvocato.CAMPO_FAX %>"  ></td>
    </tr>
    <tr>
        <td class="l">e-mail</td>
        <td class="l"><input size=50 maxlength=50 value="<%=lAvvocato.getEMail() %>" title="e-mail" type="text" name="<%= ICostantiAvvocato.CAMPO_E_MAIL %>"  ></td>
    </tr>
    <tr>
        <td class="l">Codice Fiscale</td>
        <td class="l"><input size=20 maxlength=16 value="<%=lAvvocato.getCodiceFiscale() %>" title="CodiceFiscale" type="text" name="<%= ICostantiAvvocato.CAMPO_CODICE_FISCALE %>"  ></td>
    </tr>
    <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
    <%--tr>
		<td class="l">Sospeso fino al</td>
          <td class="L">
           	<input type="text" title="Giorno Data sospensione" name="<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_SOSPENSIONE%>" maxlength="2" size="2">
            -
            <input type="text" title="Mese Data sospensione" name="<%= ICostantiAvvocato.CAMPO_MESE_DATA_SOSPENSIONE %>" maxlength="2" size="2">
            -
            <input type="text" title="Anno Data sospensione" name="<%= ICostantiAvvocato.CAMPO_ANNO_DATA_SOSPENSIONE %>" maxlength="4" size="4">
		</td>
	</tr>
	<tr>
		<td class="l">Radiato dal</td>
		<td class="L">
	    	<input type="text" title="Giorno Data radiazione" name="<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_RADIAZIONE%>" maxlength="2" size="2">
	        -
	        <input type="text" title="Mese Data radiazione" name="<%= ICostantiAvvocato.CAMPO_MESE_DATA_RADIAZIONE %>" maxlength="2" size="2">
	        -
	        <input type="text" title="Anno Data radiazione" name="<%= ICostantiAvvocato.CAMPO_ANNO_DATA_RADIAZIONE %>" maxlength="4" size="4">
		</td>
	</tr>
	<tr>
		<td class="l">Non in attività per</td>
		<td class="L">
			<select title="attività" name="<-%=ICostantiAvvocato.CAMPO_COD_NON_ATTIVITA%>">
				<-%=autoritaAvvocato%>
	       	</select>
	    </td>
	</tr--%>
<tr><td>&nbsp;</td></tr>

    <tr>
      <td colspan=2>
        <input type="Hidden" name="Action" value="<%=lAction%>">
        <input class=bottone  type="submit" value="Conferma" onclick="javascript:Inserisci(0);">
     
<%
  ProfileModel lProfilo =(ProfileModel) UtenteConnesso.getUserProfile();

  // MAC/MEV 29 07/2015 il tasto 'Associa' va visualizzato solo se presente un fascicolo in 
  // sessione, altrimenti va in errore la action
  if (lProfilo.isSiep() && request.getSession().getAttribute("fascicolo")!=null )
  {%> 
     <input class="bottone"  type="submit" value="Associa" name="IN" onclick="javascript:Inserisci(1);">
  <%} else if (lProfilo.isSige() && request.getSession().getAttribute("FascicoloSigeEsteso")!=null ) { %> 
     <input class="bottone"  type="submit" value="Associa" name="IN" onclick="javascript:Inserisci(2);">
  <%} else if (lProfilo.isSius() && request.getSession().getAttribute("fascicoloSiusGP")!=null) {  %> 
     <input class="bottone"  type="submit" value="Associa" name="IN" onclick="javascript:Inserisci(3);">
  <%
  }
%>
      </td>
    </tr>
  </table>
</form>
<script language="JavaScript" type="text/javascript">
var frmvalidator  = new Validator("LoadInserisciAvvocato");
frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_COGNOME %>","req","Il campo Cognome Avvocato è obbligatorio");
// frmvalidator.addValidation("<-%= ICostantiAvvocato.CAMPO_COGNOME %->","alpha");
frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_NOME %>","req","Il campo Nome Avvocato è obbligatorio");
frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_NOME %>","alpha");

frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_FORO %>","req","Il campo Foro  è obbligatorio");
<%-- frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_FORO %>","alpha"); --%>


frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_ANNO_DATA_NASCITA%>","maxlen=4","La lunghezza massima per l'anno di nascita è di 4 caratteri");
frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_ANNO_DATA_NASCITA%>","minlen=4","La lunghezza minima per l'anno di nascita è di 4 caratteri");
frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_ANNO_DATA_NASCITA%>","numeric");
frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_ANNO_DATA_NASCITA%>","gt=1900");
frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_ANNO_DATA_NASCITA%>","lt=3000");

<%--
frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_ANNO_DATA_SOSPENSIONE%>","maxlen=4","La lunghezza massima per l'anno di sospensione è di 4 caratteri");
frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_ANNO_DATA_SOSPENSIONE%>","minlen=4","La lunghezza minima per l'anno di sospensione è di 4 caratteri");
frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_ANNO_DATA_SOSPENSIONE%>","numeric");
frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_ANNO_DATA_SOSPENSIONE%>","gt=1900");
frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_ANNO_DATA_SOSPENSIONE%>","lt=3000");

frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_ANNO_DATA_RADIAZIONE%>","maxlen=4","La lunghezza massima per l'anno di radiazione è di 4 caratteri");
frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_ANNO_DATA_RADIAZIONE%>","minlen=4","La lunghezza minima per l'anno di radiazione è di 4 caratteri");
frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_ANNO_DATA_RADIAZIONE%>","numeric");
frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_ANNO_DATA_RADIAZIONE%>","gt=1900");
frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_ANNO_DATA_RADIAZIONE%>","lt=3000");
--%>

frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA%>","numeric");
frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA%>","gt=1");
frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA%>","lt=31");

frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA%>","numeric");
frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA%>","gt=1");
frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA%>","lt=12");

<%--
frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_GIORNO_DATA_SOSPENSIONE%>","numeric");
frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_GIORNO_DATA_SOSPENSIONE%>","gt=1");
frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_GIORNO_DATA_SOSPENSIONE%>","lt=31");

frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_MESE_DATA_SOSPENSIONE%>","numeric");
frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_MESE_DATA_SOSPENSIONE%>","gt=1");
frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_MESE_DATA_SOSPENSIONE%>","lt=12");

frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_GIORNO_DATA_RADIAZIONE%>","numeric");
frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_GIORNO_DATA_RADIAZIONE%>","gt=1");
frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_GIORNO_DATA_RADIAZIONE%>","lt=31");

frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_MESE_DATA_RADIAZIONE%>","numeric");
frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_MESE_DATA_RADIAZIONE%>","gt=1");
frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_MESE_DATA_RADIAZIONE%>","lt=12");
--%>

<%-- frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA%>","alphabetic"); --%>
frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_FORO%>","alphabetic");
<%-- 20210610 MEV_21 --%>
<%-- frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_COD_COMUNE_RESIDENZA%>","alphabetic"); --%>
frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_DESC_COMUNE_STUDIO%>","alphabetic");

frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
</html>