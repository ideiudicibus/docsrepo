 function MultiAjaxAutoCompleteDestinatario(element,basePath, url,documentId) {
	 $(element).select2({
             placeholder: "Ricerca in anagrafica destinatari",
             minimumInputLength: 1,
             multiple: true,
             ajax: {
            	 
                 url: url,
                 dataType: 'jsonp',
                 data: function(term, page) {

                     return {
                         q: term,
                         page_limit: 10
                     };
                 },
                 results: function(data, page) {
                     
                	 return {
                    	
                         results: data.searchresult
                     };
                 }
             },
             formatResult: formatResult,
             formatSelection: formatSelection,
             initSelection: function(element, callback) {
            	    var id=$(element).val();
            	    
            	    if (id!=="") {
            	    	
            	        $.ajax("/"+basePath+"/"+url+"/d", {
            	        dataType: 'jsonp',
            	            data: {
            	            	q: documentId
            	            },

            	        }).done(function(data) {
            	        	
            	            callback(data.searchresult);
            	        });
            	        
            	    }
            	}
         });
     };

     function formatResult(data) {
         return '<div>' + data.ragioneSociale + '</div>';
     };

     function formatSelection(data) {
         return data.ragioneSociale;
     };
