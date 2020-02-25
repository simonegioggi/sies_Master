<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<jsp:useBean id="Trasmissioni"  scope="request" class="java.lang.String"/>
<html>
<head>
  <title>[S.I.E.S.] - Visualizzazione Dati delle Trasmissioni </title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="/html/gen_validatorv2.js"></script>
  <script language="JavaScript">
	  	function Verify()
	    {
	      	var data_inizio=document.LoadListaEsitiRicercaTrasmissioni.GGDataTrasmissioneInizio.value+'/'+document.LoadListaEsitiRicercaTrasmissioni.MMDataTrasmissioneInizio.value+'/'+document.LoadListaEsitiRicercaTrasmissioni.AADataTrasmissioneInizio.value;
	      	var data_fine=document.LoadListaEsitiRicercaTrasmissioni.GGDataTrasmissioneFine.value+'/'+document.LoadListaEsitiRicercaTrasmissioni.MMDataTrasmissioneFine.value+'/'+document.LoadListaEsitiRicercaTrasmissioni.AADataTrasmissioneFine.value;
	
	      	if(!ControllaDataPassaVuota(data_inizio))
	      	{
	        		alert('Data iniziale non valida');
	        		return false;
	      	}
	      	
	      	if(!ControllaDataPassaVuota(data_fine))
	      	{
	        		alert('Data finale non valida');
	        		return false;
	      	}
	
	      	if(!(data_inizio.length==2 || data_fine.length==2) )
	      	{
	      			if(!CompareDate(data_inizio,data_fine))
		      		{
		        			alert('La Data di ricerca finale non può essere inferiore alla data iniziale');
		        			return false;
		      		}
	      	}
	      	return true;
	    }

  </script>
  
  
</head>

<body class="corpo">
		<table>
    		<tr>
      			<td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        		<td class="LBG"><font class="label">Funzione :</font>&nbsp;<font class="campo">Visualizzazione Dati delle Trasmissioni</font></td>
      	</tr>
    </table>
    
		<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name='LoadListaEsitiRicercaTrasmissioni'>
    		<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.webservice.action.ActListaEsitiRicercaTrasmissioni">

				<table cellspacing=2 cellpadding=2>
						<tr>
	  						<td class="Titolo" colspan="6"> Selezione Data Trasmissione </td>
						</tr>
        		
        		<tr>
	  						<td class="l">
            				<table cellspacing=2 cellpadding=2>
	      								<tr>
														<td class="label">Dalla data &nbsp;</td>
														<td class="label">
		  													<input Title="Data di ricerca inizio" type="text" name="GGDataTrasmissioneInizio" maxlength="2" size="2" ONKEYUP="SkipField('GGDataTrasmissioneInizio','MMDataTrasmissioneInizio')" onBlur="javascript:value=FillDM(value)">
		   													-
		  													<input Title="Data di ricerca inizio" type="text" name="MMDataTrasmissioneInizio" maxlength="2" size="2" ONKEYUP="SkipField('MMDataTrasmissioneInizio','AADataTrasmissioneInizio')" onBlur="javascript:value=FillDM(value)">
		   													-
		  													<input Title="Data di ricerca inizio" type="text" name="AADataTrasmissioneInizio" maxlength="4" size="4" ONKEYUP="SkipField('AADataTrasmissioneInizio','GGDataTrasmissioneFine')" onBlur="javascript:value=FillYear(value)">
														</td>
														<td class="label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Alla data </td>
														<td class="label">
		  													<input Title="Data di ricerca fine" type="text" name="GGDataTrasmissioneFine" maxlength="2" size="2" ONKEYUP="SkipField('GGDataTrasmissioneFine','MMDataTrasmissioneFine')" onBlur="javascript:value=FillDM(value)">
		   													-
		 			 											<input Title="Data di ricerca fine" type="text" name="MMDataTrasmissioneFine" maxlength="2" size="2" ONKEYUP="SkipField('MMDataTrasmissioneFine','AADataTrasmissioneFine')" onBlur="javascript:value=FillDM(value)">
		   													-
		  													<input Title="Data di ricerca fine" type="text" name="AADataTrasmissioneFine" maxlength="4" size="4" ONKEYUP="SkipField('AADataTrasmissioneFine','cmbTipoTrasmissione')" onBlur="javascript:value=FillYear(value)">
														</td>
	      								</tr>
	    							</table>
	  						</td>
        		</tr>
        		
        		<tr>
	  						<td class="Titolo" colspan="6"> Selezione Tipo Trasmissione </td>
						</tr>

						<tr>
	  						<td class="l">
            				<table cellspacing=2 cellpadding=2>
	      								<tr>
														<td class="label">Tipo Trasmissione &nbsp;</td>
														<td class="label">
          											<select  title="Tipo Trasmissione" name="cmbTipoTrasmissione">
            												<%= Trasmissioni %>
          											</select>
         										</td>
												</tr>
										</table>
								</td>
						</tr>		
						
						        		<tr>
	  						<td class="Titolo" colspan="6"> Selezione Esito Trasmissione </td>
						</tr>

						<tr>
	  						<td class="l">
            				<table cellspacing=2 cellpadding=2>
	      								<tr>
														<td class="label">Esito Trasmissione &nbsp;</td>
														<td class="label">
          											<select  title="Esito Trasmissione" name="cmbEsitoTrasmissione">
          													<option value = '-'>-
            												<option value = '0'>Procedimento Trasferito con successo
																		<option value = '100'>Procedimento NON Trasferito
																		<option value = '2'>Procedimento già Presente
          											</select>
         										</td>
												</tr>
										</table>
								</td>
						</tr>												
						
						<tr>
	  						<td class="l">
            				<table cellspacing=2 cellpadding=2>
	      								<tr>
														<td><input onclick="Javascript:return Verify();" class="bottone" type="submit" name="RICERCA" value="Ricerca"></td>
												</tr>
										</table>
								</td>
						</tr>											
  			</table>
		</FORM>
		
		<script language="JavaScript" type="text/javascript">
				
    		var frmvalidator  = new Validator("LoadListaEsitiRicercaTrasmissioni");

	      frmvalidator.addValidation("GGDataTrasmissioneInizio","maxlen=2","La lunghezza massima per il giorno di inizio è di 2 caratteri");
	      frmvalidator.addValidation("GGDataTrasmissioneInizio","minlen=2","La lunghezza minima per il giorno di inizio è di 2 caratteri");
	      frmvalidator.addValidation("GGDataTrasmissioneInizio","numeric");
	      frmvalidator.addValidation("GGDataTrasmissioneInizio","gt=1");
	      frmvalidator.addValidation("GGDataTrasmissioneInizio","lt=31");

	      frmvalidator.addValidation("MMDataTrasmissioneInizio","maxlen=2","La lunghezza massima per il mese di inizio è di 2 caratteri");
	      frmvalidator.addValidation("MMDataTrasmissioneInizio","minlen=2","La lunghezza minima per il mese di inizio è di 2 caratteri");
	      frmvalidator.addValidation("MMDataTrasmissioneInizio","numeric");
	      frmvalidator.addValidation("MMDataTrasmissioneInizio","gt=1");
	      frmvalidator.addValidation("MMDataTrasmissioneInizio","lt=12");
	
	      frmvalidator.addValidation("AADataTrasmissioneInizio","maxlen=4","La lunghezza massima per l'anno di inizio è di 4 caratteri");
	      frmvalidator.addValidation("AADataTrasmissioneInizio","minlen=4","La lunghezza minima per l'anno di inizio è di 4 caratteri");
	      frmvalidator.addValidation("AADataTrasmissioneInizio","numeric");
	      frmvalidator.addValidation("AADataTrasmissioneInizio","gt=1900");
	      frmvalidator.addValidation("AADataTrasmissioneInizio","lt=3000");
	
	      frmvalidator.addValidation("GGDataTrasmissioneFine","maxlen=2","La lunghezza massima per il giorno di fine è di 2 caratteri");
	      frmvalidator.addValidation("GGDataTrasmissioneFine","minlen=2","La lunghezza minima per il giorno di fine è di 2 caratteri");
	      frmvalidator.addValidation("GGDataTrasmissioneFine","numeric");
	      frmvalidator.addValidation("GGDataTrasmissioneFine","gt=1");
	      frmvalidator.addValidation("GGDataTrasmissioneFine","lt=31");
	
	      frmvalidator.addValidation("MMDataTrasmissioneFine","maxlen=2","La lunghezza massima per il mese di fine è di 2 caratteri");
	      frmvalidator.addValidation("MMDataTrasmissioneFine","minlen=2","La lunghezza minima per il mese di fine è di 2 caratteri");
	      frmvalidator.addValidation("MMDataTrasmissioneFine","numeric");
	      frmvalidator.addValidation("MMDataTrasmissioneFine","gt=1");
	      frmvalidator.addValidation("MMDataTrasmissioneFine","lt=12");
	
	      frmvalidator.addValidation("AADataTrasmissioneFine","maxlen=4","La lunghezza massima per l'anno di fine è di 4 caratteri");
	      frmvalidator.addValidation("AADataTrasmissioneFine","minlen=4","La lunghezza minima per l'anno di fine è di 4 caratteri");
	      frmvalidator.addValidation("AADataTrasmissioneFine","numeric");
	      frmvalidator.addValidation("AADataTrasmissioneFine","gt=1900");
	      frmvalidator.addValidation("AADataTrasmissioneFine","lt=3000");
    </script>
		  			
 </body>
 </html>
 