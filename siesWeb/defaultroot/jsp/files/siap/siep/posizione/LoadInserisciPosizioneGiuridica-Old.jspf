<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@page import="siap.siep.altracausa.action.ICostantiAltraCausa"%>
<%@page import="java.util.Iterator"%>
<%@page import="siap.sico.decodifiche.model.DecodificheModel"%>
<%@page import="f3b.util.StringUtils"%>
<%@page import="f3b.util.DateUtils"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="f3b.log.LogF3B"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<%
final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); 
String alfa_ist  = "";
String beta_ist  = "";

if(lLuogoDetenzione.getIstDetIdIstitutoDetenzione() != null && !"".equals(lLuogoDetenzione.getIstDetIdIstitutoDetenzione()) && !"-".equals(lLuogoDetenzione.getIstDetIdIstitutoDetenzione())) {
	alfa_ist = StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto()) + " di " + StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getDescrizione()) + " - " + StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getIndirizzo());
	beta_ist = lLuogoDetenzione.getIstDetIdIstitutoDetenzione();
}
%>

    <table width="100%">
      <tr>
        <td class="l">Posizione Giuridica <font class="ob">(*)</font></td>
        <td class="l" colspan="3">
          <select title="Posizione Giuridica" name="<%= ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA %>">
<%
            Iterator lIter = posizioneGiuridicaEsecuzione.iterator();
            while(lIter.hasNext()) {
              DecodificheModel lDecMod = (DecodificheModel)lIter.next();
              if(lDecMod.getCode().equals("08") || lDecMod.getCode().equals("05")) {
            	  //eslusione della posizione giuridica latitante
            	  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
            	  //siesLogger.debug("Esclusione posizione giuridica Latitante");
              } else {
                if(lDecMod.getCode().equals("10") || lDecMod.getCode().equals("03")) {
%>
                <option style="color:red" value="<%=lDecMod.getCode()%>" <%=(lPosGiu.getDescrPosizioneGiuridica().equals(lDecMod.getDescription())) ? "selected" : ""%>/><%=lDecMod.getDescription()%>
<%                
              } else if(lDecMod.getCode().equals("13") || lDecMod.getCode().equals("12") || lDecMod.getCode().equals("14") || lDecMod.getCode().equals("27")) {
%>
                 <option style="color:orange" value="<%=lDecMod.getCode()%>" <%=(lPosGiu.getDescrPosizioneGiuridica().equals(lDecMod.getDescription())) ? "selected" : ""%>/><%=lDecMod.getDescription()%>
<%                 
              } else {
%>
                <option value="<%=lDecMod.getCode()%>" <%=(lPosGiu.getDescrPosizioneGiuridica().equals(lDecMod.getDescription())) ? "selected" : ""%>/><%=lDecMod.getDescription()%>
<%
                }
              }
            } // end while
%>
          </select>
        </td>
      </tr>
      <tr>
        <td class="l">Data di Decorrenza</td>
        <td class="l"  colspan="3">
          <input Title="Giorno Data Decorrenza" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDataDecorrenza, "dd") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiPosizioneGiuridica.CAMPO_GIORNO_DATA_INIZIO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
          -
          <input Title="Mese Data Decorrenza" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDataDecorrenza, "MM"))%>" type="text" size="2" maxlength="2" name="<%= ICostantiPosizioneGiuridica.CAMPO_MESE_DATA_INIZIO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
          -
          <input Title="Anno Data Decorrenza" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDataDecorrenza, "yyyy"))%>" type="text" size="4" maxlength="4" name="<%= ICostantiPosizioneGiuridica.CAMPO_ANNO_DATA_INIZIO%>" onBlur="javascript:value=FillYear(value)">
        </td>
       </tr>
       
      <tr>
        <td class="l">Istituto</td>
	    <td class="l" colspan="3">
	      <input title="Istituto" name="Comune" id="Comune" value="<%=alfa_ist%>" size=100 readonly>
	      <input type="hidden"  name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" id="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=beta_ist%>">
	      <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciPosizioneGiuridicaOld','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
	        <img src="/images/filefolder.gif" border=0></a>
	      <a href="Javascript:pulisciIstitutoId('Comune','<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>');"><img src="/images/delete.gif" border=0></a>
	    </td>
     </tr>
      <tr>
        <td class="l">Altro Luogo Detenzione</td>
        <td class="L" colspan="3">
          <input title="Altro Luogo" value="<%=StringUtils.toStringJSP(lLuogoDetenzione.getAltroLuogo())%>" type="text" name="<%=ICostantiLuogoDetenzione.CAMPO_ALTRO_LUOGO%>" size="30">
        </td>
      </tr>

      <tr>
        <td class="l">Luogo Prova Affidamento</td>
        <td class="L">
          <input title="Presso" value="<%=StringUtils.toStringJSP(lPosGiu.getLuogoProvaAffidamento())%>" type="text" name="<%=ICostantiPosizioneGiuridica.CAMPO_LUOGO_PROVA_AFFIDAMENTO%>" size="30">
        </td>
        <td class="l">Luogo Lavoro Semilibertà</td>
        <td class="L">
          <input title="Luogo" value="<%=StringUtils.toStringJSP(lPosGiu.getLuogoLavoroSemiliberta())%>" type="text" name="<%=ICostantiPosizioneGiuridica.CAMPO_LUOGO_LAVORO_SEMILIBERTA%>" size="30">
        </td>
      </tr>

<!-- ======================================================================= -->
<!--                      SEZIONE DETENUTO ALTRA CAUSA                       -->
<!-- ======================================================================= -->
      <tr><td class="Titolo" colspan=4>Detenuto per altra causa</td></tr>
      <tr>
        <td class="l">Detenuto per altra causa</td>
        <td class="l" colspan="3">
          <input type='checkbox' name='<%=ICostantiFascicoloSiep.CAMPO_FLAG_ALTRA_CAUSA%>' value = 'S' <%=(lFascicoloAssociato.getFlagAltraCausa() != null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) ? "checked" : ""%>>
        </td>
      </tr>
      <tr>
        <td class="l">Tipo Misura</td>
        <td class="l" colspan="3">
          <select title="Tipo Misura" name="<%=ICostantiAltraCausa.CAMPO_COD_TIPO_POS_GIURIDICA%>" onchange="comboMisuraOld();">
            <%=tipoPosizioneAltraCausa%>
          </select>
        </td>
      </tr>
      <tr>
        <td class="l" width="21%">Data di Decorrenza</td>
        <td  class="l" >
          <input  Title="Giorno Data di Decorrenza detenuto per altra causa" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lAltraCausa.getDataDecorrenza(), "dd") )%>" type="text" size="2" maxlength="2" name="<%=ICostantiAltraCausa.CAMPO_GIORNO_DATA_DECORRENZA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
          -
          <input Title="Mese Data di Decorrenza detenuto per altra causa" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lAltraCausa.getDataDecorrenza(), "MM"))%>" type="text" size="2" maxlength="2" name="<%=ICostantiAltraCausa.CAMPO_MESE_DATA_DECORRENZA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
          -
          <input Title="Anno Data di Decorrenza detenuto per altra causa" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lAltraCausa.getDataDecorrenza(), "yyyy"))%>" type="text" size="4" maxlength="4" name="<%=ICostantiAltraCausa.CAMPO_ANNO_DATA_DECORRENZA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
       </td>
        <td class="l" width="20%">Data di Scadenza</td>
        <td class="l">
          <input Title="Giorno Data di scadenza detenuto per altra causa" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lAltraCausa.getDataScadenza(), "dd") )%>" type="text" size="2" maxlength="2" name="<%=ICostantiAltraCausa.CAMPO_GIORNO_DATA_SCADENZA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
          -
          <input Title="Mese Data di scadenza detenuto per altra causa" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lAltraCausa.getDataScadenza(), "MM"))%>" type="text" size="2" maxlength="2" name="<%=ICostantiAltraCausa.CAMPO_MESE_DATA_SCADENZA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
          -
          <input Title="Anno Data di scadenza detenuto per altra causa" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lAltraCausa.getDataScadenza(), "yyyy"))%>" type="text" size="4" maxlength="4" name="<%=ICostantiAltraCausa.CAMPO_ANNO_DATA_SCADENZA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        </td>
      </tr>
      <tr>
        <td class="l">Istituto</td>
<%
if(lAltraCausa.getIstitutoDetenzione() == null || lAltraCausa.getIstitutoDetenzione().equals("")) {
%>
    <td class="l"  colspan="3">
      <input title="Istituto" name="Comune2" id="Comune2" value="" size=100 readonly>
      <input type="hidden"  name="<%=ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" id="<%=ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="">
      <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciPosizioneGiuridicaOld','<%= ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune2');">
        <img src="/images/filefolder.gif" border=0></a>
      <a href="Javascript:pulisciIstitutoId('Comune2','<%=ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>');"><img src="/images/delete.gif" border=0></a>
    </td>
<%
} else {
%>
    <td class="l"  colspan="3">
      <input  title="Istituto" name="Comune2" id="Comune2" value="<%=StringUtils.toStringJSP(lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(lAltraCausa.getIstitutoDetenzione().getDescrizione())%> - <%=StringUtils.toStringJSP(lAltraCausa.getIstitutoDetenzione().getIndirizzo())%>" size=100 readonly >
      <input type="hidden"  name="<%=ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" id="<%=ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=lAltraCausa.getIstDetIdIstitutoDetenzione()%>">
      <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciPosizioneGiuridicaOld','<%= ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune2');">
        <img src="/images/filefolder.gif" border=0></a>
      <a href="Javascript:pulisciIstitutoId('Comune2','<%=ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>');"><img src="/images/delete.gif" border=0></a>
    </td>
<%
}
%>
      </tr>
      <tr>
        <td class="l">Altro Luogo Detenzione</td>
        <td class="L"  colspan="3">
          <input title="Altro Luogo Altra Causa" value="<%=StringUtils.toStringJSP(lAltraCausa.getAltroLuogo())%>" type="text" name="<%= ICostantiAltraCausa.CAMPO_ALTRO_LUOGO_ALTRA%>" size="30">
        </td>
      </tr>
      <tr>
        <td class="l">Anno/Numero Tit. Esec.</td>
        <td class="L">
           <input Title="Anno Altra Causa" value="<%=StringUtils.toStringJSP(lAltraCausa.getAnno())%>" type="text" name="<%=ICostantiAltraCausa.CAMPO_ANNO%>" maxlength="4" size="4">
           /
           <input Title="Numero Altra Causa" value="<%=StringUtils.toStringJSP(lAltraCausa.getNumero())%>" type="text" name="<%=ICostantiAltraCausa.CAMPO_NUMERO%>" maxlength="6" size="6">
        </td>
        <td class="l">Data</td>
        <td class="l">
          <input Title="Giorno Data Altra Causa" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lAltraCausa.getData(), "dd") )%>" type="text" size="2" maxlength="2" name="<%=ICostantiAltraCausa.CAMPO_GIORNO_DATA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
          -
          <input Title="Mese Data Altra Causa" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lAltraCausa.getData(), "MM"))%>" type="text" size="2" maxlength="2" name="<%=ICostantiAltraCausa.CAMPO_MESE_DATA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
          -
          <input Title="Anno Data Altra Causa" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lAltraCausa.getData(), "yyyy"))%>" type="text" size="4" maxlength="4" name="<%=ICostantiAltraCausa.CAMPO_ANNO_DATA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        </td>
      </tr>
      <tr>
        <td class="l">Autorità</td>
        <td class="l" colspan="3">
          <select Title="Autorità" name="<%=ICostantiAltraCausa.CAMPO_COD_AUTORITA%>">
            <%=autoritaEmittente%>
          </select>
        </td>
  </tr>
  <tr>
        <td class="l">Luogo</td>
        <td class="l" colspan="3">
          <input value="<%=StringUtils.toStringJSP(lAltraCausa.getDescrLuogo())%>" type="text" name="<%=ICostantiAltraCausa.CAMPO_COD_LUOGO%>">
          <a href="Javascript:ListaComuni('LoadInserisciPosizioneGiuridicaOld','<%=ICostantiAltraCausa.CAMPO_COD_LUOGO%>');">
            <img src="/images/filefolder.gif" border=0>
          </a>
        </td>
      </tr>

      <tr>
        <td colspan=4>
          <br>
          <input class="bottone" type="submit" name="INSERISCI" value="Conferma">
        </td>
      </tr>
    </table>

<script language="JavaScript">
function VerifyOld() {
  // Controllo di Obligatorietà Tipo Posizione Giuridica
  // ** La Posizione Giuridica è obligatoria se NON è Detenuto per altra causa **
  if( document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>[document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.selectedIndex].value == '-'
      && document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiFascicoloSiep.CAMPO_FLAG_ALTRA_CAUSA%>.checked == false )
  {
    alert('Il Tipo Posizione Giuridica è obbligatorio');
    document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.focus();

    return false;
  }

  // Controllo Se Altra Causa la Posizione Giuridica deve essere LIBERO
  if(document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiFascicoloSiep.CAMPO_FLAG_ALTRA_CAUSA%>.checked == true)
  {
    if(    document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>[document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.selectedIndex].value != '07'
        && document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>[document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.selectedIndex].value != '10'
        && document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>[document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.selectedIndex].value != '-' )
    {
      alert('Il Tipo Posizione Giuridica deve essere LIBERO se Detenuto per Altra Causa');
      document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.focus();

      return false;
    }

    // Se la posizione giuridica è "-" selezionare automaticamente "LIBERO"
    if(document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>[document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.selectedIndex].value == '-')
    {
      var indiceLibero=0;
      for (var i=0; i<document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.length; i++)
      {
        if(   document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>[i].text == 'Libero'
           || document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>[i].text == 'LIBERO')
        {
          indiceLibero=i;
        }
      }

      document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.selectedIndex = indiceLibero;
    }
  }

	//modifica relativa al tipo istituto
	if(document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.value != '' )
	{
	  if (document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>[document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.selectedIndex].text == 'Libero'
		|| document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>[document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.selectedIndex].text == 'LIBERO'
		|| document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>[document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.selectedIndex].value == '-'
		|| document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>[document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.selectedIndex].value == '02'
		|| document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>[document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.selectedIndex].value == '04' )
		{
<% if(lIscrizionePos.equals("S")) {%>
			alert('Posizione Giuridica e Istituto Incompatibili');
			document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.focus();
			document.LoadInserisciPosizioneGiuridicaOld.Comune.value = '';
			document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.value = '';
			return false;
<%}else{%>
			document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.value = '';
<%}%>
		}
	}
	//fine modifica relativa al tipo istituto

  // Controllo Data di Decorrenza posizione giuridica
  if (document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiPosizioneGiuridica.CAMPO_GIORNO_DATA_INIZIO%>.value.length==1)
    document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiPosizioneGiuridica.CAMPO_GIORNO_DATA_INIZIO%>.value='0'+document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiPosizioneGiuridica.CAMPO_GIORNO_DATA_INIZIO%>.value;
  if (document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiPosizioneGiuridica.CAMPO_MESE_DATA_INIZIO%>.value.length==1)
    document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiPosizioneGiuridica.CAMPO_MESE_DATA_INIZIO%>.value='0'+document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiPosizioneGiuridica.CAMPO_MESE_DATA_INIZIO%>.value;

  var d1=document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiPosizioneGiuridica.CAMPO_GIORNO_DATA_INIZIO%>.value+'/'+document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiPosizioneGiuridica.CAMPO_MESE_DATA_INIZIO%>.value+'/'+document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiPosizioneGiuridica.CAMPO_ANNO_DATA_INIZIO%>.value;
  if (d1.length > 2 && ! ControllaData(d1))
  {
    alert('Data di Decorrenza non valida');
    document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiPosizioneGiuridica.CAMPO_GIORNO_DATA_INIZIO%>.focus();

    return false;
  }

  // Controllo di Obligatorietà Data Decorrenza in caso
  // la Posizione Giuridica è di tipo "detenuto per questa causa";
  //    corrispondente ai codici :
  //    01 - IN CUSTODIA CAUTELARE PER QUESTA CAUSA IN REGIME DI DETENZIONE,
  //    02 - IN CUSTODIA CAUTELARE PER QUESTA CAUSA IN REGIME DI ARRESTI DOMICILIARI
  if(  document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>[document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.selectedIndex].value=='01'
    || document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>[document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.selectedIndex].value=='02'
    )
  {
    if (d1.length == 2)
    {
      alert('La Data di Decorrenza è obbligatoria se la Posizione Giuridica è di tipo IN CUSTODIA CAUTELARE PER QUESTA CAUSA IN REGIME DI DETENZIONE o IN CUSTODIA CAUTELARE PER QUESTA CAUSA IN REGIME DI ARRESTI DOMICILIARI');
      document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiPosizioneGiuridica.CAMPO_GIORNO_DATA_INIZIO%>.focus();

      return false;
    }
  }

  // Controllo Data di Decorrenza detenuto altra causa
  if (document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiAltraCausa.CAMPO_GIORNO_DATA_DECORRENZA%>.value.length==1)
    document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiAltraCausa.CAMPO_GIORNO_DATA_DECORRENZA%>.value='0'+document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiAltraCausa.CAMPO_GIORNO_DATA_DECORRENZA%>.value;
  if (document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiAltraCausa.CAMPO_MESE_DATA_DECORRENZA%>.value.length==1)
    document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiAltraCausa.CAMPO_MESE_DATA_DECORRENZA%>.value='0'+document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiAltraCausa.CAMPO_MESE_DATA_DECORRENZA%>.value;

  var dataDecorrenzaAltraCausa=document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiAltraCausa.CAMPO_GIORNO_DATA_DECORRENZA%>.value+'/'+document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiAltraCausa.CAMPO_MESE_DATA_DECORRENZA%>.value+'/'+document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiAltraCausa.CAMPO_ANNO_DATA_DECORRENZA%>.value;
  if (dataDecorrenzaAltraCausa.length > 2 && !ControllaData(dataDecorrenzaAltraCausa))
  {
    alert('Data di Decorrenza detenuto per altra causa non valida');
    document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiAltraCausa.CAMPO_GIORNO_DATA_DECORRENZA%>.focus();

    return false;
  }

  // Controllo Data di Scadenza detenuto altra causa
  if (document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiAltraCausa.CAMPO_GIORNO_DATA_SCADENZA%>.value.length==1)
    document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiAltraCausa.CAMPO_GIORNO_DATA_SCADENZA%>.value='0'+document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiAltraCausa.CAMPO_GIORNO_DATA_SCADENZA%>.value;
  if (document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiAltraCausa.CAMPO_MESE_DATA_SCADENZA%>.value.length==1)
    document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiAltraCausa.CAMPO_MESE_DATA_SCADENZA%>.value='0'+document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiAltraCausa.CAMPO_MESE_DATA_SCADENZA%>.value;

  var dataScadenzaAltraCausa=document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiAltraCausa.CAMPO_GIORNO_DATA_SCADENZA%>.value+'/'+document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiAltraCausa.CAMPO_MESE_DATA_SCADENZA%>.value+'/'+document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiAltraCausa.CAMPO_ANNO_DATA_SCADENZA%>.value;
  if (dataScadenzaAltraCausa.length > 2 && !ControllaData(dataScadenzaAltraCausa))
  {
    alert('Data di Scadenza detenuto per altra causa non valida');
    document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiAltraCausa.CAMPO_GIORNO_DATA_SCADENZA%>.focus();

    return false;
  }

  // Controllo Data Titolo Esecutivo detenuto altra causa
  if (document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiAltraCausa.CAMPO_GIORNO_DATA%>.value.length==1)
    document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiAltraCausa.CAMPO_GIORNO_DATA%>.value='0'+document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiAltraCausa.CAMPO_GIORNO_DATA%>.value;
  if (document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiAltraCausa.CAMPO_MESE_DATA%>.value.length==1)
    document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiAltraCausa.CAMPO_MESE_DATA%>.value='0'+document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiAltraCausa.CAMPO_MESE_DATA%>.value;

  var dataAltraCausa=document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiAltraCausa.CAMPO_GIORNO_DATA%>.value+'/'+document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiAltraCausa.CAMPO_MESE_DATA%>.value+'/'+document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiAltraCausa.CAMPO_ANNO_DATA%>.value;
  if (dataAltraCausa.length > 2 && !ControllaData(dataAltraCausa))
  {
    alert('Data Titolo Esecutivo per altra causa non valida');
    document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiAltraCausa.CAMPO_GIORNO_DATA%>.focus();

    return false;
  }

  if( document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiFascicoloSiep.CAMPO_FLAG_ALTRA_CAUSA%>.checked==true)
  {

    if( document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiAltraCausa.CAMPO_COD_TIPO_POS_GIURIDICA%>.value=="-")
    {
      alert('Il tipo misura è obbligatorio');
      return false;
    }

  }

  if( document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiAltraCausa.CAMPO_COD_TIPO_POS_GIURIDICA%>.value!="-" && document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiFascicoloSiep.CAMPO_FLAG_ALTRA_CAUSA%>.checked==false)
  {
    //alert('Selezionare Altra Causa');
    //return false;
  }

  return true;
}
 
function comboMisuraOld() {

    if( document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiAltraCausa.CAMPO_COD_TIPO_POS_GIURIDICA%>.value=="22") {
       document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiAltraCausa.CAMPO_GIORNO_DATA_DECORRENZA%>.disabled=true;
       document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiAltraCausa.CAMPO_MESE_DATA_DECORRENZA%>.disabled=true;
       document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiAltraCausa.CAMPO_ANNO_DATA_DECORRENZA%>.disabled=true;
       document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiAltraCausa.CAMPO_GIORNO_DATA_SCADENZA%>.disabled=true;
       document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiAltraCausa.CAMPO_MESE_DATA_SCADENZA%>.disabled=true;
       document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiAltraCausa.CAMPO_ANNO_DATA_SCADENZA%>.disabled=true;

    }  else {
        document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiAltraCausa.CAMPO_GIORNO_DATA_DECORRENZA%>.disabled=false;
       document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiAltraCausa.CAMPO_MESE_DATA_DECORRENZA%>.disabled=false;
       document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiAltraCausa.CAMPO_ANNO_DATA_DECORRENZA%>.disabled=false;
       document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiAltraCausa.CAMPO_GIORNO_DATA_SCADENZA%>.disabled=false;
       document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiAltraCausa.CAMPO_MESE_DATA_SCADENZA%>.disabled=false;
       document.LoadInserisciPosizioneGiuridicaOld.<%=ICostantiAltraCausa.CAMPO_ANNO_DATA_SCADENZA%>.disabled=false;

    }
}
</script>

<script language="JavaScript" type="text/javascript">
var frmvalidatorOld = new Validator("LoadInserisciPosizioneGiuridicaOld");

frmvalidatorOld.addValidation("<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>","req","Il tipo Posizione Giuridica è obbligatorio");

frmvalidatorOld.addValidation("<%=ICostantiPosizioneGiuridica.CAMPO_ANNO_DATA_INIZIO%>","numeric");
frmvalidatorOld.addValidation("<%=ICostantiPosizioneGiuridica.CAMPO_ANNO_DATA_INIZIO%>","gt=1900");
frmvalidatorOld.addValidation("<%=ICostantiPosizioneGiuridica.CAMPO_ANNO_DATA_INIZIO%>","lt=3000");

frmvalidatorOld.addValidation("<%=ICostantiAltraCausa.CAMPO_ANNO_DATA%>","numeric");
frmvalidatorOld.addValidation("<%=ICostantiAltraCausa.CAMPO_ANNO_DATA%>","gt=1900");
frmvalidatorOld.addValidation("<%=ICostantiAltraCausa.CAMPO_ANNO_DATA%>","lt=3000");

frmvalidatorOld.addValidation("<%=ICostantiAltraCausa.CAMPO_ANNO_DATA_DECORRENZA%>","numeric");
frmvalidatorOld.addValidation("<%=ICostantiAltraCausa.CAMPO_ANNO_DATA_DECORRENZA%>","gt=1900");
frmvalidatorOld.addValidation("<%=ICostantiAltraCausa.CAMPO_ANNO_DATA_DECORRENZA%>","lt=3000");

frmvalidatorOld.addValidation("<%=ICostantiAltraCausa.CAMPO_ANNO_DATA_SCADENZA%>","numeric");
frmvalidatorOld.addValidation("<%=ICostantiAltraCausa.CAMPO_ANNO_DATA_SCADENZA%>","gt=1900");
frmvalidatorOld.addValidation("<%=ICostantiAltraCausa.CAMPO_ANNO_DATA_SCADENZA%>","lt=3000");

frmvalidatorOld.addValidation("<%=ICostantiAltraCausa.CAMPO_ANNO%>","numeric");
frmvalidatorOld.addValidation("<%=ICostantiAltraCausa.CAMPO_ANNO%>","minlength=4");
frmvalidatorOld.addValidation("<%=ICostantiAltraCausa.CAMPO_NUMERO%>","numeric");

frmvalidatorOld.setAddnlValidationFunction("VerifyOld");
</script>