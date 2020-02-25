<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.siep.jms.action.ICostantiSiepJMS" %>
<%@ page import="siap.sige.sentenza.action.ICostantiFasSigeSentenza" %>

<jsp:useBean id="autoritaEsterna" scope="request" class="java.lang.String"/>

<head>
  <title> [S.I.E.S.] - Ricerca Sentenza - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript">
	  function calendario(a_formname,a_field_year,a_field_month,a_field_day)
	  {
	    desktop = 
	        window.open("<%=IWebConstants.ROOT_DIR%>" + "files/siap/sico/Calendario.jsp?formname="+a_formname+"&fieldyear="+a_field_year+"&fieldmonth="+a_field_month+"&fieldday="+a_field_day, "Calendario","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=250");
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

      if((data_inizio.length!=2 && data_fine.length!=2) && !CompareDate(data_inizio,data_fine))
      {
        alert('La Data di fine non può essere inferiore alla data di inizio');
        return false;
      }
			/************************************************************************/
 			if (document.f.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value.length==1)
        document.f.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value='0'+document.f.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value;
      if (document.f.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value.length==1)
  			document.f.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value='0'+document.f.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value;

			var data_provv=document.f.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value+'/'+document.f.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value+'/'+document.f.<%=ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>.value;

      if(!ControllaDataPassaVuota(data_provv))
      {
        alert('Data del Titolo Esecutivo non valida');
        return false;
      }
     return true;
    }

	function pulisciDateIntervalloProvv()
	{
     document.f.<%=ICostantiSentenza.CAMPO_DA_GIORNO_PROVVEDIMENTO%>.value='';
     document.f.<%=ICostantiSentenza.CAMPO_DA_MESE_PROVVEDIMENTO%>.value='';
     document.f.<%=ICostantiSentenza.CAMPO_DA_ANNO_PROVVEDIMENTO%>.value='';
     document.f.<%=ICostantiSentenza.CAMPO_A_GIORNO_PROVVEDIMENTO%>.value='';
     document.f.<%=ICostantiSentenza.CAMPO_A_MESE_PROVVEDIMENTO%>.value='';
     document.f.<%=ICostantiSentenza.CAMPO_A_ANNO_PROVVEDIMENTO%>.value='';
	}

	function pulisciDateProvv()
 	{
    document.f.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value='';
    document.f.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value='';
    document.f.<%=ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>.value='';
	}



	function VerifyChiamate(id)
	{
	if(id==1)
	{
		/* setta il parametro per il tipo di ricerca */
		document.f.<%=ICostantiSentenza.PARAMETRO_ORDINAMENTO%>.value='default';
		/* #### */
		document.f.<%=ICostantiSentenza.CAMPO_ANNO_REGE_PM%>.value='';
		document.f.<%=ICostantiSentenza.CAMPO_NUMERO_REGE_PM%>.value='';
		document.f.<%=ICostantiSentenza.CAMPO_ANNO_REGISTRO_GENERALE%>.value='';
		document.f.<%=ICostantiSentenza.CAMPO_NUMERO_REGISTRO_GENERALE%>.value='';
		pulisciDateProvv();
		pulisciDateIntervalloProvv();
  }
   if(id==2)
  {
		/* setta il parametro per il tipo di ricerca */
		document.f.<%=ICostantiSentenza.PARAMETRO_ORDINAMENTO%>.value='default';
		/* #### */
		document.f.<%=ICostantiSentenza.CAMPO_ANNO_SENTENZA%>.value='';
		document.f.<%=ICostantiSentenza.CAMPO_NUMERO_SENTENZA%>.value='';
		document.f.<%=ICostantiSentenza.CAMPO_ANNO_REGISTRO_GENERALE%>.value='';
		document.f.<%=ICostantiSentenza.CAMPO_NUMERO_REGISTRO_GENERALE%>.value='';
		pulisciDateProvv();
		
		pulisciDateIntervalloProvv();
	}
	if(id==3)
	{
		/* setta il parametro per il tipo di ricerca */
		document.f.<%=ICostantiSentenza.PARAMETRO_ORDINAMENTO%>.value='default';
		document.f.<%=ICostantiSentenza.CAMPO_ANNO_SENTENZA%>.value='';
		document.f.<%=ICostantiSentenza.CAMPO_NUMERO_SENTENZA%>.value='';
		document.f.<%=ICostantiSentenza.CAMPO_ANNO_REGE_PM%>.value='';
		document.f.<%=ICostantiSentenza.CAMPO_NUMERO_REGE_PM%>.value='';
		pulisciDateProvv();
		pulisciDateIntervalloProvv();
  }
  if(id==4)
  {
		/* setta il parametro per il tipo di ricerca */
		document.f.<%=ICostantiSentenza.PARAMETRO_ORDINAMENTO%>.value='default';
		/* #### */
		document.f.<%=ICostantiSentenza.CAMPO_ANNO_SENTENZA%>.value='';
		document.f.<%=ICostantiSentenza.CAMPO_NUMERO_SENTENZA%>.value='';
		document.f.<%=ICostantiSentenza.CAMPO_ANNO_REGE_PM%>.value='';
		document.f.<%=ICostantiSentenza.CAMPO_NUMERO_REGE_PM%>.value='';
		document.f.<%=ICostantiSentenza.CAMPO_ANNO_REGISTRO_GENERALE%>.value='';
		document.f.<%=ICostantiSentenza.CAMPO_NUMERO_REGISTRO_GENERALE%>.value='';
		pulisciDateIntervalloProvv();
  }
	if(id==5)
  {
		/* setta il parametro per il tipo di ricerca */
		document.f.<%=ICostantiSentenza.PARAMETRO_ORDINAMENTO%>.value='default';
		/* #### */
		document.f.<%=ICostantiSentenza.CAMPO_ANNO_SENTENZA%>.value='';
		document.f.<%=ICostantiSentenza.CAMPO_NUMERO_SENTENZA%>.value='';
		document.f.<%=ICostantiSentenza.CAMPO_ANNO_REGE_PM%>.value='';
		document.f.<%=ICostantiSentenza.CAMPO_NUMERO_REGE_PM%>.value='';
		document.f.<%=ICostantiSentenza.CAMPO_ANNO_REGISTRO_GENERALE%>.value='';
		document.f.<%=ICostantiSentenza.CAMPO_NUMERO_REGISTRO_GENERALE%>.value='';
		pulisciDateProvv();

  }
	return Verify();
}
  </script>
</head>

<body class="corpo" >
  <FORM method="POST" name="f" action="<%= IWebConstants.PG_MAIN%>">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sige.sentenza.action.ActRicercaSentenzaPerSige">
  <%-- #### parametro per ricerca orderBy nel Db   --%>
   <input type="HIDDEN" name="<%=ICostantiSentenza.PARAMETRO_ORDINAMENTO%>" value="">
  <%-- #### --%>

<table width="52%">
   <tr>
       <td class="titolo" width="30%" > ambito di ricerca: </td>
   </tr>
   <tr>
     <td class="L">
       Solo Ufficio <input type="radio" name="<%=ICostantiFasSigeSentenza.CAMPO_AMBITO_RICERCA%>" value="U"  checked>&nbsp;&nbsp;&nbsp;&nbsp; 
			 Intero Distretto <input type="radio" name="<%=ICostantiFasSigeSentenza.CAMPO_AMBITO_RICERCA%>" value="D" >&nbsp;
		 </td>
   </tr>
	<tr><td>&nbsp;</td></tr>
 </table>

<table >
	<tr><td class="Titolo" colspan=2>Anno/Numero Sentenza</td></tr>

    <tr>
      <td class="L" > Anno/Numero </td>
      <td class="L">
        <input type="text" title="Anno Titolo Esecutivo" name="<%=ICostantiSentenza.CAMPO_ANNO_SENTENZA%>" maxlength="4" size="4" onkeypress="return TicTabNumField(this,event)">
        /
        <input type="text" title="Numero Titolo Esecutivo" name="<%=ICostantiSentenza.CAMPO_NUMERO_SENTENZA%>" maxlength="6" size="6" onkeypress="return TicTabNumField(this,event)">
      </td>

     <td class="l" >
        <input class="bottone" type="submit" name="RICERCA" value="Ricerca" onClick="javascript:return VerifyChiamate(1);">
      </td>

 </tr>
<tr><td>&nbsp;</td></tr>

<tr><td class="Titolo"  colspan ="2" >Anno/Numero R.G.N.R.</td></tr>
<tr>
<td class="L"> Anno/Numero</td>
      <td class="L">
        <input type="text" title="Anno R.G.N.R." name="<%=ICostantiSentenza.CAMPO_ANNO_REGE_PM%>" maxlength="4" size="4" onkeypress="return TicTabNumField(this,event)">
        /
        <input type="text" title="Numero R.G.N.R." name="<%=ICostantiSentenza.CAMPO_NUMERO_REGE_PM%>" maxlength="6" size="6" onkeypress="return TicTabNumField(this,event)">
      </td>

 <td class="l" >
        <input class="bottone" type="submit" name="RICERCA" value="Ricerca" onClick="javascript:return VerifyChiamate(2);">
      </td>
</tr>
<tr><td>&nbsp;</td></tr>

<tr><td class="Titolo"  colspan ="2" >Anno/Numero Reg. Gen.</td><td class="Titolo"  colspan ="1" >Tipo Registro</td></TR>
<tr>
<td class="L"> Anno/Numero </td>
      <td class="L">
        <input type="text" title="Anno Reg. Gen." name="<%=ICostantiSentenza.CAMPO_ANNO_REGISTRO_GENERALE%>" maxlength="4" size="4" onkeypress="return TicTabNumField(this,event)">
        /
        <input type="text" title="Numero Reg. Gen." name="<%=ICostantiSentenza.CAMPO_NUMERO_REGISTRO_GENERALE%>" maxlength="6" size="6" onkeypress="return TicTabNumField(this,event)">
      </td>
      <td class="c" >
         <select  Title="tipo" name="valore">
             <option value="gip">GIP</option>
             <option value="dib">DIB</option>
             <option value="cas">CAS</option>
             <option value="cap">CAP</option>
             <option value="casap">CASAP</option>
             <!-- MEV_66: aggiunte quattro nuove proprietà -->
             <option value="gup">GUP</option>
             <option value="gup">CAPSM</option>
        </select>
     </td>
      <td class="l" >
        <input class="bottone" type="submit" name="RICERCA" value="Ricerca" onClick="javascript:return VerifyChiamate(3);">
      </td>
    </tr>
  </table>
<br>
<table >
    <tr><td class="Titolo" colspan ="2" >Data Sentenza</td>
    </tr>
    <tr>
      <td class="L" > Data &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </td>
      <td class="L" >
        <input type="text" title="Giorno Data Titolo Esecutivo" name="<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input type="text" title="Mese Data Titolo Esecutivo" name="<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input type="text" title="Anno Data Titolo Esecutivo" name="<%=ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">

		<!-- MEV 15 - Revisione SIGE -->
		<a href="javascript:calendario('f','<%=ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>','<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>','<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>');">
      			<img src="/images/calendario.gif" border=0>
       	</a>
      </td>

     <td class="l" >
        <input class="bottone" type="submit" name="RICERCA" value="Ricerca" onClick="javascript:return VerifyChiamate(4);">
      </td>
   </tr>
</table >
<table >

<tr><td>&nbsp;</td></tr>
        <tr> <td class="Titolo"  colspan ="4" >Intervallo Date Sentenze</td></tr>
<tr>
       <td class="L" width="20%" >
        <font class="label">
          Data Iniziale
        </font>
      </td>
      <td class="l" >
        <input type="text" title="Giorno Data Titolo Esecutivo" name="<%=ICostantiSentenza.CAMPO_DA_GIORNO_PROVVEDIMENTO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input type="text" title="Mese Data Titolo Esecutivo" name="<%=ICostantiSentenza.CAMPO_DA_MESE_PROVVEDIMENTO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input type="text" title="Anno Data Titolo Esecutivo" name="<%=ICostantiSentenza.CAMPO_DA_ANNO_PROVVEDIMENTO%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">

		<!-- MEV 15 - Revisione SIGE -->
		<a href="javascript:calendario('f','<%=ICostantiSentenza.CAMPO_DA_ANNO_PROVVEDIMENTO%>','<%=ICostantiSentenza.CAMPO_DA_MESE_PROVVEDIMENTO%>','<%=ICostantiSentenza.CAMPO_DA_GIORNO_PROVVEDIMENTO%>');">
      		<img src="/images/calendario.gif" border=0>
       	</a>
	</td>
 <td class="L" >
        <font class="label">
          Data Finale
        </font>
      </td>
      <td class="l">
        <input type="text" title="Giorno Data Titolo Esecutivo" name="<%=ICostantiSentenza.CAMPO_A_GIORNO_PROVVEDIMENTO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input type="text" title="Mese Data Titolo Esecutivo" name="<%=ICostantiSentenza.CAMPO_A_MESE_PROVVEDIMENTO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input type="text" title="Anno Data Titolo Esecutivo" name="<%=ICostantiSentenza.CAMPO_A_ANNO_PROVVEDIMENTO%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">

		<!-- MEV 15 - Revisione SIGE -->
		<a href="javascript:calendario('f','<%=ICostantiSentenza.CAMPO_A_ANNO_PROVVEDIMENTO%>','<%=ICostantiSentenza.CAMPO_A_MESE_PROVVEDIMENTO%>','<%=ICostantiSentenza.CAMPO_A_GIORNO_PROVVEDIMENTO%>');">
      		<img src="/images/calendario.gif" border=0>
       	</a>
      </td>

      <td class="l" >
        <input class="bottone" type="submit" name="RICERCA" value="Ricerca" onClick="javascript:return VerifyChiamate(5);">
      </td>
    </tr>
  </table>
</form>

<script language="JavaScript" type="text/javascript">

 var frmvalidator  = new Validator("f");

  frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_NUMERO_SENTENZA%>","maxlen=6","La lunghezza massima per il Numero Titolo Esecutivo è di 6 caratteri");
  frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_NUMERO_SENTENZA%>","numeric");

  frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_NUMERO_REGISTRO_GENERALE%>","maxlen=6","La lunghezza massima per il Numero Reg. Gen. è di 6 caratteri");
  frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_NUMERO_REGISTRO_GENERALE%>","numeric");

 frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_NUMERO_REGE_PM%>","maxlen=6","La lunghezza massima per il Numero R.G.N.R. è di 6 caratteri");
  frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_NUMERO_REGE_PM%>","numeric");

  frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_ANNO_SENTENZA%>","maxlen=4","La lunghezza massima per l'Anno Procedimento è di 4 caratteri");
  frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_ANNO_SENTENZA%>","minlen=4","La lunghezza minima per l'Anno Procedimento è di 4 caratteri");
  frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_ANNO_SENTENZA%>","numeric");

 frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_ANNO_REGISTRO_GENERALE%>","maxlen=4","La lunghezza massima per l'Anno Reg. Gen. è di 4 caratteri");
  frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_ANNO_REGISTRO_GENERALE%>","minlen=4","La lunghezza minima per l'Anno Reg. Gen. è di 4 caratteri");
  frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_ANNO_REGISTRO_GENERALE%>","numeric");

 frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_ANNO_REGE_PM%>","maxlen=4","La lunghezza massima per l'Anno R.G.N.R. è di 4 caratteri");
  frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_ANNO_REGE_PM%>","minlen=4","La lunghezza minima per l'Anno R.G.N.R. è di 4 caratteri");
  frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_ANNO_REGE_PM%>","numeric");

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
/**********************************************/

  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>","maxlen=2","La lunghezza massima per il giorno  è di 2 caratteri");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>","lt=31");

  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>","maxlen=2","La lunghezza massima per il mese  è di 2 caratteri");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>","lt=12");

  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>","maxlen=4","La lunghezza massima per l'anno  è di 4 caratteri");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>","lt=3000");

//}

  </script>
</body>
</html>