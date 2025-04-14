<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.siep.jms.action.ICostantiSiepJMS"%>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto"%>
<%@ page import="siap.sico.decodifiche.action.ICostantiComune"%>


<jsp:useBean id="autoritaEsterna" scope="request"
	class="java.lang.String" />
<jsp:useBean id="titolo" scope="request" class="java.lang.String" />

<%

//Controllo se il Titolo è stato passato nella request 
	String lTitolo = "Ricerca Titolo Esecutivo per Soggetto e Data";
	if (titolo != null && titolo.length() > 0)
	{
		lTitolo = titolo;
	}
%>

<head>
<title>[S.I.E.S.] - Ricerca Titolo Esecutivo per Soggetto e Data
	-</title>
<link rel="STYLESHEET" type="text/css"
	href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript">

	function radio()
   	{
  	document.f.<%=IWebConstants.ACTION_FIELD%>.value = "siap.siep.sentenza.action.ActRicercaSentenzaSoggetto";
	}

	function Verify()
	{
      if (document.f.<%=ICostantiSentenza.CAMPO_DA_GIORNO_PROVVEDIMENTO%>.value.length==1)
        document.f.<%=ICostantiSentenza.CAMPO_DA_GIORNO_PROVVEDIMENTO%>.value='0'+document.f.<%=ICostantiSentenza.CAMPO_DA_GIORNO_PROVVEDIMENTO%>.value;
      if (document.f.<%=ICostantiSentenza.CAMPO_DA_MESE_PROVVEDIMENTO%>.value.length==1)
  			document.f.<%=ICostantiSentenza.CAMPO_DA_MESE_PROVVEDIMENTO%>.value='0'+document.f.<%=ICostantiSentenza.CAMPO_DA_MESE_PROVVEDIMENTO%>.value;

      if (document.f.<%=ICostantiSentenza.CAMPO_A_GIORNO_PROVVEDIMENTO%>.value.length==1)
        document.f.<%=ICostantiSentenza.CAMPO_A_GIORNO_PROVVEDIMENTO%>.value='0'+document.f.<%=ICostantiSentenza.CAMPO_A_GIORNO_PROVVEDIMENTO%>.value;
      if (document.f.<%=ICostantiSentenza.CAMPO_A_MESE_PROVVEDIMENTO%>.value.length==1)
  			document.f.<%=ICostantiSentenza.CAMPO_A_MESE_PROVVEDIMENTO%>.value='0'+document.f.<%=ICostantiSentenza.CAMPO_A_MESE_PROVVEDIMENTO%>.value;

			var data_inizio=document.f.<%=ICostantiSentenza.CAMPO_DA_GIORNO_PROVVEDIMENTO%>.value+'/'+document.f.<%=ICostantiSentenza.CAMPO_DA_MESE_PROVVEDIMENTO%>.value+'/'+document.f.<%=ICostantiSentenza.CAMPO_DA_ANNO_PROVVEDIMENTO%>.value;
			var data_fine=document.f.<%=ICostantiSentenza.CAMPO_A_GIORNO_PROVVEDIMENTO%>.value+'/'+document.f.<%=ICostantiSentenza.CAMPO_A_MESE_PROVVEDIMENTO%>.value+'/'+document.f.<%=ICostantiSentenza.CAMPO_A_ANNO_PROVVEDIMENTO%>.value;

      if(!ControllaDataPassaVuota(data_inizio))
      {
        alert('Data di inizio non valida');
        return false;
      }
      if(!ControllaDataPassaVuota(data_fine))
      {
        alert('Data di fine non valida');
        return false;
      }
    //  if(data_inizio.length==2 || data_fine.length==2)
      // return true;

      if((data_inizio.length!=2 && data_fine.length!=2) && !CompareDate(data_inizio,data_fine))
      {
        alert('La Data di fine non può essere inferiore alla data di inizio');
        return false;
      }
/************************************************************************/
function pulisciDateIntervalloProvv()
 {
         document.f.<%=ICostantiSentenza.CAMPO_DA_GIORNO_PROVVEDIMENTO%>.value='';
         document.f.<%=ICostantiSentenza.CAMPO_DA_MESE_PROVVEDIMENTO%>.value='';
         document.f.<%=ICostantiSentenza.CAMPO_DA_ANNO_PROVVEDIMENTO%>.value='';
         document.f.<%=ICostantiSentenza.CAMPO_A_GIORNO_PROVVEDIMENTO%>.value='';
         document.f.<%=ICostantiSentenza.CAMPO_A_MESE_PROVVEDIMENTO%>.value='';
         document.f.<%=ICostantiSentenza.CAMPO_A_ANNO_PROVVEDIMENTO%>.value='';

 }

}
function VerifyAltreBDI(i)
	  {
     if (i==1)
     {

       document.f.<%=IWebConstants.ACTION_FIELD%>.value = "siap.siep.jms.action.ActRicercaEstesaSentenza";
       //document.RicercaEstesaFascicolo.submit();
     }
      if (i==2)
     {
       document.f.<%=IWebConstants.ACTION_FIELD%>.value = "siap.siep.jms.action.ActRicercaEstesaSentenzaPerTrasferimento";
       //document.RicercaEstesaFascicolo.submit();
     }
    }
function ListaComuni(a_formname,a_fieldname)
    {
     desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }

	<!-- 20210524	MEV Scheda-21 -->
    function ListaComuniNascita(a_formname,a_fieldname)
    {
      desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComuneNascita&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=400,height=500");
    }      
    function cancellaCodComuneReale() {
      	document.f.<%=ICostantiComune.CAMPO_COD_COMUNE_REALE%>.value = "";      	
    }

  </script>
</head>

<body class="corpo" onload="radio();">
	<FORM method="POST" name="f" action="<%= IWebConstants.PG_MAIN%>">
		<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="">
		<%-- #### parametro per ricerca orderBy nel Db   --%>
		<input type="HIDDEN" name="<%=ICostantiSentenza.PARAMETRO_ORDINAMENTO%>" value="">
		<input type="HIDDEN" name="<%=ICostantiComune.CAMPO_COD_COMUNE_REALE%>" value="">		
		<%-- #### --%>
		<table>
			<tr>
				<td class="LBG"><a href="Javascript:window.print();"><img
						align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif"
						alt="Stampa questa videata" border=0></a></td>
				<td class="LBG"><font class="label">Funzione :</font>&nbsp;<font
					class="campo"><%=lTitolo%></font></td>
			</tr>
		</table>
		<br> <br>
		<table>
			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td class="Titolo" colspan="4">Intervallo Date Titolo
					Esecutivo</td>
			</tr>
			<tr>
				<td class="L" width="20%"><font class="label">Data
						Iniziale</font></td>
				<td class="l"><input type="text"
					title="Giorno Data Titolo Esecutivo"
					name="<%=ICostantiSentenza.CAMPO_DA_GIORNO_PROVVEDIMENTO%>"
					maxlength="2" size="2" onFocus="javascript:textboxSelect(this)"
					onkeypress="return TicTabNumField(this,event)"
					onBlur="javascript:value=FillDM(value)"> - <input
					type="text" title="Mese Data Titolo Esecutivo"
					name="<%=ICostantiSentenza.CAMPO_DA_MESE_PROVVEDIMENTO%>"
					maxlength="2" size="2" onFocus="javascript:textboxSelect(this)"
					onkeypress="return TicTabNumField(this,event)"
					onBlur="javascript:value=FillDM(value)"> - <input
					type="text" title="Anno Data Titolo Esecutivo"
					name="<%=ICostantiSentenza.CAMPO_DA_ANNO_PROVVEDIMENTO%>"
					maxlength="4" size="4" onFocus="javascript:textboxSelect(this)"
					onkeypress="return TicTabNumField(this,event)"
					onBlur="javascript:value=FillYear(value)"></td>
				<td class="L"><font class="label">Data Finale</font></td>
				<td class="l"><input type="text"
					title="Giorno Data Titolo Esecutivo"
					name="<%=ICostantiSentenza.CAMPO_A_GIORNO_PROVVEDIMENTO%>"
					maxlength="2" size="2" onFocus="javascript:textboxSelect(this)"
					onkeypress="return TicTabNumField(this,event)"
					onBlur="javascript:value=FillDM(value)"> - <input
					type="text" title="Mese Data Titolo Esecutivo"
					name="<%=ICostantiSentenza.CAMPO_A_MESE_PROVVEDIMENTO%>"
					maxlength="2" size="2" onFocus="javascript:textboxSelect(this)"
					onkeypress="return TicTabNumField(this,event)"
					onBlur="javascript:value=FillDM(value)"> - <input
					type="text" title="Anno Data Titolo Esecutivo"
					name="<%=ICostantiSentenza.CAMPO_A_ANNO_PROVVEDIMENTO%>"
					maxlength="4" size="4" onFocus="javascript:textboxSelect(this)"
					onkeypress="return TicTabNumField(this,event)"
					onBlur="javascript:value=FillYear(value)"></td>
			</tr>
		</table>
		<table cellspacing=2 cellpadding=2>
			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td class="Titolo" colspan="4">Soggetto</td>
			</tr>
			<tr>
				<td class="l" width="115">Cognome</td>
				<td class="l"><input title="Cognome Soggetto" type="text"
					name="<%=ICostantiSoggetto.CAMPO_COGNOME%>" value="" size="30"
					maxlength="30"></td>
			</tr>
			<tr>
				<td class="l">Nome</td>
				<td class="l"><input title="Nome Soggetto" type="text"
					name="<%=ICostantiSoggetto.CAMPO_NOME%>" value="" size="30"
					maxlength="30"></td>
			</tr>
			<tr>
				<td class="l">Data di nascita</td>
				<td class="l"><input title="Data di nascita" type="text"
					name="<%= ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>"
					maxlength="2" size="2" <%=IWebConstants.UTIL_DATA%>> / <input
					title="Data di nascita" type="text"
					name="<%= ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA %>"
					maxlength="2" size="2" <%=IWebConstants.UTIL_DATA%>> / <input
					title="Data di nascita" type="text"
					name="<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>"
					maxlength="4" size="4" <%=IWebConstants.UTIL_DATA_ANNO%>></td>
			</tr>
			<tr>
				<td class="l">Comune di nascita</td>
				<td class="l"><input title="Comune di Nascita" value=""
					type="text" name="<%=ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA%>"
					maxlength="30" size="30" onChange="cancellaCodComuneReale();">
					<a
					href="Javascript:ListaComuniNascita('f','<%= ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA %>');">
						<img src="/images/filefolder.gif" border=0>
				</a></td>
			</tr>
		</table>
		<table cellspacing=2 cellpadding=2>
			<tr>
				<td colspan="2">&nbsp;</td>
			</tr>
			<tr>
				<td colspan="2"><INPUT onclick="Javascript:return Verify();"
					class="bottone" type="submit" name="RICERCA" value="Ricerca">
				</td>
			</tr>
		</table>

	</form>
	<script language="JavaScript" type="text/javascript">

 var frmvalidator  = new Validator("f");
// if(document.f.tipo[0].checked || document.f.tipo[1].checked  )
//{

  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_DA_GIORNO_PROVVEDIMENTO%>","maxlen=2","La lunghezza massima per il giorno di inizio è di 2 caratteri");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_DA_GIORNO_PROVVEDIMENTO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_DA_GIORNO_PROVVEDIMENTO%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_DA_GIORNO_PROVVEDIMENTO%>","lt=31");

  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_DA_MESE_PROVVEDIMENTO%>","maxlen=2","La lunghezza massima per il mese di inizio è di 2 caratteri");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_DA_MESE_PROVVEDIMENTO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_DA_MESE_PROVVEDIMENTO%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_DA_MESE_PROVVEDIMENTO%>","lt=12");

  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_DA_ANNO_PROVVEDIMENTO%>","maxlen=4","La lunghezza massima per l'anno di inizio è di 4 caratteri");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_DA_ANNO_PROVVEDIMENTO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_DA_ANNO_PROVVEDIMENTO%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_DA_ANNO_PROVVEDIMENTO%>","lt=3000");

  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_A_GIORNO_PROVVEDIMENTO%>","maxlen=2","La lunghezza massima per il giorno di fine è di 2 caratteri");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_A_GIORNO_PROVVEDIMENTO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_A_GIORNO_PROVVEDIMENTO%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_A_GIORNO_PROVVEDIMENTO%>","lt=31");

  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_A_MESE_PROVVEDIMENTO%>","maxlen=2","La lunghezza massima per il mese di fine è di 2 caratteri");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_A_MESE_PROVVEDIMENTO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_A_MESE_PROVVEDIMENTO%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_A_MESE_PROVVEDIMENTO%>","lt=12");

  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_A_ANNO_PROVVEDIMENTO%>","maxlen=4","La lunghezza massima per l'anno di fine è di 4 caratteri");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_A_ANNO_PROVVEDIMENTO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_A_ANNO_PROVVEDIMENTO%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_A_ANNO_PROVVEDIMENTO%>","lt=3000");

  frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_NOME %>","maxlen=35","La lunghezza massima per il nome è di 35 caratteri");
  frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_NOME %>","alpha");

  frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_COGNOME %>","maxlen=35","La lunghezza massima per il cognome è di 35 caratteri");
  frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_COGNOME %>","alpha");
//}

  </script>
</body>

</html>