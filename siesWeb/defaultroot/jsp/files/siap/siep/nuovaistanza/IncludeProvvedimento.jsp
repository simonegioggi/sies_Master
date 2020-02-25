<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.siep.nuovaistanza.action.ICostantiNuovaIstanza"%>

  <table width="100%">
    <tr>
      <td class="Titolo"> Selezionare il tipo di Provvedimento&nbsp;</td>
      <td class="Titolo" align="center">
        Sentenza <input type="radio" name="TipoProvv" value="01"   onClick="VisualizzaSentenza();" checked> 
				Decreto Penale <input type="radio" name="TipoProvv" value="02"   onClick="VisualizzaSentenza();">
				Sentenza Straniera Delibata<input type="radio" name="TipoProvv" value="05"   onClick="VisualizzaSentenza();">
      </td>
    </tr>
  </table>
  
  	<div id="SentenzaDiv" style="display:none; position:relative; ">  
			<jsp:include page="/jsp/files/siap/siep/nuovaistanza/IncludeSentenza.jsp"/>
  	</div>
  	<div id="SentenzaStranieraDiv" style="display:none; position:relative; ">      
    	<jsp:include page="/jsp/files/siap/siep/nuovaistanza/IncludeSentenzaStraniera.jsp"/>
  	</div>
  	<div id="DecretoDiv" style="display:none; position:relative; ">      
    	<jsp:include page="/jsp/files/siap/siep/nuovaistanza/IncludeDecreto.jsp"/>
  	</div>

  <table width="100%">
    <tr>
      <td class="l" >Data Irrevocabilità </td>
      <td class="l" colspan="3"> 
        <input type="text" size="2" maxlength="2"  
               name="<%= ICostantiNuovaIstanza.CAMPO_GIORNO_DATA_IRREVOCABILITA %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2"  
               name="<%= ICostantiNuovaIstanza.CAMPO_MESE_DATA_IRREVOCABILITA %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" 
               name="<%= ICostantiNuovaIstanza.CAMPO_ANNO_DATA_IRREVOCABILITA %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
  </table>
