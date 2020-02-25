<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.siep.jms.action.ICostantiSiepJMS" %>

<jsp:useBean id="autoritaemittente" scope="request" class="java.lang.String"/>
<jsp:useBean id="TornaQui" 		scope="request" class="java.lang.String" />

<html>
<head>
  <title> [S.I.E.S.] - Iscrizione Istanza - Ricerca Titolo Esecutivo - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript">
  function Verify()
  {
		if (document.f.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value.length==1)
        document.f.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value='0'+document.f.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value;
    if (document.f.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value.length==1)
  			document.f.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value='0'+document.f.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value;

	  var data_provv=document.f.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value+'/'+document.f.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value+'/'+document.f.<%=ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>.value;
		var tuttiCampiVuoti=true;

    if(data_provv.length==10 )
			tuttiCampiVuoti=false;

    if(document.f.<%=ICostantiSentenza.CAMPO_ANNO_SENTENZA%>.value.length==4 &&
       document.f.<%=ICostantiSentenza.CAMPO_NUMERO_SENTENZA%>.value.length>0 )
			tuttiCampiVuoti=false;

    if(document.f.<%=ICostantiSentenza.CAMPO_ANNO_SENTENZA%>.value.length==4 &&
       document.f.<%=ICostantiSentenza.CAMPO_NUMERO_SENTENZA%>.value.length>0 )
			tuttiCampiVuoti=false;

    if(document.f.<%=ICostantiSentenza.CAMPO_ANNO_REGE_PM%>.value.length==4 &&
       document.f.<%=ICostantiSentenza.CAMPO_NUMERO_REGE_PM%>.value.length>0 )
			tuttiCampiVuoti=false;

    if(document.f.<%=ICostantiSentenza.CAMPO_ANNO_REGISTRO_GENERALE%>.value.length==4 &&
       document.f.<%=ICostantiSentenza.CAMPO_NUMERO_REGISTRO_GENERALE%>.value.length>0 )
			tuttiCampiVuoti=false;

    if(document.f.<%=ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.value.length>1 &&
       document.f.<%=ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE%>.value.length>0 )
			tuttiCampiVuoti=false;

		if (tuttiCampiVuoti == true)
		{
        alert("Impostare almeno un criterio di ricerca");
        document.f.<%=ICostantiSentenza.CAMPO_ANNO_SENTENZA%>.focus();
        return false;
    }
    return true;
  }
	    


	function ListaComuni(a_formname,a_fieldname)
  {
    desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  }

  </script>
</head>

<body class="corpo">
  <FORM method="POST" name="f" action="<%= IWebConstants.PG_MAIN%>">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.nuovaistanza.action.ActRicercaTitEsecIstanza">
  <input type="HIDDEN" name="<%=IWebConstants.LINK_RITORNO%>" value="<%=TornaQui%>" >

  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;<font class="campo">Iscrizione Istanza - Ricerca Titolo Esecutivo</font>
      </td>
    </tr>
  </table>
<br>

<br>
<table width="100%">
    <tr><td class="Titolo" colspan=4>Dati Del Titolo Esecutivo</td></tr>

    <tr>
      <td class="L" width="25%"> Anno/Numero Titolo Esecutivo</td>
      <td class="L">
        <input type="text" title="Anno Titolo Esecutivo" name="<%=ICostantiSentenza.CAMPO_ANNO_SENTENZA%>" maxlength="4" size="4">
        /
        <input type="text" title="Numero Titolo Esecutivo" name="<%=ICostantiSentenza.CAMPO_NUMERO_SENTENZA%>" maxlength="6" size="6">
      </td>
 </tr>
<tr>
<td class="L"> Anno/Numero R.G.N.R.</td>
      <td class="L">
        <input type="text" title="Anno R.G.N.R." name="<%=ICostantiSentenza.CAMPO_ANNO_REGE_PM%>" maxlength="4" size="4">
        /
        <input type="text" title="Numero R.G.N.R." name="<%=ICostantiSentenza.CAMPO_NUMERO_REGE_PM%>" maxlength="6" size="6">
      </td>
</tr>
<tr>
<td class="L"> Anno/Numero Reg.Gen.</td>
      <td class="L">
        <input type="text" title="Anno Reg. Gen." name="<%=ICostantiSentenza.CAMPO_ANNO_REGISTRO_GENERALE%>" maxlength="4" size="4">
        /
        <input type="text" title="Numero Reg. Gen." name="<%=ICostantiSentenza.CAMPO_NUMERO_REGISTRO_GENERALE%>" maxlength="6" size="6">

         <select  Title="tipo" name="valore">
             <option value="gip">GIP</option>
             <option value="dib">DIB</option>
             <option value="cas">CAS</option>
             <option value="cap">CAP</option>
            <option value="casap">CASAP</option>
        </select>
     </td>
    </tr>
    <tr>
      <td class="L" > Data Emissione </td>
      <td class="L" >
        <input type="text" title="Giorno Data Titolo Esecutivo" name="<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input type="text" title="Mese Data Titolo Esecutivo" name="<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input type="text" title="Anno Data Titolo Esecutivo" name="<%=ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
   </tr>

    <tr>
         <td class="L">Autorità Emittente </td>
          <td class="L">
             <select Title="Autorita Esterna" name="<%=ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>" >
               <%=autoritaemittente%>
             </select>
             </td>
      </tr>
      
      <tr>
       <tr>
       <td class="l">Luogo Emittente</td>
       <td class="L">
       <input title="Sede Autorita Esterna"  type="text" name="<%= ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE %>"  maxlength="35" size="35">
       <a href="Javascript:ListaComuni('f','<%= ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE %>');">
       <img src="/images/filefolder.gif" border=0></a>
       </td>
   </tr>
   
   <tr>
      <td> &nbsp;&nbsp; </td>
   </tr>
     <tr>
      <td>
        <input class="bottone" type="submit" name="RICERCA" value="Avanti >>>">
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

	//frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>","req","Il campo Data Emissione è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>","maxlen=2","La lunghezza massima per il giorno  è di 2 caratteri");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>","lt=31");

	//frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>","req","Il campo Data Emissione è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>","maxlen=2","La lunghezza massima per il mese  è di 2 caratteri");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>","lt=12");

	//frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>","req","Il campo Data Emissione è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>","maxlen=4","La lunghezza massima per l'anno  è di 4 caratteri");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>","lt=3000");

  frmvalidator.setAddnlValidationFunction("Verify");

  </script>
</body>

</html>