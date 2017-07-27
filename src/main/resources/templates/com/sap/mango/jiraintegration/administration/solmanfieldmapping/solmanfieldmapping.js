function spGetSolmanCustomerGuids() {
    AJS.$("#solman-connections-id").auiSelect2({
        ajax: {
            url: AJS.params.baseURL + '/plugins/servlet/spGet',
            dataType: 'json',

            results : function(data, page) {
                var myResults = [];
                AJS.$.each(data, function(index, item)
                {
                    myResults.push({
                        'id':item.customerGuid,
                        'solmanconn': item
                    });
                });
                return {
                    results : myResults
                };
            },
            cache:true
        },
        theme:'classic',
        escapeMarkup: function(markup) {return markup;},
        formatResult : repoFormatResult,
        formatSelection : repoFormatSelection,
        dropdownAutoWidth : true,
        width : '600px'
    }).on("change", function(e) {
        spGetSolmanFieldMappings(e.val);
        spGetSolmanPartnerFieldMappings(e.val);
        spGetSolmanTextFieldMappings(e.val);
        spGetSolmanAppointmentMappings(e.val);
    });

    function repoFormatResult(result) {
        return '<option value="' + result.id + '">' + result.solmanconn.customerDescription + ',' + result.solmanconn.customerGuid + '</option>';
    }

    function repoFormatSelection(result) {
        return '<option value="' + result.id + '">' + result.solmanconn.customerDescription + ',' + result.solmanconn.customerGuid + '</option>';
    }
}


function spGetSolmanFieldMappings(customerGuid) {
     AJS.$('#fieldmapping-table-id tbody').empty();

    AJS.$('#fieldmapping-table-id').remove();
    AJS.$('#fieldmapping-table-id-container').append(
        '<table id="fieldmapping-table-id" class="aui aui-table-sortable">' +
        '<thead>' +
            '<tr>' +
                '<th>SolMan Process Type</th>' +
                '<th>SolMan Field</th>' +
                '<th class="aui-table-column-issue-key">Jira Field</th>' +
            '</tr>' +
        '</thead>' +
        '<tbody>' +
        '</tbody>' +
        '</table>');

     AJS.$.ajax({
                url: AJS.params.baseURL + '/plugins/servlet/getFieldMapping?solManCustGuiD=' + customerGuid,
                type: 'post',
                data: 'json',
                success: function (data) {
                    AJS.$.each(data, function (i, s) {
                        AJS.$('#fieldmapping-table-id tbody').append('<tr><td headers="solmanProcessType">' + s.solmanProcessType + '</td>' +
                        '<td style="word-break:break-all;" headers="solmanField">' + s.solmanField  + '</td>' +
                        '<td style="word-break:break-all;" headers="jiraFielde">' + s.jiraField + '</td>' +
                        '<td headers="deleteInfo"><button class="aui-button aui-button-primary" id="' + s.id + '" onclick="spDeleteSolmanFieldMapping(this);" id="deleteInfo">Delete</button></td></tr>');
                    });
                    AJS.tablessortable.setTableSortable(AJS.$("#fieldmapping-table-id"));
                },
                error: function (err) {
                   AJS.$('#solman-fieldmapping-id tbody').replaceWith('<p>No data to display.</p>');
                }
          });
}

function spSaveSolmanFieldMapping() {
          var solManCustGuiD = AJS.$("#solman-connections-id").val();
          var options = {
              solmanProcessType : AJS.$("#solmanProcessType").val(),
              solmanField : AJS.$("#solmanField").val(),
              jiraField : AJS.$("#jira-field").val()
          };

          var errorMessage = '';

          if (solManCustGuiD == '') {
              errorMessage += 'SolMan Customer GUID must be selected.<br>';
          }

          if (options.solmanProcessType == '') {
              errorMessage += 'SolMan Process type should not be empty.<br>';
          }

            if (options.solmanField == '') {
                errorMessage += 'SolMan Field should not be empty.<br>';
            }

          if (options.jiraField == '') {
                errorMessage += 'Jira Field should not be empty.<br>';
          }

          if (errorMessage != '') {
               AJS.$('aui-message-bar').append('');
               AJS.messages.error({
                  title: 'Incorrect Solution Manager Field Mapping Information',
                  body: '<p>' + errorMessage + '</p>',
                  duration : 1,
                  fadeout:true
               });
               return;
          }
          var url = AJS.params.baseURL + '/plugins/servlet/addFieldMapping?'+
              'solManCustGuiD=' + solManCustGuiD +
              '&fieldMapping=' + JSON.stringify(options);

          AJS.$.ajax({
              type: "POST",
              url: url,
              success: function(data)
              {
                  spGetSolmanFieldMappings(solManCustGuiD);
              }
          });
}

function spDeleteSolmanFieldMapping(element) {
           var options = {
                      solManCustGuiD : AJS.$("#solman-connections-id").val(),
                      id : AJS.$(element).attr("id")
           };
           var url = AJS.params.baseURL + '/plugins/servlet/deleteFieldMapping?'+
               'id=' + options.id;

           AJS.$.ajax({
                 type: "POST",
                 url: url,
                 success: function(data)
                 {
                   spGetSolmanFieldMappings(options.solManCustGuiD);
                 }
           });
}




//********************************************************************************
// partner fields mapping part
//********************************************************************************

function spGetSolmanPartnerFieldMappings(customerGuid) {
    AJS.$('#partnerfieldmapping-table-id tbody').empty();

    AJS.$('#partnerfieldmapping-table-id').remove();
    AJS.$('#partnerfieldmapping-table-id-container').append(
        '<table id="partnerfieldmapping-table-id" class="aui aui-table-sortable">' +
        '<thead>' +
            '<tr>' +
                '<th class="aui-table-column-issue-key">SolMan Process Type</th>' +
                '<th class="aui-table-column-issue-key">SolMan Partner Function</th>' +
                '<th class="aui-table-column-issue-key">SolMan Partner Field</th>' +
                '<th class="aui-table-column-issue-key">Jira Field</th>' +
            '</tr>' +
        '</thead>' +
        '<tbody>' +
        '</tbody>' +
        '</table>');


    AJS.$.ajax({
        url: AJS.params.baseURL + '/plugins/servlet/getPartnerFieldMapping?solManCustGuiD=' + customerGuid,
        type: 'post',
        data: 'json',
        success: function (data) {
            AJS.$.each(data, function (i, s) {
                AJS.$('#partnerfieldmapping-table-id tbody').append('<tr><td headers="solmanProcessType">' + s.solmanProcessType + '</td>' +
                    '<td style="word-break:break-all;" headers="solmanField">' + s.solmanPartnerFunction  + '</td>' +
                    '<td style="word-break:break-all;" headers="solmanField">' + s.solmanField  + '</td>' +
                    '<td style="word-break:break-all;" headers="jiraFielde">' + s.jiraField + '</td>' +
                    '<td headers="deleteInfo"><button class="aui-button aui-button-primary" id="' + s.id + '" onclick="spDeleteSolmanPartnerFieldMapping(this);" id="deleteInfo">Delete</button></td></tr>');
            });
            AJS.tablessortable.setTableSortable(AJS.$("#partnerfieldmapping-table-id"));
        },
        error: function (err) {
            AJS.$('#solman-fieldmapping-id tbody').replaceWith('<p>No data to display.</p>');
        }
    });
}

function spSaveSolmanPartnerFieldMapping() {
    var solManCustGuiD = AJS.$("#solman-connections-id").val();
    var options = {
        solmanProcessType : AJS.$("#solmanProcessTypePartner").val(),
        solmanPartnerFunction : AJS.$("#solmanPartnerFunction").val(),
        solmanField : AJS.$("#solmanPartnerField").val(),
        jiraField : AJS.$("#jira-partner-field").val()
    };

    var errorMessage = '';

    if (solManCustGuiD == '') {
        errorMessage += 'SolMan Customer GUID must be selected.<br>';
    }

    if (options.solmanProcessType == '') {
        errorMessage += 'SolMan Process type should not be empty.<br>';
    }

    if (options.solmanPartnerFunction == '') {
        errorMessage += 'SolMan Partner Function should not be empty.<br>';
    }

    if (options.solmanField == '') {
        errorMessage += 'SolMan Partner Field should not be empty.<br>';
    }

    if (options.jiraField == '') {
        errorMessage += 'Jira Field should not be empty.<br>';
    }

    if (errorMessage != '') {
        AJS.$('aui-message-bar').append('');
        AJS.messages.error({
            title: 'Incorrect Solution Manager Field Mapping Information',
            body: '<p>' + errorMessage + '</p>',
            duration : 1,
            fadeout:true
        });
        return;
    }
    var url = AJS.params.baseURL + '/plugins/servlet/addPartnerFieldMapping?'+
        'solManCustGuiD=' + solManCustGuiD +
        '&partnerFieldMapping=' + JSON.stringify(options);

    AJS.$.ajax({
        type: "POST",
        url: url,
        success: function(data)
        {
            spGetSolmanPartnerFieldMappings(solManCustGuiD);
        }
    });
}

function spDeleteSolmanPartnerFieldMapping(element) {
    var options = {
        solManCustGuiD : AJS.$("#solman-connections-id").val(),
        id : AJS.$(element).attr("id")
    };
    var url = AJS.params.baseURL + '/plugins/servlet/deletePartnerFieldMapping?'+
        'id=' + options.id;

    AJS.$.ajax({
        type: "POST",
        url: url,
        success: function(data)
        {
            spGetSolmanPartnerFieldMappings(options.solManCustGuiD);
        }
    });
}


//********************************************************************************
// text field mapping part
//********************************************************************************

function spGetSolmanTextFieldMappings(customerGuid) {
    AJS.$('#textfieldmapping-table-id tbody').empty();


    AJS.$('#textfieldmapping-table-id').remove();
    AJS.$('#textfieldmapping-table-id-container').append(
        '<table id="textfieldmapping-table-id" class="aui aui-table-sortable">' +
        '<thead>' +
            '<tr>' +
                '<th class="aui-table-column-issue-key">SolMan Process Type</th>' +
                '<th class="aui-table-column-issue-key">SolMan Text Type</th>' +
                '<th class="aui-table-column-issue-key">Jira Field</th>' +
            '</tr>' +
        '</thead>' +
        '<tbody>' +
        '</tbody>' +
        '</table>');

    AJS.$.ajax({
        url: AJS.params.baseURL + '/plugins/servlet/getTextFieldMapping?solManCustGuiD=' + customerGuid,
        type: 'post',
        data: 'json',
        success: function (data) {
            AJS.$.each(data, function (i, s) {
                AJS.$('#textfieldmapping-table-id tbody').append('<tr><td headers="solmanProcessType">' + s.solmanProcessType + '</td>' +
                    '<td style="word-break:break-all;" headers="solmanTextType">' + s.solmanTextType  + '</td>' +
                    '<td style="word-break:break-all;" headers="jiraFieldText">' + s.jiraField + '</td>' +
                    '<td headers="deleteInfo"><button class="aui-button aui-button-primary" id="' + s.id + '" onclick="spDeleteSolmanTextFieldMapping(this);" id="deleteInfo">Delete</button></td></tr>');
            });
            AJS.tablessortable.setTableSortable(AJS.$("#textfieldmapping-table-id"));
        },
        error: function (err) {
            AJS.$('#solman-fieldmapping-id tbody').replaceWith('<p>No data to display.</p>');
        }
    });
}

function spSaveSolmanTextFieldMapping() {
    var solManCustGuiD = AJS.$("#solman-connections-id").val();
    var options = {
        solmanProcessType : AJS.$("#solmanProcessTypeText").val(),
        solmanTextType : AJS.$("#solmanTextType").val(),
        jiraField : AJS.$("#jira-field-text").val()
    };

    var errorMessage = '';

    if (solManCustGuiD == '') {
        errorMessage += 'SolMan Customer GUID must be selected.<br>';
    }

    if (options.solmanProcessType == '') {
        errorMessage += 'SolMan Process type should not be empty.<br>';
    }

    if (options.solmanTextType == '') {
        errorMessage += 'SolMan Text Type should not be empty.<br>';
    }

    if (options.jiraField == '') {
        errorMessage += 'Jira Field should not be empty.<br>';
    }

    if (errorMessage != '') {
        AJS.$('aui-message-bar').append('');
        AJS.messages.error({
            title: 'Incorrect Solution Manager Field Mapping Information',
            body: '<p>' + errorMessage + '</p>',
            duration : 1,
            fadeout:true
        });
        return;
    }
    var url = AJS.params.baseURL + '/plugins/servlet/addTextFieldMapping?'+
        'solManCustGuiD=' + solManCustGuiD +
        '&textFieldMapping=' + JSON.stringify(options);

    AJS.$.ajax({
        type: "POST",
        url: url,
        success: function(data)
        {
            spGetSolmanTextFieldMappings(solManCustGuiD);
        }
    });
}

function spDeleteSolmanTextFieldMapping(element) {
    var options = {
        solManCustGuiD : AJS.$("#solman-connections-id").val(),
        id : AJS.$(element).attr("id")
    };
    var url = AJS.params.baseURL + '/plugins/servlet/deleteTextFieldMapping?'+
        'id=' + options.id;

    AJS.$.ajax({
        type: "POST",
        url: url,
        success: function(data)
        {
            spGetSolmanTextFieldMappings(options.solManCustGuiD);
        }
    });
}


//********************************************************************************
// appointment mapping part
//********************************************************************************

function spGetSolmanAppointmentMappings(customerGuid) {
    AJS.$('#appointmentmapping-table-id tbody').empty();

    AJS.$('#appointmentmapping-table-id').remove();
    AJS.$('#appointmentmapping-table-id-container').append(
        '<table id="appointmentmapping-table-id" class="aui aui-table-sortable">' +
        '<thead>' +
            '<tr>' +
                '<th class="aui-table-column-issue-key">SolMan Process Type</th>' +
                '<th class="aui-table-column-issue-key">SolMan Appointment Type</th>' +
                '<th class="aui-table-column-issue-key">Jira Field</th>' +
            '</tr>' +
        '</thead>' +
        '<tbody>' +
        '</tbody>' +
        '</table>');

    AJS.$.ajax({
        url: AJS.params.baseURL + '/plugins/servlet/getAppointmentMapping?solManCustGuiD=' + customerGuid,
        type: 'post',
        data: 'json',
        success: function (data) {
            AJS.$.each(data, function (i, s) {
                AJS.$('#appointmentmapping-table-id tbody').append('<tr><td headers="solmanProcessType">' + s.solmanProcessType + '</td>' +
                    '<td style="word-break:break-all;" headers="solmanTextType">' + s.solmanAppointment  + '</td>' +
                    '<td style="word-break:break-all;" headers="jiraFieldText">' + s.jiraField + '</td>' +
                    '<td headers="deleteInfo"><button class="aui-button aui-button-primary" id="' + s.id + '" onclick="spDeleteSolmanAppointmentMapping(this);" id="deleteInfo">Delete</button></td></tr>');
            });
            AJS.tablessortable.setTableSortable(AJS.$("#appointmentmapping-table-id"));
        },
        error: function (err) {
            AJS.$('#solman-fieldmapping-id tbody').replaceWith('<p>No data to display.</p>');
        }
    });
}

function spSaveSolmanAppointmentMapping() {
    var solManCustGuiD = AJS.$("#solman-connections-id").val();
    var options = {
        solmanProcessType : AJS.$("#solmanProcessTypeAppointment").val(),
        solmanAppointment : AJS.$("#solmanAppointmentType").val(),
        jiraField : AJS.$("#jira-field-appointment").val()
    };

    var errorMessage = '';

    if (solManCustGuiD == '') {
        errorMessage += 'SolMan Customer GUID must be selected.<br>';
    }

    if (options.solmanProcessType == '') {
        errorMessage += 'SolMan Process type should not be empty.<br>';
    }

    if (options.solmanAppointment == '') {
        errorMessage += 'SolMan Appointment Type should not be empty.<br>';
    }

    if (options.jiraField == '') {
        errorMessage += 'Jira Field should not be empty.<br>';
    }

    if (errorMessage != '') {
        AJS.$('aui-message-bar').append('');
        AJS.messages.error({
            title: 'Incorrect Solution Manager Field Mapping Information',
            body: '<p>' + errorMessage + '</p>',
            duration : 1,
            fadeout:true
        });
        return;
    }
    var url = AJS.params.baseURL + '/plugins/servlet/addAppointmentMapping?'+
        'solManCustGuiD=' + solManCustGuiD +
        '&appointmentMapping=' + JSON.stringify(options);

    AJS.$.ajax({
        type: "POST",
        url: url,
        success: function(data)
        {
            spGetSolmanAppointmentMappings(solManCustGuiD);
        }
    });
}

function spDeleteSolmanAppointmentMapping(element) {
    var options = {
        solManCustGuiD : AJS.$("#solman-connections-id").val(),
        id : AJS.$(element).attr("id")
    };
    var url = AJS.params.baseURL + '/plugins/servlet/deleteAppointmentMapping?'+
        'id=' + options.id;

    AJS.$.ajax({
        type: "POST",
        url: url,
        success: function(data)
        {
            spGetSolmanAppointmentMappings(options.solManCustGuiD);
        }
    });
}

//********************************************************************************
// copy configuration part
//********************************************************************************
function showCopyDialog(overwriteflag){
    overwrite = overwriteflag;

    if (overwrite) {
        AJS.$("#overwtite_warning").show();
        AJS.$("#parameter_select").hide();
    }else{
        AJS.$("#overwtite_warning").hide();
        AJS.$("#parameter_select").show();
    }

    var cust = AJS.$("#solman-connections-id").select2('data');
    if (typeof(cust) !== 'undefined' && cust !== null ) {
        var customer = cust.solmanconn;
        if (typeof(customer) !== 'undefined' && customer !== null ) {
            AJS.$("#source-cutomer-guid").html(
                customer.customerDescription + ', ' +
                customer.customerGuid
            );
        }
    }
    AJS.dialog2("#copy-conf-dialog").show();
}

function setupCopyConfigurationDialog(){
// Shows the dialog when the "Show dialog" button is clicked
    AJS.$("#dialog-show-button").click(function() {
        showCopyDialog(false);
    });

    AJS.$("#dialog-submit-button").click(function(e) {
        spCopyConfig();
        e.preventDefault();
        AJS.dialog2("#copy-conf-dialog").hide();
    });

// Hides the dialog
    AJS.$("#dialog-close-button").click(function(e) {
        e.preventDefault();
        AJS.dialog2("#copy-conf-dialog").hide();
    });

    spGetSolmanCustomerGuidsCopy();
}
function spGetSolmanCustomerGuidsCopy() {
    AJS.$("#solman-customer-id").auiSelect2({
        ajax: {
            url: AJS.params.baseURL + '/plugins/servlet/spGet',
            dataType: 'json',

            results : function(data, page) {
                var myResults = [];
                AJS.$.each(data, function(index, item)
                {
                    myResults.push({
                        'id':item.customerGuid,
                        'solmanconn': item
                    });
                });
                return {
                    results : myResults
                };
            },
            cache:true
        },
        theme:'classic',
        escapeMarkup: function(markup) {return markup;},
        formatResult : repoFormatResult,
        formatSelection : repoFormatSelection,
        dropdownAutoWidth : true,
        width : '600px'
    }).on("change", function(e) {
    });

    function repoFormatResult(result) {
        return '<option value="' + result.id + '">' + result.solmanconn.customerDescription + ',' + result.solmanconn.customerGuid + '</option>';
    }

    function repoFormatSelection(result) {
        return '<option value="' + result.id + '">' + result.solmanconn.customerDescription + ',' + result.solmanconn.customerGuid + '</option>';
    }
}

var overwrite = false;
function spCopyConfig() {
    var solManCustGuiD = AJS.$("#solman-connections-id").val();
    var solManCustGuiDDest = AJS.$("#solman-customer-id").val();

    var errorMessage = '';

    if (solManCustGuiD == '') {
        errorMessage += 'SolMan Customer GUID must be selected.<br>';
    }
    if (solManCustGuiDDest == '') {
        errorMessage += 'SolMan Destination Customer GUID must be selected.<br>';
    }

    if (errorMessage != '') {
        AJS.$('aui-message-bar').append('');
        AJS.messages.error({
            title: 'Incorrect Solution Manager Field Mapping Information',
            body: '<p>' + errorMessage + '</p>',
            duration : 1,
            fadeout:true
        });
        return;
    }
    var url = AJS.params.baseURL + '/plugins/servlet/copyFieldMapping?'+
        'solManCustGuiD=' + solManCustGuiD +
        '&solManCustGuiDDest=' + solManCustGuiDDest +
        '&overwrite=' + overwrite
        ;

    AJS.$.ajax({
        type: "POST",
        url: url,
        success: function(data)
        {

        },
        error: function(err){
            if(err.responseText == '"Overwrite"') {
                showCopyDialog(true);
            }
        }
    });
}



AJS.$(document).ready(function () {
    spGetSolmanCustomerGuids();

    setupCopyConfigurationDialog();
});






