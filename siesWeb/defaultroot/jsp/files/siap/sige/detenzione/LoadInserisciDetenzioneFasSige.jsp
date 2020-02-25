<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.decodifiche.model.DecodificheModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione" %>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>
<%@ page import="siap.siep.altracausa.action.ICostantiAltraCausa" %>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel" %>
<%@ page import="siap.sige.detenzione.action.ICostantiFasSigeDetenzione"%>

<jsp:useBean id="fasSigeDetCorrente"  scope="request" class="siap.sige.detenzione.model.FasSigeDetenzioneModel"/>
<jsp:useBean id="modalita"            scope="request" class="java.lang.String"/>
<jsp:useBean id="FascicoloSigeEsteso" scope="session" class="siap.sige.fascicolo.model.FascicoloSigeEstesoModel"/>

<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<%
  LuogoDetenzioneModel lLuogoDetenzione = fasSigeDetCorrente.getLuogoDetenzione();
  AltraCausaModel lAltraCausa = fasSigeDetCorrente.getAltraCausa();

  if(lLuogoDetenzione == null)
    lLuogoDetenzione = new LuogoDetenzioneModel();

  if(lAltraCausa == null)
    lAltraCausa = new AltraCausaModel();

	String lIdFascicoloSIEP = "";
  if (FascicoloSigeEsteso.getFascicoloSiep() != null && 
			FascicoloSigeEsteso.getFascicoloSiep().getIdFascicoloSiep() != null)
	lIdFascicoloSIEP = FascicoloSigeEsteso.getFascicoloSiep().getIdFascicoloSiep().toString();
%>

<script language="JavaScript">
  var desktop;
	<%
	if(modalita.equals("M"))
	{%>
  	var modifica = true;
<%}else{%>
  var modifica = false;
<%}%>

  function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2)
  {
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
  }

  function ListaComuni(a_formname,a_fieldname)
  {
    desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  }

  function calendario(a_formname,a_field_year,a_field_month,a_field_day)
  {
    desktop = 
        window.open("<%=IWebConstants.ROOT_DIR%>" + "files/siap/sico/Calendario.jsp?formname="+a_formname+"&fieldyear="+a_field_year+"&fieldmonth="+a_field_month+"&fieldday="+a_field_day, "Calendario","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=250");
  }
</script>

<script language="JavaScript">
  function Verify()
  {
    // Controllo validita' della data decorrenza detenzione
    var data_emissione=document.LoadInserisciDetenzioneFasSige.<%=ICostantiLuogoDetenzione.CAMPO_GIORNO_DATA_INIZIO_DETENZIONE%>.value+'/'+document.LoadInserisciDetenzioneFasSige.<%=ICostantiLuogoDetenzione.CAMPO_MESE_DATA_INIZIO_DETENZIONE%>.value+'/'+document.LoadInserisciDetenzioneFasSige.<%=ICostantiLuogoDetenzione.CAMPO_ANNO_DATA_INIZIO_DETENZIONE%>.value;
    var data_finedetenzione=document.LoadInserisciDetenzioneFasSige.<%=ICostantiLuogoDetenzione.CAMPO_GIORNO_DATA_FINE_DETENZIONE%>.value+'/'+document.LoadInserisciDetenzioneFasSige.<%=ICostantiLuogoDetenzione.CAMPO_MESE_DATA_FINE_DETENZIONE%>.value+'/'+document.LoadInserisciDetenzioneFasSige.<%=ICostantiLuogoDetenzione.CAMPO_ANNO_DATA_FINE_DETENZIONE%>.value;
    if (!modifica)
    {
	    if (data_emissione!='//')
	    {
	      if (! ControllaData(data_emissione) )
	      {
	        alert('Data di decorrenza non valida');
	        return false;
	      }
	
	    	// Data decorrenza minore <= data sistema
	    	var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
	    	if ( ! CompareDate( data_emissione,data_sistema) )
	    	{
	      	alert('Data Decorrenza maggiore della data attuale!');
	      	return false;
	    	}
		    if (data_finedetenzione!='//')
		    {
	      	if (! ControllaData(data_finedetenzione) )
	      	{
	        	alert('Data di fine detenzione non valida');
	        	return false;
	      	}
	    		// Data decorrenza minore <= data fine detenzione
	    		if ( ! CompareDate( data_emissione,data_finedetenzione) )
	    		{
	      		alert('Data Decorrenza maggiore della data fine detenzione!');
	      		return false;
	    		}
				}
	    }
   	} else {
      // Controllo validita' della data FINE DETENZIONE
      if ((data_finedetenzione.length > 2) && ! ControllaData(data_finedetenzione))
      {
         alert('Data fine detenzione non valida');
         return false;
      }
    }
    return true;
  }

	<%-- MERGE v10 COLLAUDO: aggiunta funzione di cancellazione --%>
  	function pulisciIstitutoId (nomeCampoComune, nomeCampoId) {
		var campoDescr = document.getElementById(nomeCampoComune);
		var campoId    = document.getElementById(nomeCampoId);
		campoDescr.value="";
		campoId.value="";
	}
</script>

<html>
  <head>
    <title>[S.I.E.S.] - Gestione Luogo Detenzione </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  </head>
  <body class="corpo">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class=LBG>
          <font class="label">Funzione :</font>&nbsp;
<%
            Date lDataDecorrenza = null;
            Date lDataFineDetenzione = null;

            String lAction = new String();

            if( modalita.equals("I") )
            {
              lAction = "siap.sige.detenzione.action.ActInserisciDetenzioneFasSige";
              lDataDecorrenza = lLuogoDetenzione.getDataFineDetenzione();
%>
              <font class="campo">Iscrizione Luogo Detenzione</font>
<%
            }
            else if (modalita.equals("M")) {
            	lAction = "siap.sige.detenzione.action.ActModificaDetenzioneFasSige";
             	lDataDecorrenza = lLuogoDetenzione.getDataInizioDetenzione();
              	// 20170703: modifica per la data fine: se non esiste data fine pena prendo Data Fine Detenzione
  	        	if (FascicoloSigeEsteso.getFascicoloSige().getDataFinePena() != null)
  	        		lDataFineDetenzione = FascicoloSigeEsteso.getFascicoloSige().getDataFinePena();
        		else
              		lDataFineDetenzione = lLuogoDetenzione.getDataFineDetenzione();

%>
              <font class="campo">Modifica Luogo Detenzione</font>
<%
            }
%>
        </td>
        <!-- BOTTONE DI RITORNO -->
          <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
      </tr>
  </table>

  <br>
  <table cellspacing=2 cellpadding=2>
    <tr>
      <jsp:include page="<%=ICostantiFascicoloSige.PG_LOAD_SINTESIPROCEDIMENTOSIGE%>"/>
    </tr>
  </table >

  <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciDetenzioneFasSige">
    <table cellspacing=2 cellpadding=2>

      <tr>
       	<td class="l">Posizione Giuridica</td>
        <td class="l"><%=FascicoloSigeEsteso.getFascicoloSige().getDescrPosizioneGiuridica()%></td>
      </tr>

      <tr>
        <td class="l">Data di Decorrenza</td>
        <td class="l">
          <input Title="Giorno Data Decorrenza" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDataDecorrenza, "dd") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiLuogoDetenzione.CAMPO_GIORNO_DATA_INIZIO_DETENZIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
          -
          <input Title="Mese Data Decorrenza" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDataDecorrenza, "MM"))%>" type="text" size="2" maxlength="2" name="<%= ICostantiLuogoDetenzione.CAMPO_MESE_DATA_INIZIO_DETENZIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
          -
          <input Title="Anno Data Decorrenza" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDataDecorrenza, "yyyy"))%>" type="text" size="4" maxlength="4" name="<%= ICostantiLuogoDetenzione.CAMPO_ANNO_DATA_INIZIO_DETENZIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >

			<!-- MEV 15 - Revisione SIGE -->
			<a href="javascript:calendario('LoadInserisciDetenzioneFasSige','<%=ICostantiLuogoDetenzione.CAMPO_ANNO_DATA_INIZIO_DETENZIONE%>','<%=ICostantiLuogoDetenzione.CAMPO_MESE_DATA_INIZIO_DETENZIONE%>','<%=ICostantiLuogoDetenzione.CAMPO_GIORNO_DATA_INIZIO_DETENZIONE%>');">
       			<img src="/images/calendario.gif" border=0>
        	</a>
        	
        </td>
       </tr>
       
      <tr>
        <td class="l">Data fine Detenzione</td>
        <td class="l">
          <input Title="Giorno Data fine Detenzione" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDataFineDetenzione, "dd") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiLuogoDetenzione.CAMPO_GIORNO_DATA_FINE_DETENZIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
          -
          <input Title="Mese Data fine Detenzione" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDataFineDetenzione, "MM"))%>" type="text" size="2" maxlength="2" name="<%= ICostantiLuogoDetenzione.CAMPO_MESE_DATA_FINE_DETENZIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
          -
          <input Title="Anno Data fine Detenzione" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDataFineDetenzione, "yyyy"))%>" type="text" size="4" maxlength="4" name="<%= ICostantiLuogoDetenzione.CAMPO_ANNO_DATA_FINE_DETENZIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >

		  <!-- MEV 15 - Revisione SIGE -->
		  <a href="javascript:calendario('LoadInserisciDetenzioneFasSige','<%=ICostantiLuogoDetenzione.CAMPO_ANNO_DATA_FINE_DETENZIONE%>','<%=ICostantiLuogoDetenzione.CAMPO_MESE_DATA_FINE_DETENZIONE%>','<%=ICostantiLuogoDetenzione.CAMPO_GIORNO_DATA_FINE_DETENZIONE%>');">
       		  <img src="/images/calendario.gif" border=0>
          </a>
        </td>
       </tr>

			<tr>
        		<td class="l">Tipo Istituto</td>
         <%if(lLuogoDetenzione.getIstDetIdIstitutoDetenzione() == null || lLuogoDetenzione.getIstDetIdIstitutoDetenzione().equals("") || lLuogoDetenzione.getIstDetIdIstitutoDetenzione().equals("-"))
           {%>
              	<td class="l">
              		<input readonly  Title="Istituto" name="Comune" value="" size=50>
              		<input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" size=50>
<%
            if( modalita.equals("I") )
              {%>
	              	<a href="Javascript:ListaIstitutoDetenzione('LoadInserisciDetenzioneFasSige','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
	              		<img src="/images/filefolder.gif" border=0>
	              	</a>
	              	<%-- MERGE v10 COLLAUDO: aggiunto tasto di cancellazione --%>
	              	<a href="Javascript:pulisciIstitutoId('Comune','<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>');"><img src="/images/delete.gif" border=0></a>
            <%}%>
               	</td>
         <%}else{%>
				<td class="l">
<%
					if (lLuogoDetenzione.getIstitutoDetenzione()!= null) {
%>
              		<input readonly Title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getDescrComune())%>" size=50>
<%}%>
              		<input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=lLuogoDetenzione.getIstDetIdIstitutoDetenzione()%>" size=50>
             		<a href="Javascript:ListaIstitutoDetenzione('LoadInserisciDetenzioneFasSige','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
              			<img src="/images/filefolder.gif" border=0>
              		</a>
              		<%-- MERGE v10 COLLAUDO: aggiunto tasto di cancellazione --%>
	              	<a href="Javascript:pulisciIstitutoId('Comune','<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>');"><img src="/images/delete.gif" border=0></a>
				</td>
<%}%>
      		</tr>

      <tr>
        <td class="l">Altro Luogo</td>
        <td class="L">
          <input title="Altro Luogo" value="<%=StringUtils.toStringJSP(lLuogoDetenzione.getAltroLuogo())%>" type="text" name="<%=ICostantiLuogoDetenzione.CAMPO_ALTRO_LUOGO%>" size="35"  >
        </td>
      </tr>

      <tr>
        <td colspan=2>
          <br>
          <input class="bottone" type="submit" value="Conferma">
        </td>
      </tr>

    </table>
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>">
    <input type="HIDDEN" name="<%=ICostantiFasSigeDetenzione.CAMPO_ID_FAS_SIGE_DETENZIONE%>" value="<%=StringUtils.toStringJSP(fasSigeDetCorrente.getIdFasSigeDetenzione())%>">
    <input type="HIDDEN" name="<%=ICostantiLuogoDetenzione.CAMPO_ID_LUOGO_DETENZIONE%>" value="<%=StringUtils.toStringJSP(lLuogoDetenzione.getIdLuogoDetenzione())%>">
    <input type="HIDDEN" name="<%=ICostantiAltraCausa.CAMPO_ID_ALTRA_CAUSA%>" value="<%=StringUtils.toStringJSP(lAltraCausa.getIdAltraCausa())%>">
    <input type="HIDDEN" name="<%=ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=StringUtils.toStringJSP(lAltraCausa.getIstDetIdIstitutoDetenzione())%>">
    <input type="HIDDEN" name="<%=ICostantiAltraCausa.CAMPO_ALTRO_LUOGO_ALTRA%>" value="<%=StringUtils.toStringJSP(lAltraCausa.getAltroLuogo())%>">
    <input type="HIDDEN" name="<%=ICostantiLuogoDetenzione.CAMPO_FAS_SIE_ID_FASCICOLO_SIEP%>" value="<%=lIdFascicoloSIEP%>">
  </form>

  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("LoadInserisciDetenzioneFasSige");

    frmvalidator.addValidation("<%=ICostantiLuogoDetenzione.CAMPO_ANNO_DATA_INIZIO_DETENZIONE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiLuogoDetenzione.CAMPO_ANNO_DATA_INIZIO_DETENZIONE%>","gt=1900");
    frmvalidator.addValidation("<%=ICostantiLuogoDetenzione.CAMPO_ANNO_DATA_INIZIO_DETENZIONE%>","lt=3000");
    frmvalidator.addValidation("<%=ICostantiLuogoDetenzione.CAMPO_GIORNO_DATA_INIZIO_DETENZIONE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiLuogoDetenzione.CAMPO_MESE_DATA_INIZIO_DETENZIONE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiLuogoDetenzione.CAMPO_ANNO_DATA_INIZIO_DETENZIONE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiLuogoDetenzione.CAMPO_ANNO_DATA_INIZIO_DETENZIONE%>","minlen=4","La lunghezza del campo Anno Data di Decorrenza deve essere di 4 caratteri");

    //Chiama la funzione di Verify().
    frmvalidator.setAddnlValidationFunction("Verify");
  </script>

</body>
</html>