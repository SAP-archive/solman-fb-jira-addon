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
                spGetSolmanProjectMappings(e.val);
                spGetSolmanIssueTypeMappings(e.val);
                spGetSolmanIssueStatusMappings(e.val);
                spGetSolmanPriorityMappings(e.val);
        });

        function repoFormatResult(result) {
            return '<option value="' + result.id + '">' + result.solmanconn.customerDescription + ',' + result.solmanconn.customerGuid + '</option>';
        }

        function repoFormatSelection(result) {
            return '<option value="' + result.id + '">' + result.solmanconn.customerDescription + ',' + result.solmanconn.customerGuid + '</option>';
        }
}

function spGetJiraProjects() {
     AJS.$("#jira-projects-id").auiSelect2({
            ajax: {
                url: AJS.params.baseURL + '/plugins/servlet/getJiraProjects',
                dataType: 'json',

                results : function(data, page) {
                var myResults = [];
                AJS.$.each(data, function(index, item)
                {
                    myResults.push({
                        'id': item.jiraProjectKey,
                        'jiraproject': item
                    });
                });
                return {
                  results : myResults
                };
             },
                cache:true
                },
                escapeMarkup: function(markup) {return markup;},
                formatResult : repoFormatResult,
                formatSelection : repoFormatSelection,
                width : '100%'
            });


            function repoFormatResult(result) {
                return '<option value="' + result.jiraProjectKey + '">' + result.jiraproject.jiraProjectKey + '</option>';
            }

            function repoFormatSelection(result) {
                return '<option value="' + result.jiraproject.jiraProjectKey + '">' + result.jiraproject.jiraProjectKey + '</option>';
            }
}

function spGetSolmanProjectMappings(customerGuid) {
     AJS.$('#projectmapping-table-id tbody').empty();

    AJS.$('#projectmapping-table-id').remove();
    AJS.$('#projectmapping-table-id-container').append(
        '<table id="projectmapping-table-id" class="aui aui-table-sortable">' +
        '<thead>' +
            '<tr>' +
                '<th class="aui-table-column-issue-key">SolMan Project ID</th>' +
                '<th class="aui-table-column-issue-key">Jira Project Key</th>' +
            '</tr>' +
        '</thead>' +
        '<tbody>' +
        '</tbody>' +
        '</table>');


    AJS.$.ajax({
                url: AJS.params.baseURL + '/plugins/servlet/getProjectMapping?solManCustGuiD=' + customerGuid,
                type: 'post',
                data: 'json',
                success: function (data) {
                    AJS.$.each(data, function (i, s) {
                        AJS.$('#projectmapping-table-id tbody').append('<tr><td headers="solmanProjectID">' + s.solmanProjectID + '</td>' +
                        '<td style="word-break:break-all;" headers="jiraProjectID">' + s.jiraProjectID + '</td>' +
                        '<td headers="deleteInfo"><button class="aui-button aui-button-primary" id="' + s.id + '" onclick="spDeleteSolmanProjectMapping(this);" id="deleteInfo">Delete</button></td></tr>');
                    });
                    AJS.tablessortable.setTableSortable(AJS.$("#projectmapping-table-id"));
                },
                error: function (err) {
                   AJS.$('#solman-projectmapping-id tbody').replaceWith('<p>No data to display.</p>');
                }
          });
}

function spSaveSolmanProjectMapping() {

          var options = {
              solManCustGuiD : AJS.$("#solman-connections-id").val(),
              solmanProjectID : AJS.$("#solmanProjectID").val(),
              jiraProjectID : AJS.$("#jira-projects-id").val()
          }

          var errorMessage = '';

          if (options.solManCustGuiD == '') {
              errorMessage += 'SolMan Customer GUID must be selected.<br>';
          }

          if (options.solmanProjectID == '') {
              errorMessage += 'SolMan Project ID should not be empty.<br>';
          }

          if (options.jiraProjectID == '') {
                errorMessage += 'Jira Project ID should not be empty.<br>';
          }

          if (errorMessage != '') {
               AJS.$('aui-message-bar').append('');
               AJS.messages.error({
                  title: 'Incorrect Solution Manager Project Mapping Information',
                  body: '<p>' + errorMessage + '</p>',
                  duration : 1,
                  fadeout:true
               });
               return;
          }
          var url = AJS.params.baseURL + '/plugins/servlet/addProjectMapping?projectMapping='+ JSON.stringify(options);

          AJS.$.ajax({
              type: "POST",
              url: url,
              success: function(data)
              {
                  spGetSolmanProjectMappings(options.solManCustGuiD);
              },
          });
}

function spDeleteSolmanProjectMapping(element) {
           var options = {
                      solManCustGuiD : AJS.$("#solman-connections-id").val(),
                      id : AJS.$(element).attr("id")
           }
           var url = AJS.params.baseURL + '/plugins/servlet/deleteProjectMapping?projectMapping='+ JSON.stringify(options);

           AJS.$.ajax({
                 type: "POST",
                 url: url,
                 success: function(data)
                 {
                   spGetSolmanProjectMappings(options.solManCustGuiD);
                 },
           });
}


function spGetSolmanIssueStatusMappings(customerGuid) {
     AJS.$('#issuestatusmapping-table-id tbody').empty();

    AJS.$('#issuestatusmapping-table-id').remove();
    AJS.$('#issuestatusmapping-table-id-container').append(
        '<table id="issuestatusmapping-table-id" class="aui aui-table-sortable">' +
        '<thead>' +
            '<tr>' +
                '<th class="aui-table-column-issue-key">SolMan Process Type</th>'+
                '<th class="aui-table-column-issue-key">SolMan Status</th>'+
                '<th class="aui-table-column-issue-key">Jira Transition</th>'+
            '</tr>' +
        '</thead>' +
        '<tbody>' +
        '</tbody>' +
        '</table>');

     AJS.$.ajax({
                url: AJS.params.baseURL + '/plugins/servlet/getIssueStatusMappings?solManCustGuiD=' + customerGuid,
                type: 'post',
                data: 'json',
                success: function (data) {
                    AJS.$.each(data, function (i, s) {
                        AJS.$('#issuestatusmapping-table-id tbody').append('<tr><td headers="solmanProcessType">' + s.solmanProcessType + '</td>' +
                        '<td style="word-break:break-all;" headers="solmanStatus">' + s.solmanStatus + '</td>' +
                        '<td style="word-break:break-all;" headers="jiraTransition">' + s.jiraTransition + '</td>' +
                        '<td headers="deleteInfo"><button class="aui-button aui-button-primary" id="' + s.id + '" onclick="spDeleteSolmanIssueStatusMapping(this);" id="deleteInfo">Delete</button></td></tr>');
                    });
                    AJS.tablessortable.setTableSortable(AJS.$("#issuestatusmapping-table-id"));
                },
                error: function (err) {
                   AJS.$('#issuestatusmapping-table-id tbody').replaceWith('<p>No data to display.</p>');
                }
          });
}

function spSaveSolmanIssueStatusMapping() {
         var options = {
                  solManCustGuiD : AJS.$("#solman-connections-id").val(),
                  solmanProcessType : AJS.$("#issueinboundstatus-id :input[name=solmanProcessType]").val(),
                  solmanStatus : AJS.$("#solmanStatus").val(),
                  jiraTransition : AJS.$("#jiraTransition").val()
         }

         var errorMessage = '';

         if (options.solManCustGuiD == '') {
             errorMessage += 'SolMan Customer GUID must be selected.<br>';
         }

         if (options.solmanProcessType == '') {
             errorMessage += 'SolMan Process Type must be selected.<br>';
         }

         if (options.solmanStatus == '') {
             errorMessage += 'SolMan Status should not be empty.<br>';
         }

         if (options.jiraTransition == '') {
             errorMessage += 'Jira Transition should not be empty.<br>';
         }

         if (errorMessage != '') {
                        AJS.$('aui-message-bar').append('');
                        AJS.messages.error({
                           title: 'Incorrect Solution Manager Issue Status Mapping Information',
                           body: '<p>' + errorMessage + '</p>',
                           duration : 1,
                           fadeout:true
                        });
                        return;
                   }
                   var url = AJS.params.baseURL + '/plugins/servlet/addIssueStatusMapping?issueMapping='+ JSON.stringify(options);

                   AJS.$.ajax({
                       type: "POST",
                       url: url,
                       success: function(data)
                       {
                           spGetSolmanIssueStatusMappings(options.solManCustGuiD);
                       },
                   });
}

function spDeleteSolmanIssueStatusMapping(element) {
           var options = {
                      solManCustGuiD : AJS.$("#solman-connections-id").val(),
                      id : AJS.$(element).attr("id")
           }
           var url = AJS.params.baseURL + '/plugins/servlet/deleteIssueStatusMapping?issueMapping='+ JSON.stringify(options);

           AJS.$.ajax({
                 type: "POST",
                 url: url,
                 success: function(data)
                 {
                   spGetSolmanIssueStatusMappings(options.solManCustGuiD);
                 },
           });
}


//********************************************************************************
// Issue type mapping part
//********************************************************************************

function spGetJiraIssueTypes() {
    AJS.$("#jira-issue-type").auiSelect2({
       ajax: {
                       url: AJS.params.baseURL + '/plugins/servlet/getJiraIssueTypes',
                       dataType: 'json',

                       results : function(data, page) {
                       var myResults = [];
                       AJS.$.each(data, function(index, item)
                       {
                           myResults.push({
                               'id': item.issueTypeId,
                               'issueType': item
                           });
                       });
                       return {
                         results : myResults
                       };
                    },
                       cache:true
                       },
                       escapeMarkup: function(markup) {return markup;},
                       formatResult : repoFormatResult,
                       formatSelection : repoFormatSelection,
                       width : '100%'
                   });


                   function repoFormatResult(result) {
                       return '<option value="' + result.id + '">' + result.issueType.issueType + '</option>';
                   }

                   function repoFormatSelection(result) {
                       return '<option value="' + result.id + '">' + result.issueType.issueType + '</option>';
                   }
}

function spGetSolmanIssueTypeMappings(customerGuid) {
    AJS.$('#issuetypemapping-table-id tbody').empty();

    AJS.$('#issuetypemapping-table-id').remove();
    AJS.$('#issuetypemapping-table-id-container').append(
        '<table id="issuetypemapping-table-id" class="aui aui-table-sortable">' +
        '<thead>' +
            '<tr>' +
                '<th class="aui-table-column-issue-key">SolMan Process Type</th>' +
                '<th class="aui-table-column-issue-key">Jira Issue Type</th>' +
            '</tr>' +
        '</thead>' +
        '<tbody>' +
        '</tbody>' +
        '</table>');

    AJS.$.ajax({
        url: AJS.params.baseURL + '/plugins/servlet/getIssueTypeMapping?solManCustGuiD=' + customerGuid,
        type: 'post',
        data: 'json',
        success: function (data) {
            AJS.$.each(data, function (i, s) {
                AJS.$('#issuetypemapping-table-id tbody').append('<tr><td headers="solmanProcessType">' + s.solmanProcessType + '</td>' +
                    '<td style="word-break:break-all;" headers="jiraIssueType">' + s.jiraIssueTypeName + '</td>' +
                    '<td headers="deleteInfo"><button class="aui-button aui-button-primary" id="' + s.solmanProcessType + '" onclick="spDeleteSolmanIssueTypeMapping(this);" id="deleteInfo">Delete</button></td></tr>');
            });
            AJS.tablessortable.setTableSortable(AJS.$("#issuetypemapping-table-id"));
        },
        error: function (err) {
            AJS.$('#solman-issuetypemapping-id tbody').replaceWith('<p>No data to display.</p>');
        }
    });
}

function spSaveSolmanIssueTypeMapping() {

    var options = {
        solManCustGuiD : AJS.$("#solman-connections-id").val(),
        solmanProcessType : AJS.$("#solmanProcessType").val(),
        jiraIssueType : AJS.$("#jira-issue-type").val()
    };

    var errorMessage = '';

    if (options.solManCustGuiD == '') {
        errorMessage += 'SolMan Customer GUID must be selected.<br>';
    }

    if (options.solmanProcessType == '') {
        errorMessage += 'SolMan Process type should not be empty.<br>';
    }

    if (options.jiraIssueType == '') {
        errorMessage += 'Jira Issue Type should not be empty.<br>';
    }

    if (errorMessage != '') {
        AJS.$('aui-message-bar').append('');
        AJS.messages.error({
            title: 'Incorrect Solution Manager Issue Type Mapping Information',
            body: '<p>' + errorMessage + '</p>',
            duration : 1,
            fadeout:true
        });
        return;
    }
    var url = AJS.params.baseURL + '/plugins/servlet/addIssueTypeMapping?'+
        'solManCustGuiD=' + options.solManCustGuiD +
        '&solManProcessType=' + options.solmanProcessType +
        '&jiraIssueType=' + options.jiraIssueType;

    AJS.$.ajax({
        type: "POST",
        url: url,
        success: function(data)
        {
            spGetSolmanIssueTypeMappings(options.solManCustGuiD);
        }
    });
}

//********************************************************************************
// Issue priority mapping part
//********************************************************************************
function spGetJiraPriorities() {
     AJS.$("#jira-priorities-id").auiSelect2({
                ajax: {
                    url: AJS.params.baseURL + '/plugins/servlet/getJiraPriorities',
                    dataType: 'json',

                    results : function(data, page) {
                    var myResults = [];
                    AJS.$.each(data, function(index, item)
                    {
                        myResults.push({
                            'id': item.id,
                            'priority': item
                        });
                    });
                    return {
                      results : myResults
                    };
                 },
                    cache:true
                    },
                    escapeMarkup: function(markup) {return markup;},
                    formatResult : repoFormatResult,
                    formatSelection : repoFormatSelection,
                    width : '100%'
                });


                function repoFormatResult(result) {
                    return '<option value="' + result.id + '">' + result.priority.priority + '</option>';
                }

                function repoFormatSelection(result) {
                    return '<option value="' + result.id + '">' + result.priority.priority + '</option>';
                }
}

function spSaveSolmanPriorityMapping() {

   var options = {
        solManCustGuiD : AJS.$("#solman-connections-id").val(),
        solmanPriority : AJS.$("#solmanPriority").val(),
        jiraPriority : AJS.$("#jira-priorities-id").val()
    };

   var url = AJS.params.baseURL + '/plugins/servlet/addPriorityMapping?solManCustGuiD=' + options.solManCustGuiD + '&solmanPriority=' + options.solmanPriority +
      '&jiraPriority=' + options.jiraPriority;

   AJS.$.ajax({
             type: "POST",
             url: url,
             success: function(data)
             {
               spGetSolmanPriorityMappings(options.solManCustGuiD);
             },
   });
}

function spGetSolmanPriorityMappings(customerGuid) {

  AJS.$('#prioritymappings-table-id tbody').empty();

  AJS.$('#prioritymappings-table-id').remove();
  AJS.$('#prioritymappings-table-id-container').append(
        '<table id="prioritymappings-table-id" class="aui aui-table-sortable">' +
           '<thead>' +
           '<tr>' +
                             '<th class="aui-table-column-issue-key">SolMan Priority</th>' +
                             '<th class="aui-table-column-issue-key">Jira Priority</th>' +
                         '</tr>' +
           '</thead>' +
           '<tbody>' +
           '</tbody>' +
           '</table>');

   AJS.$.ajax({
                  url: AJS.params.baseURL + '/plugins/servlet/getPriorityMappings?solManCustGuiD=' + customerGuid,
                  type: 'post',
                  data: 'json',
                  success: function (data) {
                      AJS.$.each(data, function (i, s) {
                          AJS.$('#prioritymappings-table-id tbody').append('<tr><td headers="solmanPriority">' + s.solmanPriority + '</td>' +
                          '<td style="word-break:break-all;" headers="jiraPriority">' + s.jiraPriority + '</td>' +
                          '<td headers="deleteInfo"><button class="aui-button aui-button-primary" id="' + s.id + '" onclick="spDeleteSolmanPriorityMapping(this);" id="deleteInfo">Delete</button></td></tr>');
                      });
                      AJS.tablessortable.setTableSortable(AJS.$("#prioritymappings-table-id"));
                  },
                  error: function (err) {
                     AJS.$('#prioritymappings-table-id tbody').replaceWith('<p>No data to display.</p>');
                  }
    });
}

function spDeleteSolmanPriorityMapping(element) {
    var options = {
        solManCustGuiD : AJS.$("#solman-connections-id").val(),
        id : AJS.$(element).attr("id")
    };
    var url = AJS.params.baseURL + '/plugins/servlet/deletePriorityMapping?'+
        'id=' + options.id;

    AJS.$.ajax({
        type: "POST",
        url: url,
        success: function(data)
        {
            spGetSolmanPriorityMappings(options.solManCustGuiD);
        }
    });
}


function spDeleteSolmanIssueTypeMapping(element) {
    var options = {
        solManCustGuiD : AJS.$("#solman-connections-id").val(),
        id : AJS.$(element).attr("id")
    };
    var url = AJS.params.baseURL + '/plugins/servlet/deleteIssueTypeMapping?'+
        'solManCustGuiD=' + options.solManCustGuiD +
        '&solManProcessType=' + options.id;

    AJS.$.ajax({
        type: "POST",
        url: url,
        success: function(data)
        {
            spGetSolmanIssueTypeMappings(options.solManCustGuiD);
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
    var url = AJS.params.baseURL + '/plugins/servlet/copyIssueMapping?'+
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
    spGetJiraProjects();
    spGetJiraIssueTypes();
    spGetJiraPriorities();
    setupCopyConfigurationDialog();
});