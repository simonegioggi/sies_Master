/*
*  The Validator
*   The class that handles all validation related issues
*
*   pass the name of the form while constructing.
*   methods:
*    addValidation(input_item_name,validation_descriptor,error_string)
*       call this method for each input item. Single input item can have
*       many validations
*
*    setAddnlValidationFunction(function_name)
*             call this function to set a custom validat function, which will
*						 be called after other validations are over.
*			       The function should return 'true' or 'false'
*/
function Validator(frmname)
{
  this.formobj=document.forms[frmname];

	if(!this.formobj)
	{
	  alert("BUG: Non esiste una form con nome  "+frmname);
		return;
	}
	if(this.formobj.onsubmit)
	{
	 this.formobj.old_onsubmit = this.formobj.onsubmit;
	 this.formobj.onsubmit=null;
	}
	else
	{
	 this.formobj.old_onsubmit = null;
	}
	this.formobj.onsubmit=form_submit_handler;
	this.addValidation = add_validation;
	this.addValidationWithIdx = add_validation_idx;
	this.setAddnlValidationFunction=set_addnl_vfunction;
	this.clearAllValidations = clear_all_validations;
}

function set_addnl_vfunction(functionname)
{
  this.formobj.addnlvalidation = functionname;
}

function clear_all_validations()
{
	for(var itr=0;itr < this.formobj.elements.length;itr++)
	{
		this.formobj.elements[itr].validationset = null;
	}
}

function form_submit_handler()
{
	for(var itr=0;itr < this.elements.length;itr++)
	{
    //alert("elements : " + this.elements[itr] );

		if(this.elements[itr].validationset &&
	   !this.elements[itr].validationset.validate())
		{
		  return false;
		}
	}
	if(this.addnlvalidation)
	{
	  str =" var ret = "+this.addnlvalidation+"()";
	  eval(str);
    if(!ret) return ret;
	}

  submitonce(this);

	return true;
}



 function submitonce(theform)
{
  if (document.all||document.getElementById)
   {
   //screen thru every element in the form, and hunt down "submit" and "reset"
    for (i=0;i<theform.length;i++)
     {
     var tempobj=theform.elements[i];
       if(tempobj.type.toLowerCase()=="submit" ||tempobj.type.toLowerCase()=="reset")
         //disable em
         tempobj.disabled=true;
     }
   }
}


function add_validation_idx(itemname,idx,descriptor,errstr)
{
  if(!this.formobj)
	{
	  alert("BUG: L'oggetto sulla form non e' corretto");
		return;
	}//if
	var itemobj = this.formobj[itemname][idx];

	//alert( "Elementox : " +  this.formobj[itemname][idx].value );

  if(!itemobj)
	{
	  alert("BUG: Non esiste un oggetto con nome: " + itemname + " indice : " + idx  );
		return;
	}

	add_validation_set( itemobj, descriptor, errstr );


	/*
	if(!itemobj.validationset)
	{
	  itemobj.validationset = new ValidationSet(itemobj);
	}
  itemobj.validationset.add(descriptor,errstr);
  */

}


function add_validation(itemname,descriptor,errstr)
{
  if(!this.formobj)
	{
	  alert("BUG: L'oggetto sulla form non e' corretto");
		return;
	}//if
	var itemobj = this.formobj[itemname];

  if(!itemobj)
	{
	  alert("BUG: Non esiste un oggetto con nome: "+itemname);
		return;
	}


  if( this.formobj[itemname].length )
  {
    for( i=0; i < this.formobj[itemname].length; i++ )
      add_validation_set( this.formobj[itemname][i], descriptor, errstr );
  }
  else
  {
    add_validation_set( itemobj, descriptor, errstr );
  }

	/*
	if(!itemobj.validationset)
	{
	  itemobj.validationset = new ValidationSet(itemobj);
	}
  itemobj.validationset.add(descriptor,errstr);
  */

}

function add_validation_set( itemobj, descriptor, errstr )
{
  if(!itemobj.validationset)
	{
	  itemobj.validationset = new ValidationSet(itemobj);
	}
  itemobj.validationset.add(descriptor,errstr);

}




function ValidationDesc(inputitem,desc,error)
{
  this.desc=desc;
	this.error=error;
	this.itemobj = inputitem;
	this.validate=vdesc_validate;
}
function vdesc_validate()
{
	if(!V2validateData(this.desc,this.itemobj,this.error))
	{
		try 
		{
			this.itemobj.focus();
			return false;
               
    	} 
     	catch (err){ 
			return false;
     	} 
	}

 return true;
}


function ValidationSet(inputitem)
{
  this.vSet     = new Array();
	this.add      = add_validationdesc;
	this.validate = vset_validate;
	this.itemobj  = inputitem;
}

function add_validationdesc(desc,error)
{
  this.vSet[this.vSet.length]=
	  new ValidationDesc(this.itemobj,desc,error);
}

function vset_validate()
{
   for(var itr=0;itr<this.vSet.length;itr++)
	 {
	   if(!this.vSet[itr].validate())
		 {
		   return false;
		 }
	 }
	 return true;
}

//---------------------------------EMail Check ------------------------------------

/*  checks the validity of an email address entered
*   returns true or false
*
*/

function validateEmailv2(email)
{
// a very simple email validation checking.
// you can add more complex email checking if it helps
    var splitted = email.match("^(.+)@(.+)$");
    if(splitted == null) return false;
    if(splitted[1] != null )
    {
      var regexp_user=/^\"?[\w-_\.]*\"?$/;
      if(splitted[1].match(regexp_user) == null) return false;
    }
    if(splitted[2] != null)
    {
      var regexp_domain=/^[\w-\.]*\.[A-Za-z]{2,4}$/;
      if(splitted[2].match(regexp_domain) == null)
      {
	    var regexp_ip =/^\[\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}\]$/;
	    if(splitted[2].match(regexp_ip) == null) return false;
      }// if
      return true;
    }
return false;
}

/*
* function V2validateData
* Checks each field in a form
*/
function V2validateData(strValidateStr,objValue,strError)
{
    var epos = strValidateStr.search("=");
    var  command  = "";
    var  cmdvalue = "";
    if(epos >= 0)
    {
     command  = strValidateStr.substring(0,epos);
     cmdvalue = strValidateStr.substr(epos+1);
    }
    else
    {
     command = strValidateStr;
    }

	if (command=="req" || command=="required")
  {
    if(eval(objValue.value.length) == 0 || objValue.value == '-')
    {
      if(!strError || strError.length ==0)
      {
        strError = "Il Campo "+ objValue.title +" e' Obbligatorio";
      }//if
      alert(strError);
      return false;
    }//if

  }
  else //case required
	{
		if(eval(objValue.value.length) > 0)
	    switch(command)
	    {
	      case "maxlength":
	      case "maxlen":
	      {
	        if(eval(objValue.value.length) >  eval(cmdvalue))
	        {
	          if(!strError || strError.length ==0)
	          {
	            strError = objValue.title + ": "+cmdvalue+" e' la massima lunghezza consentita ";
	          }//if
	               //alert(strError + "\n[Lunghezza Attuale = " + objValue.value.length + " ]");
            alert(strError);
	          return false;
	        }//if
	        break;
	       }//case maxlen
	       case "minlength":
	       case "minlen":
	       {
           if(eval(objValue.value.length) <  eval(cmdvalue))
           {
             if(!strError || strError.length ==0)
             {
               strError = objValue.title + ": " + cmdvalue + " e' la minima lunghezza consentita  ";
             }//if
             //alert(strError + "\n[Lunghezza Attuale = " + objValue.value.length + " ]");
             alert(strError);
             return false;
           }//if
           break;
          }//case minlen
	        case "alnum":
	        case "alphanumeric":
	        {
	          var charpos = objValue.value.search("[^A-Za-z0-9' ]");
	          if(objValue.value.length > 0 &&  charpos >= 0)
	          {
              if(!strError || strError.length ==0)
              {
                strError = objValue.title+": Sono permessi solo caratteri alfanumerici ";
              }//if
               //alert(strError + "\n [Posizione carattere errato : " + eval(charpos+1)+"]");
              alert(strError);
              return false;
            }//if
            break;
	         }//case alphanumeric
	        case "num":
	        case "numeric":
	           {
	              var charpos = objValue.value.search("[^0-9]");
	              if(objValue.value.length > 0 &&  charpos >= 0)
	              {
	                if(!strError || strError.length ==0)
	                {
	                  strError = "Il Campo "+objValue.title+" e' numerico";
	                }//if
	                //alert(strError + "\n [Posizione carattere errato : " + eval(charpos+1)+"]");
                  alert(strError);
	                return false;
	              }//if
	              break;
	           }//numeric
	        case "alphabetic":
	        case "alpha":
	           {
	              var charpos = objValue.value.search("[^A-Za-z.' ]");
	              if(objValue.value.length > 0 &&  charpos >= 0)
	              {
	                  if(!strError || strError.length ==0)
	                {
	                  strError = objValue.title+": Sono permessi solo caratteri alfabetici";
	                }//if
	                //alert(strError + "\n [Posizione carattere errato : " + eval(charpos+1)+"]");
                  alert(strError);
	                return false;
	              }//if
	              break;
	           }//alpha
			case "alnumhyphen":
				{
	              var charpos = objValue.value.search("[^A-Za-z0-9\-_]");
	              if(objValue.value.length > 0 &&  charpos >= 0)
	              {
	                  if(!strError || strError.length ==0)
	                {
	                  strError = objValue.title+": I Caratteri ammessi sono : A-Z,a-z,0-9,- and _";
	                }//if
	                //alert(strError + "\n [Posizione carattere errato : " + eval(charpos+1)+"]");
                  alert(strError);
	                return false;
	              }//if
				break;
				}
	        case "email":
	          {
	               if(!validateEmailv2(objValue.value))
	               {
	                 if(!strError || strError.length ==0)
	                 {
	                    strError = objValue.title+": Inserire un indirizzo di email valido ";
	                 }//if
	                 alert(strError);
	                 return false;
	               }//if
	           break;
	          }//case email
	        case "lt":
	        case "lessthan":
	         {
	            if(isNaN(objValue.value))
	            {
	              alert(objValue.title+": Sono ammessi solo numeri ");
	              return false;
	            }//if
	            if(eval(objValue.value) >  eval(cmdvalue))
	            {
	              if(!strError || strError.length ==0)
	              {
	                strError = objValue.title + ": Il valore dovrebbe essere minore di "+ cmdvalue;
	              }//if
	              alert(strError);
	              return false;
	             }//if
	            break;
	         }//case lessthan
	        case "gt":
	        case "greaterthan":
	         {
	            if(isNaN(objValue.value))
	            {
	              alert(objValue.title+": Sono ammessi solo numeri  ");
	              return false;
	            }//if
	             if(eval(objValue.value) <  eval(cmdvalue))
	             {
	               if(!strError || strError.length ==0)
	               {
	                 strError = objValue.title + ": Il valore dovrebbe essere maggiore di "+ cmdvalue;
	               }//if
	               alert(strError);
	               return false;
	             }//if
	            break;
	         }//case greaterthan
	        case "regexp":
	         {
	            if(!objValue.value.match(cmdvalue))
	            {
	              if(!strError || strError.length ==0)
	              {
	                strError = objValue.title+": Carattere non valido";
	              }//if
	              alert(strError);
	              return false;
	            }//if
	           break;
	         }//case regexp
	        case "dontselect":
	         {
	            if(objValue.selectedIndex == null)
	            {
	              alert("BUG: Chiesto un valore obbligatorio su un oggetto che non e' una tendina");
	              return false;
	            }
	            if(objValue.selectedIndex == eval(cmdvalue))
	            {
	             if(!strError || strError.length ==0)
	              {
	              strError = objValue.title+": Selezionare un valore ";
	              }//if
	              alert(strError);
	              return false;
	             }
	             break;
	         }//case dontselect
	    }//switch
	}
    return true;
}


// Funzione per TRIM della stringa a destra e sinistra
function trimStringa(stringa)
{    
	while (stringa.substring(0,1) == ' ')
  	{        
  		stringa = stringa.substring(1, stringa.length);    
  	}    
  	while (stringa.substring(stringa.length-1, stringa.length) == ' ')
  	{        
  		stringa = stringa.substring(0,stringa.length-1);    
  	}    
  	return stringa;
}
